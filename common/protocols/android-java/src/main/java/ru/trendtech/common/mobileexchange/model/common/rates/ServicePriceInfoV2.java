package ru.trendtech.common.mobileexchange.model.common.rates;

/**
 * Created by petr on 11.06.2015.
 */
public class ServicePriceInfoV2 {
    private int optionId;
    private String service;
    private double price;
    private String activePicUrl;
    private String notActivePicUrl;
    private String description;
    private String name;
    private String activePicUrlV2;
    private String descriptionV2;

    public ServicePriceInfoV2(int optionId, String service, double price, String activePicUrl, String notActivePicUrl, String description, String name, String activePicUrlV2, String descriptionV2) {
        this.optionId = optionId;
        this.service = service;
        this.price = price;
        this.activePicUrl = activePicUrl;
        this.notActivePicUrl = notActivePicUrl;
        this.description = description;
        this.name = name;
        this.activePicUrlV2 = activePicUrlV2;
        this.descriptionV2 = descriptionV2;
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


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
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

    public String getActivePicUrlV2() {
        return activePicUrlV2;
    }

    public void setActivePicUrlV2(String activePicUrlV2) {
        this.activePicUrlV2 = activePicUrlV2;
    }

    public String getDescriptionV2() {
        return descriptionV2;
    }

    public void setDescriptionV2(String descriptionV2) {
        this.descriptionV2 = descriptionV2;
    }
}
