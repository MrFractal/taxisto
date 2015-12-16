package ru.trendtech.common.mobileexchange.model.common.rates;


/*
1. урл активной и неактивной картинки
2. описание
3. цены различные
4. название
 */

public class ServicePriceInfo {
    private int optionId;
    private String service;
    private String details;
    private double price;
    private String activePicUrl;
    private String notActivePicUrl;
    private String description;
    private String name;



    public ServicePriceInfo() {
    }


    public ServicePriceInfo(int optionId, double price, String activePicUrl, String notActivePicUrl, String description, String name) {
        this.optionId = optionId;
        this.price = price;
        this.activePicUrl = activePicUrl;
        this.notActivePicUrl = notActivePicUrl;
        this.description = description;
        this.name = name;
    }


    public ServicePriceInfo(int optionId, double price) {
        this.optionId = optionId;
        this.price = price;
    }

    public ServicePriceInfo(int optionId, String service, double price) {
        this.optionId = optionId;
        this.service = service;
        this.price = price;
    }

    public ServicePriceInfo(int optionId, String service, String details, double price) {
        this.optionId = optionId;
        this.service = service;
        this.details = details;
        this.price = price;
    }


    public String getActivePicUrl() {
        return activePicUrl;
    }

    public void setActivePicUrl(String activePicUrl) {
        this.activePicUrl = activePicUrl;
    }

    public String getNotActivePicUrl() {
        return notActivePicUrl;
    }

    public void setNotActivePicUrl(String notActivePicUrl) {
        this.notActivePicUrl = notActivePicUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
