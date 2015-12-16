package ru.trendtech.common.mobileexchange.model.web;

/**
 * File created by max on 20/04/2014 22:53.
 *
 */

/**
 * Request for mission info. Request id can be driiver, client or mission id.
 *
 */
public class CurrentMissionInfoRequest {
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
