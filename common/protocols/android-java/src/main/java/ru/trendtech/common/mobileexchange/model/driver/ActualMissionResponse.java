package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by petr on 24.09.2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActualMissionResponse {
    private boolean actual = false;

    public boolean isActual() {
        return actual;
    }

    public void setActual(boolean actual) {
        this.actual = actual;
    }
}
