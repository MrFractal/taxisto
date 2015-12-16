package ru.trendtech.common.mobileexchange.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.push.DeviceInfoModel;
import ru.trendtech.common.mobileexchange.model.common.rates.MissionRateInfo;

import java.util.ArrayList;
import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverInfo {
    private long id;
    private String firstName;
    private String lastName;
    private String phone;
    private double totalRating;
    private int ratingPoints;
    private String totalRatingStr;
    private int autoYear;
    private String autoModel;
    private String autoNumber;
    private int autoClass;
    private String autoColor;
    private String photoUrl;

    private String photoPicture;
    private double balance;

    private List<String> photosCarsUrl = new ArrayList<String>();

    private List<DriverCash> driverCashFlow = new ArrayList<DriverCash>();

    private List<String> photosCarsPictures = new ArrayList<String>();

    private String login;

    private String password;

    private DeviceInfoModel deviceInfoModel;

    private long birthDate;

    private String administrativeStratus;

    private MissionRateInfo rate;

    private String versionApp;

    private boolean entrepreneur;

    private boolean wifi;



    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public boolean isEntrepreneur() {
        return entrepreneur;
    }

    public void setEntrepreneur(boolean entrepreneur) {
        this.entrepreneur = entrepreneur;
    }

    public String getVersionApp() {
        return versionApp;
    }

    public void setVersionApp(String versionApp) {
        this.versionApp = versionApp;
    }


    public int getRatingPoints() {
        return ratingPoints;
    }

    public void setRatingPoints(int ratingPoints) {
        this.ratingPoints = ratingPoints;
    }

    public String getTotalRatingStr() {
        DriverRatingScale driverRatingScale = new DriverRatingScale();
        int ratingPoints =  getRatingPoints();
        double raitingDriver = getTotalRating();
        if(raitingDriver>5){
            totalRatingStr = driverRatingScale.getNameRating(raitingDriver);
        }else{
            totalRatingStr = "Нет рейтинга";
        }
        return totalRatingStr;
    }



    public long getId() {
        return id;
    }

    public List<DriverCash> getDriverCashFlow() {return driverCashFlow;}

    public void setDriverCashFlow(List<DriverCash> driverCashFlow) {this.driverCashFlow = driverCashFlow;}

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(double totalRating) {
        this.totalRating = totalRating;
    }

    public String getAutoModel() {
        return autoModel;
    }

    public void setAutoModel(String autoModel) {
        this.autoModel = autoModel;
    }

    public String getAutoNumber() {
        return autoNumber;
    }

    public void setAutoNumber(String autoNumber) {
        this.autoNumber = autoNumber;
    }

    public int getAutoClass() {
        return autoClass;
    }

    public void setAutoClass(int autoClass) {
        this.autoClass = autoClass;
    }

    public String getAutoColor() {
        return autoColor;
    }

    public void setAutoColor(String autoColor) {
        this.autoColor = autoColor;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public List<String> getPhotosCarsUrl() {
        return photosCarsUrl;
    }

    public void setPhotosCarsUrl(List<String> photosCarsUrl) {
        this.photosCarsUrl = photosCarsUrl;
    }


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhotoPicture() {
        return photoPicture;
    }

    public void setPhotoPicture(String photoPicture) {
        this.photoPicture = photoPicture;
    }

    public List<String> getPhotosCarsPictures() {
        return photosCarsPictures;
    }

    public void setPhotosCarsPictures(List<String> photosCarsPictures) {
        this.photosCarsPictures = photosCarsPictures;
    }


    public DeviceInfoModel getDeviceInfoModel() {
        return deviceInfoModel;
    }

    public void setDeviceInfoModel(DeviceInfoModel deviceInfoModel) {
        this.deviceInfoModel = deviceInfoModel;
    }

    public int getAutoYear() {
        return autoYear;
    }

    public void setAutoYear(int autoYear) {
        this.autoYear = autoYear;
    }

    public long getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(long birthDate) {
        this.birthDate = birthDate;
    }

    public String getAdministrativeStratus() {
        return administrativeStratus;
    }

    public void setAdministrativeStratus(String administrativeStratus) {
        this.administrativeStratus = administrativeStratus;
    }

    public MissionRateInfo getRate() {
        return rate;
    }

    public void setRate(MissionRateInfo rate) {
        this.rate = rate;
    }
}