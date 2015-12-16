package ru.trendtech.common.mobileexchange.model.common.estimate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ClientInfo;
import ru.trendtech.common.mobileexchange.model.common.DriverInfo;
import ru.trendtech.common.mobileexchange.model.common.MissionInfo;

/**
 * Created by petr on 19.01.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EstimateInfoClient {
    private Long id;
    private int general;
    private int cleanlinessInCar;
    private int waitingTime;
    private int driverCourtesy;
    private int applicationConvenience;
    private int wifiQuality;
    private String estimateComment;
    private long estimateDate;
    private String rateStr; // (отлично, ...)
    private MissionInfo missionInfo;
    private boolean visible;

    public String getRateStr() {
          switch(this.getGeneral()){
              case 2:{
                  this.rateStr = "Ужасно";
                  break;
              }
              case 4:{
                  this.rateStr = "Плохо";
                  break;
              }
              case 6:{
                  this.rateStr = "Хорошо";
                  break;
              }
              case 8:{
                  this.rateStr = "Отлично";
                  break;
              }
              case 10:{
                  this.rateStr = "Превосходно";
                  break;
              }
              default:break;
          }
        return this.rateStr;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getGeneral() {
        return general;
    }

    public void setGeneral(int general) {
        this.general = general;
    }

    public int getCleanlinessInCar() {
        return cleanlinessInCar;
    }

    public void setCleanlinessInCar(int cleanlinessInCar) {
        this.cleanlinessInCar = cleanlinessInCar;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getDriverCourtesy() {
        return driverCourtesy;
    }

    public void setDriverCourtesy(int driverCourtesy) {
        this.driverCourtesy = driverCourtesy;
    }

    public int getApplicationConvenience() {
        return applicationConvenience;
    }

    public void setApplicationConvenience(int applicationConvenience) {
        this.applicationConvenience = applicationConvenience;
    }

    public int getWifiQuality() {
        return wifiQuality;
    }

    public void setWifiQuality(int wifiQuality) {
        this.wifiQuality = wifiQuality;
    }

    public String getEstimateComment() {
        return estimateComment;
    }

    public void setEstimateComment(String estimateComment) {
        this.estimateComment = estimateComment;
    }

    public long getEstimateDate() {
        return estimateDate;
    }

    public void setEstimateDate(long estimateDate) {
        this.estimateDate = estimateDate;
    }

    public MissionInfo getMissionInfo() {
        return missionInfo;
    }

    public void setMissionInfo(MissionInfo missionInfo) {
        this.missionInfo = missionInfo;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
