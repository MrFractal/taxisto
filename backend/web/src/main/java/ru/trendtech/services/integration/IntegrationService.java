package ru.trendtech.services.integration;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.trendtech.common.mobileexchange.model.common.CourierCalculatePriceResponse;
import ru.trendtech.common.mobileexchange.model.common.CustomException;
import ru.trendtech.common.mobileexchange.model.courier.ClientItemInfo;
import ru.trendtech.common.mobileexchange.model.courier.OrderAddressInfo;
import ru.trendtech.common.mobileexchange.model.courier.OrderInfo;
import ru.trendtech.common.mobileexchange.model.courier.client.CreateOrderResponse;
import ru.trendtech.domain.Client;
import ru.trendtech.domain.billing.PaymentType;
import ru.trendtech.domain.courier.*;
import ru.trendtech.models.ModelsUtils;
import ru.trendtech.repositories.ClientRepository;
import ru.trendtech.repositories.courier.*;
import ru.trendtech.services.common.CommonService;
import ru.trendtech.services.courier.OrderService;
import ru.trendtech.services.notifications.node.NodeJsNotificationsService;
import ru.trendtech.utils.DateTimeUtils;
import ru.trendtech.utils.MoneyUtils;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by petr on 13.10.2015.
 */
@Service
@Transactional
public class IntegrationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IntegrationService.class);
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderAddressRepository orderAddressRepository;
    @Autowired
    private CustomWindowRepository customWindowRepository;
    @Autowired
    private CommonService commonService;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ClientItemRepository clientItemRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private NodeJsNotificationsService nodeJsNotificationsService;
    @Autowired
    private OrderService orderService;



    public CreateOrderResponse createOrder(OrderInfo orderInfo, Client client) {
        CreateOrderResponse response = new CreateOrderResponse();
        List<Order> currentOrder = orderRepository.findByClientAndStateAndIsBookedAndDriverIsNull(client, Order.State.CONFIRMED, Boolean.FALSE);

        if(!CollectionUtils.isEmpty(currentOrder)) {
            throw new CustomException(1, "У вас есть текущий заказ находящийся в поиске курьера");
        }

        String timeOfFinishingStr = orderInfo.getTimeOfFinishing();
        DateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateTime timeOfFinishing;
        try {
            Date date = form.parse(timeOfFinishingStr);
            timeOfFinishing = DateTimeUtils.toDateTime(date.getTime());
        } catch (ParseException e) {
            throw new CustomException(2, "Неверный формат времени");
        }

        if(commonService.checkAlcohol(orderInfo, timeOfFinishing)){
            response.setCustomWindowInfo(ModelsUtils.toModel(customWindowRepository.findByTypeWindow(TypeWindow.ALCOHOL_STOP)));
            return response;
        }

        Order order = new Order();
        // todo: позже добавить карты и т.д.
        order.setPaymentType(PaymentType.CASH);
        order.setState(Order.State.CONFIRMED);
        order.setOrderType(OrderType.TAKE_AND_DELIVER);
        order.setIsBooked(orderInfo.isBooked());
        order.setTimeOfFinishing(timeOfFinishing);
        order.setTimeOfRequesting(DateTimeUtils.nowNovosib_GMT6());

        order.setClient(client);
        orderRepository.save(order);

        orderService.saveChangeState(order);

        // адреса откуда, куда привезти
        List<OrderAddressInfo> orderAddressInfos = orderInfo.getTargetAddressesInfo();
        List<String> targetAddress = new ArrayList<>();
        int orderNumber = 1;
        if (!CollectionUtils.isEmpty(orderAddressInfos)) {
            for (OrderAddressInfo info : orderAddressInfos) {
                OrderAddress orderAddress = ModelsUtils.fromModel(info, order, null, orderNumber);
                orderAddressRepository.save(orderAddress);
                order.getTargetAddresses().add(orderAddress);
                targetAddress.add(info.getAddress());
                orderNumber++;
            }
        }
        orderNumber = 1;
        // список того, что натыкал пользователь (до того обработки заказа диспетчером)
        List<ClientItemInfo> clientItemInfos = orderInfo.getClientItemInfos();
        if (!CollectionUtils.isEmpty(clientItemInfos)) {
            for(ClientItemInfo clientItemInfo : clientItemInfos){
                if(clientItemInfo.getItemInfo() != null) {
                    Item itemSelected = itemRepository.findOne(clientItemInfo.getItemInfo().getId());
                    ClientItem clientItem = ModelsUtils.fromModel(clientItemInfo, order, itemSelected, orderNumber);
                    clientItemRepository.save(clientItem);
                    order.getClientItems().add(clientItem);
                    orderNumber++;
                }
            }
        }

        if(orderInfo.getCommentInfo() != null) {
            Comment comment = ModelsUtils.fromModel(orderInfo.getCommentInfo(), order);
            commentRepository.save(comment);
        }

        clientRepository.save(client);

        order.setState(Order.State.CONFIRMED);
        orderRepository.save(order);

        orderService.saveChangeState(order);

        int percentInsurance = Integer.parseInt(commonService.getPropertyValue("percent_insurance"));
        order.setPercentInsuranceOnDayOfOrder(percentInsurance);

        int distanceExpected = commonService.calculateDistanceWithTimeDurationGeneral(orderInfo).getDistance();
        order.setDistanceExpected(distanceExpected);

        CourierCalculatePriceResponse calculatePriceResponse = commonService.calculatePrice(orderInfo);
        order.setPriceOfInsurance(MoneyUtils.getMoney(calculatePriceResponse.getPriceOfInsurance() / 100));
        order.setPriceDelivery(MoneyUtils.getMoney(calculatePriceResponse.getPriceDelivery() / 100));
        order.setPriceExpected(MoneyUtils.getMoney(calculatePriceResponse.getCommonPrice() / 100));
        order.setPriceInFact(MoneyUtils.getMoney(calculatePriceResponse.getCommonPrice() / 100));
        order.setPriceOfItems(MoneyUtils.getMoney(calculatePriceResponse.getPriceItems() / 100));
        order.setPriceOfPercentInsurance(MoneyUtils.getMoney(calculatePriceResponse.getPriceByPercentInsurance() / 100));
        order = orderRepository.save(order);

        nodeJsNotificationsService.courierNewOrderDispatcher(order);
        response.setOrderId(order.getId());
        return response;
    }
}
