package ru.trendtech.common.mobileexchange.model.courier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by petr on 02.10.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CourierClientCardInfo {
    private Long id;
    private String cardNumber;
    private boolean active = false;
    private boolean delete = false;

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
