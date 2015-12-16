package ru.trendtech.common.mobileexchange.model.client;

/**
 * Created by petr on 08.12.2014.
 */
public class StartWatchMissionRequest {
   private String watch_security_token;

   public String getWatch_security_token() {
       return watch_security_token;
   }

   public void setWatch_security_token(String watch_security_token) {
       this.watch_security_token = watch_security_token;
   }
}
