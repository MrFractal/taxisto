package ru.trendtech.controllers.courier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.trendtech.common.mobileexchange.model.common.*;
import ru.trendtech.common.mobileexchange.model.common.web.autocomplete.WebAutoCompleteRequest;
import ru.trendtech.common.mobileexchange.model.common.web.autocomplete.WebAutoCompleteResponse;
import ru.trendtech.common.mobileexchange.model.courier.CompleteOrderRequest;
import ru.trendtech.common.mobileexchange.model.courier.CompleteOrderResponse;
import ru.trendtech.common.mobileexchange.model.courier.web.*;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.admin.WebUser;
import ru.trendtech.domain.courier.Order;
import ru.trendtech.domain.courier.OrderType;
import ru.trendtech.repositories.WebUserRepository;
import ru.trendtech.repositories.courier.OrderRepository;
import ru.trendtech.services.administration.AdminCurierService;
import ru.trendtech.services.common.CommonService;
import ru.trendtech.services.courier.OrderService;

import java.util.EnumSet;

/**
 * Created by petr on 25.08.2015.
 */

@Controller
@RequestMapping("/admin/courier")
public class AdminCourierController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminCourierController.class);
    private static final String IN_PROGRESS_BY_OPERATOR = "IN_PROGRESS_BY_OPERATOR";
    private static final String WAIT_TO_CONFIRM = "WAIT_TO_CONFIRM";
    @Autowired
    private AdminCurierService adminCurierService;
    @Autowired
    private WebUserRepository webUserRepository;
    @Autowired
    private CommonService commonService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderService orderService;




    @RequestMapping(value = "/completeOrder", method = RequestMethod.POST)
    public
    @ResponseBody
    CompleteOrderResponse completeOrder(@RequestBody CompleteOrderRequest request) {
        WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
        if(webUser == null) {
            throw new CustomException(1, "Диспетчер не найден");
        }
            return orderService.completeOrder(request.getOrderId());
    }




    @RequestMapping(value = "/orderCancel", method = RequestMethod.POST)
    public
    @ResponseBody OrderCancelByOperatorResponse orderCancel(@RequestBody OrderCancelByOperatorRequest request) {
        WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
        if(webUser == null) {
            throw new CustomException(1, "Диспетчер не найден");
        }
        Order order = orderRepository.findOne(request.getOrderId());
        if(order == null){
            throw new CustomException(2, "Заказ отменен или не найден");
        }
            return adminCurierService.orderCancel(order, request.getReason());
    }




    @RequestMapping(value = "/updateOrder", method = RequestMethod.POST)
    @ResponseBody
    public UpdateOrderResponse updateOrder(@RequestBody UpdateOrderRequest request) {
        WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
        if(webUser == null) {
            throw new CustomException(1, "Диспетчер не найден");
        }
            return adminCurierService.updateOrder(request.getOrderInfo());
    }




    @RequestMapping(value = "/updatePrice", method = RequestMethod.POST)
    @ResponseBody
    public UpdatePriceResponse updatePrice(@RequestBody UpdatePriceRequest request) {
        WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
        if(webUser == null) {
            throw new CustomException(1, "Диспетчер не найден");
        }
            adminCurierService.updatePrice(request.getOrderId(), request.getPriceItemsInFact());
            return new UpdatePriceResponse();
    }




    @RequestMapping(value = "/updateOrderState", method = RequestMethod.POST)
    @ResponseBody
    public UpdateOrderStateResponse startOrderProcessing(@RequestBody UpdateOrderStateRequest request) {
        WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
        if(webUser == null) {
            throw new CustomException(1, "Диспетчер не найден");
        }
        Order.State newState = Order.State.valueOf(request.getState());
        ru.trendtech.domain.courier.Order order = orderRepository.findOne(request.getOrderId());
        if(order == null || order.getOrderType().equals(OrderType.TAKE_AND_DELIVER)){
            throw new CustomException(2, "Заказ не найден или 'ЗАБРАТЬ И ПРИВЕЗТИ'");
        }
        if(EnumSet.of(Order.State.CANCELED, Order.State.COMPLETED).contains(order.getState())){
            throw new CustomException(3, "Заказ отменен или завершен");
        }
        if(order.getState().equals(Order.State.CONFIRMED)) {
            throw new CustomException(4, "Заказ находится в режиме поиска курьера. Изменение статуса запрещено!");
        }


        if(!order.getState().equals(Order.State.NEW) && newState.equals(Order.State.IN_PROGRESS_BY_OPERATOR)) {
            // если новый статус "В ОБРАБОТКУ" и текущий статус не NEW - иди лесом
            throw new CustomException(6, "Нельзя повторно отправить заказ 'в обработку'");
        }

        /*
        todo: с этим не понятно!!
        if(!order.getState().equals(Order.State.IN_PROGRESS_BY_OPERATOR) && newState.equals(Order.State.WAIT_TO_CONFIRM)) {
            // если новый статус "В ОБРАБОТКУ" и текущий статус не NEW - иди лесом
            throw new CustomException(6, "Невозможно отправить заказ 'на подтверждение' - ");
        }
        */


        //if(order.getWebUser()!=null || order.getState().equals(Order.State.IN_PROGRESS_BY_OPERATOR)){
           // throw new CustomException(3, "Заказ находится в обработке");
        //}

        Order.State previousState = order.getState();


        if(previousState.equals(newState)) {
            throw new CustomException(5, String.format("Заказ уже находится в статусе %s", previousState.name()));
        }

        UpdateOrderStateResponse response = new UpdateOrderStateResponse();

        switch (request.getState()){
            case IN_PROGRESS_BY_OPERATOR :{
                break;
            }
            case WAIT_TO_CONFIRM: {
                if(CollectionUtils.isEmpty(order.getOrderItemPrices())){
                    throw new CustomException(4, "В заказе отсутствуют товары");
                }
                break;
            }
            default: {
                throw new CustomException(5, "Недопустимое состояние");
            }
        }
           orderService.updateOrderState(order, newState);
           commonService.notifiedOrderStateChangeByOperator(order, newState);
           return response;
    }


                /*
                List<OrderPayment> orderPayments = orderPaymentRepository.findByOrder(order);
                boolean ok = true;
                if(!CollectionUtils.isEmpty(orderPayments)){
                    for(OrderPayment orderPayment: orderPayments){
                         if(EnumSet.of(PaymentState.WAIT_TO_HOLD, PaymentState.FAILED_HOLD, PaymentState.FAILED_PAYMENT, PaymentState.REQUIRED_THREED_SECURE).contains(orderPayment.getPaymentState())) {
                              ok = false;
                         }
                    }
                }

                if(!ok){
                    throw new CustomException(4, "По данному заказу есть неподтвержденные банковские операции.");
                }
                state = Order.State.valueOf(request.getState());
                */






    /* todo: сделать один метод для редактирования стейта заказа
    @RequestMapping(value = "/startOrderProcessing", method = RequestMethod.POST)
    @ResponseBody
    public StartOrderProcessingResponse startOrderProcessing(@RequestBody StartOrderProcessingRequest request) {
        WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
        if(webUser == null) {
            throw new CustomException(1, "Диспетчер не найден");
        }
        ru.trendtech.domain.courier.Order order = orderRepository.findOne(request.getOrderId());
        if(order == null){
            throw new CustomException(2, "Заказ не найден");
        }
        if(order.getWebUser()!=null || order.getState().equals(Order.State.IN_PROGRESS_BY_OPERATOR)){
            throw new CustomException(3, "Заказ находится в обработке");
        }
            return adminCurierService.startOrderProcessing(order, webUser);
    }
    */






    @RequestMapping(value = "/orderTransfer", method = RequestMethod.POST)
    @ResponseBody
    public OrderTransferResponse orderTransfer(@RequestBody OrderTransferRequest request) {
        WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
        if(webUser == null) {
            throw new CustomException(1, "Диспетчер не найден");
        }
        return adminCurierService.orderTransfer(request.getOrderId(), request.getToCourierId(), webUser.getId());
    }





    // Методы возврата заказов по торг. точке/товару/услуги
    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    @ResponseBody
    public OrdersResponse orders(@RequestBody OrdersRequest request) {
        WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
        if(webUser == null) {
            throw new CustomException(1, "Диспетчер не найден");
        }
            return adminCurierService.orders(request.getOrderId(), request.getClientId(), request.getDriverId(), request.getState(), request.getItemId(), request.getStoreId(), request.getStartTime(), request.getEndTime(), request.getNumberPage(), request.getSizePage(), request.getTypeOrder());
    }



    // calculate price
    @RequestMapping(value = "/calculatePrice", method = RequestMethod.POST)
    @ResponseBody
    public CourierCalculatePriceResponse calculatePrice(@RequestBody CourierCalculatePriceRequest request) {
        WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
        if(webUser == null) {
            throw new CustomException(1, "Диспетчер не найден");
        }
            return commonService.calculatePrice(request.getOrderInfo());
    }



    @RequestMapping(value = "/activationQueue", method = RequestMethod.POST)
    @ResponseBody
    public ActivationQueueResponse activationQueue(@RequestBody ActivationQueueRequest request) {
        WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
        if(webUser == null) {
            throw new CustomException(1, "Диспетчер не найден");
        }
            return adminCurierService.activationQueue(request.getClientId(), request.getStartTime(), request.getEndTime(), request.getActivated(), request.getNumberPage(), request.getSizePage());
    }



    /* активация сервиса Zavezu */
    @RequestMapping(value = "/activateCourierService", method = RequestMethod.POST)
    @ResponseBody
    public ActivateCourierServiceResponse activateCourierService(@RequestBody ActivateCourierServiceRequest request) {
        WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
        if(webUser == null) {
            throw new CustomException(1, "Диспетчер не найден");
        }
            return adminCurierService.activateCourierService(request.getClientIds());
    }



    @RequestMapping(value = "/webAutoComplete", method = RequestMethod.POST)
    @ResponseBody
    public WebAutoCompleteResponse webAutoComplete(@RequestBody WebAutoCompleteRequest request) {
        WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
        if(webUser == null) {
            throw new CustomException(1, "Диспетчер не найден");
        }
            return commonService.webAutoComplete(request.getAddressMask());
    }



    @RequestMapping(value = "/geoPointByAddress", method = RequestMethod.POST)
    @ResponseBody
    public GeoPointResponse webAutoComplete(@RequestBody GeoPointRequest request) {
        WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
        if(webUser == null) {
            throw new CustomException(1, "Диспетчер не найден");
        }
        GeoPointResponse response = new GeoPointResponse();
        response.setPoint(commonService.getLatLonByStringAddress2GIS(request.getAddress()));
        return response;
    }



    @RequestMapping(value = "/itemPrices", method = RequestMethod.POST)
    @ResponseBody
    public ItemPriceResponse itemPrices(@RequestBody ItemPriceRequest request) {
        WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
        if(webUser == null) {
            throw new CustomException(1, "Диспетчер не найден");
        }
            return adminCurierService.itemPrices(request.getItemId(), request.getStoreId(), request.getOrderId(), request.getNumberPage(), request.getSizePage());
    }




    @RequestMapping(value = "/updateItemPrices", method = RequestMethod.POST)
    @ResponseBody
    public UpdateItemPriceResponse updateItemPrices(@RequestBody UpdateItemPriceRequest request) {
        WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
        if(webUser == null) {
            throw new CustomException(1, "Диспетчер не найден");
        }
            return adminCurierService.updateItemPrices(request.getItemPriceInfoList());
    }




    /*  адреса магазинов  */
    @RequestMapping(value = "/updateStoreAddress", method = RequestMethod.POST)
    @ResponseBody
    public UpdateStoreAddressResponse updateStoreAddress(@RequestBody UpdateStoreAddressRequest request) {
        WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
        if(webUser == null) {
            throw new CustomException(1, "Диспетчер не найден");
        }
            return adminCurierService.updateStoreAddress(request.getStoreAddressInfos());
    }



    @RequestMapping(value = "/storeAddresses", method = RequestMethod.POST)
    @ResponseBody
    public StoreAddressResponse storeAddresses(@RequestBody StoreAddressRequest request) {
        WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
        if(webUser == null) {
            throw new CustomException(1, "Диспетчер не найден");
        }
            return adminCurierService.storeAddress(request.getStoreId(), request.getStoreAddressId(), request.getNumberPage(), request.getSizePage(), request.getOrderId());
    }
    /*  ---  */




    /*  товары/услуги  */
    @RequestMapping(value = "/updateItem", method = RequestMethod.POST)
     @ResponseBody
     public UpdateItemResponse updateItem(@RequestBody UpdateItemRequest request) {
        WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
        if(webUser == null) {
            throw new CustomException(1, "Диспетчер не найден");
        }
            return adminCurierService.updateItem(request.getItemInfoList());
    }



    @RequestMapping(value = "/items", method = RequestMethod.POST)
    @ResponseBody
    public ItemResponse items(@RequestBody ItemRequest request) {
        WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
        if(webUser == null) {
            throw new CustomException(1, "Диспетчер не найден");
        }
            return adminCurierService.items(request.getItemId(), request.getMask(), request.getItemType(), request.getNumberPage(), request.getSizePage());
    }
    /*  ---  */




    /*  магазины  */
    @RequestMapping(value = "/updateStore", method = RequestMethod.POST)
    @ResponseBody
    public UpdateStoreResponse updateStore(@RequestBody UpdateStoreRequest request) {
        WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
        if(webUser == null) {
            throw new CustomException(1, "Диспетчер не найден");
        }
        return adminCurierService.updateStore(request.getStoreInfoList());
    }



    @RequestMapping(value = "/stores", method = RequestMethod.POST)
    @ResponseBody
    public StoreResponse stores(@RequestBody StoreRequest request) {
        WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
        if(webUser == null) {
            throw new CustomException(1, "Диспетчер не найден");
        }
            return adminCurierService.stores(request.getStoreId(), request.getMask(), request.getNumberPage(), request.getSizePage());
    }

    /*  ---  */


}
