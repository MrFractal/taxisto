package ru.trendtech.common.mobileexchange.model.driver;

import ru.trendtech.common.mobileexchange.model.common.DumbResponse;

/**
 * File created by max on 07/05/2014 20:44.
 */


public class MissionCompleteResponsePHP extends DumbResponse {
    private boolean completed;

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
