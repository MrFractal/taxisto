package ru.trendtech.domain.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by petr on 31.03.2015.
 */
@Document(collection = "driver_activity")
public class DriverActivity {
    @Id
    private String id;
    //@Indexed
    private long dateTime; // time of insert
    //@Indexed
    private long driverId;
    //@Indexed
    private int timeAmount;
    //@Indexed
    private int typeActivity; // 1 - work, 2 - busy

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public int getTimeAmount() {
        return timeAmount;
    }

    public void setTimeAmount(int timeAmount) {
        this.timeAmount = timeAmount;
    }

    public int getTypeActivity() {
        return typeActivity;
    }

    public void setTypeActivity(int typeActivity) {
        this.typeActivity = typeActivity;
    }
}
