package ru.trendtech.common.mobileexchange.model.web;

import ru.trendtech.common.mobileexchange.model.courier.OrderInfo;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;

/**
 * File created by max on 09/06/2014 20:48.
 */


public class WebUserModel {
    private Long id;

    private String firstName;

    private AdministratorRole role;

    private String lastName;

    private String email;

    private String phone;

    private String password;

    private String country;

    private String city;

    private String phoneOfManager;

    private String sipUser;

    private String sipPassword;

    private AssistantInfo assistantInfo;

    private String waPhone;

    private String waPassword;

    private Long taxoparkId;

    private List<OrderInfo> orderInfos = new ArrayList<OrderInfo>();

    public List<OrderInfo> getOrderInfos() {
        return orderInfos;
    }

    public void setOrderInfos(List<OrderInfo> orderInfos) {
        this.orderInfos = orderInfos;
    }

    public AssistantInfo getAssistantInfo() {
        return assistantInfo;
    }

    public void setAssistantInfo(AssistantInfo assistantInfo) {
        this.assistantInfo = assistantInfo;
    }

    public String getWaPhone() {
        return waPhone;
    }

    public void setWaPhone(String waPhone) {
        this.waPhone = waPhone;
    }

    public String getWaPassword() {
        return waPassword;
    }

    public void setWaPassword(String waPassword) {
        this.waPassword = waPassword;
    }

    public String getSipUser() {
        return sipUser;
    }

    public void setSipUser(String sipUser) {
        this.sipUser = sipUser;
    }

    public String getSipPassword() {
        return sipPassword;
    }

    public void setSipPassword(String sipPassword) {
        this.sipPassword = sipPassword;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public AdministratorRole getRole() {
        return role;
    }

    public void setRole(AdministratorRole role) {
        this.role = role;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getPhoneOfManager() {
        return phoneOfManager;
    }

    public void setPhoneOfManager(String phoneOfManager) {
        this.phoneOfManager = phoneOfManager;
    }

    public Long getTaxoparkId() {
        return taxoparkId;
    }

    public void setTaxoparkId(Long taxoparkId) {
        this.taxoparkId = taxoparkId;
    }
}
