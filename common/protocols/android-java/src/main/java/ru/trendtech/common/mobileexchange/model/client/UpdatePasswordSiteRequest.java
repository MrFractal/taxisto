package ru.trendtech.common.mobileexchange.model.client;

/**
 * Created by petr on 18.09.2014.
 */
public class UpdatePasswordSiteRequest {
    private String phone;
    private String passwordNew;
    private String passwordOld;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPasswordNew() {
        return passwordNew;
    }

    public void setPasswordNew(String passwordNew) {
        this.passwordNew = passwordNew;
    }

    public String getPasswordOld() {
        return passwordOld;
    }

    public void setPasswordOld(String passwordOld) {
        this.passwordOld = passwordOld;
    }
}
