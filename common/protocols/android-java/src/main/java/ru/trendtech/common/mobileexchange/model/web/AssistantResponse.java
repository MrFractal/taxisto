package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 14.11.2014.
 */
public class AssistantResponse {
    private List<AssistantInfo> assistantInfoList = new ArrayList<AssistantInfo>();
    private ErrorCodeHelper errorCodeHelper = new ErrorCodeHelper();

    public ErrorCodeHelper getErrorCodeHelper() {
        return errorCodeHelper;
    }

    public void setErrorCodeHelper(ErrorCodeHelper errorCodeHelper) {
        this.errorCodeHelper = errorCodeHelper;
    }

    public List<AssistantInfo> getAssistantInfoList() {
        return assistantInfoList;
    }

    public void setAssistantInfoList(List<AssistantInfo> assistantInfoList) {
        this.assistantInfoList = assistantInfoList;
    }
}
