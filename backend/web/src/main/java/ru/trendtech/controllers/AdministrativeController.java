package ru.trendtech.controllers;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.trendtech.common.mobileexchange.model.client.*;
import ru.trendtech.common.mobileexchange.model.client.corporate.*;
import ru.trendtech.common.mobileexchange.model.common.*;
import ru.trendtech.common.mobileexchange.model.common.billing.paymobile.PayTestForRegisterCardRequest;
import ru.trendtech.common.mobileexchange.model.common.billing.paymobile.PayTestForRegisterCardResponse;
import ru.trendtech.common.mobileexchange.model.common.states.ServerStateInfo;
import ru.trendtech.common.mobileexchange.model.common.support.ClientAdministrationStatusResponse;
import ru.trendtech.common.mobileexchange.model.driver.*;
import ru.trendtech.common.mobileexchange.model.node.FantomDriverRequest;
import ru.trendtech.common.mobileexchange.model.node.FantomDriverResponse;
import ru.trendtech.common.mobileexchange.model.web.*;
import ru.trendtech.common.mobileexchange.model.web.MissionCompleteRequest;
import ru.trendtech.common.mobileexchange.model.web.MissionCompleteResponse;
import ru.trendtech.common.mobileexchange.model.web.PropertyRequest;
import ru.trendtech.common.mobileexchange.model.web.PropertyResponse;
import ru.trendtech.common.mobileexchange.model.web.corporate.*;
import ru.trendtech.domain.*;
import ru.trendtech.domain.admin.WebUser;
import ru.trendtech.models.ModelsUtils;
import ru.trendtech.repositories.*;
import ru.trendtech.services.MissionService;
import ru.trendtech.services.MongoDBServices;
import ru.trendtech.services.administration.AdministrationService;
import ru.trendtech.services.billing.BillingService;
import ru.trendtech.services.client.ClientService;
import ru.trendtech.services.common.CommonService;
import ru.trendtech.services.report.ReportService;
import ru.trendtech.services.resources.ProfilesResourcesService;
import ru.trendtech.services.validate.ValidatorService;
import ru.trendtech.utils.HTTPUtil;
import ru.trendtech.utils.TokenUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/*
 * File created by petr on 20/04/2014 23:00.
*/
@Controller
@RequestMapping("/admin")
public class AdministrativeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdministrativeController.class);
    @Autowired
    private ClientService clientService;
    @Autowired
    private MissionService missionService;
    @Autowired
    private AdministrationService administrationService;
    @Autowired
    private WebUserRepository webUserRepository;
    @Autowired
    private BillingService billingService;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private TaxoparkPartnersRepository taxoparkPartnersRepository;
    @Autowired
    private ProfilesResourcesService profilesResourcesService;
    @Autowired
    private MongoDBServices mongoDBServices;
    @Autowired
    private AssistantRepository assistantRepository;
    @Autowired
    private DriverCarPhotosRepository driverCarPhotosRepository;
    @Autowired
    private ReportService reportService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private ValidatorService validatorService;




    @RequestMapping(value = "/sendEmailForClient", method = RequestMethod.POST)
    public
    @ResponseBody
    SendEmailResponse sendEmailAllClient(@RequestBody SendEmailRequest request) throws Exception {
        administrationService.sendEmailForClient(request.getClientIds(), request.getTypeEmail(), request.getSubject(), request.getUrl()); //sendEmailAllClient(request.getHtmlText(), request.getSubject());
        SendEmailResponse response = new SendEmailResponse();
        return response;
    }





    @RequestMapping(value = "/emailUnsubscribe", method = RequestMethod.POST)
    @ResponseBody
    public EmailUnsubscribeResponse emailUnsubscribe(@RequestBody EmailUnsubscribeRequest request, HttpServletRequest servletRequest) throws JSONException {
        String token = TokenUtil.getMD5("fractal" + request.getRequesterId());
        if(!token.equals(request.getSecurity_token())){
            throw new CustomException(3, "Нарушение безопасности");
        }

        Client client = clientRepository.findOne(request.getRequesterId());
        if(client == null){
            throw new CustomException(1, "Клиент не найден");
        }
        clientService.emailUnsubscribe(client, request.isUnsubscribe(), HTTPUtil.resolveIpAddress(servletRequest));
            return new EmailUnsubscribeResponse();
    }





    @RequestMapping(value = "/bukanovReport", method = RequestMethod.POST)
    public
    @ResponseBody
    BukanovReportResponse bukanovReport(@RequestBody BukanovReportRequest request) {
        return reportService.bukanovReport(request.getSecurity_token(), request.getStartTime(), request.getEndTime());
    }



    @RequestMapping(value = "/updateMission", method = RequestMethod.POST)
    public
    @ResponseBody
    UpdateMissionResponse updateMission(@RequestBody UpdateMissionRequest request) {
        return administrationService.updateMission(request.getMissionInfoARM(), request.getSecurity_token());
    }



    @RequestMapping(value = "/taxoparkCashFlow", method = RequestMethod.POST)
    @ResponseBody
    public TaxoparkCashFlowResponse taxoparkCashFlow(@RequestBody TaxoparkCashFlowRequest request) {
        String security_token = request.getSecurity_token();
        long taxoparkId = request.getTaxoparkId();
        long startTime =  request.getStartTime();
        long endTime = request.getEndTime();
        int numberPage = request.getNumberPage();
        int sizePage = request.getSizePage();
        return administrationService.taxoparkCashFlow(security_token, startTime, endTime, taxoparkId, numberPage, sizePage);
    }



    @RequestMapping(value = "/taxopark/updateBalance", method = RequestMethod.POST)
    public
    @ResponseBody
    TaxoparkUpdateBalanceResponse taxoparkUpdateBalance(@RequestBody TaxoparkUpdateBalanceRequest request){
        administrationService.taxoparkUpdateBalanceARM(request.getTaxoparkId(), request.getMissionId(), request.getAmountOfMoney(), request.getOperation(), request.getSecurity_token(), request.getComment(), request.getArticleAdjustmentId());
            return new TaxoparkUpdateBalanceResponse();
    }




    @RequestMapping(value = "/setDriverTypeX", method = RequestMethod.POST)
    public
    @ResponseBody
    FantomDriverResponse setFantomDriver(@RequestBody FantomDriverRequest request) throws IOException, JSONException, ExecutionException, InterruptedException {
        return administrationService.setFantomDriver(request.getMissionId(), request.getClientId());
    }





    @RequestMapping(value = "/linkTabletToDriver", method = RequestMethod.POST)
    public
    @ResponseBody
    LinkTabletToDriverResponse linkTabletToDriver(@RequestBody LinkTabletToDriverRequest request) {
        if(webUserRepository.findByToken(request.getSecurity_token()) == null) {
            throw new CustomException(1, "Web user not found");
        }
        administrationService.linkTabletToDriver(request.getDriverId(), request.getTabletId());
           return new LinkTabletToDriverResponse();
    }



    @RequestMapping(value = "/linkRouterToDriver", method = RequestMethod.POST)
    public
    @ResponseBody
    LinkRouterToDriverResponse linkRouterToDriver(@RequestBody LinkRouterToDriverRequest request) {
        if(webUserRepository.findByToken(request.getSecurity_token()) == null) {
            throw new CustomException(1, "Web user not found");
        }
        administrationService.linkRouterToDriver(request.getDriverId(), request.getRouterId());
        return new LinkRouterToDriverResponse();
    }



    @RequestMapping(value = "/privateTariff", method = RequestMethod.POST)
    public
    @ResponseBody
    PrivateTariffResponse privateTariff(@RequestBody PrivateTariffRequest request) {
        return administrationService.privateTariff(request.getSecurity_token(), request.getClientId());
    }


    @RequestMapping(value = "/tablet", method = RequestMethod.POST)
    public
    @ResponseBody
    TabletResponse tabletList(@RequestBody TabletRequest request) {
        if(webUserRepository.findByToken(request.getSecurity_token()) == null) {
            throw new CustomException(1, "Web user not found");
        }
        return administrationService.tabletList(request.getTabletId(), request.getNumberPage(), request.getSizePage());
    }



    @RequestMapping(value = "/reasonsUpdate", method = RequestMethod.POST)
    public
    @ResponseBody
    UpdateReasonResponse reasonsUpdate(@RequestBody UpdateReasonRequest request) {
        if(webUserRepository.findByToken(request.getSecurity_token()) == null) {
            throw new CustomException(1, "Web user not found");
        }
            administrationService.reasonsUpdate(request.getReasonInfos());
            return new UpdateReasonResponse();
    }


    @RequestMapping(value = "/reasons", method = RequestMethod.POST)
    public
    @ResponseBody
    ReasonResponse reasons(@RequestBody ReasonRequest request) {
        WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
        if(webUser == null) {
            throw new CustomException(1, "Web user not found");
        }
        return commonService.reasons(request.getReasonId(), true);
    }



    @RequestMapping(value = "/tabletUpdate", method = RequestMethod.POST)
    public
    @ResponseBody
    TabletUpdateResponse tabletUpdate(@RequestBody TabletUpdateRequest request) {
        if(webUserRepository.findByToken(request.getSecurity_token()) == null) {
            throw new CustomException(1, "Web user not found");
        }
        return administrationService.tabletUpdate(request.getTabletInfos());
    }




    @RequestMapping(value = "/updateAdditionalService", method = RequestMethod.POST)
    public
    @ResponseBody
    UpdateAdditionalServiceResponse updateAdditionalService(@RequestBody UpdateAdditionalServiceRequest request) {
        WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
        if(webUser == null){
            throw new CustomException(1, "Web user not found");
        }
         administrationService.updateAdditionalService(request.getAdditionalServiceInfoList());
            return new UpdateAdditionalServiceResponse();
    }




    @RequestMapping(value = "/additionalService", method = RequestMethod.POST)
    public
    @ResponseBody
    AdditionalServiceResponse additionalService(@RequestBody AdditionalServiceRequest request) {
        WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
        if(webUser == null){
            throw new CustomException(1, "Web user not found");
        }
        return administrationService.additionalService(request.getServiceId());
    }





    @RequestMapping(value = "/router", method = RequestMethod.POST)
    public
    @ResponseBody
    RouterResponse routerList(@RequestBody RouterRequest request) {
        if(webUserRepository.findByToken(request.getSecurity_token()) == null) {
            throw new CustomException(1, "Web user not found");
        }
        return administrationService.routerList(request.getRouterId(), request.getNumberPage(), request.getSizePage());
    }



    @RequestMapping(value = "/routerUpdate", method = RequestMethod.POST)
    public
    @ResponseBody
    RouterUpdateResponse routerUpdate(@RequestBody RouterUpdateRequest request) {
        if(webUserRepository.findByToken(request.getSecurity_token()) == null) {
            throw new CustomException(1, "Web user not found");
        }
        return administrationService.routerUpdate(request.getRouterInfos());
    }



    // log: not done
    @RequestMapping(value = "/userinfo", method = RequestMethod.POST)
    public
    @ResponseBody
    UserIdInfoResponse resolveUserIdInfo(@RequestBody UserIdInfoRequest request) {
        UserIdInfoResponse response = new UserIdInfoResponse();
        response.setInfo(administrationService.resolveUserInfo(request.getPhone()));
         return response;
    }




    // log: not use
    @RequestMapping(value = "/comission", method = RequestMethod.POST)
    public
    @ResponseBody
    ComissionResponse getComission(@RequestBody ComissionRequest request) {
           return administrationService.getComission(request.getSecurity_token(), request.getComissionType(), request.getObjectId());
    }



    // log: not use
    @RequestMapping(value = "/comissionUpdate", method = RequestMethod.POST)
    public
    @ResponseBody
    ComissionUpdateResponse comissionUpdate(@RequestBody ComissionUpdateRequest request) {
         ComissionUpdateResponse response = new ComissionUpdateResponse();
           administrationService.comissionUpdate(request.getSecurity_token(), request.getComissionInfo());
             return response;
    }



    @RequestMapping(value = "/driverRequisite", method = RequestMethod.POST)
    public
    @ResponseBody
    DriverRequisiteARMResponse driverRequisite(@RequestBody DriverRequisiteARMRequest request) {
        return administrationService.driverRequisite(request.getSecurity_token(), request.getDriverId(), request.getDriverRequisiteId());
    }


    // добавление/обновление реквизитов водителя
    @RequestMapping(value = "/driverRequisiteUpdate", method = RequestMethod.POST)
    public
    @ResponseBody
    DriverRequisiteUpdateResponse driverRequisiteUpdate(@RequestBody DriverRequisiteUpdateRequest request) {
            return administrationService.driverRequisiteUpdate(request.getSecurity_token(), request.getDriverRequisiteInfo());
    }



//    @RequestMapping(value = "/registration", method = RequestMethod.POST)
//    @Transactional
//    public
//    @ResponseBody
//    RegistrationDispatcherResponse resolveUserIdInfo(@RequestBody RegistrationDispatcherRequest request) {
//        RegistrationDispatcherResponse response = new RegistrationDispatcherResponse();
//        WebUser webUser = administrationService.registerDispatcher(request.getUser());
//        if(webUser != null){
//            response.setUserId(webUser.getId());
//        }
//        }
//        return response;
//    }


    @RequestMapping(value = "/driver/rating", method = RequestMethod.POST)
    public
    @ResponseBody
    DriverRatingARMResponse ratings(@RequestBody DriverRatingARMRequest request) {
        DriverRatingARMResponse response = new DriverRatingARMResponse();
           WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
               if(webUser == null){
                   response.getErrorCodeHelper().setErrorCode(1);
                   response.getErrorCodeHelper().setErrorMessage("Web user not found");
                     return response;
               }
        response.setDriverRating(administrationService.ratingDriver(request.getDriverId()));
           return response;
    }




    @RequestMapping(value = "/mission/details/arm", method = RequestMethod.POST)
    public
    @ResponseBody
    MissionInfoARMResponse missionDetailsARM(@RequestBody MissionInfoARMRequest request) {
        return administrationService.detailsARM(request.getMissionId(), request.getSecurity_token());
    }




    // log: +
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public
    @ResponseBody
    AdministratorLoginResponse adminLogin(@RequestBody LoginRequest request) {
        return administrationService.adminLogin(request.getLogin(), request.getPassword());
    }




    // log: not use
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public
    @ResponseBody
    LogoutResponse logout(@RequestBody LogoutRequest request) {
        LogoutResponse response = new LogoutResponse();
        WebUser webUser = webUserRepository.findOne(request.getClientId());
        if (webUser != null){
            webUser.setState(WebUser.State.OFFLINE);
              webUserRepository.save(webUser);
        }
        return response;
    }



    // log: +
    @RequestMapping(value = "/insertAutoClassPrice", method = RequestMethod.POST)
    public
    @ResponseBody
    InsertAutoClassPriceResponse insertAutoClassPrices(@RequestBody InsertAutoClassPriceRequest request) {
        return administrationService.insertAutoClassPrices(request.getSecurity_token(), request.getAutoClassRateInfoV2());
    }


    // log: +
    @RequestMapping(value = "/updateAutoClassPrice", method = RequestMethod.POST)
    public
    @ResponseBody
    UpdateAutoClassPriceResponse updateAutoClassPrices(@RequestBody UpdateAutoClassPriceRequest request) {
      return administrationService.updateAutoClassPrices(request.getSecurity_token(), request.getAutoClassRateInfoV2());
    }



    // log: none
    @RequestMapping(value = "/autoClassPrice", method = RequestMethod.POST)
    public
    @ResponseBody
    AutoClassPriceResponse autoClassPrices(@RequestBody AutoClassPriceRequest request) {
        return administrationService.autoClassPricesForArm(request.getSecurity_token(), request.getAutoClass());
    }



    // log: +
    @RequestMapping(value = "/scores", method = RequestMethod.POST)
    public
    @ResponseBody
    DriverEstimatesARMResponse scoresDriver(@RequestBody DriverEstimatesARMRequest request) {
        return administrationService.driverEstimatesARM(request.getDriverId(), request.getSecurity_token());
    }




    // f:add
    @RequestMapping(value = "/mission/booked", method = RequestMethod.POST)
    public
    @ResponseBody
    BookedARMResponse bookedMissions(@RequestBody BookedARMRequest request) {
        BookedARMResponse response = new BookedARMResponse();
        WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
          if(webUser == null){
              return response;
          }
        AdministrationService.BookedDetails bookedDetails = administrationService.onlyBookedMissionsDriver(request.getDriverId());
        response.setBookedToMe(bookedDetails.getBookedToMe());
        response.setMissions(bookedDetails.getMissions());
           return response;
    }




    // log: +
    @RequestMapping(value = "/insertPartnersGroup", method = RequestMethod.POST)
    public
    @ResponseBody
    InsertPartnersGroupResponse insertPartnersGroup(@RequestBody InsertPartnersGroupRequest request) {
          return administrationService.insertPartnersGroup(request.getPartnersGroupInfo(), request.getSecurity_token());
    }



    @RequestMapping(value = "/updatePartnersGroup", method = RequestMethod.POST)
    public
    @ResponseBody
    UpdatePartnersGroupResponse updatePartnersGroup(@RequestBody UpdatePartnersGroupRequest request) {
        UpdatePartnersGroupResponse response = new UpdatePartnersGroupResponse();
        WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
        if(webUser==null){
            return response;
        }
        response = administrationService.updatePartnersGroup(request.getPartnersGroupInfo());
        return response;
    }



//    InsertOrUpdateItemPartnersGroupRequest {
//        private ItemPartnersGroupInfo
    @RequestMapping(value = "/insertItemPartnersGroup", method = RequestMethod.POST)
    public
    @ResponseBody
    InsertItemPartnersGroupResponse insertItemPartnersGroup(@RequestBody InsertItemPartnersGroupRequest request) {
        InsertItemPartnersGroupResponse response = administrationService.insertItemPartnersGroup(request.getItemPartnersGroupInfo());
           return response;
    }



    @RequestMapping(value = "/updateItemPartnersGroup", method = RequestMethod.POST)
    public
    @ResponseBody
    UpdateItemPartnersGroupResponse updateItemPartnersGroup(@RequestBody UpdateItemPartnersGroupRequest request) {
          return administrationService.updateItemPartnersGroup(request.getItemPartnersGroupInfo(), request.getSecurity_token());
    }


    // дергает клиент
    @RequestMapping(value = "/findGroupBySection", method = RequestMethod.POST)
    public
    @ResponseBody
    FindGroupBySectionResponse findGroupBySection(@RequestBody FindGroupBySectionRequest request) {
          return administrationService.findGroupBySection(request.getSection());
    }



    @RequestMapping(value = "/findGroupBySection/arm", method = RequestMethod.POST)
    public
    @ResponseBody
    FindGroupBySectionARMResponse findGroupBySectionARM(@RequestBody FindGroupBySectionARMRequest request) {
        return administrationService.findGroupBySectionARM(request.getSection(), request.getSecurity_token());
    }



    // f:add статистика по водителю
    @RequestMapping(value = "/driver/stats", method = RequestMethod.POST)
    public
    @ResponseBody
    DriverStatsResponse getDriverStats(@RequestBody DriverStatsRequest request) {
        DriverStatsResponse response = new DriverStatsResponse();
         WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
            if(webUser==null){
                return response;
            }
        Driver driver = driverRepository.findOne(request.getDriverId());
        DriverStatsInfo driverStatsInfo = reportService.getDriverStats(driver, webUser.getTaxoparkId()); // , webUser.getTaxoparkId()
           response.setDriverStatsInfo(driverStatsInfo);
             return response;
    }




    @RequestMapping(value = "/driver/global/stats", method = RequestMethod.POST)
    public
    @ResponseBody
    GlobalDriverStatsResponse getDriverGlobalStats(@RequestBody GlobalDriverStatsRequest request) {
         return reportService.getDriverGlobalStats(request.getTaxoparkId(), request.getAssistantId(), request.getRegistrationStart(), request.getRegistrationEnd());
    }



    @RequestMapping(value = "/client/global/stats", method = RequestMethod.POST)
    public
    @ResponseBody
    GlobalClientStatsResponse getClientGlobalStats(@RequestBody GlobalClientStatsRequest request) {
        return reportService.getClientGlobalStats(request.getRegistrationStart(), request.getRegistrationEnd());
    }


    @RequestMapping(value = "/missionStateStatistic", method = RequestMethod.POST)
    public
    @ResponseBody
    MissionStateStatisticResponse getMissionStateStatistic(@RequestBody MissionStateStatisticRequest request) {
        return administrationService.getMissionStateStatistic(request.getSecurity_token(), request.getMissionId(), request.getState(), request.getStartTime(), request.getEndTime(), request.getSizePage(), request.getNumberPage());
    }



    @RequestMapping(value = "/own/driver/stats", method = RequestMethod.POST)
    public
    @ResponseBody
    OwnDriverStatsResponse getOwnDriverStats(@RequestBody OwnDriverStatsRequest request) {
        OwnDriverStatsResponse response = new OwnDriverStatsResponse();
          WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
          if(webUser==null){
             return response;
          }
          response = administrationService.getOwnDriverStats(request.getSecurity_token(), request.getDriverId(), request.getAssistantId(), request.getStartTime(), request.getEndTime(), webUser.getTaxoparkId());
             return response;
    }



    // f:add статистика по клиенту
    @RequestMapping(value = "/client/stats", method = RequestMethod.POST)
    public
    @ResponseBody
    ClientStatsResponse getDriverStats(@RequestBody ClientStatsRequest request) {
        ClientStatsResponse response = new ClientStatsResponse();
           ClientStatsInfo clientStatsInfo = reportService.getClientStats(request.getClientId());
            response.setClientStatsInfo(clientStatsInfo);
             return response;
    }



    //f:add
    @RequestMapping(value = "/assistant/stat/list", method = RequestMethod.POST)
    public
    @ResponseBody
    AssistantStatListResponse findAssistantStatsListByMask(@RequestBody AssistantStatListRequest request) {
        long timeRequestingStart = request.getTimeRequestingStart();
        long timeRequestingEnd = request.getTimeRequestingEnd();
        long taxoparkId = request.getTaxoparkId();
        long driverId = request.getDriverId();
        List<Long> assistantIdList = request.getAssistantId();
        int numberPage = request.getNumberPage();
        int sizePage = request.getSizePage();
        AssistantStatListResponse response =  administrationService.getAssistantInfStatListByMask(numberPage, sizePage, driverId, taxoparkId, assistantIdList, timeRequestingStart, timeRequestingEnd);
          return response;
    }



    //f:add
    @RequestMapping(value = "/assistant/stat/list/ext", method = RequestMethod.POST)
    public
    @ResponseBody
    AssistantStatListResponse findAssistantStatsListByMaskExtended(@RequestBody AssistantStatListRequest request) throws ParseException {
        long timeRequestingStart = request.getTimeRequestingStart();
        long timeRequestingEnd = request.getTimeRequestingEnd();
        long taxoparkId = request.getTaxoparkId();
        long driverId = request.getDriverId();
        List<Long> assistantIdList = request.getAssistantId();
        int numberPage = request.getNumberPage();
        int sizePage = request.getSizePage();

        AssistantStatListResponse response =  administrationService.getAssistantInfStatListByMaskExtended(numberPage, sizePage, driverId, taxoparkId, assistantIdList, timeRequestingStart, timeRequestingEnd);
           return response;
    }




    //f:add
    @RequestMapping(value = "/client/stat/list", method = RequestMethod.POST)
    public
    @ResponseBody
    ClientStatListResponse findClientStatsListByMask(@RequestBody ClientStatListRequest request) {
        long registartionDateStart = request.getRegistrationDateStart();
        long registartionDateEnd = request.getRegistrationDateEnd();
        int countCanceledStart =  request.getCountCanceledStart();
        int countCanceledEnd =  request.getCountCanceledEnd();
        String nameMask = request.getNameMask();
        String phoneMask = request.getPhoneMask();
        String adminStatus = request.getAdminStatus();
        int countMissionStart = request.getCountMissionStart();
        int countMissionEnd = request.getCountMissionEnd();

        ClientStatListResponse response =  administrationService.getClientInfoStatListByMask(request.getNumberPage(), request.getSizePage(), nameMask, phoneMask, adminStatus, countMissionStart, countMissionEnd, countCanceledStart, countCanceledEnd, registartionDateStart, registartionDateEnd);
            return response;
    }



    @RequestMapping(value = "/moneyRefuse", method = RequestMethod.POST)
    public
    @ResponseBody
    MoneyRefuseResponse moneyRefuse(@RequestBody MoneyRefuseRequest request) {
        MoneyRefuseResponse response = billingService.startMoneyRefuse(request.getMdOrderId());
           return response;
    }




    // f:add очистить текущую миссию на водителе
    @RequestMapping(value = "/driver/clearCurrentMission", method = RequestMethod.POST)
    public
    @ResponseBody
    ClearCurrentMissionResponse clearDriverCurrentMission(@RequestBody ClearCurrentMissionRequest request) {
        ClearCurrentMissionResponse response = administrationService.clearDriverCurrentMission(request.getRequesterId());
             return response;
    }



    // f:add очистить текущую миссию на клиенте
    @RequestMapping(value = "/client/clearCurrentMission", method = RequestMethod.POST)
    public
    @ResponseBody
    ClearCurrentMissionResponse clearClientCurrentMission(@RequestBody ClearCurrentMissionRequest request) {
        ClearCurrentMissionResponse response = administrationService.clearClientCurrentMission(request.getRequesterId());
           return response;
    }



    // f:add mission transfer
    @RequestMapping(value = "/missionTransfer", method = RequestMethod.POST)
    public
    @ResponseBody
    MissionTransferResponse missionTransfer(@RequestBody MissionTransferRequest request, HttpServletRequest servletRequest) throws JSONException {
           return administrationService.missionTransfer(request.getMissionId(), request.getDriverIdTo(), request.getSecurity_token(), HTTPUtil.resolveIpAddress(servletRequest));
    }



    // перенос всех оценок с mission to estimate table
    @RequestMapping(value = "/transferEstimate", method = RequestMethod.POST)
    public
    @ResponseBody
    EstimateTransferResponse transferEstimate(@RequestBody EstimateTransferRequest request) {
        return administrationService.transferEstimate();
    }



    @RequestMapping(value = "/transferDeviceType", method = RequestMethod.POST)
    public
    @ResponseBody
    DeviceTypeTransferResponse transferDeviceType(@RequestBody DeviceTypeTransferRequest request) {
        return administrationService.transferDeviceType();
    }



    @RequestMapping(value = "/mission/canceled/list", method = RequestMethod.POST)
    public
    @ResponseBody
    CanceledMissionListResponse canceledMissionList(@RequestBody CanceledMissionListRequest request) {
        String cancelBy = request.getCancelBy();
        long cancelById = request.getCancelById();
        long taxoparkId =request.getTaxoparkId();
        long timeOfStart = request.getTimeOfCanceledStart();
        long timeOfEnd = request.getTimeOfCanceledEnd();
        int numberPage = request.getNumberPage();
        int sizePage = request.getSizePage();
        String security_token = request.getSecurity_token();
            return administrationService.canceledMissionList(numberPage, sizePage, cancelBy, cancelById, taxoparkId, timeOfStart, timeOfEnd, security_token);
    }




    @RequestMapping(value = "/mission/cancel", method = RequestMethod.POST)
    public
    @ResponseBody
    CancelMissionResponse cancel(@RequestBody CancelMissionRequest request) {
        WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
        if(webUser==null){
            throw new CustomException(1,"WebUser not found");
        }
        CancelMissionResponse response = new CancelMissionResponse();
           administrationService.cancelMissionByAdmin(request.getMissionId(), request.getComment(), request.getReason(), request.getInitiatorId(), webUser, "operator");
               return response;
    }



    @RequestMapping(value = "/mission/completed", method = RequestMethod.POST)
    public
    @ResponseBody
    MissionCompleteResponse completedMission(@RequestBody MissionCompleteRequest request) {
          return administrationService.missionCompletedByAdmin(request.getMissionId(), request.getReason(), request.getWebUserId());
    }




    // f:add увеличить стоимость заказа
    @RequestMapping(value = "/missionCostIncrease", method = RequestMethod.POST)
    public
    @ResponseBody
    MissionCostIncreaseResponse missionCostIncrease(@RequestBody MissionCostIncreaseRequest request) {
        MissionCostIncreaseResponse response = administrationService.missionCostIncrease(request.getMissionId(), request.getSumIncrease());
          return response;
    }




    @RequestMapping(value = "/rebornMission", method = RequestMethod.POST)
    public
    @ResponseBody
    RebornMissionResponse rebornMission(@RequestBody RebornMissionRequest request) {
        RebornMissionResponse response = new RebornMissionResponse();
           administrationService.rebornMission(request.getMissionId(), Money.of(CurrencyUnit.of("RUB"), request.getSumIncrease()));
             return response;
    }




    // MONGO - logging
    @RequestMapping(value = "/loggingEventMongoList", method = RequestMethod.POST)
    public
    @ResponseBody
    LoggingEventMongoResponse findLoggingEventMongo(@RequestBody LoggingEventMongoRequest request) {
        LoggingEventMongoResponse response = new LoggingEventMongoResponse();
          response = mongoDBServices.findLoggingEventMongoList();
            return response;
    }




    // MONGO - location
    @RequestMapping(value = "/locationMongoList", method = RequestMethod.POST)
    public
    @ResponseBody
    LocationMongoResponse findLocationListMongo(@RequestBody LocationMongoRequest request) {
        LocationMongoResponse response = new LocationMongoResponse();
        //LocationMongoResponse response = new LocationMongoResponse();
        //  response = mongoDBServices.findLocationListMongo(request.getMissionId(), request.getDriverId(), request.getStartWhenSeen(), request.getEndWhenSeen(), request.getType(), request.getNumberPage(), request.getSizePage(), request.getLatStart(), request.getLatEnd(), request.getLonStart(), request.getLonEnd());
             return response;
    }



    //f:add
    @RequestMapping(value = "/news/update", method = RequestMethod.POST)
    public
    @ResponseBody
    UpdateNewsResponse updateNews(@RequestBody UpdateNewsRequest request) {
          return administrationService.updateNews(request.getNewsInfo(), request.getSecurity_token());
    }


    @RequestMapping(value = "/driverNews", method = RequestMethod.POST)
    public
    @ResponseBody
    NewsResponse driverNews(@RequestBody NewsRequest request) {
        return administrationService.driverNews(request.getSecurity_token(), request.getStartTime(), request.getEndTime(), request.getNewsId());
    }


    @RequestMapping(value = "/deleteDriverNews", method = RequestMethod.POST)
    public
    @ResponseBody
    DeleteNewsResponse deleteNews(@RequestBody DeleteNewsRequest request) {
        return administrationService.deleteDriverNews(request.getSecurity_token(), request.getNewsIds());
    }



    @RequestMapping(value = "/driver/updateBalance", method = RequestMethod.POST)
    public
    @ResponseBody
    DriverUpdateBalanceResponse updateDriverBalance(@RequestBody DriverUpdateBalanceRequest request, HttpServletRequest servletRequest){
        administrationService.updateDriverBalanceARM(request.getDriverId(), request.getMissionId(), request.getAmountOfMoney(), request.getOperation(), request.getSecurity_token(), request.getComment(), request.getArticleAdjustmentId(), HTTPUtil.resolveIpAddress(servletRequest));
          return new DriverUpdateBalanceResponse();
    }


    @RequestMapping(value = "/smsSendARM", method = RequestMethod.POST)
    public
    @ResponseBody
    SmsSendARMResponse smsSendDefault(@RequestBody SmsSendARMRequest request) {
        SmsSendARMResponse response = administrationService.smsSendARM(request.getPhone(), request.getMessage(), request.getSecurity_token());
        return response;
    }


    //f:add корректировка бонусов клиента - ARM
    //@ExceptionHandler(ExceptionHandlerController.class)
    @RequestMapping(value = "/client/updateBonuses", method = RequestMethod.POST)
    public
    @ResponseBody
    ClientUpdateBonusesResponse updateClientBonuses(@RequestBody ClientUpdateBonusesRequest request, HttpServletRequest servletRequest){
          return administrationService.updateClientBonusesARM(request.getClientId(), request.getMissionId(), request.getAmountOfMoney(), request.getOperation(), request.getSecurity_token(), request.getComment(), HTTPUtil.resolveIpAddress(servletRequest)); // всегда 4 операция;
    }



    //f:add штрафы водителя(й)
    @RequestMapping(value = "/driver/penalizationList", method = RequestMethod.POST)
    public
    @ResponseBody
    DriverPenalizationListResponse driversPenalizationList(@RequestBody DriverPenalizationListRequest request) {
      DriverPenalizationListResponse response = administrationService.driverPenalizationList(request.getDriverId(), request.getStartTime(), request.getEndTime());
        return response;
    }




    @RequestMapping(value = "/orderStatus", method = RequestMethod.POST)
    public
    @ResponseBody
    OrderStatusResponse orderStatus(@RequestBody OrderStatusRequest request) {
        OrderStatusResponse response = new OrderStatusResponse();
          response = billingService.getOrderStatusFull(request.getMdOrder());
             return response;
    }




    @RequestMapping(value = "/mdOrderListByClient", method = RequestMethod.POST)
    public
    @ResponseBody
    MdOrderClientResponse mdOrderListClient(@RequestBody MdOrderClientRequest request) {
       return administrationService.mdOrderListClient(request.getClientId());
    }




    @RequestMapping(value = "/checkBonus", method = RequestMethod.POST)
    public
    @ResponseBody
    BonusSumAmountResponse checkBonus(@RequestBody BonusSumAmountRequest request) {
       BonusSumAmountResponse response = new BonusSumAmountResponse();
          /*
          временно отключаем
          response = administrationService.checkBonus();
          */
        return response;
    }




    // ручное назначение водителя на бронь
    @RequestMapping(value = "/assigningDriverToBooking", method = RequestMethod.POST)
    public
    @ResponseBody
    AssigningDriverToBookingResponse assigningDriverToBooking(@RequestBody AssigningDriverToBookingRequest request) throws JSONException {
        AssigningDriverToBookingResponse response = new AssigningDriverToBookingResponse();
         administrationService.assigningDriverToBooking(request.getSecurity_token(), request.getDriverId(), request.getMissionId());
          return response;
    }



    @RequestMapping(value = "/tempPass", method = RequestMethod.POST)
    public
    @ResponseBody
    TemporaryPasswordResponse temporaryPassword(@RequestBody TemporaryPasswordRequest request) {
        TemporaryPasswordResponse response = new TemporaryPasswordResponse();
        if (!validatorService.validateUser(request.getWebUserId(), request.getSecurity_token(), 3)) {
             throw new CustomException(1, "Web user not found");
        }
        WebUser webUser = webUserRepository.findOne(request.getWebUserId());
        if (EnumSet.of(AdministratorRole.ADMIN, AdministratorRole.DISPATCHER).contains(webUser.getRole())) {
            response = administrationService.temporaryPassword(request.getClientId(), request.getPhone());
        }else{
            response.getErrorCodeHelper().setErrorCode(2);
            response.getErrorCodeHelper().setErrorMessage("Permission denied");
        }

            return response;
    }





    @RequestMapping(value = "/payTestForRegisterCard", method = RequestMethod.POST)
    public
    @ResponseBody PayTestForRegisterCardResponse testRegisterOrderInAlphaBank(@RequestBody PayTestForRegisterCardRequest request) {
        PayTestForRegisterCardResponse response = new PayTestForRegisterCardResponse();
        Client client = clientRepository.findOne(request.getClientId());
        String versApp =  client.getVersionApp();

        CheckVersionResponse checkResult = administrationService.checkVersion(versApp, client.getDeviceType());
               if(checkResult.getErrorCodeHelper().getErrorCode()==-1){
                   response.getErrorCodeHelper().setErrorCode(-4);
                   response.getErrorCodeHelper().setErrorMessage("Чтобы использовать оплату по банковской карте обновите пожалуйста ваше приложение");
                   response.setErrorCode("-4");
                   response.setErrorMessage("Чтобы использовать оплату по банковской карте обновите пожалуйста ваше приложение");
               }else if(checkResult.getErrorCodeHelper().getErrorCode()==1 || checkResult.getErrorCodeHelper().getErrorCode()==0){
                   response = billingService.testRegisterOrderInAlphaBank(request.getClientId(), request.getOrderNumber());
               }
        return response;
    }






    @RequestMapping(value = "/countClient", method = RequestMethod.POST)
    public
    @ResponseBody
    CountClientResponse countClient(@RequestBody CountClientRequest request) {
         CountClientResponse response = new CountClientResponse();
            CountClientHelper countClientHelper = administrationService.getCountClientsByPeriod(request.getTimeRegistrationStart(), request.getTimeRegistrationEnd());
              response.setCountClientHelper(countClientHelper);
                 return response;
    }



    @RequestMapping(value = "/periodWork", method = RequestMethod.POST)
    public
    @ResponseBody
    PeriodWorkResponse periodWork(@RequestBody PeriodWorkRequest request) {
        return administrationService.periodWork(request.getSecurity_token(), request.getNumberPage(), request.getSizePage());
    }



    @RequestMapping(value = "/corporateBalanceReport", method = RequestMethod.POST)
    public
    @ResponseBody
    CorporateBalanceReportResponse corporateBalanceReport(@RequestBody CorporateBalanceReportRequest request) {
        return administrationService.corporateBalanceReport(request.getSecurity_token(), request.getMainClientId(), request.getStartDateTime(), request.getEndDateTime());
    }





    //f:add
    @RequestMapping(value = "/mission/list", method = RequestMethod.POST)
    public
    @ResponseBody
    MissionsListResponse findMissionListByMask(@RequestBody MissionsListRequest request) {
        String nameMask = request.getNameMask();
        String phoneMask = request.getPhoneMask();
        String carModelMask = request.getCarModelMask();
        String carNumberMask = request.getCarNumberMask();
        String missionState = request.getState();
        String typeOS = request.getTypeOS();
        long startTime =  request.getDateStart();
        long endTime = request.getDateEnd();
        long missionId = request.getMissionId();
        long assistantId = request.getAssistantId();
        String security_token = request.getSecurity_token();
        long clientId = request.getClientId();
        long driverId = request.getDriverId();
        int autoType = request.getAutoType();
        Boolean onlyBooked = request.isOnlyBooked();
        MissionsListResponse response =  administrationService.findMissionListByMask_HQL(security_token, request.getNumberPage(), request.getSizePage(), startTime, endTime, nameMask, phoneMask, carModelMask, carNumberMask, missionState, missionId, assistantId, typeOS, clientId, autoType, onlyBooked, driverId);
         return response;
    }





    /*
        Нужен метод бэкенда для возврата данных по всем операциям за DriverCashFlow за период.
        Фильтры: дата/время, таксопарк, водитель, клиент.
        В следующем виде:
        Дата/время, номер заказа, клиент, водитель, операция 1, операция 2 и т.д.
     */

    @RequestMapping(value = "/driverCashFlow", method = RequestMethod.POST)
    public
    @ResponseBody
    DriverCashFlowResponse driverCashFlow(@RequestBody DriverCashFlowRequest request) {
        String security_token = request.getSecurity_token();
        long clientId = request.getClientId();
        long taxoparkId = request.getTaxoparkId();
        long driverId = request.getDriverId();
        long startTime =  request.getStartTime();
        long endTime = request.getEndTime();
        int numberPage = request.getNumberPage();
        int sizePage = request.getSizePage();
        return administrationService.driverCashFlow(security_token, startTime, endTime, clientId, taxoparkId, driverId, numberPage, sizePage);
    }




    @RequestMapping(value = "/corporateMissionStat", method = RequestMethod.POST)
    @ResponseBody
    public CorporateMissionStatResponse corporateMissionStat(@RequestBody CorporateMissionStatRequest request){
        return administrationService.corporateMissionStat(request.getSecurity_token(), request.getMainClientId(), request.getClientId(), request.getStartTime(), request.getEndTime(), request.getPageSize(), request.getNumPage());
    }



    @RequestMapping(value = "/driverCorrection", method = RequestMethod.POST)
    public
    @ResponseBody
    DriverCorrectionResponse driverCorrection(@RequestBody DriverCorrectionRequest request) {
        String security_token = request.getSecurity_token();
        long webUserId = request.getWebUserId();
        long driverId = request.getDriverId();
        long startTime =  request.getStartTime();
        long endTime = request.getEndTime();
        long articleId = request.getArticleId();
        return administrationService.driverCorrection(security_token, startTime, endTime, webUserId, driverId, articleId);
    }



    @RequestMapping(value = "/webUser/list", method = RequestMethod.POST)
    public
    @ResponseBody
    WebUserListResponse findClientListByMask(@RequestBody WebUserListRequest request) {
        return administrationService.getWebUserList(request.getSecurity_token(), request.getWebUserId());
    }




    //f:add
    @RequestMapping(value = "/client/list", method = RequestMethod.POST)
    public
    @ResponseBody
    ClientsListResponse findClientListByMask(@RequestBody ClientsListRequest request) {
        String phoneMask = request.getPhoneMask();
        String emailMask = request.getEmailMask();
        String nameMask = request.getNameMask();
        String security_token = request.getSecurity_token();
        ClientsListResponse response =  administrationService.getClientInfoListByMask(security_token, request.getNumberPage(), request.getSizePage(), nameMask, phoneMask, emailMask, request.getMainClientId(), request.getOnlyMainClient(), request.getClientId(), request.getCourierActivated());
        return response;
    }



    /*
    Criteria criteria = session.createCriteria(Emp.class)
    .setProjection( Projections.projectionList()
        .add( Projections.property("firstName") )
        .add( Projections.property("empId") ) );
Criterion criterion= Restrictions.and(Restrictions.eq("empId", 10), Restrictions.eq("empName", "bhanu"));
criteria.add(Restrictions.or(criterion, Restrictions.eq("salary", 25000)));
List result=criteria.list();
     */



    @RequestMapping(value = "/driver/list", method = RequestMethod.POST)
    public
    @ResponseBody
    DriversListResponse findDriverListByMask(@RequestBody DriversListRequest request) {
          return administrationService.getDriverInfoListByMask(request.getSecurity_token(), request.getNumberPage(), request.getSizePage(), request.getNameMask(), request.getPhoneMask(), request.getCarModelMask(), request.getCarNumberMask(), request.getAssistantId(), request.getVersionApp(), request.getLogin(), request.getAutoClass(), request.getBlockStaus(),
                  request.getTypeSalary(), request.getSalaryPriority(), request.getDriverId(), request.isTypeX(), request.isActive(), request.getCourier(), request.getPedestrian(), request.getDriverTypes()); //
    }



    //f:add
    @RequestMapping(value = "/taxopark/list", method = RequestMethod.POST)
    public
    @ResponseBody
    TaxoparkPartnersResponse findTaxoparkPartnersList(@RequestBody TaxoparkPartnersRequest request) {
        return administrationService.findTaxoparkPartnersList(request.getSecurity_token());
    }



    @RequestMapping(value = "/updateArticleAdjustments", method = RequestMethod.POST)
    public
    @ResponseBody
    UpdateArticleAdjustmentsResponse updateArticleAdjustments(@RequestBody UpdateArticleAdjustmentsRequest request){
        return administrationService.updateArticleAdjustments(request.getSecurity_token(), request.getArticleAdjustmentsInfo());
    }




    @RequestMapping(value = "/articleAdjustments", method = RequestMethod.POST)
    public
    @ResponseBody
    ArticleAdjustmentsResponse articleAdjustments(@RequestBody ArticleAdjustmentsRequest request){
        return administrationService.articleAdjustments(request.getSecurity_token(), request.getArticleAdjustmentsId());
    }



    // что нового в версии
    @RequestMapping(value = "/newsInVersionApp/update", method = RequestMethod.POST)
    public
    @ResponseBody
    UpdateNewsInVersionResponse updateNewsInVersion(@RequestBody UpdateNewsInVersionRequest request) {
          return administrationService.updateNewsInVersion(request.getSecurity_token(), request.getNewsVersionAppInfo());
    }


    @RequestMapping(value = "/versionApp/list", method = RequestMethod.POST)
    public
    @ResponseBody
    ListVersionAppResponse versionAppList(@RequestBody ListVersionAppRequest request) {
        return administrationService.versionAppList(request.getSecurity_token(), request.getDeviceType());
    }

    @RequestMapping(value = "/listNewsInVersion", method = RequestMethod.POST)
    public
    @ResponseBody
    ListNewsInVersionResponse listNewsInVersion(@RequestBody ListNewsInVersionRequest request) {
        return administrationService.listNewsInVersion(request.getSecurity_token());
    }

    //f:add
    @RequestMapping(value = "/taxopark/update", method = RequestMethod.POST)
    public
    @ResponseBody
    TaxoparkPartnersUpdateResponse updateTaxoparkPartner(@RequestBody TaxoparkPartnersUpdateRequest request) {
          return administrationService.updateTaxoparkPartner(request.getTaxoparkPartnersInfo(), request.getSecurity_token());
    }


    @RequestMapping(value = "/taxopark/find", method = RequestMethod.POST)
    public
    @ResponseBody
    TaxoparkPartnersFindResponse findTaxoparkPartner(@RequestBody TaxoparkPartnersFindRequest request) {
          return administrationService.findTaxoparkPartner(request.getTaxoparkId(), request.getSecurity_token());
    }


    @RequestMapping(value = "/taxopark/delete", method = RequestMethod.POST)
    public
    @ResponseBody
    TaxoparkPartnersDeleteResponse deleteTaxoparkPartner(@RequestBody TaxoparkPartnersDeleteRequest request) {
          return administrationService.deleteTaxoparkPartner(request.getTaxoparkId(), request.getSecurity_token());
    }


    @RequestMapping(value = "/assistant/list", method = RequestMethod.POST)
    public
    @ResponseBody
    AssistantResponse assistantList(@RequestBody AssistantRequest request) {
         return administrationService.assistantList(request.getSecurity_token());
    }



    @RequestMapping(value = "/assistant/update", method = RequestMethod.POST)
    public
    @ResponseBody
    AssistantUpdateResponse updateAssistant(@RequestBody AssistantUpdateRequest request) {
          return administrationService.updateAssistant(request.getAssistantInfo());
    }


    @RequestMapping(value = "/assistant/find", method = RequestMethod.POST)
    public
    @ResponseBody
    AssistantFindResponse findAssistant(@RequestBody AssistantFindRequest request) {
         return administrationService.findAssistant(request.getAssistantId(), request.getSecurity_token());
    }


    @RequestMapping(value = "/assistant/delete", method = RequestMethod.POST)
    public
    @ResponseBody
    AssistantDeleteResponse deleteAssistant(@RequestBody AssistantDeleteRequest request) {
          return administrationService.deleteAssistant(request.getAssistantId());
    }



    @RequestMapping(value = "/client/availableCountActivatePromo", method = RequestMethod.POST)
    public
    @ResponseBody
    ClientAvailableActivatePromoCodeResponse getAvailableCountActivatedPromoCode(@RequestBody ClientAvailableActivatePromoCodeRequest request) {
        ClientAvailableActivatePromoCodeResponse response =  administrationService.getAvailableCountActivatedPromoCode(request.getClientId());
          return response;
    }



    // обновление допустимого кол-ва активаций промокодов для клиента
    @RequestMapping(value = "/client/availableCountActivatePromoUpd", method = RequestMethod.POST)
    public
    @ResponseBody
    ClientAvailableActivatePromoCodeUpdateResponse getAvailableCountActivatedPromoCodeUpd(@RequestBody ClientAvailableActivatePromoCodeUpdateRequest request) {
           return administrationService.availableCountActivatedPromoCodeUpd(request.getClientId(), request.getCount());
    }




    //f:add сколько данному клиенту дозволено рассылать промокодов в сутки
    @RequestMapping(value = "/client/availableCountPromoSend", method = RequestMethod.POST)
    public
    @ResponseBody
    ClientCountPromoCodeResponse getClientAvailableCountPromoSend(@RequestBody ClientCountPromoCodeRequest request) {
        ClientCountPromoCodeResponse response =  administrationService.getClientAvailableCountPromoSend(request.getClientId());
          return response;
    }

    // обновление допустимого кол-ва рассылки промокодов
    @RequestMapping(value = "/client/availableCountPromoSendUpd", method = RequestMethod.POST)
    public
    @ResponseBody
    ClientCountPromoCodeUpdateResponse getAvailableCountPromoSendUpd(@RequestBody ClientCountPromoCodeUpdateRequest request) {
        ClientCountPromoCodeUpdateResponse response =  administrationService.availableCountPromoSendUpd(request.getClientId(), request.getCount());
          return response;
    }


    @RequestMapping(value = "/client/availableSumPromoSend", method = RequestMethod.POST)
    public
    @ResponseBody
    ClientSumPromoCodeResponse getClientAvailableSumPromoSend(@RequestBody ClientSumPromoCodeRequest request) {
        ClientSumPromoCodeResponse response =  administrationService.getClientAvailableSumPromoSend(request.getClientId());
          return response;
    }


    @RequestMapping(value = "/client/availableSumPromoSendUpd", method = RequestMethod.POST)
    public
    @ResponseBody
    ClientSumPromoCodeUpdateResponse getClientAvailableSumPromoSendUpd(@RequestBody ClientSumPromoCodeUpdateRequest request) {
        ClientSumPromoCodeUpdateResponse response =  administrationService.clientAvailableSumPromoSendUpd(request.getClientSumPromoId(), request.getSum());
          return response;
    }


    @RequestMapping(value = "/client/availableSumPromoInsert", method = RequestMethod.POST)
    public
    @ResponseBody
    ClientSumPromoCodeInsertResponse getClientAvailableSumPromoSendUpd(@RequestBody ClientSumPromoCodeInsertRequest request) {
        ClientSumPromoCodeInsertResponse response =  administrationService.availableSumPromoInsert(request.getClientId(), request.getSum());
          return response;
    }



    @RequestMapping(value = "/client/availableSumPromoSendDelete", method = RequestMethod.POST)
    public
    @ResponseBody
    ClientSumPromoCodeDeleteResponse clientAvailableSumPromoSendDelete(@RequestBody ClientSumPromoCodeDeleteRequest request) {
        ClientSumPromoCodeDeleteResponse response =  administrationService.clientAvailableSumPromoSendDelete(request.getSumId());
          return response;
    }


//                                Tablet previousTabet = usedTablet.getTablet();
//                                if(!previousTabet.getTabletState().equals(TabletState.BROKEN)){
//                                    previousTabet.setTabletState(TabletState.FREE);
//                                }
//                                if(previousTabet.getTabletState().equals(TabletState.BROKEN_IN_USE)){
//                                    previousTabet.setTabletState(TabletState.BROKEN);
//                                }
//                                tabletRepository.save(previousTabet);



    //                            if(tablet.getTabletState().equals(TabletState.IN_USE)){
//                                throw new CustomException(8, "Данный планшет уже используется");
//                            }
//                            if(tablet.getTabletState().equals(TabletState.BROKEN)){
//                                throw new CustomException(9, "Планшет сломан");
//                            }
//                            if(tablet.getTabletState().equals(TabletState.BROKEN_IN_USE)){
//                                throw new CustomException(10, "Планшет сломан и используется");
//                            }
    //tablet.setTimeOfUpdate(DateTimeUtils.nowNovosib_GMT6());
    //tablet.setTabletState(TabletState.IN_USE);



    /*
    TabletsUsed previousUsedTablet = tabletsUsedRepository.findByDriverAndEndUsedIsNull(driver);
    if(previousUsedTablet != null){
        previousUsedTablet.setEndUsed(DateTimeUtils.nowNovosib_GMT6());
        Tablet previousTabet = previousUsedTablet.getTablet();
        if(!previousTabet.getTabletState().equals(TabletState.BROKEN)){
            previousTabet.setTabletState(TabletState.FREE);
        }
        if(previousTabet.getTabletState().equals(TabletState.BROKEN_IN_USE)){
            previousTabet.setTabletState(TabletState.BROKEN);
        }
        tabletRepository.save(previousTabet);
        tabletsUsedRepository.save(previousUsedTablet);
    }
    */


    /* todo: НАСТРОЙЕИ ВОДИТЕЛЯ ЗДЕСЬ НЕ ОБНОВЛЯЮТСЯ, ДЛЯ ЭТОГО ЕСТЬ ОТДЕЛЬНЫЙ МЕТОД!!! */
    @RequestMapping(value = "/driver/update/arm", method = RequestMethod.POST)
    @Transactional
    public
    @ResponseBody
    DriverChangeARMResponse updateDriverARM(@RequestBody DriverChangeARMRequest request) {
            DriverChangeARMResponse response = new DriverChangeARMResponse();
            DriverInfoARM driverInfoARM = request.getDriverInfoARM();
            if (driverInfoARM != null) {
                Driver driver = driverRepository.findOne(driverInfoARM.getId());
                if (driver != null) {
                    driver = ModelsUtils.fromModelUpdateDriver(driverInfoARM, driver);

                    if (driverInfoARM.getTaxoparkPartnersInfo() != null && driverInfoARM.getTaxoparkPartnersInfo().getId() != null) {
                        TaxoparkPartners taxoparkPartners = taxoparkPartnersRepository.findOne(driverInfoARM.getTaxoparkPartnersInfo().getId());
                        if (taxoparkPartners != null) {
                            driver.setTaxoparkPartners(taxoparkPartners);
                        }
                    }
                    if (driverInfoARM.getAssistantInfo() != null && driverInfoARM.getAssistantInfo().getId() != null) {
                        Assistant assistant = assistantRepository.findOne(driverInfoARM.getAssistantInfo().getId());
                        if (assistant != null) {
                            driver.setAssistant(assistant);
                        }
                    }


                    /* привязываем планшет и роутер*/
                    if(driverInfoARM.getTabletInfo()!=null){
                         administrationService.linkTabletToDriver(driver.getId(), driverInfoARM.getTabletInfo().getId());
                    }
                    if(driverInfoARM.getRouterInfo()!=null){
                        administrationService.linkRouterToDriver(driver.getId(), driverInfoARM.getRouterInfo().getId());
                    }
                    /* */



                    /*
                    if(driverInfoARM.getTabletInfo()!=null){
                        Tablet tablet = tabletRepository.findOne(driverInfoARM.getTabletInfo().getId());
                        driver.setTablet(tablet);

                        TabletsUsed tabletsUsed = new TabletsUsed();
                        tabletsUsed.setDriver(driver);
                        tabletsUsed.setTablet(tablet);
                        tabletsUsed.setStartUsed(DateTimeUtils.nowNovosib_GMT6());
                        tabletsUsedRepository.save(tabletsUsed);
                    }
                    */





                    String picture = driverInfoARM.getPhotoPicture();
                    if (picture!=null && !StringUtils.isEmpty(picture)) {
                        String pictureUrl = profilesResourcesService.saveDriverPicture(picture, driver.getId(), false);
                        //request.getDriverInfoARM().setPhotoPicture("");
                        driver.setPhotoUrl(pictureUrl);
                        driverRepository.save(driver);
                    }
                    String pictureByVersion = driverInfoARM.getPhotoUrlByVersion();
                    if (!StringUtils.isEmpty(pictureByVersion)) {
                        String pictureUrlByVersion = profilesResourcesService.saveDriverPicture(pictureByVersion, driver.getId(), true);
                        driver.setPhotoUrlByVersion(pictureUrlByVersion);
                        driverInfoARM.setPhotoUrlByVersion(pictureUrlByVersion);
                        driverRepository.save(driver);
                    }

                    List<String> photosCarsPictures = driverInfoARM.getPhotosCarsPictures();

                    if (photosCarsPictures!=null && !photosCarsPictures.isEmpty()) {
                        List<String> pictureUrls = profilesResourcesService.saveAutoPictures(photosCarsPictures, driver.getId());
                        driver.getPhotosCarsUrl().clear();
                        driverRepository.save(driver);
                        driver.getPhotosCarsUrl().addAll(pictureUrls);
                        driverRepository.save(driver);
                    }


                    /* функционал для загрузки фоток авто в отдельную таблицу для новых версий клиентского ПО*/
                    List<DriverCarPhotosInfo> driverCarPhotosInfos = driverInfoARM.getDriverCarPhotosInfos();
                    driverCarPhotosInfos = profilesResourcesService.saveAutoPicturesByVersionApp(driverCarPhotosInfos, driver.getId());
                    /* сохранение фото авто в отедблную таблицу */
                    if(!CollectionUtils.isEmpty(driverCarPhotosInfos)){
                        List<DriverCarPhotos> driverCarPhotosSave = new ArrayList<>();
                        for (DriverCarPhotosInfo carInfo : driverCarPhotosInfos) {
                            if(carInfo.getId()==0){
                                driverCarPhotosSave.add(ModelsUtils.fromModel(carInfo, driver));
                            }else{
                                DriverCarPhotos driverCarPhotos = driverCarPhotosRepository.findOne(carInfo.getId());
                                if(driverCarPhotos == null){
                                    throw new CustomException(9, String.format("Фотография авто id=%s не найдена", carInfo.getId()));
                                }
                                driverCarPhotosSave.add(ModelsUtils.fromModelUpdate(carInfo.getPhotoUrl(), null, null, driverCarPhotos));
                            }
                        }
                        if(!CollectionUtils.isEmpty(driverCarPhotosSave)) {
                            driverCarPhotosRepository.save(driverCarPhotosSave);
                        }
                    }
                    /* end */

                    driverRepository.save(driver);
                } else {
                    throw new CustomException(1, "Водитель не найден");
                }
            }
            response.setDriverInfoARM(driverInfoARM);
               return response;
    }
    /*
                    for (DriverCarPhotosInfo carInfo : driverCarPhotosInfos) {
                        if(carInfo.getAndroidMinVersion() != null){
                            androidVersion = administrationService.versionApp(carInfo.getAndroidMinVersion().getId());
                            if (androidVersion == null) {
                                throw new CustomException(8, "Версия ПО не найдена");
                            }
                        }
                        if(carInfo.getAppleMinVersion() != null){
                            appleVersion = administrationService.versionApp(carInfo.getAppleMinVersion().getId());
                            if (appleVersion == null) {
                                throw new CustomException(8, "Версия ПО не найдена");
                            }
                        }
                    }
    */



    @RequestMapping(value = "/driver/find/arm", method = RequestMethod.POST)
    public
    @ResponseBody
    DriverFindARMResponse findDriverArm(@RequestBody DriverFindARMRequest request) {
         return administrationService.findDriverArm(request.getDriverId(), request.getSecurity_token());
    }




    @RequestMapping(value = "/driver/find", method = RequestMethod.POST)
    public
    @ResponseBody
    AdministrativeDriverInfoResponse findDriver(@RequestBody DriverInfoRequest request) {
        AdministrativeDriverInfoResponse response = new AdministrativeDriverInfoResponse();
        long driverId = request.getDriverId();
        Driver driver = administrationService.getDriverInfo(request.getDriverId());
        if (driver != null) {
            DriverInfo driverInfo = ModelsUtils.toModel(driver);
            driverInfo.setRate(billingService.getDriverRate(driverId));
              response.setDriverInfo(driverInfo);
              ServerStateInfo stateInfo = administrationService.resolveState(0, driverId);
              response.setServerState(stateInfo.getState());
        }
        return response;
    }



    @RequestMapping(value = "/driver/find/test", method = RequestMethod.POST)
    public
    @ResponseBody
    DriverInfoARMResponse findDriverARM(@RequestBody DriverInfoRequest request) {
        DriverInfoARMResponse response = new DriverInfoARMResponse();
        Driver driver = administrationService.getDriverInfo(request.getDriverId());
        if (driver != null) {
            DriverInfoARM driverInfoARM = ModelsUtils.toModelARM(driver);
            response.setDriverInfoARM(driverInfoARM);
        }
        return response;
    }





    @RequestMapping(value = "/driver/findall", method = RequestMethod.POST)
    public
    @ResponseBody
    FindDriversResponse findDriverAll(@RequestBody FindDriversRequest request) {
        FindDriversResponse response = new FindDriversResponse();
        response.getDrivers().addAll(administrationService.findDrivers(request.getIds()));
        return response;
    }




/*
    @RequestMapping(value = "/driver/create", method = RequestMethod.POST)
    public
    @ResponseBody
    DriverUpdateResponse createDriver(@RequestBody DriverUpdateRequest request) {
        DriverUpdateResponse response = new DriverUpdateResponse();
        Driver driver = administrationService.registerDriver(request.getDriverInfo(), null);
        if (driver != null) {
            DriverInfo driverInfo = ModelsUtils.toModel(driver);
            driverInfo.setRate(billingService.getDriverRate(driver.getId()));
            response.setDriverInfo(driverInfo);
            ServerStateInfo stateInfo = administrationService.resolveState(0, driver.getId());
//            response.setServerState(stateInfo.getState());
        }
        return response;
    }
*/


    @RequestMapping(value = "/updateWiFi", method = RequestMethod.POST)
    public
    @ResponseBody
    UpdateWiFiResponse updateWiFi(@RequestBody UpdateWiFiRequest request) {
        UpdateWiFiResponse response = administrationService.updateWiFi(request.getDriverId(), request.isStatus());
           return response;
    }


    @RequestMapping(value = "/statusWiFi", method = RequestMethod.POST)
    public
    @ResponseBody
    WiFiResponse statusWiFi(@RequestBody WiFiRequest request) {
        return administrationService.statusWiFi(request.getDriverId());
    }




    @RequestMapping(value = "/client/find", method = RequestMethod.POST)
    public
    @ResponseBody
    ClientInfoResponse findClient(@RequestBody ClientInfoRequest request) {
        ClientInfoResponse response = new ClientInfoResponse();
        long clientId = request.getClientId();
        Client client = administrationService.getClientInfo(clientId);
        if (client != null) {
            ClientInfo clientInfo = ModelsUtils.toModel(client);
            response.setClientInfo(clientInfo);
        }
        return response;
    }




    @RequestMapping(value = "/client/block", method = RequestMethod.POST)
    public
    @ResponseBody
    BlockClientResponse blockClient(@RequestBody BlockClientRequest request) {
        return administrationService.blockClient(request.getClientId(), request.getWebUsertId(), request.isBlock(), request.getComment(), request.getSecurity_token());
    }



    @RequestMapping(value = "/getPropertyValue", method = RequestMethod.POST)
    public
    @ResponseBody
    PropertyResponse getPropertyValue(@RequestBody PropertyRequest request) {
        PropertyResponse response = new PropertyResponse();
        String result = commonService.getPropertyValue(request.getPropName());
        response.setPropValue(result);
           return response;
    }




    // token - нет и возможно не будет
    @RequestMapping(value = "/setPropertyValue", method = RequestMethod.POST)
    public
    @ResponseBody
    PropertyUpdateResponse setPropertyValue(@RequestBody PropertyUpdateRequest request) {
        PropertyUpdateResponse response = administrationService.setPropertyValue(request.getPropName(), request.getPropValue());
        return response;
    }




    @RequestMapping(value = "/setPropertyValue/arm", method = RequestMethod.POST)
    public
    @ResponseBody
    PropertyUpdateResponse setPropertyValueARM(@RequestBody PropertyUpdateARMRequest request) {
        PropertyUpdateResponse response = administrationService.setPropertyValueARM(request.getSecurity_token(), request.getPropName(), request.getPropValue());
           return response;
    }




    @RequestMapping(value = "/clientByPromo", method = RequestMethod.POST)
    public
    @ResponseBody
    ClientByPromoResponse clientByPromo(@RequestBody ClientByPromoRequest request) {
            return administrationService.clientByPromo(request.getSecurity_token() , request.getPromoCode());
    }



    @RequestMapping(value = "/sendEmail", method = RequestMethod.POST)
    public
    @ResponseBody
    SendEmailResponse sendEmail(@RequestBody SendEmailRequest request) {
        SendEmailResponse response = administrationService.sendEmail(request.getEmail(), request.getHtmlText(), request.getSubject(), request.getSecurity_token());
        return response;
    }



//    @RequestMapping(value = "/sendEmailCollection", method = RequestMethod.POST)
//    public
//    @ResponseBody
//    SendEmailCollectionResponse sendEmailCollection(@RequestBody SendEmailCollectionRequest request) {
//        SendEmailCollectionResponse response = administrationService.sendEmailCollection(request.getListEmail(), request.getHtmlText(), request.getSubject());
//           return response;
//    }



    // add/upd events partners
    @RequestMapping(value = "/update/eventsPartners", method = RequestMethod.POST)
    public
    @ResponseBody
    UpdateEventPartnerResponse updateEventPartners(@RequestBody UpdateEventPartnerRequest request) {
        UpdateEventPartnerResponse response = new UpdateEventPartnerResponse();
         administrationService.updateEventPartners(request.getSecurity_token(), request.getEventPartnerInfos());
            return response;
    }



    // get list events partners
    @RequestMapping(value = "/eventsPartners/arm", method = RequestMethod.POST)
    public
    @ResponseBody
     EventPartnerARMResponse eventsPartnersARM(@RequestBody EventPartnerARMRequest request) {
        return administrationService.eventsPartnersARM(request.getSecurity_token(), request.getDateStart(), request.getDateEnd(), request.getEventId(), request.getPhoneMask(), request.getNumberPage(), request.getSizePage());
    }


    @RequestMapping(value = "/region", method = RequestMethod.POST)
    public
    @ResponseBody
    RegionResponse getRegionList(@RequestBody RegionRequest request) throws FileNotFoundException, UnsupportedEncodingException {
        return administrationService.regionList(request.getSecurity_token(), request.getRegionId(), request.getCoast());
    }



    @RequestMapping(value = "/updateRegion", method = RequestMethod.POST)
    public
    @ResponseBody
    UpdateRegionResponse updateRegion(@RequestBody UpdateRegionRequest request) throws FileNotFoundException, UnsupportedEncodingException {
        return administrationService.updateRegion(request.getSecurity_token(), request.getRegionInfos());
    }


    @RequestMapping(value = "/checkInsidePolygon", method = RequestMethod.POST)
    public
    @ResponseBody
    CheckPointInsidePolygonResponse checkInsidePolygon(@RequestBody CheckPointInsidePolygonRequest request) {
        return administrationService.checkInsidePolygon(request.getSecurity_token(), request.getLatitude(), request.getLongitude());
    }


    @RequestMapping(value = "/update/estimate", method = RequestMethod.POST)
    public
    @ResponseBody
    UpdateEstimateResponse updateEstimate(@RequestBody UpdateEstimateRequest request) {
        UpdateEstimateResponse response = new UpdateEstimateResponse();
          administrationService.updateEstimate(request.getSecurity_token(), request.getEstimateInfoARMList());
            return response;
    }



    // механизм "запретных периодов для отдыха" водителей (обновление, редактирование)
    @RequestMapping(value = "/update/banPeriodRestDriver", method = RequestMethod.POST)
    public
    @ResponseBody
    UpdateBanPeriodRestDriverResponse updateBanPeriodRestDriver(@RequestBody UpdateBanPeriodRestDriverRequest request, HttpServletRequest reqServlet) {
        UpdateBanPeriodRestDriverResponse response = new UpdateBanPeriodRestDriverResponse();
        administrationService.updateBanPeriodRestDriver(request.getSecurity_token(), request.getBanPeriodRestDriverInfoList(), HTTPUtil.resolveIpAddress(reqServlet));
            return response;
    }



    @RequestMapping(value = "/banPeriodRestDriver", method = RequestMethod.POST)
    public
    @ResponseBody
    BanPeriodRestDriverResponse banPeriodRestDriver(@RequestBody BanPeriodRestDriverRequest request) {
         return administrationService.banPeriodRestDriver(request.getSecurity_token(), request.getBanPeriodId());
    }


    @RequestMapping(value = "/promoExclusive", method = RequestMethod.POST)
    public
    @ResponseBody
    PromoCodeExclusiveResponse getPromoCodeExclusive(@RequestBody PromoCodeExclusiveRequest request) {
        return administrationService.getPromoCodeExclusive(request.getSecurity_token(), request.getClientId());
    }



    @RequestMapping(value = "/updateDriverPeriodWork", method = RequestMethod.POST)
    public
    @ResponseBody
    UpdateDriverPeriodWorkResponse updateDriverPeriodWork(@RequestBody UpdateDriverPeriodWorkRequest request) {
        UpdateDriverPeriodWorkResponse response = new UpdateDriverPeriodWorkResponse();
         administrationService.updateDriverPeriodWork(request.getSecurity_token(), request.getDriverPeriodWorkInfos());
           return response;
    }


    @RequestMapping(value = "/driverPeriodWork", method = RequestMethod.POST)
    public
    @ResponseBody
    DriverPeriodWorkResponse driverPeriodWork(@RequestBody DriverPeriodWorkRequest request) {
        return administrationService.driverPeriodWork(request.getSecurity_token(), request.getDriverId(), request.getStartTime(), request.getEndTime(), request.getActive(), request.getDriverPeriodWorkId());
    }


    // оценки и комментарии к заказам пользователя - по конкретному пользователю
    @RequestMapping(value = "/estimateInfoByClient", method = RequestMethod.POST)
    public
    @ResponseBody
    EstimateInfoByClientResponse estimateInfo(@RequestBody EstimateInfoByClientRequest request) {
        return administrationService.estimateInfoByClient(request.getClientId());
    }



    // оценки и комментарии к заказам пользователя - ВСЕ
    @RequestMapping(value = "/estimateInfoAll", method = RequestMethod.POST)
    public
    @ResponseBody
    EstimateInfoResponse estimateInfoList(@RequestBody EstimateInfoRequest request) {
            return reportService.estimateInfoAll(request.getNumberPage(), request.getSizePage(), request.getQueryDetailsList(), request.getSecurity_token());
    }




    // оценки и комментарии к заказам пользователя
    @RequestMapping(value = "/estimateInfoByMission", method = RequestMethod.POST)
    public
    @ResponseBody
    EstimateInfoByMissionResponse estimateInfoByMission(@RequestBody EstimateInfoByMissionRequest request) {
        return administrationService.estimateInfoByMission(request.getMissionId());
    }



    // f: add
    @RequestMapping(value = "/driver/adminstatus", method = RequestMethod.POST)
    public
    @ResponseBody
    DriverAdministrationStatusResponse adminStatusDriver(@RequestBody DriverAdministrationStatusRequest request) {
        DriverAdministrationStatusResponse response = new DriverAdministrationStatusResponse();
        DriverLocksInfo driverLocksInfo = administrationService.getDriverAdministrativeStatus(request.getDriverId(), request.getSecurity_token());
           response.setDriverLocksInfo(driverLocksInfo);
             return response;
    }



    // f: add
    @RequestMapping(value = "/client/adminstatus", method = RequestMethod.POST)
    public
    @ResponseBody
    ClientAdministrationStatusResponse adminStatusClient(@RequestBody ClientAdministrationStatusRequest request) {
           return administrationService.getClientAdministrativeStatus(request.getClientId());
    }



    // f: обналичивание денег водителя
    @RequestMapping(value = "/moneyWithdrawal", method = RequestMethod.POST)
    public
    @ResponseBody
    MoneyWithdrawalResponse moneyWithdrawal(@RequestBody MoneyWithdrawalRequest request) {
        return administrationService.moneyWithdrawal(request.getDriverId(), request.getSmsCode(), request.getCountSymbols());
    }




    // f: add
    @RequestMapping(value = "/driver/updadminstatus", method = RequestMethod.POST)
    public
    @ResponseBody
    DriverAdministrationStatusResponse updateAdminStatusDriver(@RequestBody DriverAdministrationStatusRequest request) {
        DriverAdministrationStatusResponse response = new DriverAdministrationStatusResponse();
        boolean update = administrationService.setAdministrationStatusDriver(request.getDriverId(),request.getAdminStatus(),request.getReason());
          response.setUpdate(update);
           return response;
    }




/*
    было

    @RequestMapping(value = "/client/lastmissions", method = RequestMethod.POST)
    public
    @ResponseBody
    LastMissionsResponse lastMissionsClient(@RequestBody LastMissionsRequest request) {
        LastMissionsResponse response = new LastMissionsResponse();
        List<LastMissionsInfo> missions = missionService.findLastMissionsClient(request.getUserId());
        response.getMissionsInfos().addAll(missions);
        return response;
    }

*/


/*
    было

    @RequestMapping(value = "/driver/lastmissions", method = RequestMethod.POST)
    public
    @ResponseBody
    LastMissionsResponse lastMissionsDriver(@RequestBody LastMissionsRequest request) {
        LastMissionsResponse response = new LastMissionsResponse();
        List<LastMissionsInfo> missions = missionService.findLastMissionsDriver(request.getUserId());
        response.getMissionsInfos().addAll(missions);
        return response;
    }
  */





    @RequestMapping(value = "/driver/lastmissions", method = RequestMethod.POST)
    public
    @ResponseBody
    TripsHistoryResponse findTripsHistory(@RequestBody TripsHistoryRequest request) {
        TripsHistoryResponse response = new TripsHistoryResponse();
        AdministrationService.HistoryMissions history = administrationService.onlyHistoryMissionsDriver(request.getRequesterId());
        response.setHistory(history.history);
        return response;
    }



    @RequestMapping(value = "/driver/lastmissions/arm", method = RequestMethod.POST)
    public
    @ResponseBody
    TripsHistoryResponse findTripsHistoryARM(@RequestBody TripsHistoryRequest request) {
        TripsHistoryResponse response = new TripsHistoryResponse();
        AdministrationService.HistoryMissions history = administrationService.onlyHistoryMissionsDriverARM(request.getRequesterId());
        response.setHistory(history.history);
        return response;
    }




    @RequestMapping(value = "/client/lastmissions", method = RequestMethod.POST)
    public
    @ResponseBody
    TripsHistoryResponse findTripsHistoryClient(@RequestBody TripsHistoryRequest request) {
        TripsHistoryResponse response = new TripsHistoryResponse();
        AdministrationService.HistoryMissions history = administrationService.onlyHistoryMissionsClient(request.getRequesterId());
        //LOGGER.info("history.history = "+history.history);
        response.setHistory(history.history);
        return response;
    }




    @RequestMapping(value = "/client/lastmissions/arm", method = RequestMethod.POST)
    public
    @ResponseBody
    TripsHistoryResponse findTripsHistoryClientARM(@RequestBody TripsHistoryRequest request) {
        TripsHistoryResponse response = new TripsHistoryResponse();
        AdministrationService.HistoryMissions history = administrationService.onlyHistoryMissionsClientARM(request.getRequesterId());
        //LOGGER.info("history.history = "+history.history);
        response.setHistory(history.history);
        return response;
    }




//        @RequestMapping(value = "/mission/history", method = RequestMethod.POST)
//    public
//    @ResponseBody
//    TripsHistoryResponse findTripsHistory(@RequestBody TripsHistoryRequest request) {
//        TripsHistoryResponse response = new TripsHistoryResponse();
//        AdministrationService.HistoryMissions history = administrationService.missionsHistoryDriver(request.getRequesterId());
//        //response.getHistory().addAll(history.history); f: //
//        //response.getBooked().addAll(history.booked);   f: //
//        response.setHistory(history.history);
//        response.setBooked(history.booked); не надо
//        return response;
//    }





    @RequestMapping(value = "/driver/around", method = RequestMethod.POST)
    public
    @ResponseBody
    DriversAroundResponse findDrivers(@RequestBody DriversAroundRequest request) {
        DriversAroundResponse response = new DriversAroundResponse();
        List<ItemLocation> locations = administrationService.findDriversAround(request.getCurrentLocation(), request.getRadius());
        response.getLocations().addAll(locations);
        return response;
    }



    @RequestMapping(value = "/driver/activeincity", method = RequestMethod.POST)
    public
    @ResponseBody
    FindActiveDriversResponse findDrivers(@RequestBody FindActiveDriversRequest request) {
        FindActiveDriversResponse response = new FindActiveDriversResponse();
        List<ItemLocation> locations = administrationService.findDriversActive(request.getCity(), request.isShowOccupied());
        response.getLocations().addAll(locations);
        return response;
    }



    @RequestMapping(value = "/mission/current", method = RequestMethod.POST)
    public
    @ResponseBody
    MissionInfoResponse currentMission(@RequestBody CurrentMissionInfoRequest request) {
        MissionInfoResponse response = new MissionInfoResponse();
        response.setMissionInfo(missionService.find(request.getId()));
        return response;
    }




    // запрос на детальную информацию о броне по ее id
    @RequestMapping(value = "/mission/bookingmission", method = RequestMethod.POST)
    public
    @ResponseBody
    BookingMissionInfoResponse findBookingMission(@RequestBody BookingMissionInfoRequest request) {
        BookingMissionInfoResponse response = new BookingMissionInfoResponse();

        List result = missionService.findBookingMission(request.getMissionId());

           if(!result.isEmpty()){
               MissionInfo missionInfo = (MissionInfo)result.get(0);
               DriverInfo driverInfo = (DriverInfo)result.get(1);

               response.setMissionInfo(missionInfo);
               response.setDriverInfo(driverInfo);
           }


        return response;
    }



    @RequestMapping(value = "/driver/currentstate", method = RequestMethod.POST)
    public
    @ResponseBody
    DriverCurrentStateResponse currentStateDriver(@RequestBody DriverCurrentStateRequest request) {
        DriverCurrentStateResponse response = new DriverCurrentStateResponse();
        String driverStatus = administrationService.findCurrentStateDriver(request.getDriverId(), request.getSecurity_token());
        response.setDriverState(driverStatus);
          return response;
    }



    @RequestMapping(value = "/mission/client", method = RequestMethod.POST)
    public
    @ResponseBody
    MissionInfoResponse currentMissionForClient(@RequestBody CurrentMissionInfoRequest request) {
        MissionInfoResponse response = new MissionInfoResponse();
        response.setMissionInfo(missionService.findByClient(request.getId()));
        return response;
    }



    @RequestMapping(value = "/mission/driver", method = RequestMethod.POST)
    public
    @ResponseBody
    MissionInfoResponse currentMissionForDriver(@RequestBody CurrentMissionInfoRequest request) {
        MissionInfoResponse response = new MissionInfoResponse();
        response.setMissionInfo(missionService.findByDriver(request.getId()));
        return response;
    }




    /*  CORPORATE CLIENT   */



  @RequestMapping(value = "/blockCorporateClient", method = RequestMethod.POST)
  public
  @ResponseBody
  BlockCorporateClientResponse blockClient(@RequestBody BlockCorporateClientRequest request) {
      WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
        if (webUser == null) {
          throw new CustomException(1, "Web user not found");
        }
        Client blockClient = clientRepository.findOne(request.getClientId());
        if(blockClient == null){
            throw new CustomException(2, "Client not found");
        }
        if(blockClient.getMainClient() == null) {
            throw new CustomException(4, "Клиент не является корпоративным");
        }
        return clientService.blockClient(blockClient, request.getReason());
  }



   @RequestMapping(value = "/corporateClients", method = RequestMethod.POST)
   public
   @ResponseBody
   CorporateClientARMResponse corporateProfile(@RequestBody CorporateClientARMRequest request) {
      WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
       if (webUser == null) {
          throw new CustomException(1, "Web user not found");
       }
      return administrationService.corporateClient(request.getMainClientId());
   }




    @RequestMapping(value = "/updateCorporateClientLimit", method = RequestMethod.POST)
    public
    @ResponseBody
    UpdateCorporateClientLimitResponse updateCorporateClientLimit(@RequestBody UpdateCorporateClientLimitRequest request) {
        return administrationService.updateCorporateClientLimit(request.getSecurity_token(), request.getLimitInfo());
    }



    /* add/upd user from ARM */
    @RequestMapping(value = "/updateClient", method = RequestMethod.POST)
    public
    @ResponseBody
    UpdateClientARMResponse updateClient(@RequestBody UpdateClientARMRequest request) throws Exception {
        WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
         if(webUser == null){
             throw new CustomException(1, "Web user not found");
         }
         if (!EnumSet.of(AdministratorRole.ADMIN).contains(webUser.getRole())) {
             throw new CustomException(2,"Permission denied");
         }
         return administrationService.updateClient(request.getClientInfoARMs());
    }



    @RequestMapping(value = "/corporateClient/updateBalance", method = RequestMethod.POST)
    public
    @ResponseBody
    UpdateCorporateClientBalanceResponse updateCorporateClientBalance(@RequestBody UpdateCorporateClientBalanceRequest request, HttpServletRequest reqServlet){
        return administrationService.updateCorporateClientBalance(request.getMainClientId(), request.getAmountOfMoney(), request.getOperation(), request.getSecurity_token(), request.getComment(), request.getArticleId(), HTTPUtil.resolveIpAddress(reqServlet));
    }



    @RequestMapping(value = "/corporateClientCashFlow", method = RequestMethod.POST)
    public
    @ResponseBody
    CorporateClientCashFlowResponse corporateClientCashFlow(@RequestBody CorporateClientCashFlowRequest request) {
        String security_token = request.getSecurity_token();
        long clientId = request.getClientId();
        long mainClientId = request.getMainClientId();
        long startTime =  request.getStartTime();
        long endTime = request.getEndTime();
        int numberPage = request.getNumberPage();
        int sizePage = request.getSizePage();
        return administrationService.corporateClientCashFlow(security_token, startTime, endTime, clientId, mainClientId, numberPage, sizePage);
    }





    @RequestMapping(value = "/client/find/arm", method = RequestMethod.POST)
    public
    @ResponseBody
    ClientInfoArmResponse findClientArm(@RequestBody ClientInfoArmRequest request) {
        return  administrationService.findClientArm(request.getSecurity_token(), request.getClientId());
    }



    @RequestMapping(value = "/updateDriverSetting", method = RequestMethod.POST)
    public
    @ResponseBody
    UpdateDriverSettingResponse updateDriverSetting(@RequestBody UpdateDriverSettingRequest request) {
        return administrationService.updateDriverSetting(request.getSecurity_token(), request.getDriverSettingInfo());
    }


    @RequestMapping(value = "/driverSetting", method = RequestMethod.POST)
    public
    @ResponseBody
    DriverSettingResponse driverSetting(@RequestBody DriverSettingRequest request) {
        return administrationService.driverSetting(request.getSecurity_token(), request.getDriverId());
    }



    @RequestMapping(value = "/driverTimeWorkStatistic", method = RequestMethod.POST)
    public
    @ResponseBody
    DriverTimeWorkStatisticResponse driverStatAuto(@RequestBody DriverTimeWorkStatisticRequest request) {
        WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
        if(webUser == null){
            throw new CustomException(1, "Web user not found");
        }
        return administrationService.driverTimeWorkStatistic(request.getDriverId(), request.getStartTime(), request.getEndTime(), webUser.getTaxoparkId());
    }





    @RequestMapping(value = "/activityDriver", method = RequestMethod.POST)
    public
    @ResponseBody
    ActivityDriverResponse activityDriver(@RequestBody ActivityDriverRequest request) {
        // время передается в секундах
        return mongoDBServices.activityDriver(request.getSecurity_token(), request.getDriverId(), request.getStartTime(), request.getEndTime()); // , request.getNumberPage(), request.getSizePage()
    }



    @RequestMapping(value = "/activateTariff", method = RequestMethod.POST)
    public
    @ResponseBody
    ActivateTariffResponse activateTariff(@RequestBody ActivateTariffRequest request) {
        return administrationService.activateTariff(request.getSecurity_token(), request.getClientId(), request.getExpirationDate(), request.isActive(), request.isActivated());
    }



    //DriverStatsInfo
    @RequestMapping(value = "/driverTimeWorkAndMissionCompleteStat", method = RequestMethod.POST)
    public
    @ResponseBody
    DriverTimeWorkAndMissionCompleteStatResponse driverStatAuto(@RequestBody DriverTimeWorkAndMissionCompleteStatRequest request) {
        WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
        if(webUser == null){
            throw new CustomException(1, "Web user not found");
        }
            return administrationService.driverTimeWorkAndMissionCompleteStat(request.getDriverId(), request.getStartTime(), request.getEndTime(), webUser.getTaxoparkId());
    }




    @RequestMapping(value = "/tariffRestriction", method = RequestMethod.POST)
    @ResponseBody
    public TariffRestrictionResponse tariffRestriction(@RequestBody TariffRestrictionRequest request) {
        WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
        if(webUser == null){
            throw new CustomException(1, "Web user not found");
        }
        return administrationService.tariffRestriction();
    }






    @RequestMapping(value = "/updateTariffRestriction", method = RequestMethod.POST)
    @ResponseBody
    public UpdateTariffRestrictionResponse updateTariffRestriction(@RequestBody UpdateTariffRestrictionRequest request) {
        WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
        if(webUser == null){
            throw new CustomException(1, "Web user not found");
        }
        return administrationService.updateTariffRestriction(request.getTariffRestrictionInfos(), request.isOff());
    }




    @RequestMapping(value = "/missionCanceledByClient", method = RequestMethod.POST)
    public
    @ResponseBody
    MissionCanceledByClientResponse missionCanceledByClient(@RequestBody MissionCanceledByClientRequest request) {
        WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
        if(webUser == null) {
            throw new CustomException(1, "Web user not found");
        }
            return administrationService.canceledMissionListByClient(request.getStartTime(), request.getEndTime(), webUser.getTaxoparkId());
    }



    @RequestMapping(value = "/missionByRegion", method = RequestMethod.POST)
    public
    @ResponseBody
    MissionByRegionResponse missionByRegion(@RequestBody MissionByRegionRequest request) {
        WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
        if(webUser == null) {
            throw new CustomException(1, "Web user not found");
        }
        return administrationService.missionByRegion(request.getStartTime(), request.getEndTime(), request.getRegionId(), request.getGroupBy(), webUser.getTaxoparkId());
    }




    @RequestMapping(value = "/fantomStat", method = RequestMethod.POST)
    public
    @ResponseBody
    FantomStatResponse fantomStat(@RequestBody FantomStatRequest request) {
        WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
        if(webUser == null) {
            throw new CustomException(1, "Web user not found");
        }
        return administrationService.fantomStat(request.getClientId(), request.getFantomDriverId(), request.getRegionId(), request.getState(), request.getStartTime(), request.getEndTime(), request.getNumPage(), request.getPageSize());
    }



    @RequestMapping(value = "/fantomUpdate", method = RequestMethod.POST)
    public
    @ResponseBody
    FantomUpdateResponse fantomUpdate(@RequestBody FantomUpdateRequest request) {
        WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
        if(webUser == null) {
            throw new CustomException(1, "Web user not found");
        }
        return administrationService.fantomUpdate(request.getFantomInfos());
    }




    /*
    @RequestMapping(value = "/fantoms", method = RequestMethod.POST)
    public
    @ResponseBody
    FantomResponse fantomUpdate(@RequestBody FantomRequest request) {
        WebUser webUser = webUserRepository.findByToken(request.getSecurity_token());
        if(webUser == null) {
            throw new CustomException(1, "Web user not found");
        }
        return administrationService.fantoms(request.getDriverId());
    }
    */

}
