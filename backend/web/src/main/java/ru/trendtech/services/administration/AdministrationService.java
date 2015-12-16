package ru.trendtech.services.administration;

import com.google.common.collect.Lists;
import org.apache.commons.mail.EmailException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.hibernate.criterion.Order;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.joda.time.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import ru.trendtech.common.mobileexchange.model.client.*;
import ru.trendtech.common.mobileexchange.model.client.corporate.LimitInfo;
import ru.trendtech.common.mobileexchange.model.client.corporate.UpdateCorporateClientLimitResponse;
import ru.trendtech.common.mobileexchange.model.common.*;
import ru.trendtech.common.mobileexchange.model.common.corporate.ClientInfoCorporateARM;
import ru.trendtech.common.mobileexchange.model.common.corporate.MissionInfoCorporate;
import ru.trendtech.common.mobileexchange.model.common.estimate.EstimateInfo;
import ru.trendtech.common.mobileexchange.model.common.estimate.EstimateInfoARM;
import ru.trendtech.common.mobileexchange.model.common.estimate.EstimateInfoClient;
import ru.trendtech.common.mobileexchange.model.common.push.DeviceInfoModel;
import ru.trendtech.common.mobileexchange.model.common.rates.*;
import ru.trendtech.common.mobileexchange.model.common.rates.AutoClassRateInfoV2;
import ru.trendtech.common.mobileexchange.model.common.rates.MissionRateInfoV2;
import ru.trendtech.common.mobileexchange.model.common.scores.Scores;
import ru.trendtech.common.mobileexchange.model.common.states.ServerState;
import ru.trendtech.common.mobileexchange.model.common.states.ServerStateInfo;
import ru.trendtech.common.mobileexchange.model.common.states.ServerStateInfoV2;
import ru.trendtech.common.mobileexchange.model.common.support.ClientAdministrationStatusResponse;
import ru.trendtech.common.mobileexchange.model.common.support.SupportPhone;
import ru.trendtech.common.mobileexchange.model.common.support.SupportPhoneType;
import ru.trendtech.common.mobileexchange.model.common.support.SupportPhones;
import ru.trendtech.common.mobileexchange.model.driver.*;
import ru.trendtech.common.mobileexchange.model.node.FantomDriverResponse;
import ru.trendtech.common.mobileexchange.model.web.*;
import ru.trendtech.common.mobileexchange.model.web.MissionCompleteResponse;
import ru.trendtech.common.mobileexchange.model.web.corporate.*;
import ru.trendtech.domain.AutoClass;
import ru.trendtech.domain.*;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.Location;
import ru.trendtech.domain.Properties;
import ru.trendtech.domain.ServicePrice;
import ru.trendtech.domain.admin.WebUser;
import ru.trendtech.domain.billing.*;
import ru.trendtech.domain.Terminal;
import ru.trendtech.domain.courier.OrderAddress;
import ru.trendtech.models.ModelsUtils;
import ru.trendtech.repositories.*;
import ru.trendtech.repositories.billing.AccountRepository;
import ru.trendtech.repositories.billing.ClientCardRepository;
import ru.trendtech.repositories.billing.MissionRatesRepository;
import ru.trendtech.repositories.courier.OrderRepository;
import ru.trendtech.services.MongoDBServices;
import ru.trendtech.services.common.CommonService;
import ru.trendtech.services.logging.GrayLogService;
import ru.trendtech.services.notifications.node.NodeJsNotificationsService;
import ru.trendtech.services.notifications.node.NodeJsService;
import ru.trendtech.services.TimeService;
import ru.trendtech.services.billing.BillingService;
import ru.trendtech.services.email.ServiceEmailNotification;
import ru.trendtech.services.push.devices.DevicesService;
import ru.trendtech.services.resources.ProfilesResourcesService;
import ru.trendtech.services.sms.SMSC;
import ru.trendtech.services.sms.ServiceSMSNotification;
import ru.trendtech.services.driver.search.FindDriversService;
import ru.trendtech.services.validate.ValidatorService;
import ru.trendtech.utils.*;
import ru.trendtech.utils.DateTimeUtils;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * File created by petr on 20/04/2014 23:23.
 */

@Service
@Transactional
public class AdministrationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdministrationService.class);
    //private String GOOGLE_API_KEY = "AIzaSyA7hkJksRVU9Mun3Qv6zqqChHt64-QMsQE";
    @Value("${missionDetailsText.url}")
    private String missionDetailsText = "";

    @Value("${google.client.api.key}")
    private String GOOGLE_API_KEY = "";

    @Value("${2gis.client.api.key}")
    private String GIS_API_KEY = "";
    @Autowired
    @Qualifier("exclusive")
    private DevicesService devicesService;
    @Autowired
    private FindDriversService findDriversService;
    @Autowired
    private MissionRepository missionRepository;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private DriverSettingRepository driverSettingRepository;
    @Autowired
    private MissionCostIncreaseRepository missionCostIncreaseRepository;
    @Autowired
    private MissionRatesRepository missionRatesRepository;
    @Autowired
    private ServicesRepository servicesRepository;
    @Autowired
    PeriodWorkRepository periodWorkRepository;
    @Autowired
    private ReroutingRepository reroutingRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PromoCodeRepository promoCodeRepository;
    @Autowired
    private TimeService timeService;
    @Autowired
    private BillingService billingService;
    @Autowired
    private ProfilesResourcesService profilesResourcesService;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CashRepository cashRepository;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private DriverLockRepository driverLockRepository;
    @Autowired
    private ClientLockRepository clientLockRepository;
    @Autowired
    private NodeJsService nodeJsService;
    @Autowired
    CorporateClientLocksRepository corporateClientLocksRepository;
    @Autowired
    private WebUserRepository webUserRepository;
    @Autowired
    private ServiceSMSNotification serviceSMSNotification;
    @Autowired
    private ServiceEmailNotification serviceEmailNotification;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private PropertiesRepository propertiesRepository;
    @Autowired
    private MdOrderRepository mdOrderRepository;
    @Autowired
    private MissionAddressesRepository missionAddressesRepository;
    @Autowired
    private PartnersGroupRepository partnersGroupRepository;
    @Autowired
    private ItemPartnersGroupRepository  itemPartnersGroupRepository;
    @Autowired
    private DriverRequisiteRepository driverRequisiteRepository;
    @Autowired
    private MoneyWithdrawalRepository moneyWithdrawalRepository;
    @Autowired
    private SMSMessageRepository smsMessageRepository;
    @Autowired
    private ClientSumPromoCodeRepository availableSumPromoCodeRepository;
    @Autowired
    private ClientCountPromoCodeRepository clientCountPromoCodeRepository;
    @Autowired
    private ClientAvailableActivatePromoCodeRepository clientAvailableActivatePromoCodeRepository;
    @Autowired
    private TaxoparkPartnersRepository taxoparkPartnersRepository;
    @Autowired
    private AssistantRepository assistantRepository;
    @Autowired
    private ClientCardRepository clientCardRepository;
    @Autowired
    private TerminalRepository terminalRepository;
    @Autowired
    private MissionStateStatisticRepository missionStateStatisticRepository;
    @Autowired
    private EstimateRepository estimateRepository;
    @Autowired
    private SystemPropertiesRepository systemPropertiesRepository;
    @Autowired
    private SMSC smsc;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private VersionsAppRepository versionsAppRepository;
    @Autowired
    private MissionCanceledRepository missionCanceledRepository;
    @Autowired
    private MongoDBServices mongoDBServices;
    @Autowired
    private ClientCashFlowRepository clientCashFlowRepository;
    @Autowired
    private CorporateClientCashFlowRepository corporateClientCashFlowRepository;
    @Autowired
    private ClientCorrectionsRepository clientCorrectionsRepository;
    @Autowired
    private DriverCorrectionsRepository driverCorrectionsRepository;
    @Autowired
    private SendSmsLockRepository sendSmsLockRepository;
    @Autowired
    private ArticleAdjustmentsRepository articleAdjustmentsRepository;
    @Autowired
    private QueueMissionRepository queueMissionRepository;
    @Autowired
    private MissionFantomDriverRepository missionFantomDriverRepository;
    @Autowired
    private DriverCarPhotosRepository driverCarPhotosRepository;
    @Autowired
    private ValidatorService validatorService;
    private static final String CLIENT_SENT_CODE_YES = "1";
    private static final String CLIENT_SENT_CODE_NO = "0";

    @Transactional(readOnly = true)
    public Driver getDriverInfo(long id) {
        return driverRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public Client getClientInfo(long id) {
        return clientRepository.findOne(id);
    }

    //ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
    @Autowired
    private ComissionRepository comissionRepository;
    @Autowired
    private TurboIncreaseDriverRepository turboIncreaseDriverRepository;
    @Autowired
    private NewsVersionAppRepository newsVersionAppRepository;
    @Autowired
    private BanPeriodRestDriverRepository banPeriodRestDriverRepository;
    @Autowired
    private EventPartnerRepository eventPartnerRepository;
    @Autowired
    private AlternativeStatisticsRepository alternativeStatisticsRepository;
    @Autowired
    private DriverPeriodWorkRepository driverPeriodWorkRepository;
    @Autowired
    private TipPercentRepository tipPercentRepository;
    @Autowired
    private PrivateTariffRepository privateTariffRepository;
    @Autowired
    private CorporateClientLimitRepository corporateClientLimitRepository;
    @Autowired
    private TariffRestrictionRepository tariffRestrictionRepository;
    @Autowired
    private PromoCodeExclusiveRepository promoCodeExclusiveRepository;
    @Autowired
    private MultipleMissionRepository multipleMissionRepository;
    @Autowired
    private TabletRepository tabletRepository;
    @Autowired
    private TabletsUsedRepository tabletsUsedRepository;
    @Autowired
    private RouterRepository routerRepository;
    @Autowired
    private RouterUsedRepository routerUsedRepository;
    @Autowired
    private AdditionalServiceRepository additionalServiceRepository;
    @Autowired
    private ReasonRepository reasonRepository;
    @Autowired
    private CommonService commonService;
    @Autowired
    private TaxoparkCashFlowRepository taxoparkCashFlowRepository;
    @Autowired
    private NodeJsNotificationsService nodeJsNotificationsService;
    @Autowired
    private GrayLogService grayLogService;
    @Autowired
    private OrderRepository orderRepository;



    public void reasonsUpdate(List<ReasonInfo> reasonInfos){
         List<Reason> saveReasons = new ArrayList<>();
         for(ReasonInfo info: reasonInfos){
             if(info.getReasonId() == 0){
                 saveReasons.add(ModelsUtils.fromModel(info, null));
             } else{
                 Reason reason = reasonRepository.findOne(info.getReasonId());
                 if(reason == null){
                     throw new CustomException(1, "Reason not found");
                 }
                 saveReasons.add(ModelsUtils.fromModel(info, reason));
             }
         }
                 reasonRepository.save(saveReasons);
    }



    public TabletUpdateResponse tabletUpdate(List<TabletInfo> tabletInfos){
        TabletUpdateResponse response = new TabletUpdateResponse();
        for(TabletInfo info: tabletInfos){
            if(info.getId() == 0){
                Tablet saveTablet = ModelsUtils.fromModel(info, null);
                tabletRepository.save(saveTablet);
                if(info.getDriverInfoARM()!=null){
                    Driver driver = driverRepository.findOne(info.getDriverInfoARM().getId());
                    if(driver == null){
                        throw new CustomException(1, String.format("Водитель с id=% не найден", info.getDriverInfoARM().getId()));
                    }
                    bindTabletToDriver(driver, saveTablet);
                }
                saveTablet = ModelsUtils.fromModel(info, saveTablet);
                tabletRepository.save(saveTablet);
            }else{
                Tablet tablet = tabletRepository.findOne(info.getId());
                if(tablet == null){
                    throw new CustomException(2, "Планшет не найден");
                }
                if(info.getDriverInfoARM()!=null){
                    if(info.getDriverInfoARM().getId()==0){
                        bindTabletToDriver(null, tablet);
                    } else{
                        Driver driver = driverRepository.findOne(info.getDriverInfoARM().getId());
                        if(driver == null){
                            throw new CustomException(1, String.format("Водитель с id=% не найден", info.getDriverInfoARM().getId()));
                        }
                        bindTabletToDriver(driver, tablet);
                    }
                }
                tablet = ModelsUtils.fromModel(info, tablet);
                tabletRepository.save(tablet);
            }
        }
        return response;
    }






    public RouterUpdateResponse routerUpdate(List<RouterInfo> routerInfos){
        RouterUpdateResponse response = new RouterUpdateResponse();
        for(RouterInfo info: routerInfos){
            if(info.getId() == 0){
                Router saveRouter = ModelsUtils.fromModel(info, null);
                routerRepository.save(saveRouter);
                if(info.getDriverInfoARM()!=null){
                    Driver driver = driverRepository.findOne(info.getDriverInfoARM().getId());
                    if(driver == null){
                        throw new CustomException(1, String.format("Водитель с id=% не найден", info.getDriverInfoARM().getId()));
                    }
                    bindRouterToDriver(driver, saveRouter);
                }
                saveRouter = ModelsUtils.fromModel(info, saveRouter);
                routerRepository.save(saveRouter);
            }else{
                Router router = routerRepository.findOne(info.getId());
                if(router == null){
                    throw new CustomException(2, "Роутер не найден");
                }
                if(info.getDriverInfoARM()!=null){
                    if(info.getDriverInfoARM().getId()==0){
                        bindRouterToDriver(null, router);
                    } else{
                        Driver driver = driverRepository.findOne(info.getDriverInfoARM().getId());
                        if(driver == null){
                            throw new CustomException(1, String.format("Водитель с id=% не найден", info.getDriverInfoARM().getId()));
                        }
                        bindRouterToDriver(driver, router);
                    }
                }
                router = ModelsUtils.fromModel(info, router);
                routerRepository.save(router);
            }
        }
        return response;
    }




    public void updateAdditionalService(List<AdditionalServiceInfo> additionalServiceInfos){
        for(AdditionalServiceInfo info: additionalServiceInfos){
            if(info.getId() == 0){
                AdditionalService saveService = ModelsUtils.fromModel(info, null);
                additionalServiceRepository.save(saveService);
            }else{
                AdditionalService service = additionalServiceRepository.findOne(info.getId());
                if(service == null){
                    throw new CustomException(2, "Опция не найдена");
                }
                additionalServiceRepository.save(ModelsUtils.fromModel(info, service));
            }
        }
    }




    public AdditionalServiceResponse additionalService(long serviceId){
        AdditionalServiceResponse response = new AdditionalServiceResponse();
        if(serviceId == 0){
            Iterable<AdditionalService> services = additionalServiceRepository.findAll();
            for(AdditionalService service: services){
                response.getAdditionalServiceInfos().add(ModelsUtils.toModel(service));
            }
        }else {
            AdditionalService service = additionalServiceRepository.findOne(serviceId);
            if(service == null){
                throw new CustomException(1, "Опция не найдена");
            }
            response.getAdditionalServiceInfos().add(ModelsUtils.toModel(service));
        }
          return response;
    }




    public DriverSettingResponse driverSetting(String security_token, long driverId) {
        WebUser webUser = webUserRepository.findByToken(security_token);
        if (webUser == null) {
            throw new CustomException(1,"Web user not found");
        }
        Driver driver = driverRepository.findOne(driverId);
        if(driver == null){
            throw new CustomException(2, "Водитель не найден");
        }
        DriverSettingResponse response = new DriverSettingResponse();
        DriverSetting driverSetting = driverSettingRepository.findByDriver(driver);
        if(driverSetting==null){
            throw new CustomException(0, "Настройки не найдены");
        }
        response.setDriverSettingInfo(ModelsUtils.toModelDriverSettingInfo(driverSetting));
            return response;
    }





    public UpdateDriverSettingResponse updateDriverSetting(String security_token, DriverSettingInfo info) {
        WebUser webUser = webUserRepository.findByToken(security_token);
        if (webUser == null) {
            throw new CustomException(1,"Web user not found");
        }
        Driver driver = driverRepository.findOne(info.getDriverId());
        if(driver==null){
            throw new CustomException(1, "Водитель не найден");
        }
        UpdateDriverSettingResponse response = new UpdateDriverSettingResponse();

        List<Long> ids = new ArrayList<>();
        List<AdditionalService> additionalServices = null;

        if(!CollectionUtils.isEmpty(info.getAdditionalServices())){
            for(AdditionalServiceInfo serviceInfo: info.getAdditionalServices()){
                ids.add(serviceInfo.getId());
            }
                additionalServices =  additionalServiceRepository.findByIdIn(ids);
        }

        DriverSetting driverSetting = driverSettingRepository.findByDriver(driver);
        if(driverSetting == null){
                // create setting
                driverSetting = ModelsUtils.fromModel(info, driver, additionalServices);
                driverSetting = driverSettingRepository.save(driverSetting);

                /* adding 28.08.2015 */
                driver.setDriverSetting(driverSetting);
                driverRepository.save(driver);
        }else{
                // update setting
            if(!CollectionUtils.isEmpty(driverSetting.getAdditionalServices())){
                driverSetting.getAdditionalServices().clear();
                driverSettingRepository.save(driverSetting);
            }
                driverSetting = driverSettingRepository.save(ModelsUtils.fromModelUpdate(info, driverSetting, additionalServices));

                /* adding 28.08.2015 */
                driver.setDriverSetting(driverSetting);
                driverRepository.save(driver);

                if(driverSetting.getSumIncrease() != info.getSumIncrease()){
                    /* log event if sum increase was change */
                    mongoDBServices.createEvent(3, "" + webUser.getId(), 3, 0, "updateDriverSetting", "driverId:" + driver.getId(), "");
                    //mongoDBServices.createEvent(3, "" + webUser.getId(), 3, "updateDriverSetting", "", "", driverSetting.getId(), driverSetting.getDriver().getId());
                }
        }
        return response;
    }





    public TabletResponse tabletList(long tabletId, int numberPage, int sizePage){
        TabletResponse response = new TabletResponse();
         if(tabletId == 0){
            Page<Tablet> tablets = tabletRepository.findAll(new PageRequest(numberPage, sizePage, Sort.Direction.DESC, "timeOfUpdate"));
             for(Tablet tablet: tablets){
                 response.getTabletInfoList().add(ModelsUtils.toModel(tablet, driverRepository.findByTablet(tablet)));
             }
             response.setLastPageNumber(((tabletRepository.countTablets() / sizePage) + 1));
             response.setTotalItems(tablets.getTotalElements());
         }else {
             Tablet tablet = tabletRepository.findOne(tabletId);
             if(tablet == null){
                  throw new CustomException(1, "Планшет не найден");
             }
              response.getTabletInfoList().add(ModelsUtils.toModel(tablet, driverRepository.findByTablet(tablet)));
         }
        return response;
    }



    public RouterResponse routerList(long routerId, int numberPage, int sizePage){
        RouterResponse response = new RouterResponse();
        if(routerId == 0){
            Page<Router> routers = routerRepository.findAll(new PageRequest(numberPage, sizePage, Sort.Direction.DESC, "timeOfPurchase"));
            for(Router router: routers){
                response.getRouterInfoList().add(ModelsUtils.toModel(router, driverRepository.findByRouter(router)));
            }
            response.setLastPageNumber(((routerRepository.countRouters() / sizePage) + 1));
            response.setTotalItems(routers.getTotalElements());
        }else {
            Router router = routerRepository.findOne(routerId);
            if(router == null){
                throw new CustomException(1, "Роутер не найден");
            }
            response.getRouterInfoList().add(ModelsUtils.toModel(router, driverRepository.findByRouter(router)));
        }
        return response;
    }




    /*
    if(driverInfoARM.getTabletInfo().getId()==0){
                            // отвязываю планшет
                            driver.setTablet(null);
                            driverRepository.save(driver);

                            TabletsUsed usedTablet = tabletsUsedRepository.findByDriverAndEndUsedIsNull(driver);
                            if(usedTablet != null){
                                usedTablet.setEndUsed(DateTimeUtils.nowNovosib_GMT6());
                                tabletsUsedRepository.save(usedTablet);
                            }
                        }else{
                            Tablet tablet = tabletRepository.findOne(driverInfoARM.getTabletInfo().getId());
                            if(tablet == null) {
                                throw new CustomException(7, "Планшет не найден");
                            }

                            TabletsUsed used = tabletsUsedRepository.findByTabletAndDriverNotAndEndUsedIsNull(tablet, driver);
                            if(used!=null){
                                throw new CustomException(8, String.format("Данный планшет используется водителем id=%", used.getDriver().getId()));
                            }

                            driver.setTablet(tablet);
                            driverRepository.save(driver);

                            TabletsUsed usedTablet = new TabletsUsed();
                            usedTablet.setDriver(driver);
                            usedTablet.setTablet(tablet);
                            usedTablet.setStartUsed(DateTimeUtils.nowNovosib_GMT6());
                            tabletsUsedRepository.save(usedTablet);

     */





    public void bindTabletToDriver(Driver driver, Tablet tablet){
        if(driver!=null){
            // привязать
            TabletsUsed used = tabletsUsedRepository.findByTabletAndEndUsedIsNull(tablet); // findByTabletAndDriverNotAndEndUsedIsNull
            if(used!=null && used.getDriver().getId()!=driver.getId()){
                throw new CustomException(8, "Данный планшет используется другим водителем");
            }

            tablet.setTimeOfUpdate(DateTimeUtils.nowNovosib_GMT6());
            tabletRepository.save(tablet);
            driver.setTablet(tablet);
            driverRepository.save(driver);

            if(used == null){
                used = new TabletsUsed();
            }
            used.setDriver(driver);
            used.setTablet(tablet);
            used.setStartUsed(DateTimeUtils.nowNovosib_GMT6());
            tabletsUsedRepository.save(used);
        } else{
            // отвязываю планшет
            TabletsUsed usedTablet = tabletsUsedRepository.findByTabletAndEndUsedIsNull(tablet);
            if(usedTablet != null){
                Driver driverUsedTable = usedTablet.getDriver();
                if(driverUsedTable!=null){
                    driverUsedTable.setTablet(null);
                    driverRepository.save(driverUsedTable);
                }
                usedTablet.setEndUsed(DateTimeUtils.nowNovosib_GMT6());
                tabletsUsedRepository.save(usedTablet);
            }
        }
    }



    public void bindRouterToDriver(Driver driver, Router router){
        if(driver!=null){
            // привязать
            RouterUsed used = routerUsedRepository.findByRouterAndEndUsedIsNull(router); //
            if(used!=null && used.getDriver().getId()!=driver.getId()){
                throw new CustomException(8, "Данный роутер используется другим водителем");
            }
            driver.setRouter(router);
            driverRepository.save(driver);
            if(used == null){
               used = new RouterUsed();
            }
            used.setDriver(driver);
            used.setRouter(router);
            used.setStartUsed(DateTimeUtils.nowNovosib_GMT6());
            routerUsedRepository.save(used);
        } else{
            // отвязываю планшет
            RouterUsed routerUsed = routerUsedRepository.findByRouterAndEndUsedIsNull(router);
            if(routerUsed != null){
                Driver driverUsedRouter = routerUsed.getDriver();
                if(driverUsedRouter!=null){
                    driverUsedRouter.setRouter(null);
                    driverRepository.save(driverUsedRouter);
                }
                routerUsed.setEndUsed(DateTimeUtils.nowNovosib_GMT6());
                routerUsedRepository.save(routerUsed);
            }
        }
    }



    public void linkTabletToDriver(long driverId, long tabletId){
        Driver driver = driverRepository.findOne(driverId);
        if(driver == null){
            throw new CustomException(1, "Водитель не найден");
        }
        if(tabletId!=0){
            // привязать планшет
            Tablet tablet = tabletRepository.findOne(tabletId);
            if(tablet == null){
                throw new CustomException(7, "Планшет не найден");
            }
            TabletsUsed tabletsUsed = tabletsUsedRepository.findByTabletAndDriverNotAndEndUsedIsNull(tablet, driver); // // tabletsUsedRepository.findByTabletAndEndUsedIsNull(tablet)
            if(tabletsUsed!=null){
                throw new CustomException(8, String.format("Данный планшет используется водителем id=%", tabletsUsed.getDriver().getId()));
            }

            tablet.setTimeOfUpdate(DateTimeUtils.nowNovosib_GMT6());
            tabletRepository.save(tablet);
            driver.setTablet(tablet);
            driverRepository.save(driver);

            tabletsUsed = new TabletsUsed();
            tabletsUsed.setDriver(driver);
            tabletsUsed.setTablet(tablet);
            tabletsUsed.setStartUsed(DateTimeUtils.nowNovosib_GMT6());
            tabletsUsedRepository.save(tabletsUsed);
        } else{
            // отвязываю планшет
            driver.setTablet(null);
            driverRepository.save(driver);

            TabletsUsed usedTablet = tabletsUsedRepository.findByDriverAndEndUsedIsNull(driver);
            if(usedTablet != null){
                usedTablet.setEndUsed(DateTimeUtils.nowNovosib_GMT6());
                tabletsUsedRepository.save(usedTablet);
            }
        }
    }




    public void linkRouterToDriver(long driverId, long routerId){
        Driver driver = driverRepository.findOne(driverId);
        if(driver == null){
            throw new CustomException(1, "Водитель не найден");
        }
        if(routerId!=0){
            // привязать роутер
            Router router = routerRepository.findOne(routerId);
            if(router == null){
                throw new CustomException(7, "Роутер не найден");
            }
            RouterUsed routerUsed = routerUsedRepository.findByRouterAndDriverNotAndEndUsedIsNull(router, driver);
            if(routerUsed!=null){
                throw new CustomException(8, String.format("Данный роутер используется водителем id=%", routerUsed.getDriver().getId()));
            }

            driver.setRouter(router);
            driverRepository.save(driver);

            routerUsed = new RouterUsed();
            routerUsed.setDriver(driver);
            routerUsed.setRouter(router);
            routerUsed.setStartUsed(DateTimeUtils.nowNovosib_GMT6());
            routerUsedRepository.save(routerUsed);
        } else{
            // отвязываю роутер
            driver.setRouter(null);
            driverRepository.save(driver);

            RouterUsed routerUsed = routerUsedRepository.findByDriverAndEndUsedIsNull(driver);
            if(routerUsed != null){
                routerUsed.setEndUsed(DateTimeUtils.nowNovosib_GMT6());
                routerUsedRepository.save(routerUsed);
            }
        }
    }






    // для новых версияй клиентского ПО загружать новую фотку водителя и автомобиля
    public DriverInfo fillPhotoDriverAndCars(DriverInfo driverInfo, Driver driver, boolean versionAppIsNew) {
        if(driverInfo!=null){
            if(versionAppIsNew){
                // driver photo
                driverInfo.setPhotoUrl(driver.getPhotoUrlByVersion());
                // car photos
                List<DriverCarPhotos> driverCarPhotosList =  driverCarPhotosRepository.findByDriver(driver);
                if(!CollectionUtils.isEmpty(driverCarPhotosList)){
                    driverInfo.getPhotosCarsUrl().clear();
                    for(DriverCarPhotos driverCarPhotos: driverCarPhotosList){
                        driverInfo.getPhotosCarsUrl().add(driverCarPhotos.getPhotoUrl());
                    }
                }
            }
        }
            return driverInfo;
    }






    @Transactional(readOnly = true)
    public UserIdInfo resolveUserInfo(String phone) {
        UserIdInfo result = null;
        String number = PhoneUtils.normalizeNumber(phone);
        if (number != null) {
            Client client = clientRepository.findByPhone(number);
            if (client != null) {
                result = new UserIdInfo();
                result.setType(UserIdInfo.UserType.CLIENT);
                result.setId(client.getId());
            }
        } else {
            Driver driver = driverRepository.findByLogin(phone);// ?
            if (driver != null) {
                result = new UserIdInfo();
                result.setType(UserIdInfo.UserType.DRIVER);
                result.setId(driver.getId());
            }
        }
        return result;
    }






    // добавить ключ для гугла
    public boolean isNovosibAddress(ItemLocation location){
        boolean result = true;
        try {
            String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng="+location.getLatitude() + "," + location.getLongitude();//+"&key="+GOOGLE_API_KEY;
            String answer = HTTPUtil.senPostQuery(url, null);
            LOGGER.info("^^^^^^^^^^^^^^^^^^^^^ Method isNovosibAddress: answer"+answer);
            String totalStr;
            JSONObject answerJson = new JSONObject(answer);
            if(answerJson.has("results")){
                JSONArray resultJsonArray = (JSONArray) answerJson.get("results");
                JSONObject address_components = (JSONObject) resultJsonArray.get(0);
                JSONArray jsonArray = (JSONArray)address_components.get("address_components");

                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject longNameJson = (JSONObject) jsonArray.get(i);
                    totalStr = longNameJson.get("long_name").toString();
                    if(longNameJson.has("types")){
                        JSONArray jsa = (JSONArray) longNameJson.get("types");
                        String adminArea = jsa.get(0).toString();
                        if(adminArea.equals("administrative_area_level_1")){
                            if(!totalStr.equals("Novosibirskaya oblast'") || !totalStr.equals("Новосибирская область")){
                                LOGGER.info("REGION: "+totalStr);
                                result=false;
                            }
                        }
                    }
                }
            }

        }catch(Exception e){
            e.printStackTrace();
              result=true;
        }
           return result;
    }




    private WebUser fromModel(WebUserModel webUserModel) {
        return ModelsUtils.fromModel(webUserModel, new WebUser());
    }

//    public DriverCashFlow fillCash(DriverCashFlow driverCashFlow){
//        DriverCashFlow cashFlow = cashRepository.save(driverCashFlow);
//        return cashFlow;
//    }




    public void generateDate(){
        Session session = entityManager.unwrap(Session.class);
        List<java.sql.Date> result = session.createSQLQuery(" select a.Date\n" +
                "from (\n" +
                "    select DATE_SUB(curdate(), INTERVAL 5 MONTH) + INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as Date\n" +
                "    from (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9 ) as a\n" +
                "    cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as b\n" +
                "    cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as c\n" +
                ") a order by Date").list();

        for(java.sql.Date dat:result){
            Query q1 = session.createSQLQuery("insert into report_dates values(:d)");
            q1.setParameter("d", dat);
            q1.executeUpdate();
        }
    }



    public MissionFindResponse missionFind(long missionId){
        MissionFindResponse response = new MissionFindResponse();
        MissionInfo result;
        Mission mission = missionRepository.findOne(missionId);
        if (mission != null) {
            result = ModelsUtils.toModel(mission);

            Estimate estimate = estimateRepository.findByMission(mission);
            if(estimate!=null){
                result.setRating(estimate.getGeneral());
            }
            response.setMissionInfo(result);
        }
        return response;
    }





    public TerminalConfigurationResponse terminalConfiguration(long missionId, long terminalId){
        TerminalConfigurationResponse response = new TerminalConfigurationResponse();
        MissionInfo result;

        Mission mission = missionRepository.findByMissionAndTerminal(missionId, terminalId);

        if (mission != null) {
            Client client = mission.getClientInfo();
              if(client!=null){
                  String genString =  StrUtils.generateAlphaNumString(5);
                  String security_token = TokenUtil.getMD5("fractal"+genString);
                  client.setToken(security_token);
                  clientRepository.save(client);

                  response.setSecurity_token(security_token);
                  result = ModelsUtils.toModel(mission);
                  response.setMissionInfo(result);
                  response.setDeviceId(getClientDeviceId(client));
                  response.getErrorCodeHelper().setErrorCode(0);
                  response.getErrorCodeHelper().setErrorMessage("");
              }else{
                  response.getErrorCodeHelper().setErrorCode(2);
                  response.getErrorCodeHelper().setErrorMessage("Клиент не найден");
              }
        }else{
            response.getErrorCodeHelper().setErrorCode(1);
            response.getErrorCodeHelper().setErrorMessage("Нет миссии связанной с данным терминалом");
        }
          return response;
    }





    public long getClientDeviceId(Client client) {
        long deviceId=0;
        Set<DeviceInfo> deviceInfoSet = client.getDevices();
        if (deviceInfoSet != null) {
            List<DeviceInfo> myList = Lists.newArrayList(deviceInfoSet.iterator());
            if (!myList.isEmpty()) {
                DeviceInfo di = myList.get(0);
                if (di != null) {
                    deviceId =di.getId();
                }
            }
        }
        return deviceId;
    }



    private Client startRegisterSite(Client client, ClientInfo clientInfo, DeviceInfoModel deviceInfoModel, String phone){
        String smsCode = generateSMS();
        clientInfo.setPhone(phone);
        client = fromModel(clientInfo, client);
        client.setSmsCode(smsCode);
        client.setPassword(clientInfo.getPassword());
        client.setAdministrativeState(Client.State.ACTIVE);

        serviceSMSNotification.registrationConfirm(phone, smsCode, "");

        Account account = billingService.createClientAccountWithBonuses(0);
        client.setAccount(account);

        if (deviceInfoModel != null) {
            client.setRegistrationTime(timeService.nowDateTime());
            client.getDevices().clear();
            clientRepository.save(client);

            DeviceInfo deviceInfo = devicesService.register(client.getDevices(), deviceInfoModel);

            if (deviceInfo != null) {
                client.getDevices().add(deviceInfo);
            }
        }

        String pictureUrl = savePicture(client.getId(), clientInfo.getPicure(), false);
        if (pictureUrl != null) {
            client.setPicureUrl(pictureUrl);
        }
        client = clientRepository.save(client);

        return client;
    }



    public RegistrationInfoSiteResponse  registerClientOnSite(ClientInfo clientInfo, DeviceInfoModel deviceInfoModel) {
        RegistrationInfoSiteResponse response = new RegistrationInfoSiteResponse();
        String phone = PhoneUtils.normalizeNumber(clientInfo.getPhone());

      if(phone!=null){
        Client client = clientRepository.findByPhone(phone);
          if (client == null) {

              client = startRegisterSite(new Client(), clientInfo, deviceInfoModel, phone);

              Set<DeviceInfo> deviceInfoSet = client.getDevices();
              if (deviceInfoSet != null) {
                  List<DeviceInfo> myList = Lists.newArrayList(deviceInfoSet.iterator());
                  if (!myList.isEmpty()) {
                      DeviceInfo di = myList.get(0);
                      response.setDeviceId(di.getId());
                  }
              }
              response.setClientId(client.getId());
              response.setSmsCode(client.getSmsCode());
              response.getErrorCodeHelper().setErrorCode(0);
              response.getErrorCodeHelper().setErrorMessage("Регистрация прошла успешно");
          }else{

              if(client.getRegistrationState().equals(Client.RegistrationState.TERMINAL_CONFIRMED) || client.getRegistrationState().equals(Client.RegistrationState.TERMINAL_NEW)){
                  // есть такой клиент в базе, который регился с терминала
                  client = startRegisterSite(client, clientInfo, deviceInfoModel, phone);
                  response.getErrorCodeHelper().setErrorCode(2);
                  response.getErrorCodeHelper().setErrorMessage("Регистрация прошла успешно");
                  response.setClientId(client.getId());
              }else if(Client.RegistrationState.NEW.equals(client.getRegistrationState())){
                  response.getErrorCodeHelper().setErrorMessage("Пользователь с таким номером уже существует");
                  response.getErrorCodeHelper().setErrorCode(1);
                  response.setClientId(-1);
              }
          }
    }else{
          response.setClientId(-1);
          response.getErrorCodeHelper().setErrorCode(3);
          response.getErrorCodeHelper().setErrorMessage("Данный телефонный код запрещен к регистрации");
    }
        return response;
    }





    public SMSCodeConfirmTerminalResponse smsCodeConfirmTerminal(String phone, String smsCode, String security_token){
        SMSCodeConfirmTerminalResponse response = new SMSCodeConfirmTerminalResponse();
        String phoneNormalized = PhoneUtils.normalizeNumber(phone);
        String phoneNormalized_T = phoneNormalized+"_t";
        if(phoneNormalized!=null) {
            Client client = clientRepository.findByPhone(phoneNormalized_T);
            if (client != null) {
                if (client.getToken() != null && client.getToken().equals(security_token)) {
                    if (client.getSmsCode() != null) {
                        if (client.getSmsCode().equals(smsCode)) {
                            client.setSmsCode(null);
                            client.setRegistrationState(Client.RegistrationState.TERMINAL_CONFIRMED);//TERMINAL_CONFIRMED
                            clientRepository.save(client);
                            response.setResult(true);
                            response.setClientId(client.getId());
                            response.getErrorCodeHelper().setErrorCode(0);
                            response.getErrorCodeHelper().setErrorMessage("");
                        }else{
                            response.getErrorCodeHelper().setErrorCode(1);
                            response.getErrorCodeHelper().setErrorMessage("sms code not equal");
                        }
                    }
                }else{
                    response.getErrorCodeHelper().setErrorCode(3);
                    response.getErrorCodeHelper().setErrorMessage("Tokens are not equal");
                }
            }
        }
           return response;
    }




    public String sendRegistrationTextOnEmail(Client client) throws IOException {
        Document doc = Jsoup.connect("http://taxisto.ru/mail/reg/index.html").get();
              if(doc!=null){
                  Element divContentHeader = doc.select("div.content_header").first();
                  divContentHeader.html(client.getFirstName()+",<br/> приветствуем Вас!");
                    return doc.outerHtml();
              }else{
                    return "";
              }
    }



    public String sendPromoForActivatePrivateTariffOnEmail(Client client, String promo, int typeEmail) throws IOException {
        /* typeEmail: 1 - thanks, 2 - sorry, 3 - welcome, 4 - often, 5 - lowcost_sometimes, 6 - какое-то письмо с именем клиента*/
        Document doc = null;
        String result = "";
        if(typeEmail == 1){
            doc = Jsoup.connect("http://www.taxisto.ru/mail/lowcost_thankyou/index.html").get();
            Element promoEl = doc.select("span.promo_code").first();
            promoEl.html(promo);
        }else if(typeEmail == 2){
            doc = Jsoup.connect("http://www.taxisto.ru/mail/lowcost_sorry/index.html").get();
            Element promoEl = doc.select("span.promo_code").first();
            promoEl.html(promo);
        }else if(typeEmail == 3){
            doc = Jsoup.connect("http://www.taxisto.ru/mail/lowcost_welcome/index.html").get();
            Element promoEl = doc.select("span.promo_code").first();
            promoEl.html(promo);
        }else if(typeEmail == 4){
            doc = Jsoup.connect("http://www.taxisto.ru/mail/lowcost_often/index.html").get();
            Element promoEl = doc.select("span.promo_code").first();
            promoEl.html(promo);
        }else if(typeEmail == 5){
            doc = Jsoup.connect("http://www.taxisto.ru/mail/lowcost_sometimes/index.html").get();
            Element promoEl = doc.select("span.promo_code").first();
            promoEl.html(promo);
        }else if(typeEmail == 6){
            doc = Jsoup.connect("http://www.taxisto.ru/mail/lowcost_thankyou_again/index.html").get();
        }else if(typeEmail == 7){
            doc = Jsoup.connect("http://taxisto.ru/mail/lowcost_thankyou_thrid_again/index.html").get();
        }else if(typeEmail == 8){
            // +100 руб за несостоявшуюся поездку
            doc = Jsoup.connect("http://www.taxisto.ru/mail/sorry_bonus_april/index.html").get();
        }else if(typeEmail == 9){
            doc = Jsoup.connect("http://taxisto.ru/mail/fourth_thank_you/index.html").get();
        }

        if(doc!=null){
            Element clientName = doc.select("span.client_name").first();
            clientName.html(client.getFirstName());
            result = doc.outerHtml();
        }
        return result;
    }




    public String sendPaymentTextOnEmail(Mission mission, String security_token) throws IOException {
        Document doc = Jsoup.connect("http://www.taxisto.ru/mail/order-bill-with-receipt-button/index.html").get(); // www.taxisto.ru/mail/order-bill/index.html
        if(doc!=null){
            /*
    <div class="client_name" style="z-index: 999; font-family: arial, 'MS Reference Sans Serif', verdana, sans-serif; font-weight: bold; font-size: 17px; margin-top: -701px; margin-left: 88px;">Иванов Иван</div>
    <div class="pay_var" style="z-index: 999; font-family: arial, 'MS Reference Sans Serif', verdana, sans-serif; font-weight: bold; font-size: 17px; margin-top: -17px; margin-left: 414px;">MasterCard ************3476</div>
    <div class="order_date" style="z-index: 999; font-family: arial, 'MS Reference Sans Serif', verdana, sans-serif; font-weight: bold; font-size: 17px; margin-top: 42px; margin-left: 88px;">30 мая 2014, 14:44 МСК</div>
    <div class="addr_from" style="z-index: 999; font-family: arial, 'MS Reference Sans Serif', verdana, sans-serif; font-weight: bold; font-size: 17px; margin-top: 99px; margin-left: 88px;">Красный Проспект, 200</div>
    <div class="addr_to" style="z-index: 999; font-family: arial, 'MS Reference Sans Serif', verdana, sans-serif; font-weight: bold; font-size: 17px; margin-top: 43px; margin-left: 88px;">Сибиряков-Гвардейцев, 39, подъед 7</div>
    <div class="distance" style="z-index: 999; font-family: arial, 'MS Reference Sans Serif', verdana, sans-serif; font-weight: bold; font-size: 17px; margin-top: 42px; margin-left: 88px;">29 км</div>
    <div class="waiting_time" style="z-index: 999; font-family: arial, 'MS Reference Sans Serif', verdana, sans-serif; font-weight: bold; font-size: 17px; margin-top: 42px; margin-left: 88px;">16 мин</div>
    <div class="pauses" style="z-index: 999; font-family: arial, 'MS Reference Sans Serif', verdana, sans-serif; font-weight: bold; font-size: 17px; margin-top: 42px; margin-left: 88px;">1</div>
    <div class="driver" style="z-index: 999; font-family: arial, 'MS Reference Sans Serif', verdana, sans-serif; font-weight: bold; font-size: 17px; margin-top: -260px; margin-left: 414px;">Микко Хакконен</div>
    <div class="car" style="z-index: 999; font-family: arial, 'MS Reference Sans Serif', verdana, sans-serif; font-weight: bold; font-size: 17px; margin-top: 44px; margin-left: 414px;">NISSAN JUKE</div>
    <div class="time_in_trip_all" style="z-index: 999; font-family: arial, 'MS Reference Sans Serif', verdana, sans-serif; font-weight: bold; font-size: 17px; margin-top: 42px; margin-left: 239px;">35 мин</div>
    <div class="waiting_time_notree" style="z-index: 999; font-family: arial, 'MS Reference Sans Serif', verdana, sans-serif; font-weight: bold; font-size: 17px; margin-top: 42px; margin-left: 239px;">5 мин</div>
    <div class="services" style="z-index: 999; font-family: arial, 'MS Reference Sans Serif', verdana, sans-serif; font-weight: bold; font-size: 17px; margin-top: 42px; margin-left: 239px;">Курящий салон, провоз крупных животных</div>
    <div class="services" style="z-index: 999; font-family: arial, 'MS Reference Sans Serif', verdana, sans-serif; font-weight: bold; font-size: 21px; margin-top: 59px; margin-left: 88px;">Сумма к оплате: 250 руб</div>
             */
            Element divClientName = doc.select("span.client_name").first();
            divClientName.html(mission.getClientInfo().getFirstName());

            Element divPayVar = doc.select("span.pay_var").first();
               if(mission.getPaymentType().equals(PaymentType.CARD)){
                   divPayVar.html(mission.getClientInfo().getFirstName());
                   ClientCard clientCard = clientCardRepository.findByClientAndActive(mission.getClientInfo(), Boolean.TRUE);
                      if(clientCard!=null){
                          String dumb = "********";
                          divPayVar.html(getCCTypeForPaymentLetter(clientCard.getPan())+" "+dumb+clientCard.getPan().substring(8, clientCard.getPan().length()));
                      }else{
                          divPayVar.html("Безнал");
                      }
               } else if(mission.getPaymentType().equals(PaymentType.CASH)){
                          divPayVar.html("Наличными");
               }

            Element divOrderDate = doc.select("span.order_date").first();
            //DateTimeFormatter dtfOut = DateTimeFormat.forPattern("dd MMM yyyy, HH:mm МСК"); // 30 мая 2014, 14:44 МСК
            //LOGGER.info(dt.toString("dd MMM yyyy, HH:mm МСК", new Locale("ru")));

            if(mission.getTimeOfRequesting()!=null){
                  divOrderDate.html(mission.getTimeOfRequesting().toString("dd MMM yyyy, HH:mm", new Locale("ru")));
              }


            Element addr_from = doc.select("span.addr_from").first();
            addr_from.html(mission.getLocationFrom().getAddress());

            Element addr_to = doc.select("span.addr_to").first();
               if(mission.getLocationTo()!=null && mission.getLocationTo().getAddress()!=null){
                   addr_to.html(mission.getLocationTo().getAddress());
               }else{
                   addr_to.html("");
               }


            Element distance = doc.select("span.distance").first();
            double distanceInKM = mission.getStatistics().getDistanceInFact()/1000.00;
            BigDecimal x = new BigDecimal(distanceInKM);
            BigDecimal roundedDistance = x.round(new MathContext(MathUtil.countDigit(x), RoundingMode.UP));
            distance.html(""+roundedDistance.intValue()+" км");

            Element waiting_time = doc.select("span.waiting_time").first();
            int waitMinDriver = commonService.waitDriverClientInMinutes(mission);
               if(waitMinDriver!=0){
                   waiting_time.html(waitMinDriver+" мин");
               }else{
                   waiting_time.html("");
               }

            Element pauses = doc.select("span.pauses").first();
              if(mission.getStatistics().getPauses()!=null){
                  pauses.html(""+mission.getStatistics().getPauses().size());
              }else{
                  pauses.html("");
              }

            Element driver = doc.select("span.driver").first();
              if(mission.getDriverInfo()!=null){
                  driver.html(mission.getDriverInfo().getFirstName()+" "+mission.getDriverInfo().getLastName());
              }

            Element car = doc.select("span.car").first();
            car.html(mission.getDriverInfo().getAutoModel());

            Element time_in_trip_all = doc.select("span.time_in_trip_all").first();
            time_in_trip_all.html("" + Math.abs(timeInTripInMinutes(mission)) + " мин");

            int freeWaitMinutes = commonService.getFreeWaitMinutesByAutoClass(mission);
            int diff = freeWaitMinutes - waitMinDriver;
            Element waiting_time_notree = doc.select("span.waiting_time_notree").first();

              if(diff>=0){
                  waiting_time_notree.html("");
              }else{
                  diff = Math.abs(diff);
                  waiting_time_notree.html(""+diff+" мин");
              }

            Element servicesFirst = doc.select("span.services").first();
            StringBuilder stringBuilder = new StringBuilder();
            List<ServicePriceInfo> usedServices = usedServicesList(mission);
               int k=0;
               for(int i=0;i<usedServices.size();i++){
                   ServicePriceInfo servicePriceInfo = usedServices.get(i);
                   stringBuilder.append(servicePriceInfo.getName() != null ? servicePriceInfo.getName() : "");
                        if(k<usedServices.size()-1){
                            stringBuilder.append(", ");
                        }
                     k++;
               }
            servicesFirst.html(stringBuilder.toString());

            Element sum = doc.select("span.sum").last();
            sum.html((int)mission.getStatistics().getPriceInFact().getAmount().doubleValue()+" руб");

            Element link = doc.select("a").first();
            link.attr("href", String.format("http://www.taxisto.ru/order/strict-accountability/%s/%s/", mission.getId(), security_token));
            //String relHref = link.attr("href");
            //System.out.println("relHref= "+relHref);
            //link.html(String.format("http://www.taxisto.ru/order/strict-accountability/%s/%s/", mission.getId(), security_token));

            return doc.outerHtml();
        }else{
            return "";
        }
    }




    public PaymentLetterResponse paymentLetter(String security_token, long missionId, boolean toClient) throws IOException {
        String token = TokenUtil.getMD5("fractal" + missionId);
        if(!token.equals(security_token)){
            throw new CustomException(3, "Нарушение безопасности");
        }
        PaymentLetterResponse response = new PaymentLetterResponse();
            Mission mission = missionRepository.findOne(missionId);
            if(mission == null) {
                throw new CustomException(1,"Миссия не найдена");
            }
            String resultHtml;
            String subject;
            String email;
                  if(toClient){
                      resultHtml = sendPaymentTextOnEmail(mission, token);
                      subject = "Taxisto: детализация поездки";
                      email = mission.getClientInfo().getEmail();
                  }else{
                      resultHtml = String.format("Клиент %s[id: %s] запросил бланк строгой отчетности по заказу %s", mission.getClientInfo().getFirstName(), mission.getClientInfo().getId(), mission.getId());
                      subject = "Taxisto: запрос бланка строгой отчетности";
                      email = commonService.getPropertyValue("info_mail");
                  }
                  if (resultHtml.equals("")) {
                      throw new CustomException(2,"Не удалось найти исходный документ");
                  }
            SendEmailUtil.sendEmail(email, resultHtml, subject);
              return response;
    }




    public RegistrationLetterResponse registrationLetter(long clientId) throws IOException, EmailException {
        RegistrationLetterResponse response = new RegistrationLetterResponse();
          Client client =clientRepository.findOne(clientId);
           if(client!=null){
               String resultHtml = sendRegistrationTextOnEmail(client);
                  if(!resultHtml.equals("")){
                      response.setHtml(resultHtml);
                      //serviceEmailNotification.sendSimpleEmail(client.getEmail(), "Вы успешно зарегистрированы в Таксисто", resultHtml); // (String emailTo, String subject, String text ){
                      SendEmailUtil.sendEmail(client.getEmail(),resultHtml, "Taxisto: успешная регистрация"); //
                      response.getErrorCodeHelper().setErrorCode(0);
                      response.getErrorCodeHelper().setErrorMessage("");
                  }else{
                      response.getErrorCodeHelper().setErrorCode(2);
                      response.getErrorCodeHelper().setErrorMessage("Не удалось найти исходный документ");
                  }
           }else{
               response.setHtml("");
               response.getErrorCodeHelper().setErrorCode(1);
               response.getErrorCodeHelper().setErrorMessage("Клиент не найден");
           }
             return response;
    }





    public SmsSendMissionInfoResponse smsMissionInfo(long missionId, String security_token){
        SmsSendMissionInfoResponse response = new SmsSendMissionInfoResponse();
        Mission mission = missionRepository.findOne(missionId);
           if(mission!=null){
               Client client = mission.getClientInfo();
               if (client != null) {
                   if (client.getToken() != null && client.getToken().equals(security_token)) {
                       Driver driver = mission.getDriverInfo();
                          if(driver!=null){
                              DriverInfo driverInfo = ModelsUtils.toModel(driver);
                              serviceSMSNotification.smsMissionDetailsInfo(client.getPhone(), driverInfo, mission, "");
                              response.getErrorCodeHelper().setErrorCode(0);
                              response.getErrorCodeHelper().setErrorMessage("");
                          }else{
                              response.getErrorCodeHelper().setErrorCode(2);
                              response.getErrorCodeHelper().setErrorMessage("У текущей миссии нет водителя");
                          }
                   }else{
                       response.getErrorCodeHelper().setErrorCode(3);
                       response.getErrorCodeHelper().setErrorMessage("Tokens are not equal");
                   }
               }
           }else{
               response.getErrorCodeHelper().setErrorCode(1);
               response.getErrorCodeHelper().setErrorMessage("Mission not found");
           }

         return response;
    }





   public SMSCodeRepeateTerminalResponse  smsCodeRepeateTerminal(String phone, String security_token){
       SMSCodeRepeateTerminalResponse response = new SMSCodeRepeateTerminalResponse();
       String phoneNormalized = PhoneUtils.normalizeNumber(phone);
       String phoneNormalizedT = phoneNormalized+"_t";
       if(phoneNormalized!=null) {
           Client client = clientRepository.findByPhone(phoneNormalizedT);
           if (client != null) {
               if (client.getToken() != null && client.getToken().equals(security_token)) {
                   String smsCode = generateSMS();
                   client.setSmsCode(smsCode);
                   clientRepository.save(client);

                   SMSMessage smsMessage = createSMSMessage("", client);
                   serviceSMSNotification.repeateSmsCodeTerminal(phoneNormalized, smsCode, Integer.toString(smsMessage.getId()));
                   smsMessage.setSmsText("Повторный код подтверждения Таксисто: " + smsCode + ".");
                   smsMessageRepository.save(smsMessage);

                   response.setResult(true);
                   response.setSmsCode(smsCode);
               }else{
                   response.getErrorCodeHelper().setErrorCode(3);
                   response.getErrorCodeHelper().setErrorMessage("Tokens are not equal");
               }
           }
       }
          return response;
    }








    public StartMissionTerminalResponse startMissionTerminal(String phone){
        StartMissionTerminalResponse response = new StartMissionTerminalResponse();
        String phoneNormalized = PhoneUtils.normalizeNumber(phone);
        String smsCode = generateSMS();

        String phoneNormalized_T = phoneNormalized+"_t";

        Client client = new Client();

        if(phoneNormalized!=null) {
            Client clientDB = clientRepository.findByPhone(phoneNormalized_T);
            String genString =  StrUtils.generateAlphaNumString(5);
            String security_token = TokenUtil.getMD5("fractal"+genString);

            if (clientDB == null){
                client.setFirstName("Terminal client");
                client.setLastName("Terminal client");
                client.setPhone(phoneNormalized_T);
                client.setRegistrationState(Client.RegistrationState.TERMINAL_NEW);//TERMINAL_NEW
                client.setAdministrativeState(Client.State.INACTIVE);
                client.setGender(Client.Gender.UNDEFINED);
                client.setSmsCode(smsCode);
                Account account = billingService.createClientAccountWithBonuses(0);
                client.setAccount(account);
                client.setToken(security_token); // здесь поставить более сложную генерацию токена

                DeviceInfoModel deviceInfoModel = buildDefaultDeviceInfo();

                if (deviceInfoModel != null){
                    client.setRegistrationTime(timeService.nowDateTime());
                    client.getDevices().clear();
                    clientRepository.save(client);

                    DeviceInfo deviceInfo = devicesService.register(client.getDevices(), deviceInfoModel);
                    if (deviceInfo != null){
                        client.getDevices().add(deviceInfo);
                    }
                        clientRepository.save(client);
                }
                long deviceId = getDeviceId(client.getDevices());
                response.setDeviceId(deviceId);
                response.setClientId(client.getId());
                response.setSecurity_token(security_token);
                clientRepository.save(client);
            }else{
                //clientDB.setToken(security_token);
                clientDB.setSmsCode(smsCode);
                long deviceId = getDeviceId(clientDB.getDevices());
                response.setDeviceId(deviceId);
                response.setClientId(clientDB.getId());
                response.setSecurity_token(clientDB.getToken());
                clientRepository.save(clientDB);
            }
                response.getErrorCodeHelper().setErrorCode(0);
                response.getErrorCodeHelper().setErrorMessage("Код подтверждения отправлен");
                response.setSmsCode(smsCode);

                serviceSMSNotification.terminalStartMission(phoneNormalized, smsCode, "");
        }else{
           response.getErrorCodeHelper().setErrorCode(1);
           response.getErrorCodeHelper().setErrorMessage("Данный телефонный код запрещен к регистрации");
           response.setClientId(-1);
        }
        return response;
    }




   private long getDeviceId(Set<DeviceInfo> deviceInfoSet){
        long result = 0;
        if(deviceInfoSet!=null){
            List<DeviceInfo> myList = Lists.newArrayList(deviceInfoSet.iterator());
            if(!myList.isEmpty()){
                DeviceInfo di = myList.get(0);
                 result = di.getId();
            }
        }
            return result;
    }


    private DeviceInfoModel buildDefaultDeviceInfo() {
        DeviceInfoModel model = new DeviceInfoModel();
        model.setDeviceType(4);
        model.setNewToken("terminal_new_token"+DateTime.now());
        model.setOldToken("terminal_old_token" + DateTime.now());
           return model;
    }


    /*
     Новый вариант с учетом терминальных юзеров
     public Client registerClient(ClientInfo clientInfo, DeviceInfoModel deviceInfoModel) {
        RegistrationInfoResponse response = new RegistrationInfoResponse();
        String phone = PhoneUtils.normalizeNumber(clientInfo.getPhone());

        Client client = clientRepository.findByPhone(phone);

        if (client == null) {
               // нет такого клиента в базе
               client = startRegisterMobile(new Client(), clientInfo, deviceInfoModel, phone);
        }else{
               if(client.getRegistrationState().equals(Client.RegistrationState.TERMINAL_CONFIRMED) || client.getRegistrationState().equals(Client.RegistrationState.TERMINAL_NEW)){
                  // есть такой клиент в базе, который регился с терминала
                  client = startRegisterMobile(client, clientInfo, deviceInfoModel, phone);
               }else{
                  client= new Client();
                  client.setId(-1l);
               }
        }
        return client;
    }
     */





   public Client registerClient(ClientInfo clientInfo, DeviceInfoModel deviceInfoModel, String userIp) {
        //RegistrationInfoResponse response = new RegistrationInfoResponse();
       Client client = null;
       try {
           String phone = PhoneUtils.normalizeNumber(clientInfo.getPhone());
           if (phone != null) {
               client = clientRepository.findByPhone(phone);

               if (client == null) {
                   String smsCode = generateSMS();
                   clientInfo.setPhone(phone);
                   client = fromModel(clientInfo, new Client());
                   client.setSmsCode(smsCode);
                   client.setPassword(clientInfo.getPassword());
                   client.setAdministrativeState(Client.State.ACTIVE);

                   Account account = billingService.createClientAccountWithBonuses(0);
                   client.setAccount(account);

                   if (deviceInfoModel != null) {

                       TimeZone tz = TimeZone.getTimeZone("GMT+6");
                       Calendar calendar2 = Calendar.getInstance();
                       calendar2.add(Calendar.MILLISECOND, tz.getOffset(DateTime.now().toDate().getTime()));
                       DateTime nowDateTime = new DateTime(calendar2.getTime());

                       client.setRegistrationTime(nowDateTime); // timeService.nowDateTime()
                       client.getDevices().clear();
                       clientRepository.save(client);
                       DeviceInfo deviceInfo = devicesService.register(client.getDevices(), deviceInfoModel);
                       if (deviceInfo != null) {

                           client.getDevices().add(deviceInfo);

                           // Акция по 100 рублей!
                           //long regMillisec = nowDateTime.getMillis();
                           //long endAkcionTime = 1416589200000L;
                           //LOGGER.info("regMillisec="+regMillisec+" stopAkcion="+1416589200000L);
                           //if(regMillisec<endAkcionTime){
                           // начисляем бонусы
                           //Properties prop = propertiesRepository.findByPropName("count_client");
                           //int countClientInt = Integer.parseInt(prop.getPropValue());
                           //if(countClientInt==1){
                           //updateClientBonuses(client.getId(), Money.of(CurrencyUnit.of("BNS"), 100));
                           //countClientInt = countClientInt+1;
                           //prop.setPropValue(Integer.toString(countClientInt));
                           //propertiesRepository.save(prop);

                           //ClientActivatedPromoCodes clientActivatedPromoCodes  = new ClientActivatedPromoCodes();
                           //clientActivatedPromoCodes.setClientId(client.getId());
                           //clientActivatedPromoCodes.setPromoCodeId(-1);
                           //clientActivatedPromoCodes.setDateOfUsed(regMillisec);
                           //clientActivatedPromoCodesRepository.save(clientActivatedPromoCodes);
                           //}else{
                           //  LOGGER.info("Акция не стартовала");
                           //}
                           //}else{
                           //    LOGGER.info("Акция просрочена");
                           //}


                           /*
                           Бонусы для первых 500 андроид клиентов
                               if(deviceInfo.getType().equals(DeviceInfo.Type.ANDROID_CLIENT)){
                                   // если это андроид
                                          int countAndroidClient = devicesRepository.findCountByDeviceType(DeviceInfo.Type.ANDROID_CLIENT);
                                          if(countAndroidClient<=500){
                                              // начисляем 150 рублей бонусов этому клиенту
                                              updateClientBonuses(client.getId(), Money.of(CurrencyUnit.of("BNS"), 200));
                                          }
                               }
                           */
                       }
                   }

                   client = clientRepository.save(client);

                   String pictureUrl = savePicture(client.getId(), clientInfo.getPicure(), false); // false - registration, true - update
                   if (pictureUrl != null) {
                       client.setPicureUrl(pictureUrl);
                   }
                   client = clientRepository.save(client);

                   /* save to db user IP*/
                   if(!StringUtils.isEmpty(userIp)){
                       // save in db user ip
                       alternativeStatisticsRepository.save(new AlternativeStatistics("reg_c_ip", userIp, DateTimeUtils.toDate(DateTimeUtils.nowNovosib_GMT6())));
                   }
                   /* end */

                   //serviceSMSNotification.registrationConfirm(phone, smsCode, "");
                   /* sms send by smsc */
                   sendSMS(phone, "Код подтверждения вашего аккаунта Таксисто: " + smsCode + ".", client);
                   /* end */

               } else {
                   if (!client.getRegistrationState().equals(Client.RegistrationState.NEW)) {
                       client = new Client();
                       client.setId(-1l);
                   } else {
                       String smsCode = generateSMS();
                       client.setSmsCode(smsCode);
                           /* sms send by smsc */
                       sendSMS(phone, "Код подтверждения вашего аккаунта Таксисто: " + smsCode + ".", client);
                           /* end */
                       client = clientRepository.save(client);
                   }
               }
           } else {
               client = new Client();
               client.setId(-1l);
           }
       }catch(Exception h){
           LOGGER.info("registerClient Exception %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% = "+h.getMessage());
       }
        return client;
    }


   public String getCCTypeForPaymentLetter(String ccNumber){
        String visaRegex        = "^4[0-9]{12}(?:[0-9]{3})?$";
        String masterRegex      = "^5[1-5][0-9]{14}$";
        String res=null;
        try {
            String new_str = ccNumber.replace ("**", "999999");
            res = (new_str.matches(visaRegex) ? "VISA" : new_str.matches(masterRegex) ? "MasterCard":"empty");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
   }


    public String getCCType(String ccNumber){
        LOGGER.info("~~~~~~~~~~~~~~~~~ ccNumber: "+ccNumber);
        String visaRegex        = "^4[0-9]{12}(?:[0-9]{3})?$";
        String masterRegex      = "^5[1-5][0-9]{14}$";
        String res = "";
        try {
            if(!StringUtils.isEmpty(ccNumber)){
                String new_str = ccNumber.replace ("**", "999999");
                res = (new_str.matches(visaRegex) ? "VISA" : new_str.matches(masterRegex) ? "MASTER":"empty");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }



    private SMSMessage createSMSMessage(String smsText, Client client){
        SMSMessage smsMessage = new SMSMessage();
        smsMessage.setSmsText(smsText);
        smsMessage.setClient(client);
        smsMessage.setCountTry(1);
        TimeZone tz = TimeZone.getTimeZone("GMT+6");
        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.MILLISECOND, tz.getOffset(DateTime.now().toDate().getTime()));
        DateTime nowDateTime = new DateTime(calendar2.getTime());
        smsMessage.setTimeOfCreate(nowDateTime);
        smsMessage.setTimeOfDelivery(0);
        smsMessageRepository.save(smsMessage);
        return smsMessage;
    }





    public void sendSMS(String phone, String message, Client client){

        //Session session = em.unwrap(Session.class);

//        List<java.sql.Date> result = session.createSQLQuery("    select a.Date\n" +
//                "from (\n" +
//                "    select DATE_SUB(curdate(), INTERVAL 5 MONTH) + INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as Date\n" +
//                "    from (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9 ) as a\n" +
//                "    cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as b\n" +
//                "    cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as c\n" +
//                ") a order by Date").list();
//
//
//        for(java.sql.Date dat:result){
//            Query q1 = session.createSQLQuery("insert into report_dates values(:d)");
//            q1.setParameter("d", dat);
//            q1.executeUpdate();
//
//            LOGGER.info(dat);
//        }



        SMSMessage smsMessage = createSMSMessage(message, client);

        String[] strings = smsc.send(phone, message, 0, "",Integer.toString(smsMessage.getId()), 0, "", "");
        int code = Integer.parseInt(strings[1]);

        if (!strings[1].equals("") && Integer.parseInt(strings[1]) >= 0) {
                  // отправлено
                 smsMessage.setErrorMessageSMS_C("Сообщение отправлено");
                 //smsc.smsStatusSendSMSC(smsMessage);
                 smsMessageRepository.save(smsMessage);
            } else{
                switch(code){
                    case -1:{
                        smsMessage.setErrorMessageSMS_C("Ошибка в параметрах");
                        smsMessageRepository.save(smsMessage);
                        break;
                    }
                    case -2:{
                        smsMessage.setErrorMessageSMS_C("Неверный логин или пароль");
                        smsMessageRepository.save(smsMessage);
                        break;
                    }
                    case -3:{
                        smsMessage.setErrorMessageSMS_C("Недостаточно средств на счете Клиента");
                        smsMessageRepository.save(smsMessage);
                        break;
                    }
                    case -4:{
                        smsMessage.setErrorMessageSMS_C("IP-адрес временно заблокирован из-за частых ошибок в запросах");
                        smsMessageRepository.save(smsMessage);
                        break;
                    }
                    case -5:{
                        smsMessage.setErrorMessageSMS_C("Неверный формат даты");
                        smsMessageRepository.save(smsMessage);
                        break;
                    }
                    case -6:{
                        smsMessage.setErrorMessageSMS_C("Сообщение запрещено (по тексту или по имени отправителя)");
                        smsMessageRepository.save(smsMessage);
                        break;
                    }
                    case -7:{
                        smsMessage.setErrorMessageSMS_C("Неверный формат номера телефона");
                        smsMessageRepository.save(smsMessage);
                        break;
                    }
                    case -8:{
                        smsMessage.setErrorMessageSMS_C("Сообщение на указанный номер не может быть доставлено");
                         smsMessageRepository.save(smsMessage);
                         break;
                    }
                    case -9:{
                        smsMessage.setErrorMessageSMS_C("Отправка более одного одинакового запроса на передачу SMS");
                          smsMessageRepository.save(smsMessage);
                            break;
                    }
                    default: break;
                }
            }

        //LOGGER.info("strings= "+strings);
    }



/*
    1	Ошибка в параметрах.
    2	Неверный логин или пароль.
    3	Недостаточно средств на счете Клиента.
    4	IP-адрес временно заблокирован из-за частых ошибок в запросах. Подробнее
    5	Неверный формат даты.
    6	Сообщение запрещено (по тексту или по имени отправителя).
    7	Неверный формат номера телефона.
    8	Сообщение на указанный номер не может быть доставлено.
    9	Отправка более одного одинакового запроса на передачу SMS-сообщения либо более пяти одинаковых запросов на получение стоимости сообщения в течение минуты.
  */




    private Client fromModel(ClientInfo clientInfo, Client client) {
        return ModelsUtils.fromModel(clientInfo, client); // new Client()
    }



    /*
    //              DeviceInfoModel deviceInfoModel2 = new DeviceInfoModel();
//              deviceInfoModel2.setDeviceType(1);
//              deviceInfoModel2.setNewToken(StrUtils.generateAlphaNumString(4));
     */

    private Client startRegisterMobile(Client client, ClientInfo clientInfo, DeviceInfoModel deviceInfoModel, String phone){
        String smsCode = generateSMS();
        clientInfo.setPhone(phone);
        client = fromModel(clientInfo, client);
        client.setSmsCode(smsCode);
        client.setPassword(clientInfo.getPassword());
        client.setAdministrativeState(Client.State.ACTIVE);

        serviceSMSNotification.registrationConfirm(phone, smsCode, "");

        Account account = billingService.createClientAccountWithBonuses(0);
        client.setAccount(account);

        if (deviceInfoModel != null){
            client.setRegistrationTime(timeService.nowDateTime());
            client.getDevices().clear();
            clientRepository.save(client);
            DeviceInfo deviceInfo = devicesService.register(client.getDevices(), deviceInfoModel);
            if (deviceInfo != null){
                client.getDevices().add(deviceInfo);
            }
        }
        client = clientRepository.save(client);
        String pictureUrl = savePicture(client.getId(), clientInfo.getPicure(), false); // false - registration, true - update
        if (pictureUrl != null) {
            client.setPicureUrl(pictureUrl);
        }
        client = clientRepository.save(client);
          return client;
    }


    /*
         String smsCode = generateSMS();
            clientInfo.setPhone(phone);
            client = fromModel(clientInfo);
            client.setSmsCode(smsCode);
            client.setPassword(clientInfo.getPassword());
            client.setAdministrativeState(Client.State.ACTIVE);

            serviceSMSNotification.registrationConfirm(phone, smsCode);

            Account account = billingService.createClientAccountWithBonuses(0);
            client.setAccount(account);

            if (deviceInfoModel != null){
                client.setRegistrationTime(timeService.nowDateTime());
                client.getDevices().clear();
                clientRepository.save(client);
//              DeviceInfoModel deviceInfoModel2 = new DeviceInfoModel();
//              deviceInfoModel2.setDeviceType(1);
//              deviceInfoModel2.setNewToken(StrUtils.generateAlphaNumString(4));
                DeviceInfo deviceInfo = devicesService.register(client.getDevices(), deviceInfoModel);
                if (deviceInfo != null){
                    client.getDevices().add(deviceInfo);
                }
            }
            client = clientRepository.save(client);
            String pictureUrl = savePicture(client.getId(), clientInfo.getPicure(), false); // false - registration, true - update
            if (pictureUrl != null) {
                client.setPicureUrl(pictureUrl);
            }
            client = clientRepository.save(client);
     */


    public List<ServicePrice> addServicesPrices(List<ServicePriceInfo> servicePriceInfo){
        List<ServicePrice> servicePriceList = new ArrayList<>();
        for(ServicePriceInfo services : servicePriceInfo){
            ServicePrice servicePrice = new ServicePrice();
            servicePrice.setMoney_amount(services.getPrice());
            servicePrice.setMoney_currency("RUB");
            servicePrice.setService(MissionService.getByValue(services.getOptionId()));

            //f:add
            servicePrice.setActivePicUrl(services.getActivePicUrl());
            servicePrice.setNotActivePicUrl(services.getNotActivePicUrl());
            servicePrice.setDescription(services.getDescription());
            servicePrice.setName(services.getName());
            //

            servicePriceList.add(servicePrice);
            servicesRepository.save(servicePrice);
        }
        return servicePriceList;
    }



//    // f:add
//    public List<AutoClassPrice> addAutoClassPrices(List<AutoClassRateInfo> autoClassRateInfoList){
//        List<AutoClassPrice> autoClassPriceList = new ArrayList<>();
//        for(AutoClassRateInfo autoClassRateInfo : autoClassRateInfoList){
//
//            AutoClassPrice autoClassPrice = new AutoClassPrice();
//            autoClassPrice.setAutoClass(AutoClass.getByValue(autoClassRateInfo.getAutoClass()));
//            autoClassPrice.setPrice(MoneyUtils.getRubles(autoClassRateInfo.getPrice()));
//            autoClassPrice.setKmIncluded(autoClassRateInfo.getKmIncluded());
//            autoClassPrice.setPriceKm(autoClassRateInfo.getPriceKm());
//            autoClassPrice.setPriceHour(autoClassRateInfo.getPriceHour());
//            autoClassPrice.setFreeWaitMinutes(autoClassRateInfo.getFreeWaitMinutes());
//            autoClassPrice.setPerMinuteWaitAmount(autoClassRateInfo.getPerMinuteWaitAmount());
//            autoClassPrice.setIntercity(autoClassRateInfo.getIntercity());
//
//
//            autoClassPriceList.add(autoClassPrice);
//            autoClassPriceRepository.save(autoClassPrice);
//        }
//        return autoClassPriceList;
//    }






    public WebUser registerDispatcher(WebUserModel webUserModel){
        String email = webUserModel.getEmail();//phone = PhoneUtils.normalizeNumber(clientInfo.getPhone());
//        WebUser webUser = webUserRepository.findOne(email);
        WebUser webUser = fromModel(webUserModel);
        webUserRepository.save(webUser);

//        if(webUser == null){
//            webUser.setCity(webUserModel.getCity());
//            webUser.setCountry(webUserModel.getCountry());
//            webUser.setEmail(webUserModel.getEmail());
//            webUser.setFirstName(webUser.getFirstName());
//            webUser.setLastName(webUser.getLastName());
//            webUser.setPhone(webUser.getPhone());
//            webUser.setRole(webUser.getRole());
//            webUser.setPassword(webUser.getPassword());
//            webUser.setPhoneOfManager(webUser.getPhoneOfManager());
//            webUser = webUserRepository.save(webUser);
//        }
        return webUser;
    }

    /*
     public Client registerClient(ClientInfo clientInfo, DeviceInfoModel deviceInfoModel) {
        String phone = PhoneUtils.normalizeNumber(clientInfo.getPhone());

        Client client = clientRepository.findByPhone(phone);

        if (client == null) {
//            String smsCode = generateSMS();
            String smsCode = "123";
            clientInfo.setPhone(phone);
            client = fromModel(clientInfo);
            client.setSmsCode(smsCode);
            client.setPassword(clientInfo.getPassword());
            client.setAdministrativeState(Client.State.ACTIVE);

            serviceSMSNotification.registrationConfirm(phone, smsCode);

            Account account = billingService.createClientAccountWithBonuses(1000);
            client.setAccount(account);

            if (deviceInfoModel != null){
                client.setRegistrationTime(timeService.nowDateTime());
                client.getDevices().clear();
                clientRepository.save(client);
                DeviceInfo deviceInfo = devicesService.register(client.getDevices(), deviceInfoModel);
                if (deviceInfo != null){
                    client.getDevices().add(deviceInfo);
                }
            }
            client = clientRepository.save(client);

            String pictureUrl = savePicture(client.getId(), clientInfo.getPicure());
            if (pictureUrl != null) {
                client.setPicureUrl(pictureUrl);
            }
            client = clientRepository.save(client);
        }
        return client;
    }

    */



    private String savePicture(long clientId, String picure, boolean isUpdate) {
        String pictureUrl = null;
        if (!StringUtils.isEmpty(picure)) {
            pictureUrl = profilesResourcesService.saveClientPicture(picure, clientId, isUpdate);
        }
        return pictureUrl;
    }

    private String savePictureDriver(long clientId, String picure) {
        String pictureUrl = null;
        if (!StringUtils.isEmpty(picure)) {
            pictureUrl = profilesResourcesService.saveDriverPicture(picure, clientId, false);
        }
        return pictureUrl;
    }



    public String generateSMS() {
        String smsCode;
        do {
            smsCode = StrUtils.generateSMSCode();
        } while (clientRepository.findBySmsCode(smsCode) != null);
          return smsCode;
    }



    public String generateSMSByCountSymbols(int count) {
        String smsCode;
        do {
            smsCode = StrUtils.generateSMSCodeByCountSymbol(count);
        } while (moneyWithdrawalRepository.findBySmsCode(smsCode) != null);
        return smsCode;
    }


    public UpdatePasswordSiteResponse recoveryPasswordSiteLogic(String phone, String passwordNew, String passwordOld) {
        UpdatePasswordSiteResponse response = new UpdatePasswordSiteResponse();

        ErrorCodeHelper errorCodeHelper = new ErrorCodeHelper();

        if (!StringUtils.isEmpty(phone)) {

           String phoneNormalized = PhoneUtils.normalizeNumber(phone);
            if(phoneNormalized!=null) {
                Client client = clientRepository.findByPhoneAndPassword(phoneNormalized, passwordOld);
                if (client != null) {
                    client.setPassword(passwordNew);
                    clientRepository.save(client);
                    errorCodeHelper.setErrorMessage("Пароль успешно сохранен");
                    errorCodeHelper.setErrorCode(0);
                    response.setErrorCodeHelper(errorCodeHelper);
                } else {
                    // пользователя с такими данными не существует
                    errorCodeHelper.setErrorMessage("Клиент не найден");
                    errorCodeHelper.setErrorCode(1);
                    response.setErrorCodeHelper(errorCodeHelper);
                }
            }else{
                LOGGER.info("phone number is not valid");
            }
        }else{
            errorCodeHelper.setErrorMessage("Пустой номер телефона");
            errorCodeHelper.setErrorCode(2);
            response.setErrorCodeHelper(errorCodeHelper);
        }
        return response;
    }


    public String showTarifByVersionApp(String version, String clientType){
        String result ="";
        VersionsApp versionsApp = versionsAppRepository.findByVersionAndClientType(version, clientType);
        if(versionsApp!=null){
            result = versionsApp.getShowTarif();
        }
        return result;
    }


    @Transactional
    public static boolean useLoop(String[] arr, String targetValue) {
        for(String s: arr){
            if(s.trim().equals(targetValue))
                return true;
        }
        return false;
    }





    @Transactional
    public List<MissionRateInfo> showRatesByVersionApp(List<MissionRateInfo> rates, Client client) {
        List<MissionRateInfo> afterClearRates = new ArrayList<>();
        // старй вариант
        //String clientType = DeviceUtil.getClientType(client.getDevices());
        String showVersion = showTarifByVersionApp(client.getVersionApp(), client.getDeviceType());
        if (showVersion!=null && !showVersion.isEmpty()){
            String[] versions = showVersion.split(",");
            MissionRateInfo missionRateInfo = rates.get(0);
            List<AutoClassRateInfo> autoClassRateInfoList = missionRateInfo.getAutoClassRateInfos();

            for (ListIterator<AutoClassRateInfo> i = autoClassRateInfoList.listIterator(); i.hasNext(); ) {
                AutoClassRateInfo el = i.next();
                if(!useLoop(versions, String.valueOf(el.getAutoClass()))){
                    i.remove();
                }
            }
            missionRateInfo.setAutoClassRateInfos(autoClassRateInfoList);
            afterClearRates.add(missionRateInfo);
        }
        return afterClearRates;
    }



    /* for: */
    @Transactional
    public List<MissionRateInfo> showRatesByPrivateTariff_For_Old_Version(List<MissionRateInfo> rates, Client client) {
        //PrivateTariff privateTariff = privateTariffRepository.findByClientAndActiveAndIsActivated(client, Boolean.TRUE, Boolean.TRUE);
        PrivateTariff privateTariff = privateTariffRepository.findByClientAndTariffNameAndActive(client, AutoClass.BONUS.name(), Boolean.TRUE);
        List<MissionRateInfo> afterClearRates = new ArrayList<>();
        if(privateTariff!=null){
            // задан индивидуальный тариф, возвращаем списко как есть
            return rates;
        }else{
            MissionRateInfo missionRateInfo = rates.get(0);
            List<AutoClassRateInfo> autoClassRateInfoList = missionRateInfo.getAutoClassRateInfos();
            for (ListIterator<AutoClassRateInfo> i = autoClassRateInfoList.listIterator(); i.hasNext(); ) {
                AutoClassRateInfo el = i.next();
                if(el.getAutoClass()==AutoClass.BONUS.getValue()){
                    i.remove();
                }
            }
            missionRateInfo.setAutoClassRateInfos(autoClassRateInfoList);
            afterClearRates.add(missionRateInfo);
            return afterClearRates;
        }
    }



    @Transactional
    public List<MissionRateInfoV2> showRatesByPrivateTariff(List<MissionRateInfoV2> rates, Client client) {
        //PrivateTariff privateTariff = privateTariffRepository.findByClientAndTariffNameAndActiveAndIsActivatedAndExpirationDateGreaterThan(client, AutoClass.BONUS.name(), Boolean.TRUE, Boolean.TRUE, DateTimeUtils.nowNovosib_GMT6()); //findByClientAndActiveAndIsActivated(client, Boolean.TRUE, Boolean.TRUE);
        PrivateTariff privateTariff = privateTariffRepository.findByClientAndTariffNameAndActive(client, AutoClass.BONUS.name(), Boolean.TRUE);
        List<MissionRateInfoV2> afterClearRates = new ArrayList<>();
          if(privateTariff!=null){
              // задан индивидуальный тариф, возвращаем списко как есть
              return rates;
          }else{
              MissionRateInfoV2 missionRateInfo = rates.get(0);
              List<AutoClassRateInfoV2> autoClassRateInfoList = missionRateInfo.getAutoClassRateInfos();
              for (ListIterator<AutoClassRateInfoV2> i = autoClassRateInfoList.listIterator(); i.hasNext(); ) {
                  AutoClassRateInfoV2 el = i.next();
                  if(el.getAutoClass()==AutoClass.BONUS.getValue()){
                      i.remove();
                  }
              }
              missionRateInfo.setAutoClassRateInfos(autoClassRateInfoList);
              afterClearRates.add(missionRateInfo);
              return afterClearRates;
          }
    }




    @Transactional
    public List<MissionRateInfoV2> showRatesByVersionApp_V2(List<MissionRateInfoV2> rates, Client client) {
        List<MissionRateInfoV2> afterClearRates = new ArrayList<>();

        /* получить тип клиента используя DeviceInfo из Client - старй вариант*/
        //String clientType = DeviceUtil.getClientType(client.getDevices());
        //String showVersion = showTarifByVersionApp(client.getVersionApp(), clientType);

        /* Новый вариант. Получаем тип клиента непосредственно из Client*/
        String showVersion = showTarifByVersionApp(client.getVersionApp(), client.getDeviceType());

        if (showVersion!=null && !showVersion.isEmpty()){
            String[] versions = showVersion.split(",");
            MissionRateInfoV2 missionRateInfo = rates.get(0);
            List<AutoClassRateInfoV2> autoClassRateInfoList = missionRateInfo.getAutoClassRateInfos();

            for (ListIterator<AutoClassRateInfoV2> i = autoClassRateInfoList.listIterator(); i.hasNext(); ) {
                AutoClassRateInfoV2 el = i.next();
                if(!useLoop(versions, String.valueOf(el.getAutoClass()))){
                    i.remove();
                }
            }
            missionRateInfo.setAutoClassRateInfos(autoClassRateInfoList);
            afterClearRates.add(missionRateInfo);
        }
        return afterClearRates;
    }







    public UpdatePasswordResponse recoveryPassword(String phone, String passwordNew, String smsCodeNew) {
        //String result = null;
        UpdatePasswordResponse response =new UpdatePasswordResponse();
        String phoneNormalized = PhoneUtils.normalizeNumber(phone);
           if(phoneNormalized!=null){
            Client client = clientRepository.findByPhone(phoneNormalized);
            if (client != null) {
                if (passwordNew == null && smsCodeNew == null) {
                    String smsCode = generateSMS();
                    client.setSmsCode(smsCode);
                    clientRepository.save(client);
                    //result=smsCode;
                    //LOGGER.info("generate sms = "+smsCode+" email = "+client.getEmail());
                    //boolean sent = serviceSMSNotification.passwordRecovery(phone, smsCode);  //sms way
                    response.setClientId(client.getId());
                    response.getErrorCodeHelper().setErrorCode(0);
                    response.getErrorCodeHelper().setErrorMessage("Email сообщение с кодом успешно отправлено");
                    response.setSmsCode(smsCode);
                    serviceEmailNotification.passwordRecovery(client.getEmail(), smsCode);  //email way
                } else {
                    LOGGER.info("passwordNew!=null && smsCodeNew!=null");
                    if (client.getSmsCode().equals(smsCodeNew)) {
                        LOGGER.info("client.getSmsCode().equals(smsCodeNew");
                        client.setPassword(passwordNew);
                        client.setSmsCode(null);
                        clientRepository.save(client);
                        response.setClientId(client.getId());
                        response.getErrorCodeHelper().setErrorCode(0);
                        response.getErrorCodeHelper().setErrorMessage("Пароль успешно изменен");
                        response.setSmsCode(smsCodeNew);
                        //result = smsCodeNew;
                    }else{
                        response.setClientId(-1);
                        response.getErrorCodeHelper().setErrorCode(1);
                        response.getErrorCodeHelper().setErrorMessage("Неправильный смс код");
                    }
                }
            }else{
                response.setClientId(-1);
                response.getErrorCodeHelper().setErrorCode(4);
                response.getErrorCodeHelper().setErrorMessage("Клиент не найден");
            }
                  }else{
                     response.setClientId(-1);
                     response.getErrorCodeHelper().setErrorCode(2);
                     response.getErrorCodeHelper().setErrorMessage("Данный телефонный код запрещен к использованию");
                  }
        return response;
    }




    public Client confirmClient(String phone, String sms) throws IOException {
        Client client = null;
        if (!StringUtils.isEmpty(phone)) {
            String phoneNormalized = PhoneUtils.normalizeNumber(phone);
            client = clientRepository.findByPhoneAndRegistrationStateAndSmsCode(phoneNormalized, Client.RegistrationState.NEW, sms); // findByPhoneAndRegistrationState
            if (client != null) {
                if (client.getSmsCode() != null) {
                    if (client.getSmsCode().equals(sms)) {
                        client.setRegistrationState(Client.RegistrationState.CONFIRMED);
                        client.setSmsCode(null);
                        String genString =  StrUtils.generateAlphaNumString(5);
                        client.setToken(TokenUtil.getMD5("fractal" + genString));
                        clientRepository.save(client);

                        /* log event */
                        mongoDBServices.createEvent(2, ""+client.getId(), 3, 0, "client/registration/confirm", "", "");

                        // отправляем письмо о успешной регистрации
                        String resultHtml = sendRegistrationTextOnEmail(client);
                        if(!resultHtml.equals("")){
                              if(client.getEmail()!=null){
                                  SendEmailUtil.sendEmail(client.getEmail(),resultHtml, "Taxisto: успешная регистрация");
                              }
                        }
                    }
                }
            }
        }
        return client;
    }






    public LateDriverBookedResponse lateDriverBookedMin(int lateDriverBookedMin, long missionId, String security_token){
        LateDriverBookedResponse response = new LateDriverBookedResponse();
        //Driver driver = driverRepository.findByToken(security_token);
        //if(driver == null){
        //    throw new CustomException(1, "Водитель не найден");
        //}
        Mission mission = missionRepository.findOne(missionId);
           if(mission!=null){
               int oldMin = mission.getLateDriverBookedMin();
               int newMin = oldMin+lateDriverBookedMin;
               mission.setLateDriverBookedMin(newMin);
               missionRepository.save(mission);
               response.setSuccess(true);
           }
        return response;
    }




    public void confirmBookedMission(long missionId) {
        Mission mission = missionRepository.findOne(missionId);
        if (mission != null) {
            Driver driver = mission.getDriverInfo();
            Mission currentMission = driver.getCurrentMission();
            if (currentMission != null) { //if driver confirm booked mission, but he is busy now - set mission booking state = DRIVER_APPROVED
                mission.setBookingState(Mission.BookingState.DRIVER_APPROVED);
            } else {
                mission.setBookingState(Mission.BookingState.STARTED);
                mission.setState(Mission.State.NEW);
                driver.getBookedMissions().remove(mission);
                int minutes = Minutes.minutesBetween(timeService.nowDateTime(), mission.getTimeOfStarting()).getMinutes();
                missionRepository.save(mission);
                driverRepository.save(driver);
                assignDriver(missionId, driver.getId(), minutes);
            }
        }
    }

    /**
     * Driver refused of booked mission. Set booked mission in initial sate
     * @param missionId mission
     */
    public void declineAssignedBookedMission(long missionId) {
        Mission mission = missionRepository.findOne(missionId);
        if (mission != null) {
            if(mission.getDriverInfo() != null) {
                declineMission(missionId, mission.getDriverInfo().getId(), mission.getDriverInfo().getToken());
            }

            mission.setDriverInfo(null);
            mission.setBookingState(Mission.BookingState.WAITING);
            mission.setState(Mission.State.BOOKED);
            missionRepository.save(mission);
        }
    }






    public int assignDriver(long missionId, long driverId, int arrivalTime) {
        int result = 0;
        Mission mission = missionRepository.findOne(missionId);
        if (mission != null) {
            Driver driver = driverRepository.findOne(driverId);
            if (mission.getState() == Mission.State.NEW) {
                mission.setDriverInfo(driver);
                mission.setState(Mission.State.ASSIGNED);
                mission.getExpectedArrivalTimes().add(arrivalTime);
                driver.setCurrentMission(mission);
                mission.setTimeOfAssigning(timeService.nowDateTime());
                result = arrivalTime;
            } else if (mission.getState() == Mission.State.BOOKED) {
                driver.getBookedMissions().add(mission);
                mission.setBookingState(Mission.BookingState.DRIVER_ASSIGNED);
                mission.setTimeOfAssigning(timeService.nowDateTime());
                result = arrivalTime;
            }
            if (result != 0){
                missionRepository.save(mission);
                driverRepository.save(driver);
                //notificationsService.driverAssigned(driverId, mission.getId(), arrivalTime, Mission.State.BOOKED.equals(mission.getState()));
            }
        }
        return result;
    }




    public void test(){
//        if(driverId!=null){
//            Driver driver = driverRepository.findOne(driverId);
//            LOGGER.info("driver = "+driver);
//        }
//        if(missionId!=null){
//            Mission mission = missionRepository.findOne(missionId);
//            LOGGER.info("mission = " + mission);
//        }
    }



    /*
    private int take; // 0 - может взять бронь, 1 - уже есть бронь в течении часа до или после, 2 - не хватает денег в кошельке.
    private long missionBookedId = 0; // 0 если нет броней в течении часа, id брони которая мешает взять заказ, если их 2, то 1 по времени.
    private Long missionId;
           Проверить есть ли у водителя миссия до или после этой, где начало брони меньше часа.
           И проверить кошелек водителя, он должен быть больше 200 рублей.
     */

    public CheckTakeBookedResponse checkTakeBooked(long driverId, long missionId, String security_token){
           CheckTakeBookedResponse response = new CheckTakeBookedResponse();
           Driver driver = driverRepository.findOne(driverId);
           //if(!commonService.isOkSecurityToken(driver, security_token, 2)){
               //throw new CustomException(1, "Водитель не найден");
           //}
           Mission mission = missionRepository.findOne(missionId);

           response.setMissionId(missionId);

                if(driver!=null && mission!=null){
                    Money moneyDriver =  driver.getAccount().getMoney();
                    boolean bigAmount = moneyDriver.isGreaterThan(Money.of(CurrencyUnit.of("RUB"), 50));
                        if(!bigAmount){
                            // не хватает денег в кошельке < 200
                            response.setTake(2);
                        }else{
                    List<Mission> missionBookedDriver = missionRepository.findByStateAndBookedDriverIdOrderByTimeOfStartingAsc(Mission.State.BOOKED, driver);
                    DateTime timeOfStarting = mission.getTimeOfStarting();
                    List<MissionSortUtil> missionSortUtilList = new ArrayList<>();
                          for(Mission missionDriver: missionBookedDriver){
                              Minutes minutes = Minutes.minutesBetween(missionDriver.getTimeOfStarting(), timeOfStarting);
                              int countMinutes =  Math.abs(minutes.getMinutes());

                                  if(countMinutes < 60){
                                      MissionSortUtil mSU = new MissionSortUtil(missionDriver, minutes.getMinutes()); // здесь минуты не по модулю, чтобы правильно сработала сортировка
                                      missionSortUtilList.add(mSU);
                                  }
                          }
                         if(missionSortUtilList.isEmpty()){
                             // можно взять бронь
                                response.setTake(0);
                         }else{
                             // нельзя взять бронь
                                response.setTake(1);
                             // сортировка
                             Collections.sort(missionSortUtilList, new Comparator<MissionSortUtil>() {
                                 @Override
                                 public int compare(MissionSortUtil miss1, MissionSortUtil miss2) {
                                     return new Integer(miss1.getDiffMinutes()).compareTo(new Integer(miss2.getDiffMinutes()));
                                 }
                             });
                             MissionSortUtil t = missionSortUtilList.get(0); // первая по времени
                             response.setMissionBookedId(t.getMission().getId());
                         }
                      }
                  }
          return response;
    }




    public EstimateInfoByMissionResponse estimateInfoByMission(long missionId){
        EstimateInfoByMissionResponse response = new EstimateInfoByMissionResponse();
        Mission mission = missionRepository.findByIdAndState(missionId, Mission.State.COMPLETED);
        if(mission!=null) {
                    Mission.Score missionScore = mission.getScore();
                    if(missionScore!=null && missionScore.getGeneral()!=0){
                        EstimateInfoDetails estimateInfoDetails = new EstimateInfoDetails();
                        estimateInfoDetails.setMissionId(mission.getId());
                        estimateInfoDetails.setApplication_convenience(missionScore.getApplicationConvenience());
                        estimateInfoDetails.setCleanliness(missionScore.getCleanlinessInCar());
                        estimateInfoDetails.setDriver_courtesy(missionScore.getDriverCourtesy());
                        estimateInfoDetails.setTimeOfStart(DateTimeUtils.toDate(mission.getTimeOfStarting()));
                        estimateInfoDetails.setWifi_quality(missionScore.getWifiQuality());
                        estimateInfoDetails.setGeneral(missionScore.getGeneral());
                        if(missionScore.getEstimateComment()!=null){
                            estimateInfoDetails.setEstimate_comment(missionScore.getEstimateComment());
                        }else{
                            estimateInfoDetails.setEstimate_comment("");
                        }
                response.setEstimateInfoDetails(estimateInfoDetails);
                response.getErrorCodeHelper().setErrorCode(0);
                response.getErrorCodeHelper().setErrorMessage("");
            }else{
                response.getErrorCodeHelper().setErrorCode(1);
                response.getErrorCodeHelper().setErrorMessage("По данному заказу нет оценок");
            }
        }else{
            response.getErrorCodeHelper().setErrorCode(2);
            response.getErrorCodeHelper().setErrorMessage("Миссия не найдена");
        }
           return response;
    }



/*
    public EstimateInfoResponse estimateInfoAll() {
        EstimateInfoResponse response = new EstimateInfoResponse();
        if(client!=null) {
            List<Mission> missions = missionRepository.findByClientInfoAndStateOrderByTimeOfStartingDesc(client, Mission.State.COMPLETED);
            if(missions!=null){
                for(Mission mission: missions){
                    Mission.Score missionScore = mission.getScore();
                    if(missionScore!=null && missionScore.getGeneral()!=0){
                        EstimateInfoDetails estimateInfoDetails = new EstimateInfoDetails();
                        estimateInfoDetails.setMissionId(mission.getId());
                        estimateInfoDetails.setApplication_convenience(missionScore.getApplicationConvenience());
                        estimateInfoDetails.setCleanliness(missionScore.getCleanlinessInCar());
                        estimateInfoDetails.setDriver_courtesy(missionScore.getDriverCourtesy());
                        estimateInfoDetails.setTimeOfStart(DateTimeUtils.toDate(mission.getTimeOfStarting()));
                        estimateInfoDetails.setWifi_quality(missionScore.getWifiQuality());
                        estimateInfoDetails.setGeneral(missionScore.getGeneral());
                        if(missionScore.getEstimateComment()!=null){
                            estimateInfoDetails.setEstimate_comment(missionScore.getEstimateComment());
                        }else{
                            estimateInfoDetails.setEstimate_comment("");
                        }
                        response.getEstimateInfoDetailsList().add(estimateInfoDetails);
                    }
                }
                response.getErrorCodeHelper().setErrorCode(0);
                response.getErrorCodeHelper().setErrorMessage("");
            }else{
                response.getErrorCodeHelper().setErrorCode(1);
                response.getErrorCodeHelper().setErrorMessage("Mission not found");
            }
        }else{
            response.getErrorCodeHelper().setErrorCode(2);
            response.getErrorCodeHelper().setErrorMessage("Client not found");
        }
        return response;
    }
*/

    /*
    private long missionId;
    private long driverId;
    private long clientId;
    private long timeOfRequesting;
    private long wifiQuality;
    private long applicationConvenience;
    private long driverCourtesy;
    private long cleanliness;
    private long general;
     */


    public DeviceTypeTransferResponse transferDeviceType(){
        DeviceTypeTransferResponse response = new DeviceTypeTransferResponse();
        Iterable<Client> iterable =  clientRepository.findAll();
            for(Client client :iterable){
                Set<DeviceInfo> deviceInfoSet = client.getDevices();
                    if(!deviceInfoSet.isEmpty()){
                        for(DeviceInfo deviceInfo: deviceInfoSet) {
                         if (deviceInfo != null) {
                             if (deviceInfo.getType() != null) {
                                 client.setDeviceType(deviceInfo.getType().toString());
                            }
                        }
                    }
                }
            }
        clientRepository.save(iterable);
          return response;
    }






    // перенос всех оценок
    public EstimateTransferResponse transferEstimate(){
        EstimateTransferResponse response = new EstimateTransferResponse();
        List<Mission> missionList = missionRepository.findByStateAndGeneral(Mission.State.COMPLETED);

        List<Estimate> listForUpd = new ArrayList<>();
           for(Mission mission :missionList){
               Estimate estimate = estimateRepository.findByMission(mission);
                  if(estimate == null){
                      estimate = new Estimate();
                  }
               Mission.Score missionScore = mission.getScore();
                  if(missionScore.getGeneral()!=0){
                      estimate.setMission(mission);
                      estimate.setEstimateComment(missionScore.getEstimateComment());
                      estimate.setWaitingTime(missionScore.getWaitingTime());
                      estimate.setApplicationConvenience(missionScore.getApplicationConvenience());
                      estimate.setCleanlinessInCar(missionScore.getCleanlinessInCar());
                      estimate.setDriverCourtesy(missionScore.getDriverCourtesy());
                       if(mission.getTimeOfFinishing()!=null){
                           estimate.setEstimateDate(mission.getTimeOfFinishing());
                       }else{
                           estimate.setEstimateDate(DateTimeUtils.nowNovosib_GMT6());
                       }
                      estimate.setGeneral(missionScore.getGeneral());
                      estimate.setWifiQuality(missionScore.getWifiQuality());
                      estimate.setDriver(mission.getDriverInfo());
                      estimate.setClient(mission.getClientInfo());
                      listForUpd.add(estimate);
                  }
           }
        if(!listForUpd.isEmpty()){
            estimateRepository.save(listForUpd);
        }
         return response;
    }




    public EstimateInfoResponse estimateInfoByDriver(int numPage, int pageSize, List<QueryDetails> queryDetailsList){
        EstimateInfoResponse response =  new EstimateInfoResponse();
        response.setTotalItems(0);
        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(Estimate.class);

        if(CollectionUtils.isEmpty(queryDetailsList)){
              return response;
        }
        QueryDetails queryDetails = queryDetailsList.get(0);
        criteria.add(Restrictions.eq("driver.id", Long.valueOf(queryDetails.getEqual().toString())));
        criteria.add(Restrictions.ne("estimateComment", ""));
        criteria.add(Restrictions.eq("visible", Boolean.TRUE));

        criteria.addOrder(Order.desc("id"));
        criteria.setFirstResult((numPage - 1) * pageSize);
        criteria.setMaxResults(pageSize);

        List<Estimate> estimates = criteria.list();

        if(!CollectionUtils.isEmpty(estimates)) {
            for (Estimate estimate : estimates) {
                EstimateInfoARM estimateInfo = new EstimateInfoARM();
                estimateInfo.setEstimateComment(estimate.getEstimateComment());
                estimateInfo.setClientInfoARM(ModelsUtils.toModelClientInfoARM(estimate.getClient(), null));
                estimateInfo.setEstimateDate(estimate.getEstimateDate().getMillis());
                estimateInfo.setGeneral(estimate.getGeneral());
                response.getEstimateInfoARMList().add(estimateInfo);
            }
        }
        return response;
    }












    public EstimateInfoByClientResponse estimateInfoByClient(long clientId) {
        EstimateInfoByClientResponse response = new EstimateInfoByClientResponse();
        Client client = clientRepository.findOne(clientId);
        if(client!=null) {
            List<Estimate> estimateList = estimateRepository.findByClientOrderByEstimateDateDesc(client);
                if(estimateList!=null){
                    for(Estimate estimate: estimateList){
                          if(estimate.getGeneral()!=0){
                              EstimateInfoARM estimateInfoARM = new EstimateInfoARM();
                              estimateInfoARM.setId(estimate.getId());
                              estimateInfoARM.setMissionInfoARM(ModelsUtils.toModel_MissionInfoARM(estimate.getMission()));
                              estimateInfoARM.setApplicationConvenience(estimate.getApplicationConvenience());
                              estimateInfoARM.setCleanlinessInCar(estimate.getCleanlinessInCar());
                              estimateInfoARM.setDriverCourtesy(estimate.getDriverCourtesy());
                              estimateInfoARM.setEstimateDate(DateTimeUtils.toDate(estimate.getEstimateDate()));
                              estimateInfoARM.setWifiQuality(estimate.getWifiQuality());
                              estimateInfoARM.setGeneral(estimate.getGeneral());
                              estimateInfoARM.setVisible(estimate.isVisible());
                              if(estimate.getEstimateComment()!=null){
                                  estimateInfoARM.setEstimateComment(estimate.getEstimateComment());
                              }else{
                                  estimateInfoARM.setEstimateComment("");
                              }
                              response.getEstimateInfoARMList().add(estimateInfoARM);
                          }
                      }
                }else{
                    throw new CustomException(1,"Mission not found");
                }
        }else{
            throw new CustomException(2,"Client not found");
        }
        return response;
    }




    public int timeOfStartBooked(long missionId){
        int result = 0;
        Mission mission = missionRepository.findOne(missionId);
           if(mission!=null){
               DateTime timeOfStarting = mission.getTimeOfStarting();
               DateTime nowDateTime = timeService.nowDateTime();
               Minutes minutes = Minutes.minutesBetween(nowDateTime, timeOfStarting);

               result = minutes.getMinutes();
               LOGGER.info("result = " + result);
           }
             return result;
    }



    /*
    old version
    public void completeMission(long missionId, PaymentInfo paymentInfo) {
        Mission mission = missionRepository.findOne(missionId);
        if (mission != null) {
            /* здесь делать пересчет
            mission.getStatistics().setPriceInFact(MoneyUtils.getRubles(paymentInfo.getTotalPrice()));
            mission.setState(Mission.State.COMPLETED);
            clearClientMission(mission);
            missionRepository.save(mission);
            //notificationsService.missionPayment(missionId);
        }
    }
    */


    public void completeMission(long missionId, long driverId){
        Mission mission = missionRepository.findOne(missionId);
        Driver driver = driverRepository.findOne(driverId);
        if (mission == null) {
            throw new CustomException(1, "Миссия не найдена");
        }
        if(driver == null){
            throw new CustomException(2, "Водитель не найден");
        }
        if(mission.getDriverInfo()!=null && mission.getDriverInfo().getId() != driverId){
            throw new CustomException(4, "Миссия не соответствует водителю");
        }

         /* todo: проверка на стейт миссии, completed он на этом этапе или нет ? */
        MissionCanceled missionCanceled = missionCanceledRepository.findByMissionIdAndCancelBy(missionId, "driver");

        if(missionCanceled != null && missionCanceled.getCancelById() != null){
            // по данному заказу были отмены водителем
            Driver whoCanceled = driverRepository.findOne(missionCanceled.getCancelById());

               if(whoCanceled != null && !whoCanceled.isTypeX() && missionCanceled.getReasonInfo() != null && missionCanceled.getReasonInfo().getFine() > 0){
                   /* со счета отказавшегося водителя списывается сумма указанная в причине отказа */
                   updateDriverBalanceSystem(whoCanceled.getId(), mission.getId(), -missionCanceled.getReasonInfo().getFine(), 1);


                   /* на счет водителя выполнившего заказ начисляется сумма указанная в причине отказа
                       todo: убрал по причине того, что в момент отказа я увеличиваю стоимость заказа, т.е. водитель выполнивший заказ уже получает доплату
                       updateDriverBalanceSystem(mission.getDriverInfo().getId(), mission.getId(), missionCanceled.getReasonInfo().getFine(), 21);
                   */
               }
        }
    }





    public void getLocationFromMongoDB(){
        //Iterable<ru.trendtech.domain.mongo.Location> result =  locationRepositoryMongoDB.findAll();
        //LOGGER.info("result = "+result);
    }




    public AutoClassPriceResponse autoClassPricesForArm(String security_token, int autoClass){
        AutoClassPriceResponse response = new AutoClassPriceResponse();
            WebUser webUser = webUserRepository.findByToken(security_token);
            if (webUser != null) {
                if(isAccess(true, webUser.getRole())) { // true - read
                    MissionRate missionRate = missionRatesRepository.findOne((long) 1);
                    Set<AutoClassPrice> autoClassPriceSet = missionRate.getAutoClassPrices();
                    for (AutoClassPrice autoClassPrice : autoClassPriceSet) {
                           if(autoClass==0){
                               response.getAutoClassRateInfoV2List().add(ModelsUtils.toModel(autoClassPrice, false));
                           }else{
                               if (autoClassPrice.getAutoClass().getValue()==autoClass) {
                                   response.getAutoClassRateInfoV2List().add(ModelsUtils.toModel(autoClassPrice, false));
                               }
                           }
                    }
                }else{
                    throw new CustomException(2, "Permission denied");
                }
            }else{
                throw new CustomException(1, "Web user not found");
            }
           return response;
    }


    // read = true - чтение, read = false - запись
    public boolean isAccess(boolean read, AdministratorRole role){
         boolean result =false;
            if(read){
                // чтение
                if(EnumSet.of(AdministratorRole.DISPATCHER, AdministratorRole.ADMIN, AdministratorRole.READONLY).contains(role)){
                    result = true;
                }
            }else{
                // запись
                if(EnumSet.of(AdministratorRole.ADMIN).contains(role)){
                    result = true;
                }
            }
          return result;
    }



    private ComissionResponse getComissionInfoList(int comissionType, long objectId){
        ComissionResponse response = new ComissionResponse();
        switch(comissionType){
            case 0:{
                // общая комиссия
                List<Comission> comissions = comissionRepository.findByComissionType(comissionType);
                for(Comission comission: comissions){
                    response.getComissionInfoList().add(ModelsUtils.toModel(comission));
                }
                break;
            }
            case 1:{
                // водительская комиссия
                if(objectId == 0){
                    // комиссия по всем водителям
                    List<Comission> comissions = comissionRepository.findByComissionType(comissionType);
                    for(Comission comission: comissions){
                        response.getComissionInfoList().add(ModelsUtils.toModel(comission));
                    }
                }else{
                    Comission comission = comissionRepository.findByComissionTypeAndObjectId(comissionType, objectId);
                    response.getComissionInfoList().add(ModelsUtils.toModel(comission));
                }
                break;
            }
            case 2:{
                // комиссия таксопарка
                if(objectId == 0){
                    // по всем таксопаркам
                    List<Comission> comissions = comissionRepository.findByComissionType(comissionType);
                    for(Comission comission: comissions){
                        response.getComissionInfoList().add(ModelsUtils.toModel(comission));
                    }
                }else{
                    Comission comission = comissionRepository.findByComissionTypeAndObjectId(comissionType, objectId);
                    response.getComissionInfoList().add(ModelsUtils.toModel(comission));
                }
                break;
            }
            default: break;
        }
        return response;
    }



    // получить комиссии по таксопаркам
    public ComissionResponse getComission(String security_token, int comissionType, long objectId){
        ComissionResponse response = new ComissionResponse();
             WebUser webUser = webUserRepository.findByToken(security_token);
             if (webUser != null) {
                 if(isAccess(true, webUser.getRole())) {
                     response = getComissionInfoList(comissionType, objectId);
                 }else{
                     throw new CustomException(4, "Permission denied");
                 }
             }else{
                 throw new CustomException(2, "Web user not found");
             }
          return  response;
    }




    public DriverRequisiteARMResponse driverRequisite(String security_token, long driverId, long driverRequisiteId){
        DriverRequisiteARMResponse response = new DriverRequisiteARMResponse();
        WebUser webUser = webUserRepository.findByToken(security_token);
        if (webUser == null) {
            throw new CustomException(1,"Web user not found");
        }
        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(DriverRequisite.class);

        if(driverId!=0){
            Driver driver = driverRepository.findOne(driverId);
              if(driver == null){
                  throw new CustomException(4,"Driver not found");
              }
            Criterion pMask = Restrictions.eq("driver", driver);
            criteria.add(pMask);
        }
        if(driverRequisiteId!=0){
            Criterion pMask = Restrictions.eq("id", driverRequisiteId);
            criteria.add(pMask);
        }
        criteria.addOrder(Order.desc("id"));
        List<DriverRequisite> listDriverRequisite = criteria.list();
        if(listDriverRequisite!=null && !listDriverRequisite.isEmpty()) {
            for (DriverRequisite driverRequisite : listDriverRequisite) {
                response.getDriverRequisiteInfos().add(ModelsUtils.toModelDriverRequisiteInfoARM(driverRequisite));
            }
        }
          return response;
    }




    public DriverRequisiteUpdateResponse driverRequisiteUpdate(String security_token, DriverRequisiteInfoARM driverRequisiteInfo){
        WebUser webUser = webUserRepository.findByToken(security_token);
        if (webUser == null) {
            throw new CustomException(1,"Web user not found");
        }
        if(!isAccess(false, webUser.getRole())){
            throw new CustomException(2,"Permission denied");
        }
        Driver driver = driverRepository.findOne(driverRequisiteInfo.getDriverId());
        if(driver == null){
            throw new CustomException(4,"Driver not found");
        }
        DriverRequisiteUpdateResponse response = new DriverRequisiteUpdateResponse();

        // устанавливаем для текущего водителя активность реквизита в false
        setRequisiteDriverToFalse(driver);

        if(driverRequisiteInfo.getId() == 0){
            // добавление реквизитов водителя
            DriverRequisite driverRequisite = new DriverRequisite();
            driverRequisite.setActive(Boolean.TRUE);
            driverRequisite.setStartHours(driverRequisiteInfo.getStartHours());
            driverRequisite.setStartMinutes(driverRequisiteInfo.getStartMinutes());
            driverRequisite.setStaffer(driverRequisiteInfo.isStaffer()); // штатный/наемный
            driverRequisite.setCountMinutesOfRest(driverRequisiteInfo.getCountMinutesOfRest()); // кол-во минут отдыха
            driverRequisite.setDismissalTime(DateTimeUtils.toDateTime(driverRequisiteInfo.getDismissalTime()));
            driverRequisite.setDriver(driver);
            driverRequisite.setEndHours(driverRequisiteInfo.getEndHours());
            driverRequisite.setEndMinutes(driverRequisiteInfo.getEndMinutes());
            driverRequisite.setSalaryPerDay(driverRequisiteInfo.getSalaryPerDay());
            driverRequisite.setTypeDismissal(driverRequisiteInfo.getTypeDismissal());
            driverRequisite.setTypeSalary(driverRequisiteInfo.getTypeSalary()==null?0:driverRequisiteInfo.getTypeSalary());
            driverRequisite.setSalaryPriority(driverRequisiteInfo.getSalaryPriority()==null?0:driverRequisiteInfo.getSalaryPriority());
            driverRequisiteRepository.save(driverRequisite);
        }else{
            // поиск реквизита по id и текущему драйверу
            DriverRequisite driverRequisite = driverRequisiteRepository.findByIdAndDriver(driverRequisiteInfo.getId(), driver);
               if(driverRequisite == null){
                   throw new CustomException(5,"DriverRequisite with id: "+driverRequisiteInfo.getId()+" and driver id: "+driver.getId()+" not found");
               }
                   // обновление реквизитов водителя
            driverRequisite.setUpdateTime(DateTimeUtils.nowNovosib_GMT6());
            driverRequisite.setStartHours(driverRequisiteInfo.getStartHours());
            driverRequisite.setStartMinutes(driverRequisiteInfo.getStartMinutes());
            driverRequisite.setStaffer(driverRequisiteInfo.isStaffer()); // штатный/наемный
            driverRequisite.setCountMinutesOfRest(driverRequisiteInfo.getCountMinutesOfRest()); // кол-во минут отдыха
            driverRequisite.setDismissalTime(DateTimeUtils.toDateTime(driverRequisiteInfo.getDismissalTime()));
            driverRequisite.setEndHours(driverRequisiteInfo.getEndHours());
            driverRequisite.setEndMinutes(driverRequisiteInfo.getEndMinutes());
            driverRequisite.setSalaryPerDay(driverRequisiteInfo.getSalaryPerDay());
                   driverRequisite.setTypeDismissal(driverRequisiteInfo.getTypeDismissal());
                   if(driverRequisiteInfo.getTypeSalary()!=null){
                       driverRequisite.setTypeSalary(driverRequisiteInfo.getTypeSalary());
                   }
                   if(driverRequisiteInfo.getSalaryPriority()!=null){
                       driverRequisite.setSalaryPriority(driverRequisiteInfo.getSalaryPriority());
                   }
                   if(driverRequisiteInfo.getTypeDismissal()==0){
                       // уволен
                       driverRequisite.setActive(Boolean.FALSE);
                   }else{
                       driverRequisite.setActive(driverRequisiteInfo.isActive());
                   }
                   driverRequisiteRepository.save(driverRequisite);
        }
        return response;
    }






    private void setRequisiteDriverToFalse(Driver driver){
        List<DriverRequisite> driverRequisiteList =  driverRequisiteRepository.findByDriver(driver);
        if(!CollectionUtils.isEmpty(driverRequisiteList)){
            for(DriverRequisite driverRequisite :driverRequisiteList){
                driverRequisite.setActive(false);
                driverRequisiteRepository.save(driverRequisite);
            }
            //driverRequisiteRepository.save(driverRequisiteList);
        }
    }



    public void comissionUpdate(String security_token, ComissionInfo comissionInfo){
        WebUser webUser = webUserRepository.findByToken(security_token);
        int comissionType = comissionInfo.getComissionType(); // (0 - общая комиссия, 1 - для водителя, 2 - комиссия на таксопарк)

            if (webUser == null) {
                throw new CustomException(1, "Web user not found");
            }
            if(!isAccess(false, webUser.getRole())){
                throw new CustomException(2, "Permission denied");
            }
            if(comissionInfo.getStartTime()==0 || comissionInfo.getEndTime()==0){
                throw new CustomException(4, "Укажите период");
            }

        switch(comissionType){
            case 0:{
                // комиссия по умолчанию
                List<Comission> comissionDefaultList = comissionRepository.findByComissionType(comissionType);

                if(comissionDefaultList == null || comissionDefaultList.isEmpty()){
                    comissionRepository.save(ModelsUtils.fromModel(comissionInfo));
                }else{
                    comissionRepository.save(ModelsUtils.fromModel(comissionInfo, comissionDefaultList.get(0)));
                }
                break;
            }
            case 1:{
                // комиссия для водителя
                Driver driver = driverRepository.findOne(comissionInfo.getObjectId());

                  if(driver == null){
                      throw new CustomException(6,"Водитель не найден");
                  }

                Comission comissionDriver = comissionRepository.findByComissionTypeAndObjectId(comissionType, comissionInfo.getObjectId());

                if(comissionDriver == null){
                    comissionRepository.save(ModelsUtils.fromModel(comissionInfo));
                }else{
                    comissionDriver = ModelsUtils.fromModel(comissionInfo, comissionDriver);
                    comissionRepository.save(comissionDriver);
                }
                break;
            }
            case 2:{
                // комиссия для таксопарка
                TaxoparkPartners taxoparkPartners = taxoparkPartnersRepository.findOne(comissionInfo.getObjectId());
                if(taxoparkPartners == null){
                   throw new CustomException(8,"Таксопарк не найден");
                }

                 Comission comissionTaxopark = comissionRepository.findByComissionTypeAndObjectId(comissionType, comissionInfo.getObjectId());

                if(comissionTaxopark == null){
                    comissionRepository.save(ModelsUtils.fromModel(comissionInfo));
                }else{
                    comissionTaxopark = ModelsUtils.fromModel(comissionInfo, comissionTaxopark);
                    comissionRepository.save(comissionTaxopark);
                }
                break;
            }
            default: break;
        }
    }


    /*
                    if(comissionInfo.getId()==null){
                        // add
                        comissionRepository.save(ModelsUtils.fromModel(comissionInfo));
                        throw new CustomException(0,"Successfully added");
                    }else{
                        // upd
                        Comission comission =  comissionRepository.findOne(comissionInfo.getId());
                           if(comission!=null){
                               comissionRepository.save(ModelsUtils.fromModel(comissionInfo, comission));
                               throw new CustomException(0,"Successfully updated");
                           }else{
                               throw new CustomException(5,"Item not found");
                           }
                    }
     */




    private String getMdOrderState(Mission mission){
       String result = "";
          if(mission.getPaymentType().equals(PaymentType.CARD)){
              MDOrder mdOrder = mdOrderRepository.findByMission(mission);
                if(mdOrder!=null){
                    result = mdOrder.getPaymentDescription();
                }
          }
        return result;
    }





    public MissionInfoARMResponse detailsARM(long missionId, String security_token){
        MissionInfoARMResponse response = new MissionInfoARMResponse();
        WebUser webUser = webUserRepository.findByToken(security_token);
           if(webUser==null){
               throw new CustomException(1, "WebUser not found");
           }
        Mission mission;
        if(webUser.getTaxoparkId()!=null){
            mission = missionRepository.findByIdAndDriverInfoTaxoparkPartnersId(missionId, webUser.getTaxoparkId());
        } else{
            mission = missionRepository.findOne(missionId);
        }
        if (mission != null) {
            MissionCanceled missionCanceled = missionCanceledRepository.findByMissionId(mission.getId());

            MissionInfoARM missionInfo = ModelsUtils.toModel_MissionInfoARM(mission);

            if(missionCanceled!=null){
                missionInfo.setCancelBy(missionCanceled.getCancelBy()); // эту фигню можно убрать
                missionInfo.setMissionCanceledInfo(ModelsUtils.toModel(missionCanceled));
            }

            missionInfo.setPaymentDescription(getMdOrderState(mission));
            Estimate estimate = estimateRepository.findByMission(mission);
               if(estimate!=null){
                   missionInfo.setRating(estimate.getGeneral());
               }
            response.setMissionInfoARM(missionInfo);
        }else{
            throw new CustomException(2, "Mission not found");
        }
          return response;
    }






    public InsertAutoClassPriceResponse insertAutoClassPrices(String security_token, AutoClassRateInfoV2 autoClassRateInfoV2){
        WebUser webUser = webUserRepository.findByToken(security_token);
        if (webUser == null) {
            throw new CustomException(1,"Web user not found");
        }
        if (!EnumSet.of(AdministratorRole.ADMIN).contains(webUser.getRole())) {
            throw new CustomException(2,"Permission denied");
        }
        InsertAutoClassPriceResponse  response = new InsertAutoClassPriceResponse();

               MissionRate missionRate = missionRatesRepository.findOne((long) 1);
               Set<AutoClassPrice> autoClassPriceSet = missionRate.getAutoClassPrices();
               AutoClassPrice newTarif = new AutoClassPrice();
               newTarif.setFreeWaitMinutes(autoClassRateInfoV2.getFreeWaitMinutes());
               newTarif.setIntercity(autoClassRateInfoV2.getIntercity());
               newTarif.setKmIncluded(autoClassRateInfoV2.getKmIncluded());
               newTarif.setPerHourAmount(autoClassRateInfoV2.getPerHourAmount());
               newTarif.setPerMinuteWaitAmount(autoClassRateInfoV2.getPerMinuteWaitAmount());
               newTarif.setPrice(Money.of(CurrencyUnit.of("RUB"), autoClassRateInfoV2.getPrice()));
               newTarif.setPriceHour(autoClassRateInfoV2.getPriceHour());
               newTarif.setPriceKm(autoClassRateInfoV2.getPriceKm());
               newTarif.setAutoClass(AutoClass.valueOf(autoClassRateInfoV2.getAutoClassStr()));
               newTarif.setActive(autoClassRateInfoV2.isActive());
               newTarif.setAutoExample(autoClassRateInfoV2.getAutoExample());
               newTarif.setActivePicUrl(autoClassRateInfoV2.getActivePicUrl());
               newTarif.setNotActivePicUrl(autoClassRateInfoV2.getNotActivePicUrl());
               newTarif.setDescription(autoClassRateInfoV2.getDescription());
               autoClassPriceSet.add(newTarif);

               missionRate.setAutoClassPrices(autoClassPriceSet);
               missionRatesRepository.save(missionRate);

        mongoDBServices.createEvent(3, "" + webUser.getId(), 1, 0, "insertAutoClassPrice", "", "");
          return response;
    }








    public UpdateAutoClassPriceResponse updateAutoClassPrices(String security_token, AutoClassRateInfoV2 autoClassRateInfoV2){
        UpdateAutoClassPriceResponse response = new UpdateAutoClassPriceResponse();
            WebUser webUser = webUserRepository.findByToken(security_token);
            if (webUser != null) {
                if (webUser.getRole().equals(AdministratorRole.ADMIN)) {
                    MissionRate missionRate = missionRatesRepository.findOne((long) 1);
                    Set<AutoClassPrice> autoClassPriceSet = missionRate.getAutoClassPrices();
                    for (AutoClassPrice autoClassPrice : autoClassPriceSet) {
                        if (autoClassPrice.getAutoClass().getValue()==autoClassRateInfoV2.getAutoClass()) {
                            int priceBeforeUpd = autoClassPrice.getPrice().getAmount().intValue();
                            int priceAfterUpd = autoClassRateInfoV2.getPrice();
                            autoClassPrice.setFreeWaitMinutes(autoClassRateInfoV2.getFreeWaitMinutes());
                            autoClassPrice.setIntercity(autoClassRateInfoV2.getIntercity());
                            autoClassPrice.setKmIncluded(autoClassRateInfoV2.getKmIncluded());
                            autoClassPrice.setPerHourAmount(autoClassRateInfoV2.getPerHourAmount());
                            autoClassPrice.setPerMinuteWaitAmount(autoClassRateInfoV2.getPerMinuteWaitAmount());
                            autoClassPrice.setPrice(Money.of(CurrencyUnit.of("RUB"), autoClassRateInfoV2.getPrice()));
                            autoClassPrice.setPriceHour(autoClassRateInfoV2.getPriceHour());
                            autoClassPrice.setPriceKm(autoClassRateInfoV2.getPriceKm());
                            //autoClassPrice.setPriceKmCorporate(autoClassRateInfoV2.getPriceKmCorporate());
                            autoClassPrice.setActive(autoClassRateInfoV2.isActive());
                            autoClassPrice.setAutoExample(autoClassRateInfoV2.getAutoExample());
                            autoClassPrice.setDescription(autoClassRateInfoV2.getDescription());
                            autoClassPrice.setActivePicUrl(autoClassRateInfoV2.getActivePicUrl());
                            autoClassPrice.setNotActivePicUrl(autoClassRateInfoV2.getNotActivePicUrl());

                            if(priceBeforeUpd!=priceAfterUpd){
                                alternativeStatisticsRepository.save(new AlternativeStatistics(autoClassPrice.getAutoClass().toString(), "b:"+priceBeforeUpd+", a:"+priceAfterUpd, DateTimeUtils.toDate(DateTimeUtils.nowNovosib_GMT6())));
                            }
                        }
                    }
                    missionRate.setAutoClassPrices(autoClassPriceSet);
                    missionRatesRepository.save(missionRate);
                } else {
                    response.getErrorCodeHelper().setErrorCode(2);
                    response.getErrorCodeHelper().setErrorMessage("Permission denied");
                }
                mongoDBServices.createEvent(3, "" + webUser.getId(), 3, 0, "updateAutoClassPrice", "", "");
                //mongoDBServices.createEvent(3, "" + webUser.getId(), 3, "updateAutoClassPrice", "", "", 0, 0);
            } else {
                response.getErrorCodeHelper().setErrorCode(1);
                response.getErrorCodeHelper().setErrorMessage("Web user not found");
            }

           return response;
    }



    public ClientInfoArmResponse findClientArm(String security_token, long clientId){
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser==null){
            throw new CustomException(1, "Web user not found");
        }
        Client client = getClientInfo(clientId);
        if(client==null){
            throw new CustomException(2, "Client user not found");
        }
        ClientInfoArmResponse response = new ClientInfoArmResponse();
        ClientInfoARM clientInfoArm = ModelsUtils.toModelClientInfoARM(client, corporateClientLimitRepository.findByClient(client));

        List<PrivateTariff> privateTariffs = privateTariffRepository.findByClientAndActive(client, Boolean.TRUE);
        for(PrivateTariff tariff:privateTariffs){
            clientInfoArm.getAllowTariff().add(ModelsUtils.toModel(tariff)); //    AutoClass.valueOf(tariff.getTariffName()).getValue()
        }
        response.setClientInfoARM(clientInfoArm);
        return response;
    }





    public DriverFindARMResponse findDriverArm(long driverId, String security_token){
        DriverFindARMResponse response = new DriverFindARMResponse();
        WebUser webUser = webUserRepository.findByToken(security_token);
           if(webUser==null){
               return response;
           }

        Driver driver;
        if(webUser.getTaxoparkId()!=null){
            driver = driverRepository.findByIdAndTaxoparkPartnersId(driverId, webUser.getTaxoparkId());
        } else{
            driver = driverRepository.findOne(driverId);
        }

        if (driver != null) {
            DriverInfoARM driverInfoARM = ModelsUtils.toModelARM(driver);

            if(driver.getAssistant()!=null){
                AssistantInfo assistantInfo = ModelsUtils.toModel(assistantRepository.findOne(driver.getAssistant().getId()));
                driverInfoARM.setAssistantInfo(assistantInfo);
            }

            MissionRateInfo missionRateInfo = billingService.getDriverRate(driverId);
            List<AutoClassRateInfo> autoClassRateInfoList = missionRateInfo.getAutoClassRateInfos();
            for(AutoClassRateInfo autoClassRateInfo: autoClassRateInfoList){
                if(autoClassRateInfo.getAutoClass()==driverInfoARM.getAutoClass()){
                    driverInfoARM.setRateDriver(autoClassRateInfo.getPrice());
                }
            }
            // 1 - водительская
            ComissionResponse resp =  getComissionInfoList(1, driverId);
            driverInfoARM.setComissionInfos(resp.getComissionInfoList());
            List<DriverCarPhotos> driverCarPhotoses = driverCarPhotosRepository.findByDriver(driver);
            if(!CollectionUtils.isEmpty(driverCarPhotoses)){
                for(DriverCarPhotos carPhotos : driverCarPhotoses){
                    driverInfoARM.getDriverCarPhotosInfos().add(ModelsUtils.toModel(carPhotos));
                }
            }
            response.setDriverInfoARM(driverInfoARM);
            ServerStateInfo stateInfo = resolveState(0, driverId);
            response.setServerState(stateInfo.getState());
        }
           return response;
    }




    public AdministratorLoginResponse adminLogin(String login, String password) {
        AdministratorLoginResponse response = new AdministratorLoginResponse();
        List<WebUser> webUsers = webUserRepository.findByEmailAndPassword(login, password);
        if (!webUsers.isEmpty()) {
            if (webUsers.size() == 1) {
                WebUser admin = webUsers.get(0);
                admin.setState(WebUser.State.ONLINE);
                admin = webUserRepository.save(admin);
                response.setAdminId(admin.getId());

                List<ru.trendtech.domain.courier.Order.State> states = new ArrayList<>();
                states.add(ru.trendtech.domain.courier.Order.State.CANCELED);
                states.add(ru.trendtech.domain.courier.Order.State.COMPLETED);

                List<ru.trendtech.domain.courier.Order> currentOrder = orderRepository.findByWebUserAndStateIsNotIn(admin, states);
                WebUserModel userModel = ModelsUtils.toModel(admin, currentOrder);

                response.setUserModel(userModel);
                response.setConfigurationModel(getConfigurationModel());
                String genString = StrUtils.generateAlphaNumString(5);
                String security_token = TokenUtil.getMD5("fractal" + genString);
                response.setSecurity_token(security_token);
                admin.setToken(security_token);
                webUserRepository.save(admin);

                mongoDBServices.createEvent(3, "" + admin.getId(), 3, 0, "loginWebUser", "", "");
            }
        }
          return response;
    }



    private WebUserConfigurationModel getConfigurationModel() {
        WebUserConfigurationModel result = new WebUserConfigurationModel();
        result.setAsteriskIP("0.0.0.0");
        return result;
    }




    //f:add
    public DriverEstimatesARMResponse driverEstimatesARM(long driverId, String security_token) {
        WebUser webUser = webUserRepository.findByToken(security_token);
        if (webUser == null) {
            throw new CustomException(1,"Web user not found");
        }
        Driver driver = driverRepository.findOne(driverId);
        if (driver == null) {
            throw new CustomException(2,"Driver not found");
        }
        DriverEstimatesARMResponse response = new DriverEstimatesARMResponse();

                    List<Estimate> estimateList = estimateRepository.findByDriverOrderByEstimateDateDesc(driver);
                        for (Estimate estimate : estimateList) {
                            if (estimate.getGeneral() != 0) {
                                EstimateInfoARM estimateInfoARM = new EstimateInfoARM();
                                estimateInfoARM.setMissionInfoARM(ModelsUtils.toModel_MissionInfoARM(estimate.getMission()));
                                estimateInfoARM.setApplicationConvenience(estimate.getApplicationConvenience());
                                estimateInfoARM.setCleanlinessInCar(estimate.getCleanlinessInCar());
                                estimateInfoARM.setDriverCourtesy(estimate.getDriverCourtesy());
                                estimateInfoARM.setGeneral(estimate.getGeneral());
                                estimateInfoARM.setWaitingTime(estimate.getWaitingTime());
                                estimateInfoARM.setWifiQuality(estimate.getWifiQuality());
                                estimateInfoARM.setVisible(estimate.isVisible());
                                estimateInfoARM.setEstimateDate(DateTimeUtils.toDate(estimate.getEstimateDate()));
                                if (estimate.getEstimateComment() != null) {
                                    estimateInfoARM.setEstimateComment(estimate.getEstimateComment());
                                } else {
                                    estimateInfoARM.setEstimateComment("");
                                }
                                response.getEstimateInfoClientList().add(estimateInfoARM);
                            }
                        }
        //mongoDBServices.createEvent(3, "" + driverId, 3, "scores", "webUserId: "+ webUser.getId(), "", 0, driverId);
           return response;
    }





    // вывод всех оценок по водителю на клиенте
    public DriverEstimatesV2Response estimateListV2(long clientId, String security_token, long driverId){
        if (!validatorService.validateUser(clientId, security_token, 1)) {
            throw new CustomException(3, "Клиент не найден");
        }
        Driver driver = driverRepository.findOne(driverId);
        if(driver == null){
            throw new CustomException(2,"Driver not found");
        }
        DriverEstimatesV2Response response = new DriverEstimatesV2Response();
        List<Estimate> estimateList = estimateRepository.findByDriverOrderByEstimateDateDesc(driver);
        if(estimateList!=null){
            response.setDriverProfileInfo(ModelsUtils.toModelDriverProfileInfo(driver));
            for(Estimate estimate :estimateList){
                if(estimate.isVisible()&& estimate.getEstimateComment()!=null && !estimate.getEstimateComment().isEmpty()){
                    response.getEstimateInfotList().add(ModelsUtils.toModelEstimateInfo(estimate));
                }
            }
        }
        return response;
    }






    // вывод всех оценок по водителю на клиенте
    public DriverEstimatesResponse estimateList(long clientId, String security_token, long driverId){
        if(!validatorService.validateUser(clientId, security_token, 1)){
            throw new CustomException(3, "Клиент не найден");
        }
        Client client = clientRepository.findOne(clientId);
        Driver driver = driverRepository.findOne(driverId);
        if(driver == null){
            throw new CustomException(2,"Driver not found");
        }
        DriverEstimatesResponse response = new DriverEstimatesResponse();
        List<Estimate> estimateList = estimateRepository.findByDriverAndMissionIsNotNullOrderByEstimateDateDesc(driver); // findByDriverOrderByEstimateDateDesc
        if(estimateList!=null){
           // преобразуем все в модель естимейт инфо и отправляем на клиента
           response.setDriverProfileInfo(ModelsUtils.toModelDriverProfileInfo(driver));

            for(Estimate estimate :estimateList){
                if(estimate.isVisible() && estimate.getEstimateComment()!=null && !estimate.getEstimateComment().isEmpty()){
                    response.getEstimateInfoClientList().add(ModelsUtils.toModel(estimate));
                }
            }
        }
          return response;
    }





    public CalculateSumByTipResponse calculateSumByTip(String security_token, long clientId, Long tipPercentId, int sum) {
        if(!validatorService.validateUser(clientId, security_token, 1)){
            throw new CustomException(3, "Клиент не найден");
        }
        TipPercent tipPercent = tipPercentRepository.findOne(tipPercentId);
        if(tipPercent == null){
            throw new CustomException(4, "TipPercent not found");
        }
            CalculateSumByTipResponse response = new CalculateSumByTipResponse();
            double resultSum = (sum + (sum*(tipPercent.getPercent()/100.0)))*100;
            response.setResultSum((int) resultSum);
            return response;
    }






    public CalculateTipResponse calculateTips(String security_token, long clientId) {
        if(!validatorService.validateUser(clientId, security_token, 1)){
            throw new CustomException(3, "Клиент не найден");
        }
        Client client = clientRepository.findOne(clientId);
        Mission mission = client.getMission();
        if(mission == null) {
            throw new CustomException(4, "Mission not found");
        }
        CalculateTipResponse response = new CalculateTipResponse();
        int sum = mission.getStatistics().getPriceInFact().getAmount().intValue();
        if(client.getAccount().getBonuses().getAmount().intValue()<=0){
            // бонусов нет - значит предлагаем оставить чаевые
            Iterable<TipPercent> iterable = tipPercentRepository.findAll();
            for(TipPercent tipPercent : iterable){
                response.getTipPercentInfos().add(ModelsUtils.toModelTipPercentInfo(tipPercent, sum));
            }
        }
        response.setSum(sum*100);
          return response;
    }




    // если нет миссии, передавать сюда missionId=-1
    public void missionEstimateV2(String security_token, EstimateInfo estimateInfo) {
        if(!validatorService.validateUser(estimateInfo.getClientId(), security_token, 1)){
            throw new CustomException(3, "Клиент не найден");
        }
        Client client = clientRepository.findOne(estimateInfo.getClientId());
        Driver driver = driverRepository.findOne(estimateInfo.getDriverId());
        if (driver == null) {
            throw new CustomException(2,"Driver not found");
        }

        Mission mission = missionRepository.findOne(estimateInfo.getMissionId());
        if(mission!=null){
            Estimate estimateDB = estimateRepository.findByMission(mission);
            if (estimateDB != null) {
                throw new CustomException(4,"Already estimated");
            }
        }

        int ratedMissions = driver.getRatedMissions() + 1;
        int ratingPoints = driver.getRatingPoints() + estimateInfo.getGeneral();

        driver.setRatedMissions(ratedMissions);
        driver.setRatingPoints(ratingPoints);
        driver.setRating(ratingPoints / ratedMissions);

        Estimate estimate = new Estimate();

        estimate.setApplicationConvenience(estimateInfo.getApplicationConvenience());
        estimate.setCleanlinessInCar(estimateInfo.getCleanlinessInCar());
        estimate.setDriverCourtesy(estimateInfo.getDriverCourtesy());
        estimate.setGeneral(estimateInfo.getGeneral());
        estimate.setWaitingTime(estimateInfo.getWaitingTime());
        estimate.setWifiQuality(estimateInfo.getWifiQuality());
        estimate.setEstimateDate(DateTimeUtils.nowNovosib_GMT6());
        estimate.setMission(mission);
        estimate.setDriver(driver);
        estimate.setClient(client);
        estimate.setEstimateComment(estimateInfo.getEstimateComment());

        estimateRepository.save(estimate);
    }







    public void missionEstimate(String sec_token, EstimateInfoClient estimateInfo) {
            Mission mission = missionRepository.findOne(estimateInfo.getMissionInfo().getId());
            if (mission == null) {
                throw new CustomException(1,"Mission not found");
            }

            Client client = mission.getClientInfo();
            if (client.getToken() == null || !client.getToken().equals(sec_token)) {
                throw new CustomException(3,"Tokens are not equal");
            }

            Driver driver = mission.getDriverInfo();

            if (driver == null) {
                throw new CustomException(2,"Driver not found");
            }

            Estimate estimateDB = estimateRepository.findByMission(mission);

            if (estimateDB != null) {
                throw new CustomException(4,"Already estimated");
            }

            if (estimateInfo.getGeneral() == 0) {
                throw new CustomException(5,"General can't be null");
            }

            int ratedMissions = driver.getRatedMissions() + 1;
            int ratingPoints = driver.getRatingPoints() + estimateInfo.getGeneral();

            driver.setRatedMissions(ratedMissions);
            driver.setRatingPoints(ratingPoints);
            driver.setRating(ratingPoints / ratedMissions);

            Estimate estimate = new Estimate();

            estimate.setApplicationConvenience(estimateInfo.getApplicationConvenience());
            estimate.setCleanlinessInCar(estimateInfo.getCleanlinessInCar());
            estimate.setDriverCourtesy(estimateInfo.getDriverCourtesy());
            estimate.setGeneral(estimateInfo.getGeneral());
            estimate.setWaitingTime(estimateInfo.getWaitingTime());
            estimate.setWifiQuality(estimateInfo.getWifiQuality());
            estimate.setEstimateDate(DateTimeUtils.nowNovosib_GMT6());
            estimate.setMission(mission);
            estimate.setDriver(driver);
            estimate.setClient(client);
            estimate.setEstimateComment(estimateInfo.getEstimateComment());
            estimateRepository.save(estimate);
    }







// действующий метод вывода оценок
public void rateDriver(long missionId, Scores scores, String sec_token) {
   Mission mission = missionRepository.findOne(missionId);
   if(mission!=null) {
       Client client = mission.getClientInfo();
       if(client!=null){
          if (client.getToken() != null && client.getToken().equals(sec_token)) {
               if (scores.getGeneral() != 0) {
                   Driver driver = mission.getDriverInfo();
                   //LOGGER.info("driver = "+driver+" scores.getGeneral() = "+scores.getGeneral());
                   if (driver != null) {
                       int ratedMissions = driver.getRatedMissions() + 1;
                       int ratingPoints = driver.getRatingPoints() + scores.getGeneral();

                       driver.setRatedMissions(ratedMissions);
                       driver.setRatingPoints(ratingPoints);
                       driver.setRating(ratingPoints / ratedMissions); // f: add

                       //driver.setRating(ratingPoints / ratedMissions / 5); f: //

                       Mission.Score missionScore = mission.getScore();

                       Scores.Details scoresDetails = scores.getDetails();

                       missionScore.setApplicationConvenience(scoresDetails.getApplicationConvenience());
                       missionScore.setCleanlinessInCar(scoresDetails.getCleanlinessInCar());
                       missionScore.setDriverCourtesy(scoresDetails.getDriverCourtesy());
                       missionScore.setGeneral(scores.getGeneral());
                       missionScore.setWaitingTime(scoresDetails.getWaitingTime());
                       missionScore.setWifiQuality(scoresDetails.getWifiQuality());
                         if(scoresDetails.getEstimate_comment()!=null){
                             missionScore.setEstimateComment(scoresDetails.getEstimate_comment());
                         }else{
                             missionScore.setEstimateComment("");
                         }
                       missionRepository.save(mission);
                   }
               }
           } else {
               LOGGER.info("Tokens are not equals");
           }
       }
   }
}

/*
 Mission mission = missionRepository.findOne(missionId);
        if (mission != null) {
            Driver driver = mission.getDriverInfo();
            if (driver != null) {
                driver.setCurrentMission(null);
                driverRepository.save(driver);
                if (isDriverCancelled) {
                    billingService.moneyForCancelMission(driver.getId());
                }
            }
            mission.setState(Mission.State.CANCELED);
            if (comment != null) {
                comment = comment + "\n" + "Прекращена. Причина: " + reason;
                mission.setComments(comment);
            }
            clearClientMission(mission);
            mission = missionRepository.save(mission);
        }
        if (isDriverCancelled) {
            notificationsService.cancelMissionByDriver(missionId);
        } else {
            notificationsService.cancelMissionByClient(missionId);
        }
 */


    /*
    Mission mission = missionRepository.findOne(missionId);
        if (mission != null) {
            Driver driver = mission.getDriverInfo();
            if (driver != null) {
                driver.setCurrentMission(null);
                driverRepository.save(driver);
                if (isDriverCancelled) {
                    billingService.moneyForCancelMission(driver.getId());
                }
            }
            mission.setState(Mission.State.CANCELED);
            if (comment != null) {
                comment = comment + "\n" + "Прекращена. Причина: " + reason;
                mission.setComments(comment);
            }
            clearClientMission(mission);
            mission = missionRepository.save(mission);
        }
        if (isDriverCancelled) {
            notificationsService.cancelMissionByDriver(missionId);
        } else {
            notificationsService.cancelMissionByClient(missionId);
        }
     */


    public void cancelMissionByDriver(long missionId) throws JSONException {

    }





    /*
       if (isDriverCancelled) {
            //notificationsService.cancelMissionByDriver(missionId);
        } else {
            //notificationsService.cancelMissionByClient(missionId);
        }
    */


    public void clearDriverAndClientMission(Mission mission, String comment, boolean missionFromQueque){
        Driver driver = mission.getDriverInfo();
        if (driver != null && !missionFromQueque) {
            // на миссии есть водитель и эта миссия не из очереди [текущая]
            clearDriverMission(mission);
        }
        mission.setState(Mission.State.CANCELED);
        mission.setComments(comment);

        clearClientMission(mission);
        missionRepository.save(mission);
    }


    private void clearDriverMission(Mission mission) {
        Driver driver = mission.getDriverInfo();
        driver.setCurrentMission(null);
        driverRepository.save(driver);
        /* clear missionId in DriverLocation */
        DriverLocation location = locationRepository.findByDriverId(driver.getId());
        location.setMission(null);
        locationRepository.save(location);
    }



    private void clearClientMission(Mission mission) {
        Client client = mission.getClientInfo();
        client.setMission(null);
        clientRepository.save(client);
    }



    /*
      OLD:

       sb.append("select \n" +
                "count(\n" +
                "case when (((unix_timestamp(can.time_of_canceled))-(unix_timestamp(m.time_requesting)))>30 and (select count(*) from mission m2 \n" +
                "join driver drv on drv.id = m2.driverInfo_id\n" +
                "where m2.clientInfo_id=m.clientInfo_id and m2.driverInfo_id is not null and (unix_timestamp(m2.time_requesting)+21600) between (unix_timestamp(m.time_requesting)+21600) and (unix_timestamp(m.time_requesting)+25200)\n" +
                ")=0) then 1 end) as count,\n" +
                " DATE_FORMAT(m.time_requesting, '%Y-%m-%d') as timeRequest  \n" +
                "  from mission m\n" +
                "  join mission_canceled can on can.mission_id = m.id\n" +
                "  where can.time_of_canceled is not null and can.cancel_by='client'");

        if(timeCancelStart!=0 || timeCancelEnd!=0){
            sb.append(" and (unix_timestamp(can.time_of_canceled)+21600) between "+timeCancelStart + " and " + timeCancelEnd);
        }
        if(taxoparkId!=null){
            //sb.append(" and d.taxopark_id = "+taxoparkId);
        }
        sb.append(" group by timeRequest having count>0  order by timeRequest desc");

     */

    /*

    NEW

    select * from(
select
count(
case when (((unix_timestamp(can.time_of_canceled))-(unix_timestamp(m.time_requesting)))>30 and (select count(*) from mission m2
join driver drv on drv.id = m2.driverInfo_id
where m2.clientInfo_id=m.clientInfo_id and m2.driverInfo_id is not null and
(unix_timestamp(m2.time_requesting)+21600) between (unix_timestamp(m.time_requesting)+21600) and (unix_timestamp(m.time_requesting)+25200))=0) then 1 end) as count,
 DATE_FORMAT(m.time_requesting, '%Y-%m-%d') as timeRequest
  from mission m
  join mission_canceled can on can.mission_id = m.id
  where can.time_of_canceled is not null and can.cancel_by='client' and (unix_timestamp(can.time_of_canceled)+21600) between 1436137200 and 1436914799 group by timeRequest
  union all
select 0, r.dates as d from report_dates r) as t1 where 1=1
and (unix_timestamp(t1.timeRequest)+21600) between 1436137200 and 1436914799
group by t1.timeRequest order by t1.timeRequest desc
     */

    /*
    28.07.2015
    "select * from(select \n" +
                "count(\n" +
                "case when (((unix_timestamp(can.time_of_canceled))-(unix_timestamp(m.time_requesting)))>30 and (select count(*) from mission m2 \n" +
                "join driver drv on drv.id = m2.driverInfo_id\n" +
                "where m2.clientInfo_id=m.clientInfo_id and m2.driverInfo_id is not null and (unix_timestamp(m2.time_requesting)+21600) between (unix_timestamp(m.time_requesting)+21600) and (unix_timestamp(m.time_requesting)+25200)\n" +
                ")=0) then 1 end) as count,\n" +
                " DATE_FORMAT(m.time_requesting, '%Y-%m-%d') as timeRequest  \n" +
                "  from mission m\n" +
                "  join mission_canceled can on can.mission_id = m.id\n" +
                "  where can.time_of_canceled is not null and can.cancel_by='client' "
     */





    public void testCancel(Long taxoparkId, long timeCancelStart, long timeCancelEnd){
        StringBuilder sb = new StringBuilder();
        sb.append("select * from(select count(*), DATE_FORMAT(m.time_requesting, '%Y-%m-%d') as timeRequest from mission m\n" +
                "    join mission_canceled can on can.mission_id=m.id and can.cancel_by='client' and can.time_of_canceled is not null\n" +
                (taxoparkId!=null ? " join driver drv on drv.id=m.driverInfo_id and drv.taxopark_id="+taxoparkId+"\n" :"") +
                "    where\n" +
                "    \n" +
                "    m.test_order=0 and \n" +
                "    (select count(*) from mission m2\n" +
                "where (unix_timestamp(m2.time_requesting)+21600) between 1439510400 and 1439596800 \n" +
                " and m2.clientInfo_id=m.clientInfo_id and m2.driverInfo_id is not null and m2.test_order=0 and\n" +
                "(unix_timestamp(m2.time_requesting)+21600) between (unix_timestamp(m.time_requesting)+21600) and (unix_timestamp(m.time_requesting)+25200))!=0\n" +
                "\n" +
                "    and (unix_timestamp(can.time_of_canceled)+21600) between "+timeCancelStart + " and " + timeCancelEnd+" \n" +
                "    and (unix_timestamp(can.time_of_canceled))-(unix_timestamp(m.time_requesting))>30 group by timeRequest\n" +
                " union all  select 0, r.dates as d from report_dates r) as t1 where 1=1 \n" +
                " and (unix_timestamp(t1.timeRequest)+21600) between "+timeCancelStart + " and " + timeCancelEnd +
                " group by t1.timeRequest order by t1.timeRequest desc");
    }




    /*
    15.08.15
    sb.append("select * from(select count(*), DATE_FORMAT(m.time_requesting, '%Y-%m-%d') as timeRequest from mission m\n" +
                "    join driver drv on drv.id = m.driverInfo_id " + (taxoparkId!=null ? " and drv.taxopark_id="+taxoparkId+"\n":"") +
                "    join mission_canceled can on can.mission_id=m.id\n" +
                "    where m.driverInfo_id is not null\n" +
                "    and can.time_of_canceled is not null and can.cancel_by='client'\n" +
                "    and (unix_timestamp(m.time_requesting)+21600) between (unix_timestamp(m.time_requesting)+21600) and (unix_timestamp(m.time_requesting)+25200) " +
                "    and m.test_order=0 ");

        if(timeCancelStart!=0 || timeCancelEnd!=0){
            sb.append(" and (unix_timestamp(can.time_of_canceled)+21600) between "+timeCancelStart + " and " + timeCancelEnd);
        }
        sb.append(" and (unix_timestamp(can.time_of_canceled))-(unix_timestamp(m.time_requesting))>30 group by timeRequest\n" +
                " union all " +
                " select 0, r.dates as d from report_dates r) as t1 where 1=1 ");
        if(timeCancelStart!=0 || timeCancelEnd!=0){
            sb.append(" and (unix_timestamp(t1.timeRequest)+21600) between "+timeCancelStart + " and " + timeCancelEnd);
        }
        sb.append(" group by t1.timeRequest order by t1.timeRequest desc");

     */

    // список реальных отмен  "+(taxoparkId!=null?" and drv.taxopark_id ="+taxoparkId:"")+"
    public MissionCanceledByClientResponse canceledMissionListByClient(long timeCancelStart, long timeCancelEnd , Long taxoparkId){
        Session session = entityManager.unwrap(Session.class);
        StringBuilder sb = new StringBuilder();

        sb.append("select * from(select count(*), DATE_FORMAT(m.time_requesting, '%Y-%m-%d') as timeRequest from mission m\n" +
                "    join mission_canceled can on can.mission_id=m.id and can.cancel_by='client' and can.time_of_canceled is not null\n" +
                (taxoparkId!=null ? " join driver drv on drv.id=m.driverInfo_id and drv.taxopark_id="+taxoparkId+"\n" :"") +
                "    where\n" +
                "    \n" +
                "    m.test_order=0 and \n" +
                "    (select count(*) from mission m2\n" +
                "where (unix_timestamp(m2.time_requesting)+21600) between "+timeCancelStart + " and " + timeCancelEnd+" \n" +
                " and m2.clientInfo_id=m.clientInfo_id and m2.driverInfo_id is not null and m2.test_order=0 and\n" +
                "(unix_timestamp(m2.time_requesting)+21600) between (unix_timestamp(m.time_requesting)+21600) and (unix_timestamp(m.time_requesting)+25200))!=0\n" +
                "\n" +
                "    and (unix_timestamp(can.time_of_canceled)+21600) between "+timeCancelStart + " and " + timeCancelEnd+" \n" +
                "    and (unix_timestamp(can.time_of_canceled))-(unix_timestamp(m.time_requesting))>30 group by timeRequest\n" +
                " union all  select 0, r.dates as d from report_dates r) as t1 where 1=1 \n" +
                " and t1.timeRequest between '"+DateTimeUtils.toDateTime(timeCancelStart*1000).toLocalDate() + "' and '" + DateTimeUtils.toDateTime(timeCancelEnd*1000).toLocalDate() +"'\n"+
                " group by t1.timeRequest order by t1.timeRequest desc");


        LOGGER.info("QUERY canceledMissionListByClient: " + sb.toString());
        //serviceEmailNotification.sendSimpleEmail("fr@bekker.com.ua", "subject", sb.toString());

        List listValue = session.createSQLQuery(sb.toString()).list();
        Iterator it = listValue.iterator();

        MissionCanceledByClientResponse response = new MissionCanceledByClientResponse();

        while(it.hasNext()) {
            Object row[] = (Object[]) it.next();
            response.getMissionCanceledByClientInfos().add(new MissionCanceledByClientInfo(row[1].toString(), row[0].toString()));
        }
        return response;
    }






    // список отмененные миссий по маске
    public CanceledMissionListResponse  canceledMissionList(int numberPage, int pageSize, String cancelBy, long cancelById, long taxoparkId, long timeCancelStart, long timeCancelEnd, String security_token){
        CanceledMissionListResponse response = new CanceledMissionListResponse();
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser == null){
            throw new CustomException(1, "Web user not found");
        }
        Session session = entityManager.unwrap(Session.class);

        String[] param = new String[]{"client","driver","operator","server"};

        if(cancelBy!=null && !cancelBy.isEmpty()){
            String q="";
            StringBuilder sb = new StringBuilder();
              if(cancelBy.equals("client")){
                   q="client";
              }else if(cancelBy.equals("driver")){
                  q="driver";
              }else if(cancelBy.equals("operator")){
                  q="operator";
              }else if(cancelBy.equals("server")){
                  q="server";
              }
            sb.append("select\n" +
                    "        count(distinct can.mission_id) as count\n" +
                    "        from mission m\n" +
                    "        left join client cl on cl.id = m.clientInfo_id\n" +
                    "        left join driver dr on dr.id = m.driverInfo_id\n" +
                    "        left join mission_canceled can on can.mission_id = m.id\n" +
                    "        where can.time_of_canceled is not null and 1=1 and can.cancel_by='"+q+"'");
            if(webUser.getTaxoparkId()!=null){
                sb.append(" and dr.taxopark_id=" + webUser.getTaxoparkId());
            }
            if(taxoparkId!=0){
                sb.append(" and dr.taxopark_id="+taxoparkId);
            }
            if(cancelById!=0){
                sb.append(" and cl.id="+cancelById);
            }
            if(timeCancelStart!=0 || timeCancelEnd!=0){
                sb.append(" and (unix_timestamp(can.time_of_canceled)) between "+timeCancelStart+" and "+timeCancelEnd);
            }

            sb.append(" and can.state_before_canceled!='COMPLETED'");

            int count = ((BigInteger)session.createSQLQuery(sb.toString()).uniqueResult()).intValue();

            if(cancelBy.equals("client")){
                response.setCountClient(count);
            }else if(cancelBy.equals("driver")){
                response.setCountDriver(count);
            }else if(cancelBy.equals("operator")){
                response.setCountOperator(count);
            }else if(cancelBy.equals("server")){
                response.setCountServer(count);
            }

        }else{
            List<String> queryCountList = new ArrayList<>();
            for(String by: param){
                StringBuilder sb = new StringBuilder();
                sb.append("select\n" +
                        "        count(distinct can.mission_id) as count\n" +
                        "        from mission m\n" +
                        "        left join client cl on cl.id = m.clientInfo_id\n" +
                        "        left join driver dr on dr.id = m.driverInfo_id\n" +
                        "        left join mission_canceled can on can.mission_id = m.id\n" +
                        "        where can.time_of_canceled is not null and 1=1 and can.cancel_by='"+by+"'");
                if(webUser.getTaxoparkId()!=null){
                    sb.append(" and dr.taxopark_id=" + webUser.getTaxoparkId());
                }
                if(taxoparkId!=0){
                    sb.append(" and dr.taxopark_id="+taxoparkId);
                }
                if(cancelById!=0){
                    sb.append(" and cl.id="+cancelById);
                }
                if(timeCancelStart!=0 || timeCancelEnd!=0){
                    sb.append(" and (unix_timestamp(can.time_of_canceled)) between "+timeCancelStart+" and "+timeCancelEnd);
                }
                sb.append(" and can.state_before_canceled!='COMPLETED'");

                queryCountList.add(sb.toString());
            }

            int countClient = ((BigInteger)session.createSQLQuery(queryCountList.get(0)).uniqueResult()).intValue();
            int countDriver = ((BigInteger)session.createSQLQuery(queryCountList.get(1)).uniqueResult()).intValue();
            int countOperator = ((BigInteger)session.createSQLQuery(queryCountList.get(2)).uniqueResult()).intValue();
            int countServer = ((BigInteger)session.createSQLQuery(queryCountList.get(3)).uniqueResult()).intValue();

            response.setCountClient(countClient);
            response.setCountDriver(countDriver);
            response.setCountOperator(countOperator);
            response.setCountServer(countServer);
        }


        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append("select SQL_CALC_FOUND_ROWS\n" +
                "   unix_timestamp(m.time_requesting),\n" +
                "   unix_timestamp(can.time_of_canceled) as tc,\n" +
                "   m.clientInfo_id,\n" +
                "   cl.phone as phoneClient,\n" +
                "   m.driverInfo_id,\n" +
                "   dr.phone as phoneDriver,\n" +
                "   can.cancel_by as cancelBy,\n" +
                "   m.id,\n" +
                "   can.state_before_canceled\n" +
                "        from mission m\n" +
                "        left join client cl on cl.id = m.clientInfo_id\n" +
                "        left join driver dr on dr.id = m.driverInfo_id\n" +
                "        left join mission_canceled can on can.mission_id = m.id\n" +
                "        where can.time_of_canceled is not null and 1=1 ");

        if(cancelBy!=null){
            queryBuilder.append(" and can.cancel_by='"+cancelBy+"'");
        }
        if(webUser.getTaxoparkId()!=null){
            queryBuilder.append(" and dr.taxopark_id=" + webUser.getTaxoparkId());
        }
        if(taxoparkId!=0){
            queryBuilder.append(" and dr.taxopark_id="+taxoparkId);
        }
        if(cancelById!=0){
            queryBuilder.append(" and cl.id="+cancelById);
        }
        if(timeCancelStart!=0 || timeCancelEnd!=0){
            queryBuilder.append(" and (unix_timestamp(can.time_of_canceled)) between "+timeCancelStart+" and "+timeCancelEnd);
        }

        queryBuilder.append(" and can.state_before_canceled!='COMPLETED'");

        queryBuilder.append(" GROUP BY can.mission_id order by tc desc LIMIT "+((numberPage) * pageSize)+","+pageSize);

        LOGGER.info("query = " + queryBuilder.toString());

        List<Object> result = session.createSQLQuery(queryBuilder.toString()).list();

        BigInteger totalCount = (BigInteger)session.createSQLQuery("SELECT FOUND_ROWS()").uniqueResult();

        response.setTotalItems(totalCount.longValue());
        Long lastPageNumber = ((totalCount.longValue() / pageSize) + 1);
        response.setLastPageNumber(lastPageNumber);

             Iterator it = result.iterator();

             while(it.hasNext()) {
                 Object row[] = (Object[]) it.next();
                 CancelMissionInfo cancelMissionInfo = new CancelMissionInfo();
                 if (row[0] != null) {
                     long time= Long.parseLong(row[0].toString())+21600;
                     cancelMissionInfo.setTimeOfRequesting(time*1000);
                 }
                 if (row[1] != null) {
                     long time= Long.parseLong(row[1].toString())+21600;
                     cancelMissionInfo.setTimeOfCanceled(time*1000);
                 }
                 if (row[2] != null) {
                     long clientId = Long.parseLong(row[2].toString());
                     ClientInfo clientInfo = ModelsUtils.toModel(clientRepository.findOne(clientId));
                     cancelMissionInfo.setClientInfo(clientInfo);
                 }
                 if (row[3] != null) {
                     cancelMissionInfo.setPhoneClient(row[3].toString());
                 }
                 if (row[4] != null) {
                     long driverId = Long.parseLong(row[4].toString());
                     DriverInfoARM driverInfoARM = ModelsUtils.toModelARM(driverRepository.findOne(driverId));
                     cancelMissionInfo.setDriverInfoARM(driverInfoARM);
                 }
                 if (row[5] != null) {
                     cancelMissionInfo.setPhoneDriver(row[5].toString());
                 }
                 if (row[6] != null) {
                     String cancBy = row[6].toString();
                     cancelMissionInfo.setCancelBy(cancBy);
                 }
                 if (row[7] != null) {
                     cancelMissionInfo.setMissionId(Long.parseLong(row[7].toString()));
                 }
                 if (row[8] != null) {
                     cancelMissionInfo.setStateBeforeCanceled(row[8].toString());
                 }
                 response.getCancelMissionInfoList().add(cancelMissionInfo);
             }
        return response;
    }





    public void cancelMissionByAdmin(long missionId, String comment, int reason, long initiatorId, WebUser webUser, String cancelBy) {
        Mission mission = missionRepository.findOne(missionId);
        if (mission != null) {
            String stateBeforeCanceled = mission.getState().toString();
            Driver driver = mission.getDriverInfo();
            if (driver != null) {
                driver.setCurrentMission(null);
                driverRepository.save(driver);
                // шлем нотификацию клиенту через нод
                JSONObject json = new JSONObject();
                try {
                    // событие для клиента и водителя
                    json.put("missionId", mission.getId());
                    // для водителя
                    nodeJsService.notified("mission_canceled_by_operator", json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            mission.setState(Mission.State.CANCELED);
            if (comment != null) {
                comment = comment + "\n" + "Прекращена. Причина: " + reason;
                mission.setComments(comment);
            }
            clearClientMission(mission);
            missionRepository.save(mission);
            missionCanceledStore(missionId, cancelBy, initiatorId, stateBeforeCanceled, reasonRepository.findOne(5L), "");

            mongoDBServices.createEvent(webUser != null ? 3 : 0, "" + (webUser != null ? webUser.getId() : 0), 3, 0, "cancelMissionByAdmin", "clientId:"+mission.getClientInfo().getId(), "");
        }
    }



    public void missionCanceledStore(long missionId, String cancelBy, Long cancelById, String stateBeforeCanceled, Reason reason, String reasonDescription){
        MissionCanceled missionCanceled = missionCanceledRepository.findByMissionId(missionId);
           if(missionCanceled == null){
               missionCanceled = new MissionCanceled();
               missionCanceled.setCancelBy(cancelBy);
               missionCanceled.setMissionId(missionId);
               missionCanceled.setCancelById(cancelById);
               missionCanceled.setTimeOfCanceled(DateTimeUtils.nowNovosib_GMT6());
               missionCanceled.setStateBeforeCanceled(stateBeforeCanceled);
               missionCanceled.setReasonInfo(reason);
               missionCanceled.setReason(reasonDescription);
               missionCanceledRepository.save(missionCanceled);
           }
    }



    public void clientSeated(long missionId) {
            Mission mission = missionRepository.findOne(missionId);
            DateTime timeOfSeating = timeService.nowDateTime();
            mission.setTimeOfSeating(timeOfSeating);
            mission.setState(Mission.State.IN_TRIP);
            missionRepository.save(mission);
            //notificationsService.missionStart(missionId);
    }








//    public PaymentInfo calculateMissionPrice(Mission mission) {
//        PaymentInfo result = new PaymentInfo();
//        if (mission != null) {
//            MissionRate missionRate = mission.getMissionRate();
//            Money total = MoneyUtils.getRubles(0);
//            // f:remove
//            //total.plus(missionRate.getPriceForAuto(mission.getAutoClass()).multipliedBy(mission.getStatistics().getDistanceInFact()));
//            //total.plus(missionRate.getPriceMinimal());
//
//            for (MissionService service : mission.getStatistics().getServicesExpected()) {
////                total.plus(missionRate.getPriceForService(service));
//            }
//
//            total.plus(missionRate.getPriceStop().multipliedBy(mission.getStatistics().getPauses().size()));
//
//            int pausesTime = 0;
//
//            for (Mission.PauseInfo pauseInfo : mission.getStatistics().getPauses()) {
//                pausesTime += Minutes.minutesBetween(pauseInfo.getStartPause(), pauseInfo.getEndPause()).getMinutes();
//            }
//            total.plus(missionRate.getPriceWaitingMinute().multipliedBy(pausesTime));
//
//            total.plus(missionRate.getPriceStop().multipliedBy(mission.getStatistics().getPauses().size()));
//
//            result.setTotalPrice(total.getAmount().doubleValue());
//            result.setBonusesAmount(mission.getClientInfo().getAccount().getBonuses().getAmount().doubleValue());
//            result.setPausesCount(mission.getStatistics().getPauses().size());
//            int missionTime = Minutes.minutesBetween(mission.getTimeOfArriving(), mission.getTimeOfFinishing()).getMinutes();
//            result.setMissionTime(missionTime);
//            result.setPausesTime(pausesTime);
//            result.setUseBonuses(mission.getStatistics().isUseBonuses());
//            result.setDistanceExpected(mission.getStatistics().getDistanceExpected());
//            result.setDistanceInFact(mission.getStatistics().getDistanceInFact());
//        }
//        return result;
//    }



    public PaymentInfo calculateMissionPrice(long missionId, PaymentInfo paymentInfo) {
        PaymentInfo result = paymentInfo;
        Mission mission = missionRepository.findOne(missionId);
        if (mission != null) {
//            paymentInfo.
        }
        //notificationsService.missionPayment(missionId);
        return result;
    }



    public MissionRate createMissionRate(MissionRateInfo missionRateInfo) {
        MissionRate missionRate = new MissionRate();
        missionRate.setName(missionRateInfo.getName());
        missionRate.setPriceMinimal(MoneyUtils.getRubles(missionRateInfo.getPriceMinimal()));
        missionRate.setPriceWaitingMinute(MoneyUtils.getRubles(missionRateInfo.getPriceMinute()));
        missionRate.setPriceStop(MoneyUtils.getRubles(missionRateInfo.getPriceStop()));
        missionRate.setFreeWaitingTime(missionRateInfo.getFreeWaitingTime());

        for (AutoClassRateInfo item : missionRateInfo.getAutoClassRateInfos()) {
            AutoClassPrice autoClassPrice = new AutoClassPrice();
            autoClassPrice.setAutoClass(AutoClass.getByValue(item.getAutoClass()));
            autoClassPrice.setPrice(MoneyUtils.getRubles(item.getPrice()));
            autoClassPrice.setKmIncluded(item.getKmIncluded());
            autoClassPrice.setPriceKm(item.getPriceKm());
            autoClassPrice.setPriceHour(item.getPriceHour());
            autoClassPrice.setFreeWaitMinutes(item.getFreeWaitMinutes());
            autoClassPrice.setPerMinuteWaitAmount(item.getPerMinuteWaitAmount());
            autoClassPrice.setIntercity(item.getIntercity());
            missionRate.getAutoClassPrices().add(autoClassPrice);
        }

//        for (ServicePriceInfo item : missionRateInfo.getServicesPrices()) {
//            ServicePrice servicePrice = new ServicePrice();
//            servicePrice.setService(MissionService.getByValue(item.getOptionId()));
//            servicePrice.setPrice(MoneyUtils.getRubles(item.getPrice()));
////            missionRate.getServicesPrices().add(servicePrice);
//        }

        missionRate = missionRatesRepository.save(missionRate);

        return missionRate;
    }




         public String getSmsText(long missionId, String clientPhone) throws IOException, JSONException{
             String result;
             //String url = "http://stg.taxisto.ru:81/index.php/client/GTD";ddd
             /*
              JSON={"MissionDetailsText":"
              Стоимость поездки: включая 10 минут ожидания и первые 3 км 145 руб
              Остановки в пути: 2 остановок на 1 минут по 5 руб за минуту -5 руб  Платные км (сверх включенных): 0 км по 12 руб0 руб
               Доп услуга: Животное - 50 руб  ИТОГО: 200.00 руб"}
             */

             JSONObject json = new JSONObject();
             json.put("missionId", missionId);
             json.put("clientPhone", clientPhone);

             DefaultHttpClient httpClient = new DefaultHttpClient();

             String answer = postToURL(missionDetailsText, json , httpClient); // message

             LOGGER.info("**************** answer = "+answer);

             JSONObject jsonObj = new JSONObject(answer);
             httpClient.getConnectionManager().shutdown();

             result = jsonObj.get("MissionDetailsText").toString();
                return result;
         }







    private static String postToURL(String url, JSONObject message, DefaultHttpClient httpClient) throws IOException, RuntimeException {
        HttpPost postRequest = new HttpPost(url);

        StringEntity input = new StringEntity(message.toString());
        input.setContentType("application/json");
        //input.setContentEncoding("UTF-8");
        postRequest.setEntity(input);

        HttpResponse response = httpClient.execute(postRequest);

        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatusLine().getStatusCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

        String output;
        StringBuffer totalOutput = new StringBuffer();

        while ((output = br.readLine()) != null) {
            totalOutput.append(output);
        }
        return totalOutput.toString();
    }





    /* объединить этот метод с методом waitDriverClientInMinutes, иначе фигня*/
    public int timeInTripInMinutes(Mission mission){
        DateTime timeOfSeating =  mission.getTimeOfSeating();
        DateTime timeOfFinishing =  mission.getTimeOfFinishing();

        int minutesWaitDriver = 0;

        if(timeOfSeating!=null && timeOfFinishing!=null){
            Minutes minutes = Minutes.minutesBetween(timeOfFinishing, timeOfSeating);
            minutesWaitDriver =  Math.abs(minutes.getMinutes());
        }
        return minutesWaitDriver;
    }





    public List<ServicePriceInfo> usedServicesList(Mission mission){
        List<MissionService> missionServiceList = mission.getStatistics().getServicesInFact();
        List<ServicePriceInfo> list = new ArrayList<>();
        for(MissionService missionService :missionServiceList){
            ServicePriceInfo servicePriceInfo = new ServicePriceInfo();
            servicePriceInfo.setOptionId(missionService.getValue());
            servicePriceInfo.setName(getHumanNameOption(missionService.getValue()));
            list.add(servicePriceInfo);
        }
          return list;
    }


    private String getHumanNameOption(int code){
        String result = "";
        switch (code){
            case 1:{
                result= "Дети 6-12 лет";
                break;
            }
            case 2:{
                result = "Багаж в салоне";
                break;
            }
            case 3:{
                result = "Провоз больших животных";
                break;
            }
            case 4:{
                result = "Встреча с табличкой";
                break;
            }
            case 5:{
                result = "Провоз маленьких животных";
                break;
            }
            case 6:{
                result = "Кондиционер";
                break;
            }
            case 7:{
                result = "Дети до 5 лет";
                break;
            }
            default: break;
        }
        return result;
    }






    public boolean isContainsDate(DateTime start, DateTime end){ //  Comission comission
        boolean result =false;
        Interval interval = new Interval(start, end); // comission.getStartTime(), comission.getEndTime()
        if(interval.contains(DateTimeUtils.toDate(DateTimeUtils.nowNovosib_GMT6()))){
             result = true;
        }
          return result;
    }




    public MissionCompleteResponse missionCompletedByAdmin(final long missionId, int reason, long webUserId)  {

        WebUser webUser = webUserRepository.findOne(webUserId);
        if(webUser == null){
           throw new CustomException(1,"Web user not found");
        }
        if (!EnumSet.of(AdministratorRole.ADMIN, AdministratorRole.DISPATCHER).contains(webUser.getRole())) {
            throw new CustomException(2,"Permission denied");
        }
        MissionCompleteResponse response = new MissionCompleteResponse();
        final Mission mission = missionRepository.findOne(missionId);

        if(mission==null || !EnumSet.of(Mission.State.IN_TRIP).contains(mission.getState())) {
            throw new CustomException(4,"Состояние мисии не позволяет перевести миссию в complete");
        }

        Client client = mission.getClientInfo();
        Driver driver = mission.getDriverInfo();

        if(client == null){
            throw new CustomException(4,"Client not found");
        }
        if(driver == null){
            throw new CustomException(5,"Driver not found");
        }

        List<DriverCashFlow> driverCashFlowList = cashRepository.findByMission(mission);

        if(driverCashFlowList!=null && !driverCashFlowList.isEmpty()){
            throw new CustomException(5,"По данной миссии уже произведена оплата");
        }

               SumDetails sumDetails = new SumDetails();
               int COUNT_PAUSES_IN_MINUTES = 0;
               int resultSumMission = 0;

               /* 1. Расчет пауз в пути*/
               PaymentInfo paymentInfo = new PaymentInfo();

               COUNT_PAUSES_IN_MINUTES = commonService.getPausesInMinutesCount(mission);

               /* 2. Использованные сервисы в пути (конечные)*/
               //List<ServicePriceInfo> list = usedServicesList(mission);

               List<Rerouting> rerouting = reroutingRepository.findByMissionId(mission.getId());

               // беру опции, которые получились по факту (животные, дети)
               List<MissionService> listServices = mission.getStatistics().getServicesInFact();

               int distanceReal = mission.getStatistics().getDistanceInFact()/1000;
               LOGGER.info("distanceReal="+distanceReal);

               if((mission.getLocationTo()==null || mission.getLocationTo()!=null) && rerouting!=null && !rerouting.isEmpty()){
                   //  была смена мершрута или не задан конечный адрес
                   LOGGER.info("была смена мершрута или не задан конечный адрес");
                   sumDetails = commonService.calculatePriceForMission(mission.getAutoClass().getValue(), distanceReal, listServices);
                   mission.getStatistics().setPriceInFact(Money.of(CurrencyUnit.of("RUB"), sumDetails.getSumWithOptions()));
                   missionRepository.save(mission);
               }else { // if(mission.getLocationTo()!=null && rerouting==null)
                   // указан конечный адрес и смены маршрута не было, значит берем price expected amount
                   LOGGER.info("указан конечный адрес и смены маршрута не было, значит берем price expected amount");
                   long distanceExpected= mission.getStatistics().getDistanceExpected() % 1000;
                   if (distanceExpected == 0) {
                       distanceExpected = distanceExpected/1000;
                   }
                   sumDetails = commonService.calculatePriceForMission(mission.getAutoClass().getValue(), distanceExpected, listServices);
               }

               if(mission.getLocationTo()==null || rerouting!=null) {
                   LOGGER.info("Была смена маршрута или не задан конечный адрес");
                   LOGGER.info("Сумма с допами = " + sumDetails.getSumWithOptions() + " сумма без допов = " + sumDetails.getSumWithoutOptions());
               }

               mission.setComments("Миссия завершена оператором");
               missionRepository.save(mission);


                /* расчет времени ожидания */

               int minutesWaitDriver = commonService.waitDriverClientInMinutes(mission);

               LOGGER.info("Паузы в минутах: "+COUNT_PAUSES_IN_MINUTES);
               LOGGER.info("Водитель прождал в мин: "+minutesWaitDriver);


               sumDetails = commonService.calculatePriceForWaiting(mission.getAutoClass().getValue(), minutesWaitDriver, sumDetails);

               sumDetails = commonService.calculatePriceForPauses(mission.getAutoClass().getValue(), COUNT_PAUSES_IN_MINUTES, sumDetails);

               resultSumMission+= sumDetails.getSumWithOptions();
               resultSumMission+= sumDetails.getSumForPauses();
               resultSumMission+= sumDetails.getSumForWaiting();

               LOGGER.info("sumDetails.getSumForPauses() = "+sumDetails.getSumForPauses());
               LOGGER.info("sumDetails.getSumForWaiting() = "+sumDetails.getSumForWaiting());
               LOGGER.info("Итоговая стоимость за поездку: "+resultSumMission);

               paymentInfo.setTotalPrice(resultSumMission);

               // снимаем % за заказ с водителя если она есть
               String commission = commonService.getPropertyValue("percentage_commission");
               int  percentageCommission = Integer.parseInt(commission)/100;

               Comission driverComission = comissionRepository.findByComissionTypeAndObjectId(1, driver.getId());
               Comission taxoparkComission = comissionRepository.findByComissionTypeAndObjectId(2, driver.getTaxoparkPartners().getId());
               List<Comission> defaultComissionList = comissionRepository.findByComissionType(0);
               Comission defaultComission = null;
                  if(defaultComissionList!=null && !defaultComissionList.isEmpty()){
                      defaultComission = defaultComissionList.get(0);
                  }

                 if(driverComission!=null){
                   // комиссия для водителя задана, проверяем актуальна ли она по дате
                       if(isContainsDate(driverComission.getStartTime(), driverComission.getEndTime())){
                           percentageCommission = driverComission.getComissionAmount();
                       }else{
                           if(taxoparkComission!=null){
                               if(isContainsDate(taxoparkComission.getStartTime(),taxoparkComission.getEndTime())){
                                   percentageCommission = taxoparkComission.getComissionAmount();
                               }else{
                                   if(defaultComission!=null){
                                       if(isContainsDate(defaultComission.getStartTime(), defaultComission.getEndTime())){
                                           percentageCommission = defaultComission.getComissionAmount();
                                       }
                                   }
                               }
                           }else{
                                if(defaultComission!=null){
                                    if(isContainsDate(defaultComission.getStartTime(), defaultComission.getEndTime())){
                                        percentageCommission = defaultComission.getComissionAmount();
                                    }
                                }
                           }
                       }
                 }else{
                     if(taxoparkComission!=null){
                         if(isContainsDate(taxoparkComission.getStartTime(),taxoparkComission.getEndTime())){
                             percentageCommission = taxoparkComission.getComissionAmount();
                         }else{
                             if(defaultComission!=null){
                                 if(isContainsDate(defaultComission.getStartTime(), defaultComission.getEndTime())){
                                     percentageCommission = defaultComission.getComissionAmount();
                                 }
                             }
                         }
                     }else{
                         if(defaultComission!=null){
                             if(isContainsDate(defaultComission.getStartTime(), defaultComission.getEndTime())){
                                 percentageCommission = defaultComission.getComissionAmount();
                             }
                         }
                     }
                 }

               double comissionAmount = resultSumMission*percentageCommission;
               LOGGER.info("Снимаем с волителя сумму ("+percentageCommission+"% от стоимости заказа) = "+comissionAmount);

                 if(comissionAmount!=0.0){
                     LOGGER.info("Комиссия не = 0, значит снимаем ее, комиссия = "+comissionAmount);
                     updateDriverBalanceSystem(mission.getDriverInfo().getId(), mission.getId(), -comissionAmount, 10);
                 }else{
                     LOGGER.info("Комиссия равна 0, не снимаем ничего с водителя");
                 }

    /*
  0 - оплата наличными, 1 - штраф(брони, заказы), 2 - пополнение счета (терминал), 3 - обналичивание (вывод) (АРМ), 4 - оплата безналом, 5 - пополнение бонусами, 6 - штраф (АРМ), 7 -  корректировка баланса (АРМ)
  8 - доплата водителю до суммы, 9 - начисление бонусов за кол-во выполненных заказов в сутки, 10 - комиссия с водителя за заказ
    */

               int driverResultSumMission = (int)(resultSumMission-comissionAmount);
               LOGGER.info("Итоговая сумма (сумма за миссию - %) = "+driverResultSumMission);

               int clientBonusesAmount = client.getAccount().getBonuses().getAmount().intValue();
               LOGGER.info("кол-во бонусов клиента = "+clientBonusesAmount);
               int diffBetweenSumMissionAndClientBonuses = clientBonusesAmount - resultSumMission;

                if(diffBetweenSumMissionAndClientBonuses>=0){
                    // списываем с бонусов клиента полную стоимость поездки
                    //Long clientId, Long missionId, double amount, int operation, WebUser webUser
                    // 2 - расчет за заказ (бонусы)
                    operationWithBonusesClient(client.getId(), missionId, -resultSumMission, 2, null, "", null);// old updateClientBonusesARM(client.getId(), missionId, -resultSumMission, 2, webUser);
                    LOGGER.info("С кошелька клиента снимаем сумму бонусов = "+diffBetweenSumMissionAndClientBonuses);
                    // снятую сумму бонусов я должен зачислить водителю
                    updateDriverBalanceSystem(mission.getDriverInfo().getId(), mission.getId(), resultSumMission, 5); // сюда передавать айди диспетчера
                }else{
                    diffBetweenSumMissionAndClientBonuses = Math.abs(diffBetweenSumMissionAndClientBonuses);
                    // значит бонусов меньше, чем получилось за поездку
                    // списываем все бонусы, клиенту наличкой доплатить разницу

                    operationWithClientCashFlow(client.getId(), missionId, -diffBetweenSumMissionAndClientBonuses, 0); // сумма налом, фиксируем ее в ClientCashFlow


                    LOGGER.info("С кошелька клиента снимаем сумму = "+clientBonusesAmount+ ". Доплата наличкой = "+diffBetweenSumMissionAndClientBonuses);
                    // начисляем данную сумму водителю
                       if(clientBonusesAmount!=0){
                           // начисляем сумму бонусов водителю
                           updateDriverBalanceSystem(mission.getDriverInfo().getId(), mission.getId(), clientBonusesAmount, 5); // сумма бонусов, которая плюсуется водителю
                           updateDriverBalanceSystem(mission.getDriverInfo().getId(), mission.getId(), diffBetweenSumMissionAndClientBonuses, 0); // сумма налом, которая плюсуется водителю
                           operationWithBonusesClient(client.getId(), missionId, -clientBonusesAmount, 2, null, "", null); // сумма бонусов, которую снимаем с клиента
                       }else{
                           // бонусы клиента = 0, значит плюсуем налом всю стоимость поездки
                           updateDriverBalanceSystem(mission.getDriverInfo().getId(), mission.getId(), resultSumMission, 0); // сумма налом, которая плюсуется водителю
                       }
                }

               // бонусеая сумма, которую мы начисляем водителю
               int sumIncrease = mission.getStatistics().getSumIncrease().getAmount().intValue();
                  if(sumIncrease!=0){
                      // Long driverId, Long missionId, double amount, int operation
                      updateDriverBalanceSystem(mission.getDriverInfo().getId(), mission.getId(), mission.getStatistics().getSumIncrease().getAmount().intValue(), 8);
                  }

               JSONObject jsonObject = new JSONObject();
               try {
                   jsonObject.put("missionId",missionId);
               } catch (JSONException e) {
                   e.printStackTrace();
               }
               nodeJsService.notified("mission_payment_finished", jsonObject);

               MDOrder mdOrder = mdOrderRepository.findByMission(mission);
                 if(mdOrder!=null){
                     // значит была оплата картой (частично, или полностью), значит зачисляем сумму оплаты по карте на счет водителя
                     // начисляем данную сумму водителю
                     //LOGGER.info("Начислили водителю сумму оплаты по карте в рублях = "+mdOrder.getSum()/100);
                     //updateDriverBalance(mission.getDriverInfo().getId(), mission.getId(), mdOrder.getSum()/100, 5, null);
                 }
               client.setMission(null);
               driver.setCurrentMission(null);

               driverRepository.save(driver);
               clientRepository.save(client);
           return response;
    }






    public GetTripDetailsTextResponse startGetText(long driverId, String security_token) throws IOException, JSONException {
        GetTripDetailsTextResponse response = new GetTripDetailsTextResponse();
        Session session = entityManager.unwrap(Session.class);

        Driver driver = driverRepository.findOne(driverId);
        //if(!commonService.isOkSecurityToken(driver, security_token, 2)){
            //return response;
        //}

        DetachedCriteria maxQuery = DetachedCriteria.forClass(Mission.class );
        maxQuery.add(Restrictions.eq("driverInfo", driver));
        maxQuery.setProjection( Projections.max( "timeOfRequesting" ) );

        Criteria criteria = session.createCriteria(Mission.class);
        criteria.add(Restrictions.eq("driverInfo", driver));
        criteria.add(Property.forName("timeOfRequesting").eq(maxQuery));

        Mission mission =  (Mission)criteria.uniqueResult();
           if(mission!=null){
               Client client = mission.getClientInfo();
               String smsText = getSmsText(mission.getId(), client.getPhone());
               serviceSMSNotification.sendCustomSMS(driver.getPhone(), smsText, "");
               serviceSMSNotification.sendCustomSMS(client.getPhone(), smsText, "");
                  response.setSent(true);
           }
            return response;
    }






    private int getCountSentPromoCodeByClient(Client client){
        int countSentPromoByClient = 0;

        DateTime s = new DateTime();
        DateTime e = new DateTime();

        DateTime d= e.plusDays(1);

        List<PromoCodes> promoCodesList = promoCodeRepository.findByFromIdAndDateOfIssueBetween(client.getId(), DateTimeUtils.toDate(s.withTimeAtStartOfDay()), DateTimeUtils.toDate(d.withTimeAtStartOfDay()));

        if(promoCodesList!=null){
            countSentPromoByClient = promoCodesList.size();
        }
        return countSentPromoByClient;
    }



    public int getCountSentPromoCodeByAllDay(){
        int countSendByDay = 0;
        // за все время
        List<PromoCodes> promoCodesList = promoCodeRepository.findByFromIdIsNotNullAndFromIdNot(10L); // findByFromIdIsNotNull
        if(promoCodesList!=null){
            countSendByDay = promoCodesList.size();
        }
        return countSendByDay;
    }





    public CountClientHelper getCountClientsByAllPeriod(){
        CountClientHelper countClientHelper = new CountClientHelper();

        int countAllClientAndroid = 0;
        int countAllClientApple = 0;
        int countClientTerminal = 0;
        int countClientOther = 0;

        Iterable<Client> clientList =  clientRepository.findAll();
        for(Client client: clientList){
              String deviceType = client.getDeviceType();
              if(deviceType!=null && !deviceType.isEmpty()){
                              if(deviceType.equals(DeviceInfo.Type.ANDROID_CLIENT.toString())){
                                  countAllClientAndroid++;
                              }else if(deviceType.equals(DeviceInfo.Type.APPLE.toString())){
                                  countAllClientApple++;
                              }else if(deviceType.equals(DeviceInfo.Type.ANDROID.toString())){
                                  countAllClientApple++;
                              }else if(deviceType.equals(DeviceInfo.Type.TERMINAL_CLIENT.toString())){
                                  countClientTerminal++;
                              }
              }else{
                  countClientOther++;
              }
          }
        countClientHelper.setCountClientAndroid(countAllClientAndroid);
        countClientHelper.setCountClientApple(countAllClientApple);
        countClientHelper.setCountClientTerminal(countClientTerminal);
        countClientHelper.setCountClientOther(countClientOther);

        return countClientHelper;
    }







    public CountClientHelper getCountClientsByPeriod(long start, long end){
        CountClientHelper countClientHelper = new CountClientHelper();

        int countClientAndroid = 0;
        int countClientApple = 0;
        int countClientTerminal = 0;
        int countClientOther = 0;

        List<Client> clientList =  clientRepository.findByRegistrationTimeBetween(DateTimeUtils.toDateTime(start),DateTimeUtils.toDateTime(end));

           for(Client client: clientList) {
               //Set<DeviceInfo> deviceInfoSet = client.getDevices();
               String deviceType = client.getDeviceType();
               if(deviceType!=null && !deviceType.isEmpty()){
                           if (deviceType.equals(DeviceInfo.Type.ANDROID_CLIENT.toString())) {
                               countClientAndroid++;
                           } else if (deviceType.equals(DeviceInfo.Type.APPLE.toString())) {
                               countClientApple++;
                           } else if (deviceType.equals(DeviceInfo.Type.ANDROID.toString())) {
                               countClientApple++;
                           } else if (deviceType.equals(DeviceInfo.Type.TERMINAL_CLIENT.toString())) {
                               countClientTerminal++;
                           }
           }else{
                   countClientOther++;
                }
           }

        countClientHelper.setCountClientAndroid(countClientAndroid);
        countClientHelper.setCountClientApple(countClientApple);
        countClientHelper.setCountClientTerminal(countClientTerminal);
        countClientHelper.setCountClientOther(countClientOther);

           return countClientHelper;
    }





    public int getCountSentPromoCodeByDay(){
        int countSendByDay = 0;

        DateTime s = new DateTime();
        DateTime e = new DateTime();
        DateTime d= e.plusDays(1);

        // за сутки
        List<PromoCodes> promoCodesList = promoCodeRepository.findByDateOfIssueBetweenAndFromIdNot(s.withTimeAtStartOfDay().getMillis(), d.withTimeAtStartOfDay().getMillis(), 10L);
        // за все время
        //List<PromoCodes> promoCodesList = promoCodeRepository.findByFromIdIsNotNull();

        if(promoCodesList!=null){
            countSendByDay = promoCodesList.size();
        }
        return countSendByDay;
    }



    // private String social_network; // VK,FB,MAIL,WHATSAPP,CONTACTS,INSTAGRAM
    public GetFreePromoCodeResponse getFreePromoCode(long clientId, String socialNetwork){
        GetFreePromoCodeResponse response = new GetFreePromoCodeResponse();
        response.setAmount(150);

        Client client = clientRepository.findOne(clientId);

            if(client!=null){

                String propertySentCode = getPropertySentCode("client_sent_code");
                //LOGGER.info("propertySentCode ==================="+propertySentCode+" CLIENT_SENT_CODE_NO="+CLIENT_SENT_CODE_NO+" propertySentCode.equals(CLIENT_SENT_CODE_NO)="+propertySentCode.equals(CLIENT_SENT_CODE_NO));
                if(propertySentCode.equals(CLIENT_SENT_CODE_NO)){
                    // не разрешена отправка промокодов
                    response = generatePromoCodeText(response, null, socialNetwork, CLIENT_SENT_CODE_NO);
                    response.getErrorCodeHelper().setErrorCode(6);
                    response.getErrorCodeHelper().setErrorMessage("К сожалению, рассылка промокодов временно недоступна");
                }else{
                    // разрешено

                    List<PromoCodes> promoCodesList = promoCodeRepository.findFreePromoCodes();

                    if(promoCodesList!=null && !promoCodesList.isEmpty()){

                        Random rand = new Random();
                        int randomIndex = rand.nextInt(promoCodesList.size());
                        PromoCodes promoCode = promoCodesList.get(randomIndex);

                        Money money = Money.of(MoneyUtils.BONUSES_CURRENCY, promoCode.getAmount());

                        // можно ли пользователю отсылать промокод с такой суммой
                        ClientSumPromoCode availableSumPromoCodeObj = availableSumPromoCodeRepository.findByClientIdAndAvailableAmount(clientId, promoCode.getAmount());
                        // сколько раз он может отсылать в сутки
                        ClientCountPromoCode availableCountPromoCodeObj = clientCountPromoCodeRepository.findByClientId(clientId);

                        // сколько конкретный клиент уже отправил промокодов за эти сутки
                        int countSentPromoCodeByClientByDay = getCountSentPromoCodeByClient(client);

                        LOGGER.info("сколько конкретный клиент уже отправил промокодов за эти сутки = "+countSentPromoCodeByClientByDay);

                        // сколько отправлено за текущие сутки вообще всеми клиентами
                        int countClientSentPromoCodeByDay = getCountSentPromoCodeByDay();
                        LOGGER.info("сколько отправлено за текущие сутки вообще всеми клиентами = "+countClientSentPromoCodeByDay);

                        // сколько можно отправлять промокодов в сутки
                        int availableCountPromoCodeSendByDay = Integer.parseInt(commonService.getPropertyValue("send_count_promo_code_of_day"));
                        LOGGER.info("сколько можно отправлять промокодов в сутки вообще= "+availableCountPromoCodeSendByDay);

                        // по дефолту из базы
                        String propClientAvailableSumPromoCode = commonService.getPropertyValue("client_available_sum_promo_code");
                        int propClientAvailableSumPromoCodeInt = Integer.parseInt(propClientAvailableSumPromoCode);

                        boolean sumIsOk = false;
                        if(availableSumPromoCodeObj!=null){
                            sumIsOk =true;
                        }else{
                            if(promoCode.getAmount() == propClientAvailableSumPromoCodeInt){
                                sumIsOk =true;
                            }
                        }

                        if(sumIsOk){
                            //значит клиенту можно рассылать промокоды с такой суммой
                            // проверяем не превышен ли лимит рассылки промокодов вообще за сутки всеми
                            if(countClientSentPromoCodeByDay<availableCountPromoCodeSendByDay){
                                // проверяем не превышен ли лимит рассылки конкретным клиентом за сутки
                                int count = 0;
                                String propClientAvailableCountPromo = commonService.getPropertyValue("client_available_count_promo_code");

                                if(availableCountPromoCodeObj!=null){
                                           count = availableCountPromoCodeObj.getAvailableCount();
                                }else{
                                    LOGGER.info("для клиента не задано предельно допустимое кол-во рассылки промокодов, берем из базы по дефолту");
                                       if(propClientAvailableCountPromo!=null){
                                           count = Integer.parseInt(propClientAvailableCountPromo);
                                       }
                                }
                                    // сколько дозволено отправлять клиенту
                                      if(count!=0) {
                                          if (countSentPromoCodeByClientByDay < count) {
                                              // кол-во отправленных за сутки не превышает предельно допустимого кол-ва, значит отсылаем
                                              //при этом плюсуем к тому, сколько он уже отослал и к общему кол-ву плюсуем

                                              String propExpirationDatePromoCode = commonService.getPropertyValue("expiration_date_promo_code_app");

                                              String promo_code = promoCode.getPromoCode();
                                              //DateTime dtOrg = new DateTime(DateTime.now());
                                              DateTime dtOrg = DateTimeUtils.nowNovosib_GMT6();
                                              response = generatePromoCodeText(response, promo_code, socialNetwork, CLIENT_SENT_CODE_YES);
                                              promoCode.setFromId(client.getId());
                                              promoCode.setChannel(socialNetwork);
                                              promoCode.setDateOfIssue(dtOrg.getMillis());// дата выдачи
                                              int plusDay = Integer.parseInt(propExpirationDatePromoCode);
                                              DateTime expirationDate = dtOrg.plusDays(plusDay);
                                              promoCode.setExpirationDate(expirationDate.getMillis());
                                              if (socialNetwork.equals("WHATSAPP")) {
                                                  promoCode.setAvailableUsedCount(1);
                                              }
                                              promoCodeRepository.save(promoCode);
                                              response.getErrorCodeHelper().setErrorCode(0);
                                              response.getErrorCodeHelper().setErrorMessage("Промокод выдан");

                                              mongoDBServices.createEvent(2, "" + clientId, 3, 0, "getFreePromoCode", "", "");


                                          } else {
                                              LOGGER.info("Клиент исчерпал дозволенное конкретно ему кол-во рассылки промокодов");
                                              response.getErrorCodeHelper().setErrorCode(5);
                                              response.getErrorCodeHelper().setErrorMessage("Вы исчерпали предельно допустимое количество рассылки промокодов");
                                          }
                                      }else{
                                          response.getErrorCodeHelper().setErrorCode(4);
                                          response.getErrorCodeHelper().setErrorMessage("для клиента не задано предельно допустимое кол-во рассылки промокодов");
                                      }
                            }else{
                                LOGGER.info("Превышен лимит рассылки промокодов за сутки");
                                response.getErrorCodeHelper().setErrorCode(2);
                                response.getErrorCodeHelper().setErrorMessage("Превышен лимит рассылки промокодов за сутки");
                            }
                        }else{
                            LOGGER.info("Клиенту нельзя рассылать промокоды с такой суммой");
                            response.getErrorCodeHelper().setErrorCode(1);
                            response.getErrorCodeHelper().setErrorMessage("Рассылка промокодов с данной суммой запрещена");
                        }
                    }else{
                        response = generatePromoCodeText(response, null, socialNetwork, CLIENT_SENT_CODE_YES);
                        response.getErrorCodeHelper().setErrorCode(6);
                        response.getErrorCodeHelper().setErrorMessage("Нет свободных промокодов");
                    }
                }
            }
          return response;
    }






    private GetFreePromoCodeResponse generatePromoCodeText(GetFreePromoCodeResponse response, String promo_code, String socialNetwork, String denied){
           String whatsAppSmsMailWithPromo = "Привет!\n" +
                   "Я пользуюсь Таксисто - новым крутым онлайн-сервисом заказа такси. \n" +
                   "Скачай приложение Таксисто в AppStore\n" +
                   "Введи промо-код В КОШЕЛЬКЕ ПРИЛОЖЕНИЯ и получи 150 рублей в подарок на первую поездку!)))\n" +
                   "Вся информация об этом сервисе на taxisto.ru.";

           String whatsAppSmsMailNOPromo = "Привет!\n" +
                "Я пользуюсь Таксисто - новым крутым онлайн-сервисом заказа такси. \n" +
                "Скачай приложение Таксисто в AppStore\n" +
                "Вся информация об этом сервисе на taxisto.ru.";


           if(denied.equals(CLIENT_SENT_CODE_NO)){
               // не разрешена рассылка промокодов
               if(socialNetwork.equals("VK")){
                   response.setTextToPost("");
                   response.setUrlPicture("http://www.taxisto.ru/mobile/social/vk.jpg");
               }else
               if(socialNetwork.equals("FB")){
                   response.setTextToPost("");
                   response.setUrlPicture("http://www.taxisto.ru/mobile/social/fb.png");
               }else
               //////////////////////////////////////
               if(socialNetwork.equals("MAIL")){
                   response.setTextToPost(whatsAppSmsMailNOPromo);
                   response.setSubject(whatsAppSmsMailNOPromo);
               }else
               if(socialNetwork.equals("WHATSAPP")){
                   response.setTextToPost(whatsAppSmsMailNOPromo);
                   response.setSubject(whatsAppSmsMailNOPromo);
               }else
               if(socialNetwork.equals("CONTACTS")){
                   response.setTextToPost(whatsAppSmsMailNOPromo);
                   response.setSubject(whatsAppSmsMailNOPromo);
               }else
               //////////////////////////////////////
               if(socialNetwork.equals("INSTAGRAM")){
                   response.setTextToPost("");
                   response.setUrlPicture("http://www.taxisto.ru/mobile/social/instagram.jpg");
               }
               response.getErrorCodeHelper().setErrorCode(0);
               response.getErrorCodeHelper().setErrorMessage("");
               response.setSubject("Привет!\n" +
                       "Я пользуюсь Таксисто - новым крутым онлайн-сервисом заказа такси. ");
                  return response;
           }else if(denied.equals(CLIENT_SENT_CODE_YES)){
           if(promo_code!=null){
               if(socialNetwork.equals("VK")){
                   response.setTextToPost("Промо-код на 150 рублей: "+promo_code);
                   response.setUrlPicture("http://www.taxisto.ru/mobile/social/vk.jpg");
               }else
               if(socialNetwork.equals("FB")){
                   response.setTextToPost("Промо-код на 150 рублей: "+promo_code);
                   response.setUrlPicture("http://www.taxisto.ru/mobile/social/fb.png");
               }else
               //////////////////////////////////////
               if(socialNetwork.equals("MAIL")){
                   //response.setTextToPost("MAIL промо-код: "+promo_code);
                   response.setTextToPost(whatsAppSmsMailWithPromo+"\n"+"Промо-код: "+promo_code);
               }else
               if(socialNetwork.equals("WHATSAPP")){
                   response.setTextToPost(whatsAppSmsMailWithPromo+"\n"+"Промо-код: "+promo_code);
               }else
               if(socialNetwork.equals("CONTACTS")){
                   response.setTextToPost(whatsAppSmsMailWithPromo+"\n"+"Промо-код: "+promo_code);
               }else
               //////////////////////////////////////
               if(socialNetwork.equals("INSTAGRAM")){
                   response.setTextToPost("Промо-код на 150 рублей: "+promo_code);
                   response.setUrlPicture("http://www.taxisto.ru/mobile/social/instagram.jpg");
               }
               response.getErrorCodeHelper().setErrorCode(0);
               response.getErrorCodeHelper().setErrorMessage("Ok");
               response.setSubject("Привет!\n" +
                       "Я пользуюсь Таксисто - новым крутым онлайн-сервисом заказа такси. ");
              }else{
               if(socialNetwork.equals("VK")){
                   response.setTextToPost("");
                   response.setUrlPicture("http://www.taxisto.ru/mobile/social/vk.jpg");
               }else if(socialNetwork.equals("FB")){
                   response.setTextToPost("");
                   response.setUrlPicture("http://www.taxisto.ru/mobile/social/fb.png");
               }else
               if(socialNetwork.equals("MAIL")){
                   response.setTextToPost(whatsAppSmsMailNOPromo);
               }else if(socialNetwork.equals("WHATSAPP")){
                   response.setTextToPost(whatsAppSmsMailNOPromo);
               }else if(socialNetwork.equals("CONTACTS")){
                   response.setTextToPost(whatsAppSmsMailNOPromo);
               }else
               if(socialNetwork.equals("INSTAGRAM")){
                   response.setTextToPost("INSTAGRAM");
                   response.setUrlPicture("http://www.taxisto.ru/mobile/social/instagram.jpg");
               }
               response.getErrorCodeHelper().setErrorCode(1);
               response.getErrorCodeHelper().setErrorMessage("Нет свободных промокодов");
               response.setSubject("Привет!\n" +
                       "Я пользуюсь Таксисто - новым крутым онлайн-сервисом заказа такси. ");
           }
       }
        return response;
    }




    public void cancelMission(boolean cancelBookedMission, Mission mission){
        // очищаем миссию и на клиенте и не водителе
        String stateBeforeCanceled = mission.getState().toString();
        mission.setState(Mission.State.CANCELED);
        missionRepository.save(mission);

        Client client = mission.getClientInfo();
        if(client!=null){
            client.setMission(null);
            client.setBookedMissions(null);// обнуляем поле booked_client_id из таблицы mission
            if(cancelBookedMission){
                serviceSMSNotification.cancelBookedMission(client.getPhone(), mission.getId(), "");
            }
            clientRepository.save(client);
        }
        Driver driver = mission.getDriverInfo();
        if(driver!=null){
            driver.setCurrentMission(null);
            driverRepository.save(driver);
        }
        // save cancel mission state
        missionCanceledStore(mission.getId(), "server", new Long(0), stateBeforeCanceled, null, "");
    }







    public AutoSearchElapsedTimeResponse autoSearchElapsedTime(long clientId, String security_token, long missionId) {
        AutoSearchElapsedTimeResponse response = new AutoSearchElapsedTimeResponse();
        if (!validatorService.validateUser(clientId, security_token, 1)) {
            response.getErrorCodeHelper().setErrorCode(3);
            response.getErrorCodeHelper().setErrorMessage("Client not found");
            return response;
        }
            Mission mission = missionRepository.findOne(missionId);

            if (mission != null) {
                if(mission.getState().equals(Mission.State.AUTO_SEARCH)) {
                    Seconds seconds = Seconds.secondsBetween(DateTimeUtils.nowNovosib_GMT6(), mission.getTimeOfRequesting());

                    int diffSec = seconds.getSeconds();
                      if(diffSec<0){
                          diffSec=Math.abs(diffSec);
                      }
                    int autoSearchElapsedTime = 600-diffSec;
                    response.setElapsedTime(autoSearchElapsedTime);
                }else{
                    response.getErrorCodeHelper().setErrorCode(4);
                    response.getErrorCodeHelper().setErrorMessage("Mission state is not AUTO_SEARCH");
                }
            }else {
                response.getErrorCodeHelper().setErrorCode(1);
                response.getErrorCodeHelper().setErrorMessage("Mission not found");
            }

        return response;
    }




     public void saveMissionStateStatistic(Mission mission, String state){
             if(missionStateStatisticRepository.findByMission(mission)==null){
                 MissionStateStatistic missStateStat = new MissionStateStatistic();
                 missStateStat.setMission(mission);
                 missStateStat.setState(state);
                 missStateStat.setDateTime(DateTimeUtils.nowNovosib_GMT6());
                 missionStateStatisticRepository.save(missStateStat);
             }
     }


     public UpdateWiFiResponse updateWiFi(long driverId, boolean status){
         UpdateWiFiResponse response = new UpdateWiFiResponse();
             Driver driver = driverRepository.findOne(driverId);
                 if(driver!=null){
                    driver.setWifi(status);
                    driverRepository.save(driver);
                    response.getErrorCodeHelper().setErrorCode(0);
                    response.getErrorCodeHelper().setErrorMessage("");
                 }else{
                    response.getErrorCodeHelper().setErrorCode(1);
                    response.getErrorCodeHelper().setErrorMessage("Водитель не найден");
                 }
                   return response;
        }


        public WiFiResponse statusWiFi(long driverId){
            WiFiResponse response = new WiFiResponse();
            Driver driver = driverRepository.findOne(driverId);
            if(driver!=null){
                response.setWifiStatus(driver.isWifi());
                response.getErrorCodeHelper().setErrorCode(0);
                response.getErrorCodeHelper().setErrorMessage("");
            }else{
                response.getErrorCodeHelper().setErrorCode(1);
                response.getErrorCodeHelper().setErrorMessage("Водитель не найден");
            }
            return response;
        }





    public UpdateMissionResponse  updateMission(MissionInfoARM missionInfoARM, String security_token){
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser==null){
            throw new CustomException(1, "Web user not found");
        }
        UpdateMissionResponse response = new UpdateMissionResponse();
        if(missionInfoARM!=null){
            Mission mission  = missionRepository.findOne(missionInfoARM.getId());

               if(missionInfoARM.getClientInfo()!=null){
                   long clientId = missionInfoARM.getClientInfo().getId();
                   Client client = clientRepository.findOne(clientId);
                   mission.setClientInfo(client);
               }
               if(missionInfoARM.getDriverInfoARM()!=null){
                   long driverId = missionInfoARM.getDriverInfoARM().getId();
                   Driver driver = driverRepository.findOne(driverId);
                   mission.setTaxopark(driver.getTaxoparkPartners());
                   mission.setDriverInfo(driver);
               }else{
                   mission.setDriverInfo(null);
               }

            int prevoiusSum = mission.getStatistics().getPriceInFact().getAmount().intValue();
            mission = ModelsUtils.fromModelUpdateMission(missionInfoARM, mission);
            missionRepository.save(mission);


            List<?> clientCashFlows;
            int sumAfterUpdate = mission.getStatistics().getPriceInFact().getAmount().intValue();
            boolean isCorporateMission = commonService.isCorporateMission(mission);
            if(isCorporateMission){
                 clientCashFlows = corporateClientCashFlowRepository.findByMission(mission);
            } else {
                 clientCashFlows = clientCashFlowRepository.findByMission(mission);
            }

            if(!CollectionUtils.isEmpty(clientCashFlows)){
                int diff = prevoiusSum - sumAfterUpdate;
                // если в кошельке клиента (корпоративного или обычного) есть запись и сумма заказа редактировалась, делаем в этом кошельке отметку
                if(diff!=0){
                    if(isCorporateMission){
                        updateCorporateClientCashFlow(mission.getClientInfo().getMainClient(), diff, 7, "", null);
                    } else {
                        operationWithBonusesClient(mission.getClientInfo().getId(), mission.getId(), diff, 6, webUser, "", null);
                    }
                }
            }
                response.setUpdate(true);
                mongoDBServices.createEvent(3, "" + webUser.getId(), 3, mission.getId(), "updateMission", "", "");
                //mongoDBServices.createEvent(3, "" + webUser.getId(), 3, "updateMission", "", "", mission.getClientInfo().getId(), mission.getDriverInfo() != null ? mission.getDriverInfo().getId() : 0);
        }
        response.setMissionInfoARM(missionInfoARM);
           return response;
    }




    public boolean isPaymentCardAvailable() {
        boolean result = false;
          Properties prop = propertiesRepository.findByPropName("card_payment");
              if(prop.getPropValue().equals("1")){
                  result = true;
              }
        return result;
    }




    public boolean isOverPrice(MissionInfo missionInfo){
        boolean result = false;
        if (missionInfo != null && missionInfo.getClientInfo() != null) {
            if (missionInfo.getPrice() >= 10000.0 || missionInfo.getPrice() < 0) {
                result = true;
            }
        }
         return result;
    }




    /**
     * create and store new mission. Set booking if needed
     * @param missionInfo - mission info
     * @return stored mission
     */
    @Transactional
    public FreeDriverResponse createNewMissionTerminal(MissionInfo missionInfo, String token, long terminalId) {
        FreeDriverResponse response = new FreeDriverResponse();
        Mission result;

        long clientId = missionInfo.getClientInfo().getId();
        Client client = clientRepository.findOne(clientId);
        Terminal terminal = terminalRepository.findOne(terminalId);

        if(terminal!=null){
            // добавляем айди терминала
        }

        if(client!=null){
            if (client.getToken() != null && client.getToken().equals(token)) {

                boolean hasNewMission = hasNewMission(clientId);

                if (!hasNewMission) {
                    // create mission
                    result = createMissionTerminal(missionInfo);

                    result.setTerminal(terminal);
                    missionRepository.save(result);

                    boolean booked = isApplicableForBooking(missionInfo);
                    if (booked) {
                        result.setState(Mission.State.BOOKED);
                        result.setBookingState(Mission.BookingState.WAITING);
                        client.getBookedMissions().add(result);
                    } else {
                        client.setMission(result);
                    }

                    result = missionRepository.save(result);
                    clientRepository.save(client);

                    if (!booked) {
                        //notificationsService.newMissionAvailable(result.getId());
                    }

                    List<MissionAddressesInfo> missionAddressesInfoList = missionInfo.getMissionAddressesInfos();

                    if (!missionAddressesInfoList.isEmpty()) {

                        for (MissionAddressesInfo missionAddressesInfo : missionAddressesInfoList) {
                            MissionAddresses missionAddresses = ModelsUtils.fromModel(missionAddressesInfo, result);
                            missionAddressesRepository.save(missionAddresses);
                            result.getMissionAddresses().add(missionAddresses);
                        }
                    }


                    response.setBooked(Mission.State.BOOKED.equals(result.getState()));
                    response.setMissionId(result.getId());

                    //mongoDBServices.createEvent(2, ""+clientId, 1, "driversRequestTerminal", "", "", clientId, 0);
                } else {
                    response.setMissionId(-1);
                    response.getErrorCodeHelper().setErrorCode(2);
                    response.getErrorCodeHelper().setErrorMessage("У Вас есть текущий заказ");
                }
            } else {
                response.setMissionId(-1);
                response.getErrorCodeHelper().setErrorCode(3);
                response.getErrorCodeHelper().setErrorMessage("Несовпадение ключа безопасности");
            }
        }else{
            response.setMissionId(-1);
            response.getErrorCodeHelper().setErrorCode(1);
            response.getErrorCodeHelper().setErrorMessage("Пользователь с такими данными не найден");
        }

        return response;
    }



    private int getSumOptions(List<Integer> options){
        int sumOptios = 0;
        if(options!=null){
            for (Integer val : options) {
                MissionService service = MissionService.getByValue(val);
                if (!MissionService.UNKNOWN.equals(service)) {
                    ru.trendtech.domain.ServicePrice servicesFromDB = servicesRepository.findByService(service);
                    sumOptios+= (int)servicesFromDB.getMoney_amount();
                }
            }
        }
        return sumOptios;
    }



    private int missionSumIncrease(MissionInfo missionInfo){
        int amount_uplift = Integer.parseInt(commonService.getPropertyValue("amount_uplift"));
        int priceExpected = (int)missionInfo.getExpectedPrice(); // было getPrice
        int realPrice = 0;
        int sumIncrease = 0;

        if(priceExpected==0){
              return 0;
        }

        /*
        if(priceExpected==299){
                realPrice = calculateCostForTrip(missionInfo);
                sumIncrease = realPrice - priceExpected;
                     минус сумма допников, если были
                     sumIncrease-= getSumOptions(missionInfo.getOptions());
            }else
        */

        //boolean lowCoster = AutoClass.getByValue(missionInfo.getAutoType()).equals(AutoClass.LOW_COSTER);
        //if(lowCoster){
        //    sumIncrease += 40;
        //}else {
                if (priceExpected < amount_uplift) {
                    sumIncrease += (amount_uplift - priceExpected);
                }
        //}

//            if(amount_uplift!=0){
//                if(lowCoster){
//                       sumIncrease += 40;
//                }else {
//                    if (priceExpected < amount_uplift) {
//                        sumIncrease += (amount_uplift - priceExpected);
//                    }
//                }
//            }else{
//                sumIncrease = Integer.parseInt(getPropertyValue("minus_cost"));
//            }
        LOGGER.info("sumIncrease="+sumIncrease);
           return sumIncrease;
    }



    public boolean isOverLimit(Client client, PaymentType paymentType, int missionPayAmount){
       boolean result = false;
          if(paymentType.equals(PaymentType.CORPORATE_BILL)){ // paymentType.equals(PaymentType.CORPORATE_CARD) ||
               // сумма(в коп.) потраченная за текущий месяц с корпоративной карты или корпоративного счета

              DateTime dateTime = new DateTime();
              DateTime firstDate = dateTime.dayOfMonth().withMinimumValue();
              DateTime lastDate = dateTime.dayOfMonth().withMaximumValue();

              Integer sumMonth = corporateClientCashFlowRepository.findSumMoneyAmountByDateTime(client, firstDate.withTimeAtStartOfDay(), lastDate.withTimeAtStartOfDay().plusDays(1)); // : потраченная сумма за месяц
              Integer sumWeek = corporateClientCashFlowRepository.findSumMoneyAmountByDateTime(client, dateTime.dayOfWeek().withMinimumValue(), dateTime.dayOfWeek().withMaximumValue()); // : потраченная сумма за неделю

              sumMonth = sumMonth!=null ? sumMonth: 0;
              sumWeek = sumWeek!=null ? sumWeek: 0;

              LOGGER.info("SUM BY CURRENT MONTH: "+sumMonth+" BY WEEK:" +sumWeek+" missionPayAmount: "+missionPayAmount);

              List<CorporateClientLimit> corporateClientLimitList = corporateClientLimitRepository.findByClient(client); //, client.getMainClient()

              if(corporateClientLimitList!=null){

                  for(CorporateClientLimit limit: corporateClientLimitList){
                        if(limit.getPeriod().equals(CorporateClientLimit.Period.MONTH)){
                            if((limit.getLimitAmount()-(sumMonth+missionPayAmount))<0){
                                 LOGGER.info("Вы превысили свой месячный лимит: limit.getLimitAmount(): "+limit.getLimitAmount());
                                 //throw new CustomException(7, "Вы превысили свой месячный лимит");
                                 return true;
                            }
                        }else if(limit.getPeriod().equals(CorporateClientLimit.Period.WEEK)){
                            if((limit.getLimitAmount()-(sumWeek+missionPayAmount))<0){
                                //throw new CustomException(8, "Вы превысили свой недельный лимит");
                                LOGGER.info("Вы превысили свой недельный лимит limit.getLimitAmount(): "+limit.getLimitAmount());
                                return true;
                            }
                        }
                  }
                         // здесь проверяем доступность средств на балансе главной коропартивной учетной записи текущего клиента
                          Money corporateBalance = client.getMainClient().getAccount().getCorporateBalance();
                          //Money creditLimitMoney = client.getMainClient().getAccount().getCreditLimitMoney();
                            if(corporateBalance.getAmount()!=null && ((corporateBalance.getAmount().intValue()*100)-missionPayAmount)<0) {
                                // вы превысили баланс корпоративного профиля [недостаточно средств]
                                LOGGER.info("Вы превысили баланс корпоративного профиля [недостаточно средств]: "+corporateBalance.getAmount());
                                //throw new CustomException(9, "Вы превысили баланс корпоративного профиля [недостаточно средств]");
                                return true;
                            }
                            /*
                            }else if((creditLimitMoney.getAmount().intValue()-missionPayAmount)<=0){
                               // вы превысили баланс кредитных средств
                               result = true;
                               //throw new CustomException(10, "Вы превысили баланс кредитных средств");
                            }
                            */
                  }
          }
        return result;
    }



    public boolean isCorporateClient(Client client){
        boolean result = false;
          if(client.getMainClient()!=null){
              result = true;
          }
        return  result;
    }


    public boolean corporateClientIsBlock(Client client){
        boolean result = false;
        if(client.getAccount().getState().equals(Account.State.BLOCKED)){
            result = true;
        }
        return  result;
    }






    public Mission createMissionWithMultipleSupport(MissionInfo missionInfo, String timeStarting) throws IOException, JSONException, ParseException {
        Mission mission;
        long clientId = missionInfo.getClientInfo().getId();
        Client client = clientRepository.findOne(clientId);

        DateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = form.parse(timeStarting);
        DateTime timeOfStart = DateTimeUtils.toDateTime(date.getTime());
        missionInfo.setTimeOfStart(timeOfStart.getMillis());

        /*
        boolean hasNewMission = hasNewMission(clientId);
        if(hasNewMission){
            throw new CustomException(2, "У Вас есть текущий заказ");
        }
        */

            // create mission
            boolean booked = missionInfo.isBooked();
            mission = createMissionSTR(missionInfo);

            int sumIncrease = missionSumIncrease(missionInfo);
            if(sumIncrease>0){
                Money amountIncreaseDefault = Money.of(CurrencyUnit.of("RUB"), sumIncrease);
                mission.getStatistics().setSumIncrease(amountIncreaseDefault);
                missionRepository.save(mission);
            }

            if(booked){
                mission.setIsBooked(Boolean.TRUE);
                mission.setState(Mission.State.BOOKED);
                mission.setBookingState(Mission.BookingState.WAITING);
                mission.setTimeOfStarting(timeOfStart);
                client.getBookedMissions().add(mission);
            }else{
                mission.setIsBooked(Boolean.FALSE);
                // если не бронь, то на сейчас, пишу правильно время
                DateTime dtStart = DateTimeUtils.nowNovosib_GMT6();
                mission.setTimeOfStarting(dtStart);
                client.setMission(mission);
            }

            mission = missionRepository.save(mission);

            /* save state stat */
            if(mission.isBooked()){
                saveMissionStateStatistic(mission, "BOOKED");
            }
            /* end */

            clientRepository.save(client);

            List<MissionAddressesInfo> missionAddressesInfoList = missionInfo.getMissionAddressesInfos();
            if (!missionAddressesInfoList.isEmpty()) {
                for (MissionAddressesInfo missionAddressesInfo : missionAddressesInfoList) {
                    MissionAddresses missionAddresses = ModelsUtils.fromModel(missionAddressesInfo, mission);
                    missionAddressesRepository.save(missionAddresses);
                    mission.getMissionAddresses().add(missionAddresses);
                }
            }

        /* определяем берег с которого делается заказ */
        RegionInfo info = pointInsidePolygon(missionInfo.getLocationFrom().getLatitude(), missionInfo.getLocationFrom().getLongitude());
        String coast = "unknown";
        if(info != null) {
            if (!StringUtils.isEmpty(info.getCoast())) {
                coast = info.getCoast();
            }
        }
        mission.setCoast(coast);
        missionRepository.save(mission);
        /* end */
        //mongoDBServices.createEvent(2, ""+clientId, 1, "createMissionWithMultipleSupport", "", "", clientId, 0);

        return mission;
    }




    private MissionInfo clearMissionAddresses(MissionInfo missionInfo){
        missionInfo.setAddressFrom(missionInfo.getAddressFrom().replaceAll("[{}]", ""));
        missionInfo.setAddressTo(missionInfo.getAddressTo().replaceAll("[{}]", ""));

           return missionInfo;
    }



    private Mission defineRegion(Mission mission){
        String coast = "unknown";
        RegionInfo info = pointInsidePolygon(mission.getLocationFrom().getLatitude(), mission.getLocationFrom().getLongitude());
        if(info != null){
            if (!StringUtils.isEmpty(info.getCoast())) {
               coast = info.getCoast();
            }
            mission.setCoast(coast);
            mission.setRegion(regionRepository.findOne(info.getId()));
            missionRepository.save(mission);
        }
          return mission;
    }




    @Transactional
    public FreeDriverResponse createNewMissionSTR(MissionInfo missionInfo, String timeStarting, String ip) throws IOException, JSONException, ParseException {
        FreeDriverResponse response = new FreeDriverResponse();
        Mission result;
        long clientId = missionInfo.getClientInfo().getId();
        boolean isAfterMin = false;
        Client client = clientRepository.findOne(clientId);
                int afterMin = Integer.parseInt(commonService.getPropertyValue("after_min_booked"));
                DateTime timeOfStart = DateTimeUtils.nowNovosib_GMT6();
                if(!timeStarting.equals("after_min")){
                    DateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = form.parse(timeStarting);
                    timeOfStart = DateTimeUtils.toDateTime(date.getTime());
                }else{
                    isAfterMin = true;
                    timeOfStart = timeOfStart.plusMinutes(afterMin);
                    missionInfo.setComment(String.format("Заказ через: %s мин.", afterMin).toUpperCase() + "\n" + (StringUtils.isEmpty(missionInfo.getComment()) ? "" : missionInfo.getComment()));
                }
                  missionInfo.setTimeOfStart(timeOfStart.getMillis());

                  //boolean booked = isApplicableForBooking(missionInfo);
                  //if(!booked){
                  boolean hasNewMission = hasNewMission(clientId);

                  if (!hasNewMission) {
                    // create mission
                    boolean booked = missionInfo.isBooked();

                    result = createMissionSTR(missionInfo);

                    result.setTimeIsAfter(isAfterMin);

                       int sumIncrease = missionSumIncrease(missionInfo);
                           if(sumIncrease>0){
                                Money amountIncreaseDefault = Money.of(CurrencyUnit.of("RUB"), sumIncrease);
                                result.getStatistics().setSumIncrease(amountIncreaseDefault);
                                missionRepository.save(result);
                           }
                      /*
                      boolean corporateClient = false;
                      if(EnumSet.of(PaymentType.CORPORATE_CASH, PaymentType.CORPORATE_BILL, PaymentType.CORPORATE_CARD).contains(PaymentType.getByValue(missionInfo.getPaymentType()))){
                          corporateClient = true;
                      }
                      */

                      missionRepository.save(result);

                      if(booked){
                          result.setIsBooked(Boolean.TRUE);
                          result.setState(Mission.State.BOOKED);
                          result.setBookingState(Mission.BookingState.WAITING);
                          client.getBookedMissions().add(result);
                          /* save state stat */
                             saveMissionStateStatistic(result, Mission.State.BOOKED.name());
                          /* end */
                      }else{
                          result.setIsBooked(Boolean.FALSE);
                          // если не бронь, то на сейчас, пишу правильно время
                          //DateTime dtStart = DateTimeUtils.nowNovosib_GMT6();
                          //result.setTimeOfStarting(dtStart);
                          client.setMission(result);
                      }
                      result.setTimeOfStarting(timeOfStart);
                      result = missionRepository.save(result);
                      clientRepository.save(client);


                    List<MissionAddressesInfo> missionAddressesInfoList = missionInfo.getMissionAddressesInfos();
                    if (!missionAddressesInfoList.isEmpty()) {
                        for (MissionAddressesInfo missionAddressesInfo : missionAddressesInfoList) {
                            MissionAddresses missionAddresses = ModelsUtils.fromModel(missionAddressesInfo, result);
                            missionAddressesRepository.save(missionAddresses);
                            result.getMissionAddresses().add(missionAddresses);
                        }
                    }
                    response.setBooked(Mission.State.BOOKED.equals(result.getState()));
                    response.setMissionId(result.getId());

                    /* определяем регион и берег старта заказа */
                       defineRegion(result);
                    /* end */

                    /* log */
                    mongoDBServices.createEvent(2, "" + clientId, 1, result.getId(), "createMissionStr", "", "");
                    grayLogService.sendToGrayLog(clientId, 0, 0, "createMissionStr", "Client", result.getId(), ip, "", "", "");

                } else {
                    response.setMissionId(-1);
                    response.getErrorCodeHelper().setErrorCode(2);
                    response.getErrorCodeHelper().setErrorMessage("У Вас есть текущий заказ");
                }
        return response;
    }





    @Transactional
    public FreeDriverResponse createNewMissionOLD_VERSION(MissionInfo missionInfo, String token) throws IOException, JSONException {
        FreeDriverResponse response = new FreeDriverResponse();
        Mission result = null;

        long clientId = missionInfo.getClientInfo().getId();
        Client client = clientRepository.findOne(clientId);

        if(client!=null){
            if (client.getToken() != null && client.getToken().equals(token)) {

                boolean hasNewMission = hasNewMission(clientId);

                if (!hasNewMission) {
                    // create mission
                    result = createMission(missionInfo);

                      /* Временный костыль до 15 ноября */
                    //Money amountIncreaseDefault = Money.of(CurrencyUnit.of("RUB"), 100);
                    //result.getStatistics().setSumIncrease(amountIncreaseDefault);
                    //missionRepository.save(result);
                      /*  */

                    boolean booked = isApplicableForBooking(missionInfo);
                    if (booked) {
                        result.setState(Mission.State.BOOKED);
                        result.setBookingState(Mission.BookingState.WAITING);
                        client.getBookedMissions().add(result);
                    } else {
                        client.setMission(result);
                    }

                    result = missionRepository.save(result);

                    client = clientRepository.save(client);

                    if (!booked) {
                        //notificationsService.newMissionAvailable(result.getId());
                    }

                    List<MissionAddressesInfo> missionAddressesInfoList = missionInfo.getMissionAddressesInfos();

                      /* create for solved bug*/
                    //List<String> latLonList = new ArrayList<>();
                      /* create for solved bug*/

                    if (!missionAddressesInfoList.isEmpty()) {

                        for (MissionAddressesInfo missionAddressesInfo : missionAddressesInfoList) {
                            MissionAddresses missionAddresses = ModelsUtils.fromModel(missionAddressesInfo, result);
                            missionAddressesRepository.save(missionAddresses);
                            result.getMissionAddresses().add(missionAddresses);
                        }

                    }
                    response.setBooked(Mission.State.BOOKED.equals(result.getState()));
                    response.setMissionId(result.getId());
                    response.getErrorCodeHelper().setErrorCode(0);
                    response.getErrorCodeHelper().setErrorMessage("Заказ успешно создан");
                    LOGGER.debug("###################### MISSION CREATED: id=" + result.getId());
                } else {
                    response.setMissionId(-1);
                    response.getErrorCodeHelper().setErrorCode(2);
                    response.getErrorCodeHelper().setErrorMessage("У Вас есть текущий заказ");
                    LOGGER.debug("###################### У Вас есть текущий заказ");
                }
            } else {
                response.setMissionId(-1);
                response.getErrorCodeHelper().setErrorCode(3);
                response.getErrorCodeHelper().setErrorMessage("Несовпадение ключа безопасности");
                LOGGER.debug("###################### Несовпадение ключа безопасности");
            }
        }else{
            response.setMissionId(-1);
            response.getErrorCodeHelper().setErrorCode(1);
            response.getErrorCodeHelper().setErrorMessage("Пользователь с такими данными не найден");
            LOGGER.debug("###################### Пользователь с такими данными не найден");
        }
        return response;
    }




    private String gisRegion(String point){
        String answer = "";
        String url = "http://catalog.api.2gis.ru/2.0/geo/search";

        List<NameValuePair> urlParameters = new ArrayList<>();

        urlParameters.add(new BasicNameValuePair("type", "adm_div.district,adm_div.city"));
        urlParameters.add(new BasicNameValuePair("page_size", "1"));
        urlParameters.add(new BasicNameValuePair("key", GIS_API_KEY));
        urlParameters.add(new BasicNameValuePair("point", point));

        try {
            //"result":{"total":1,"items":[
            //    {"id":"141347373711458","subtype":"district","subtype_name":"Район","full_name":"Новосибирск, Центральный","name":"Центральный","type":"adm_div"}
            //    ]}}
            answer = HTTPUtil.senPostQuery(url, urlParameters);

            LOGGER.info("````````````answer="+answer);

            JSONObject jsonRoot = new JSONObject(answer);

            //({"meta":{"code":404,"error":{"type":"itemNotFound","message":"Results not found"}}})

              if(jsonRoot.has("result")){
                  JSONObject jsonRes = (JSONObject)jsonRoot.get("result");
                  JSONArray jsonItems = (JSONArray)jsonRes.get("items");
                  JSONObject jsonItemSub = jsonItems.getJSONObject(0);
                  String str = jsonItemSub.get("full_name").toString();
                  answer = str.replaceAll("Новосибирск,","").trim();
              }else{
                   answer = "";
              }

        } catch (JSONException j) {
            LOGGER.info("gisRegion json exception: "+j.getMessage());
             j.printStackTrace();
              answer="";
        }
          return answer;
    }








    public Mission completeRegion(Mission mission){
        double latFrom = mission.getLocationFrom().getLatitude();
        double lonFrom = mission.getLocationFrom().getLongitude();
        String pointFrom = lonFrom+","+latFrom;

        LOGGER.info("pointFrom = "+pointFrom);
           if(mission.getLocationFrom().getRegion()==null){
                String val = gisRegion(pointFrom);
               if(!val.isEmpty()){
                   mission.getLocationFrom().setRegion("("+val+")");
               }
           }

        if(mission.getLocationTo()!=null && mission.getLocationTo().getRegion()==null){
            LOGGER.info("mission.getLocationTo()!=null");
            double latTo = mission.getLocationTo().getLatitude();
            double lonTo = mission.getLocationTo().getLongitude();
            String pointTo= lonTo+","+latTo;

            String val = gisRegion(pointTo);
            if(!val.isEmpty()) {
                mission.getLocationTo().setRegion("(" + val + ")");
            }
        }
        missionRepository.save(mission);
           return mission;
    }




    /**
     * create and store new mission. Set booking if needed
     * @param missionInfo - mission info
     * @return stored mission
     */
    @Transactional
    public FreeDriverResponse createNewMission(MissionInfo missionInfo, String token)  {
        FreeDriverResponse response = new FreeDriverResponse();
        Mission result;
        long clientId = missionInfo.getClientInfo().getId();
        Client client = clientRepository.findOne(clientId);

            if(client!=null){
              if (client.getToken() != null && client.getToken().equals(token)) {
                     //if(!booked){
                       boolean hasNewMission = hasNewMission(clientId);

                  if (!hasNewMission) {
                      // create mission
                      result = createMission(missionInfo);

                      int sumIncrease = missionSumIncrease(missionInfo);
                      if(sumIncrease>0){
                          Money amountIncreaseDefault = Money.of(CurrencyUnit.of("RUB"), sumIncrease);
                          result.getStatistics().setSumIncrease(amountIncreaseDefault);
                          missionRepository.save(result);
                      }

                      boolean booked = isApplicableForBooking(missionInfo);

                      if (booked) {
                          result.setIsBooked(Boolean.TRUE);
                          result.setState(Mission.State.BOOKED);
                          result.setBookingState(Mission.BookingState.WAITING);
                          client.getBookedMissions().add(result);
                      } else {
                          result.setIsBooked(Boolean.FALSE);
                          client.setMission(result);
                      }

                      result = missionRepository.save(result);
                      /* save state stat */
                      if(result.isBooked()){// booked
                          saveMissionStateStatistic(result, "BOOKED");
                      }
                      /* end */

                      clientRepository.save(client);

                      if (!booked) {
                          //notificationsService.newMissionAvailable(result.getId());
                      }

                      List<MissionAddressesInfo> missionAddressesInfoList = missionInfo.getMissionAddressesInfos();
                      if (!missionAddressesInfoList.isEmpty()) {
                          for (MissionAddressesInfo missionAddressesInfo : missionAddressesInfoList) {
                              MissionAddresses missionAddresses = ModelsUtils.fromModel(missionAddressesInfo, result);
                              missionAddressesRepository.save(missionAddresses);
                              result.getMissionAddresses().add(missionAddresses);
                          }
                      }
                      response.setBooked(Mission.State.BOOKED.equals(result.getState()));
                      response.setMissionId(result.getId());

                      /* костыль для перерасчета стоимости поездки*/
                      /*
                      try {
                          recalculatePriceMission(missionInfo, result, client);
                      } catch (JSONException e) {
                          e.printStackTrace();
                      }
                      */
                      /* end */

                      /* определяем регион и берег старта заказа */
                      result = defineRegion(result);

                      completeRegion(result);

                      mongoDBServices.createEvent(2, ""+clientId, 1, result.getId(), "createMission", "", "");
                  } else {
                      response.getErrorCodeHelper().setErrorCode(2);
                      response.getErrorCodeHelper().setErrorMessage("У Вас есть текущий заказ");
                  }
            } else {
                response.getErrorCodeHelper().setErrorCode(3);
                response.getErrorCodeHelper().setErrorMessage("Несовпадение ключа безопасности");
            }
        }else{
                response.getErrorCodeHelper().setErrorCode(1);
                response.getErrorCodeHelper().setErrorMessage("Пользователь с такими данными не найден");
        }
        return response;
    }






    public Mission recalculatePriceMission(MissionInfo missionInfo, Mission mission, Client client) throws IOException, JSONException {
        List<MissionAddressesInfo> missionAddressesInfoList = missionInfo.getMissionAddressesInfos();

            /* create for solved bug*/
            List<String> latLonList = new ArrayList<>();

        Location locationFrom = mission.getLocationFrom();
        String latFrom = Double.toString(locationFrom.getLatitude());
        String lonFrom = Double.toString(locationFrom.getLongitude());
        String la_lo_from = lonFrom+" "+latFrom;
        latLonList.add(la_lo_from);

        if (!missionAddressesInfoList.isEmpty()) {
            for (MissionAddressesInfo missionAddressesInfo : missionAddressesInfoList) {
                MissionAddresses missionAddresses = ModelsUtils.fromModel(missionAddressesInfo, mission);
                Location locationTo = missionAddresses.getLocation();
                if(locationTo!=null){
                    String latTo = Double.toString(locationTo.getLatitude());
                    String lonTo = Double.toString(locationTo.getLongitude());
                    String la_lo_to = lonTo+" "+latTo;
                    latLonList.add(la_lo_to);
            }
        }
      }

        //LOGGER.info("latLon="+latLonList);

        if(!latLonList.isEmpty()){
            //LOGGER.info("Recalculate la lo List = "+latLonList);
            CalculateDistanceResponse responseCalcDist = new CalculateDistanceResponse();
            responseCalcDist = calculateDistance2GIS(latLonList, client.getToken(), client.getId(), responseCalcDist);
            LOGGER.info("Recalculate responseCalcDist getDistanceKm= "+responseCalcDist.getDistanceKm());
            mission.getStatistics().setDistanceExpected(responseCalcDist.getDistanceKm()*1000);
            missionRepository.save(mission);
            boolean isCorporate = client.getMainClient() != null ? true: false;
            List<Integer> resultSumList = calculateStartPrice(missionInfo.getAutoType(), responseCalcDist.getDistanceKm(), missionInfo.getOptions(), isCorporate);
            Money priceExpected = Money.of(CurrencyUnit.of("RUB"), Double.parseDouble(resultSumList.get(1).toString()));
            mission.getStatistics().setPriceExpected(priceExpected);
            missionRepository.save(mission);
        }
          return mission;
    }


    /**
     * Check if mission should be booking
     * @param missionInfo mission
     * @return is booking
     */
    private static boolean isApplicableForBooking(MissionInfo missionInfo) {
        boolean result = false;
        if (missionInfo.getTimeOfStart() > 0) {
            DateTime now = DateTimeUtils.nowNovosib(); // было просто now
            DateTime timeOfStart = DateTimeUtils.toDateTime(missionInfo.getTimeOfStart());
            Minutes minutesBetween = Minutes.minutesBetween(now, timeOfStart);
            result = minutesBetween.getMinutes() >= 60;
            // поставить везде DateTime.now().getMillis()
        }
        return result;
    }



    public static boolean isApplicableForBookingTerminal(MissionInfo missionInfo) {
        boolean result = false;
        if (missionInfo.getTimeOfStart() > 0) {
            DateTime now = DateTimeUtils.nowNovosib_GMT6(); // было просто now
            DateTime timeOfStart = DateTimeUtils.toDateTime(missionInfo.getTimeOfStart());
            Minutes minutesBetween = Minutes.minutesBetween(now, timeOfStart);
            result = minutesBetween.getMinutes() >= 60;

            LOGGER.info("isApplicableForBookingTerminal timeNow= "+now+" timeOfStart= "+timeOfStart);

            // поставить везде DateTime.now().getMillis()
        }
        return result;
    }


    private static boolean isApplicableForBookingSTR(MissionInfo missionInfo) {
        boolean result = false;
        if (missionInfo.getTimeOfStart() > 0) {
            DateTime now = DateTimeUtils.nowNovosib_GMT6(); // было просто now
            DateTime timeOfStart = DateTimeUtils.toDateTime(missionInfo.getTimeOfStart());
            Minutes minutesBetween = Minutes.minutesBetween(now, timeOfStart);
            result = minutesBetween.getMinutes() >= 60;
        }
        return result;
    }




//    private static boolean isApplicableForBookingSTR(MissionInfo missionInfo, String timeStarting) throws ParseException {
//        boolean result = false;
//        if (missionInfo.getTimeOfStart() > 0) {
//            DateTime now = DateTimeUtils.now();
//
//            DateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Date date = form.parse(timeStarting);
//            DateTime timeOfStart = DateTimeUtils.toDateTime(date.getTime());
//
//            Minutes minutesBetween = Minutes.minutesBetween(now, timeOfStart);
//            result = minutesBetween.getMinutes() >= 60;
//        }
//        return result;
//    }





    private boolean hasNewMission(long clientId) {
        boolean hasNewMission = false;

            Client client = clientRepository.findOne(clientId);
               if(client.getMission()!=null){
                   hasNewMission = true;
               }else{
                       // hasNewMission = !missionRepository.findByClientInfoAndState(client, Mission.State.NEW).isEmpty();
                       List<Mission> listMission = missionRepository.findByClientInfoAndState(client, Mission.State.NEW);
                       if(!listMission.isEmpty()){
                           hasNewMission = true;
                       }
               }
        return hasNewMission;
    }





    private Mission createMissionSTR(MissionInfo missionInfo) throws IOException {
        Mission mission = new Mission();
        if(missionInfo.getComment()!=null){
            if(missionInfo.getComment().toLowerCase().startsWith("testt9")){
                mission.setTestOrder(true);
                missionInfo.setComment("Тестовый заказ. Не брать!");
            }
        }

        mission.setTimeOfRequesting(DateTimeUtils.nowNovosib_GMT6());
        mission.setState(Mission.State.NEW);
        mission.setPaymentStateCard(Mission.PaymentStateCard.NONE);
        mission.setMissionRate(getDefaultMissionRate());

        Client client = clientRepository.findOne(missionInfo.getClientInfo().getId());
        mission.setClientInfo(client);

        //boolean isCorporate = client.getMainClient() != null ? true: false;

        /* recalculate price [возможно нужно будет еще и дистанцию пересчитать] */
        /*
        List<Integer> resultSumList = calculateStartPrice(missionInfo.getAutoType(), missionInfo.getExpectedDistance(), missionInfo.getOptions(), isCorporate);
        missionInfo.setPrice(resultSumList.get(0)); // с опциями
        missionInfo.setExpectedPrice(resultSumList.get(1));
        */
        /* */

        /* recelculate price*/
        CalculatePriceResponse priceResponse = calculateCostForTrip(client, missionInfo);
        missionInfo.setPrice(priceResponse.getResultSum()); // с опциями
        missionInfo.setExpectedPrice(priceResponse.getResultSum());
        /* end */

        mission = ModelsUtils.fromModel(missionInfo, mission);
           return mission;
    }





    private Mission createMission(MissionInfo missionInfo) {
        Mission mission = new Mission();
        if(missionInfo.getComment()!=null){
            if(missionInfo.getComment().startsWith("testt9")){
                mission.setTestOrder(true);
                missionInfo.setComment("Тестовый заказ. Не брать!");
            }
        }
        mission.setTimeOfStarting((new DateTime(missionInfo.getTimeOfStart()))); // .plusHours(6) - это было
        mission.setTimeOfRequesting(new DateTime(missionInfo.getTimeOfRequesting()));
        mission.setState(Mission.State.NEW);
        mission.setPaymentStateCard(Mission.PaymentStateCard.NONE);
        mission.setMissionRate(getDefaultMissionRate());
        Client client = clientRepository.findOne(missionInfo.getClientInfo().getId());
        mission.setClientInfo(client);

        boolean isCorporate = client.getMainClient() != null ? true: false;

        /* recalculate price [возможно нужно будет еще и дистанцию пересчитать] */
        List<Integer> resultSumList = calculateStartPrice(missionInfo.getAutoType(), missionInfo.getExpectedDistance(), missionInfo.getOptions(), isCorporate);
        missionInfo.setPrice(resultSumList.get(0));
        missionInfo.setExpectedPrice(resultSumList.get(1));
        /* */

        mission = ModelsUtils.fromModel(missionInfo, mission);
          return mission;
    }




    private Mission createMissionTerminal(MissionInfo missionInfo) {
        Mission mission = new Mission();

        TimeZone tz = TimeZone.getTimeZone("GMT+6");
        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.MILLISECOND, tz.getOffset(timeService.now().getTime()));
        DateTime dt2= new DateTime(calendar2.getTime());
        mission.setTimeOfRequesting(dt2);

        mission.setTimeOfStarting((new DateTime(missionInfo.getTimeOfStart())).plusHours(6)); // .plusHours(6) - это было

        mission.setState(Mission.State.NEW);

        mission.setPaymentStateCard(Mission.PaymentStateCard.NONE);

        mission.setMissionRate(getDefaultMissionRate());

        Client client = clientRepository.findOne(missionInfo.getClientInfo().getId());
        mission.setClientInfo(client);

        mission = ModelsUtils.fromModel(missionInfo, mission);
        return mission;
    }




    public MissionRate getDefaultMissionRate() {
        return missionRatesRepository.findOne((long) 1);
    }

//    public class GTimerTask extends TimerTask {
//        private int seconds = 30;
//        private Timer GTimer;
//
//        GTimerTask(Timer Time,int sec,JLabel lab) {
//            this.seconds = sec;
//            this.GTimer = Time;
//        }
//
//        @Override
//        public void run() {
//            this.seconds--;
//            if (this.seconds<0) {
//            /* Делаем здесь то что вам нужно по истечению времени*/
//                GTimer.cancel();
//            } else {
//                //TLabel.setText("" + this.seconds);
//            }
//        }
//
//    }




    public void rebornMission(final long missionId, Money sumIncrease){
        final Mission[] mission = {missionRepository.findOne(missionId)};
        // sumIncrease - берем из пропертис

          if(mission[0] !=null){
              Money sumIncreaseDB = mission[0].getStatistics().getSumIncrease();
              mission[0].getStatistics().setSumIncrease(sumIncreaseDB.plus(sumIncrease));
              missionRepository.save(mission[0]);
          }


        if(mission[0] !=null){
            Thread sendMessage = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        double radius = 0;
                        int priority;
                        int timeSec = 0;
                        do{
                            mission[0] = missionRepository.findOne(missionId);
                            LOGGER.info("Mission state="+ mission[0].getState().toString());

                            JSONObject json = new JSONObject();
                                try{
                                json.put("missionId", mission[0].getId());
                                JSONObject loc = new JSONObject();
                                 loc.put("latitude", mission[0].getLocationFrom().getLatitude());
                                 loc.put("longitude", mission[0].getLocationFrom().getLongitude());
                                 json.put("location", loc);

                            if(timeSec>=30){
                                 if(mission[0].getState().equals(Mission.State.NEW)){
                                   // прошло 30 секунд, а статус миссии по прежнему NEW, значит канселим ее
                                     // тупо канселим миссию
                                     mission[0].setState(Mission.State.CANCELED);
                                     missionRepository.save(mission[0]);
                                     LOGGER.info("Mission cancel");
                                     return;
                                 }else{
                                     // любой другой статус, просто выходим
                                     return;
                                 }
                            }else {
                                timeSec++;
                                     /* приоритет - 2*/
                                    priority = 2;
                                   if(timeSec>0 && timeSec<=5){
                                   }
                                   else if(timeSec>5 && timeSec<=10){
                                       radius=7;
                                   }
                                   else if(timeSec>10 && timeSec<=15){
                                       radius=9;
                                   }
                                   else if(timeSec>15 && timeSec<=20){
                                       radius=12;
                                   }
                                   else if(timeSec>20 && timeSec<=23){
                                       radius=15;
                                   }
                                   else if(timeSec>23 && timeSec<=26){
                                       radius=15;
                                   }
                                   else if(timeSec>26 && timeSec<=30){
                                       radius=15;
                                   }
                                       json.put("count", priority);
                                       json.put("radius", radius);
                                       nodeJsService.notified("find_drivers", json);

                                    /* приоритет - 1 */

                                priority = 1;
                                if(timeSec>3 && timeSec<=5){
                                    radius=0.5;
                                }
                                else if(timeSec>5 && timeSec<=10){
                                    radius=1;
                                }
                                else if(timeSec>10 && timeSec<=15){
                                    radius=3;
                                }
                                else if(timeSec>15 && timeSec<=20){
                                    radius=5;
                                }
                                else if(timeSec>20 && timeSec<=23){
                                    radius=7;
                                }
                                else if(timeSec>23 && timeSec<=26){
                                    radius=10;
                                }
                                else if(timeSec>26 && timeSec<=30){
                                    radius=15;
                                }
                                json.put("count", priority);
                                json.put("radius", radius);
                                nodeJsService.notified("find_drivers", json);

                                LOGGER.info("Sleep one second");
                                Thread.sleep(1000);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        }while(mission[0].getState().equals(Mission.State.NEW));

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            sendMessage.start();
                 }
    }




    /*
    if(sumIncrease.getAmount().intValue()!=0){
                      //  старый вариант увеличения стоимости миссии, от которой отказался водитель (отдельная табличка)
                          MissionCostIncrease missionCostIncrease = missionCostIncreaseRepository.findByMission(mission);
                          if (missionCostIncrease == null) {
                              missionCostIncrease = new MissionCostIncrease();
                              missionCostIncrease.setTimeCreated(DateTime.now().getMillis());
                          }
                          missionCostIncrease.setDriver(mission.getDriverInfo());
                          missionCostIncrease.setClient(mission.getClientInfo());
                          missionCostIncrease.setMission(mission);
                          missionCostIncrease.setSumExpected(mission.getStatistics().getPriceExpected().getAmount().doubleValue()); // заявленная?
                          missionCostIncrease.setSumIncrease(sumIncrease.getAmount().doubleValue());
                          missionCostIncrease.setComment(comment);
                          missionCostIncreaseRepository.save(missionCostIncrease);

                      }
     */



    //f:add
    public MissionCostIncreaseResponse missionCostIncrease(long missionId, Double sumIncrease){
        MissionCostIncreaseResponse response = new MissionCostIncreaseResponse();

            Mission mission = missionRepository.findOne(missionId);
              if(mission!=null){
                  Money sumIncreaseDB = mission.getStatistics().getSumIncrease();
                  mission.getStatistics().setSumIncrease(sumIncreaseDB.plus(sumIncrease));
                  missionRepository.save(mission);
                  response.getErrorCodeHelper().setErrorCode(0);
                  response.getErrorCodeHelper().setErrorMessage("");

                  /* СТАРЫЙ ВАРИАНТ УВЕЛИЧЕНИ СУММЫ СТОИМОСТИ ПОЕЗДКИ
                  MissionCostIncrease missionCostIncrease = missionCostIncreaseRepository.findByMission(mission);
                     if(missionCostIncrease==null){
                        missionCostIncrease = new MissionCostIncrease();
                        missionCostIncrease.setTimeCreated(DateTime.now().getMillis());
                     }
                      missionCostIncrease.setDriver(mission.getDriverInfo());
                      missionCostIncrease.setClient(mission.getClientInfo());
                      missionCostIncrease.setMission(mission);
                      missionCostIncrease.setSumExpected(mission.getStatistics().getPriceExpected().getAmount().doubleValue()); // заявленная?
                      missionCostIncrease.setSumIncrease(sumIncrease);
                      missionCostIncrease.setComment(comment);
                      missionCostIncreaseRepository.save(missionCostIncrease);
                      response.setIncrease(true);
                      */
              }else{
                  response.getErrorCodeHelper().setErrorCode(1);
                  response.getErrorCodeHelper().setErrorMessage("Миссия не найдена");
              }
              return response;
    }



    //f:add
    public HistoryMissions onlyHistoryMissionsDriver(long driverId) {
        HistoryMissions result = null;
        Driver driver = driverRepository.findOne(driverId);
        if (driver != null) {
            List<Mission> missions = missionRepository.findByDriverInfoOrderByTimeOfStartingDesc(driver); // f:add
            result = new HistoryMissions();
            for (Mission mission : missions) {
                if (!Mission.State.BOOKED.equals(mission.getState())){
                    MissionInfo missionInfo = ModelsUtils.toModel(mission);
                    result.history.add(missionInfo);
                }
            }
        }
        return result;
    }



    public HistoryMissions onlyHistoryMissionsDriverARM(long driverId) {
        HistoryMissions result = null;
        Driver driver = driverRepository.findOne(driverId);
        if (driver != null) {
            List<Mission> missions = missionRepository.findByDriverInfoOrderByTimeOfStartingDesc(driver); // f:add
            result = new HistoryMissions();
            for (Mission mission : missions) {
                if (!Mission.State.BOOKED.equals(mission.getState())){
                    MissionInfo missionInfo = ModelsUtils.toModel(mission);
                    result.history.add(missionInfo);
                }
            }
        }
        return result;
    }



    //f:add
    public HistoryMissions onlyHistoryMissionsClient(long clientId) {
        HistoryMissions result = null;
        Client client = clientRepository.findOne(clientId);
        if (client != null) {
            List<Mission> missions = missionRepository.findByClientInfoOrderByTimeOfStartingDesc(client);
            result = new HistoryMissions();
            for (Mission mission : missions) {
                if (Mission.State.COMPLETED.equals(mission.getState())){
                    MissionInfo missionInfo = ModelsUtils.toModel(mission);
                    result.history.add(missionInfo);
                }
            }
        }

        return result;
    }




    public HistoryMissions onlyHistoryMissionsClientARM(long clientId) {
        HistoryMissions result = null;
        Client client = clientRepository.findOne(clientId);
        if (client != null) {
            List<Mission> missions = missionRepository.findByClientInfoOrderByTimeOfRequestingDesc(client);
            result = new HistoryMissions();
            for (Mission mission : missions) {
                result.history.add(ModelsUtils.toModel(mission));
            }
        }

        return result;
    }





    @Transactional
    public HistoryMissions missionsHistoryDriverV2(long driverId, String security_token) {
        HistoryMissions result = new HistoryMissions();
        Driver driver = driverRepository.findOne(driverId);

        //if(!commonService.isOkSecurityToken(driver, security_token, 2)){
            //return result;
        //}

        if (driver != null) {
            List<Mission> missions = missionRepository.findByDriverInfoAndStateOrderByTimeOfStartingDesc(driver, Mission.State.COMPLETED, new PageRequest(0, 5));
            if(!CollectionUtils.isEmpty(missions)){
                for(Mission mission : missions) {
                    MissionInfo missionInfo = ModelsUtils.toModel(mission);
                    result.history.add(missionInfo);
                }
            }

            missions = missionRepository.findByDriverInfoAndStateOrderByTimeOfStartingDesc(driver, Mission.State.BOOKED); // f:add
            for (Mission mission : missions) {
                MissionInfo missionInfo = ModelsUtils.toModel(mission);
                result.booked.add(missionInfo);
            }
        }
        return result;
    }





    @Transactional
    public HistoryMissions missionsHistoryDriver(long driverId) {
        HistoryMissions result = new HistoryMissions();
        Driver driver = driverRepository.findOne(driverId);
        if (driver != null) {
            List<Mission> missions = new ArrayList<>(); //missionRepository.findByDriverInfoAndStateOrderByTimeOfStartingDesc(driver, Mission.State.COMPLETED, new PageRequest(0, 1));
            if(!CollectionUtils.isEmpty(missions)){
                for(Mission mission : missions) {
                    MissionInfo missionInfo = ModelsUtils.toModel(mission);
                    result.history.add(missionInfo);
                }
            }

            missions = missionRepository.findByDriverInfoAndStateOrderByTimeOfStartingDesc(driver, Mission.State.BOOKED); // f:add
            for (Mission mission : missions) {
                MissionInfo missionInfo = ModelsUtils.toModel(mission);
                result.booked.add(missionInfo);
            }
        }
        return result;
    }


    /*
     public HistoryMissions missionsHistoryDriver(long driverId) {
        // f: если у брони статус driver_assigned or driver_approved, нужно возвращать еще инфу о драйвере по его id в mission.driverInfo_id
        HistoryMissions result = null;
        Driver driver = driverRepository.findOne(driverId);
        if (driver != null) {
            List<Mission> missions = missionRepository.findByDriverInfo(driver);
            result = new HistoryMissions();

            for (Mission mission : missions) {
                if (!Mission.State.BOOKED.equals(mission.getState())){
                    MissionInfo missionInfo = ModelsUtils.toModel(mission);
                    result.history.add(missionInfo);
                }
            }

            missions = missionRepository.findByDriverInfoAndState(driver, Mission.State.BOOKED);
            for (Mission mission : missions) {
                MissionInfo missionInfo = ModelsUtils.toModel(mission);
                result.booked.add(missionInfo);
            }


        }
        return result;
    }
     */


    /*
       public HistoryMissions missionsHistoryDriver(long driverId) {
        // если у брони статус driver_assigned or driver_approved, нужно возвращать еще инфу о драйвере по его id в mission.driverInfo_id
        HistoryMissions result = null;
        Driver driver = driverRepository.findOne(driverId);
        if (driver != null) {
            List<Mission> missions = missionRepository.findByDriverInfo(driver);
            result = new HistoryMissions();
            for (Mission mission : missions) {
                MissionInfo missionInfo = ModelsUtils.toModel(mission);
                result.history.add(missionInfo);
            }
        }
        return result;
    }
     */




    public List<Integer> calculateStartPrice(int autoClass, long distance, List<Integer> options, boolean isCorporate){
        // расчет первоначальной стоимости поездки
        AutoClassPrice autoClassPriceIncoming = null;
        List<Integer> resultList = new ArrayList<>();
        int sumWithOption;
        int sumWithoutOption = 0;
        int autoClassPrice=0;
        AutoClass autoClassComing = AutoClass.getByValue(autoClass);
        MissionRate missionRate = missionRatesRepository.findOne((long) 1);
        Set<AutoClassPrice> autoClassPriceSet = missionRate.getAutoClassPrices();
        if(autoClassPriceSet!=null){
            List<AutoClassPrice> autoClassPriceList = Lists.newArrayList(autoClassPriceSet.iterator());
                for(AutoClassPrice autoClassPriceDB :autoClassPriceList){
                     if(autoClassComing.getValue()==autoClassPriceDB.getAutoClass().getValue()){
                         autoClassPriceIncoming = autoClassPriceDB;
                         autoClassPrice=autoClassPriceDB.getPrice().getAmount().intValue();
                         int resPrice = 0;
                         if(distance>=0){
                               // значит не "из" и не "в" аэропорт
                               resPrice = commonService.calculatePriceByAutoClassAndDistance(distance, autoClassPriceDB, isCorporate);
                         }else {
                               // значит аэропорт, проверяем класс авто стандарт - или нет
                               if(autoClassComing.getValue()!=AutoClass.STANDARD.getValue()){
                                   // класс авто комфорт или бизнесс, значит считаем стоимость обычным способом
                                   resPrice = commonService.calculatePriceByAutoClassAndDistance(Math.abs(distance), autoClassPriceDB, isCorporate);
                               }
                         }
                         sumWithoutOption = resPrice;
                     }
                }
        }

        int minus_cost = Integer.parseInt(commonService.getPropertyValue("minus_cost"));
        // убрал 02.12.2014
        if(sumWithoutOption!=299){
              sumWithoutOption = Math.max(sumWithoutOption-minus_cost, autoClassPrice);
        }
        //LOGGER.info("autoClassComing === "+autoClassComing+" autoClassPriceIncoming = "+autoClassPriceIncoming);

        int KmIncluded = 0;
        if(autoClassPriceIncoming!=null){
            KmIncluded = autoClassPriceIncoming.getKmIncluded();
        }

        if(distance>KmIncluded ||  !EnumSet.of(AutoClass.LOW_COSTER, AutoClass.BONUS).contains(autoClassComing)){ //   !autoClassComing.equals(AutoClass.LOW_COSTER) ||!autoClassComing.equals(AutoClass.BONUS)
            //Округляем если дистанция больше чем включено в тариф или если тариф не лоукостер
            int z=sumWithoutOption % 5;
            if (z != 0) {
                z= 5-z;
                sumWithoutOption+= z;
            }
        }

        if(sumWithoutOption==0 && autoClassPriceIncoming!=null){
            sumWithoutOption = autoClassPriceIncoming.getPrice().getAmount().intValue();
        }
        resultList.add(sumWithoutOption);

           if(options!=null){
               for (Integer val : options) {
                   MissionService service = MissionService.getByValue(val);
                   if (!MissionService.UNKNOWN.equals(service)) {
                       ru.trendtech.domain.ServicePrice servicesFromDB = servicesRepository.findByService(service);
                            sumWithoutOption+= (int)servicesFromDB.getMoney_amount();
                         }
                   }
           }

        sumWithOption = sumWithoutOption; //Math.max(sumWithoutOption-15, autoClassPrice);

           if(distance > KmIncluded){ // autoClassPriceIncoming.getKmIncluded()
               int s=sumWithOption % 5;
               if (s != 0) {
                   s= 5-s;
                   sumWithOption+= s;
               }
           }
        if(sumWithOption==0 && autoClassPriceIncoming!=null){
            sumWithOption = autoClassPriceIncoming.getPrice().getAmount().intValue();
        }
        // сумма с доп услугами
        resultList.add(sumWithOption);
          return resultList;
    }




    public static class SumDetails {
        private int sumWithOptions=0;
        private int sumWithoutOptions=0;
        private int sumForWaiting=0;
        private int sumForPauses=0;

        public int getSumForWaiting() {
            return sumForWaiting;
        }

        public void setSumForWaiting(int sumForWaiting) {
            this.sumForWaiting = sumForWaiting;
        }

        public int getSumForPauses() {
            return sumForPauses;
        }

        public void setSumForPauses(int sumForPauses) {
            this.sumForPauses = sumForPauses;
        }

        public int getSumWithOptions() {
            return sumWithOptions;
        }

        public void setSumWithOptions(int sumWithOptions) {
            this.sumWithOptions = sumWithOptions;
        }

        public int getSumWithoutOptions() {
            return sumWithoutOptions;
        }

        public void setSumWithoutOptions(int sumWithoutOptions) {
            this.sumWithoutOptions = sumWithoutOptions;
        }
    }





    public CalculateDistanceResponse calculateDistance_StringAddress(List<String> adressesList, String token, long clientId, CalculateDistanceResponse response) throws IOException, JSONException { //  List<String> adressesList
        Client client = clientRepository.findOne(clientId);
        if (client != null) {
            if(client.getToken()!= null && client.getToken().equals(token)){

               List<String> latLonList = new ArrayList();
               for(String address: adressesList) {
                  // latLonList.add(fromAddressToLatLon(address));
               }


                int allDistance = 0;
                int k=0;

                for(int i=0; i<latLonList.size();i++){
                    k++;
                    String addressFrom = latLonList.get(i);
                    String addressTo = "";
                    if(k<latLonList.size()){
                        addressTo = latLonList.get(k);
                        String[] latLonAddressFrom = addressFrom.split("\\s");
                        String[] latLonAddressTo = addressTo.split("\\s");
                        allDistance+= commonService.routeBuildingFromLatLon(latLonAddressFrom[1], latLonAddressFrom[0], latLonAddressTo[1], latLonAddressTo[0]);
                    }
                }

                double distanceInKM = allDistance/1000.00;
                BigDecimal x = new BigDecimal(distanceInKM);
                BigDecimal roundedDistance = x.round(new MathContext(MathUtil.countDigit(x), RoundingMode.UP));
                //int minOne = roundedDistance.intValue();
                LOGGER.info("Полная дистанция в метрах: "+allDistance+ " в км: "+roundedDistance);

                response.setDistanceKm(roundedDistance.intValue()); // - сюда в мерах пишу  roundedDistance.intValue()
                response.getErrorCodeHelper().setErrorCode(0);
                response.getErrorCodeHelper().setErrorMessage("");
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





    private boolean isAeroBuilding(String buildingId){
       boolean contains = false;
       String[] buildingsAero = new String[]{"141373143532254","141373143532252","141373143532251","141373143560237","141373143532256", "141373143571305", "141373143532248"};
           for(String val: buildingsAero){
               if(buildingId.equals(val)){
                    contains=true;
               }
           }
         return contains;
    }


    /*
1	Дзержинский район
2	Железнодорожный район
3	Заельцовский район
4	Калининский район
5	Кировский район - левый
6	Ленинский район - левый
7	Октябрьский район
8	Первомайский район
9	Советский район
10	Центральный район
     */



    // 0 - не найден, 1 - левый - 2 - правый,
    private int regionType(String region){
      int result = 0;
          if(region.equals("Дзержинский") || region.equals("Дзержинский")|| region.equals("Железнодорожный") || region.equals("Заельцовский") || region.equals("Калининский") || region.equals("Октябрьский") || region.equals("Первомайский") || region.equals("Центральный")){ //  region.equals("Советский")
              result = 2;
          }else if(region.equals("Кировский") || region.equals("Ленинский")){
              result = 1;
          }
        return result;
    }



    public boolean isAeroPrice(List<String> latLonList){
        boolean result = false;

              String from = latLonList.get(0);
              String to = latLonList.get(1);
              String[] s_from = from.split(" ");
              String[] s_to = to.split(" ");

              LOGGER.info("from = "+from+" replace="+s_from[0]+","+s_from[1]+" to = "+s_to[0]+","+s_to[1]);

              String buildinId_From = getBuildinId(s_from[0]+","+s_from[1]);
              String buildinId_To = getBuildinId(s_to[0] + "," + s_to[1]);

                       if(isAeroBuilding(buildinId_From)){
                          // первый адрес аэропрот, проверяем район второго адреса
                           String val = gisRegion(s_to[0]+","+s_to[1]);
                                if(regionType(val)==2 || regionType(val)==1){
                                    // поездка на правый или левый берег
                                    result = true;
                                }
                                // else if(regionType(val)==1){
                                // поездка на левый берег
                                //    result = 250;
                                //}
                       }else{
                              if(isAeroBuilding(buildinId_To)){
                                  // второй адрес аэропорт, проверяем район откуда
                                  String val = gisRegion(s_from[0]+","+s_from[1]);
                                  if(regionType(val)==2 || regionType(val)==1){
                                      result = true;
                                  }
                                  /*
                                  if(regionType(val)==2){
                                      // поездка c правого берега
                                      result = 300;
                                  } else if(regionType(val)==1){
                                      // поездка с левого берега
                                      result = 250;
                                  }
                                  */
                              }
                       }
        return result;
    }




    public String getBuildinId(String latLon){
        String result = "";
        String answer = "";
        try {
            String url = "http://catalog.api.2gis.ru/2.0/geo/search?";
            //http://catalog.api.2gis.ru/2.0/geo/search?point=82.667452971821007,55.009529683447198&type=building&radius=200&key=ruoblg7173&fields=items.address&page_size=1
            List<NameValuePair> urlParameters = new ArrayList<>();

            urlParameters.add(new BasicNameValuePair("point", latLon));
            urlParameters.add(new BasicNameValuePair("type", "building"));
            urlParameters.add(new BasicNameValuePair("radius", "200"));
            urlParameters.add(new BasicNameValuePair("page_size", "1"));
            urlParameters.add(new BasicNameValuePair("key", GIS_API_KEY));

            answer = HTTPUtil.senPostQuery(url, urlParameters);

            JSONObject answerJson = new JSONObject(answer);

            JSONObject resultJson = (JSONObject) answerJson.get("result");

            JSONArray itemsArray = (JSONArray) resultJson.get("items");
            JSONObject building = (JSONObject) itemsArray.get(0);

            result = building.get("id").toString();

        }catch(JSONException n){
            LOGGER.debug("2GIS: getBuildinId JSONException: "+n.getMessage()+" answer: "+answer);
        }
          return result;
    }



    // to = true - "в", to = false - "с"
    private int getMarkupByRegion(double lat, double lon, boolean to){
        int markup = 0;
        RegionInfo info = pointInsidePolygon(lat, lon);
        if(info!=null){
            switch (info.getTypeRegion()) {
                case 0:{
                   // по городу
                   LOGGER.info(String.format("Точка %s,%s по городу. Регион: %s", lat, lon, info.getNameRegion()));
                   break;
                }
                case 1:{
                  // аэропорт
                    if(to){
                      // в аэропорт
                      LOGGER.info(String.format("Точка %s,%s в Аэропорт", lat, lon));
                      //markup = Integer.parseInt(getPropertyValue("to_aero"));
                      markup = info.getToMarkup();
                    }else{
                      // с аэропорта
                      LOGGER.info(String.format("Точка %s,%s с Аэропорта", lat, lon));
                      //markup = Integer.parseInt(getPropertyValue("from_aero"));
                      markup = info.getFromMarkup();
                    }
                    break;
                }
                case 2:{
                    // отдаленный район
                    if(!to){
                       // с отдаленного района
                       //markup = Integer.parseInt(getPropertyValue("from_remote"));
                       LOGGER.info(String.format("Точка %s,%s с отдаленного района", lat, lon));
                       markup = info.getFromMarkup();
                    }else{
                       LOGGER.info(String.format("Точка %s,%s в отдаленный район", lat, lon));
                       markup = info.getToMarkup();
                    }
                     break;
                }
                default:break;
            }
        }else{
            LOGGER.info(String.format("Точка %s,%s не в полигоне", lat, lon));
        }
          return markup;
    }








     // новый метод расчета стоимости + расстояние
     public CalculatePriceResponse calculateCostForTrip(Client client, MissionInfo missionInfo) throws IOException { // String security_token, long clientId,
         CalculatePriceResponse response = new CalculatePriceResponse();
         int distance; int resultSumMarkup = 0;
         List<String> latLonList = new ArrayList<>();
         List<Integer> listSumMarkup = new ArrayList<>();

         boolean isCorporate = client.getMainClient() != null ? true: false;
         if(missionInfo.getLocationFrom() == null){
             List<Integer> resultSumList = calculateStartPrice(missionInfo.getAutoType(), 0, missionInfo.getOptions(), isCorporate);
                response.setDistance(0);
                response.setResultSum(resultSumList.get(1));
                  return response;
         }

         latLonList.add(missionInfo.getLocationFrom().getLongitude()+" "+missionInfo.getLocationFrom().getLatitude());

            List<MissionAddressesInfo> addressesInfoList = missionInfo.getMissionAddressesInfos();
                for (MissionAddressesInfo missionAddressesInfo : addressesInfoList) {
                   double lat = missionAddressesInfo.getLatitude();
                   double lon = missionAddressesInfo.getLongitude();
                         if(lat != 0.0 && lon != 0.0){
                            latLonList.add(lon+" "+lat);
                         }
                }

         if(latLonList.size()==1){
             listSumMarkup.add(getMarkupByRegion(missionInfo.getLocationFrom().getLatitude(), missionInfo.getLocationFrom().getLongitude(), false));
         }else {
             int k = 0;
             for (int i = 0; i < latLonList.size(); i++) {
                 k++;
                 String s1 = latLonList.get(i);
                 if (k <= latLonList.size() - 1) {
                     String s2 = latLonList.get(k);
                     String la_lo_s1[] =  s1.split(" ");
                     String la_lo_s2[] =  s2.split(" ");
                     listSumMarkup.add(getMarkupByRegion(Double.parseDouble(la_lo_s1[1]), Double.parseDouble(la_lo_s1[0]), false));
                     listSumMarkup.add(getMarkupByRegion(Double.parseDouble(la_lo_s2[1]), Double.parseDouble(la_lo_s2[0]), true));
                 }
             }
         }

         if(!CollectionUtils.isEmpty(listSumMarkup)){
             Collections.sort(listSumMarkup);
             resultSumMarkup+=listSumMarkup.get(listSumMarkup.size()-1);
         }
               distance = calculateDistance(latLonList);

               List<Integer> resultSumList = calculateStartPrice(missionInfo.getAutoType(), distance, missionInfo.getOptions(), isCorporate);
               response.setResultSum(resultSumList.get(1) + resultSumMarkup);
               response.setDistance(distance);
                  return response;
     }






    private int calculateDistance(List<String> latLonList) throws IOException {
        if(latLonList.size()==1){
           // без конечного адреса
           return 0;
        }
        boolean byGoogle = false;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < latLonList.size(); i++) {
            if (i == 0) {
                builder.append(latLonList.get(i));
            } else {
                builder.append("," + latLonList.get(i));
            }
        }
        CommonService.DirectionHelper directionHelper = commonService.routeBuildingFromLatLon2GIS(builder.toString());

        if(directionHelper.getDistance() == 0){
            // пытаемся посчитать через гугл
            LOGGER.debug("Try calculate distance from Google");
            byGoogle = true;
            directionHelper.setDistance(commonService.calculateDistance_Google(latLonList));
        }

        BigDecimal x = new BigDecimal((directionHelper.getDistance()/1000.00));
        BigDecimal roundedDistance = x.round(new MathContext(MathUtil.countDigit(x), RoundingMode.UP));

        if(byGoogle){
            LOGGER.debug("GOOGLE: distance in m: " + directionHelper.getDistance() + " in km: "+roundedDistance+" latLonList="+latLonList);
        }else{
            LOGGER.debug("2GIS: distance in m: " + directionHelper.getDistance() + " in km: " + roundedDistance + " latLonList=" + latLonList);
        }

        return roundedDistance.intValue();
    }







    public CalculateDistanceResponse calculateDistance2GIS(List<String> latLonList, String token, long clientId, CalculateDistanceResponse response) throws IOException, JSONException { //  List<String> adressesList
        Client client = clientRepository.findOne(clientId);
        if (client != null) {
            if(client.getToken()!= null && client.getToken().equals(token)) {

                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < latLonList.size(); i++) {
                    if (i == 0) {
                        builder.append(latLonList.get(i));
                    } else {
                        builder.append("," + latLonList.get(i));
                    }
                }

                CommonService.DirectionHelper directionHelper = commonService.routeBuildingFromLatLon2GIS(builder.toString());
                int allDistance =  directionHelper.getDistance(); //routeBuildingFromLatLon2GIS(builder.toString());

                boolean service = false;

                 if(allDistance==0){
                     // пытаемся посчитать через гугл
                     LOGGER.debug("Try calculate distance from Google");
                     service=true;
                     allDistance = commonService.calculateDistance_Google(latLonList);
                 }

                /* ТАРИФ АЭРОПОРТ. ВКЛЮЧАЕМ С 20 декабря
                String aeroOn = getPropertyValue("tariff_aero");
                if (aeroOn.equals("1")) {
                    // on
                    if (latLonList.size() == 2) {
                        boolean aeroPrice = isAeroPrice(latLonList);
                        if (aeroPrice) {
                            // тариф аэропорт
                            allDistance = allDistance * (-1);
                        }
                    }
                }
                */
                        /* ТАРИФ АЭРОПОРТ */


                double distanceInKM = allDistance/1000.00;
                BigDecimal x = new BigDecimal(distanceInKM);
                BigDecimal roundedDistance = x.round(new MathContext(MathUtil.countDigit(x), RoundingMode.UP));

                if(service){
                    LOGGER.debug("GOOGLE: distance in m: "+allDistance+ " in km: "+roundedDistance+" clientId="+clientId+" latLonList="+latLonList);
                }else{
                    LOGGER.debug("2GIS: distance in m: "+allDistance+ " in km: "+roundedDistance+" clientId="+clientId+" latLonList="+latLonList);
                }

                response.setDistanceKm(roundedDistance.intValue()); // - сюда в мерах пишу  roundedDistance.intValue()
                response.getErrorCodeHelper().setErrorCode(0);
                response.getErrorCodeHelper().setErrorMessage("");
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





    public CalculateDistanceResponse calculateDistance(List<String> latLonList, String token, long clientId, CalculateDistanceResponse response) throws IOException{ //  List<String> adressesList
        Client client = clientRepository.findOne(clientId);
        if (client != null) {
            if(client.getToken()!= null && client.getToken().equals(token)){
               /*
                 old way - from address to lat lon
               List<String> latLonList = new ArrayList();
               for(String address: adressesList) {
                   latLonList.add(fromAddressToLatLon(address));
               }
               */

        int allDistance = 0;
        int k=0;

        for(int i=0; i<latLonList.size();i++){
            k++;
            String addressFrom = latLonList.get(i);
            String addressTo = "";
            if(k<latLonList.size()){
                addressTo = latLonList.get(k);
                String[] latLonAddressFrom = addressFrom.split("\\s");
                String[] latLonAddressTo = addressTo.split("\\s");
                if(latLonAddressTo[0]!=null && latLonAddressTo[1]!=null && !latLonAddressTo[0].equals("0.0") && !latLonAddressTo[1].equals("0.0")){
                    allDistance+= commonService.routeBuildingFromLatLon(latLonAddressFrom[1], latLonAddressFrom[0], latLonAddressTo[1], latLonAddressTo[0]);
                }
            }
        }

        double distanceInKM = allDistance/1000.00;
        BigDecimal x = new BigDecimal(distanceInKM);
        BigDecimal roundedDistance = x.round(new MathContext(MathUtil.countDigit(x), RoundingMode.UP));
        //int minOne = roundedDistance.intValue();
        LOGGER.debug("Fractal: distance in m: "+allDistance+ " in km: "+roundedDistance+" clientId="+clientId+" latLonList="+latLonList);
        //LOGGER.info("Полная дистанция в метрах: "+allDistance+ " в км: "+roundedDistance);

        response.setDistanceKm(roundedDistance.intValue()); // - сюда в мерах пишу  roundedDistance.intValue()
        response.getErrorCodeHelper().setErrorCode(0);
        response.getErrorCodeHelper().setErrorMessage("");
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











    public TripsHistorySiteResponse missionsHistoryClientSite(long clientId, String token, long startTime, long endTime, int numPage, int pageSize) {
        TripsHistorySiteResponse response = new TripsHistorySiteResponse();

            Client client = clientRepository.findOne(clientId);
            if (client != null) {
                if (client.getToken() != null && client.getToken().equals(token)) {
                    Session session = entityManager.unwrap(Session.class);
                    Criteria criteria = session.createCriteria(Mission.class);
                    criteria.createAlias("clientInfo", "client");
                    criteria = QueryUtils.fillDateTimeParameter(criteria, "timeOfRequesting", startTime, endTime);
                    criteria.add(Restrictions.eq("client.id", clientId));
                    criteria.add(Restrictions.eq("state", Mission.State.COMPLETED));
                    criteria.addOrder(Order.desc("timeOfRequesting"));
                    criteria.setProjection(Projections.rowCount());
                    Long total = (Long)criteria.uniqueResult();
                    criteria.setProjection(null);

                    long lastPageNumber = ((total / pageSize) + 1);
                    response.setLastPageNumber(lastPageNumber);

                    criteria.setFirstResult((numPage - 1) * pageSize);
                    criteria.setMaxResults(pageSize);

                    List<Mission> listMission = criteria.list();

                    for (Mission mission : listMission) {
                        MissionInfo missionInfo = ModelsUtils.toModelClient(mission);
                        response.getBookedAndHistory().add(missionInfo);
                    }
                } else {
                    LOGGER.info("Tokens are not equal");
                }
            }
        return response;
    }



    public void missionPause(long missionId, ItemLocation location, boolean pauseBegin) {
        Mission mission = missionRepository.findOne(missionId);
        if (mission != null) {
            if (pauseBegin) {
                mission.setState(Mission.State.IN_TRIP_PAUSED);
                Mission.PauseInfo pauseInfo = new Mission.PauseInfo();
                pauseInfo.setStartPause(DateTimeUtils.now());
                pauseInfo.setLocation(new Location(location.getLatitude(), location.getLongitude()));
                mission.getStatistics().getPauses().add(pauseInfo);
            } else {
                Mission.PauseInfo pauseInfo = mission.getStatistics().getPauses().get(mission.getStatistics().getPauses().size() - 1);
                pauseInfo.setEndPause(DateTimeUtils.now());
                mission.setState(Mission.State.IN_TRIP);
            }
            missionRepository.save(mission);
            if (pauseBegin) {
                //notificationsService.missionPauseStart(missionId, location);
            } else {
                //notificationsService.missionPauseEnd(missionId, location);
            }
        }
    }




    public List<RatingItem> ratingDrivers(long driverId) {
        ArrayList<RatingItem> result = new ArrayList<>();
        long totalCount = driverRepository.count();
        Iterable<Driver> driverIterable = driverRepository.findAll(new Sort(new Sort.Order(Sort.Direction.DESC, "ratingPoints")));
        long position = 1;
        for (Driver driver : driverIterable) {
            RatingItem ratingItem = ModelsUtils.buildRatingItem(totalCount, 0, driver.getId(), driver.getFirstName(), driver.getLastName(), driver.getRating());
            if (driver.getId() == driverId) {
                result.add(0, ratingItem);
            } else {
                result.add(ratingItem);
            }
            position++;
        }
        return result;
    }





    public String ratingDriver(long driverId) {
        String result = "";
        Driver driver = driverRepository.findOne(driverId);
         if(driver!=null){
             DriverRatingScale driverRatingScale = new DriverRatingScale();
                 if(driver.getRatingPoints()>5){
                     result = driverRatingScale.getNameRating(driver.getRating());
                 }
         }
        return result;
    }




    //f:add
    public AutoClassEditResponse autoClassEdit(long driverId, int autoClass){
        AutoClassEditResponse response = new AutoClassEditResponse();
          Driver driver = driverRepository.findOne(driverId);
              if(driver!=null){
                  driver.setAutoClass(AutoClass.getByValue(autoClass));
                  driverRepository.save(driver);
                    response.setEdit(true);
              }
          return response;
    }




    public String findCurrentStateDriver(long driverId, String security_token){
        String result = "";
        WebUser webUser = webUserRepository.findByToken(security_token);
          if(webUser==null){
              return result;
          }

        Driver driver = driverRepository.findOne(driverId);

        if(driver!=null){
              List<DriverLocks> driverLocks = driverLockRepository.findByDriverIdAndTimeOfUnlockIsNull(driver.getId(), new PageRequest(0, 1));
        if(!CollectionUtils.isEmpty(driverLocks)){
           // драйвер заблокирован
           result = "Водитель заблокирован";
        }else{
               Mission mission =  driver.getCurrentMission();
               //(занят, свободен, едет на заказ, едет с клиентом)
                  if(mission!=null){
                       if(mission.getState().equals(Mission.State.IN_TRIP)){
                           result = "Водитель едет с клиентом";
                       }else if(mission.getState().equals(Mission.State.ASSIGNED)){
                           result = "Водитель едет на заказ";
                       }
                  }else{
                      if(driver.getState().equals(Driver.State.BUSY)){
                          result = "Водитель занят";
                      }else if(driver.getState().equals(Driver.State.AVAILABLE)){
                          result = "Водитель свободен";
                      }else if(driver.getState().equals(Driver.State.OFFLINE)){
                          result = "Водитель offline";
                      }
                  }
             }
        }
          return result;
   }



    /*
     0 - оплата наличными, 1 - штраф(брони, заказы), 2 - пополнение счета (терминал), 3 - обналичивание (вывод) (АРМ), 4 - оплата безналом, 5 - пополнение бонусами, 6 - штраф (АРМ), 7 -  корректировка баланса (АРМ)
     8 - доплата водителю до суммы, 9 - начисление бонусов за кол-во выполненных заказов в сутки, 10 - комиссия с водителя за заказ
     */
    public void updateDriverBalanceARM(Long driverId, Long missionId, double amount, int operation, String security_token, String comment, Long articleAdjustmentId, String ip){
        WebUser webUser = webUserRepository.findByToken(security_token);
        if (webUser == null) {
            throw new CustomException(1,"Web user not found");
        }
        if(webUser.getTaxoparkId() == null){
            if (!EnumSet.of(AdministratorRole.ADMIN).contains(webUser.getRole())) {
                throw new CustomException(2,"Permission denied");
            }
        }
                    int fix = -1;
                    switch(operation){
                        case 3:{
                            //обналичивание (вывод) (АРМ)
                            operationWithMoney(driverId, missionId, amount > 0 ? amount * fix : amount, operation, webUser, comment, null, articleAdjustmentId);
                            break;
                        }
                        case 6:{
                            // штраф (АРМ)
                            operationWithMoney(driverId, missionId, amount>0?amount*fix:amount, operation, webUser, comment, null, articleAdjustmentId);
                            break;
                        }
                        case 7:{
                            // корректировка баланса (АРМ)
                            operationWithMoney(driverId, missionId, amount, operation, webUser, comment, null, articleAdjustmentId);
                            break;
                        }
                        default: {
                            throw new CustomException(4,"Not recognised operation");
                        }
                    }

              mongoDBServices.createEvent(3, ""+webUser.getId(), 3, missionId!=null ? missionId : 0, "driverUpdateBalance", "amount:"+amount, "");
              grayLogService.sendToGrayLog(0, driverId, webUser.getId(), "driverUpdateBalance", "Admin", 0, ip, "amount:"+amount, "", "");
    }





    /*
        1	Пополнение баланса (доплата за доп. км)	+
        2	Корректировка (пополнение баланса)	+
        3	Вывод ДС с баланса (безналичная выплата)	-
        4	Вывод ДС с баланса (наличная выплата)	-
        5	Корректировка (вывод ДС с баланса)	-
     */


    public void taxoparkUpdateBalanceARM(Long taxoparkId, Long missionId, int amount, int operation, String security_token, String comment, Long articleAdjustmentId){
        WebUser webUser = webUserRepository.findByToken(security_token);
        if (webUser == null) {
            throw new CustomException(1,"Web user not found");
        }
        if (!EnumSet.of(AdministratorRole.ADMIN).contains(webUser.getRole())) {
            throw new CustomException(2,"Permission denied");
        }
        TaxoparkPartners taxoparkPartners = taxoparkPartnersRepository.findOne(taxoparkId);
        if(taxoparkPartners == null){
            throw new CustomException(4,"Taxopark not found");
        }
        Mission mission = null;
        if(missionId != null){
            mission = missionRepository.findOne(missionId);
               if(mission == null){
                   throw new CustomException(5,"Mission not found");
               }
        }
        ArticleAdjustments article = null;
        if(articleAdjustmentId != null){
              article = articleAdjustmentsRepository.findOne(articleAdjustmentId);
                 if(article == null){
                     throw new CustomException(5,"Article not found");
                 }
        }
        int fix = -1;
        switch(operation){
            case 2:{
                // Корректировка (пополнение баланса)	+
                updateTaxoparkCashFlow(taxoparkPartners, amount < 0 ? amount*fix : amount, operation, comment, article, mission);
                break;
            }
            case 3:{
                // Вывод ДС с баланса (безналичная выплата)	-
                updateTaxoparkCashFlow(taxoparkPartners, amount < 0 ? amount : amount*fix, operation, comment, article, mission);
                break;
            }
            case 4:{
                // Вывод ДС с баланса (наличная выплата)	-
                updateTaxoparkCashFlow(taxoparkPartners, amount < 0 ? amount : amount*fix, operation, comment, article, mission);
                break;
            }
            case 5:{
                // Корректировка (вывод ДС с баланса)	-
                updateTaxoparkCashFlow(taxoparkPartners, amount < 0 ? amount : amount*fix, operation, comment, article, mission);
                break;
            }
            default: {
                throw new CustomException(4,"Not recognised operation");
            }
        }
          mongoDBServices.createEvent(3, ""+webUser.getId(), 3, missionId!=null ? missionId : 0, "taxoparkUpdateBalance", "taxoparkId:"+taxoparkId, "amount:"+amount);
        //mongoDBServices.createEvent(3, "" + webUser.getId(), 3, "taxoparkUpdateBalance", "taxoparkId: " + taxoparkId, "", 0, 0);
    }






     public void updateDriverBalanceSystem(Long driverId, Long missionId, double amount, int operation){
        /*
      0 - оплата наличными, 1 - штраф(брони, заказы), 2 - пополнение счета (терминал), 3 - обналичивание (вывод) (АРМ), 4 - оплата безналом, 5 - пополнение бонусами, 6 - штраф (АРМ), 7 -  корректировка баланса (АРМ)
      8 - доплата водителю до суммы, 9 - начисление бонусов за кол-во выполненных заказов в сутки, 10 - комиссия с водителя за заказ
      11 - чаевые
      12 - начисление ЗП зарплатному водителю
      13 - снятие с ЗП за платный отдых
        */
          int fix = -1;
                        switch(operation){
                            case 0:{
                                // оплата наличными
                                operationWithMoney(driverId, missionId, amount>0?amount:amount*fix, operation, null, "", null, null);
                                break;
                            }
                            case 1:{
                                // штраф (брони, заказы)
                                operationWithMoney(driverId, missionId, amount>0?amount*fix:amount, operation, null, "", null, null);
                                break;
                            }
                            case 2:{
                                //пополнение счета
                                operationWithMoney(driverId, missionId, amount>0?amount:amount*fix, operation, null, "", null, null);
                                break;
                            }
                            case 4:{
                                //оплата безналом
                                operationWithMoney(driverId, missionId, amount>0?amount:amount*fix, operation, null, "", null, null);
                                break;
                            }
                            case 5:{
                                // пополнение бонусами
                                operationWithMoney(driverId, missionId, amount>0?amount:amount*fix, operation, null, "", null, null);
                                break;
                            }
                            case 8:{
                                // доплата водителю до суммы
                                operationWithMoney(driverId, missionId, amount>0?amount:amount*fix, operation, null, "", null, null);
                                break;
                            }
                            case 9:{
                                // начисление бонусов за кол-во выполненных заказов в сутки
                                operationWithMoney(driverId, missionId, amount, operation, null, "", null, null);
                                break;
                            }
                            case 10:{
                                // комиссия за заказ с водителя
                                operationWithMoney(driverId, missionId, amount>0?amount*fix:amount, operation, null, "", null, null);
                                break;
                            }
                            case 11:{
                                // чаевые
                                operationWithMoney(driverId, missionId, amount>0?amount:amount*fix, operation, null, "", null, null);
                                break;
                            }


                            /* операции связанные с зарплатными водителями */
                            case 12:{
                                // начисление ЗП зарплатному водителю
                                operationWithMoney(driverId, missionId, amount, operation, null, "", null, null);
                                break;
                            }
                            case 13:{
                                // снятие с ЗП за платный отдых
                                operationWithMoney(driverId, missionId, amount, operation, null, "", null, null);
                                break;
                            }


                            case 19:{
                                // снятие суммы за пользование планшетом
                                operationWithMoney(driverId, missionId, amount, operation, null, "", null, null);
                                break;
                            }
                            default: {
                                break;
                            }
                        }
        }





        // old version
        @Transactional
        public boolean updateClientBonuses(Long clientId, Money amountOfMoney, WebUser webUser) {
            boolean result = false;
            Client client = clientRepository.findOne(clientId);

            if(client!=null){
                Account clientAccount =  accountRepository.findOne(client.getAccount().getId());
                Money money = clientAccount.getBonuses();
                clientAccount.setBonuses(money.plus(amountOfMoney));
                accountRepository.save(clientAccount);
                result = true;
                sendPaymentStat(webUser, amountOfMoney.getAmount().doubleValue(), client, false); // true - driver, false - client
            }
            return result;
        }






    private void updateTaxoparkCashFlow(TaxoparkPartners taxoparkPartners, int amount, int operation, String comment, ArticleAdjustments article, Mission mission){
        Money amountOfMoney = Money.of(CurrencyUnit.of("RUB"), amount);
        Money taxoparkBalance = taxoparkPartners.getMoneyAmount();
        taxoparkPartners.setMoneyAmount(taxoparkBalance.plus(amountOfMoney));

        taxoparkPartnersRepository.save(taxoparkPartners);

        TaxoparkCashFlow taxoparkCashFlow = new TaxoparkCashFlow();
        taxoparkCashFlow.setComment(comment);
        taxoparkCashFlow.setArticle(article);
        taxoparkCashFlow.setDateOperation(DateTimeUtils.nowNovosib_GMT6());
        taxoparkCashFlow.setSum(amount * 100);
        taxoparkCashFlow.setOperation(operation);
        taxoparkCashFlow.setMission(mission);
        taxoparkCashFlow.setTaxopark(taxoparkPartners);
        taxoparkCashFlowRepository .save(taxoparkCashFlow);
    }




    private void updateCorporateClientCashFlow(Client mainClient, int amount, int operation, String comment, ArticleAdjustments article){
            Account mainClientAccount =  accountRepository.findOne(mainClient.getAccount().getId());
            Money amountOfMoney = Money.of(CurrencyUnit.of("RUB"), amount);
            Money corporateBalance = mainClientAccount.getCorporateBalance();
            mainClientAccount.setCorporateBalance(corporateBalance.plus(amountOfMoney));

            accountRepository.save(mainClientAccount);

            CorporateClientCashFlow corporateClientCashFlow = new CorporateClientCashFlow();
            corporateClientCashFlow.setComment(comment);
            corporateClientCashFlow.setArticle(article);
            corporateClientCashFlow.setDateOperation(DateTimeUtils.nowNovosib_GMT6());
            corporateClientCashFlow.setSum(amount * 100);
            corporateClientCashFlow.setMainClient(mainClient);
            corporateClientCashFlow.setClient(mainClient);
            corporateClientCashFlow.setOperation(operation);
            corporateClientCashFlowRepository.save(corporateClientCashFlow);
    }







    @Transactional
    public UpdateCorporateClientBalanceResponse updateCorporateClientBalance(long mainClientId, int amount, int operation, String security_token, String comment, Long articleId, String ip){
        WebUser webUser = webUserRepository.findByToken(security_token);

        if (webUser == null) {
            throw new CustomException(1,"Web user not found");
        }
        if (!EnumSet.of(AdministratorRole.ADMIN).contains(webUser.getRole())) {
            throw new CustomException(2,"Permission denied");
        }

        ArticleAdjustments article = articleAdjustmentsRepository.findOne(articleId);
        if(article == null){
            throw new CustomException(4, "Статья не найдена");
        }

        Client mainClient = clientRepository.findOne(mainClientId);
        if(mainClient == null || !mainClient.getId().equals(mainClient.getMainClient().getId())){
            throw new CustomException(5, "Клиент не найден или не является главным");
        }

        UpdateCorporateClientBalanceResponse response = new UpdateCorporateClientBalanceResponse();

        switch(operation) {
            case 6: {
                // корректировка
                updateCorporateClientCashFlow(mainClient, amount, operation, comment, article);
                   break;
            }
            default: {
                throw new CustomException(4,"Not recognised operation");
            }
        }
          mongoDBServices.createEvent(3, ""+webUser.getId(), 3, 0, "updateCorporateClientBalance", "amount:"+amount, "");
          grayLogService.sendToGrayLog(mainClientId, 0, webUser.getId(), "updateCorporateClientBalance", "Admin", 0, ip, "", "", "");

        return response;
    }





      /*
    operation:
0 - расчет за заказ (деньги нал)
1 - расчет за заказ (деньги карта)
2 - расчет за заказ (бонусы)
3 - приход бонусов (активация промо кода)
4 - корректировка
     */


    @Transactional
    public ClientUpdateBonusesResponse updateClientBonusesARM(long clientId, Long missionId, double amount, int operation, String security_token, String comment, String ip){
        ClientUpdateBonusesResponse response = new ClientUpdateBonusesResponse();
        Long articleId = null;
        WebUser webUser = webUserRepository.findByToken(security_token);
        if (webUser == null) {
            throw new CustomException(1,"Web user not found");
        }
        if (!EnumSet.of(AdministratorRole.ADMIN).contains(webUser.getRole())) {
            throw new CustomException(2,"Permission denied");
        }
              switch(operation) {
                  case 4: {
                    // корректировка
                    operationWithBonusesClient(clientId, missionId, amount, operation, webUser, comment, articleId);
                    break;
                          }
                  default: {
                    throw new CustomException(4,"Not recognised operation");
                       }
                    }
        mongoDBServices.createEvent(3, ""+webUser.getId(), 3, missionId!=null ? missionId : 0, "updateClientBonusesARM", "amount:"+amount, "");
        grayLogService.sendToGrayLog(clientId, 0, webUser.getId(), "updateClientBonusesARM", "Admin", 0, ip, "amount:" + amount, "", "");
          return response;
    }





    public void operationWithClientCashFlow(long clientId, Long missionId, double amount, int operation){
        Client client = clientRepository.findOne(clientId);
        Mission mission = missionRepository.findOne(missionId);
        ClientCashFlow clientCashFlow = new ClientCashFlow(client, mission, operation, (int) (amount * (100)));
        clientCashFlow.setMission(mission);
        clientCashFlow.setDateOperation(DateTimeUtils.nowNovosib_GMT6());
        clientCashFlowRepository.save(clientCashFlow);
    }


    public boolean sorryMoneyIsTransferToClient(Mission mission){
        boolean transfer = false;
         if(clientCashFlowRepository.findByMission(mission) != null){
              transfer = true;
         }
         return transfer;
    }



    // обновляем бонусы клиента и делаем отметку в ClientCashFlow
    public void operationWithBonusesClient(long clientId, Long missionId, double amount, int operation, WebUser webUser, String comment, Long articleId){
         Mission mission = null;
         ArticleAdjustments articleAdjustments = null;
         Money amountOfMoney = Money.of(CurrencyUnit.of("BNS"), amount);
         Client client = clientRepository.findOne(clientId);
         if(missionId!=null){
            mission = missionRepository.findOne(missionId);
         }
         if(articleId!=null){
            articleAdjustments = articleAdjustmentsRepository.findOne(articleId);
         }

         Account clientAccount =  accountRepository.findOne(client.getAccount().getId());
         Money bonuses = clientAccount.getBonuses();
         clientAccount.setBonuses(bonuses.plus(amountOfMoney));
         accountRepository.save(clientAccount);


         ClientCashFlow clientCashFlow = new ClientCashFlow(client, mission, operation, (int) (amount * (100)));
         clientCashFlow.setMission(mission);
         clientCashFlow.setDateOperation(DateTimeUtils.nowNovosib_GMT6());
         clientCashFlow.setArticle(articleAdjustments);
         clientCashFlowRepository.save(clientCashFlow);

         if(operation == 4){
            ClientCorrections clientCorrection = new ClientCorrections();
            clientCorrection.setClientCashFlow(clientCashFlow);
            clientCorrection.setComments(comment);
            clientCorrection.setBonusesBefore(bonuses.getAmount().intValue());
            clientCorrection.setBonusesAfter(bonuses.plus(amountOfMoney).getAmount().intValue());
            clientCorrection.setWebUser(webUser);
            clientCorrectionsRepository.save(clientCorrection);
         }

         // отправляю письмо с деталями
         if (webUser!=null) {
             sendPaymentStat(webUser, amountOfMoney.getAmount().doubleValue(), client, false); // true - driver, false - client
         }
     }







    // операция с балансом водителя
    public void operationWithMoney(Long driverId, Long missionId, double amount, int operation, WebUser webUser, String comment, Long driverPeriodWorkId, Long articleAdjustmentId){
        Mission mission = null;
        DriverPeriodWork driverPeriodWork = null;
        ArticleAdjustments articleAdjustment = null;
        Money amountOfMoney = Money.of(CurrencyUnit.of("RUB"), amount);
        Driver driver = driverRepository.findOne(driverId);
            if (missionId != null) {
              mission = missionRepository.findOne(missionId);
            }

        if(driverPeriodWorkId!=null){
            driverPeriodWork = driverPeriodWorkRepository.findOne(driverPeriodWorkId);
        }
        if(articleAdjustmentId!=null){
            articleAdjustment = articleAdjustmentsRepository.findOne(articleAdjustmentId);
        }

                Account driverAccount = accountRepository.findOne(driver.getAccount().getId()); //driver.getAccount();
                Money money = driverAccount.getMoney();
                driverAccount.setMoney(money.plus(amountOfMoney));
                DriverCashFlow driverCashFlow = new DriverCashFlow(driver, mission, operation, (int) (amount * (100)), driverPeriodWork);

                driverCashFlow.setMission(mission);
                driverCashFlow.setDate_operation(DateTimeUtils.nowNovosib_GMT6());

                cashRepository.save(driverCashFlow);
                accountRepository.save(driverAccount);

                if(operation==7){
                    // 7 - корректировка баланса (АРМ)
                    DriverCorrections driverCorrection = new DriverCorrections();
                    driverCorrection.setDriverCashFlow(driverCashFlow);
                    driverCorrection.setComments(comment);
                    driverCorrection.setBalanceBefore(money.getAmount().intValue());
                    driverCorrection.setBalanceAfter(money.plus(amountOfMoney).getAmount().intValue());
                    driverCorrection.setWebUser(webUser);
                    driverCorrection.setArticleAdjustments(articleAdjustment);
                    driverCorrectionsRepository.save(driverCorrection);
                }
                // отправляю письмо с деталями
                if (webUser != null) {
                    sendPaymentStat(webUser, amount, driver, true);
                }
    }







    public void sendPaymentStat(WebUser webUser, double amount, Object obj, boolean isDriver){
        String to = commonService.getPropertyValue("payment_recipient_mail");
        String subject;
        String driverOrClient;
        int balance;

         if(isDriver){
             Driver driver = (Driver)obj;
             subject = "Водитель: "+driver.getFirstName()+" "+driver.getLastName()+". Сумма: "+amount+". Диспетчер: "+webUser.getFirstName()+" "+webUser.getLastName();
             driverOrClient = "Водитель: "+driver.getFirstName()+" "+driver.getLastName();
             balance = accountRepository.findOne(driver.getAccount().getId()).getMoney().getAmount().intValue();
         }else{
             // client
             Client client = (Client)obj;
             subject = "Клиент: "+client.getFirstName()+" "+client.getLastName()+". Сумма: "+amount+". Диспетчер: "+webUser.getFirstName()+" "+webUser.getLastName();
             driverOrClient = "Клиент: "+client.getFirstName()+" "+client.getLastName();
             balance = accountRepository.findOne(client.getAccount().getId()).getBonuses().getAmount().intValue();
         }

        StringBuilder htmlBody = new StringBuilder();
          htmlBody.append("<html><body>");
          htmlBody.append("<ul>");
             htmlBody.append("<li><font color=\"#0000FF\"><span style=\"color: #333333; font-family: Arial, sans-serif; font-size: 14px; line-height: 20px; background-color: #ffffff;\">Дата корректировки: "+DateTimeUtils.nowNovosib_GMT6()+"</span></font></li>");
             htmlBody.append("<li><font color=\"#0000FF\"><span style=\"color: #333333; font-family: Arial, sans-serif; font-size: 14px; line-height: 20px; background-color: #ffffff;\"> Диспетчер: "+webUser.getFirstName()+" "+webUser.getLastName()+"</span></font></li>");
             htmlBody.append("<li><span style=\"color: #333333; font-family: Arial, sans-serif; font-size: 14px; line-height: 20px; background-color: #ffffff;\">"+driverOrClient+"</span></li>");
             htmlBody.append("<li><span style=\"color: #333333; font-family: Arial, sans-serif; font-size: 14px; line-height: 20px; background-color: #ffffff;\">Сума корректировки: "+amount+"</span></li>");
             htmlBody.append("<li><span style=\"color: #333333; font-family: Arial, sans-serif; font-size: 14px; line-height: 20px; background-color: #ffffff;\">Баланс: "+balance+" руб.</span></li>");
          htmlBody.append("</ul>");
          htmlBody.append("</body></html>");

        SendEmailUtil.sendEmail(to, htmlBody.toString(), subject);
    }





    public GeneratePromoCodesResponse generateExclusivePromoCodes(int countPromoCode, int countSymbols, int availableUsedCount, int lifetimeDaysAfterActivation){
        GeneratePromoCodesResponse response = new GeneratePromoCodesResponse();
        PromoCodeExclusive promoCodes;
        for(int i=0; i<countPromoCode; i++) {
            promoCodes = new PromoCodeExclusive();
            promoCodes.setAvailableUsedCount(availableUsedCount);
            promoCodes.setPromoCode(StrUtils.generateAlphaNumString(countSymbols));
            promoCodes.setUsedCount(0);
            promoCodes.setLifetimeDaysAfterActivation(lifetimeDaysAfterActivation);
            promoCodeExclusiveRepository.save(promoCodes);
            response.setGenerate(true);
        }
        return response;
    }



    public GeneratePromoCodesResponse generatePromoCodes(int countPromoCode, int countSymbols, int amount, int availableUsedCount, String channel){
        GeneratePromoCodesResponse response = new GeneratePromoCodesResponse();

        String propFlayer = commonService.getPropertyValue("expiration_date_promo_code_flayer");
        int propFlayerDayInt = Integer.parseInt(propFlayer);

        String availableCountUse = commonService.getPropertyValue("send_count_promo_code_of_day");
        int availableCountUseInt = Integer.parseInt(availableCountUse);

        for(int i=0; i<countPromoCode; i++) {
            PromoCodes promoCodes = new PromoCodes();

            if (channel.equals("flyers")) {
                // промокоды для флаеров
                promoCodes.setFromId(10L); // flayers user default
                promoCodes.setChannel("flayers");
                DateTime dtOrg = new DateTime(DateTimeUtils.nowNovosib_GMT6());

                promoCodes.setDateOfIssue(DateTimeUtils.nowNovosib_GMT6().getMillis());// дата выдачи
                DateTime expirationDate = dtOrg.withTimeAtStartOfDay().plusDays(propFlayerDayInt);
                promoCodes.setExpirationDate(expirationDate.getMillis());
            }else if (channel.equals("app")){
                // промокоды для приложения
                promoCodes.setChannel("app");
            }
            promoCodes.setAvailableUsedCount(availableUsedCount);
            promoCodes.setPromoCode(StrUtils.generateAlphaNumString(countSymbols));
            promoCodes.setAmount(amount);
            promoCodes.setUsedCount(0);
            promoCodeRepository.save(promoCodes);
            response.setGenerate(true);
        }
           return response;
    }



    public ClearCurrentMissionResponse clearDriverCurrentMission(long driverId){
        ClearCurrentMissionResponse response = new ClearCurrentMissionResponse();
             Driver driver = driverRepository.findOne(driverId);
                if(driver!=null){
                    if(driver.getCurrentMission().getState().equals(Mission.State.CANCELED)||driver.getCurrentMission().getState().equals(Mission.State.COMPLETED)){
                     driver.setCurrentMission(null);
                     driverRepository.save(driver);
                     response.setClear(true);
                   }
                }
                return response;
    }




    public ClearCurrentMissionResponse clearClientCurrentMission(long clientId){
        ClearCurrentMissionResponse response = new ClearCurrentMissionResponse();
        Client client = clientRepository.findOne(clientId);
        if(client!=null){
               if(client.getMission().getState().equals(Mission.State.CANCELED)||client.getMission().getState().equals(Mission.State.COMPLETED)){
                   client.setMission(null);
                   clientRepository.save(client);
                     response.setClear(true);
               }
        }
            return response;
    }




    public MissionTransferResponse missionTransfer(long missionId, long driverIdTo, String security_token, String ip) throws JSONException {
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser == null) {
           throw new CustomException(1, "Web user not found");
        }
        MissionTransferResponse response =new MissionTransferResponse();
        Mission mission = missionRepository.findOne(missionId);
        Driver driverTo = driverRepository.findOne(driverIdTo);

           if(mission==null){
               throw new CustomException(2,"Миссия не найдена");
           }
              Driver driverFrom =  mission.getDriverInfo();

              boolean booked = mission.isBooked(); // getState().equals(Mission.State.BOOKED);

                     if(driverTo==null) {
                         throw new CustomException(4,"Не указан водитель для которого передается заказ");
                     }
                     if(driverTo.getAdministrativeStatus().equals(Driver.AdministrativeStatus.BLOCKED)){
                         throw new CustomException(5, "Водитель заблокирован");
                     }
                     if(driverTo.getState().equals(Driver.State.OFFLINE)){
                         throw new CustomException(6, "Водитель OFFLINE");
                     }
                     if(driverTo.getState().equals(Driver.State.BUSY)){
                         throw new CustomException(7, "Водитель занят");
                     }
                     if(driverTo.getCurrentMission() != null){
                         throw new CustomException(8, "У водителя на которого вы пытаетесь передать заказ есть незавершенная миссия");
                     }
                     // очищаем миссию у веодителя от которого передается миссия
                     if(mission.getState().equals(Mission.State.CANCELED) || mission.getState().equals(Mission.State.COMPLETED) || mission.getState().equals(Mission.State.ARRIVED)){
                        throw new CustomException(9,"Миссия со статусом: "+mission.getState().toString()+" не соответсвует операции передачи другому водителю");
                     }

                     if(booked){
                         mission.setBookingState(Mission.BookingState.DRIVER_ASSIGNED);
                         mission.setBookedDriverId(driverTo);
                     }else{
                         mission.setState(Mission.State.ASSIGNED);
                         mission.setLateDriverBookedMin(commonService.calculateArrivalTime(mission, null, driverTo));  // время добавить как параметр через арм
                         missionRepository.save(mission);
                         driverTo.setCurrentMission(mission);
                         driverRepository.save(driverTo);
                     }
                         mission.setTimeOfAssigning(DateTimeUtils.nowNovosib_GMT6());
                         mission.setDriverInfo(driverTo);
                         mission.setTaxopark(driverTo.getTaxoparkPartners());
                         missionRepository.save(mission);

                     long driverFromId = (driverFrom != null ? driverFrom.getId() : 0);

                     if(driverFrom != null && (driverFrom.getCurrentMission()!=null && driverFrom.getCurrentMission().getId() == missionId)){
                         DriverLocation driverFromLocation = locationRepository.findByDriverId(driverFrom.getId());
                         driverFromLocation.setMission(null);
                         locationRepository.save(driverFromLocation);
                         driverFrom.setCurrentMission(null);
                         driverRepository.save(driverFrom);

                         /* send sms to driver from */
                         serviceSMSNotification.missionTransfer(driverFrom.getPhone(), "");
                     }
                         /*
                         DriverLocation driverToLocation = locationRepository.findByDriverId(driverTo.getId());
                         driverToLocation.setMission(mission);
                         locationRepository.save(driverToLocation);
                         driverTo.setCurrentMission(mission);
                         driverRepository.save(driverTo);
                         */

                     if(!booked){
                         QueueMission missionFromQueue = queueMissionRepository.findByMission(mission);
                         if(missionFromQueue == null){
                             missionFromQueue = new QueueMission();
                         }
                             missionFromQueue.setDriver(driverTo);
                             missionFromQueue.setTimeOfAssigning(DateTimeUtils.nowNovosib_GMT6());
                             missionFromQueue.setMission(mission);
                             queueMissionRepository.save(missionFromQueue);

                             // nodeJsNotificationsService.transferMissionToDriver(missionId, driverFromId, driverTo.getId());
                             // nodeJsNotificationsService.driverChange(mission);
                     } else {
                         // booked
                         if(driverFrom != null){
                             nodeJsNotificationsService.driverCustomMessage(driverFromId, "Ваша бронь передана другому водителю");
                         }
                             nodeJsNotificationsService.driverCustomMessage(driverTo.getId(), String.format("Вам передали бронь с  %s  на  %s", mission.getLocationFrom().getAddress(), DateTimeUtils.stringDateTimeByPattern(mission.getTimeOfStarting(), "dd.MM 'в' HH:mm")));
                     }

                         nodeJsNotificationsService.transferMissionToDriver(missionId, driverFromId, driverTo.getId(), mission.getBookingState().name(), booked);
                         /* driver_changed - не самодостаточное событие, оно генерится нодом */
                         //nodeJsNotificationsService.driverChange(mission);

                         nodeJsNotificationsService.driverRefresh(driverIdTo, webUser.getId());
                         nodeJsNotificationsService.driverRefresh(driverFromId, webUser.getId());

                         /* logging */
                         mongoDBServices.createEvent(3, "" + webUser.getId(), 3, missionId, "missionTransfer", "driverIdFrom:" + driverFromId, "driverIdTo:" + driverTo.getId());
                         grayLogService.sendToGrayLog(mission.getClientInfo().getId(), driverFromId, webUser.getId(), "missionTransfer", "Admin", missionId, ip, "driverIdFrom:"+driverFromId, "driverIdTo:"+driverTo.getId(), "");
        return response;
    }





    public InsertPartnersGroupResponse insertPartnersGroup(PartnersGroupInfo partnersGroupInfo, String security_token){
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser==null){
            throw new CustomException(1,"Web user not found");
        }

        InsertPartnersGroupResponse response = new InsertPartnersGroupResponse();
        PartnersGroup partnersGroup = ModelsUtils.fromModel(partnersGroupInfo);
        partnersGroupRepository.save(partnersGroup);
        response.setPartnersGroupInfo(ModelsUtils.toModel(partnersGroup));

        //mongoDBServices.createEvent(3, "" + webUser.getId(), 3, "insertPartnersGroup", "", "", 0, 0);
              return response;
    }



    public UpdatePartnersGroupResponse updatePartnersGroup(PartnersGroupInfo partnersGroupInfo){
        UpdatePartnersGroupResponse response = new UpdatePartnersGroupResponse();
        PartnersGroup partnersGroup = partnersGroupRepository.findOne(partnersGroupInfo.getIdGroup());
           if(partnersGroup!=null){
               partnersGroup = ModelsUtils.fromModel(partnersGroupInfo, partnersGroup);
               partnersGroupRepository.save(partnersGroup);
               response.setPartnersGroupInfo(ModelsUtils.toModel(partnersGroup));
           }
        return response;
    }



    public InsertItemPartnersGroupResponse insertItemPartnersGroup(ItemPartnersGroupInfo itemPartnersGroupInfo){
        InsertItemPartnersGroupResponse response = new InsertItemPartnersGroupResponse();
             if(itemPartnersGroupInfo.getGroupId()!=null){
                 PartnersGroup partnersGroup = partnersGroupRepository.findOne(itemPartnersGroupInfo.getGroupId());
                 if(partnersGroup != null){
                     ItemPartnersGroup itemPartnersGroup =  ModelsUtils.fromModel(itemPartnersGroupInfo, partnersGroup);
                     itemPartnersGroupRepository.save(itemPartnersGroup);
                     response.setSuccess(true);
                 }
             }

           return response;
    }



    public UpdateItemPartnersGroupResponse updateItemPartnersGroup(ItemPartnersGroupInfo itemPartnersGroupInfo, String security_token){
        UpdateItemPartnersGroupResponse response = new UpdateItemPartnersGroupResponse();
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser == null){
            throw new CustomException(1,"Web user not found");
        }
                if(itemPartnersGroupInfo.getItemId()!=null){
                    ItemPartnersGroup itemPartnersGroup =  itemPartnersGroupRepository.findOne(itemPartnersGroupInfo.getItemId());
                    if(itemPartnersGroup != null){
                         if(itemPartnersGroupInfo.getGroupId()!= null){
                             PartnersGroup partnersGroup = partnersGroupRepository.findOne(itemPartnersGroupInfo.getGroupId());
                             if(partnersGroup != null){
                                 itemPartnersGroup =  ModelsUtils.fromModel(itemPartnersGroupInfo, partnersGroup);
                                 itemPartnersGroupRepository.save(itemPartnersGroup);
                                 response.setSuccess(true);
                             }
                         }

                    }
                }

        return response;
    }



    public FindGroupBySectionARMResponse findGroupBySectionARM(String section, String security_token){
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser == null){
            throw new CustomException(1,"Web user not found");
        }
        FindGroupBySectionARMResponse response =new FindGroupBySectionARMResponse();
        List<PartnersGroup> partnersGroupList = partnersGroupRepository.findBySection(section);
        if(partnersGroupList!=null){
            for(PartnersGroup partnersGroup :partnersGroupList){
                PartnersGroupInfo partnersGroupInfo =  ModelsUtils.toModel(partnersGroup);
                response.getPartnersGroupInfoList().add(partnersGroupInfo);
            }
        }
        return response;
    }




    // клиентский вызов
    public FindGroupBySectionResponse findGroupBySection(String section){
        FindGroupBySectionResponse response =new FindGroupBySectionResponse();
               List<PartnersGroup> partnersGroupList = partnersGroupRepository.findBySection(section);
                  if(partnersGroupList!=null){
                         for(PartnersGroup partnersGroup :partnersGroupList){
                             PartnersGroupInfo partnersGroupInfo =  ModelsUtils.toModel(partnersGroup);
                             response.getPartnersGroupInfoList().add(partnersGroupInfo);
                         }
                  }
           return response;
    }










    public MissionStateStatisticResponse getMissionStateStatistic(String security_token, long missionId, String state, long startTime, long endTime, int sizePage, int numberPage) {
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser==null){
            throw new CustomException(1,"Web user not found");
        }
        MissionStateStatisticResponse response = new MissionStateStatisticResponse();
        Session session = entityManager.unwrap(Session.class);

        Criteria criteria = session.createCriteria(MissionStateStatistic.class);
        //Criteria count = session.createCriteria(MissionStateStatistic.class);

        if(missionId!=0){
            criteria.add(Restrictions.eq("mission.id", missionId));
            //count.add(Restrictions.eq("mission.id", missionId));
        }

        if(!StringUtils.isEmpty(state)){
            criteria.add(Restrictions.eq("state", state));
            //count.add(Restrictions.eq("state", state));
        }

        criteria = QueryUtils.fillDateTimeParameter(criteria, "dateTime", startTime, endTime);
        /*
        if(startTime!=0 || endTime!=0){
            criteria.add(Restrictions.ge("dateTime", new DateTime(new Date(startTime * 1000))));
            criteria.add(Restrictions.lt("dateTime", new DateTime(new Date(endTime * 1000))));

            //count.add(Restrictions.ge("dateTime", new DateTime(new Date(startTime * 1000))));
            //count.add(Restrictions.lt("dateTime", new DateTime(new Date(endTime * 1000))));
        }
        */


        criteria.addOrder(Order.desc("dateTime"));

        criteria.setProjection(Projections.rowCount());
        Long total = (Long)criteria.uniqueResult();
        criteria.setProjection(null);

        response.setTotalItems(total);

        long lastPageNumber = ((total / sizePage) + 1);
        response.setLastPageNumber(lastPageNumber);

        criteria.setFirstResult((numberPage - 1) * sizePage);
        criteria.setMaxResults(sizePage);

        List<MissionStateStatistic> listMissionStateStat = criteria.list();

        for(MissionStateStatistic missionStateStatistic:listMissionStateStat){
            response.getMissionStateStatisticInfos().add(ModelsUtils.toModelMissionStateStatisticInfo(missionStateStatistic));
        }

          return response;
    }








    /*
        Результат: логин, водитель (ФИО), отработано часов/смен, выполненные заказы (кол-во), выполненные заказы (сумма), бесплатный отдых, платный отдых, ЗП.
    */
    public OwnDriverStatsResponse getOwnDriverStats(String security_token, long driverId, long assistantId,long startTime, long endTime, Long taxoparkId){
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser==null){
            throw new CustomException(1,"Web user not found");
        }
        OwnDriverStatsResponse response = new OwnDriverStatsResponse();
        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(DriverPeriodWork.class);
        criteria.createAlias("driver", "drv");
        if(driverId!=0){
            criteria.add(Restrictions.eq("drv.id", driverId));
        }
        if(assistantId!=0){
            Criterion pMask = Restrictions.eq("drv.assistant.id", assistantId);
            criteria.add(pMask);
        }
        criteria = QueryUtils.fillDateTimeParameter(criteria, "startWork", startTime, endTime);
        /*
        if(startTime!=0 || endTime!=0){
            criteria.add(Restrictions.ge("startWork", new DateTime(new Date(startTime * 1000))));
            criteria.add(Restrictions.lt("startWork", new DateTime(new Date(endTime * 1000))));
        }
        */
        if(taxoparkId!=null){
            criteria.add(Restrictions.eq("drv.taxoparkPartners.id", taxoparkId));
        }

        criteria.add(Restrictions.isNotNull("timeWorkInFactOfStarting"));
        criteria.addOrder(Order.desc("startWork"));

        List<DriverPeriodWork> listDriverPeriodWork = criteria.list();

        HashMap hash = new HashMap();
        int countDrivers = 0;
        int sumSalary = 0;

        int generalCountWorkflow = 0;
        int generalFreeRest = 0;
        int generalPayRest = 0;

        for(DriverPeriodWork driverPeriodWork: listDriverPeriodWork){
          DriverRequisite requisite =  driverPeriodWork.getDriverRequisite(); // driverRequisiteRepository.findByDriverAndActive(driverPeriodWork.getDriver(), true);
          int salary = requisite!=null?requisite.getSalaryPerDay():0;

          OwnDriverStatsInfo info = new OwnDriverStatsInfo();
            int countComplete = countCompleteMissionByDriverAndPeriod(driverPeriodWork.getDriver().getId(), driverPeriodWork.getStartWork(), driverPeriodWork.getEndWork());
            int sumComplete = (int)sumCompleteMissionByDriverAndPeriod(driverPeriodWork.getDriver().getId(), driverPeriodWork.getStartWork(), driverPeriodWork.getEndWork());
            info.setCountCompletedMission(countComplete);
            info.setSumCompletedMission(sumComplete);

            info.setPayRest(DateTimeUtils.splitToComponentTimes(new BigDecimal(driverPeriodWork.getTimeSecPayRest())));
            info.setCountWorkflow(DateTimeUtils.splitToComponentTimes(new BigDecimal(driverPeriodWork.getTimeSecWork())));
            info.setFreeRest(DateTimeUtils.splitToComponentTimes(new BigDecimal(driverPeriodWork.getTimeSecRest())));
            info.setLogin(driverPeriodWork.getDriver().getLogin());
            info.setName(driverPeriodWork.getDriver().getFirstName()+" "+driverPeriodWork.getDriver().getLastName());
            info.setDriverId(driverPeriodWork.getDriver().getId());
            info.setTimeStartWork(driverPeriodWork.getStartWork().getMillis());
            info.setSalary(salary);

            response.getGeneralStatsInfo().setGeneralCountCompletedMission(response.getGeneralStatsInfo().getGeneralCountCompletedMission()+countComplete);
            response.getGeneralStatsInfo().setGeneralSumCompletedMission(response.getGeneralStatsInfo().getGeneralSumCompletedMission()+sumComplete);

            generalCountWorkflow += driverPeriodWork.getTimeSecWork();
            generalFreeRest += driverPeriodWork.getTimeSecRest();
            generalPayRest += driverPeriodWork.getTimeSecPayRest();

            response.getGeneralStatsInfo().setGeneralSalary(response.getGeneralStatsInfo().getGeneralSalary()+salary);

            if(!hash.containsKey(driverPeriodWork.getDriver().getId())){
                //int salary = driverRequisiteRepository.findByDriverAndActive(driverPeriodWork.getDriver(), true).getSalaryPerDay();
                hash.put(driverPeriodWork.getDriver().getId(), null);
                sumSalary+=salary;
                info.setSalary(salary);
                countDrivers++;
            }
            response.getOwnDriverStatsInfos().add(info);
        }

        response.getGeneralStatsInfo().setGeneralCountWorkflow(DateTimeUtils.splitToComponentTimes(new BigDecimal(generalCountWorkflow)));
        response.getGeneralStatsInfo().setGeneralFreeRest(DateTimeUtils.splitToComponentTimes(new BigDecimal(generalFreeRest)));
        response.getGeneralStatsInfo().setGeneralPayRest(DateTimeUtils.splitToComponentTimes(new BigDecimal(generalPayRest)));

          response.getGeneralStatsInfo().setCountDrivers(countDrivers);
          return response;
    }



    public int countCompleteMissionByDriverAndPeriod(long driverId, DateTime startTime, DateTime endTime){
        Session session = entityManager.unwrap(Session.class);
        Query q1 = session.createSQLQuery("select count(*) from mission where driverInfo_id = :id and state=:state and UNIX_TIMESTAMP(time_finishing)+21600 BETWEEN :d1 and :d2 and test_order=0");
        long s = startTime.getMillis()/1000;
        long e = endTime.getMillis()/1000;
        q1.setParameter("id", driverId);
        q1.setParameter("state","COMPLETED");
        q1.setParameter("d1", s);
        q1.setParameter("d2", e);
        BigInteger resultAllCount = (BigInteger)q1.uniqueResult();
          return resultAllCount!=null?resultAllCount.intValue():0;
    }




    public double sumCompleteMissionByDriverAndPeriod(long driverId, DateTime startTime, DateTime endTime){
        Session session = entityManager.unwrap(Session.class);
        Query q1 = session.createSQLQuery("select sum(price_in_fact_amount) from mission where driverInfo_id = :id and state=:state and UNIX_TIMESTAMP(time_finishing)+21600 BETWEEN :d1 and :d2 and test_order=0");
        q1.setParameter("id", driverId);
        q1.setParameter("state", "COMPLETED");
        q1.setParameter("d1", startTime.getMillis() / 1000);
        q1.setParameter("d2", endTime.getMillis() / 1000);
        BigDecimal resultAllCount = (BigDecimal)q1.uniqueResult();
        return  resultAllCount!=null?resultAllCount.doubleValue():0;
    }













    /*
    "select count(*) from mission where clientInfo_id = :id and state='COMPLETED'"+
                        " union "+
                "select count(*)/count(distinct date_format(time_requesting,'%Y-%m')) from mission where clientInfo_id = :id and state='COMPLETED'"+
                        " union "+
                "SELECT SUM(price_in_fact_amount) from mission where clientInfo_id = :id and state='COMPLETED'"+
                        " union "+
                "select sum(price_in_fact_amount)/count(*) from mission where clientInfo_id = :id and state='COMPLETED'"+
                        " union "+
                "SELECT count(*) FROM mission_canceled c\n" +
                        "    inner join mission m on m.id=c.mission_id\n" +
                        "    and m.clientInfo_id = :id and m.state='CANCELED' and c.cancel_by= 'client' and m.driverInfo_id is null" +
                        " union "+
                "SELECT count(*) FROM mission_canceled c\n" +
                        "    inner join mission m on m.id=c.mission_id\n" +
                        "    and m.clientInfo_id = :id and m.state='CANCELED' and c.cancel_by= 'client' and m.driverInfo_id is not null"+
                        " union "+
                "SELECT count(*) FROM mission where clientInfo_id= :id and auto_class= 'LOW_COSTER' and state='COMPLETED'"+
                        " union "+
                "SELECT count(*) FROM mission where clientInfo_id= :id and auto_class= 'STANDARD' and state='COMPLETED'"+
                        " union "+
                "SELECT count(*) FROM mission where clientInfo_id= :id and auto_class= 'COMFORT' and state='COMPLETED'"+
                        " union "+
                "SELECT count(*) FROM mission where clientInfo_id= :id and auto_class= 'BUSINESS' and state='COMPLETED'"+
                        " union "+
                "SELECT count(*) FROM estimate where general!=0 and client_id= :id"+
                        " union "+
                "SELECT count(*) FROM estimate where estimate_comment!='' and client_id= :id"+
                        " union "+
                "SELECT avg(general) FROM estimate where general!=0 and client_id=:id"+
                        " union "+
                "SELECT count(*) FROM promo_codes where from_id= :id"+
                        " union "+
                "SELECT count(*) FROM promo_codes where to_id= :id"+
                        " union "+
                "SELECT count(*) FROM mission_state_statistic s \n" +
                        "inner join mission m on m.id=s.mission_id\n" +
                        "where s.state='AUTO_SEARCH' and m.clientInfo_id= :id"+
                        " union "+
                "select s.mission_id from services_expected s\n" +
                        "        inner join mission m on m.id=s.mission_id\n" +
                        "        where m.state = 'COMPLETED' and m.clientInfo_id =:id group by m.id"+
                        " union "+
                "SELECT Sum(c) FROM (SELECT id, COUNT(*) AS c\n" +
                        "        from mission where from_address is not null and state='COMPLETED' and clientInfo_id=:id group by from_address\n" +
                        "        having count(*)>1) as b"+
                        " union "+
                "SELECT Sum(c) FROM (SELECT COUNT(*) AS c from mission where to_address is not null and state='COMPLETED' and clientInfo_id=:id group by to_address having count(*)>1) as b"+
                        " union "+
                "select count(*) from mission m where m.from_address=m.to_address and m.clientInfo_id= :id and m.state='COMPLETED'"+
                        " union "+
                "SELECT count(*) FROM mission_state_statistic s \n" +
                        "inner join mission m on m.id=s.mission_id\n" +
                        "where s.state='WATCH_MISSION' and m.clientInfo_id= :id"+
                "");
     */

    private void statClient(Client client){
        Session session = entityManager.unwrap(Session.class);
        /* 1 - countCancelByClientWithoutDriver
           2 - countCancelByClientWithDriver
         */
        Query q = session.createSQLQuery(
                "select count(*) from mission where clientInfo_id = :id and state='COMPLETED'" +
                        " union " +
                        "select count(*)/count(distinct date_format(time_finishing,'%Y-%m')) from mission where clientInfo_id = :id and state='COMPLETED' and test_order=0" +
                        " union " +
                        "SELECT SUM(price_in_fact_amount) from mission where clientInfo_id = :id and state='COMPLETED'" +
                        " union " +
                        "select sum(price_in_fact_amount)/count(*) from mission where clientInfo_id = :id and state='COMPLETED'" +
                        " union " +
                        "SELECT count(*) FROM mission_canceled c\n" +
                        "    inner join mission m on (m.id=c.mission_id and m.clientInfo_id = :id and m.state='CANCELED' and c.cancel_by= 'client' and m.driverInfo_id is null)" +
                        " union " +
                        "SELECT count(*) FROM mission_canceled c\n" +
                        "    inner join mission m on (m.id=c.mission_id and m.clientInfo_id = :id and m.state='CANCELED' and c.cancel_by= 'client' and m.driverInfo_id is not null)" +
                        " union " +
                        "SELECT count(*) FROM mission where clientInfo_id= :id and auto_class= 'LOW_COSTER' and state='COMPLETED'" +
                        " union " +
                        "SELECT count(*) FROM mission where clientInfo_id= :id and auto_class= 'STANDARD' and state='COMPLETED'" +
                        " union " +
                        "SELECT count(*) FROM mission where clientInfo_id= :id and auto_class= 'COMFORT' and state='COMPLETED'" +
                        " union " +
                        "SELECT count(*) FROM mission where clientInfo_id= :id and auto_class= 'BUSINESS' and state='COMPLETED'" +
                        " union " +
                        "SELECT count(*) FROM estimate where general!=0 and client_id= :id" +
                        " union " +
                        "SELECT count(*) FROM estimate where estimate_comment!='' and client_id= :id" +
                        " union " +
                        "SELECT avg(general) FROM estimate where general!=0 and client_id=:id" +
                        " union " +
                        "SELECT count(*) FROM promo_codes where from_id= :id" +
                        " union " +
                        "SELECT count(*) FROM promo_codes where to_id= :id" +
                        " union " +
                        "SELECT count(*) FROM mission_state_statistic s \n" +
                        "inner join mission m on (m.id=s.mission_id and s.state='AUTO_SEARCH' and m.clientInfo_id= :id)" +
                        " union " +
                        "select s.mission_id from services_expected s\n" +
                        "        inner join mission m on (m.id=s.mission_id and m.state = 'COMPLETED' and m.clientInfo_id =:id) group by m.id" +
                        " union " +
                        "SELECT Sum(c) FROM (SELECT id, COUNT(*) AS c\n" +
                        "        from mission where from_address is not null and state='COMPLETED' and clientInfo_id=:id group by from_address\n" +
                        "        having count(*)>1) as b" +
                        " union " +
                        "SELECT Sum(c) FROM (SELECT COUNT(*) AS c from mission where to_address is not null and state='COMPLETED' and clientInfo_id=:id group by to_address having count(*)>1) as b" +
                        " union " +
                        "select count(*) from mission m where m.from_address=m.to_address and m.clientInfo_id= :id and m.state='COMPLETED'" +
                        " union " +
                        "SELECT count(*) FROM mission_state_statistic s \n" +
                        "inner join mission m on (m.id=s.mission_id and s.state='WATCH_MISSION' and m.clientInfo_id= :id)" +

                        "");
        q.setParameter("id", client.getId());
        List result = q.list();
        LOGGER.info("result = " + result);
    }




















    private void lockSmsSendByClient(Client client, String reason, DateTime autoUnlockTime){
        if(sendSmsLockRepository.findByTimeOfUnlockIsNullAndClient(client)!=null) {
            throw new CustomException(1,"Already is lock");
        }
        SendSmsLock smsSendByClient = new SendSmsLock();
        smsSendByClient.setClient(client);
        smsSendByClient.setTimeOfLock(DateTimeUtils.nowNovosib_GMT6());
        smsSendByClient.setAutoUnlockTime(autoUnlockTime);
        smsSendByClient.setReason(reason);
        sendSmsLockRepository.save(smsSendByClient);

        SendEmailUtil.sendEmail(client.getEmail(), reason, "Taxisto: внимание!");
    }






    public boolean allowedToSendSms(long clientId){
        boolean result=false;
        DateTime now = DateTimeUtils.nowNovosib_GMT6();
        DateTime startTime = now.minusMinutes(1);

        Client client = clientRepository.findOne(clientId);

        int limitByMinute = Integer.parseInt(commonService.getPropertyValue("limit_sms_minute"));
        int limitByHour = Integer.parseInt(commonService.getPropertyValue("limit_sms_hour"));
        int limitByDay = Integer.parseInt(commonService.getPropertyValue("limit_sms_day"));

        int autoUnlockOn = Integer.parseInt(commonService.getPropertyValue("auto_unlock")); // если 1, значит включена автоматическая разбокировка через время заданное на сервере, если 0, тогда разблокировка ручная
        int autoUnlockAfterMin = Integer.parseInt(commonService.getPropertyValue("auto_unlock_after"));
        DateTime autoUnlockTime = null;

        if(autoUnlockOn==1){
            autoUnlockTime = now.plusMinutes(autoUnlockAfterMin);
        }

        List<SMSMessage> smsMessageList = smsMessageRepository.findByTimeOfCreateBetweenAndClient(startTime, now, client);
           if(smsMessageList.size()<limitByMinute){
               startTime = now.minusHours(1);
               smsMessageList = smsMessageRepository.findByTimeOfCreateBetweenAndClient(startTime, now, client);
               if(smsMessageList.size()<limitByHour){
                   startTime = now.minusDays(1);
                   smsMessageList = smsMessageRepository.findByTimeOfCreateBetweenAndClient(startTime, now, client);
                   if(smsMessageList.size()<limitByDay){
                        // разрешаем отрпвлять смс
                           result=true;
                   }else{
                       LOGGER.info("Превышен предельно допустимый лимит отправки смс в день");
                       lockSmsSendByClient(client, "Вы превысили предельно допустимый лимит отправки смс в день",  autoUnlockTime);
                   }
               }else{
                   LOGGER.info("Превышен предельно допустимый лимит отправки смс в час");
                   lockSmsSendByClient(client, "Вы превысили предельно допустимый лимит отправки смс в час",  autoUnlockTime);
               }
           }else{
               // баним номер клиента
               LOGGER.info("Превышен предельно допустимый лимит отправки смс в минуту");
               lockSmsSendByClient(client, "Вы превысили предельно допустимый лимит отправки смс в минуту",  autoUnlockTime); //DateTime autoUnlockTime
           }
        return result;
    }





    // ручное назначение водителя на бронь
    public void assigningDriverToBooking(String security_token, long driverId, long missionId) throws JSONException {
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser == null){
            throw new CustomException(1,"Web user not found");
        }
        if(!EnumSet.of(AdministratorRole.DISPATCHER, AdministratorRole.ADMIN).contains(webUser.getRole())){
            throw new CustomException(2,"Permission denied");
        }

        Mission mission = missionRepository.findOne(missionId);

        if(mission == null){
            throw new CustomException(4,"Миссия не найдена");
        }
        if(mission.getBookedDriverId()!=null && mission.getDriverInfo()!=null){
            throw new CustomException(5,"На данный заказ уже назначен водитель");
        }


        if(!mission.isBooked() && !mission.getBookingState().equals(Mission.BookingState.WAITING)){
            throw new CustomException(6,"Миссия не является бронью или на нее уже назначен водитель");
        }

        Driver driverTo = driverRepository.findOne(driverId);
        if(driverTo == null){
            throw new CustomException(7,"Driver not found ");
        }

        mission.setBookingState(Mission.BookingState.DRIVER_ASSIGNED);
        mission.setBookedDriverId(driverTo);
        mission.setDriverInfo(driverTo);
        mission.setCountNotified(0);
        mission.setTimeOfAssigning(DateTimeUtils.nowNovosib_GMT6());
        mission.setTaxopark(driverTo.getTaxoparkPartners());
        missionRepository.save(mission);

        /* 1. отправить на нод событие для водителя, которому мы назначили бронь. взять у Сергея*/
        JSONObject json = new JSONObject();
            json.put("missionId", mission.getId());
            json.put("from_driverId", 0);
            json.put("to_driverId", driverTo.getId());

        nodeJsService.notified("transfer_mission_to_driver", json);

        JSONObject jsonClient = new JSONObject();
        jsonClient.put("missionId", mission.getId());

        nodeJsService.notified("mission_booked_driver_assigned", jsonClient);

        /* 2. отправить клиенту смс о том, что водитель назначен на бронь*/
        serviceSMSNotification.driverBookedAssigned(mission.getClientInfo());

        /* log */
        mongoDBServices.createEvent(3, ""+webUser.getId(), 3, missionId, "assigningDriverToBooking", "driverIdTo:"+driverTo.getId(), "");
        //mongoDBServices.createEvent(0, "" + webUser.getId(), 3, "assigningDriverToBooking", "", "", mission.getClientInfo().getId(), driverTo.getId());
    }



    // выслать временный пароль
    public TemporaryPasswordResponse temporaryPassword(long clientId, String phone){
        TemporaryPasswordResponse response = new TemporaryPasswordResponse();
        try {
            Client client = null;
            if (clientId != 0) {
                client = clientRepository.findOne(clientId);
                phone = client.getPhone();
            }else if(phone != null && !phone.isEmpty()){
                client = clientRepository.findByPhone(phone);
            }

            if (client != null) {
                String smsCode = generateTempPassword();
                serviceSMSNotification.sendCustomSMS(phone, "Ваш новый пароль: "+smsCode, "");
                //serviceSMSNotification.temporaryPassword(phone, smsCode, "");
                client.setPassword(smsCode);
                clientRepository.save(client);
            }else{
                response.getErrorCodeHelper().setErrorCode(4);
                response.getErrorCodeHelper().setErrorMessage("Client not found");
            }
        }catch(Exception e){
            e.printStackTrace();
            response.getErrorCodeHelper().setErrorCode(5);
            response.getErrorCodeHelper().setErrorMessage("temporaryPassword exception: "+e.getMessage());
        }
        return response;
    }





    private String generateTempPasswordForCorporateClient() throws Exception{
       return StrUtils.generateNumString(6);
    }

    private String generateTempPassword() throws  Exception{
       return StrUtils.generateSmallNumString(6);
    }




    public BonusSumAmountResponse checkBonus(){
          BonusSumAmountResponse response = new BonusSumAmountResponse();
          try {
              int bonus_count_order = Integer.parseInt(commonService.getPropertyValue("bonus_count_order"));
              int bonus_sum_amount = Integer.parseInt(commonService.getPropertyValue("bonus_sum_amount"));

              DateTime s = DateTimeUtils.nowNovosib_GMT6();
              DateTime e = DateTimeUtils.nowNovosib_GMT6();
              DateTime d = e.minusDays(1); // вчера

              LOGGER.info("checkBonus start = " + d.withTimeAtStartOfDay());
              LOGGER.info("checkBonus end = " + s.withTimeAtStartOfDay());

              Iterable<Driver> iterable = driverRepository.findAll();

              for (Driver driver : iterable) {
                  int countCompletedOrder = missionRepository.countCompletedMissionByDriver(driver.getId(), Mission.State.COMPLETED ,d.withTimeAtStartOfDay(), s.withTimeAtStartOfDay());
                  if (countCompletedOrder>= bonus_count_order) {
                      // начисляем бабло за выполненные заказы
                      updateDriverBalanceSystem(driver.getId(), 0L, bonus_sum_amount, 9);
                      response.getDriverIdList().add(driver.getId());
                  }
              }
          }catch(Exception e){
               e.printStackTrace();
               response.getErrorCodeHelper().setErrorCode(1);
               response.getErrorCodeHelper().setErrorMessage("Exception in checkBonus: "+e.getMessage());
          }
               return response;
    }






    public MdOrderClientResponse mdOrderListClient(long clientId){
        MdOrderClientResponse response = new MdOrderClientResponse();
           Client client = clientRepository.findOne(clientId);
             if(client!=null){
                 List<MDOrder> mdOrderList = mdOrderRepository.findByClient(client);
                   for(MDOrder mdOrder :mdOrderList){
                      response.getMdOrderInfoList().add(ModelsUtils.toModel(mdOrder));
                   }
                 response.getErrorCodeHelper().setErrorCode(0);
                 response.getErrorCodeHelper().setErrorMessage("");
             }else{
                 response.getErrorCodeHelper().setErrorCode(1);
                 response.getErrorCodeHelper().setErrorMessage("Клиент не найден");
             }
            return response;
    }







    public DriverPenalizationListResponse driverPenalizationList(Long driverId, Long startTime,Long endTime){
        DriverPenalizationListResponse response = new DriverPenalizationListResponse();
        List<DriverCash> driverCashList = new ArrayList<>();
        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(DriverCashFlow.class);
        criteria.add(Restrictions.eq("operation", 1));
        if(driverId!=null){
            criteria.add(Restrictions.eq("driver.id", driverId));
        }
        /*
        if(startDateTime!=null && endDateTime!=null){
             Date d1 = new Date(startDateTime * 1000);
             Date d2 = new Date(endDateTime * 1000);
             criteria.add(Restrictions.ge("date_operation", new DateTime(d1)));
             criteria.add(Restrictions.lt("date_operation", new DateTime(d2)));
        }else if(startDateTime!=null && endDateTime==null){
             Date d1 = new Date(startDateTime * 1000);
             criteria.add(Restrictions.ge("date_operation", new DateTime(d1)));
        }else if(startDateTime==null && endDateTime!=null){
             Date d2 = new Date(endDateTime * 1000);
             criteria.add(Restrictions.lt("date_operation", new DateTime(d2)));
        }
        */

        criteria = QueryUtils.fillDateTimeParameter(criteria, "date_operation", startTime, endTime);
        List<DriverCashFlow> driverCashFlowList = criteria.list();
           for(DriverCashFlow driverCashFlow : driverCashFlowList){
               driverCashList.add(ModelsUtils.toModel(driverCashFlow));
           }
             response.setDriverPenalizationCashList(driverCashList);

               return response;
    }





    public DriverCorrectionResponse driverCorrection(String security_token, long startTime, long endTime, long webUserId, long driverId, long articleId) {
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser==null){
            throw new CustomException(1, "Web user not found");
        }
        DriverCorrectionResponse response = new DriverCorrectionResponse();
        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(DriverCorrections.class);
        criteria.createAlias("driverCashFlow", "cashFlow");
        if(webUserId!=0){
            criteria.add(Restrictions.eq("webUser.id", webUserId));
        }
        if(driverId!=0){
            criteria.add(Restrictions.eq("cashFlow.driver.id", driverId));
        }
        if(articleId!=0){
            criteria.add(Restrictions.eq("articleAdjustments.id", articleId));
        }
        criteria = QueryUtils.fillDateTimeParameter(criteria, "cashFlow.date_operation", startTime, endTime);
        /*
        if(startTime != 0 && endTime != 0){
            criteria.add(Restrictions.ge("cashFlow.date_operation", new DateTime(new Date(startTime))));
            criteria.add(Restrictions.lt("cashFlow.date_operation", new DateTime(new Date(endTime))));
        }else if(startTime != 0 && endTime == 0){
            criteria.add(Restrictions.ge("cashFlow.date_operation", new DateTime(new Date(startTime))));
        }else if(startTime == 0 && endTime != 0){
            criteria.add(Restrictions.lt("cashFlow.date_operation", new DateTime(new Date(endTime))));
        }
        */
        criteria.addOrder(Order.desc("cashFlow.date_operation"));

        List<DriverCorrections> listDriverCorrection = criteria.list();
        for(DriverCorrections driverCorrection: listDriverCorrection){
            response.getDriverCorrectionInfos().add(ModelsUtils.toModelDriverCorrectionInfo(driverCorrection));
        }
        return response;
    }





    public TaxoparkCashFlowResponse taxoparkCashFlow(String security_token, long startTime, long endTime, long taxoparkId, int numberPage, int sizePage){
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser==null){
            throw new CustomException(1, "Web user not found");
        }
        TaxoparkCashFlowResponse response = new TaxoparkCashFlowResponse();
        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(TaxoparkCashFlow.class);
        //Criteria crit = session.createCriteria(TaxoparkCashFlow.class);

        if(taxoparkId!=0){
            criteria.add(Restrictions.eq("taxopark.id", taxoparkId));
            //crit.add(Restrictions.eq("taxopark.id", taxoparkId));
        }
        if(webUser.getTaxoparkId()!=null){
            criteria.add(Restrictions.eq("taxopark.id", webUser.getTaxoparkId()));
            //crit.add(Restrictions.eq("taxopark.id", webUser.getTaxoparkId()));
        }

        criteria = QueryUtils.fillDateTimeParameter(criteria, "dateOperation", startTime, endTime);
        /*
        if(startTime!=0 || endTime!=0){
            criteria.add(Restrictions.ge("dateOperation", new DateTime(new Date(startTime * 1000))));
            criteria.add(Restrictions.lt("dateOperation", new DateTime(new Date(endTime * 1000))));

            //crit.add(Restrictions.ge("dateOperation", new DateTime(new Date(startTime * 1000))));
            //crit.add(Restrictions.lt("dateOperation", new DateTime(new Date(endTime * 1000))));
        }
        */

        criteria.addOrder(Order.desc("dateOperation"));

        criteria.setProjection(Projections.rowCount());
        Long total = (Long)criteria.uniqueResult();
        criteria.setProjection(null);

        response.setTotalItems(total);

        long lastPageNumber = ((total / sizePage) + 1);
        response.setLastPageNumber(lastPageNumber);

        criteria.setFirstResult((numberPage - 1) * sizePage);
        criteria.setMaxResults(sizePage);

        List<TaxoparkCashFlow> taxoparkCashFlowList = criteria.list();

        for(TaxoparkCashFlow cashFlow: taxoparkCashFlowList){
            response.getTaxoparkCashFlowInfos().add(ModelsUtils.toModel(cashFlow));
        }
           return response;
    }






    public CorporateClientCashFlowResponse corporateClientCashFlow(String security_token, long startTime, long endTime, long clientId, long mainClientId, int numberPage, int sizePage){
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser==null){
            throw new CustomException(1, "Web user not found");
        }
        CorporateClientCashFlowResponse response = new CorporateClientCashFlowResponse();
        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(CorporateClientCashFlow.class);
        //Criteria crit = session.createCriteria(CorporateClientCashFlow.class);

        if(clientId!=0){
            criteria.add(Restrictions.eq("client.id", clientId));
            //crit.add(Restrictions.eq("client.id", clientId));
        }
        if(mainClientId!=0){
            criteria.add(Restrictions.eq("mainClient.id", mainClientId));
            //crit.add(Restrictions.eq("mainClient.id", mainClientId));
        }

        criteria = QueryUtils.fillDateTimeParameter(criteria, "dateOperation", startTime, endTime);

        /*
        if(startTime!=0 || endTime!=0){
            criteria.add(Restrictions.ge("dateOperation", new DateTime(new Date(startTime * 1000))));
            criteria.add(Restrictions.lt("dateOperation", new DateTime(new Date(endTime * 1000))));

            //crit.add(Restrictions.ge("dateOperation", new DateTime(new Date(startTime * 1000))));
            //crit.add(Restrictions.lt("dateOperation", new DateTime(new Date(endTime * 1000))));
        }
        */



        criteria.addOrder(Order.desc("dateOperation"));

        criteria.setProjection(Projections.rowCount());
        Long total = (Long)criteria.uniqueResult();
        criteria.setProjection(null);

        response.setTotalItems(total);

        long lastPageNumber = ((total / sizePage) + 1);
        response.setLastPageNumber(lastPageNumber);

        criteria.setFirstResult((numberPage - 1) * sizePage);
        criteria.setMaxResults(sizePage);

        List<CorporateClientCashFlow> listCorporateClientCashFlow = criteria.list();

        for(CorporateClientCashFlow cashFlow: listCorporateClientCashFlow){
            response.getCorporateClientCashFlowInfos().add(ModelsUtils.toModelCorporateClientCashFlow(cashFlow));
        }

          return response;
    }





    public CorporateMissionStatResponse corporateMissionStat(String security_token, long mainClientId, long clientId, long startTime, long endTime, int pageSize, int numPage){
            WebUser webUser = webUserRepository.findByToken(security_token);
            if(webUser == null){
                throw new CustomException(1, "Web user not found");
            }
            Session session = entityManager.unwrap(Session.class);
            Criteria criteria = session.createCriteria(Mission.class);
            criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            criteria.createAlias("clientInfo", "client");

            if(mainClientId!=0){
                criteria.add(Restrictions.eq("client.mainClient.id", mainClientId));
            }
            if(clientId!=0){
                criteria.add(Restrictions.eq("client.id", clientId));
            }

            criteria = QueryUtils.fillDateTimeParameter(criteria, "timeOfRequesting", startTime, endTime);

            criteria.add(Restrictions.eq("state", Mission.State.COMPLETED));
            criteria.add(Restrictions.or(Restrictions.eq("paymentType", PaymentType.CORPORATE_CARD), Restrictions.eq("paymentType", PaymentType.CORPORATE_BILL), Restrictions.eq("paymentType", PaymentType.CORPORATE_CASH)));
            criteria.addOrder(Order.desc("timeOfRequesting"));

            CorporateMissionStatResponse response = new CorporateMissionStatResponse();

            criteria.setProjection(Projections.rowCount());
            Long total = (Long) criteria.uniqueResult();

            criteria.setProjection(null);
            criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            response.setTotalItems(total);

            long lastPageNumber = ((total / pageSize) );
            response.setLastPageNumber(lastPageNumber);

            List<Mission> missionsBeforePaging = criteria.list();

            int allSum = 0;
            for(Mission mission :missionsBeforePaging){
                allSum+= mission.getStatistics().getPriceInFact()!=null ? mission.getStatistics().getPriceInFact().getAmount().intValue() : 0;
            }
            response.setAllSum(allSum);

            criteria.setFirstResult((numPage - 1) * pageSize);
            criteria.setMaxResults(pageSize);

            List<Mission> missions = criteria.list();

            int sum = 0;
            for(Mission mission: missions){
                MissionInfoCorporate missionInfo = ModelsUtils.toModelMissionInfoCorporate(mission);
                CommonService.TimeWaitClientUtil timeWaitClientUtil = commonService.freeTimeLeftWaitClient(mission);
                int countWaitPaymentMinutes = 0;
                if(timeWaitClientUtil.getFreeTimeInFact() < 0){
                    countWaitPaymentMinutes = Math.abs(timeWaitClientUtil.getFreeTimeInFact());
                }
                missionInfo.setMinPaymentWait(countWaitPaymentMinutes);
                missionInfo.setCostPaymentWait(commonService.pricePaymentWaiting(mission.getAutoClass().getValue(), missionInfo.getMinPaymentWait()));
                sum+=mission.getStatistics().getPriceInFact().getAmount().intValue();
                response.getMissionInfos().add(missionInfo);
            }
            response.setSum(sum);
            return response;
    }






    public DriverCashFlowResponse driverCashFlow(String security_token, long startTime, long endTime, long clientId, long taxoparkId, long driverId, int numberPage, int sizePage){
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser==null){
            throw new CustomException(1, "Web user not found");
        }
        DriverCashFlowResponse response = new DriverCashFlowResponse();

        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(DriverCashFlow.class);
        //Criteria crit = session.createCriteria(DriverCashFlow.class);

        if(clientId!=0){
            criteria.createAlias("mission", "miss");
            //crit.createAlias("mission", "miss");

            criteria.add(Restrictions.eq("miss.clientInfo.id", clientId));
            //crit.add(Restrictions.eq("miss.clientInfo.id", clientId));
        }
        if(taxoparkId!=0){
            criteria.createAlias("driver", "drv");
            //crit.createAlias("driver", "drv");

            criteria.add(Restrictions.eq("drv.taxoparkPartners.id", taxoparkId));
            //crit.add(Restrictions.eq("drv.taxoparkPartners.id", taxoparkId));
        }
        if(driverId!=0){
            criteria.add(Restrictions.eq("driver.id", driverId));
            //crit.add(Restrictions.eq("driver.id", driverId));
        }
        criteria = QueryUtils.fillDateTimeParameter(criteria, "date_operation", startTime, endTime);
        /*
        if(startTime!=0 || endTime!=0){
            criteria.add(Restrictions.ge("date_operation", new DateTime(new Date(startTime * 1000))));
            criteria.add(Restrictions.lt("date_operation", new DateTime(new Date(endTime * 1000))));

            //crit.add(Restrictions.ge("date_operation", new DateTime(new Date(startTime * 1000))));
            //crit.add(Restrictions.lt("date_operation", new DateTime(new Date(endTime * 1000))));
        }
        */



        criteria.addOrder(Order.desc("date_operation"));

        criteria.setProjection(Projections.rowCount());
        Long total = (Long)criteria.uniqueResult();
        criteria.setProjection(null);

        response.setTotalItems(total);

        long lastPageNumber = ((total / sizePage) + 1);
        response.setLastPageNumber(lastPageNumber);

        criteria.setFirstResult((numberPage - 1) * sizePage);
        criteria.setMaxResults(sizePage);

        List<DriverCashFlow> listDriverCashFlow = criteria.list();

        for(DriverCashFlow driverCashFlow: listDriverCashFlow){
            response.getDriverCashFlowInfos().add(ModelsUtils.toModelDriverCashFlowInfo(driverCashFlow));
        }
           return response;
    }






    public PeriodWorkResponse periodWork(String security_token, int numberPage, int sizePage){
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser==null){
            throw new CustomException(1, "Web user not found");
        }
        PeriodWorkResponse response = new PeriodWorkResponse();
        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(PeriodWork.class);

        criteria.addOrder(Order.desc("startPeriod"));

        criteria.setProjection(Projections.rowCount());
        Long total = (Long)criteria.uniqueResult();
        criteria.setProjection(null);

        response.setTotalItems(total);

        long lastPageNumber = ((total / sizePage) + 1);
        response.setLastPageNumber(lastPageNumber);

        criteria.setFirstResult((numberPage - 1) * sizePage);
        criteria.setMaxResults(sizePage);

        List<PeriodWork> listPeriodWork = criteria.list();

        for(PeriodWork periodWork: listPeriodWork){
           response.getPeriodWorkInfoList().add(ModelsUtils.toModel(periodWork));
        }
          return response;
    }




    public CorporateBalanceReportResponse corporateBalanceReport(String security_token, long mainClientId, long startDateTime, long endDateTime){
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser == null){
            throw new CustomException(1, "Web user not found");
        }
        CorporateBalanceReportResponse response = new CorporateBalanceReportResponse();

        if(mainClientId!=0){
            Client mainClient = clientRepository.findOne(mainClientId);
            if(mainClient==null){
                throw new CustomException(2, String.format("Клиент id %s не найден", mainClientId));
            }
            if(!mainClient.getId().equals(mainClient.getMainClient().getId())){
                throw new CustomException(4, String.format("Клиент %s не является главным", mainClientId));
            }
            response = getCorporateBalanceReportInfoList(response, mainClient, startDateTime, endDateTime);
            //criteria.createAlias("mainClient", "mainClient").add(Restrictions.eqProperty("cl.id", "mainClient.id"));
        }else{
            // по всем
            List<Client> mainClientList = clientRepository.mainClientList();
            if(!CollectionUtils.isEmpty(mainClientList)){
                for(Client mainClient: mainClientList){
                    response = getCorporateBalanceReportInfoList(response, mainClient, startDateTime, endDateTime);
                }
            }
        }
        return response;
    }




    private CorporateBalanceReportResponse getCorporateBalanceReportInfoList(CorporateBalanceReportResponse response, Client mainClient, long startDateTime, long endDateTime){
        CorporateBalanceReportInfo info = new CorporateBalanceReportInfo();
        info.setStartBalanceAmount(corporateBalanceAmount(mainClient, startDateTime));
        info.setEndBalanceAmount(corporateBalanceAmount(mainClient, endDateTime));
        info.setClientInfoARM(ModelsUtils.toModelClientInfoARM(mainClient, null));
        info.setIncreaseAmount((int)increaseAmount(mainClient, startDateTime, endDateTime, true));
        info.setDecrease((int)increaseAmount(mainClient, startDateTime, endDateTime, false));
        response.getBalanceReportInfos().add(info);
           return response;
    }


    // сумма операций по корпоративной учетке
    private int corporateBalanceAmount(Client mainClient, long dateTimeLong){
        DateTime dateTime = DateTimeUtils.nowNovosib_GMT6();
        if(dateTimeLong!=0){
            dateTime = new DateTime(new Date(dateTimeLong * 1000));
        }
        Session session = entityManager.unwrap(Session.class);
        Criteria crSum = session.createCriteria(CorporateClientCashFlow.class);
        crSum.add(Restrictions.eq("mainClient", mainClient));
        crSum.add(Restrictions.lt("dateOperation", dateTime));
        crSum.add(Restrictions.and(Restrictions.ne("operation", 2), Restrictions.ne("operation", 3))); // 2,3 - безнал
        crSum.setProjection(Projections.sum("sum"));

        Long res =  (Long)crSum.uniqueResult();
        return res!=null ? res.intValue()/100:0;
    }





    private long increaseAmount(Client mainClient, long startDateTime, long endDateTime, boolean flag){ // flag = true - приход, false = расход
        Session session = entityManager.unwrap(Session.class);
        Criteria crSumCorrection = session.createCriteria(CorporateClientCashFlow.class);
        crSumCorrection.add(Restrictions.eq("mainClient", mainClient));
        crSumCorrection = QueryUtils.fillDateTimeParameter(crSumCorrection , "dateOperation", startDateTime, endDateTime);
        crSumCorrection.add(Restrictions.and(Restrictions.ne("operation", 2), Restrictions.ne("operation", 3)));
        crSumCorrection.add(flag ? Restrictions.gt("sum", 0) : Restrictions.lt("sum", 0));
        crSumCorrection.setProjection(Projections.sum("sum"));
        Long sumCorrection = (Long)crSumCorrection.uniqueResult();
        sumCorrection = (sumCorrection != null ? sumCorrection.intValue() : 0L)/100;
        return sumCorrection;
    }





    public MissionsListResponse findMissionListByMask(String security_token, int numPage, int pageSize, long startDate, long endDate, String nameMask, String phoneMask, String carModelMask, String carNumberMask, String missionState, long missionId, long assistantId, String typeOS, long clientId, int autoType, Boolean onlyBooked){
        LOGGER.info("ACTUAL LIST MISSION");
        MissionsListResponse response = new MissionsListResponse();
            WebUser webUser = webUserRepository.findByToken(security_token);
            if (webUser == null) {
                return response;
            }
            //Criteria.DISTINCT_ROOT_ENTITY DistinctRootEntityResultTransformer.INSTANCE

/*
 WORK!!!
 Query query = session.createQuery("From Mission");
            //query.
            query.setFirstResult(0);
            query.setMaxResults(10);
            List<Mission> fooList = query.list();
 */

            List<MissionInfoARM> missionInfoList = new ArrayList<MissionInfoARM>();
            Session session = entityManager.unwrap(Session.class);
            Criteria criteria = session.createCriteria(Mission.class);
            criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);


            criteria.createAlias("clientInfo", "client");
            criteria.createAlias("driverInfo", "driver");


            if (carNumberMask != null || carModelMask != null || assistantId != 0) {
                //criteria.createAlias("driverInfo", "driver");
                //crit.createAlias("driverInfo", "driver");
            }
            if(webUser.getTaxoparkId()!=null){
                criteria.add(Restrictions.eq("driver.taxoparkPartners.id", webUser.getTaxoparkId()));
                //crit.add(Restrictions.eq("driver.taxoparkPartners.id", webUser.getTaxoparkId()));
            }
            if (onlyBooked != null) {
                criteria.add(Restrictions.eq("isBooked", onlyBooked));
                //crit.add(Restrictions.eq("isBooked", onlyBooked));
            }
            if (missionState != null) {
                criteria.add(Restrictions.eq("state", Mission.State.valueOf(missionState)));
                //crit.add(Restrictions.eq("state", Mission.State.valueOf(missionState)));
            }

            if (typeOS != null) {
            /* старый вариант поиска клиентов по типу ОС из DeviceInfo класса Client*/
            /*
            criteria.createAlias("clientInfo.devices", "devices");
            criteria.add(Restrictions.eq("devices.type", DeviceInfo.Type.valueOf(typeOS)));
            criteria.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);

            crit.createAlias("clientInfo.devices", "devices");
            crit.add(Restrictions.eq("devices.type", DeviceInfo.Type.valueOf(typeOS)));
            crit.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
            */

            /* новый вариант - поиск по типу ОС, который получаем непосредственно из Client*/
                criteria.add(Restrictions.eq("client.deviceType", typeOS));
                //crit.add(Restrictions.eq("client.deviceType", typeOS));
            }
            if (autoType != 0) {
                criteria.add(Restrictions.eq("autoClass", AutoClass.getByValue(autoType)));
                //crit.add(Restrictions.eq("autoClass", AutoClass.getByValue(autoType)));
            }
            if (missionId != 0) {
                criteria.add(Restrictions.eq("id", missionId));
                //crit.add(Restrictions.eq("id", missionId));
            }
            if (clientId != 0) {
                criteria.add(Restrictions.eq("client.id", clientId));
                //crit.add(Restrictions.eq("client.id", clientId));
            }
            if (assistantId != 0) {
                criteria.add(Restrictions.eq("driver.assistant.id", assistantId));
                //crit.add(Restrictions.eq("driver.assistant.id", assistantId));
            }
            if (carNumberMask != null) {
                criteria.add(Restrictions.ilike("driver.autoNumber", "%" + carNumberMask + "%"));
                //crit.add(Restrictions.ilike("driver.autoNumber", "%" + carNumberMask + "%"));
            }
            if (carModelMask != null) {
                criteria.add(Restrictions.ilike("driver.autoModel", "%" + carModelMask + "%"));
                //crit.add(Restrictions.ilike("driver.autoModel", "%" + carModelMask + "%"));
            }
            if (onlyBooked != null && onlyBooked.equals(Boolean.TRUE)) {
                criteria = QueryUtils.fillDateTimeParameter(criteria, "timeOfStarting", startDate, endDate);
                /*
                if (startDate != 0 && endDate != 0) {
                    criteria.add(Restrictions.ge("timeOfStarting", new DateTime(new Date(startDate * 1000))));
                    criteria.add(Restrictions.lt("timeOfStarting", new DateTime(new Date(endDate * 1000))));

                    //crit.add(Restrictions.ge("timeOfStarting", new DateTime(new Date(startDate * 1000))));
                    //crit.add(Restrictions.lt("timeOfStarting", new DateTime(new Date(endDate * 1000))));
                } else if (startDate != 0 && endDate == 0) {
                    criteria.add(Restrictions.ge("timeOfStarting", new DateTime(new Date(startDate * 1000))));
                    //crit.add(Restrictions.ge("timeOfStarting", new DateTime(new Date(startDate * 1000))));
                } else if (startDate == 0 && endDate != 0) {
                    criteria.add(Restrictions.lt("timeOfStarting", new DateTime(new Date(endDate * 1000))));
                    //crit.add(Restrictions.lt("timeOfStarting",  new DateTime(new Date(endDate * 1000))));
                }
                */

            } else {


                criteria = QueryUtils.fillDateTimeParameter(criteria, "timeOfRequesting", startDate, endDate);
                /*
                if (startDate != 0 && endDate != 0) {
                    criteria.add(Restrictions.ge("timeOfRequesting", new DateTime(new Date(startDate * 1000))));
                    criteria.add(Restrictions.lt("timeOfRequesting", new DateTime(new Date(endDate * 1000))));

                    //crit.add(Restrictions.ge("timeOfRequesting", new DateTime(new Date(startDate * 1000))));
                    //crit.add(Restrictions.lt("timeOfRequesting", new DateTime(new Date(endDate * 1000))));
                } else if (startDate != 0 && endDate == 0) {
                    criteria.add(Restrictions.ge("timeOfRequesting", new DateTime(new Date(startDate * 1000))));
                    //crit.add(Restrictions.ge("timeOfRequesting", new DateTime(new Date(startDate * 1000))));
                } else if (startDate == 0 && endDate != 0) {
                    criteria.add(Restrictions.lt("timeOfRequesting", new DateTime(new Date(endDate * 1000))));
                    //crit.add(Restrictions.lt("timeOfRequesting", new DateTime(new Date(endDate * 1000))));
                }
                */
            }
            if (nameMask != null) {
                criteria.add(Restrictions.or(Restrictions.ilike("client.firstName", "%" + nameMask + "%"), Restrictions.ilike("client.lastName", "%" + nameMask + "%")));
                //crit.add(Restrictions.or(Restrictions.ilike("client.firstName", "%" + nameMask + "%"), Restrictions.ilike("client.lastName", "%" + nameMask + "%")));
            }
            if (phoneMask != null) {
                criteria.add(Restrictions.ilike("client.phone", "%" + phoneMask + "%"));
                //crit.add(Restrictions.ilike("client.phone", "%" + phoneMask + "%"));
            }

            if (onlyBooked != null && onlyBooked.equals(Boolean.TRUE)) {
                criteria.addOrder(Order.desc("timeOfStarting"));
            } else {
                criteria.addOrder(Order.desc("timeOfRequesting"));
            }

        criteria.setProjection(Projections.rowCount());
        Long total = (Long)criteria.uniqueResult();
        criteria.setProjection(null);

        response.setTotalItems(total);

        long lastPageNumber = ((total / pageSize) + 1);
        response.setLastPageNumber(lastPageNumber);

        criteria.setFirstResult((numPage - 1) * pageSize);
        criteria.setMaxResults(pageSize);

        List<Mission> listMission = criteria.list();

            for (Mission miss : listMission) {
                missionInfoList.add(ModelsUtils.toModel_MissionInfoARM(miss));
            }





        response.setMissionInfoARM(missionInfoList);

             return response;
    }





    public MissionsListResponse findMissionListByMask_HQL(String security_token, int numPage, int pageSize, long startDate, long endDate, String nameMask, String phoneMask, String carModelMask, String carNumberMask, String missionState, long missionId, long assistantId, String typeOS, long clientId, int autoType, Boolean onlyBooked, long driverId){
        MissionsListResponse response = new MissionsListResponse();
        WebUser webUser = webUserRepository.findByToken(security_token);
        if (webUser == null) {
            return response;
        }
        List<MissionInfoARM> missionInfoList = new ArrayList();
        Session session = entityManager.unwrap(Session.class);

        Map<String, Object> parameters = new HashMap();
        StringBuffer queryBuf = new StringBuffer("from Mission m ");

        if(clientId!=0 || nameMask != null || typeOS!=null || phoneMask!=null){
            queryBuf.append(" join m.clientInfo c");
        }
        if(assistantId!=0 || carModelMask!=null || carNumberMask!=null || webUser.getTaxoparkId()!=null || driverId!=0){
            queryBuf.append(" join m.driverInfo d");
        }
            queryBuf.append(" where 1=1 ");
        if(missionId!=0){
            queryBuf.append(" and m.id=:missionId");
            parameters.put("missionId", missionId);
        }
        if(webUser.getTaxoparkId()!=null){
            queryBuf.append(" and d.taxoparkPartners.id=:taxoparkId");
            parameters.put("taxoparkId", webUser.getTaxoparkId());
        }
        if (onlyBooked != null) {
            queryBuf.append(" and m.isBooked=:isBooked");
            parameters.put("isBooked", onlyBooked);
        }
        if (missionState != null) {
            queryBuf.append(" and m.state=:state");
            parameters.put("state", Mission.State.valueOf(missionState));
        }
        if (typeOS != null) {
            queryBuf.append(" and c.deviceType=:deviceType");
            parameters.put("deviceType", typeOS);
        }
        if (autoType != 0) {
            queryBuf.append(" and m.autoClass=:autoClass");
            parameters.put("autoClass", AutoClass.getByValue(autoType));
        }
        if (clientId != 0) {
            queryBuf.append(" and c.id=:clientId");
            parameters.put("clientId", clientId);
        }
        if (driverId != 0) {
            queryBuf.append(" and d.id=:driverId");
            parameters.put("driverId", driverId);
        }
        if (assistantId != 0) {
            queryBuf.append(" and d.assistant.id=:assistantId");
            parameters.put("assistantId", assistantId);
        }
        if (carNumberMask != null) {
            queryBuf.append(" and d.autoNumber like " + "'%" + carNumberMask + "%'");
        }
        if (carModelMask != null) {
            queryBuf.append(" and d.autoModel like " + "'%" + carModelMask + "%'");
        }
        if (nameMask != null) {
            queryBuf.append(" and (c.firstName like " + "'%" + nameMask + "%'" + " or c.lastName like " + "'%" + nameMask + "%')");
        }
        if (phoneMask != null) {
            queryBuf.append(" and c.phone like " + "'%" + phoneMask + "%'");
        }

        String filterTime = "timeOfRequesting";
        if(missionState != null && Mission.State.valueOf(missionState).equals(Mission.State.COMPLETED)){
            filterTime = "timeOfFinishing";
        }
        if(onlyBooked != null && onlyBooked.equals(Boolean.TRUE)){
            filterTime = "timeOfStarting";
        }
        if (startDate != 0 && endDate != 0) {
            queryBuf.append(" and m."+filterTime+">=:startDate and m."+filterTime+"<=:endDate");
            parameters.put("startDate", new DateTime(new Date(startDate * 1000)));
            parameters.put("endDate", new DateTime(new Date(endDate * 1000)));
        } else if (startDate != 0 && endDate == 0) {
            queryBuf.append(" and m."+filterTime+">=:startDate ");
            parameters.put("startDate", new DateTime(new Date(startDate * 1000)));
        } else if (startDate == 0 && endDate != 0) {
            queryBuf.append(" and m."+filterTime+"<=:endDate");
            parameters.put("endDate", new DateTime(new Date(endDate * 1000)));
        }

        queryBuf.append(" order by m.timeOfRequesting desc");

        String hqlQuery = queryBuf.toString();

        Query query = session.createQuery(hqlQuery);
        Query queryCount = session.createQuery("select count(m) "+hqlQuery);
        Iterator iter = parameters.keySet().iterator();
        while (iter.hasNext()) {
            String name = (String) iter.next();
            Object value = parameters.get(name);
            query.setParameter(name,value);
            queryCount.setParameter(name,value);
        }

        query.setFirstResult((numPage - 1) * pageSize);
        query.setMaxResults(pageSize);

        LOGGER.info("Fractal: Result query: " + query.getQueryString() + " \n Query count: " + queryCount);

        List<Object> result = (List<Object>) query.list();
        Iterator itr = result.iterator();
        while(itr.hasNext() ){
            Mission mission = null;
            Object object = itr.next();
            if(object instanceof Mission){
                mission = (Mission) object;
            } else if(object instanceof Object[]){
                Object[] obj = (Object[]) object;
                mission = (Mission)obj[0];
            }
            if(mission!=null){
                missionInfoList.add(ModelsUtils.toModel_MissionInfoARM(mission));
            }
        }
        response.setMissionInfoARM(missionInfoList);

        Long count = (Long)queryCount.uniqueResult();
        response.setTotalItems(count);
        response.setLastPageNumber(((count / pageSize)));
           return response;
    }







    public boolean isContainsSum(long clientId, int sum){
         boolean result = false;
         ClientSumPromoCode clientSumPromoCodeBySum =  availableSumPromoCodeRepository.findByClientIdAndAvailableAmount(clientId, sum);
            if(clientSumPromoCodeBySum!=null){
                 result= true;
            }
           return result;
    }


    public ClientSumPromoCodeUpdateResponse clientAvailableSumPromoSendUpd(long clientSumPromoCodeId, int sum){
        ClientSumPromoCodeUpdateResponse response = new ClientSumPromoCodeUpdateResponse();
            ClientSumPromoCode clientSumPromoCode =  availableSumPromoCodeRepository.findOne(clientSumPromoCodeId);
               if(clientSumPromoCode!=null){
                       if(isContainsSum(clientSumPromoCode.getClientId(), sum)){
                           response.getErrorCodeHelper().setErrorCode(2);
                           response.getErrorCodeHelper().setErrorMessage("У клиента уже задана данная сумма");
                       }else{
                           clientSumPromoCode.setAvailableAmount(sum);
                           availableSumPromoCodeRepository.save(clientSumPromoCode);
                           response.getErrorCodeHelper().setErrorCode(0);
                           response.getErrorCodeHelper().setErrorMessage("Успешно обновлено");
                       }
        }else{
            response.getErrorCodeHelper().setErrorCode(1);
            response.getErrorCodeHelper().setErrorMessage("Запись не найдена");
        }
        return response;
    }





    public ClientSumPromoCodeDeleteResponse clientAvailableSumPromoSendDelete(long clientSumPromoCodeId){
        ClientSumPromoCodeDeleteResponse response = new ClientSumPromoCodeDeleteResponse();
        ClientSumPromoCode clientSumPromoCode =  availableSumPromoCodeRepository.findOne(clientSumPromoCodeId);
        if(clientSumPromoCode!=null){
                availableSumPromoCodeRepository.delete(clientSumPromoCode);
        }else{
            response.getErrorCodeHelper().setErrorCode(1);
            response.getErrorCodeHelper().setErrorMessage("Запись не найдена");
        }
        return response;
    }




    public ClientSumPromoCodeInsertResponse availableSumPromoInsert(long clientId, int sum){
        ClientSumPromoCodeInsertResponse response =  new ClientSumPromoCodeInsertResponse();
        if(isContainsSum(clientId, sum)){
            response.getErrorCodeHelper().setErrorCode(2);
            response.getErrorCodeHelper().setErrorMessage("У клиента уже задана данная сумма");
        }else{
            ClientSumPromoCode clientSumPromoCode = new ClientSumPromoCode();
            clientSumPromoCode.setClientId(clientId);
            clientSumPromoCode.setAvailableAmount(sum);

            availableSumPromoCodeRepository.save(clientSumPromoCode);
        }
          return response;
    }



    public ClientSumPromoCodeResponse getClientAvailableSumPromoSend(long clientId){
        ClientSumPromoCodeResponse response = new ClientSumPromoCodeResponse();
        Client client= clientRepository.findOne(clientId);
        if(client!=null){
            List<ClientSumPromoCode> clientSumPromoCodeList =  availableSumPromoCodeRepository.findByClientId(clientId);
            if(clientSumPromoCodeList!=null && !clientSumPromoCodeList.isEmpty()){
                   for(ClientSumPromoCode clientSumPromoCode: clientSumPromoCodeList){
                       ClientSumPromoCodeResponse.Details detail = new ClientSumPromoCodeResponse.Details();
                       detail.setId(clientSumPromoCode.getId());
                       detail.setSum(clientSumPromoCode.getAvailableAmount());
                       response.getDetails().add(detail);
                   }
                       response.getErrorCodeHelper().setErrorCode(0);
                       response.getErrorCodeHelper().setErrorMessage("");
            }else{
                response.getErrorCodeHelper().setErrorCode(2);
                response.getErrorCodeHelper().setErrorMessage("Для клиента не задано ни одной суммы");
            }
        }else{
            response.getErrorCodeHelper().setErrorCode(1);
            response.getErrorCodeHelper().setErrorMessage("Клиент не найден");
        }
        return response;
    }






    public ClientAvailableActivatePromoCodeResponse getAvailableCountActivatedPromoCode(long clientId){
        ClientAvailableActivatePromoCodeResponse response = new ClientAvailableActivatePromoCodeResponse();
        Client client= clientRepository.findOne(clientId);
        if(client!=null){
            ClientAvailableActivatePromoCode clientAvailableActivatePromoCode = clientAvailableActivatePromoCodeRepository.findByClientId(clientId);
            if (clientAvailableActivatePromoCode != null) {
                response.getDetails().setId(clientAvailableActivatePromoCode.getId());
                response.getDetails().setCount(clientAvailableActivatePromoCode.getAvailableActivateCount());
                response.getErrorCodeHelper().setErrorCode(0);
                response.getErrorCodeHelper().setErrorMessage("");
            } else {
                response.getErrorCodeHelper().setErrorCode(2);
                response.getErrorCodeHelper().setErrorMessage("Для клиента не задано допустимое количество активаций промокодов в сутки");
            }
        }else{
            response.getErrorCodeHelper().setErrorCode(1);
            response.getErrorCodeHelper().setErrorMessage("Клиент не найден");
        }
        return response;
    }




    public ClientCountPromoCodeResponse getClientAvailableCountPromoSend(long clientId){
        ClientCountPromoCodeResponse response = new ClientCountPromoCodeResponse();
        Client client= clientRepository.findOne(clientId);
          if(client!=null){
              ClientCountPromoCode clientCountPromoCode =  clientCountPromoCodeRepository.findByClientId(clientId);
                  if(clientCountPromoCode!=null){
                      response.getDetails().setId(clientCountPromoCode.getId());
                      response.getDetails().setCount(clientCountPromoCode.getAvailableCount());
                      response.getErrorCodeHelper().setErrorCode(0);
                      response.getErrorCodeHelper().setErrorMessage("");
                  }else{
                      response.getErrorCodeHelper().setErrorCode(2);
                      response.getErrorCodeHelper().setErrorMessage("Для клиента не задано допустимое количество оправки промокодов в сутки");
                  }
          }else{
              response.getErrorCodeHelper().setErrorCode(1);
              response.getErrorCodeHelper().setErrorMessage("Клиент не найден");
          }
          return response;
    }







    public ClientAvailableActivatePromoCodeUpdateResponse availableCountActivatedPromoCodeUpd(long clientId, int count){
        ClientAvailableActivatePromoCodeUpdateResponse response = new ClientAvailableActivatePromoCodeUpdateResponse();
        Client client= clientRepository.findOne(clientId);
        if(client!=null){
            ClientAvailableActivatePromoCode clientAvailableActivatePromoCode  = clientAvailableActivatePromoCodeRepository.findByClientId(clientId);
            if(clientAvailableActivatePromoCode==null){
                clientAvailableActivatePromoCode = new ClientAvailableActivatePromoCode();
            }
            clientAvailableActivatePromoCode.setAvailableActivateCount(count);
            clientAvailableActivatePromoCodeRepository.save(clientAvailableActivatePromoCode);
            response.getErrorCodeHelper().setErrorCode(0);
            response.getErrorCodeHelper().setErrorMessage("Успешно обновлено");
        }else{
            response.getErrorCodeHelper().setErrorCode(1);
            response.getErrorCodeHelper().setErrorMessage("Клиент не найден");
        }
        return response;
    }





    public ClientCountPromoCodeUpdateResponse availableCountPromoSendUpd(long clientId, int count){
        ClientCountPromoCodeUpdateResponse response = new ClientCountPromoCodeUpdateResponse();
        Client client= clientRepository.findOne(clientId);
        if(client!=null){
            ClientCountPromoCode clientCountPromoCode = clientCountPromoCodeRepository.findByClientId(clientId);
            if(clientCountPromoCode==null){
                clientCountPromoCode = new ClientCountPromoCode();
            }
            clientCountPromoCode.setAvailableCount(count);
            clientCountPromoCodeRepository.save(clientCountPromoCode);
            response.getErrorCodeHelper().setErrorCode(0);
            response.getErrorCodeHelper().setErrorMessage("Успешно обновлено");
        }else{
            response.getErrorCodeHelper().setErrorCode(1);
            response.getErrorCodeHelper().setErrorMessage("Клиент не найден");
        }
         return response;
    }



    public AssistantStatListResponse getAssistantInfStatListByMaskExtended(int numberPage,int sizePage,long driverId,long taxoparkId, List assistantIdList, long timeRequestingStart,long timeRequestingEnd) throws ParseException {
        AssistantStatListResponse response = new AssistantStatListResponse();
        Session session = entityManager.unwrap(Session.class);
        StringBuilder queryBuilder = new StringBuilder();
        long assistantId = 0;
          if(!assistantIdList.isEmpty()){
              Long.parseLong(assistantIdList.get(0).toString());
              assistantId = Long.parseLong(assistantIdList.get(0).toString());
          }

        queryBuilder.append("select SQL_CALC_FOUND_ROWS * from(\n" +
                "SELECT DATE(m.time_requesting) AS date_one, \n" +
                "count(case when m.state = 'COMPLETED' then 1 end) as completed_cnt,\n" +
                "sum(case when m.state='COMPLETED' then m.price_in_fact_amount end) as sum_completed,\n" +
                "count(case when m.state = 'CANCELED' and m.id in(select canc.mission_id from mission_canceled canc where canc.mission_id=m.id and canc.cancel_by='driver') then 1 end) as canceled_cnt,\n" +
                "sum(case when m.state = 'CANCELED' and m.id in(select canc.mission_id from mission_canceled canc where canc.mission_id=m.id and canc.cancel_by='driver') then m.price_in_fact_amount end) as sum_canceled\n" +
                "FROM mission m\n" +
                "WHERE 1=1 and m.driverInfo_id in(select drv.id from driver drv where drv.assistant_id="+assistantId+")");

        if(taxoparkId!=0){
            queryBuilder.append(" and m.driverInfo_id in(select drv.id from driver drv where drv.taxopark_id="+taxoparkId+")");
        }
        if(driverId!=0){
            queryBuilder.append(" and m.driverInfo_id="+driverId+"");
        }

        queryBuilder.append(" GROUP BY date_one");

        queryBuilder.append(" union all\n" +
                "select r.dates as d, 0, 0, 0, 0 from report_dates r\n" +
                ") as t1 where 1=1 ");


        DateTime dateTimeStart =  new DateTime(timeRequestingStart);
        DateTime dateTimeEnd =  new DateTime(timeRequestingEnd);
        LOGGER.info("dateTimeStart="+dateTimeStart+" mili="+dateTimeStart.getMillis());
        if(timeRequestingStart!=0 || timeRequestingEnd!=0){
            queryBuilder.append(" and unix_timestamp(t1.date_one) between "+dateTimeStart.getMillis() + " and " + dateTimeEnd.getMillis());
        }

        queryBuilder.append(" group by t1.date_one order by date_one LIMIT "+((numberPage - 1) * sizePage)+","+sizePage);

        List<Object> result = session.createSQLQuery(queryBuilder.toString()).list();

        Long totalCount = (Long)session.createSQLQuery("SELECT FOUND_ROWS()").uniqueResult();
        response.setTotalItems(totalCount);

        Iterator it = result.iterator();
        while(it.hasNext()) {
            Object row[] = (Object[]) it.next();
            AssistantStatInfo assistantStatInfo = new AssistantStatInfo();
            if(row[0]!=null) {
                String strDate= row[0].toString();
                DateFormat form = new SimpleDateFormat("yyyy-MM-dd");
                Date date = form.parse(strDate);
                assistantStatInfo.setDay(date.getTime() / 1000);
            }
            if(row[1]!=null) {
                assistantStatInfo.setCountCompletedMiss(Integer.parseInt(row[1].toString()));
            }
            if(row[2]!=null) {
                assistantStatInfo.setSumCompletedMiss(Double.parseDouble(row[2].toString()));
            }
            if(row[3]!=null) {
                assistantStatInfo.setCountCanceledMiss(Integer.parseInt(row[3].toString()));
            }
            if(row[4]!=null) {
                assistantStatInfo.setSumCanceledMiss(Double.parseDouble(row[4].toString()));
            }
            response.getAssistantStatsInfos().add(assistantStatInfo);
        }


          return response;
    }





      public AssistantStatListResponse getAssistantInfStatListByMask(int numberPage,int sizePage,long driverId,long taxoparkId, List assistantIdList, long timeRequestingStart,long timeRequestingEnd){
          AssistantStatListResponse response = new AssistantStatListResponse();
          Session session = entityManager.unwrap(Session.class);
          StringBuilder queryBuilder = new StringBuilder();


          queryBuilder.append("select SQL_CALC_FOUND_ROWS\n" +
                  "  ass.id,\n" +
                  "  count(case when o.state = 'COMPLETED' then 1 end) as completed_cnt,\n" +
                  "  sum(case when o.state='COMPLETED' then o.price_in_fact_amount end) as sumCompl,\n" +
                  "  count(case when can.cancel_by='driver' then 1 end) as canceled_cnt,\n" +
                  "  sum(case when o.state='CANCELED' then o.price_in_fact_amount end) as sumCanc\n" +
                  "from assistant ass\n" +
                  "left join driver drv on drv.assistant_id = ass.id\n" +
                  "left join mission o on o.driverInfo_id = drv.id\n" +
                  "left join mission_canceled can on can.mission_id = o.id\n");

             queryBuilder.append(" where 1=1");

             if(taxoparkId!=0){
                queryBuilder.append(" and drv.taxopark_id="+taxoparkId);
             }
             if(driverId!=0){
               queryBuilder.append(" and drv.id="+driverId);
             }
             if(assistantIdList!=null && !assistantIdList.isEmpty()){
                 StringBuilder sub = new StringBuilder();
                   for(int i=0;i<assistantIdList.size();i++){
                       if(i==0){
                           sub.append(assistantIdList.get(i));
                       }else{
                           sub.append(","+assistantIdList.get(i));
                       }
                   }
               queryBuilder.append(" and drv.assistant_id in("+sub.toString()+")");
             }

          DateTime dateTimeStart =  new DateTime(timeRequestingStart);
          DateTime dateTimeEnd =  new DateTime(timeRequestingEnd);

          LOGGER.info("dateTimeStart="+dateTimeStart+" mili="+dateTimeStart.getMillis());

          if(timeRequestingStart!=0 || timeRequestingEnd!=0){
              queryBuilder.append(" and unix_timestamp(o.time_requesting) between "+dateTimeStart.getMillis()+" and "+dateTimeEnd.getMillis());
          }
               queryBuilder.append(" group by ass.id order by canceled_cnt desc  LIMIT "+((numberPage - 1) * sizePage)+","+sizePage);

          LOGGER.info("query assistant stat= "+queryBuilder.toString());

          List<Object> result = session.createSQLQuery(queryBuilder.toString()).list();

          BigInteger totalCount = (BigInteger)session.createSQLQuery("SELECT FOUND_ROWS()").uniqueResult();

          response.setTotalItems(totalCount.longValue());

          Iterator it = result.iterator();
          while(it.hasNext()) {
              Object row[] = (Object[]) it.next();
              AssistantStatInfo assistantStatInfo = new AssistantStatInfo();
              if(row[0]!=null) {
                  long assistantId = Long.parseLong(row[0].toString());
                  Assistant assistant = assistantRepository.findOne(assistantId);
                  AssistantInfo assistantInfo =  ModelsUtils.toModel(assistant);
                  assistantStatInfo.setAssistantInfo(assistantInfo);
              }
              if(row[1]!=null) {
                  assistantStatInfo.setCountCompletedMiss(Integer.parseInt(row[1].toString()));
              }
              if(row[2]!=null) {
                  assistantStatInfo.setSumCompletedMiss(Double.parseDouble(row[2].toString()));
              }
              if(row[3]!=null) {
                  assistantStatInfo.setCountCanceledMiss(Integer.parseInt(row[3].toString()));
              }
              if(row[4]!=null) {
                  assistantStatInfo.setSumCanceledMiss(Double.parseDouble(row[4].toString()));
              }
              response.getAssistantStatsInfos().add(assistantStatInfo);
          }


              return response;
        }



    public ClientStatListResponse getClientInfoStatListByMask(int numPage, int pageSize, String nameMask,String phoneMask,String adminStatus, int countMissionCompletedStart, int countMissionCompletedEnd, int countCanceledStart, int countCanceledEnd, long registartionDateStart, long registartionDateEnd){
        Session session = entityManager.unwrap(Session.class);
        StringBuilder queryBuilder = new StringBuilder();

        //(select count(*) from mission_canceled canc where canc.cancel_by='client' and c.id=(select m.clientInfo_id from mission m where m.id=canc.mission_id)) as canceled_cnt
        //(select count(*) from mission_canceled canc where canc.cancel_by='client' and c.id=(select m.clientInfo_id from mission m where m.id=canc.mission_id)) as canceled_cnt

        // work variant
        queryBuilder.append("select SQL_CALC_FOUND_ROWS\n" +
                "  c.id,\n" +
                "  c.first_name, c.last_name, c.email, c.phone,\n" +
                "  count(case when o.state = 'COMPLETED' then 1 end) as completed_cnt,\n" +
                "  (select count(*) from mission_canceled canc where canc.cancel_by='client' and c.id=(select m.clientInfo_id from mission m where m.id=canc.mission_id)) as canceled_cnt,\n" + // count(case when o.state = 'CANCELED' then 1 end) as canceled_cnt,
                "  (select count(*) from client_activated_promo_codes act where act.client_id=c.id) as activate_promo_cnt,\n" + // count(a.id) as activate_promo_cnt,
                "  account.bonuses_amount as bonusesAmount\n" +
                " from client c\n" +
                "  left join mission o on c.id = o.clientInfo_id\n" +
                "  left join account account on c.account_id = account.id");


        queryBuilder.append(" where 1=1");

        if(nameMask!=null){
            queryBuilder.append(" and c.first_name like '%"+nameMask+"%' or c.last_name like '%"+nameMask+"%'");
        }
        if(phoneMask!=null){
            queryBuilder.append(" and c.phone like '%"+phoneMask+"%'");
        }
        if(adminStatus!=null){
            queryBuilder.append(" and c.state='"+Client.State.valueOf(adminStatus)+"'");
        }

        Date d1 = new Date(registartionDateStart * 1000);
        Date d2 = new Date(registartionDateEnd * 1000);
        DateTime newD1 = new DateTime(d1);
        DateTime newD2 = new DateTime(d2);

        if(registartionDateStart!=0 || registartionDateEnd!=0){
            queryBuilder.append(" and unix_timestamp(c.registration_time) between "+newD1.getMillis()/1000+" and "+newD2.getMillis()/1000);
        }

        queryBuilder.append(" group by c.id, c.first_name");

        queryBuilder.append(" HAVING 1=1");


        if(countMissionCompletedStart!=0 || countMissionCompletedStart!=0){
            queryBuilder.append(" and completed_cnt between "+countMissionCompletedStart+" and "+countMissionCompletedEnd);
        }
        if(countCanceledStart!=0 || countCanceledEnd!=0){
            queryBuilder.append(" and canceled_cnt between "+countCanceledStart+" and "+countCanceledEnd);
        }


        queryBuilder.append(" order by canceled_cnt desc LIMIT "+((numPage - 1) * pageSize)+","+pageSize);

        LOGGER.info("queryBuilder =" +queryBuilder.toString());

        ClientStatListResponse response = new ClientStatListResponse();

        List<Object> result = session.createSQLQuery(queryBuilder.toString()).list();

        Long totalCount = (Long)session.createSQLQuery("SELECT FOUND_ROWS()").uniqueResult();

        response.setTotalItems(totalCount);
        Long lastPageNumber = ((totalCount / pageSize) + 1);
        response.setLastPageNumber(lastPageNumber);

        Iterator it = result.iterator();
        while(it.hasNext()){
            Object row[] = (Object[])it.next();
            ClientStatsInfo_V2 clientStatsInfo_V2 = new ClientStatsInfo_V2();
            if(row[0]!=null) {
                clientStatsInfo_V2.setId(Long.parseLong(row[0].toString()));
            }else{
                clientStatsInfo_V2.setId(-1);
            }
            if(row[1]!=null) {
                clientStatsInfo_V2.setFirstName(row[1].toString());
            }else{
                clientStatsInfo_V2.setFirstName("");
            }
            if(row[2]!=null) {
                clientStatsInfo_V2.setLastName(row[2].toString());
            }else{
                clientStatsInfo_V2.setLastName("");
            }
            if(row[3]!=null) {
                clientStatsInfo_V2.setEmail(row[3].toString());
            }else{
                clientStatsInfo_V2.setEmail("");
            }
            if(row[4]!=null){
                clientStatsInfo_V2.setPhone(row[4].toString());
            }else{
                clientStatsInfo_V2.setPhone("");
            }
            if(row[5]!=null) {
                clientStatsInfo_V2.setCountCompletedMission(Integer.parseInt(row[5].toString()));
            }else{
                clientStatsInfo_V2.setCountCompletedMission(0);
            }
            if(row[6]!=null) {
                clientStatsInfo_V2.setCountCanceledMission(Integer.parseInt(row[6].toString()));
            }else{
                clientStatsInfo_V2.setCountCanceledMission(0);
            }
            if(row[7]!=null) {
                clientStatsInfo_V2.setCountSentPromoCodes(Integer.parseInt(row[7].toString()));
            }else{
                clientStatsInfo_V2.setCountSentPromoCodes(0);
            }
            if(row[8]!=null) {
                clientStatsInfo_V2.setAmountBalance(Integer.parseInt(row[8].toString())/100);
            }else{
                clientStatsInfo_V2.setAmountBalance(0);
            }
            response.getClientInfoStat().add(clientStatsInfo_V2);
        }


        /*
          With Java 8 steams:
list.stream()
  .skip(page * size)
  .limit(size)
  .collect(Collectors.toCollection(ArrayList::new));
         */

//        int size = subL.size();
//
//        int from = (numPage-1) * pageSize;
//        from = from < size ? from: size;
//
//        int to   = (numPage) * pageSize;
//        to   = to < size ? to : size;
//
//        response.setClientInfoStat(subL.subList(from,to));

        // sort client by count canceled mission
       /*
        Collections.sort(response.getClientInfoStat(), new Comparator<ClientStatsInfo_V2>() {
            @Override
            public int compare(ClientStatsInfo_V2 loc1, ClientStatsInfo_V2 loc2) {
                return new Integer(loc2.getCountCanceledMission()).compareTo(new Integer(loc1.getCountCanceledMission()));
            }
        });
        */
          return response;
    }






    public boolean confirmD(ClientStatsInfo_V2 clientStatsInfo_V2, int countCanceledStart, int countCanceledEnd){
        boolean flag = false;
        if(countCanceledStart!=0 && countCanceledEnd!=0){
            if(clientStatsInfo_V2.getCountCanceledMission()<=countCanceledEnd && clientStatsInfo_V2.getCountCanceledMission()>=countCanceledStart){
                 flag=true;
            }
        }else if(countCanceledStart!=0 && countCanceledEnd==0){
            if(clientStatsInfo_V2.getCountCanceledMission()>=countCanceledStart){
                flag=true;
            }
        }else if(countCanceledStart==0 && countCanceledEnd!=0){
            if(clientStatsInfo_V2.getCountCanceledMission()<=countCanceledEnd){
                flag=true;
            }
        }else if(countCanceledStart==0 && countCanceledEnd==0){
            flag=true;
        }
           return flag;
    }



    /*
    public ArticleListResponse getArticleList(String security_token, long articleId){
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser==null){
            throw new CustomException(1, "Web user not found");
        }
        ArticleListResponse response = new ArticleListResponse();
        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(ArticleAdjustments.class);
        if(articleId!=0){
            criteria.add(Restrictions.eq("id", articleId));
        }
        List <ArticleAdjustments> listRes = criteria.list();
        for(ArticleAdjustments article: listRes){
            response.getArticleInfos().add(ModelsUtils.toModel(article));
        }
        return response;
    }
    */



    public WebUserListResponse getWebUserList(String security_token, long webUserId){
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser==null){
            throw new CustomException(1, "Web user not found");
        }
        WebUserListResponse response= new WebUserListResponse();
        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(WebUser.class);
        if(webUserId!=0){
            criteria.add(Restrictions.eq("id", webUserId));
        }
        List <WebUser> listRes = criteria.list();
        for(WebUser webU: listRes){
            response.getWebUserInfoList().add(ModelsUtils.toModelWebUserInfo(webU));
        }
        return response;
    }








    //f:add
    public ClientsListResponse  getClientInfoListByMask(String security_token, int numPage, int pageSize, String nameMask, String phoneMask, String emailMask, long mainClientId, Boolean onlyMainClient, long clientId, Boolean courierActivated){
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser == null){
            throw new CustomException(1, "Web user not found");
        }
        ClientsListResponse response = new ClientsListResponse();
        List<ClientInfoARM> clientListInfo = new ArrayList();
        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(Client.class);

        if(clientId!=0){
            criteria.add(Restrictions.eq("id", clientId));
        }
        if(nameMask!=null){
            criteria.add(Restrictions.or(Restrictions.ilike("firstName", "%" + nameMask + "%"), Restrictions.ilike("lastName", "%" + nameMask + "%")));
        }
        if(phoneMask!=null){
            criteria.add(Restrictions.ilike("phone", "%" + phoneMask + "%"));
        }
        if(emailMask!=null){
            criteria.add(Restrictions.ilike("email", "%" + emailMask + "%"));
        }
        if(mainClientId!=0){
            criteria.add(Restrictions.eq("mainClient.id", mainClientId));
        }
        if(onlyMainClient!=null){
             if(onlyMainClient){
                 criteria.createAlias("mainClient", "mainClient").add(Restrictions.eqProperty("id", "mainClient.id"));
             }
        }
        if(courierActivated != null){
            criteria.add(Restrictions.eq("courierActivated", courierActivated));
        }


        criteria.addOrder(Order.desc("id"));

        criteria.setProjection(Projections.rowCount());
        Long total = (Long)criteria.uniqueResult();

        criteria.setProjection(null);
        criteria.setResultTransformer(Criteria.ROOT_ENTITY);

        response.setTotalItems(total);

        long lastPageNumber = ((total / pageSize) + 1);
        response.setLastPageNumber(lastPageNumber);

        criteria.setFirstResult((numPage - 1) * pageSize);
        criteria.setMaxResults(pageSize);

        List<Client> listRes = criteria.list();

        for(Client c: listRes){
           ClientInfoARM clientInfoARM = ModelsUtils.toModelClientInfoARM(c, null);
           clientListInfo.add(clientInfoARM);
        }
           response.setClientInfo(clientListInfo);
        return response;
    }



    public ListNewsInVersionResponse listNewsInVersion(String security_token) {
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser == null){
            throw new CustomException(1, "Web user not found");
        }
        ListNewsInVersionResponse response = new ListNewsInVersionResponse();
        // поиск всех новостей в релизах
        Iterable<NewsVersionApp> myIterator = newsVersionAppRepository.findAll();
        for(NewsVersionApp newsVersionApp:myIterator){
          response.getNewsVersionAppInfoList().add(ModelsUtils.toModel(newsVersionApp));
        }
      return response;
    }




    public ArticleAdjustmentsResponse articleAdjustments(String security_token, long articleAdjustmentsId){
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser == null){
            throw new CustomException(1, "Web user not found");
        }
        ArticleAdjustmentsResponse response = new ArticleAdjustmentsResponse();
           if(articleAdjustmentsId==0){
               Iterable<ArticleAdjustments> iterable = articleAdjustmentsRepository.findAll();
               for(ArticleAdjustments articleAdjustments :iterable){
                   ArticleAdjustmentsInfo info = new ArticleAdjustmentsInfo();
                   info.setId(articleAdjustments.getId());
                   info.setName(articleAdjustments.getName());
                   response.getArticleAdjustmentsInfos().add(info);
               }
           }else{
               ArticleAdjustments articleAdjustments = articleAdjustmentsRepository.findOne(articleAdjustmentsId);
                 if(articleAdjustments==null){
                    throw new CustomException(1,"ArticleAdjustments not found");
                 }
               ArticleAdjustmentsInfo info = new ArticleAdjustmentsInfo();
               info.setId(articleAdjustments.getId());
               info.setName(articleAdjustments.getName());
               response.getArticleAdjustmentsInfos().add(info);
           }
         return response;
    }



    public ListVersionAppResponse versionAppList(String security_token, String deviceType){
        ListVersionAppResponse response = new ListVersionAppResponse();

        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser == null){
            throw new CustomException(1, "Web user not found");
        }

          if(!StringUtils.isEmpty(deviceType)){
              // вывод версий по типу устройства (APPLE, ANDROID....)
              List<VersionsApp> versionsAppList = versionsAppRepository.findByClientType(deviceType);
              for(VersionsApp versionsApp: versionsAppList){
                  response.getVersionsAppInfos().add(ModelsUtils.toModel(versionsApp));
              }
          }else{
             // вывод всех существующих версий
              Iterable<VersionsApp> myIterator = versionsAppRepository.findAll();
              List<VersionsApp> versionsAppList = Lists.newArrayList(myIterator);
              for(VersionsApp versionsApp: versionsAppList){
                  response.getVersionsAppInfos().add(ModelsUtils.toModel(versionsApp));
              }
          }
        return response;
    }




    public AssistantResponse assistantList(String security_token){
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser == null){
            throw new CustomException(1, "Web user not found by token = "+security_token);
        }
        AssistantResponse response = new AssistantResponse();
        Iterable<Assistant> myIterator = assistantRepository.findAll();
        for(Assistant assistant: myIterator){
            response.getAssistantInfoList().add(ModelsUtils.toModel(assistant));
        }
        /*
        List<Assistant> assistantList = Lists.newArrayList(myIterator);
        for(Assistant assistant:assistantList){
           response.getAssistantInfoList().add(ModelsUtils.toModel(assistant));
        }
        */
        return response;
    }



//    public NewsListResponse newsList(){
//        NewsListResponse response = new NewsListResponse();
//        Iterable<News> myIterator = newsRepository.findAll();
//        List<News> newsList = Lists.newArrayList(myIterator);
//        for(News news:newsList){
//            response.getNewsInfoList().add(ModelsUtils.toModel(news));
//        }
//        return response;
//    }



    public AssistantUpdateResponse updateAssistant(AssistantInfo assistantInfo){
        AssistantUpdateResponse response = new AssistantUpdateResponse();
        if(assistantInfo.getId()!=null){
            Assistant assistant = assistantRepository.findOne(assistantInfo.getId());
            if(assistant!=null){
                assistant = ModelsUtils.fromModel(assistantInfo, assistant);
                assistantRepository.save(assistant);
                response.getErrorCodeHelper().setErrorCode(0);
                response.getErrorCodeHelper().setErrorMessage("Успешно обновлено");
                response.setAssistantInfo(assistantInfo);
            }else{
                response.getErrorCodeHelper().setErrorCode(1);
                response.getErrorCodeHelper().setErrorMessage("Assistant not found");
            }
        }else{
            Assistant assistant = new Assistant();
            assistant = ModelsUtils.fromModel(assistantInfo, assistant);
            assistantRepository.save(assistant);
            response.getErrorCodeHelper().setErrorCode(0);
            response.getErrorCodeHelper().setErrorMessage("Успешно добавлено");
            response.setAssistantInfo(assistantInfo);
        }
        return response;
    }




    public TurboIncreaseDriverResponse turboIncreaseDriver(TurboIncreaseDriverInfo turboIncreaseDriverInfo){
        TurboIncreaseDriverResponse response = new TurboIncreaseDriverResponse();
        try{
            Mission mission = missionRepository.findOne(turboIncreaseDriverInfo.getMissionId());
            Driver driver = driverRepository.findOne(turboIncreaseDriverInfo.getDriverId());
            TurboIncreaseDriver turboIncreaseDriver = turboIncreaseDriverRepository.findByMissionAndDriver(mission, driver);
                if(turboIncreaseDriverRepository!=null){
                    // update
                      if(mission.getState().equals(Mission.State.TURBO)){
                          /*
                            отправляю событие на нод о том, что водитель изменил стоимость заказа
                          */
                      }else{
                          response.getErrorCodeHelper().setErrorCode(2);
                          response.getErrorCodeHelper().setErrorMessage("Миссия принята или отменена");
                      }
                }else{
                    // insert

                }
        }catch(Exception e){
            e.printStackTrace();
            response.getErrorCodeHelper().setErrorCode(1);
            response.getErrorCodeHelper().setErrorMessage("turboIncreaseDriver exception: "+e.getMessage());
        }
          return response;
    }



    public DeleteNewsResponse deleteDriverNews(String security_token, List<Long> newsIds){
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser == null) {
            throw new CustomException(1,"WebUser not found");
        }
        DeleteNewsResponse response= new DeleteNewsResponse();
        Iterable<News> newsForRemove = newsRepository.findAll(newsIds);
        if(newsForRemove != null){
           newsRepository.delete(newsForRemove);
        }
        return response;
    }






    public UpdateNewsResponse updateNews(NewsInfo newsInfo, String security_token){
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser == null){
            throw new CustomException(1,"WebUser not found");
        }
        UpdateNewsResponse response = new UpdateNewsResponse();
        if(newsInfo.getId()!=null){
            News news = newsRepository.findOne(newsInfo.getId());
            if(news!=null){
                news = ModelsUtils.fromModel(newsInfo, news);
                newsRepository.save(news);
                response.setNewsInfo(newsInfo);
            }else{
                throw new CustomException(2,"News not found");
            }
        }else{
            News news = new News();
            news = ModelsUtils.fromModel(newsInfo, news);
            newsRepository.save(news);
            response.setNewsInfo(newsInfo);
        }
        return response;
    }



    public AssistantFindResponse findAssistant(long assistantId, String security_token){
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser == null){
            throw new CustomException(1,"Web user not found");
        }
        AssistantFindResponse response = new AssistantFindResponse();
        Assistant assistant = assistantRepository.findOne(assistantId);
        if(assistant!=null){
            response.setAssistantInfo(ModelsUtils.toModel(assistant));
        }else{
            throw new CustomException(2,"Assistant not found");
        }
        return response;
    }




    public AssistantDeleteResponse deleteAssistant(long assistantId){
        AssistantDeleteResponse response = new AssistantDeleteResponse();
        Assistant assistant =  assistantRepository.findOne(assistantId);
        if(assistant!=null){
            // перед удалением зануляем у водителей айди автопарков

            List<Driver> driverList  = driverRepository.findByAssistant(assistant);

              for(Driver driver:driverList){
                  driver.setAssistant(null);
                  driverRepository.save(driver);
              }
            assistantRepository.delete(assistant);

            response.getErrorCodeHelper().setErrorCode(0);
            response.getErrorCodeHelper().setErrorMessage("Удалено");
        }else{
            response.getErrorCodeHelper().setErrorCode(1);
            response.getErrorCodeHelper().setErrorMessage("Assistant not found");
        }
        return response;
    }


    // --------------------------------------------------------



    public TaxoparkPartnersResponse findTaxoparkPartnersList(String security_token){
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser == null){
            throw new CustomException(1,"Web user not found");
        }
        TaxoparkPartnersResponse response = new TaxoparkPartnersResponse();
        Iterable<TaxoparkPartners> myIterator = taxoparkPartnersRepository.findAll();
              for(TaxoparkPartners taxoparkPartners: myIterator){
                  response.getTaxoparkPartnersInfoList().add(ModelsUtils.toModel(taxoparkPartners));
              }
            return response;
    }






    public UpdateArticleAdjustmentsResponse updateArticleAdjustments(String security_token, ArticleAdjustmentsInfo articleAdjustmentsInfo){
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser == null){
            throw new CustomException(1,"Web user not found");
        }
        if(!EnumSet.of(AdministratorRole.DISPATCHER, AdministratorRole.ADMIN).contains(webUser.getRole())){
            throw new CustomException(2,"Permission denied");
        }
        UpdateArticleAdjustmentsResponse response = new UpdateArticleAdjustmentsResponse();

        if(articleAdjustmentsInfo.getId()==0){
            // добавление
            ArticleAdjustments article = new ArticleAdjustments();
            article.setName(articleAdjustmentsInfo.getName());
            articleAdjustmentsRepository.save(article);
        }else{
            // обновление
            ArticleAdjustments articleAdjustments = articleAdjustmentsRepository.findOne(articleAdjustmentsInfo.getId());
            if (articleAdjustments==null){
                throw new CustomException(4,"ArticleAdjustments not found");
            }
            articleAdjustments.setName(articleAdjustmentsInfo.getName());
            articleAdjustmentsRepository.save(articleAdjustments);
        }
        return response;
    }




    public UpdateNewsInVersionResponse updateNewsInVersion(String security_token, NewsVersionAppInfo newsVersionAppInfo){

        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser == null){
            throw new CustomException(1,"Web user not found");
        }
        if(!EnumSet.of(AdministratorRole.DISPATCHER, AdministratorRole.ADMIN).contains(webUser.getRole())){
            throw new CustomException(2,"Permission denied");
        }
        UpdateNewsInVersionResponse response = new UpdateNewsInVersionResponse();
        VersionsApp versionsApp = versionsAppRepository.findOne(newsVersionAppInfo.getVersionsAppInfo().getId());

        if(versionsApp==null){
            throw new CustomException(4,"Version app not found");
        }

        if(newsVersionAppInfo.getId()==0){
              // добавление новости для версии
              NewsVersionApp newsVersionApp = new NewsVersionApp();
              newsVersionAppRepository.save(ModelsUtils.fromModel(newsVersionAppInfo, newsVersionApp, versionsApp));
        }else{
              // обновление
              NewsVersionApp newsVersionApp = newsVersionAppRepository.findOne(newsVersionAppInfo.getId());
              if(newsVersionApp == null){
                  throw new CustomException(5,"News not found");
              }
              newsVersionAppRepository.save(ModelsUtils.fromModel(newsVersionAppInfo, newsVersionApp, versionsApp));
        }
          return response;
    }




    public TaxoparkPartnersUpdateResponse updateTaxoparkPartner(TaxoparkPartnersInfo taxoparkPartnersInfo, String security_token){
           WebUser webUser = webUserRepository.findByToken(security_token);
           if(webUser == null){
               throw new CustomException(1,"Web user not found");
           }
           TaxoparkPartnersUpdateResponse response = new TaxoparkPartnersUpdateResponse();

           TaxoparkPartners taxoparkPartners;

                 if(taxoparkPartnersInfo.getId() != 0){
                     taxoparkPartners = taxoparkPartnersRepository.findOne(taxoparkPartnersInfo.getId());

                     if(taxoparkPartners == null) {
                         throw new CustomException(2,"Taxopark not found");
                     }
                         taxoparkPartners = ModelsUtils.fromModel(taxoparkPartnersInfo, taxoparkPartners);
                         taxoparkPartnersRepository.save(taxoparkPartners);

                         response.getErrorCodeHelper().setErrorMessage("Успешно обновлено");
                         response.setTaxoparkPartnersInfo(taxoparkPartnersInfo);

                 }else{
                         taxoparkPartners = ModelsUtils.fromModel(taxoparkPartnersInfo, null);
                         taxoparkPartnersRepository.save(taxoparkPartners);

                         response.getErrorCodeHelper().setErrorMessage("Успешно добавлено");
                         response.setTaxoparkPartnersInfo(taxoparkPartnersInfo);
                 }
             return response;
    }



    public TaxoparkPartnersFindResponse findTaxoparkPartner(long taxoparkId, String security_token){
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser == null){
            throw new CustomException(1, "Web user not found");
        }
        TaxoparkPartnersFindResponse response = new TaxoparkPartnersFindResponse();
        TaxoparkPartners taxoparkPartners = taxoparkPartnersRepository.findOne(taxoparkId);
              if(taxoparkPartners == null){
                  throw new CustomException(2, "Taxopark not found");
              }
                  response.setTaxoparkPartnersInfo(ModelsUtils.toModel(taxoparkPartners));
                  return response;
    }




        public TaxoparkPartnersDeleteResponse deleteTaxoparkPartner(long taxoparkId, String security_token){
            WebUser webUser = webUserRepository.findByToken(security_token);
            if(webUser == null){
                throw new CustomException(1, "Web user not found");
            }
             TaxoparkPartnersDeleteResponse response = new TaxoparkPartnersDeleteResponse();
             TaxoparkPartners taxoparkPartners =  taxoparkPartnersRepository.findOne(taxoparkId);
                   if(taxoparkPartners!=null){
                       // перед удалением зануляем у водителей айди автопарков
                       taxoparkPartnersRepository.delete(taxoparkPartners);

                       List<Driver> driverByTaxopark = driverRepository.findByTaxoparkPartners(taxoparkPartners);
                          for(Driver driver: driverByTaxopark){
                              driver.setTaxoparkPartners(null);
                          }
                              driverRepository.save(driverByTaxopark);

                              /* удалить содержимое Taxopark cash flow */

                              /* log */
                              mongoDBServices.createEvent(3, ""+webUser.getId(), 3, 0, "deleteTaxoparkPartner", "", "");
                              //mongoDBServices.createEvent(3, ""+webUser.getId(), 3, "deleteTaxoparkPartner", "", "", 0, 0);
                   }else{
                       response.getErrorCodeHelper().setErrorCode(1);
                       response.getErrorCodeHelper().setErrorMessage("Taxopark not found");
                   }
              return response;
        }






    public DriversListResponse getDriverInfoListByMask(String security_token, int numPage, int pageSize, String nameMask, String phoneMask, String carModelMask, String carNumberMask, long assistantId, String versionApp, String login, String autoClass, String blockStatus, Integer typeSalary, Integer salaryPriority, long driverId, Boolean typeX, Boolean active, Boolean courier, Boolean pedestrian, List<String> driverTypes){
        DriversListResponse response = new DriversListResponse();
        WebUser webUser = webUserRepository.findByToken(security_token);
           if(webUser==null){
               return response;
           }
        List<DriverSetting.DriverType> driverTypeList = new ArrayList<>();
        List<DriverInfoARM> driverListInfoARM = new ArrayList();
        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(Driver.class);

        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        if(driverId != 0){
            criteria.add(Restrictions.eq("id", driverId));
        }
        if(webUser.getTaxoparkId()!=null){
            criteria.add(Restrictions.eq("taxoparkPartners.id", webUser.getTaxoparkId()));
        }

        if(nameMask != null){
            criteria.add(Restrictions.or(Restrictions.ilike("firstName", "%" + nameMask + "%"), Restrictions.ilike("lastName", "%" + nameMask + "%")));
        }
        if(phoneMask != null){
            criteria.add(Restrictions.ilike("phone", "%" + phoneMask + "%"));
        }
        if(carModelMask != null){
            criteria.add(Restrictions.ilike("autoModel", "%" + carModelMask + "%"));
        }
        if(carNumberMask != null){
            criteria.add(Restrictions.ilike("autoNumber", "%" + carNumberMask + "%"));
        }
        if(assistantId != 0){
            criteria.add(Restrictions.eq("assistant", assistantRepository.findOne(assistantId)));
        }
        if(versionApp != null){
            criteria.add(Restrictions.ilike("versionApp", "%" + versionApp + "%"));
        }
        if(login != null){
            criteria.add(Restrictions.ilike("login", "%" + login + "%"));
        }
        if(autoClass != null){
            criteria.add(Restrictions.eq("autoClass", AutoClass.valueOf(autoClass)));
        }
        if(typeX != null){
            criteria.add(Restrictions.eq("typeX", typeX));
        }

        if(active!=null){
            Driver.State state;
            if(active){
                state = Driver.State.AVAILABLE;
            }else{
                state = Driver.State.OFFLINE;
            }
            criteria.add(Restrictions.eq("state", state));
        }
        if(blockStatus!=null){
            criteria.add(Restrictions.eq("administrativeStatus", Driver.AdministrativeStatus.valueOf(blockStatus)));
        }
        if(typeSalary != null && salaryPriority != null){
            criteria.createAlias("driverRequisites", "driverRequisites");
            criteria.add(Restrictions.eq("driverRequisites.active", true));
            criteria.add(Restrictions.eq("driverRequisites.typeSalary", typeSalary));
            criteria.add(Restrictions.eq("driverRequisites.salaryPriority", salaryPriority));
            criteria.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
        }else if(typeSalary != null && salaryPriority == null){
            criteria.createAlias("driverRequisites", "driverRequisites");
            criteria.add(Restrictions.eq("driverRequisites.active", true));
            criteria.add(Restrictions.eq("driverRequisites.typeSalary", typeSalary));
            criteria.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
        }else if(typeSalary == null && salaryPriority != null){
            criteria.createAlias("driverRequisites", "driverRequisites");
            criteria.add(Restrictions.eq("driverRequisites.active", true));
            criteria.add(Restrictions.eq("driverRequisites.salaryPriority", salaryPriority));
            criteria.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);

        }
        if(!CollectionUtils.isEmpty(driverTypes)){
            for(String driverType: driverTypes){
                driverTypeList.add(DriverSetting.DriverType.valueOf(driverType));
            }

            criteria.createAlias("driverSetting", "driverSetting");
            criteria.add(Restrictions.in("driverSetting.driverType", driverTypeList));
            if(pedestrian != null){
                criteria.add(Restrictions.eq("driverSetting.pedestrian", pedestrian));
            }
        }

        criteria.addOrder(Order.desc("id"));

        criteria.setProjection(Projections.rowCount());
        Long count = (Long)criteria.uniqueResult();

        criteria.setProjection(null);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        response.setTotalItems(count);

        Long lastPageNumber = ((count / pageSize) + 1);
        response.setLastPageNumber(lastPageNumber);

        criteria.setFirstResult((numPage - 1) * pageSize);
        criteria.setMaxResults(pageSize);

        List<Driver> listRes = criteria.list();

        DriverInfoARM infoARM;
        for(Driver driver: listRes){
           DriverLocation driverLocation = locationRepository.findByDriver(driver);
           infoARM = ModelsUtils.toModelARM(driver);
            if(driverLocation!=null && driverLocation.getLocation()!=null){
                infoARM.setItemLocation(ModelsUtils.toModel(driverLocation.getLocation()));
            }
            driverListInfoARM.add(infoARM);
        }
         response.setDriverInfoARM(driverListInfoARM);
            return response;
    }





    /*
    //f:add
    public DriversListResponse getDriverInfoListByMask_new(int countItems, int fromPosition, String nameMask, String phoneMask, String carModelMask, String carNumberMask){
        DriversListResponse response = new DriversListResponse();
        List<DriverInfo> driverListInfo = new ArrayList();
        Session session = em.unwrap(Session.class);

        Criteria criteria = session.createCriteria(Driver.class);
        criteria.setFirstResult(fromPosition);
        criteria.setMaxResults(countItems);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

//        LOGGER.info("pageSize = "+pageSize);
//        LOGGER.info("pageNumber = "+pageNumber);

//        criteria.setFirstResult((pageNumber - 1) * pageSize);
//        criteria.setMaxResults(pageSize);

        List<String> params = new ArrayList<String>();
        StringBuilder sqlBuilder = new StringBuilder()
                .append("select * ")
                .append("from driver ")
                .append("where 1=1 ");

        if (nameMask != null) {
            sqlBuilder.append("and first_name like ? ");
            params.add(nameMask + "%");
        }
        if(phoneMask!=null){
            sqlBuilder.append("and phone like ? ");
            params.add(phoneMask + "%");
        }
        if(carModelMask!=null){
            sqlBuilder.append("and auto_model like ? ");
            params.add(carModelMask + "%");
        }
        if(carNumberMask!=null){
            sqlBuilder.append("and auto_number like ? ");
            params.add(carNumberMask + "%");
        }
        sqlBuilder.append(" limit ");
        String sql = sqlBuilder.toString();
        LOGGER.info("sql="+sql);

        if(nameMask!=null){
            Criterion name = Restrictions.ilike("firstName", "%" + nameMask + "%");
            criteria.add(name);
        }
        if(phoneMask!=null){
            Criterion pMask = Restrictions.ilike("phone", "%" + phoneMask + "%");
            criteria.add(pMask);
        }
        if(carModelMask!=null){
            Criterion carMMask = Restrictions.ilike("autoModel", "%" + carModelMask + "%");
            criteria.add(carMMask);
        }
        if(carNumberMask!=null){
            Criterion carNumMask = Restrictions.ilike("autoNumber", "%" + carNumberMask + "%");
            criteria.add(carNumMask);
        }

        List <Driver> listRes = criteria.list();

        for(Driver driver2: listRes){
            driverListInfo.add(ModelsUtils.toModel(driver2));
        }
        //response.setTotalItems(listRes.size());
        response.setDriverInfo(driverListInfo);

            return response;
    }
*/





    // LogoutResponse.Details
    public LogoutResponse logoutClient(long clientId, boolean force, DeviceInfoModel deviceInfo) {
        LogoutResponse response = new LogoutResponse();
        //LogoutResponse.Details result = new LogoutResponse.Details();
        Client client = clientRepository.findOne(clientId);
        if (client != null) {

                    Mission mission = client.getMission();
                    if (mission != null) {
                        // f:remove
                        //cancelMissionByClient(mission.getId(), "logout", 1,client.getId(), token);
                        // f:add:start
                           if(mission.getState().equals(Mission.State.NEW)){
                               Driver driver = mission.getDriverInfo();
                               if (driver != null) {
                                   driver.setCurrentMission(null);
                                   driverRepository.save(driver);
                                   // отослать всем событие о том, что миссия отканселилась
                                   JSONObject json = new JSONObject();
                                   try {
                                       json.put("missionId", mission.getId());
                                       nodeJsService.notified("mission_canceled_by_server", json);
                                   } catch (JSONException e) {
                                       e.printStackTrace();
                                   }
                               }
                               mission.setState(Mission.State.CANCELED);
                               String comment = "logout";
                               comment = comment + "\n" + "Прекращена. Причина: " + 1;
                               mission.setComments(comment);

                               clearClientMission(mission);
                               mission = missionRepository.save(mission);
                               response.getDetails().setCompleted(true);
                               // f:add:end
                               //missionRepository.findByClientInfo(client);
                           }
                    }
                    devicesService.unregister(deviceInfo);
                    client.setAdministrativeState(Client.State.INACTIVE);
                    clientRepository.save(client);
                    //result.setCompleted(true);

                    response.getErrorCodeHelper().setErrorCode(0);
                    response.getErrorCodeHelper().setErrorMessage("Logout successfully");

        }else{
            response.getErrorCodeHelper().setErrorCode(1);
            response.getErrorCodeHelper().setErrorMessage("Client not found");
        }
        return response;
    }




    private boolean canLogout(long driverId) {
        Driver driver = driverRepository.findOne(driverId);
        return (driver.getCurrentMission() != null);
    }




    private boolean canCancelMission(long missionId) {
        boolean result = false;
        Mission mission = missionRepository.findOne(missionId);
        EnumSet<Mission.State> possibleForCancel = EnumSet.allOf(Mission.State.class);
        if (possibleForCancel.contains(mission.getState())) {
            result = true;
        }
        return result;
    }



    public void driverLate(long driverId, int time, String security_token) {
        Driver driver = driverRepository.findOne(driverId);
        //if(commonService.isOkSecurityToken(driver, security_token, 2)){
            if (driver != null) {
                Mission mission = driver.getCurrentMission();
                mission.getExpectedArrivalTimes().add(time);
                missionRepository.save(mission);
                //notificationsService.driverLate(driverId, time);
            }
        //}
    }


    public void declineMission(long missionId, long driverId, String security_token) {
        Mission mission = missionRepository.findOne(missionId);
        Driver driver = driverRepository.findOne(driverId);

        //if(commonService.isOkSecurityToken(driver, security_token, 2)){
            if (mission != null && driver != null) {
                // mission.getDeclinedDrivers().add(driver);
                // maybe: mission.setDriverInfo(null); ?  - to investigate..
                missionRepository.save(mission);
            }
        //}
    }

    public void deleteTest() {
        ArrayList<Mission> missions = new ArrayList<>();
        Mission mission = new Mission();
        Mission mission1 = missionRepository.save(mission);
        missions.add(mission1);
        missionRepository.delete(missions);
    }





    // f:add
    public BookedDetails onlyBookedMissionsDriver(long driverId) {
        BookedDetails result = new BookedDetails();
        Driver driver = driverRepository.findOne(driverId);
        if (driver != null) {
            List<Mission> bookingMy = new ArrayList<>(driver.getBookedMissions());
            result.setBookedToMe(bookingMy.size());
            addBooked(result, bookingMy, true);
        }
        return result;
    }



    public BookedDetails bookedMissionsDriver(long driverId) {
        Driver driver = driverRepository.findOne(driverId);
        BookedDetails result = new BookedDetails();
           List<Mission> bookingNew = missionRepository.findByStateAndBookingStateAndDriverInfoIsNullOrderByTimeOfStartingAsc(Mission.State.BOOKED, Mission.BookingState.WAITING);
            for (ListIterator<Mission> i = bookingNew.listIterator(); i.hasNext(); ) {
                Mission mission = i.next();
                   switch(mission.getAutoClass().getValue()){
                       case 1:{
                           break;
                       }
                       case 2:{
                           if(driver.getAutoClass().getValue() == 1) {
                               // класс авто водителя стандарт, а класс миссии комфорт - удаляем
                               i.remove();
                           }
                           break;
                       }
                       case 3:{
                           if(driver.getAutoClass().getValue() == 1 || driver.getAutoClass().getValue() == 2) {
                               // класс авто водителя стандарт или комфорт, а класс миссии бизесс - удаляем
                               i.remove();
                           }
                           break;
                       }
                       default: break;
                   }
            }

           List<Mission> bookingMy =  missionRepository.findByStateAndBookingStateNotAndBookedDriverIdOrderByTimeOfStartingAsc(Mission.State.BOOKED, Mission.BookingState.NONE, driver); // waiting booked driver

           result.setBookedNew(bookingNew.size());
           result.setBookedToMe(bookingMy.size());

           addBooked(result, bookingNew, false);
           addBooked(result, bookingMy, true);
        return result;
    }





    private void addBooked(BookedDetails result, List<Mission> bookingNew, boolean booked) {
        for (Mission mission : bookingNew) {
            MissionInfo model = ModelsUtils.toModel(mission);
            model.setBooked(booked);
            result.getMissions().add(model);
        }
    }

//    public BookedDetails bookedMissionsClient(long driverId) {
//        BookedDetails result = new BookedDetails();
//        Driver driver = driverRepository.findOne(driverId);
//        if (driver != null) {
//            List<Mission> bookingNew = missionRepository.findByStateAndBookingStateOrderByTimeOfStartingAsc(Mission.State.BOOKED, Mission.BookingState.WAITING);
//            List<Mission> bookingMy = new ArrayList<>();
//            bookingMy.addAll(driver.getBookedMissions());
//            List<Mission> booking = new ArrayList<>();
//            booking.addAll(bookingMy);
//            booking.addAll(bookingNew);
//            result.setBookedNew(bookingNew.size());
//            result.setBookedToMe(bookingMy.size());
//
//            for (Mission mission : booking) {
//                result.getMissions().add(ModelsUtils.toModel(mission));
//            }
//        }
//        return result;
//    }



    public CheckVersionResponse checkVersion(String version, String clientType){
        CheckVersionResponse response = new CheckVersionResponse();
          VersionsApp versionsApp = versionsAppRepository.findByVersionAndClientType(version, clientType);
                if(versionsApp==null){
                    // пользователь с такой версией и типом устройства не найден
                    response.getErrorCodeHelper().setErrorCode(-1);
                    response.getErrorCodeHelper().setErrorMessage("Критически старая версия");
                }else {
                        if(versionsApp.getStatusVersion()==-1){
                            // очень древняя версия приложения
                            response.getErrorCodeHelper().setErrorCode(-1);
                            response.getErrorCodeHelper().setErrorMessage("Критически старая версия");
                        }else if(versionsApp.getStatusVersion()==1){
                             response.getErrorCodeHelper().setErrorCode(0);
                             response.getErrorCodeHelper().setErrorMessage("Актуальная версия");
                        }else if(versionsApp.getStatusVersion()==0){
                             response.getErrorCodeHelper().setErrorCode(1);
                             response.getErrorCodeHelper().setErrorMessage("Желательно обновится");
                        }
                }
            return response;
    }



    // посмотреть новости по релизу для конкретного типа устройства
    public CheckNewsInVersionResponse checkNewsInVersion(String version, String clientType){
        CheckNewsInVersionResponse response = new CheckNewsInVersionResponse();
          VersionsApp versionsApp = versionsAppRepository.findByVersionAndClientType(version, clientType);
               if(versionsApp == null){
                  throw new CustomException(1,"Пользователь с данной версией приложения и типом устройства не найден");
               }
          List<NewsVersionApp> newsVersionApps = newsVersionAppRepository.findByVersionsAppAndActive(versionsApp, Boolean.TRUE);
            if(newsVersionApps!=null && !newsVersionApps.isEmpty()){
                   for(NewsVersionApp newsVersionApp: newsVersionApps){
                       response.getNewsVersionAppInfo().add(ModelsUtils.toModel(newsVersionApp));
                   }
            }
               return response;
    }




 public List<Mission> getActiveMissionList(MultipleMission multipleMission){
        List<Mission> activeMissionList = new ArrayList<>();
         if(multipleMission != null){

             Set<Mission> missions = (multipleMissionRepository.findOne(multipleMission.getId())).getMultipleMissions();//multipleMission.getMultipleMissions();
             if(!CollectionUtils.isEmpty(missions)){
                 for(Mission mission: missions){
                     if(!EnumSet.of(Mission.State.COMPLETED, Mission.State.CANCELED).contains(mission.getState())){
                         // миссия активна
                         activeMissionList.add(mission);
                     }
                 }
             }
         }
        return activeMissionList;
 }



    public ServerStateInfoV2 resolveStateForClientWithMultipleMissionSupport(Client clientInfo) {
        ServerStateInfoV2 result;
        ServerState state = ServerState.NOT_LOGGED_IN;
        Mission mission = null;
        List<Mission> activeMissionList;
        List<MissionInfo> activeMissionInfoList = null;
        MissionInfo missionInfo = null;

        state = ServerState.DEFAULT;
        long lastStartTime = 0;
        PaymentInfo paymentInfo = new PaymentInfo();

        activeMissionList = getActiveMissionList(clientInfo.getMultipleMission());

        if(clientInfo.getMultipleMission()!=null && CollectionUtils.isEmpty(activeMissionList)){
            // на клиенте есть мультизаказ все миссии которого COMPLETED или CANCELED - очищаем multiple_id
            clientInfo.setMultipleMission(null);
            clientRepository.save(clientInfo);
            result = new ServerStateInfoV2(state.getStateId(), missionInfo, paymentInfo, lastStartTime, activeMissionInfoList);
              return result;
        }


        if (!CollectionUtils.isEmpty(activeMissionList)){
            // показываем клиенту ту миссию, которая была выбрана у него в момент когда он потушил приложение
            if(clientInfo.getMission()==null){
                mission = activeMissionList.get(0);
                mission = missionRepository.findOne(mission.getId());
                clientInfo.setMission(mission);
                clientRepository.save(clientInfo);
            }else{
                mission = clientInfo.getMission();
                mission = missionRepository.findOne(mission.getId());
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
                int totalTimePausesInMinute = 0;
                paymentInfo.setPausesTime(totalTimePausesInMinute);
            } else if (Mission.State.IN_TRIP_END.equals(mission.getState())){
                paymentInfo = missionFinished(10, mission.getId());
                state = ServerState.TRIP_FINISHED;
                paymentInfo.setWaitingOverFree(mission.getStatistics().getOverWaitingTime());
                int totalTimePausesInMinute = 0;
                paymentInfo.setPausesTime(totalTimePausesInMinute);
            }
        }


        if(mission!=null){
            missionInfo =  ModelsUtils.toModel(mission);
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


        //  ServerStateInfo -  сюда добавить еще список других актуальных миссий (List<MissionInfo>)
        if(!CollectionUtils.isEmpty(activeMissionList)){
            activeMissionInfoList = new ArrayList<>();
            for(Mission miss: activeMissionList){
                activeMissionInfoList.add(ModelsUtils.toModel(miss));
            }
        }

        result = new ServerStateInfoV2(state.getStateId(), missionInfo, paymentInfo, lastStartTime, activeMissionInfoList);
           return result;
    }




    public PaymentInfo missionFinished(int distanceInFact, long missionId) {
        Mission mission = missionRepository.findOne(missionId);
        DateTime timeOfFinishing = timeService.nowDateTime();
        //mission.setState(Mission.State.IN_TRIP_END); убрал по просьбе Вали
        mission.setTimeOfFinishing(timeOfFinishing);
        mission.getStatistics().setDistanceInFact(distanceInFact);
        mission = missionRepository.save(mission);
        //notificationsService.missionEnd(missionId);
        return calculateMissionPrice(mission);
    }

    public PaymentInfo calculateMissionPrice(Mission mission) {
        PaymentInfo result = new PaymentInfo();
        if (mission != null) {
            MissionRate missionRate = mission.getMissionRate();
            Money total = MoneyUtils.getRubles(0);
            // f:remove
            //total.plus(missionRate.getPriceForAuto(mission.getAutoClass()).multipliedBy(mission.getStatistics().getDistanceInFact()));
            //total.plus(missionRate.getPriceMinimal());

            for (MissionService service : mission.getStatistics().getServicesExpected()) {
//                total.plus(missionRate.getPriceForService(service));
            }

            total.plus(missionRate.getPriceStop().multipliedBy(mission.getStatistics().getPauses().size()));

            int pausesTime = 0;

            for (Mission.PauseInfo pauseInfo : mission.getStatistics().getPauses()) {
                pausesTime += Minutes.minutesBetween(pauseInfo.getStartPause(), pauseInfo.getEndPause()).getMinutes();
            }
            total.plus(missionRate.getPriceWaitingMinute().multipliedBy(pausesTime));

            total.plus(missionRate.getPriceStop().multipliedBy(mission.getStatistics().getPauses().size()));

            result.setTotalPrice(total.getAmount().doubleValue());
            result.setBonusesAmount(mission.getClientInfo().getAccount().getBonuses().getAmount().doubleValue());
            result.setPausesCount(mission.getStatistics().getPauses().size());
            int missionTime = Minutes.minutesBetween(mission.getTimeOfArriving(), mission.getTimeOfFinishing()).getMinutes();
            result.setMissionTime(missionTime);
            result.setPausesTime(pausesTime);
            result.setUseBonuses(mission.getStatistics().isUseBonuses());
            result.setDistanceExpected(mission.getStatistics().getDistanceExpected());
            result.setDistanceInFact(mission.getStatistics().getDistanceInFact());
        }
        return result;
    }



    public ServerStateInfo resolveState(long clientId, long driverId) {
        ServerStateInfo result;
        ServerState state = ServerState.NOT_LOGGED_IN;
        Mission mission = null;
        Driver driverInfo = null;
        Client clientInfo = null;

        if (clientId != 0){
            state = ServerState.DEFAULT;
            clientInfo = clientRepository.findOne(clientId);
            if (clientInfo != null){
                mission = clientInfo.getMission();
            } else {
                LOGGER.error("Client with id: {} not found!", clientId);
            }
        } else if (driverId != 0){
            state = ServerState.DEFAULT;
            driverInfo = driverRepository.findOne(driverId);
            if (driverInfo != null){
                mission = driverInfo.getCurrentMission();
                if(driverInfo.getState().equals(Driver.State.BUSY)){
                    state = ServerState.DRIVER_BUSY;
                }
            } else {
                LOGGER.error("Driver with id: {} not found!", driverId);
            }
        }
        long lastStartTime = 0;
        PaymentInfo paymentInfo = new PaymentInfo();

        if (mission != null){
            List<Mission.PauseInfo> pauses = mission.getStatistics().getPauses();
            if (!pauses.isEmpty()){
                Mission.PauseInfo pauseInfo = pauses.get(pauses.size() - 1);
                if (pauseInfo.getEndPause() == null){
                    // pauseInfo
                    //lastStartTime = timeService.nowDateTimeMillis();
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
                //paymentInfo.setWaitingOverFree(mission.getStatistics().getOverWaitingTime());

                paymentInfo.setPausesCount(pauses.size());
                int totalTimePausesInMinute = 0;

                for(Mission.PauseInfo pauseInfo: pauses){
                    DateTime start =  pauseInfo.getStartPause();
                    DateTime end = pauseInfo.getEndPause();
                        if(end!=null){
                            //Minutes minutes = Minutes.minutesBetween(start, end);
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
                paymentInfo.setPausesTime(totalTimePausesInMinute);
            } else if (Mission.State.IN_TRIP_END.equals(mission.getState())){
                paymentInfo = missionFinished(10, mission.getId());
                state = ServerState.TRIP_FINISHED;
                paymentInfo.setWaitingOverFree(mission.getStatistics().getOverWaitingTime());
                paymentInfo.setPausesCount(pauses.size());
                int totalTimePausesInMinute = 0;
                    for(Mission.PauseInfo pauseInfo: pauses){
                           DateTime start =  pauseInfo.getStartPause();
                           DateTime end = pauseInfo.getEndPause();
                           Minutes minutes = Minutes.minutesBetween(start, end);
                           totalTimePausesInMinute+= minutes.getMinutes();
                    }
                           paymentInfo.setPausesTime(totalTimePausesInMinute);
            } else if(EnumSet.of(Mission.State.COMPLETED, Mission.State.CANCELED).contains(mission.getState())){
                if (clientInfo != null){
                    clientInfo.setMission(null);
                    clientRepository.save(clientInfo);
                } else if (driverInfo != null){
                    driverInfo.setCurrentMission(null);
                    driverRepository.save(driverInfo);
                }
            }
        }

        MissionInfo missionInfo = null;
        if(mission!=null){
             missionInfo =  ModelsUtils.toModel(mission);

             /* отдавать единые номера  */
             missionInfo = (MissionInfo)commonService.commonPhoneNumber(missionInfo);

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
        result = new ServerStateInfo(state.getStateId(), missionInfo, paymentInfo, lastStartTime); // mission != null ? ModelsUtils.toModel(mission) : null
           return result;
    }


/*
    public DriverPeriodWorkResponse driverPeriodWork(String security_token, long driverId, long startTime, long endTime, boolean active){
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser == null){
            throw new CustomException(1,"Web user not found");
        }
        DriverPeriodWorkResponse response = new DriverPeriodWorkResponse();
        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(DriverPeriodWork.class);

        if(driverId!=0){
            criteria.add(Restrictions.eq("driver.id", driverId));
        }
        if(startTime!=0 || endTime!=0){
            criteria.add(Restrictions.ge("startWork", new DateTime(new Date(startTime * 1000))));
            criteria.add(Restrictions.lt("startWork", new DateTime(new Date(endTime * 1000))));
        }
        criteria.add(Restrictions.eq("active", active));
        criteria.addOrder(Order.desc("startWork"));

        List<DriverPeriodWork> listDriverPeriodWork = criteria.list();

        for(DriverPeriodWork driverPeriodWork: listDriverPeriodWork){
            response.getDriverPeriodWorkInfos().add(ModelsUtils.toModelDriverPeriodWorkInfo(driverPeriodWork));
        }
        return response;
    }
     */




    // выдать список новостей для водителей
    public NewsResponse driverNews(String security_token, long startTime, long endTime, long newsId){
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser == null){
            throw new CustomException(1,"Web user not found");
        }
        NewsResponse response = new NewsResponse();
        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(News.class);

        if(newsId!=0){
            criteria.add(Restrictions.eq("id", newsId));
        }
        criteria = QueryUtils.fillDateTimeParameter(criteria, "timeOfStarting", startTime, endTime);
        /*
        if(startTime != 0 && endTime != 0){
            criteria.add(Restrictions.ge("timeOfStarting", new DateTime(new Date(startTime))));
            criteria.add(Restrictions.lt("timeOfStarting", new DateTime(new Date(endTime))));
        }else if(startTime != 0 && endTime == 0){
            criteria.add(Restrictions.ge("timeOfStarting", new DateTime(new Date(startTime))));
        }else if(startTime == 0 && endTime != 0){
            criteria.add(Restrictions.lt("timeOfStarting", new DateTime(new Date(endTime))));
        }
        */

        criteria.addOrder(Order.desc("timeOfStarting"));
        List<News> listNews = criteria.list();
                for (News news : listNews) {
                    response.getNewsInfos().add(ModelsUtils.toModel(news));
                }
        return response;
    }





    @Transactional
    public DriverNewPhoneResponse updatePhoneDriver(long driverId, String newPhone) {
        DriverNewPhoneResponse response = new DriverNewPhoneResponse();
        Driver driverByPhone = driverRepository.findByPhone(newPhone);
        Driver driverById = driverRepository.findOne(driverId);

        if(driverByPhone==null){
              if(driverById!=null){
                  String sms = generateSMS();
                  driverById.setSmsCode(sms);
                  serviceSMSNotification.phoneDriverUpdate(driverById.getPhone(), sms, "");
                  driverRepository.save(driverById);
                  response.setSendCode(true);
                  response.setPhone(newPhone);
              }
        }
        return response;
    }









    public void cancelMissionByNode(Long clientId){
        if (clientId != null){
            Client client = clientRepository.findOne(clientId);
                  if(client!=null){
                       List<Mission> missionList = missionRepository.findByClientInfoAndState(client, Mission.State.NEW);
                        if(missionList!=null && !missionList.isEmpty()){
                            client.setMission(null);
                            clientRepository.save(client);
                            for(Mission mission: missionList){
                                    Driver driver = mission.getDriverInfo();
                                    if(driver!=null){
                                        driver.setCurrentMission(null);
                                        driverRepository.save(driver);
                                    }
                                JSONObject json = new JSONObject();
                                try {
                                    json.put("missionId", mission.getId());
                                    nodeJsService.notified("mission_canceled_by_server",json);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                    mission.setState(Mission.State.CANCELED);
                                    missionRepository.save(mission);
                            }
                        }
                  }
         }
    }



    @Transactional
    public DriverConfirmationPhoneResponse confirmationPhoneDriver(long driverId, String smsCode, String newPhone) {
        DriverConfirmationPhoneResponse response = new DriverConfirmationPhoneResponse();
        Driver driver = driverRepository.findOne(driverId);
        if(driver!=null){
              if(driver.getSmsCode().equals(smsCode)){
                  driver.setPhone(newPhone);
                  driver.setSmsCode(null);
                  driverRepository.save(driver);
                  response.setUpdatePhone(true);
              }
        }
        return response;
    }




    public ClientByPromoResponse clientByPromo(String security_token, String promoCode){
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser == null){
            throw new CustomException(1, "Web user not found");
        }
        ClientByPromoResponse response = new ClientByPromoResponse();
        PromoCodes promoCodes = promoCodeRepository.findByPromoCode(promoCode);
        if(promoCodes == null){
            throw new CustomException(2, "Promo code not found");
        }
        Long toId = promoCodes.getToId();
        if(toId == null){
            throw new CustomException(4, "Promo code is not activated");
        }
        Client client = clientRepository.findOne(toId);
        if(client == null){
            throw new CustomException(5, "Client by TO_ID not found");
        }
                ClientInfoShort clientInfoShort =  new ClientInfoShort();
                clientInfoShort.setFirstName(client.getFirstName());
                clientInfoShort.setLastName(client.getLastName());
                clientInfoShort.setEmail(client.getEmail());
                clientInfoShort.setPhone(client.getPhone());
                clientInfoShort.setClientId(client.getId());
                  response.getClientInfoShortList().add(clientInfoShort);
          return response;
    }



    public void sendEmailAllClient(String resultHtml, String subject){
        Iterable<Client> iterable = clientRepository.findAll();
        for(Client client: iterable){
             if(client.getEmail()!=null && !client.getEmail().isEmpty()){
                 SendEmailUtil.sendEmail(client.getEmail(), resultHtml, subject);
             }
        }
    }



    public void sendEmailForClient(List<Long> clientIds, int typeEmail, String subject, String url) throws Exception{
        //String subject = "Вам подарок от Таксисто )))"; //SendHTTPQueryUtil.senPostQuery("http://www.taxisto.ru/mail/lowcost_welcome/email_subject.txt", null);
        StringBuffer buffer = new StringBuffer();
        Iterable<Client> iterable = clientRepository.findAll(clientIds);
        for(Client client: iterable) {
            if(typeEmail==8) {
                operationWithBonusesClient(client.getId(), null, 100, 4, null, "", new Long(8));
            }
            if (client.getEmail() != null && !client.getEmail().isEmpty()) {
                String resultHtml = sendPromoForActivatePrivateTariffOnEmail(client, "", typeEmail);
                SendEmailUtil.sendEmail(client.getEmail(), resultHtml, subject);
            }else{
                buffer.append(String.format("Email not send for client id=%s, email=%s\n",client.getId(),client.getEmail()));
            }
        }
        /*
        рассылка промо
        for(Client client: iterable){
            if(client.getEmail()!=null && !client.getEmail().isEmpty()){
                if(privateTariffRepository.findByClient(client)==null){
                    String promo = getFreePromoCodeExclusive(client);
                    if(!promo.isEmpty()){
                        String resultHtml = sendPromoForActivatePrivateTariffOnEmail(client, promo, typeEmail);
                        SendEmailUtil.sendEmail(client.getEmail(), resultHtml, subject);
                    }else{
                        buffer.append(String.format("Не удалось получить промокод для клиента client id=%s, email=%s. Возможно ему уже был выдан промокод\n",client.getId(),client.getEmail()));
                    }
                }
            }else{
                buffer.append(String.format("Email not send for client id=%s, email=%s\n",client.getId(),client.getEmail()));
            }
        }
        */
        LOGGER.info(buffer.toString());
    }




    public SendEmailResponse sendEmail(String email, String resultHtml, String subject, String security_token){
        WebUser webUser = webUserRepository.findByToken(security_token);
        SendEmailResponse response = new SendEmailResponse();
        if(webUser == null){
            response.getErrorCodeHelper().setErrorCode(1);
            response.getErrorCodeHelper().setErrorMessage("Web user not found");
            return response;
        }
              if(email!=null && resultHtml!=null && subject!=null){
                  SendEmailUtil.sendEmail(email, resultHtml, subject);
              }else{
                  response.getErrorCodeHelper().setErrorCode(2);
                  response.getErrorCodeHelper().setErrorMessage("Тема, мыло или текст не могут быть пустыми");
              }
          return  response;
    }




    public PropertyUpdateResponse setPropertyValue(String prop_name, String prop_value){
        PropertyUpdateResponse response = new PropertyUpdateResponse();
        if(prop_name!=null && !prop_name.isEmpty()){
            Properties properties = propertiesRepository.findByPropName(prop_name);
            if(properties!=null){
                properties.setPropValue(prop_value);
                response.getErrorCodeHelper().setErrorCode(0);
                response.getErrorCodeHelper().setErrorMessage("");
            }else{
                response.getErrorCodeHelper().setErrorCode(1);
                response.getErrorCodeHelper().setErrorMessage("Свойство не найдено");
            }
        }else{
            response.getErrorCodeHelper().setErrorCode(2);
            response.getErrorCodeHelper().setErrorMessage("Свойство не может быть пустым");
        }
        return response;
    }





    public PropertyUpdateResponse setPropertyValueARM(String security_token, String prop_name, String prop_value){
        WebUser webUser = webUserRepository.findByToken(security_token);
         if(webUser == null){
             throw new CustomException(1, "Web user not found");
         }
         if (!webUser.getRole().equals(AdministratorRole.ADMIN)) {
             throw new CustomException(2, "Permission denied");
         }
         if(prop_name==null || prop_name.isEmpty()){
             throw new CustomException(4, "Свойство не может быть пустым");
         }
         Properties properties = propertiesRepository.findByPropName(prop_name);
         if(properties == null){
             throw new CustomException(5, "Свойство не найдено");
         }
         properties.setPropValue(prop_value);
         propertiesRepository.save(properties);

         mongoDBServices.createEvent(3, ""+webUser.getId(), 3, 0, "setPropertyValueArm", "", "");
         //mongoDBServices.createEvent(3, ""+webUser.getId(), 3, "setPropertyValueArm", "", "", 0, 0);
        return new PropertyUpdateResponse();
    }




    public String getPropertySentCode(String prop_name){
        String result = "";
              if(prop_name!=null && !prop_name.equals("")&& prop_name.equals("client_sent_code")){
                  Properties properties = propertiesRepository.findByPropName(prop_name);
                         if(properties!=null){
                             String prop_val = properties.getPropValue();
                             if(prop_val!=null && !prop_val.equals("")){
                                 result = prop_val;
                             }
                         }
              }
          return result;
    }




    public boolean setPropertySentCode(String prop_name, String prop_value){
        boolean result = false;
        if(prop_name!=null && !prop_name.equals("")&& prop_name.equals("client_sent_code")){
             Properties properties = propertiesRepository.findByPropName(prop_name);
               String prop_val = properties.getPropValue();
                 if(prop_val!=null && !prop_val.equals("")){
                     properties.setPropValue(prop_value);
                     propertiesRepository.save(properties);
                     result = true;
                 }
        }
        return result;
    }



    @Transactional
    public LoginClientResponse loginClient(String phone, String password, DeviceInfoModel deviceInfoModel) {
        ClientInfo result = null;
        LoginClientResponse response = new LoginClientResponse();
        Client client = clientRepository.findByPhoneAndPassword(phone, password);

        if (client != null) {
            if (Client.RegistrationState.CONFIRMED.equals(client.getRegistrationState())){

                Set<DeviceInfo> deviceInfoSet =  client.getDevices();

                   if(deviceInfoSet!=null){
                       if(deviceInfoSet.size()!=0){
                       DeviceInfo dI= (DeviceInfo)deviceInfoSet.toArray()[0];
                       if(dI!=null){
                           if(deviceInfoModel!=null){
                               dI.setToken(deviceInfoModel.getNewToken());
                               dI.setType(DeviceInfo.Type.getDeviceType(deviceInfoModel.getDeviceType()));
                           }
                         }
                      }
                   }

                client.setLastLoginTime(timeService.nowDateTime());
                clientRepository.save(client);
                result = ModelsUtils.toModel(client);

                response.setErrorCode(0);
                response.setMessage("");
                response.setClientInfo(result);
                response.setConfirmed(true);
            } else {
                result = new ClientInfo();
                response.setErrorCode(2);
                response.setMessage("Client not confirmed");
                response.setClientInfo(result);
                response.setConfirmed(false);
            }
        }else{
            response.setErrorCode(1);
            response.setMessage("Client not found");
        }
        return response;
    }



    public SmsSendARMResponse smsSendARM(String phone, String message, String security_token){
        SmsSendARMResponse response = new SmsSendARMResponse();
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser==null){
            response.getErrorCodeHelper().setErrorCode(1);
            response.getErrorCodeHelper().setErrorMessage("User not found");
            return response;
        }
        if(!EnumSet.of(AdministratorRole.DISPATCHER, AdministratorRole.ADMIN).contains(webUser.getRole())){
            response.getErrorCodeHelper().setErrorCode(2);
            response.getErrorCodeHelper().setErrorMessage("Permission denied");
            return response;
        }
        if (!StringUtils.isEmpty(phone)) {
            String phoneNormalized = PhoneUtils.normalizeNumber(phone);
            serviceSMSNotification.sendCustomSMS(phoneNormalized, message, "");
        }
        return response;
    }



    public SmsSendDefaultResponse smsSendDefault(String phone, String message){
        SmsSendDefaultResponse response = new SmsSendDefaultResponse();
        if (!StringUtils.isEmpty(phone)) {
            String phoneNormalized = PhoneUtils.normalizeNumber(phone);
            serviceSMSNotification.sendCustomSMS(phoneNormalized, message, "");
                 response.setSendSms(true);
        }
        return response;
    }





    public ActualMissionResponse missionIsActual(long missionId){
        ActualMissionResponse response = new ActualMissionResponse();
          Mission mission = missionRepository.findOne(missionId);
            if(mission!=null){
                if(mission.getState().equals(Mission.State.NEW) || mission.getState().equals(Mission.State.AUTO_SEARCH)){
                    response.setActual(true);
                }
            }
        return response;
    }




    public ActualMissionListResponse getActualMissionList(boolean isMark, List<Long> dirtyMissionIdList){
        ActualMissionListResponse response = new ActualMissionListResponse();

           if(dirtyMissionIdList!=null){
              for(Long missionId: dirtyMissionIdList){
                 Mission mission = missionRepository.findOne(missionId);
                    if(mission!=null){
                          if(isMark){
                            // проверка обычных миссий NEW
                              if(mission.getState().equals(Mission.State.NEW)){
                                  response.getActualMissionList().add(ModelsUtils.toModel(mission));
                                  response.setMark(isMark);
                              }
                              // проверка миссий AUTO_SEARCH
                              if(mission.getState().equals(Mission.State.AUTO_SEARCH)){
                                  response.getActualMissionList().add(ModelsUtils.toModel(mission));
                                  response.setMark(isMark);
                              }


                              /*      Сюда еще нужно будет впилить TURBO когда будет    */



                          }else{
                            // проверка букетов
                              if(mission.getState().equals(Mission.State.BOOKED) && mission.getBookingState().equals(Mission.BookingState.WAITING)){
                                  response.getActualMissionList().add(ModelsUtils.toModel(mission));
                                  response.setMark(isMark);
                              }
                          }
                    }
                 }
              }
          return response;
        }



    public DriverInfo loginDriver(String login, String password, DeviceInfoModel infoModel, String versionApp) {
        DriverInfo driverInfo = null;
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

            mongoDBServices.createEvent(1, ""+driver.getId(), 3, 0, "driverLogin", "", "");
            //mongoDBServices.createEvent(1, "" + driver.getId(), 3, "login", "", "", 0, driver == null ? 0 : driver.getId());
        }
        return driverInfo;
    }



    /**
    //DeviceInfo deviceInfo = devicesService.register(driver.getDevices(), infoModel);
            //driver.getDevices().clear();
            //driverRepository.save(driver);
            //if (deviceInfo != null){
                //driver.getDevices().add(deviceInfo);
            //}


            Set<DeviceInfo> deviceInfoSet =  driver.getDevices();
                if(deviceInfoSet!=null){
                    if(deviceInfoSet.size()!=0){
                      DeviceInfo dI= (DeviceInfo)deviceInfoSet.toArray()[0];
                    if(dI!=null){
                        if(infoModel!=null){
                            dI.setToken(infoModel.getNewToken());
                            dI.setType(DeviceInfo.Type.getDeviceType(infoModel.getDeviceType()));
                        }
                    }
                  }
                }
     **/

/**
    public DriverInfo loginDriver(String login, String password, DeviceInfoModel infoModel) {
        DriverInfo driverInfo = null;
        Driver driver = driverRepository.findByLoginAndPassword(login, password);
        if (driver != null) {

            DeviceInfo deviceInfo = devicesService.register(driver.getDevices(), infoModel);

            driver.getDevices().clear();
            driverRepository.save(driver);

            if (deviceInfo != null){
                driver.getDevices().add(deviceInfo);
            }

            driver.setState(Driver.State.AVAILABLE);

            driverRepository.save(driver);
            driverInfo = ModelsUtils.toModel(driver);
        }
        return driverInfo;
    }
**/



    private String getFreePromoCodeExclusive(Client client){
        List<PromoCodeExclusive> promoCodesList = promoCodeExclusiveRepository.findByDateOfIssueIsNull(); // findListPromoCodeExclusive
        if(promoCodesList==null || promoCodesList.isEmpty()){
            throw new CustomException(2,"No available promocodes");
        }
        Random rand = new Random();
        int randomIndex = rand.nextInt(promoCodesList.size());
        PromoCodeExclusive promoExclusive = promoCodesList.get(randomIndex);

        PrivateTariff privateTariff = privateTariffRepository.findByClientAndTariffName(client, AutoClass.BONUS.name());
        if(privateTariff!=null){
            LOGGER.info("Данному клиенту уже был выдан промокод для активации тарифа. Активация клиентом: "+(privateTariff.isActivated()?"да":"нет"));
            return "";
            //throw new CustomException(4, "Данному клиенту уже был выдан промокод для активации тарифа. Активация клиентом: "+(privateTariff.isActivated()?"да":"нет"));
        }
        privateTariff = new PrivateTariff();
        privateTariff.setClient(client);
        privateTariff.setTariffName(AutoClass.BONUS.name());
        privateTariff.setActive(Boolean.TRUE);
        privateTariff.setActivated(Boolean.FALSE);
        privateTariff.setPromoExclusive(promoExclusive);
        privateTariffRepository.save(privateTariff);

        promoExclusive.setDateOfIssue(DateTimeUtils.nowNovosib_GMT6());
        promoCodeExclusiveRepository.save(promoExclusive);

        return promoExclusive.getPromoCode();
    }





    public PromoCodeExclusiveResponse getPromoCodeExclusive(String security_token, long clientId){
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser == null){
            throw new CustomException(1,"Web user not found");
        }
        Client client = clientRepository.findOne(clientId);
        if(client == null){
            throw new CustomException(2,"Client user not found");
        }
        PromoCodeExclusiveResponse response = new PromoCodeExclusiveResponse();
        String promo = getFreePromoCodeExclusive(client);
        if(promo.isEmpty()){
            throw new CustomException(4, "Данному клиенту уже был выдан промокод для активации тарифа");
        }

            return response;
    }



    public BanPeriodRestDriverResponse banPeriodRestDriver(String security_token, long banPeriodId){
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser == null){
            throw new CustomException(1,"Web user not found");
        }
        BanPeriodRestDriverResponse response = new BanPeriodRestDriverResponse();
        if(banPeriodId==0){
            Iterable<BanPeriodRestDriver> iterable = banPeriodRestDriverRepository.findAll();
            for(BanPeriodRestDriver ban: iterable){
                response.getBanPeriodRestDriverInfoList().add(ModelsUtils.toModel(ban));
            }
        }else{
            BanPeriodRestDriver bp = banPeriodRestDriverRepository.findOne(banPeriodId);
               if(bp==null){
                   throw new CustomException(1,"Ban period not found");
               }
            response.getBanPeriodRestDriverInfoList().add(ModelsUtils.toModel(bp));
        }
          return response;
    }




    public DriverPeriodWorkResponse driverPeriodWork(String security_token, long driverId, long startTime, long endTime, Boolean active, long driverPeriodWorkId){
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser == null){
            throw new CustomException(1,"Web user not found");
        }
        DriverPeriodWorkResponse response = new DriverPeriodWorkResponse();
        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(DriverPeriodWork.class);

        if(driverId!=0){
            criteria.add(Restrictions.eq("driver.id", driverId));
        }
            criteria = QueryUtils.fillDateTimeParameter(criteria, "startWork", startTime, endTime);
        /*
        if(startTime!=0 || endTime!=0){
            criteria.add(Restrictions.ge("startWork", new DateTime(new Date(startTime * 1000))));
            criteria.add(Restrictions.lt("startWork", new DateTime(new Date(endTime * 1000))));
        }
        */
        if(active!=null){
            criteria.add(Restrictions.eq("active", active));
        }
        if(driverPeriodWorkId!=0){
            criteria.add(Restrictions.eq("id", driverPeriodWorkId));
        }
        criteria.addOrder(Order.desc("startWork"));

        List<DriverPeriodWork> listDriverPeriodWork = criteria.list();

        for(DriverPeriodWork driverPeriodWork: listDriverPeriodWork){
            response.getDriverPeriodWorkInfos().add(ModelsUtils.toModelDriverPeriodWorkInfo(driverPeriodWork));
        }
        return response;
    }




    public void updateDriverPeriodWork(String security_token, List<DriverPeriodWorkInfo> driverPeriodWorkInfos){
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser == null){
            throw new CustomException(1, "Web user not found");
        }
        if(!EnumSet.of(AdministratorRole.DISPATCHER, AdministratorRole.ADMIN).contains(webUser.getRole())){
            throw new CustomException(2, "Permission denied");
        }

        List<DriverPeriodWork> list = new ArrayList<>();
        //List<PeriodWork> listPeriodWork = new ArrayList<>();
        for(DriverPeriodWorkInfo driverPeriodWorkInfo: driverPeriodWorkInfos){
            Driver driver = driverRepository.findOne(driverPeriodWorkInfo.getDriverId());
            DriverRequisite requisite = driverRequisiteRepository.findByIdAndActive(driverPeriodWorkInfo.getRequisiteId(), true);
              if(driver == null){
                 throw new CustomException(4, String.format("Driver with id %s not found", driverPeriodWorkInfo.getDriverId()));
              }
              if(requisite == null){
                 throw new CustomException(5, "DriverRequsite: "+driverPeriodWorkInfo.getRequisiteId()+" for Driver id: "+driver.getId()+" not found or not active");
              }

            DriverPeriodWork dpw;
            // PeriodWork periodWork;
            if(driverPeriodWorkInfo.getId() == 0){
                // add
                dpw = ModelsUtils.fromModel(driverPeriodWorkInfo);
                /*
                periodWork = ModelsUtils.fromModelToPeriodWork(driverPeriodWorkInfo);
                if(periodWorkRepository.findByStartPeriodAndEndPeriod(periodWork.getStartPeriod(), periodWork.getEndPeriod()) == null){
                    listPeriodWork.add(ModelsUtils.fromModelToPeriodWork(driverPeriodWorkInfo));
                }
                LOGGER.info("periodWork: start = "+periodWork.getStartPeriod()+" end = "+periodWork.getEndPeriod());
                */
            }else{
                // upd
                DriverPeriodWork driverPeriodWork =  driverPeriodWorkRepository.findOne(driverPeriodWorkInfo.getId());
                if(driverPeriodWork == null){
                    throw new CustomException(7, "DriverPeriodWork with id: "+driverPeriodWorkInfo.getId()+" not found");
                }
                dpw = ModelsUtils.fromModelUpd(driverPeriodWorkInfo, driverPeriodWork);
            }
            dpw.setDriver(driver);
            dpw.setDriverRequisite(requisite);
            list.add(dpw);
        }
        driverPeriodWorkRepository.save(list);
        //periodWorkRepository.save(listPeriodWork);
    }





    public void updateBanPeriodRestDriver(String security_token, List<BanPeriodRestDriverInfo> banPeriodRestDriverInfos, String ip){
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser == null){
            throw new CustomException(1,"Web user not found");
        }
        if(!EnumSet.of(AdministratorRole.DISPATCHER, AdministratorRole.ADMIN).contains(webUser.getRole())){
            throw new CustomException(2,"Permission denied");
        }
        List<BanPeriodRestDriver> list = new ArrayList<>();

        for(BanPeriodRestDriverInfo banPeriodRestDriverInfo: banPeriodRestDriverInfos){
               if(banPeriodRestDriverInfo.getId() == 0){
                   // add
                   list.add(ModelsUtils.fromModel(banPeriodRestDriverInfo));
               }else{
                   // upd
                   BanPeriodRestDriver banPeriodRestDriver =  banPeriodRestDriverRepository.findOne(banPeriodRestDriverInfo.getId());
                     if(banPeriodRestDriver == null){
                         throw new CustomException(4,"BanPeriodRestDriver with id: "+banPeriodRestDriverInfo.getId()+" not found");
                     }
                   list.add(ModelsUtils.fromModelUpd(banPeriodRestDriverInfo, banPeriodRestDriver));
               }
        }
        banPeriodRestDriverRepository.save(list);
    }





    public EventPartnerResponse eventsPartners(String security_token, long clientId){
        if (!validatorService.validateUser(clientId, security_token, 1)) {
            throw new CustomException(3, "Клиент не найден");
        }
        EventPartnerResponse response = new EventPartnerResponse();
        List<EventPartner> eventPartnerList = eventPartnerRepository.findByTimeOfEventAfterOrderByTimeOfEventAsc(DateTimeUtils.nowNovosib_GMT6().withTimeAtStartOfDay());
           for(EventPartner event :eventPartnerList){
               response.getEventPartnerInfos().add(ModelsUtils.toModel(event));
           }
         return response;
    }



    public EventPartnerARMResponse eventsPartnersARM(String security_token, long startDate, long endDate, long eventId, String phoneMask, int numberPage, int sizePage){
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser == null){
            throw new CustomException(1,"Web user not found");
        }
        EventPartnerARMResponse response = new EventPartnerARMResponse();

        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(EventPartner.class);
        //Criteria crit = session.createCriteria(EventPartner.class);

        if(eventId!=0){
            criteria.add(Restrictions.eq("id", eventId));
            //crit.add(Restrictions.eq("id", eventId));
        }
        criteria = QueryUtils.fillDateTimeParameter(criteria, "timeOfEvent", startDate, endDate);
        /*
        if(dateStart!=0 || dateEnd!=0){
            criteria.add(Restrictions.ge("timeOfEvent", new DateTime(new Date(dateStart * 1000))));
            criteria.add(Restrictions.lt("timeOfEvent", new DateTime(new Date(dateEnd * 1000))));
            //crit.add(Restrictions.ge("timeOfEvent", new DateTime(new Date(dateStart * 1000))));
            //crit.add(Restrictions.lt("timeOfEvent", new DateTime(new Date(dateEnd * 1000))));
        }
        */
        if(phoneMask!=null){
            criteria.add(Restrictions.ilike("phone", "%" + phoneMask + "%"));
            //crit.add(Restrictions.ilike("phone", "%" + phoneMask + "%"));
        }


        criteria.addOrder(Order.desc("timeOfEvent"));

        criteria.setProjection(Projections.rowCount());
        Long total = (Long)criteria.uniqueResult();
        criteria.setProjection(null);

        response.setTotalItems(total);

        long lastPageNumber = ((total / sizePage) + 1);
        response.setLastPageNumber(lastPageNumber);

        criteria.setFirstResult((numberPage - 1) * sizePage);
        criteria.setMaxResults(sizePage);

        List<EventPartner> listEventPartners = criteria.list();

        for(EventPartner eventPartner: listEventPartners){
            response.getEventPartnerInfos().add(ModelsUtils.toModel(eventPartner));
        }
          return response;
    }




    public void updateEventPartners(String security_token, List<EventPartnerInfo> eventPartnerInfos){
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser == null){
            throw new CustomException(1,"Web user not found");
        }
        if(!EnumSet.of(AdministratorRole.DISPATCHER, AdministratorRole.ADMIN).contains(webUser.getRole())){
            throw new CustomException(2,"Permission denied");
        }
        List<EventPartner> list = new ArrayList<>();
        for(EventPartnerInfo info :eventPartnerInfos){
            String phoneNormalized = PhoneUtils.normalizeNumber(info.getPhone());
            if(phoneNormalized==null){
                throw new CustomException(4,"EventPartner phone: "+info.getPhone()+" isn't normalized");
            }
            if(info.getId() == 0){
                // add
                list.add(ModelsUtils.fromModel(info));
            }else{
                EventPartner eventPartner = eventPartnerRepository.findOne(info.getId());
                if(eventPartner == null){
                    throw new CustomException(5,"EventPartner with id: "+info.getId()+" not found");
                }else{
                    // upd
                    list.add(ModelsUtils.fromModelUpd(info, eventPartner));
                }
            }
        }
        eventPartnerRepository.save(list);
    }


    public VersionsApp versionApp(long versionAppId){
        return versionsAppRepository.findOne(versionAppId);
    }


    public RegionInfo pointInsidePolygon(double latitude, double longitude){
        RegionInfo info = null;
        if(latitude==0.0 || longitude ==0.0){
            return info;
        }
        Iterable<Region> regions = regionRepository.findByIsActive(true);
        List<Region> listRegions = Lists.newArrayList(regions);
        if(CollectionUtils.isEmpty(listRegions)){
            throw new CustomException(2, "Список регионов пуст");
        }
        for(Region region: listRegions){
            List<String> coordinatesList = region.getRegionCoordinates();
            if(!CollectionUtils.isEmpty(coordinatesList)){
                if(GeoUtils.isInsidePolygon(latitude, longitude, coordinatesList)){
                    return ModelsUtils.toModel(region, false); // false - не отдавать координаты
                }
            }
        }
          return info;
    }



    public CheckPointInsidePolygonResponse checkInsidePolygon(String security_token, double latitude, double longitude) {
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser == null){
            throw new CustomException(1, "WebUser not found");
        }
        CheckPointInsidePolygonResponse response= new CheckPointInsidePolygonResponse();
          response.setRegionInfo(pointInsidePolygon(latitude, longitude));
           return response;
    }




    // модерирование отзыва на водителя оператором
    public void updateEstimate(String security_token, List<EstimateInfoARM> estimateInfoARMs){
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser == null){
            throw new CustomException(1,"Web user not found");
        }
        if(!EnumSet.of(AdministratorRole.DISPATCHER, AdministratorRole.ADMIN).contains(webUser.getRole())){
            throw new CustomException(2,"Permission denied");
        }
        List<Estimate> list = new ArrayList<>();
        for(EstimateInfoARM estimateInfoARM: estimateInfoARMs){
            Estimate estimate = estimateRepository.findOne(estimateInfoARM.getId());
            if(estimate == null){
                  throw new CustomException(4,"Estimate with id: "+estimateInfoARM.getId()+" not found");
              }
              estimate.setVisible(estimateInfoARM.isVisible());
              list.add(estimate);
        }
              estimateRepository.save(list);
    }






    public BlockClientResponse blockClient(long clientId, long webUserId, boolean block, String reason, String security_token) {
        BlockClientResponse response = new BlockClientResponse();
        Client client = getClientInfo(clientId);
        WebUser webUser = webUserRepository.findOne(webUserId);

              //if (webUser.getToken() != null && webUser.getToken().equals(security_token)) {
                  if (client != null) {
                      if(!client.getAdministrativeState().equals(Client.State.BLOCKED)){
                          // актив или инактив
                          if(block){
                              ClientLocks clientLocks =new ClientLocks();
                              clientLocks.setClientId(clientId);
                              clientLocks.setTimeOfLock(DateTime.now().getMillis());
                              clientLocks.setReason(reason);
                              clientLockRepository.save(clientLocks);
                              client.setAdministrativeState(Client.State.BLOCKED);
                              client.setToken(""+ StrUtils.generateAlphaNumString(4));

                              clientRepository.save(client);

                              response.getErrorCodeHelper().setErrorCode(0);
                              response.getErrorCodeHelper().setErrorMessage("Клиент заблокирован");

                          }
                      }else{
                          // статус = заблокирован, значит разблокируем
                          ClientLocks clientLocks = clientLockRepository.findByClientIdAndTimeOfUnlockIsNull(clientId);

                          if(clientLocks!=null){
                              clientLocks.setTimeOfUnlock(DateTime.now().getMillis());
                              clientLockRepository.save(clientLocks);
                          }
                          client.setAdministrativeState(Client.State.ACTIVE);
                          clientRepository.save(client);
                          response.getErrorCodeHelper().setErrorCode(0);
                          response.getErrorCodeHelper().setErrorMessage("Клиент разблокирован");
                      }
                  } else {
                      response.getErrorCodeHelper().setErrorCode(1);
                      response.getErrorCodeHelper().setErrorMessage("Клиент не найден");
                  }
        return response;
    }



    /*  BLOCKED CLIENT OLD VERSION
    public boolean blockClient(long clientId, long blockingTime, boolean block, String reason) {
        boolean result = true;
        Client client = getClientInfo(clientId);
        if (client != null) {
            if (block){
                client.setAdministrativeState(Client.State.BLOCKED);
                DateTime dateTime = timeService.nowDateTime().plusMillis((int) blockingTime);
                client.setBlockingTime(dateTime);

            }else {
                client.setAdministrativeState(Client.State.ACTIVE);
                client.setBlockingTime(null);
            }
            clientRepository.save(client);
        } else {
            result = false;
        }
        return result;
    }
    */


       // f:add
       public boolean setAdministrationStatusDriver(long driverId, String adminStatus,String reason){
           Driver driver = getDriverInfo(driverId);
           boolean update = false;

              if (driver != null) {
                   if(!driver.getAdministrativeStatus().equals(Driver.AdministrativeStatus.BLOCKED)){
                        // драйвер не заблокирован, тогда
                       switch (adminStatus){
                           case "INACTIVE": {
                               driver.setAdministrativeStatus(Driver.AdministrativeStatus.INACTIVE);
                               driverRepository.save(driver);
                               break;
                           }
                           case "ACTIVE": {
                               driver.setAdministrativeStatus(Driver.AdministrativeStatus.ACTIVE);
                               driverRepository.save(driver);
                               break;
                           }
                           case "BLOCKED": {
                               DriverLocks driverLocks =new DriverLocks();
                               driverLocks.setDriverId(driverId);
                               if(driver.getCurrentMission()!=null)
                                  driverLocks.setMissionId(driver.getCurrentMission().getId());
                               driverLocks.setTimeOfLock(DateTime.now().getMillis());
                               driverLocks.setReason(reason);
                               driverLockRepository.save(driverLocks);
                               driver.setAdministrativeStatus(Driver.AdministrativeStatus.BLOCKED);
                               driverRepository.save(driver);
                               break;
                           }
                       }
                           update = true;
                   }else{
                      // водитель уже был заблокирован
                       if(!adminStatus.equals("BLOCKED")){
                           List<DriverLocks> driverLocks = driverLockRepository.findByDriverIdAndTimeOfUnlockIsNull(driverId, new PageRequest(0, 5));
                           if(!CollectionUtils.isEmpty(driverLocks)){
                               DriverLocks locks = driverLocks.get(0);
                               locks.setTimeOfUnlock(DateTime.now().getMillis());
                               driverLockRepository.save(locks);
                           }
                           driver.setAdministrativeStatus(Driver.AdministrativeStatus.valueOf(adminStatus));
                           driverRepository.save(driver);
                           update = true;
                       }
                   }
             }
           return update;
       }





    // обналичивание денег
    public MoneyWithdrawalResponse moneyWithdrawal(long driverId, String smsCode, int countSymbols){
        MoneyWithdrawalResponse response = new MoneyWithdrawalResponse();
        Driver driver = driverRepository.findOne(driverId);

            if(driver!=null){

                MoneyWithdrawal moneyWithdrawal =  moneyWithdrawalRepository.findByDriver(driver);

                    if(moneyWithdrawal == null){
                        moneyWithdrawal = new MoneyWithdrawal();
                        moneyWithdrawal.setDriver(driver);

                        moneyWithdrawalRepository.save(moneyWithdrawal);

                        String smsCodeGen = generateSMSByCountSymbols(countSymbols);
                        moneyWithdrawal.setSmsCode(smsCodeGen);
                        moneyWithdrawalRepository.save(moneyWithdrawal);
                        serviceSMSNotification.sendCustomSMS(driver.getPhone(), "Код подтверждения для выдачи наличных: " + smsCodeGen,"");
                        response.setSmsCode(smsCodeGen);
                        response.getErrorCodeHelper().setErrorCode(0);
                        response.getErrorCodeHelper().setErrorMessage("Код подтверждения отправлен");
                    }else{
                           if(smsCode==null){
                              // генерируем для данного водителя еще раз
                               String smsCodeGen = generateSMSByCountSymbols(countSymbols);
                               moneyWithdrawal.setSmsCode(smsCodeGen);
                               moneyWithdrawalRepository.save(moneyWithdrawal);
                               serviceSMSNotification.sendCustomSMS(driver.getPhone(), "Код подтверждения для выдачи наличных: "+smsCodeGen, "");
                               response.getErrorCodeHelper().setErrorCode(0);
                               response.getErrorCodeHelper().setErrorMessage("Код подтверждения отправлен");
                           }else{
                               if(smsCode.equals(moneyWithdrawal.getSmsCode())){
                                   // equals
                                   moneyWithdrawal.setSmsCode(null);
                                   moneyWithdrawalRepository.save(moneyWithdrawal);
                                   response.getErrorCodeHelper().setErrorCode(0);
                                   response.getErrorCodeHelper().setErrorMessage("Код успешно подтвержден");
                               }else{
                                   // смс коды не совпадают
                                   response.getErrorCodeHelper().setErrorCode(2);
                                   response.getErrorCodeHelper().setErrorMessage("Неправильный смс код");
                               }
                           }
                    }
            }else{
                response.getErrorCodeHelper().setErrorCode(1);
                response.getErrorCodeHelper().setErrorMessage("Водитель не найден");
            }
           return response;
    }



     // f:add
     public DriverLocksInfo getDriverAdministrativeStatus(long driverId, String security_token){
         DriverLocksInfo result = null;
         WebUser webUser = webUserRepository.findByToken(security_token);
           if(webUser==null){
               return result;
           }
         Driver driver = getDriverInfo(driverId);
         if (driver != null) {
             if(!driver.getAdministrativeStatus().equals(Driver.AdministrativeStatus.BLOCKED)){
                 DriverLocksInfo driverLocksInfo = new DriverLocksInfo();
                 driverLocksInfo.setAdministrativeStatus(driver.getAdministrativeStatus().toString());
                 result = driverLocksInfo;
             }else{
                 List<DriverLocks> driverLocks =  driverLockRepository.findByDriverIdAndTimeOfUnlockIsNull(driverId, new PageRequest(0, 1));
                 if(!CollectionUtils.isEmpty(driverLocks)){
                     result =  ModelsUtils.toModel(driverLocks.get(0));
                     result.setAdministrativeStatus(driver.getAdministrativeStatus().toString());
                 }
             }
         }
            return result;
    }




    // f:add
    public ClientAdministrationStatusResponse getClientAdministrativeStatus(long clientId){
        ClientAdministrationStatusResponse response = new ClientAdministrationStatusResponse();
        Client client = getClientInfo(clientId);
        if (client != null) {
            ClientLocksInfo result = new ClientLocksInfo();
            if(!client.getAdministrativeState().equals(Client.State.BLOCKED)){
                result.setAdministrativeStatus(client.getAdministrativeState().toString());
            }else{
                ClientLocks clientLocks =  clientLockRepository.findByClientIdAndTimeOfUnlockIsNull(clientId);
                result =  ModelsUtils.toModel(clientLocks);
                result.setAdministrativeStatus(client.getAdministrativeState().toString());
            }
            response.setClientLocksInfo(result);
            response.getErrorCodeHelper().setErrorCode(0);
            response.getErrorCodeHelper().setErrorMessage("");
        }else{
            response.getErrorCodeHelper().setErrorCode(1);
            response.getErrorCodeHelper().setErrorMessage("Client not found");
        }
        return response;
    }


/*
    public boolean blockDriver(long driverId, long blockingTime, boolean block) {
        boolean result = true;
        Driver driver = getDriverInfo(driverId);
        if (driver != null) {
            if (block){
                driver.setAdministrativeStatus(Driver.AdministrativeStatus.BLOCKED);
                DateTime dateTime = timeService.nowDateTime().plusMillis((int) blockingTime);
                driver.setBlockingTime(dateTime);
            }else {
                driver.setAdministrativeStatus(Driver.AdministrativeStatus.ACTIVE);
                client.setBlockingTime(null);
            }
            driverRepository.save(driver);
        } else {
            result = false;
        }
        return result;
    }
*/



    public List<ItemLocation> findDriversAround(ItemLocation currentLocation, int radius) {
        ArrayList<ItemLocation> locations = new ArrayList<>();
        //todo: implement method
        return locations;
    }

    public List<ItemLocation> findDriversActive(String city, boolean showOccupied) {
        ArrayList<ItemLocation> result = new ArrayList<>();
        List<Driver> drivers = driverRepository.findByStateAndCity(Driver.State.AVAILABLE, city);
        for (Driver driver : drivers) {
            List<DriverLocation> locations = locationRepository.findByDriverOrderByIdDesc(driver);
            if (locations.size() > 0){
                result.add(locations.get(0).toItemLocation());
            }
        }
        return result;
    }




    @Transactional
    public List<DriverInfo> findDrivers(List<Long> ids) {
        ArrayList<DriverInfo> result = new ArrayList<>();
        Iterable<Driver> iterable;
        if (ids.isEmpty()){
            iterable = driverRepository.findAll();
        } else {
            iterable = driverRepository.findAll(ids);
        }
        for (Driver driver : iterable) {
            result.add(ModelsUtils.toModel(driver));
        }
        return result;
    }



    public static class HistoryMissionsARM {
        public ArrayList<MissionInfoARM> history = new ArrayList<>();
        public ArrayList<MissionInfoARM> booked = new ArrayList<>();
    }


    public static class HistoryMissions {
        public ArrayList<MissionInfo> history = new ArrayList<>();
        public ArrayList<MissionInfo> booked = new ArrayList<>();
    }

    public static class HistoryMissions_STR {
        public ArrayList<MissionHistory> history = new ArrayList<>();
        public ArrayList<MissionHistory> booked = new ArrayList<>();
    }


    public static class HistoryMissionsSite {
        public ArrayList<MissionInfo> bookedAndHistory = new ArrayList<>();
    }

    public static class BookedDetails {
        private int bookedNew = 0;
        private int bookedToMe = 0;
        private List<MissionInfo> missions = new ArrayList<>();

        public int getBookedNew() {
            return bookedNew;
        }

        public void setBookedNew(int bookedNew) {
            this.bookedNew = bookedNew;
        }

        public int getBookedToMe() {
            return bookedToMe;
        }

        public void setBookedToMe(int bookedToMe) {
            this.bookedToMe = bookedToMe;
        }

        public List<MissionInfo> getMissions() {
            return missions;
        }

        public void setMissions(List<MissionInfo> missions) {
            this.missions = missions;
        }
    }




    public class MissionSortUtil{
        private Mission mission;
        private int diffMinutes;

        public MissionSortUtil(Mission mission, int diffMinutes) {
              this.mission = mission;
              this.diffMinutes = diffMinutes;
        }

        public Mission getMission() {
            return mission;
        }

        public void setMission(Mission mission) {
            this.mission = mission;
        }

        public int getDiffMinutes() {
            return diffMinutes;
        }

        public void setDiffMinutes(int diffMinutes) {
            this.diffMinutes = diffMinutes;
        }
    }




    public UpdateCorporateClientLimitResponse updateCorporateClientLimit(String security_token, LimitInfo info) {
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser == null){
            throw new CustomException(1, "Web user not found");
        }
        Client mainClient = clientRepository.findOne(info.getMainClientId());
        if(mainClient == null){
            throw new CustomException(2, "Main client not found");
        }
        if(mainClient.getMainClient() == null){
            throw new CustomException(4, "Клиент не является корпоративным");
        }
        if(!mainClient.getMainClient().getId().equals(mainClient.getId())){
            throw new CustomException(5, String.format("Client id %s не является главным", mainClient.getId()));
        }
        Client subClient = clientRepository.findOne(info.getClientId());
        if(subClient == null){
            throw new CustomException(6, String.format("Client id %s not found", info.getClientId()));
        }
        if(subClient.getMainClient()==null || !subClient.getMainClient().getId().equals(mainClient.getId())){
            throw new CustomException(7, String.format("Несоответствие mainClient"));
        }

        UpdateCorporateClientLimitResponse response = new UpdateCorporateClientLimitResponse();

        if (info.getLimitId() == 0) {
            // add
            CorporateClientLimit corporateClientLimit = corporateClientLimitRepository.findByClientAndPeriod(subClient, CorporateClientLimit.Period.getByValue(info.getTypePeriod()));
            if (corporateClientLimit != null) {
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
        } else {
            CorporateClientLimit corporateLimit = corporateClientLimitRepository.findOne(info.getLimitId());
            if (corporateLimit == null) {
                throw new CustomException(9, String.format("CorporateClientLimit id %s not found", info.getLimitId()));
            }
            //corporateLimit.setClient(subClient);
            corporateLimit.setLimitAmount(info.getLimitAmount());
            corporateLimit.setUpdateTime(DateTimeUtils.nowNovosib_GMT6());
            //corporateLimit.setPeriod(CorporateClientLimit.Period.getByValue(info.getTypePeriod()));
            //corporateLimit.setMainClient(mainClient);
            corporateClientLimitRepository.save(corporateLimit);
            response.setErrorMessage("Updated!");
        }
        return response;
    }




    public CorporateClientARMResponse corporateClient(long mainClientId){
        CorporateClientARMResponse response = new CorporateClientARMResponse();
        Client mainClient = clientRepository.findOne(mainClientId);
        List<Client> clientList;
        if(mainClientId!=0){
             clientList = clientRepository.findByMainClient(mainClient);
        }else{
             clientList = clientRepository.findByMainClientIsNotNull();
        }
        if(clientList!=null){
            for(Client client: clientList){
                CorporateClientLocks clientLocks = corporateClientLocksRepository.findByClientAndTimeOfUnlockIsNull(client);
                ClientInfoCorporateARM info = ModelsUtils.toModelClientInfoCorporateARM(client, corporateClientLimitRepository.findByClient(client), clientLocks!=null ? true : false); // client.getAccount().getState().equals(Account.State.BLOCKED)
                if(clientLocks!=null){
                    info.setReason(clientLocks.getReason());
                }
                List<PrivateTariff> privateTariffs = privateTariffRepository.findByClientAndActive(client, Boolean.TRUE);
                for(PrivateTariff tariff:privateTariffs){
                    info.getAllowTariff().add(ModelsUtils.toModel(tariff));// AutoClass.valueOf(tariff.getTariffName()).getValue()
                }
                response.getClientInfoCorporateList().add(info);
            }
        }
        return response;
    }



    /*
    ClientInfoCorporateARM {
    private long clientId;
    private long mainClientId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private boolean isBlock;
    private LimitInfo monthLimit;
    private LimitInfo weekLimit;
     */



    private DeviceInfoModel buildDeviceInfo() {
        DeviceInfoModel model = new DeviceInfoModel();
        model.setDeviceType(DeviceInfo.Type.UNKNOWN.getId());
        model.setNewToken("be97e0450b8f80c1");
        model.setOldToken("zxcdfdsfgdg345df");
        return model;
    }





    @Transactional
    private Client createClient(ClientInfoARM info) throws Exception {
      String phone = PhoneUtils.normalizeNumber(info.getPhone());
        if(phone==null){
           throw new CustomException(1, String.format("Phone number incorrect!"));
        }
        Client client = clientRepository.findByPhone(phone);
        if(client != null){
            throw new CustomException(2, String.format("Клиент с таким номером телефона %s уже существует: ", phone));
        }
        if(info.getCorporateLogin()!=null && clientRepository.findByCorporateLogin(info.getCorporateLogin())!=null){
            throw new CustomException(5, String.format("Клиент с таким логином %s уже существует: ", info.getCorporateLogin()));
        }
        if(info.getMonthLimit()!=null && info.getWeekLimit()!=null && info.getMonthLimit().getTypePeriod()==info.getWeekLimit().getTypePeriod()){
            throw new CustomException(4, "Нельзя установить одинаковые типы лимитов");
        }
        String tempPass = generateTempPasswordForCorporateClient();
        client = ModelsUtils.fromModel(info);
        //client.setPassword(tempPass);
        client.setAdministrativeState(Client.State.ACTIVE);
        client.setRegistrationState(Client.RegistrationState.CONFIRMED);

        Account account = billingService.createClientAccountWithBonuses(0);
        client.setAccount(account);
        client.setRegistrationTime(DateTimeUtils.nowNovosib_GMT6());
        client.getDevices().clear();
        clientRepository.save(client);

        updateClientPrivateTariff(client, info.getAllowTariff());

        if(info.isRoot()){
            client.setMainClient(client);
            /* баланс будет корректироваться отдельно */
            account.setKind(Account.Kind.CORPORATE);
        }
        DeviceInfoModel deviceInfoModel = buildDeviceInfo();
        DeviceInfo deviceInfo = devicesService.register(client.getDevices(), deviceInfoModel);
        client.getDevices().add(deviceInfo);
        client = clientRepository.save(client);

        addClientLimit(info.getMonthLimit(), client);
        addClientLimit(info.getWeekLimit(), client);

        serviceSMSNotification.temporaryPassword(client.getPhone(), tempPass, "");

        return client;
    }




    private void addClientLimit(LimitInfo limitInfo, Client client){
         if(limitInfo!=null){
            CorporateClientLimit newClientLimit = new CorporateClientLimit();
            newClientLimit.setClient(client);
            //newClientLimit.setMainClient(client.getMainClient());
            newClientLimit.setLimitAmount(limitInfo.getLimitAmount()*100);
            newClientLimit.setUpdateTime(DateTimeUtils.nowNovosib_GMT6());
            newClientLimit.setPeriod(CorporateClientLimit.Period.getByValue(limitInfo.getTypePeriod()));
            corporateClientLimitRepository.save(newClientLimit);
        }
    }




    private void updClientLimit(LimitInfo limitInfo, CorporateClientLimit corporateLimit) {
        corporateLimit.setLimitAmount(limitInfo.getLimitAmount() * 100);
        corporateLimit.setUpdateTime(DateTimeUtils.nowNovosib_GMT6());
        corporateClientLimitRepository.save(corporateLimit);
    }



    @Transactional
    private void updateClient(ClientInfoARM info){
        Client client = clientRepository.findOne(info.getId());
        if(client == null){
            throw new CustomException(1, String.format("Клиент %s не найден", info.getId()));
        }
        if(info.getPhone()!=null){
            String phone = PhoneUtils.normalizeNumber(info.getPhone());
            if(StringUtils.isEmpty(phone)){
                throw new CustomException(2, String.format("Неправильный формат номера телефона"));
            }
            Client clientByPhone = clientRepository.findByPhone(phone);
            if(clientByPhone != null && !clientByPhone.getId().equals(client.getId())){
                throw new CustomException(4, String.format("Клиент с таким номером телефона уже существует: %s", phone));
            }
        }
        if(info.getCorporateLogin()!=null){
            Client clientByCorpLogin = clientRepository.findByCorporateLogin(info.getCorporateLogin());
            if(clientByCorpLogin!=null && !clientByCorpLogin.getId().equals(client.getId())){
                throw new CustomException(8, String.format("Клиент с таким логином %s уже существует: ", info.getCorporateLogin()));
            }
        }
        if(info.getMonthLimit()!=null && info.getWeekLimit()!=null && info.getMonthLimit().getTypePeriod()==info.getWeekLimit().getTypePeriod()){
            throw new CustomException(5, "Нельзя установить одинаковые типы лимитов");
        }

        client = ModelsUtils.fromModelUpd(info, client);
        List<Long> temp = new ArrayList<>();

        if(info.getMainClientId()==0){
            if(client.getMainClient()!=null){
                if(client.getId().equals(client.getMainClient().getId())){
                    // обнуляем корневого клиента, нужна проверка есть ли у него дочерние
                    temp.add(client.getId());
                    List<Client> child = clientRepository.findByMainClientAndIdNotIn(client, temp);
                     if(!CollectionUtils.isEmpty(child)){
                         String listString = "";
                         for (Client c : child) {
                             listString += c.getId() + "\t";
                         }
                         throw new CustomException(6, String.format("Клиент %s содержит дочерние элементы %s", client.getId(), listString));
                     }else{
                         client.setMainClient(null);
                     }
                }else{
                    // данный клиент не является корневым, значит просто отвязываем его
                    client.setMainClient(null);
                }
            }
        }else{
            // обновление parent id
            Client mainClient = clientRepository.findOne(info.getMainClientId());
            if(mainClient==null){
                throw new CustomException(6, String.format("Клиент %s не найден", info.getMainClientId()));
            }
            if(client.getMainClient()==null){
                    // просто ставим ему parent id
                    client.setMainClient(mainClient);
                }else{
                    if(!client.getId().equals(client.getMainClient().getId())){
                      // попытка изменить parent id на другой: проверяем есть ли дочерние на текущем клиенте
                      temp.add(client.getId());
                      List<Client> child = clientRepository.findByMainClientAndIdNotIn(client, temp);
                        if(!CollectionUtils.isEmpty(child)){
                            String listString = "";
                            for (Client c : child) {
                                listString += c.getId() + "\t";
                            }
                            throw new CustomException(7, String.format("Клиент %s содержит дочерние элементы %s", client.getId(), listString));
                        }else{
                            client.setMainClient(mainClient);
                        }
                    }
                }
        }

        clientRepository.save(client);

        if(info.getMonthLimit()!=null){
            CorporateClientLimit corporateLimitMonth = corporateClientLimitRepository.findByClientAndPeriod(client, CorporateClientLimit.Period.getByValue(info.getMonthLimit().getTypePeriod()));
            if(corporateLimitMonth == null) {
                addClientLimit(info.getMonthLimit(), client);
            }else{
                updClientLimit(info.getMonthLimit(), corporateLimitMonth);
            }
        }
        if(info.getWeekLimit()!=null){
            CorporateClientLimit corporateLimitWeek = corporateClientLimitRepository.findByClientAndPeriod(client, CorporateClientLimit.Period.getByValue(info.getWeekLimit().getTypePeriod()));
            if(corporateLimitWeek == null){
                addClientLimit(info.getWeekLimit(), client);
            }else{
                updClientLimit(info.getWeekLimit(), corporateLimitWeek);
            }
        }

        updateClientPrivateTariff(client, info.getAllowTariff());

    }





    private void updateClientPrivateTariff(Client client, List<PrivateTariffInfo> allowTariffList){
            if(allowTariffList != null) {
                // сделать все тарифы неактивными
                disabledAllTariff(client);
                List<PrivateTariff> result = new ArrayList<>();
                PrivateTariff privateTariff;
                for (PrivateTariffInfo info : allowTariffList) {
                    privateTariff = privateTariffRepository.findByClientAndTariffName(client, AutoClass.getByValue(info.getTariffId()).name());
                    if (privateTariff == null) {
                        privateTariff = new PrivateTariff();
                    }
                    privateTariff.setActivated(true);
                    privateTariff.setActive(true);
                    privateTariff.setTariffName(AutoClass.getByValue(info.getTariffId()).name());
                    privateTariff.setClient(client);
                    privateTariff.setFreeWaitMinutes(info.getFreeWaitMinutesByTariff());
                    result.add(privateTariff);
                }
                privateTariffRepository.save(result);
            }

    }



    private void disabledAllTariff(Client client){
        List<PrivateTariff> privateTariffs = privateTariffRepository.findByClient(client);
        for(PrivateTariff tariff:privateTariffs){
            if(!tariff.getTariffName().equals(AutoClass.BONUS.name())){
                tariff.setActive(false);
            }
        }
        privateTariffRepository.save(privateTariffs);
    }


    public UpdateClientARMResponse updateClient(List<ClientInfoARM> corporateClientList) throws Exception {
        UpdateClientARMResponse response = new UpdateClientARMResponse();
        for(ClientInfoARM info :corporateClientList){
            if(info.getId()==0){
                // add client
                createClient(info);
            }else{
                // upd client
                updateClient(info);
            }
        }
        return response;
    }





    public RegionResponse regionList(String security_token, long regionId, String coast) {
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser==null){
            throw new CustomException(1, "Web user not found");
        }
        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(Region.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        if(regionId!=0){
            if(regionRepository.findOne(regionId) == null){
                throw new CustomException(4, "Region not found");
            }
            criteria.add(Restrictions.eq("id", regionId));
        }
        if(!StringUtils.isEmpty(coast)){
            criteria.add(Restrictions.eq("coast", coast));
        }
        criteria.addOrder(Order.desc("coast"));
        List<Region> regions = criteria.list();
        RegionResponse response = new RegionResponse();
        for(Region region: regions){
            response.getRegionInfos().add(ModelsUtils.toModel(region, regionId == 0 ? false : true));
        }
          return response;
    }




    // файл с координатами и айди региона
    public UpdateRegionResponse updateRegion(String security_token, List<RegionInfo> regionInfos) throws FileNotFoundException, UnsupportedEncodingException {
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser==null){
            throw new CustomException(1, "Web user not found");
        }
        UpdateRegionResponse response = new UpdateRegionResponse();
        int countAdd = 0;
        int countUpd = 0;
        for(RegionInfo regionInfo: regionInfos) {
            Region region;
            if (regionInfo.getId() == 0) {
                // add
                region = new Region();
                countAdd++;
            } else {
                // upd
                region = regionRepository.findOne(regionInfo.getId());
                if (region == null) {
                    throw new CustomException(2, String.format("Регион %s не найден", regionInfo.getId()));
                }
                countUpd++;
            }
            if(regionInfo.getNameRegion() != null){
                region.setNameRegion(regionInfo.getNameRegion());
            }
            if(regionInfo.getCoast() != null){
                region.setCoast(regionInfo.getCoast());
            }
            if(regionInfo.getMarkup() != null){
                region.setMarkup(regionInfo.getMarkup());
            }
            if(regionInfo.isActive() != null){
                region.setIsActive(regionInfo.isActive());
            }
            if(regionInfo.getTypeRegion()!=null){
                region.setTypeRegion(regionInfo.getTypeRegion());
            }
            if(regionInfo.getToMarkup() != null){
                region.setToMarkup(regionInfo.getToMarkup());
            }
            if(regionInfo.getFromMarkup() != null){
                region.setFromMarkup(regionInfo.getFromMarkup());
            }
            if(regionInfo.getRadius() != null){
                region.setRadius(regionInfo.getRadius());
            }
            regionRepository.save(region);

            if (!StringUtils.isEmpty(regionInfo.getBase64Coord())) {
                String textMessage = FileUtil.base64ToText(regionInfo.getBase64Coord());
                String[] arrayCoord = textMessage.split("\n");
                List<String> coordList = Arrays.asList(arrayCoord);
                if (!CollectionUtils.isEmpty(coordList)) {
                    region.getRegionCoordinates().clear();
                    regionRepository.save(region);
                    region.getRegionCoordinates().addAll(coordList);
                    regionRepository.save(region);
                }
            }
        }
        response.setErrorMessage(String.format("Добавлено: %s, обновлено: %s", countAdd, countUpd));
          return response;
    }




    public DriverSettingResponse driverStatAuto(String security_token, long driverId) {
        WebUser webUser = webUserRepository.findByToken(security_token);
        if (webUser == null) {
            throw new CustomException(1,"Web user not found");
        }
        Driver driver = driverRepository.findOne(driverId);
        if(driver == null){
            throw new CustomException(1, "Водитель не найден");
        }
        //serviceEmailNotification
        //mongoDBServices.activityDriver()
        DriverSettingResponse response = new DriverSettingResponse();
        DriverSetting driverSetting = driverSettingRepository.findByDriver(driver);
        if(driverSetting==null){
            response.setErrorMessage("Настройки не найдены");
        }
        response.setDriverSettingInfo(ModelsUtils.toModelDriverSettingInfo(driverSetting));
        return response;
    }








    /*
    List resultWithAliasedBean = s.createCriteria(Enrolment.class)
            .createAlias("student", "st")
            .createAlias("course", "co")
            .setProjection( Projections.projectionList()
                            .add( Projections.property("co.description"), "courseDescription" )
            )
            .setResultTransformer( new AliasToBeanResultTransformer(StudentDTO.class) )
            .list();
    StudentDTO dto = (StudentDTO)resultWithAliasedBean.get(0);
    */



    /*  ВТУЛИТЬ АЛИАС ТУ БИН: пофиксить вероятность иньекции */
    public DriverTimeWorkStatisticResponse driverTimeWorkStatistic(long driverId, String startTime, String endTime, Long taxoparkId){
        DriverTimeWorkStatisticResponse response = new DriverTimeWorkStatisticResponse();
        Session session = entityManager.unwrap(Session.class);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(
                "SELECT sum(dtw.sec_stay_busy) as sumStayBusy,\n" +
                        "sum(dtw.sec_state_online) as sumStateOnline,\n" +
                        "sum(dtw.time_sec_pay_rest) as sumTimeSecPayRest, \n" +
                        "sum(dtw.time_sec_rest) as sumTimeSecRest,\n" +
                        "sum(dtw.time_sec_work) as sumTimeSecWork,\n" +
                        "dtw.driver_id as driver FROM driver_time_work dtw join driver drv on dtw.driver_id=drv.id where 1=1\n"
        );
        if(driverId!=0){
            stringBuilder.append(" and dtw.driver_id = " + driverId);
        }
        if(taxoparkId!=null){
            stringBuilder.append(" and drv.taxopark_id = " + taxoparkId);
        }
        if(!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime)){
            stringBuilder.append(" and dtw.work_date BETWEEN '" + LocalDate.parse(startTime) + "' and '" + LocalDate.parse(endTime) + "'");
        }
            stringBuilder.append(" group by dtw.driver_id");

        List list = session.createSQLQuery(stringBuilder.toString()).list();

        Iterator it = list.iterator();
        while(it.hasNext()) {
            Object row[] = (Object[]) it.next();
            DriverTimeWorkInfo info = new DriverTimeWorkInfo();
            info.setSumStayBusy(DateTimeUtils.splitToComponentTimes(new BigDecimal(Integer.parseInt(row[0].toString()))));
            info.setSumStateOnline(DateTimeUtils.splitToComponentTimes(new BigDecimal(Integer.parseInt(row[1].toString()))));
            info.setSumTimeSecPayRest(DateTimeUtils.splitToComponentTimes(new BigDecimal(Integer.parseInt(row[2].toString()))));
            info.setSumTimeSecRest(DateTimeUtils.splitToComponentTimes(new BigDecimal(Integer.parseInt(row[3].toString()))));
            info.setSumTimeSecWork(DateTimeUtils.splitToComponentTimes(new BigDecimal(Integer.parseInt(row[4].toString()))));
            info.setDriverInfo(ModelsUtils.toModel(driverRepository.findOne(Long.parseLong(row[5].toString()))));
            response.getDriverTimeWorkInfos().add(info);
        }
          return response;
    }










    public DriverTimeWorkAndMissionCompleteStatResponse driverTimeWorkAndMissionCompleteStat(long driverId, String startTime, String endTime, Long taxoparkId){
        DriverTimeWorkAndMissionCompleteStatResponse response = new DriverTimeWorkAndMissionCompleteStatResponse();
        List<DriverTimeWorkInfo> driverTimeWorkInfos = new ArrayList<>();
        Session session = entityManager.unwrap(Session.class);
        StringBuilder stringBuilder = new StringBuilder();

        DateTime start = LocalDate.parse(startTime).toDateTimeAtStartOfDay();
        DateTime end = LocalDate.parse(endTime).toDateTimeAtStartOfDay().plusDays(1);

        stringBuilder.append(
                "SELECT sum(dtw.sec_stay_busy) as sumStayBusy,\n" +
                        "sum(dtw.sec_state_online) as sumStateOnline,\n" +
                        "sum(dtw.time_sec_pay_rest) as sumTimeSecPayRest, \n" +
                        "sum(dtw.time_sec_rest) as sumTimeSecRest,\n" +
                        "sum(dtw.time_sec_work) as sumTimeSecWork,\n" +
                        "dtw.driver_id as driver \n"
        );
        stringBuilder.append(" FROM driver_time_work dtw join driver drv on dtw.driver_id=drv.id where 1=1");

        if(driverId!=0){
            stringBuilder.append(" and dtw.driver_id = " + driverId);
        }
        if(taxoparkId!=null){
            stringBuilder.append(" and drv.taxopark_id = " + taxoparkId);
        }
        if(!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime)){
            stringBuilder.append(" and dtw.work_date BETWEEN '" + start + "' and '" + end + "'"); // LocalDate.parse(startTime)
        }
        stringBuilder.append(" group by dtw.driver_id");

        List list = session.createSQLQuery(stringBuilder.toString()).list();
        Iterator it = list.iterator();
        while(it.hasNext()) {
            Object row[] = (Object[]) it.next();
            DriverTimeWorkInfo info = new DriverTimeWorkInfo();
            info.setSumStayBusy(DateTimeUtils.splitToComponentTimes(new BigDecimal(Integer.parseInt(row[0].toString()))));
            info.setSumStateOnline(DateTimeUtils.splitToComponentTimes(new BigDecimal(Integer.parseInt(row[1].toString()))));
            info.setSumTimeSecPayRest(DateTimeUtils.splitToComponentTimes(new BigDecimal(Integer.parseInt(row[2].toString()))));
            info.setSumTimeSecRest(DateTimeUtils.splitToComponentTimes(new BigDecimal(Integer.parseInt(row[3].toString()))));
            info.setSumTimeSecWork(DateTimeUtils.splitToComponentTimes(new BigDecimal(Integer.parseInt(row[4].toString()))));
            info.setDriverInfo(ModelsUtils.toModel(driverRepository.findOne(Long.parseLong(row[5].toString()))));
            driverTimeWorkInfos.add(info);
        }

        StringBuilder stringBuilderCount;
        StringBuilder stringBuilderSum;
        for(DriverTimeWorkInfo driverTimeWorkInfo: driverTimeWorkInfos){
            DriverTimeWorkAndMissionCompleteInfo info = new DriverTimeWorkAndMissionCompleteInfo();
             info.setDriverTimeWorkInfo(driverTimeWorkInfo);

             stringBuilderCount = new StringBuilder();
             stringBuilderSum = new StringBuilder();

             stringBuilderCount.append(" SELECT count(*) from mission where state='COMPLETED' and test_order=0 ");
             stringBuilderSum.append(" SELECT sum(price_in_fact_amount) from mission where state='COMPLETED' and test_order=0");

             stringBuilderCount.append(" and driverInfo_id = " + driverTimeWorkInfo.getDriverInfo().getId());
             stringBuilderSum.append(" and driverInfo_id = " + driverTimeWorkInfo.getDriverInfo().getId());
             if(!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime)){
                 stringBuilderCount.append(" and time_finishing BETWEEN '" + start + "' and '" + end + "'");
                 stringBuilderSum.append(" and time_finishing BETWEEN '" + start + "' and '" + end + "'");
             }

             BigInteger countMission = (BigInteger)session.createSQLQuery(stringBuilderCount.toString()).uniqueResult();
             info.setCountMission(countMission!=null ? countMission.intValue(): 0);
             BigDecimal sumMission = (BigDecimal)session.createSQLQuery(stringBuilderSum.toString()).uniqueResult();
             info.setSumMission(sumMission!=null ? sumMission.intValue(): 0);
             response.getDriverTimeWorkAndMissionCompleteInfos().add(info);
        }
        return response;
    }







    public ActivateTariffResponse activateTariff(String security_token, long clientId, long expirationDate, boolean active, boolean activated){
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser == null){
           throw new CustomException(1, "Web user not found");
        }
        Client client = clientRepository.findOne(clientId);
        if(client == null){
            throw new CustomException(2, "Client not found");
        }
        if(missionRepository.findCountClientByMissionState(client, Mission.State.COMPLETED)==0){
            throw new CustomException(4, "Активация отклонена! У данного клиента нет завершенных поездок.");
        }
        ActivateTariffResponse response = new ActivateTariffResponse();
        PrivateTariff privateTariff = privateTariffRepository.findByClientAndTariffName(client, AutoClass.BONUS.name());
        if(privateTariff == null){
            privateTariff = new PrivateTariff();
            privateTariff.setActivationDate(DateTimeUtils.nowNovosib_GMT6());
            privateTariff.setActivated(true);
            privateTariff.setTariffName(AutoClass.BONUS.name());
            privateTariff.setClient(client);
        }
            privateTariff.setActive(active);
            privateTariff.setActivated(activated);
        if(expirationDate!=0){
            privateTariff.setExpirationDate(DateTimeUtils.toDateTime(expirationDate));
        }
        if(activated){
            privateTariff.setActivationDate(DateTimeUtils.nowNovosib_GMT6());
        }
        privateTariffRepository.save(privateTariff);

        mongoDBServices.createEvent(0, "" + webUser.getId(), 3, 0, "activateBONUS", "", "");
        //mongoDBServices.createEvent(0, "" + webUser.getId(), 3, "activateBONUS", "", "", clientId, 0);
          return response;
    }




    // список пользователей с тарифом БОНУС
    public PrivateTariffResponse privateTariff(String security_token, long clientId){
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser == null){
            throw new CustomException(1, "Web user not found");
        }
        PrivateTariffResponse response = new PrivateTariffResponse();
        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(PrivateTariff.class);

        if(clientId!=0){
            if(clientRepository.findOne(clientId) == null){
                throw new CustomException(2, String.format("Client id=%s not found", clientId));
            }
            criteria.add(Restrictions.eq("client.id", clientId));
        }
        criteria.add(Restrictions.eq("tariffName", AutoClass.BONUS.name()));
        List<PrivateTariff> clientsWithPrivateTariff = criteria.list();
        if(!CollectionUtils.isEmpty(clientsWithPrivateTariff)){
            for(PrivateTariff privateTariff: clientsWithPrivateTariff){
                response.getPrivateTariffInfos().add(ModelsUtils.toModel(privateTariff));
            }
        }
           return response;
    }









    public Location getDriverLocation(Driver driver){
         DriverLocation location = locationRepository.findByDriver(driver);
           return location.getLocation();
    }



    private void missionAssignedForFantomDriver(DriverLocation driverLocation, Mission mission, double distance) throws JSONException, IOException {
        /* делаю пока без списка и сортировки, назначаю первого попавшегося*/
        JSONObject notifyClient = new JSONObject();
        JSONObject locationJson = new JSONObject();
        notifyClient.put("missionId", mission.getId());

        Random rn = new Random();
        int max = Integer.parseInt(commonService.getPropertyValue("max_random_arrival_time"));
        int min = Integer.parseInt(commonService.getPropertyValue("min_random_arrival_time"));
        int randomTimeArrival = rn.nextInt(max - min + 1) + min;

        //int calculateTimeArrival = calculateArrivalTime(mission, driverLocation.getDriver());

        notifyClient.put("arrivalTime", randomTimeArrival);
        locationJson.put("latitude", driverLocation.getLocation().getLatitude());
        locationJson.put("longitude", driverLocation.getLocation().getLongitude());
        locationJson.put("driverId", driverLocation.getDriver().getId());
        locationJson.put("distance", distance);
        locationJson.put("angle", -90);
        notifyClient.put("location", locationJson);
        nodeJsService.notified("mission_assign", notifyClient);
    }





    /*
    28.07.2015

    sb.append("select \n" +
                "count(\n" +
                "case when (((unix_timestamp(can.time_of_canceled))-(unix_timestamp(m.time_requesting)))>30 and (select count(*) from mission m2 \n" +
                "join driver drv on drv.id = m2.driverInfo_id\n" +
                "where m2.clientInfo_id=m.clientInfo_id and m2.driverInfo_id is not null and (unix_timestamp(m2.time_requesting)+21600) between (unix_timestamp(m.time_requesting)+21600) and (unix_timestamp(m.time_requesting)+25200)\n" +
                ")=0) then 1 end) as count,\n" +
                " DATE_FORMAT(m.time_requesting, '%Y-%m-%d') as timeRequest,  \n" +
                " region_id as region\n"+
                "  from mission m\n" +
                "  join mission_canceled can on can.mission_id = m.id\n" +
                "  where can.time_of_canceled is not null and can.cancel_by='client' and m.test_order=0 "); // and m.region_id is not null

        if(startTime!=0 || endTime!=0){
            sb.append(" and (unix_timestamp(can.time_of_canceled)+21600) between "+startTime + " and " + endTime);
        }
        if(regionId!=0){
            sb.append(" and m.region_id=" + regionId);
        }
        if(groupByRegion){
            sb.append(" group by m.region_id");
        }else {
            sb.append(" group by timeRequest having count>0 order by timeRequest desc");
        }
    */


    /*
    15.08.15

        sb.append("select count(*) as count, DATE_FORMAT(m.time_requesting, '%Y-%m-%d') as timeRequest, m.region_id as region from mission m\n" +
                "    join driver drv on drv.id = m.driverInfo_id " +
                (taxoparkId!=null ? " and drv.taxopark_id="+taxoparkId+" \n":" ") +
                "    join mission_canceled can on can.mission_id=m.id \n" +
                "    where m.driverInfo_id is not null \n" +
                "    and can.time_of_canceled is not null and can.cancel_by='client' and m.test_order=0 \n"+
                "    and (unix_timestamp(m.time_requesting)+21600) between (unix_timestamp(m.time_requesting)+21600) and (unix_timestamp(m.time_requesting)+25200) ");
        if(startTime!=0 || endTime!=0){
            sb.append(" and (unix_timestamp(can.time_of_canceled)+21600) between "+startTime + " and " + endTime);
            sb.append(" and (unix_timestamp(m.time_requesting)+21600) between "+startTime + " and " + endTime);
        }
            sb.append(" and (unix_timestamp(can.time_of_canceled))-(unix_timestamp(m.time_requesting))>30 ");
        if(regionId!=0){
            sb.append(" and m.region_id=" + regionId);
        }
        if(groupByRegion){
            sb.append(" group by m.region_id ");
        }
     */

    public MissionByRegionResponse missionByRegion(long startTime, long endTime, long regionId, String groupBy, Long taxoparkId){
        MissionByRegionResponse response = new MissionByRegionResponse();
        Session session = entityManager.unwrap(Session.class);
        StringBuilder sb = new StringBuilder();
        boolean groupByRegion = groupBy.equals("region");

        /*
        select count(*), DATE_FORMAT(m.time_requesting, '%Y-%m-%d') as timeRequest, m.region_id as region from mission m
join driver drv on drv.id = m.driverInfo_id
join mission_canceled can on can.mission_id=m.id
where m.driverInfo_id is not null
and can.time_of_canceled is not null and can.cancel_by='client'
and (unix_timestamp(m.time_requesting)+21600) between (unix_timestamp(m.time_requesting)+21600) and (unix_timestamp(m.time_requesting)+25200)
and m.test_order=0
and (unix_timestamp(can.time_of_canceled)+21600) between 1436914800 and 1437433199
and (unix_timestamp(can.time_of_canceled))-(unix_timestamp(m.time_requesting))>30
and (unix_timestamp(m.time_requesting)+21600) between 1436914800 and 1437433199 group by region
         */


        sb.append("select count(*), DATE_FORMAT(m.time_requesting, '%Y-%m-%d') as timeRequest, m.region_id as region from mission m\n" +
                "    join mission_canceled can on can.mission_id=m.id and can.cancel_by='client' and can.time_of_canceled is not null\n" +
                (taxoparkId!=null ? " join driver drv on drv.id=m.driverInfo_id and drv.taxopark_id="+taxoparkId+"\n" :"") +
                "    where\n" +
                "    m.test_order=0 and \n" +
                "    (select count(*) from mission m2\n" +
                "where (unix_timestamp(m2.time_requesting)+21600) between "+startTime + " and " + endTime+" \n" +
                " and m2.clientInfo_id=m.clientInfo_id and m2.driverInfo_id is not null and m2.test_order=0 and\n" +
                "(unix_timestamp(m2.time_requesting)+21600) between (unix_timestamp(m.time_requesting)+21600) and (unix_timestamp(m.time_requesting)+25200))!=0\n" +
                "\n" +
                "    and (unix_timestamp(can.time_of_canceled)+21600) between "+startTime + " and " + endTime+" \n");
        sb.append(" and (unix_timestamp(can.time_of_canceled))-(unix_timestamp(m.time_requesting))>30 \n");
        if(regionId!=0){
            sb.append(" and m.region_id=" + regionId);
        }
        if(groupByRegion){
            sb.append(" group by m.region_id ");
        }


        LOGGER.info("Query missionByRegion: "+sb.toString());

        List listValue = session.createSQLQuery(sb.toString()).list();
        Iterator it = listValue.iterator();


        Map<Object, MissionStatisticByRegionInfo> hashMap = new HashMap();

        MissionStatisticByRegionInfo info;
        while(it.hasNext()) {
            info = new MissionStatisticByRegionInfo();
            Object row[] = (Object[]) it.next();
            int count = ((BigInteger) row[0]).intValue();
            long id = row[2]!=null ? ((BigInteger) row[2]).longValue(): -1;
            String date = row[1]!=null ? row[1].toString() : "";
            info.setCountCanceledMission(count);
            info.setRegionInfo(groupByRegion?ModelsUtils.toModel(regionRepository.findOne(id), true):null);
            info.setDate(date);
            hashMap.put(groupByRegion ? id : date, info);
        }
        sb.setLength(0);

        // join driver drv on drv.id=m.driverInfo_id
        sb.append("select count(*), m.region_id, DATE_FORMAT(m.time_finishing, '%Y-%m-%d') from mission m "); // and region_id is not null
        sb.append(" join driver drv on drv.id=m.driverInfo_id ");
        sb.append(" where m.state='COMPLETED' and m.test_order=0 ");
        if(regionId!=0){
            sb.append(" and m.region_id=" + regionId);
        }
        if(startTime!=0 || endTime!=0){
            sb.append(" and (unix_timestamp(m.time_finishing)+21600) between "+startTime + " and " + endTime);
        }
        if(taxoparkId!=null) {
            sb.append(" and drv.taxopark_id=" + taxoparkId);
        }
        if(groupByRegion){
            sb.append(" group by m.region_id");
        }else {
            sb.append(" group by DATE_FORMAT(m.time_finishing, '%Y-%m-%d') order by m.time_finishing desc");
        }


        List completeList = session.createSQLQuery(sb.toString()).list();
        Iterator completeListIterator = completeList.iterator();
        while(completeListIterator.hasNext()){
            Object row[] = (Object[]) completeListIterator.next();
            int countCompleted =  ((BigInteger)row[0]).intValue();
            long region_id = row[1]!=null ? ((BigInteger) row[1]).longValue(): -1;
            String date = row[2].toString();
            if(hashMap.containsKey(groupByRegion?region_id:date)){
                info =  hashMap.get(groupByRegion?region_id:date);
            }else{
                info = new MissionStatisticByRegionInfo();
                info.setRegionInfo(groupByRegion?ModelsUtils.toModel(regionRepository.findOne(region_id), true):null);
            }
            info.setCountCompletedMission(countCompleted);
            info.setDate(date);
            hashMap.put(groupByRegion?region_id:date, info);
        }

        hashMap = !groupByRegion?MapUtils.sortByKey(hashMap):hashMap;

        for(Map.Entry<Object, MissionStatisticByRegionInfo> entry : hashMap.entrySet()) {
            response.getMissionStatisticByRegionInfos().add(entry.getValue());
        }

        return response;
    }




    @Transactional
    public FantomDriverResponse setFantomDriver(Long missionId, Long clientId) throws JSONException, IOException, ExecutionException, InterruptedException {
        FantomDriverResponse response = new FantomDriverResponse();
        if(missionId != null && clientId != null){
            Mission mission = missionRepository.findOne(missionId);
            Client client = clientRepository.findOne(clientId);

                if(mission!=null && client!=null && client.getMission()!=null &&  mission.getClientInfo()!=null && mission.getClientInfo().getId().equals(client.getId()) && client.getMission().getId().equals(mission.getId())){

                  MissionFantomDriver missionFantomDriver =  missionFantomDriverRepository.findByMission(mission);
                   List<MissionFantomDriver> missionFantomListByClient = missionFantomDriverRepository.findByMissionClientInfoAndTimeOfAssigningBetween(client, DateTimeUtils.nowNovosib_GMT6().minusHours(1), DateTimeUtils.nowNovosib_GMT6());

                    if(mission.getDriverInfo() != null || missionFantomDriver!=null || !CollectionUtils.isEmpty(missionFantomListByClient)){
                       // на текущую миссию уже назначен водитель
                       throw new CustomException(1, String.format("Водитель назначен или за последний час на клиента id=%s назначался фантом", clientId));
                    }else{
                        List<Driver> listFantom = driverRepository.findByTypeXAndStateNotAndCurrentMissionIsNull(Boolean.TRUE, Driver.State.OFFLINE);
                        LOGGER.info("^^^^^^^^^^^^^^^^^^^^^  listFantom ="+listFantom);
                        if(CollectionUtils.isEmpty(listFantom)){
                             return response;
                        }

                    List<DriverLocation> driverLocations = locationRepository.findByDriverIn(listFantom);
                          if(!CollectionUtils.isEmpty(driverLocations)){
                              for(DriverLocation driverLocation :driverLocations){
                                    Location location = driverLocation.getLocation();
                                    double distance = GeoUtils.distance(mission.getLocationFrom().getLatitude(), mission.getLocationFrom().getLongitude(), location.getLatitude(), location.getLongitude());
                                           if (distance < 3) {
                                               if(missionFantomDriverRepository.findByMission(mission) == null){
                                                   /* save fantom stat */
                                                   MissionFantomDriver fantom = new MissionFantomDriver(DateTimeUtils.nowNovosib_GMT6(), mission, driverLocation.getDriver());
                                                   missionFantomDriverRepository.save(fantom);

                                                   /* assigned mission */
                                                   missionAssignedForFantomDriver(driverLocation, mission, distance);

                                                   /* log event */
                                                   //mongoDBServices.createEvent(1, "" + driverLocation.getDriver().getId(), 3, "setFantomDriver", "", "", client.getId(), driverLocation.getDriver().getId());
                                                   mongoDBServices.createEvent(1, ""+driverLocation.getDriver().getId(), 3, mission.getId(), "setFantomDriver", "clientId:"+clientId, "");

                                                   /* запускаю поиск водителя */
                                                   findDriversService.findDriversWhenMissionWithFantomDriver(mission);
                                                   break;
                                               }
                                           }
                                  }
                          }
                    }
                }
        }
          return response;
    }




    public TariffRestrictionResponse tariffRestriction(){
        TariffRestrictionResponse response = new TariffRestrictionResponse();
        for(TariffRestriction tariffRestriction: tariffRestrictionRepository.findAll()){
            response.getTariffRestrictionInfos().add(ModelsUtils.toModel(tariffRestriction));
        }
        String off = commonService.getPropertyValue("low_coster_off");
        if(!StringUtils.isEmpty(off)){
              if(off.equals("1")){
                  response.setOff(true);
              } else if(off.equals("0")){
                  response.setOff(false);
              }
        }
          return response;
    }






    public UpdateTariffRestrictionResponse updateTariffRestriction(List<TariffRestrictionInfo> tariffRestrictionInfos, boolean isOff){
        UpdateTariffRestrictionResponse response = new UpdateTariffRestrictionResponse();
        Properties properties = propertiesRepository.findByPropName("low_coster_off");
        if(properties != null){
             properties.setPropValue(isOff ? "1": "0");
             propertiesRepository.save(properties);
        }
        List<TariffRestriction> tariffRestrictions = new ArrayList<>();
        for(TariffRestrictionInfo info: tariffRestrictionInfos){
              if(info.getId() == 0){
                 // add
                   if(tariffRestrictionRepository.countRestriction()>=2){
                       throw new CustomException(1, "Количество ограничений на тариф не должно превышать 2");
                   }
                      tariffRestrictions.add(ModelsUtils.fromModel(info, null));
              } else{
                  // upd
                   TariffRestriction tariffRestriction = tariffRestrictionRepository.findOne(info.getId());
                      if(tariffRestriction == null){
                          throw new CustomException(2, "Тариф не найден");
                      }
                      tariffRestrictions.add(ModelsUtils.fromModel(info, tariffRestriction));
              }
        }
        if(!CollectionUtils.isEmpty(tariffRestrictions)){
            tariffRestrictionRepository.save(tariffRestrictions);
        }
          return response;
    }






    public FantomStatResponse fantomStat(long clientId, long fantomDriverId, long regionId, String state, long startTime, long endTime, int numPage, int pageSize){
        FantomStatResponse response = new FantomStatResponse();

        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(MissionFantomDriver.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        //Criteria crit = session.createCriteria(MissionFantomDriver.class);
        criteria.createAlias("mission", "mission");
        //crit.createAlias("mission", "mission");

        if(fantomDriverId!=0){
            criteria.add(Restrictions.eq("fantomDiver.id", fantomDriverId));
            //crit.add(Restrictions.eq("fantomDiver.id", fantomDriverId));
        }
        if(clientId!=0){
            criteria.add(Restrictions.eq("mission.clientInfo.id", clientId));
            //crit.add(Restrictions.eq("mission.clientInfo.id", clientId));
        }
        if(regionId!=0){
            criteria.add(Restrictions.eq("mission.region.id", regionId));
            //crit.add(Restrictions.eq("mission.region.id", regionId));
        }

        //DateTime newD1 = new DateTime(new Date(startTime * 1000));
        //DateTime newD2 = new DateTime(new Date(endTime * 1000));

        criteria = QueryUtils.fillDateTimeParameter(criteria, "timeOfAssigning", startTime, endTime);
        /*
        if (startTime != 0 && endTime != 0) {
              criteria.add(Restrictions.ge("timeOfAssigning", newD1));
              criteria.add(Restrictions.lt("timeOfAssigning", newD2));

            crit.add(Restrictions.ge("timeOfAssigning", newD1));
            crit.add(Restrictions.lt("timeOfAssigning", newD2));
        } else if (startTime != 0 && endTime == 0) {
              criteria.add(Restrictions.ge("timeOfAssigning", newD1));
              crit.add(Restrictions.ge("timeOfAssigning", newD1));
        } else if (startTime == 0 && endTime != 0) {
              criteria.add(Restrictions.lt("timeOfAssigning", newD2));
              crit.add(Restrictions.lt("timeOfAssigning", newD2));
        }
        */



        if(!StringUtils.isEmpty(state)) {
            criteria.add(Restrictions.eq("mission.state", Mission.State.valueOf(state)));
            //crit.add(Restrictions.eq("mission.state", Mission.State.valueOf(state)));
        }
        criteria.add(Restrictions.eq("mission.testOrder", Boolean.FALSE));
        //crit.add(Restrictions.eq("mission.testOrder", Boolean.FALSE));


        criteria.addOrder(Order.desc("timeOfAssigning"));

        criteria.setProjection(Projections.rowCount());
        Long total = (Long)criteria.uniqueResult();
        criteria.setProjection(null);

        response.setTotalItems(total);

        long lastPageNumber = ((total / pageSize) + 1);
        response.setLastPageNumber(lastPageNumber);

        List<MissionFantomDriver> missionFantomDriverListAll = criteria.list();

        criteria.setFirstResult((numPage - 1) * pageSize);
        criteria.setMaxResults(pageSize);

        List<MissionFantomDriver> missionFantomDriverListWithPagging = criteria.list();

        if(!CollectionUtils.isEmpty(missionFantomDriverListAll)){
            for(MissionFantomDriver fantom :missionFantomDriverListAll){
                List<ClientCashFlow> clientCashFlow = clientCashFlowRepository.findByMissionAndClientAndOperation(fantom.getMission(), fantom.getMission().getClientInfo(), 20);
                    if(!CollectionUtils.isEmpty(clientCashFlow)){
                       response.setGeneralSorrySum(response.getGeneralSorrySum() + 100);
                    }
                response.setGeneralIncreaseSum(response.getGeneralIncreaseSum()+fantom.getSumIncrease());
                response.setGeneralMissionSum(response.getGeneralMissionSum()+fantom.getMission().getStatistics().getPriceInFact().getAmount().intValue());
            }
        }


        if(!CollectionUtils.isEmpty(missionFantomDriverListWithPagging)) {
            for (MissionFantomDriver missionFantomDriver : missionFantomDriverListWithPagging) {
                FantomStatInfo info = ModelsUtils.toModel(missionFantomDriver);
                List<ClientCashFlow> clientCashFlow = clientCashFlowRepository.findByMissionAndClientAndOperation(missionFantomDriver.getMission(), missionFantomDriver.getMission().getClientInfo(), 20);
                  if(!CollectionUtils.isEmpty(clientCashFlow)){
                      info.setSorrySum(100);
                  }
                  MissionCanceled missionCanceled = missionCanceledRepository.findByMissionId(missionFantomDriver.getMission().getId());
                  if(missionCanceled!=null){
                      info.setCancelBy(missionCanceled.getCancelBy());
                  }
                response.getFantomStatInfos().add(info);
            }
        }

         return response;
    }



    public FantomUpdateResponse fantomUpdate(List<FantomInfo> fantomInfos){
        FantomUpdateResponse response = new FantomUpdateResponse();
        for(FantomInfo info: fantomInfos){
            if(info.getDriverInfoARM()!=null){
                Driver driver = driverRepository.findOne(info.getDriverInfoARM().getId());
                if(driver == null){
                    throw new CustomException(1, "Водитель не найден");
                }
                if(info.getItemLocation()!=null){
                    DriverLocation driverLocation =  locationRepository.findByDriver(driver);
                        if(driverLocation == null){
                            throw new CustomException(1, "Текущие координаты водителя не найдены");
                        }
                    driverLocation.getLocation().setLatitude(info.getItemLocation().getLatitude());
                    driverLocation.getLocation().setLongitude(info.getItemLocation().getLongitude());
                    locationRepository.save(driverLocation);
                }
                if(info.isActive()!=null){
                    driver.setState(info.isActive()?Driver.State.AVAILABLE:Driver.State.OFFLINE);
                    driverRepository.save(driver);
                }
            }
        }
          return response;
    }



    public FantomResponse fantoms(long driverId){
        FantomResponse response = new FantomResponse();
        List<Driver> driverList = new ArrayList<>();
        if(driverId==0){
            driverList = driverRepository.findByTypeXOrderByStateAsc(true);
            for(Driver driver: driverList){
                DriverLocation driverLocation = locationRepository.findByDriver(driver);
                response.getFantomInfos().add(ModelsUtils.toModel(driver, driverLocation.getLocation()));
            }
        } else{
            Driver driver = driverRepository.findByTypeXAndIdOrderByStateAsc(true, driverId);
            if(driver==null){
                throw new CustomException(1, "Водитель не найден");
            }
            DriverLocation driverLocation = locationRepository.findByDriver(driver);
            response.getFantomInfos().add(ModelsUtils.toModel(driver, driverLocation.getLocation()));

        }

        return response;
    }



    public SupportPhones loadSupportPhones() {
        SupportPhones result = new SupportPhones();
        result.getPhones().put(SupportPhoneType.FORGET_THINGS, new SupportPhone("+79138996188", "88000"));
        result.getPhones().put(SupportPhoneType.UNACCEPTIBLE_PRICE, new SupportPhone("+79130675460", "88001"));
        result.getPhones().put(SupportPhoneType.CARD_OR_PRO_PROBLEM, new SupportPhone("+79130675249", "88002"));
        result.getPhones().put(SupportPhoneType.COMPLAINT, new SupportPhone("+79130674834", "88003"));
        result.getPhones().put(SupportPhoneType.BREAKED_CAR, new SupportPhone("+79130673749", "88004"));
        result.getPhones().put(SupportPhoneType.RECALL, new SupportPhone("+79130674239", "88005"));
        result.getPhones().put(SupportPhoneType.HAVE_NO_CHECK, new SupportPhone("+79130674135", "88006"));
        result.getPhones().put(SupportPhoneType.CUSTOM, new SupportPhone("+79130674684", "88007"));
        return result;
    }



}


