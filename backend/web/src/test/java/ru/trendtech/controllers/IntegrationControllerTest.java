package ru.trendtech.controllers;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.Test;
import ru.trendtech.common.mobileexchange.model.common.ClientInfo;
import ru.trendtech.common.mobileexchange.model.common.web.autocomplete.WebAutoCompleteRequest;
import ru.trendtech.common.mobileexchange.model.common.web.autocomplete.WebAutoCompleteResponse;
import ru.trendtech.common.mobileexchange.model.courier.*;
import ru.trendtech.common.mobileexchange.model.courier.client.CreateOrderRequest;
import ru.trendtech.common.mobileexchange.model.courier.client.CreateOrderResponse;

import java.util.ArrayList;
import java.util.List;

import static ru.trendtech.controllers.ProfilesUtils.clientCourierUrl;
import static ru.trendtech.controllers.ProfilesUtils.integrationCourierUrl;

/**
 * Created by petr on 13.10.2015.
 */
public class IntegrationControllerTest {
    private final RestTemplate template = Utils.template();



    @Test
    public void webAutoComplete() throws Exception {
        WebAutoCompleteRequest request = new WebAutoCompleteRequest();
        request.setSecurity_token("zzz");
        request.setAddressMask("берез");
        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        WebAutoCompleteResponse response = template.postForObject(integrationCourierUrl("/webAutoComplete"), request, WebAutoCompleteResponse.class);
        response.getWebAutocompleteInfos();
        response.getErrorMessage();
        response.getErrorCode();
    }


    @Test
    public void createOrder() throws Exception {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setRequesterId(24);
        request.setSecurity_token("zzz");
        request.setOrderInfo(buildOrderInfo());
        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        CreateOrderResponse response = template.postForObject(integrationCourierUrl("/createOrder"), request, CreateOrderResponse.class);
        response.getErrorMessage();
        response.getErrorCode();
    }


    private OrderInfo buildOrderInfo(){
        OrderInfo orderInfo = new OrderInfo();
        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setId(24);
        orderInfo.setClientInfo(clientInfo);
        CommentInfo commentInfo = new CommentInfo();
        commentInfo.setComment("Hello world!");
        orderInfo.setCommentInfo(commentInfo);
        orderInfo.setOrderType(2);
        orderInfo.setTimeOfFinishing("2015-08-26 23:30:12");
        orderInfo.setTargetAddressesInfo(buildTargetAddressInfo());
        orderInfo.setClientItemInfos(buildClientItemInfo());
        return orderInfo;
    }



    private List<OrderAddressInfo> buildTargetAddressInfo(){
        List<OrderAddressInfo> orderAddressInfos = new ArrayList<>();
        OrderAddressInfo one = new OrderAddressInfo();
        one.setAddress("Саввы Кожевникова,1");
        one.setContactPerson("Petr");
        one.setContactPersonPhone("+79538695889");

        one.setLatitude(55.047353);
        one.setLongitude(82.938324);

        OrderAddressInfo two = new OrderAddressInfo();
        two.setAddress("Достоевского,58");
        two.setContactPerson("Max");
        two.setContactPersonPhone("+79538695889");

        two.setLatitude(54.984014);
        two.setLongitude(82.872696);

        orderAddressInfos.add(one);
        orderAddressInfos.add(two);
        return orderAddressInfos;
    }



    private List<ClientItemInfo> buildClientItemInfo(){
        List<ClientItemInfo> clientItemInfos = new ArrayList<>();
        ClientItemInfo one = new ClientItemInfo();
        one.setCountItem(2);

        ItemInfo firstItemInfo = new ItemInfo();
        firstItemInfo.setId(1L);
        one.setItemInfo(firstItemInfo);

        ClientItemInfo second = new ClientItemInfo();
        ItemInfo secondItemInfo = new ItemInfo();
        secondItemInfo.setId(0L);
        secondItemInfo.setItemName("хочу в баню!!!");

        second.setItemInfo(secondItemInfo);

        clientItemInfos.add(one);
        clientItemInfos.add(second);

        return clientItemInfos;
    }
}
