package ru.trendtech.common.mobileexchange.model.driver;

/**
 * Created by petr on 27.08.14.
 */
public class TestRequest {
    private long clientId;
    private String dumbField;

    public String getDumbField() {
        return dumbField;
    }

    public void setDumbField(String dumbField) {
        this.dumbField = dumbField;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }
}
