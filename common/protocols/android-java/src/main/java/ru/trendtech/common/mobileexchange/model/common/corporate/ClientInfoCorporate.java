package ru.trendtech.common.mobileexchange.model.common.corporate;

import ru.trendtech.common.mobileexchange.model.client.corporate.LimitInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 16.03.2015.
 */
public class ClientInfoCorporate {
    private long clientId;
    private long mainClientId;
    private String firstName;
    private String lastName;
    private String firstNameMain;
    private String lastNameMain;
    private String email;
    private String phone;
    private boolean isBlock;
    private List<LimitInfo> limitInfos = new ArrayList<LimitInfo>();

    public String getFirstNameMain() {
        return firstNameMain;
    }

    public void setFirstNameMain(String firstNameMain) {
        this.firstNameMain = firstNameMain;
    }

    public String getLastNameMain() {
        return lastNameMain;
    }

    public void setLastNameMain(String lastNameMain) {
        this.lastNameMain = lastNameMain;
    }

    public List<LimitInfo> getLimitInfos() {
        return limitInfos;
    }

    public void setLimitInfos(List<LimitInfo> limitInfos) {
        this.limitInfos = limitInfos;
    }

    public long getMainClientId() {
        return mainClientId;
    }

    public void setMainClientId(long mainClientId) {
        this.mainClientId = mainClientId;
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

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
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
}
