package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 14.11.2014.
 */
public class AssistantDeleteRequest {
    private long assistantId;
    private String security_token;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public long getAssistantId() {
        return assistantId;
    }

    public void setAssistantId(long assistantId) {
        this.assistantId = assistantId;
    }
}
