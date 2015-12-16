package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverNewPhoneResponse {

    private boolean sendCode = false;
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isSendCode() {
        return sendCode;
    }

    public void setSendCode(boolean sendCode) {
        this.sendCode = sendCode;
    }
}
