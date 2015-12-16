package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 17.11.2014.
 */

public class AssistantStatListResponse {
    private Long totalItems;
    private List<AssistantStatInfo> assistantStatsInfos =new ArrayList<AssistantStatInfo>();
    private ErrorCodeHelper errorCodeHelper =new ErrorCodeHelper();

    public Long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Long totalItems) {
        this.totalItems = totalItems;
    }

    public List<AssistantStatInfo> getAssistantStatsInfos() {
        return assistantStatsInfos;
    }

    public void setAssistantStatsInfos(List<AssistantStatInfo> assistantStatsInfos) {
        this.assistantStatsInfos = assistantStatsInfos;
    }

    public ErrorCodeHelper getErrorCodeHelper() {
        return errorCodeHelper;
    }

    public void setErrorCodeHelper(ErrorCodeHelper errorCodeHelper) {
        this.errorCodeHelper = errorCodeHelper;
    }
}
