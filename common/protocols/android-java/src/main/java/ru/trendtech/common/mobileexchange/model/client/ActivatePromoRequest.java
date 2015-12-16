package ru.trendtech.common.mobileexchange.model.client;

/**
 * Created by petr on 12.03.2015.
 */
public class ActivatePromoRequest {
    private long clientId;
    private String text;
    private String security_token;

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }
}
