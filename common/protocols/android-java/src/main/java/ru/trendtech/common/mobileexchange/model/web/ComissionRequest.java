package ru.trendtech.common.mobileexchange.model.web;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 17.12.2014.
 */
public class ComissionRequest {
    private String security_token;
    private int comissionType; // id taxopark or id driver
    private long objectId;  // if not define then return all comission by comissionType

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public int getComissionType() {
        return comissionType;
    }

    public void setComissionType(int comissionType) {
        this.comissionType = comissionType;
    }

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }
}
