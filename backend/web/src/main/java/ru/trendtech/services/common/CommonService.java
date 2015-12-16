package ru.trendtech.services.common;

import com.google.common.collect.Lists;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.graylog2.GelfMessage;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.Seconds;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import ru.trendtech.common.mobileexchange.model.common.*;
import ru.trendtech.common.mobileexchange.model.common.web.autocomplete.WebAutoCompleteResponse;
import ru.trendtech.common.mobileexchange.model.common.web.autocomplete.WebAutocompleteInfo;
import ru.trendtech.common.mobileexchange.model.courier.*;
import ru.trendtech.common.mobileexchange.model.courier.web.DefaultPriceResponse;
import ru.trendtech.common.mobileexchange.model.web.GlobalClientStatsInfo;
import ru.trendtech.common.mobileexchange.model.web.GlobalClientStatsResponse;
import ru.trendtech.domain.*;
import ru.trendtech.domain.AutoClass;
import ru.trendtech.domain.Properties;
import ru.trendtech.domain.billing.AutoClassPrice;
import ru.trendtech.domain.billing.MissionRate;
import ru.trendtech.domain.billing.PaymentType;
import ru.trendtech.domain.courier.*;
import ru.trendtech.domain.courier.Comment;
import ru.trendtech.models.ModelsUtils;
import ru.trendtech.repositories.*;
import ru.trendtech.repositories.billing.MissionRatesRepository;
import ru.trendtech.repositories.courier.*;
import ru.trendtech.services.administration.AdministrationService;
import ru.trendtech.services.notifications.node.NodeJsNotificationsService;
import ru.trendtech.services.notifications.push.App42PushNotificationService;
import ru.trendtech.services.sms.ServiceSMSNotification;
import ru.trendtech.utils.*;
import java.io.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * Created by petr on 29.06.2015.
 */
@Service
@Transactional
public class CommonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonService.class);
    private AtomicInteger count = new AtomicInteger();
    private final String YES = "yes";
    private final String GIS_URL_STREET_MAP = "http://catalog.api.2gis.ru/2.0/geo/search?fields=items.geometry.centroid,items.adm_div,items.address&radius=250&type=street,building&region_id=1&key="; // ,building
    private final String GIS_URL_ORGANISATION_MAP = "http://catalog.api.2gis.ru/2.0/catalog/branch/search?fields=items.geometry.centroid,items.adm_div,items.address&radius=250&type=street,building&region_id=1&key="; // ,building
    private final String GIS_URL_GEO_POINT_BY_ADDRESS = "http://catalog.api.2gis.ru/2.0/geo/search?fields=items.geometry.centroid&radius=200&type=street,building,attraction&page_size=50&region_id=1&key=";
    //private final String GIS_URL_ADDRESS_BY_GEO_POINT = "http://catalog.api.2gis.ru/2.0/geo/search?fields=items.geometry.centroid,items.adm_div,items.address&region_id=1&key="; boris
    private final String GIS_URL_ADDRESS_BY_GEO_POINT = "http://catalog.api.2gis.ru/2.0/geo/search?type=building&radius=250&fields=items.geometry.centroid,items.adm_div,items.address&region_id=1&key="; // for ANDROID
    private final String NSK = "г. Новосибирск";

    @Value("${2gis.client.api.key}")
    private String GIS_API_KEY = "";
    @Autowired
    private VersionsAppRepository versionsAppRepository;
    @Autowired
    private ReasonRepository reasonRepository;
    @Autowired
    private MissionRatesRepository missionRatesRepository;
    @Autowired
    private PrivateTariffRepository privateTariffRepository;
    @Autowired
    private ServicesRepository servicesRepository;
    @Autowired
    private PropertiesRepository propertiesRepository;
    @Autowired
    private DefaultPriceRepository defaultPriceRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private PriceChangesRepository priceChangesRepository;
    @Autowired
    private NodeJsNotificationsService nodeJsNotificationsService;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemPropertyRepository itemPropertyRepository;
    @Autowired
    private CustomWindowRepository customWindowRepository;
    @Autowired
    private LateOrderRepository lateOrderRepository;
    @Autowired
    private App42PushNotificationService app42PushNotificationService;
    @Autowired
    private ServiceSMSNotification serviceSMSNotification;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private AdministrationService administrationService;




    public void restartSearchCourier(){
        List<Order> confirmedOrders = orderRepository.findByState(Order.State.CONFIRMED);
        if(!CollectionUtils.isEmpty(confirmedOrders)){
              for(Order order: confirmedOrders){
                  DateTime timeRequesting = order.getTimeOfRequesting();
                  DateTime now = DateTimeUtils.nowNovosib_GMT6();
                  if(timeRequesting != null){

                  }
              }
        }
    }




    public void sendEvent(long missionId, boolean result, String answer){
        nodeJsNotificationsService.sendMissionPaymentResult(missionId, result, answer);
    }





    public AtomicInteger getCount() {
        return count;
    }

    public void setCount(AtomicInteger count) {
        this.count = count;
    }






    private boolean checkTimeAlcohol(DateTime targetTime) {
        boolean result = false;

        DateTime start = DateTimeUtils.nowNovosib_GMT6();
        DateTime end = DateTimeUtils.nowNovosib_GMT6();

        int startHours = 22;
        int startMinutes = 00;
        int endHours = 9;
        int endMinutes = 00;

        start = start.withHourOfDay(startHours);
        start = start.withMinuteOfHour(startMinutes);
        end = end.withHourOfDay(endHours);
        end = end.withMinuteOfHour(endMinutes);

        if (targetTime.isBefore(end.getMillis()) && targetTime.isAfter(start.getMillis())) {
            result = true;
        }
        return result;
    }


    public CustomWindowInfo checkCourierServiceRestrictionTime(DateTime timeOfFinish) {
        CustomWindowInfo customWindowInfo = null;

        String startTime = getPropertyValue("courier_resrtiction_start_time");
        String endTime = getPropertyValue("courier_resrtiction_end_time");

        //DateTime targetTime = DateTimeUtils.nowNovosib_GMT6();

        DateTime start = timeOfFinish; //DateTimeUtils.nowNovosib_GMT6();
        DateTime end = timeOfFinish; //DateTimeUtils.nowNovosib_GMT6();

        String first[] = startTime.split(":");
        String second[] = endTime.split(":");

        int startHours = Integer.parseInt(first[0]);
        int startMinutes = Integer.parseInt(first[1]);
        int endHours = Integer.parseInt(second[0]);
        int endMinutes = Integer.parseInt(second[1]);


        start = start.withHourOfDay(startHours);
        start = start.withMinuteOfHour(startMinutes);
        end = end.withHourOfDay(endHours);
        end = end.withMinuteOfHour(endMinutes);

        DateTime startDay = timeOfFinish.withTimeAtStartOfDay(); // 00 часовб 00 минут
        DateTime startDayPlusEleven = startDay.plusHours(endHours);
        DateTime endDay = startDay.plusDays(1);
        DateTime endDayMinusOne = endDay.minusHours(1);

        LOGGER.info("startDay = " + startDay+" startDayPlusEleven="+startDayPlusEleven +" endDay="+endDay +" endDayMinusOne="+endDayMinusOne + " timeOfFinish="+timeOfFinish);
        LOGGER.info("timeOfFinish.isBefore(startDayPlusEleven.getMillis()): " + (timeOfFinish.isBefore(startDayPlusEleven.getMillis())));
        LOGGER.info("timeOfFinish.isAfter(startDay.getMillis())): " + (timeOfFinish.isAfter(startDay.getMillis())));
        LOGGER.info("timeOfFinish.isBefore(endDay.getMillis(): " + timeOfFinish.isBefore(endDay.getMillis()));
        LOGGER.info("timeOfFinish.isAfter(endDayMinusOne.getMillis()): " + timeOfFinish.isAfter(endDayMinusOne.getMillis()));

        //end = end.plusDays(1);
        if((timeOfFinish.isBefore(startDayPlusEleven.getMillis()) && timeOfFinish.isAfter(startDay.getMillis())) || (timeOfFinish.isBefore(endDay.getMillis()) && timeOfFinish.isAfter(endDayMinusOne.getMillis()))){
            customWindowInfo = ModelsUtils.toModel(customWindowRepository.findByTypeWindow(TypeWindow.SERVICE_UNAVAILABLE));
            customWindowInfo.setContentText(String.format(customWindowInfo.getContentText(), endHours+":00", startHours+":00"));
            throw new CustomException(1, String.format("Сервис Zavezu работает с\n %s до %s", endHours+":00", startHours+":00"));
        }

        //LOGGER.info("CheckCourierServiceRestrictionTime | start: " + start + " end: " + end);
        //LOGGER.info("timeOfFinish.isBefore(end.getMillis()) is: " + timeOfFinish.isBefore(end.getMillis()));
        //LOGGER.info("timeOfFinish.isAfter(start.getMillis()) is: " + timeOfFinish.isAfter(start.getMillis()));
        //if (timeOfFinish.isBefore(end.getMillis()) && timeOfFinish.isAfter(start.getMillis())) {
        //   customWindowInfo = ModelsUtils.toModel(customWindowRepository.findByTypeWindow(TypeWindow.SERVICE_UNAVAILABLE));
        //   customWindowInfo.setContentText(String.format(customWindowInfo.getContentText(), endTime, startTime));
        //   throw new CustomException(1, String.format("Сервис Zavezu работает с\n %s до %s", endHours+":00", startHours+":00"));
        //}

        return customWindowInfo;
    }





    public boolean checkAlcohol(OrderInfo orderInfo, DateTime timeOfStart) {
        boolean result = false;
        List<ClientItemInfo> clientItemInfos = orderInfo.getClientItemInfos();
        if (!CollectionUtils.isEmpty(clientItemInfos)) {
            for (ClientItemInfo clientItemInfo : clientItemInfos) {
                ItemInfo itemInfo = clientItemInfo.getItemInfo();
                if (itemInfo != null && itemInfo.getId() != 0) {
                    Item item = itemRepository.findOne(itemInfo.getId());
                    if (!CollectionUtils.isEmpty(item.getItemPropertyId())) {
                        ItemProperty itemProperty;
                        for (Long id : item.getItemPropertyId()) {
                            itemProperty = itemPropertyRepository.findOne(id);
                            if (itemProperty != null) {
                                if (itemProperty.isAlcohol()) {
                                    if (checkTimeAlcohol(timeOfStart)) {
                                        result = true;
                                        return result;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }


    public ItemLocation getLocationByFirstOrderAddress(Set<OrderAddress> orderAddressSet) {
        List<OrderAddress> orderAddresses = new ArrayList<>(orderAddressSet);
        OrderAddress firstItem = orderAddresses.get(0);
        ItemLocation location = new ItemLocation();
        location.setLatitude(firstItem.getAddressLocation().getLatitude());
        location.setLongitude(firstItem.getAddressLocation().getLongitude());
        return location;
    }


    public int distanceFromCurrentCourierLocationToLastStore(ru.trendtech.domain.courier.Order order, Driver driver) {
        DriverLocation driverLocation = locationRepository.findByDriver(driver);
        if (driverLocation == null) {
            return 0;
        }
        String route = "";

        if (order != null) {
            StringBuilder stringBuilder = new StringBuilder();
            // текущие координаты водилы
            stringBuilder.append(driverLocation.getLocation().getLongitude() + " " + driverLocation.getLocation().getLatitude());
            // координаты магазинов
            if (!CollectionUtils.isEmpty(order.getOrderItemPrices())) {
                stringBuilder.append(",");
                for (OrderItemPrice orderItemPrice : order.getOrderItemPrices()) {
                    stringBuilder.append(orderItemPrice.getItemPrice().getStoreAddress().getStoreLocation().getLongitude() + " " + orderItemPrice.getItemPrice().getStoreAddress().getStoreLocation().getLatitude());
                }
            }
            route = stringBuilder.toString();
            LOGGER.info("Direction points [Current driver location -> stores]: " + route);
            int length = stringBuilder.length();
            // todo: послать если < 2
            LOGGER.info("Кол-во (lat, lon) точек для расчета дистанции: " + length);
        }
            CommonService.DirectionHelper directionHelper = routeBuildingFromLatLon2GIS(route);
            return directionHelper.getDistance();
    }









    public int calculateArrivalTime(Mission mission, ru.trendtech.domain.courier.Order order, Driver driver) {
        DriverLocation driverLocation = locationRepository.findByDriver(driver);
        if (driverLocation == null) {
            return -1;
        }
        double latitude;
        double longitude;
        String route = "";

        if (mission != null) {
            latitude = mission.getLocationFrom().getLatitude();
            longitude = mission.getLocationFrom().getLongitude();
            route = driverLocation.getLocation().getLongitude() + " " + driverLocation.getLocation().getLatitude() + "," + longitude + " " + latitude;
        }

        if (order != null) {
            StringBuilder stringBuilder = new StringBuilder();
            ItemLocation firstTargetLocation = getLocationByFirstOrderAddress(order.getTargetAddresses());
            latitude = firstTargetLocation.getLatitude();
            longitude = firstTargetLocation.getLongitude();

            // текущие координаты водилы
            stringBuilder.append(driverLocation.getLocation().getLongitude() + " " + driverLocation.getLocation().getLatitude());

            // координаты магазинов
            if (!CollectionUtils.isEmpty(order.getOrderItemPrices())) {
                stringBuilder.append(",");
                for (OrderItemPrice orderItemPrice : order.getOrderItemPrices()) {
                    stringBuilder.append(orderItemPrice.getItemPrice().getStoreAddress().getStoreLocation().getLongitude() + " " + orderItemPrice.getItemPrice().getStoreAddress().getStoreLocation().getLatitude());
                }
            }
            // координаты клиента
            stringBuilder.append(",");
            stringBuilder.append(longitude + " " + latitude);
            route = stringBuilder.toString();
            LOGGER.info("Direction points [driver -> stores -> client]: " + route);
        }
            CommonService.DirectionHelper directionHelper = routeBuildingFromLatLon2GIS(route);
            return directionHelper.getTimeDuration() == -1 ? 0 : directionHelper.getTimeDuration() + 3;
    }

    /*
        List<AlternativeStatistics> alternativeStatisticsList = alternativeStatisticsRepository.findByTypeStatValue(""+mission.getId(), new PageRequest(0, 1));
        if(CollectionUtils.isEmpty(alternativeStatisticsList)){
            AlternativeStatistics alternativeStatistics = new AlternativeStatistics("", ""+mission.getId(), DateTimeUtils.nowNovosib_GMT6().getMillis());
            if(directionHelper.getTimeDuration()==-1){
                alternativeStatistics.setTypeStat("arrival_time_is_not_calc");
            }else{
                alternativeStatistics.setTypeStat("arrival_time_is_calc");
            }
            alternativeStatisticsRepository.save(alternativeStatistics);
        }
        */


    public void buildPriceChange(Order order, OrderPayment orderPayment, int diff) {
        if (diff != 0) {
            PriceChanges priceChanges = new PriceChanges();
            priceChanges.setTimeOfRequesting(DateTimeUtils.nowNovosib_GMT6());
            priceChanges.setOrderPayment(orderPayment);
            priceChanges.setChangeType(PriceChanges.ChangeType.UP);
            if (diff < 0) {
                // уменьшение стоимости
                priceChanges.setChangeType(PriceChanges.ChangeType.DOWN);
            }
            priceChanges.setChangeAmount(Math.abs(diff));
            priceChanges.setOrder(order);
            priceChangesRepository.save(priceChanges);
        }
    }


    public void saveDriverLocation(long driverId, ItemLocation location, int distance) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        Driver driver = driverRepository.findOne(driverId);
        if (driver != null) {
            DriverLocation driverLocation = buildDriverLocation(driver, latitude, longitude, distance);
            locationRepository.save(driverLocation);
        }
    }


    public DriverLocation buildDriverLocation(Driver driver, double latitude, double longitude, int distance) {
        return new DriverLocation(DateTimeUtils.nowNovosib_GMT6(), driver, driver.getCurrentMission(), new Location(latitude, longitude), driver.getCurrentOrder(), distance);
    }



    /*
    public OrderPayment createOrderPayment(OrderPayment.PaymentState paymentState, Order order, PayOnlineHelper payOnlineHelper, int newPrice, OrderPayment orderPayment){ // копейки
        if(orderPayment == null){
            orderPayment = new OrderPayment();
            orderPayment.setClient(order.getClient());
            orderPayment.setOrder(order);
            orderPayment.setTimeOfRequesting(DateTimeUtils.nowNovosib_GMT6());
            orderPayment.setPaymentState(OrderPayment.PaymentState.WAIT_TO_HOLD);
        }
        orderPayment.setPaymentState(paymentState);
        orderPayment.setPriceAmount(MoneyUtils.getMoney(newPrice));
        if(payOnlineHelper != null){
            orderPayment.setErrorCode(payOnlineHelper.getCode());
            orderPayment.setMessage(payOnlineHelper.getMessage());
            orderPayment.setTransactionId(payOnlineHelper.getTransactionId());
        }
        return orderPayment;
    }
     */


    public int generalTimeLate(Long orderId){
        Integer result = lateOrderRepository.generalTimeLate(orderId);
        return  result != null ? result : 0;
    }



    public ItemLocation getCurrentDriverLocation(Driver driver){
       ItemLocation itemLocation = null;
        if(driver != null) {
            DriverLocation driverLocation = locationRepository.findByDriver(driver);
            if (driverLocation != null) {
                itemLocation = ModelsUtils.toModel(driver.getId(), driverLocation.getLocation().getLatitude(), driverLocation.getLocation().getLongitude(), driverLocation.getAngle());
            }
        }
                return itemLocation;
    }








    //@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void notifiedOrderStateChangeByOperator(Order order, Order.State state) {
        if(state.equals(Order.State.WAIT_TO_CONFIRM)){
            nodeJsNotificationsService.courierOrderConfirm(order);
            app42PushNotificationService.courierOrderConfirm(order);
            serviceSMSNotification.courierOrderConfirm(order.getClient());
        }
        if(state.equals(Order.State.IN_PROGRESS_BY_OPERATOR)){
            app42PushNotificationService.inProgressByOperator(order);
        }
    }




    /*
        public static <T> void forEach(List<T> list, Consumer<T> c) {
            for (T elem : list) {
                c.accept(elem);
            }
        }
    */
    public GetOrderResponse getOrder(Order order){
        GetOrderResponse response = new GetOrderResponse();
        //int  list = lateOrderRepository.findByOrder(order);
        //forEach(list, System.out::println);

        int generalTimeLate = generalTimeLate(order.getId());

        OrderInfo orderInfo = ModelsUtils.toModel(order, generalTimeLate);
        if(order.getDriver() != null){
            DriverInfo driverInfo = administrationService.fillPhotoDriverAndCars(orderInfo.getDriverInfo(), order.getDriver(), true);
            driverInfo.setPassword(null);
            orderInfo.setDriverInfo(driverInfo);
        }

        Comment comment = commentRepository.findByOrder(order);
        if(comment != null){
            orderInfo.setCommentInfo(ModelsUtils.toMode(comment));
        }

        //int expectedArrivalTime = order.getExpectedArrivalTime();

        int arrivalTime = calculateArrivalTime(order);
        orderInfo.setExpectedArrivalTime(arrivalTime);

        //order.setExpectedArrivalTime(arrivalTime);
        //orderRepository.save(order);

        LOGGER.info("Прибытие через: " + arrivalTime);

         orderInfo.setCurrentCourierLocation(getCurrentDriverLocation(order.getDriver())); //   ModelsUtils.toModel(order.getDriver().getId(), driverLocation.getLocation().getLatitude(), driverLocation.getLocation().getLongitude(), driverLocation.getAngle())
            response.setOrderInfo(orderInfo);
               return response;
    }


    //LOGGER.info("Опоздание: " + generalTimeLate);
    //arrivalTime += generalTimeLate;

        /*
        if(expectedArrivalTime != 0){
            DateTime expectedTimeOfCompleting = order.getExpectedTimeOfCompleting();
             if(expectedTimeOfCompleting != null){
                 //Seconds seconds = Seconds.secondsBetween(DateTimeUtils.nowNovosib_GMT6(), expectedTimeOfCompleting);
                 Minutes minutes = Minutes.minutesBetween(DateTimeUtils.nowNovosib_GMT6(), expectedTimeOfCompleting);
                 orderInfo.setExpectedArrivalTime(MathUtil.getRoundUp(Math.abs(minutes.getMinutes() + generalTimeLate)));
             } else {
                 if(order.getTimeOfStarting() != null){
                      if(order.getTimeOfStarting().isAfter(DateTimeUtils.nowNovosib_GMT6())){
                          Minutes minutes = Minutes.minutesBetween(DateTimeUtils.nowNovosib_GMT6(), order.getTimeOfStarting());
                          int min = minutes.getMinutes();
                          LOGGER.info("Min between DateTimeUtils.nowNovosib_GMT6() and order.getTimeOfStarting(): " + min + " Время опоздания: " + generalTimeLate);
                      } else {
                          if(generalTimeLate != 0){
                              //order.getTimeOfStarting().plusMinutes(generalTimeLate)
                          } else{

                          }
                          Minutes minutes = Minutes.minutesBetween(DateTimeUtils.nowNovosib_GMT6(), order.getTimeOfStarting());
                      }
                 }
             }

        } else {
            // нет расчетного времени прибытия
            DateTime startTime = order.getTimeOfStarting();
            DateTime timeNow = DateTimeUtils.nowNovosib_GMT6();

            if(startTime != null){
                if(startTime.isAfter(timeNow)){
                    Minutes minutes = Minutes.minutesBetween(timeNow, startTime);
                    int min = minutes.getMinutes();
                    LOGGER.info("Min between DateTimeUtils.nowNovosib_GMT6() and order.getTimeOfStarting(): " + min + " Время опоздания: " + generalTimeLate);
                } else {
                    if(generalTimeLate != 0){
                         order.getTimeOfStarting().plusMinutes(generalTimeLate);
                    } else{

                    }
                    Minutes minutes = Minutes.minutesBetween(DateTimeUtils.nowNovosib_GMT6(), order.getTimeOfStarting());
                }
            }
        }
        */




    private int calculateArrivalTime(Order order){
        int result = 0;
        DateTime timeOfFinishing = order.getTimeOfFinishing();
        DateTime timeNow = DateTimeUtils.nowNovosib_GMT6();
        DateTime timeOfFinishingExpected = order.getTimeOfFinishingExpected();
        int generalTimeLate = generalTimeLate(order.getId());
        if(timeOfFinishingExpected == null) {
            timeOfFinishing.plusMinutes(generalTimeLate);
            if (timeOfFinishing.isAfter(timeNow)) {
                Minutes minutes = Minutes.minutesBetween(timeNow, timeOfFinishing);
                result = minutes.getMinutes();
            }
        } else {
            timeOfFinishingExpected.plusMinutes(generalTimeLate);
                Minutes minutes;
                if(timeOfFinishingExpected.isAfter(timeNow)){
                    minutes = Minutes.minutesBetween(timeNow, timeOfFinishingExpected);
                    result = minutes.getMinutes();
                }
        }
        return result;
    }



    public DefaultPriceResponse defaultPrice(){
         DefaultPriceResponse response = new DefaultPriceResponse();
         Iterable<DefaultPrice> defaultPrices = defaultPriceRepository.findAll();
         for(DefaultPrice defaultPrice : defaultPrices){
               response.getDefaultPriceInfoList().add(ModelsUtils.toModel(defaultPrice));
         }
               return response;
    }




    /* список лат лонов магазинов */
    private StringBuilder wayPointsToStore(OrderInfo orderInfo) {
        StringBuilder waypoints = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder();

        List<OrderItemPriceInfo> orderItemPriceInfos = orderInfo.getOrderItemPriceInfos();

        if (!CollectionUtils.isEmpty(orderItemPriceInfos)) {
            boolean first = true;
            double latitude;
            double longitude;

            HashMap<Long, String> hashMap = new HashMap();
            for (OrderItemPriceInfo orderItemPriceInfo : orderItemPriceInfos) {
                ItemPriceInfo itemPriceInfo = orderItemPriceInfo.getItemPriceInfo();
                if (itemPriceInfo != null) {
                    StoreAddressInfo storeAddressInfo = itemPriceInfo.getStoreAddressInfo();
                    if (storeAddressInfo != null) {
                        latitude = storeAddressInfo.getLatitude();
                        longitude = storeAddressInfo.getLongitude();
                        String latLon = longitude + " " + latitude;
                        LOGGER.info("wayPointsToStore: latLon = " + latLon);
                        if(latitude != 0 && longitude != 0) {
                            hashMap.put(storeAddressInfo.getId(), latLon);
                        }
                    }
                }
            }

            for (Long key : hashMap.keySet()) {
                String value = hashMap.get(key);
                if(first){
                    stringBuilder.append(value);
                    first = false;
                } else {
                    stringBuilder.append("," + value);
                }
            }
        }
        //LOGGER.info("NEW wayPointsToStore:  "+ stringBuilder);
        return stringBuilder;
        //return waypoints;
    }




    /* список лат лонов целевых адресов клиента (откуда, куда) */
    private StringBuilder wayPointsToTargetAddress(OrderInfo orderInfo) {
        StringBuilder waypoints = new StringBuilder();
        List<OrderAddressInfo> orderAddressInfos = orderInfo.getTargetAddressesInfo();
        if(!CollectionUtils.isEmpty(orderAddressInfos)){
            boolean first = true;
            double latitude;
            double longitude;
            for (OrderAddressInfo orderAddressInfo : orderAddressInfos) {
                latitude = orderAddressInfo.getLatitude();
                longitude = orderAddressInfo.getLongitude();
                if(latitude != 0 && longitude != 0){
                    if (first) {
                        waypoints.append(longitude + " " + latitude);
                        first = false;
                    } else {
                        waypoints.append("," + longitude + " " + latitude);
                    }
                }
            }
        }
        LOGGER.info("Result waypoints in wayPointsToTargetAddress: " + waypoints);
            return waypoints;
    }




    /* расчет дистанции по магазинам - Zavezu */
    public DirectionHelper calculateDistanceWithTimeDurationByStore(OrderInfo orderInfo) {
         DirectionHelper directionHelper = new DirectionHelper();
         String waypoints = wayPointsToStore(orderInfo).toString();
         if(!StringUtils.isEmpty(waypoints)){
             directionHelper = calculateDistanceWithTimeDuration(wayPointsToStore(orderInfo).toString());
         }
             return directionHelper;
    }




    /* расчет дистанции - Zavezu */
    public DirectionHelper calculateDistanceWithTimeDurationGeneral(OrderInfo orderInfo) {
        StringBuilder result = wayPointsToStore(orderInfo);
        if(!StringUtils.isEmpty(result.toString())){
            result.append(","+wayPointsToTargetAddress(orderInfo).toString());
        } else{
            result.append(wayPointsToTargetAddress(orderInfo).toString());
        }
            return calculateDistanceWithTimeDuration(result.toString());
    }





    private int priceByOrderTypeAndDistance(int orderType, int generalDistanceInMeters){
        int result;
        DefaultPrice defaultPrice = defaultPriceRepository.findByActiveAndOrderType(Boolean.TRUE, OrderType.getByValue(orderType));
        int minimalPrice = defaultPrice.getMinimalPrice().getAmountMinorInt();
        int kmIncluded = defaultPrice.getKmIncluded();
        int priceKm = defaultPrice.getPerKmPrice().getAmountMinorInt();
        //int orderProcessingPrice =  defaultPrice.getOrderProcessingPrice().getAmountMinorInt();

        int roundedDistanceInKM =  MathUtil.convertAnRoundUpdMetersToKm(generalDistanceInMeters);// roundedDistance.intValue();

        if(roundedDistanceInKM > kmIncluded){
            int diffKm = (roundedDistanceInKM - kmIncluded);
            result = diffKm * priceKm + minimalPrice;
        }else{
            result = minimalPrice;
        }
            /* стоимость обработки заказа */
            //result += orderProcessingPrice;
            return result;
    }




    private int priceToCourier(int orderType, int generalDistanceInMeters){
        int result;
        DefaultPrice defaultPrice = defaultPriceRepository.findByActiveAndOrderType(Boolean.TRUE, OrderType.getByValue(orderType));
        //int minimalPrice = defaultPrice.getMinimalPrice().getAmountMinorInt();

        int sumToCourier = defaultPrice.getToCourierPrice().getAmountMinorInt();
        int kmIncluded = defaultPrice.getKmIncluded();
        int priceKm = defaultPrice.getPerKmPrice().getAmountMinorInt();
        //int orderProcessingPrice =  defaultPrice.getOrderProcessingPrice().getAmountMinorInt();

        int roundedDistanceInKM =  MathUtil.convertAnRoundUpdMetersToKm(generalDistanceInMeters);// roundedDistance.intValue();

        if(roundedDistanceInKM > kmIncluded){
            int diffKm = (roundedDistanceInKM - kmIncluded);
            result = diffKm * priceKm + sumToCourier;
        }else{
            result = sumToCourier;
        }
            /* стоимость обработки заказа */
        //result += orderProcessingPrice;

        return result;
    }



    /* Zavezu - стоимость доставки*/
    private int calculatePriceDeliveryByOrderTypeAndDistance(int orderType, int generalDistanceInMeters){
        int result;
        DefaultPrice defaultPrice = defaultPriceRepository.findByActiveAndOrderType(Boolean.TRUE, OrderType.getByValue(orderType));
        int minimalPrice = defaultPrice.getMinimalPrice().getAmountMinorInt();

        //int sumToCourier = defaultPrice.getToCourierPrice().getAmountMinorInt();
        int kmIncluded = defaultPrice.getKmIncluded();
        int priceKm = defaultPrice.getPerKmPrice().getAmountMinorInt();
        //int orderProcessingPrice =  defaultPrice.getOrderProcessingPrice().getAmountMinorInt();

        int roundedDistanceInKM =  MathUtil.convertAnRoundUpdMetersToKm(generalDistanceInMeters);// roundedDistance.intValue();

        if(roundedDistanceInKM > kmIncluded){
            int diffKm = (roundedDistanceInKM - kmIncluded);
            result = diffKm * priceKm + minimalPrice;
        }else{
            result = minimalPrice;
        }
            /* стоимость обработки заказа */
            //result += orderProcessingPrice;

            return result;
    }









    /* стоимость суммы выбранного товара + оценочная стоимость*/
    public int calculatePriceByOrderItemPrice(List<OrderItemPriceInfo> orderItemPriceInfos, int orderType){
        int result = 0;
        if(!CollectionUtils.isEmpty(orderItemPriceInfos)){
            for(OrderItemPriceInfo info : orderItemPriceInfos){
                // было:
                //result += info.getItemPriceInfo().getPrice() * info.getCountItem(); // стоимость * кол-во
                // стало (привет Войтову):

                result += info.getItemPriceInfo().getItemInfo().getDefaultItemPrice() * info.getCountItem(); // стоимость * кол-во

                //int priceOfInsurance = info.getPriceOfInsurance();
                //int percentInsurance = Integer.parseInt(getPropertyValue("percent_insurance"));
                //result += (priceOfInsurance * percentInsurance/100);
            }
        }
        return result;
    }





    /* расчет стоимости заказа Zavezu */
    public CourierCalculatePriceResponse calculatePrice(OrderInfo orderInfo){
        CourierCalculatePriceResponse response = new CourierCalculatePriceResponse();
        /* общая расчетная дистанция и время, которое тратит водитель на поездку по магазинам и доставку к клиенту*/
        DirectionHelper generalDistanceWithTimeDuration = calculateDistanceWithTimeDurationGeneral(orderInfo);
        int generalDistance = generalDistanceWithTimeDuration.getDistance(); // in m
        int priceDelivery = calculatePriceDeliveryByOrderTypeAndDistance(orderInfo.getOrderType(), generalDistance);

        int priceByOrderItem = 0;
        int priceByIncreasePercent = 0;
        int increasePercent = 0;
        float percentComissionOrderProcessingByBank = 0;
        int pricePercentComissionOrderProcessingByBank = 0;

        StringBuilder logBuilder = new StringBuilder();
        logBuilder.append("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ \n");
        logBuilder.append("Заказ: " + OrderType.getByValue(orderInfo.getOrderType()) + " # " + orderInfo.getId() + "\n");

        if(!CollectionUtils.isEmpty(orderInfo.getOrderItemPriceInfos())){
             /* стоимость выбранного товара + оценочная стоимость */
            priceByOrderItem = calculatePriceByOrderItemPrice(orderInfo.getOrderItemPriceInfos(), orderInfo.getOrderType());
            logBuilder.append(" - Стоимость товара без надбавок: " + priceByOrderItem/100 + "\n");
            /* стоимость заказа + % на случай если товар будет дороже [только если заказ купить и привезти] */
            increasePercent =  orderInfo.getIncreasePercent(); // Integer.parseInt(getPropertyValue("increase_percent"));
            priceByIncreasePercent = (priceByOrderItem * increasePercent/100);
            priceByOrderItem += priceByIncreasePercent;
        }

        int priceOfInsurance = orderInfo.getPriceOfInsurance();
        int commonPrice = 0;
        int percentInsurance = orderInfo.getPercentInsuranceOnDayOfOrder(); //Integer.parseInt(getPropertyValue("percent_insurance"));
        int priceByPercentInsurance = (priceOfInsurance * percentInsurance/100);

        /* % от стоимости покупки (наши затраты за обслуживание банком) */
        percentComissionOrderProcessingByBank =  Float.parseFloat(getPropertyValue("percent_comission_order_processing"));
        pricePercentComissionOrderProcessingByBank = (int)((priceByOrderItem * percentComissionOrderProcessingByBank/100));

        priceDelivery += pricePercentComissionOrderProcessingByBank;

        /* купить и привезти */
        logBuilder.append(" - Сумма стоимости товара: " + priceByOrderItem / 100 + "\n");
        logBuilder.append(" - % увеличения по стоимости товара: " + increasePercent + "\n");
        logBuilder.append(" - Сумма %-а увеличения от суммы стоимости товара: " + priceByIncreasePercent / 100 + "\n");
        logBuilder.append(" - % от стоимости покупки (наши затраты за обслуживание банком): " + percentComissionOrderProcessingByBank + "\n");
        logBuilder.append(" - Сумма %-а увеличения от суммы стоимости товара: " + pricePercentComissionOrderProcessingByBank / 100 + increasePercent + "\n");

        int generalSumByAdditionalAddress = 0;
        List<OrderAddressInfo> orderAddressInfos = orderInfo.getTargetAddressesInfo();
        if(!CollectionUtils.isEmpty(orderAddressInfos) && orderAddressInfos.size() > 2){
            int countTargetAddress = orderAddressInfos.size() - 2;
            DefaultPrice defaultPrice = defaultPriceRepository.findByActiveAndOrderType(true, OrderType.getByValue(orderInfo.getOrderType()));
            generalSumByAdditionalAddress = defaultPrice.getAdditionalAddressPrice().getAmountMinorInt() * countTargetAddress;
            LOGGER.info("Стоимость за дополнительный адрес(а): " + generalSumByAdditionalAddress);
        }


        /* забрать и привезти */
        logBuilder.append(" - Сумма оценочной стоимость: " + priceOfInsurance + "\n");
        logBuilder.append(" - Кол-во %: " + percentInsurance + "\n");
        logBuilder.append(" - Сумма % от оценочной стоимости: " + priceByPercentInsurance + "\n");
        logBuilder.append(" - Доставка: " + priceDelivery / 100 + "\n");
        logBuilder.append(" - Доплата за доп. адрес: " +  generalSumByAdditionalAddress / 100 + "\n");
        //LOGGER.info("---------------------------------------------------------");


        priceDelivery += priceByPercentInsurance;
        priceDelivery += generalSumByAdditionalAddress;


        commonPrice += priceDelivery;
        commonPrice += priceByOrderItem;

        logBuilder.append(" -- Общая стоимость: " + commonPrice + "\n");
        logBuilder.append("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ \n");
        LOGGER.info(logBuilder.toString());

        response.setPriceDelivery(priceDelivery);
        response.setPriceItems(priceByOrderItem);
        response.setDistance(generalDistance);
        response.setPriceOfInsurance(priceOfInsurance); // цена страховки
        response.setCommonPrice(commonPrice);
        response.setPriceByPercentInsurance(priceByPercentInsurance);
        response.setPriceByAdditionalAddress(generalSumByAdditionalAddress);
        return response;
    }






    private List<WebAutocompleteInfo> parseMapBody(Map map, String addressMask){
        String sortValueProperties = getPropertyValue("web_autocomplete_sort_by_default");
        boolean sortByDefault= sortValueProperties.equals(YES);

        List<WebAutocompleteInfo> webAutocompleteInfos = new ArrayList<>();
        List<WebAutocompleteInfo> streetNskInfos = new ArrayList<>();
        List<WebAutocompleteInfo> otherNskInfos = new ArrayList<>();
        List<WebAutocompleteInfo> streetNotNskInfos = new ArrayList<>();
        List<WebAutocompleteInfo> otherNotNskInfos = new ArrayList<>();

        //List<WebAutocompleteInfo> subWebAutocompleteInfos = new ArrayList<>();

        if(map.containsKey("result")){
            Map result = (Map)map.get("result");
            if(result.containsKey("items")){
                List<Map> items = (ArrayList)result.get("items");
                for(Map item: items){

                    WebAutocompleteInfo info = new WebAutocompleteInfo();
                    String typeItem = "";
                    if(item.containsKey("type")){
                        // улица, строение или  ...
                        typeItem = (String)item.get("type");
                        /*
                        if(typeItem.equals("street")){
                             info.setTypeItem("street");
                        }
                        */
                    }

                    if(item.containsKey("adm_div")){
                        List<Map> adminDivs = (ArrayList)item.get("adm_div");
                        for(Map admDiv: adminDivs){
                            //LOGGER.info("admDiv: "+admDiv);
                            if(admDiv.containsKey("type")){
                                String type = (String)admDiv.get("type");
                                String name = (String)admDiv.get("name");
                                if(type.equals("district")){
                                    info.setRegion(name);
                                } else if(type.equals("city")){
                                    info.setCity(name);
                                } else if(type.equals("settlement")){
                                    info.setCity(name);
                                }
                            }
                        }
                    }
                    if(item.containsKey("geometry")){
                        Map geometry = (Map)item.get("geometry");
                        if(geometry.containsKey("centroid")){
                            String point = (String)geometry.get("centroid");
                            int indexStartSymbol = point.indexOf("(");
                            point = point.substring(indexStartSymbol+1, point.length()-1);
                            String[] splitPoint = point.split(" ");
                            info.setLatitude(splitPoint[1]);
                            info.setLongitude(splitPoint[0]);
                        }
                    }

                    String addressName = null;
                    if(item.containsKey("address_name")){
                        addressName = (String)item.get("address_name");
                    }

                    if(item.containsKey("address")){
                        // адрес улицы берем отсюда, в случае если есть поле components
                        Map address = (Map)item.get("address");
                        if(address.containsKey("components")){
                            List components = (ArrayList)address.get("components");
                            Map mapComponent = (Map)components.get(0);
                            if(mapComponent.containsKey("street")){
                                String street = (String)mapComponent.get("street");
                                info.setStreet(street);
                            }
                            if(mapComponent.containsKey("number")){
                                String number = (String)mapComponent.get("number");
                                info.setHouse(number != null ? number : "");
                            }
                        }
                    } else if(item.containsKey("name")){
                        String streetName = (String)item.get("name");
                        info.setStreet(streetName);
                    }

                    boolean containsString = false;
                    String splitAddressMask[] = addressMask.replaceAll("ё", "е").split("[\\s,;]+");
                    //LOGGER.info("STREET ~~~~~~~~~~~~~~~~~~~~~~ = " + info.getStreet());
                    for(String res : splitAddressMask){
                        containsString = org.apache.commons.lang3.StringUtils.containsIgnoreCase(info.getStreet().replaceAll("ё", "е"), res.replaceAll("ё", "е"));
                        if(containsString){
                            //LOGGER.info("Sub string contains in STREET. BREAKE!!!");
                            break;
                        }
                    }

                    boolean byPoint = addressMask.equals("by_point");
                    boolean containsAll = false;

                    //LOGGER.info("addressName !!!!!!!!!!!!!!!!!! ================= " + addressName + " AddressMask: " + addressMask);
                    /*
                    if(!StringUtils.isEmpty(addressName) && !containsString){
                        addressName = addressName.replaceAll("\\s", "");
                        addressName = addressName.replaceAll("ё", "е");

                        addressMask = addressMask.replaceAll("\\s", "");
                        containsAll = org.apache.commons.lang3.StringUtils.containsIgnoreCase(addressName, addressMask);
                    }
                    */


                    if(sortByDefault){
                        //LOGGER.info("info: " + info.getStreet() +" | " + info.getCity() + " containsAll = " + containsAll);
                        if(containsString || byPoint || containsAll){
                            webAutocompleteInfos.add(info);
                        }
                    } else {
                        LOGGER.info("CUSTOM SORT!!!!"+info.getRegion().equals(NSK)+" | "+info.getRegion()+" NSK: "+NSK+" typeItem: "+typeItem);
                        if(info.getRegion().equals(NSK)){
                                 // Новосибирск
                            if(!StringUtils.isEmpty(typeItem) && typeItem.equals("street")){ // info.getTypeItem()
                                // улица
                                if (containsString || byPoint) {
                                    streetNskInfos.add(info);
                                }
                            } else{
                                // другое
                                if(containsString || byPoint) {
                                    otherNskInfos.add(info);
                                }
                            }
                        } else{
                                // Другой город
                            if(!StringUtils.isEmpty(typeItem) && typeItem.equals("street")){ // info.getTypeItem()
                                // улица
                                if(containsString || byPoint) {
                                    streetNotNskInfos.add(info);
                                }
                            } else{
                                // другое
                                if(containsString || byPoint) {
                                    otherNotNskInfos.add(info);
                                }
                            }
                        }
                    }
                }
            }
        }
        if(!sortByDefault){
            webAutocompleteInfos.addAll(streetNskInfos);
            webAutocompleteInfos.addAll(otherNskInfos);
            webAutocompleteInfos.addAll(streetNotNskInfos);
            webAutocompleteInfos.addAll(otherNotNskInfos);
        }
             return webAutocompleteInfos;
    }






    private class RegionSorter implements Comparator<WebAutocompleteInfo>{
        public int compare(WebAutocompleteInfo one, WebAutocompleteInfo another){
            String oneRegion = one.getRegion();
            String anotherRegion = another.getRegion();
            return oneRegion.compareTo(anotherRegion);
        }
    }


/*
    public class PriceSorter implements Comparator<ItemPrice>{
        public int compare(ItemPrice one, ItemPrice another){
            int returnVal = 0;
            if(one.getPrice().getAmount().intValue()  < another.getPrice().getAmount().intValue()){
                returnVal =  -1;
            }else if(one.getPrice().getAmount().intValue() > another.getPrice().getAmount().intValue()){
                returnVal =  1;
            }else if(one.getPrice() == another.getPrice()){
                returnVal =  0;
            }
            return returnVal;
        }
    }
*/



    public WebAutoCompleteResponse webAutoComplete(String addressMask){
        WebAutoCompleteResponse response = new WebAutoCompleteResponse();

        if(StringUtils.isEmpty(addressMask) || addressMask.length() < 3){
            response.setErrorMessage("Недопустимое количество символов");
            response.setErrorCode(0);
            return response;
        }

        Map map = JsonUtils.getMapByJsonString(startAutoComplete2GIS(addressMask, true));

        List<WebAutocompleteInfo> webAutocompleteInfos = parseMapBody(map, addressMask);

        if(CollectionUtils.isEmpty(webAutocompleteInfos)){
            /* пытемся найти в организациях */
            // todo: ПОИСК ПО ОРГАНИЗАЦИЯМ ПОКА УБРАЛ (09.10.2015)
            //map = JsonUtils.getMapByJsonString(startAutoComplete2GIS(addressMask, false));
            //webAutocompleteInfos = parseMapBody(map, addressMask);
        }
           response.setWebAutocompleteInfos(webAutocompleteInfos);
              return response;
    }




    public String startAutoComplete2GIS(String addressMask, boolean firstStep) {
        String bodyAnswer = "";
        try {
            String url;
            LOGGER.info("addressMask ================= " + addressMask);
            if(firstStep){
                LOGGER.info("first step");
                url = GIS_URL_STREET_MAP + GIS_API_KEY +"&q=" + URLEncoder.encode(addressMask, "UTF-8");
            } else {
                LOGGER.info("second step");
                url = GIS_URL_ORGANISATION_MAP + GIS_API_KEY +"&q=" + URLEncoder.encode(addressMask, "UTF-8");
            }

            bodyAnswer = HTTPUtil.sendHttpQuery(url, null);

            LOGGER.info("bodyAnswer: " + bodyAnswer);

            JSONObject answerJson = new JSONObject(bodyAnswer);
            if(answerJson.has("result")){
                bodyAnswer = answerJson.toString();
            }else{
                LOGGER.info("autoComplete2GIS: В json ответе 2gis не найден параметр result");
            }
        } catch(JSONException n){
            n.printStackTrace();
        } catch(UnsupportedEncodingException un){
            un.printStackTrace();
        }
        return bodyAnswer;
    }






    /* get string address from geo point: format - 82.944653,55.021355 */
    public WebAutocompleteInfo getStringAddressByLatLon2GIS(String point) {
        WebAutocompleteInfo info = new WebAutocompleteInfo();
        try {
            String url = GIS_URL_ADDRESS_BY_GEO_POINT + GIS_API_KEY+ "&point="+ URLEncoder.encode(point, "UTF-8");

            String bodyAnswer = HTTPUtil.sendHttpQuery(url, null);
            JSONObject answerJson = new JSONObject(bodyAnswer);

            /*
              body answer example:
              {"meta":{"code":200},"result":{"total":4,
              "items":[{"id":"141373143584693","name":"Новосибирск, Чехова, 67","full_name":"Новосибирск, Чехова, 67","purpose_name":"Религиозное сооружение","address_name":"Чехова, 67","type":"building"},{"id":"141347373711463","subtype":"district","full_name":"Новосибирск, Октябрьский","name":"Октябрьский","type":"adm_div"},{"id":"141424683123065","subtype":"place","full_name":"Новосибирск, Правый берег","name":"Правый берег","type":"adm_div"},
              {"id":"141360258613345","subtype":"city","full_name":"Новосибирск","name":"Новосибирск","type":"adm_div"}]}}
            */

            if(answerJson.has("result")){
                Map map = JsonUtils.getMapByJsonString(bodyAnswer);
                List<WebAutocompleteInfo> webAutocompleteInfos = parseMapBody(map, "by_point");

                if(!CollectionUtils.isEmpty(webAutocompleteInfos)){
                    info = webAutocompleteInfos.get(0);
                    //address = info.getStreet() + (!StringUtils.isEmpty(info.getHouse()) ? ", " + info.getHouse() : "");
                }
            }else{
                LOGGER.info("getStringAddressByLatLon2GIS: В json ответе 2gis не найден параметр result");
            }
        } catch(JSONException n){
            n.printStackTrace();
        } catch(UnsupportedEncodingException un){
            un.printStackTrace();
        }
        return info;
    }




    /* get geo point from string address - format: 82.938324 55.047353 */
    public String getLatLonByStringAddress2GIS(String address) {
        String point = "";
        try {
            String url = GIS_URL_GEO_POINT_BY_ADDRESS + GIS_API_KEY+ "&q="+ URLEncoder.encode(address, "UTF-8");
            String bodyAnswer = HTTPUtil.sendHttpQuery(url, null);
            JSONObject answerJson = new JSONObject(bodyAnswer);
            /*
              body answer example: {"meta":{"code":200},"result":{"total":1,
              "items":[{"id":"141373143519667","name":"На Достоевского, бизнес-центр","full_name":"Новосибирск, На Достоевского, бизнес-центр","building_name":"На Достоевского, бизнес-центр","purpose_name":"Бизнес-центр","address_name":"Достоевского, 58","geometry":{"centroid":"POINT(82.938324 55.047353)"},"type":"building"}]}}
            */

            if(answerJson.has("result")){
                JSONObject resultJson = (JSONObject) answerJson.get("result");
                JSONArray itemsArray = (JSONArray) resultJson.get("items");
                JSONObject item = (JSONObject) itemsArray.get(0);
                JSONObject geometry = (JSONObject) item.get("geometry");
                point = geometry.get("centroid").toString();
            }else{
                LOGGER.info("fromStringAddressToLatLon2GIS: В json ответе 2gis не найден параметр result");
            }
        } catch(JSONException n){
            n.printStackTrace();
        } catch(UnsupportedEncodingException un){
            un.printStackTrace();
        }
        return point;
    }





    public DirectionHelper calculateDistanceWithTimeDuration(String query)  { // store = true - дистанция по магазинам
        String answer = "";
        DirectionHelper directionHelper = new DirectionHelper();
        try {
            //String url="http://maps.googleapis.com/maps/api/directions/json?origin=" + latitude_from + "," + longitude_from + "&destination=" + latitude_to + "," + longitude_to + "&sensor=true&alternatives=true&units=metric&mode=driving";//&key="+GOOGLE_API_KEY;
            /*
                Допустимые значения:
                shortest - кратчайший маршрут
                optimal_statistic - оптимальный маршрут с учетом статистики
                optimal_jams - оптимальный маршрут с учетом пробок
                Значение по умолчанию - optimal_statistic
             */
            String url = "http://catalog.api.2gis.ru/2.0/transport/calculate_directions";
            List<NameValuePair> urlParameters = new ArrayList<>();
            urlParameters.add(new BasicNameValuePair("waypoints", query));
            urlParameters.add(new BasicNameValuePair("routing_type", "shortest")); // кратчайшие
            urlParameters.add(new BasicNameValuePair("key", GIS_API_KEY));

            answer = HTTPUtil.senPostQuery(url, urlParameters);

            LOGGER.info("answer: "+answer);

            JSONObject answerJson = new JSONObject(answer);

            //all result = {"meta":{"code":200},"result":{"total":1,"items":[{"type":"optimal_statistic","total_duration":697,"total_distance":3613,

            if(answerJson.has("result")){
                JSONObject resultJson = (JSONObject) answerJson.get("result");
                JSONArray itemsArray = (JSONArray) resultJson.get("items");
                JSONObject totalDistance = (JSONObject) itemsArray.get(0);

                String totalStr = totalDistance.get("total_distance").toString();
                String totalTimeDuration = totalDistance.get("total_duration").toString();

                directionHelper.setDistance(Integer.parseInt(totalStr));
                directionHelper.setTimeDuration(Integer.parseInt(totalTimeDuration)/60);
            }else{
                LOGGER.info("2GIS answer: В json ответе 2gis не найден параметр result ~~~~~");
                directionHelper.setTimeDuration(-1);
            }
            //result = Integer.parseInt(totalStr);
        } catch(JSONException n){
            LOGGER.debug("2GIS: routeBuildingFromLatLon2GIS exception: "+n.getMessage()+" answer: "+answer);
            n.printStackTrace();
        }
        return directionHelper;
    }





    public class DirectionHelper {
        int distance = 0;
        int timeDuration = 0; // min

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public int getTimeDuration() {
            return timeDuration;
        }

        public void setTimeDuration(int timeDuration) {
            this.timeDuration = timeDuration;
        }
    }



    public int routeBuildingFromLatLon(String latitude_from, String longitude_from, String latitude_to, String longitude_to) {
        int result = 0;
        String answer = "";
        try {
            //LOGGER.info("GOOGLE_API_KEY="+GOOGLE_API_KEY);
            String url = "http://maps.googleapis.com/maps/api/directions/json?origin=" + latitude_from + "," + longitude_from + "&destination=" + latitude_to + "," + longitude_to + "&sensor=true&alternatives=true&units=metric&mode=driving";//&key="+GOOGLE_API_KEY;

            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);

            HttpResponse execute = client.execute(httpGet);
            InputStream content = execute.getEntity().getContent();
            BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
            String s;
            while ((s = buffer.readLine()) != null) {
                answer += s;
            }
            result = parseGoogleAnswer(answer);
        }catch(IOException io){
            LOGGER.info("routeBuildingFromLatLon IOExcption: " + io.getMessage());
        }
        return result;
    }



    private int parseGoogleAnswer(String result)  {
        int distance = 0;
        try {
            final JSONObject json = new JSONObject(result);
            if(!json.has("routes")){
                return distance;
            }
            final JSONObject jsonRoute = json.getJSONArray("routes").getJSONObject(0);
            final JSONObject leg = jsonRoute.getJSONArray("legs").getJSONObject(0);
            final JSONArray steps = leg.getJSONArray("steps");
            final int numSteps = steps.length();
            for (int i = 0; i < numSteps; i++) {
                final JSONObject step = steps.getJSONObject(i);
                final int length = step.getJSONObject("distance").getInt("value");
                distance += length;
            }
        }catch(JSONException je){
            je.printStackTrace();
            LOGGER.info("parseGoogleAnswer json exception: " + je.getMessage());
            return distance;
        }
        return distance;
    }




    public int calculateDistance_Google(List<String> latLonList){
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
                allDistance+= routeBuildingFromLatLon(latLonAddressFrom[1], latLonAddressFrom[0], latLonAddressTo[1], latLonAddressTo[0]);
            }
        }
        return  allDistance;
    }




    /* метод расчета дистанции по списку адресов */
    public int calculateDistanceByStringAddressList(List<String> addressList) {
       List<String> latLonList = new ArrayList<>();
       int distanceInMetr;
       StringBuilder waypoints = new StringBuilder();
       String point;
       boolean first = true;
       for (String address : addressList) {

           point = getLatLonByStringAddress2GIS(address);

           // todo: поставить проверку на валидность point:  что будем делать этом случае?

           int indexStartSymbol = point.indexOf("(");
           point = point.substring(indexStartSymbol+1, point.length()-1);

           if (first) {
                   waypoints.append(point);
                   first = false;
               } else {
                   waypoints.append("," + point);
               }
           latLonList.add(point);
       }

       LOGGER.info("waypoints: "+waypoints.toString());

       DirectionHelper directionHelper = calculateDistanceWithTimeDuration(waypoints.toString());
       int allDistance =  directionHelper.getDistance();

       LOGGER.info("allDistance: "+allDistance);

       boolean service = false;

        if(allDistance==0){
            // пытаемся посчитать через гугл
            LOGGER.debug("Try calculate distance from Google");
            service=true;
            //allDistance = calculateDistance_Google(latLonList);
        }

            double distanceInKM = allDistance / 1000.00;
            BigDecimal x = new BigDecimal(distanceInKM);
            BigDecimal roundedDistance = x.round(new MathContext(MathUtil.countDigit(x), RoundingMode.UP));
            LOGGER.info("Полная дистанция в метрах: " + allDistance + " в км: " + roundedDistance);
            distanceInMetr = allDistance;
       return distanceInMetr;
    }





    protected String parseYandexAnswer(String result) throws JSONException {
        String coordinates = "";
        JSONArray jArray;
        JSONObject jObject;

        jObject = new JSONObject(result);
        JSONObject jsonObject = jObject.getJSONObject("response");
        JSONObject jsonObject2 = jsonObject.getJSONObject("GeoObjectCollection");
        jArray = jsonObject2.getJSONArray("featureMember");
        if(jArray.length() > 0) {
            jsonObject = jArray.getJSONObject(0);
            jsonObject2 = jsonObject.getJSONObject("GeoObject");
            jsonObject = jsonObject2.getJSONObject("Point");
            coordinates = jsonObject.getString("pos");
        }
        return coordinates;
    }



    public String fromStringAddressToLatLonYandex(String address) {
        //String url=http://geocode-maps.yandex.ru/1.x/?format=json&geocode=Москва, улица Новый Арбат, дом 24
        String result = "";
        String answer = "";
        try {
            address = address.replaceAll("\\s", "");
            String url = "http://geocode-maps.yandex.ru/1.x/?format=json&geocode=" + address;
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse execute = client.execute(httpGet);
            InputStream content = execute.getEntity().getContent();
            BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
            String s;
            while ((s = buffer.readLine()) != null) {
                answer += s;
            }
            result = parseYandexAnswer(answer);
        }catch(IOException io){

        } catch(JSONException je){

        }
        return result;
    }



    public GelfMessage createGelfMessage(long clientId, long driverId, String mess, String controller, long missionId, String source, String text1, String text2, String text3){
        GelfMessage message = new GelfMessage();
        message.setJavaTimestamp(DateTimeUtils.nowNovosib_GMT6().getMillis());
        message.addField("clientId", clientId)
        .addField("driverId", driverId)
        .addField("message", mess)
        .addField("controller", controller)
        .addField("missionId", missionId)
        .addField("source", source)
        .addField("text1", text1)
        .addField("text2", text2)
        .addField("text3", text3);

            return message;
    }




    /* долг клиента */
    public int clientSumDebt(Client client){
        int sumDebt = 0;
        if(client != null){
            int sumBonuses = client.getAccount().getBonuses().getAmount().intValue();
            if(sumBonuses < 0){
                sumDebt = sumBonuses;
            }
        }
        return sumDebt;
    }



    @Async
    public Future<GlobalClientStatsResponse> generateReport() {
        GlobalClientStatsResponse response = new GlobalClientStatsResponse();
        try {
              while(count.get() != 200){
                  count.incrementAndGet();
                  GlobalClientStatsInfo info = new GlobalClientStatsInfo();
                  info.setAllSummMissionMoney(0);
                  info.setClientId(24);
                  info.setBlockState("BLOCKED");
                  response.getGlobalClientStatsInfos().add(info);

                  LOGGER.info("count: " + count);
                  Thread.sleep(1000);
              }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new AsyncResult(response);
    }




    public Object commonPhoneNumber(Object model){
        String commonClientNumber = getPropertyValue("client_call_via_pbx");
        String commonDriverNumber = getPropertyValue("driver_call_via_pbx");
        if(model instanceof MissionInfo){
            MissionInfo missionInfo = (MissionInfo)model;
            if(!StringUtils.isEmpty(commonClientNumber)){
                missionInfo.getClientInfo().setPhone(commonClientNumber);
            }
            if(!StringUtils.isEmpty(commonDriverNumber)){
                if(missionInfo.getDriverInfo()!=null){
                    missionInfo.getDriverInfo().setPhone(commonDriverNumber);
                }
            }
            return missionInfo;
        } else
        if(model instanceof ClientInfo){
            ClientInfo clientInfo = (ClientInfo)model;
            if(!StringUtils.isEmpty(commonClientNumber)){
                clientInfo.setPhone(commonClientNumber);
            }
            return clientInfo;
        }else
        if(model instanceof DriverInfo){
            DriverInfo driverInfo = (DriverInfo)model;
            if(!StringUtils.isEmpty(commonDriverNumber)){
                driverInfo.setPhone(commonDriverNumber);
            }
            return driverInfo;
        } else {
            return model;
        }
    }



    public String getPropertyValue(String prop_name){
        String result = "";
        if(prop_name!=null){
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




    public boolean checkServiceByVersionApp(String clientVersionApp, String clientType){
        boolean newVersion = false;
        VersionsApp versionsApp = versionsAppRepository.findByVersionAndClientType(clientVersionApp, clientType);
        if(versionsApp!=null){
            if(clientType.equals("ANDROID_CLIENT")){
                newVersion = clientVersionAppIsNew(getIntValueVersion(getPropertyValue("min_version_app_android")), getIntValueVersion(clientVersionApp));
            }else if(clientType.equals("APPLE")){
                newVersion = clientVersionAppIsNew(getIntValueVersion(getPropertyValue("min_version_app_apple")), getIntValueVersion(clientVersionApp));
            }
        }
        return newVersion;
    }



    public ReasonResponse reasons(long reasonId, boolean isWebUser){
        ReasonResponse response = new ReasonResponse();
        if(reasonId!=0){
            Reason reason;
            if(isWebUser){
                reason = reasonRepository.findOne(reasonId);
            } else{
                reason = reasonRepository.findByIdAndToDriver(reasonId, Boolean.TRUE);
            }
            if(reason == null){
                throw new CustomException(1, "Reason not found");
            }
            response.getReasonInfos().add(ModelsUtils.toModel(reason));
        }else{
            Iterable<Reason> reasons;
            if(isWebUser){
                reasons = reasonRepository.findAll();
            } else{
                reasons = reasonRepository.findByToDriver(Boolean.TRUE);
            }
            for(Reason reason: reasons){
                response.getReasonInfos().add(ModelsUtils.toModel(reason));
            }
        }
           return response;
    }



    private boolean clientVersionAppIsNew(int intMinVersion, int intClientVersion){
        if(intClientVersion>=intMinVersion){
            return true;
        }else{
            return false;
        }
    }



    public int getIntValueVersion(String versionApp){
        int result = 0;
        String[] split = versionApp.split(Pattern.quote("."));
        StringBuilder sb = new StringBuilder();
        for(String s: split){
            sb.append(s);
        }
        if(!StringUtils.isEmpty(sb.toString())){
            result = Integer.parseInt(sb.toString());
        }
        return result;
    }



    public boolean isCorporateMission(Mission mission){
        boolean missionIsCorporate = false;
        if(EnumSet.of(PaymentType.CORPORATE_CARD, PaymentType.CORPORATE_CASH, PaymentType.CORPORATE_BILL).contains(mission.getPaymentType())){
            missionIsCorporate = true;
        }
          return missionIsCorporate;
    }




    // количество пауз в минутах
    public int getPausesInMinutesCount(Mission mission){
        int COUNT_PAUSES_IN_MINUTES = 0;
        List<Mission.PauseInfo> pauses = mission.getStatistics().getPauses();
        if (!pauses.isEmpty()) {
            for (Mission.PauseInfo pauseInfo : pauses) {
                DateTime start = pauseInfo.getStartPause();
                DateTime end = pauseInfo.getEndPause();
                if (end == null) {
                    end= DateTimeUtils.nowNovosib_GMT6();
                }
                Seconds seconds = Seconds.secondsBetween(start, end);
                int min;
                int sec = seconds.getSeconds();
                if (sec % 60 != 0) {
                    min = (sec / 60 + 1);
                } else {
                    min = sec / 60;
                }
                COUNT_PAUSES_IN_MINUTES += min;
            }
        }
        return COUNT_PAUSES_IN_MINUTES;
    }




    // стоимость платного ожидания
    public int pricePaymentWaiting(int autoClass, int waitingDriverMinutes){
        int resultSum = 0;
        AutoClass autoClassComing = AutoClass.getByValue(autoClass);
        MissionRate missionRate = missionRatesRepository.findOne((long) 1);
        Set<AutoClassPrice> autoClassPriceSet = missionRate.getAutoClassPrices();
        if(autoClassPriceSet!=null){
            List<AutoClassPrice> autoClassPriceList = Lists.newArrayList(autoClassPriceSet.iterator());
            for(AutoClassPrice autoClassPriceDB :autoClassPriceList){
                if(autoClassComing.getValue()==autoClassPriceDB.getAutoClass().getValue()){
                    int costOneMinutesByClassAuto = autoClassPriceDB.getPerMinuteWaitAmount();
                    resultSum += costOneMinutesByClassAuto * waitingDriverMinutes;
                }
            }
        }
        return resultSum;
    }



    // стоимость платного ожидания
    public AdministrationService.SumDetails calculatePriceForWaiting(int autoClass, int waitingDriverMinutes, AdministrationService.SumDetails sumDetails){
        int resultSum = 0;
        AutoClass autoClassComing = AutoClass.getByValue(autoClass);
        MissionRate missionRate = missionRatesRepository.findOne((long) 1);
        Set<AutoClassPrice> autoClassPriceSet = missionRate.getAutoClassPrices();
        if(autoClassPriceSet!=null){
            List<AutoClassPrice> autoClassPriceList = Lists.newArrayList(autoClassPriceSet.iterator());
            for(AutoClassPrice autoClassPriceDB :autoClassPriceList){
                if(autoClassComing.getValue()==autoClassPriceDB.getAutoClass().getValue()){
                    int costOneMinutesByClassAuto = autoClassPriceDB.getPerMinuteWaitAmount();
                    int freeWaitMinutes = autoClassPriceDB.getFreeWaitMinutes();
                    int diffMin = freeWaitMinutes - waitingDriverMinutes;
                    if(diffMin<0){
                        diffMin=Math.abs(diffMin);
                        resultSum += costOneMinutesByClassAuto*diffMin;
                    }
                }
            }
            sumDetails.setSumForWaiting(resultSum);
        }

        return sumDetails;
    }




    // общее количество бесплатного времени ожидания в минутах в зависимости от класса авто, клиента (корп. или нет)
    public int getFreeWaitMinutesByAutoClass(Mission mission){ // AutoClass autoClass,
        int resMin = 0;

            MissionRate missionRate = missionRatesRepository.findOne((long) 1);
            Set<AutoClassPrice> autoClassPriceSet = missionRate.getAutoClassPrices();
            if(autoClassPriceSet!=null){
                List<AutoClassPrice> autoClassPriceList = Lists.newArrayList(autoClassPriceSet.iterator());
                for(AutoClassPrice autoClassPriceDB :autoClassPriceList) {
                    if (mission.getAutoClass().getValue() == autoClassPriceDB.getAutoClass().getValue()) {
                        resMin = autoClassPriceDB.getFreeWaitMinutes();
                    }
                }
            }

            // если клиент корпоративный и поездка корпоративная, то берем время свободного ожидания из таблицы private_tariff
            Client mainClient = mission.getClientInfo().getMainClient();
            if(mainClient != null && isCorporateMission(mission)){
                // клиент корпоративный и поездка корпоративная
                PrivateTariff privateTariff = privateTariffRepository.findByClientAndTariffName(mission.getClientInfo(), mission.getAutoClass().name());
                /*
                  бесплатное время ожидания берем сначала с текущего клиента, если его нет, берем из главной корп. учетной записи
                */
                if(privateTariff == null){
                    privateTariff = privateTariffRepository.findByClientAndTariffName(mainClient, mission.getAutoClass().name());
                }
                      if(privateTariff != null){
                           resMin = privateTariff.getFreeWaitMinutes();
                      }
            }
        return resMin;
    }




    public static class TimeWaitClientUtil {
        private int freeTime;
        private int freeTimeInFact;
        private int howLongWaitInFact;

        public int getHowLongWaitInFact() {
            return howLongWaitInFact;
        }

        public void setHowLongWaitInFact(int howLongWaitInFact) {
            this.howLongWaitInFact = howLongWaitInFact;
        }

        public int getFreeTime() {
            return freeTime;
        }

        public void setFreeTime(int freeTime) {
            this.freeTime = freeTime;
        }

        public int getFreeTimeInFact() {
            return freeTimeInFact;
        }

        public void setFreeTimeInFact(int freeTimeInFact) {
            this.freeTimeInFact = freeTimeInFact;
        }
    }





    // количество времени, которое потратил водитель на ожидание клиента с момента приезда
    public TimeWaitClientUtil freeTimeLeftWaitClient(Mission mission){
         TimeWaitClientUtil timeWaitClientUtil = new TimeWaitClientUtil();
         Seconds seconds;
         int result = getFreeWaitMinutesByAutoClass(mission);

         if(mission.isBooked()!=null && (mission.isBooked() || mission.isTimeIsAfter()) && !isCorporateMission(mission)){
             result = 0;
         }

         timeWaitClientUtil.setFreeTime(result);

         if(mission.getTimeOfSeating() == null){
             if(mission.getTimeOfArriving() != null && mission.getTimeOfStarting()!=null){
                 if(mission.getTimeOfStarting().isAfter(mission.getTimeOfArriving())){
                     // водитель прибыл раньше на заказ
                     seconds = Seconds.secondsBetween(mission.getTimeOfArriving(), mission.getTimeOfStarting());
                     result += MathUtil.getRoundUp(Math.abs(seconds.getSeconds()));
                     timeWaitClientUtil.setHowLongWaitInFact(MathUtil.getRoundUp(Math.abs(Seconds.secondsBetween(mission.getTimeOfArriving(), DateTimeUtils.nowNovosib_GMT6()).getSeconds())));
                     result -= timeWaitClientUtil.getHowLongWaitInFact();
                 } else {
                     seconds = Seconds.secondsBetween(mission.getTimeOfArriving(), DateTimeUtils.nowNovosib_GMT6());
                     result = result - MathUtil.getRoundUp(Math.abs(seconds.getSeconds()));
                     timeWaitClientUtil.setHowLongWaitInFact(MathUtil.getRoundUp(Math.abs(seconds.getSeconds())));
                 }
             }
         } else {
             boolean earlier = false;
             if(mission.getTimeOfStarting().isAfter(mission.getTimeOfArriving())){
                 // водитель прибыл раньше на заказ
                 if(mission.getTimeOfStarting().isAfter(mission.getTimeOfSeating())){
                     // клиент сел в машину раньше назначенного времени
                     seconds = Seconds.secondsBetween(mission.getTimeOfArriving(), mission.getTimeOfSeating());
                     earlier = true;
                 }else{
                     // водитель прибыл раньше, клиент позже
                     seconds = Seconds.secondsBetween(mission.getTimeOfStarting(), mission.getTimeOfSeating());
                 }
             } else {
                 // водитель прибыл позже на заказ
                 seconds = Seconds.secondsBetween(mission.getTimeOfArriving(), mission.getTimeOfSeating());
             }
             if(earlier){
                 // оба прибыли раньше на заказ
                 result = result + MathUtil.getRoundUp(Math.abs(seconds.getSeconds()));
             } else {
                 result = result - MathUtil.getRoundUp(Math.abs(seconds.getSeconds()));
             }

                 timeWaitClientUtil.setHowLongWaitInFact(MathUtil.getRoundUp(Math.abs(seconds.getSeconds())));
         }
                 timeWaitClientUtil.setFreeTimeInFact(result);
                    return timeWaitClientUtil;
    }




    // количество времени, которое потратил водитель на ожидание клиента с момента приезда до момента когда клиент сел в машину
    public int waitDriverClientInMinutes(Mission mission){
        DateTime timeOfSeating =  mission.getTimeOfSeating();
        DateTime timeOfArriving;
        if(mission.getTimeOfStarting().isAfter(mission.getTimeOfArriving())){
            timeOfArriving =  mission.getTimeOfStarting();
        }else{
            timeOfArriving =  mission.getTimeOfArriving();
        }
        int minutesWaitDriver = 0;

        if(timeOfSeating!=null && timeOfArriving!=null){
            Seconds seconds = Seconds.secondsBetween(timeOfArriving, timeOfSeating);
                int min;
                int sec = seconds.getSeconds();
                if (sec % 60 != 0) {
                    min = (sec / 60 + 1);
                } else {
                    min = sec / 60;
                }
            minutesWaitDriver += min;
        }
        return minutesWaitDriver;
    }



    // стоимость времени пауз
    public AdministrationService.SumDetails calculatePriceForPauses(int autoClass, int pausedMinutes, AdministrationService.SumDetails sumDetails){
        int resultSum = 0;
        AutoClass autoClassComing = AutoClass.getByValue(autoClass);
        MissionRate missionRate = missionRatesRepository.findOne((long) 1);
        Set<AutoClassPrice> autoClassPriceSet = missionRate.getAutoClassPrices();
        if(autoClassPriceSet!=null){
            List<AutoClassPrice> autoClassPriceList = Lists.newArrayList(autoClassPriceSet.iterator());
            for(AutoClassPrice autoClassPriceDB :autoClassPriceList){
                if(autoClassComing.getValue()==autoClassPriceDB.getAutoClass().getValue()){
                    int costOneMinutesByClassAuto = autoClassPriceDB.getPerMinuteWaitAmount();
                    resultSum+= costOneMinutesByClassAuto*pausedMinutes;
                }
            }
            sumDetails.setSumForPauses(resultSum);
        }
        return sumDetails;
    }





    public AdministrationService.SumDetails calculatePriceForMission(int autoClass, long distance, List<MissionService> listService){
        AdministrationService.SumDetails sumDetails = new AdministrationService.SumDetails();
        int result = 0;
        AutoClass autoClassComing = AutoClass.getByValue(autoClass);
        MissionRate missionRate = missionRatesRepository.findOne((long) 1);
        Set<AutoClassPrice> autoClassPriceSet = missionRate.getAutoClassPrices();
        if(autoClassPriceSet!=null){
            List<AutoClassPrice> autoClassPriceList = Lists.newArrayList(autoClassPriceSet.iterator());
            for(AutoClassPrice autoClassPriceDB :autoClassPriceList){
                if(autoClassComing.getValue()==autoClassPriceDB.getAutoClass().getValue()){
                    long resPrice = calculatePriceByAutoClassAndDistance(distance, autoClassPriceDB, false);
                    result =(int)resPrice;
                }
            }
            sumDetails.setSumWithoutOptions(result); // сумма без опций
        }
        if(listService!=null){
            for (MissionService option : listService) {
                if (!MissionService.UNKNOWN.equals(option)) {
                    ru.trendtech.domain.ServicePrice servicesFromDB = servicesRepository.findByService(option);
                    result+=servicesFromDB.getMoney_amount();
                }
            }
            sumDetails.setSumWithOptions(result); // сумма с опциями
        }
        return sumDetails;
    }




    public int calculatePriceByAutoClassAndDistance(long distanceInKM, AutoClassPrice autoClassPriceDB, boolean isCorporate){
        int result;
        int kmIncluded = autoClassPriceDB.getKmIncluded();
        int priceKm = isCorporate? autoClassPriceDB.getPriceKmCorporate() : autoClassPriceDB.getPriceKm();
        int price = autoClassPriceDB.getPrice().getAmount().intValue();

        BigDecimal x = new BigDecimal(distanceInKM);
        BigDecimal roundedDistance = x.round(new MathContext(MathUtil.countDigit(x), RoundingMode.UP));

        int roundedDistanceInKM = roundedDistance.intValue();

        if(roundedDistanceInKM>kmIncluded){
            int diffKm = (roundedDistanceInKM-kmIncluded);
            result = diffKm*priceKm+price;
        }else{
            result = price;
        }
        return result;
    }






    public DirectionHelper routeBuildingFromLatLon2GIS(String query) {
        String answer = "";
        DirectionHelper directionHelper = new DirectionHelper();
        try {
            //String url="http://maps.googleapis.com/maps/api/directions/json?origin=" + latitude_from + "," + longitude_from + "&destination=" + latitude_to + "," + longitude_to + "&sensor=true&alternatives=true&units=metric&mode=driving";//&key="+GOOGLE_API_KEY;
            /*
                Допустимые значения:
                shortest - кратчайший маршрут
                optimal_statistic - оптимальный маршрут с учетом статистики
                optimal_jams - оптимальный маршрут с учетом пробок
                Значение по умолчанию - optimal_statistic
             */
            String url = "http://catalog.api.2gis.ru/2.0/transport/calculate_directions";
            List<NameValuePair> urlParameters = new ArrayList<>();
            urlParameters.add(new BasicNameValuePair("waypoints", query));
            urlParameters.add(new BasicNameValuePair("routing_type", "shortest")); // кратчайшие
            urlParameters.add(new BasicNameValuePair("key", GIS_API_KEY));

            answer = HTTPUtil.senPostQuery(url, urlParameters);

            JSONObject answerJson = new JSONObject(answer);

            //all result = {"meta":{"code":200},"result":{"total":1,"items":[{"type":"optimal_statistic","total_duration":697,"total_distance":3613,

            if(answerJson.has("result")){
                JSONObject resultJson = (JSONObject) answerJson.get("result");

                JSONArray itemsArray = (JSONArray) resultJson.get("items");
                JSONObject totalDistance = (JSONObject) itemsArray.get(0);

                String totalStr = totalDistance.get("total_distance").toString();
                String totalTimeDuration = totalDistance.get("total_duration").toString();
                directionHelper.setDistance(Integer.parseInt(totalStr));
                directionHelper.setTimeDuration(Integer.parseInt(totalTimeDuration)/60);
            }else{
                LOGGER.info("2GIS answer: В json ответе 2gis не найден параметр result");
                directionHelper.setTimeDuration(-1);
            }
            //result = Integer.parseInt(totalStr);
        }catch(JSONException n){
            LOGGER.debug("2GIS: routeBuildingFromLatLon2GIS exception: "+n.getMessage()+" answer: "+answer);
            n.printStackTrace();
        }
        return directionHelper;
    }
}
