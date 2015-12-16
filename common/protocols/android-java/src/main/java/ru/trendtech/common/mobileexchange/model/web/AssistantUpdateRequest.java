package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 14.11.2014.
 */
public class AssistantUpdateRequest {
    private AssistantInfo assistantInfo;
    private String security_token;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public AssistantInfo getAssistantInfo() {
        return assistantInfo;
    }

    public void setAssistantInfo(AssistantInfo assistantInfo) {
        this.assistantInfo = assistantInfo;
    }
}
