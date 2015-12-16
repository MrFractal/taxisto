package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 14.11.2014.
 */
public class AssistantRequest {
    private String def;
    private String security_token;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }


    public String getDef() {
        return def;
    }

    public void setDef(String def) {
        this.def = def;
    }
}
