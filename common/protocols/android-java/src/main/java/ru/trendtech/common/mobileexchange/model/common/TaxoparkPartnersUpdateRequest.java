package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 14.10.2014.
 */

public class TaxoparkPartnersUpdateRequest {
   private TaxoparkPartnersInfo taxoparkPartnersInfo;
   private String security_token;

   public String getSecurity_token() {
       return security_token;
   }

   public void setSecurity_token(String security_token) {
       this.security_token = security_token;
   }

   public TaxoparkPartnersInfo getTaxoparkPartnersInfo() {
       return taxoparkPartnersInfo;
   }

   public void setTaxoparkPartnersInfo(TaxoparkPartnersInfo taxoparkPartnersInfo) {
       this.taxoparkPartnersInfo = taxoparkPartnersInfo;
   }
}
