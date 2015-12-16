package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by ivanenok on 4/14/2014.
 */
public class InviteFriendRequest {
    private long driverId;
    private String phone;

    public InviteFriendRequest() {
    }

    public InviteFriendRequest(long driverId, String phone) {
        this.driverId = driverId;
        this.phone = phone;
    }

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
