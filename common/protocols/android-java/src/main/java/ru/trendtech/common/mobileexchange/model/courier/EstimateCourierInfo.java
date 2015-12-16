package ru.trendtech.common.mobileexchange.model.courier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ClientInfo;

/**
 * Created by petr on 15.09.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EstimateCourierInfo {
    private Long id;
    private int general = 0;
    private long orderId;
    private long estimateDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getGeneral() {
        return general;
    }

    public void setGeneral(int general) {
        this.general = general;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getEstimateDate() {
        return estimateDate;
    }

    public void setEstimateDate(long estimateDate) {
        this.estimateDate = estimateDate;
    }
}
