package ru.trendtech.utils;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
import org.slf4j.LoggerFactory;
//import ru.trendtech.domain.RegionCoordinates;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by petr on 08.10.14.
 */
public class GeoUtils {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(GeoUtils.class);
    private static final int EARTH_RADIUS_KM = 6371;

    private final static double ORIGIN_SHIFT = Math.PI * 6378137;

    public static double distance(double lat1, double lng1, double lat2, double lng2) {
        int r = EARTH_RADIUS_KM; // average radius of the earth in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                        * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return r * c;
    }



    // calculate distance in miles between lat/longs [Haversine formula]
    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 3958.75;
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;

        return dist;
    }


    public static double y2lat(double aY) {
        return Math.toDegrees(2* Math.atan(Math.exp(Math.toRadians(aY))) - Math.PI/2);
    }

    public static double lat2y(double aLat) {
        return Math.toDegrees(Math.log(Math.tan(Math.PI/4+Math.toRadians(aLat)/2)));
    }

    public static double lon2x(double aLon) {
        return aLon * ORIGIN_SHIFT / 180.0;
    }



    public static class LocationUtil{
        private String address;
        private double latitude;
        private double longitude;
        private double distance;
        public LocationUtil(double latitude, double longitude, double distance, String address) {
            this.address = address;
            this.latitude = latitude;
            this.longitude = longitude;
            this.distance = distance;
        }
        public String getAddress() {
            return address;
        }
        public double getLatitude() {
            return latitude;
        }
        public double getLongitude() {
            return longitude;
        }
        public double getDistance() {
            return distance;
        }
    }


    public static boolean isInsidePolygon(double lat, double lon, List<String> coordinatesList){
        final GeometryFactory gf = new GeometryFactory();

        final ArrayList<Coordinate> points = new ArrayList<Coordinate>();
           for(String regionCoordinates: coordinatesList){
               String[] strings = regionCoordinates.split(",");
               points.add(new Coordinate(Double.parseDouble(strings[1]), Double.parseDouble(strings[0])));
           }
        final Polygon polygon = gf.createPolygon(new LinearRing(new CoordinateArraySequence(points.toArray(new Coordinate[points.size()])), gf), null);
        final Coordinate coord = new Coordinate(lat, lon); // 55.048357, 82.880614
        final Point point = gf.createPoint(coord);
           return point.within(polygon);
    }

    /*
Lat=55.751667
Long=37.617778
rLat=Lat*pi/180
rLong=Long*pi/180
a=6378137
b=6356752.3142
f=(a-b)/a
e=sqrt(2*f-f^2)
X=a*rLong
Y=a*log(tan(pi/4+rLat/2)*((1-e*sin(rLat))/(1+e*sin(rLat)))^(e/2))
     */
}
