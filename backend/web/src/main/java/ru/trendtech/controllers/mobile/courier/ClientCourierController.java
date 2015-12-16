package ru.trendtech.controllers.mobile.courier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.trendtech.common.mobileexchange.model.client.ActivatePromoRequest;
import ru.trendtech.common.mobileexchange.model.client.ActivatePromoResponse;
import ru.trendtech.common.mobileexchange.model.common.*;
import ru.trendtech.common.mobileexchange.model.common.web.autocomplete.WebAutoCompleteRequest;
import ru.trendtech.common.mobileexchange.model.common.web.autocomplete.WebAutoCompleteResponse;
import ru.trendtech.common.mobileexchange.model.courier.client.*;
import ru.trendtech.common.mobileexchange.model.courier.web.DefaultPriceRequest;
import ru.trendtech.common.mobileexchange.model.courier.web.DefaultPriceResponse;
import ru.trendtech.domain.Client;
import ru.trendtech.domain.courier.Order;
import ru.trendtech.domain.courier.OrderType;
import ru.trendtech.repositories.ClientRepository;
import ru.trendtech.repositories.courier.OrderRepository;
import ru.trendtech.services.client.ClientCourierService;
import ru.trendtech.services.common.CommonService;
import ru.trendtech.services.courier.OrderService;
import ru.trendtech.services.promo.PromoCodeCourierService;
import ru.trendtech.services.promo.PromoCodeServiceI;
import ru.trendtech.services.validate.ValidatorService;
import ru.trendtech.utils.HTTPUtil;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by petr on 25.08.2015.
 */
@Controller
@RequestMapping("/client/courier")
public class ClientCourierController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientCourierController.class);
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClientCourierService clientCourierService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ValidatorService validatorService;
    @Autowired
    private OrderService orderService;
    @Qualifier("promoCourier")
    @Autowired
    private PromoCodeServiceI promoCodeCourierService;



    @RequestMapping(value = "/activate/promo", method = RequestMethod.POST)
    public
    @ResponseBody
    ActivatePromoResponse activatePromoCode(@RequestBody ActivatePromoRequest request) {
        if(!validatorService.validateUser(request.getClientId(), request.getSecurity_token(), 1)){
            throw new CustomException(3, "Клиент не найден");
        }
        ActivatePromoResponse response = new ActivatePromoResponse();
        promoCodeCourierService.activatePromo(request.getClientId() , 0, request.getText());
            return response;
    }




    @RequestMapping(value = "/repeatDS", method = RequestMethod.POST)
    @ResponseBody
    public RepeatThreeDSecureResponse repeatDS(@RequestBody RepeatThreeDSecureRequest request) {
          return clientCourierService.repeatThreeDS(request.getOrderPaymentId());
    }



    @RequestMapping(value = "/confirmedOrder", method = RequestMethod.POST)
    @ResponseBody
    public ConfirmedOrderResponse confirmedOrder(@RequestBody ConfirmedOrderRequest request, HttpServletRequest servletRequest) {
        if(!validatorService.validateUser(request.getRequesterId(), request.getSecurity_token(), 1)){
            throw new CustomException(3, "Клиент не найден");
        }
        ru.trendtech.domain.courier.Order order = orderRepository.findOne(request.getOrderId());
        if(order == null){
            throw new CustomException(2, "Заказ не найден");
        }
        if(!order.getState().equals(Order.State.WAIT_TO_CONFIRM)){
            throw new CustomException(1, "Неверный статус заказа");
        }
        if(!CollectionUtils.isEmpty(orderRepository.findByClientAndStateAndIsBookedAndDriverIsNull(clientRepository.findOne(request.getRequesterId()), Order.State.CONFIRMED, Boolean.FALSE))){
            throw new CustomException(4, "Невозможно подвердить заказ т.к. запущен поиск курьера по ранее созданному заказу");
        }
            //if(order.getPaymentType().equals(PaymentType.CASH)){
                return clientCourierService.confirmedOrderWithCash(order, HTTPUtil.resolveIpAddress(servletRequest));
            //} else if(order.getPaymentType().equals(PaymentType.CARD)){
                //return clientCourierService.confirmedOrderWithCash(order, HTTPUtil.resolveIpAddress(servletRequest));
            //} else return null;
    }



    @RequestMapping(value = "/threedSecureInitialize", method = RequestMethod.POST)
    @ResponseBody
    public ThreedSecureInitializeResponse threedSecureInitialize(@RequestBody ThreedSecureInitializeRequest request) {
        if(!validatorService.validateUser(request.getRequesterId(), request.getSecurity_token(), 1)){
            throw new CustomException(3, "Клиент не найден");
        }
            clientCourierService.threedSecureInitialize(request.getOrderId());
            return new ThreedSecureInitializeResponse();
    }












    @RequestMapping(value = "/confirmedOrderCard", method = RequestMethod.POST)
    @ResponseBody
    public ConfirmedOrderResponse confirmedOrderCard(@RequestBody ConfirmedOrderRequest request, HttpServletRequest servletRequest) {
        if(!validatorService.validateUser(request.getRequesterId(), request.getSecurity_token(), 1)){
            throw new CustomException(3, "Клиент не найден");
        }
        ru.trendtech.domain.courier.Order order = orderRepository.findOne(request.getOrderId());
        if(order == null){
            throw new CustomException(2, "Заказ не найден");
        }
        if(!order.getState().equals(Order.State.WAIT_TO_CONFIRM)){
            throw new CustomException(1, "Неверный статус заказа");
        }
        if(!CollectionUtils.isEmpty(orderRepository.findByClientAndStateAndIsBookedAndDriverIsNull(clientRepository.findOne(request.getRequesterId()) , Order.State.CONFIRMED, Boolean.FALSE))){
            throw new CustomException(4, "Невозможно подвердить заказ т.к. запущен поиск курьера по ранее созданному заказу");
        }
            return clientCourierService.confirmedOrderWithCard(order, HTTPUtil.resolveIpAddress(servletRequest));
    }






    @RequestMapping(value = "/bindingCard", method = RequestMethod.POST)
    public
    @ResponseBody
    BindingCardResponse bindingCard(@RequestBody BindingCardRequest request) {
        if(!validatorService.validateUser(request.getRequesterId(), request.getSecurity_token(), 1)){
            throw new CustomException(3, "Клиент не найден");
        }
            return clientCourierService.bindingClientCard(clientRepository.findOne(request.getRequesterId()));
    }




    @RequestMapping(value = "/updateCard", method = RequestMethod.POST)
    public
    @ResponseBody
    DeleteCardResponse updateCard(@RequestBody DeleteCardRequest request) {
        if(!validatorService.validateUser(request.getRequesterId(), request.getSecurity_token(), 1)){
            throw new CustomException(3, "Клиент не найден");
        }
            return clientCourierService.updateCard(request.getRequesterId(), request.getCardId(), request.isDelete(), request.isActive());
    }




    @RequestMapping(value = "/card", method = RequestMethod.POST)
    public
    @ResponseBody
    ClientCardResponse card(@RequestBody ClientCardRequest request) {
        if(!validatorService.validateUser(request.getRequesterId(), request.getSecurity_token(), 1)){
            throw new CustomException(3, "Клиент не найден");
        }
            return clientCourierService.card(request.getRequesterId());
    }




    /* todo: 1. всю бизнес-логику перенести в слой сервиса. 2. interceptor */



    @RequestMapping(value = "/estimateCourier", method = RequestMethod.POST)
    public
    @ResponseBody
    EstimateCourierResponse estimateCourier(@RequestBody EstimateCourierRequest request) {
        if(!validatorService.validateUser(request.getRequesterId(), request.getSecurity_token(), 1)){
            throw new CustomException(3, "Клиент не найден");
        }
        Order order = orderRepository.findOne(request.getOrderId());
        if(order == null){
            throw new CustomException(2, "Заказ не найден");
        }
        int general = request.getGeneral();
        if(general == 0){
            throw new CustomException(1, "Оценка должна быть отличной от нуля");
        }
            return clientCourierService.estimateCourier(order, general);
    }



    @RequestMapping(value = "/orderCancel", method = RequestMethod.POST)
    public
    @ResponseBody
    OrderCancelResponse orderCancel(@RequestBody OrderCancelRequest request) {
        if(!validatorService.validateUser(request.getRequesterId(), request.getSecurity_token(), 1)){
            throw new CustomException(3, "Клиент не найден");
        }
        Client client = clientRepository.findOne(request.getRequesterId());
        Order order = orderRepository.findByIdAndClientAndStateIsNot(request.getOrderId(), client, Order.State.CANCELED);
        if(order == null){
            throw new CustomException(2, "Заказ отменен или не найден");
        }
            return clientCourierService.orderCancel(client, order, request.isApprove());
    }





    @RequestMapping(value = "/confirmedCustomWindow", method = RequestMethod.POST)
    public
    @ResponseBody
    ConfirmedCustomWindowResponse confirmedCustomWindow(@RequestBody ConfirmedCustomWindowRequest request) {
        if(!validatorService.validateUser(request.getRequesterId(), request.getSecurity_token(), 1)){
            throw new CustomException(3, "Клиент не найден");
        }
            return clientCourierService.confirmedCustomWindow(clientRepository.findOne(request.getRequesterId()), request.getTypeWindow());
    }





    @RequestMapping(value = "/configuration", method = RequestMethod.POST)
    public
    @ResponseBody
    CourierClientSystemConfigurationResponse getConfiguration(@RequestBody CourierClientSystemConfigurationRequest request) {
        if(!validatorService.validateUser(request.getRequesterId(), request.getSecurity_token(), 1)){
            throw new CustomException(3, "Клиент не найден");
        }
            return clientCourierService.getConfiguration(clientRepository.findOne(request.getRequesterId()));
    }



    /* запрос на возможность использования сервиса */
    @RequestMapping(value = "/canUseService", method = RequestMethod.POST)
    @ResponseBody
    public CanUseServiceResponse canUseService(@RequestBody CanUseServiceRequest request) {
        if(!validatorService.validateUser(request.getRequesterId(), request.getSecurity_token(), 1)){
            throw new CustomException(3, "Клиент не найден");
        }
         return clientCourierService.canUseService(request.getRequesterId());
    }




    /* сделать запрос на активацию сервиса Zavezu */
    @RequestMapping(value = "/activationCourierService", method = RequestMethod.POST)
    @ResponseBody
    public ActivationCourierServiceResponse activationCourierService(@RequestBody ActivationCourierServiceRequest request) {
        if(!validatorService.validateUser(request.getRequesterId(), request.getSecurity_token(), 1)){
            throw new CustomException(3, "Клиент не найден");
        }
            return clientCourierService.activationCourierService(clientRepository.findOne(request.getRequesterId()));
    }





    @RequestMapping(value = "/createOrder", method = RequestMethod.POST)
    @ResponseBody
    public CreateOrderResponse createOrder(@RequestBody CreateOrderRequest request) {
        LOGGER.info("createOrder: request.getRequesterId() = " +request.getRequesterId() + " token = " + request.getSecurity_token());
//        if(!validatorService.validateUser(request.getRequesterId(), request.getSecurity_token(), 1)){
//            throw new CustomException(3, "Клиент не найден");
//        }
        Client client = clientRepository.findByToken(request.getSecurity_token());
        if(client == null){
            throw new CustomException(3, "Клиент не найден");
        }
        if(client.getOrder() != null){
            throw new CustomException(1, "У вас есть текущий заказ");
        }
        if(request.getOrderInfo().getClientInfo() != null && (request.getOrderInfo().getClientInfo().getId() != client.getId().longValue())){
            throw new CustomException(2, "Несоответствие данных");
        }
        CreateOrderResponse response =  clientCourierService.createOrder(request.getOrderInfo(), client);

        if(response.getOrderId() != 0){
            Order order = orderRepository.findOne(response.getOrderId());
            if(order.getOrderType().equals(OrderType.TAKE_AND_DELIVER)){
                order.setState(Order.State.CONFIRMED);
                orderRepository.save(order);
                orderService.saveChangeState(order);
                clientCourierService.startSearch(order);
            }
        }
             return response;
    }





    @RequestMapping(value = "/orderHistory", method = RequestMethod.POST)
    @ResponseBody
    public OrderHistoryResponse orderHistory(@RequestBody OrderHistoryRequest request) {
        if(!validatorService.validateUser(request.getRequesterId(), request.getSecurity_token(), 1)){
            throw new CustomException(3, "Клиент не найден");
        }
            return clientCourierService.orderHistory(request.isCurrent(), clientRepository.findOne(request.getRequesterId()));
    }





    @RequestMapping(value = "/webAutoComplete", method = RequestMethod.POST)
    @ResponseBody
    public WebAutoCompleteResponse webAutoComplete(@RequestBody WebAutoCompleteRequest request) {
        if(!validatorService.validateUser(request.getRequesterId(), request.getSecurity_token(), 1)){
            throw new CustomException(3, "Клиент не найден");
        }
            return commonService.webAutoComplete(request.getAddressMask());
    }




    @RequestMapping(value = "/addressByGeoPoint", method = RequestMethod.POST)
    @ResponseBody
    public AddressByGeoPointResponse addressByGeoPoint(@RequestBody AddressByGeoPointRequest request) {
        if(!validatorService.validateUser(request.getRequesterId(), request.getSecurity_token(), 1)){
            throw new CustomException(3, "Клиент не найден");
        }
            AddressByGeoPointResponse response = new AddressByGeoPointResponse();
            response.setWebAutocompleteInfo(commonService.getStringAddressByLatLon2GIS(request.getPoint()));
            return response;
    }




    /* todo: запилить позже модели запросов для вовзрата адреса по лат лонам, метод здесь: commonService.getStringAddressByLatLon2GIS("82.944653,55.021355"); */
    @RequestMapping(value = "/geoPointByAddress", method = RequestMethod.POST)
    @ResponseBody
    public GeoPointResponse geoPointByAddress(@RequestBody GeoPointRequest request) {
        if(!validatorService.validateUser(request.getRequesterId(), request.getSecurity_token(), 1)){
            throw new CustomException(3, "Клиент не найден");
        }
            GeoPointResponse response = new GeoPointResponse();
            response.setPoint(commonService.getLatLonByStringAddress2GIS(request.getAddress()));
            return response;
    }




    @RequestMapping(value = "/defaultPrice", method = RequestMethod.POST)
    @ResponseBody
    public DefaultPriceResponse defaultPrice(@RequestBody DefaultPriceRequest request) {
        if(!validatorService.validateUser(request.getRequesterId(), request.getSecurity_token(), 1)){
            throw new CustomException(3, "Клиент не найден");
        }
            return commonService.defaultPrice();
    }





    @RequestMapping(value = "/calculatePrice", method = RequestMethod.POST)
    @ResponseBody
    public CourierCalculatePriceResponse calculatePrice(@RequestBody CourierCalculatePriceRequest request) {
        if(!validatorService.validateUser(request.getRequesterId(), request.getSecurity_token(), 1)){
            throw new CustomException(3, "Клиент не найден");
        }
            return commonService.calculatePrice(request.getOrderInfo());
    }





    @RequestMapping(value = "/order", method = RequestMethod.POST)
    @ResponseBody
    public GetOrderResponse getOrder(@RequestBody GetOrderRequest request) {
        if(!validatorService.validateUser(request.getRequesterId(), request.getSecurity_token(), 1)){
            throw new CustomException(3, "Клиент не найден");
        }
        Order order = orderRepository.findOne(request.getOrderId());
        if(order == null){
            throw new CustomException(2, "Заказ не найден");
        }
        Client client = clientRepository.findOne(request.getRequesterId());
        if(client.getOrder() != null && client.getOrder().getId() != order.getId()){
            throw new CustomException(4, "Нарушение целостности данных");
        }
            return commonService.getOrder(order);
    }




    @RequestMapping(value = "/itemHistory", method = RequestMethod.POST)
    @ResponseBody
    public ItemHistoryResponse itemHistory(@RequestBody ItemHistoryRequest request) {
        if(!validatorService.validateUser(request.getRequesterId(), request.getSecurity_token(), 1)){
            throw new CustomException(3, "Клиент не найден");
        }
            return clientCourierService.itemHistory(request.getRequesterId());
    }






    /* todo: сортировку втулить */
    @RequestMapping(value = "/orderItemPriceHistory", method = RequestMethod.POST)
    @ResponseBody
    public OrderItemPriceHistoryResponse orderItemPriceHistory(@RequestBody OrderItemPriceHistoryRequest request) {
        if(!validatorService.validateUser(request.getRequesterId(), request.getSecurity_token(), 1)){
            throw new CustomException(3, "Клиент не найден");
        }
            return clientCourierService.orderItemPriceHistory(request.getRequesterId());
    }



}
