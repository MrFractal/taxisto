package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;

/**
 * Created by petr on 10.12.2014.
 */
public class SendEmailResponse {
   private ErrorCodeHelper errorCodeHelper = new ErrorCodeHelper();

   public ErrorCodeHelper getErrorCodeHelper() {
       return errorCodeHelper;
   }

   public void setErrorCodeHelper(ErrorCodeHelper errorCodeHelper) {
       this.errorCodeHelper = errorCodeHelper;
   }
}
