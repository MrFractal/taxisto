package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by petr on 02.03.2015.
 */

/* not use */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IsOwnDriverRequest {
   private long driverId;

   public long getDriverId() {
       return driverId;
   }

   public void setDriverId(long driverId) {
        this.driverId = driverId;
   }
}
