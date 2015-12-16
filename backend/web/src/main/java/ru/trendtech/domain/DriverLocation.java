package ru.trendtech.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import ru.trendtech.common.mobileexchange.model.common.ItemLocation;
import ru.trendtech.domain.courier.Order;

import javax.persistence.*;
import java.util.Comparator;

@Entity
@Table(name = "driver_location")
public class DriverLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "when_seen")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime when;

//    @Column(unique = true)
//    @ManyToOne(optional = false)
//    private Driver driver;

    @OneToOne
    @JoinColumn(name = "driver_id", unique = true)
    private Driver driver;

    @ManyToOne
    private Order order;

    @ManyToOne
    private Mission mission;

    //@ManyToOne
    //@JoinColumn(name = "mission_id_od")
    //private Mission missionOD;

    @Column(name = "time_of_next_offer")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeOfNextOffer;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "address", column = @Column(name = "address")),
            @AttributeOverride(name = "latitude", column = @Column(name = "latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "longitude")),
    })
    private Location location;

    @Column(name = "distance")
    private Integer distance;

    @Column(name = "angle")
    private Integer angle=0;

    @Column(name = "coast")
    private String coast;

    @OneToOne
    @JoinColumn(name = "region_id")
    private Region region;

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public String getCoast() {
        return coast;
    }

    public void setCoast(String coast) {
        this.coast = coast;
    }

    public DriverLocation() {
    }

    public DriverLocation(DateTime when, Location location) {
        this.when = when;
        this.location = location;
    }

    public DriverLocation(DateTime when, Driver driver, Mission mission, Location location, Order order, int distance) {
        this.when = when;
        this.driver = driver;
        this.mission = mission;
        this.location = location;
        this.order = order;
        this.distance = distance;
    }

    public DateTime getTimeOfNextOffer() {
        return timeOfNextOffer;
    }

    public void setTimeOfNextOffer(DateTime timeOfNextOffer) {
        this.timeOfNextOffer = timeOfNextOffer;
    }

//    public Mission getMissionOD() {
//        return missionOD;
//    }
//
//    public void setMissionOD(Mission missionOD) {
//        this.missionOD = missionOD;
//    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getWhen() {
        return when;
    }

    public void setWhen(DateTime when) {
        this.when = when;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public Integer getAngle() {
        return angle;
    }

    public void setAngle(Integer angle) {
        this.angle = angle;
    }

    public ItemLocation toItemLocation() {
        ItemLocation result = new ItemLocation();
        result.setLatitude(this.location.getLatitude());
        result.setLongitude(this.location.getLongitude());
        return result;
    }


    private class DistanceComparator implements Comparator<DriverLocation> {
        @Override
        public int compare(DriverLocation o1, DriverLocation o2) {
            double dist1 = distanceTo(o1);
            double dist2 = distanceTo(o2);
            if (dist1 < dist2) {
                return -1;
            } else if (dist1 > dist2){
                return +1;
            } else {
                return 0;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DriverLocation that = (DriverLocation) o;

        if (!location.equals(that.location)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return location.hashCode();
    }

    private double distanceTo(DriverLocation locationTo) {
        double dlon, dlat, a, dist;
        dlon = location.getLongitude() - locationTo.getLocation().getLongitude();
        dlat = location.getLatitude() - locationTo.getLocation().getLatitude();
        a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(location.getLatitude()) * Math.cos(locationTo.getLocation().getLatitude()) * Math.pow(Math.sin(dlon / 2), 2);
        dist = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 6378140 * dist;
    }
}
