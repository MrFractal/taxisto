package ru.trendtech.common.mobileexchange.model.client;

import ru.trendtech.common.mobileexchange.model.common.ClientCardInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 16.09.2014.
 */

public class ClientCardListResponse {
   List<ClientCardInfo> clientCardInfoList = new ArrayList();

   public List<ClientCardInfo> getClientCardInfoList() {
        return clientCardInfoList;
    }

   public void setClientCardInfoList(List<ClientCardInfo> clientCardInfoList) {
       this.clientCardInfoList = clientCardInfoList;
   }
}
