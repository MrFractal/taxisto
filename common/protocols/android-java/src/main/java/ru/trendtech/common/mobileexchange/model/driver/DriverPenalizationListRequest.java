package ru.trendtech.common.mobileexchange.model.driver;

/**
 * Created by petr on 02.09.14.
 */


 /*
Реализовать метод, который бы возвращал штрафы водителя/водителей.
Метод должен находиться в AdministrativeController.
Должны быть отборы:
Водитель (если NULL, то все),
Дата начала периода (если NULL, то от самого первого)
Дата конца периода (если NULL, то до самого последнего)
     */

public class DriverPenalizationListRequest {
     private Long driverId;
     private Long startTime;
     private Long endTime;
     private String security_token;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }


    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
}
