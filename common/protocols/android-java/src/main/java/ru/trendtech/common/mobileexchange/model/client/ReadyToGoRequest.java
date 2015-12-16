package ru.trendtech.common.mobileexchange.model.client;

import ru.trendtech.common.mobileexchange.model.common.ClientRequest;

/**
 * File created by max on 23/04/2014 1:36.
 */


public class ReadyToGoRequest extends ClientRequest {
    private int time;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
