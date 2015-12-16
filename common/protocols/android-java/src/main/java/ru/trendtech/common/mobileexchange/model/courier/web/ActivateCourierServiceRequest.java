package ru.trendtech.common.mobileexchange.model.courier.web;

import ru.trendtech.common.mobileexchange.model.courier.web.CommonRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 01.09.2015.
 */
public class ActivateCourierServiceRequest extends CommonRequest{
    private List<Long> clientIds = new ArrayList<Long>();

    public List<Long> getClientIds() {
        return clientIds;
    }

    public void setClientIds(List<Long> clientIds) {
        this.clientIds = clientIds;
    }
}
