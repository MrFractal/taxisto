package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 29.06.2015.
 */
public class FantomInfo {
    private ItemLocation itemLocation;
    private Boolean active;
    private DriverInfoARM driverInfoARM;

    public ItemLocation getItemLocation() {
        return itemLocation;
    }

    public void setItemLocation(ItemLocation itemLocation) {
        this.itemLocation = itemLocation;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public DriverInfoARM getDriverInfoARM() {
        return driverInfoARM;
    }

    public void setDriverInfoARM(DriverInfoARM driverInfoARM) {
        this.driverInfoARM = driverInfoARM;
    }
}
