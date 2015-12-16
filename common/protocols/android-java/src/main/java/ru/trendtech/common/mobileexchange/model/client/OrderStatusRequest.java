package ru.trendtech.common.mobileexchange.model.client;

/**
 * Created by petr on 17.10.2014.
 */

public class OrderStatusRequest {
   private String mdOrder;
   private String security_token;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

   public String getMdOrder() {
        return mdOrder;
   }

   public void setMdOrder(String mdOrder) {
        this.mdOrder = mdOrder;
   }
}
