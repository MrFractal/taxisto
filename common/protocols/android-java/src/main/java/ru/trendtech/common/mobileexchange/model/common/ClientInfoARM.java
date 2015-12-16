package ru.trendtech.common.mobileexchange.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.client.CardInfo;
import ru.trendtech.common.mobileexchange.model.client.corporate.LimitInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 25.03.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientInfoARM {
    private long id;
    private String firstName;
    private String lastName;
    private String picure; // string with base64encoded picture will be rework to byte[]
    private String picureUrl;
    private String country; // formatted on server side
    private String city; // formatted on server side
    private String phone;
    private int birthdayDay;
    private int birthdayMonth;
    private int birthdayYear;
    private String email;
    private Boolean gender; // true - male, false - female
    private int rating;
    private List<CardInfo> cards = new ArrayList<CardInfo>();
    private String password;
    private double bonuses;
    private double amount;
    private long registrationDate;
    private long blockingDate;
    private long administrativeState;
    private String versionApp;
    private Long mainClientId;
    private boolean isRoot = false;
    private LimitInfo monthLimit;
    private LimitInfo weekLimit;
    private int corporateBalance;
    //private List<Integer> allowTariff = new ArrayList<Integer>();
    private List<PrivateTariffInfo> allowTariff = new ArrayList<PrivateTariffInfo>();
    private String corporateLogin;
    private String corporatePassword;
    private Boolean courierActivated;

    public Boolean getCourierActivated() {
        return courierActivated;
    }

    public void setCourierActivated(Boolean courierActivated) {
        this.courierActivated = courierActivated;
    }

    public String getCorporateLogin() {
        return corporateLogin;
    }

    public void setCorporateLogin(String corporateLogin) {
        this.corporateLogin = corporateLogin;
    }

    public String getCorporatePassword() {
        return corporatePassword;
    }

    public void setCorporatePassword(String corporatePassword) {
        this.corporatePassword = corporatePassword;
    }

    public List<PrivateTariffInfo> getAllowTariff() {
        return allowTariff;
    }

    public void setAllowTariff(List<PrivateTariffInfo> allowTariff) {
        this.allowTariff = allowTariff;
    }

    //    public List<Integer> getAllowTariff() {
//        return allowTariff;
//    }
//
//    public void setAllowTariff(List<Integer> allowTariff) {
//        this.allowTariff = allowTariff;
//    }

    public int getCorporateBalance() {
        return corporateBalance;
    }

    public void setCorporateBalance(int corporateBalance) {
        this.corporateBalance = corporateBalance;
    }

    public LimitInfo getMonthLimit() {
        return monthLimit;
    }

    public void setMonthLimit(LimitInfo monthLimit) {
        this.monthLimit = monthLimit;
    }

    public LimitInfo getWeekLimit() {
        return weekLimit;
    }

    public void setWeekLimit(LimitInfo weekLimit) {
        this.weekLimit = weekLimit;
    }

    public Long getMainClientId() {
        return mainClientId;
    }

    public void setMainClientId(Long mainClientId) {
        this.mainClientId = mainClientId;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public void setRoot(boolean isRoot) {
        this.isRoot = isRoot;
    }

    public String getVersionApp() {
        return versionApp;
    }

    public void setVersionApp(String versionApp) {
        this.versionApp = versionApp;
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

    public String getPicure() {
        return picure;
    }

    public void setPicure(String picure) {
        this.picure = picure;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getBirthdayDay() {
        return birthdayDay;
    }

    public void setBirthdayDay(int birthdayDay) {
        this.birthdayDay = birthdayDay;
    }

    public int getBirthdayMonth() {
        return birthdayMonth;
    }

    public void setBirthdayMonth(int birthdayMonth) {
        this.birthdayMonth = birthdayMonth;
    }

    public int getBirthdayYear() {
        return birthdayYear;
    }

    public void setBirthdayYear(int birthdayYear) {
        this.birthdayYear = birthdayYear;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPicureUrl() {
        return picureUrl;
    }

    public void setPicureUrl(String picureUrl) {
        this.picureUrl = picureUrl;
    }

    public double getBonuses() {
        return bonuses;
    }

    public void setBonuses(double bonuses) {
        this.bonuses = bonuses;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(long registrationDate) {
        this.registrationDate = registrationDate;
    }

    public long getBlockingDate() {
        return blockingDate;
    }

    public void setBlockingDate(long blockingDate) {
        this.blockingDate = blockingDate;
    }

    public long getAdministrativeState() {
        return administrativeState;
    }

    public void setAdministrativeState(long administrativeState) {
        this.administrativeState = administrativeState;
    }
}
