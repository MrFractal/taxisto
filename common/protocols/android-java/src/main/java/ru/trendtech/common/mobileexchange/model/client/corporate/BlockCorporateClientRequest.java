package ru.trendtech.common.mobileexchange.model.client.corporate;

/**
 * Created by petr on 20.03.2015.
 */
public class BlockCorporateClientRequest {
    private String security_token;
    private long mainClientId;
    private long clientId;
    private String reason;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public long getMainClientId() {
        return mainClientId;
    }

    public void setMainClientId(long mainClientId) {
        this.mainClientId = mainClientId;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }


    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
