package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 12.05.2015.
 */
public class ReasonInfo {
    private long reasonId;
    private String reason;
    private int fine;
    private int clientBonus;
    private boolean toDriver;

    public boolean isToDriver() {
        return toDriver;
    }

    public void setToDriver(boolean toDriver) {
        this.toDriver = toDriver;
    }

    public int getClientBonus() {
        return clientBonus;
    }

    public void setClientBonus(int clientBonus) {
        this.clientBonus = clientBonus;
    }

    public int getFine() {
        return fine;
    }

    public void setFine(int fine) {
        this.fine = fine;
    }

    public long getReasonId() {
        return reasonId;
    }

    public void setReasonId(long reasonId) {
        this.reasonId = reasonId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
