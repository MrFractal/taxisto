package ru.trendtech.common.mobileexchange.model.client;

/**
 * Created by max on 06.02.14.
 */
public class RegistrationConfirmRequest {

    private String login;
    private String codeSMS; // must have formart XXXXXX where X - digit


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getCodeSMS() {
        return codeSMS;
    }

    public void setCodeSMS(String codeSMS) {
        this.codeSMS = codeSMS;
    }
}
