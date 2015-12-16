package ru.trendtech.common.mobileexchange.model.common;

import ru.trendtech.common.mobileexchange.model.common.push.DeviceInfoModel;
import ru.trendtech.common.mobileexchange.model.common.rates.MissionRateInfo;
import ru.trendtech.common.mobileexchange.model.driver.DriverSettingInfo;
import ru.trendtech.common.mobileexchange.model.web.AssistantInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 14.10.2014.
 */
public class DriverInfoARM{
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
    private String photoUrlByVersion;
    private double balance;
    private List<String> photosCarsUrl = new ArrayList<String>();
    private List<DriverCash> driverCashFlow = new ArrayList<DriverCash>();
    private List<String> photosCarsPictures = new ArrayList<String>();
    private List<DriverCarPhotosInfo> driverCarPhotosInfos = new ArrayList<DriverCarPhotosInfo>();
    private String login;
    private String password;
    private DeviceInfoModel deviceInfoModel;
    private long birthDate;
    private String administrativeStratus;
    private MissionRateInfo rate;
    private String versionApp;
    private boolean entrepreneur;
    private boolean wifi;
    private long timeOfRegistration;
    private String email;
    private int rateDriver;
    private TaxoparkPartnersInfo taxoparkPartnersInfo;
    private AssistantInfo assistantInfo;
    private TabletInfo tabletInfo;
    private RouterInfo routerInfo;
    private int growth;
    private int weight;
    private String familyStatus;
    private boolean childrens;
    private String hobby;
    private String dream;
    private List<ComissionInfo> comissionInfos = new ArrayList<ComissionInfo>();
    private int typeSalary;
    private int salaryPriority;
    private boolean typeX;
    private ItemLocation itemLocation;
    private Boolean active;
    private DriverSettingInfo driverSettingInfo;

    public DriverSettingInfo getDriverSettingInfo() {
        return driverSettingInfo;
    }

    public void setDriverSettingInfo(DriverSettingInfo driverSettingInfo) {
        this.driverSettingInfo = driverSettingInfo;
    }

    public RouterInfo getRouterInfo() {
        return routerInfo;
    }

    public void setRouterInfo(RouterInfo routerInfo) {
        this.routerInfo = routerInfo;
    }

    public ItemLocation getItemLocation() {
        return itemLocation;
    }

    public void setItemLocation(ItemLocation itemLocation) {
        this.itemLocation = itemLocation;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public TabletInfo getTabletInfo() {
        return tabletInfo;
    }

    public void setTabletInfo(TabletInfo tabletInfo) {
        this.tabletInfo = tabletInfo;
    }

    public String getPhotoUrlByVersion() {
        return photoUrlByVersion;
    }

    public void setPhotoUrlByVersion(String photoUrlByVersion) {
        this.photoUrlByVersion = photoUrlByVersion;
    }

    public List<DriverCarPhotosInfo> getDriverCarPhotosInfos() {
        return driverCarPhotosInfos;
    }

    public void setDriverCarPhotosInfos(List<DriverCarPhotosInfo> driverCarPhotosInfos) {
        this.driverCarPhotosInfos = driverCarPhotosInfos;
    }

    public boolean isTypeX() {
        return typeX;
    }

    public void setTypeX(boolean typeX) {
        this.typeX = typeX;
    }

    public int getTypeSalary() {
        return typeSalary;
    }

    public void setTypeSalary(int typeSalary) {
        this.typeSalary = typeSalary;
    }

    public int getSalaryPriority() {
        return salaryPriority;
    }

    public void setSalaryPriority(int salaryPriority) {
        this.salaryPriority = salaryPriority;
    }

    public List<ComissionInfo> getComissionInfos() {
        return comissionInfos;
    }

    public void setComissionInfos(List<ComissionInfo> comissionInfos) {
        this.comissionInfos = comissionInfos;
    }

    public long getId() {
        return id;
    }

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

    public int getRatingPoints() {
        return ratingPoints;
    }

    public void setRatingPoints(int ratingPoints) {
        this.ratingPoints = ratingPoints;
    }

    public String getTotalRatingStr() {
        return totalRatingStr;
    }

    public void setTotalRatingStr(String totalRatingStr) {
        this.totalRatingStr = totalRatingStr;
    }

    public int getAutoYear() {
        return autoYear;
    }

    public void setAutoYear(int autoYear) {
        this.autoYear = autoYear;
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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhotoPicture() {
        return photoPicture;
    }

    public void setPhotoPicture(String photoPicture) {
        this.photoPicture = photoPicture;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<String> getPhotosCarsUrl() {
        return photosCarsUrl;
    }

    public void setPhotosCarsUrl(List<String> photosCarsUrl) {
        this.photosCarsUrl = photosCarsUrl;
    }

    public List<DriverCash> getDriverCashFlow() {
        return driverCashFlow;
    }

    public void setDriverCashFlow(List<DriverCash> driverCashFlow) {
        this.driverCashFlow = driverCashFlow;
    }

    public List<String> getPhotosCarsPictures() {
        return photosCarsPictures;
    }

    public void setPhotosCarsPictures(List<String> photosCarsPictures) {
        this.photosCarsPictures = photosCarsPictures;
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

    public DeviceInfoModel getDeviceInfoModel() {
        return deviceInfoModel;
    }

    public void setDeviceInfoModel(DeviceInfoModel deviceInfoModel) {
        this.deviceInfoModel = deviceInfoModel;
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

    public String getVersionApp() {
        return versionApp;
    }

    public void setVersionApp(String versionApp) {
        this.versionApp = versionApp;
    }

    public boolean isEntrepreneur() {
        return entrepreneur;
    }

    public void setEntrepreneur(boolean entrepreneur) {
        this.entrepreneur = entrepreneur;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public int getGrowth() {
        return growth;
    }

    public void setGrowth(int growth) {
        this.growth = growth;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getFamilyStatus() {
        return familyStatus;
    }

    public void setFamilyStatus(String familyStatus) {
        this.familyStatus = familyStatus;
    }

    public boolean isChildrens() {
        return childrens;
    }

    public void setChildrens(boolean childrens) {
        this.childrens = childrens;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getDream() {
        return dream;
    }

    public void setDream(String dream) {
        this.dream = dream;
    }

    public AssistantInfo getAssistantInfo() {
        return assistantInfo;
    }

    public void setAssistantInfo(AssistantInfo assistantInfo) {
        this.assistantInfo = assistantInfo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getTimeOfRegistration() {
        return timeOfRegistration;
    }

    public void setTimeOfRegistration(long timeOfRegistration) {
        this.timeOfRegistration = timeOfRegistration;
    }


    public TaxoparkPartnersInfo getTaxoparkPartnersInfo() {
        return taxoparkPartnersInfo;
    }

    public void setTaxoparkPartnersInfo(TaxoparkPartnersInfo taxoparkPartnersInfo) {
        this.taxoparkPartnersInfo = taxoparkPartnersInfo;
    }

    public int getRateDriver() {
        return rateDriver;
    }

    public void setRateDriver(int rateDriver) {
        this.rateDriver = rateDriver;
    }

//    public MissionRateInfo getRate() {
//        return rate;
//    }
//
//    public void setRate(MissionRateInfo rate) {
//        this.rate = rate;
//    }
}

/*
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
    private long timeOfRegistration;
    private String email;
    private List<String> photosCarsUrl = new ArrayList<String>();
    private List<DriverCash> driverCashFlow = new ArrayList<DriverCash>();
    private List<String> photosCarsPictures = new ArrayList<String>();
    private String login;
    private String password;
    private DeviceInfoModel deviceInfoModel;
    private long birthDate;
    private String administrativeStratus;
    //private MissionRateInfo rate;
    private int rate;
    private String versionApp;
    private boolean entrepreneur;
    private boolean wifi;
    private TaxoparkPartnersInfo taxoparkPartnersInfo;
    private AssistantInfo assistantInfo;
 */