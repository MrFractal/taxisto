package ru.trendtech.common.mobileexchange.model.common.sms;

/**
 * Created by petr on 01.08.14.
 */
public class SMSCodeRequest {
    private String login;
    private String security_token;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
