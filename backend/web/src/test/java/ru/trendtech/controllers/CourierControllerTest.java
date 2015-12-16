package ru.trendtech.controllers;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.testng.annotations.Test;
import ru.trendtech.common.mobileexchange.model.courier.CompleteOrderRequest;
import ru.trendtech.common.mobileexchange.model.courier.CompleteOrderResponse;
import ru.trendtech.common.mobileexchange.model.courier.client.OrderHistoryRequest;
import ru.trendtech.common.mobileexchange.model.courier.client.OrderHistoryResponse;
import ru.trendtech.common.mobileexchange.model.courier.driver.*;

import static ru.trendtech.controllers.ProfilesUtils.courierUrl;

/**
 * Created by petr on 22.09.2015.
 */

public class CourierControllerTest {



    @Test
    public void prepaireComplete() throws Exception {
        PrepaireCompleteOrderRequest request = new PrepaireCompleteOrderRequest();
        request.setRequesterId(40);
        request.setOrderId(335);
        request.setSecurity_token("20525e991d9bd0775eb36366d3f118da");
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        PrepaireCompleteOrderResponse response = Utils.template().postForObject(courierUrl("/prepaireComplete"), request, PrepaireCompleteOrderResponse.class);
        response.getErrorMessage();
        response.getErrorCode();
    }



    @Test
    public void completeOrder() throws Exception {
        CompleteOrderRequest request = new CompleteOrderRequest();
        request.setRequesterId(8);
        request.setOrderId(41);
        request.setSecurity_token("xxx");
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        CompleteOrderResponse response = Utils.template().postForObject(courierUrl("/completeOrder"), request, CompleteOrderResponse.class);
        response.getErrorMessage();
        response.getErrorCode();
    }




    @Test
    public void orderHistory() throws Exception {
        OrderHistoryRequest request = new OrderHistoryRequest();
        request.setRequesterId(8);
        request.setSecurity_token("00d66449245360b99efbc08281614988");
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        OrderHistoryResponse response = Utils.template().postForObject(courierUrl("/orderHistory"), request, OrderHistoryResponse.class);
        response.getOrderInfos();
    }





    @Test
    public void readyToGo() throws Exception {
        ReadyToGoRequest request = new ReadyToGoRequest();
        request.setRequesterId(8);
        request.setSecurity_token("520976e7aa96fadb9e681763e74cbb7a");
        request.setOrderId(379);
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        ReadyToGoResponse response = Utils.template().postForObject(courierUrl("/readyToGo"), request, ReadyToGoResponse.class);
        response.getArrivalTime();
        response.getErrorMessage();
        response.getErrorCode();
    }


    @Test
    public void readyToProgress() throws Exception {
        ReadyToProgressRequest request = new ReadyToProgressRequest();
        request.setRequesterId(8);
        request.setSecurity_token("520976e7aa96fadb9e681763e74cbb7a");
        request.setOrderId(379);
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        ReadyToProgressResponse response = Utils.template().postForObject(courierUrl("/readyToProgress"), request, ReadyToProgressResponse.class);
        response.getArrivalTime();
        response.getErrorMessage();
        response.getErrorCode();
    }



    @Test
    public void takeOrder() throws Exception {
        TakeOrderRequest request = new TakeOrderRequest();
        request.setRequesterId(8);
        request.setSecurity_token("00d66449245360b99efbc08281614988");
        request.setOrderId(20);
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        TakeOrderResponse response = Utils.template().postForObject(courierUrl("/takeOrder"), request, TakeOrderResponse.class);
        response.getErrorMessage();
        response.getErrorCode();
    }




    @Test
    public void lateOrder() throws Exception {
        LateOrderRequest request = new LateOrderRequest();
        request.setRequesterId(8);
        request.setSecurity_token("520976e7aa96fadb9e681763e74cbb7a");
        request.setMinutesLate(30);
        request.setOrderId(432);
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        LateOrderResponse response = Utils.template().postForObject(courierUrl("/lateOrder"), request, LateOrderResponse.class);
        response.getErrorMessage();
        response.getErrorCode();
    }



}
