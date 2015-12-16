package ru.trendtech.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.trendtech.common.mobileexchange.model.client.*;
import ru.trendtech.common.mobileexchange.model.common.*;
import ru.trendtech.common.mobileexchange.model.common.estimate.EstimateInfoClient;
import ru.trendtech.common.mobileexchange.model.common.push.DeviceInfoModel;
import ru.trendtech.common.mobileexchange.model.common.scores.Scores;
import ru.trendtech.common.mobileexchange.model.common.sms.SMSCodeRequest;
import ru.trendtech.common.mobileexchange.model.common.sms.SMSCodeResponse;
import ru.trendtech.common.mobileexchange.model.common.support.SupportPhonesRequest;
import ru.trendtech.common.mobileexchange.model.common.support.SupportPhonesResponse;
import ru.trendtech.common.mobileexchange.model.driver.TestRequest;
import ru.trendtech.common.mobileexchange.model.driver.TestResponse;
import ru.trendtech.common.mobileexchange.model.web.AutoClassPriceRequest;
import ru.trendtech.models.ModelsUtils;
import ru.trendtech.services.sms.ServiceSMSNotification;
import ru.trendtech.utils.DateTimeUtils;
import ru.trendtech.utils.PhoneUtils;

import java.util.*;
import java.util.concurrent.Callable;

import static ru.trendtech.controllers.ProfilesUtils.clientUrl;
import static ru.trendtech.controllers.ProfilesUtils.commonUrl;

/**
 * Created by max on 08.02.14.
 */
@ContextConfiguration("classpath:/META-INF/application-context.xml")
@TestExecutionListeners({TransactionalTestExecutionListener.class})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class ClientControllerTest {

    private static final String DEFAULT_PHONE_SECOND = PhoneUtils.normalizeNumber("79833158962");
    private static final String DEFAULT_PHONE_FIRST = PhoneUtils.normalizeNumber("79833158988");

    private static final String DEFAULT_PASSWORD = "123456";

    private static final String DEFAULT_CODE_SMS = "111111";

    private final RestTemplate template = Utils.template();



    @Test
    public void testCreateMultipleMission() throws Exception {
        CreateMultipleMissionRequest request = new CreateMultipleMissionRequest();
        request.setClientId(24);
        request.setSecurity_token("zzz");


        //////////////////////////////////////////////
        MultipleMissionInfo m1 = new MultipleMissionInfo();
        MissionInfo missionInfo1 = new MissionInfo();
        missionInfo1.setAddressFrom("Ольги Жилиной, 60");


        ClientInfo clientInfo1 = new ClientInfo();
        clientInfo1.setId(24);

        missionInfo1.setClientInfo(clientInfo1);
        missionInfo1.setAutoType(1);
        ItemLocation location1 = ModelsUtils.toModel(55.047353, 82.938324);

        missionInfo1.setPaymentType(1);
        missionInfo1.setLocationFrom(location1);

        m1.setMissionInfo(missionInfo1);
        m1.setTimeOfStarting("2015-05-08 16:47:10");
        m1.setAutoCount(1);

        ///////////////////////////////////////////////

        MultipleMissionInfo m2 = new MultipleMissionInfo();
        MissionInfo missionInfo2 = new MissionInfo();

        missionInfo2.setAddressFrom("Ватутина, 107");

        ClientInfo clientInfo2 = new ClientInfo();
        clientInfo2.setId(172);

        missionInfo2.setClientInfo(clientInfo2);
        missionInfo2.setAutoType(2);
        ItemLocation location2 = ModelsUtils.toModel(55.047353, 82.938324);
        missionInfo2.setPaymentType(1);
        missionInfo2.setLocationFrom(location2);

        m2.setMissionInfo(missionInfo2);
        m2.setTimeOfStarting("2015-05-05 18:09:42");
        m2.setAutoCount(1);


        request.getMultipleMissionInfos().add(m1);
        //request.getMultipleMissionInfos().add(m2);

        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        CreateMultipleMissionResponse response = template.postForObject(clientUrl("/createMultipleMission"), request, CreateMultipleMissionResponse.class);
        response.getErrorMessage();
    }





    @Test
    public void testCurrentStateMission() throws Exception {
        CurrentStateMissionRequest request = new CurrentStateMissionRequest();
        request.setMissionId(59940);
        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        Callable<String> response = template.postForObject(clientUrl("/currentStateMission"), request, Callable.class);
        response.toString();
    }








    @Test
    public void photoDelete() throws Exception {
        ClientPhotoDeleteRequest request = new ClientPhotoDeleteRequest();
        request.setClientId(24);
        request.setSecurity_token("98631822677b4f872cdf6c07c5edc7d6");
        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        ClientPhotoDeleteResponse response = template.postForObject(clientUrl("/photoDelete"), request, ClientPhotoDeleteResponse.class);
        response.getErrorCodeHelper().getErrorMessage();
    }





    @Test
    public void configuration() throws Exception {
        ClientSystemConfigurationRequest request = new ClientSystemConfigurationRequest();
        request.setClientId(24);
        request.setSecurity_token("f798846cf8388087faa057e33c04e6ca");
        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        ClientSystemConfigurationResponse response = template.postForObject(clientUrl("/configuration"), request, ClientSystemConfigurationResponse.class);
        response.getAfterMinBooked();
    }








    @Test
    public void findClientV2() throws Exception {
        ClientInfoRequest request = new ClientInfoRequest();
        request.setClientId(24);
        request.setSecurity_token("0c5dc3bf731ac7caf500f4ecf1225a93");
        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        ClientInfoResponseV2 response = template.postForObject(clientUrl("/find/v2"), request, ClientInfoResponseV2.class);
        response.getMissionCount();
        response.getClientInfo();
    }



    @Test
    public void autoClassPrices() throws Exception {
        AutoClassPriceRequest request = new AutoClassPriceRequest();
        request.setSecurity_token("b20711cc0e0507cefb91adb9ba77e178");
        //request.setAutoClass(0);
        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        AutoClassPriceResponseV2 response = template.postForObject(clientUrl("/autoClassPrice"), request, AutoClassPriceResponseV2.class);
        response.getAutoClassRateInfoV3List();
        response.getErrorMessage();
    }




    @Test
    public void missionFind() throws Exception {
        MissionFindRequestV2 request = new MissionFindRequestV2();
        request.setSecurity_token("0c5dc3bf731ac7caf500f4ecf1225a93");
        request.setRequesterId(24);
        request.setMissionId(384);
        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        MissionFindResponse response = template.postForObject(clientUrl("/missionFind/v2"), request, MissionFindResponse.class);
        response.getMissionInfo();
    }






    @Test
    public void servicesPrices() throws Exception {
        ServicePriceRequest request = new ServicePriceRequest();
        request.setSecurity_token("zzz");
        request.setRequesterId(24);
        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        ServicePriceResponse response = template.postForObject(clientUrl("/servicesPrices"), request, ServicePriceResponse.class);
        response.getServicePriceInfos();
        response.getErrorMessage();
    }





    @Test
    public void supportPhones() throws Exception {
        SupportPhonesRequest request = new SupportPhonesRequest();
        request.setSecurity_token("0c5dc3bf731ac7caf500f4ecf1225a93");
        request.setRequesterId(24);
        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        SupportPhonesResponse response = template.postForObject(clientUrl("/supportPhones"), request, SupportPhonesResponse.class);
        response.getSupportPhones();
        response.getErrorMessage();
    }




    @Test
    public void testLoginTest() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setLogin("+79538444489");
        request.setPassword("123456");
        request.setDeviceInfoModel(buildDeviceInfo());
        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        LoginClientResponse response = template.postForObject(clientUrl("/login"), request, LoginClientResponse.class);
        ClientInfo clientInfo = response.getClientInfo();
    }



    @Test
    public void smsMissionInfo() throws Exception {
       SmsSendMissionInfoRequest request = new SmsSendMissionInfoRequest();
       request.setSecurity_token("zzz");
       request.setMissionId(3);
       String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
       SmsSendMissionInfoResponse response = template.postForObject(clientUrl("/smsSendMissionInfo"), request, SmsSendMissionInfoResponse.class);
       response.getErrorCodeHelper();
    }




    @Test
    public void missionEstimate() throws Exception {
        MissionEstimateRequest request = new MissionEstimateRequest();
        request.setSecurity_token("12beb20bd5f07a8fc9664eac67ab619f");

        EstimateInfoClient info = new EstimateInfoClient();

        MissionInfo missionInfo = new MissionInfo();
        missionInfo.setId(3642);

        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setId(172);
        missionInfo.setClientInfo(clientInfo);

        DriverInfo driverInfo = new DriverInfo();
        driverInfo.setId(8);
        missionInfo.setDriverInfo(driverInfo);

        info.setMissionInfo(missionInfo);

        info.setGeneral(8);

        request.setEstimateInfoClient(info);

        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        MissionEstimateResponse response = template.postForObject(clientUrl("/mission/estimate"), request, MissionEstimateResponse.class);
        response.getTimestamp();

    }




    @Test
    public void testLogin() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setLogin(DEFAULT_PHONE_SECOND);
        request.setPassword(DEFAULT_PASSWORD);
        request.setDeviceInfoModel(buildDeviceInfo());
        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        LoginClientResponse response = template.postForObject(clientUrl("/login"), request, LoginClientResponse.class);
        ClientInfo clientInfo = response.getClientInfo();
    }

    @Test
    public void testRegisterWithRecreate() throws Exception {
//        long id = testRegister();
//        if (id == -1) {
//            testDelete();
//        }
    }





    @Test
    private void deleteClientCard() {
        DeleteClientCardRequest request = new DeleteClientCardRequest();
        request.setClientCardId(4);
        DeleteClientCardResponse response = template.postForObject(clientUrl("/deleteClientCard"), request, DeleteClientCardResponse.class);
    }



        @Test
        private void startPriceCalculate() throws JsonProcessingException {
            // расчет первоначальной стоимости поездки
            GetStartPriceRequest request = new GetStartPriceRequest();

            request.setAuto_class(4); // бизнесс
            request.setDistance(4);

            List<Integer> options = new ArrayList<>();
            //options.add(5);
            //options.add(3);
            request.setOptions(options);
            String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
            GetStartPriceResponse response = template.postForObject(clientUrl("/getStartPrice"), request, GetStartPriceResponse.class);
            response.getSum();
            response.getSumWithOption();
        }



        @Test
        private void smsCodeRepeateTerminal() throws Exception{
            SMSCodeRepeateTerminalRequest request = new SMSCodeRepeateTerminalRequest();
            request.setSecurity_token("zzz");
            request.setPhone("+79538695889");
            SMSCodeRepeateTerminalResponse response = template.postForObject(clientUrl("/smsCodeRepeate"), request, SMSCodeRepeateTerminalResponse.class);
            response.getErrorCodeHelper().getErrorMessage();
        }



    @Test
    private void autoSearchStart() throws Exception{
        AutoSearchRequest request = new AutoSearchRequest();
        request.setSecurity_token("zzz");
        request.setClientId(17);
        request.setMissionId(5);
        AutoSearchResponse response = template.postForObject(clientUrl("/autoSearch"), request, AutoSearchResponse.class);
        response.getErrorCodeHelper().getErrorMessage();
    }




    @Test
    private void autoSearchCancel() throws Exception{
        AutoSearchRequest request = new AutoSearchRequest();
        request.setSecurity_token("c75ba525f7541c64b0ed2815850c7901");
        request.setClientId(172);
        request.setMissionId(9903);
        AutoSearchResponse response = template.postForObject(clientUrl("/autoSearchCancel"), request, AutoSearchResponse.class);
        response.getErrorCodeHelper().getErrorMessage();
        response.getErrorCodeHelper().getErrorCode();
    }





        @Test
        private void imageSource() throws Exception{
            ImageSourceRequest request = new ImageSourceRequest();
            request.setSecurity_token("bc938a963fc5aca5167478691247def2");
            request.setRequesterId(1805);
            String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
            ImageSourceResponse response = template.postForObject(clientUrl("/imageSource"), request, ImageSourceResponse.class);
            response.getInfoList();
        }



// метод 2gis
//    @Test
//    private void calculateDistance2GIS() throws Exception{
//        // длинна маршрута по точкам
//        CalculateDistanceRequest request = new CalculateDistanceRequest();
//        request.setClientId(3);
//        request.setSecurity_token("zzz");
//        ArrayList<String> adressesList = new ArrayList<>();
//
//
//        adressesList.add("82.938135 55.047373");//достоевсеого 58
//        adressesList.add("82.915588 55.029177"); // саввы
//
//        request.setLatLonList(adressesList);
//        request.getLatLonList();
//
//        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
//
//        CalculateDistanceResponse response = template.postForObject(clientUrl("/calculateDistance/2gis"), request, CalculateDistanceResponse.class);
//        response.getDistanceKm();
//        response.getErrorCodeHelper();
//    }


    @Test
    private void calculateDistance_old() throws Exception{
        // длинна маршрута по точкам
        CalculateDistanceRequest request = new CalculateDistanceRequest();
        request.setClientId(24);
        request.setSecurity_token("zzz");
        ArrayList<String> adressesList = new ArrayList<>();

        adressesList.add("82.938135 55.047373");//достоевсеого 58
        //adressesList.add("82.973125 54.956461"); // саввы

        request.setLatLonList(adressesList);
        request.getLatLonList();

        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);

        CalculateDistanceResponse response = template.postForObject(clientUrl("/calculateDistance_old"), request, CalculateDistanceResponse.class);
        response.getDistanceKm();
        response.getErrorCodeHelper();
    }






    // метод для расчета поездки - новый!
    @Test
    private void calculatePrice() throws Exception{
        // длинна маршрута по точкам
        CalculatePriceRequest request = new CalculatePriceRequest();
        request.setClientId(24);
        request.setSecurity_token("257b82c623c9484b5ebaaf63e521dd8e"); // e53d3b12a1db29405bf19f4bf3fa5c99

        MissionInfo missionInfo = new MissionInfo();

        missionInfo.setAutoType(4);

        ItemLocation location = ModelsUtils.toModel(55.008557, 82.936895);
        missionInfo.setLocationFrom(location);

        ItemLocation locationTo = ModelsUtils.toModel(54.890094, 83.077083);
        //missionInfo.setLocationTo(locationTo);

        List<MissionAddressesInfo> missionAddressesInfoList = new ArrayList<>();


        MissionAddressesInfo missionAddressesInfo1 = new MissionAddressesInfo();
        missionAddressesInfo1.setLatitude(54.938132);
        missionAddressesInfo1.setLongitude(83.105051);
        missionAddressesInfo1.setAddress("Одоевского, 1/9");

        MissionAddressesInfo missionAddressesInfo2 = new MissionAddressesInfo();
        missionAddressesInfo2.setLatitude(54.937248);
        missionAddressesInfo2.setLongitude(83.126352);
        missionAddressesInfo2.setAddress("Твардовского, 16");

        MissionAddressesInfo missionAddressesInfo3 = new MissionAddressesInfo();
        missionAddressesInfo3.setLatitude(54.890094);
        missionAddressesInfo3.setLongitude(83.077083);
        missionAddressesInfo3.setAddress("Лесосечная, 2");

        missionAddressesInfoList.add(missionAddressesInfo1);
        missionAddressesInfoList.add(missionAddressesInfo2);
        missionAddressesInfoList.add(missionAddressesInfo3);


        missionInfo.setMissionAddressesInfos(missionAddressesInfoList);

        missionInfo.setPaymentType(1);

        request.setMissionInfo(missionInfo);

        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);

        CalculatePriceResponse response = template.postForObject(clientUrl("/calculatePrice"), request, CalculatePriceResponse.class);
        response.getResultSum();
        response.getErrorMessage();
    }



    @Test
        private void calculateDistance() throws Exception{
            // длинна маршрута по точкам
            CalculateDistanceRequest request = new CalculateDistanceRequest();
            request.setClientId(24L);
            request.setSecurity_token("e53d3b12a1db29405bf19f4bf3fa5c99");
            List<String> adressesList = new ArrayList<>();

            //55.033047, 82.910018
            //55.013694, 82.657124

            //adressesList.add("82.93839263916016 55.04734802246094");
            //adressesList.add("86.0600346959086 55.3442414027991");

//            adressesList.add("82.938297 55.052046");
//            adressesList.add("82.938135 55.047373");

            /*
            6 км
            adressesList.add("82.9829777712043 55.04470082834");
            adressesList.add("82.9339907522992 55.0586181910299");
            */

            adressesList.add("82.971395 55.100502");
            adressesList.add("82.860728 55.047172");


            request.setLatLonList(adressesList);
            request.getLatLonList();
            String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
            CalculateDistanceResponse response = template.postForObject(clientUrl("/calculateDistance"), request, CalculateDistanceResponse.class);
            response.getDistanceKm();
            response.getErrorCodeHelper();
        }

    //total_distance: 3613
    //distance in m: 3747


    // DGIS autocomplete
    @Test
    public void findAddressByMask() throws Exception {
        FindAddressRequest request = new FindAddressRequest();

        request.setAddressMask("лен");

        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        FindAddressResponse response = template.postForObject(clientUrl("/findAddress"), request, FindAddressResponse.class);
        response.getDgisStreetList();
    }






    @Test
    public void turboIncrease() throws Exception {
        TurboIncreaseRequest request = new TurboIncreaseRequest();
        request.setAmount(35);
        request.setMissionId(5);
        request.setClientId(1);
        request.setSecurity_token("zzz");
        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        TurboIncreaseResponse response = template.postForObject(clientUrl("/startTurbo"), request, TurboIncreaseResponse.class);
        response.getErrorCodeHelper();
    }




    @Test
    public void testRegister() throws Exception {
        RegistrationInfoRequest request = new RegistrationInfoRequest();
        ClientInfo clientInfo = ClientSveta();

        DeviceInfoModel deviceInfoModel = buildDeviceInfo();
        //String encodeFromFile = ProfilesUtils.getPicture("client.png");
        //clientInfo.setPicure(encodeFromFile);
        request.setClientInfo(clientInfo);
        request.setDeviceInfoModel(deviceInfoModel);

        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        RegistrationInfoResponse response = template.postForObject(clientUrl("/registration"), request, RegistrationInfoResponse.class);
        response.getClientId();
        response.getDeviceId();
    }



    private DeviceInfoModel buildDeviceInfo() {
        DeviceInfoModel model = new DeviceInfoModel();
        model.setDeviceType(3);
        model.setNewToken("be97e0450b8f80c1");
        model.setOldToken("test token " + DateTime.now());
           return model;
    }



    @Test
    public void testDriverAssigned() throws Exception {
        AssignedDriverRequest request = new AssignedDriverRequest();
        //request.setDriverId(384);
        request.setMissionId(12780);
        request.setSecurity_token("c0b790c5b546ea269a373bfc831f8513");
        AssignedDriverResponse response = template.postForObject(clientUrl("/drivers/assigned"), request, AssignedDriverResponse.class);
        DriverInfo driverInfo = response.getDriverInfo();
    }




    @Test
    public void testAssignedDriverLocation() throws Exception {
        AssignedDriverLocationRequest request = new AssignedDriverLocationRequest();
        request.setDriverId(1);
        AssignedDriverLocationResponse response = template.postForObject(clientUrl("/drivers/assigned/location"), request, AssignedDriverLocationResponse.class);
        ItemLocation location = response.getLocation();
    }



    @Test
    public void estimateList() throws Exception {
        DriverEstimatesRequest request = new DriverEstimatesRequest();
        request.setDriverId(23);
        request.setSecurity_token("a00bac176f8b0718961bf5375fda3277");
        request.setClientId(172);
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        DriverEstimatesResponse response = template.postForObject(clientUrl("/estimate/list"), request, DriverEstimatesResponse.class);
        response.getDriverProfileInfo();

    }


    @Test
    public void estimateListV2() throws Exception {
        DriverEstimatesRequest request = new DriverEstimatesRequest();
        request.setDriverId(23);
        request.setSecurity_token("a00bac176f8b0718961bf5375fda3277");
        request.setClientId(172);
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        DriverEstimatesV2Response response = template.postForObject(clientUrl("/estimate/list/v2"), request, DriverEstimatesV2Response.class);
        response.getDriverProfileInfo();
    }




    @Test
    public void findDrivers() throws Exception {
         DriversAroundRequest request = new DriversAroundRequest();
         request.setRadius(15);
         ItemLocation itemLocation = new ItemLocation();
         itemLocation.setLatitude(55.009158338827454);
         itemLocation.setLongitude(82.939672460371255);
         request.setCurrentLocation(itemLocation);
         DriversAroundResponse response = template.postForObject(clientUrl("/drivers"), request, DriversAroundResponse.class);
         response.getLocations();
    }




    @Test
    public void testAutoSearch() throws Exception {
        StartWatchMissionRequest request = new StartWatchMissionRequest();
        request.setWatch_security_token("0014dab7a99230ec0dfb3c1b02de225z");
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        StartWatchMissionResponse response = template.postForObject(clientUrl("/startWatchMission"), request, StartWatchMissionResponse.class);
        response.getDriverInfoARM();
        response.getErrorCodeHelper();
    }




    @Test
    public void testDriversAround() throws Exception {
        DriversAroundRequest request = new DriversAroundRequest();
        request.setRadius(50);
        ItemLocation it= new ItemLocation();
        it.setLatitude(55.0473747253418);
        it.setLongitude(82.9381332397461);

        request.setCurrentLocation(it);
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        DriversAroundResponse response = template.postForObject(clientUrl("/drivers"), request, DriversAroundResponse.class);
        List<ItemLocation> locations = response.getLocations();
    }




    @Test
    public void testRegisterAndConfirmClient() throws Exception {
        RegistrationInfoRequest request = new RegistrationInfoRequest();
        request.setClientInfo(defaultClient());
        request.setDeviceInfoModel(buildDeviceInfo());

        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        RegistrationInfoResponse response = template.postForObject(clientUrl("/registration"), request, RegistrationInfoResponse.class);
        String sms = response.getSmsCode();
        long clientId = response.getClientId();

        RegistrationConfirmRequest request1 = new RegistrationConfirmRequest();
        request1.setCodeSMS(sms);

        asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request1);
        RegistrationConfirmResponse response1 = template.postForObject(clientUrl("/registration/confirm"), request1, RegistrationConfirmResponse.class);
        clientId = response1.getClientId();
    }



    @Test
    public void testRegistrationConfirmByLogin() throws Exception {
        RegistrationConfirmRequest request = new RegistrationConfirmRequest();
        request.setLogin("+79177660000");
        request.setCodeSMS("123");
        RegistrationConfirmResponse response = template.postForObject(clientUrl("/registration/confirm"), request, RegistrationConfirmResponse.class);
        long clientId = response.getClientId();
    }




    @Test
    public void testRepeatSmsCodeSend() throws Exception {
        SMSCodeRequest request = new SMSCodeRequest();
        request.setLogin("+79538695889"); //+79030760303 - dmitriy +79538695889 - my
        SMSCodeResponse response = template.postForObject(clientUrl("/registration/smscode"), request, SMSCodeResponse.class);
        ServiceSMSNotification sms = new ServiceSMSNotification();
        sms.registrationConfirm(request.getLogin(), "new_sms_code", "");
        boolean clientId = response.isSent();
    }




    @Test
    public void markMissionAsDelete() throws Exception {
        MarkMissionAsDeleteRequest request = new MarkMissionAsDeleteRequest();
        request.setSecurity_token("6e39608dae3b2054d9945c374e53b738");
        request.setClientId(24);

        List<Long> missionIdMarkDelete = new ArrayList<>();
        missionIdMarkDelete.add(383L);
        //missionIdMarkDelete.add(424L);
        //missionIdMarkDelete.add(463L);

        request.setMissionMarkAsDeleteIdList(missionIdMarkDelete);

        MarkMissionAsDeleteResponse response = template.postForObject(clientUrl("/markMissionAsDelete"), request, MarkMissionAsDeleteResponse.class);
        response.getErrorCodeHelper();
    }




    @Test
    public void testRegistrationConfirm() throws Exception {
        RegistrationConfirmRequest request = new RegistrationConfirmRequest();
//        request.setCodeSMS(DEFAULT_CODE_SMS);
        request.setCodeSMS("191575");
        request.setLogin("");

        RegistrationConfirmResponse response = template.postForObject(clientUrl("/registration/confirm"), request, RegistrationConfirmResponse.class);
        long clientId = response.getClientId();
    }

    private ClientInfo defaultClient() {
//        StringsListResponse countries = template.getForObject(commonUrl("/countries"), StringsListResponse.class);
//        StringsListResponse cities = template.getForObject(commonUrl("/cities"), StringsListResponse.class);

        ClientInfo result = new ClientInfo();
        result.setGender(true);
//        result.setCountry(countries.getValues().get(0));
//        result.setCity(cities.getValues().get(0));
        result.setPhone(DEFAULT_PHONE_FIRST);
        result.setPassword(DEFAULT_PASSWORD);
        result.setEmail("fr@bekker.com.ua");
        result.setFirstName("Петр");
        result.setLastName("Руденко");

        result.setBirthdayYear(1982);
        result.setBirthdayMonth(1);
        result.setBirthdayDay(15);

        return result;
    }

    private ClientInfo ClientSveta() {
//        StringsListResponse countries = template.getForObject(commonUrl("/countries"), StringsListResponse.class);
//        StringsListResponse cities = template.getForObject(commonUrl("/cities"), StringsListResponse.class);

        ClientInfo result = new ClientInfo();
        result.setGender(true);
//        result.setCountry(countries.getValues().get(0));
//        result.setCity(cities.getValues().get(0));
        result.setPhone("+79144660000");
        result.setPassword(DEFAULT_PASSWORD);
        //result.setEmail("fr@bekker.com.ua");
        result.setFirstName("test");
        result.setLastName("test");

        result.setBirthdayYear(1980);
        result.setBirthdayMonth(2);
        result.setBirthdayDay(12);

        return result;
    }

    private ClientInfo defaultClientSecond() {
        StringsListResponse countries = template.getForObject(commonUrl("/countries"), StringsListResponse.class);
        StringsListResponse cities = template.getForObject(commonUrl("/cities"), StringsListResponse.class);

        ClientInfo result = new ClientInfo();
        result.setGender(true);
        result.setCountry(countries.getValues().get(0));
        result.setCity(cities.getValues().get(0));
        result.setPhone(DEFAULT_PHONE_SECOND);
        result.setPassword(DEFAULT_PASSWORD);
        result.setEmail("valentina@gmail.com");
        result.setFirstName("Валентина");
        result.setLastName("Гордеева");

        result.setBirthdayYear(1986);
        result.setBirthdayMonth(5);
        result.setBirthdayDay(11);

        return result;
    }


    @Test
    public void testUpdate() throws Exception {
        ClientInfoRequest request = new ClientInfoRequest(1);
        ClientInfoResponse response = template.postForObject(clientUrl("/find"), request, ClientInfoResponse.class);
        ClientInfo clientInfo = response.getClientInfo();
        request.setClientId(response.getClientInfo().getId());
        request.setClientInfo(defaultClient());
        response = template.postForObject(clientUrl("/update"), request, ClientInfoResponse.class);
    }




    @Test
    public void passwordRecoveryUpdateAuthUser() throws Exception {
        UpdatePasswordRequest request = new UpdatePasswordRequest();
        request.setClientId(17);
        request.setSecurity_token("zzz");
        request.setPassword("dsfsfsdf");
        UpdatePasswordResponse response = template.postForObject(clientUrl("/recovery/updateAuthUser"), request, UpdatePasswordResponse.class);
    }



    @Test
    public void getFreePromoCode() throws Exception {
        GetFreePromoCodeRequest request = new GetFreePromoCodeRequest();
        request.setSecurity_token("zzz");
        request.setClientId(24);
        request.setSocial_network("VK");

        GetFreePromoCodeResponse response = template.postForObject(clientUrl("/getFreePromoCode"), request, GetFreePromoCodeResponse.class);
        String promoCode = response.getTextToPost();
        response.getErrorCodeHelper().getErrorMessage();
        response.getErrorCodeHelper().getErrorCode();
    }


    @Test
    public void createNewMission() throws Exception {
        ClientInfoRequest request = new ClientInfoRequest(5);
        request.setSecurity_token("zzz");
        ClientInfoResponse response = template.postForObject(clientUrl("/find"), request, ClientInfoResponse.class);
        ClientInfo clientInfo = response.getClientInfo();

        MissionInfo mission = new MissionInfo();
        mission.setAddressFrom("Достоевского 58");
        mission.setAddressTo(null);

        mission.setComment("коробке");

        //mission.setMissionState("CANCELED");

        //mission.setTimeOfStart(1409896235000L); //  DateTimeUtils.now().getMillis()
        //mission.setTimeOfRequesting(DateTimeUtils.now().getMillis());

        mission.setTimeOfStart((DateTimeUtils.now()).getMillis());// plusHours(6)

        TimeZone tz = TimeZone.getTimeZone("GMT+6");
        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.MILLISECOND, tz.getOffset(DateTimeUtils.now().getMillis()));
        DateTime dt2= new DateTime(calendar2.getTime());
        mission.setTimeOfRequesting(dt2.getMillis());


        //multi addresses
        List<MissionAddressesInfo> missionAddressesInfoList = new ArrayList<>();

        /*
        "56.472207 84.957395",
        "56.476563 84.949535",
        "56.505162 85.046859"
         */


        MissionAddressesInfo missionAddressesInfo1 = new MissionAddressesInfo();
        missionAddressesInfo1.setLatitude(56.472207);
        missionAddressesInfo1.setLongitude(84.957395);
        missionAddressesInfo1.setAddress("Связистов 1");

        MissionAddressesInfo missionAddressesInfo2 = new MissionAddressesInfo();
        missionAddressesInfo2.setLatitude(56.476563);
        missionAddressesInfo2.setLongitude(84.949535);
        missionAddressesInfo2.setAddress("Кожевникова 3");

        missionAddressesInfoList.add(missionAddressesInfo1);
        missionAddressesInfoList.add(missionAddressesInfo2);

        mission.setMissionAddressesInfos(missionAddressesInfoList);

        ItemLocation location = ModelsUtils.toModel(55.03923, 82.927818);
        mission.setPaymentType(1);

        mission.setLocationFrom(location);

        mission.setClientInfo(clientInfo);

        FreeDriverRequest requestFree = new FreeDriverRequest();
        requestFree.setSecurity_token("zzz");
        requestFree.setMissionInfo(mission);

        requestFree.getMissionInfo();

        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(requestFree);
        FreeDriverResponse response1 = template.postForObject(clientUrl("/drivers/request"), requestFree, FreeDriverResponse.class);

        Long missionID = response1.getMissionId();
        response1.getErrorCodeHelper();
    }




    @Test
    public void startMissionTerminal() throws Exception {
        StartMissionTerminalRequest request = new StartMissionTerminalRequest();
        request.setPhone("+79538695889");
        StartMissionTerminalResponse response = template.postForObject(clientUrl("/startMissionTerminal"), request, StartMissionTerminalResponse.class);
        response.getErrorCodeHelper().getErrorMessage();
        response.getErrorCodeHelper().getErrorCode();
    }



    @Test
    public void smsCodeConfirmTerminal() throws Exception {
        SMSCodeConfirmTerminalRequest request = new SMSCodeConfirmTerminalRequest();
        request.setPhone("+79538695889");
        request.setSmsCode("248");
        request.setSecurity_token("2927ddca3ead1d53289ddf5b9302e1db");

        SMSCodeConfirmTerminalResponse response = template.postForObject(clientUrl("/smsCodeConfirm"), request, SMSCodeConfirmTerminalResponse.class);
        response.getErrorCodeHelper().getErrorMessage();
        response.getErrorCodeHelper().getErrorCode();
    }


    /*
     "timeOfStart" => 1412153463000,
    "options" => array (
        0 => 2,
        1 => 4,
    ),
    "addressFrom" => "Новосибирск Пушкина 1",
    "addressTo" => "Новосибирск саввы кожевникова 1",
    "locationFrom" => array (
        "la" => 55.0453479,
        "lo" => 82.9437427,
    ),
    "locationTo" => array (
        "la" => 54.9564486,
        "lo" => 82.9731943,
    ),
    "paymentType" => 1,
    "autoType" => 2,
    "comment" => "тестовый комментарий",
    "clientInfo" => 59,
    "security_token" => "04c9fbb211a6dcc74b5129c043a824f4",
     */
    @Test
    public void createMissionFromTerminal() throws JsonProcessingException {

        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setId(17);

        MissionInfo mission = new MissionInfo();
        mission.setAddressFrom("Новосибирск Пушкина 1");
        mission.setAddressTo("Новосибирск саввы кожевникова 1");

        mission.setTimeOfStart(1412153463000L); //  DateTimeUtils.now().getMillis()
        mission.setTimeOfRequesting(DateTimeUtils.now().getMillis());


        ItemLocation locationFrom = ModelsUtils.toModel(55.03923, 82.927818);
        ItemLocation locationTo = ModelsUtils.toModel(54.9564486, 82.9731943);

        mission.setPaymentType(1);

        mission.setLocationFrom(locationFrom);
        mission.setLocationTo(locationTo);

        mission.setClientInfo(clientInfo);

        mission.setAutoType(2);

        List<Integer> option = new ArrayList<>();
        option.add(2);
        option.add(4);

        mission.setOptions(option);

        FreeDriverRequest requestFree = new FreeDriverRequest();
        requestFree.setSecurity_token("zzz");
        requestFree.setMissionInfo(mission);

        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(requestFree);
        FreeDriverResponse response = template.postForObject(clientUrl("/drivers/request"), requestFree, FreeDriverResponse.class);

        response.getErrorCodeHelper();
    }



    // f: add
    @Test
    public void testScoreMission(){

        RateDriverRequest request = new RateDriverRequest();
        request.setMissionId(16);
        request.setDriverId(8);

        Scores scores = new Scores();

        scores.setGeneral(2);
        Scores.Details details = new Scores.Details();
        details.setApplicationConvenience(4);
        details.setCleanlinessInCar(2);
        details.setDriverCourtesy(4);
        details.setWaitingTime(2);
        details.setWifiQuality(2);

        scores.setDetails(details);

        request.setScores(scores);

        RateDriverResponse response = Utils.template().postForObject(clientUrl("/mission/score"), request, RateDriverResponse.class);
    }




    @Test
    public void testAddMultipleCards() throws Exception {
        CardsInfoRequest request = new CardsInfoRequest();
        request.setClientId(1);
        CardInfo cardInfo = buildCardInfo();
        request.setCardInfo(cardInfo);
        CardsInfoResponse response = template.postForObject(clientUrl("/cards/add"), request, CardsInfoResponse.class);
        request.getCardInfo().setExpirationYear(request.getCardInfo().getExpirationYear() + 1);
        CardsInfoResponse response1 = template.postForObject(clientUrl("/cards/add"), request, CardsInfoResponse.class);
        request.getCardInfo().setExpirationYear(request.getCardInfo().getExpirationYear() + 1);
        CardsInfoResponse response2 = template.postForObject(clientUrl("/cards/add"), request, CardsInfoResponse.class);
        request.getCardInfo().setExpirationYear(request.getCardInfo().getExpirationYear() + 1);
        CardsInfoResponse response3 = template.postForObject(clientUrl("/cards/add"), request, CardsInfoResponse.class);
        request.getCardInfo().setExpirationYear(request.getCardInfo().getExpirationYear() + 1);
        CardsInfoResponse response4 = template.postForObject(clientUrl("/cards/add"), request, CardsInfoResponse.class);
        response.getCardInfo();
    }

    @Test
    public void testAddCard() throws Exception {
        CardsInfoRequest request = new CardsInfoRequest();
        request.setClientId(10);
        CardInfo cardInfo = buildCardInfo();
        request.setCardInfo(cardInfo);
        CardsInfoResponse response = template.postForObject(clientUrl("/cards/add"), request, CardsInfoResponse.class);
    }

    private CardInfo buildCardInfo() {
        CardInfo cardInfo = new CardInfo();
        cardInfo.setNumber("1234567812345678");
        cardInfo.setExpirationMonth(5);
        cardInfo.setExpirationYear(2017);
        cardInfo.setCvv("287");
        return cardInfo;
    }

    @Test
    public void testGetCards() throws Exception {
        CardsInfoRequest request = new CardsInfoRequest();
        request.setClientId(10);
        CardsInfoResponse response = template.postForObject(clientUrl("/cards"), request, CardsInfoResponse.class);
    }

    @Test
    public void testUpdateCards() throws Exception {
        CardsInfoRequest request = new CardsInfoRequest();
        request.setClientId(10);
        CardsInfoResponse response = template.postForObject(clientUrl("/cards"), request, CardsInfoResponse.class);
        CardInfo cardInfo = response.getCardInfo().get(0);
        cardInfo.setExpirationYear(cardInfo.getExpirationYear() + 1);
        cardInfo.setId(0);
        request.setCardInfo(cardInfo);
        response = template.postForObject(clientUrl("/cards/update"), request, CardsInfoResponse.class);
    }







    @Test
    public void testRecovery() throws Exception {

        UpdatePasswordRequest request = new UpdatePasswordRequest();
        request.setPhone("+79538695889"); // +79538695889 - my
        request.setClientId(24);
        // это уже после того, как клиент получил код подтверждения и указал новый пароль
        request.setPassword("zzz");
        request.setSmsCode("836");

        UpdatePasswordResponse response = template.postForObject(clientUrl("/recovery"), request, UpdatePasswordResponse.class);

        response.getSmsCode();
        response.getClientId();
    }

    @BeforeMethod
    public void forceSetProfile() throws Exception {
        ProfilesUtils.setProfileForce("max");
    }



    @Test
    public void testLoginMax() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setLogin("+79999999998");
        request.setPassword("123456");
        DeviceInfoModel deviceInfoModel = new DeviceInfoModel();
        deviceInfoModel.setNewToken("333");
        deviceInfoModel.setOldToken("!!!!!!!!APA91bFHUQFl_Pag");
        deviceInfoModel.setDeviceType(1);
        request.setDeviceInfoModel(deviceInfoModel);
        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        LoginClientResponse response = template.postForObject(clientUrl("/login"), request, LoginClientResponse.class);
        response.getErrorCode();
        response.getMessage();
        ClientInfo clientInfo = response.getClientInfo();
    }



    @Test
    public void testTripsHistory() throws Exception {
        TripsHistoryRequest request = new TripsHistoryRequest(8927);
        request.setSecurity_token("24004bb980e8c71b4562e02a0ebeaecf");
        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        TripsHistoryResponse response = Utils.template().postForObject(clientUrl("/mission/history"), request, TripsHistoryResponse.class);
        List<MissionInfo> missionInfos = response.getHistory();
        missionInfos.size();
    }



    @Test
    public void testTripsHistorySTR() throws Exception {
        TripsHistoryRequest request = new TripsHistoryRequest(286);
        request.setSecurity_token("42ef0df0b3a38ec73c1aee2700c08efa");
        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        TripsHistorySTRResponse response = Utils.template().postForObject(clientUrl("/mission/history/str"), request, TripsHistorySTRResponse.class);
        List<MissionHistory> missionHistory = response.getHistory();
        List<MissionHistory> missionBooked = response.getBooked();
        //missionInfos.size();
    }



    @Test
    public void testTripsHistorySite() throws Exception {
        TripsHistorySiteRequest request = new TripsHistorySiteRequest();
        request.setNumberPage(1);
        request.setSizePage(1);
        //request.setDateStart(1381923104);
        //request.setDateEnd(1413459104);
        request.setSecurity_token("0be77b52522e3ad5a1c15055b2152082");
        request.setRequesterId(52);

        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);

        TripsHistorySiteResponse response = Utils.template().postForObject(clientUrl("/mission/historySite"), request, TripsHistorySiteResponse.class);
          List<MissionInfo> missionInfos = response.getBookedAndHistory();
              response.getLastPageNumber();
              missionInfos.size();
    }





        @Test
        public void registrationLetter() throws Exception {
            RegistrationLetterRequest request = new RegistrationLetterRequest();
            request.setClientId(3);
            RegistrationLetterResponse response = Utils.template().postForObject(clientUrl("/registrationLetter"), request, RegistrationLetterResponse.class);
              response.getHtml();
        }



    @Test
    public void getClientCardListAndroid() throws Exception {
        ClientCardListRequest request = new ClientCardListRequest();
        request.setClientId(11);
        request.setSecurity_token("zzz");
        ClientCardListAndroidResponse response = Utils.template().postForObject(clientUrl("/getClientCardList/android"), request, ClientCardListAndroidResponse.class);
        List<ClientCardInfoAndroid> clientCardInfoList = response.getClientCardInfoList();
    }



    @Test
    public void getClientCardList() throws Exception {
        ClientCardListRequest request = new ClientCardListRequest();
        request.setClientId(272);
        request.setSecurity_token("42fa09e97672635cb2a8d527779d55ca");
        ClientCardListResponse response = Utils.template().postForObject(clientUrl("/getClientCardList"), request, ClientCardListResponse.class);
        response.getClientCardInfoList();
    }



    @Test
    public void lifeTimeBonusCard() throws Exception {
        LifeTimeBonusCardRequest request = new LifeTimeBonusCardRequest();

        request.setChannel("app_download");
        request.setText("rrr");
        request.setFrom_id(2);

        LifeTimeBonusCardResponse response = template.postForObject(clientUrl("/billing/lifetime"), request, LifeTimeBonusCardResponse.class);
    }




    @Test
    public void testCardActivation() throws Exception {
        DateTime.now(DateTimeZone.forTimeZone(TimeZone.getTimeZone("Asia/Novosibirsk")));
        ActivateBonusCardRequest request = new ActivateBonusCardRequest();
         request.setId(21);
         request.setSecurity_token("319233464bc2a545d0aa174f084d4918");
         request.setText("YTREWQ"); // RXLG4G
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        ActivateBonusCardResponse response = template.postForObject(clientUrl("/billing/activate"), request, ActivateBonusCardResponse.class);
        double amount = response.getAmount();
    }




    @Test
    public void testNewMissionValentina() throws Exception {
//        Calendar calendar = Calendar.getInstance();
//        TimeZone tz = TimeZone.getDefault();
//        calendar.setTimeInMillis(new Date().getTime());
//        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));

        ClientInfoRequest request = new ClientInfoRequest(18);
        request.setSecurity_token("zzz");
        ClientInfoResponse response = template.postForObject(clientUrl("/find"), request, ClientInfoResponse.class);
        ClientInfo clientInfo = response.getClientInfo();

        MissionInfo mission = new MissionInfo();


        List<Integer> options = new ArrayList<>();
        options.add(2);
        options.add(3);
        mission.setOptions(options);

        mission.setTimeOfStart(DateTimeUtils.now().getMillis());

        mission.setAddressFrom("Связистов 139");
        mission.setAddressTo("Титова 23");
        mission.setComment("tregsdfgdfg");

        mission.setPaymentType(1);

        mission.setLocationFrom(ModelsUtils.toModel(55.03923, 82.927818));
        mission.setLocationTo(ModelsUtils.toModel(55.03923, 82.927818));
        mission.setClientInfo(clientInfo);


        FreeDriverRequest requestFree1 = new FreeDriverRequest();
        requestFree1.setMissionInfo(mission);
        requestFree1.setSecurity_token("zzz");

        //FreeDriverRequest requestFree2 = new FreeDriverRequest();
        //requestFree2.setMissionInfo(mission);


        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(requestFree1);
        FreeDriverResponse response1 = template.postForObject(clientUrl("/drivers/request"), requestFree1, FreeDriverResponse.class);
        response1.getErrorCodeHelper();
        Long missionID1 = response1.getMissionId();


    }





    @Test
    public void testNewMissionTerminal() throws Exception {

        ClientInfoRequest request = new ClientInfoRequest(7);
        request.setSecurity_token("zzz");
        ClientInfoResponse response = template.postForObject(clientUrl("/find"), request, ClientInfoResponse.class);
        ClientInfo clientInfo = response.getClientInfo();

        MissionInfo mission = new MissionInfo();

        List<Integer> options = new ArrayList<>();
        options.add(2);
        options.add(3);
        mission.setOptions(options);

        mission.setTimeOfStart(DateTimeUtils.now().getMillis());

        mission.setAddressFrom("Связистов 139");
        mission.setAddressTo("Титова 23");
        mission.setComment("tregsdfgdfg");

        mission.setPaymentType(1);

        mission.setLocationFrom(ModelsUtils.toModel(55.03923, 82.927818));
        mission.setLocationTo(ModelsUtils.toModel(55.03923, 82.927818));
        mission.setClientInfo(clientInfo);

        FreeDriverTerminalRequest requestFree1 = new FreeDriverTerminalRequest();
        requestFree1.setMissionInfo(mission);
        requestFree1.setSecurity_token("zzz");
        requestFree1.setTerminalId(1);

        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(requestFree1);
        FreeDriverResponse response1 = template.postForObject(clientUrl("/drivers/request/terminal"), requestFree1, FreeDriverResponse.class);
        response1.getErrorCodeHelper();
        Long missionID1 = response1.getMissionId();
    }


    @Test
    public void testCancelMissionMax() throws Exception {
        CancelMissionRequest request = new CancelMissionRequest();
        request.setMissionId(28);
        request.setInitiatorId(13);
        request.setComment("передумал");
        request.setReason(3);
        request.setForce(true);
        request.setTimestamp(DateTime.now().getMillis());

        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        CancelMissionResponse response = template.postForObject(clientUrl("/mission/cancel"), request, CancelMissionResponse.class);
        Long missionID = response.getMissionId();
    }




    @Test
    public void testPhonechangeClient() throws Exception {
        RegistrationPhoneChangeRequest request = new RegistrationPhoneChangeRequest();
        request.setLogin("+79999999998");
        request.setPassword("123456");
        request.setPhone("+70000000000");//70000000000

        RegistrationPhoneChangeResponse response = template.postForObject(clientUrl("/registration/phonechange"), request, RegistrationPhoneChangeResponse.class);
    }



    @Test
    public void testMethod() throws Exception {
        TestRequest request = new TestRequest();
        request.setClientId(new Long(24));
        request.setDumbField("siiiiiisd");
        TestResponse response = Utils.template().postForObject(clientUrl("/testRequest"), request, TestResponse.class);
        response.toString();
    }





    @Test
    public void getCCType() throws Exception {
        ResolveCreditCardTypeRequest request = new ResolveCreditCardTypeRequest();
        request.setCreditCardNumber("521324336007");
        ResolveCreditCardTypeResponse response = Utils.template().postForObject(clientUrl("/getCCType"), request, ResolveCreditCardTypeResponse.class);
           response.getTypeCreditCard();
    }




    @Test
    public void testNewBookingUser() throws Exception {
        ClientInfoRequest request = new ClientInfoRequest(9); //
        ClientInfoResponse response = template.postForObject(clientUrl("/find"), request, ClientInfoResponse.class);

        ClientInfo clientInfo = response.getClientInfo(); // почему здесь response.getClientInfo(), а не request.getClientInfo()

        MissionInfo mission = new MissionInfo();
        mission.setAddressFrom("Связистов 139");
        mission.setAddressTo("Титова 23");
        mission.setComment("кофейку");
        mission.setAutoType(1); // 1- standart, 2-comfort, 3-business
        mission.setTimeOfStart(DateTime.now().plus((long) (1 * 60 * 60 * 1000)).getMillis());


        mission.setPaymentType(1);

        mission.setLocationFrom(ModelsUtils.toModel(55.03923, 82.927818));
        mission.setLocationTo(ModelsUtils.toModel(55.03923, 82.927818));
        mission.setClientInfo(clientInfo);
        mission.setBooked(true);

        FreeDriverRequest requestFree = new FreeDriverRequest();
        requestFree.setMissionInfo(mission);

        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(requestFree);

        FreeDriverResponse response1 = template.postForObject(clientUrl("/drivers/request"), requestFree, FreeDriverResponse.class);
        Long missionID = response1.getMissionId();

        response1.getErrorCodeHelper();

    }







    @Test
    public void testFindClient() throws Exception {
        ClientInfoRequest request = new ClientInfoRequest(17);
        request.setSecurity_token("zzz");
        //String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        ClientInfoResponse response = template.postForObject(clientUrl("/find"), request, ClientInfoResponse.class);
        ClientInfo clientInfo = response.getClientInfo();

    }



    @Test
    public void findUser() throws Exception {
        ClientInfoRequest request = new ClientInfoRequest(17); // 3 - Валя
        request.setSecurity_token("zzz");

        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        ClientInfoResponse response = template.postForObject(clientUrl("/find"), request, ClientInfoResponse.class);

        ClientInfo clientInfo = response.getClientInfo();
    }



    @Test
    public void testUpdateClientMax() throws Exception {
        ClientInfoRequest request = new ClientInfoRequest(79);
        request.setSecurity_token("aa7b038a00351a4fd8fb76a6e906b929");
        //ClientInfoResponse response = template.postForObject(clientUrl("/find"), request, ClientInfoResponse.class);

        ClientInfo clientInfo = new ClientInfo();//  response.getClientInfo();
        clientInfo.setId(79);
        clientInfo.setGender(false);
        clientInfo.setPicure("/9j/4AAQSkZJRgABAQ");
        clientInfo.setLastName("Пупкни1");
        clientInfo.setFirstName("Вася1");
        clientInfo.setPhone("+79885552214");
        clientInfo.setBirthdayDay(15);
        clientInfo.setBirthdayMonth(10);
        clientInfo.setBirthdayYear(1987);


        request.setClientInfo(clientInfo);

        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        ClientInfoResponse response = template.postForObject(clientUrl("/update"), request, ClientInfoResponse.class);
        clientInfo = response.getClientInfo();

//        clientInfo = response.getClientInfo();
//        clientInfo.setGender(null);
//        request.setClientInfo(clientInfo);
//        response = template.postForObject(clientUrl("/update"), request, ClientInfoResponse.class);

//        clientInfo = response.getClientInfo();
//        clientInfo.setGender(true);
//        request.setClientInfo(clientInfo);
//        response = template.postForObject(clientUrl("/update"), request, ClientInfoResponse.class);
    }




    @Test
    public void testRegisterMax() throws Exception {
        RegistrationInfoRequest request = new RegistrationInfoRequest();
        ClientInfo clientInfo = defaultClient();
        clientInfo.setPhone("+79132890411"); // Юлия

        DeviceInfoModel deviceInfoModel = buildDeviceInfo();

//      String encodeFromFile = ProfilesUtils.getPicture("client.png");
//      clientInfo.setPicure(encodeFromFile);
        request.setClientInfo(clientInfo);
        request.setDeviceInfoModel(deviceInfoModel);

        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        RegistrationInfoResponse response = template.postForObject(clientUrl("/registration"), request, RegistrationInfoResponse.class);
        response.getClientId();
        response.getDeviceId();
    }






    @Test
    public void testRegisterClientOnSite() throws Exception {
        RegistrationInfoRequest request = new RegistrationInfoRequest();
        ClientInfo clientInfo = defaultClient();
        clientInfo.setPhone("+78134566466");

        DeviceInfoModel deviceInfoModel = buildDeviceInfo();

        request.setClientInfo(clientInfo);
        request.setDeviceInfoModel(deviceInfoModel);

        String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        RegistrationInfoSiteResponse response = template.postForObject(clientUrl("/registrationSite"), request, RegistrationInfoSiteResponse.class);
        response.getErrorCodeHelper();
    }


/*
curl -X 'POST' -H 'content-type:application/json' -d '{"missionId":75, "clientPhone":"+79030760303"}' http://dev.taxisto.ru:81/index.php/client/GetTripDetailsText
*/



//    public void test(){
//
//    String stringUrl = "https://qualysapi.qualys.eu/api/2.0/fo/report/?action=list";
//    URL url = new URL(stringUrl);
//    URLConnection uc = url.openConnection();
//
//    uc.setRequestProperty("X-Requested-With", "Curl");
//
//    String userpass = "username" + ":" + "password";
//    String basicAuth = "Basic " + new String(new Base64().encode(userpass.getBytes()));
//    uc.setRequestProperty("Authorization", basicAuth);
//
//    InputStreamReader inputStreamReader = new InputStreamReader(uc.getInputStream());
//    }

}
