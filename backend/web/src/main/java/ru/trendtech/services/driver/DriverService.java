package ru.trendtech.services.driver;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Minutes;
import org.joda.time.Seconds;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import ru.trendtech.common.mobileexchange.model.common.*;
import ru.trendtech.common.mobileexchange.model.common.push.DeviceInfoModel;
import ru.trendtech.common.mobileexchange.model.common.rates.MissionRateInfoV2;
import ru.trendtech.common.mobileexchange.model.common.states.ServerStateInfo;
import ru.trendtech.common.mobileexchange.model.driver.*;
import ru.trendtech.common.mobileexchange.model.web.DriverRequisiteResponse;
import ru.trendtech.domain.*;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.Properties;
import ru.trendtech.domain.billing.Account;
import ru.trendtech.domain.billing.PaymentType;
import ru.trendtech.domain.mongo.DriverActivity;
import ru.trendtech.models.ModelsUtils;
import ru.trendtech.repositories.*;
import ru.trendtech.repositories.billing.AccountRepository;
import ru.trendtech.repositories.mongo.DriverActivityRepository;
import ru.trendtech.services.MongoDBServices;
import ru.trendtech.services.common.CommonService;
import ru.trendtech.services.driver.search.FindDriversService;
import ru.trendtech.services.logging.GrayLogService;
import ru.trendtech.services.notifications.NotificationsService;
import ru.trendtech.services.notifications.node.NodeJsService;
import ru.trendtech.services.administration.AdministrationService;
import ru.trendtech.services.billing.BillingService;
import ru.trendtech.services.push.devices.DevicesService;
import ru.trendtech.services.resources.ProfilesResourcesService;
import ru.trendtech.services.sms.ServiceSMSNotification;
import ru.trendtech.utils.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by petr on 30.01.2015.
 */

@Service
@Transactional
public class DriverService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DriverService.class);
    @Autowired
    private DriverPeriodWorkRepository driverPeriodWorkRepository;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private AdministrationService administrationService;
    @Autowired
    private MongoDBServices mongoDBServices;
    @Autowired
    private DriverRequisiteRepository driverRequisiteRepository;
    @Autowired
    private BanPeriodRestDriverRepository banPeriodRestDriverRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private CashRepository driverCashRepository;
    @Autowired
    private AccountRepository accountRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private ReadingNewsRepository readingNewsRepository;
    @Autowired
    private NodeJsService nodeJsService;
    @Autowired
    private MissionRepository missionRepository;
    @Autowired
    private ReasonMissionDeclinedRepository reasonMissionDeclinedRepository;
    @Autowired
    private DriverActivityRepository driverActivityRepository;
    @Autowired
    private QueueMissionRepository queueMissionRepository;
    @Autowired
    private DriverSettingRepository driverSettingRepository;
    @Autowired
    private DriverTimeWorkRepository driverTimeWorkRepository;
    @Autowired
    private ProfilesResourcesService profilesResourcesService;
    @Autowired
    private BillingService billingService;
    @Autowired
    private AssistantRepository assistantRepository;
    @Autowired
    private TaxoparkPartnersRepository taxoparkPartnersRepository;
    @Autowired
    @Qualifier("exclusive")
    private DevicesService devicesService;
    @Autowired
    private DriverCarPhotosRepository driverCarPhotosRepository;
    @Autowired
    private ServiceSMSNotification serviceSMSNotification;
    @Autowired
    @Qualifier("node")
    private NotificationsService nodeJsNotificationsService;
    @Autowired
    @Qualifier("push")
    private NotificationsService app42PushNotificationService;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private TransactRepository transactRepository;
    @Autowired
    private AdditionalServiceRepository additionalServiceRepository;
    @Autowired
    private ReasonRepository reasonRepository;
    @Autowired
    private FindDriversService findDriversService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private GrayLogService grayLogService;
    @Autowired
    private PropertiesRepository propertiesRepository;




    public LoginDriverResponse loginDriverV2(String login, String password, DeviceInfoModel infoModel, String versionApp, String ip) {
        LoginDriverResponse loginDriverResponse = new LoginDriverResponse();
        DriverInfo driverInfo;
        Driver driver = driverRepository.findByLoginAndPassword(login, password);

        if (driver != null) {
            // if dirver admin status is BLOCKED - goodbye
            if(driver.getAdministrativeStatus().equals(Driver.AdministrativeStatus.BLOCKED)){
                throw new CustomException(1, "Ваша учетная запись заблокирована!");
            }
            driver.setVersionApp(versionApp);

            if(!driver.getState().equals(Driver.State.BUSY)){
                driver.setState(Driver.State.AVAILABLE);
            }

            String genString = StrUtils.generateAlphaNumString(5);
            String security_token = TokenUtil.getMD5("fractal" + genString);
            driver.setToken(security_token);

            driverRepository.save(driver);
            driverInfo = ModelsUtils.toModel(driver);

            loginDriverResponse.setDriverInfo(driverInfo);
            loginDriverResponse.setSecurity_token(security_token);

            mongoDBServices.createEvent(1, "" + driver.getId(), 3, 0, "driverLoginV2", "", "");
            grayLogService.sendToGrayLog(0, driver.getId(), 0, "login", "Driver", 0, ip, "Version app:"+versionApp, "", "");
        }
        return loginDriverResponse;
    }




    public void cancelMissionByDriver(long missionId, String comment, long driverId, long reasonId, String ip) throws InterruptedException, ExecutionException, JSONException {
        Mission mission = missionRepository.findOne(missionId);
        Driver driver = driverRepository.findOne(driverId);
        Reason reason = reasonRepository.findOne(reasonId);
        if(mission == null){
            throw new CustomException(1, "Миссия не найдена");
        }
        if(driver == null){
            throw new CustomException(2, "Водитель не найден");
        }
        if(reason == null){
            throw new CustomException(4, "Причина не найдена");
        }
        if(mission.getDriverInfo() == null){
            throw new CustomException(5, "У текущей миссии нет водителя");
        }
        if(mission.getDriverInfo().getId() != driverId){
            throw new CustomException(6, "Миссия не соответсвует водителю");
        }

        /*
          todo: поставить проверку на то, были ли уже отказы от данного заказа реальными водителями
          есди да - то мы должны просто отменить заказ и отослать соответствующее сообщение пользователю
           List<MissionCanceled> missionsCanceled = missionCanceledRepository.findByMissionId(mission.getId());
        if(!CollectionUtils.isEmpty(missionsCanceled)){
            MissionCanceled missionCanceled = missionsCanceled.get(0);
            missionCanceled.
        }
        */

        /* очищаем информацию о миссии на днном водителе */
        clearDriverMission(driver);

        /* сохраняем информацию об отмене с указанием причины отмены */
        administrationService.missionCanceledStore(missionId, "driver", driverId, mission.getState().name(), reason, comment);

        /* увеличиваем сумму доплаты на сумму указанную в причине */
        LOGGER.info("Before sum increase = "+mission.getStatistics().getSumIncrease());
        mission.getStatistics().setSumIncrease(mission.getStatistics().getSumIncrease().plus(reason.getClientBonus()));
        missionRepository.save(mission);
        LOGGER.info("After sum increase = " + mission.getStatistics().getSumIncrease());

        /* запускаем поиск следующего водителя */
        findDriversService.findDriversWhenDriverCanceledMission(mission);
    }




    public void transactTest(){
        accountRepository.save(new Account());
        transactRepository.save(new Transact(-100000L));
        throw new CustomException(-5, "Transaction rollBack");
    }





    private void clearDriverMission(Driver driver) {
        driver.setCurrentMission(null);
        driverRepository.save(driver);
        /* clear missionId in DriverLocation */
        DriverLocation location = locationRepository.findByDriverId(driver.getId());
        location.setMission(null);
        locationRepository.save(location);
    }


    public RegisterDriverResponse registerDriver(DriverInfoARM info, DeviceInfoModel deviceInfoModel){
        RegisterDriverResponse response = new RegisterDriverResponse();
        String phone = PhoneUtils.normalizeNumber(info.getPhone());

        Driver driver;
        if (phone != null) {
            driver = ModelsUtils.fromModelARM(info);

            if(info.getTaxoparkPartnersInfo()!=null){
                TaxoparkPartners taxoparkPartners = taxoparkPartnersRepository.findOne(info.getTaxoparkPartnersInfo().getId());
                driver.setTaxoparkPartners(taxoparkPartners);
            }
            if(info.getAssistantInfo()!=null){
                Assistant assistant = assistantRepository.findOne(info.getAssistantInfo().getId());
                driver.setAssistant(assistant);
            }

            if(info.getDriverSettingInfo() != null){
                DriverSetting driverSetting = driverSettingRepository.save(ModelsUtils.fromModel(info.getDriverSettingInfo(), driver, null));
                driverSettingRepository.save(driverSetting);
                driver.setDriverSetting(driverSetting);
            }


            if (info.getLogin() != null && !info.getLogin().isEmpty()) {
                if(driverRepository.findByLogin(info.getLogin())!=null){
                    throw new CustomException(4,"В базе есть водитель с таким логином");
                }else{
                    driver.setLogin(info.getLogin());
                }
            } else {
                driver.setLogin(generateLogin());
            }
            if (deviceInfoModel != null) {
                driver.getDevices().clear();
                DeviceInfo deviceInfo = devicesService.register(driver.getDevices(), deviceInfoModel);

                if (deviceInfo != null) {
                    driver.getDevices().add(deviceInfo);
                    Account account = billingService.createDriverAccountWithRubles(0);
                    driver.setAccount(account);
                    driver = driverRepository.save(driver);

                    /* привязываем планшет и роутер*/
                    if(info.getTabletInfo()!=null){
                        administrationService.linkTabletToDriver(driver.getId(), info.getTabletInfo().getId());
                    }
                    if(info.getRouterInfo()!=null){
                        administrationService.linkRouterToDriver(driver.getId(), info.getRouterInfo().getId());
                    }
                    /* */


                    /* КОСТЫЛЬ ДЛЯ ДИМЫ ЧТОБЫ РАБОТАЛИ ЗП ВОДИТЕЛИ !!!!!! */
                    DriverLocation driverLocation = locationRepository.findByDriverId(driver.getId());
                    if (driverLocation == null) {
                        driverLocation = new DriverLocation();
                        driverLocation.setDriver(driver);
                        locationRepository.save(driverLocation);
                    }
                    /* КОСТЫЛЬ END */

                    String picture = info.getPhotoPicture();
                    if (picture != null && !StringUtils.isEmpty(picture)) {
                        String pictureUrl = profilesResourcesService.saveDriverPicture(picture, driver.getId(), false);
                        driver.setPhotoUrl(pictureUrl);
                        driverRepository.save(driver);
                    }
                    String pictureByVersion = info.getPhotoUrlByVersion();
                    if (!StringUtils.isEmpty(pictureByVersion)) {
                        String pictureUrlByVersion = profilesResourcesService.saveDriverPicture(pictureByVersion, driver.getId(), true);
                        driver.setPhotoUrlByVersion(pictureUrlByVersion);
                        driverRepository.save(driver);
                    }
                    List<String> photosCarsPictures = info.getPhotosCarsPictures();
                    if (photosCarsPictures != null && !photosCarsPictures.isEmpty()) {
                        List<String> pictureUrls = profilesResourcesService.saveAutoPictures(photosCarsPictures, driver.getId());
                        driver.getPhotosCarsUrl().addAll(pictureUrls);
                        driverRepository.save(driver);
                    }

                    /* функционал для загрузки фоток авто в отдельную таблицу для новых версий клиентского ПО*/
                    List<DriverCarPhotosInfo> driverCarPhotosInfos = info.getDriverCarPhotosInfos();
                    if (!CollectionUtils.isEmpty(driverCarPhotosInfos)) {
                        driverCarPhotosInfos = profilesResourcesService.saveAutoPicturesByVersionApp(driverCarPhotosInfos, driver.getId());
                        List<DriverCarPhotos> driverCarPhotosSave = new ArrayList<>();
                        for (DriverCarPhotosInfo carInfo : driverCarPhotosInfos) {
                            driverCarPhotosSave.add(ModelsUtils.fromModel(carInfo.getPhotoUrl(), driver));// administrationService.versionApp(carInfo.getAndroidMinVersion().getId())
                        }
                        if(!CollectionUtils.isEmpty(driverCarPhotosSave)) {
                            driverCarPhotosRepository.save(driverCarPhotosSave);
                        }
                    }
                    /* */

                    response.setDriverId(driver.getId());
                    response.setLogin(driver.getLogin());
                } else {
                    throw new CustomException(6,"Не удалось создать deviceId для водителя");
                }
            } else {
                throw new CustomException(5,"DeviceInfoModel не может быть null");
            }
        } else {
            throw new CustomException(2,"Некорректный номер телефона");
        }
        return response;
    }




    public FreeWaitTimeResponse freeWaitTime(long missionId){
        Mission mission = missionRepository.findOne(missionId);
        if(mission == null){
            throw new CustomException(2, "Mission not found");
        }
        FreeWaitTimeResponse response = new FreeWaitTimeResponse();
        CommonService.TimeWaitClientUtil timeWaitClientUtil = commonService.freeTimeLeftWaitClient(mission);
        response.setFreeTimeInFact(timeWaitClientUtil.getFreeTimeInFact());
        response.setFreeTime(timeWaitClientUtil.getFreeTime());
        response.setHowLongWaitInFact(timeWaitClientUtil.getHowLongWaitInFact());
              return response;
    }





    public void driverArrived(long driverId, long missionId) {
        Driver driver = driverRepository.findOne(driverId);
        if(driver == null){
            throw new CustomException(1, "Водитель не найден");
        }
        Mission mission = missionRepository.findOne(missionId);
        if(mission == null){
            throw new CustomException(2, "Миссия не найдена");
        }
        if(!driver.getCurrentMission().getId().equals(mission.getId())){
            throw new CustomException(4, "Миссия на водителе не соответствует текущей миссии");
        }
        if(mission.getState().equals(Mission.State.CANCELED)){
            /*
              думаю эту фигню можно выпилить
              notificationsService.driverCustomMessage(driverId, "Заказ был отменен! Свяжитесь с диспетчером.");
            */
            throw new CustomException(5, "Заказ был отменен! Свяжитесь с диспетчером.");
        }
            Client client = mission.getClientInfo();
            mission.setTimeOfArriving(DateTimeUtils.nowNovosib_GMT6());
            mission.setState(Mission.State.ARRIVED);
            driver.setCurrentMission(mission);
            client.setMission(mission);
            clientRepository.save(client);
            driverRepository.save(driver);
            missionRepository.save(mission);
        if(!mission.isBooked()){
            serviceSMSNotification.driverArrived(mission.getClientInfo(), driver, "", mission.getStatistics().getPriceExpected().getAmount().intValue());
        } else {
            serviceSMSNotification.driverBookedArrived(mission.getClientInfo(), driver, "");
        }
            nodeJsNotificationsService.driverArrived(mission, mission.isBooked(), "");

            String message = commonService.getPropertyValue("driver_arrived_push_mess");
            app42PushNotificationService.driverArrived(mission, mission.isBooked(), message);
    }






    /* расчитать время прибытия водителя к клиенту (оптимальное) */
    public TimeOfArrivalResponse calculateTimeOfArrival(long missionId, long driverId, String ip, String security_token) throws IOException {
        TimeOfArrivalResponse response = new TimeOfArrivalResponse();
        Driver driver = driverRepository.findOne(driverId);
        //if(!commonService.isOkSecurityToken(driver, security_token, 2)){
            //throw new CustomException(1, "Водитель не найден");
        //}

        Mission mission = missionRepository.findOne(missionId);
        if(mission == null){
            throw new CustomException(2, "Миссия не найдена");
        }

        boolean timeIsAfter = mission.isTimeIsAfter();
        MissionInfo missionInfo = ModelsUtils.toModel(mission);

        /* отдавать единые номера  */
        missionInfo = (MissionInfo)commonService.commonPhoneNumber(missionInfo);

        if(mission.getDriverInfo()!=null && mission.getDriverInfo().isTypeX()){
            // если водитель назначен и тип водителя фантом, возвращаем стейт миссии нью и поле driverInfo_id=null
            missionInfo.setMissionState("NEW");
            missionInfo.setDriverInfo(null);
        }

        response.setMissionInfo(missionInfo);

        int on = Integer.parseInt(commonService.getPropertyValue("calculate_time_duration"));
        if(on == 0){
            return response;
        }

        /*
           расчетное время прибытия [2gis]
           если расчетное время больше чем время указанное в заказе на "через Х минут" - шлем водителю отказ в виде стейт миссион NONE
        */
        int timeOfArrival = commonService.calculateArrivalTime(mission, null, driver);
        if(timeIsAfter){
            /* клиент сделал заказ на через afterMin минут */
            int afterMin = Integer.parseInt(commonService.getPropertyValue("after_min_booked"));
            timeOfArrival = afterMin;
            if(timeOfArrival > afterMin){
                // расчетное время прибытия большем чем время(afterMin) на которое клиент сделал заказ
                missionInfo = response.getMissionInfo();
                missionInfo.setMissionState("NONE");
                response.setMissionInfo(missionInfo);
            }
        }
           response.setArrivalTime(timeOfArrival);

        /* если для водителя задано увеличение стоимости заказа */
        DriverSetting driverSetting = driverSettingRepository.findByDriver(driver);
        if(driverSetting!=null && driverSetting.getSumIncrease()>0){
            missionInfo = response.getMissionInfo();
            double sumIncreaseByMission = missionInfo.getSumIncrease();
            double sumIncreaseByDriver = (double)driverSetting.getSumIncrease();
            double resultSum = sumIncreaseByMission+sumIncreaseByDriver;
            missionInfo.setSumIncrease(resultSum);
            response.setMissionInfo(missionInfo);
        }

        /* записать в mongoDB факт снятия текущего заказа с водителя */
        mongoDBServices.createEvent(1, "" + driverId, 0, missionId, "calculateTimeOfArrival", "timeOfArrival:" + timeOfArrival, "");
        grayLogService.sendToGrayLog(mission.getClientInfo().getId(), driverId, 0, "calculateTimeOfArrival", "Driver", missionId, ip, "timeOfArrival:" + timeOfArrival, "", "");
            return response;
    }




    public DriverRequisiteResponseV2 requisiteV2(long driverId, String security_token){
        DriverRequisiteResponseV2 response = new DriverRequisiteResponseV2();
        Driver driver = driverRepository.findOne(driverId);

        //if(!commonService.isOkSecurityToken(driver, security_token, 2)){
            // throw new CustomException(1, "Водитель не найден");
        //}

        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(DriverRequisite.class);

        if(driverId!=0){
            criteria.add(Restrictions.eq("driver", driver));
        }
            //criteria.add(Restrictions.eq("salaryPriority", 0)); // 0 - штатный водитель, 1 - не штатный
            criteria.add(Restrictions.eq("active", true));
            criteria.add(Restrictions.eq("typeDismissal", 1));
            criteria.addOrder(Order.desc("id"));

        List<DriverRequisite> listDriverRequisite = criteria.list();

        if(listDriverRequisite!=null && !listDriverRequisite.isEmpty()) {
            for (DriverRequisite driverRequisite : listDriverRequisite) {
                response.getDriverRequisiteInfos().add(ModelsUtils.toModelDRVreqV2(driverRequisite));
            }
        }
        return response;
    }


    public DriverRequisiteResponse requisite(long driverId){
        DriverRequisiteResponse response = new DriverRequisiteResponse();
        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(DriverRequisite.class);

        if(driverId!=0){
            Driver driver = driverRepository.findOne(driverId);
            if(driver == null){
                throw new CustomException(4,"Driver not found");
            }
            criteria.add(Restrictions.eq("driver", driver));
        }
        //criteria.add(Restrictions.eq("salaryPriority", 0)); // 0 - штатный водитель, 1 - не штатный
        criteria.add(Restrictions.eq("active", true));
        criteria.add(Restrictions.eq("typeDismissal", 1));
        criteria.addOrder(Order.desc("id"));

        List<DriverRequisite> listDriverRequisite = criteria.list();

        if(listDriverRequisite!=null && !listDriverRequisite.isEmpty()) {
            for (DriverRequisite driverRequisite : listDriverRequisite) {
                response.getDriverRequisiteInfos().add(ModelsUtils.toModel(driverRequisite));
            }
        }
        return response;
    }





    public LogoutResponse.Details logoutDriver(long driverId, boolean force, DeviceInfoModel deviceInfo) {
        LogoutResponse.Details details = new LogoutResponse.Details();
        /*
        Driver driver = driverRepository.findOne(driverId);
        if (driver != null) {
            driver.setDeclinedDrivers("");
            Mission mission = driver.getCurrentMission();
            if (mission != null) {
                cancelMissionByDriver(mission.getId(), "logout", 1, driver.getId());
            }

            //if(!driver.getState().equals(Driver.State.BUSY)){
            driver.setState(Driver.State.OFFLINE);
            //}

            // Валя предлагает обнулять при logout-е
            // driver.setCurrentMission(null);

            devicesService.unregister(deviceInfo);
            driverRepository.save(driver);
            details.setCompleted(true);
        }
        */
        return details;
    }



    /*
      помещаем заказ в очередь, как только водитель завершает заказ, из этой очереди берется следующий ближайший по времени заказ и отображается на экране у водителя
      когда у водилы будет возможность брать заказы на разное время, необходимо ставить проверку на то,
      нет ли на нем уже взятых заказов на это же время
    */
    public PutMissionInQueueResponse putMissionInQueue(long driverId, long missionId, int arrivalTime, String ip, String security_token) {
        PutMissionInQueueResponse response = new PutMissionInQueueResponse();
        Driver driver = driverRepository.findOne(driverId);
        //if(!commonService.isOkSecurityToken(driver, security_token, 2)){
            //throw new CustomException(1, "Водитель не найден");
        //}

        Mission mission = missionRepository.findOne(missionId);
        if(mission ==  null){
           throw new CustomException(1, "Миссия не найдена");
        }

        LOGGER.info("putMissionInQueue: driverId = "+driverId+" missionId = "+missionId+" arrivalTime = "+arrivalTime+" missionState = "+mission.getState().name());

        if(queueMissionRepository.findByMission(mission) != null || mission.getState().equals(Mission.State.CANCELED)){
            if(!StringUtils.isEmpty(driver.getVersionApp())){
                int versionNumber = commonService.getIntValueVersion(driver.getVersionApp());
                if(versionNumber >0 && versionNumber < 169){
                    nodeJsNotificationsService.driverCustomMessage(driverId , "Заказ отменен или взят другим водителем");
                }
            }
                   throw new CustomException(2, "Заказ отменен или взят другим водителем");
        }

        QueueMission queueMissionInDB = getFirstMissionFromQueue(driverId);

        if(queueMissionInDB != null){
            throw new CustomException(4, "У вас есть следующий заказ");
        }

        LOGGER.info("putMissionInQueue: ОТДАЕМ ЗАКАЗ ВОДИТЕЛЮ!");

        DateTime timeOfAssigning = DateTimeUtils.nowNovosib_GMT6();

        /* сохраняем в очередь */
        QueueMission queueMission = new QueueMission();
        queueMission.setDriver(driver);
        queueMission.setMission(mission);
        queueMission.setTimeOfAssigning(timeOfAssigning);
        queueMissionRepository.save(queueMission);


        /* меняем статус миссии на клиенте и записываем доп инфу [время через сколько будет, время ассайна]*/
        mission.setLateDriverBookedMin(arrivalTime);
        mission.setTimeOfAssigning(timeOfAssigning);
        mission.setDriverInfo(driver);
        mission.setState(Mission.State.ASSIGNED);
        mission.setTaxopark(driver.getTaxoparkPartners());
        missionRepository.save(mission);

        DriverLocation location = locationRepository.findByDriverId(driverId);

        if(driver.getCurrentMission() == null){
            driver.setCurrentMission(mission);
            driverRepository.save(driver);
            nodeJsNotificationsService.missionAssigneMsg(mission, driverId, arrivalTime, location, "");

            location.setMission(mission);
            locationRepository.save(location);
        }else{
            nodeJsNotificationsService.driverAssigneSecondOrder(mission, driverId, arrivalTime, location);
        }

        /* push */
        String message = commonService.getPropertyValue("driver_assigne_push_mess");
        app42PushNotificationService.missionAssigneMsg(mission, driverId, 0, null, message);

        /* log */
        mongoDBServices.createEvent(1, ""+driverId, 3, missionId, "putMissionInQueue", "clientId:"+mission.getClientInfo().getId(), "");
        grayLogService.sendToGrayLog(mission.getClientInfo().getId(), driverId, 0, "putMissionInQueue", "Driver", missionId, ip, "", "", "");

        return response;
    }




    public QueueMission getFirstMissionFromQueue(long driverId){
        Session session = entityManager.unwrap(Session.class);
        Criteria queueCriteria = session.createCriteria(QueueMission.class);
        queueCriteria.createAlias("mission", "m");
        queueCriteria.add(Restrictions.eq("driver.id", driverId));
        queueCriteria.add(Restrictions.eq("m.driverInfo.id", driverId));
        queueCriteria.add(Restrictions.eq("m.state", Mission.State.ASSIGNED));
        queueCriteria.addOrder(Order.asc("timeOfAssigning"));
        QueueMission queueMission = null;
        List<QueueMission> queueMissions = queueCriteria.list();
        if(!CollectionUtils.isEmpty(queueMissions)){
            queueMission = queueMissions.get(0);
        }
        return queueMission;
    }





    public GetMissionFromQueueResponse getMissionFromQueue(long driverId, String ip, String security_token){
        /* взять самую ближайшую незавершенную миссию из очереди для отображения на экране водителя [со статусом ассигнед]*/
        Driver driver = driverRepository.findOne(driverId);

        //if(!commonService.isOkSecurityToken(driver, security_token, 2)){
            //throw new CustomException(1, "Водитель не найден");
        //}

        GetMissionFromQueueResponse response = new GetMissionFromQueueResponse();

        QueueMission queueMission = getFirstMissionFromQueue(driverId);

        if(queueMission!=null){
            Mission mission = missionRepository.findOne(queueMission.getMission().getId());

            if(!EnumSet.of(Mission.State.ASSIGNED).contains(mission.getState())){
                throw new CustomException(2, "Заказ отменен");
            }

            MissionInfo missionInfo = ModelsUtils.toModel(mission);
            /* отдавать единые номера  */
            missionInfo = (MissionInfo)commonService.commonPhoneNumber(missionInfo);

            /* если водитель ЗП - доплаты нет */
            List<DriverRequisite> driverRequisites = driverRequisiteRepository.findByDriverAndActive(driver, true);
            if(!CollectionUtils.isEmpty(driverRequisites)){
                DriverRequisite driverRequisite = driverRequisites.get(0);
                if(driverRequisite.getTypeSalary() == 0 || driverRequisite.getTypeSalary() == 2){
                    missionInfo.setSumIncrease(0);
                }
            }

            response.setMissionInfo(missionInfo);

            /* для текущего водителя нужно прописать эту миссию в driver_location */
            DriverLocation driverLocation = locationRepository.findByDriverId(driverId);
            driverLocation.setMission(mission);
            locationRepository.save(driverLocation);
            response.setErrorMessage("Миссия запущена!");

            driver.setCurrentMission(mission);
            driverRepository.save(driver);

            /* log */
            mongoDBServices.createEvent(1, "" + driverId, 3, queueMission.getMission().getId(), "getMissionFromQueue", "clientId:" + queueMission.getMission().getClientInfo().getId(), "");
            grayLogService.sendToGrayLog(mission.getClientInfo().getId(), driverId, 0, "getMissionFromQueue", "Driver", queueMission.getMission().getId(), ip, "", "", "");
        }else{
            response.setErrorMessage("Очередь пуста");
        }
        return response;
    }



    // взять миссию от которой отказался другой водитель по причине (дтп, поломка)
    public TakeMissionDeclinedResponse takeMissionDeclined(long missionId, long driverIdResponded, int arrivalTime) throws JSONException {
        /*
        mission_declined_responded:
        driverId - кому послать
        driverId_responded - кто откликнулся
        arrivalTime - время "буду через"
        */
        Driver driverResponded = driverRepository.findOne(driverIdResponded);
        Mission mission = missionRepository.findOne(missionId);
        if(driverResponded == null || mission == null){
            throw new CustomException(1, "Водитель или миссия не найдены");
        }
        if(mission.getDriverInfo() == null){
            throw new CustomException(7, "По данной миссии не найден водитель");
        }
        if(driverResponded.getCurrentMission() != null){
            throw new CustomException(2, "У вас уже имеется миссия");
        }

        /* Здесь валится exception т.к. строк по миссии может быть больше 1 [ПОФИКСИТЬ] */
        List<ReasonMissionDeclined> reasonMissionDeclinedList = reasonMissionDeclinedRepository.findByMission(mission);

        if(CollectionUtils.isEmpty(reasonMissionDeclinedList)){
            throw new CustomException(6, "Не найдена причина отказа от миссии");
        }
        ReasonMissionDeclined reasonMissionDeclined = reasonMissionDeclinedList.get(0);
        if(reasonMissionDeclined.getTakeDriver() != null){
            if(reasonMissionDeclined.getTakeDriver().getId().equals(driverIdResponded)){
                throw new CustomException(4, "Вы уже взяли данную миссию");
            }else{
                throw new CustomException(5, "Миссию взял другой водитель");
            }
        }

        TakeMissionDeclinedResponse response = new TakeMissionDeclinedResponse();

        /* очищаю миссию на том, кто отказался от нее */
        Driver whoDeclined = mission.getDriverInfo();
        whoDeclined.setCurrentMission(null);
        driverRepository.save(whoDeclined);

        DriverLocation locWhoDeclined =  locationRepository.findByDriverId(whoDeclined.getId());
        locWhoDeclined.setMission(null);
        locationRepository.save(locWhoDeclined);
        /* */

        /* для нового водителя устанавливаю принятую им миссию */
        mission.setDriverInfo(driverResponded);
        mission.setLateDriverBookedMin(arrivalTime);
        mission.setState(Mission.State.ASSIGNED);
        mission.setTimeOfAssigning(DateTimeUtils.nowNovosib_GMT6());
        missionRepository.save(mission);

        driverResponded.setCurrentMission(mission);
        driverRepository.save(driverResponded);
        /* */

        DriverLocation locationRespondedDriver = locationRepository.findByDriverId(driverIdResponded);
        locationRespondedDriver.setMission(mission);
        locationRepository.save(locationRespondedDriver);

        /* устанавливаю кто взял миссию в таблице с причинами отказов */
        reasonMissionDeclined.setTakeDriver(driverResponded);
        reasonMissionDeclinedRepository.save(reasonMissionDeclined);

        /* оповещаю водителя который отказался от заказа, что заказ принят другим водителем*/
        JSONObject json = new JSONObject();
        json.put("driverId", mission.getDriverInfo().getId());
        json.put("driverId_responded", driverIdResponded);
        json.put("arrivalTime", arrivalTime);
        json.put("missionId", missionId);
        nodeJsService.notified("mission_declined_responded", json);


        /* оповещаем клиента о том, что сменился водитель */
        JSONObject notifClient = new JSONObject();
        notifClient.put("missionId", mission.getId());
        notifClient.put("arrivalTime", arrivalTime);
        double distance = GeoUtils.distance(mission.getLocationFrom().getLatitude(), mission.getLocationFrom().getLongitude(), locationRespondedDriver.getLocation().getLatitude(), locationRespondedDriver.getLocation().getLongitude());
        notifClient.put("distance", distance);
        nodeJsService.notified("driver_changed", notifClient);

         return response;
    }



    private void finedDriver(long driverId, long missionId, int sumFine){
           administrationService.updateDriverBalanceSystem(driverId, missionId, -sumFine, 1);
    }




    /* оповещаем клиента о том, что на миссии появился новый водитель: driver */
    private void driverChange(Mission mission, Driver driver) throws JSONException, IOException {
        DriverLocation locationRespondedDriver = locationRepository.findByDriverId(driver.getId());
        JSONObject notifClient = new JSONObject();
        notifClient.put("missionId", mission.getId());
        notifClient.put("arrivalTime", commonService.calculateArrivalTime(mission, null, driver));
        double distance = GeoUtils.distance(mission.getLocationFrom().getLatitude(), mission.getLocationFrom().getLongitude(), locationRespondedDriver.getLocation().getLatitude(), locationRespondedDriver.getLocation().getLongitude());
        notifClient.put("distance", distance);
        nodeJsService.notified("driver_changed", notifClient);
    }











    // НОВАЯ СХЕМА ОТКАЗА ОТ ЗАКАЗА
    public void missionDeclined(long driverId, long missionId, int typeReason, String customReason){
        Driver driver = driverRepository.findOne(driverId);
        Mission mission = missionRepository.findOne(missionId);
        if(driver == null || mission == null){
            throw new CustomException(1, "Водитель или миссия не найдены");
        }
        if(mission.getDriverInfo() == null){
            throw new CustomException(2, "Миссия без водителя");
        }
        if(!driver.getId().equals(mission.getDriverInfo().getId())){
            throw new CustomException(4, "Водитель на миссии не соответствует текущему водителю");
        }
        /*
        UNKNOWN(0),
        CRASH(1),
        CLIENT_NOT_WANT_WAIT(2),
        WANT_SMOKE(3),
        CLIENT_NOT_RESPONDE(4),
        CLIENT_NOT_GO_OUT(5),
         */
        ReasonMissionDeclined.TypeReason reason =  ReasonMissionDeclined.TypeReason.getByValue(typeReason);
        if(EnumSet.of(Mission.State.IN_TRIP, Mission.State.IN_TRIP_PAUSED, Mission.State.IN_TRIP_END, Mission.State.ARRIVED).contains(reason)){

        }
        if(mission.getState().equals(Mission.State.ASSIGNED)){
            throw new CustomException(8, "Для передачи заказа свяжитесь, пожалуйста, с диспетчером");
        }
    }







    public MissionDeclinedResponse missionDeclinedByReason(long driverId, long missionId, int typeReason) throws JSONException {
        Driver driver = driverRepository.findOne(driverId);
        Mission mission = missionRepository.findOne(missionId);
        if(driver == null || mission == null){
             throw new CustomException(1, "Водитель или миссия не найдены");
        }
        if(mission.getState().equals(Mission.State.ASSIGNED)){
            throw new CustomException(8, "Для передачи заказа свяжитесь, пожалуйста, с диспетчером");
        }
        if(mission.getDriverInfo() == null){
             throw new CustomException(2, "Миссия без водителя");
        }
        if(!driver.getId().equals(mission.getDriverInfo().getId())){
            throw new CustomException(4, "Водитель на миссии не соответствует текущему водителю");
        }
        MissionDeclinedResponse response = new MissionDeclinedResponse();

        int searchRadius = Integer.parseInt(commonService.getPropertyValue("radius_for_declined_mission"));
        ReasonMissionDeclined reasonMissionDeclined = reasonMissionDeclinedRepository.findByMissionAndDriver(mission, driver);

        if(reasonMissionDeclined == null){
            reasonMissionDeclined = new ReasonMissionDeclined();
        }
        reasonMissionDeclined.setDriver(driver);
        reasonMissionDeclined.setMission(mission);
        reasonMissionDeclined.setTimeOfDeclined(DateTimeUtils.nowNovosib_GMT6());
        reasonMissionDeclined.setTypeReason(ReasonMissionDeclined.TypeReason.getByValue(typeReason));
        reasonMissionDeclinedRepository.save(reasonMissionDeclined);

        if(ReasonMissionDeclined.TypeReason.getByValue(typeReason).equals(ReasonMissionDeclined.TypeReason.CLIENT_NOT_WANT_WAIT)){
            // клиент не захотел ждать, значит просто отменяем заказ
            administrationService.cancelMissionByAdmin(missionId, "Client does not want wait", 2, 0, null, "server");
              return response;
        }

        List<Driver> freeDrivers = driverRepository.findByStateAndCurrentMissionIsNull(Driver.State.AVAILABLE);
        if(CollectionUtils.isEmpty(freeDrivers)){
             throw new CustomException(7, "Нет свободных водителей");
        }

        /* теперь смотрим водитель отказывается от заказа с клиентом, или без */
        JSONObject json = new JSONObject();
        json.put("missionId", missionId);

        if(EnumSet.of(Mission.State.IN_TRIP, Mission.State.IN_TRIP_PAUSED, Mission.State.IN_TRIP_END, Mission.State.ARRIVED).contains(mission.getState())){
             // водитель отказывается от заказа с клиентом в машине
             DriverLocation currentDriverLocation = locationRepository.findByDriverId(driverId);
             if(currentDriverLocation == null){
                 throw new CustomException(5, "Текущая локация водителя пустая");
             }

            json.put("latitude", currentDriverLocation.getLocation().getLatitude());
            json.put("longitude", currentDriverLocation.getLocation().getLongitude());

            /* сохраняем координаты места поломки в поля lat_from, lon_from таблицы mission [адрес String не трогаю пока, возможно его следует потом тоже поменять!!!]*/
            LOGGER.info("OLD COORDINATES: la="+mission.getLocationFrom().getLatitude()+" lo="+mission.getLocationFrom().getLongitude()+" NEW LOCATION: la="+currentDriverLocation.getLocation().getLatitude()+" lo="+currentDriverLocation.getLocation().getLongitude());
            mission.setLocationTo(new Location(currentDriverLocation.getLocation().getLatitude(), currentDriverLocation.getLocation().getLongitude()));
            missionRepository.save(mission);

             for(Driver freeDriver: freeDrivers){
                 DriverLocation freeDriverLocation = locationRepository.findByDriverId(freeDriver.getId());
                 double distance = GeoUtils.distance(freeDriverLocation.getLocation().getLatitude(), freeDriverLocation.getLocation().getLongitude(), currentDriverLocation.getLocation().getLatitude(), currentDriverLocation.getLocation().getLongitude());
                 if(distance<=searchRadius){
                     json.put("driverId", freeDriver.getId());
                     nodeJsService.notified("mission_declined_by_reason", json);
                 }
             }

        }else if(EnumSet.of(Mission.State.ASSIGNED).contains(mission.getState())){
            // водитель отказывается от заказа по пути к клиенту

            json.put("latitude", mission.getLocationFrom().getLatitude());
            json.put("longitude", mission.getLocationFrom().getLongitude());

            for(Driver freeDriver: freeDrivers){
                DriverLocation freeDriverLocation = locationRepository.findByDriverId(freeDriver.getId());
                double distance = GeoUtils.distance(freeDriverLocation.getLocation().getLatitude(), freeDriverLocation.getLocation().getLongitude(), mission.getLocationFrom().getLatitude(), mission.getLocationFrom().getLongitude());
                if(distance<=searchRadius){
                    json.put("driverId", freeDriver.getId());
                    nodeJsService.notified("mission_declined_by_reason", json);
                }
            }
        }else{
            throw new CustomException(6, String.format("Статус миссии %s, %s  не соответсвует передаче", missionId, mission.getState().name()));
        }
        return response;
    }



    // метод для получения текущей смены водителя
    private DriverPeriodWork getDriverPeriodWorkToday(Driver driver){
        DriverPeriodWork result = null;
        Page<DriverPeriodWork> driverPeriodWorkPage = driverPeriodWorkRepository.findByEndWorkAfterAndDriverAndActive(DateTimeUtils.nowNovosib_GMT6(), driver, true, new PageRequest(0, 1, Sort.Direction.ASC, "endWork"));
        if(driverPeriodWorkPage.getContent()!=null && !driverPeriodWorkPage.getContent().isEmpty()){
              result = driverPeriodWorkPage.getContent().get(0);
        }
        return result;
    }




    public boolean isOwnDriver(long driverId){
        boolean result = false;
        Driver driver = driverRepository.findOne(driverId);
        if(driver == null){
            throw new CustomException(1,"Водитель не найден");
        }
        List<DriverRequisite> driverRequisite = driverRequisiteRepository.findByDriverAndActive(driver, true);
        if(!CollectionUtils.isEmpty(driverRequisite)){
            result= true;
        }
        return result;
    }



    public DriverSettingResponse driverSetting(long driverId) {
        Driver driver = driverRepository.findOne(driverId);
        if(driver == null){
            throw new CustomException(1, "Водитель не найден");
        }
        DriverSettingResponse response = new DriverSettingResponse();
        DriverSetting driverSetting = driverSettingRepository.findByDriver(driver);
        if(driverSetting == null){
             response.setErrorMessage("Настройки не найдены");
        }
          response.setDriverSettingInfo(ModelsUtils.toModelDriverSettingInfo(driverSetting));
            return response;
    }




    public UpdateDriverSettingResponse updateDriverSetting(DriverSettingInfo info) {
        Driver driver = driverRepository.findOne(info.getDriverId());
        if(driver==null){
            throw new CustomException(1, "Водитель не найден");
        }
        UpdateDriverSettingResponse response = new UpdateDriverSettingResponse();
        DriverSetting driverSetting = driverSettingRepository.findByDriver(driver);

        List<Long> ids = new ArrayList<>();
        if(!CollectionUtils.isEmpty(info.getAdditionalServices())){
            for(AdditionalServiceInfo serviceInfo: info.getAdditionalServices()){
                ids.add(serviceInfo.getId());
            }
        }
        List<AdditionalService> additionalServices =  additionalServiceRepository.findByIdIn(ids);
        if(driverSetting==null){
            // create setting
            driverSettingRepository.save(ModelsUtils.fromModel(info, driver, additionalServices));
        }else{
            // update setting
            driverSettingRepository.save(ModelsUtils.fromModelUpdate(info, driverSetting, additionalServices));
        }
         return response;
    }





    // получене списка актуальных миссий в радиусе
    public ActualMissionInRadiusResponse actualMissionInRadius(long driverId, int radius, String security_token){
        ActualMissionInRadiusResponse response = new ActualMissionInRadiusResponse();

        Driver driver = driverRepository.findOne(driverId);
        //if(!commonService.isOkSecurityToken(driver, security_token, 2)){
            //throw new CustomException(1, "Водитель не найден");
        //}

        DriverLocation driverLocation = locationRepository.findByDriverId(driverId);
        if(driverLocation == null){
            throw new CustomException(2, "Не найдена текущая локация");
        }

        List<Mission> missions =  new ArrayList<>();

        if(driver.isEntrepreneur()){
            missions.addAll(missionRepository.findByStateOrderByTimeOfStartingAsc(Mission.State.AUTO_SEARCH));
            missions.addAll(missionRepository.findByStateOrderByTimeOfStartingAsc(Mission.State.NEW));
            missions.addAll(missionRepository.findByStateAndDriverInfoTypeXOrderByTimeOfStartingAsc(Mission.State.ASSIGNED, Boolean.TRUE));
        }else{
            List<PaymentType> paymentTypes = new ArrayList<>();
            paymentTypes.add(PaymentType.CORPORATE_CARD);
            paymentTypes.add(PaymentType.CARD);
            missions.addAll(missionRepository.findByStateAndPaymentTypeNotInOrderByTimeOfStartingAsc(Mission.State.AUTO_SEARCH, paymentTypes));
            missions.addAll(missionRepository.findByStateAndPaymentTypeNotInOrderByTimeOfStartingAsc(Mission.State.NEW, paymentTypes));
            missions.addAll(missionRepository.findByStateAndPaymentTypeNotInAndDriverInfoTypeXOrderByTimeOfStartingAsc(Mission.State.ASSIGNED, paymentTypes, Boolean.TRUE));
        }

        radius = radius==0 ? Integer.parseInt(commonService.getPropertyValue("radius_search_mission_default")):radius;

        for (ListIterator<Mission> i = missions.listIterator(); i.hasNext(); ) {
            Mission mission = i.next();
            /*
                STANDARD(1),
                COMFORT(2),
                BUSINESS(3),
                LOW_COSTER(4)
             */
            switch(mission.getAutoClass().getValue()){
                case 1:{
                    // STANDARD(1)
                    break;
                }
                case 2:{
                    // COMFORT(2)
                    if(driver.getAutoClass().getValue() == 1) {
                        // класс авто водителя стандарт, а класс миссии комфорт - удаляем
                        i.remove();
                    }
                    break;
                }
                case 3:{
                    // BUSINESS(3)
                    if(driver.getAutoClass().getValue() == 1 || driver.getAutoClass().getValue() == 2) {
                        // класс авто водителя стандарт или комфорт, а класс миссии бизесс - удаляем
                        i.remove();
                    }
                    break;
                }
                default: break;
            }
        }

        for(Mission mission: missions){
            Location locationFrom = mission.getLocationFrom();
            Location driverCurrentLocation = driverLocation.getLocation();
            double distance = GeoUtils.distance(
                    driverCurrentLocation.getLatitude(), driverCurrentLocation.getLongitude(),
                    locationFrom.getLatitude(), locationFrom.getLongitude());
            if (distance <= radius) {
                DistanceDetails distanceDetails = new DistanceDetails();
                MissionInfo missionInfo = ModelsUtils.toModel(mission);

                /* отдавать единые номера  */
                missionInfo = (MissionInfo)commonService.commonPhoneNumber(missionInfo);

                if(mission.getDriverInfo()!=null && mission.getDriverInfo().isTypeX()){
                    // если на миссию назначен фантомный водитель - при выдаче инфы о заказе обнуляем его
                    missionInfo.setDriverInfo(null);
                }

                distanceDetails.setMissionInfo(missionInfo);
                distanceDetails.setDistanceToClient(distance);
                response.getDistanceDetailsList().add(distanceDetails);
            }
        }
        return response;
    }





    public DriverStartWorkResponse driverGetStartWork(long driverId, String security_token){
        DriverStartWorkResponse response = new DriverStartWorkResponse();
        Driver driver = driverRepository.findOne(driverId);
        //if(!commonService.isOkSecurityToken(driver, security_token, 2)){
            //throw new CustomException(1, "Водитель не найден");
        //}

        DriverPeriodWork driverPeriodWork = getDriverPeriodWorkToday(driver); //driverPeriodWorkRepository.findByStartWorkBetweenAndDriverAndActive(d.withTimeAtStartOfDay(), s.withTimeAtStartOfDay(), driver, true);
           if(driverPeriodWork == null) {
               throw new CustomException(2, "Не задана смена");
           }
           response.setDriverStartWorkInfo(ModelsUtils.toModel(driverPeriodWork));
        return response;
    }




    // возможно нужно добавить булево поле, где будет фиксироваться: true - начать смену, false - закончить смену
    public DriverStartWorkResponse driverStartWork(long driverId, String security_token){
        DriverStartWorkResponse response = new DriverStartWorkResponse();
        Driver driver = driverRepository.findOne(driverId);
        //if(!commonService.isOkSecurityToken(driver, security_token, 2)){
            //throw new CustomException(1, "Водитель не найден");
        //}

        List<DriverRequisite> driverRequisite = driverRequisiteRepository.findByDriverAndActive(driver, true);
        if(CollectionUtils.isEmpty(driverRequisite)){
            throw new CustomException(2, String.format("Вы не являетесь зарплатным водителем. DriverRequisite for %s not found", driverId));
        }

        DriverPeriodWork driverPeriodWork = getDriverPeriodWorkToday(driver);
        if(driverPeriodWork == null){
            throw new CustomException(4,"Смена не задана");
        }

        if(driverPeriodWork.getTimeWorkInFactOfStarting() == null){
            // если водитель еще не назначился на смену
            if(driverPeriodWork.getStartWork().isAfter(DateTimeUtils.nowNovosib_GMT6())){
                Minutes minutes = Minutes.minutesBetween(DateTimeUtils.nowNovosib_GMT6(), driverPeriodWork.getStartWork());
                if(Math.abs(minutes.getMinutes())>30) {
                    throw new CustomException(5,"Вы можете начать смену не раньше чем за 30 минут до ее фактического начала"); //  Смена начинается: +driverPeriodWork.getStartWork());
                }
                driverPeriodWork.setTimeWorkInFactOfStarting(DateTimeUtils.nowNovosib_GMT6());
                driverPeriodWorkRepository.save(driverPeriodWork);
            }else{
                /* здесь я должен начислить ему зарплату - ПРИ УСЛОВИИ ЧТО ОН ЗА ВОДИТЕЛЬ, иначе не начисляю */
                /* проверяем не начисляли ли мы ему уже за эту смену */
                // 12 - начисление ЗП зарплатному водителю [typeSalary=0, 2]
                if(driverRequisite.get(0).getTypeSalary()==0 || driverRequisite.get(0).getTypeSalary()==2){
                    DriverCashFlow driverCashFlow = driverCashRepository.findByDriverAndDriverPeriodWorkAndOperation(driver, driverPeriodWork, 12);
                    if(driverCashFlow == null){
                        // начисляем ЗП
                        administrationService.operationWithMoney(driverId, null, driverRequisite.get(0).getSalaryPerDay(), 12, null, "", driverPeriodWork.getId(), null);
                    }
                }
            }
            driverPeriodWork.setTimeWorkInFactOfStarting(DateTimeUtils.nowNovosib_GMT6());
            driverPeriodWorkRepository.save(driverPeriodWork);
        } else {
            throw new CustomException(6,"Вы уже записались на данную смену");
        }
        response.setDriverStartWorkInfo(ModelsUtils.toModel(driverPeriodWork));
            return response;
    }




    private int calculateMinutesBySeconds(int sec){
        int min;
        if(sec % 60 != 0){
            min = sec/60 +1;
        }else{
            min = sec/60;
        }
          return min;
    }






    public CommonTripHistoryResponse commonTripHistory(long driverId) throws ParseException {
        CommonTripHistoryResponse response = new CommonTripHistoryResponse();
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createSQLQuery(
                "select * from(select \n" +
                "m.id,\n" +
                "DATE_FORMAT(m.time_requesting, '%d-%m-%Y') as dateO,\n" +
                "DATE_FORMAT(m.time_requesting, '%H.%i') as timeO, m.sum_increase, m.price_in_fact_amount,\n" +
                "'mission' as typeR, m.from_address as address, m.time_requesting from mission m\n" +
                "join mission_addresses ma on ma.mission_id=m.id\n" +
                "where state='COMPLETED'  and driverInfo_id=:id \n" +
                "union\n" +
                "select c_ord.id, DATE_FORMAT(c_ord.time_finishing, '%d-%m-%Y'),  \n" +
                "DATE_FORMAT(c_ord.time_finishing, '%H.%i'), 0.00, \n" +
                "ROUND(c_ord.price_in_fact/100, 2), \n" +
                "'order' as typeR,\n" +
                "(select coAddr.address from c_order_address coAddr where coAddr.c_order_id=c_ord.id order by coAddr.id desc limit 0,1) as address, c_ord.time_finishing\n" +
                "from c_order c_ord\n" +
                "where c_ord.state in('COMPLETED')  and c_ord.driver_id=:id) as RES order by time_requesting desc limit 0,10");
        query.setParameter("id", driverId);
        List result = query.list();

        Iterator it = result.iterator();

        TreeMap<Long, List<CommonTripHistoryInfo>> hash = new TreeMap();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        while(it.hasNext()) {
            Object row[] = (Object[]) it.next();
            long tripId = ((BigInteger) row[0]).intValue();
            String dateTrip = ((String) row[1]);
            String timeTrip = ((String) row[2]);
            double increaseAmount = ((BigDecimal)row[3]).doubleValue();
            double priceInFact = ((BigDecimal)row[4]).doubleValue();
            String typeTrip = ((String) row[5]);
            String address = ((String) row[6]);

            long millisec = dateFormat.parse(dateTrip).getTime();

            if(hash.containsKey(millisec)){
                hash.put(millisec, fillCommonHistoryList(hash.get(millisec), buildCommonTripHistoryInfo(tripId, dateTrip, timeTrip, increaseAmount, priceInFact, typeTrip, address)));
            } else {
                hash.put(millisec, fillCommonHistoryList(null, buildCommonTripHistoryInfo(tripId, dateTrip, timeTrip, increaseAmount, priceInFact, typeTrip, address)));
            }
        }

        NavigableMap<Long, List<CommonTripHistoryInfo>> nmap = hash.descendingMap();
        for(Map.Entry<Long, List<CommonTripHistoryInfo>> entry : nmap.entrySet()) {
            List<CommonTripHistoryInfo> value = entry.getValue();
            response.getCommonHistory().put(value.get(0).getDateTrip(), value);
        }

        return response;
    }







    private List<CommonTripHistoryInfo> fillCommonHistoryList(List<CommonTripHistoryInfo> commonTripHistoryList, CommonTripHistoryInfo info){
        if(commonTripHistoryList == null){
             commonTripHistoryList = new ArrayList<>();
        }
        LOGGER.info("zzz = " + info.getDateTrip());
        commonTripHistoryList.add(info);
        return commonTripHistoryList;
    }


    private CommonTripHistoryInfo buildCommonTripHistoryInfo(long tripId, String dateTrip, String timeTrip, double increaseAmount, double priceInFact, String typeTrip, String address){
        CommonTripHistoryInfo info = new CommonTripHistoryInfo();
        info.setTripId(tripId);
        info.setDateTrip(dateTrip);
        info.setTimeTrip(timeTrip);
        info.setIncreaseAmount(Double.toString(increaseAmount));
        info.setPriceInFact(Double.toString(priceInFact));
        info.setTypeTrip(typeTrip);
        info.setAddress(address);
        return info;
    }




    // спросить у сервера - отдых платный или нет?
    public BusyIsPaymentResponse busyIsPayment(long driverId, String security_token){
        Driver driver = driverRepository.findOne(driverId);
        //if(!commonService.isOkSecurityToken(driver, security_token, 2)){
            //throw new CustomException(1, "Водитель не найден");
        //}

        List<DriverRequisite> driverRequisite = driverRequisiteRepository.findByDriverAndActive(driver, Boolean.TRUE);
        if(CollectionUtils.isEmpty(driverRequisite)){
            throw new CustomException(2, String.format("Не заданы реквизиты для водителя: %s", driverId));
        }

        BusyIsPaymentResponse response = new BusyIsPaymentResponse();

        /* если тип водителя не зарплатник, то у него всегда отдых бесплатный -????*/
        if(driverRequisite.get(0).getSalaryPriority()==1){
            response.setResult(false);
             return response;
        }

        DriverPeriodWork driverPeriodWork = getDriverPeriodWorkToday(driver);// old -driverPeriodWorkRepository.findByStartWorkBeforeAndEndWorkAfterAndDriver(DateTimeUtils.nowNovosib_GMT6(), DateTimeUtils.nowNovosib_GMT6(), driver);
            if(driverPeriodWork==null){
               throw new CustomException(3, String.format("Не задана смена для водителя %s", driverId));
            }

            String costPayRestInMinutes = commonService.getPropertyValue("cost_pay_rest_driver_in_minutes");
            int defaultCountMinRest = driverRequisite.get(0).getCountMinutesOfRest();
            int countRestMinInFact = calculateMinutesBySeconds(driverPeriodWork.getTimeSecRest());

                if(countRestMinInFact>defaultCountMinRest){
                    // отдохнул больше чем было положено, оповещаем об этом
                    LOGGER.info(String.format("Водитель %s использовал разрешенное время для отдыха в эту смену", driverId));
                    /*
                       как только водитель получил в ответе, что он превысил бесплатное время отдыха, мы спрашиваем у него:
                       согласен ли он платить за отдых : и возвращаем стоимость минуты платного отдыха, если он нажал ДА, в локацию шлется уже платные секунды отдыха
                    */
                    response.setResult(true);
                    response.setErrorMessage(String.format("Вы использовали разрешенное время для отдыха в эту смену. Вы можете выбрать платный отдых за %s руб/мин",costPayRestInMinutes));
                    return response;
                }

            /* проверяем не пытается ли он отдохнуть в запрещенный период времени [временные промежутки не должны накладывать, иначе придется работать со списком]*/
            Page<BanPeriodRestDriver> banPeriodRestDriver =  banPeriodRestDriverRepository.findByTimeOfStartingBeforeAndTimeOfEndingAfterAndActive(DateTimeUtils.nowNovosib_GMT6(), DateTimeUtils.nowNovosib_GMT6(), Boolean.TRUE, new PageRequest(0, 1));
            List<BanPeriodRestDriver> banPeriodRestDriverList = banPeriodRestDriver.getContent();
            LOGGER.info("banPeriodRestDriverList=" + banPeriodRestDriverList);

            if(banPeriodRestDriverList!=null && !banPeriodRestDriverList.isEmpty()){
                // значит водитель пытается отдохнуть в запрещенный период времени, значит высвечиваем ему месседж и задаем вопрос согласен ли он отдыхать платно
                LOGGER.info("В это время в Таксисто не принято отдыхать, потому что много заказов. Вы можете выбрать платный отдых за %s руб/мин");
                response.setResult(true);
                response.setErrorMessage(String.format("В это время в Таксисто не принято отдыхать, потому что много заказов. Вы можете выбрать платный отдых за %s руб/мин", costPayRestInMinutes));
                return response;
            }
        return response;
    }




    public BusyDriverResponse driverBusy(long driverId, boolean isValue, String security_token){
        BusyDriverResponse response = new BusyDriverResponse();
        Driver driver = driverRepository.findOne(driverId);

        //if(!commonService.isOkSecurityToken(driver, security_token, 2)){
            //throw new CustomException(1, "Водитель не найден");
        //}

            Driver.State state = isValue ? Driver.State.BUSY : Driver.State.AVAILABLE;
            driver.setState(state);
            response.setBusy(isValue);
            response.setDriverId(driverId);

            //DriverRequisite driverRequisite = driverRequisiteRepository.findByDriverAndActive(driver, true);
            DriverRequisite driverRequisite = driverRequisiteRepository.findByDriverAndActiveAndSalaryPriority(driver, true, 0);

               if(driverRequisite!=null){
                   DriverPeriodWork driverPeriodWork = getDriverPeriodWorkToday(driver);
                      if(driverPeriodWork==null){
                          throw new CustomException(2,"DriverPeriodWork not found");
                      }

                   /* если это ЗП водитель, каждый раз, когда он заканчивает отдыхать, мы списываем с него бабло за платный отдых, если отдых действительно был платным!!!*/
                   BusyIsPaymentResponse resp = busyIsPayment(driverId, security_token);
                   if(!isValue) {
                       LOGGER.info("Выходим из отдыха resp.isResult() = "+resp.isResult());
                       // выходит из отдыха (если он выходит из платного отдыха - снимаем бабло)
                       if(resp.isResult()){
                           operationWithOwnDriver(driver, driverPeriodWork);
                       }
                   }else{
                       // входит в отдых, проверяем платный ли он, если да, то фиксируем время начала платного отдыха
                       if(resp.isResult()){
                          // если отдых водителя платный, я записываю время старта плтаного отдыха
                          driverPeriodWork.setLastTimePayRest(DateTimeUtils.nowNovosib_GMT6());
                          driverPeriodWorkRepository.save(driverPeriodWork);
                       }
                   }
               }
            /* log */
            mongoDBServices.createEvent(1, "" + driverId, 3, 0, "busy", "", "");

        return response;
    }





    // 13 - снятие с ЗП за платный отдых
    private void operationWithOwnDriver(Driver driver, DriverPeriodWork driverPeriodWork){
        String costPayRestInMinutes = commonService.getPropertyValue("cost_pay_rest_driver_in_minutes");
        Seconds seconds = Seconds.secondsBetween(DateTimeUtils.nowNovosib_GMT6(), driverPeriodWork.getLastTimePayRest() == null ? DateTimeUtils.nowNovosib_GMT6() : driverPeriodWork.getLastTimePayRest());

        int minRest = Math.abs(seconds.getSeconds()/60); //calculateMinutesBySeconds(seconds.getSeconds());
        int calcCostPayRest = minRest*(Integer.parseInt(costPayRestInMinutes))*(-1);

                  if(calcCostPayRest!=0){
                      DriverCashFlow driverCashFlow = driverCashRepository.findByDriverAndDriverPeriodWorkAndOperation(driver, driverPeriodWork, 13);
                      if(driverCashFlow==null){
                          // создаем отметку за платный отдых
                          administrationService.operationWithMoney(driver.getId(), null, calcCostPayRest, 13, null, "", driverPeriodWork.getId(), null);
                      }else {

                          // обновляем платный отдых
                          Account driverAccount = accountRepository.findOne(driver.getAccount().getId());
                          driverAccount.setMoney(driverAccount.getMoney().plus(calcCostPayRest));
                          calcCostPayRest = calcCostPayRest*100;
                          //LOGGER.info("driverCashFlow.getSum() = " + driverCashFlow.getSum() + " calcCostPayRest = " + calcCostPayRest + " driverCashFlow.getSum()+calcCostPayRest = " + (driverCashFlow.getSum() + calcCostPayRest));
                          driverCashFlow.setSum(driverCashFlow.getSum()+calcCostPayRest);
                          driverCashFlow.setDate_operation(DateTimeUtils.nowNovosib_GMT6());
                          driverCashRepository.save(driverCashFlow);
                          accountRepository.save(driverAccount);
                      }
                  }
    }




    public AlarmResponse driverAlarm(long driverId, boolean isAlarm) throws Exception{
        Driver driver = driverRepository.findOne(driverId);
        if (driver == null) {
            throw new CustomException(1,"Driver not found");
        }
        AlarmResponse response = new AlarmResponse();
        JSONObject alarm = new JSONObject();
        if(isAlarm){
            driver.setState(Driver.State.ALARM);

             /* send event START alarm to all driver */
            alarm.put("driverId",driverId);
            alarm.put("on",Boolean.TRUE);
             /*end*/

        }else{
            driver.setState(Driver.State.AVAILABLE);
              /* send event END alarm to all driver */
            alarm.put("driverId",driverId);
            alarm.put("on",Boolean.FALSE);
              /*end*/
        }
        driverRepository.save(driver);
        nodeJsService.notified("driver_alarm", alarm);
        /*log*/
        mongoDBServices.createEvent(1, "" + driverId, 3, 0, "alarm", "", "");

        return response;
    }





    /* сохраняем в монгу время активности водителя [свободен, занят]*/
    private void saveTimeActivity(Driver driver){
        /* нужна проверка нет ли записи за последние 10 секунд, если есть, то ничего непишем */
        //if(!mongoDBServices.checkTime(driver.getId(), driver.getState().equals(Driver.State.BUSY)?2:1)){
            DriverActivity driverActivity = new DriverActivity();
            driverActivity.setTypeActivity(driver.getState().equals(Driver.State.BUSY)?2:1);
            driverActivity.setDriverId(driver.getId());
            driverActivity.setTimeAmount(10); // seconds
            driverActivity.setDateTime(new DateTime().getMillis() / 1000L);
            driverActivityRepository.save(driverActivity);
        //}
    }




    private void sendLocationToClient(DriverLocation driverLocation) throws JSONException {
        if(driverLocation != null){
            QueueMission queueMission = getFirstMissionFromQueue(driverLocation.getDriver().getId());
            if(queueMission != null){
                 /* шлем клиенту локацию водителя */
                nodeJsNotificationsService.driverLocation(queueMission.getMission(), driverLocation);
            }
            /*
            ru.trendtech.domain.courier.Order order = driverLocation.getOrder();
            if(order != null && !order.getState().equals(ru.trendtech.domain.courier.Order.State.COMPLETED) && order.getState().equals(ru.trendtech.domain.courier.Order.State.CANCELED)){
                nodeJsNotificationsService.courierSendLocationToClient(order, driverLocation);
            }
            */
        }
    }





    public DriverLocationV2Response saveDriverLocationWithTimeTracking(long driverId, ItemLocation location, int timeWork, int timeRest, int timePayRest, String security_token, int distance) throws JSONException {
        DriverLocationV2Response response = new DriverLocationV2Response();
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        Driver driver = driverRepository.findOne(driverId);
        //if(!commonService.isOkSecurityToken(driver, security_token, 2)){
            //throw new CustomException(1, "Водитель не найден");
        //}

            /* сохраняем в монгу время активности водителя [свободен, занят] */
            saveTimeActivity(driver);

            /* если в driver_location нет записи по текущему водителю - создаем ее*/
            DriverLocation driverLocation = locationRepository.findByDriverId(driverId);
            if(driverLocation == null){
                driverLocation = commonService.buildDriverLocation(driver, latitude, longitude, distance);
                locationRepository.save(driverLocation);
            }

            /*
               если по текущему водителю есть взятая миссия находящаяся в очереди - шлем по ней локейшены данного водителя клиенту по данной миссии
               сделано для того, чтобы клиент второго заказа, который взял водитель, видел в приложении его перемешение на карте
            */
            sendLocationToClient(driverLocation);

            /* save time tracking */
            //DriverRequisite driverRequisite = driverRequisiteRepository.findByDriverAndActive(driver, Boolean.TRUE);
            DriverRequisite driverRequisite = driverRequisiteRepository.findByDriverAndActiveAndSalaryPriority(driver, Boolean.TRUE, 0);

            if(driverRequisite != null){
                DriverPeriodWork driverPeriodWork = getDriverPeriodWorkToday(driver); //driverPeriodWorkRepository.findByStartWorkBeforeAndEndWorkAfterAndDriver(DateTimeUtils.nowNovosib_GMT6(), DateTimeUtils.nowNovosib_GMT6(), driver);
                if(driverPeriodWork == null){
                   //throw new CustomException(1, "На водителе не задана смена");
                   /* если он зарплатный и на нем не задана смена, он работает как обычный водитель */
                   LOGGER.info("На зарплатном водителе {} не задана смена", driverId);
                   throw new CustomException(1, String.format("На зарплатном водителе %s не задана смена", driverId));
                }
                if(driverPeriodWork.getStartWork().isAfter(DateTimeUtils.nowNovosib_GMT6()) || driverPeriodWork.getTimeWorkInFactOfStarting() == null){
                   // будущая смена или смена не начата водителем, - ничего не записываем
                   return response;
                }
                if(timeWork < driverPeriodWork.getTimeSecWork() || timeRest < driverPeriodWork.getTimeSecRest() || timePayRest < driverPeriodWork.getTimeSecPayRest()){
                    // ситуация когда водитель обнулил у себя время отдыха или работы
                   return response;
                }
                    driverPeriodWork.setTimeSecWork(timeWork);
                    driverPeriodWork.setTimeSecRest(timeRest);
                    driverPeriodWork.setTimeSecPayRest(timePayRest);
                    driverPeriodWork.setUpdateTime(DateTimeUtils.nowNovosib_GMT6());
                    driverPeriodWorkRepository.save(driverPeriodWork);
            }
            /* end */
        return response;
    }





    public ReadNewsResponse setNewsAsRead(long driverId, long newsId, String security_token){
        ReadNewsResponse response =  new ReadNewsResponse();
        Driver driver = driverRepository.findOne(driverId);

        //if(!commonService.isOkSecurityToken(driver, security_token, 2)){
            //response.getErrorCodeHelper().setErrorCode(1);
            //response.getErrorCodeHelper().setErrorMessage("Водитель не найден");
            //return response;
        //}

        News news= newsRepository.findOne(newsId);

            if(news!=null){
                ReadingNews readingNews = readingNewsRepository.findByNewsAndDriver(news, driver);
                if(readingNews!=null){
                    response.getErrorCodeHelper().setErrorCode(4);
                    response.getErrorCodeHelper().setErrorMessage("Новость уже помечена как прочитанная");
                }else{
                    ReadingNews rn = new ReadingNews();
                    rn.setDriver(driver);
                    rn.setNews(news);
                    rn.setTimeOfReading(DateTimeUtils.nowNovosib());
                    readingNewsRepository.save(rn);
                }
            }else{
                response.getErrorCodeHelper().setErrorCode(2);
                response.getErrorCodeHelper().setErrorMessage("Новость с таким номером не найдена");
            }

        return response;
    }




    // показать самую свежую, непрочитанную новость для водителя
    public DriverFreshNewsResponse driverFreshNews(long driverId, String security_token){

        DriverFreshNewsResponse response = new DriverFreshNewsResponse();
        Driver driver = driverRepository.findOne(driverId);

        //if(!commonService.isOkSecurityToken(driver, security_token, 2)){
            //response.getErrorCodeHelper().setErrorCode(1);
            //response.getErrorCodeHelper().setErrorMessage("Водитель не найден");
            //return response;
        //}

        News freshNews = null; // свежая, сегодняшняя новость
        Page<News> page = newsRepository.freshNews(DateTimeUtils.nowNovosib_GMT6().withTimeAtStartOfDay(), DateTimeUtils.nowNovosib_GMT6().withTimeAtStartOfDay().plusDays(1), new PageRequest(0, 1));
        if (page.getContent()!=null && !page.getContent().isEmpty()){
            freshNews = page.getContent().get(0);
        }
        if(freshNews == null){
           response.getErrorCodeHelper().setErrorCode(0);
           response.getErrorCodeHelper().setErrorMessage("Свежие новости отсутствуют");
           return response;
        }


            ReadingNews readingNews = readingNewsRepository.findByNewsAndDriver(freshNews, driver);
            if (readingNews != null) {
                // значит самую свежую он уже прочитал
                response.getErrorCodeHelper().setErrorCode(0);
                response.getErrorCodeHelper().setErrorMessage("Все свежие новости прочитаны");
            }else{
                // показываем водителю новость
                NewsInfo freshNewsInfo = ModelsUtils.toModel(freshNews);
                response.setNewsInfo(freshNewsInfo);
            }

        return response;
    }





    public DriverNewsResponse driverNews(long driverId, String security_token){
        // показать последние 5 непросроченых новости по водителю, с указателем какие из них прочитаны, какие нет
        DriverNewsResponse response = new DriverNewsResponse();
        Driver driver = driverRepository.findOne(driverId);

        //if(!commonService.isOkSecurityToken(driver, security_token, 2)){
            //response.getErrorCodeHelper().setErrorCode(1);
            //response.getErrorCodeHelper().setErrorMessage("Водитель не найден");
            //return response;
        //}

            Page<News> page = newsRepository.freshNews(DateTimeUtils.nowNovosib_GMT6().minusDays(10).withTimeAtStartOfDay(), DateTimeUtils.nowNovosib_GMT6().withTimeAtStartOfDay().plusDays(1), new PageRequest(0, 5));
            List<News> listNews = page.getContent();
                for (News news : listNews) {
                    NewsInfo newsInfo = ModelsUtils.toModel(news);
                    ReadingNews readingNews = readingNewsRepository.findByNewsAndDriver(news, driver);
                    if (readingNews != null) {
                        newsInfo.setReading(true);
                    }
                    response.getNewsInfoList().add(newsInfo);
                }

        return response;
    }





    private LocalDate checkDate(String temp) throws ArrayIndexOutOfBoundsException{
        LocalDate local;
        try {
            String[] spl = temp.split("-");
            StringBuffer buff = new StringBuffer();
            buff.append(spl[0]+"-");
            buff.append(Integer.parseInt(spl[1])+1);
            buff.append("-"+spl[2]);
            local = LocalDate.parse(buff.toString());
        } catch(org.joda.time.IllegalFieldValueException e){
             LOGGER.info("Exception checkDate: "+e.getMessage()+" info.getDate(): "+temp);
             return null;
        } catch(ArrayIndexOutOfBoundsException ex){
             LOGGER.info("Exception checkDate: "+ex.getMessage()+" info.getDate(): "+temp);
             return null;
        }
             return local;
    }




    public TimersDataResponse timersDataDriver(List<TimersDataInfo> timersDataInfos, String security_token, long driverId) throws ParseException {
        TimersDataResponse response = new TimersDataResponse();

        Driver driver = driverRepository.findByToken(security_token);
        //if(!commonService.isOkSecurityToken(driver, security_token, 2)){
            //throw new CustomException(1, "Водитель не найден");
        //}
        if(driver == null){
            driver = driverRepository.findOne(driverId);
        }

        for(TimersDataInfo info: timersDataInfos){
            LocalDate date = checkDate(info.getDate()); // "yyyy-dd-MM");

            if(date == null){
                response.setErrorCode(-1);
                response.setErrorMessage("Date is null");
                  return response;
            }

            DriverTimeWork driverTimeWork = driverTimeWorkRepository.findByDriverAndWorkDate(driver, date);
             if(driverTimeWork == null){
                 driverTimeWork = new DriverTimeWork();
                 driverTimeWork.setDriver(driver);
                 driverTimeWork.setWorkDate(date);
                 driverTimeWork.setTimeSecWork((int) info.getTimeWork());
                 driverTimeWork.setTimeSecRest((int) info.getTimeFreeRest());
                 driverTimeWork.setTimeSecPayRest((int) info.getTimePayRest());
                 driverTimeWork.setSecondsStateOnline(info.getSecondsStateOnline());
                 driverTimeWork.setSecondsStateBusy(info.getSecondsStateBusy());
                 driverTimeWorkRepository.save(driverTimeWork);
             }else{
                 int currentTimeWork = driverTimeWork.getTimeSecWork();
                 int currentTimeRest = driverTimeWork.getTimeSecRest();
                 int currentPayRest = driverTimeWork.getTimeSecPayRest();

                 int currentSecondsStateOnline = driverTimeWork.getSecondsStateOnline();
                 int currentSecondsStateBusy = driverTimeWork.getSecondsStateBusy();

                 int diffTimeWork = (int)info.getTimeWork() - currentTimeWork;
                 int diffTimeRest = (int)info.getTimeFreeRest() - currentTimeRest;
                 int diffTimePayRest = (int)info.getTimePayRest() - currentPayRest;

                 int diffTimeSecondsStateOnline = info.getSecondsStateOnline() - currentSecondsStateOnline;
                 int diffTimeSecondsStateBusy =   info.getSecondsStateBusy() - currentSecondsStateBusy;

                 LOGGER.info("currentTimeWork = " + currentTimeWork + " currentTimeRest = " + currentTimeRest + " currentPayRest = " + currentPayRest + " Request: TimeWork= " + info.getTimeWork()+" TimeFreeRest= "+info.getTimeFreeRest());
                 if(diffTimeSecondsStateOnline>0){
                     driverTimeWork.setSecondsStateOnline(currentSecondsStateOnline + diffTimeSecondsStateOnline);
                 }
                 if(diffTimeSecondsStateBusy>0){
                     driverTimeWork.setSecondsStateBusy(currentSecondsStateBusy + diffTimeSecondsStateBusy);
                 }
                 if(diffTimeWork>0){
                     driverTimeWork.setTimeSecWork(currentTimeWork+diffTimeWork);
                 }
                 if(diffTimeRest>0){
                     driverTimeWork.setTimeSecRest(currentTimeRest+diffTimeRest);
                 }
                 if(diffTimePayRest>0){
                     driverTimeWork.setTimeSecPayRest(currentPayRest+diffTimePayRest);
                 }
                 driverTimeWorkRepository.save(driverTimeWork);

                 if(diffTimeSecondsStateOnline<0 || diffTimeSecondsStateBusy<0){
                     //throw new CustomException(2, String.format("Время активности работы водителя id = %s не совпадают с текущим наработанным временем", info.getDriverId()));
                     LOGGER.info(String.format("Время активности работы водителя id = %s не совпадают с текущим наработанным временем", info.getDriverId()));
                     //return response;
                 }
                 if(diffTimeWork<0 || diffTimeRest<0 || diffTimePayRest<0){
                     //throw new CustomException(4, String.format("Время активности работы водителя id = %s не совпадают с текущим наработанным временем", info.getDriverId()));
                     LOGGER.info(String.format("Время активности работы водителя id = %s не совпадают с текущим наработанным временем", info.getDriverId()));
                     //return response;
                 }
             }
        }
            return response;
    }




    private String generateLogin() {
        String login;
        do {
            login = StrUtils.generateDriverLogin();
        } while (login != null && driverRepository.findByLogin(login) != null);
        return login;
    }





    /* конфигурация, которую грузит водитель */
    public SystemConfiguration_V3_Response getConfiguration_v3(long clientId, long driverId, String security_token){
        SystemConfiguration_V3_Response response = new SystemConfiguration_V3_Response();

        Client client = clientRepository.findOne(clientId);
        Driver driver = driverRepository.findOne(driverId);

        if(driver != null){
            //if(!commonService.isOkSecurityToken(driver, security_token, 2)){
                //return response;
            //}
        }

        Properties prop = propertiesRepository.findByPropName("use_map");
        String useMap = prop.getPropValue();
        response.setUseMap(useMap);

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

        /* расчет оставшегося бесплатного времени ожидания (если отрицательное, значит платное)  */
        if(driver!=null && driver.getCurrentMission()!=null){
            CommonService.TimeWaitClientUtil freeTimeWaitClientUtil = commonService.freeTimeLeftWaitClient(driver.getCurrentMission());
            /* осталось */
            response.setHowLongWaitDriverAssign(freeTimeWaitClientUtil.getFreeTimeInFact());
        }

        if (client != null) {
            if(client.getToken()!= null && client.getToken().equals(security_token)) {
                ServerStateInfo serverStateInfo = administrationService.resolveState(clientId, driverId);
                response.setServerStateInfo(serverStateInfo);

                    /* удаление тарифа LOW_COSTER если его нет в таблице private_tariff*/
                rates = administrationService.showRatesByPrivateTariff(rates, client);

                    /* удаление тарифом, которые не соответсвуют версии приложения */
                rates = administrationService.showRatesByVersionApp_V2(rates, client);

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



}
