package ru.trendtech.common.mobileexchange.model.web;

/**
 * File created by max on 20/04/2014 23:25.
 */


public class UserIdInfo {
    private long id;
    private UserType type;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    /**
     * File created by max on 20/04/2014 22:45.
     */


    public static enum UserType {
        CLIENT,
        DRIVER,
        MANAGER, // well be used in future?
        ;
    }
}
