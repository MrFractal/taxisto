package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ClientInfoARM;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

/**
 * Created by petr on 09.04.2015.
 */
public class ClientInfoArmResponse extends ErrorCodeHelper {
    private ClientInfoARM clientInfoARM;

    public ClientInfoARM getClientInfoARM() {
        return clientInfoARM;
    }

    public void setClientInfoARM(ClientInfoARM clientInfoARM) {
        this.clientInfoARM = clientInfoARM;
    }
}
