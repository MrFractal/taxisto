package ru.trendtech.common.mobileexchange.model.client;

/**
 * Created by petr on 07.08.14.
 */


public class RegistrationPhoneChangeResponse {
    private boolean result;
    String reason;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
