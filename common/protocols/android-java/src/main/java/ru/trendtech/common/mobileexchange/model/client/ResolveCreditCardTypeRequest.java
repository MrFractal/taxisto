package ru.trendtech.common.mobileexchange.model.client;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 07.11.2014.
 */

public class ResolveCreditCardTypeRequest {
   private String creditCardNumber;

   public String getCreditCardNumber() {
        return creditCardNumber;
   }

   public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
   }
}
