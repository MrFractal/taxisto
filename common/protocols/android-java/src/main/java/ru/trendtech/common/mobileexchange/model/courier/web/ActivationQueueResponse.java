package ru.trendtech.common.mobileexchange.model.courier.web;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.courier.ActivationQueueInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 04.09.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActivationQueueResponse extends ErrorCodeHelper {
    private List<ActivationQueueInfo> activationQueueInfos = new ArrayList<ActivationQueueInfo>();
    private long lastPageNumber;
    private long totalItems;

    public long getLastPageNumber() {
        return lastPageNumber;
    }

    public void setLastPageNumber(long lastPageNumber) {
        this.lastPageNumber = lastPageNumber;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    public List<ActivationQueueInfo> getActivationQueueInfos() {
        return activationQueueInfos;
    }

    public void setActivationQueueInfos(List<ActivationQueueInfo> activationQueueInfos) {
        this.activationQueueInfos = activationQueueInfos;
    }
}
