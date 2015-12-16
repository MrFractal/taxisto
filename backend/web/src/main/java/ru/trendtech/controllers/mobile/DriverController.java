package ru.trendtech.controllers.mobile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.trendtech.common.mobileexchange.model.client.*;
import ru.trendtech.common.mobileexchange.model.common.*;
import ru.trendtech.common.mobileexchange.model.common.billing.paymobile.AskClientForPaymentCardRequest;
import ru.trendtech.common.mobileexchange.model.common.billing.paymobile.AskClientForPaymentCardResponse;
import ru.trendtech.common.mobileexchange.model.common.billing.paymobile.PayOrderFinishRequest;
import ru.trendtech.common.mobileexchange.model.common.billing.paymobile.PayOrderFinishResponse;
import ru.trendtech.common.mobileexchange.model.common.rates.PaymentInfo;
import ru.trendtech.common.mobileexchange.model.driver.*;
import ru.trendtech.common.mobileexchange.model.driver.MissionCompleteRequest;
import ru.trendtech.common.mobileexchange.model.driver.MissionCompleteResponse;
import ru.trendtech.common.mobileexchange.model.web.*;
import ru.trendtech.domain.*;
import ru.trendtech.models.ModelsUtils;
import ru.trendtech.repositories.*;
import ru.trendtech.services.TimeService;
import ru.trendtech.services.administration.AdministrationService;
import ru.trendtech.services.billing.BillingService;
import ru.trendtech.services.common.CommonService;
import ru.trendtech.services.driver.DriverService;
import ru.trendtech.services.validate.ValidatorService;
import ru.trendtech.utils.GeoUtils;
import ru.trendtech.utils.HTTPUtil;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/driver")
class DriverController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DriverController.class);
    // @Autowired
    // private NotificationsService notificationsService;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private MissionRepository missionRepository;
    @Autowired
    PayTerminalRepository payTerminalRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private TimeService timeService;
    @Autowired
    private AdministrationService administrationService;
    @Autowired
    private DriverService driverService;
    @Autowired
    private BillingService billingService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private ValidatorService validatorService;




    @RequestMapping(value = "/commonTripHistory", method = RequestMethod.POST)
    @ResponseBody
    public CommonTripHistoryResponse commonTripHistory(@RequestBody CommonTripHistoryRequest request) throws ParseException {
        if(!validatorService.validateUser(request.getRequesterId(), request.getSecurity_token(), 2)){
            throw new CustomException(3, "Клиент не найден");
        }
            return driverService.commonTripHistory(request.getRequesterId());
    }





    @RequestMapping(value = "/freeWaitTime", method = RequestMethod.POST)
    @ResponseBody
    public FreeWaitTimeResponse freeWaitTime(@RequestBody FreeWaitTimeRequest request) {
         LOGGER.info("freeWaitTime: token=" + request.getSecurity_token() + " missionId=" + request.getMissionId());
         //Driver driver = driverRepository.findByToken(request.getSecurity_token());
         //if(driver == null){
         //   throw new CustomException(1, "Driver not found");
         //}
            return driverService.freeWaitTime(request.getMissionId());
    }




    @RequestMapping(value = "/mission/arrived", method = RequestMethod.POST)
    public
    @ResponseBody
    DriverArrivedResponse arrived(@RequestBody DriverArrivedRequest request) {
        driverService.driverArrived(request.getDriverId(), request.getMissionId());
        return new DriverArrivedResponse();
    }




    @RequestMapping(value = "/mission/complete", method = RequestMethod.POST)
    public
    @ResponseBody
    MissionCompleteResponse complete(@RequestBody MissionCompleteRequest request) {
        MissionCompleteResponse response = new MissionCompleteResponse();
        administrationService.completeMission(request.getMissionId(), request.getDriverId());
            return response;
    }




    @RequestMapping(value = "/mission/cancel", method = RequestMethod.POST)
    public
    @ResponseBody
    CancelMissionResponse cancel(@RequestBody CancelMissionRequest request, HttpServletRequest httpServletRequest) throws InterruptedException, ExecutionException, JSONException {
        //if(driverRepository.findByToken(request.getSecurity_token()) == null) {
        //    throw new CustomException(1, "Driver not found");
        //}
        driverService.cancelMissionByDriver(request.getMissionId(), request.getComment(), request.getInitiatorId(), request.getReasonId(), HTTPUtil.resolveIpAddress(httpServletRequest));
            return new CancelMissionResponse();
    }




    @RequestMapping(value = "/reasons", method = RequestMethod.POST)
    @ResponseBody
    public ReasonResponse reasons(@RequestBody ReasonRequest request) {
        //if(driverRepository.findByToken(request.getSecurity_token()) == null) {
        //    throw new CustomException(1, "Driver not found");
        //}
        return commonService.reasons(request.getReasonId(), false);
    }



    @RequestMapping(value = "/estimateInfoByDriver", method = RequestMethod.POST)
    public
    @ResponseBody
    EstimateInfoResponse estimateInfoByDriver(@RequestBody EstimateInfoRequest request) {
        return administrationService.estimateInfoByDriver(1, 10, request.getQueryDetailsList());
    }



    @RequestMapping(value = "/servicesPrices", method = RequestMethod.POST)
    public
    @ResponseBody
    ServicePriceResponse autoClassPrices(@RequestBody ServicePriceRequest request) {
        LOGGER.info("servicesPrices: token="+request.getRequesterId() + " driverId="+request.getRequesterId());

        Boolean isNewClient = false;

        Driver driver =  driverRepository.findOne(request.getRequesterId()); //driverRepository.findByToken(request.getSecurity_token());
        if(driver == null){
            //throw new CustomException(1, "Driver not found");
            isNewClient = true;
        }

        if(driver != null && driver.getCurrentMission() != null){
            isNewClient = commonService.checkServiceByVersionApp(driver.getCurrentMission().getClientInfo().getVersionApp(), driver.getCurrentMission().getClientInfo().getDeviceType());
        }
        if(request.getRequesterId() == -1){
            isNewClient = null;
        }
        ServicePriceResponse response = new ServicePriceResponse();
          response.setServicePriceInfos(billingService.getServicePrices(true)); // clientService.checkServiceByVersionApp(client.getVersionApp(), client.getDeviceType()
             return response;
    }



    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public
    @ResponseBody
    RegisterDriverResponse register(@RequestBody RegisterDriverRequest request) {
        return driverService.registerDriver(request.getDriverInfoARM(), request.getDeviceInfoModel());
    }




    @RequestMapping(value = "/stopSearch", method = RequestMethod.POST)
    public
    @ResponseBody
    StringsListResponse stopSearchDriver(@RequestBody StringsListRequest request)  {
        StringsListResponse response = new StringsListResponse();
          driverService.transactTest();
            response.getValues().add("stop search driver");
              return response;
    }




    @RequestMapping(value = "/wifi", method = RequestMethod.POST)
    public
    @ResponseBody
    WifiAvailableResponse availableWifi(@RequestBody WifiAvailableRequest request) {
        WifiAvailableResponse response = new WifiAvailableResponse();
         //notificationsService.availableWiFi(request.getMissionId(), request.getNetworkId(), request.getPassword());
           return response;
    }



    @RequestMapping(value = "/takeMissionDeclined", method = RequestMethod.POST)
    public
    @ResponseBody
    TakeMissionDeclinedResponse takeMissionDeclined(@RequestBody TakeMissionDeclinedRequest request) throws JsonProcessingException, JSONException {
        return driverService.takeMissionDeclined(request.getMissionId(), request.getDriverIdResponded(), request.getArrivalTime());
    }



    @RequestMapping(value = "/missionDeclinedByReason", method = RequestMethod.POST)
    public
    @ResponseBody
    MissionDeclinedResponse missionDeclinedByReason(@RequestBody MissionDeclinedRequest request) throws JsonProcessingException, JSONException {
        return driverService.missionDeclinedByReason(request.getDriverId(), request.getMissionId(), request.getTypeReason());
    }



    @RequestMapping(value = "/updatePhone", method = RequestMethod.POST)
    public
    @ResponseBody
    DriverNewPhoneResponse updatePhoneDriver(@RequestBody DriverNewPhoneRequest request) throws JsonProcessingException {
        DriverNewPhoneResponse response = administrationService.updatePhoneDriver(request.getDriverId(), request.getPhone());
        return response;
    }




/*
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @Transactional
    public
    @ResponseBody
    DriverChangeResponse updateDriver(@RequestBody DriverChangeRequest request) {
        DriverChangeResponse response = new DriverChangeResponse();
        DriverInfo driverInfo = request.getDriverInfo();
              if(driverInfo!=null) {
                  Driver driver = driverRepository.findOne(driverInfo.getId());
                  if (driver != null) {

                      driver = ModelsUtils.fromModelUpdateDriver(driverInfo, driver);

                      if (!StringUtils.isEmpty(request.getDriverInfo().getPhotoPicture())) {
                          String pictureUrl = profilesResourcesService.saveDriverPicture(request.getDriverInfo().getPhotoPicture(), driver.getId());
                          request.getDriverInfo().setPhotoPicture("");
                          driver.setPhotoUrl(pictureUrl);
                          //driverRepository.save(driver);
                      }
                      List<String> photosCarsPictures = driverInfo.getPhotosCarsPictures();
                      if (!photosCarsPictures.isEmpty()) {
                          List<String> pictureUrls = profilesResourcesService.saveAutoPictures(photosCarsPictures, driver.getId());
                          driver.getPhotosCarsUrl().addAll(pictureUrls);
                          //driverRepository.save(driver);
                      }
                      driverRepository.save(driver);
                  }
              }
        response.setDriverInfo(driverInfo);
          return response;
    }
    */



    @RequestMapping(value = "/confirmationPhone", method = RequestMethod.POST)
    public
    @ResponseBody
    DriverConfirmationPhoneResponse confirmationPhoneDriver(@RequestBody DriverConfirmationPhoneRequest request) throws JsonProcessingException {
        DriverConfirmationPhoneResponse response = administrationService.confirmationPhoneDriver(request.getDriverId(), request.getCode(), request.getNewPhone());
        return response;
    }


    @RequestMapping(value = "/find", method = RequestMethod.POST)
    public
    @ResponseBody
    DriverInfoResponse findDriver(@RequestBody DriverInfoRequest request) {
        DriverInfoResponse response = new DriverInfoResponse();
        long driverId = request.getDriverId();
        Driver driver = driverRepository.findOne(driverId);

        if (driver != null) {
            DriverInfo driverInfo = ModelsUtils.toModel(driver);
            response.setDriverInfo(driverInfo);
        }
        return response;
    }



    @RequestMapping(value = "/alarm", method = RequestMethod.POST)
    public
    @ResponseBody
    AlarmResponse alarm(@RequestBody AlarmRequest request) throws Exception{
        return driverService.driverAlarm(request.getDriverId(), request.isStart());
    }




    @RequestMapping(value = "/actualMissionIdList", method = RequestMethod.POST)
    public
    @ResponseBody
    ActualMissionListResponse getActualMissionList(@RequestBody ActualMissionListRequest request) {
       return administrationService.getActualMissionList(request.isMark(), request.getDirtyMissionIdList());
    }



    // список актуальных миссий в радиусе
    @RequestMapping(value = "/actualMissionInRadius", method = RequestMethod.POST)
    public
    @ResponseBody
    ActualMissionInRadiusResponse actualMissionInRadius(@RequestBody ActualMissionInRadiusRequest request) {
         LOGGER.info("actualMissionInRadius: token=" + request.getSecurity_token() + " driverId="+request.getDriverId());
         return driverService.actualMissionInRadius(request.getDriverId(), request.getRadius(), request.getSecurity_token());
    }




    @RequestMapping(value = "/updateDriverSetting", method = RequestMethod.POST)
    public
    @ResponseBody
    UpdateDriverSettingResponse updateDriverSetting(@RequestBody UpdateDriverSettingRequest request) {
        return driverService.updateDriverSetting(request.getDriverSettingInfo());
    }



    @RequestMapping(value = "/driverSetting", method = RequestMethod.POST)
    public
    @ResponseBody
    DriverSettingResponse driverSetting(@RequestBody DriverSettingRequest request) {
        return driverService.driverSetting(request.getDriverId());
    }



    // спросить клиента о возможности снятии суммы с карты
    @RequestMapping(value = "/askClientForPaymentCard", method = RequestMethod.POST)
    public
    @ResponseBody
    AskClientForPaymentCardResponse askClientForPaymentCard(@RequestBody AskClientForPaymentCardRequest request) {
        return billingService.askClientForPaymentCard(request.getDriverId(), request.getMissionId(), request.getSum(), request.getSecurity_token());
    }




    @RequestMapping(value = "/payOrderFinishWithTip", method = RequestMethod.POST)
    public
    @ResponseBody
    PayOrderFinishResponse payOrder(@RequestBody PayOrderFinishRequest request) throws IOException, JSONException {
        LOGGER.info("payOrderFinishWithTip: token="+request.getSecurity_token() + " driverId=" + request.getDriverId() + " missionId="+request.getMissionId());
        return billingService.payOrderFinishWithTip(request.getMissionId(), request.getSecurity_token());
    }




    // начать смену
    @RequestMapping(value = "/startWork", method = RequestMethod.POST)
    public
    @ResponseBody
    DriverStartWorkResponse driverStartWork(@RequestBody DriverStartWorkRequest request) {
        LOGGER.info("startWork: token="+request.getSecurity_token() + " driverId=" + request.getDriverId());
        return driverService.driverStartWork(request.getDriverId(), request.getSecurity_token());
    }


    // получить
    @RequestMapping(value = "/getStartWork", method = RequestMethod.POST)
    public
    @ResponseBody
    DriverStartWorkResponse driverGetStartWork(@RequestBody DriverStartWorkRequest request) {
        LOGGER.info("getStartWork: token="+request.getSecurity_token() + " driverId=" + request.getDriverId());
        return driverService.driverGetStartWork(request.getDriverId(), request.getSecurity_token());
    }



    @RequestMapping(value = "/missionIsActual", method = RequestMethod.POST)
    public
    @ResponseBody
    ActualMissionResponse missionIsActual(@RequestBody ActualMissionRequest request) {
        return administrationService.missionIsActual(request.getMissionId());
    }



    @RequestMapping(value = "/requisite/v2", method = RequestMethod.POST)
    public
    @ResponseBody
    DriverRequisiteResponseV2 driverRequisiteV2(@RequestBody DriverRequisiteRequest request) {
        LOGGER.info("/requisite/v2 token="+request.getSecurity_token() + " driverId=" + request.getDriverId()+" requisiteId=" + request.getDriverRequisiteId());
        return driverService.requisiteV2(request.getDriverId(), request.getSecurity_token());
    }



    @RequestMapping(value = "/requisite", method = RequestMethod.POST)
    public
    @ResponseBody
    DriverRequisiteResponse driverRequisite(@RequestBody DriverRequisiteRequest request) {
        return driverService.requisite(request.getDriverId());
    }




    /*
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public
    @ResponseBody
    LoginDriverResponse login(@RequestBody LoginRequest request) { // LoginDriverResponse
        LoginDriverResponse response = new LoginDriverResponse();
        String login = request.getLogin();
        String password = request.getPassword();
        String versionApp = request.getVersionApp();
        DriverInfo driverInfo = administrationService.loginDriver(login, password, request.getDeviceInfoModel(), versionApp);
          response.setDriverInfo(driverInfo);
            return response;
    }
    */

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public
    @ResponseBody
    LoginDriverResponse loginv2(@RequestBody LoginRequest request, HttpServletRequest reqServlet) {
        String login = request.getLogin();
        String password = request.getPassword();
        String versionApp = request.getVersionApp();
        return driverService.loginDriverV2(login, password, request.getDeviceInfoModel(), versionApp, HTTPUtil.resolveIpAddress(reqServlet));
    }





    @RequestMapping(value = "/busyIsPayment", method = RequestMethod.POST)
    public
    @ResponseBody
    BusyIsPaymentResponse busyIsPayment(@RequestBody BusyIsPaymentRequest request) {
        LOGGER.info("busyIsPayment: token="+request.getSecurity_token() +" driverId="+request.getDriverId());
        return driverService.busyIsPayment(request.getDriverId(), request.getSecurity_token());
    }



    @RequestMapping(value = "/busy", method = RequestMethod.POST)
    public
    @ResponseBody
    BusyDriverResponse busy(@RequestBody BusyDriverRequest request) {
        LOGGER.info("/busy: token=" + request.getSecurity_token() + " driverId=" + request.getDriverId());
        return driverService.driverBusy(request.getDriverId(), request.isValue(), request.getSecurity_token());
    }



    @RequestMapping(value = "/location", method = RequestMethod.POST)
    public
    @ResponseBody
    DriverLocationResponse setLocation(@RequestBody DriverLocationRequest request) {
        DriverLocationResponse response = new DriverLocationResponse();
        commonService.saveDriverLocation(request.getDriverId(), request.getLocation(), request.getDistance());
           return response;
    }



    @RequestMapping(value = "/location/timeTracking", method = RequestMethod.POST)
    public
    @ResponseBody
    DriverLocationV2Response setLocationWithTimeTracking(@RequestBody DriverLocationV2Request request) throws JSONException {
        LOGGER.info("/location/timeTracking token=" + request.getSecurity_token() + " driverId=" + request.getDriverId() + " missionId=" + request.getMissionId());
        return driverService.saveDriverLocationWithTimeTracking(request.getDriverId(), request.getLocation(), request.getTimeWork(), request.getTimeRest(), request.getTimePayRest(), request.getSecurity_token(), request.getDistance());
    }



    @RequestMapping(value = "/ping", method = RequestMethod.POST)
    public
    @ResponseBody
    PingResponse ping(@RequestBody PingRequest request) {
        PingResponse response = new PingResponse();
            response.setTimestamp(timeService.nowMillis());
               return response;
    }




    @RequestMapping(value = "/mission/details", method = RequestMethod.POST)
    public
    @ResponseBody
    MissionInfoResponse details(@RequestBody MissionInfoRequest request) {
        MissionInfoResponse response = new MissionInfoResponse();
        long missionId = request.getMissionId();
        Mission mission = missionRepository.findOne(missionId);
        if (mission != null) {
            MissionInfo missionInfo = ModelsUtils.toModel(mission);

            /* отдавать единые номера  */
            missionInfo = (MissionInfo)commonService.commonPhoneNumber(missionInfo);

            response.setMissionInfo(missionInfo);
        }
        return response;
    }




    @RequestMapping(value = "/putMissionInQueue", method = RequestMethod.POST)
    @Transactional
    public
    @ResponseBody
    PutMissionInQueueResponse putMissionInQueue(@RequestBody PutMissionInQueueRequest request, HttpServletRequest reqServlet) {
        LOGGER.info("putMissionInQueue: token="+request.getSecurity_token() +" driverId="+request.getDriverId());
        return driverService.putMissionInQueue(request.getDriverId(), request.getMissionId(), request.getArrivalTime(), HTTPUtil.resolveIpAddress(reqServlet), request.getSecurity_token());
    }



    @RequestMapping(value = "/getMissionInQueue", method = RequestMethod.POST)
    public
    @ResponseBody
    GetMissionFromQueueResponse getMissionFromQueue(@RequestBody GetMissionFromQueueRequest request, HttpServletRequest reqServlet) throws JsonProcessingException, JSONException {
        LOGGER.info("getMissionInQueue: token="+request.getSecurity_token() +" driverId="+request.getDriverId());
        return driverService.getMissionFromQueue(request.getDriverId(), HTTPUtil.resolveIpAddress(reqServlet), request.getSecurity_token());
    }



    @RequestMapping(value = "/sendTripDetailsText", method = RequestMethod.POST)
    @Transactional
    public
    @ResponseBody
    GetTripDetailsTextResponse GetTripDetailsText(@RequestBody GetTripDetailsTextRequest request) throws IOException, JSONException {
        LOGGER.info("sendTripDetailsText: token=" + request.getSecurity_token() + " driverId=" + request.getDriverId());
        return administrationService.startGetText(request.getDriverId(), request.getSecurity_token());
    }




    @RequestMapping(value = "/mission/pause", method = RequestMethod.POST)
    public
    @ResponseBody
    TripPauseResponse pause(@RequestBody TripPauseRequest request) {
        LOGGER.info("sendTripDetailsText: token="+request.getSecurity_token() +" driverId="+request.getDriverId());
        TripPauseResponse response = new TripPauseResponse();
        administrationService.missionPause(request.getMissionId(), request.getLocation(), request.isPauseBegin());
          return response;
    }




    @RequestMapping(value = "/mission/late", method = RequestMethod.POST)
    public
    @ResponseBody
    DriverLateResponse late(@RequestBody DriverLateRequest request) {
        LOGGER.info("/mission/late: token="+request.getSecurity_token() +" driverId="+request.getDriverId());
        administrationService.driverLate(request.getDriverId(), request.getTime(), request.getSecurity_token());
          return new DriverLateResponse();
    }




    @RequestMapping(value = "/mission/find", method = RequestMethod.POST)
    @Transactional(readOnly = true)
    public
    @ResponseBody
    FindMissionResponse findMission(@RequestBody FindMissionRequest request) {
        // todo: we MUST implement search logic related to radius

        FindMissionResponse response = new FindMissionResponse();
        long driverId = request.getDriverId();

        Driver driver = driverRepository.findOne(driverId);

        if (driver != null && driver.getState().equals(Driver.State.AVAILABLE)) {
            List<Mission> missions = missionRepository.findByState(Mission.State.NEW);
            for (Mission mission : missions) {
                boolean possible = true;
//                for (Driver driverItem : mission.getDeclinedDrivers()) {
//                    if (driverItem.getId() == driverId) {
//                        possible = false;
//                        break;
//                    }
//                }
                if (possible) {
                    MissionInfo missionInfo = ModelsUtils.toModel(mission);
                    response.setMissionInfo(missionInfo);
                    break;
                }
            }
        }
        return response;
    }



    //f:add
    @RequestMapping(value = "/freshNews", method = RequestMethod.POST)
    public
    @ResponseBody
    DriverFreshNewsResponse newsList(@RequestBody DriverFreshNewsRequest request) {
         LOGGER.info("freshNews: token="+request.getSecurity_token() +" driverId="+request.getDriverId());
         return driverService.driverFreshNews(request.getDriverId(), request.getSecurity_token());
    }



    // показать последние 5 новостей для водителя
    @RequestMapping(value = "/news", method = RequestMethod.POST)
    public
    @ResponseBody
    DriverNewsResponse driverNews(@RequestBody DriverNewsRequest request) {
        LOGGER.info("news: token="+request.getSecurity_token() +" driverId="+request.getDriverId());
        return driverService.driverNews(request.getDriverId(), request.getSecurity_token());
    }



    @RequestMapping(value = "/setNewsAsRead", method = RequestMethod.POST)
    public
    @ResponseBody
    ReadNewsResponse setNewsAsRead(@RequestBody ReadNewsRequest request) {
        LOGGER.info("setNewsAsRead: token="+request.getSecurity_token() +" driverId="+request.getDriverId());
        return driverService.setNewsAsRead(request.getDriverId(), request.getNewsId(), request.getSecurity_token());
    }



    @RequestMapping(value = "/turboIncrease", method = RequestMethod.POST)
    public
    @ResponseBody
    TurboIncreaseDriverResponse turboIncreaseDriver(@RequestBody TurboIncreaseDriverRequest request) {
        TurboIncreaseDriverResponse response = new TurboIncreaseDriverResponse();
        //TurboIncreaseDriverResponse response =  administrationService.turboIncreaseDriver(request.getTurboIncreaseDriverInfo());
          return response;
    }



    @RequestMapping(value = "/mission/assign", method = RequestMethod.POST)
    public
    @ResponseBody
    AssignMissionResponse assign(@RequestBody AssignMissionRequest request) {
        AssignMissionResponse response = new AssignMissionResponse();

        int arrivalTime = administrationService.assignDriver(
                request.getMissionId(),
                request.getDriverId(),
                request.getArrivalTime());
        response.setAssigned(arrivalTime != 0);
        response.setTime(arrivalTime);
        return response;
    }




    /**
     * Get all missions with state BOOKED and bookingState WAITING
     * and also current driver booked missions.
     * @param request BookedRequest{driverId}
     * @return BookedResponse {counts -  bookedNew, bookedToMe; list of booked missions}
     */
    @RequestMapping(value = "/mission/booked", method = RequestMethod.POST)
    public
    @ResponseBody
    BookedResponse bookedMissions(@RequestBody BookedRequest request) {
        BookedResponse response = new BookedResponse();
        LOGGER.info("~~~~~~~~~~~~~~~~~~~  /mission/booked: token = "+request.getSecurity_token() + " driverId = "+request.getDriverId());
        Driver driver = driverRepository.findOne(request.getDriverId());
        if(driver == null){
            throw new CustomException(1, "Водитель не найден");
        }
        if(driver.getDriverSetting() != null && driver.getDriverSetting().getDriverType().equals(DriverSetting.DriverType.COURIER)){
              return response;
        }
        AdministrationService.BookedDetails bookedDetails = administrationService.bookedMissionsDriver(driver.getId());
        response.setBookedNew(bookedDetails.getBookedNew());
        response.setBookedToMe(bookedDetails.getBookedToMe());
        response.setMissions(bookedDetails.getMissions());
           return response;
    }





    @RequestMapping(value = "/lateDriverBookedMin", method = RequestMethod.POST)
    public
    @ResponseBody
    LateDriverBookedResponse lateDriverBookedMin(@RequestBody LateDriverBookedRequest request) {
        LOGGER.info("lateDriverBookedMin: token="+request.getSecurity_token() +" driverId="+request.getRequesterId() + " missionId=" + request.getMissionId());
          return administrationService.lateDriverBookedMin(request.getLateDriverBookedMin(), request.getMissionId(), request.getSecurity_token());
    }




    /*
     * Answer from driver for notification about assigned booked mission.
     * @param request BookedMissionConfirmRequest{missionId, decline}
     * @return
     */
    @RequestMapping(value = "/mission/booked/confirm", method = RequestMethod.POST)
    public
    @ResponseBody
    BookedMissionConfirmResponse confirmBooked(@RequestBody BookedMissionConfirmRequest request) {
        BookedMissionConfirmResponse response = new BookedMissionConfirmResponse();
        long missionId = request.getMissionId();
        boolean decline = request.isDecline();
        if(!decline) {
            administrationService.confirmBookedMission(missionId);
        } else {
            administrationService.declineAssignedBookedMission(missionId);
        }
            return response;
    }



    @RequestMapping(value = "/mission/decline", method = RequestMethod.POST)
    public
    @ResponseBody
    DeclineMissionResponse decline(@RequestBody DeclineMissionRequest request) {
        DeclineMissionResponse response = new DeclineMissionResponse();

        long missionId = request.getMissionId();
        long driverId = request.getDriverId();

        LOGGER.info("/mission/decline: token="+request.getSecurity_token() + " driverId="+request.getDriverId() + " missionId="+missionId);

        administrationService.declineMission(missionId, driverId, request.getSecurity_token());
        response.setMissionId(missionId);
        return response;
    }




    @RequestMapping(value = "/mission/clientlate", method = RequestMethod.POST)
    public
    @ResponseBody
    TooLongWaitingResponse waitingTooLong(@RequestBody TooLongWaitingRequest request) {
        TooLongWaitingResponse response = new TooLongWaitingResponse();
        long driverId = request.getDriverId();
        //administrationService.driverArrived(driverId);
          return response;
    }


    @RequestMapping(value = "/mission/seated", method = RequestMethod.POST)
    public
    @ResponseBody
    ClientSeatedResponse seated(@RequestBody ClientSeatedRequest request) {
        ClientSeatedResponse response = new ClientSeatedResponse();
        long missionId = request.getMissionId();
        administrationService.clientSeated(missionId);

        return response;
    }




    @RequestMapping(value = "/mission/finished", method = RequestMethod.POST)
    public
    @ResponseBody
    MissionFinishedResponse finished(@RequestBody MissionFinishedRequest request) {
        MissionFinishedResponse response = new MissionFinishedResponse();
        long missionId = request.getMissionId();

        PaymentInfo paymentInfo = administrationService.missionFinished(request.getDistanceInFact(), missionId);

        response.setPaymentInfo(paymentInfo);

        response.setTimestamp(timeService.nowDateTimeMillis());
        return response;
    }







    @RequestMapping(value = "/terminals", method = RequestMethod.POST)
    public
    @ResponseBody
    NearestTerminalsResponse findPayTerminals(@RequestBody NearestTerminalsRequest request) {
       NearestTerminalsResponse response = new NearestTerminalsResponse();
       List addressResult= new ArrayList();
       List<PayTerminals> payTerminals = Lists.newArrayList(payTerminalRepository.findAll());
       List<GeoUtils.LocationUtil> locationList = new ArrayList<>();
       DriverLocation locations = locationRepository.findByDriverId(request.getDriverId());

       if(locations!=null){
        final Location driverLocation = locations.getLocation();
        for (PayTerminals payTerminal : payTerminals) {
                double distance = GeoUtils.distance(
                        payTerminal.getLatitude(), payTerminal.getLongitude(),
                        driverLocation.getLatitude(), driverLocation.getLongitude());
                if(distance < 3000){
                    String fullAddress = payTerminal.getCity()+", "+payTerminal.getStreet()+", "+payTerminal.getHouse();
                    locationList.add(new GeoUtils.LocationUtil(payTerminal.getLatitude(), payTerminal.getLongitude(), distance, fullAddress));
                }
           }
    }

        Collections.sort(locationList, new Comparator<GeoUtils.LocationUtil>() {
            @Override
            public int compare(GeoUtils.LocationUtil loc1, GeoUtils.LocationUtil loc2) {
                return new Double(loc1.getDistance()).compareTo(new Double(loc2.getDistance()));
            }
        });
        for(GeoUtils.LocationUtil res :locationList){
            addressResult.add(res.getAddress());
        }
        response.setAddress(addressResult);
        return response;
    }






    @RequestMapping(value = "/checkTakeBooked", method = RequestMethod.POST)
    public
    @ResponseBody
    CheckTakeBookedResponse checkTakeBooked(@RequestBody CheckTakeBookedRequest request) {
        LOGGER.info("checkTakeBooked: token=" + request.getSecurity_token() + " driverId=" + request.getDriverId());
        return administrationService.checkTakeBooked(request.getDriverId(), request.getMissionId(), request.getSecurity_token());
    }




    @RequestMapping(value = "/timeStartBooked", method = RequestMethod.POST)
    public
    @ResponseBody
    TimeStartBookedResponse timeOfStartBooked(@RequestBody TimeStartBookedRequest request) {
        TimeStartBookedResponse response = new TimeStartBookedResponse();
        long missionId = request.getMissionId();
        int timeStart = administrationService.timeOfStartBooked(missionId);
           response.setTime(timeStart);
             return response;
    }





    @RequestMapping(value = "/editAutoClass", method = RequestMethod.POST)
    public
    @ResponseBody
    AutoClassEditResponse autoClassEdit(@RequestBody AutoClassEditRequest request) {
         return administrationService.autoClassEdit(request.getDriverId(), request.getAutoClass());
    }



    @RequestMapping(value = "/rating", method = RequestMethod.POST)
    public
    @ResponseBody
    DriverRatingResponse ratings(@RequestBody DriverRatingRequest request) {
        DriverRatingResponse response = new DriverRatingResponse();
           response.setDriverRating(administrationService.ratingDriver(request.getDriverId()));
            return response;
    }



    @RequestMapping(value = "/mission/calculation", method = RequestMethod.POST)
    public
    @ResponseBody
    PriceCalculationResponse calculation(@RequestBody PriceCalculationRequest request) {
        PriceCalculationResponse response = new PriceCalculationResponse();
        PaymentInfo paymentInfo = administrationService.calculateMissionPrice(request.getMissionId(), request.getPaymentInfo());
        // почему paymentInfo не вставляется в response.setPaymentInfo(..)
        response.setTimestamp(timeService.now().getTime());
        return response;
    }





    @RequestMapping(value = "/mission/history", method = RequestMethod.POST)
    @ResponseBody
    public TripsHistoryResponse findTripsHistory(@RequestBody TripsHistoryRequest request) {
        TripsHistoryResponse response = new TripsHistoryResponse();
        AdministrationService.HistoryMissions history = administrationService.missionsHistoryDriverV2(request.getRequesterId(), request.getSecurity_token());
        response.setHistory(history.history);
        response.setBooked(history.booked);
          return response;
    }





    @RequestMapping(value = "/ratings", method = RequestMethod.POST)
    public
    @ResponseBody
    DriversRatingsResponse ratings(@RequestBody DriversRatingsRequest request) {
        DriversRatingsResponse response = new DriversRatingsResponse();
        List<RatingItem> ratings = administrationService.ratingDrivers(request.getDriverId());
        response.getRatings().addAll(ratings);
          return response;
    }




    @RequestMapping(value = "/saveTimersData", method = RequestMethod.POST)
    @Transactional
    public
    @ResponseBody
    TimersDataResponse timersDataDriver(@RequestBody TimersDataRequest request) throws IOException, JSONException, ParseException {
        LOGGER.info("saveTimersData: token="+request.getSecurity_token() + " driverId="+request.getRequesterId());
        return driverService.timersDataDriver(request.getTimersDataInfos(), request.getSecurity_token(), request.getRequesterId());
    }



    @RequestMapping(value = "/calculateTimeOfArrival", method = RequestMethod.POST)
    public
    @ResponseBody
    TimeOfArrivalResponse calculateTimeOfArrival(@RequestBody TimeOfArrivalRequest request, HttpServletRequest reqServlet) throws IOException {
        LOGGER.info("calculateTimeOfArrival: token="+request.getSecurity_token() + " driverId="+request.getRequesterId());
        return driverService.calculateTimeOfArrival(request.getMissionId(), request.getDriverId(), HTTPUtil.resolveIpAddress(reqServlet), request.getSecurity_token());
    }


}