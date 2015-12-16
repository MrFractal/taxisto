package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by max on 09.02.14.
 */
public class ClientInfoRequest extends ClientRequest{
    private ClientInfo clientInfo;
    private String security_token;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public ClientInfoRequest() {
        super();
    }

    public ClientInfoRequest(long clientId) {
        super(clientId);
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }
}
