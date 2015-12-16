package ru.trendtech.domain.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by petr on 12.01.2015.
 */

@Document(collection = "events")
public class Events {
    @Id
    private String id;
    private Long date_time;
    private int object_type; // (0 - system, 1 - driver, 2 - client, 3 - web_user)
    private String object_id;  // (driver, client, web_user)
    private int event_id; // (0 - read, 1 - insert, 2 - delete, 3 - change)
    private long mission_id;
    private long clientId;
    private long driverId;
    private String event_field_1; // name nethod
    private String event_field_2; //
    private String event_field_3; // error message

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getObject_type() {
        return object_type;
    }

    public void setObject_type(int object_type) {
        this.object_type = object_type;
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public Long getDate_time() {
        return date_time;
    }

    public void setDate_time(Long date_time) {
        this.date_time = date_time;
    }

    public String getObject_id() {
        return object_id;
    }

    public void setObject_id(String object_id) {
        this.object_id = object_id;
    }

    public long getMission_id() {
        return mission_id;
    }

    public void setMission_id(long mission_id) {
        this.mission_id = mission_id;
    }

    public String getEvent_field_1() {
        return event_field_1;
    }

    public void setEvent_field_1(String event_field_1) {
        this.event_field_1 = event_field_1;
    }

    public String getEvent_field_2() {
        return event_field_2;
    }

    public void setEvent_field_2(String event_field_2) {
        this.event_field_2 = event_field_2;
    }

    public String getEvent_field_3() {
        return event_field_3;
    }

    public void setEvent_field_3(String event_field_3) {
        this.event_field_3 = event_field_3;
    }
}

