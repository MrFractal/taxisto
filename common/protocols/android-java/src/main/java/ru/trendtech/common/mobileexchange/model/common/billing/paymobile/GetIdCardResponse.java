package ru.trendtech.common.mobileexchange.model.common.billing.paymobile;

/**
 * Created by petr on 15.09.2014.
 */

public class GetIdCardResponse {
    private long idCard;
    private String idCardStr;
    private String mdOrder;

    public String getMdOrder() {
        return mdOrder;
    }

    public void setMdOrder(String mdOrder) {
        this.mdOrder = mdOrder;
    }

    public long getIdCard() {
        return idCard;
    }

    public void setIdCard(long idCard) {
        this.idCard = idCard;
    }

    public String getIdCardStr() {
        return idCardStr;
    }

    public void setIdCardStr(String idCardStr) {
        this.idCardStr = idCardStr;
    }
}
