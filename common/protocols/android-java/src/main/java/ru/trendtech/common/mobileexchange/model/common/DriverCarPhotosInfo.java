package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 17.06.2015.
 */
public class DriverCarPhotosInfo {
    private long id;
    private String photoUrl;
//    private VersionsAppInfo androidMinVersion;
//    private VersionsAppInfo appleMinVersion;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

//    public VersionsAppInfo getAndroidMinVersion() {
//        return androidMinVersion;
//    }
//
//    public void setAndroidMinVersion(VersionsAppInfo androidMinVersion) {
//        this.androidMinVersion = androidMinVersion;
//    }
//
//    public VersionsAppInfo getAppleMinVersion() {
//        return appleMinVersion;
//    }
//
//    public void setAppleMinVersion(VersionsAppInfo appleMinVersion) {
//        this.appleMinVersion = appleMinVersion;
//    }
}
