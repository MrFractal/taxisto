package ru.trendtech.controllers.mobile.courier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.trendtech.common.mobileexchange.model.common.CourierCalculatePriceRequest;
import ru.trendtech.common.mobileexchange.model.common.CourierCalculatePriceResponse;
import ru.trendtech.common.mobileexchange.model.common.CustomException;
import ru.trendtech.common.mobileexchange.model.common.web.autocomplete.WebAutoCompleteRequest;
import ru.trendtech.common.mobileexchange.model.common.web.autocomplete.WebAutoCompleteResponse;
import ru.trendtech.common.mobileexchange.model.courier.client.CreateOrderRequest;
import ru.trendtech.common.mobileexchange.model.courier.client.CreateOrderResponse;
import ru.trendtech.domain.Client;
import ru.trendtech.domain.admin.WebUser;
import ru.trendtech.domain.courier.Order;
import ru.trendtech.domain.courier.OrderType;
import ru.trendtech.repositories.ClientRepository;
import ru.trendtech.repositories.courier.OrderRepository;
import ru.trendtech.services.client.ClientCourierService;
import ru.trendtech.services.common.CommonService;
import ru.trendtech.services.integration.IntegrationService;

/**
 * Created by petr on 13.10.2015.
 */

@Controller
@RequestMapping("/courier/integration")
public class IntegrationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(IntegrationController.class);
    @Autowired
    private IntegrationService integrationService;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClientCourierService clientCourierService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CommonService commonService;



    @RequestMapping(value = "/createOrder", method = RequestMethod.POST)
    @ResponseBody
    public CreateOrderResponse createOrder(@RequestBody CreateOrderRequest request) {
        Client client = clientRepository.findByToken(request.getSecurity_token());
        if(client == null){
            throw new CustomException(1, "Клиент не найден");
        }
        CreateOrderResponse response =  integrationService.createOrder(request.getOrderInfo(), client);
        if(response.getOrderId() != 0){
            Order order = orderRepository.findOne(response.getOrderId());
            clientCourierService.startSearch(order);
        }
        return response;
    }


    @RequestMapping(value = "/webAutoComplete", method = RequestMethod.POST)
    @ResponseBody
    public WebAutoCompleteResponse webAutoComplete(@RequestBody WebAutoCompleteRequest request) {
        return commonService.webAutoComplete(request.getAddressMask());
    }


    @RequestMapping(value = "/calculatePrice", method = RequestMethod.POST)
    @ResponseBody
    public CourierCalculatePriceResponse calculatePrice(@RequestBody CourierCalculatePriceRequest request) {
        Client client = clientRepository.findByToken(request.getSecurity_token());
        if(client == null) {
            throw new CustomException(1, "Клиент не найден");
        }
            return commonService.calculatePrice(request.getOrderInfo());
    }

}
