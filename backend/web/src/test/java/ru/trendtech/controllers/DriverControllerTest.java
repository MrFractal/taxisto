package ru.trendtech.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.joda.time.LocalDate;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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
import ru.trendtech.common.mobileexchange.model.client.*;
import ru.trendtech.common.mobileexchange.model.common.*;
import ru.trendtech.common.mobileexchange.model.common.push.DeviceInfoModel;
import ru.trendtech.common.mobileexchange.model.common.rates.PaymentInfo;
import ru.trendtech.common.mobileexchange.model.common.rates.ServicePriceInfo;
import ru.trendtech.common.mobileexchange.model.common.scores.Scores;
import ru.trendtech.common.mobileexchange.model.driver.*;
import ru.trendtech.common.mobileexchange.model.web.*;
import ru.trendtech.domain.Mission;
import ru.trendtech.domain.VersionsApp;
import ru.trendtech.models.ModelsUtils;
import ru.trendtech.services.administration.AdministrationService;
import ru.trendtech.utils.DateTimeUtils;
import ru.trendtech.utils.PhoneUtils;

import java.util.ArrayList;
import java.util.List;

import static ru.trendtech.controllers.ProfilesUtils.adminUrl;
import static ru.trendtech.controllers.ProfilesUtils.clientUrl;
import static ru.trendtech.controllers.ProfilesUtils.driverUrl;


public class DriverControllerTest {
    //work
    private static final String PHONE = PhoneUtils.normalizeNumber("89833158338");
    private static final String PHONE_Temp = PhoneUtils.normalizeNumber("89955999922");
    private static final String PHONE_2 = PhoneUtils.normalizeNumber("89239999399");

    private static final int DEFAULT_DRIVER_ID = 3;


    @Test
    public void testRegister() throws Exception {
        RegisterDriverRequest request = new RegisterDriverRequest();

        DriverInfoARM driverInfo = buildDefaultDriver();
        request.setDriverInfoARM(driverInfo);
        request.setDeviceInfoModel(buildDeviceInfo());
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        RegisterDriverResponse response = Utils.template().postForObject("http://192.168.88.191:8080/driver/register", request, RegisterDriverResponse.class);
        long driverId = response.getDriverId();
    }




    @Test
    public void commonTripHistory() throws Exception {
        CommonTripHistoryRequest request = new CommonTripHistoryRequest();
        request.setRequesterId(8);
        request.setSecurity_token("xxx");
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        CommonTripHistoryResponse response = Utils.template().postForObject(driverUrl("/commonTripHistory"), request, CommonTripHistoryResponse.class);
        response.getCommonHistory();
        response.getErrorCode();
    }




    @Test
    public void freeWaitTime() throws Exception {
        FreeWaitTimeRequest request = new FreeWaitTimeRequest();
        request.setMissionId(118693);
        request.setSecurity_token("xxx");
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        FreeWaitTimeResponse response = Utils.template().postForObject(driverUrl("/freeWaitTime"), request, FreeWaitTimeResponse.class);
        response.getFreeTimeInFact();
        response.getFreeTime();
    }




    @Test
    public void estimateInfoByDriver() throws Exception {
        EstimateInfoRequest request = new EstimateInfoRequest();
        List<QueryDetails> list = new ArrayList<>();
        QueryDetails queryDetail = new QueryDetails();
        queryDetail.setEqual(8L);

        list.add(queryDetail);
        request.setQueryDetailsList(list);

        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        EstimateInfoResponse response = Utils.template().postForObject(driverUrl("/estimateInfoByDriver"), request, EstimateInfoResponse.class);
        response.getEstimateInfoARMList();
        response.getTotalItems();
    }


    @Test
    public void requisite() throws Exception {
        DriverRequisiteRequest request = new DriverRequisiteRequest();
        request.setDriverId(8);
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        DriverRequisiteResponse response = Utils.template().postForObject(driverUrl("/requisite"), request, DriverRequisiteResponse.class);
        response.getDriverRequisiteInfos();
    }



    @Test
    public void driverStartWork() throws Exception{
        DriverStartWorkRequest request = new DriverStartWorkRequest();
        request.setDriverId(43);
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        DriverStartWorkResponse response = Utils.template().postForObject(driverUrl("/startWork"), request, DriverStartWorkResponse.class);
        String asStringResponse = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(response);
        response.getDriverStartWorkInfo();
    }


    @Test
    public void driverGetStartWork() throws Exception {
        DriverStartWorkRequest request = new DriverStartWorkRequest();
        request.setDriverId(702);
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        DriverStartWorkResponse response = Utils.template().postForObject(driverUrl("/getStartWork"), request, DriverStartWorkResponse.class);
        response.getDriverStartWorkInfo();
        response.getErrorMessage();
    }



    @Test
    public void actualMissionInRadius() throws Exception {
        ActualMissionInRadiusRequest request = new ActualMissionInRadiusRequest();
        request.setDriverId(8);
        request.setRadius(8);
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        ActualMissionInRadiusResponse response = Utils.template().postForObject(driverUrl("/actualMissionInRadius"), request, ActualMissionInRadiusResponse.class);
        response.getDistanceDetailsList();
        response.getErrorMessage();
    }



    @Test
    public void testLogin() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setDeviceInfoModel(buildDeviceInfo());
        request.setLogin("555555"); // f: 117203
        request.setPassword("123456");
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        LoginDriverResponse response = Utils.template().postForObject(driverUrl("/login"), request, LoginDriverResponse.class); // f: LoginDriverResponse.class
        DriverInfo info = response.getDriverInfo();
    }






    @Test
    public void startTimeBooked(){
        TimeStartBookedRequest request = new TimeStartBookedRequest();
        request.setMissionId(1);
        TimeStartBookedResponse response = Utils.template().postForObject(driverUrl("/timeStartBooked"), request, TimeStartBookedResponse.class);
        long minutes = response.getTime();
    }





    //f: add
    @Test
    public void schowMissionDetailsF(){
        MissionInfoRequest request = new MissionInfoRequest();
        request.setMissionId(10633);
        MissionInfoResponse response = Utils.template().postForObject(driverUrl("/mission/details"), request, MissionInfoResponse.class);
        MissionInfo missionInfo = response.getMissionInfo();
        missionInfo.getOptions();
        double price = missionInfo.getPrice();
    }






    /**
     *   @RequestMapping(value = "/mission/score", method = RequestMethod.POST)
    public
     @ResponseBody
     RateDriverResponse scoreMission(@RequestBody RateDriverRequest request) {
     RateDriverResponse response = new RateDriverResponse();
     administrationService.rateDriver(request.getMissionId(), request.getScores());
     return response;
     }
     */


    @Test
    public void stopSearch() throws Exception {
        StringsListRequest request = new StringsListRequest();
        request.getValues().add("stop");
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        StringsListResponse response = Utils.template().postForObject(driverUrl("/stopSearch"), request, StringsListResponse.class);
        response.getValues();
    }


    @Test
    public void testRegisterDriver() throws Exception {
        // test registration driver
        RegisterDriverRequest request = new RegisterDriverRequest();

        DriverInfoARM driverInfo = buildDefaultDriver();

        request.setDriverInfoARM(driverInfo);
        request.setDeviceInfoModel(buildDeviceInfo());

        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        RegisterDriverResponse responseRegister = Utils.template().postForObject(driverUrl("/register"), request, RegisterDriverResponse.class);
        responseRegister.getErrorMessage();
        responseRegister.getErrorCode();
    }




    @Test
    public void news() throws Exception {
        DriverNewsRequest request = new DriverNewsRequest();
        request.setDriverId(1);
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        DriverNewsResponse response = Utils.template().postForObject(driverUrl("/news"), request, DriverNewsResponse.class);
        response.getErrorCodeHelper();
    }




    private DriverInfoARM buildDefaultDriver() {
        DriverInfoARM driverInfo = new DriverInfoARM();
        //driverInfo.setLogin("110227");

        TaxoparkPartnersInfo taxoparkPartnersInfo =  new TaxoparkPartnersInfo();
        taxoparkPartnersInfo.setId(1l);
        driverInfo.setTaxoparkPartnersInfo(taxoparkPartnersInfo);

        AssistantInfo assistantInfo =  new AssistantInfo();
        assistantInfo.setId(1l);
        driverInfo.setAssistantInfo(assistantInfo);

        driverInfo.setEmail("fr@bekker.com.ua");
        driverInfo.setFirstName("Тест45452");
        driverInfo.setLastName("Тест2");
        driverInfo.setAutoModel("Audi2");
        driverInfo.setAutoClass(1);
        driverInfo.setAutoColor("Красный2");
        driverInfo.setAutoNumber("а345уе54");
        driverInfo.setBalance(100);
        driverInfo.setTotalRating(0);
        driverInfo.setPassword("123456");
        driverInfo.setPhone("+79077185888"); //PHONE -

        driverInfo.setBirthDate(DateTimeUtils.now().getMillis()); // 887836767

        VersionsAppInfo versionsAppInfo1 = new VersionsAppInfo();
        versionsAppInfo1.setId(31);

        VersionsAppInfo versionsAppInfo2 = new VersionsAppInfo();
        versionsAppInfo2.setId(33);



        DriverCarPhotosInfo photo1 = new DriverCarPhotosInfo();
        photo1.setPhotoUrl("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");
        //photo1.setAndroidMinVersion(versionsAppInfo1);
        //photo1.setAppleMinVersion(versionsAppInfo2);

        DriverCarPhotosInfo photo2 = new DriverCarPhotosInfo();
        photo2.setPhotoUrl("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");
        //photo2.setAndroidMinVersion(versionsAppInfo1);
        //photo2.setAppleMinVersion(versionsAppInfo2);

        driverInfo.getDriverCarPhotosInfos().add(photo1);
        driverInfo.getDriverCarPhotosInfos().add(photo2);

        List<String> cars = new ArrayList<>();
        cars.add("xxx");
        cars.add("xxx");
        //driverInfo.setPhotosCarsPictures(cars);

        return driverInfo;
    }







    @Test
    public void testRegisterAndLogin() throws Exception {
        RegisterDriverRequest request = new RegisterDriverRequest();

        DriverInfoARM driverInfo = buildDefaultDriver();
        request.setDriverInfoARM(driverInfo);
        request.setDeviceInfoModel(buildDeviceInfo());
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        RegisterDriverResponse responseRegister = Utils.template().postForObject(driverUrl("/register"), request, RegisterDriverResponse.class);

        LoginRequest requestLogin = new LoginRequest();
        requestLogin.setDeviceInfoModel(buildDeviceInfo());
        requestLogin.setLogin(responseRegister.getLogin());
        requestLogin.setPassword("123456");
        String asString1 = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        LoginDriverResponse response = Utils.template().postForObject(driverUrl("/login"), requestLogin, LoginDriverResponse.class);
        DriverInfo loggedInDriverInfo = response.getDriverInfo();
    }





    /*
     @RequestMapping(value = "/checkTakeBooked", method = RequestMethod.POST)
    public
    @ResponseBody
    CheckTakeBookedResponse checkTakeBooked(@RequestBody CheckTakeBookedRequest request) {
        long driverId = request.getDriverId();
        long missionId = request.getMissionId();
        CheckTakeBookedResponse response =  administrationService.checkTakeBooked(driverId, missionId);
        return response;
    }
     */

    @Test
    public void checkTakeBooked() throws Exception {
        CheckTakeBookedRequest request = new CheckTakeBookedRequest();
          request.setDriverId(1);
          request.setMissionId(2);
        CheckTakeBookedResponse response = Utils.template().postForObject(driverUrl("/checkTakeBooked"), request, CheckTakeBookedResponse.class);
    }



    // возможность сменить номер телефона
    @Test
    public void updatePhoneDriver() throws Exception {
        DriverNewPhoneRequest request = new DriverNewPhoneRequest();
        request.setDriverId(8);
        request.setPhone("+79538696036");
        DriverNewPhoneResponse response = Utils.template().postForObject(driverUrl("/updatePhone"), request, DriverNewPhoneResponse.class);
    }



    @Test
    public void updateDriverTest() throws Exception{
        //DriverChangeRequest request = new DriverChangeRequest();

        DriverInfoRequest request = new DriverInfoRequest(8);
        DriverInfoResponse response = Utils.template().postForObject(driverUrl("/find"), request, DriverInfoResponse.class);

        DriverInfo driverInfo =  response.getDriverInfo();
        driverInfo.setAutoNumber("xxx");
        driverInfo.setAutoColor("фиолетовый");

        LocalDate locDate = new LocalDate(379890000L);
        //DateTimeUtils.getLocalDate(locDate.getYear(), locDate.getMonthOfYear(), locDate.getDayOfMonth());
        //DateTimeUtils.toDateTime(379890000000L).toLocalDate();

        driverInfo.setBirthDate(379890000);

        DriverChangeRequest request1 = new DriverChangeRequest();
        request1.setDriverInfo(driverInfo);


        DriverChangeResponse response2 = Utils.template().postForObject(driverUrl("/update"), request1, DriverChangeResponse.class);
        DriverInfo driverInfo2 = response2.getDriverInfo();

        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request1);
    }



    // подтверждение смены телефона
    @Test
    public void confirmationPhoneDriver() throws Exception {
        DriverConfirmationPhoneRequest request = new DriverConfirmationPhoneRequest();
        request.setCode("210756");
        request.setDriverId(8);
        request.setNewPhone("+79538696036");
        DriverConfirmationPhoneResponse response = Utils.template().postForObject(driverUrl("/confirmationPhone"), request, DriverConfirmationPhoneResponse.class);
    }




    @Test
    public void testRegisterTaxi() throws Exception {
        RegisterDriverRequest request = new RegisterDriverRequest();

        DriverInfoARM driverInfo = buildDefaultDriver1();
        request.setDriverInfoARM(driverInfo);
        request.setDeviceInfoModel(buildDeviceInfo());
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        RegisterDriverResponse responseRegister = Utils.template().postForObject(driverUrl("/register"), request, RegisterDriverResponse.class);

        LoginRequest requestLogin = new LoginRequest();
        requestLogin.setDeviceInfoModel(buildDeviceInfo());
        requestLogin.setLogin(responseRegister.getLogin());
        requestLogin.setPassword("123456");
        String asString1 = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        LoginDriverResponse response = Utils.template().postForObject(driverUrl("/login"), requestLogin, LoginDriverResponse.class);
        DriverInfo loggedInDriverInfo = response.getDriverInfo();
    }




    private DeviceInfoModel buildDeviceInfo() {
        DeviceInfoModel model = new DeviceInfoModel();
        model.setDeviceType(1);
        model.setNewToken("APA91b");
        model.setOldToken("GtNfSt");
           return model;
    }



    private DriverInfoARM buildDefaultDriver1() {
        DriverInfoARM driverInfo = new DriverInfoARM();
        driverInfo.setFirstName("Дмитрий");
        driverInfo.setLastName("Медведев");
        driverInfo.setAutoModel("Honda Accord");
        driverInfo.setAutoClass(1);
        driverInfo.setAutoColor("Серый");
        driverInfo.setAutoNumber("а905уе54");
        driverInfo.setBalance(200);
        driverInfo.setTotalRating(0);
        driverInfo.setPassword("123456");
        driverInfo.setPhone(PHONE_Temp);
        driverInfo.setBirthDate(887836767);
        return driverInfo;
    }


    private DriverInfo buildDefaultDriverTwo() {
        DriverInfo driverInfo = new DriverInfo();
        driverInfo.setFirstName("Николай");
        driverInfo.setLastName("Петрович");
        driverInfo.setAutoModel("Honda");
        driverInfo.setAutoClass(1);
        driverInfo.setAutoColor("Синий");
        driverInfo.setAutoNumber("а300уе54");
        driverInfo.setBalance(100);
        driverInfo.setTotalRating(0);
        driverInfo.setPassword("123456");
        driverInfo.setPhone(PHONE_2);
        return driverInfo;
    }

    @Test
    public void testFindDriverDetails() throws Exception {
        DriverInfoRequest request = new DriverInfoRequest(17); // DEFAULT_DRIVER_ID
        DriverInfoResponse response = Utils.template().postForObject(driverUrl("/find"), request, DriverInfoResponse.class);
        DriverInfo driverInfo = response.getDriverInfo();
    }

    @Test
    public void testFindDriverDetails1() throws Exception {
        DriverInfoRequest request = new DriverInfoRequest(5);
        DriverInfoResponse response = Utils.template().postForObject(driverUrl("/find"), request, DriverInfoResponse.class);
        response.getDriverInfo();
    }

    @Test
    public void testDriverArrived() throws Exception {
        DriverArrivedRequest request = new DriverArrivedRequest();
        request.setDriverId(40);
        request.setMissionId(12818);
        DriverArrivedResponse response = Utils.template().postForObject(driverUrl("/mission/arrived"), request, DriverArrivedResponse.class);
        response.getErrorMessage();
    }



    @Test
    public void testMissionPauseStart() throws Exception {
        TripPauseRequest request = new TripPauseRequest();
        request.setDriverId(6);
        request.setMissionId(41);
        request.setPauseBegin(true);

        DriverArrivedResponse response = Utils.template().postForObject(driverUrl("/mission/pause"), request, DriverArrivedResponse.class);

    }



    @Test
    public void testRatingDriver(){
        DriverRatingRequest request = new DriverRatingRequest();
        request.setDriverId(8);

        DriverRatingResponse response = Utils.template().postForObject(driverUrl("/rating"), request, DriverRatingResponse.class);
    }



    @Test
    public void testMissionPauseEnd() throws Exception {
        TripPauseRequest request = new TripPauseRequest();
        request.setDriverId(6);
        request.setMissionId(41);
        request.setPauseBegin(false);

        DriverArrivedResponse response = Utils.template().postForObject(driverUrl("/mission/pause"), request, DriverArrivedResponse.class);

    }

    @BeforeMethod
    public void forseSetProfile() throws Exception {
        ProfilesUtils.setProfileForce("staging");//default
    }

    @Test
    public void testClientSeated() throws Exception {
        ClientSeatedRequest request = new ClientSeatedRequest();
        request.setMissionId(41);
        ClientSeatedResponse response = Utils.template().postForObject(driverUrl("/mission/seated"), request, ClientSeatedResponse.class);
        long timestamp = response.getTimestamp();
    }

    @Test
    public void testMissionAssign() throws Exception {
        AssignMissionRequest request = new AssignMissionRequest();
        request.setArrivalTime(40);
        request.setDriverId(6);
        request.setMissionId(7);
        AssignMissionResponse response = Utils.template().postForObject(driverUrl("/mission/assign"), request, AssignMissionResponse.class);
        response.isAssigned();
    }

    @Test
    public void testMissionFinished() throws Exception {
        MissionFinishedRequest request = new MissionFinishedRequest();
        request.setDriverId(6);
        request.setMissionId(41);
        MissionFinishedResponse response = Utils.template().postForObject(driverUrl("/mission/finished"), request, MissionFinishedResponse.class);
        long timestamp = response.getTimestamp();
    }





     @Test
     public void findPayTerminals() throws Exception {
           NearestTerminalsRequest request = new NearestTerminalsRequest();
           request.setDriverId(8);
           NearestTerminalsResponse response = Utils.template().postForObject(driverUrl("/terminals"), request, NearestTerminalsResponse.class);
       }




    @Test
    public void testGetTripDetailsText() throws Exception {
        GetTripDetailsTextRequest request = new GetTripDetailsTextRequest();
        request.setDriverId(8);

        GetTripDetailsTextResponse response =  Utils.template().postForObject(driverUrl("/sendTripDetailsText"), request, GetTripDetailsTextResponse.class);

//        request.setClientPhone("+79030760303");

//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
//
//        //String url = BASE_URL + "/android/played.json";
//        restTemplate.getForObject("http://dev.taxisto.ru:81/index.php/client/GetTripDetailsText", GetTripDetailsTextResponse.class);
//
//
//        LOGGER.info("");


//        JSONObject json =new JSONObject();
//        json.put("missionId",75);
//        json.put("clientPhone","+79030760303");






//        GetTripDetailsTextResponse response = template.postForObject("http://dev.taxisto.ru:81/index.php/client/GTD", request, GetTripDetailsTextResponse.class); //

        // setSlide();
        //String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);


        //MappingJackson2HttpMessageConverter response = Utils.template().postForObject("http://dev.taxisto.ru:81/index.php/client/GetTripDetailsText", request, MappingJackson2HttpMessageConverter.class);
        //LOGGER.info("response="+response);
        //String asString = ((MappingJackson2HttpMessageConverter) template.getMessageConverters().get(0)).getObjectMapper().writeValueAsString(response);
        //String text = response.getText();
    }



    @Test
    public void testMissionFinishedPay() throws Exception {
        MissionFinishedRequest request = new MissionFinishedRequest();
        request.setDriverId(6);
        request.setMissionId(41);
        request.setDistanceInFact(15);
//        request.setPausesTime(15);
        List<ServicePriceInfo> list = new ArrayList<>();
        ServicePriceInfo servicePriceInfo = new ServicePriceInfo();
        servicePriceInfo.setOptionId(1);
        servicePriceInfo.setPrice(39);
        servicePriceInfo.setDetails("edede");
        servicePriceInfo.setService("rrr");
        list.add(servicePriceInfo);
        request.setServicesInFact(list);

        MissionFinishedResponse response = Utils.template().postForObject("http://109.120.152.140:81/index.php/driver/MissionFinishedInfo", request, MissionFinishedResponse.class);
        response.getPaymentInfo();
//        MissionFinishedResponse response = Utils.template().postForObject("http://192.168.88.191:8080/driver/mission/finished", request, MissionFinishedResponse.class);
        long timestamp = response.getTimestamp();
    }


    /*
    ArrayList<ServicePriceInfo> servicesPrices = new ArrayList<>();//Configuration.getInstance().getServicesPrices();
    ServicePriceInfo servicePriceInfo = new ServicePriceInfo();
    servicePriceInfo.setPrice(300);
    servicePriceInfo.setDetails("111");
    servicePriceInfo.setOptionId(1);
    servicePriceInfo.setService("777");
    servicesPrices.add(servicePriceInfo);

    MissionFinishedRequest request_finish = new MissionFinishedRequest();
    request_finish.setDistanceInFact((int) mDistance);
    request_finish.setMissionId(Configuration.getInstance().getCurrentMissionInfo().getId());
    request_finish.setDriverId(driverInfo.getId());
    request_finish.setServicesInFact(servicesPrices);
    MissionFinishedInfo - php
    request_finish.setDistanceInFact((int) mDistance);
    request_finish.setMissionId(Configuration.getInstance().getCurrentMissionInfo().getId());
    request_finish.setDriverId(driverInfo.getId());
    request_finish.setServicesInFact(servicesPrices);
    */



    @Test
    public void testMissionCalcPay() throws Exception {
        PriceCalculationRequest request = new PriceCalculationRequest();
        request.setDriverId(8);// f: 6
        request.setMissionId(16); // f: 41
        PaymentInfo pay = new PaymentInfo();
        List<Integer> options = new ArrayList<Integer>();
        options.add(2);
        options.add(5);
//        pay.setOptions(options);
        request.setPaymentInfo(pay);
        MissionFinishedResponse response = Utils.template().postForObject("http://109.120.152.140:81/index.php/driver/PriceCalculation", request, MissionFinishedResponse.class);
        long timestamp = response.getTimestamp();
    }





    @Test
    public void testMissionCalculation() throws Exception {
        PriceCalculationRequest request = new PriceCalculationRequest();
        request.setDriverId(6);
        request.setMissionId(41);
        PriceCalculationResponse response = Utils.template().postForObject(driverUrl("/mission/calculation"), request, PriceCalculationResponse.class);
        long timestamp = response.getTimestamp();
    }

    @Test
    public void testDecline() throws Exception {
        DeclineMissionRequest request = new DeclineMissionRequest(1, 1005);
        DeclineMissionResponse response = Utils.template().postForObject(driverUrl("/mission/decline"), request, DeclineMissionResponse.class);
        long missionId = response.getMissionId();
    }

    @Test
    public void testFindAndAssign() throws Exception {
        FindMissionRequest request = new FindMissionRequest();
        request.setDriverId(6);
        request.setRadius(7);
        FindMissionResponse response = Utils.template().postForObject(driverUrl("/mission/find"), request, FindMissionResponse.class);
        MissionInfo missionInfo = response.getMissionInfo();
    }

    @Test
    public void testFindMission() throws Exception {

        FindMissionRequest request = new FindMissionRequest();
        request.setDriverId(1);
        request.setRadius(10);
        FindMissionResponse response = Utils.template().postForObject(driverUrl("/mission/find"), request, FindMissionResponse.class);
        MissionInfo missionInfo = response.getMissionInfo();
    }



    @Test
    public void testFindMissionBooked() throws Exception {

        BookedRequest request = new BookedRequest();
        request.setDriverId(1);

        BookedResponse response = Utils.template().postForObject(driverUrl("/mission/booked"), request, BookedResponse.class);
        List<MissionInfo> listMissionInfo = response.getMissions();
        int bookedNew=response.getBookedNew();
        int bookedToMe=response.getBookedToMe();
        listMissionInfo.size();
    }


    @Test
    public void testLoginDriver() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setLogin("111111");
        request.setPassword("123456");

        LoginDriverResponse response = Utils.template().postForObject(driverUrl("/login"), request, LoginDriverResponse.class);
        DriverInfo driverInfo = response.getDriverInfo();
    }


    // f: add
    @Test
    public void testLoginDriverPetr() throws Exception {

        LoginRequest request = new LoginRequest();
        request.setLogin("555555");
        request.setPassword("123456");

        DeviceInfoModel deviceInfoModel = new DeviceInfoModel();
        deviceInfoModel.setNewToken("new token");
        deviceInfoModel.setOldToken("jTYUNrwrntuZ9i7w5lm5gK0G1ZDYVDZEcHEkKJqzLpAtlHB4zZDvKB4AsykFz1eCA23cy3Nhjcoaag");
        deviceInfoModel.setDeviceType(2);
        request.setDeviceInfoModel(deviceInfoModel);

        LoginDriverResponse response = Utils.template().postForObject(driverUrl("/login"), request, LoginDriverResponse.class);
        DriverInfo driverInfo = response.getDriverInfo();

    }




    @Test
    public void driverLocation() throws Exception {
//        ItemLocation itemLocation = new ItemLocation(1, 55.0166667, 82.9333333);
        ItemLocation itemLocation = ModelsUtils.toModel(1, 1.0166667, 2.9333333);
        DriverLocationRequest request = new DriverLocationRequest();
        request.setLocation(itemLocation);
        DriverLocationResponse response = Utils.template().postForObject(driverUrl("/location"), request, DriverLocationResponse.class);
        response.getTimestamp();
    }




    @Test
    public void setLocationWithTimeTracking() throws Exception {
        ItemLocation itemLocation = ModelsUtils.toModel(15, 1.0166667, 2.9333333);
        DriverLocationV2Request request = new DriverLocationV2Request();
        request.setTimeWork(15);
        request.setTimePayRest(10);
        request.setTimeRest(5);
        request.setDriverId(23);
        request.setLocation(itemLocation);
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        DriverLocationV2Response response = Utils.template().postForObject(driverUrl("/location/timeTracking"), request, DriverLocationV2Response.class);
        response.getErrorMessage();
    }





    @Test
    public void busyIsPayment() throws Exception {
        BusyIsPaymentRequest request = new BusyIsPaymentRequest();
        request.setDriverId(3);
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        BusyIsPaymentResponse response = Utils.template().postForObject(driverUrl("/busyIsPayment"), request, BusyIsPaymentResponse.class);
        response.isResult();
    }



    @Test
    public void testTripsHistory() throws Exception {
        TripsHistoryRequest request = new TripsHistoryRequest();
        request.setRequesterId(8);
        TripsHistoryResponse response = Utils.template().postForObject(driverUrl("/mission/history"), request, TripsHistoryResponse.class);
        response.getBooked();
        List<MissionInfo> missionInfos = response.getHistory();
//            for(MissionInfo missionInfo :missionInfos){
//                LOGGER.info("missionInfo date = "+ DateTimeUtils.toDateTime(missionInfo.getTimeOfSeating()));
//            }
        missionInfos.size();
    }




    @Test
    public void testCancelMission() throws Exception {
        CancelMissionRequest request = new CancelMissionRequest();
        request.setMissionId(31);
        request.setForce(true);
        String asString = ((MappingJackson2HttpMessageConverter) Utils.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        CancelMissionResponse response = Utils.template().postForObject(clientUrl("/mission/cancel"), request, CancelMissionResponse.class);
        Long missionID = response.getMissionId();
    }

}
