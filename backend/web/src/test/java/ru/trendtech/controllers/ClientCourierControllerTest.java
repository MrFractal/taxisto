package ru.trendtech.controllers;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.Test;
import ru.trendtech.common.mobileexchange.model.common.*;
import ru.trendtech.common.mobileexchange.model.common.web.autocomplete.WebAutoCompleteRequest;
import ru.trendtech.common.mobileexchange.model.common.web.autocomplete.WebAutoCompleteResponse;
import ru.trendtech.common.mobileexchange.model.courier.*;
import ru.trendtech.common.mobileexchange.model.courier.client.*;
import ru.trendtech.common.mobileexchange.model.courier.web.DefaultPriceRequest;
import ru.trendtech.common.mobileexchange.model.courier.web.DefaultPriceResponse;
import ru.trendtech.domain.courier.TypeWindow;
import java.util.ArrayList;
import java.util.List;

import static ru.trendtech.controllers.ProfilesUtils.clientCourierUrl;

/**
 * Created by petr on 26.08.2015.
 */
@ContextConfiguration("classpath:/META-INF/application-context.xml")
@TestExecutionListeners({TransactionalTestExecutionListener.class})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class ClientCourierControllerTest {
    private final RestTemplate template = Utils.template();



    @Test
    public void confirmedOrder() throws Exception {
        ConfirmedOrderRequest request = new ConfirmedOrderRequest();
        request.setSecurity_token("91de600a4bc773b8d3c2cfa490d36cbe");
        request.setRequesterId(24);
        request.setOrderId(4);
        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        ConfirmedOrderResponse response = template.postForObject(clientCourierUrl("/confirmedOrder"), request, ConfirmedOrderResponse.class);
        response.getCustomWindowInfo();
        response.getErrorMessage();
        response.getErrorCode();
    }





    @Test
    public void defaultPrice() throws Exception {
        DefaultPriceRequest request = new DefaultPriceRequest();
        request.setSecurity_token("91de600a4bc773b8d3c2cfa490d36cbe"); // 915863f41b5e4a1f10a79e08b3b2e741
        request.setRequesterId(24);
        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        DefaultPriceResponse response = template.postForObject(clientCourierUrl("/defaultPrice"), request, DefaultPriceResponse.class);
        response.getDefaultPriceInfoList();
        response.getErrorMessage();
        response.getErrorCode();
    }


    @Test
    public void confirmedOrderCard() throws Exception {
        ConfirmedOrderRequest request = new ConfirmedOrderRequest();
        request.setSecurity_token("8edcf02083f791d46db786fedc40a2ec"); // 915863f41b5e4a1f10a79e08b3b2e741
        request.setRequesterId(24);
        request.setOrderId(897);
        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        ConfirmedOrderResponse response = template.postForObject(clientCourierUrl("/confirmedOrderCard"), request, ConfirmedOrderResponse.class);
        response.getCustomWindowInfo();
        response.getErrorMessage();
        response.getErrorCode();
    }



    @Test
    public void threedSecureInitialize() throws Exception {
        ThreedSecureInitializeRequest request = new ThreedSecureInitializeRequest();
        request.setSecurity_token("8edcf02083f791d46db786fedc40a2ec");
        request.setRequesterId(24);
        request.setOrderId(37);
        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        ThreedSecureInitializeResponse response = template.postForObject(clientCourierUrl("/threedSecureInitialize"), request, ThreedSecureInitializeResponse.class);
        response.getErrorMessage();
        response.getErrorCode();
    }





    @Test
    public void repeatDS() throws Exception {
        RepeatThreeDSecureRequest request = new RepeatThreeDSecureRequest();
        request.setSecurity_token("3734ec6eba754a5504cbc33c831fee8e");
        request.setRequesterId(24);
        request.setOrderPaymentId(16);
        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        RepeatThreeDSecureResponse response = template.postForObject(clientCourierUrl("/repeatDS"), request, RepeatThreeDSecureResponse.class);
        response.getRedirectUrl();
        response.getErrorMessage();
        response.getErrorCode();
    }



    @Test
    public void confirmedCustomWindow() throws Exception {
        ConfirmedCustomWindowRequest request = new ConfirmedCustomWindowRequest();
        request.setSecurity_token("3734ec6eba754a5504cbc33c831fee8e");
        request.setRequesterId(24);
        request.setTypeWindow(TypeWindow.ACTIVATION_SERVICE_CONGRATULATION.name());
        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        ConfirmedCustomWindowResponse response = template.postForObject(clientCourierUrl("/confirmedCustomWindow"), request, ConfirmedCustomWindowResponse.class);
        response.getErrorMessage();
        response.getErrorCode();
    }



    @Test
    public void bindingCard() throws Exception {
        BindingCardRequest request = new BindingCardRequest();
        request.setSecurity_token("319a2d58b2362da65db30847ace51f0f"); // 319a2d58b2362da65db30847ace51f0f
        request.setRequesterId(24);
        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        BindingCardResponse response = template.postForObject(clientCourierUrl("/bindingCard"), request, BindingCardResponse.class);
        response.getUrl();
        response.getErrorMessage();
        response.getErrorCode();
    }





    @Test
    public void testCards() throws Exception {
        ClientCardRequest request = new ClientCardRequest();
        request.setSecurity_token("319a2d58b2362da65db30847ace51f0f"); // 319a2d58b2362da65db30847ace51f0f
        request.setRequesterId(24);
        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        ClientCardResponse response = template.postForObject(clientCourierUrl("/card"), request, ClientCardResponse.class);
        response.getCourierClientCardInfos();
        response.getErrorMessage();
        response.getErrorCode();
    }



    @Test
    public void canUseService() throws Exception {
        CanUseServiceRequest request = new CanUseServiceRequest();
        request.setSecurity_token("6ab16e2527ada8b357cef0a154209fdd");
        request.setRequesterId(328);
        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        CanUseServiceResponse response = template.postForObject(clientCourierUrl("/canUseService"), request, CanUseServiceResponse.class);
        response.getCustomWindowInfo();
        response.getErrorMessage();
        response.getErrorCode();
    }



    @Test
    public void orderHistory() throws Exception {
        OrderHistoryRequest request = new OrderHistoryRequest();
        request.setSecurity_token("zzz");
        request.setRequesterId(24);
        request.setCurrent(true);
        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        OrderHistoryResponse response = template.postForObject(clientCourierUrl("/orderHistory"), request, OrderHistoryResponse.class);
        response.getOrderInfos();
        response.getErrorMessage();
        response.getErrorCode();
    }


    @Test
    public void order() throws Exception {
        GetOrderRequest request = new GetOrderRequest();
        request.setSecurity_token("41eeb4383829453f152fe3c4019f0b98");
        request.setRequesterId(292);
        request.setOrderId(382);
        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        GetOrderResponse response = template.postForObject(clientCourierUrl("/order"), request, GetOrderResponse.class);
        response.getOrderInfo();
        response.getErrorMessage();
        response.getErrorCode();
    }



    @Test
    public void orderItemPriceHistory() throws Exception {
        OrderItemPriceHistoryRequest request = new OrderItemPriceHistoryRequest();
        request.setSecurity_token("zzz");
        request.setRequesterId(24);
        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        OrderItemPriceHistoryResponse response = template.postForObject(clientCourierUrl("/orderItemPriceHistory"), request, OrderItemPriceHistoryResponse.class);
        response.getOrderItemPriceInfos();
        response.getErrorMessage();
        response.getErrorCode();
    }





    @Test
    public void webAutoComplete() throws Exception {
        WebAutoCompleteRequest request = new WebAutoCompleteRequest();
        request.setSecurity_token("zzz");
        request.setAddressMask("Военная,5");
        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        WebAutoCompleteResponse response = template.postForObject(clientCourierUrl("/webAutoComplete"), request, WebAutoCompleteResponse.class);
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
        CreateOrderResponse response = template.postForObject(clientCourierUrl("/createOrder"), request, CreateOrderResponse.class);
        response.getErrorMessage();
        response.getErrorCode();
    }






    @Test
    public void addressByGeoPoint() throws Exception {
        AddressByGeoPointRequest request = new AddressByGeoPointRequest();
        request.setRequesterId(24);
        request.setSecurity_token("zzz");
        request.setPoint("82.9008098,55.0360146");
        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        AddressByGeoPointResponse response = template.postForObject(clientCourierUrl("/addressByGeoPoint"), request, AddressByGeoPointResponse.class);
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


    private List<OrderItemPriceInfo> buldOrderItemPriceInfos(){
        List<OrderItemPriceInfo> orderItemPriceInfos = new ArrayList<>();

        OrderItemPriceInfo one = new OrderItemPriceInfo();
        one.setCountItem(1);

        ItemPriceInfo itemPriceInfo = new ItemPriceInfo();
        itemPriceInfo.setId(1L);

        one.setItemPriceInfo(itemPriceInfo);

        orderItemPriceInfos.add(one);

        return orderItemPriceInfos;
    }



    private List<OrderAddressInfo> buildTargetAddressInfo(){
        List<OrderAddressInfo> orderAddressInfos = new ArrayList<>();
        OrderAddressInfo one = new OrderAddressInfo();
        one.setAddress("Саввы Кожевникова,1");
        one.setContactPerson("Petr");
        one.setContactPersonPhone("+79538695889");
        one.setIsToAddress(Boolean.TRUE);
        one.setTo(true);

        System.out.println("one get isToAddress: " + one.isToAddress());

        one.setLatitude(55.047353);
        one.setLongitude(82.938324);

        OrderAddressInfo two = new OrderAddressInfo();
        two.setIsToAddress(true);
        System.out.println("two isToAddress: " + two.isToAddress());
        two.setAddress("Достоевского,58");
        two.setContactPerson("Max");
        two.setContactPersonPhone("+79538695889");

        two.setLatitude(54.984014);
        two.setLongitude(82.872696);
        two.setTo(true);

        orderAddressInfos.add(one);
        orderAddressInfos.add(two);
        return orderAddressInfos;
    }

}
