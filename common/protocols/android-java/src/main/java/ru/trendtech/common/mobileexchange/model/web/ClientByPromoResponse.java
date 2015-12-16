package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ClientInfoShort;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 10.12.2014.
 */
public class ClientByPromoResponse {
   private List<ClientInfoShort> clientInfoShortList = new ArrayList<ClientInfoShort>();

   public List<ClientInfoShort> getClientInfoShortList() {
       return clientInfoShortList;
   }

   public void setClientInfoShortList(List<ClientInfoShort> clientInfoShortList) {
       this.clientInfoShortList = clientInfoShortList;
   }
}
