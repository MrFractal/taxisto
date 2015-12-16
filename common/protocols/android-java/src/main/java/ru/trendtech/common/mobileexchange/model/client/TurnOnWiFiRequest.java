package ru.trendtech.common.mobileexchange.model.client;

/**
 * Created by petr on 06.07.2015.
 */
public class TurnOnWiFiRequest {
    private String security_token;
    private boolean turnOn;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public boolean isTurnOn() {
        return turnOn;
    }

    public void setTurnOn(boolean turnOn) {
        this.turnOn = turnOn;
    }
}
