package ru.trendtech.common.mobileexchange.model.client;

/**
 * Created by max on 06.02.14.
 *
 *
 * card's infor representation
 * some fields will be empty for non-secure operations
 *
 */
public class CardInfo {
    private long id;
    private String number;
    private int expirationMonth;
    private int expirationYear;
    private String cvv;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getExpirationMonth() {
        return expirationMonth;
    }

    public void setExpirationMonth(int expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    public int getExpirationYear() {
        return expirationYear;
    }

    public void setExpirationYear(int expirationYear) {
        this.expirationYear = expirationYear;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}
