package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 19.09.2014.
 */

public class InsertItemPartnersGroupRequest {
    private ItemPartnersGroupInfo itemPartnersGroupInfo;
    private String security_token;

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public ItemPartnersGroupInfo getItemPartnersGroupInfo() {
        return itemPartnersGroupInfo;
    }

    public void setItemPartnersGroupInfo(ItemPartnersGroupInfo itemPartnersGroupInfo) {
        this.itemPartnersGroupInfo = itemPartnersGroupInfo;
    }
}
