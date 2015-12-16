package ru.trendtech.common.mobileexchange.model.client;

/**
 * Created by petr on 17.09.2014.
 */
public class PropertyRequest {
    private String propName;
    private String security_token;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public String getPropName() {
        return propName;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }
}
