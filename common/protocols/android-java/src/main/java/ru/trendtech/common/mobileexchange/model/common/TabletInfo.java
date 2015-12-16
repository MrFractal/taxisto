package ru.trendtech.common.mobileexchange.model.common;


/**
 * Created by petr on 22.06.2015.
 */
public class TabletInfo{
    private long id;
    private String model;
    private String imeiNumber;
    private String phone;
    private Integer tabletState;
    private long timeOfUpdate;
    private Boolean own;
    private DriverInfoARM driverInfoARM;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public DriverInfoARM getDriverInfoARM() {
        return driverInfoARM;
    }

    public void setDriverInfoARM(DriverInfoARM driverInfoARM) {
        this.driverInfoARM = driverInfoARM;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getImeiNumber() {
        return imeiNumber;
    }

    public void setImeiNumber(String imeiNumber) {
        this.imeiNumber = imeiNumber;
    }

    public Integer getTabletState() {
        return tabletState;
    }

    public void setTabletState(Integer tabletState) {
        this.tabletState = tabletState;
    }

    public long getTimeOfUpdate() {
        return timeOfUpdate;
    }

    public void setTimeOfUpdate(long timeOfUpdate) {
        this.timeOfUpdate = timeOfUpdate;
    }

    public Boolean isOwn() {
        return own;
    }

    public void setOwn(Boolean own) {
        this.own = own;
    }
}
