package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 30.06.2015.
 */
public class RouterInfo {
    private Long id;
    private String model;
    private String macAddress;
    private String simNumber;
    private String ip;
    private long timeOfPurchase;
    private DriverInfoARM driverInfoARM;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public DriverInfoARM getDriverInfoARM() {
        return driverInfoARM;
    }

    public void setDriverInfoARM(DriverInfoARM driverInfoARM) {
        this.driverInfoARM = driverInfoARM;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getSimNumber() {
        return simNumber;
    }

    public void setSimNumber(String simNumber) {
        this.simNumber = simNumber;
    }

    public long getTimeOfPurchase() {
        return timeOfPurchase;
    }

    public void setTimeOfPurchase(long timeOfPurchase) {
        this.timeOfPurchase = timeOfPurchase;
    }
}
