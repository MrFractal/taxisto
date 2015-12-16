package ru.trendtech.domain;

import org.hibernate.annotations.Table;
import ru.trendtech.domain.billing.Account;

import javax.persistence.*;

/**
 * Created by petr on 17.06.2015.
 */
@Entity
@javax.persistence.Table(name = "driver_car_photos")
public class DriverCarPhotos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @Column(name = "photo_url")
    private String photoUrl;

//    @OneToOne
//    @JoinColumn(name = "ac_min_ver_id", nullable = false)
//    private VersionsApp androidMinVersion;
//
//    @OneToOne
//    @JoinColumn(name = "ic_min_ver_id", nullable = false)
//    private VersionsApp appleMinVersion;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

//    public VersionsApp getAndroidMinVersion() {
//        return androidMinVersion;
//    }
//
//    public void setAndroidMinVersion(VersionsApp androidMinVersion) {
//        this.androidMinVersion = androidMinVersion;
//    }
//
//    public VersionsApp getAppleMinVersion() {
//        return appleMinVersion;
//    }
//
//    public void setAppleMinVersion(VersionsApp appleMinVersion) {
//        this.appleMinVersion = appleMinVersion;
//    }
}


