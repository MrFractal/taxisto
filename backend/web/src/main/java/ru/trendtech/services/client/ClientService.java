package ru.trendtech.services.client;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.Seconds;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import ru.trendtech.common.mobileexchange.model.client.*;
import ru.trendtech.common.mobileexchange.model.client.corporate.*;
import ru.trendtech.common.mobileexchange.model.common.*;
import ru.trendtech.common.mobileexchange.model.common.corporate.MissionInfoCorporate;
import ru.trendtech.common.mobileexchange.model.common.rates.*;
import ru.trendtech.common.mobileexchange.model.common.states.ServerState;
import ru.trendtech.common.mobileexchange.model.common.states.ServerStateInfo;
import ru.trendtech.domain.*;
import ru.trendtech.domain.AutoClass;
import ru.trendtech.domain.Properties;
import ru.trendtech.domain.billing.Account;
import ru.trendtech.domain.billing.AutoClassPrice;
import ru.trendtech.domain.billing.MissionRate;
import ru.trendtech.domain.billing.PaymentType;
import ru.trendtech.domain.dgis.DGISStreet;
import ru.trendtech.models.ModelsUtils;
import ru.trendtech.repositories.*;
import ru.trendtech.repositories.billing.AccountRepository;
import ru.trendtech.repositories.billing.ClientCardRepository;
import ru.trendtech.repositories.billing.MissionRatesRepository;
import ru.trendtech.services.MongoDBServices;
import ru.trendtech.services.administration.AdministrationService;
import ru.trendtech.services.billing.BillingService;
import ru.trendtech.services.common.CommonService;
import ru.trendtech.services.logging.GrayLogService;
import ru.trendtech.services.search.AutoSearchMission;
import ru.trendtech.services.sms.ServiceSMSNotification;
import ru.trendtech.services.turbo.TurboMission;
import ru.trendtech.services.validate.ValidatorService;
import ru.trendtech.services.wifi.WiFiConnection;
import ru.trendtech.services.notifications.node.NodeJsNotificationsService;
import ru.trendtech.services.search.ServiceAutoSearch;
import ru.trendtech.services.driver.search.FindDriversService;
import ru.trendtech.utils.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by petr on 12.03.2015.
 */
@Service
@Transactional
public class ClientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientService.class);
    @Autowired
    AdministrationService administrationService;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    PromoCodeExclusiveRepository promoCodeExclusiveRepository;
    @Autowired
    PrivateTariffRepository privateTariffRepository;
    @Autowired
    private PromoCodeRepository promoCodeRepository;
    @Autowired
    private ClientAvailableActivatePromoCodeRepository clientAvailableActivatePromoCodeRepository;
    @Autowired
    private ClientActivatedPromoCodesRepository clientActivatedPromoCodesRepository;
    @Autowired
    private MongoDBServices mongoDBServices;
    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    private ServicesRepository servicesRepository;
    @Autowired
    private BillingService billingService;
    @Autowired
    private MissionRatesRepository missionRatesRepository;
    @Autowired
    private CorporateClientLimitRepository corporateClientLimitRepository;
    @Autowired
    private CorporateClientLocksRepository corporateClientLocksRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private MissionRepository missionRepository;
    @Autowired
    private MultipleMissionRepository multipleMissionRepository;
    @Autowired
    private FindDriversService findDriversService;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private ServiceAutoSearch autoSearchService;
    @Autowired
    private EstimateRepository estimateRepository;
    @Autowired
    private AutoClassCostExampleRepository autoClassCostExampleRepository;
    @Autowired
    private CommonService commonService;
    @Autowired
    private ReroutingRepository reroutingRepository;
    @Autowired
    private MdOrderRepository mdOrderRepository;
    @Autowired
    private ImageSourceRepository imageSourceRepository;
    @Autowired
    private ClientWifiConnectionRepository clientWifiConnectionRepository;
    @Autowired
    private NodeJsNotificationsService nodeJsNotificationsService;
    @Autowired
    private ReasonRepository reasonRepository;
    @Autowired
    private WatchMissionRepository watchMissionRepository;
    @Autowired
    private ServiceSMSNotification serviceSMSNotification;
    @Autowired
    private TariffRestrictionRepository tariffRestrictionRepository;
    @Autowired
    private PropertiesRepository propertiesRepository;
    @Autowired
    private ClientCardRepository clientCardRepository;
    @Autowired
    private GrayLogService grayLogService;
    @Autowired
    private ValidatorService validatorService;
    @Autowired
    @Qualifier("mts")
    private WiFiConnection wiFiConnection;




    // isTurnOn = true - включить, isTurnOn = false - выключить
    public TurnOnWiFiResponse turnOnWiFi(boolean isTurnOn, Client client){

        // todo: убрать отсюда логику с isTurnOn = false и перенести в вотчер

        TurnOnWiFiResponse response = new TurnOnWiFiResponse();
        Mission mission = client.getMission();
        if(mission == null){
           throw new CustomException(1, "Текущий заказ отсутствует");
        }
        if(mission.getClientInfo()!= null && !mission.getClientInfo().getId().equals(client.getId())){
            throw new CustomException(8, "Миссия не соответсвует клиенту");
        }
        if(!mission.getState().equals(Mission.State.IN_TRIP)){
            throw new CustomException(2, "Миссия не находится в режиме поездки");
        }
        if(mission.getDriverInfo() == null){
            throw new CustomException(9, "Отсутствует водитель");
        }

        String connectionResult;

        ClientWifiConnection wifi = clientWifiConnectionRepository.findByMission(mission);

        if(wifi!=null){
            throw new CustomException(4, "Запрос на включение wifi выполнен");
        }

        wifi = new ClientWifiConnection(DateTimeUtils.nowNovosib_GMT6(), mission);
        clientWifiConnectionRepository.save(wifi);

        connectionResult = wiFiConnection.openConnection(mission);

        if(connectionResult.equals("ok")){
            wifi.setState(ClientWifiConnection.StateConnection.ONLINE);
            clientWifiConnectionRepository.save(wifi);
        } else{
            wifi.setState(ClientWifiConnection.StateConnection.ERROR);
            clientWifiConnectionRepository.save(wifi);
            throw new CustomException(6, String.format("При открытии соединения произошла ошибка: %s", connectionResult));
        }
           return response;
    }

    /*
        else{
            wifi = clientWifiConnectionRepository.findByMissionAndTimeOfClosingIsNull(mission);
            if(wifi == null){
                throw new CustomException(5, "Нет открытых соединений");
            }

            connectionResult = wiFiConnection.closeConnection(mission);

            if(connectionResult.equals("ok")){
                wifi.setTimeOfClosing(DateTimeUtils.nowNovosib_GMT6());
                wifi.setState(ClientWifiConnection.StateConnection.OFFLINE);
                clientWifiConnectionRepository.save(wifi);
            }else{
                // при закрытии произошла ошибка
                wifi.setState(ClientWifiConnection.StateConnection.ERROR);
                clientWifiConnectionRepository.save(wifi);
                throw new CustomException(7, String.format("При закрытии соединения произошла ошибка: %s", connectionResult));
            }
        }
        */







    public ImageSourceResponse imageSource(){
        ImageSourceResponse response = new ImageSourceResponse();
        Iterable<ImageSource> imageSourceList = imageSourceRepository.findAll();
          for(ImageSource imageSource: imageSourceList){
              response.getInfoList().add(ModelsUtils.toModel(imageSource));
          }
          return response;
    }





    public ServerStateInfo resolveClientState(long clientId) {
        ServerStateInfo result;
        ServerState state = ServerState.NOT_LOGGED_IN;
        Mission mission = null;
        Client clientInfo = null;

        if (clientId != 0){
            state = ServerState.DEFAULT;
            clientInfo = clientRepository.findOne(clientId);

            if (clientInfo != null){
                mission = clientInfo.getMission();
            } else {
                LOGGER.error("Client with id: {} not found!", clientId);
            }
        }
        long lastStartTime = 0;
        PaymentInfo paymentInfo = new PaymentInfo();

        if (mission != null){
            List<Mission.PauseInfo> pauses = mission.getStatistics().getPauses();
            if (!pauses.isEmpty()){
                Mission.PauseInfo pauseInfo = pauses.get(pauses.size() - 1);
                if (pauseInfo.getEndPause() == null){
                    lastStartTime = pauseInfo.getStartPause().getMillis();
                }
            }
            if(Mission.State.NEW.equals(mission.getState())){
                state = ServerState.SEARCH_DRIVER;
            } else if(Mission.State.AUTO_SEARCH.equals(mission.getState())){
                state = ServerState.AUTO_SEARCH_DRIVER;
            } else if (Mission.State.ASSIGNED.equals(mission.getState())){
                state = ServerState.ASSIGNED_DRIVER;
            } else if (Mission.State.ARRIVED.equals(mission.getState())){
                state = ServerState.ARRIVED_DRIVER;
            } else if (EnumSet.of(Mission.State.IN_TRIP, Mission.State.IN_TRIP_PAUSED).contains(mission.getState())){
                state = ServerState.IN_TRIP;

                DateTime timeOfSeating =  mission.getTimeOfSeating();
                DateTime timeOfArriving =  mission.getTimeOfArriving();
                int secondsOverWaitingTime = 0;

                if(timeOfSeating!=null && timeOfArriving!=null){
                    Seconds seconds = Seconds.secondsBetween(timeOfArriving, timeOfSeating);
                    int sec = seconds.getSeconds();
                    if(sec>0){
                        secondsOverWaitingTime += sec;
                    }else{
                        sec = sec*(-1);
                        secondsOverWaitingTime += sec;
                    }
                }
                paymentInfo.setWaitingOverFree(secondsOverWaitingTime);
                paymentInfo.setPausesCount(pauses.size());

                /*
                int totalTimePausesInMinute = 0;
                for(Mission.PauseInfo pauseInfo: pauses){
                    DateTime start =  pauseInfo.getStartPause();
                    DateTime end = pauseInfo.getEndPause();
                    if(end!=null){
                        Seconds seconds = Seconds.secondsBetween(start, end);
                        int min;
                        int sec = seconds.getSeconds();
                        if(sec % 60 != 0){
                            min = (sec/60 +1);
                        }else{
                            min = sec/60;
                        }
                        totalTimePausesInMinute+= min;
                    }
                }
                */
                paymentInfo.setPausesTime(commonService.getPausesInMinutesCount(mission)); // totalTimePausesInMinute
            } else if (Mission.State.IN_TRIP_END.equals(mission.getState())){
                paymentInfo = administrationService.missionFinished(10, mission.getId());
                state = ServerState.TRIP_FINISHED;
                paymentInfo.setWaitingOverFree(mission.getStatistics().getOverWaitingTime());
                paymentInfo.setPausesCount(pauses.size());
                /*
                int totalTimePausesInMinute = 0;
                for(Mission.PauseInfo pauseInfo: pauses){
                    DateTime start =  pauseInfo.getStartPause();
                    DateTime end = pauseInfo.getEndPause();
                    Minutes minutes = Minutes.minutesBetween(start, end);
                    totalTimePausesInMinute+= minutes.getMinutes();
                }
                */
                paymentInfo.setPausesTime(commonService.getPausesInMinutesCount(mission)); // totalTimePausesInMinute
            } else if(EnumSet.of(Mission.State.COMPLETED, Mission.State.CANCELED).contains(mission.getState())){
                if (clientInfo != null){
                    clientInfo.setMission(null);
                    clientRepository.save(clientInfo);
                }
            }
        }

        MissionInfo missionInfo = null;
        if(mission!=null){
            missionInfo =  ModelsUtils.toModel(mission);

            /* отдавать единые номера  */
            missionInfo = (MissionInfo)commonService.commonPhoneNumber(missionInfo);

            boolean versionAppIsNew = commonService.checkServiceByVersionApp(clientInfo.getVersionApp(), clientInfo.getDeviceType());
            missionInfo.setDriverInfo(administrationService.fillPhotoDriverAndCars(missionInfo.getDriverInfo(), mission.getDriverInfo(), versionAppIsNew));
            missionInfo.setDistance(mission.getStatistics().getDistanceInFact());

            List<Rerouting> rerouting = reroutingRepository.findByMissionId(mission.getId());
            if(rerouting!=null && !rerouting.isEmpty()){
                missionInfo.setRerouting(true);
            }else{
                missionInfo.setRerouting(false);
            }
            if(!Mission.PaymentStateCard.NONE.equals(mission.getState())){
                MDOrder mdOrder = mdOrderRepository.findByMission(mission);
                if(mdOrder!=null){
                    int sum = mdOrder.getSum()/100;
                    missionInfo.setPaymentCardPrice(sum);
                }
            }
        }
        result = new ServerStateInfo(state.getStateId(), missionInfo, paymentInfo, lastStartTime);
           return result;
    }




    public AdministrationService.HistoryMissions missionsHistoryClient(long clientId, String token) {
        AdministrationService.HistoryMissions result = new AdministrationService.HistoryMissions();
            Client client = clientRepository.findOne(clientId);
            if (client != null) {
                if (client.getToken() != null && client.getToken().equals(token)) {
                    int pageSize = Integer.parseInt(commonService.getPropertyValue("mission_complete_limit"));
                    List<Mission> missions = missionRepository.findByClientInfoAndStateAndStatusDeleteOrderByTimeOfStartingDesc(client, Mission.State.COMPLETED, false, new PageRequest(0, pageSize)); // , new PageRequest(numPage, count)
                    for (Mission mission : missions) {
                        MissionInfo missionInfo = ModelsUtils.toModelClient(mission);
                        Estimate estimate = estimateRepository.findByMission(mission);
                        if(estimate!=null){
                            missionInfo.setRating(estimate.getGeneral());
                        }
                        result.history.add(missionInfo);
                    }

                    missions = missionRepository.findByClientInfoAndStateOrderByTimeOfStartingDesc(client, Mission.State.BOOKED); // f:add
                    for (Mission mission : missions) {
                        MissionInfo missionInfo = ModelsUtils.toModelClient(mission);
                        Estimate estimate = estimateRepository.findByMission(mission);
                        if(estimate!=null){
                            missionInfo.setRating(estimate.getGeneral());
                        }
                        result.booked.add(missionInfo);
                    }
                } else {
                    LOGGER.info("Tokens are not equal");
                }
            }
        return result;
    }





    public MissionFindResponse missionFindV2(long missionId, Client client){
        MissionFindResponse response = new MissionFindResponse();
        MissionInfo result;
        Mission mission = missionRepository.findOne(missionId);
        if (mission != null) {
            boolean versionAppIsNew = commonService.checkServiceByVersionApp(client.getVersionApp(), client.getDeviceType());
            result = ModelsUtils.toModel(mission);
            result.setDriverInfo(administrationService.fillPhotoDriverAndCars(result.getDriverInfo(), mission.getDriverInfo(), versionAppIsNew));

            Estimate estimate = estimateRepository.findByMission(mission);
            if(estimate!=null){
                result.setRating(estimate.getGeneral());
            }

            /* отдавать единые номера  */
            result = (MissionInfo)commonService.commonPhoneNumber(result);

            response.setMissionInfo(result);
        }
        return response;
    }





    public AdministrationService.HistoryMissions_STR missionsHistoryClientSTR(long clientId, String token) { // , int numPage, int count
        AdministrationService.HistoryMissions_STR result = new AdministrationService.HistoryMissions_STR();
        Client client = clientRepository.findOne(clientId);
        if (client != null) {
            if(client.getToken()!= null && client.getToken().equals(token)){
                int pageSize = Integer.parseInt(commonService.getPropertyValue("mission_complete_limit"));
                boolean versionAppIsNew = commonService.checkServiceByVersionApp(client.getVersionApp(), client.getDeviceType());

                List<Mission> missions = missionRepository.findByClientInfoAndStateAndStatusDeleteOrderByTimeOfStartingDesc(client, Mission.State.COMPLETED, Boolean.FALSE, new PageRequest(0, pageSize)); // , new PageRequest(numPage, count)
                result = new AdministrationService.HistoryMissions_STR();

                for (Mission mission : missions) {
                    MissionHistory missionHistoryCompleted = new MissionHistory();

                    MissionInfo missionInfo = ModelsUtils.toModelClient(mission);

                    /* отдавать единые номера  */
                    missionInfo = (MissionInfo)commonService.commonPhoneNumber(missionInfo);

                    missionInfo.setDriverInfo(administrationService.fillPhotoDriverAndCars(missionInfo.getDriverInfo(), mission.getDriverInfo(), versionAppIsNew));

                    /* берем general из таблицы estimate,  позже в toModelClient убрать missionInfo.setRating(((double)mission.getScore().getGeneral()));*/
                    Estimate estimate = estimateRepository.findByMission(mission);
                    if(estimate!=null){
                        missionInfo.setRating(estimate.getGeneral());
                    }

                    missionHistoryCompleted.setMissionInfo(missionInfo);
                    missionHistoryCompleted.setTimeRequesting(mission.getTimeOfRequesting().toString());
                    missionHistoryCompleted.setTimeStarting(mission.getTimeOfStarting().toString());
                    missionHistoryCompleted.setTimeNow(DateTimeUtils.nowNovosib_GMT6().toString());
                    missionHistoryCompleted.setAutoClassStr(ModelsUtils.autoTypeStr(mission.getAutoClass().getValue()));
                    result.history.add(missionHistoryCompleted);
                    //result.history.add(completedList);
                }

                missions = missionRepository.findByClientInfoAndStateOrderByTimeOfStartingDesc(client, Mission.State.BOOKED); // f:add

                for (Mission mission : missions) {
                    MissionHistory missionHistoryBooked = new MissionHistory();
                    MissionInfo missionInfo = ModelsUtils.toModelClient(mission);

                    /* отдавать единые номера  */
                    missionInfo = (MissionInfo)commonService.commonPhoneNumber(missionInfo);

                    missionInfo.setDriverInfo(administrationService.fillPhotoDriverAndCars(missionInfo.getDriverInfo(), mission.getDriverInfo(), versionAppIsNew));

                    Estimate estimate = estimateRepository.findByMission(mission);
                    if(estimate!=null){
                        missionInfo.setRating(estimate.getGeneral());
                    }
                    missionHistoryBooked.setMissionInfo(missionInfo);
                    missionHistoryBooked.setTimeRequesting(mission.getTimeOfRequesting().toString());
                    missionHistoryBooked.setTimeStarting(mission.getTimeOfStarting().toString());
                    missionHistoryBooked.setTimeNow(DateTimeUtils.nowNovosib_GMT6().toString());
                    missionHistoryBooked.setAutoClassStr(ModelsUtils.autoTypeStr(mission.getAutoClass().getValue()));
                    result.booked.add(missionHistoryBooked);
                }

            }else{
                LOGGER.info("Tokens are not equal");
            }
        }
        return result;
    }





    public AutoClassPriceResponseV2 autoClassPrices(Client client){
        AutoClassPriceResponseV2 response = new AutoClassPriceResponseV2();
        PrivateTariff privateTariff = privateTariffRepository.findByClientAndTariffNameAndActive(client, AutoClass.BONUS.name(), Boolean.TRUE);
        boolean isCorporate = false;
        if(client!=null && client.getMainClient()!=null){
            isCorporate = true;
        }
        MissionRate missionRate = missionRatesRepository.findOne((long) 1);
        Set<AutoClassPrice> autoClassPriceSet = missionRate.getAutoClassPrices();

        List<AutoClassRateInfoV3> autoClassRateInfos = billingService.getAutoClassRatesV3(autoClassPriceSet, isCorporate);

        /* check auto class price by private tariff */
        if(privateTariff == null) {
            // для пользователя недоступен тариф бонус, значит удалаяем его из коллекции
                for (ListIterator<AutoClassRateInfoV3> i = autoClassRateInfos.listIterator(); i.hasNext(); ) {
                    AutoClassRateInfoV3 el = i.next();
                    if(el.getAutoClass()==AutoClass.BONUS.getValue()){
                        i.remove();
                    }
                }
        }else{
            // тариф бонус доступен, удаляем лоукостер и вместо него ставим бонусный тариф - эта шняга сделана для того, чтобы не поплыл новый дизайн
            for (ListIterator<AutoClassRateInfoV3> i = autoClassRateInfos.listIterator(); i.hasNext(); ) {
                AutoClassRateInfoV3 el = i.next();
                if(el.getAutoClass()==AutoClass.LOW_COSTER.getValue()){
                    i.remove();
                }
            }
        }
        /* end */

        /* check auto class price by version app client */
        String showVersion = administrationService.showTarifByVersionApp(client.getVersionApp(), client.getDeviceType());
        if (showVersion!=null && !showVersion.isEmpty()){
            String[] versions = showVersion.split(",");
            for (ListIterator<AutoClassRateInfoV3> i = autoClassRateInfos.listIterator(); i.hasNext(); ) {
                AutoClassRateInfoV3 el = i.next();
                if(!administrationService.useLoop(versions, String.valueOf(el.getAutoClass()))){
                    i.remove();
                }
            }
        }
        /* end */
            for(AutoClassRateInfoV3 infoV3: autoClassRateInfos){
               List<AutoClassCostExample> autoClassCostExamples = autoClassCostExampleRepository.findByAutoClass(AutoClass.getByValue(infoV3.getAutoClass()));
               if(!CollectionUtils.isEmpty(autoClassCostExamples)){
                   for(AutoClassCostExample example: autoClassCostExamples){
                       infoV3.getAutoClassCostExampleInfos().add(ModelsUtils.toModel(example));
                   }
               }
            }
            response.setAutoClassRateInfoV3List(autoClassRateInfos);

            return response;
    }




    public ClientMoneyAmountResponse getMoneyAmount(long clientId){
        ClientMoneyAmountResponse response = new ClientMoneyAmountResponse();
        Client client = clientRepository.findOne(clientId);
        response.setBonusesAmount(client.getAccount().getBonuses().getAmount().intValue());
        if(client.getMainClient()!=null){
          response.setCorporateAmount(client.getMainClient().getAccount().getCorporateBalance().getAmount().intValue());
        }
        response.setCourierAmount(client.getAccount().getCourierBonuses() != null ? client.getAccount().getCourierBonuses().getAmount().intValue() : 0);
         return response;
    }





    // проверка MissionInfo на валидность
    private ErrorCodeHelper checkMission(MissionInfo missionInfo, Client client){
        ErrorCodeHelper errorCodeHelper = new ErrorCodeHelper();
        if (missionInfo == null || missionInfo.getClientInfo() == null) {
            errorCodeHelper.setErrorCode(1);
            errorCodeHelper.setErrorMessage("Пользователь с такими данными не найден");
            return errorCodeHelper;
        }

        if(missionInfo.getLocationFrom()!=null && missionInfo.getLocationFrom().getLatitude()!=0.0 && missionInfo.getLocationFrom().getLongitude()!=0.0){
            RegionInfo info = administrationService.pointInsidePolygon(missionInfo.getLocationFrom().getLatitude(), missionInfo.getLocationFrom().getLongitude());
            if(info == null){
                errorCodeHelper.setErrorCode(8);
                errorCodeHelper.setErrorMessage("Заказ такси в Вашем регионе недоступен!");
                return errorCodeHelper;
            }
        }


        /* КОСТЫЛЬ ДЛЯ БОРИСА: СОЗДАН 13.07.2015 19:18  */
        PaymentType paymentType = PaymentType.getByValue(missionInfo.getPaymentType());
        if(paymentType.getValue()>2 && client.getDeviceType().equals("APPLE") && (client.getVersionApp().equals("2.0.2") || client.getVersionApp().equals("2.0.0"))){ // client.getVersionApp().equals("2.0.3") || client.getVersionApp().equals("2.0.2") || client.getVersionApp().equals("2.0.0")
              paymentType = PaymentType.getByValue(paymentType.getValue()+1);
        }


        if(!EnumSet.of(PaymentType.CORPORATE_CARD, PaymentType.CORPORATE_BILL, PaymentType.CORPORATE_CASH).contains(paymentType)){
            Properties properties = propertiesRepository.findByPropName("low_coster_off");
            if(properties != null){
                if(properties.getPropValue().equals("1") && AutoClass.getByValue(missionInfo.getAutoType()).equals(AutoClass.LOW_COSTER)){
                    // тариф лоукостер выключен
                    errorCodeHelper.setErrorCode(9);
                    errorCodeHelper.setErrorMessage(properties.getDescription());
                    return errorCodeHelper;
                }
            }
            RestrictionHelper restrictionHelper = checkRestriction(missionInfo.getAutoType(), missionInfo);
            if(restrictionHelper.isRestriction()){
                errorCodeHelper.setErrorCode(9);
                errorCodeHelper.setErrorMessage(restrictionHelper.getMessage());
                return errorCodeHelper;
            }
        }

        /*
        if(EnumSet.of(PaymentType.CASH).contains(paymentType)){
            int sumDebt = commonService.clientSumDebt(client);
            if(sumDebt < 0){
                errorCodeHelper.setErrorCode(11);
                errorCodeHelper.setErrorMessage(String.format(commonService.getPropertyValue("client_debt_message"), Math.abs(sumDebt)));
                return errorCodeHelper;
            }
        }
        */


        /* если заказ бронь, проверяем включены ли брони*/
        if(missionInfo.isBooked()){
            int bookedActive = Integer.parseInt(commonService.getPropertyValue("booked_active"));
            if(bookedActive==0){
                // брони выключены
                errorCodeHelper.setErrorCode(7);
                errorCodeHelper.setErrorMessage("В данные момент предварительное бронирование недоступно, вы можете сделать заказ только на ближайшее время.Приносим свои извинения!");
                return errorCodeHelper;
            }
        }
        if(administrationService.isOverPrice(missionInfo)){
            errorCodeHelper.setErrorCode(6);
            errorCodeHelper.setErrorMessage("Конечный адрес указан некорректно!");
            return errorCodeHelper;
        }


        boolean isPaymentCardAvailable = administrationService.isPaymentCardAvailable();

        if (!isPaymentCardAvailable && (paymentType.equals(PaymentType.CARD) || paymentType.equals(PaymentType.CORPORATE_CARD))) {
            // оплата картой недоступна
            errorCodeHelper.setErrorCode(4);
            errorCodeHelper.setErrorMessage("Оплата картой недоступна");
            return errorCodeHelper;
        }

        PrivateTariff privateTariff = privateTariffRepository.findByClientAndTariffNameAndActive(client, AutoClass.getByValue(missionInfo.getAutoType()).name(), true);

        if(paymentType.equals(PaymentType.CARD)){
            ClientCard clientCard = getActiveClientCard(client);
            if(clientCard==null){
                errorCodeHelper.setErrorCode(5);
                errorCodeHelper.setErrorMessage("Данный вид заказа станет доступным только после добавления карты в приложение!");
                return errorCodeHelper;
            }
        }

        else if(paymentType.equals(PaymentType.CORPORATE_CARD)){
            if(!isCorporateClient(client)){
                errorCodeHelper.setErrorCode(6);
                errorCodeHelper.setErrorMessage("Вы не являетесь корпоративным клиентом");
                return errorCodeHelper;
            }
            if(client.getAccount().getState().equals(Account.State.BLOCKED)|| client.getMainClient().getAccount().getState().equals(Account.State.BLOCKED)){
                errorCodeHelper.setErrorCode(8);
                errorCodeHelper.setErrorMessage("Корпоративная учетная запись заблокирована!");
                return errorCodeHelper;
            }
            if(privateTariff==null){
                errorCodeHelper.setErrorCode(11);
                errorCodeHelper.setErrorMessage("Данный тип авто для Вас недоступен");
                return errorCodeHelper;
            }
            ClientCard mainClientCard = getActiveClientCard(client.getMainClient());
            if(mainClientCard==null){
                errorCodeHelper.setErrorCode(5);
                errorCodeHelper.setErrorMessage("Данный вид заказа станет доступным только после добавления карты в приложение!");
                return errorCodeHelper;
            }
        }


        else if(paymentType.equals(PaymentType.CORPORATE_BILL)){
            if(!isCorporateClient(client)){
                errorCodeHelper.setErrorCode(6);
                errorCodeHelper.setErrorMessage("Вы не являетесь корпоративным клиентом");
                return errorCodeHelper;
            }
            if(client.getAccount().getState().equals(Account.State.BLOCKED)|| client.getMainClient().getAccount().getState().equals(Account.State.BLOCKED)){
                errorCodeHelper.setErrorCode(8);
                errorCodeHelper.setErrorMessage("Корпоративная учетная запись заблокирована!");
                return errorCodeHelper;
            }
            if(privateTariff==null){
                errorCodeHelper.setErrorCode(14);
                errorCodeHelper.setErrorMessage("Данный тип авто для Вас недоступен");
                return errorCodeHelper;
            }
            // лимиты пока только на корпорейт билл распространяются
            boolean isOverLimit = administrationService.isOverLimit(client, PaymentType.getByValue(missionInfo.getPaymentType()), (int)missionInfo.getExpectedPrice()*100);
            if(isOverLimit){
                //превышен лимит
                errorCodeHelper.setErrorCode(7);
                errorCodeHelper.setErrorMessage("Превышен лимит средств");
                return errorCodeHelper;
            }
        }


        else if(paymentType.equals(PaymentType.CORPORATE_CASH)){
            if(!isCorporateClient(client)){
                errorCodeHelper.setErrorCode(6);
                errorCodeHelper.setErrorMessage("Вы не являетесь корпоративным клиентом");
                return errorCodeHelper;
            }
            if(client.getAccount().getState().equals(Account.State.BLOCKED)|| client.getMainClient().getAccount().getState().equals(Account.State.BLOCKED)){
                errorCodeHelper.setErrorCode(8);
                errorCodeHelper.setErrorMessage("Корпоративная учетная запись заблокирована!");
                return errorCodeHelper;
            }
            if(privateTariff==null){
                errorCodeHelper.setErrorCode(15);
                errorCodeHelper.setErrorMessage("Данный тип авто для Вас недоступен");
                return errorCodeHelper;
            }
        }

        if(AutoClass.BONUS.name().equals(AutoClass.getByValue(missionInfo.getAutoType()).name())){
            privateTariff = privateTariffRepository.findByClientAndTariffNameAndActiveAndIsActivatedAndExpirationDateGreaterThan(client, AutoClass.BONUS.name(), true, true, DateTimeUtils.nowNovosib_GMT6());
              if(privateTariff == null){
                  String mess = commonService.getPropertyValue("activate_bonus_tariff_message");
                  errorCodeHelper.setErrorCode(16);
                  errorCodeHelper.setErrorMessage(mess);
                  return errorCodeHelper;
              }
        }

        return errorCodeHelper;
    }







    public FreeDriverResponse createMissionWithStringTime(MissionInfo missionInfo, String timeStarting, Client client, String ip) throws JSONException, ParseException, IOException {
        FreeDriverResponse response = new FreeDriverResponse();
        ErrorCodeHelper errorCodeHelper = checkMission(missionInfo, client);
        if(errorCodeHelper.getErrorCode()!=0){
            response.setMissionId(-1);
            response.setErrorCodeHelper(errorCodeHelper);
              return response;
        }
            /* КОСТЫЛЬ ДЛЯ БОРИСА: СОЗДАН 13.07.2015 19:18  */
            PaymentType paymentType = PaymentType.getByValue(missionInfo.getPaymentType());
            if(paymentType.getValue()>2 && client.getDeviceType().equals("APPLE") && (client.getVersionApp().equals("2.0.2") || client.getVersionApp().equals("2.0.0"))){
                paymentType = PaymentType.getByValue(paymentType.getValue() + 1);
                missionInfo.setPaymentType(paymentType.getValue());
            }
            /* end  */
             return administrationService.createNewMissionSTR(missionInfo, timeStarting, ip);
    }





    public FreeDriverResponse createMissionOldVersion(MissionInfo missionInfo, String token) throws JSONException, ParseException, IOException {
        FreeDriverResponse response = new FreeDriverResponse();
        if (missionInfo == null && missionInfo.getClientInfo() == null) {
            response.setMissionId(-1);
            response.getErrorCodeHelper().setErrorCode(1);
            response.getErrorCodeHelper().setErrorMessage("Пользователь с такими данными не найден");
            return response;
        }
        Client client = clientRepository.findOne(missionInfo.getClientInfo().getId());
        if(client==null){
            response.setMissionId(-1);
            response.getErrorCodeHelper().setErrorCode(10);
            response.getErrorCodeHelper().setErrorMessage("Пользователь с такими данными не найден");
            return response;
        }

        if(missionInfo.getLocationFrom()!=null && missionInfo.getLocationFrom().getLatitude()!=0.0 && missionInfo.getLocationFrom().getLongitude()!=0.0){
            RegionInfo info = administrationService.pointInsidePolygon(missionInfo.getLocationFrom().getLatitude(), missionInfo.getLocationFrom().getLongitude());
            if(info == null){
                response.getErrorCodeHelper().setErrorCode(8);
                response.getErrorCodeHelper().setErrorMessage("Заказ такси в Вашем регионе недоступен!");
                return  response;
            }
        }


        PaymentType paymentType = PaymentType.getByValue(missionInfo.getPaymentType());
        if(!EnumSet.of(PaymentType.CORPORATE_CARD, PaymentType.CORPORATE_BILL, PaymentType.CORPORATE_CASH).contains(paymentType)){
            RestrictionHelper restrictionHelper = checkRestriction(missionInfo.getAutoType(), missionInfo);
            if(restrictionHelper.isRestriction()){
                response.getErrorCodeHelper().setErrorCode(9);
                response.getErrorCodeHelper().setErrorMessage(restrictionHelper.getMessage());
                return response;
            }
        }
        if(administrationService.isOverPrice(missionInfo)){
            response.setMissionId(-1);
            response.getErrorCodeHelper().setErrorCode(7);
            response.getErrorCodeHelper().setErrorMessage("Конечный адрес указан некорректно!");
            return response;
        }


        boolean isPaymentCardAvailable = administrationService.isPaymentCardAvailable();
        missionInfo.setTimeOfRequesting(DateTimeUtils.nowNovosib_GMT6().getMillis());
        missionInfo.setTimeOfStart(missionInfo.getTimeOfStart()+21600000); // +6 часов
        long diff =  missionInfo.getTimeOfStart() - missionInfo.getTimeOfRequesting();

        if (!isPaymentCardAvailable && (paymentType.equals(PaymentType.CARD) || paymentType.equals(PaymentType.CORPORATE_CARD))) {
            // оплата картой недоступна
            response.setMissionId(-1);
            response.getErrorCodeHelper().setErrorCode(4);
            response.getErrorCodeHelper().setErrorMessage("Оплата картой недоступна");
            return response;
        }

        PrivateTariff privateTariff = privateTariffRepository.findByClientAndTariffNameAndActive(client, AutoClass.getByValue(missionInfo.getAutoType()).name(), true);

        if(paymentType.equals(PaymentType.CARD)){
            ClientCard clientCard = getActiveClientCard(client);
            if(clientCard==null){
                response.setMissionId(-1);
                response.getErrorCodeHelper().setErrorCode(5);
                response.getErrorCodeHelper().setErrorMessage("Данный вид заказа станет доступным только после добавления карты в приложение!");
                return response;
            }
        }


        else if(paymentType.equals(PaymentType.CORPORATE_CARD)){
            if(!isCorporateClient(client)){
                response.setMissionId(-1);
                response.getErrorCodeHelper().setErrorCode(6);
                response.getErrorCodeHelper().setErrorMessage("Вы не являетесь корпоративным клиентом");
                return response;
            }
            if(client.getAccount().getState().equals(Account.State.BLOCKED)|| client.getMainClient().getAccount().getState().equals(Account.State.BLOCKED)){
                response.getErrorCodeHelper().setErrorCode(8);
                response.getErrorCodeHelper().setErrorMessage("Корпоративная учетная запись заблокирована!");
                return response;
            }
            if(privateTariff==null){
                response.setMissionId(-1);
                response.getErrorCodeHelper().setErrorCode(11);
                response.getErrorCodeHelper().setErrorMessage("Данный тип авто для Вас недоступен");
                return response;
            }
            ClientCard mainClientCard = getActiveClientCard(client.getMainClient());
            if(mainClientCard==null){
                response.setMissionId(-1);
                response.getErrorCodeHelper().setErrorCode(5);
                response.getErrorCodeHelper().setErrorMessage("Данный вид заказа станет доступным только после добавления карты в приложение!");
                return response;
            }
        }



        else if(paymentType.equals(PaymentType.CORPORATE_BILL)){
            if(!isCorporateClient(client)){
                response.setMissionId(-1);
                response.getErrorCodeHelper().setErrorCode(6);
                response.getErrorCodeHelper().setErrorMessage("Вы не являетесь корпоративным клиентом");
                return response;
            }
            if(client.getAccount().getState().equals(Account.State.BLOCKED)|| client.getMainClient().getAccount().getState().equals(Account.State.BLOCKED)){
                response.getErrorCodeHelper().setErrorCode(8);
                response.getErrorCodeHelper().setErrorMessage("Корпоративная учетная запись заблокирована!");
                return response;
            }
            if(privateTariff==null){
                response.setMissionId(-1);
                response.getErrorCodeHelper().setErrorCode(11);
                response.getErrorCodeHelper().setErrorMessage("Данный тип авто для Вас недоступен");
                return response;
            }
            // лимиты пока только на корпорейт билл распространяются
            boolean isOverLimit = administrationService.isOverLimit(client, PaymentType.getByValue(missionInfo.getPaymentType()), (int)missionInfo.getExpectedPrice()*100);
            if(isOverLimit){
                //превышен лимит
                response.getErrorCodeHelper().setErrorCode(7);
                response.getErrorCodeHelper().setErrorMessage("Превышен лимит средств");
                return response;
            }
        }


        else if(paymentType.equals(PaymentType.CORPORATE_CASH)){
            if(!isCorporateClient(client)){
                response.setMissionId(-1);
                response.getErrorCodeHelper().setErrorCode(6);
                response.getErrorCodeHelper().setErrorMessage("Вы не являетесь корпоративным клиентом");
                return response;
            }
            if(client.getAccount().getState().equals(Account.State.BLOCKED)|| client.getMainClient().getAccount().getState().equals(Account.State.BLOCKED)){
                response.getErrorCodeHelper().setErrorCode(8);
                response.getErrorCodeHelper().setErrorMessage("Корпоративная учетная запись заблокирована!");
                return response;
            }
            if(privateTariff==null){
                response.setMissionId(-1);
                response.getErrorCodeHelper().setErrorCode(11);
                response.getErrorCodeHelper().setErrorMessage("Данный тип авто для Вас недоступен");
                return response;
            }
        }


        if(AutoClass.BONUS.name().equals(AutoClass.getByValue(missionInfo.getAutoType()).name())){
            privateTariff = privateTariffRepository.findByClientAndTariffNameAndActiveAndIsActivatedAndExpirationDateGreaterThan(client, AutoClass.BONUS.name(), true, true, DateTimeUtils.nowNovosib_GMT6());
            if(privateTariff == null){
                String mess = commonService.getPropertyValue("activate_bonus_tariff_message");
                response.getErrorCodeHelper().setErrorCode(16);
                response.getErrorCodeHelper().setErrorMessage(mess);
                return response;
            }
        }


        if(diff<=0){
            // обычнй заказ
            missionInfo.setTimeOfStart(missionInfo.getTimeOfRequesting()+60000);
        }else{
            DateTime timeOfStarting = new DateTime(missionInfo.getTimeOfStart());
            DateTime timeOfRequesting = new DateTime(missionInfo.getTimeOfRequesting());
            Minutes minutes = Minutes.minutesBetween(timeOfRequesting, timeOfStarting);

            if(minutes.getMinutes()>2){
                response.setMissionId(-1);
                response.getErrorCodeHelper().setErrorCode(6);
                response.getErrorCodeHelper().setErrorMessage("В данные момент предварительное бронирование недоступно, вы можете сделать заказ только на ближайшее время.Приносим свои извинения!");
                return response;
            }
        }

        response = administrationService.createNewMission(missionInfo, token);

         return response;
    }







    public RestrictionHelper checkRestriction(int tariff, MissionInfo missionInfo){
        boolean flag = false;
        RestrictionHelper restrictionHelper = new RestrictionHelper();
        List<TariffRestriction> tariffRestrictions = tariffRestrictionRepository.findByTariffNameAndActiveAndHoliday(AutoClass.getByValue(tariff).name(), true, DateTimeUtils.nowNovosib_GMT6().getDayOfWeek() <= 5 ? false : true);
        if(tariffRestrictions!=null && !tariffRestrictions.isEmpty() && tariffRestrictions.size()==2){

            String text = commonService.getPropertyValue("text_restriction");
            String textHtml = commonService.getPropertyValue("text_restriction_html");

            for(TariffRestriction restriction :tariffRestrictions){
                DateTime start = DateTimeUtils.nowNovosib_GMT6();
                DateTime end = DateTimeUtils.nowNovosib_GMT6();
                DateTime now = DateTimeUtils.nowNovosib_GMT6();
                if(missionInfo.isBooked()){
                    now = DateTimeUtils.toDateTime(missionInfo.getTimeOfStart()+21600000);
                }

                int startHours = restriction.getStartHours();
                int startMinutes = restriction.getStartMinutes();
                int endHours = restriction.getEndHours();
                int endMinutes = restriction.getEndMinutes();

                start = start.withHourOfDay(startHours);
                start = start.withMinuteOfHour(startMinutes);
                end =  end.withHourOfDay(endHours);
                end = end.withMinuteOfHour(endMinutes);

                if(now.isBefore(end.getMillis()) && now.isAfter(start.getMillis())){
                    flag = true;
                }
            }
            if(flag){
                TariffRestriction first = tariffRestrictions.get(0);
                TariffRestriction second = tariffRestrictions.get(1);
                restrictionHelper.setRestriction(flag);
                restrictionHelper.setMessage(String.format(text, MathUtil.splitDigit(first.getStartHours()) + ":" + MathUtil.splitDigit(first.getStartMinutes()), MathUtil.splitDigit(first.getEndHours()) + ":" + MathUtil.splitDigit(first.getEndMinutes()), MathUtil.splitDigit(second.getStartHours()) + ":" + MathUtil.splitDigit(second.getStartMinutes()), MathUtil.splitDigit(second.getEndHours()) + ":" + MathUtil.splitDigit(second.getEndMinutes())));
                restrictionHelper.setHtmlMessage(String.format(textHtml, MathUtil.splitDigit(first.getStartHours()) + ":" + MathUtil.splitDigit(first.getStartMinutes()), MathUtil.splitDigit(first.getEndHours()) + ":" + MathUtil.splitDigit(first.getEndMinutes()), MathUtil.splitDigit(second.getStartHours()) + ":" + MathUtil.splitDigit(second.getStartMinutes()), MathUtil.splitDigit(second.getEndHours()) + ":" + MathUtil.splitDigit(second.getEndMinutes())));
            }
        }
        return restrictionHelper;
    }



    private boolean isCorporateClient(Client client){
         return client.getMainClient()!=null;
    }



//    private ClientCard getActiveClientCard(Client client){
//        return getActiveClientCard(client);
//    }


    public void emailUnsubscribe(Client client, boolean unsubscribe, String ip){
              client.setEmailUnsubscribe(unsubscribe);
              clientRepository.save(client);
    }



    public void cancelMissionByClient(long missionId, long clientId, Reason reason, String ip) {
        Mission mission = missionRepository.findOne(missionId);
        if(mission == null){
            throw new CustomException(1, "Миссия не найдена");
        }
        String stateBeforeCanceled = mission.getState().toString();
        boolean missionFromQueque = false;
        Driver driver = mission.getDriverInfo();
        if (driver != null) {
            if(driver.getCurrentMission()!=null && driver.getCurrentMission().getId().equals(missionId)){
                nodeJsNotificationsService.missionCancelSecondOrder(missionId, driver.getId());
            }else{
                nodeJsNotificationsService.driverCustomMessageARM(driver.getId(), "Клиент отменил Ваш следующий заказ");
                missionFromQueque = true;
            }
        }
            grayLogService.sendToGrayLog(clientId, 0, 0, "cancelMissionByClient", "Client", missionId, ip, "Reason:" + reason.getReason(), "", "");
            nodeJsNotificationsService.missionBecameUnavailable(missionId, driver != null ? driver.getId() : 0);
            administrationService.clearDriverAndClientMission(mission, reason.getReason(), missionFromQueque);
            administrationService.missionCanceledStore(mission.getId(), "client", clientId, stateBeforeCanceled, reason, "");
    }





    public CorporateLoginResponse corporateLogin(String login, String password){
        Client client = clientRepository.findByCorporateLoginAndCorporatePasswordAndRegistrationStateAndMainClientNotNull(login, password, Client.RegistrationState.CONFIRMED);//findByPhoneAndPasswordAndRegistrationStateAndMainClientNotNull(login, password, Client.RegistrationState.CONFIRMED);
        if(client == null){
            throw new CustomException(2, "Login or password incorrect or user is not CONFIRMED or corporate");
        }
        if(client.getAccount().getState().equals(Account.State.BLOCKED)){
            throw new CustomException(4, "Client is BLOCKED");
        }
        if(corporateClientIsLock(client)){
            throw new CustomException(5, "Ваша учетная запись заблокирована");
        }
        if(!client.getId().equals(client.getMainClient().getId())){
            throw new CustomException(6, "Permission denied");
        }
            CorporateLoginResponse response = new CorporateLoginResponse();
            String token = TokenUtil.getMD5("fractal" + StrUtils.generateAlphaNumString(5));
            client.setToken(token);
            clientRepository.save(client);

            response.setSecurity_token(token);
            response.setMainClientId(client.getId());
            return response;
    }



    public CorporateProfileResponse corporateProfile(long clientId, String security_token){
        if(!validatorService.validateUser(clientId, security_token, 1)){
            throw new CustomException(3, "Tokens are not equals");
        }
        Client mainClient = clientRepository.findOne(clientId);
        if(mainClient.getAccount().getState().equals(Account.State.BLOCKED)){
            throw new CustomException(2, "Ваша учетная запись заблокирована");
        }

        if(mainClient.getMainClient() == null){
            throw new CustomException(2, "You are not corporate client");
        }
        if(!mainClient.getMainClient().getId().equals(mainClient.getId())){
            throw new CustomException(4, "Permission denied");
        }
        CorporateProfileResponse response = new CorporateProfileResponse();
        Money balance = mainClient.getAccount().getCorporateBalance();
        Money credit = mainClient.getAccount().getCreditLimitMoney();
        if(balance!=null){
            response.setBalance(balance.getAmount().intValue());
        }
        if(credit!=null){
            response.setCreditLimit(credit.getAmount().intValue());
        }
        response.setProfileName(mainClient.getFirstName());
        response.setCountUser(countUserInCorporateProfile(mainClient));
        return response;
    }




    public CorporateClientResponse corporateClient(Client mainClient){
        CorporateClientResponse response = new CorporateClientResponse();
        List<Client> clientList = clientRepository.findByMainClient(mainClient);
        for(Client client: clientList){
           response.getClientInfoCorporateList().add(ModelsUtils.toModelCorporate(client, corporateClientLimitRepository.findByClient(client), client.getAccount().getState().equals(Account.State.BLOCKED)?true:false)); // corporateClientIsLock(client)
        }
        return response;
    }



    public boolean corporateClientIsLock(Client client){
         return corporateClientLocksRepository.findByClientAndTimeOfUnlockIsNull(client)!=null?true:false;
    }






    @Transactional
    public List phonechangeClient(String login, String password, String newPhone) {
        List result = new ArrayList();
        Client client = clientRepository.findByPhoneAndPasswordAndRegistrationState(login, password, Client.RegistrationState.NEW);
        if(client!=null){
            // client find
            Client clientByNewPhone = clientRepository.findByPhone(newPhone);
            if(clientByNewPhone!=null){
                // есьт клиент с таким номером
                if(!clientByNewPhone.getPhone().equals(client.getPhone())){
                    result.add(false);
                    result.add("Клиент с таким номером уже существует");
                }else{
                    result.add(false);
                    result.add("Это ваш номер");
                }
            }else{
                client.setPhone(newPhone);
                clientRepository.save(client);
                result.add(true);
                result.add("Номер успешно изменен");
            }
        }else{
            result.add(false);
            result.add("Неправильный логин или пароль");
        }
        return result;
    }




    public StartWatchMissionResponse startWatchMission(String watch_security_token){ // String security_token, long clientId,
        StartWatchMissionResponse response = new StartWatchMissionResponse();
        WatchMission watchMission = watchMissionRepository.findByToken(watch_security_token);
        if(watchMission!=null){
            Mission mission = watchMission.getMission();
            if(!mission.getState().equals(Mission.State.CANCELED) && !mission.getState().equals(Mission.State.COMPLETED)){
                Driver driver = mission.getDriverInfo();
                List<DriverLocation> locations = locationRepository.findByDriverOrderByIdDesc(driver);

                if (locations != null && !locations.isEmpty()) {
                    response.setLocation(ModelsUtils.toModel(driver.getId(), locations.get(0).getLocation(), locations.get(0).getAngle()));
                    response.setDriverInfoARM(ModelsUtils.toModelARM_Short(driver));
                    response.getErrorCodeHelper().setErrorCode(0);
                    response.getErrorCodeHelper().setErrorMessage("");
                    response.setStateMission(mission.getState().toString());

                    double lat1 = mission.getLocationFrom().getLatitude();
                    double lon1 = mission.getLocationFrom().getLongitude();

                    double lat2= locations.get(0).getLocation().getLatitude();
                    double lon2= locations.get(0).getLocation().getLongitude();
                    double distance = GeoUtils.distance(lat1, lon1, lat2, lon2);
                    response.setDistanceBeforeArrival(distance);

                    if(mission.getState().equals(Mission.State.ASSIGNED)){
                        int totalMinBeforeDriverArrived = 0;
                        DateTime timeOfStarting =  mission.getTimeOfStarting();
                        DateTime timeOfAssigning =   mission.getTimeOfAssigning();
                        Minutes minutes = Minutes.minutesBetween(timeOfAssigning, timeOfStarting);
                        int diffMin = minutes.getMinutes();
                        if(diffMin<0){
                            diffMin=diffMin*(-1);
                        }
                        totalMinBeforeDriverArrived = diffMin;
                        // с учетом опоздания
                        totalMinBeforeDriverArrived+=mission.getLateDriverBookedMin();
                        response.setMinBeforeArrival(totalMinBeforeDriverArrived);
                    }
                    /* save state stat */
                    administrationService.saveMissionStateStatistic(mission, "WATCH_MISSION");
                }else{
                    response.getErrorCodeHelper().setErrorCode(4);
                    response.getErrorCodeHelper().setErrorMessage("Координаты водителя не найдены");
                }
            }else{
                response.getErrorCodeHelper().setErrorCode(1);
                response.getErrorCodeHelper().setErrorMessage("Миссия завершена!");
            }
        }else{
            response.getErrorCodeHelper().setErrorCode(2);
            response.getErrorCodeHelper().setErrorMessage("Миссия по данному ключу не найдена");
        }
        return response;
    }




    public WatchMissionResponse watchMission(long clientId, String security_token, long missionId){
        WatchMissionResponse response = new WatchMissionResponse();
        if(!validatorService.validateUser(clientId, security_token, 1)){
            response.getErrorCodeHelper().setErrorCode(3);
            response.getErrorCodeHelper().setErrorMessage("Tokens are not equal");
            return response;
        }
        Client client = clientRepository.findOne(clientId);
        Mission mission = missionRepository.findOne(missionId);
        Driver driver = mission.getDriverInfo();

        if (mission != null && driver != null) {
                if(mission.getClientInfo().getId()==client.getId()){
                    if(!mission.getState().equals(Mission.State.CANCELED) && !mission.getState().equals(Mission.State.COMPLETED)){
                        // айди клиента на миссии совпадает с текущим айди клиента
                        WatchMission watchMission = watchMissionRepository.findByMission(mission);
                        if (watchMission != null) {
                            response.getErrorCodeHelper().setErrorMessage("Наблюдение за миссией уже создано");
                            response.setUrl(watchMission.getUrl());
                            response.setWatch_security_token(watchMission.getToken());
                        } else {
                            // просмотров нет, генерим токен и возвращаем модель клиенту
                            WatchMission newWatchMission = new WatchMission();
                            newWatchMission.setDriver(driver);
                            newWatchMission.setClient(client);
                            newWatchMission.setMission(mission);
                            String genString = StrUtils.generateAlphaNumString(5);
                            String watch_security_token = TokenUtil.getMD5("fractal" + genString);
                            newWatchMission.setToken(watch_security_token);
                            String url = commonService.getPropertyValue("url_watch_mission");
                            newWatchMission.setUrl(url + "" + watch_security_token);
                            watchMissionRepository.save(newWatchMission);

                            response.setUrl(newWatchMission.getUrl());
                            response.setWatch_security_token(newWatchMission.getToken());
                        }
                    }else {
                        response.getErrorCodeHelper().setErrorMessage("Миссия завершена!!");
                    }
                }else{
                    response.getErrorCodeHelper().setErrorCode(4);
                    response.getErrorCodeHelper().setErrorMessage("Айди клиента на миссии не совпадает с текущим айди клиента");
                }

        } else {
            response.getErrorCodeHelper().setErrorCode(1);
            response.getErrorCodeHelper().setErrorMessage("Mission or Driver not found");
        }
        return response;
    }





    public TurboIncreaseResponse turboIncrease(long clientId, String security_token, long missionId, int amount, String ip){
        TurboIncreaseResponse response = new TurboIncreaseResponse();
        Client client = clientRepository.findOne(clientId);
        Mission mission = missionRepository.findOne(missionId);

        if (client != null && mission!=null) {
                if (mission.getClientInfo().getId() == client.getId()) {

                    if(mission.getState().equals(Mission.State.NEW) || mission.getState().equals(Mission.State.TURBO)){
                        // запуск автопоиска возможен только для миссии в статуес NEW
                        mission.setState(Mission.State.TURBO);
                        mission.setTimeOfRequesting(DateTimeUtils.nowNovosib_GMT6());
                        Money amountOfMoney =  Money.of(CurrencyUnit.of("RUB"), amount);
                        Money moneyExpected = mission.getStatistics().getPriceExpected();
                        mission.getStatistics().setPriceExpected(moneyExpected.plus(amountOfMoney));
                        missionRepository.save(mission);

                        TurboMission turboMission = new TurboMission();
                        turboMission.setMission(mission);

                        //turboMissionService.startTurbo(turboMission);

                        response.getErrorCodeHelper().setErrorMessage("Turbo mission start");
                    }else{
                        response.getErrorCodeHelper().setErrorCode(4);
                        response.getErrorCodeHelper().setErrorMessage("Запсук турбозаказа возможен только для миссии со статусом NEW");
                    }

                    // отсылаем с новой стоимостью в нод
                    autoSearch(clientId, security_token, missionId, ip);

                }else{
                    response.getErrorCodeHelper().setErrorCode(4);
                    response.getErrorCodeHelper().setErrorMessage("Айди клиента на миссии не совпадает с текущим айди клиента");
                }
        }else{
            response.getErrorCodeHelper().setErrorCode(1);
            response.getErrorCodeHelper().setErrorMessage("Client or Mission not found");
        }
        return response;
    }




    public TurboElapsedTimeResponse turboElapsedTime(long clientId, String security_token, long missionId){
        TurboElapsedTimeResponse response = new TurboElapsedTimeResponse();
        try {
            Client client = clientRepository.findOne(clientId);
            Mission mission = missionRepository.findOne(missionId);

            if (client != null && mission != null) {
                    if(mission.getState().equals(Mission.State.TURBO)) {
                        Seconds seconds = Seconds.secondsBetween(DateTimeUtils.nowNovosib_GMT6(), mission.getTimeOfRequesting());
                        int diffSec = seconds.getSeconds();
                        if(diffSec<0){
                            diffSec=Math.abs(diffSec);
                        }
                        int turboElapsedTime = 600-diffSec;
                        LOGGER.info("second between = "+seconds.getSeconds()+" turboElapsedTime="+turboElapsedTime);
                        response.setElapsedTime(turboElapsedTime);
                        response.getErrorCodeHelper().setErrorCode(0);
                        response.getErrorCodeHelper().setErrorMessage("");
                    }else{
                        response.getErrorCodeHelper().setErrorCode(4);
                        response.getErrorCodeHelper().setErrorMessage("Mission state is not TURBO");
                    }
            }else{
                response.getErrorCodeHelper().setErrorCode(1);
                response.getErrorCodeHelper().setErrorMessage("Client or Mission not found");
            }
        } catch (Exception g) {
            response.getErrorCodeHelper().setErrorCode(2);
            response.getErrorCodeHelper().setErrorMessage("Exception in turboElapsedTime: " + g.getMessage());
            g.printStackTrace();
        }
        return response;
    }




    public TurboIncreaseResponse turboCancel(long clientId, String security_token, long missionId){
        TurboIncreaseResponse response = new TurboIncreaseResponse();
        try {
            Client client = clientRepository.findOne(clientId);
            Mission mission = missionRepository.findOne(missionId);
            if (client != null && mission!=null) {
                    if(mission.getState().equals(Mission.State.TURBO)){
                        //turboMissionService.stopTurboAndCancelMission(missionId);
                    }else{
                        response.getErrorCodeHelper().setErrorCode(4);
                        response.getErrorCodeHelper().setErrorMessage("Остановить можно только миссию со статусом TURBO");
                    }
            }else{
                response.getErrorCodeHelper().setErrorCode(1);
                response.getErrorCodeHelper().setErrorMessage("Client or Mission not found");
            }
        }catch(Exception g){
            response.getErrorCodeHelper().setErrorCode(2);
            response.getErrorCodeHelper().setErrorMessage("Exception in turboCancel: " + g.getMessage());
        }
        return response;
    }




    public AutoSearchResponse autoSearch(long clientId, String security_token, long missionId, String ip){
        AutoSearchResponse response = new AutoSearchResponse();

            if(!validatorService.validateUser(clientId, security_token, 1)) {
                response.getErrorCodeHelper().setErrorCode(3);
                response.getErrorCodeHelper().setErrorMessage("Tokens are not equal");
                return response;
            }

                Mission mission = missionRepository.findOne(missionId);
                if(mission.getState().equals(Mission.State.NEW)){
                    // запуск автопоиска возможен только для миссии в статуес NEW

                    mission.setState(Mission.State.AUTO_SEARCH);
                    mission.setTimeOfRequesting(DateTimeUtils.nowNovosib_GMT6());
                    missionRepository.save(mission);

                    /* save state stat */
                    administrationService.saveMissionStateStatistic(mission, "AUTO_SEARCH");

                    AutoSearchMission autoSearchMission = new AutoSearchMission();
                    autoSearchMission.setMission(mission);

                    autoSearchService.startAutosearch(autoSearchMission);

                    grayLogService.sendToGrayLog(clientId, 0, 0, "startAutoSearch", "Client", missionId, ip, "", "", "");
                }else{
                    response.getErrorCodeHelper().setErrorCode(4);
                    response.getErrorCodeHelper().setErrorMessage("Запсук автопоиска возможен только для миссии со статусом NEW");
                }

        return response;
    }




    public CallMeDriverResponse callMeDriver(long clientId, long driverId, String token){
        CallMeDriverResponse response = new CallMeDriverResponse();
        Client client = clientRepository.findOne(clientId);
        Driver driver = driverRepository.findOne(driverId);
        if(client!=null && driver!=null){
            if (client.getToken() != null && client.getToken().equals(token)) {
                  nodeJsNotificationsService.driverCustomMessage(driverId, "Клиент не может с вами связаться. Позвоните ему: "+client.getPhone());
                  serviceSMSNotification.sendCustomSMS(driver.getPhone(),"Клиент не может с вами связаться. Позвоните ему: "+client.getPhone(), "");
                  response.setSuccess(true);
            }
        }
        return response;
    }



    public String tariffRestriction(int tariff){
        String result = "";
        List<TariffRestriction> tariffRestrictions = tariffRestrictionRepository.findByTariffNameAndActiveAndHoliday(AutoClass.getByValue(tariff).name(), true, false);
        if(tariffRestrictions!=null && !tariffRestrictions.isEmpty() && tariffRestrictions.size()==2){
            String textHtml = commonService.getPropertyValue("text_restriction_html");
            TariffRestriction first = tariffRestrictions.get(0);
            TariffRestriction second = tariffRestrictions.get(1);
            result = String.format(textHtml, MathUtil.splitDigit(first.getStartHours()) + ":" + MathUtil.splitDigit(first.getStartMinutes()), MathUtil.splitDigit(first.getEndHours()) + ":" + MathUtil.splitDigit(first.getEndMinutes()), MathUtil.splitDigit(second.getStartHours()) + ":" + MathUtil.splitDigit(second.getStartMinutes()), MathUtil.splitDigit(second.getEndHours()) + ":" + MathUtil.splitDigit(second.getEndMinutes()));
        }else{
            LOGGER.info("tariffRestrictions is empty OR !=2");
        }
        return result;
    }




    public MarkMissionAsDeleteResponse markMissionAsDelete(String security_token, long clientId, List<Long> listMissionMarkAsDelete){
        MarkMissionAsDeleteResponse response = new MarkMissionAsDeleteResponse();
        Client client = clientRepository.findOne(clientId);

        if (client != null) {
            if (client.getToken() != null && client.getToken().equals(security_token)) {
                Set<Mission> missionSet = new HashSet();
                for(Long missionId :listMissionMarkAsDelete){
                    Mission mission = missionRepository.findOne(missionId);
                    mission.setStatusDelete(true);
                    missionSet.add(mission);
                }
                missionRepository.save(missionSet);
                response.getErrorCodeHelper().setErrorCode(0);
                response.getErrorCodeHelper().setErrorMessage("");
            }else{
                response.getErrorCodeHelper().setErrorCode(3);
                response.getErrorCodeHelper().setErrorMessage("Tokens are not equal");
            }
        }else{
            response.getErrorCodeHelper().setErrorCode(1);
            response.getErrorCodeHelper().setErrorMessage("Клиент не найден");
        }
        return response;
    }






    //@Transactional
    public FindAddressResponse findAddressByMask(String security_token, String addressMask) {
        FindAddressResponse response = new FindAddressResponse();
        if(StringUtils.isEmpty(addressMask)){
            return response;
        }


        int countSymbols =  addressMask.length();
        addressMask = StrUtils.capitalize(addressMask);

        Session session = entityManager.unwrap(Session.class);

        int numPage = 1;
        int pageSize = 20;

        Map parameters = new HashMap();

        StringBuffer queryBuf;
        queryBuf = new StringBuffer("from DGISAddress address ");
        if(countSymbols<= 3){
            queryBuf = new StringBuffer("select * from dgis_grym_street street ");
        } else if(countSymbols > 3){
            queryBuf.append(" join address.dgisStreet street");
            queryBuf.append(" join address.dgisMapBuilding building");
            queryBuf.append(" or building.name REGEXP " + "'%" + addressMask + "%'");
            //queryBuf.append(" or building.city REGEXP " + "'%" + addressMask + "%'");
            //queryBuf.append(" or building.purpose REGEXP " + "'%" + addressMask + "%'");
        }

        queryBuf.append(" where 1=1 ");
        queryBuf.append(" and name REGEXP '"+addressMask+"'");
        queryBuf.append(" order by city_idx asc, name limit 0,"+pageSize);

        String hqlQuery = queryBuf.toString();
        LOGGER.info("hqlQuery = "+hqlQuery);
        Query query = session.createSQLQuery(hqlQuery);

//        Query queryCount = session.createQuery("select count(m) "+hqlQuery);
//        Iterator iter = parameters.keySet().iterator();
//        while (iter.hasNext()) {
//            String name = (String) iter.next();
//            Object value = parameters.get(name);
//            query.setParameter(name,value);
//            queryCount.setParameter(name,value);
//        }

        query.setFirstResult((numPage - 1) * pageSize);
        query.setMaxResults(pageSize);

        LOGGER.info("Fractal: Result query: " + query.getQueryString());

        List<Object> result = (List<Object>) query.list();
        Iterator itr = result.iterator();
        while(itr.hasNext() ){
            DGISStreet street = null;
            Object object = itr.next();

            if(object instanceof DGISStreet){
                street = (DGISStreet) object;
            } else if(object instanceof Object[]){
                Object[] obj = (Object[]) object;
                for(int i=0; i<obj.length; i++){
                    LOGGER.info("obj: "+obj[i]);
                }
                //street = (DGISStreet)obj[0];
            }

            /*
            String res = ""+dgisAddress.getDgisMapBuilding().getCity()+
                    ", "+dgisAddress.getDgisStreet().getNameStreet()+
                    ", "+dgisAddress.getNumber()+
                    ", тип: "+dgisAddress.getDgisMapBuilding().getPurpose()+
                    ", lat/lon: "+dgisAddress.getDgisMapBuilding().getGeoX()+" "+dgisAddress.getDgisMapBuilding().getGeoY();
            */

            //LOGGER.info("object: "+object+" street = "+street);
            //response.getDgisStreetList().add(res);
        }

        /*
        Criteria criteria = session.createCriteria(DGISAddress.class);

        criteria.createAlias("dgisStreet", "street");
        criteria.createAlias("dgisMapBuilding", "building");

        Criterion rest1= Restrictions.or(Restrictions.ilike("street.nameStreet", "%" + addressMask + "%"), Restrictions.ilike("building.name", "%" + addressMask + "%"));
        Criterion rest2= Restrictions.or(Restrictions.ilike("building.city", "%" + addressMask + "%"), Restrictions.ilike("building.purpose", "%" + addressMask + "%"));
        Criterion rest3= Restrictions.or(rest1, rest2);
        Criterion rest4= Restrictions.ilike("number", "%" + addressMask + "%");

        criteria.add(Restrictions.or(rest3, rest4));

        criteria.addOrder(Order.asc("street.cityIndex"));

        List<DGISAddress> addressList = criteria.list();

        FindAddressResponse response = new FindAddressResponse();

        for(DGISAddress dgisAddress :addressList){
            String res = ""+dgisAddress.getDgisMapBuilding().getCity()+
                    ", "+dgisAddress.getDgisStreet().getNameStreet()+
                    ", "+dgisAddress.getNumber()+
                    ", тип: "+dgisAddress.getDgisMapBuilding().getPurpose()+
                    ", lat/lon: "+dgisAddress.getDgisMapBuilding().getGeoX()+" "+dgisAddress.getDgisMapBuilding().getGeoY();
            LOGGER.info(res);
            response.getDgisStreetList().add(res);
        }

        LOGGER.info("Address mask = "+addressMask);


        */



        /*
        List<DGISStreet> streetList = dgisStreetRepository.findByNameStreetContainingIgnoreCase(addressMask);
        //Iterable streetList = dgisStreetRepository.findAll();
        //List<DGISStreet> myList = Lists.newArrayList(streetList);
             if(streetList!=null){
                 for(DGISStreet street :streetList){
                     List<DGISAddress> addressList = street.getDgisAddressList();
                     for(DGISAddress address :addressList){
                         if(address.getDgisMapBuilding()!=null){
                             LOGGER.info(""+address.getDgisMapBuilding().getCity()+", "+street.getNameStreet()+", "+address.getNumber()+" lat/lon: "+address.getDgisMapBuilding().getGeoX()+", "+address.getDgisMapBuilding().getGeoY());
                             LOGGER.info();
                         }
                     }
                 }
             }
        */
        return response;
    }







    public UpdateCorporateClientLimitResponse updateCorporateClientLimit(String security_token, LimitInfo info) {
        if(!validatorService.validateUser(info.getMainClientId(), security_token, 1)) {
            throw new CustomException(3, "Tokens are not equal");
        }
        Client mainClient = clientRepository.findOne(info.getMainClientId());
        if(mainClient.getAccount().getState().equals(Account.State.BLOCKED)){
            throw new CustomException(2, "Ваша учетная запись заблокирована");
        }
        if(mainClient.getMainClient() == null){
            throw new CustomException(4, "You are not corporate client");
        }
        if(!mainClient.getMainClient().getId().equals(mainClient.getId())){
            throw new CustomException(5, "Permission denied");
        }
        Client subClient = clientRepository.findOne(info.getClientId());
        if(subClient == null){
            throw new CustomException(6, String.format("Client id %s not found", info.getClientId()));
        }
        if(subClient.getMainClient()==null || !subClient.getMainClient().getId().equals(mainClient.getId())){
            throw new CustomException(7, String.format("Несоответствие mainClient"));
        }
        UpdateCorporateClientLimitResponse response = new UpdateCorporateClientLimitResponse();

        if(info.getLimitId() == 0){
            // add
            CorporateClientLimit corporateClientLimit = corporateClientLimitRepository.findByClientAndPeriod(subClient, CorporateClientLimit.Period.getByValue(info.getTypePeriod()));
            if(corporateClientLimit != null){
                throw new CustomException(8, "Для данного клиента уже установлен лимит на этот период");
            }

            CorporateClientLimit newClientLimit = new CorporateClientLimit();
            newClientLimit.setClient(subClient);
            //newClientLimit.setMainClient(mainClient);
            newClientLimit.setLimitAmount(info.getLimitAmount());
            newClientLimit.setUpdateTime(DateTimeUtils.nowNovosib_GMT6());
            newClientLimit.setPeriod(CorporateClientLimit.Period.getByValue(info.getTypePeriod()));
            corporateClientLimitRepository.save(newClientLimit);
            response.setErrorMessage("Added!");
        }else{

            CorporateClientLimit corporateLimit = corporateClientLimitRepository.findOne(info.getLimitId());
            if(corporateLimit == null){
              throw new CustomException(9, String.format("CorporateClientLimit id %s not found", info.getLimitId()));
            }
            //corporateLimit.setClient(subClient);
            corporateLimit.setLimitAmount(info.getLimitAmount());
            corporateLimit.setUpdateTime(DateTimeUtils.nowNovosib_GMT6());
            //corporateLimit.setPeriod(CorporateClientLimit.Period.getByValue(info.getTypePeriod()));
            //corporateLimit.setCorporateAccount(mainClient);
            corporateClientLimitRepository.save(corporateLimit);
            response.setErrorMessage("Updated!");
        }
        return  response;
    }





    public BlockCorporateClientResponse blockClient(Client client, String reason){
        BlockCorporateClientResponse response= new BlockCorporateClientResponse();
        if(!client.getAccount().getState().equals(Account.State.BLOCKED)){
            // блокируем
                CorporateClientLocks clientLocks = new CorporateClientLocks();
                clientLocks.setTimeOfLock(DateTimeUtils.nowNovosib_GMT6());
                clientLocks.setReason(reason);
                clientLocks.setClient(client);
                corporateClientLocksRepository.save(clientLocks);

                Account subClientAccount = client.getAccount();
                subClientAccount.setState(Account.State.BLOCKED);
                accountRepository.save(subClientAccount);
                response.setErrorMessage("Клиент заблокирован");
        }else{
            // статус = заблокирован, значит разблокируем
            CorporateClientLocks clientLocks = corporateClientLocksRepository.findByClientAndTimeOfUnlockIsNull(client);
            if(clientLocks!=null){
                clientLocks.setTimeOfUnlock(DateTimeUtils.nowNovosib_GMT6());
                corporateClientLocksRepository.save(clientLocks);
            }
            Account subClientAccount = client.getAccount();
            subClientAccount.setState(Account.State.ACTIVE);
            accountRepository.save(subClientAccount);
            response.setErrorMessage("Клиент разблокирован");
        }
        return response;
    }




    public CorporateStatResponse corporateStat(long mainClientId, String security_token, long clientId, long startTime, long endTime, int numPage, int pageSize){
        if(!validatorService.validateUser(mainClientId, security_token, 1)) {
            throw new CustomException(3, "Tokens are not equal");
        }
        Client client = clientRepository.findOne(mainClientId);
        if(client.getAccount().getState().equals(Account.State.BLOCKED)){
            throw new CustomException(2, "Ваша учетная запись заблокирована");
        }
        if(client.getMainClient() == null){
            throw new CustomException(4, "You are not corporate client");
        }
        if(!client.getMainClient().getId().equals(client.getId())){
            throw new CustomException(5, "Permission denied");
        }
        if(clientId!=0){
            Client subClient = clientRepository.findOne(clientId);
              if(subClient == null){
                  throw new CustomException(6, "Sub client not found");
              }
              if(subClient.getMainClient()==null){
                  throw new CustomException(7, "You are not corporate client");
              }
              if(!subClient.getMainClient().getId().equals(client.getId())){
                  throw new CustomException(8, String.format("Несоответствие mainClient"));
              }
        }
        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(Mission.class);
        Criteria criteriaCount = session.createCriteria(Mission.class);

        criteria.createAlias("clientInfo", "client");
        criteriaCount.createAlias("clientInfo", "client");

        if(clientId!=0){
            criteria.add(Restrictions.eq("client.id", clientId));
            criteriaCount.add(Restrictions.eq("client.id", clientId));
        }
        if(startTime != 0 && endTime != 0){
            criteria.add(Restrictions.ge("timeOfRequesting", new DateTime(new Date(startTime)).withTimeAtStartOfDay()));
            criteria.add(Restrictions.lt("timeOfRequesting", new DateTime(new Date(endTime)).withTimeAtStartOfDay().plusDays(1)));
            criteriaCount.add(Restrictions.ge("timeOfRequesting", new DateTime(new Date(startTime)).withTimeAtStartOfDay()));
            criteriaCount.add(Restrictions.lt("timeOfRequesting", new DateTime(new Date(endTime)).withTimeAtStartOfDay().plusDays(1)));
        }else if(startTime != 0 && endTime == 0){
            criteria.add(Restrictions.ge("timeOfRequesting", new DateTime(new Date(startTime)).withTimeAtStartOfDay()));
            criteriaCount.add(Restrictions.ge("timeOfRequesting", new DateTime(new Date(startTime)).withTimeAtStartOfDay()));
        }else if(startTime == 0 && endTime != 0){
            criteria.add(Restrictions.lt("timeOfRequesting", new DateTime(new Date(endTime)).withTimeAtStartOfDay().plusDays(1)));
            criteriaCount.add(Restrictions.lt("timeOfRequesting", new DateTime(new Date(endTime)).withTimeAtStartOfDay().plusDays(1)));
        }

        criteria.add(Restrictions.eq("client.mainClient.id", mainClientId));
        criteriaCount.add(Restrictions.eq("client.mainClient.id", mainClientId));
        criteria.add(Restrictions.eq("state", Mission.State.COMPLETED));
        criteria.add(Restrictions.or(Restrictions.eq("paymentType", PaymentType.CORPORATE_CARD), Restrictions.eq("paymentType", PaymentType.CORPORATE_BILL), Restrictions.eq("paymentType", PaymentType.CORPORATE_CASH)));
        criteriaCount.add(Restrictions.eq("state", Mission.State.COMPLETED));
        criteriaCount.add(Restrictions.or(Restrictions.eq("paymentType", PaymentType.CORPORATE_CARD), Restrictions.eq("paymentType", PaymentType.CORPORATE_BILL), Restrictions.eq("paymentType", PaymentType.CORPORATE_CASH)));
        criteria.addOrder(Order.desc("timeOfRequesting"));

        CorporateStatResponse response = new CorporateStatResponse();

        criteriaCount.setProjection(Projections.rowCount());
        int total = Integer.valueOf(criteriaCount.uniqueResult().toString());
        response.setTotalItems(total);

        int lastPageNumber = ((total / pageSize) ); // +1
        response.setLastPageNumber(lastPageNumber);

        List<Mission> missionsBeforePaging = criteria.list();

        int allSum = 0;
        for(Mission mission :missionsBeforePaging){
            allSum += mission.getStatistics().getPriceInFact()!=null ? mission.getStatistics().getPriceInFact().getAmount().intValue() : 0;
        }
        response.setAllSum(allSum);

        criteria.setFirstResult((numPage - 1) * pageSize);
        criteria.setMaxResults(pageSize);

        List<Mission> missions = criteria.list();
        MissionRate missionRate = missionRatesRepository.findOne((long) 1);
        int sum = 0;
        for(Mission mission: missions){
            MissionInfoCorporate missionInfo = ModelsUtils.toModelMissionInfoCorporate(mission);
            missionInfo.setServicePriceInfos(getServicePrice(mission));
            missionInfo.setPausesMin(commonService.getPausesInMinutesCount(mission));
            missionInfo.setAutoClassRateInfo(billingService.getAutoClassRateInfoV2(missionRate.getAutoClassPrices(), mission.getAutoClass().getValue()));
            sum += mission.getStatistics().getPriceInFact().getAmount().intValue();

            CommonService.TimeWaitClientUtil timeWaitClientUtil = commonService.freeTimeLeftWaitClient(mission);

            int countWaitPaymentMinutes = 0;

            if(timeWaitClientUtil.getFreeTimeInFact()<0){
                countWaitPaymentMinutes = Math.abs(timeWaitClientUtil.getFreeTimeInFact());
            }
            missionInfo.setMinPaymentWait(countWaitPaymentMinutes);
            missionInfo.setCostPaymentWait(commonService.pricePaymentWaiting(mission.getAutoClass().getValue(), missionInfo.getMinPaymentWait()));

            response.getMissionInfos().add(missionInfo);
        }
        response.setSum(sum);
        return response;
    }




    private List<ServicePriceInfo> getServicePrice(Mission mission){
        List<ServicePriceInfo> servicePriceInfos =new ArrayList<>();
        if(mission.getStatistics().getServicesInFact()!=null && !mission.getStatistics().getServicesInFact().isEmpty()){
            servicePriceInfos = ModelsUtils.toModel(servicesRepository.findByServiceIn(mission.getStatistics().getServicesInFact()), commonService.checkServiceByVersionApp(mission.getClientInfo().getVersionApp(), mission.getClientInfo().getDeviceType()));
        }
        return  servicePriceInfos;
    }



    private int countUserInCorporateProfile(Client corporate){
        return clientRepository.countUserInCorporateProfile(corporate);
    }





    @Transactional
    public ActivatePromoResponse activatePromoCode(long clientId, String security_token, String promoText){
        Client client = clientRepository.findOne(clientId);
        if (client == null) {
            throw new CustomException(1, "Client not found");
        }
        if (client.getToken() == null || (client.getToken() != null && !client.getToken().equals(security_token))) {
            throw new CustomException(3, "Token not equals");
        }
        ActivatePromoResponse response = new ActivatePromoResponse();

        PromoCodeExclusive promoCodeExclusive = promoCodeExclusiveRepository.findByPromoCodeIgnoreCase(promoText); // findPromoCodeExclusive / .toLowerCase()

        String mess = "Тариф «Бонус» добавлен в ваш список тарифов";

        if(promoCodeExclusive!=null){
            if(promoCodeExclusive.getUsedCount() >= promoCodeExclusive.getAvailableUsedCount()){
                throw new CustomException(8, "Превышено предельно допустимое количество активаций");
            }
            // активация тарифа BONUS
            PrivateTariff privateTariff = privateTariffRepository.findByClientAndTariffName(client, AutoClass.BONUS.name()); // AndActive , , Boolean.TRUE

            if(privateTariff == null){
                privateTariff = new PrivateTariff();
            }else{
                if(privateTariff.getActive()){
                    throw new CustomException(7, "Вы активировали данный промокод ранее");
                }
            }
            privateTariff.setClient(client);
            privateTariff.setTariffName(AutoClass.BONUS.name());
            privateTariff.setActive(Boolean.TRUE);
            privateTariff.setPromoExclusive(promoCodeExclusive);
            privateTariffRepository.save(privateTariff);

            response.setTitle("Поздравляем!");
            response.setMessage(mess);
            promoCodeExclusive.setUsedCount(promoCodeExclusive.getUsedCount()+1);
            promoCodeExclusiveRepository.save(promoCodeExclusive);

        }else{
            PromoCodes promoCode =  promoCodeRepository.findByPromoCodeIgnoreCase(promoText);
              if(promoCode == null){
                  throw new CustomException(2, "Промокод не найден");
              }
                  transferBonusesToClient(client, promoCode);
                  response.setSum(promoCode.getAmount());
        }
          return response;
    }



    private int getCountClientActivatePromoCode(long clientId){
        List<ClientActivatedPromoCodes> clientActivatedPromoCodesList = clientActivatedPromoCodesRepository.findByClientId(clientId);
        if(clientActivatedPromoCodesList!=null){
            return clientActivatedPromoCodesList.size();
        }else{
            return 0;
        }
    }



    @Transactional
    private void transferBonusesToClient(Client client, PromoCodes promoCode) {
        // активация с проверкой срока жизни
        if(promoCode.getFromId()==null) {
            throw new CustomException(8,"Не задан fromId");
        }
            Money money = Money.of(MoneyUtils.BONUSES_CURRENCY, promoCode.getAmount());

            long expirationDateMillis = promoCode.getExpirationDate();
            DateTime expirationDate = new DateTime(expirationDateMillis);
            DateTime nowDateTime = DateTimeUtils.nowNovosib_GMT6();

            Minutes minutes = Minutes.minutesBetween(nowDateTime, expirationDate);
            int usedCount = promoCode.getUsedCount(); // сколько раз активировали
            LOGGER.info("promoCode: " + promoCode);
            LOGGER.info("promoCode.getAvailableUsedCount(): " + promoCode.getAvailableUsedCount());
            int availableUsedCount = promoCode.getAvailableUsedCount(); // сколько раз вообще его можно активировать
            int diffBetweenUsedCountAndAvailableCount = availableUsedCount-usedCount;

            // сколько можно использовать клиенту вообще
            ClientAvailableActivatePromoCode clientAvailableActivatePromoCodeObj = clientAvailableActivatePromoCodeRepository.findByClientId(client.getId());
            // сколько уже активировал вообще
            int clientCountActivatedPromo = getCountClientActivatePromoCode(client.getId());

            if(minutes.getMinutes()<0) {
               throw new CustomException(9,"Промокод просрочен");
            }

            if(promoCode.getFromId().equals(client.getId())){
               throw new CustomException(10,"Активация для самого себя запрещена!");
            }
            if(diffBetweenUsedCountAndAvailableCount<0){
               throw new CustomException(11,"Количество активаций превышает предельно допустимое число");
            }
                            String propClientAvailableActivatePromoCode = commonService.getPropertyValue("client_available_activate_promo_code");
                            int count = 0;
                            if(clientAvailableActivatePromoCodeObj!=null){
                                    count = clientAvailableActivatePromoCodeObj.getAvailableActivateCount();
                            }else{
                                if(propClientAvailableActivatePromoCode!=null){
                                    count = Integer.parseInt(propClientAvailableActivatePromoCode);
                                }
                            }
                            if(count==0) {
                                throw new CustomException(12,"Для клиента не задано предельно допустимое кол-во активаций промокодов");
                            }
                            if(clientCountActivatedPromo>=count) {
                                throw new CustomException(13,"Превышен лимит предельно допустимого числа активаций промокодов");
                            }

                            ClientActivatedPromoCodes clientActivatedPromo = clientActivatedPromoCodesRepository.findByPromoCodeIdAndClientId(promoCode.getId(), client.getId());

                            if(clientActivatedPromo!=null){
                                throw new CustomException(13,"Вы уже активировали данный промокод ранее");
                            }

                            administrationService.operationWithBonusesClient(client.getId(), null, money.getAmount().intValue(), 3, null, "", null);

                            promoCode.setToId(client.getId());
                            usedCount = usedCount+1;
                            promoCode.setUsedCount(usedCount);
                            promoCodeRepository.save(promoCode);

                            ClientActivatedPromoCodes clientActivatedPromoCodes  = new ClientActivatedPromoCodes();
                            clientActivatedPromoCodes.setClientId(client.getId());
                            clientActivatedPromoCodes.setPromoCodeId(promoCode.getId());
                            clientActivatedPromoCodes.setDateOfUsed(DateTimeUtils.nowNovosib_GMT6().getMillis());
                            clientActivatedPromoCodesRepository.save(clientActivatedPromoCodes);

                            mongoDBServices.createEvent(2, ""+client.getId(), 3, 0, "billingActivate", "", "");
    }




    public void autoSearchAnswer(long missionId, boolean yes, String ip){
         Mission mission = missionRepository.findOne(missionId);
         if(mission == null){
             throw new CustomException(1, String.format("Миссия с id=%s не найдена", missionId));
         }
         if(mission.getState().equals(Mission.State.CANCELED)){
             throw new CustomException(2, String.format("Миссия с id=%s отменена!", missionId));
         }
         if(yes){
             // клиент согласен перейти в режим AUTO_SEARCH
             mission.setState(Mission.State.AUTO_SEARCH);
             missionRepository.save(mission);
             /* после подтверждения перехода в режим автопоиска клиент должен дергать метод, который будет показывать результаты поиска */
         }else{
             findDriversService.removeThreadSearchFromQueue(missionId, 0);
             cancelMissionByClient(mission.getId(), mission.getClientInfo().getId(), reasonRepository.findOne(2L), ip);
         }
    }


/*
StartSearchDrivers startSearch;
        try {
            startSearch = ((StartSearchDrivers) scheduledFuture.get());
            LOGGER.info("1");
            while(!scheduledFuture.isCancelled()){
                LOGGER.info("2");
                results = startSearch.getPrintInfo();
                LOGGER.info("3");
            }
        }catch(java.util.concurrent.CancellationException exc){
            results = "Поток остановлен";
            LOGGER.info(results);
        }
 */



    public CurrentStateMissionResponse currentStateMission(long missionId) throws ExecutionException, InterruptedException {
         Mission mission = missionRepository.findOne(missionId);
         if(mission == null){
             throw new CustomException(1, String.format("Миссия id=%s не найдена", missionId));
         }
            CurrentStateMissionResponse response = new CurrentStateMissionResponse();
            response.setMissionInfo(ModelsUtils.toModel(mission));
            response.setIsLate(mission.getIsLate());

        List<Driver> drivers = new ArrayList<>();
        Driver drv = mission.getDriverInfo();
        if(drv!=null){
           drivers.add(drv);
        }else{
            drivers.addAll(driverRepository.findByState(Driver.State.AVAILABLE));
        }
        List<Mission> activeMissionList = administrationService.getActiveMissionList(mission.getClientInfo().getMultipleMission());
        if(CollectionUtils.isEmpty(activeMissionList)){
            response.setMultipleEmpty(true);
        }
        LOGGER.info("Multiple collection is empty: "+ CollectionUtils.isEmpty(activeMissionList)+" collection="+activeMissionList);

        if(CollectionUtils.isEmpty(drivers)){
            return response;
        }

        for (Driver driver : drivers) {
            List<DriverLocation> locations = locationRepository.findByDriverOrderByIdDesc(driver);
            if (locations != null && !locations.isEmpty()) {

                int angle = locations.get(0).getAngle()!=null?locations.get(0).getAngle():0;
                Location driverLocation = locations.get(0).getLocation();

                double distance = GeoUtils.distance(
                        mission.getLocationFrom().getLatitude(), mission.getLocationFrom().getLongitude(),
                        driverLocation.getLatitude(), driverLocation.getLongitude());

                if (distance <= 7) {
                    boolean occupied = driver.getCurrentMission() != null;
                    if (driver.getState().equals(Driver.State.BUSY)) {
                        occupied = true;
                    }
                    ItemLocation itemLocation = ModelsUtils.toModel(driver.getId(), driverLocation.getLatitude(), driverLocation.getLongitude(), occupied); // driverLocation.getCity(), driverLocation.getRegion()
                    itemLocation.setAngle(angle);
                    response.getDriverCurrentLocations().add(itemLocation);
                }
            }
        }
              return response;
    }



    /*
    while(findDrivers.getQueueMission().containsKey(missionId)){
             try {
                 LOGGER.info("555555555555555555555");
                 ScheduledFuture<StartSearchDrivers> scheduledFuture = (ScheduledFuture) findDrivers.getQueueMission().get(missionId);
                 LOGGER.info("6666666666666666666666");
                 if (scheduledFuture.isDone()){
                     StartSearchDrivers startSearch = scheduledFuture.get();
                     LOGGER.info("7777777777777777777777");
                     missionState = startSearch.getPrintInfo();
                   }else{
                     LOGGER.info("not is done - search продолжается!!!");
                   }
             }catch(java.util.concurrent.CancellationException exc){
                 missionState = "ПОТОК ОСТАНОВЛЕН!!!!!!!!!!!!!!!!!!!";
             } catch (ExecutionException ee) {
                 LOGGER.info("excxxxx = "+ee.getCause());
             } catch (InterruptedException ie) {
                 Thread.currentThread().interrupt(); // ignore/reset
                 LOGGER.info("ie gggggggg = "+ie.getCause());
             }
             LOGGER.debug("missionState ========================" + missionState);
             return missionState;
         }
            //Mission mission = missionRepository.findOne(missionId);
            //missionState = mission.getState().name();

            return missionState;
     */




    public AutoSearchResponse autoSearchCancel(long clientId, String security_token, long missionId, String ip){
        AutoSearchResponse response = new AutoSearchResponse();

        if(!validatorService.validateUser(clientId, security_token, 1)) {
            response.getErrorCodeHelper().setErrorCode(3);
            response.getErrorCodeHelper().setErrorMessage("Клиент не найден");
            return response;
        }
            Client client = clientRepository.findOne(clientId);
            Mission mission = missionRepository.findOne(missionId);
            if (mission != null) {
                String stateBeforeCanceled = mission.getState().name();
                    if(mission.getState().equals(Mission.State.AUTO_SEARCH)){
                        autoSearchService.stopAutosearchAndCancelMission(missionId);
                        client.setMission(null);
                        clientRepository.save(client);
                        administrationService.missionCanceledStore(mission.getId(), "client", clientId, stateBeforeCanceled, reasonRepository.findOne(2L), "");
                        grayLogService.sendToGrayLog(clientId, 0, 0, "autoSearchCancel", "Client", missionId, ip, "", "", "");
                        response.getErrorCodeHelper().setErrorMessage("Auto search canceled completed!!!");
                    }else{
                        response.getErrorCodeHelper().setErrorCode(4);
                        response.getErrorCodeHelper().setErrorMessage("Остановить можно только миссию со статусом AUTO_SEARCH");
                    }
            }else{
                response.getErrorCodeHelper().setErrorCode(1);
                response.getErrorCodeHelper().setErrorMessage("Mission not found");
            }
        return response;
    }




    @Transactional(propagation= Propagation.REQUIRED)  //(rollbackForClassName = {"CustomException"} , propagation = Propagation.REQUIRED)
    public CreateMultipleMissionResponse createMultipleMission(List<MultipleMissionInfo> multipleMissionInfos, Client client) throws ParseException, IOException, JSONException, ExecutionException, InterruptedException {
        CreateMultipleMissionResponse response = new CreateMultipleMissionResponse();
        MultipleMission multipleMission = new MultipleMission();

        for(MultipleMissionInfo info: multipleMissionInfos){
            MissionInfo missionInfo =  info.getMissionInfo();
            ErrorCodeHelper errorCodeHelper = checkMission(missionInfo, client);
            if(errorCodeHelper.getErrorCode()!=0){
                throw new CustomException(errorCodeHelper.getErrorCode(), errorCodeHelper.getErrorMessage());
            }
            for(int i=1; i<= info.getAutoCount(); i++){
                Mission mission = administrationService.createMissionWithMultipleSupport(missionInfo, info.getTimeOfStarting()); //createMissionWithStringTimeStarting(missionInfo, info.getTimeOfStarting());
                  if(!mission.isBooked()) {
                      multipleMission.getMultipleMissions().add(mission);
                  }
            }
        }

        if(!CollectionUtils.isEmpty(multipleMission.getMultipleMissions())){
            multipleMission = multipleMissionRepository.save(multipleMission);
            client.setMultipleMission(multipleMission);
            for(Mission mission: multipleMission.getMultipleMissions()){
                response.getMissionInfos().add(ModelsUtils.toModel(mission));
            }
                // оповещаем водителей о том, что поступил множественный заказ
                findDriversService.findMultipleDrivers(multipleMission);
        }
           return response;
    }




    public DeleteClientCardResponse deleteClientCard(long clientCardId, String token){
        DeleteClientCardResponse response =new DeleteClientCardResponse();
        ClientCard clientCard = clientCardRepository.findOne(clientCardId);
        if(clientCard!=null) {
            Client client = clientCard.getClient();
            if(client!= null){
                if (client.getToken() != null && client.getToken().equals(token)) {
                    clientCard.setStatusDelete(true);
                    clientCardRepository.save(clientCard);
                    if (clientCard.getActive().equals(Boolean.TRUE)) {
                        // active card
                        LOGGER.info("active card");
                        setAnotherClientCardActiveIfExist(clientCard.getClient(), clientCard);
                    }
                    response.setResult(true);
                }else{
                    LOGGER.info("Tokens are not equals");
                }
            }
        }else{
            LOGGER.debug("ClientCard id {} not found", clientCardId);
        }
        return response;
    }



    public void setAnotherClientCardActiveIfExist(Client client, ClientCard clientCard){
        if(client!=null){
            List<ClientCard> clientCardList = clientCardRepository.findByClientAndStatusDelete(client, false);
            if(clientCardList!= null){
                if(!clientCardList.isEmpty()){
                    ClientCard clientCardActive = clientCardList.get(0);
                    clientCardActive.setActive(true);
                    clientCard.setActive(false);
                    clientCardRepository.save(clientCardActive);
                }else{
                    clientCard.setActive(false);
                    clientCardRepository.save(clientCard);
                }
            }
        }
    }



    public UpdateClientCardResponse updateClientCard(ClientCardInfo clientCardInfo, String token){
        // clientId / cardId / active = true
        UpdateClientCardResponse response  = new UpdateClientCardResponse();
        if(clientCardInfo!=null){
            Client client = clientRepository.findOne(clientCardInfo.getClientId());
            if(client!=null) {
                if (client.getToken() != null && client.getToken().equals(token)) {
                    ClientCard clientCard = clientCardRepository.findOne(clientCardInfo.getId());
                    if (client != null && clientCard != null) {
                        List<ClientCard> clientCardList = clientCardRepository.findByClient(client);
                        setAllClientCardNotActive(clientCardList);
                        clientCardRepository.save(ModelsUtils.fromModel(clientCardInfo, clientCard));
                        response.setResult(true);
                    }
                }else {
                    LOGGER.info("Tokens are not equal");
                }
            }
        }
        return response;
    }



    public void setAllClientCardNotActive(List<ClientCard> clientCardList){
        if(!clientCardList.isEmpty()){
            for(ClientCard clientCard: clientCardList){
                clientCard.setActive(false);
                clientCardRepository.save(clientCard);
            }
        }
    }


    public ClientCard getActiveClientCard(Client client){
        ClientCard clientCardActive = clientCardRepository.findByClientAndStatusDeleteAndActive(client, false, true);
        return clientCardActive;
    }



    public ClientCardListAndroidResponse getClientCardListAndroid(long clientId, String token){
        ClientCardListAndroidResponse response =new ClientCardListAndroidResponse();
        Client client = clientRepository.findOne(clientId);
        if(client!=null){
            if (client.getToken() != null && client.getToken().equals(token)) {
                List<ClientCard> clientCardList = clientCardRepository.findByClientAndStatusDelete(client, false);
                if(clientCardList!=null && !clientCardList.isEmpty()){
                    for(ClientCard clientCard: clientCardList){
                        if(clientCard.getBindingId()!=null){
                            ClientCardInfoAndroid clientCardInfoAndroid = new ClientCardInfoAndroid();
                            clientCardInfoAndroid.setTypeCard(administrationService.getCCType(clientCard.getPan()));
                            clientCardInfoAndroid = ModelsUtils.toModelAndroid(clientCard, clientCardInfoAndroid);
                            response.getClientCardInfoList().add(clientCardInfoAndroid);
                        }
                    }
                }
            }
        }
        return response;
    }



    public ClientCardListResponse getClientCardList(long clientId, String token){
        ClientCardListResponse response =new ClientCardListResponse();
        Client client = clientRepository.findOne(clientId);

        if(client!=null){
            if (client.getToken() != null && client.getToken().equals(token)) {
                List<ClientCard> clientCardList = clientCardRepository.findByClientAndStatusDelete(client, false);

                if(clientCardList!=null && !clientCardList.isEmpty()){
                    for(ClientCard clientCard: clientCardList){
                        if(clientCard.getBindingId()!=null){
                            ClientCardInfo clientCardInfo = ModelsUtils.toModel(clientCard);
                            response.getClientCardInfoList().add(clientCardInfo);
                        }
                    }
                }
            }
        }
        return response;
    }




    public long missionCompleteCount(Client client){
        int pageSize = Integer.parseInt(commonService.getPropertyValue("mission_complete_limit"));
        long count = (long)entityManager.createQuery("select count(m) from Mission m where m.clientInfo = ?1 and m.state = ?2 and m.testOrder = ?3 and m.statusDelete = ?4")
                .setParameter(1, client)
                .setParameter(2, Mission.State.COMPLETED)
                .setParameter(3, Boolean.FALSE)
                .setParameter(4, Boolean.FALSE)
                .getSingleResult(); // .setMaxResults(pageSize)
        return count;
    }



    public static class RestrictionHelper{
        private boolean restriction = false;
        private String message="";
        private String htmlMessage="";

        public String getHtmlMessage() {
            return htmlMessage;
        }

        public void setHtmlMessage(String htmlMessage) {
            this.htmlMessage = htmlMessage;
        }

        public boolean isRestriction() {
            return restriction;
        }

        public void setRestriction(boolean restriction) {
            this.restriction = restriction;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }


}
