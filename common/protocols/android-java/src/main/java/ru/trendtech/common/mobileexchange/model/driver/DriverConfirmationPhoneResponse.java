package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverConfirmationPhoneResponse {
    private boolean updatePhone = false;

    public boolean isUpdatePhone() {
        return updatePhone;
    }

    public void setUpdatePhone(boolean updatePhone) {
        this.updatePhone = updatePhone;
    }
}
