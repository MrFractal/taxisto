package ru.trendtech.common.mobileexchange.model.client.corporate;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.common.corporate.ClientInfoCorporate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 16.03.2015.
 */
public class CorporateClientResponse extends ErrorCodeHelper {
    private List<ClientInfoCorporate> clientInfoCorporateList = new ArrayList<ClientInfoCorporate>();

    public List<ClientInfoCorporate> getClientInfoCorporateList() {
        return clientInfoCorporateList;
    }

    public void setClientInfoCorporateList(List<ClientInfoCorporate> clientInfoCorporateList) {
        this.clientInfoCorporateList = clientInfoCorporateList;
    }
}
