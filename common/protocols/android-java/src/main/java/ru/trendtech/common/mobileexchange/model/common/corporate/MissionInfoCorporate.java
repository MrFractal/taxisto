package ru.trendtech.common.mobileexchange.model.common.corporate;

import ru.trendtech.common.mobileexchange.model.common.*;
import ru.trendtech.common.mobileexchange.model.common.rates.AutoClassRateInfoV2;
import ru.trendtech.common.mobileexchange.model.common.rates.ServicePriceInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 14.03.2015.
 */
public class MissionInfoCorporate {
    private long id;
    private long timeOfRequesting;
    private long timeOfArriving;
    private long timeOfFinishing;
    private long timeOfSeating;
    private long timeOfStart;
    private String addressFrom;
    private String addressTo;
    private List<MissionAddressesInfo> missionAddressesInfos = new ArrayList<MissionAddressesInfo>();
    // TYPES: CASH = 1, CARD = 2
    private int paymentType;
    private double price;
    private int paymentCardPrice;
    // TYPES: STANDARD = 1, COMFORT = 2, BUSINESS = 3, LOW_COSTER = 4
    private String autoTypeStr;
    private DriverInfoCorporate driverInfo;
    private ClientInfoCorporate clientInfo;
    private List<ServicePriceInfo> servicePriceInfos = new ArrayList<ServicePriceInfo>();
    private AutoClassRateInfoV2 autoClassRateInfo;
    private long distance;
    private long expectedDistance;
    private int pausesMin;
    private int costPaymentWait;
    private int minPaymentWait;

    public int getMinPaymentWait() {
        return minPaymentWait;
    }

    public void setMinPaymentWait(int minPaymentWait) {
        this.minPaymentWait = minPaymentWait;
    }

    public int getCostPaymentWait() {
        return costPaymentWait;
    }

    public void setCostPaymentWait(int costPaymentWait) {
        this.costPaymentWait = costPaymentWait;
    }

    public long getExpectedDistance() {
        return expectedDistance;
    }

    public void setExpectedDistance(long expectedDistance) {
        this.expectedDistance = expectedDistance;
    }

    public long getTimeOfStart() {
        return timeOfStart;
    }

    public void setTimeOfStart(long timeOfStart) {
        this.timeOfStart = timeOfStart;
    }

    public long getTimeOfSeating() {
        return timeOfSeating;
    }

    public void setTimeOfSeating(long timeOfSeating) {
        this.timeOfSeating = timeOfSeating;
    }

    public int getPausesMin() {
        return pausesMin;
    }

    public void setPausesMin(int pausesMin) {
        this.pausesMin = pausesMin;
    }

    public String getAutoTypeStr() {
        return autoTypeStr;
    }

    public void setAutoTypeStr(String autoTypeStr) {
        this.autoTypeStr = autoTypeStr;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public AutoClassRateInfoV2 getAutoClassRateInfo() {
        return autoClassRateInfo;
    }

    public void setAutoClassRateInfo(AutoClassRateInfoV2 autoClassRateInfo) {
        this.autoClassRateInfo = autoClassRateInfo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTimeOfRequesting() {
        return timeOfRequesting;
    }

    public void setTimeOfRequesting(long timeOfRequesting) {
        this.timeOfRequesting = timeOfRequesting;
    }

    public long getTimeOfArriving() {
        return timeOfArriving;
    }

    public void setTimeOfArriving(long timeOfArriving) {
        this.timeOfArriving = timeOfArriving;
    }

    public long getTimeOfFinishing() {
        return timeOfFinishing;
    }

    public void setTimeOfFinishing(long timeOfFinishing) {
        this.timeOfFinishing = timeOfFinishing;
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

    public List<MissionAddressesInfo> getMissionAddressesInfos() {
        return missionAddressesInfos;
    }

    public void setMissionAddressesInfos(List<MissionAddressesInfo> missionAddressesInfos) {
        this.missionAddressesInfos = missionAddressesInfos;
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

    public int getPaymentCardPrice() {
        return paymentCardPrice;
    }

    public void setPaymentCardPrice(int paymentCardPrice) {
        this.paymentCardPrice = paymentCardPrice;
    }

    public DriverInfoCorporate getDriverInfo() {
        return driverInfo;
    }

    public void setDriverInfo(DriverInfoCorporate driverInfo) {
        this.driverInfo = driverInfo;
    }

    public ClientInfoCorporate getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(ClientInfoCorporate clientInfo) {
        this.clientInfo = clientInfo;
    }

    public List<ServicePriceInfo> getServicePriceInfos() {
        return servicePriceInfos;
    }

    public void setServicePriceInfos(List<ServicePriceInfo> servicePriceInfos) {
        this.servicePriceInfos = servicePriceInfos;
    }
}
