package ru.trendtech.controllers.mobile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import org.apache.commons.mail.EmailException;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.trendtech.common.mobileexchange.model.client.*;
import ru.trendtech.common.mobileexchange.model.common.*;
import ru.trendtech.common.mobileexchange.model.common.billing.paymobile.RegisterOrderClientWithTipRequest;
import ru.trendtech.common.mobileexchange.model.common.billing.paymobile.RegisterOrderClientWithTipResponse;
import ru.trendtech.common.mobileexchange.model.common.push.DeviceInfoModel;
import ru.trendtech.common.mobileexchange.model.common.rates.MissionRateInfo;
import ru.trendtech.common.mobileexchange.model.common.sms.SMSCodeRequest;
import ru.trendtech.common.mobileexchange.model.common.sms.SMSCodeResponse;
import ru.trendtech.common.mobileexchange.model.common.support.SupportPhonesRequest;
import ru.trendtech.common.mobileexchange.model.common.support.SupportPhonesResponse;
import ru.trendtech.common.mobileexchange.model.driver.TestRequest;
import ru.trendtech.common.mobileexchange.model.driver.TestResponse;
import ru.trendtech.common.mobileexchange.model.web.AutoClassPriceRequest;
import ru.trendtech.domain.*;
import ru.trendtech.domain.AutoClass;
import ru.trendtech.domain.Properties;
import ru.trendtech.domain.billing.PaymentType;
import ru.trendtech.models.ModelsUtils;
import ru.trendtech.repositories.*;
import ru.trendtech.services.TimeService;
import ru.trendtech.services.administration.AdministrationService;
import ru.trendtech.services.billing.BillingService;
import ru.trendtech.services.client.ClientService;
import ru.trendtech.services.common.CommonService;
import ru.trendtech.services.resources.ProfilesResourcesService;
import ru.trendtech.services.sms.ServiceSMSNotification;
import ru.trendtech.services.validate.ValidatorService;
import ru.trendtech.utils.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * Created by max on 06.02.14.
 */
@Controller
@RequestMapping("/client")
@Transactional
class ClientController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientController.class);
    //@Autowired
    //private NotificationsService notificationsService;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private MissionRepository missionRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private TimeService timeService;
    @Autowired
    private BillingService billingService;
    @Autowired
    private ProfilesResourcesService profilesResourcesService;
    @Autowired
    private AdministrationService administrationService;
    @Autowired
    private ServiceSMSNotification serviceSMSNotification;
    @Autowired
    private PrivateTariffRepository privateTariffRepository;
    @Autowired
    private PromoCodeExclusiveRepository promoCodeExclusiveRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private PropertiesRepository propertiesRepository;
    @Autowired
    private CommonService commonService;
    @Autowired
    private ReasonRepository reasonRepository;
    @Autowired
    private ValidatorService validatorService;




    // вывод баланса бонусов и баланса главной корп. учетной записи
    @RequestMapping(value = "/moneyAmount", method = RequestMethod.POST)
    @ResponseBody
    public ClientMoneyAmountResponse getMoneyAmount(@RequestBody ClientMoneyAmountRequest request){
        if(!validatorService.validateUser(request.getRequesterId(), request.getSecurity_token(), 1)){
            throw new CustomException(3, "Tokens are not equals");
        }
        return clientService.getMoneyAmount(request.getRequesterId());
    }



    @RequestMapping(value = "/emailUnsubscribe", method = RequestMethod.POST)
    @ResponseBody
    public EmailUnsubscribeResponse emailUnsubscribe(@RequestBody EmailUnsubscribeRequest request, HttpServletRequest servletRequest) throws JSONException {
        long clientId = request.getRequesterId();
        Client client = clientRepository.findOne(clientId);
        if(client == null){
            throw new CustomException(1, "Клиент не найден");
        }
        if(client.getToken() == null || !client.getToken().equals(request.getSecurity_token())) {
            throw new CustomException(3, "Несовпадение токенов");
        }
        clientService.emailUnsubscribe(client, request.isUnsubscribe(), HTTPUtil.resolveIpAddress(servletRequest));
            return new EmailUnsubscribeResponse();
    }



    @RequestMapping(value = "/mission/cancel", method = RequestMethod.POST)
    @ResponseBody
    public CancelMissionResponse cancel(@RequestBody CancelMissionRequest request, HttpServletRequest servletRequest) throws JSONException {
        long clientId = request.getInitiatorId();
        long missionId = request.getMissionId();
        Client client = clientRepository.findOne(clientId);
        if(client == null){
            throw new CustomException(1, "Клиент не найден");
        }
        if(client.getToken() == null || !client.getToken().equals(request.getSecurity_token())) {
            throw new CustomException(3, "Несовпадение токенов");
        }
        clientService.cancelMissionByClient(missionId, clientId, reasonRepository.findOne(2L), HTTPUtil.resolveIpAddress(servletRequest));
            return new CancelMissionResponse();
    }



    // token +
    @RequestMapping(value = "/mission/history", method = RequestMethod.POST)
    public
    @ResponseBody
    TripsHistoryResponse findTripsHistory(@RequestBody TripsHistoryRequest request) throws JsonProcessingException {
        TripsHistoryResponse response = new TripsHistoryResponse();
        AdministrationService.HistoryMissions history = clientService.missionsHistoryClient(request.getRequesterId(), request.getSecurity_token());//, request.getNumberPage(), request.getCount()
        response.setHistory(history.history);
        response.setBooked(history.booked);
        return response;
    }



    @RequestMapping(value = "/mission/history/str", method = RequestMethod.POST)
    public
    @ResponseBody
    TripsHistorySTRResponse findTripsHistoryForSTR(@RequestBody TripsHistoryRequest request) throws JsonProcessingException {
        TripsHistorySTRResponse response = new TripsHistorySTRResponse();
        AdministrationService.HistoryMissions_STR history = clientService.missionsHistoryClientSTR(request.getRequesterId(), request.getSecurity_token());
        response.setHistory(history.history);
        response.setBooked(history.booked);
        return response;
    }



    @RequestMapping(value = "/turnOnWiFi", method = RequestMethod.POST) // , produces = {MediaType.APPLICATION_JSON_VALUE}
    @Transactional
    public
    @ResponseBody
    TurnOnWiFiResponse turnOnWiFi(@RequestBody TurnOnWiFiRequest request) {
        Client client = clientRepository.findByToken(request.getSecurity_token());
        if(client == null){
            throw new CustomException(3, "Tokens are not equal");
        }
        return clientService.turnOnWiFi(request.isTurnOn(), client);
    }




    @RequestMapping(value = "/imageSource", method = RequestMethod.POST) // , produces = {MediaType.APPLICATION_JSON_VALUE}
    @Transactional
    public
    @ResponseBody
    ImageSourceResponse imageSource(@RequestBody ImageSourceRequest request) {
        Client client = clientRepository.findByToken(request.getSecurity_token());
        if(client == null){
            throw new CustomException(3, "Tokens are not equal");
        }
            return clientService.imageSource();
    }




    // поиск миссия по айди
    @RequestMapping(value = "/missionFind/v2", method = RequestMethod.POST) // , produces = {MediaType.APPLICATION_JSON_VALUE}
    @Transactional
    public
    @ResponseBody
    MissionFindResponse missionFindV2(@RequestBody MissionFindRequestV2 request) {
        Client client = clientRepository.findByToken(request.getSecurity_token());
        if(client == null){
            throw new CustomException(3, "Tokens are not equal");
        }
            return clientService.missionFindV2(request.getMissionId(), client);
    }





    // only first 30 orders
    @RequestMapping(value = "/find/v2", method = RequestMethod.POST)
    public
    @ResponseBody
    ClientInfoResponseV2 findClientV2(@RequestBody ClientInfoRequest request) {
        ClientInfoResponseV2 response = new ClientInfoResponseV2();
        Client client = administrationService.getClientInfo(request.getClientId());
        if (client != null) {
            response.setClientInfo(ModelsUtils.toModel(client));

            long count = clientService.missionCompleteCount(client);
            response.setMissionCount((int)count);
        }
        return response;
    }
    

    @RequestMapping(value = "/servicesPrices", method = RequestMethod.POST)
    public
    @ResponseBody
    ServicePriceResponse servicesPrices(@RequestBody ServicePriceRequest request) {
        Client client = clientRepository.findByToken(request.getSecurity_token());
        if(client == null){
            throw new CustomException(3, "Tokens are not equal");
        }
        ServicePriceResponse response = new ServicePriceResponse();
          response.setServicePriceInfos(billingService.getServicePrices(true)); // clientService.checkServiceByVersionApp(client.getVersionApp(), client.getDeviceType()
             return response;
    }



    @RequestMapping(value = "/supportPhones", method = RequestMethod.POST)
    public
    @ResponseBody
    SupportPhonesResponse supportPhones(@RequestBody SupportPhonesRequest request) {
        Client client = clientRepository.findByToken(request.getSecurity_token());
        if(client == null){
            throw new CustomException(3, "Tokens are not equal");
        }
        SupportPhonesResponse response = new SupportPhonesResponse();
        response.getSupportPhones().getPhones().putAll(administrationService.loadSupportPhones().getPhones());
           return response;
    }



    @RequestMapping(value = "/autoClassPrice", method = RequestMethod.POST)
    public
    @ResponseBody
    AutoClassPriceResponseV2 autoClassPrices(@RequestBody AutoClassPriceRequest request) {
        Client client = clientRepository.findByToken(request.getSecurity_token());
        if(client == null){
            throw new CustomException(3, "Tokens are not equal");
        }
        return clientService.autoClassPrices(client);
    }





    @RequestMapping(value = "/configuration", method = RequestMethod.POST)
    public
    @ResponseBody
    ClientSystemConfigurationResponse getConfiguration(@RequestBody ClientSystemConfigurationRequest request) {
        ClientSystemConfigurationResponse response = new ClientSystemConfigurationResponse();
        Properties prop = propertiesRepository.findByPropName("use_map");
        response.setUseMap(prop.getPropValue());
        response.setHowLongWaitDriverAssign(60);

        Client client = clientRepository.findOne(request.getClientId());
        if (client != null) {
            if(client.getToken()!= null && client.getToken().equals(request.getSecurity_token())) {
                response.setServerStateInfo(clientService.resolveClientState(request.getClientId()));
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
                response.setAfterMinBooked(Integer.parseInt(commonService.getPropertyValue("after_min_booked")));
            }
        }
        return response;
    }




    @RequestMapping(value = "/findAddress", method = RequestMethod.POST)
    public
    @ResponseBody
    FindAddressResponse findAddressByMask(@RequestBody FindAddressRequest request) throws JsonProcessingException {
        FindAddressResponse response = new FindAddressResponse();
          String security_token = request.getSecurity_token();
             return clientService.findAddressByMask(security_token, request.getAddressMask());
    }




    @RequestMapping(value = "/registration/phonechange", method = RequestMethod.POST)
    public
    @ResponseBody
    RegistrationPhoneChangeResponse login(@RequestBody RegistrationPhoneChangeRequest request) throws JsonProcessingException {
        RegistrationPhoneChangeResponse response = new RegistrationPhoneChangeResponse();
        String login = request.getLogin();
        String password = request.getPassword();
        String newPhone = request.getPhone();

        List result = clientService.phonechangeClient(login, password, newPhone);

           if(!result.isEmpty()){
               response.setResult((boolean)result.get(0));
               response.setReason((String) result.get(1));
           }

        return response;
    }


    @RequestMapping(value = "/getClientCardList", method = RequestMethod.POST)
    public
    @ResponseBody
    ClientCardListResponse getClientCardList(@RequestBody ClientCardListRequest request) throws JsonProcessingException {
         return clientService.getClientCardList(request.getClientId(), request.getSecurity_token());
    }



    @RequestMapping(value = "/getClientCardList/android", method = RequestMethod.POST)
    public
    @ResponseBody
    ClientCardListAndroidResponse getClientCardListAndroid(@RequestBody ClientCardListRequest request, HttpServletRequest reqServlet) throws JsonProcessingException {
          return clientService.getClientCardListAndroid(request.getClientId(), request.getSecurity_token());
    }



    @RequestMapping(value = "/deleteClientCard", method = RequestMethod.POST)
    @ResponseBody
    public DeleteClientCardResponse deleteClientCard(@RequestBody DeleteClientCardRequest request, HttpServletRequest reqServlet) throws JsonProcessingException {
         return clientService.deleteClientCard(request.getClientCardId(), request.getSecurity_token());
    }



    @RequestMapping(value = "/updateClientCard", method = RequestMethod.POST)
    public
    @ResponseBody
    UpdateClientCardResponse updateClientCard(@RequestBody UpdateClientCardRequest request, HttpServletRequest reqServlet) throws JsonProcessingException {
          return clientService.updateClientCard(request.getClientCardInfo(), request.getSecurity_token());
    }



    // клиент логинится через пхп, там он получает токен
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public
    @ResponseBody
    LoginClientResponse login(@RequestBody LoginRequest request) {
        //LoginClientResponse response = new LoginClientResponse();
        String phone = PhoneUtils.normalizeNumber(request.getLogin());
        String password = request.getPassword();
        //ClientInfo clientInfo = administrationService.loginClient(phone, password, request.getDeviceInfoModel());
        LoginClientResponse response = administrationService.loginClient(phone, password, request.getDeviceInfoModel());
        //if (clientInfo != null){
        //    response.setClientInfo(clientInfo);
        //    response.setConfirmed(clientInfo.getId() > 0);
        //}
        return response;
    }



    // token +
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public
    @ResponseBody
    LogoutResponse logoutClient(@RequestBody LogoutRequest request) {
        LogoutResponse response = new LogoutResponse();
        Client client = clientRepository.findOne(request.getClientId());
        if(client!=null) {
            if (client.getToken() != null && client.getToken().equals(request.getSecurity_token())) {
                response = administrationService.logoutClient(request.getClientId(), request.isForce(), request.getDeviceInfo());
            }else{
            response.getErrorCodeHelper().setErrorCode(3);
            response.getErrorCodeHelper().setErrorMessage("Tokens are not equal");
          }
        }
          //response.setDetails(details);
            return response;
    }





    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public
    @ResponseBody
    LogoutResponse auth(@RequestBody LogoutRequest request) {
        LogoutResponse response = new LogoutResponse();
        Client client = clientRepository.findOne(request.getClientId());
        if(client!=null) {
            if (client.getToken() != null && client.getToken().equals(request.getSecurity_token())) {
                response = administrationService.logoutClient(request.getClientId(), request.isForce(), request.getDeviceInfo());
            }else{
                response.getErrorCodeHelper().setErrorCode(3);
                response.getErrorCodeHelper().setErrorMessage("Tokens are not equal");
            }
        }
        //response.setDetails(details);
        return response;
    }





    @RequestMapping(value = "/startMissionTerminal", method = RequestMethod.POST)
    @Transactional
    public
    @ResponseBody
    StartMissionTerminalResponse startMissionTerminal(@RequestBody StartMissionTerminalRequest request, HttpServletRequest reqServlet) throws JsonProcessingException {
        //String asString = ((MappingJackson2HttpMessageConverter) UtilsJSON.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        //LOGGER.debug("IP client [/startMissionTerminal]= " + reqServlet.getHeader("x-forwarded-for")+" param: "+asString);
        StartMissionTerminalResponse response = administrationService.startMissionTerminal(request.getPhone());
          return response;
    }



    @RequestMapping(value = "/smsCodeConfirm", method = RequestMethod.POST)
    @Transactional
    public
    @ResponseBody
    SMSCodeConfirmTerminalResponse smsCodeConfirmTerminal(@RequestBody SMSCodeConfirmTerminalRequest request) {
        SMSCodeConfirmTerminalResponse response = administrationService.smsCodeConfirmTerminal(request.getPhone(), request.getSmsCode(), request.getSecurity_token());
          return response;
    }


    @RequestMapping(value = "/smsCodeRepeate", method = RequestMethod.POST)
    public
    @ResponseBody
    SMSCodeRepeateTerminalResponse smsCodeRepeateTerminal(@RequestBody SMSCodeRepeateTerminalRequest request) {
        SMSCodeRepeateTerminalResponse response = administrationService.smsCodeRepeateTerminal(request.getPhone(), request.getSecurity_token());
          return response;
    }



    @RequestMapping(value = "/smsSendMissionInfo", method = RequestMethod.POST)
    public
    @ResponseBody
    SmsSendMissionInfoResponse smsMissionInfo(@RequestBody SmsSendMissionInfoRequest request) {
        SmsSendMissionInfoResponse response = administrationService.smsMissionInfo(request.getMissionId(), request.getSecurity_token());
           return response;
    }



    @RequestMapping(value = "/registrationLetter", method = RequestMethod.POST)
    public
    @ResponseBody
    RegistrationLetterResponse registrationLetter(@RequestBody RegistrationLetterRequest request) throws IOException, EmailException {
           return administrationService.registrationLetter(request.getClientId());
    }




    @RequestMapping(value = "/registration", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public
    @ResponseBody
    RegistrationInfoResponse register(@RequestBody RegistrationInfoRequest request, HttpServletRequest reqServlet) {
        RegistrationInfoResponse response = new RegistrationInfoResponse();
        ClientInfo clientInfo = request.getClientInfo();
        DeviceInfoModel deviceInfoModel = request.getDeviceInfoModel();
        Client client = administrationService.registerClient(clientInfo, deviceInfoModel, reqServlet.getHeader("x-forwarded-for"));

        if (client != null) {
               if(client.getId()==-1){
                   response.setClientId(-1);
                   response.setDeviceId(0L);
               }else{
                   response.setClientId(client.getId());

                   Set<DeviceInfo> deviceInfoSet = client.getDevices();
                   if(deviceInfoSet!=null){
                       List<DeviceInfo> myList = Lists.newArrayList(deviceInfoSet.iterator());
                       if(!myList.isEmpty()){
                           DeviceInfo di = myList.get(0);
                             if(di!=null){
                                 response.setDeviceId(di.getId());
                             }else{
                                 response.setDeviceId(0L);
                             }
                       }
                   }
               }
        }
        return response;
    }





    @RequestMapping(value = "/registrationSite", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    @Transactional
    public
    @ResponseBody
    RegistrationInfoSiteResponse registerOnSite(@RequestBody RegistrationInfoRequest request) {
        ClientInfo clientInfo = request.getClientInfo();
        DeviceInfoModel deviceInfoModel = request.getDeviceInfoModel();
           RegistrationInfoSiteResponse response = administrationService.registerClientOnSite(clientInfo, deviceInfoModel);
              return response;
    }




    @RequestMapping(value = "/terminalConfiguration", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public
    @ResponseBody
    TerminalConfigurationResponse terminalConfiguration(@RequestBody TerminalConfigurationRequest request, HttpServletRequest reqServlet) throws JsonProcessingException {
        //String asString = ((MappingJackson2HttpMessageConverter) UtilsJSON.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        //LOGGER.debug("IP client [/terminalConfiguration]= " + reqServlet.getHeader("x-forwarded-for")+" param: "+asString);
        TerminalConfigurationResponse response = administrationService.terminalConfiguration(request.getMissionId(), request.getTerminalId());
          return response;
    }



    @RequestMapping(value = "/recoveryPasswordSite", method = RequestMethod.POST)
    public
    @ResponseBody
    UpdatePasswordSiteResponse recoveryPasswordSiteLogic(@RequestBody UpdatePasswordSiteRequest request) throws JsonProcessingException {
        UpdatePasswordSiteResponse response  = administrationService.recoveryPasswordSiteLogic(request.getPhone(), request.getPasswordNew(), request.getPasswordOld());
          return response;
    }




    @RequestMapping(value = "/recovery", method = RequestMethod.POST)
    public
    @ResponseBody
    UpdatePasswordResponse passwordRecovery(@RequestBody UpdatePasswordRequest request) throws JsonProcessingException {
          return administrationService.recoveryPassword(request.getPhone(), request.getPassword(), request.getSmsCode());
    }


    @RequestMapping(value = "/recovery/updateAuthUser", method = RequestMethod.POST)
    public
    @ResponseBody
    UpdatePasswordResponse passwordRecoveryUpdateAuthUser(@RequestBody UpdatePasswordRequest request) throws JsonProcessingException {
        UpdatePasswordResponse response = new UpdatePasswordResponse();
        long clientId = request.getClientId();
        String token = request.getSecurity_token();
        Client client = clientRepository.findOne(clientId);
        if (client != null) {
           if (client.getToken() != null && client.getToken().equals(token)) {
                  client.setPassword(request.getPassword());
                  clientRepository.save(client);
                  response.setClientId(clientId);
           }
        }
        return response;
    }





    @RequestMapping(value = "/recovery/update", method = RequestMethod.POST)
    public
    @ResponseBody
    UpdatePasswordResponse passwordRecoveryUpdate(@RequestBody UpdatePasswordRequest request) throws JsonProcessingException {
        UpdatePasswordResponse response = new UpdatePasswordResponse();
        Client client = clientRepository.findOne(request.getClientId());
        if (client != null) {
              if (!StringUtils.isEmpty(request.getSmsCode()) && !StringUtils.isEmpty(request.getPassword())) {
                    if (request.getSmsCode().equals(client.getSmsCode())) {
                        client.setPassword(request.getPassword());
                        client.setSmsCode("");
                        clientRepository.save(client);
                    }
              }
        }
        return response;
    }




    @RequestMapping(value = "/mission/readytogo", method = RequestMethod.POST)
    public
    @ResponseBody
    ReadyToGoResponse readyToGo(@RequestBody ReadyToGoRequest request) {
        ReadyToGoResponse response = new ReadyToGoResponse();
        long clientId = request.getClientId();
        Client client = clientRepository.findOne(clientId);
        if (client != null) {
            Mission mission = client.getMission();
            if (mission != null) {
                mission.setState(Mission.State.READY_TO_GO);
                mission.setTimeOfReadyToGo(timeService.nowDateTime());
                missionRepository.save(mission);

                //notificationsService.clientReadyToGo(mission.getId(), request.getTime());
            } else {
                LOGGER.error("Mission not found for ready to go notification");
            }
        }
        return response;
    }




    @RequestMapping(value = "/markMissionAsDelete", method = RequestMethod.POST)
    public
    @ResponseBody
    MarkMissionAsDeleteResponse markMissionAsDelete(@RequestBody MarkMissionAsDeleteRequest request) throws JsonProcessingException {
           return clientService.markMissionAsDelete(request.getSecurity_token(), request.getClientId(), request.getMissionMarkAsDeleteIdList());
    }




    // token - здесь токен не нужен
    @RequestMapping(value = "/drivers", method = RequestMethod.POST)
    public
    @ResponseBody
    DriversAroundResponse findDrivers(@RequestBody DriversAroundRequest request) {
        DriversAroundResponse response = new DriversAroundResponse();
            List<Driver.State> driverStates = new ArrayList<>();
            driverStates.add(Driver.State.AVAILABLE);
            driverStates.add(Driver.State.BUSY);

            List<Driver> drivers = driverRepository.findByStateIn(driverStates);
            for (Driver driver : drivers) {
                List<DriverLocation> locations = locationRepository.findByDriverOrderByIdDesc(driver);
                if (locations != null && !locations.isEmpty()) {

                    int angle = locations.get(0).getAngle() != null ? locations.get(0).getAngle() : 0;
                    Location driverLocation = locations.get(0).getLocation();
                    if(driverLocation != null) {
                        double distance = GeoUtils.distance(request.getCurrentLocation().getLatitude(), request.getCurrentLocation().getLongitude(), driverLocation.getLatitude(), driverLocation.getLongitude());
                        if (distance < request.getRadius()) {
                            boolean occupied = false;
                            ItemLocation itemLocation = ModelsUtils.toModel(driver.getId(), driverLocation.getLatitude(), driverLocation.getLongitude(), occupied);
                            itemLocation.setAngle(angle);
                            response.getLocations().add(itemLocation);
                        }
                    }
                }
            }
            return response;
    }




    // получит информацию о назначенном водителе
    @RequestMapping(value = "/drivers/assigned", method = RequestMethod.POST)
    public
    @ResponseBody
    AssignedDriverResponse isDriverFound(@RequestBody AssignedDriverRequest request) throws JsonProcessingException {
        AssignedDriverResponse response = new AssignedDriverResponse();
        Mission mission = missionRepository.findOne(request.getMissionId());
        if (mission != null){
            Client client = clientRepository.findOne(mission.getClientInfo().getId());
                  if(client!=null){
                          if (client.getToken() != null && client.getToken().equals(request.getSecurity_token())) {
                                 boolean versionAppIsNew = commonService.checkServiceByVersionApp(client.getVersionApp(), client.getDeviceType());
                                 Driver driver = mission.getDriverInfo();
                                 if (driver != null) {
                                     int time = -1;
                                     if (!mission.getExpectedArrivalTimes().isEmpty()) {
                                         time = mission.getExpectedArrivalTimes().get(0);
                                     }
                                     response.setExpectedTime(time);
                                     DriverInfo driverInfo = ModelsUtils.toModel(driver);

                                     if(!versionAppIsNew){
                                         driverInfo.setWifi(false);
                                     }

                                     driverInfo = (DriverInfo)commonService.commonPhoneNumber(driverInfo);
                                     response.setDriverInfo(administrationService.fillPhotoDriverAndCars(driverInfo, driver, versionAppIsNew));
                                 }
                         }
                  }
        }
        return response;
    }




    // token - не нужен
    @RequestMapping(value = "/drivers/assigned/location", method = RequestMethod.POST)
    public
    @ResponseBody
    AssignedDriverLocationResponse findDriverLocation(@RequestBody AssignedDriverLocationRequest subscription) throws JsonProcessingException {
        AssignedDriverLocationResponse response = new AssignedDriverLocationResponse();
        long driverId = subscription.getDriverId();
        Driver driver = driverRepository.findOne(driverId);
        if (driver != null && driver.getCurrentMission() != null) {
            Mission mission = driver.getCurrentMission();
            response.setArrived(Mission.State.ARRIVED.equals(mission.getState()));
            List<DriverLocation> locations = locationRepository.findByDriverOrderByIdDesc(driver);
            if (locations != null && !locations.isEmpty()) {
                response.setLocation(ModelsUtils.toModel(driverId, locations.get(0).getLocation()));
            } else {
                response.setLocation(ModelsUtils.toModel(driverId, mission.getLocationFrom()));
            }
        }
        return response;
    }




    @RequestMapping(value = "/activate/promo", method = RequestMethod.POST)
    public
    @ResponseBody
    ActivatePromoResponse activatePromoCode(@RequestBody ActivatePromoRequest request) {
          return clientService.activatePromoCode(request.getClientId(), request.getSecurity_token(), request.getText());
    }




    @RequestMapping(value = "/billing/activate", method = RequestMethod.POST)
    public
    @ResponseBody
    ActivateBonusCardResponse activateBonusCard(@RequestBody ActivateBonusCardRequest request) throws ParseException, JsonProcessingException {
        ActivateBonusCardResponse response = new ActivateBonusCardResponse();
        long clientId = request.getId();
        String token = request.getSecurity_token();
        Client client = clientRepository.findOne(clientId);
        if (client != null) {
            if (client.getToken() != null && client.getToken().equals(token)) {
                /*
                   активация тарифа по промокоду. если введенный соответсвует тарифу, то мы создаем запись в таблице private_tariff с этим тарифом,
                   после чего при загрузке конфигурации мы смотрим в эту таблицу и подгружаем тариф
                */
                PromoCodeExclusive promoCodeExclusive = promoCodeExclusiveRepository.findByPromoCodeIgnoreCase(request.getText());//PromoCodeExclusive promoCodeExclusive = promoCodeExclusiveRepository.findPromoCodeExclusive(request.getText().toLowerCase());

                  if(promoCodeExclusive!=null){
                      if(promoCodeExclusive.getUsedCount() >= promoCodeExclusive.getAvailableUsedCount()){
                          response.setAmount(0);
                            return response;
                      }
                      // активация тарифа BONUS
                      PrivateTariff privateTariff = privateTariffRepository.findByClientAndTariffName(client, AutoClass.BONUS.name());
                      if(privateTariff!=null){
                          //if(privateTariff.isActivated()){
                          if(privateTariff.getActive()){
                              response.setAmount(0);
                                return response;
                          }
                      }else{
                          privateTariff = new PrivateTariff();
                      }
                          privateTariff.setClient(client);
                          privateTariff.setTariffName(AutoClass.BONUS.name());
                          privateTariff.setActive(Boolean.TRUE);
                          privateTariff.setPromoExclusive(promoCodeExclusive);
                          privateTariffRepository.save(privateTariff);

                          promoCodeExclusive.setUsedCount(promoCodeExclusive.getUsedCount()+1);
                          promoCodeExclusiveRepository.save(promoCodeExclusive);
                          response.setAmount(1);
                            return response;
                  }else{
                      response  = billingService.activateBonusCode(client.getId(), request.getText());
                  }
            }else{
                LOGGER.info("Tokens are not equal");
            }
        }
        return response;
    }




    @RequestMapping(value = "/billing/lifetime", method = RequestMethod.POST)
    public
    @ResponseBody
    LifeTimeBonusCardResponse lifeTimeBonusCard(@RequestBody LifeTimeBonusCardRequest request) throws JsonProcessingException {
        LifeTimeBonusCardResponse response = new LifeTimeBonusCardResponse();
            Client client = clientRepository.findOne(request.getFrom_id());
                if(client!=null){
                    if (client.getToken() != null && client.getToken().equals(request.getSecurity_token())) {
                        boolean upd = billingService.lifeTimeBonusCard(request.getFrom_id(), request.getChannel(), request.getText());
                        response.setUpd(upd);
                    }
                }
          return response;
    }




    @RequestMapping(value = "/watchMission", method = RequestMethod.POST)
    public
    @ResponseBody
    WatchMissionResponse watchMission(@RequestBody WatchMissionRequest request) throws JsonProcessingException {
          return clientService.watchMission(request.getClientId(), request.getSecurity_token(), request.getMissionId());
    }



    @RequestMapping(value = "/startWatchMission", method = RequestMethod.POST)
    public
    @ResponseBody
    StartWatchMissionResponse startWatchMission(@RequestBody StartWatchMissionRequest request) throws JsonProcessingException {
          return clientService.startWatchMission(request.getWatch_security_token());
    }



    @RequestMapping(value = "/startTurbo", method = RequestMethod.POST)
    public
    @ResponseBody
    TurboIncreaseResponse turboIncrease(@RequestBody TurboIncreaseRequest request, HttpServletRequest httpServletRequest) {
          return clientService.turboIncrease(request.getClientId(), request.getSecurity_token(), request.getMissionId(), request.getAmount(), HTTPUtil.resolveIpAddress(httpServletRequest));
    }


    @RequestMapping(value = "/turboElapsedTime", method = RequestMethod.POST)
    public
    @ResponseBody
    TurboElapsedTimeResponse turboElapsedTime(@RequestBody TurboElapsedTimeRequest request) {
          return clientService.turboElapsedTime(request.getClientId(), request.getSecurity_token(), request.getMissionId());
    }



    @RequestMapping(value = "/turboCancel", method = RequestMethod.POST)
    public
    @ResponseBody
    TurboIncreaseResponse turboCancel(@RequestBody TurboIncreaseRequest request) {
          return clientService.turboCancel(request.getClientId(), request.getSecurity_token(), request.getMissionId());
    }


    @RequestMapping(value = "/autoSearch", method = RequestMethod.POST)
    public
    @ResponseBody
    AutoSearchResponse autoSearch(@RequestBody AutoSearchRequest request, HttpServletRequest reqServlet) throws JsonProcessingException {
         return clientService.autoSearch(request.getClientId(), request.getSecurity_token(), request.getMissionId(), HTTPUtil.resolveIpAddress(reqServlet));
    }


    @RequestMapping(value = "/autoSearchElapsedTime", method = RequestMethod.POST)
    public
    @ResponseBody
    AutoSearchElapsedTimeResponse autoSearchElapsedTime(@RequestBody AutoSearchElapsedTimeRequest request, HttpServletRequest reqServlet) throws JsonProcessingException {
         return administrationService.autoSearchElapsedTime(request.getClientId(), request.getSecurity_token(), request.getMissionId());
    }


    @RequestMapping(value = "/autoSearchCancel", method = RequestMethod.POST)
    public
    @ResponseBody
    AutoSearchResponse autoSearchCancel(@RequestBody AutoSearchRequest request, HttpServletRequest httpServletRequest) throws JsonProcessingException {
         return clientService.autoSearchCancel(request.getClientId(), request.getSecurity_token(), request.getMissionId(), HTTPUtil.resolveIpAddress(httpServletRequest));
    }


    @RequestMapping(value = "/autoSearchCancel_android", method = RequestMethod.POST)
    public
    @ResponseBody
    AutoSearchResponse autoSearchCancel_android(@RequestBody AutoSearchCancelRequest request, HttpServletRequest httpServletRequest) throws JsonProcessingException {
         return clientService.autoSearchCancel(request.getClientId(), request.getSecurity_token(), request.getMissionId(), HTTPUtil.resolveIpAddress(httpServletRequest));
    }


    @RequestMapping(value = "/callMeDriver", method = RequestMethod.POST)
    public
    @ResponseBody
    CallMeDriverResponse callMeDriver(@RequestBody CallMeDriverRequest request) throws JsonProcessingException {
          return clientService.callMeDriver(request.getClientId(), request.getDriverId(), request.getSecurity_token());
    }




    @RequestMapping(value = "/getFreePromoCode", method = RequestMethod.POST)
    public
    @ResponseBody
    GetFreePromoCodeResponse getFreePromoCode(@RequestBody GetFreePromoCodeRequest request) throws JsonProcessingException {
        GetFreePromoCodeResponse response = new GetFreePromoCodeResponse();
        Client client = clientRepository.findOne(request.getClientId());
        if (client != null) {
            if (client.getToken() != null && client.getToken().equals(request.getSecurity_token())) {
                response  = administrationService.getFreePromoCode(request.getClientId(), request.getSocial_network());
            }else{
                response.getErrorCodeHelper().setErrorCode(3);
                response.getErrorCodeHelper().setErrorMessage("Несовпадение ключа безопасности");
            }
        }
           return response;
    }



    @RequestMapping(value = "/drivers/request/terminal", method = RequestMethod.POST)
    public
    @ResponseBody
    FreeDriverResponse createNewMissionInTerminal(@RequestBody FreeDriverTerminalRequest request) throws JsonProcessingException {
        FreeDriverResponse response = new FreeDriverResponse();
            MissionInfo missionInfo = request.getMissionInfo();
            String token = request.getSecurity_token();
            boolean isPaymentCardAvailable = administrationService.isPaymentCardAvailable();

            /* если заказ бронь, проверяем включены ли брони*/
            if(administrationService.isApplicableForBookingTerminal(missionInfo)){
                int bookedActive = Integer.parseInt(commonService.getPropertyValue("booked_active"));
                if(bookedActive==0){
                    // брони выключены
                    response.getErrorCodeHelper().setErrorCode(7);
                    response.getErrorCodeHelper().setErrorMessage("В данные момент предварительное бронирование недоступно, вы можете сделать заказ только на ближайшее время.Приносим свои извинения!");
                    return  response;
                }
            }

            if (missionInfo != null && missionInfo.getClientInfo() != null) {
                long clientId = missionInfo.getClientInfo().getId();
                Client client = clientRepository.findOne(clientId);

                if (missionInfo.getPaymentType() == 2) {
                    if (!isPaymentCardAvailable) {
                        response.setMissionId(-1);
                        response.getErrorCodeHelper().setErrorCode(4);
                        response.getErrorCodeHelper().setErrorMessage("Оплата картой недоступна");
                    } else {
                        // оплата картой доступна
                        ClientCard clientCardActive = clientService.getActiveClientCard(client);
                        if (clientCardActive != null) {
                            response = administrationService.createNewMissionTerminal(missionInfo, token, request.getTerminalId());
                        } else {
                            response.setMissionId(-1);
                            response.getErrorCodeHelper().setErrorCode(5);
                            response.getErrorCodeHelper().setErrorMessage("Данный вид заказа станет доступным только после добавления карты в приложение!");
                        }
                    }
                } else {
                    response = administrationService.createNewMissionTerminal(missionInfo, token, request.getTerminalId());
                }
            } else {
                response.setMissionId(-1);
                response.getErrorCodeHelper().setErrorCode(1);
                response.getErrorCodeHelper().setErrorMessage("Пользователь с такими данными не найден");
            }
            return response;
    }



    @RequestMapping(value = "/drivers/request/old_version", method = RequestMethod.POST)
    @Transactional
    public
    @ResponseBody
    FreeDriverResponse createNewMissionOLD_VERSION(@RequestBody FreeDriverRequest request) {
        FreeDriverResponse response = new FreeDriverResponse();
            MissionInfo missionInfo = request.getMissionInfo();
            String token = request.getSecurity_token();
            boolean isPaymentCardAvailable = administrationService.isPaymentCardAvailable();

            if (missionInfo != null && missionInfo.getClientInfo() != null) {

                long clientId = missionInfo.getClientInfo().getId();
                Client client = clientRepository.findOne(clientId);

                if ((missionInfo.getPaymentType() == PaymentType.CARD.getValue())|| (missionInfo.getPaymentType() == PaymentType.CORPORATE_CARD.getValue())) {
                    if (!isPaymentCardAvailable) {
                        // оплата картой недоступна
                        response.setMissionId(-1);
                        response.getErrorCodeHelper().setErrorCode(4);
                        response.getErrorCodeHelper().setErrorMessage("Оплата картой недоступна");
                    } else {
                        // оплата картой доступна
                        ClientCard clientCardActive = clientService.getActiveClientCard(client);

                        if (clientCardActive != null) {
                            // есть вбитая карточка
                                response = administrationService.createNewMission(missionInfo, token);
                        } else {
                            response.setMissionId(-1);
                            response.getErrorCodeHelper().setErrorCode(5);
                            response.getErrorCodeHelper().setErrorMessage("Данный вид заказа станет доступным только после добавления карты в приложение!");
                        }
                    }
                } else {
                            response = administrationService.createNewMission(missionInfo, token);
                }
            } else {
                response.setMissionId(-1);
                response.getErrorCodeHelper().setErrorCode(1);
                response.getErrorCodeHelper().setErrorMessage("Пользователь с такими данными не найден");
            }
            return response;
    }





    @RequestMapping(value = "/lowCosterDesc", method = RequestMethod.POST)
    public
    @ResponseBody
    LowCosterDescResponse LowCosterDescription(@RequestBody LowCosterDescRequest request) {
        LowCosterDescResponse response = new LowCosterDescResponse();
        Client client = clientRepository.findOne(request.getClientId());
        if (client != null) {
            if (client.getToken() != null && client.getToken().equals(request.getSecurity_token())) {
                response.setHtmlMessage(clientService.tariffRestriction(AutoClass.LOW_COSTER.getValue()));
            }else{
                response.setErrorCode(3);
                response.setErrorMessage("Несовпадение ключа безопасности");
            }
        }
        return response;
    }






    // token + , create new mission
    @RequestMapping(value = "/drivers/request/str", method = RequestMethod.POST)
    @Transactional
    public
    @ResponseBody
    FreeDriverResponse createNewMissionSTR(@RequestBody FreeDriverRequestSTR request, HttpServletRequest reqServlet) throws Exception {
        FreeDriverResponse response = new FreeDriverResponse();
        if (!validatorService.validateUser(request.getMissionInfo().getClientInfo().getId(), request.getSecurity_token(), 1)) {
            response.getErrorCodeHelper().setErrorCode(3);
            response.getErrorCodeHelper().setErrorMessage("Несовпадение ключа безопасности");
            return response;
        }
            return clientService.createMissionWithStringTime(request.getMissionInfo(), request.getTimeOfStarting(), clientRepository.findOne(request.getMissionInfo().getClientInfo().getId()), HTTPUtil.resolveIpAddress(reqServlet));
    }





    @RequestMapping(value = "/drivers/request", method = RequestMethod.POST)
    @Transactional
    public
    @ResponseBody
    FreeDriverResponse createNewMission(@RequestBody FreeDriverRequest request) throws IOException, ParseException, JSONException { //throws IOException, JSONException
           return clientService.createMissionOldVersion(request.getMissionInfo(), request.getSecurity_token());
    }





    // token -
    @RequestMapping(value = "/ping", method = RequestMethod.POST)
    public
    @ResponseBody
    PingResponse ping(@RequestBody PingRequest request) {
        PingResponse response = new PingResponse();
        response.setTimestamp(timeService.nowMillis());
        return response;
    }




    @RequestMapping(value = "/missionRates", method = RequestMethod.POST)
    public
    @ResponseBody
    MissionRateResponse missionRates(@RequestBody MissionRateRequest request) throws JsonProcessingException {
        MissionRateResponse response = new MissionRateResponse();
        Client client = clientRepository.findOne(request.getClientId());
        if (client != null) {
            if (client.getToken() != null && client.getToken().equals(request.getSecurity_token())) {
                List<MissionRateInfo> rates = billingService.findMissionRates(client.getMainClient()!=null?true:false);
                /* удаление тарифом, которые не соответсвуют версии приложения */
                rates = administrationService.showRatesByVersionApp(rates, client);

                response.getRates().addAll(rates);
            }else{
                response.getErrorCodeHelper().setErrorCode(3);
                response.getErrorCodeHelper().setErrorMessage("Tokens are not equal");
            }
        }else{
            response.getErrorCodeHelper().setErrorCode(1);
            response.getErrorCodeHelper().setErrorMessage("Client not found");
        }
        return response;
    }



    // token +
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @Transactional
    public
    @ResponseBody
    ClientInfoResponse update(@RequestBody ClientInfoRequest request) throws JsonProcessingException {
        long clientId = request.getClientId();
        String token = request.getSecurity_token();
        ClientInfo clientInfo = request.getClientInfo();

        Client client = clientRepository.findOne(clientId);
        if (client != null) {
            if(client.getToken()!= null && client.getToken().equals(token)) {
                client = ModelsUtils.fromModel(clientInfo, client);
                if (!StringUtils.isEmpty(request.getClientInfo().getPicure())) {
                    String pictureUrl = profilesResourcesService.saveClientPicture(request.getClientInfo().getPicure(), clientId, true);
                    //request.getClientInfo().setPicure(pictureUrl);
                    client.setPicureUrl(pictureUrl);
                    clientInfo.setPicureUrl(pictureUrl);
                }
                    clientRepository.save(client);
            }
        }
        ClientInfoResponse response = new ClientInfoResponse();
        response.setClientInfo(clientInfo);
            return response;
    }





    @RequestMapping(value = "/photoDelete", method = RequestMethod.POST)
    public
    @ResponseBody
    ClientPhotoDeleteResponse photoDelete(@RequestBody ClientPhotoDeleteRequest request) {
        ClientPhotoDeleteResponse response = new ClientPhotoDeleteResponse();
        long clientId = request.getClientId();
        String token = request.getSecurity_token();
        Client client = clientRepository.findOne(clientId);
        if (client != null) {
            if(client.getToken()!= null && client.getToken().equals(token)){
                profilesResourcesService.deleteClientPhotos(client.getPicureUrl());
                client.setPicureUrl(null);
                clientRepository.save(client);
            }else{
                response.getErrorCodeHelper().setErrorCode(3);
                response.getErrorCodeHelper().setErrorMessage("Tokens are not equals");
            }
        }
        return response;
    }





    @RequestMapping(value = "/getCCType", method = RequestMethod.POST)
    public
    @ResponseBody
    ResolveCreditCardTypeResponse getCCType(@RequestBody ResolveCreditCardTypeRequest request, HttpServletRequest reqServlet) throws JsonProcessingException {
        ResolveCreditCardTypeResponse response = new ResolveCreditCardTypeResponse();
        //String asString = ((MappingJackson2HttpMessageConverter) UtilsJSON.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        //LOGGER.debug("IP client [/getCCType]= " + reqServlet.getHeader("x-forwarded-for")+" param: "+asString);
         String ob = administrationService.getCCType(request.getCreditCardNumber());
             response.setTypeCreditCard(ob.toString());
              return response;
    }



    //f:add тестовый
    @RequestMapping(value = "/testRequest", method = RequestMethod.POST)
    public
    @ResponseBody
    TestResponse testMethod(@RequestBody TestRequest request) {
         TestResponse response = new TestResponse();
         Client client = clientRepository.findOne(request.getClientId());
             /* sms send by smsc */
             if(administrationService.allowedToSendSms(request.getClientId())){
                 administrationService.sendSMS(client.getPhone(),request.getDumbField(), client);
             }
             /* end */
          return response;
    }




    @RequestMapping(value = "/find", method = RequestMethod.POST)
    public
    @ResponseBody
    ClientInfoResponse find(@RequestBody ClientInfoRequest request) throws JsonProcessingException {
        ClientInfoResponse response = new ClientInfoResponse();
        String token = request.getSecurity_token();
        Client client = clientRepository.findOne(request.getClientId());
        if (client != null) {
            if(client.getToken()!= null && client.getToken().equals(token)){
                ClientInfo clientInfo = ModelsUtils.toModel(client);
                response.setClientInfo(clientInfo);
            }
        }
        return response;
    }





    // совмещенный метод для расчета стоимости поездки
    @RequestMapping(value = "/calculatePrice", method = RequestMethod.POST)
    @ResponseBody
    public CalculatePriceResponse calculatePrice(@RequestBody CalculatePriceRequest request) throws IOException {
        if (!validatorService.validateUser(request.getClientId(), request.getSecurity_token(), 1)) {
            throw new CustomException(3, "Tokens are not equals");
        }
            return administrationService.calculateCostForTrip(clientRepository.findOne(request.getClientId()), request.getMissionInfo());
    }




    // token +
    @RequestMapping(value = "/getStartPrice", method = RequestMethod.POST)
    public
    @ResponseBody
    GetStartPriceResponse startPriceCalculate(@RequestBody GetStartPriceRequest request) throws JsonProcessingException {
        GetStartPriceResponse response = new GetStartPriceResponse();
            List<Integer> resultSumList = administrationService.calculateStartPrice(request.getAuto_class(), request.getDistance(), request.getOptions(), false);
              response.setSum(resultSumList.get(0));
              response.setSumWithOption(resultSumList.get(1));
                return response;
    }




    // token +
    @RequestMapping(value = "/calculateDistance", method = RequestMethod.POST) // /2gis
    public
    @ResponseBody
    CalculateDistanceResponse calculateDistance_2GIS(@RequestBody CalculateDistanceRequest request) throws JsonProcessingException {
        CalculateDistanceResponse response = new CalculateDistanceResponse();
        try {
            if(request.getLatLonList()!=null && request.getLatLonList().size()==1){
                response.setDistanceKm(0);
            }else{
                response = administrationService.calculateDistance2GIS(request.getLatLonList(), request.getSecurity_token(), request.getClientId(), response);
            }
        } catch (IOException e) {
            e.printStackTrace();
            response.getErrorCodeHelper().setErrorCode(2);
            response.getErrorCodeHelper().setErrorMessage("CalculateDistance IOException: "+e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            response.getErrorCodeHelper().setErrorCode(4);
            response.getErrorCodeHelper().setErrorMessage("CalculateDistance JSONException: "+e.getMessage());
        }
        return response;
    }




    // token +
    @RequestMapping(value = "/calculateDistance/stringAddress", method = RequestMethod.POST)
    public
    @ResponseBody
    CalculateDistanceResponse calculateDistance_StringAddress(@RequestBody CalculateDistance_V2_Request request, HttpServletRequest reqServlet) throws IOException, JSONException {
           return administrationService.calculateDistance_StringAddress(request.getAddressList(), request.getSecurity_token(), request.getClientId(), new CalculateDistanceResponse());
    }




    // token +
    @RequestMapping(value = "/calculateDistance/2gis", method = RequestMethod.POST) //
    public
    @ResponseBody
    CalculateDistanceResponse calculateDistance_2GIS_New(@RequestBody CalculateDistanceRequest request, HttpServletRequest reqServlet) throws JsonProcessingException {
        CalculateDistanceResponse response = new CalculateDistanceResponse();
        //String asString = ((MappingJackson2HttpMessageConverter) UtilsJSON.template().getMessageConverters().get(0)).getObjectMapper().writeValueAsString(request);
        //LOGGER.debug("IP client [/calculateDistance/2gis]= " + reqServlet.getHeader("x-forwarded-for")+" param: "+asString);
        try {
            response = administrationService.calculateDistance2GIS(request.getLatLonList(), request.getSecurity_token(), request.getClientId(), response);
        } catch (IOException e) {
            e.printStackTrace();
            response.getErrorCodeHelper().setErrorCode(2);
            response.getErrorCodeHelper().setErrorMessage("IOException");
        } catch (JSONException e) {
            e.printStackTrace();
            response.getErrorCodeHelper().setErrorCode(4);
            response.getErrorCodeHelper().setErrorMessage("JSONException");
        }
        return response;
    }



   // token +
   @RequestMapping(value = "/calculateDistance_old", method = RequestMethod.POST)
   public
   @ResponseBody
   CalculateDistanceResponse calculateDistance(@RequestBody CalculateDistanceRequest request) {
        CalculateDistanceResponse response = new CalculateDistanceResponse();
        try {
            response = administrationService.calculateDistance(request.getLatLonList(), request.getSecurity_token(), request.getClientId(), response);
        } catch (IOException e) {
            e.printStackTrace();
            response.getErrorCodeHelper().setErrorCode(2);
            response.getErrorCodeHelper().setErrorMessage("IOException");
        }
        return response;
   }





    // token +
    @RequestMapping(value = "/mission/historySite", method = RequestMethod.POST)
    public
    @ResponseBody
    TripsHistorySiteResponse findTripsHistorySite(@RequestBody TripsHistorySiteRequest request) throws JsonProcessingException {
           return administrationService.missionsHistoryClientSite(request.getRequesterId(), request.getSecurity_token(), request.getDateStart(), request.getDateEnd(), request.getNumberPage(), request.getSizePage());
    }




    // token +
    @RequestMapping(value = "/mission/score", method = RequestMethod.POST)
    public
    @ResponseBody
    RateDriverResponse scoreMission(@RequestBody RateDriverRequest request) throws JsonProcessingException {
        RateDriverResponse response = new RateDriverResponse();
          administrationService.rateDriver(request.getMissionId(), request.getScores(), request.getSecurity_token());
            return response;
    }




    // token + [клиент указывает чаевые, происходит регистрация заказа в альфа банке, после чего ответ отсылается водителю]
    @RequestMapping(value = "/registerOrderWithTip", method = RequestMethod.POST)
    public
    @ResponseBody
    RegisterOrderClientWithTipResponse registerOrderWithTip(@RequestBody RegisterOrderClientWithTipRequest request) {
        RegisterOrderClientWithTipResponse response = new RegisterOrderClientWithTipResponse();
           billingService.sentOrderEventFromClientToDriverWithTip(request.isAnswer(), request.getClientId(), request.getMissionId(), request.getSecurity_token(), request.getTipPercentId(), request.getSumWithoutTipPercent());
             return response;
    }



    /*  получить проценты чаевых */
    @RequestMapping(value = "/calculateTips", method = RequestMethod.POST)
    public
    @ResponseBody
    CalculateTipResponse getTips(@RequestBody CalculateTipRequest request) {
        return administrationService.calculateTips(request.getSecurity_token(), request.getClientId());
    }


    @RequestMapping(value = "/mission/estimate/v2", method = RequestMethod.POST)
    public
    @ResponseBody
    MissionEstimateResponse missionEstimateV2(@RequestBody MissionEstimateV2Request request) {
        MissionEstimateResponse response = new MissionEstimateResponse();
         administrationService.missionEstimateV2(request.getSecurity_token(), request.getEstimateInfo());
            return response;
    }


    // token + оценить поездку(водителя)
    @RequestMapping(value = "/mission/estimate", method = RequestMethod.POST)
    public
    @ResponseBody
    MissionEstimateResponse missionEstimate(@RequestBody MissionEstimateRequest request) {
        MissionEstimateResponse response = new MissionEstimateResponse();
         administrationService.missionEstimate(request.getSecurity_token(), request.getEstimateInfoClient());
           return response;
    }



    // оценки и комментарии водителя
    @RequestMapping(value = "/estimate/list", method = RequestMethod.POST)
    public
    @ResponseBody
    DriverEstimatesResponse estimateList(@RequestBody DriverEstimatesRequest request) {
        return administrationService.estimateList(request.getClientId(), request.getSecurity_token(), request.getDriverId());
    }


    // оценки и комментарии водителя
    @RequestMapping(value = "/estimate/list/v2", method = RequestMethod.POST)
    public
    @ResponseBody
    DriverEstimatesV2Response estimateListV2(@RequestBody DriverEstimatesRequest request) {
        return administrationService.estimateListV2(request.getClientId(), request.getSecurity_token(), request.getDriverId());
    }



    // token +
    @RequestMapping(value = "/mission/started", method = RequestMethod.POST)
    public
    @ResponseBody
    StartTripResponse checkMissionStarted(@RequestBody StartTripRequest request, HttpServletRequest reqServlet) {
        StartTripResponse response = new StartTripResponse();
        //LOGGER.debug("IP client [/mission/started]= " + reqServlet.getHeader("x-forwarded-for"));
        long missionId = request.getMissionId();
        Mission mission = missionRepository.findOne(missionId);
         if(mission!=null) {
             Client client = clientRepository.findOne(mission.getClientInfo().getId());
             if (client != null) {
                 if (client.getToken() != null && client.getToken().equals(request.getSecurity_token())) {
                     response.setStarted(Mission.State.IN_TRIP.equals(mission.getState()));
                 }else{
                     LOGGER.info("Tokens are not equal");
                 }
             }
         }
        return response;
    }




    // get list events partners
    @RequestMapping(value = "/eventsPartners", method = RequestMethod.POST)
    public
    @ResponseBody
    EventPartnerResponse eventsPartners(@RequestBody EventPartnerRequest request) {
        return administrationService.eventsPartners(request.getSecurity_token(), request.getClientId());
    }



    @RequestMapping(value = "/registration/smscode", method = RequestMethod.POST)
    @Transactional
    public
    @ResponseBody
    SMSCodeResponse repeatSmsCodeSend(@RequestBody SMSCodeRequest request, HttpServletRequest reqServlet) {
        SMSCodeResponse response = new SMSCodeResponse();
        //LOGGER.debug("IP client [/registration/smscode]= " + reqServlet.getHeader("x-forwarded-for"));
        if (!StringUtils.isEmpty(request.getLogin())) {
            String phoneNormalized = PhoneUtils.normalizeNumber(request.getLogin());
            Client client = clientRepository.findByPhone(phoneNormalized);
            if (client != null) {
                if (!client.getRegistrationState().equals(Client.RegistrationState.CONFIRMED)) {
                    String newSms = administrationService.generateSMS();
                    client.setSmsCode(newSms);
                    clientRepository.save(client);
                    serviceSMSNotification.registrationConfirm(client.getPhone(), newSms, "");
                    response.setSent(true);
                }else {
                    response.setSent(false);
                }
            }else{
                response.setSent(false);
            }
        }
       return response;
    }



    @RequestMapping(value = "/registration/confirm", method = RequestMethod.POST)
    @Transactional
    public
    @ResponseBody
    RegistrationConfirmResponse confirm(@RequestBody RegistrationConfirmRequest request) throws IOException, EmailException {
        RegistrationConfirmResponse response = new RegistrationConfirmResponse();
        response.setClientId(-1);
        if (!StringUtils.isEmpty(request.getLogin())) {
            String phoneNormalized = PhoneUtils.normalizeNumber(request.getLogin());
            Client client = administrationService.confirmClient(phoneNormalized,request.getCodeSMS());
            if (client != null) {
                response.setClientId(client.getId());
                response.setSecurity_token(client.getToken());
            }
        }
        return response;
    }



    // реализован на пхп
    @RequestMapping(value = "/mission/payment", method = RequestMethod.POST)
    public
    @ResponseBody
    PaymentResponse processPayment(@RequestBody PaymentRequest request) {
        PaymentResponse response = new PaymentResponse();
        long missionId = request.getMissionId();
        Mission mission = missionRepository.findOne(missionId);

//        response.setPayment(Mission.State.IN_TRIP_END.equals(mission.getState()));
//        response.setMissionInfo(ModelsUtils.toModel(mission));
//        if (response.getPayment()) {
//            ServicePrice services = new ServicePrice();
//            services.setService("К оплате:");
//            if (mission.getPrice() != null) {
//                services.setPrice(mission.getPrice().getAmount().floatValue());
//            } else {
//                services.setPrice(200);
//            }
//            response.getPaymentInfo().getServices().add(services);
//        }
        return response;
    }



    @RequestMapping(value = "/createMultipleMission", method = RequestMethod.POST)
    public
    @ResponseBody
    CreateMultipleMissionResponse createMultipleMission(@RequestBody CreateMultipleMissionRequest request) throws Exception {
        if (!validatorService.validateUser(request.getClientId(), request.getSecurity_token(), 1)) {
            throw new CustomException(3, "Tokens are not equals");
        }
        Client client = clientRepository.findOne(request.getClientId());
        if(client.getMultipleMission()!=null){
            throw new CustomException(2, "У вас есть текущий мульти заказ");
        }
            return clientService.createMultipleMission(request.getMultipleMissionInfos(), client);
    }



    //сделать метод для очистки multiple_mission_id чтобы можно было делать следующий заказ
    @RequestMapping(value = "/autoSearchAnswer", method = RequestMethod.POST)
    @Transactional
    public
    @ResponseBody
    AutoSearchAnswerResponse createMultipleMission(@RequestBody AutoSearchAnswerRequest request, HttpServletRequest httpServletRequest) throws Exception {
        if (!validatorService.validateUser(request.getClientId(), request.getSecurity_token(), 1)) {
            throw new CustomException(3, "Tokens are not equals");
        }
        clientService.autoSearchAnswer(request.getMissionId(), request.isAnswer(), HTTPUtil.resolveIpAddress(httpServletRequest));
            return new AutoSearchAnswerResponse();
    }




    @RequestMapping(value = "/currentStateMission", method = RequestMethod.POST)
    @ResponseBody
    public CurrentStateMissionResponse currentStateMission(@RequestBody CurrentStateMissionRequest request) throws Exception {
        if (!validatorService.validateUser(request.getClientId(), request.getSecurity_token(), 1)) {
            throw new CustomException(3, "Tokens are not equals");
        }
            return clientService.currentStateMission(request.getMissionId());
    }



    @RequestMapping(value = "/missionCompleteCount", method = RequestMethod.POST)
    @ResponseBody
    public MissionCompleteCountResponse missionCompleteCount(@RequestBody MissionCompleteCountRequest request) throws Exception {
        if (!validatorService.validateUser(request.getClientId(), request.getSecurity_token(), 1)) {
            throw new CustomException(3, "Tokens are not equals");
        }
            MissionCompleteCountResponse response = new MissionCompleteCountResponse();
            response.setCount((int)clientService.missionCompleteCount(clientRepository.findOne(request.getClientId())));
            return response;
    }


}