package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 10.10.2014.
 */
public class EstimateInfoDetails {
    private long missionId;
    private int wifi_quality;
    private int application_convenience;
    private int driver_courtesy;
    private int cleanliness;
    private int general;
    private String estimate_comment;
    private long timeOfStart;
    private TaxoparkPartnersInfo taxoparkPartnersInfo;
    private ClientInfo clientInfo;
    private DriverInfoARM driverInfoARM;

    public TaxoparkPartnersInfo getTaxoparkPartnersInfo() {
        return taxoparkPartnersInfo;
    }

    public void setTaxoparkPartnersInfo(TaxoparkPartnersInfo taxoparkPartnersInfo) {
        this.taxoparkPartnersInfo = taxoparkPartnersInfo;
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    public DriverInfoARM getDriverInfoARM() {
        return driverInfoARM;
    }

    public void setDriverInfoARM(DriverInfoARM driverInfoARM) {
        this.driverInfoARM = driverInfoARM;
    }

    public long getTimeOfStart() {
        return timeOfStart;
    }

    public void setTimeOfStart(long timeOfStart) {
        this.timeOfStart = timeOfStart;
    }

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }

    public int getWifi_quality() {
        return wifi_quality;
    }

    public void setWifi_quality(int wifi_quality) {
        this.wifi_quality = wifi_quality;
    }

    public int getApplication_convenience() {
        return application_convenience;
    }

    public void setApplication_convenience(int application_convenience) {
        this.application_convenience = application_convenience;
    }

    public int getDriver_courtesy() {
        return driver_courtesy;
    }

    public void setDriver_courtesy(int driver_courtesy) {
        this.driver_courtesy = driver_courtesy;
    }


    public int getCleanliness() {
        return cleanliness;
    }

    public void setCleanliness(int cleanliness) {
        this.cleanliness = cleanliness;
    }

    public int getGeneral() {
        return general;
    }

    public void setGeneral(int general) {
        this.general = general;
    }

    public String getEstimate_comment() {
        return estimate_comment;
    }

    public void setEstimate_comment(String estimate_comment) {
        this.estimate_comment = estimate_comment;
    }
}
