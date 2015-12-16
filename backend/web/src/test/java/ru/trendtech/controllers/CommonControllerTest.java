package ru.trendtech.controllers;

import com.shephertz.app42.paas.sdk.java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.trendtech.common.mobileexchange.model.client.PaymentLetterRequest;
import ru.trendtech.common.mobileexchange.model.client.PaymentLetterResponse;
import ru.trendtech.common.mobileexchange.model.client.TestSendPushRequest;
import ru.trendtech.common.mobileexchange.model.client.TestSendPushResponse;
import ru.trendtech.common.mobileexchange.model.common.*;
import ru.trendtech.common.mobileexchange.model.common.lang.LocalizationRequest;
import ru.trendtech.common.mobileexchange.model.common.lang.LocalizationResponse;
import ru.trendtech.common.mobileexchange.model.common.push.SendEventRequest;
import ru.trendtech.common.mobileexchange.model.common.push.SendEventResponse;
import ru.trendtech.common.mobileexchange.model.common.sms.SendSMSRequest;
import ru.trendtech.common.mobileexchange.model.common.sms.SendSMSResponse;
import ru.trendtech.common.mobileexchange.model.rabbit.RabbitRequest;
import ru.trendtech.common.mobileexchange.model.rabbit.RabbitResponse;
import ru.trendtech.utils.TokenUtil;

import java.util.List;
import java.util.concurrent.Callable;

import static ru.trendtech.controllers.ProfilesUtils.commonUrl;

/**
 * Created by max on 08.02.14.
 */
@ContextConfiguration("classpath:/META-INF/application-context.xml")
@TestExecutionListeners({TransactionalTestExecutionListener.class})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class CommonControllerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonControllerTest.class);
    private final RestTemplate template = Utils.template();





    @Test
    public void testGetCities() throws Exception {
        StringsListResponse forObject = template.getForObject(commonUrl("/cities"), StringsListResponse.class);
        List<String> values = forObject.getValues();
        values.size();
    }

    @Test
    public void testGetCountries() throws Exception {

    }

    @BeforeMethod
    public void forseSetProfile() throws Exception {
        ProfilesUtils.setProfileForce("max");
    }


    @Test
    public void sendSMS() throws Exception {
        SendSMSRequest request = new SendSMSRequest();
        request.setPhone("+79139425020");//+79137161558 +79833158988  +79835100540  +79538695889  +79139425020
        request.setSenderId(24);
        request.setType(1);
        request.setSecurity_token("359ef9d0998b496ac0e4c30a2082fe53");
        request.setMessage("Ваш временный пароль: 12345z");
        SendSMSResponse response = template.postForObject(commonUrl("/sendsms"), request, SendSMSResponse.class);

    }




    @Test
    public void localization() throws Exception {
        LocalizationRequest request = new LocalizationRequest();
        request.setLang("en");
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        LocalizationResponse response = template.postForObject(commonUrl("/localization"), request, LocalizationResponse.class);
        response.getErrorMessage();
    }


    @Test
    public void checkVersion() throws Exception {
        CheckVersionRequest request = new CheckVersionRequest();
        request.setClientType("DRIVER");
        request.setVersion("1.3.1");
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        CheckVersionResponse response = template.postForObject(commonUrl("/checkVersion"), request, CheckVersionResponse.class);
        response.getErrorCodeHelper();
    }



    @Test
    public void publish() throws Exception {
        RabbitRequest request = new RabbitRequest();
        request.setMessage("Привет");
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        RabbitResponse response = template.postForObject(commonUrl("/publish"), request, RabbitResponse.class);
        response.getErrorMessage();
    }





    @Test
    public void startSearch() throws Exception {
        StringsListRequest request = new StringsListRequest();
        request.getValues().add("start");
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        StringsListResponse response = template.postForObject(commonUrl("/test"), request, StringsListResponse.class);
        response.getValues();
    }



    @Test
    public void sendPush() throws Exception {
        TestSendPushRequest request = new TestSendPushRequest();

        request.setClientId(8927);
        request.setMessage("Ваш заказ принят. Чтобы мы начали выполнять ваш заказ, подтвердите его в приложении Таксисто в разделе Zavezu «Текущие заказы».");

        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        TestSendPushResponse response = template.postForObject(commonUrl("/sendPush"), request, TestSendPushResponse.class);
        response.getErrorMessage();
        response.getErrorCode();
    }






    @Test
    public void sendNodeEvent() throws Exception {
        SendEventRequest request = new SendEventRequest();
        //request.setMissionId(L);
        request.setResult(true);
        request.setAnswer("fsasdasdadd");

        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        SendEventResponse response = template.postForObject(commonUrl("/sendNodeEvent"), request, SendEventResponse.class);
        response.getErrorCode();
        response.getErrorMessage();
    }



    @Test
    public void configuration() throws Exception {
        SystemConfigurationRequest request = new SystemConfigurationRequest();
        request.setClientId(24);
        request.setSecurity_token("zzz");
        //request.setDriverId(8);

        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        SystemConfigurationResponse response = template.postForObject(commonUrl("/configuration"), request, SystemConfigurationResponse.class);
        response.getServerStateInfo();
        LOGGER.info("response = " + response);
    }




    @Test
    public void configuration_useMap() throws Exception {
        SystemConfigurationRequest request = new SystemConfigurationRequest();
        request.setClientId(232);
        request.setSecurity_token("02dc53d7e57f1892bc1dd83e09b28d7f");
        //request.setDriverId(144);

        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        SystemConfiguration_V2_Response response = template.postForObject(commonUrl("/configuration/useMap"), request, SystemConfiguration_V2_Response.class);
        response.getServerStateInfo();
        LOGGER.info("response = " + response);
    }




    @Test
    public void configuration_V5() throws Exception {
        SystemConfigurationRequest request = new SystemConfigurationRequest();
        request.setClientId(24);
        request.setSecurity_token("95ce1e87aa5c786016d6f7bc64ce3000");
        //request.setDriverId(8);

        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        SystemConfiguration_V5_Response response = template.postForObject(commonUrl("/configuration/v5"), request, SystemConfiguration_V5_Response.class);
        response.getServerStateInfo();
    }


    @Test
    public void configuration_V4() throws Exception {
        SystemConfigurationRequest request = new SystemConfigurationRequest();
        //request.setClientId(172);
        //request.setSecurity_token("2510ae89d23ffa35f89b4933492ca205");
        request.setDriverId(8);

        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        SystemConfiguration_V4_Response response = template.postForObject(commonUrl("/configuration/v4"), request, SystemConfiguration_V4_Response.class);
        response.getServerStateInfo();
    }





    @Test
    public void configuration_V6() throws Exception {
        SystemConfigurationRequest request = new SystemConfigurationRequest();
        request.setClientId(297);
        request.setSecurity_token("");

        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        SystemConfiguration_V6_Response response = template.postForObject(commonUrl("/configuration/v6"), request, SystemConfiguration_V6_Response.class);
        response.getServerStateInfo();
        response.getDriverCurrentLocation();
    }



    @Test
    public void configuration_V3() throws Exception {
        SystemConfigurationRequest request = new SystemConfigurationRequest();
        //request.setClientId(286);
        //request.setSecurity_token("1ef87c882abe432f99df6c16941a405c");
        request.setDriverId(40);


        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        SystemConfiguration_V3_Response response = template.postForObject(commonUrl("/configuration/v3"), request, SystemConfiguration_V3_Response.class);
        response.getServerStateInfo();
    }




    //f:add
    @Test
    public void generatePromoCodes() throws Exception { // id>25155;
        GeneratePromoCodesRequest request = new GeneratePromoCodesRequest();
        request.setCountSymbols(6); // кол-во символов
        request.setCountPromoCode(1000); // кол-во промокодов
        request.setAmount(100); // сумма
        request.setChannel("flyers"); // источник
        //request.setChannel("app");
        request.setAvailableUsedCount(1); // кол-во активаций
        GeneratePromoCodesResponse response = template.postForObject(commonUrl("/generatePromoCodes"), request, GeneratePromoCodesResponse.class);
        response.isGenerate();
    }



    @Test
    public void generateExclusivePromoCodes() throws Exception {
        GeneratePromoCodesRequest request = new GeneratePromoCodesRequest();
        request.setCountSymbols(7); // кол-во символов
        request.setCountPromoCode(105); // кол-во промокодов
        request.setAvailableUsedCount(1); // кол-во активаций
        request.setLifetimeDaysAfterActivation(30);
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        GeneratePromoCodesResponse response = template.postForObject(commonUrl("/generateExclusivePromoCodes"), request, GeneratePromoCodesResponse.class);
        response.isGenerate();
    }


    @Test
    public void paymentLetter() throws Exception {
        PaymentLetterRequest request = new PaymentLetterRequest();
        String token = TokenUtil.getMD5("fractal"+2191);
        request.setMissionId(2191);
        request.setToClient(true);
        request.setSecurity_token(token);
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        PaymentLetterResponse response = Utils.template().postForObject(commonUrl("/paymentLetter"), request, PaymentLetterResponse.class);
        response.getErrorMessage();
    }


    //f:add
    @Test
    public void missionFind() throws Exception {
        MissionFindRequest request = new MissionFindRequest();
        request.setMissionId(3149);
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        MissionFindResponse response = template.postForObject(commonUrl("/missionFind"), request, MissionFindResponse.class);
        response.getMissionInfo();
    }


     //f:add
     @Test
     public void sendSmsDefault() throws Exception {
        SmsSendDefaultRequest request = new SmsSendDefaultRequest();
        request.setPhone("+79538695889");
        request.setMessage("test");
        SmsSendDefaultResponse response = template.postForObject(commonUrl("/smsSendDefault"), request, SmsSendDefaultResponse.class);
        response.isSendSms();
     }

}
