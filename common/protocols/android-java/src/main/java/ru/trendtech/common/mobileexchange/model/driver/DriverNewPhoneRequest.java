package ru.trendtech.common.mobileexchange.model.driver;

public class DriverNewPhoneRequest {

    private long driverId;
    private String phone;

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
