package ru.trendtech.common.mobileexchange.model.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by petr on 03.09.14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetTripDetailsTextResponse {

    private boolean sent = false;

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }
}
