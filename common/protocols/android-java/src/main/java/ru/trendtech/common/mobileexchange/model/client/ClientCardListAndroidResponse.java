package ru.trendtech.common.mobileexchange.model.client;

import ru.trendtech.common.mobileexchange.model.common.ClientCardInfo;
import ru.trendtech.common.mobileexchange.model.common.ClientCardInfoAndroid;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 07.11.2014.
 */
public class ClientCardListAndroidResponse {
    List<ClientCardInfoAndroid> clientCardInfoList = new ArrayList();

    public List<ClientCardInfoAndroid> getClientCardInfoList() {
        return clientCardInfoList;
    }

    public void setClientCardInfoList(List<ClientCardInfoAndroid> clientCardInfoList) {
        this.clientCardInfoList = clientCardInfoList;
    }
}
