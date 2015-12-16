package ru.trendtech.common.mobileexchange.model.notifications;

import ru.trendtech.common.mobileexchange.model.common.ItemLocation;

/**
 * Created by max on 06.02.14.
 */
public class AssignedDriverLocationNotification {
    private ItemLocation loc;

    private long id;

    public ItemLocation getLoc() {
        return loc;
    }

    public void setLoc(ItemLocation loc) {
        this.loc = loc;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
