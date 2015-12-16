package ru.trendtech.controllers.mobile;

import org.apache.commons.mail.EmailException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.trendtech.common.mobileexchange.model.client.PaymentLetterRequest;
import ru.trendtech.common.mobileexchange.model.client.PaymentLetterResponse;
import ru.trendtech.common.mobileexchange.model.client.TestSendPushRequest;
import ru.trendtech.common.mobileexchange.model.client.TestSendPushResponse;
import ru.trendtech.common.mobileexchange.model.common.*;
import ru.trendtech.common.mobileexchange.model.common.lang.LocalizationRequest;
import ru.trendtech.common.mobileexchange.model.common.lang.LocalizationResponse;
import ru.trendtech.common.mobileexchange.model.common.push.DeviceInfoModel;
import ru.trendtech.common.mobileexchange.model.common.push.SendEventRequest;
import ru.trendtech.common.mobileexchange.model.common.push.SendEventResponse;
import ru.trendtech.common.mobileexchange.model.common.rates.AutoClassRateInfo;
import ru.trendtech.common.mobileexchange.model.common.rates.MissionRateInfo;
import ru.trendtech.common.mobileexchange.model.common.rates.MissionRateInfoV2;
import ru.trendtech.common.mobileexchange.model.common.rates.ServicePriceInfo;
import ru.trendtech.common.mobileexchange.model.common.sms.SendSMSRequest;
import ru.trendtech.common.mobileexchange.model.common.sms.SendSMSResponse;
import ru.trendtech.common.mobileexchange.model.common.states.ServerStateInfo;
import ru.trendtech.common.mobileexchange.model.common.states.ServerStateInfoV2;
import ru.trendtech.common.mobileexchange.model.test.Example;
import ru.trendtech.common.mobileexchange.model.web.GlobalClientStatsResponse;
import ru.trendtech.common.mobileexchange.model.web.WebUserModel;
import ru.trendtech.controllers.CommonHelpers;
import ru.trendtech.domain.*;
import ru.trendtech.domain.Properties;
import ru.trendtech.domain.admin.WebUser;
import ru.trendtech.domain.billing.Account;
import ru.trendtech.domain.billing.MissionRate;
import ru.trendtech.domain.courier.OrderPayment;
import ru.trendtech.models.ModelsUtils;
import ru.trendtech.models.error.ErrorType;
import ru.trendtech.repositories.*;
import ru.trendtech.services.administration.AdministrationService;
import ru.trendtech.services.billing.BillingService;
import ru.trendtech.services.common.CommonService;
import ru.trendtech.services.driver.DriverService;
import ru.trendtech.services.logging.GrayLogService;
import ru.trendtech.services.notifications.push.App42PushNotificationService;
import ru.trendtech.services.resources.ProfilesResourcesService;
import ru.trendtech.services.sms.ServiceSMSNotification;
import ru.trendtech.utils.HTTPUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by max on 08.02.14.
 */
@Controller
@RequestMapping("/common")
class CommonController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonController.class);
    @Autowired
    private AdministrationService administrationService;
    @Autowired
    private BillingService billingService;
    @Autowired
    private ServiceSMSNotification serviceSMSNotification;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ProfilesResourcesService profilesResourcesService;
    @Autowired
    private PropertiesRepository propertiesRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private DriverService driverService;
    @Autowired
    private MissionRepository missionRepository;
    @Autowired
    private CommonService commonService;
    @Autowired
    private App42PushNotificationService app42PushNotificationService;
    @Autowired
    private GrayLogService grayLogService;
    @Value("${default_lang}")
    private String defaultLang;
    @Autowired
    private OrderPaymentRepository orderPaymentRepository;


    @RequestMapping(value = "/cities", method = RequestMethod.GET)
    public
    @ResponseBody
    StringsListResponse getCities() {
        //OrderPayment orderPayment = orderPaymentRepository.findByTransactionId("51708363");
        StringsListResponse response = new StringsListResponse();
        response.getValues().add("Новосибирск");
        response.getValues().add("Киев");
        return response;
    }





    @RequestMapping(value = "/localization", method = RequestMethod.POST)
    @ResponseBody
    public LocalizationResponse localization(@RequestBody LocalizationRequest request){
        LocalizationResponse response = new LocalizationResponse();
        if(!StringUtils.isEmpty(request.getLang())){
            throw ErrorType.getCustomExceptionByKey("CLIENT_NOT_FOUND", defaultLang);
        }
           return response;
    }



    @RequestMapping(value = "/zzz", method = RequestMethod.POST)
    public
    @ResponseBody
    StringsListResponse startSearchDriver(@RequestParam("language") String language)  {
        StringsListResponse response = new StringsListResponse();
        response.getValues().add("language: "+language);
        response.getValues().add("push message: ");
            return response;
    }



    @RequestMapping(value="/asyncStart", method=RequestMethod.GET)
    @ResponseBody
    public String asyncStart(HttpSession session) throws ExecutionException, InterruptedException {
        Future<GlobalClientStatsResponse> report = (Future<GlobalClientStatsResponse>)session.getAttribute("report");
        if(report == null){
            report = commonService.generateReport();
            session.setAttribute("report", report);
        }
            return "Task is running. To see result go: localhost:8080/common/asyncResult";
    }



    @RequestMapping(value = "/asyncResult", method=RequestMethod.GET)
    @ResponseBody
    public GlobalClientStatsResponse reportStatus(HttpSession session) throws ExecutionException, InterruptedException {
        GlobalClientStatsResponse response = new GlobalClientStatsResponse();
        Future<GlobalClientStatsResponse> report = (Future<GlobalClientStatsResponse>)session.getAttribute("report");
        if(report == null){
            throw new CustomException(1, "No run task");
        }
        if(report.isDone()) {
            commonService.getCount().set(0);
            session.setAttribute("report", null);
            return report.get();
        } else {
            response.setErrorMessage("Still working! Count complete:" + commonService.getCount());
            return response;
        }
    }




    @RequestMapping(value = "/greyTest", method = RequestMethod.GET)
    @ResponseBody
    public GlobalClientStatsResponse grayLogTest(HttpServletRequest request){
        GlobalClientStatsResponse response = new GlobalClientStatsResponse();
        grayLogService.sendToGrayLog(24, 40, 0, "TestIp", "Common", 0, HTTPUtil.resolveIpAddress(request), "text1", "text2", "text3");
        return response;
    }



    @RequestMapping(value = "/sendPush", method = RequestMethod.POST)
    public
    @ResponseBody
    TestSendPushResponse sendPush(@RequestBody TestSendPushRequest request) {
        app42PushNotificationService.clientCustomMessage(request.getClientId(), request.getMessage());
           return new TestSendPushResponse();
    }



    @RequestMapping(value = "/sendNodeEvent", method = RequestMethod.POST)
    public
    @ResponseBody
    SendEventResponse sendPush(@RequestBody SendEventRequest request) {
        commonService.sendEvent(request.getMissionId(), request.isResult(), request.getAnswer());
        return new SendEventResponse();
    }




    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public Example test(@RequestBody InviteFriendRequest request) {
        return new Example();
    }




    //http://taxisto.ru/mail/bill/index.html
    @RequestMapping(value = "/paymentLetter", method = RequestMethod.POST)
    public
    @ResponseBody
    PaymentLetterResponse paymentLetter(@RequestBody PaymentLetterRequest request) throws IOException {
        return administrationService.paymentLetter(request.getSecurity_token(), request.getMissionId(), request.isToClient());
    }



    // with multiple support
    @RequestMapping(value = "/configuration/v6", method = RequestMethod.POST)
    public
    @ResponseBody
    SystemConfiguration_V6_Response getConfiguration_v6(@RequestBody SystemConfigurationRequest request) {
        SystemConfiguration_V6_Response response = new SystemConfiguration_V6_Response();
        Properties prop = propertiesRepository.findByPropName("use_map");
        String useMap = prop.getPropValue();
        response.setUseMap(useMap);
        long clientId = request.getClientId();
        String security_token = request.getSecurity_token();

        response.getSupportPhones().getPhones().putAll(administrationService.loadSupportPhones().getPhones());
        response.setHowLongWaitDriverAssign(60);

        Client client = clientRepository.findOne(clientId);
        boolean isCorporate = client.getMainClient() != null ? true: false;

            List<MissionRateInfoV2> rates = billingService.findMissionRatesV2(isCorporate);

                if(client != null && client.getToken()!= null && client.getToken().equals(security_token)) {
                    ServerStateInfoV2 serverStateInfo = administrationService.resolveStateForClientWithMultipleMissionSupport(client);
                    response.setServerStateInfo(serverStateInfo);

                    /* удаление тарифа LOW_COSTER если его нет в таблице private_tariff*/
                    rates = administrationService.showRatesByPrivateTariff(rates, client);
                    /* удаление тарифом, которые не соответсвуют версии приложения */
                    rates = administrationService.showRatesByVersionApp_V2(rates, client);

                    /* показываем клиенту опаздывает водитель или нет*/
                    MissionInfo missionInfo = serverStateInfo.getMissionInfo();
                    if(missionInfo!=null){
                        Mission mission = missionRepository.findOne(missionInfo.getId());
                         if(mission!=null){
                             response.setLate(mission.getIsLate());
                             Driver driver =  mission.getDriverInfo();
                             if(driver!=null){
                                 // водитель назначен на заказ
                                 DriverLocation location = locationRepository.findByDriverId(driver.getId());
                                 ItemLocation itemLocationDriver = ModelsUtils.toModel(driver.getId(), location.getLocation());
                                 response.setDriverCurrentLocation(itemLocationDriver);
                             }
                         }
                    }
                    /* устанавливаем признак корпоративности клиента */
                    if(client.getMainClient()!=null){
                        response.setCorporate(true);
                    }
                }else{
                    throw new CustomException(3, String.format("Tokens are not equals or client with id=%s not found", clientId));
                }
        response.getRates().addAll(rates);
        return response;
    }






    @RequestMapping(value = "/configuration/v5", method = RequestMethod.POST)
    public
    @ResponseBody
    SystemConfiguration_V5_Response getConfiguration_v5(@RequestBody SystemConfigurationRequest request) {
        SystemConfiguration_V5_Response response = new SystemConfiguration_V5_Response();
        Properties prop = propertiesRepository.findByPropName("use_map");
        String useMap = prop.getPropValue();
        response.setUseMap(useMap);
        long clientId = request.getClientId();
        long driverId = request.getDriverId();
        String security_token = request.getSecurity_token();

        Client client = clientRepository.findOne(clientId);
        boolean isCorporate = false;
        if(client!=null && client.getMainClient()!=null){
            isCorporate = true;
        }
        List<MissionRateInfoV2> rates = billingService.findMissionRatesV2(isCorporate);

        if (driverId != 0) {
            // нафиг это грузить в конфигурацию????
            AdministrationService.BookedDetails bookedDetails = administrationService.bookedMissionsDriver(driverId);
            AdministrationService.HistoryMissions history = administrationService.missionsHistoryDriver(driverId);
            response.setBookedNew(bookedDetails.getBookedNew());
            response.setBookedToMe(bookedDetails.getBookedToMe());
            response.setHistory(history.history);
        }
        response.getSupportPhones().getPhones().putAll(administrationService.loadSupportPhones().getPhones());
        response.setHowLongWaitDriverAssign(60);

            if (client != null) {
                if(client.getToken()!= null && client.getToken().equals(security_token)) {
                    ServerStateInfo serverStateInfo = administrationService.resolveState(clientId, driverId);
                    response.setServerStateInfo(serverStateInfo);

                    /* удаление тарифа BONUS если его нет в таблице private_tariff*/
                    rates = administrationService.showRatesByPrivateTariff(rates, client);
                    /* удаление тарифом, которые не соответсвуют версии приложения */
                    rates = administrationService.showRatesByVersionApp_V2(rates, client);

                    /* показываем клиенту опаздывает водитель или нет*/
                    if(client.getMission()!=null && client.getMission().getIsLate()!=null){
                        response.setLate(client.getMission().getIsLate());
                        Driver driver = client.getMission().getDriverInfo();
                        if(driver!=null){
                            // водитель назначен на заказ
                            DriverLocation location = locationRepository.findByDriverId(driver.getId());
                            ItemLocation itemLocationDriver = ModelsUtils.toModel(driver.getId(), location.getLocation());
                            response.setDriverCurrentLocation(itemLocationDriver);
                        }
                    }
                    /* устанавливаем признак корпоративности клиента */
                    if(client.getMainClient()!=null){
                        response.setCorporate(true);
                    }
                }else{
                    LOGGER.info("Tokens are not equal");
                }
            } else if(driverId!=0){
                ServerStateInfo serverStateInfo = administrationService.resolveState(clientId, driverId);
                response.setServerStateInfo(serverStateInfo);
           }
        response.getRates().addAll(rates);
        return response;
    }




    @RequestMapping(value = "/configuration/v4", method = RequestMethod.POST)
    public
    @ResponseBody
    SystemConfiguration_V4_Response getConfiguration_v4(@RequestBody SystemConfigurationRequest request) {
        SystemConfiguration_V4_Response response = new SystemConfiguration_V4_Response();
        Properties prop = propertiesRepository.findByPropName("use_map");
        String useMap = prop.getPropValue();
        response.setUseMap(useMap);
        long clientId = request.getClientId();
        long driverId = request.getDriverId();
        String security_token = request.getSecurity_token();
        Client client = clientRepository.findOne(clientId);
        boolean isCorporate = false;
        if(client!=null && client.getMainClient()!=null){
            isCorporate = true;
        }
        List<MissionRateInfoV2> rates = billingService.findMissionRatesV2(isCorporate);

        if (driverId != 0) {
            // нафиг это грузить в конфигурацию????
            AdministrationService.BookedDetails bookedDetails = administrationService.bookedMissionsDriver(driverId);
            AdministrationService.HistoryMissions history = administrationService.missionsHistoryDriver(driverId);
            response.setBookedNew(bookedDetails.getBookedNew());
            response.setBookedToMe(bookedDetails.getBookedToMe());
            response.setHistory(history.history);
        }

        response.getSupportPhones().getPhones().putAll(administrationService.loadSupportPhones().getPhones());
        response.setHowLongWaitDriverAssign(60);

            if (client != null) {
                if(client.getToken()!= null && client.getToken().equals(security_token)) {
                    ServerStateInfo serverStateInfo = administrationService.resolveState(clientId, driverId);
                    response.setServerStateInfo(serverStateInfo);

                    /* удаление тарифа BONUS если его нет в таблице private_tariff*/
                    rates = administrationService.showRatesByPrivateTariff(rates, client);

                    /* удаление тарифом, которые не соответсвуют версии приложения */
                    rates = administrationService.showRatesByVersionApp_V2(rates, client);

                    /* показываем клиенту опаздывает водитель или нет*/
                    if(client.getMission()!=null && client.getMission().getIsLate()!=null){
                        response.setLate(client.getMission().getIsLate());
                        Driver driver = client.getMission().getDriverInfo();
                           if(driver!=null){
                              // водитель назначен на заказ
                              DriverLocation location = locationRepository.findByDriverId(driver.getId());
                              ItemLocation itemLocationDriver = ModelsUtils.toModel(driver.getId(), location.getLocation());
                              response.setDriverCurrentLocation(itemLocationDriver);
                           }
                    }
                }else{
                    LOGGER.info("Tokens are not equal");
                }
            } else if(driverId!=0){
                ServerStateInfo serverStateInfo = administrationService.resolveState(clientId, driverId);
                response.setServerStateInfo(serverStateInfo);
            }

        response.getRates().addAll(rates);
        return response;
    }




    @RequestMapping(value = "/configuration/v3", method = RequestMethod.POST)
    public
    @ResponseBody
    SystemConfiguration_V3_Response getConfiguration_v3(@RequestBody SystemConfigurationRequest request) {
        return driverService.getConfiguration_v3(request.getClientId(), request.getDriverId(), request.getSecurity_token());
    }




    @RequestMapping(value = "/configuration/useMap", method = RequestMethod.POST)
    public
    @ResponseBody
    SystemConfiguration_V2_Response getConfiguration_New(@RequestBody SystemConfigurationRequest request) {
        SystemConfiguration_V2_Response response = new SystemConfiguration_V2_Response();

        Properties prop = propertiesRepository.findByPropName("use_map");
        String useMap = prop.getPropValue();
        response.setUseMap(useMap);

        long clientId = request.getClientId();
        long driverId = request.getDriverId();
        String security_token = request.getSecurity_token();

        Client client = clientRepository.findOne(clientId);
        boolean isCorporate = false;
        if(client!=null && client.getMainClient()!=null){
            isCorporate = true;
        }
        List<MissionRateInfo> rates = billingService.findMissionRates(isCorporate);

        if (driverId != 0) {
            // нафиг это грузить в конфигурацию????
            AdministrationService.BookedDetails bookedDetails = administrationService.bookedMissionsDriver(driverId);
            AdministrationService.HistoryMissions history = administrationService.missionsHistoryDriver(driverId);
            response.setBookedNew(bookedDetails.getBookedNew());
            response.setBookedToMe(bookedDetails.getBookedToMe());
            response.setHistory(history.history);
        }
        response.getSupportPhones().getPhones().putAll(administrationService.loadSupportPhones().getPhones());
        response.setHowLongWaitDriverAssign(60);


            if (client != null) {
                if(client.getToken()!= null && client.getToken().equals(security_token)) {
                    ServerStateInfo serverStateInfo = administrationService.resolveState(clientId, driverId);
                    response.setServerStateInfo(serverStateInfo);

                    /* удаление тарифа BONUS если его нет в таблице private_tariff*/
                    rates = administrationService.showRatesByPrivateTariff_For_Old_Version(rates, client);

                    /* удаление тарифом, которые не соответсвуют версии приложения */
                    rates = administrationService.showRatesByVersionApp(rates, client);
                }else{
                    LOGGER.info("Tokens are not equal");
                }
            }else if(driverId!=0){
                ServerStateInfo serverStateInfo = administrationService.resolveState(clientId, driverId);
                response.setServerStateInfo(serverStateInfo);
            }
        response.getRates().addAll(rates);
          return response;
    }







    @RequestMapping(value = "/configuration", method = RequestMethod.POST)
    public
    @ResponseBody
    SystemConfigurationResponse getConfiguration(@RequestBody SystemConfigurationRequest request) {
        SystemConfigurationResponse response = new SystemConfigurationResponse();

        long clientId = request.getClientId();
        long driverId = request.getDriverId();
        String security_token = request.getSecurity_token();

        Client client = clientRepository.findOne(clientId);
        boolean isCorporate = false;
        if(client!=null && client.getMainClient()!=null){
            isCorporate = true;
        }
        List<MissionRateInfo> rates = billingService.findMissionRates(isCorporate);

        if (driverId != 0) {
            AdministrationService.BookedDetails bookedDetails = administrationService.bookedMissionsDriver(driverId);
            AdministrationService.HistoryMissions history = administrationService.missionsHistoryDriver(driverId);
            response.setBookedNew(bookedDetails.getBookedNew());
            response.setBookedToMe(bookedDetails.getBookedToMe());
            response.setHistory(history.history);
        }

        response.getSupportPhones().getPhones().putAll(administrationService.loadSupportPhones().getPhones());
        response.setHowLongWaitDriverAssign(60);


        if (client != null) {
                if(client.getToken()!= null && client.getToken().equals(security_token)) {
                     ServerStateInfo serverStateInfo = administrationService.resolveState(clientId, driverId);
                     response.setServerStateInfo(serverStateInfo);

                     /* удаление тарифа BONUS если его нет в таблице private_tariff*/
                     rates = administrationService.showRatesByPrivateTariff_For_Old_Version(rates, client);

                     /* удаление тарифом, которые не соответсвуют версии приложения */
                     rates = administrationService.showRatesByVersionApp(rates, client);
                }else{
                    LOGGER.info("Tokens are not equal");
                }
        } else if(driverId!=0){
            ServerStateInfo serverStateInfo = administrationService.resolveState(clientId, driverId);
            response.setServerStateInfo(serverStateInfo);
        }

        response.getRates().addAll(rates);
        return response;
    }




    @RequestMapping(value = "/checkVersion", method = RequestMethod.POST)
    public
    @ResponseBody
    CheckVersionResponse checkVersion(@RequestBody CheckVersionRequest request) {
           return administrationService.checkVersion(request.getVersion(), request.getClientType());
    }



    @RequestMapping(value = "/checkNewsInVersion", method = RequestMethod.POST)
    public
    @ResponseBody
    CheckNewsInVersionResponse checkNewsInVersion(@RequestBody CheckNewsInVersionRequest request) {
        return administrationService.checkNewsInVersion(request.getVersion(), request.getClientType());
    }



    @RequestMapping(value = "/bla", method = RequestMethod.GET)
    public
    @ResponseBody
    StringsListResponse secTest() {
        StringsListResponse response = new StringsListResponse();
          if(true){
              throw new CustomException(20, "Custom exception!");
          }
          return response;
    }





    @RequestMapping(value = "/promoStat", method = RequestMethod.GET)
    public
    @ResponseBody
    String getPromoStat(ModelMap model) {
        StringsListResponse response = new StringsListResponse();

        DateTime s = new DateTime();
        DateTime e = new DateTime();
        DateTime d= e.plusDays(1);

        CountClientHelper countClientHelper =  administrationService.getCountClientsByPeriod(s.withTimeAtStartOfDay().getMillis(), d.withTimeAtStartOfDay().getMillis());
        String propertySentCode = administrationService.getPropertySentCode("client_sent_code");
        // сколько можно отправлять промокодов в сутки
        int availableCountPromoCodeSendByDay = Integer.parseInt(commonService.getPropertyValue("send_count_promo_code_of_day"));
        // сколько отправлено за текущие сутки вообще всеми клиентами
        int countClientSentPromoCodeByDay = administrationService.getCountSentPromoCodeByDay();
        int activateAllByDay = billingService.getCountAllActivatePromoCodeByDay();

        StringBuilder stringBuilder = new StringBuilder();


        if(propertySentCode.equals("1")){
             stringBuilder.append("Promo code send: " + "<font color=\"green\"><b>open</b></font>" + "<br>");
          }else{
             stringBuilder.append("Promo code send: "+ "<font color=\"red\"><b>close</b></font>"+"<br>");
          }

        CountClientHelper allClientCountHelper = administrationService.getCountClientsByAllPeriod();

        stringBuilder.append("Limit send promo code for day: <b>"+availableCountPromoCodeSendByDay+"</b><br>");
        stringBuilder.append("Sent promo code today: <b>" + countClientSentPromoCodeByDay + "</b><br>");
        stringBuilder.append("Sent promo code all period: <b>" + (administrationService.getCountSentPromoCodeByAllDay()) + "</b><br>"); // 3228  -730
        //stringBuilder.append("Limit activate for day: <b>" + propClientAvailableActivateSumPromoCode + "</b><br>");
        stringBuilder.append("Activated promo code today: <b>" + activateAllByDay + "</b><br>");
        stringBuilder.append("Activated promo all period: <b>" + (billingService.getCountAllActivatePromoCodeByAllDay()) + "</b><br>");
        stringBuilder.append("Total count client: <b>" + billingService.getCountAllClient() + "</b><br>"); //allClientCountHelper.getCountAll()
        stringBuilder.append("Total count Android client: <b>" + allClientCountHelper.getCountClientAndroid() + "</b><br>");
        stringBuilder.append("Total count Apple client: <b>" + allClientCountHelper.getCountClientApple() + "</b><br>");
        stringBuilder.append("Total count Terminal client: <b>" + allClientCountHelper.getCountClientTerminal() + "</b><br>");
        stringBuilder.append("Total count other client: <b>" + allClientCountHelper.getCountClientOther() + "</b><br>");
        stringBuilder.append("Count Apple client for day: <b>" + countClientHelper.getCountClientApple() + "</b><br>");
        stringBuilder.append("Count Android client for day: <b>" + countClientHelper.getCountClientAndroid() + "</b><br>");
        stringBuilder.append("Count Terminal client for day: <b>" + countClientHelper.getCountClientTerminal() + "</b><br>");
        stringBuilder.append("Count other client for day: <b>" + countClientHelper.getCountClientOther() + "</b><br>");

        return stringBuilder.toString();
    }



    // token +, отправка смс только клиентом, для водителя сделать аналогичный метод
    @RequestMapping(value = "/sendsms", method = RequestMethod.POST)
    public
    @ResponseBody
    SendSMSResponse sendSMS(@RequestBody SendSMSRequest request) {
        SendSMSResponse response = new SendSMSResponse();
        Client client = clientRepository.findOne(request.getSenderId());
        String security_token = request.getSecurity_token();
        if(client!=null){
            if (client.getToken() != null && client.getToken().equals(security_token)) {
                serviceSMSNotification.sendCustomSMS(request.getPhone(), request.getMessage(), "");
            }else{
                LOGGER.info("Tokens are not equal");
            }
        }
        return response;
    }



    @RequestMapping(value = "/smsSendDefault", method = RequestMethod.POST)
    public
    @ResponseBody
    SmsSendDefaultResponse smsSendDefault(@RequestBody SmsSendDefaultRequest request) {
        SmsSendDefaultResponse response = administrationService.smsSendDefault(request.getPhone(), request.getMessage());
         return response;
    }



    @RequestMapping(value = "/generatePromoCodes", method = RequestMethod.POST)
    public
    @ResponseBody
    GeneratePromoCodesResponse generatePromoCodes(@RequestBody GeneratePromoCodesRequest request) {
        GeneratePromoCodesResponse response = administrationService.generatePromoCodes(request.getCountPromoCode(), request.getCountSymbols(), request.getAmount(), request.getAvailableUsedCount(), request.getChannel());
          return response;
    }



    @RequestMapping(value = "/generateExclusivePromoCodes", method = RequestMethod.POST)
    public
    @ResponseBody
    GeneratePromoCodesResponse generateExclusivePromoCodes(@RequestBody GeneratePromoCodesRequest request) {
        GeneratePromoCodesResponse response = administrationService.generateExclusivePromoCodes(request.getCountPromoCode(), request.getCountSymbols(), request.getAvailableUsedCount(), request.getLifetimeDaysAfterActivation());
          return response;
    }



    // поиск миссия по айди
    @RequestMapping(value = "/missionFind", method = RequestMethod.POST) // , produces = {MediaType.APPLICATION_JSON_VALUE}
    @Transactional
    public
    @ResponseBody
    MissionFindResponse missionFind(@RequestBody MissionFindRequest request) {
          return administrationService.missionFind(request.getMissionId());
    }





    @RequestMapping(value = "/invite", method = RequestMethod.POST)
    public
    @ResponseBody
    InviteFriendResponse sendSMS(@RequestBody InviteFriendRequest request) {
        InviteFriendResponse response = new InviteFriendResponse();
        serviceSMSNotification.inviteFriend(request.getPhone());
        return response;
    }





    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public
    @ResponseBody
    LogoutResponse logout(@RequestBody LogoutRequest request) {
        LogoutResponse response = new LogoutResponse();
        if (request.getClientId() > 0){
            administrationService.logoutClient(request.getClientId(), request.isForce(), request.getDeviceInfo());
        }
        if (request.getDriverId() > 0){
            driverService.logoutDriver(request.getDriverId(), request.isForce(), request.getDeviceInfo());
        }
        return response;
    }





    @RequestMapping(value = "/initialize", method = RequestMethod.GET)
    public
    @ResponseBody
    StringsListResponse buildInitialData() throws IOException, EmailException {
        StringsListResponse response = new StringsListResponse();

        Account account;
        account = registerCompanyAccount();
        account = registerBonusAccount();

        ///// build drivers /////////////

        /*
        Driver driver;
        driver = registerDriverValya();
        driver = registerDriverSergey();
        driver = registerDriverDimaSmirnov();
        driver = registerDriverNataliya();
        driver = registerDriverSvetlana();
        driver = registerDriverAnton();
        driver = registerDriverMax();
        driver = registerDriverPetr();
        driver = registerDriverRoman();
        driver = registerDriverJulia();
        driver = registerDriverAlex();
        */

        ///////////////////////////////



        //String smsCode;
        String phone;

        phone = registerClientSergery().getPhone();

        administrationService.confirmClient(phone,getSmsCodeClient(phone));

        phone = registerClientSvetlana().getPhone();
        administrationService.confirmClient(phone, getSmsCodeClient(phone));

        phone = registerClientValya().getPhone();
        administrationService.confirmClient(phone,getSmsCodeClient(phone));

        phone = registerClientDima().getPhone();
        administrationService.confirmClient(phone, getSmsCodeClient(phone));

        phone = registerClientRoma().getPhone();
        administrationService.confirmClient(phone, getSmsCodeClient(phone));

        phone = registerClientVoitov().getPhone();
        administrationService.confirmClient(phone, getSmsCodeClient(phone));

        phone = registerClientDimaSmirnov().getPhone();
        administrationService.confirmClient(phone, getSmsCodeClient(phone));

        phone = registerClientNata().getPhone();
        administrationService.confirmClient(phone, getSmsCodeClient(phone));

        phone = registerClientJulia().getPhone();
        administrationService.confirmClient(phone, getSmsCodeClient(phone));


        // create user test
          for(int i=1;i<=8;i++){
              phone = registerClientTest(i).getPhone();
              administrationService.confirmClient(phone, getSmsCodeClient(phone));
          }



        registerDispatcher();

//        WebUserModel webUserModel = registerDispatcher();
//        administrationService.registerDispatcher();
//        smsCode = registerClientWeb().getSmsCode();
//        administrationService.confirmClient(smsCode);



//        Mission mission = new Mission();
//        mission.setId((long)33);
//        DriverCashFlow driverCashFlow = new DriverCashFlow(driver, mission, 2, 300);
//        administrationService.fillCash(driverCashFlow);

        MissionRate missionRate;
        missionRate = administrationService.createMissionRate(buildTestMissionRate());

        // убрал
        List<MissionRateInfo> rates = findMissionRates();

        List<ServicePriceInfo> services = getServicePrices();
        administrationService.addServicesPrices(services);


        //List<AutoClassRateInfo> autoClassRateInfoList = getAutoClassRates();
        //administrationService.addAutoClassPrices(autoClassRateInfoList);


        return response;
    }




     public String getSmsCodeClient(String phone){
         Client smsCodeClient = clientRepository.findByPhone(phone);
             return smsCodeClient.getSmsCode();
     }






//    private Client registerClientWeb() {
//        ClientInfo clientInfo = CommonHelpers.buildClientInfoWeb();
//        DeviceInfoModel deviceInfoModel = buildDeviceInfo(3, "");
//        Client client = administrationService.registerClient(clientInfo, deviceInfoModel);
//        return client;
//    }

    private MissionRateInfo buildTestMissionRate() {
        List<MissionRateInfo> rates = findMissionRates();
        return rates.get(0);
    }





    public List<MissionRateInfo> findMissionRates() {
        MissionRateInfo rateInfo = new MissionRateInfo();
        rateInfo.setId(ru.trendtech.domain.AutoClass.STANDARD.getValue());
        rateInfo.setName("Стандарт");
        rateInfo.setFreeWaitingTime(5);
        rateInfo.setPriceMinimal(150);
        rateInfo.setPriceMinute(1);
        rateInfo.setPriceStop(5);
        rateInfo.setServicesPrices(getServicePrices());
        rateInfo.setAutoClassRateInfos(getAutoClassRates());

        return Arrays.asList(rateInfo);

    }





    private List<AutoClassRateInfo> getAutoClassRates() {
        ArrayList<AutoClassRateInfo> result = new ArrayList<>();

        result.add(new AutoClassRateInfo(ru.trendtech.domain.AutoClass.STANDARD.getValue(), 150, 3, 12, 350,
                10, 5, 16)); //350 6 параметр время бесплатного ожидания
        result.add(new AutoClassRateInfo(ru.trendtech.domain.AutoClass.COMFORT.getValue(), 200, 3, 15, 450,
                10, 5, 20)); // 450
        result.add(new AutoClassRateInfo(ru.trendtech.domain.AutoClass.BUSINESS.getValue(), 300, 3, 20, 700,
                10, 10, 25)); // 700

        return result;
    }

    private List<ServicePriceInfo> getServicePrices() {
          ArrayList<ServicePriceInfo> result = new ArrayList<>();

        String picChildActiveUrl = profilesResourcesService.saveServicePicture("AAAAABJRU5ErkJggg==", "active_child");

        String picChildNotActiveUrl = profilesResourcesService.saveServicePicture("B5TBddwAAAABJRU5ErkJggg==", "not_active_child");

        String picAnimalActiveUrl = profilesResourcesService.saveServicePicture("iVBORw0KGgoAAAANSUhEUgAAAGAAAABSBaYfAAAAAElFTkSuQmCC", "active_animal");

        String picAnimalNotActiveUrl = profilesResourcesService.saveServicePicture("iVBORw0KGgoAAAANSUhEUgAAAGAAAABSE", "not_active_animal");

        String picBagageActiveUrl = profilesResourcesService.saveServicePicture("hrkLbRLAAAAAElFTkSuQmCC", "active_baggage");

        String picBagageNotActiveUrl = profilesResourcesService.saveServicePicture("gAAAABJRU5ErkJggg==", "not_active_baggage");

        String picTablActiveUrl = profilesResourcesService.saveServicePicture("5CYII=", "active_meeting");

        String picTablNotActiveUrl = profilesResourcesService.saveServicePicture("R/SoKF/DAAAAAElFTkSuQmCC", "not_active_meeting");

        result.add(new ServicePriceInfo(MissionService.CHILDREN.getValue(), 0, picChildActiveUrl,picChildNotActiveUrl,"Если вы едете с ребенком до 12 лет, укажите это. Мы отправим к вам Таксисто с удерживающим устройством ФЭСТ","Дети"));
        result.add(new ServicePriceInfo(MissionService.BAGGAGE.getValue(), 50,picBagageActiveUrl,picBagageNotActiveUrl,"Багаж, который не вошел в багажник и уютно разместился в салоне","Багаж в салоне"));
        result.add(new ServicePriceInfo(MissionService.ANIMAL.getValue(), 100,picAnimalActiveUrl,picAnimalNotActiveUrl,"Для перевозки вашего четвероного друга, необходимо наличие подстилки на сидение","Провоз больших животных"));
        result.add(new ServicePriceInfo(MissionService.MEETING_WITH_SIGN.getValue(), 100,picTablActiveUrl,picTablNotActiveUrl,"Таксисто встретит вас и ваших гостей прямо у выхода из зоны выдачи багажа с табличкой с именем пассажира","Встреча с табличкой"));
        result.add(new ServicePriceInfo(MissionService.SMALL_ANIMAL.getValue(), 50,picAnimalActiveUrl,picAnimalNotActiveUrl,"Таксисто перевезет вашего питомца, если он будет в специальном контейнере.","Провоз маленьких животных"));

        return result;
    }

    private Account registerCompanyAccount() {
        Account account = billingService.createCompanyAccount();
        return account;
    }

    private Account registerBonusAccount() {
        Account account = billingService.createBonusAccount();
        return account;
    }

    /*
    private Driver registerDriverValya() {
        DriverInfo driverInfo = CommonHelpers.buildDriverInfoValya();
        DeviceInfoModel deviceInfoModel = buildDeviceInfo(1, "APA91bFPMBg_UDg3fvy-JiO6W0zVd-uOV_9Z7LVo2rUqEfnB2x6MUfgQJ10kTMwFfJ7D6w3whZmd4mD-QGm12mrSlUS8I6LW44iOv8LIPY0aS-0UnsgIFWeg603C_1Kyd2nwgKwzbLXkiSNiIC5jJU4M5TF_QulIoQ");
        Driver driver = administrationService.registerDriver(driverInfo, deviceInfoModel);
        return driver;
    }

    private Driver registerDriverMax() {
        DriverInfo driverInfo = CommonHelpers.buildDriverInfoDefault();
        DeviceInfoModel deviceInfoModel = buildDeviceInfo(1, "APA91bFbUTora2n3uEACS--kGbDk8N4nV_-s1jS3-vQXmiVRgsw1dNz5ktDanX9DWUTyqJrMpF0jw72HUmFh2xw4tSGY8LcosqwCXbh_Wgvx7KXmQo1zzmhPkhuVsPdezkZeK4lCVaFBKwrFxbw7QlZZVYJCTx1uHg");
        Driver driver = administrationService.registerDriver(driverInfo, deviceInfoModel);
        return driver;
    }


    private Driver registerDriverPetr() {
        DriverInfo driverInfo = CommonHelpers.buildDriverInfoPetr();
        DeviceInfoModel deviceInfoModel = buildDeviceInfo(1, "APA91bFbUTora2n3uEACS--kGbDk8N4nV_-s1jS3-vQXmiVRgsw1dNz5ktDanX9DWUTyqJrMpF0jw72HUmFh2xw4tSGY8LcosqwCXbh_Wgvx7KXmQo1zzmhPkhuVsPdezkZeK4lCVaFBKwrFxbw7QlZZVYJCTx1uHg");
        Driver driver = administrationService.registerDriver(driverInfo, deviceInfoModel);
        return driver;
    }


    private Driver registerDriverRoman() {
        DriverInfo driverInfo = CommonHelpers.buildDriverInfoRoman();
        DeviceInfoModel deviceInfoModel = buildDeviceInfo(1, "anX9DWUTyqJrMpF0jw72HUmFh2xw4tSGY8LcosqwCXbh_Wgvx7KXmQo1zzmhPkhuVsPdezkZeK4lCVaFBKwrFxbw7QlZZVYJCTx1uHg");
        Driver driver = administrationService.registerDriver(driverInfo, deviceInfoModel);
        return driver;
    }

    private Driver registerDriverJulia() {
        DriverInfo driverInfo = CommonHelpers.buildDriverInfoJulia();
        DeviceInfoModel deviceInfoModel = buildDeviceInfo(1, "anX9DWUTyqJrMpF0jw72HUmFh2xw4tSGY8LcosqwCXbh_Wgvx7KXmQo1zzmhPkhuVsPdezkZeK4lCVaFBKwrFxbw7QlZZVYJCTx1uHg");
        Driver driver = administrationService.registerDriver(driverInfo, deviceInfoModel);
        return driver;
    }

    private Driver registerDriverAlex() {
        DriverInfo driverInfo = CommonHelpers.buildDriverInfoAlex();
        DeviceInfoModel deviceInfoModel = buildDeviceInfo(1, "anX9DWUTyqJrMpF0jw72HUmFh2xw4tSGY8LcosqwCXbh_Wgvx7KXmQo1zzmhPkhuVsPdezkZeK4lCVaFBKwrFxbw7QlZZVYJCTx1uHg");
        Driver driver = administrationService.registerDriver(driverInfo, deviceInfoModel);
        return driver;
    }


    private Driver registerDriverSergey() {
        DriverInfo driverInfo = CommonHelpers.buildDriverInfoSergey();
        DeviceInfoModel deviceInfoModel = buildDeviceInfo(1, "APA91bFbUTora2n3uEACS--kGbDk8N4nV_-s1jS3-vQXmiVRgsw1dNz5ktDanX9DWUTyqJrMpF0jw72HUmFh2xw4tSGY8LcosqwCXbh_Wgvx7KXmQo1zzmhPkhuVsPdezkZeK4lCVaFBKwrFxbw7QlZZVYJCTx1uHg");
        Driver driver = administrationService.registerDriver(driverInfo, deviceInfoModel);
        return driver;
    }

    private Driver registerDriverDimaSmirnov() {
        DriverInfo driverInfo = CommonHelpers.buildDriverInfoDimaSmirnov();
        DeviceInfoModel deviceInfoModel = buildDeviceInfo(1, "APA91bFbUTora2n3uEACS--kGbDk8N4nV_-s1jS3-vQXmiVRgsw1dNz5ktDanX9DWUTyqJrMpF0jw72HUmFh2xw4tSGY8LcosqwCXbh_Wgvx7KXmQo1zzmhPkhuVsPdezkZeK4lCVaFBKwrFxbw7QlZZVYJCTx1uHg");
        Driver driver = administrationService.registerDriver(driverInfo, deviceInfoModel);
        return driver;
    }

    private Driver registerDriverNataliya() {
        DriverInfo driverInfo = CommonHelpers.buildDriverInfoNataliya();
        DeviceInfoModel deviceInfoModel = buildDeviceInfo(1, "APA91bFbUTora2n3uEACS--kGbDk8N4nV_-s1jS3-vQXmiVRgsw1dNz5ktDanX9DWUTyqJrMpF0jw72HUmFh2xw4tSGY8LcosqwCXbh_Wgvx7KXmQo1zzmhPkhuVsPdezkZeK4lCVaFBKwrFxbw7QlZZVYJCTx1uHg");
        Driver driver = administrationService.registerDriver(driverInfo, deviceInfoModel);
        return driver;
    }

    private Driver registerDriverSvetlana() {
        DriverInfo driverInfo = CommonHelpers.buildDriverInfoSvetlana();
        DeviceInfoModel deviceInfoModel = buildDeviceInfo(1, "APA91bFbUTora2n3uEACS--kGbDk8N4nV_-s1jS3-vQXmiVRgsw1dNz5ktDanX9DWUTyqJrMpF0jw72HUmFh2xw4tSGY8LcosqwCXbh_Wgvx7KXmQo1zzmhPkhuVsPdezkZeK4lCVaFBKwrFxbw7QlZZVYJCTx1uHg");
        Driver driver = administrationService.registerDriver(driverInfo, deviceInfoModel);
        return driver;
    }

    private Driver registerDriverAnton() {
        DriverInfo driverInfo = CommonHelpers.buildDriverInfoAnton();
        DeviceInfoModel deviceInfoModel = buildDeviceInfo(1, "APA91bFbUTora2n3uEACS--kGbDk8N4nV_-s1jS3-vQXmiVRgsw1dNz5ktDanX9DWUTyqJrMpF0jw72HUmFh2xw4tSGY8LcosqwCXbh_Wgvx7KXmQo1zzmhPkhuVsPdezkZeK4lCVaFBKwrFxbw7QlZZVYJCTx1uHg");
        Driver driver = administrationService.registerDriver(driverInfo, deviceInfoModel);
        return driver;
    }
    */

    private Client registerClientValya() {
        ClientInfo clientInfo = CommonHelpers.buildClientInfoValya();
        DeviceInfoModel deviceInfoModel = buildDeviceInfo(1, "APA91bGm5qYVxsvIdhh8LjLfi39sMaw01KoiYQQiH_tBVE_bHHU_a5nFcrtQgohe-FMCPwBKbaTtwyydzeYQcPubEjknu7hxuMQuGnpqTc9ogfTvuOfOSz_6Ubf3KmvRUG_DHGaBHaifptDTPjTjmQgWDd6d3ocWvA");
        Client client = administrationService.registerClient(clientInfo, deviceInfoModel, "");
        return client;
    }


    private Client registerClientSvetlana() {
        ClientInfo clientInfo = CommonHelpers.buildClientInfoSvetlana();
        DeviceInfoModel deviceInfoModel = buildDeviceInfo(1, "99b2601a07b63c217f6566351ebeddf42e04da8426577759e931d289bc256d75");
        Client client = administrationService.registerClient(clientInfo, deviceInfoModel, "");
        return client;
    }


    private Client registerClientSergery() {
        ClientInfo clientInfo = CommonHelpers.buildClientInfoSergey();
        DeviceInfoModel deviceInfoModel = buildDeviceInfo(2, "f65877254fd8d4f723c6be4171648dd055a3c01d52bf342e3c1b33074b233f4a");
        Client client = administrationService.registerClient(clientInfo, deviceInfoModel, "");
        return client;
    }


    private Client registerClientDima() {
        ClientInfo clientInfo = CommonHelpers.buildClientInfoDima();
        DeviceInfoModel deviceInfoModel = buildDeviceInfo(2, "beabd16f97e0450b12a431a29a8f80c165a3be79dd7e55d3e0679436859bc987");
        Client client = administrationService.registerClient(clientInfo, deviceInfoModel, "");
        return client;
    }

    private Client registerClientDimaSmirnov() {
        ClientInfo clientInfo = CommonHelpers.buildClientInfoDimaSmirnov();
        DeviceInfoModel deviceInfoModel = buildDeviceInfo(2, "beabd16f97e0450b12a431a29a8f80c165a3be79dd7e55d3e0679436859bc987");
        Client client = administrationService.registerClient(clientInfo, deviceInfoModel, "");
        return client;
    }

    private WebUser registerDispatcher() {
        WebUserModel webUserModel = CommonHelpers.buildDispatcher();//buildClientInfoDimaSmirnov();
        WebUser webUser = administrationService.registerDispatcher(webUserModel);
//        WebUser webUser = new WebUser();
        return webUser;
    }

    private Client registerClientRoma() {
        ClientInfo clientInfo = CommonHelpers.buildClientInfoRoma();
        DeviceInfoModel deviceInfoModel = buildDeviceInfo(2, "beabd16f97e0450b12a431a29a8f80c165a3be79dd7e55d3e0679436859bc987");
        Client client = administrationService.registerClient(clientInfo, deviceInfoModel, "");
        return client;
    }

    private Client registerClientVoitov(){
        ClientInfo clientInfo = CommonHelpers.buildClientInfoVoitov();
        DeviceInfoModel deviceInfoModel = buildDeviceInfo(2, "beabd16f97e0450b12a431a29a8f80c165a3be79dd7e55d3e0679436859bc987");
        Client client = administrationService.registerClient(clientInfo, deviceInfoModel, "");
        return client;
    }

    private Client registerClientNata(){
        ClientInfo clientInfo = CommonHelpers.buildClientInfoNata();
        DeviceInfoModel deviceInfoModel = buildDeviceInfo(2, "beabd16f97e0450b12a431a29a8f80c165a3be79dd7e55d3e0679436859bc987");
        Client client = administrationService.registerClient(clientInfo, deviceInfoModel, "");
        return client;
    }

    private Client registerClientJulia(){
        ClientInfo clientInfo = CommonHelpers.buildClientInfoJulia();
        DeviceInfoModel deviceInfoModel = buildDeviceInfo(2, "beabd16f97e0450b12a431a29a8f80c165a3be79dd7e55d3e0679436859bc987");
        Client client = administrationService.registerClient(clientInfo, deviceInfoModel, "");
        return client;
    }



    private Client registerClientTest(int number){
        ClientInfo clientInfo = CommonHelpers.buildClientInfoTest(number);
        DeviceInfoModel deviceInfoModel = buildDeviceInfo(2, "beabd16f97e0450b12a431a29a8f80c165a3be79dd7e55d3e0679436859bc987");
          Client client = administrationService.registerClient(clientInfo, deviceInfoModel, "");
           return client;
    }



    private DeviceInfoModel buildDeviceInfo(int type, String token) {
        DeviceInfoModel deviceInfoModel = new DeviceInfoModel();
        deviceInfoModel.setDeviceType(type);
        deviceInfoModel.setNewToken(token);
        return deviceInfoModel;
    }


    @RequestMapping(value = "/countries", method = RequestMethod.GET)
    public
    @ResponseBody
    StringsListResponse getCountries() {
        StringsListResponse response = new StringsListResponse();
        response.getValues().add("Россия");
        return response;
    }



}
