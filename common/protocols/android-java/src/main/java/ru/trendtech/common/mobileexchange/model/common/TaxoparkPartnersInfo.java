package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 13.10.2014.
 */
public class TaxoparkPartnersInfo {
    private Long id;
    private String nameTaxopark;
    private String officeAddress;
    private String officePhone;
    private String responsibilityFio;
    private String responsibilityPhone;
    private int priority = 0;
    private int moneyAmount = 0;
    private boolean increaseToDriver;

    public boolean isIncreaseToDriver() {
        return increaseToDriver;
    }

    public void setIncreaseToDriver(boolean increaseToDriver) {
        this.increaseToDriver = increaseToDriver;
    }

    public int getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(int moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameTaxopark() {
        return nameTaxopark;
    }

    public void setNameTaxopark(String nameTaxopark) {
        this.nameTaxopark = nameTaxopark;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getResponsibilityFio() {
        return responsibilityFio;
    }

    public void setResponsibilityFio(String responsibilityFio) {
        this.responsibilityFio = responsibilityFio;
    }

    public String getResponsibilityPhone() {
        return responsibilityPhone;
    }

    public void setResponsibilityPhone(String responsibilityPhone) {
        this.responsibilityPhone = responsibilityPhone;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
