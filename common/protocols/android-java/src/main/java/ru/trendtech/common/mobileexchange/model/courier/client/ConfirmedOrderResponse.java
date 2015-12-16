package ru.trendtech.common.mobileexchange.model.courier.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.trendtech.common.mobileexchange.model.common.ErrorCodeHelper;
import ru.trendtech.common.mobileexchange.model.courier.CustomWindowInfo;

/**
 * Created by petr on 07.09.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfirmedOrderResponse extends ErrorCodeHelper {
    private CustomWindowInfo customWindowInfo;
    private String acsUrl;
    private String paReq;
    private String pd;
    private String md;
    private String termUrl;

    public String getPd() {
        return pd;
    }

    public void setPd(String pd) {
        this.pd = pd;
    }

    public String getTermUrl() {
        return termUrl;
    }

    public void setTermUrl(String termUrl) {
        this.termUrl = termUrl;
    }

    public String getAcsUrl() {
        return acsUrl;
    }

    public void setAcsUrl(String acsUrl) {
        this.acsUrl = acsUrl;
    }

    public String getPaReq() {
        return paReq;
    }

    public void setPaReq(String paReq) {
        this.paReq = paReq;
    }

    public String getMd() {
        return md;
    }

    public void setMd(String md) {
        this.md = md;
    }

//    public String getHtmlText() {
//        return htmlText;
//    }
//
//    public void setHtmlText(String htmlText) {
//        this.htmlText = htmlText;
//    }

    public CustomWindowInfo getCustomWindowInfo() {
        return customWindowInfo;
    }

    public void setCustomWindowInfo(CustomWindowInfo customWindowInfo) {
        this.customWindowInfo = customWindowInfo;
    }
}
