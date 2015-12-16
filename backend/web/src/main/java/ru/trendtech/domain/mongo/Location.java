package ru.trendtech.domain.mongo;

import com.mongodb.BasicDBObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
* Created by petr on 20.08.14.
*/

@Document(collection = "locations")
public class Location {
    @Id
    private String id;
    private long driverId;
    private long missionId;
    private double latitude;
    private double longitude;
    private Long when_seen;
    private String type;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Long getWhen_seen() {
        return when_seen;
    }

    public void setWhen_seen(Long when_seen) {
        this.when_seen = when_seen;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}




/*
when_seen (unix time),
driverId,
missionId,
type (going_to_client, going_with_client, stop_with_client, trouble, free)




@Document(collection = "users")
public class User {

	@Id
	private String id;

	@Indexed
	private String ic;

	private String name;

	private int age;

	//getter, setter and constructor methods

}


*/

