package ru.trendtech.services.administration;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import ru.trendtech.common.mobileexchange.model.common.CourierCalculatePriceResponse;
import ru.trendtech.common.mobileexchange.model.common.CustomException;
import ru.trendtech.common.mobileexchange.model.common.ItemLocation;
import ru.trendtech.common.mobileexchange.model.courier.*;
import ru.trendtech.common.mobileexchange.model.courier.web.*;
import ru.trendtech.domain.*;
import ru.trendtech.domain.admin.WebUser;
import ru.trendtech.domain.billing.Account;
import ru.trendtech.domain.courier.*;
import ru.trendtech.models.ModelsUtils;
import ru.trendtech.repositories.CashRepository;
import ru.trendtech.repositories.ClientRepository;
import ru.trendtech.repositories.DriverRepository;
import ru.trendtech.repositories.LocationRepository;
import ru.trendtech.repositories.billing.AccountRepository;
import ru.trendtech.repositories.courier.*;
import ru.trendtech.services.common.CommonService;
import ru.trendtech.services.courier.OrderService;
import ru.trendtech.services.notifications.node.NodeJsNotificationsService;
import ru.trendtech.services.notifications.push.App42PushNotificationService;
import ru.trendtech.services.sms.ServiceSMSNotification;
import ru.trendtech.utils.DateTimeUtils;
import ru.trendtech.utils.GeoUtils;
import ru.trendtech.utils.MoneyUtils;
import ru.trendtech.utils.QueryUtils;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

/**
 * Created by petr on 25.08.2015.
 */
@Service
@Transactional
public class AdminCurierService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminCurierService.class);
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private ItemPriceRepository itemPriceRepository;
    @Autowired
    private StoreAddressRepository storeAddressRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ActivationQueueRepository activationQueueRepository;
    @Autowired
    private NodeJsNotificationsService nodeJsNotificationsService;
    @Autowired
    private App42PushNotificationService app42PushNotificationService;
    @Autowired
    private ServiceSMSNotification serviceSMSNotification;
    @Autowired
    private CustomWindowRepository customWindowRepository;
    @Autowired
    private CustomWindowUsesRepository customWindowUsesRepository;
    @Autowired
    private OrderAddressRepository orderAddressRepository;
    @Autowired
    private CommonService commonService;
    @Autowired
    private OrderItemPriceRepository orderItemPriceRepository;
    @Autowired
    private PriceChangesRepository priceChangesRepository;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private QueueOrderRepository queueOrderRepository;
    @Autowired
    private ItemPropertyRepository itemPropertyRepository;
    @Autowired
    private CashRepository cashRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private OrderService orderService;


    public OrderCancelByOperatorResponse orderCancel(Order order, String reason){
        OrderCancelByOperatorResponse response = new OrderCancelByOperatorResponse();

        Driver driver = order.getDriver();
        if(driver != null && driver.getCurrentOrder() != null && driver.getCurrentOrder().getId().equals(order.getId())){
            DriverLocation driverLocation = locationRepository.findByDriverId(driver.getId());
            driverLocation.setOrder(null);
            locationRepository.save(driverLocation);

            driver.setCurrentOrder(null);
            driverRepository.save(driver);
        }

        order.setState(Order.State.CANCELED);
        order.setReasonCancel(reason);
        orderRepository.save(order);

        orderService.saveChangeState(order);

        nodeJsNotificationsService.courierOrderCanceledToClientAndCourier(order);
        app42PushNotificationService.cancelOrder(order);

        return response;
    }



    // корректировка суммы товара
    public UpdatePriceResponse updatePrice(long orderId, int priceItemsInFact){
        ru.trendtech.domain.courier.Order order = orderRepository.findOne(orderId);
        if(order == null){
            throw new CustomException(2, "Заказ не найден");
        }
        if(EnumSet.of(Order.State.CANCELED, Order.State.COMPLETED).contains(order.getState())){
            throw new CustomException(3, "Заказ отменен или завершен");
        }
        if(priceItemsInFact == 0){
            throw new CustomException(4, "Сумма не может быть 0");
        }
        if(order.getPriceOfItems().getAmountMinorInt() < priceItemsInFact){
            throw new CustomException(5, "Увеличение стоимости запрещено");
        }
        UpdatePriceResponse response = new UpdatePriceResponse();
        int diff = order.getPriceOfItems().getAmountMinorInt() - priceItemsInFact;

        /* отнимаем от стоимости по факту разницу между текущей стоимостью товара и новой стоимостью */
        int priceOrderInFact = order.getPriceInFact().getAmountMinorInt();
        priceOrderInFact -= diff;

        order.setPriceInFact(MoneyUtils.getMoney(priceOrderInFact / 100));
        order.setPriceOfItems(MoneyUtils.getMoney(priceItemsInFact / 100));
        orderRepository.save(order);

        updateCashFlow(order, diff, 28);

        nodeJsNotificationsService.courierOrderSumChanged(order);

        return response;
    }





    public void updateCashFlow(Order order, int sumAmount, int operation){
        DriverCashFlow priceByPercentInsurance = new DriverCashFlow();
        priceByPercentInsurance.setSum(sumAmount);
        priceByPercentInsurance.setDate_operation(DateTimeUtils.nowNovosib_GMT6());
        priceByPercentInsurance.setDriver(order.getDriver());
        priceByPercentInsurance.setOrder(order);
        priceByPercentInsurance.setOperation(operation);
        cashRepository.save(priceByPercentInsurance);
    }




    public UpdateOrderResponse updateOrder(OrderInfo orderInfo){
        ru.trendtech.domain.courier.Order order = orderRepository.findOne(orderInfo.getId());
        if(order == null){
            throw new CustomException(2, "Заказ не найден");
        }
        if(EnumSet.of(Order.State.CANCELED, Order.State.COMPLETED).contains(order.getState())){
            throw new CustomException(3, "Заказ отменен или завершен");
        }

        UpdateOrderResponse response = new UpdateOrderResponse();

        // адреса откуда, куда привезти
        List<OrderAddressInfo> orderAddressInfos = orderInfo.getTargetAddressesInfo();

        if (!CollectionUtils.isEmpty(orderAddressInfos)) {

            Set<OrderAddress> targetAddresses = order.getTargetAddresses();
            if (!CollectionUtils.isEmpty(targetAddresses)){
                order.getTargetAddresses().clear();
                orderRepository.save(order);
            }

            int orderNumber = 1;
            for (OrderAddressInfo info : orderAddressInfos) {
                OrderAddress orderAddress = null;
                if(info.getId() != 0){
                    orderAddress = orderAddressRepository.findOne(info.getId());
                        if(orderAddress == null){
                           throw new CustomException(2, String.format("Целевой адрес id=% не найден", info.getId()));
                        }
                }
                orderAddress = ModelsUtils.fromModel(info, order, orderAddress, orderNumber);
                orderAddress = orderAddressRepository.save(orderAddress);
                order.getTargetAddresses().add(orderAddress);
                orderNumber++;
            }
        }

        order.setTimeOfFinishingExpected(DateTimeUtils.toDateTime(orderInfo.getTimeOfFinishingExpected()));
        order.setOrderType(OrderType.getByValue(orderInfo.getOrderType()));
        order = orderRepository.save(order);

        /* todo: нужно ли здесь перезаписывать % ? */
        int increasePercent = Integer.parseInt(commonService.getPropertyValue("increase_percent"));
        order.setIncreasePercent(increasePercent);

        // список выбранных пользователем покупок
        List<OrderItemPriceInfo> orderItemPriceInfos = orderInfo.getOrderItemPriceInfos();
        if (!CollectionUtils.isEmpty(orderItemPriceInfos)) {

            Set<OrderItemPrice> orderItemPrices = order.getOrderItemPrices();
            if (!CollectionUtils.isEmpty(orderItemPrices)){
                order.getOrderItemPrices().clear();
                orderRepository.save(order);
            }


            // отсюда мы должны взять адреса магазинов доя водителя и запихнуть их в storeAddress
            int orderNumber = 1;
            for(OrderItemPriceInfo orderItemPriceInfo: orderItemPriceInfos){
                OrderItemPrice orderItemPrice = null;
                if(orderItemPriceInfo.getId() != 0){
                    orderItemPrice = orderItemPriceRepository.findOne(orderItemPriceInfo.getId());
                    if(orderItemPrice == null){
                        throw new CustomException(2, String.format("OrderItemPrice с id=% не найден", orderItemPriceInfo.getId()));
                    }
                }

                ItemPrice itemPrice = getItemPrice(orderItemPriceInfo.getItemPriceInfo());
                itemPriceRepository.save(itemPrice);

                orderItemPrice = ModelsUtils.fromModel(orderItemPriceInfo, itemPrice, order, orderItemPrice, orderNumber);

                orderItemPrice.setPriceOnDayOfOrder(itemPrice.getItem().getDefaultItemPrice());
                orderItemPriceRepository.save(orderItemPrice);
                order.getOrderItemPrices().add(orderItemPrice);
                orderNumber++;
            }
        }

        order.setDistanceExpected(commonService.calculateDistanceWithTimeDurationGeneral(orderInfo).getDistance());
        order = orderRepository.save(order);

        /* обновляем orderInfo с учетом изменений выше */
        orderInfo = ModelsUtils.toModel(order, 0);
        int previousSum = order.getPriceExpected().getAmountMinorInt();

        CourierCalculatePriceResponse calculatePriceResponse = commonService.calculatePrice(orderInfo);

        List<PriceChanges> priceChanges = priceChangesRepository.findByOrder(order);
        if(!CollectionUtils.isEmpty(priceChanges)) {
            int priceChangeSum = 0;
            for(PriceChanges price: priceChanges){
                if(price.getChangeType().equals(PriceChanges.ChangeType.DOWN)){
                    priceChangeSum = priceChangeSum - price.getChangeAmount();
                } else if(price.getChangeType().equals(PriceChanges.ChangeType.UP)){
                    priceChangeSum = priceChangeSum + price.getChangeAmount();
                }
            }

            LOGGER.info("priceChangeSum: " + priceChangeSum + " calculatePriceResponse.getCommon(): "+calculatePriceResponse.getCommonPrice());
            if(priceChangeSum < calculatePriceResponse.getCommonPrice()) {
                // сумма заказа увеличилась
                LOGGER.info("сумма заказа увеличилась"+ (priceChangeSum - calculatePriceResponse.getCommonPrice()));
                order.setState(Order.State.WAIT_TO_CONFIRM);
                orderRepository.save(order);

                orderService.saveChangeState(order);
            }
        }

        /* сохраняем измененную сумму */
        int diff = calculatePriceResponse.getCommonPrice() - previousSum;
        commonService.buildPriceChange(order, null, diff);

        order.setPriceOfInsurance(MoneyUtils.getMoney(calculatePriceResponse.getPriceOfInsurance() / 100));
        order.setPriceDelivery(MoneyUtils.getMoney(calculatePriceResponse.getPriceDelivery() / 100));
        order.setPriceExpected(MoneyUtils.getMoney(calculatePriceResponse.getCommonPrice() / 100));
        order.setPriceInFact(MoneyUtils.getMoney(calculatePriceResponse.getCommonPrice() / 100));

        order.setPriceOfItemsExpected(MoneyUtils.getMoney(calculatePriceResponse.getPriceItems() / 100));
        order.setPriceOfItems(MoneyUtils.getMoney(calculatePriceResponse.getPriceItems() / 100));

        order.setPriceOfPercentInsurance(MoneyUtils.getMoney(calculatePriceResponse.getPriceByPercentInsurance() / 100));

        orderRepository.save(order);
        response.setOrderInfo(ModelsUtils.toModel(order, 0));
        return response;
    }





        /*
        boolean sumIsUp = false;
        if(previousSum > 0 && previousSum < calculatePriceResponse.getPrice()){
           int diff = calculatePriceResponse.getPrice() - previousSum;
              //todo: здесь нужно будет не плодить новый PaymentOrder
              //OrderPayment orderPayment = commonService.createOrderPayment(OrderPayment.PaymentState.REQUIRED_TO_HOLD, order, null, diff);
              //orderPaymentRepository.save(orderPayment);

           // изменилась сумма заказа в большу сторону, значит устанавливаем стейт повторного подтверждения заказа для клиента
           order.setState(Order.State.WAIT_TO_CONFIRM);
           orderRepository.save(order);
           sumIsUp = true;
        }
        */




    public StartOrderProcessingResponse startOrderProcessing(ru.trendtech.domain.courier.Order order, WebUser webUser){
        StartOrderProcessingResponse response = new StartOrderProcessingResponse();
        order.setWebUser(webUser);
        order.setTimeOfStartingOrderProcessing(DateTimeUtils.nowNovosib_GMT6());
        order.setState(Order.State.IN_PROGRESS_BY_OPERATOR);
        orderRepository.save(order);

        orderService.saveChangeState(order);
        return response;
    }





    public void updateDriverLocation(Driver driver, Order order){
        DriverLocation driverFromLocation = locationRepository.findByDriverId(driver.getId());
        driverFromLocation.setOrder(order);
        locationRepository.save(driverFromLocation);
    }



    public OrderTransferResponse orderTransfer(long orderId, long toDriverId, long webUserId) {
        OrderTransferResponse response =new OrderTransferResponse();

        Order order = orderRepository.findOne(orderId);
        if(order == null){
            throw new CustomException(2,"Заказ не найден");
        }

        Driver driverTo = driverRepository.findOne(toDriverId);
        if(driverTo == null) {
            throw new CustomException(4, "Не указан водитель для назначения заказа");
        }

        Driver driverFrom =  order.getDriver();

        boolean booked = order.isBooked();


        if(driverTo.getAdministrativeStatus().equals(Driver.AdministrativeStatus.BLOCKED)){
            throw new CustomException(5, "Курьер на которого вы пытаетесь назначить заказ - заблокирован!");
        }
        if(driverTo.getState().equals(Driver.State.OFFLINE)){
            throw new CustomException(6, "Курьер на которого вы пытаетесь назначить  - OFFLINE!");
        }
        if(driverTo.getState().equals(Driver.State.BUSY)){
            throw new CustomException(7, "Курьер занят");
        }
        if(driverTo.getCurrentOrder() != null){
            throw new CustomException(8, "У курьера есть незавершенный заказ");
        }

        // todo: еще при каких статусах мы должны послать???
        if(order.getState().equals(Order.State.NEW) || order.getState().equals(Order.State.CANCELED) || order.getState().equals(Order.State.COMPLETED) || order.getState().equals(Order.State.IN_PROGRESS_BY_COURIER) || (order.getState().equals(Order.State.WAIT_TO_CONFIRM) && order.getDriver() == null )){
              throw new CustomException(9, "Заказ со статусом: "+order.getState().toString()+" не соответствует операции передачи другому водителю");
        }


        QueueOrder queueOrder = queueOrderRepository.findByOrder(order);
        if(queueOrder == null){
            queueOrder = new QueueOrder();
        }
            queueOrder.setDriver(driverTo);
            queueOrder.setTimeOfAssigning(DateTimeUtils.nowNovosib_GMT6());
            queueOrder.setOrder(order);
            queueOrderRepository.save(queueOrder);

        long driverFromId = 0;

        if(driverFrom != null){
             driverFromId = driverFrom.getId();
             if(driverFrom.getCurrentOrder() != null && driverFrom.getCurrentOrder().getId() == orderId){
                 /* заказ, который снимаем с водителя был текущим у driverFrom, следовательно, текущим он должен стать и у driverTo */
                 updateDriverLocation(driverFrom, null);
                 driverFrom.setCurrentOrder(null);
                 driverRepository.save(driverFrom);

                 /* send sms to driver from */
                 serviceSMSNotification.missionTransfer(driverFrom.getPhone(), "");

                 updateDriverLocation(driverTo, order);
                 driverTo.setCurrentOrder(order);
                 driverRepository.save(driverTo);
             }
        }

        order.setDriver(driverTo);
        orderRepository.save(order);

        nodeJsNotificationsService.transferMissionToDriver(orderId, driverFromId, toDriverId, order.getState().name(), booked);
        nodeJsNotificationsService.driverRefresh(toDriverId, webUserId);
        nodeJsNotificationsService.driverRefresh(driverFromId, webUserId);

        return response;
    }






    public ActivationQueueResponse activationQueue(long clientId, long startTime, long endTime, Boolean activated, int numberPage, int sizePage){
        ActivationQueueResponse response = new ActivationQueueResponse();
        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(ActivationQueue.class);

        if(clientId != 0){
            criteria.add(Restrictions.eq("client.id", clientId));
        }
        if(activated != null){
            if(activated){
                criteria.add(Restrictions.isNotNull("timeOfActivation"));
            } else {
                criteria.add(Restrictions.isNull("timeOfActivation"));
            }
        }
        criteria = QueryUtils.fillDateTimeParameter(criteria, "timeOfRequest", startTime, endTime);
        criteria.addOrder(org.hibernate.criterion.Order.asc("timeOfRequest"));

        criteria.setProjection(Projections.rowCount());
        Long total = (Long)criteria.uniqueResult();

        criteria.setProjection(null);
        criteria.setResultTransformer(Criteria.ROOT_ENTITY);

        response.setTotalItems(total);

        long lastPageNumber = ((total / sizePage) + 1);
        response.setLastPageNumber(lastPageNumber);

        criteria.setFirstResult((numberPage - 1) * sizePage);
        criteria.setMaxResults(sizePage);

        List<ActivationQueue> result = criteria.list();

        for(ActivationQueue activationQueue: result){
            response.getActivationQueueInfos().add(ModelsUtils.toModel(activationQueue));
        }
        return response;
    }





    public ActivateCourierServiceResponse activateCourierService(List<Long> clientIds){
        ActivateCourierServiceResponse response = new ActivateCourierServiceResponse();
        Client client;
        List<ActivationQueue> saveActivationQueue = new ArrayList<>();
        if(!CollectionUtils.isEmpty(clientIds)){
            for(Long clientId : clientIds){
                client = clientRepository.findOne(clientId);
                if(client == null){
                    throw new CustomException(1, String.format("Клиента id=%s не найден", clientId));
                }
                ActivationQueue activationQueue = activationQueueRepository.findByClient(client);
                if (activationQueue == null){
                    throw new CustomException(2, String.format("Невозможно активировать клиента id=%s, т.к. он не делал запрос на активацию", clientId));
                }
                if(activationQueue.getTimeOfActivation()!=null){
                    throw new CustomException(2, String.format("Клиент id=%s уже активирован", clientId));
                }
                activationQueue.setTimeOfActivation(DateTimeUtils.nowNovosib_GMT6());
                saveActivationQueue.add(activationQueue);

                client.setCourierActivated(Boolean.TRUE);
                clientRepository.save(client);
            }
        }
        activationQueueRepository.save(saveActivationQueue);

        notifiedClient(saveActivationQueue);

        return response;
    }



    private CustomWindowUses buildUses(CustomWindow window, Client client){
        CustomWindowUses customWindowUses = new CustomWindowUses();
        customWindowUses.setClient(client);
        customWindowUses.setCustomWindow(window);
        customWindowUses.setActionType(CustomWindowUses.ActionType.NOT_SHOWN);
        customWindowUses.setIsShowed(Boolean.FALSE);
        return customWindowUses;
    }





    private void notifiedClient(List<ActivationQueue> saveActivationQueue){
        if(!CollectionUtils.isEmpty(saveActivationQueue)){
            CustomWindow customWindow = customWindowRepository.findByTypeWindow(TypeWindow.ACTIVATION_SERVICE_CONGRATULATION);
            CustomWindowUses customWindowUses;

            String message = commonService.getPropertyValue("message_service_activated");
            for(ActivationQueue activationQueue : saveActivationQueue){

                  customWindowUses = buildUses(customWindow, activationQueue.getClient());
                  customWindowUsesRepository.save(customWindowUses);

                  /* 1. отправить sms */
                    serviceSMSNotification.serviceSuccessfullyActivated(activationQueue.getClient().getPhone(), message);
                  /* 2. отправить в node */
                    nodeJsNotificationsService.serviceSuccessfullyActivated(activationQueue.getClient().getId(), message);
                  /* 3. отправить push */
                    app42PushNotificationService.serviceSuccessfullyActivated(activationQueue.getClient().getId(), message);
                  /* 4. отправить email */
                  //serviceEmailNotification
            }
        }
    }




    public OrdersResponse orders(long orderId, long clientId, long driverId, String state, long itemId, long storeId, long startTime, long endTime, int numberPage, int sizePage, int typeOrder){
        OrdersResponse response = new OrdersResponse();
        Session session = entityManager.unwrap(Session.class);

        Criteria criteria = session.createCriteria(Order.class);
        criteria.setResultTransformer(Criteria.ROOT_ENTITY);
        if(orderId != 0){
            criteria.add(Restrictions.eq("id", orderId));
        }
        if(clientId != 0){
            criteria.add(Restrictions.eq("client.id", clientId));
        }
        if(driverId != 0){
            criteria.add(Restrictions.eq("driver.id", driverId));
        }
        boolean alias = false;
        if(storeId != 0){
            alias = true;
            criteria.createAlias("orderItemPrices", "oip");
            criteria.createAlias("oip.itemPrice", "ip");
            criteria.createAlias("ip.storeAddress", "sa");
            criteria.createAlias("sa.store", "s");
            criteria.add(Restrictions.eq("s.id", storeId));
            criteria.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
        }
        if(itemId != 0){
            if(!alias){
                criteria.createAlias("orderItemPrices", "oip");
                criteria.createAlias("oip.itemPrice", "ip");
            }
            criteria.createAlias("ip.item", "it");
            criteria.add(Restrictions.eq("it.id", itemId));
            criteria.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
        }
        if(!StringUtils.isEmpty(state)){
            criteria.add(Restrictions.eq("state", Order.State.valueOf(state)));
        }
        if(typeOrder != 0){
            criteria.add(Restrictions.eq("orderType", OrderType.getByValue(typeOrder)));
        }

        criteria = QueryUtils.fillDateTimeParameter(criteria , "timeOfRequesting", startTime, endTime);
        criteria.addOrder(org.hibernate.criterion.Order.desc("timeOfRequesting"));

        criteria.setProjection(Projections.rowCount());
        Long total = (Long)criteria.uniqueResult();

        criteria.setProjection(null);
        criteria.setResultTransformer(Criteria.ROOT_ENTITY);

        response.setTotalItems(total);

        long lastPageNumber = ((total / sizePage) + 1);
        response.setLastPageNumber(lastPageNumber);

        criteria.setFirstResult((numberPage - 1) * sizePage);
        criteria.setMaxResults(sizePage);

        List<Order> result = criteria.list();

        for(Order order: result){
            response.getOrderInfoList().add(ModelsUtils.toModel(order, commonService.generalTimeLate(order.getId())));
        }
        return response;
    }




    // todo: сортировка!
    public StoreResponse stores(long storeId, String mask, int numberPage, int sizePage){
        StoreResponse response = new StoreResponse();
        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(Store.class);

        if(storeId!=0){
            criteria.add(Restrictions.eq("id", storeId));
        }
        if(mask != null){
            criteria.add(Restrictions.ilike("storeName", mask, MatchMode.ANYWHERE));
        }

        criteria.addOrder(org.hibernate.criterion.Order.desc("id"));

        criteria.setProjection(Projections.rowCount());
        Long total = (Long)criteria.uniqueResult();

        criteria.setProjection(null);
        criteria.setResultTransformer(Criteria.ROOT_ENTITY);

        response.setTotalItems(total);

        long lastPageNumber = ((total / sizePage) + 1);
        response.setLastPageNumber(lastPageNumber);

        criteria.setFirstResult((numberPage - 1) * sizePage);
        criteria.setMaxResults(sizePage);

        List<Store> result = criteria.list();

        for(Store store: result){
            response.getStoreInfoList().add(ModelsUtils.toModel(store));
        }
          return response;
    }



    public UpdateStoreResponse updateStore(List<StoreInfo> storeInfos){
        UpdateStoreResponse response = new UpdateStoreResponse();
        List<Store> saveStores = new ArrayList<>();
        for(StoreInfo info: storeInfos){
            if(info.getId() == 0){
                saveStores.add(ModelsUtils.fromModel(info, null));
            } else{
                Store store = storeRepository.findOne(info.getId());
                if(store == null){
                    throw new CustomException(1, "Магазин не найден");
                }
                saveStores.add(ModelsUtils.fromModel(info, store));
            }
        }
        storeRepository.save(saveStores);
        response.setStoreInfoList(ModelsUtils.toModelStoreInfoList(saveStores));
          return response;
    }





    public UpdateStoreAddressResponse updateStoreAddress(List<StoreAddressInfo> storeAddressInfos){
        UpdateStoreAddressResponse response = new UpdateStoreAddressResponse();
        List<StoreAddress> saveStoreAddress = new ArrayList<>();

        for(StoreAddressInfo info: storeAddressInfos){

            Store store = null;
            if(info.getStoreInfo().getId() != 0){
                store = storeRepository.findOne(info.getStoreInfo().getId());
                if(store == null){
                    throw new CustomException(2, String.format("Магазин id=% не найден", info.getStoreInfo().getId()));
                }
            }
            store = ModelsUtils.fromModel(info.getStoreInfo(), store);
            store = storeRepository.save(store);

            StoreAddress storeAddress = null;
            if(info.getId() != 0){
                storeAddress = storeAddressRepository.findOne(info.getId());
                if(storeAddress == null){
                    throw new CustomException(3, String.format("Адрес магазина id=% не найден", info.getId()));
                }
            }
            storeAddress = ModelsUtils.fromModel(info, store, storeAddress);

            saveStoreAddress.add(storeAddress);
        }
        storeAddressRepository.save(saveStoreAddress);
        response.setStoreAddressInfos(ModelsUtils.toModelStoreAddressInfoList(saveStoreAddress));
           return response;
    }

    /*
            Store store = storeRepository.findOne(info.getStoreInfo().getId());ghghgh
            if(store == null){
                throw new CustomException(1, "Магазин не найден");
            }
            if(info.getId() == 0){
                saveStoreAddress.add(ModelsUtils.fromModel(info, store, null));
            } else{
                StoreAddress storeAddress = storeAddressRepository.findOne(info.getId());
                if(storeAddress == null){
                    throw new CustomException(1, "Адрес магазина не найден");
                }
                saveStoreAddress.add(ModelsUtils.fromModel(info, store, storeAddress));
            }
     */



    /*
    public UpdateItemResponse updateItem(List<ItemInfo> itemInfos){
        UpdateItemResponse response = new UpdateItemResponse();
        List<Item> saveItems = new ArrayList<>();


        for(ItemInfo info: itemInfos){
            if(info.getId() == 0){
                saveItems.add(ModelsUtils.fromModel(info, null));
            } else{
                Item item = itemRepository.findOne(info.getId());
                if(item == null){
                    throw new CustomException(1, "Товар не найден");
                }
                saveItems.add(ModelsUtils.fromModel(info, item));
            }
        }
           itemRepository.save(saveItems);
           response.setItemInfoList(ModelsUtils.toModelItemInfoList(saveItems));
           return response;
    }
*/



    public List<ItemProperty> buildItemProperties(ItemInfo info){
          List<ItemProperty> itemProperties = new ArrayList<>();
          if(!CollectionUtils.isEmpty(info.getItemPropertyInfos())){
               for(ItemPropertyInfo propertyInfo: info.getItemPropertyInfos()){
                   ItemProperty itemProperty;
                   if(propertyInfo.getId() ==0){
                       itemProperty = ModelsUtils.fromModel(null, propertyInfo);
                       itemProperty = itemPropertyRepository.save(itemProperty);
                       itemProperties.add(itemProperty);
                   } else {
                       itemProperty =  itemPropertyRepository.findOne(propertyInfo.getId());
                       if(itemProperty == null){
                           throw new CustomException(1, "Свойство не найдено");
                       }
                       itemProperty = ModelsUtils.fromModel(itemProperty, propertyInfo);
                       itemProperty = itemPropertyRepository.save(itemProperty);
                       itemProperties.add(itemProperty);
                   }
               }
          }
                  return itemProperties;
    }



    public UpdateItemResponse updateItem(List<ItemInfo> itemInfos){
        UpdateItemResponse response = new UpdateItemResponse();
        List<Item> saveItems = new ArrayList<>();

        Item item;
        for(ItemInfo info: itemInfos){
            if(info.getId() == 0){

                item = ModelsUtils.fromModel(info, null);

                List<ItemProperty> itemProperties = buildItemProperties(info);
                if(!CollectionUtils.isEmpty(itemProperties)){
                    item.getItemPropertyId().clear();
                    for(ItemProperty ip: itemProperties){
                        item.getItemPropertyId().add(ip.getId());
                    }
                }

                saveItems.add(item);
            } else{
                item = itemRepository.findOne(info.getId());
                if(item == null){
                    throw new CustomException(1, "Товар не найден");
                }
                item = ModelsUtils.fromModel(info, item);
                List<ItemProperty> itemProperties = buildItemProperties(info);
                if(!CollectionUtils.isEmpty(itemProperties)){
                    item.getItemPropertyId().clear();
                    for(ItemProperty ip: itemProperties){
                        item.getItemPropertyId().add(ip.getId());
                    }
                }
                saveItems.add(item);
            }
        }

        itemRepository.save(saveItems);

        for(Item it :saveItems){
            ItemInfo itemInfo = ModelsUtils.toModel(it);
            if(!CollectionUtils.isEmpty(it.getItemPropertyId())){
                for(Long id: it.getItemPropertyId()){
                    itemInfo.getItemPropertyInfos().add(ModelsUtils.toModel(itemPropertyRepository.findOne(id)));
                }
            }
                response.getItemInfoList().add(itemInfo);
        }
        return response;
    }




    private ItemPrice getItemPrice(ItemPriceInfo info){
        ItemPrice itemPrice = null;
        Item item = null;

        if (info.getItemInfo().getId() != 0){
            item = itemRepository.findOne(info.getItemInfo().getId());
            if(item == null){
                throw new CustomException(1, String.format("Товар id=% не найден", info.getItemInfo().getId()));
            }
        }
        item = ModelsUtils.fromModel(info.getItemInfo(), item);
        item = itemRepository.save(item);

        List<ItemProperty> itemProperties = buildItemProperties(info.getItemInfo());
        if(!CollectionUtils.isEmpty(itemProperties)){
            for(ItemProperty ip: itemProperties){
                item.getItemPropertyId().add(ip.getId());
            }
        }


        Store store = null;
        if(info.getStoreAddressInfo() != null) {
            if (info.getStoreAddressInfo().getStoreInfo().getId() != 0) {
                store = storeRepository.findOne(info.getStoreAddressInfo().getStoreInfo().getId());
                if (store == null) {
                    throw new CustomException(2, String.format("Магазин id=% не найден", info.getStoreAddressInfo().getStoreInfo().getId()));
                }
            }
            store = ModelsUtils.fromModel(info.getStoreAddressInfo().getStoreInfo(), store);
            store = storeRepository.save(store);
        }


        StoreAddress storeAddress = null;
        if(info.getStoreAddressInfo() != null) {
            if (info.getStoreAddressInfo().getId() != 0) {
                storeAddress = storeAddressRepository.findOne(info.getStoreAddressInfo().getId());
                if (storeAddress == null) {
                    throw new CustomException(3, String.format("Адрес магазина id=% не найден", info.getStoreAddressInfo().getId()));
                }
            }
            storeAddress = ModelsUtils.fromModel(info.getStoreAddressInfo(), store, storeAddress);
            storeAddress = storeAddressRepository.save(storeAddress);
        }

        if(info.getId() != 0){
            itemPrice = itemPriceRepository.findOne(info.getId());
            if(itemPrice == null){
                throw new CustomException(4, String.format("Стоимость товара id=% не найдена", info.getId()));
            }
        }
        itemPrice = ModelsUtils.fromModel(info, itemPrice, storeAddress, item);
        //itemPriceRepository.save(itemPrice);
        return itemPrice;
    }



    public UpdateItemPriceResponse updateItemPrices(List<ItemPriceInfo> itemPriceInfos){
        UpdateItemPriceResponse response = new UpdateItemPriceResponse();
        List<ItemPrice> saveItemPrices = new ArrayList<>();
        for(ItemPriceInfo info: itemPriceInfos){
            /*
            Item item = null;
            if(info.getItemInfo().getId() != 0){
                item = itemRepository.findOne(info.getItemInfo().getId());
                if(item == null){
                    throw new CustomException(1, String.format("Товар id=% не найден", info.getItemInfo().getId()));
                }
            }
            item = ModelsUtils.fromModel(info.getItemInfo(), item);
            item = itemRepository.save(item);

            Store store = null;
            if(info.getStoreAddressInfo().getStoreInfo().getId() != 0){
                store = storeRepository.findOne(info.getStoreAddressInfo().getStoreInfo().getId());
                if(store == null){
                    throw new CustomException(2, String.format("Магазин id=% не найден", info.getStoreAddressInfo().getStoreInfo().getId()));
                }
            }
            store = ModelsUtils.fromModel(info.getStoreAddressInfo().getStoreInfo(), store);
            store = storeRepository.save(store);

            StoreAddress storeAddress = null;
            if(info.getStoreAddressInfo().getId() != 0){
                storeAddress = storeAddressRepository.findOne(info.getStoreAddressInfo().getId());
                if(storeAddress == null){
                    throw new CustomException(3, String.format("Адрес магазина id=% не найден", info.getStoreAddressInfo().getId()));
                }
            }
            storeAddress = ModelsUtils.fromModel(info.getStoreAddressInfo(), store, storeAddress);
            storeAddress = storeAddressRepository.save(storeAddress);

            ItemPrice itemPrice = null;
            if(info.getId() != 0){
                itemPrice = itemPriceRepository.findOne(info.getId());
                if(itemPrice == null){
                    throw new CustomException(4, String.format("Стоимость товара id=% не найдена", info.getId()));
                }
            }
            itemPrice = ModelsUtils.fromModel(info, itemPrice, storeAddress, item);
            */

            saveItemPrices.add(getItemPrice(info));
        }
           itemPriceRepository.save(saveItemPrices);
           response.setItemPriceInfoList(ModelsUtils.toModelItemPriceInfoList(saveItemPrices));
               return response;
    }


    /*
    StoreAddress storeAddress = null;
            Item item = null;
            if(info.getStoreAddressInfo().getId() !=0 ){
                storeAddress = storeAddressRepository.findOne(info.getStoreAddressInfo().getId());
            } else {
                storeAddress = storeAddressRepository.save(ModelsUtils.fromModel(info.getStoreAddressInfo()));
            }
            if(info.getItemInfo().getId() == 0){
                throw new CustomException(1, String.format("Товар id=% не найден", info.getItemInfo().getId()));
            } else {

            }
            if(info.getId() == 0){
                saveItemPrices.add(ModelsUtils.fromModel(info, null, storeAddress, item));
            } else{
                ItemPrice itemPrice = itemPriceRepository.findOne(info.getId());
                if(itemPrice == null){
                    throw new CustomException(1, String.format("Цена с id=% не найдена", info.getId()));
                }
                    saveItemPrices.add(ModelsUtils.fromModel(info, itemPrice, storeAddress, item));
            }
     */





    public ItemPriceResponse itemPrices(long itemId, long storeId, long orderId, int numberPage, int sizePage){
        ItemPriceResponse response = new ItemPriceResponse();
        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(ItemPrice.class);

        criteria.createAlias("storeAddress", "sa");
        criteria.createAlias("sa.store", "s");

        if(itemId != 0){
            criteria.add(Restrictions.eq("item.id", itemId));
        }
        if(storeId != 0){
            criteria.add(Restrictions.eq("s.id", storeId));
        }
        criteria.addOrder(org.hibernate.criterion.Order.desc("id"));

        criteria.setProjection(Projections.rowCount());
        Long total = (Long)criteria.uniqueResult();

        criteria.setProjection(null);
        criteria.setResultTransformer(Criteria.ROOT_ENTITY);

        response.setTotalItems(total);

        long lastPageNumber = ((total / sizePage) + 1);
        response.setLastPageNumber(lastPageNumber);

        criteria.setFirstResult((numberPage - 1) * sizePage);
        criteria.setMaxResults(sizePage);

        List<ItemPrice> result = criteria.list();

        if(orderId != 0){
            //for(ItemPrice res : result){
            //    LOGGER.info("До: "+res.getId());
            //}
            Order order = orderRepository.findOne(orderId);
            //зафигачить расстояние до клиента
            ItemLocation userLocation = commonService.getLocationByFirstOrderAddress(order.getTargetAddresses());
            result = sortItemPrices(result, userLocation);
        }

        for(ItemPrice itemPrice: result){
            response.getItemPriceInfoList().add(ModelsUtils.toModel(itemPrice));
        }
            return response;
    }



    public List<ItemPrice> sortItemPrices(List<ItemPrice> prices, ItemLocation userLocation){
        List<ItemPrice> itemPricesWithDistance = new ArrayList<>();

        for(ItemPrice itemPrice : prices){
            StoreAddress storeAddress = itemPrice.getStoreAddress();
            double distance = GeoUtils.distance(storeAddress.getStoreLocation().getLatitude(), storeAddress.getStoreLocation().getLongitude(), userLocation.getLatitude(), userLocation.getLongitude());
            storeAddress.setDistance(distance);
            itemPrice.setStoreAddress(storeAddress);
            itemPricesWithDistance.add(itemPrice);
        }

        Collections.sort(itemPricesWithDistance, new Comparator<ItemPrice>() {
            @Override
            public int compare(ItemPrice first, ItemPrice second) {
                return new Double(first.getStoreAddress().getDistance()).compareTo(new Double(second.getStoreAddress().getDistance()));
            }
        });
        //for(ItemPrice res : itemPricesWithDistance){
        //    LOGGER.info("После: itemPriceId = "+res.getId() + " расстояние до магазина: "+res.getStoreAddress().getDistance());
        //}
                return itemPricesWithDistance;
    }




    public StoreAddressResponse storeAddress(long storeId, long storeAddressId, int numberPage, int sizePage, long orderId){
        StoreAddressResponse response = new StoreAddressResponse();
        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(StoreAddress.class);

        if(storeAddressId != 0){
            criteria.add(Restrictions.eq("id", storeAddressId));
        }
        if(storeId != 0){
            criteria.add(Restrictions.eq("store.id", storeId));
        }

        criteria.addOrder(org.hibernate.criterion.Order.desc("id"));

        criteria.setProjection(Projections.rowCount());
        Long total = (Long)criteria.uniqueResult();

        criteria.setProjection(null);
        criteria.setResultTransformer(Criteria.ROOT_ENTITY);

        response.setTotalItems(total);

        long lastPageNumber = ((total / sizePage) + 1);
        response.setLastPageNumber(lastPageNumber);

        criteria.setFirstResult((numberPage - 1) * sizePage);
        criteria.setMaxResults(sizePage);

        List<StoreAddress> result = criteria.list();

        if(orderId != 0){
            //for(StoreAddress res : result){
            //    LOGGER.info("До: "+res.getStore().getStoreName());
            //}
            Order order = orderRepository.findOne(orderId);
            ItemLocation userLocation = commonService.getLocationByFirstOrderAddress(order.getTargetAddresses());
            result = sortStoreAddress(result, userLocation);
        }

        for(StoreAddress storeAddress: result){
            response.getStoreAddressInfos().add(ModelsUtils.toModel(storeAddress));
        }
        return response;
    }







    public List<StoreAddress> sortStoreAddress(List<StoreAddress> adresses, ItemLocation userLocation){
        List<StoreAddress> storeAddressWithDistance = new ArrayList<>();

        for (StoreAddress storeAddress : adresses) {
           double distance = GeoUtils.distance(storeAddress.getStoreLocation().getLatitude(), storeAddress.getStoreLocation().getLongitude(), userLocation.getLatitude(), userLocation.getLongitude());
           storeAddress.setDistance(distance);
           storeAddressWithDistance.add(storeAddress);
        }

        Collections.sort(storeAddressWithDistance, new Comparator<StoreAddress>() {
            @Override
            public int compare(StoreAddress first, StoreAddress second) {
                return new Double(first.getDistance()).compareTo(new Double(second.getDistance()));
            }
        });
        //for(StoreAddress res : storeAddressWithDistance){
        //   LOGGER.info("После: Магазин = "+res.getStore().getStoreName()+" расстояние до магазина: "+res.getDistance());
        //}
        return storeAddressWithDistance;
    }




    public ItemResponse items(long itemId, String mask, String itemType, int numberPage, int sizePage){
        ItemResponse response = new ItemResponse();
        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(Item.class);

        if(itemId!=0){
            criteria.add(Restrictions.eq("id", itemId));
        }
        if(mask != null){
            criteria.add(Restrictions.ilike("itemName", mask, MatchMode.ANYWHERE));
        }
        if(itemType != null){
            criteria.add(Restrictions.eq("itemType", Item.ItemType.valueOf(itemType)));
        }

        criteria.addOrder(org.hibernate.criterion.Order.desc("id"));

        criteria.setProjection(Projections.rowCount());
        Long total = (Long)criteria.uniqueResult();

        criteria.setProjection(null);
        criteria.setResultTransformer(Criteria.ROOT_ENTITY);

        response.setTotalItems(total);

        long lastPageNumber = ((total / sizePage) + 1);
        response.setLastPageNumber(lastPageNumber);

        criteria.setFirstResult((numberPage - 1) * sizePage);
        criteria.setMaxResults(sizePage);

        List<Item> result = criteria.list();

        for(Item item: result){

                ItemInfo itemInfo = ModelsUtils.toModel(item);
                if(!CollectionUtils.isEmpty(item.getItemPropertyId())){
                    for(Long id: item.getItemPropertyId()){
                        itemInfo.getItemPropertyInfos().add(ModelsUtils.toModel(itemPropertyRepository.findOne(id)));
                    }
                }
                response.getItemInfos().add(itemInfo);

            //response.getItemInfos().add(ModelsUtils.toModel(item));
        }
            return response;
    }
}
