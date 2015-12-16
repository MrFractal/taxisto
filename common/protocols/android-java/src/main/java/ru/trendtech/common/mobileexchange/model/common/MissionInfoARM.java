package ru.trendtech.common.mobileexchange.model.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 18.11.2014.
 */
public class MissionInfoARM {
    private long id;
    private long timeOfStart;
    private long timeOfRequesting;
    private long timeOfAssigning;
    private long timeOfArriving;
    private long timeOfSeating; // means of this field  ??????
    private long timeOfFinishing;
    private long timeOfPayment;
    private boolean booked = false;
    private boolean useBonuses;
    private List<Integer> expectedArrivalTimes = new ArrayList<Integer>();
    // OPTIONS: CHILDREN = 1, ANIMAL_IN_CONTAINER = 2, ANIMAL_WITHOUT_CONTAINER = 3, BAGGAGE = 4, SMOKING = 5
    private List<Integer> options = new ArrayList<Integer>();
    private double rating; // private byte rating;
    private boolean fixedMission;
    private String addressFrom;
    private String addressTo;
    private String cityTo;
    private String cityFrom;
    private String regionTo;
    private String regionFrom;
    private int porchNumber;
    private ItemLocation locationFrom;
    private ItemLocation locationTo;
    private List<MissionAddressesInfo> missionAddressesInfos = new ArrayList<MissionAddressesInfo>();
    // TYPES: CASH = 1, CARD = 2
    private int paymentType;
    private double price;
    private double expectedPrice;
    private double sumIncrease;
    private long expectedDistance;
    private int paymentCardPrice;
    // TYPES: STANDARD = 1, COMFORT = 2, BUSINESS = 3
    private int autoType;
    private String autoTypeStr;
    private String comment;
    private DriverInfoARM driverInfoARM;
    private ClientInfo clientInfo;
    private boolean rerouting;
    private long distance;
    private String missionState;
    private String paymentStateCard;
    private int lateDriverBookedMin;
    private String paymentDescription;
    private Boolean isLate;
    private String cancelBy;
    // заказ на через timeAfterMin минут
    private Integer timeAfterMin = 0;
    private MissionCanceledInfo missionCanceledInfo;

    public MissionCanceledInfo getMissionCanceledInfo() {
        return missionCanceledInfo;
    }

    public void setMissionCanceledInfo(MissionCanceledInfo missionCanceledInfo) {
        this.missionCanceledInfo = missionCanceledInfo;
    }

    public Integer getTimeAfterMin() {
        return timeAfterMin;
    }

    public void setTimeAfterMin(Integer timeAfterMin) {
        this.timeAfterMin = timeAfterMin;
    }

    public String getAutoTypeStr() {
        return autoTypeStr;
    }

    public void setAutoTypeStr(String autoTypeStr) {
        this.autoTypeStr = autoTypeStr;
    }

    public String getCancelBy() {
        return cancelBy;
    }

    public void setCancelBy(String cancelBy) {
        this.cancelBy = cancelBy;
    }

    public Boolean getIsLate() {
        return isLate;
    }

    public void setIsLate(Boolean isLate) {
        this.isLate = isLate;
    }

    public String getPaymentDescription() {
        return paymentDescription;
    }

    public void setPaymentDescription(String paymentDescription) {
        this.paymentDescription = paymentDescription;
    }

    public double getSumIncrease() {
        return sumIncrease;
    }

    public void setSumIncrease(double sumIncrease) {
        this.sumIncrease = sumIncrease;
    }

    public int getPaymentCardPrice() {
        return paymentCardPrice;
    }

    public void setPaymentCardPrice(int paymentCardPrice) {
        this.paymentCardPrice = paymentCardPrice;
    }

    public List<MissionAddressesInfo> getMissionAddressesInfos() {
        return missionAddressesInfos;
    }

    public void setMissionAddressesInfos(List<MissionAddressesInfo> missionAddressesInfos) {
        this.missionAddressesInfos = missionAddressesInfos;
    }

    public int getLateDriverBookedMin() {
        return lateDriverBookedMin;
    }

    public void setLateDriverBookedMin(int lateDriverBookedMin) {
        this.lateDriverBookedMin = lateDriverBookedMin;
    }

    public String getPaymentStateCard() {
        return paymentStateCard;
    }

    public void setPaymentStateCard(String paymentStateCard) {
        this.paymentStateCard = paymentStateCard;
    }

    public String getCityTo() {
        return cityTo;
    }

    public void setCityTo(String cityTo) {
        this.cityTo = cityTo;
    }

    public String getCityFrom() {
        return cityFrom;
    }

    public void setCityFrom(String cityFrom) {
        this.cityFrom = cityFrom;
    }

    public String getRegionTo() {
        return regionTo;
    }

    public void setRegionTo(String regionTo) {
        this.regionTo = regionTo;
    }

    public String getRegionFrom() {
        return regionFrom;
    }

    public void setRegionFrom(String regionFrom) {
        this.regionFrom = regionFrom;
    }

    public String getMissionState() {
        return missionState;
    }

    public void setMissionState(String missionState) {
        this.missionState = missionState;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public boolean isRerouting() {
        return rerouting;
    }

    public void setRerouting(boolean rerouting) {
        this.rerouting = rerouting;
    }

    public boolean isUseBonuses() {
        return useBonuses;
    }

    public void setUseBonuses(boolean useBonuses) {
        this.useBonuses = useBonuses;
    }

    public long getExpectedDistance() {
        return expectedDistance;
    }

    public void setExpectedDistance(long expectedDistance) {
        this.expectedDistance = expectedDistance;
    }

    public double getExpectedPrice() {
        return expectedPrice;
    }

    public void setExpectedPrice(double expectedPrice) {
        this.expectedPrice = expectedPrice;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Integer> getExpectedArrivalTimes() {
        return expectedArrivalTimes;
    }

    public void setExpectedArrivalTimes(List<Integer> expectedArrivalTimes) {
        this.expectedArrivalTimes = expectedArrivalTimes;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getAddressFrom() {
        return addressFrom;
    }

    public void setAddressFrom(String addressFrom) {
        this.addressFrom = addressFrom;
    }

    public String getAddressTo() {
        return addressTo;
    }

    public void setAddressTo(String addressTo) {
        this.addressTo = addressTo;
    }

    public ItemLocation getLocationFrom() {
        return locationFrom;
    }

    public void setLocationFrom(ItemLocation locationFrom) {
        this.locationFrom = locationFrom;
    }

    public ItemLocation getLocationTo() {
        return locationTo;
    }

    public void setLocationTo(ItemLocation locationTo) {
        this.locationTo = locationTo;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public DriverInfoARM getDriverInfoARM() {
        return driverInfoARM;
    }

    public void setDriverInfoARM(DriverInfoARM driverInfoARM) {
        this.driverInfoARM = driverInfoARM;
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    public int getAutoType() {
        return autoType;
    }

    public void setAutoType(int autoType) {
        this.autoType = autoType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Integer> getOptions() {
        return options;
    }

    public void setOptions(List<Integer> options) {
        this.options = options;
    }

    public boolean isFixedMission() {
        return fixedMission;
    }

    public void setFixedMission(boolean fixedMission) {
        this.fixedMission = fixedMission;
    }

    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    public long getTimeOfStart() {
        return timeOfStart;
    }

    public void setTimeOfStart(long timeOfStart) {
        this.timeOfStart = timeOfStart;
    }

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

    public long getTimeOfArriving() {
        return timeOfArriving;
    }

    public void setTimeOfArriving(long timeOfArriving) {
        this.timeOfArriving = timeOfArriving;
    }

    public long getTimeOfSeating() {
        return timeOfSeating;
    }

    public void setTimeOfSeating(long timeOfSeating) {
        this.timeOfSeating = timeOfSeating;
    }

    public long getTimeOfFinishing() {
        return timeOfFinishing;
    }

    public void setTimeOfFinishing(long timeOfFinishing) {
        this.timeOfFinishing = timeOfFinishing;
    }

    public long getTimeOfPayment() {
        return timeOfPayment;
    }

    public void setTimeOfPayment(long timeOfPayment) {
        this.timeOfPayment = timeOfPayment;
    }

    public int getPorchNumber() {
        return porchNumber;
    }

    public void setPorchNumber(int porchNumber) {
        this.porchNumber = porchNumber;
    }
}
