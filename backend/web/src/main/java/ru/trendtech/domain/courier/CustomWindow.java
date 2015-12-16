package ru.trendtech.domain.courier;

import javax.persistence.*;

/**
 * Created by petr on 03.09.2015.
 */
@Entity
@Table(name = "c_custom_window")
public class CustomWindow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "title")
    private String title;

    @Column(name = "content_text")
    private String contentText;

    @Column(name = "button_text")
    private String buttonText;

    @Column(name = "device_type")
    private String deviceType; // (APPLE, ANDROID_CLIENT, DRIVER)

    @Column(name = "type_window", nullable = false, unique = true)
    @Enumerated(value = EnumType.STRING)
    private TypeWindow typeWindow;


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

    public TypeWindow getTypeWindow() {
        return typeWindow;
    }

    public void setTypeWindow(TypeWindow typeWindow) {
        this.typeWindow = typeWindow;
    }


}
