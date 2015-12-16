package ru.trendtech.common.mobileexchange.model.courier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ClientInfo;
import ru.trendtech.common.mobileexchange.model.common.DriverInfo;
import ru.trendtech.common.mobileexchange.model.common.ItemLocation;
import ru.trendtech.common.mobileexchange.model.web.WebUserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 21.08.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderInfo {
    private long id;
    private String nameOfGroup;
    private long timeOfRequesting;
    private long timeOfAssigning;

    private String timeOfFinishing; // время завершения заказа (задается пользователем)
    private long timeOfFinishingExpected; // ожидаемое время завершения
    private long timeOfFinishingInFact; // время завершения заказа по факту

    private long timeOfReadyToGo; // время когда водитель собрал весь товар, или нечто откуда нужно привезти, и готов ехать к клиенту
    private long timeOfReadyToProgress;
    private long timeOfStartingOrderProcessing; // время когда диспетчер взял заказ на обработку
    // private long expectedTimeOfCompleting; // ожидаемое время завершения заказа
    private int expectedArrivalTime; // расчетное время прибытия
    private int courierLateMinutes; // суммарное время опоздание курьера
    private int orderType; // [UNKNOWN(0),BUY_AND_DELIVER(1),TAKE_AND_DELIVER(2),OTHER(3)]
    private int paymentType;
    private String state;
    private DriverInfo driverInfo;
    private ClientInfo clientInfo;
    private int priceExpected;
    private int priceInFact;
    private int priceDelivery; // стоимость доставки

    private int priceItems; // стоимость за товар - по факту
    private int priceItemsExpected; // ожидаемая


    private int distanceExpectedToStore; // ожидаемая длина дистанции по магазинам
    private int distanceInFactToStore; // фактическая длина дистанции по магазинам
    private int distanceExpected; // ожидаемая общая длинна дистанции (по магазинам + к клиенту)
    private int distanceInFact; // фактическая общая длинна дистанции (по магазинам + к клиенту)
    //private List<StoreAddressInfo> storeAddressInfos = new ArrayList<StoreAddressInfo>();
    private List<OrderAddressInfo> targetAddressesInfo = new ArrayList<OrderAddressInfo>();
    private List<OrderItemPriceInfo> orderItemPriceInfos = new ArrayList<OrderItemPriceInfo>(); // список товаров и услуг
    private List<ClientItemInfo> clientItemInfos = new ArrayList<ClientItemInfo>(); // // то что натыкал пользователь (не подтверждено диспетчером)
    private CommentInfo commentInfo;
    private int increasePercent;
    private boolean isBooked;
    private int percentInsuranceOnDayOfOrder; // % страховки в день покупки
    private int priceOfInsurance; // оценочная стоимость(страховка товара)
    private ItemLocation currentCourierLocation;

    // todo: WebUserInfo webUserInfo;


    public long getTimeOfFinishingExpected() {
        return timeOfFinishingExpected;
    }

    public String getTimeOfFinishing() {
        return timeOfFinishing;
    }

    public void setTimeOfFinishing(String timeOfFinishing) {
        this.timeOfFinishing = timeOfFinishing;
    }

    public void setTimeOfFinishingExpected(long timeOfFinishingExpected) {
        this.timeOfFinishingExpected = timeOfFinishingExpected;
    }

    public int getCourierLateMinutes() {
        return courierLateMinutes;
    }

    public void setCourierLateMinutes(int courierLateMinutes) {
        this.courierLateMinutes = courierLateMinutes;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public ItemLocation getCurrentCourierLocation() {
        return currentCourierLocation;
    }

    public void setCurrentCourierLocation(ItemLocation currentCourierLocation) {
        this.currentCourierLocation = currentCourierLocation;
    }

    public int getExpectedArrivalTime() {
        return expectedArrivalTime;
    }

    public void setExpectedArrivalTime(int expectedArrivalTime) {
        this.expectedArrivalTime = expectedArrivalTime;
    }

    public long getTimeOfReadyToProgress() {
        return timeOfReadyToProgress;
    }

    public void setTimeOfReadyToProgress(long timeOfReadyToProgress) {
        this.timeOfReadyToProgress = timeOfReadyToProgress;
    }

    public int getPriceItemsExpected() {
        return priceItemsExpected;
    }

    public void setPriceItemsExpected(int priceItemsExpected) {
        this.priceItemsExpected = priceItemsExpected;
    }


        public int getPriceItems() {
        return priceItems;
    }

    public void setPriceItems(int priceItems) {
        this.priceItems = priceItems;
    }

    public int getPercentInsuranceOnDayOfOrder() {
        return percentInsuranceOnDayOfOrder;
    }

    public void setPercentInsuranceOnDayOfOrder(int percentInsuranceOnDayOfOrder) {
        this.percentInsuranceOnDayOfOrder = percentInsuranceOnDayOfOrder;
    }

    public int getPriceOfInsurance() {
        return priceOfInsurance;
    }

    public void setPriceOfInsurance(int priceOfInsurance) {
        this.priceOfInsurance = priceOfInsurance;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setIsBooked(boolean isBooked) {
        this.isBooked = isBooked;
    }

    public String getNameOfGroup() {
        return nameOfGroup;
    }

    public void setNameOfGroup(String nameOfGroup) {
        this.nameOfGroup = nameOfGroup;
    }

//    public long getExpectedTimeOfCompleting() {
//        return expectedTimeOfCompleting;
//    }
//
//    public void setExpectedTimeOfCompleting(long expectedTimeOfCompleting) {
//        this.expectedTimeOfCompleting = expectedTimeOfCompleting;
//    }

    public int getIncreasePercent() {
        return increasePercent;
    }

    public void setIncreasePercent(int increasePercent) {
        this.increasePercent = increasePercent;
    }

    public int getPriceDelivery() {
        return priceDelivery;
    }

    public void setPriceDelivery(int priceDelivery) {
        this.priceDelivery = priceDelivery;
    }

    public long getTimeOfStartingOrderProcessing() {
        return timeOfStartingOrderProcessing;
    }

    public void setTimeOfStartingOrderProcessing(long timeOfStartingOrderProcessing) {
        this.timeOfStartingOrderProcessing = timeOfStartingOrderProcessing;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

//    public String getTimeOfStarting() {
//        return timeOfStarting;
//    }
//
//    public void setTimeOfStarting(String timeOfStarting) {
//        this.timeOfStarting = timeOfStarting;
//    }

    public long getTimeOfRequesting() {
        return timeOfRequesting;
    }

    public void setTimeOfRequesting(long timeOfRequesting) {
        this.timeOfRequesting = timeOfRequesting;
    }

    public long getTimeOfAssigning() {
        return timeOfAssigning;
    }

    public void setTimeOfAssigning(long timeOfAssigning) {
        this.timeOfAssigning = timeOfAssigning;
    }

//    public long getTimeOfFinishing() {
//        return timeOfFinishing;
//    }
//
//    public void setTimeOfFinishing(long timeOfFinishing) {
//        this.timeOfFinishing = timeOfFinishing;
//    }


//    public String getTimeOfFinishingExpected() {
//        return timeOfFinishingExpected;
//    }
//
//    public void setTimeOfFinishingExpected(String timeOfFinishingExpected) {
//        this.timeOfFinishingExpected = timeOfFinishingExpected;
//    }

    public long getTimeOfFinishingInFact() {
        return timeOfFinishingInFact;
    }

    public void setTimeOfFinishingInFact(long timeOfFinishingInFact) {
        this.timeOfFinishingInFact = timeOfFinishingInFact;
    }

    public long getTimeOfReadyToGo() {
        return timeOfReadyToGo;
    }

    public void setTimeOfReadyToGo(long timeOfReadyToGo) {
        this.timeOfReadyToGo = timeOfReadyToGo;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public DriverInfo getDriverInfo() {
        return driverInfo;
    }

    public void setDriverInfo(DriverInfo driverInfo) {
        this.driverInfo = driverInfo;
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    public int getPriceExpected() {
        return priceExpected;
    }

    public void setPriceExpected(int priceExpected) {
        this.priceExpected = priceExpected;
    }

    public int getPriceInFact() {
        return priceInFact;
    }

    public void setPriceInFact(int priceInFact) {
        this.priceInFact = priceInFact;
    }

    public int getDistanceExpectedToStore() {
        return distanceExpectedToStore;
    }

    public void setDistanceExpectedToStore(int distanceExpectedToStore) {
        this.distanceExpectedToStore = distanceExpectedToStore;
    }

    public int getDistanceInFactToStore() {
        return distanceInFactToStore;
    }

    public void setDistanceInFactToStore(int distanceInFactToStore) {
        this.distanceInFactToStore = distanceInFactToStore;
    }

    public int getDistanceExpected() {
        return distanceExpected;
    }

    public void setDistanceExpected(int distanceExpected) {
        this.distanceExpected = distanceExpected;
    }

    public int getDistanceInFact() {
        return distanceInFact;
    }

    public void setDistanceInFact(int distanceInFact) {
        this.distanceInFact = distanceInFact;
    }

//    public List<StoreAddressInfo> getStoreAddressInfos() {
//        return storeAddressInfos;
//    }
//
//    public void setStoreAddressInfos(List<StoreAddressInfo> storeAddressInfos) {
//        this.storeAddressInfos = storeAddressInfos;
//    }

    public List<OrderAddressInfo> getTargetAddressesInfo() {
        return targetAddressesInfo;
    }

    public void setTargetAddressesInfo(List<OrderAddressInfo> targetAddressesInfo) {
        this.targetAddressesInfo = targetAddressesInfo;
    }

    public List<OrderItemPriceInfo> getOrderItemPriceInfos() {
        return orderItemPriceInfos;
    }

    public void setOrderItemPriceInfos(List<OrderItemPriceInfo> orderItemPriceInfos) {
        this.orderItemPriceInfos = orderItemPriceInfos;
    }

    public List<ClientItemInfo> getClientItemInfos() {
        return clientItemInfos;
    }

    public void setClientItemInfos(List<ClientItemInfo> clientItemInfos) {
        this.clientItemInfos = clientItemInfos;
    }

    public CommentInfo getCommentInfo() {
        return commentInfo;
    }

    public void setCommentInfo(CommentInfo commentInfo) {
        this.commentInfo = commentInfo;
    }
}
