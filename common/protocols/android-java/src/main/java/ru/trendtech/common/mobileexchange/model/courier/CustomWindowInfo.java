package ru.trendtech.common.mobileexchange.model.courier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by petr on 03.09.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomWindowInfo {
    private Long id;
    private String imgUrl;
    private String title;
    private String contentText;
    private String buttonText;
    private String deviceType; // (APPLE, ANDROID_CLIENT, DRIVER)
    private TypeWindowInfo typeWindow;
    /*
    values:
    REQUIRED_REQUEST, // для активации сервиса требуется запрос
    ACTIVATION_SERVICE_CONGRATULATION, // окошко с информацией об активация сервиса
    OPERATOR_IS_BUSY, // окошко с информацией о номере очереди
    REQUIRED_CARD, // требуется карта
    ALCOHOL_STOP, // после 10 бухать нельзя
    */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public TypeWindowInfo getTypeWindow() {
        return typeWindow;
    }

    public void setTypeWindow(TypeWindowInfo typeWindow) {
        this.typeWindow = typeWindow;
    }
}
