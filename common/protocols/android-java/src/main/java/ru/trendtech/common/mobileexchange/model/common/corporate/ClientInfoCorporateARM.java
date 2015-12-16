package ru.trendtech.common.mobileexchange.model.common.corporate;

import ru.trendtech.common.mobileexchange.model.client.corporate.LimitInfo;
import ru.trendtech.common.mobileexchange.model.common.PrivateTariffInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 26.03.2015.
 */
public class ClientInfoCorporateARM {
    private long clientId;
    private long mainClientId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private boolean isBlock;
    private String reason;
    private LimitInfo monthLimit;
    private LimitInfo weekLimit;
    private String password;
    private boolean parent=false;
    private List<PrivateTariffInfo> allowTariff = new ArrayList<PrivateTariffInfo>();

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    //private int balanceAmount;
    //private int creditLimitAmount;

//    public int getBalanceAmount() {
//        return balanceAmount;
//    }
//
//    public void setBalanceAmount(int balanceAmount) {
//        this.balanceAmount = balanceAmount;
//    }
//
//    public int getCreditLimitAmount() {
//        return creditLimitAmount;
//    }
//
//    public void setCreditLimitAmount(int creditLimitAmount) {
//        this.creditLimitAmount = creditLimitAmount;
//    }

    public boolean isParent() {
        return parent;
    }

    public void setParent(boolean parent) {
        this.parent = parent;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public long getMainClientId() {
        return mainClientId;
    }

    public void setMainClientId(long mainClientId) {
        this.mainClientId = mainClientId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isBlock() {
        return isBlock;
    }

    public void setBlock(boolean isBlock) {
        this.isBlock = isBlock;
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
}
