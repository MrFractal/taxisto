package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by max on 06.02.14.
 */
public class PingRequest {
    private long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    // diagnostic info should be send later
}
