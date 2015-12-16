package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 26.01.2015.
 */
public class FindGroupBySectionARMRequest {
    private String security_token;
    private String section;

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }
}
