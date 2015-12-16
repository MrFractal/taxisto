package ru.trendtech.controllers;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.trendtech.common.mobileexchange.model.client.RegistrationPhoneChangeRequest;
import ru.trendtech.common.mobileexchange.model.client.RegistrationPhoneChangeResponse;
import ru.trendtech.common.mobileexchange.model.common.StringsListResponse;
import ru.trendtech.common.mobileexchange.model.common.billing.paymobile.*;
import ru.trendtech.utils.PhoneUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static ru.trendtech.controllers.ProfilesUtils.clientUrl;
import static ru.trendtech.controllers.ProfilesUtils.paymentUrl;

/**
 * Created by petr on 17.09.2014.
 */
@ContextConfiguration("classpath:/META-INF/application-context.xml")
@TestExecutionListeners({TransactionalTestExecutionListener.class})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class BillingControllerTest {

private final RestTemplate template = Utils.template();


    @BeforeMethod
    public void forceSetProfile() throws Exception {
        ProfilesUtils.setProfileForce("max");
    }




    @Test
    public void testGetIdCard() throws Exception {
        GetIdCardRequest request = new GetIdCardRequest();
        request.setClientId(14);
        GetIdCardResponse response = template.postForObject(paymentUrl("/getIdCard"), request, GetIdCardResponse.class);
        long idCard = response.getIdCard();
        String idCardStr = response.getIdCardStr();
    }


    @Test
    public void testGetSuccess() throws Exception {
        //TestRequest request = new TestRequest();
        //request.setAttribute("key", "fractal");
        StringsListResponse forObject = template.getForObject(paymentUrl("/payonline/success"), StringsListResponse.class);
        List<String> values = forObject.getValues();
        values.size();
    }




    @Test
    public void testGetFail() throws Exception {
        StringsListResponse forObject = template.getForObject(paymentUrl("/payonline/fail"), StringsListResponse.class);
        List<String> values = forObject.getValues();
        values.size();
    }




    @Test
    public void testStoreCardData() throws Exception {
      StoreCardDataRequest request = new StoreCardDataRequest();
      request.setSecurity_token("zzz");
      request.setActive(true);
      request.setBindingId("dd9c7912-b54b-49fsdsdsdsdsd");
      request.setCardId(0L);
      request.setCardholderName("Fractal");
      request.setExpirationMonth(12);
      request.setExpirationYear(2015);
      request.setMdOrder("5bb31b7a-72bfsdsdsdsd2138b0");
      request.setMrchOrder("card_1141102632444");
      request.setPan("411111**1111");
      request.setClientId(12L);
      StoreCardDataResponse response = template.postForObject(paymentUrl("/storeCardData"), request, StoreCardDataResponse.class);
      response.isResult();
    }



    /*
    active = 1;
    bindingId = "dd9c7912-b54b-49fe-aa8e-94012728e202";
    cardId = 0;
    cardholderName = "Fed s";
    clientId = 1;
    expirationMonth = 12;
    expirationYear = 2015;
    mdOrder = "5bb31b7a-72bf-4323-9f7c-17f1db2138b0";
    mrchOrder = "card_1141102632444";
    pan = "411111**1111";
     */



    @Test
    public void testRegisterOrderClient() throws Exception {
        RegisterOrderClientRequest request = new RegisterOrderClientRequest();
         request.setClientId(1);
         request.setSecurity_token("zzz");
         request.setMissionId(16);
         request.setAnswer(true);
        RegisterOrderClientResponse response = template.postForObject(paymentUrl("/registerOrderClient"), request, RegisterOrderClientResponse.class);
    }





    @Test
    public void registerOrderDriver_V2() throws Exception {
        RegisterOrderDriverRequest request = new RegisterOrderDriverRequest();

        request.setMissionId(3);
        request.setDriverId(3);
        request.setSum(15000);

        RegisterOrderDriver_V2_Response response = template.postForObject(paymentUrl("/registerOrderDriver/v2"), request, RegisterOrderDriver_V2_Response.class);
        response.getErrorCodeHelper();
    }



    @Test
    public void payOprder() throws Exception {
        PayOrderRequest request = new PayOrderRequest();
        request.setMissionId(17);
        PayOrderResponse response = template.postForObject(paymentUrl("/payOrder"), request, PayOrderResponse.class);
        response.isSuccess();
    }



    @Test
    public void testRegisterOrderDriver() throws Exception {
        RegisterOrderDriverRequest request = new RegisterOrderDriverRequest();
           request.setMissionId(2);
           request.setDriverId(1);
           request.setSum(80);
        RegisterOrderDriverResponse response = template.postForObject(paymentUrl("/registerOrderDriver"), request, RegisterOrderDriverResponse.class);
        response.isResult();
    }


}

