package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

/**
 * Created by petr on 14.11.2014.
 */
public class AssistantFindResponse {
    private AssistantInfo assistantInfo;
    private ErrorCodeHelper errorCodeHelper = new ErrorCodeHelper();

    public AssistantInfo getAssistantInfo() {
        return assistantInfo;
    }

    public void setAssistantInfo(AssistantInfo assistantInfo) {
        this.assistantInfo = assistantInfo;
    }

    public ErrorCodeHelper getErrorCodeHelper() {
        return errorCodeHelper;
    }

    public void setErrorCodeHelper(ErrorCodeHelper errorCodeHelper) {
        this.errorCodeHelper = errorCodeHelper;
    }
}
