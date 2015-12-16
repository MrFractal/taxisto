package ru.trendtech.common.mobileexchange.model.web;

/**
 * File created by max on 09/06/2014 22:22.
 */

public class BlockClientRequest {
    private long clientId;
    private long webUsertId;
    //private long blockingTime; // im MS
    private boolean block = true;
    private String security_token;
    private String comment;


    public long getWebUsertId() {
        return webUsertId;
    }

    public void setWebUsertId(long webUsertId) {
        this.webUsertId = webUsertId;
    }

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

//    public long getBlockingTime() {
//        return blockingTime;
//    }
//
//    public void setBlockingTime(long blockingTime) {
//        this.blockingTime = blockingTime;
//    }

    public boolean isBlock() {
        return block;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }
}
