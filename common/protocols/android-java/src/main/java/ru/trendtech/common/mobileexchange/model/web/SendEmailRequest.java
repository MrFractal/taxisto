package ru.trendtech.common.mobileexchange.model.web;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 10.12.2014.
 */
public class SendEmailRequest {
   private String email;
   private String htmlText;
   private String subject;
   private String security_token;
   private List<Long> clientIds = new ArrayList<Long>();
   private int typeEmail; /* typeEmail: 1 - thanks, 2 - sorry, 3 - welcome*/
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Long> getClientIds() {
        return clientIds;
    }

    public void setClientIds(List<Long> clientIds) {
        this.clientIds = clientIds;
    }

    public int getTypeEmail() {
        return typeEmail;
    }

    public void setTypeEmail(int typeEmail) {
        this.typeEmail = typeEmail;
    }

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHtmlText() {
        return htmlText;
    }

    public void setHtmlText(String htmlText) {
        this.htmlText = htmlText;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
