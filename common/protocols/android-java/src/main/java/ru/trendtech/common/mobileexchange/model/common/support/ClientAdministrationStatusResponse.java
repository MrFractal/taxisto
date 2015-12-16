package ru.trendtech.common.mobileexchange.model.common.support;

import ru.trendtech.common.mobileexchange.model.common.ClientLocksInfo;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

/**
 * Created by petr on 11.10.2014.
 */
public class ClientAdministrationStatusResponse {
    private ClientLocksInfo clientLocksInfo;
    private ErrorCodeHelper errorCodeHelper =new ErrorCodeHelper();

    public ErrorCodeHelper getErrorCodeHelper() {
        return errorCodeHelper;
    }

    public void setErrorCodeHelper(ErrorCodeHelper errorCodeHelper) {
        this.errorCodeHelper = errorCodeHelper;
    }

    public ClientLocksInfo getClientLocksInfo() {
        return clientLocksInfo;
    }

    public void setClientLocksInfo(ClientLocksInfo clientLocksInfo) {
        this.clientLocksInfo = clientLocksInfo;
    }
}
