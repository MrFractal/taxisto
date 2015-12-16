package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.AdditionalServiceInfo;
import ru.trendtech.common.mobileexchange.model.common.DriverTypeInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 03.04.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverSettingInfo {
    private Long id;
    private long driverId;
    private boolean robotActive;
    private int defaultRadius; // [1-15]
    private String toAddress;
    private double toLat;
    private double toLon;
    /* включить цепочку заказов [предлагать водиле следующий заказ в радиусе] */
    private boolean nextOffer;
    private boolean aeroport;
    /* список услуг, которые может предоставить водитель [детское кресло, клетка для животных ...] */
    private List<Integer> driverServices = new ArrayList<Integer>();
    /* выбор тарифа для водителя: если он выбирает только Комфорт и Бизнес, то он видит только эти заказы */
    private List<Integer> driverAutoClass = new ArrayList<Integer>();
    /* список доп сервисов, которые может предоставить водитель [мясорубка ...] */
    private List<AdditionalServiceInfo> additionalServices = new ArrayList<AdditionalServiceInfo>();
    private int sumIncrease = 0;

    /* курьер */
    private boolean courier = false;
    private String cardNumber;
    private int depositeAmount; // поле «Депозит» - сумма, на которую водитель готов принимать заказы, если необходимо что-то купить.
    private boolean pedestrian = false; // поле "Пеший" - если Курьер без авто.
    private DriverTypeInfo driverTypeInfo;

    public DriverTypeInfo getDriverTypeInfo() {
        return driverTypeInfo;
    }

    public void setDriverTypeInfo(DriverTypeInfo driverTypeInfo) {
        this.driverTypeInfo = driverTypeInfo;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getDepositeAmount() {
        return depositeAmount;
    }

    public void setDepositeAmount(int depositeAmount) {
        this.depositeAmount = depositeAmount;
    }

    public boolean isPedestrian() {
        return pedestrian;
    }

    public void setPedestrian(boolean pedestrian) {
        this.pedestrian = pedestrian;
    }

    public boolean isCourier() {
        return courier;
    }

    public void setCourier(boolean courier) {
        this.courier = courier;
    }

    public int getSumIncrease() {
        return sumIncrease;
    }

    public void setSumIncrease(int sumIncrease) {
        this.sumIncrease = sumIncrease;
    }

    public List<AdditionalServiceInfo> getAdditionalServices() {
        return additionalServices;
    }

    public void setAdditionalServices(List<AdditionalServiceInfo> additionalServices) {
        this.additionalServices = additionalServices;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public boolean isRobotActive() {
        return robotActive;
    }

    public void setRobotActive(boolean robotActive) {
        this.robotActive = robotActive;
    }

    public int getDefaultRadius() {
        return defaultRadius;
    }

    public void setDefaultRadius(int defaultRadius) {
        this.defaultRadius = defaultRadius;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public double getToLat() {
        return toLat;
    }

    public void setToLat(double toLat) {
        this.toLat = toLat;
    }

    public double getToLon() {
        return toLon;
    }

    public void setToLon(double toLon) {
        this.toLon = toLon;
    }

    public boolean isNextOffer() {
        return nextOffer;
    }

    public void setNextOffer(boolean nextOffer) {
        this.nextOffer = nextOffer;
    }

    public boolean isAeroport() {
        return aeroport;
    }

    public void setAeroport(boolean aeroport) {
        this.aeroport = aeroport;
    }

    public List<Integer> getDriverServices() {
        return driverServices;
    }

    public void setDriverServices(List<Integer> driverServices) {
        this.driverServices = driverServices;
    }

    public List<Integer> getDriverAutoClass() {
        return driverAutoClass;
    }

    public void setDriverAutoClass(List<Integer> driverAutoClass) {
        this.driverAutoClass = driverAutoClass;
    }
}
