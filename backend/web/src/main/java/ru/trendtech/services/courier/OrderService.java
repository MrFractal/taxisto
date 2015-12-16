package ru.trendtech.services.courier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.trendtech.common.mobileexchange.model.common.CustomException;
import ru.trendtech.common.mobileexchange.model.courier.CompleteOrderResponse;
import ru.trendtech.common.mobileexchange.model.courier.driver.UpdateTargetAddressStateResponse;
import ru.trendtech.domain.Client;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.DriverLocation;
import ru.trendtech.domain.billing.PaymentType;
import ru.trendtech.domain.courier.*;
import ru.trendtech.repositories.ClientRepository;
import ru.trendtech.repositories.DriverRepository;
import ru.trendtech.repositories.LocationRepository;
import ru.trendtech.repositories.OrderPaymentRepository;
import ru.trendtech.repositories.courier.DefaultPriceRepository;
import ru.trendtech.repositories.courier.OrderAddressRepository;
import ru.trendtech.repositories.courier.OrderRepository;
import ru.trendtech.repositories.courier.OrderStateHistoryRepository;
import ru.trendtech.services.billing.CourierBillingService;
import ru.trendtech.services.common.CommonService;
import ru.trendtech.services.driver.CourierService;
import ru.trendtech.services.notifications.node.NodeJsNotificationsService;
import ru.trendtech.services.notifications.push.App42PushNotificationService;
import ru.trendtech.utils.DateTimeUtils;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by petr on 30.10.2015.
 */
@Service
@Transactional
public class OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private DefaultPriceRepository defaultPriceRepository;
    @Autowired
    private CourierService courierService;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private NodeJsNotificationsService nodeJsNotificationsService;
    @Autowired
    private App42PushNotificationService app42PushNotificationService;
    @Autowired
    private OrderStateHistoryRepository orderStateHistoryRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private OrderAddressRepository orderAddressRepository;
    @Autowired
    private CommonService commonService;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private OrderPaymentRepository orderPaymentRepository;
    @Autowired
    private CourierBillingService courierBillingService;





    private void allTargetAddressInCurrentStateToUnknown(Order order){
        List<OrderAddress> orderAddresses = orderAddressRepository.findByTargetAddressStateAndOrder(TargetAddressState.CURRENT_ADDRESS, order);
            if (!CollectionUtils.isEmpty(orderAddresses)) {
                for (OrderAddress address : orderAddresses) {
                    address.setTargetAddressState(TargetAddressState.UNKNOWN);
                }
                orderAddressRepository.save(orderAddresses);
            }
    }






    private TargetAddressState checkForUpdateTargetAddressState(OrderAddress orderAddress, TargetAddressState newTargetAddressState){
        boolean isToCurrent = newTargetAddressState.equals(TargetAddressState.CURRENT_ADDRESS);
        boolean isTook = newTargetAddressState.equals(TargetAddressState.TOOK);
        boolean isDelivered = newTargetAddressState.equals(TargetAddressState.DELIVERED);
        boolean isToAddress = orderAddress.isTo();

        if(TargetAddressState.UNKNOWN.equals(newTargetAddressState)){
            throw new CustomException(1, "Нельзя перевести адрес в статус UNKNOWN");
        }
        if(isToCurrent) {
            allTargetAddressInCurrentStateToUnknown(orderAddress.getOrder());
        }
        if(isToAddress){
            if(isTook)
                throw new CustomException(2, "Нельзя перевести целевой адрес в статус 'Забрал'");
        } else {
            if(isDelivered)
                throw new CustomException(3, "Нельзя перевести адрес в статус 'Доставил'");
        }
        orderAddress.setTargetAddressState(newTargetAddressState);
        orderAddressRepository.save(orderAddress);
        return newTargetAddressState;
    }




    public UpdateTargetAddressStateResponse updateTargetAddressState(long targetAddressId, int newState){
        UpdateTargetAddressStateResponse response = new UpdateTargetAddressStateResponse();
        OrderAddress orderAddress = orderAddressRepository.findOne(targetAddressId);
        LOGGER.info(String.format("Target address id: %s, orderAddress: %s" , targetAddressId, orderAddress));
        if(orderAddress == null){
            throw new CustomException(1, "Адрес не найден");
        }
        TargetAddressState newTargetAddressState = TargetAddressState.getByValue(newState);
        newTargetAddressState = checkForUpdateTargetAddressState(orderAddress, newTargetAddressState);
        response.setNewTargetAddressState(newTargetAddressState.getValue());
        app42PushNotificationService.clientCustomMessage(orderAddress.getOrder().getClient().getId(), messageForTargetAddressStateChange(newTargetAddressState));
        return response;
    }




    private String messageForTargetAddressStateChange(TargetAddressState state){
        String message = "";
        switch (state){
            case UNKNOWN: {
                break;
            }
            case CURRENT_ADDRESS: {
                message = commonService.getPropertyValue("target_address_state_change_to_current");
                break;
            }
            case TOOK: {
                message = commonService.getPropertyValue("target_address_state_change_to_took");
                break;
            }
            case DELIVERED: {
                message = commonService.getPropertyValue("target_address_state_change_to_delivered");
                break;
            }
            case PROBLEM: {
                message = commonService.getPropertyValue("target_address_state_change_to_problem");
                break;
            }
            default: break;
        }
        return message;
    }





    public void cancelOrderByServer(Order order){
        order.setState(Order.State.CANCELED);
        order.setReasonCancel("Отменен сервером");
        order.setTimeOfCanceling(DateTimeUtils.nowNovosib_GMT6());
        orderRepository.save(order);

        saveChangeState(order);

        Client client = order.getClient();
        client.setOrder(null);
        clientRepository.save(client);
    }



    public void updateOrderState(Order order, Order.State newState){
        Order.State previousState = order.getState();
        order.setPreviousState(previousState);
        order.setState(newState);
        orderRepository.save(order);
        saveChangeState(order);
    }






    public CompleteOrderResponse completeOrderCard(long orderId){
        CompleteOrderResponse response = new CompleteOrderResponse();
        Order order = orderRepository.findOne(orderId);
        if(order == null){
            throw new CustomException(1, "Заказ не найден");
        }
        if(order.getDriver() == null){
            throw new CustomException(2, "На данном заказе нет курьера");
        }
        if(EnumSet.of(Order.State.CANCELED, Order.State.COMPLETED).contains(order.getState())){
            throw new CustomException(3, "Заказ отменен или завершен");
        }

        DefaultPrice defaultPrice = defaultPriceRepository.findByActiveAndOrderType(Boolean.TRUE, order.getOrderType());

        int priceForUs = defaultPrice.getComissionPrice().getAmountMinorInt(); // сумма, которую забираем себе
        int priceToCourier = defaultPrice.getToCourierPrice().getAmountMinorInt();

        LOGGER.info("Доставка: " + order.getPriceDelivery().getAmount());
        LOGGER.info("Стоимость за товар: " + (order.getPriceOfItems() != null ? order.getPriceOfItems().getAmount() : 0));
        LOGGER.info("Общая стоимость: " + order.getPriceInFact().getAmount());

        int deliveryPrice = order.getPriceDelivery().getAmountMinorInt();
        int sumToCourierForAdditionalAddress = 0;
        int ourSumForAdditionalAddress = 0;

        if(!CollectionUtils.isEmpty(order.getTargetAddresses()) && order.getTargetAddresses().size() > 2){
            int countAdditionalAddress = order.getTargetAddresses().size() - 2;
            sumToCourierForAdditionalAddress = defaultPrice.getToCourierForAddressPrice().getAmountMinorInt() * countAdditionalAddress;
            ourSumForAdditionalAddress =  defaultPrice.getCommissionForAddressPrice().getAmountMinorInt() * countAdditionalAddress;
        }
        LOGGER.info("Сумма курьеру за доп адрес: " + sumToCourierForAdditionalAddress);
        LOGGER.info("Наша сумма за доп адрес: " + ourSumForAdditionalAddress);
        int toCourierForKm = deliveryPrice - priceToCourier - priceForUs;
        LOGGER.info("Курьеру: по тарифу = " + priceToCourier + " + за км = " + toCourierForKm + " + за доп адрес = " + sumToCourierForAdditionalAddress);


        Driver driver = order.getDriver();

        if(driver != null){
            /* нет водилы - нет бабла */
            driver.setCurrentOrder(null);
            driverRepository.save(driver);

            DriverLocation driverLocation = locationRepository.findByDriverId(driver.getId());
            driverLocation.setOrder(null);
            locationRepository.save(driverLocation);

            if(order.getPaymentType().equals(PaymentType.CARD)) {
                // картой
                order.setState(Order.State.WAIT_TO_PAYMENT);
                orderRepository.save(order);
                saveChangeState(order);
                courierBillingService.initCompleteHoldByOrder(order);
            }
        }

        List<OrderPayment> orderPayments = orderPaymentRepository.findByOrderAndPaymentState(order, PaymentState.FAILED_PAYMENT);
        if(CollectionUtils.isEmpty(orderPayments)){
            order.setTimeOfFinishingInFact(DateTimeUtils.nowNovosib_GMT6());
            order.setState(Order.State.COMPLETED);
            orderRepository.save(order);
            saveChangeState(order);
            nodeJsNotificationsService.courierOrderFinished(order);
        } else {
            // todo: ИНАЧЕ ЧТО???
        }
        return response;
    }






    public CompleteOrderResponse completeOrder(long orderId){
        CompleteOrderResponse response = new CompleteOrderResponse();
        Order order = orderRepository.findOne(orderId);
        if(order == null){
            throw new CustomException(1, "Заказ не найден");
        }
        if(order.getDriver() == null){
            throw new CustomException(2, "На данном заказе нет курьера");
        }
        if(EnumSet.of(Order.State.CANCELED, Order.State.COMPLETED).contains(order.getState())){
            throw new CustomException(3, "Заказ отменен или завершен");
        }
        //  if(!driver.getCurrentOrder().getId().equals(orderId)){
        //     throw new CustomException(3, "Попытка завершить чужой заказ");
        //  }

        DefaultPrice defaultPrice = defaultPriceRepository.findByActiveAndOrderType(Boolean.TRUE, order.getOrderType());

        int priceForUs = defaultPrice.getComissionPrice().getAmountMinorInt(); // сумма, которую забираем себе
        int priceToCourier = defaultPrice.getToCourierPrice().getAmountMinorInt();

        LOGGER.info("Доставка: " + order.getPriceDelivery().getAmount());
        LOGGER.info("Стоимость за товар: " + (order.getPriceOfItems() != null ? order.getPriceOfItems().getAmount() : 0));
        LOGGER.info("Общая стоимость: " + order.getPriceInFact().getAmount());

        int deliveryPrice = order.getPriceDelivery().getAmountMinorInt();

        int sumToCourierForAdditionalAddress = 0;
        int ourSumForAdditionalAddress = 0;
        if(!CollectionUtils.isEmpty(order.getTargetAddresses()) && order.getTargetAddresses().size() > 2){
            int countAdditionalAddress = order.getTargetAddresses().size() - 2;
            sumToCourierForAdditionalAddress = defaultPrice.getToCourierForAddressPrice().getAmountMinorInt() * countAdditionalAddress;
            ourSumForAdditionalAddress =  defaultPrice.getCommissionForAddressPrice().getAmountMinorInt() * countAdditionalAddress;
        }
        LOGGER.info("Сумма курьеру за доп адрес: " + sumToCourierForAdditionalAddress);
        LOGGER.info("Наша сумма за доп адрес: " + ourSumForAdditionalAddress);

        int toCourierForKm = deliveryPrice - priceToCourier - priceForUs;
        LOGGER.info("Курьеру: по тарифу = " + priceToCourier + " + за км = " + toCourierForKm + " + за доп адрес = " + sumToCourierForAdditionalAddress);


        Driver driver = order.getDriver();

        if(driver != null){
            /* нет водилы - нет бабла */
            driver.setCurrentOrder(null);
            driverRepository.save(driver);

            DriverLocation driverLocation = locationRepository.findByDriverId(driver.getId());
            driverLocation.setOrder(null);
            locationRepository.save(driverLocation);

            if(order.getPaymentType().equals(PaymentType.CASH)){
                // наличкой
                courierService.operationWithDriverCashFlow(driver, order, priceForUs, priceToCourier, toCourierForKm, sumToCourierForAdditionalAddress, ourSumForAdditionalAddress);
            }
        }

        order.setTimeOfFinishingInFact(DateTimeUtils.nowNovosib_GMT6());
        order.setState(Order.State.COMPLETED);
        orderRepository.save(order);

        saveChangeState(order);

        nodeJsNotificationsService.courierOrderFinished(order);
        return response;
    }






    public void saveChangeState(Order order) {
        List<OrderStateHistory> orderStateHistories = orderStateHistoryRepository.findByOrderOrderByTimeOfChangeDesc(order, new PageRequest(0, 1));
        Order.State previousState = null;
        if(!CollectionUtils.isEmpty(orderStateHistories)){
            previousState = orderStateHistories.get(0).getState();
        }
        if(previousState == null || !order.getState().equals(previousState)){
            orderStateHistoryRepository.save(new OrderStateHistory(order, order.getState(), DateTimeUtils.nowNovosib_GMT6()));
        }
    }
}
