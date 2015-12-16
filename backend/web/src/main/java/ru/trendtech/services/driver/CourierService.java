package ru.trendtech.services.driver;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import ru.trendtech.common.mobileexchange.model.common.CustomException;
import ru.trendtech.common.mobileexchange.model.common.DriverTypeInfo;
import ru.trendtech.common.mobileexchange.model.common.ItemLocation;
import ru.trendtech.common.mobileexchange.model.courier.CompleteOrderResponse;
import ru.trendtech.common.mobileexchange.model.courier.OrderInfo;
import ru.trendtech.common.mobileexchange.model.courier.client.OrderHistoryResponse;
import ru.trendtech.common.mobileexchange.model.courier.driver.*;
import ru.trendtech.common.mobileexchange.model.web.AdministratorRole;
import ru.trendtech.domain.*;
import ru.trendtech.domain.billing.Account;
import ru.trendtech.domain.billing.PaymentType;
import ru.trendtech.domain.courier.*;
import ru.trendtech.models.ModelsUtils;
import ru.trendtech.repositories.CashRepository;
import ru.trendtech.repositories.DriverRepository;
import ru.trendtech.repositories.LocationRepository;
import ru.trendtech.repositories.WebUserRepository;
import ru.trendtech.repositories.billing.AccountRepository;
import ru.trendtech.repositories.courier.*;
import ru.trendtech.services.administration.AdminCurierService;
import ru.trendtech.services.common.CommonService;
import ru.trendtech.services.courier.OrderService;
import ru.trendtech.services.notifications.node.NodeJsNotificationsService;
import ru.trendtech.services.notifications.push.App42PushNotificationService;
import ru.trendtech.services.sms.ServiceSMSNotification;
import ru.trendtech.utils.DateTimeUtils;
import ru.trendtech.utils.GeoUtils;
import ru.trendtech.utils.MoneyUtils;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by petr on 10.09.2015.
 */

@Service
@Transactional
public class CourierService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CourierService.class);
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private LocationRepository courierLocationRepository;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private LateOrderRepository lateOrderRepository;
    @Autowired
    private RemindOrderRepository remindOrderRepository;
    @Autowired
    private QueueOrderRepository queueOrderRepository;
    @Autowired
    private CommonService commonService;
    @Autowired
    private NodeJsNotificationsService nodeJsNotificationsService;
    @Autowired
    private App42PushNotificationService app42PushNotificationService;
    @Autowired
    private DriverService driverService;
    @Autowired
    private DefaultPriceRepository defaultPriceRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ServiceSMSNotification serviceSMSNotification;
    @Autowired
    private AdminCurierService adminCurierService;
    @Autowired
    private OrderService orderService;

    /*
         // int minimalPrice = defaultPrice.getMinimalPrice().getAmountMinorInt(); // минималка
         // int priceInFactAmount = order.getPriceInFact().getAmountMinorInt();
    */




    public PrepaireCompleteOrderResponse prepaireComplete(Driver driver, long orderId){
        PrepaireCompleteOrderResponse response = new PrepaireCompleteOrderResponse();
        Order order = orderRepository.findOne(orderId);
        if(order == null){
            throw new CustomException(1, "Заказ не найден");
        }
        if(EnumSet.of(Order.State.CANCELED, Order.State.COMPLETED).contains(order.getState())){
            throw new CustomException(2, "Заказ отменен или завершен");
        }
        DefaultPrice defaultPrice = defaultPriceRepository.findByActiveAndOrderType(Boolean.TRUE, order.getOrderType());
        int priceForUs = defaultPrice.getComissionPrice().getAmountMinorInt(); // сумма, которую забираем себе
        int sumToCourierByTariff = defaultPrice.getToCourierPrice().getAmountMinorInt();

        LOGGER.info("Доставка: " + order.getPriceDelivery().getAmount());
        LOGGER.info("Стоимость за товар: " + (order.getPriceOfItems() != null ? order.getPriceOfItems().getAmount() : 0));
        LOGGER.info("Общая стоимость: " + order.getPriceInFact().getAmount());

        int sumToCourierForAdditionalAddress = 0;
        int ourSumForAdditionalAddress = 0;
        if(!CollectionUtils.isEmpty(order.getTargetAddresses()) && order.getTargetAddresses().size() > 1){
            int countAdditionalAddress = order.getTargetAddresses().size() - 1;
            sumToCourierForAdditionalAddress = defaultPrice.getToCourierForAddressPrice().getAmountMinorInt() * countAdditionalAddress;
            ourSumForAdditionalAddress =  defaultPrice.getCommissionForAddressPrice().getAmountMinorInt() * countAdditionalAddress;
        }
        LOGGER.info("Сумма курьеру за доп адрес: " + sumToCourierForAdditionalAddress);
        LOGGER.info("Наша сумма за доп адрес: " + ourSumForAdditionalAddress);


        int deliveryPrice = order.getPriceDelivery().getAmountMinorInt();

        int sumToCourierForKm = deliveryPrice - sumToCourierByTariff - priceForUs;
        LOGGER.info("Курьеру: по тарифу = " + sumToCourierByTariff + " + за км = " + sumToCourierForKm + " + за доп адрес = " + sumToCourierForAdditionalAddress);

        response.setPriceItems((order.getPriceOfItems() != null ? order.getPriceOfItems().getAmount().intValue() : 0));
        response.setSumToCourier(order.getPriceDelivery().getAmount().intValue());
        response.setGeneralPrice(order.getPriceInFact().getAmount().intValue());
        return response;
    }



    /*
                22	K: Общая стоимость заказа
                23	K: Наша комиссия
                24	K: Курьеру по тарифу
                25	K: Стоимость за км
                26	К: Доплата за адрес
                27  К: % от стоимости страховки товара
                28  К: Наша комиссия за доп адрес
    */
    public void operationWithDriverCashFlow(Driver driver, Order order, int priceForUs, int priceToCourierByTariff, int toCourierForKm, int sumToCourierForAdditionalAddress, int ourSumForAdditionalAddress){
        adminCurierService.updateCashFlow(order, order.getPriceInFact().getAmountMinorInt(), 22);
        adminCurierService.updateCashFlow(order, priceForUs, 23);
        adminCurierService.updateCashFlow(order, priceToCourierByTariff, 24);
        adminCurierService.updateCashFlow(order, toCourierForKm, 25);
        adminCurierService.updateCashFlow(order, sumToCourierForAdditionalAddress, 26);
        adminCurierService.updateCashFlow(order, order.getPriceOfPercentInsurance().getAmountMinorInt(), 27);
        adminCurierService.updateCashFlow(order, ourSumForAdditionalAddress, 28);

        int generalSumToCourier = priceToCourierByTariff + toCourierForKm + sumToCourierForAdditionalAddress;

        Money amountOfMoney = MoneyUtils.getMoney(generalSumToCourier / 100);

        Account driverAccount = accountRepository.findOne(driver.getAccount().getId());
        Money courierBalance = driverAccount.getMoney();
        courierBalance = courierBalance.plus(amountOfMoney);
        driverAccount.setMoney(courierBalance);
        accountRepository.save(driverAccount);
    }





    /*
    public CompleteOrderResponse completeOrder(Driver driver, long orderId){
        CompleteOrderResponse response = new CompleteOrderResponse();
        Order order = orderRepository.findOne(orderId);
        if(order == null){
            throw new CustomException(1, "Заказ не найден");
        }
        if(EnumSet.of(Order.State.CANCELED, Order.State.COMPLETED).contains(order.getState())){
            throw new CustomException(2, "Заказ отменен или завершен");
        }
//        if(driver.getCurrentOrder().getId() != orderId){
//            throw new CustomException(3, "Попытка завершить чужой заказ");
//        }

        DefaultPrice defaultPrice = defaultPriceRepository.findByActiveAndOrderType(Boolean.TRUE, order.getOrderType());

        int priceForUs = defaultPrice.getComissionPrice().getAmountMinorInt(); // сумма, которую забираем себе
        int priceToCourier = defaultPrice.getToCourierPrice().getAmountMinorInt();

        LOGGER.info("Доставка: " + order.getPriceDelivery().getAmount());
        LOGGER.info("Стоимость за товар: " + (order.getPriceOfItems() != null ? order.getPriceOfItems().getAmount() : 0));
        LOGGER.info("Общая стоимость: " + order.getPriceInFact().getAmount());

        int deliveryPrice = order.getPriceDelivery().getAmountMinorInt();

        int toCourierForKm = deliveryPrice - priceToCourier - priceForUs;
        LOGGER.info("Курьеру: по тарифу = " + priceToCourier + " + за км = " + toCourierForKm);

        if(order.getPaymentType().equals(PaymentType.CASH)){
            // наличкой
            operationWithDriverCashFlow(driver, order, priceForUs, priceToCourier, toCourierForKm);
        } else {
             // картой
            order.setState(Order.State.WAIT_TO_PAYMENT);
        }

        order.setTimeOfFinishingInFact(DateTimeUtils.nowNovosib_GMT6());
        order.setState(Order.State.COMPLETED);
        orderRepository.save(order);

        commonService.saveChangeState(order);

        driver.setCurrentOrder(null);
        driverRepository.save(driver);
        nodeJsNotificationsService.courierOrderFinished(order);
           return response;
    }
    */



    /*
        LOGGER.info("Стоимость по факту: " + priceInFactAmount / 100);
        LOGGER.info("Минималка: " + minimalPrice / 100);
        LOGGER.info("Комиссия: " + sumForUs / 100);
        LOGGER.info("Сумма курьеру по типу заказа: " + sumToCourier / 100);
        int priceForKm = priceInFactAmount - sumForUs - sumToCourier;
        LOGGER.info("Доплата за километраж: " + priceForKm);
        sumToCourier += priceForKm > 0 ? priceForKm : 0;
        int increasePercent = order.getIncreasePercent();
        int insurancePercent = order.getPercentInsuranceOnDayOfOrder();
        LOGGER.info("Общая сумма курьеру: " + sumToCourier);
        */





    public CourierConfigurationResponse configuration(Driver driver){
        CourierConfigurationResponse response = new CourierConfigurationResponse();
        DriverSetting driverSetting = driver.getDriverSetting();
        response.setDriverTypeInfo(DriverTypeInfo.valueOf(DriverSetting.DriverType.DRIVER.name()));
        if(driverSetting != null) {
            if(driverSetting.getDriverType() != null && !StringUtils.isEmpty(driverSetting.getDriverType().name())) {
                response.setDriverTypeInfo(DriverTypeInfo.valueOf(driverSetting.getDriverType().name()));
            }
        }
        response.setTaxistoConfiguration(driverService.getConfiguration_v3(0, driver.getId(), driver.getToken()));
        if(driver.getCurrentOrder() != null){
            response.setOrderInfo(ModelsUtils.toModel(driver.getCurrentOrder(), 0));
        }
            return response;
    }





    public void saveCourierLocation(long driverId, ItemLocation location, int distance) {
        double newLatitude = location.getLatitude();
        double newlongitude = location.getLongitude();
        Driver driver = driverRepository.findOne(driverId);
        if (driver != null) {
            DriverLocation driverLocation = courierLocationRepository.findByDriver(driver);
            Location previousLocation = driverLocation.getLocation();

            double latitudePrevious = previousLocation.getLatitude();
            double longitudePrevious = previousLocation.getLongitude();

            if(driverLocation == null){
                driverLocation = commonService.buildDriverLocation(driver, newLatitude, newlongitude, distance);
            } else {
                if(previousLocation != null){
                    previousLocation.setLatitude(newLatitude);
                    previousLocation.setLongitude(newlongitude);
                    driverLocation.setDistance(distance);
                    driverLocation.setLocation(previousLocation);
                }
            }
                courierLocationRepository.save(driverLocation);

            /* если у водителя есть текущий заказ - шлем координаты клиенту */
            Order order = driver.getCurrentOrder();
            if(order != null){
                nodeJsNotificationsService.courierSendLocationToClient(order, driverLocation);
                 /*
                 РЕШИЛИ СДЕЛАТЬ ЭТУ ШНЯГУ НА ВОДИТЕЛЕ
                 // 2. todo: расчитать кол-во метров, которые водитель откатал и записать в нужную колонку в зависимости от того, какой статус заказа сейчас (едет к клиенту или по магазинам)
                 double distance = GeoUtils.distance(latitudePrevious, longitudePrevious, newLatitude, newlongitude);
                 if(order.getState().equals(Order.State.IN_PROGRESS_BY_COURIER)){
                     order.setDistanceInFact(order.getDistanceInFact() + (int)distance);
                 } else if(order.getState().equals(Order.State.GO_TO_CLIENT)){
                     order.setDistanceInFactToStore(order.getDistanceInFactToStore() + (int) distance);
                 }
                    orderRepository.save(order);
                  */

                 // 1. todo: событие нод для смены положение водителя на карте
            }
        }
    }




    public RemindConfirmResponse remindConfirm(Order order){
        RemindConfirmResponse response = new RemindConfirmResponse();
        RemindOrder remindOrder = remindOrderRepository.findByOrderAndTimeOfConfirmIsNull(order);
        if(remindOrder != null){
            remindOrder.setTimeOfConfirm(DateTimeUtils.nowNovosib_GMT6());
            remindOrderRepository.save(remindOrder);
        }
        return response;
    }





    private void take(Order order, Driver driver){
        DriverLocation location = courierLocationRepository.findByDriver(driver);
        location.setOrder(order);
        courierLocationRepository.save(location);

        driver.setCurrentOrder(order);
        driverRepository.save(driver);

        order.setState(Order.State.IN_PROGRESS_BY_COURIER);
        order.setTimeOfReadyToProgress(DateTimeUtils.nowNovosib_GMT6());

        int arrivalTime = commonService.calculateArrivalTime(null, order, driver);
        LOGGER.info("readyToProgress real arrivalTime = " + arrivalTime + " excrected: (arrivalTime + 30) = " + (arrivalTime + 30)); // todo: 30 - унести в проертис
        arrivalTime += 30;
        order.setExpectedArrivalTime(arrivalTime);
        order.setTimeOfFinishingExpected(DateTimeUtils.nowNovosib_GMT6().plusMinutes(arrivalTime));
        orderRepository.save(order);

        orderService.saveChangeState(order);
    }






    public ReadyToProgressResponse readyToProgress(long orderId, Driver driver){
        ReadyToProgressResponse response = new ReadyToProgressResponse();
        Order order = orderRepository.findOne(orderId);
        if(order == null){
            throw new CustomException(1, "Заказ не найден");
        }
        if(EnumSet.of(Order.State.CANCELED, Order.State.COMPLETED).contains(order.getState())){
            throw new CustomException(2, "Заказ завершен или отменен");
        }
        if(order.getDriver() == null && (order.getDriver().getId() != driver.getId())){
            throw new CustomException(3, "Нарушение безопасности");
        }
        if(driver.getCurrentOrder() == null){
             take(order, driver);
        }
        if(driver.getCurrentOrder() != null && (driver.getCurrentOrder().getId() != orderId)){
            throw new CustomException(4, "У вас есть текущий заказ");
        }

        // todo: calculate expected distance to store: расчет с текущей локации водителя
        order.setTimeOfStartingOrderProcessing(DateTimeUtils.nowNovosib_GMT6());
        orderRepository.save(order);

        nodeJsNotificationsService.readyToProgress(order);

            return response;
    }




    public ReadyToGoResponse readyToGo(long orderId, Driver driver){
        Order order = orderRepository.findOne(orderId);
        if(order == null){
            throw new CustomException(1, "Заказ не найден");
        }
        if(EnumSet.of(Order.State.CANCELED, Order.State.COMPLETED).contains(order.getState())){
            throw new CustomException(2, "Заказ завершен или отменен");
        }
        LOGGER.info("readyToGo ----------> : orderId = " + orderId + ", driverId = " + driver.getId() + ", driverId on order = " + (order.getDriver()!=null ? order.getDriver().getId() : 0));
        /*
        todo:
        if(order.getDriver().getId() != driver.getId()){
            throw new CustomException(4, "Нарушение безопасности");
        }
        */
        ReadyToGoResponse response = new ReadyToGoResponse();

        /* расчетная дистанция от текущих координат водилы до точки последнего магазина */
        int distance = commonService.distanceFromCurrentCourierLocationToLastStore(order, driver);
        order.setDistanceExpectedToStore(distance);

        order.setTimeOfReadyToGo(DateTimeUtils.nowNovosib_GMT6());
        order.setState(Order.State.GO_TO_CLIENT);
        orderRepository.save(order);

        orderService.saveChangeState(order);

        response.setArrivalTime(commonService.calculateArrivalTime(null, order, driver));

        nodeJsNotificationsService.courierAllPurchased(order);
        return response;
    }




    private LateOrder buildLateCourier(Order order, int minutesLate){
        LateOrder lateOrder = new LateOrder();
        lateOrder.setOrder(order);
        lateOrder.setMinutesLate(minutesLate);
        lateOrder.setTimeOfRequesting(DateTimeUtils.nowNovosib_GMT6());
        return lateOrder;
    }



    public LateOrderResponse lateOrder(Order order, int lateMinutes){
        LateOrderResponse response = new LateOrderResponse();
        lateOrderRepository.save(buildLateCourier(order, lateMinutes));

        /*
        DateTime finishTime = order.getTimeOfFinishingExpected();
        finishTime = finishTime.plusMinutes(lateMinutes);
        order.setTimeOfFinishingExpected(finishTime);
        */

        order.setTimeOfFinishingExpected(order.getTimeOfFinishingExpected().plusMinutes(lateMinutes));
        orderRepository.save(order);

        nodeJsNotificationsService.courierLate(order, lateMinutes);
        return response;
    }



    /*

       todo: при завершении заказа курьером - я лезу в очередь, беру следующий по порядку заказ по этому курьеру, прописываю его в currentOrder и делаю ему ребут конфигурации


    */



    public TakeOrderResponse takeOrder(Order order, Driver driver){
        TakeOrderResponse response = new TakeOrderResponse();

        if(queueOrderRepository.findByOrder(order) != null){
             throw new CustomException(1, "Заказ уже взят");
        }
        /* сохраняем в очередь */
        QueueOrder queueOrder = new QueueOrder();
        queueOrder.setDriver(driver);
        queueOrder.setOrder(order);
        queueOrder.setTimeOfAssigning(DateTimeUtils.nowNovosib_GMT6());
        queueOrderRepository.save(queueOrder);

        order.setDriver(driver);
        order.setState(Order.State.TAKEN_BY_COURIER);
        order.setTimeOfAssigning(DateTimeUtils.nowNovosib_GMT6());
        orderRepository.save(order);

        orderService.saveChangeState(order);

        serviceSMSNotification.courierAssigned(order.getClient(), driver, "" + order.getId());

        /*
           todo: впилить снятие текущего заказа с webUser
        */

        DriverLocation location = courierLocationRepository.findByDriver(driver);
        nodeJsNotificationsService.courierOrderAssign(order, commonService.calculateArrivalTime(null, order, driver), location);

        app42PushNotificationService.courierOrderAssign(order, 0, null);

        return response;
    }



    public OrderHistoryResponse orderHistory(Driver driver){
        OrderHistoryResponse response = new OrderHistoryResponse();
        List<Order.State> states = new ArrayList<>();
        List<OrderInfo> orderInfos = new ArrayList<>();

        states.add(Order.State.WAIT_TO_CONFIRM);
        states.add(Order.State.GO_TO_CLIENT);
        states.add(Order.State.IN_PROGRESS_BY_COURIER);
        states.add(Order.State.TAKEN_BY_COURIER);

        List<Order> orders = orderRepository.findByStateInAndDriverOrderByTimeOfRequestingDesc(states, driver);
        orderInfos = buildOrderInfoList(orders, orderInfos);

        states.clear();

        states.add(Order.State.CONFIRMED);
        orders = orderRepository.findByStateInAndDriverIsNullOrderByTimeOfRequestingDesc(states);
        orderInfos = buildOrderInfoList(orders, orderInfos);

        response.setOrderInfos(orderInfos);
        return response;
    }



    private List<OrderInfo> buildOrderInfoList(List<Order> orders, List<OrderInfo> ordersInfo){
        if(!CollectionUtils.isEmpty(orders)){
            for(Order order : orders){
                ordersInfo.add(ModelsUtils.toModel(order, 0));
            }
        }
             return ordersInfo;
    }


}
