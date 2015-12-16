package ru.trendtech.domain;

import javax.persistence.*;
/**
 * Created by petr on 01.09.14.
 */

@Entity
@Table(name = "mission_addresses")
public class MissionAddresses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "address", column = @Column(name = "address")),
            @AttributeOverride(name = "latitude", column = @Column(name = "latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "longitude")),
    })
    private Location location;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }







//    private class DistanceComparator implements Comparator<MissionAddresses> {
//        @Override
//        public int compare(MissionAddresses o1, MissionAddresses o2) {
//            double dist1 = distanceTo(o1);
//            double dist2 = distanceTo(o2);
//            if (dist1 < dist2) {
//                return -1;
//            } else if (dist1 > dist2){
//                return +1;
//            } else {
//                return 0;
//            }
//        }
//    }


//
//    @Override
//    public int hashCode() {
//        return location.hashCode();
//    }
//
//    private double distanceTo(MissionAddresses locationTo) {
//        double dlon, dlat, a, dist;
//        dlon = location.getLongitude() - locationTo.getLocation().getLongitude();
//        dlat = location.getLatitude() - locationTo.getLocation().getLatitude();
//        a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(location.getLatitude()) * Math.cos(locationTo.getLocation().getLatitude()) * Math.pow(Math.sin(dlon / 2), 2);
//        dist = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//        return 6378140 * dist;
//    }



}
