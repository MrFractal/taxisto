package ru.trendtech.common.mobileexchange.model.common.support;

/**
 * File created by max on 20/05/2014 20:40.
 */


public class SupportPhone {
    private String mobile;
    private String voip;
    private String message;

    public SupportPhone() {
    }

    public SupportPhone(String mobile, String voip) {
        this(mobile, voip, null);
    }

    public SupportPhone(String mobile, String voip, String message) {
        this.mobile = mobile;
        this.voip = voip;
        this.message = message;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getVoip() {
        return voip;
    }

    public void setVoip(String voip) {
        this.voip = voip;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
