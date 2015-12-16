package ru.trendtech.common.mobileexchange.model.web.corporate;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.common.corporate.ClientInfoCorporateARM;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 26.03.2015.
 */
public class CorporateClientARMResponse  extends ErrorCodeHelper{
    private List<ClientInfoCorporateARM> clientInfoCorporateList = new ArrayList<ClientInfoCorporateARM>();

    public List<ClientInfoCorporateARM> getClientInfoCorporateList() {
        return clientInfoCorporateList;
    }

    public void setClientInfoCorporateList(List<ClientInfoCorporateARM> clientInfoCorporateList) {
        this.clientInfoCorporateList = clientInfoCorporateList;
    }
}
