package ru.trendtech.common.mobileexchange.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by petr on 17.09.2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorCodeHelper{
   private int errorCode = 0;
   private String errorMessage="Successfully!";

   public int getErrorCode() {
        return errorCode;
    }

   public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

   public String getErrorMessage() {
        return errorMessage;
    }

   public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
