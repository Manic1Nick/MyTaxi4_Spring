package ua.artcode.taxi.model;

import ua.artcode.taxi.utils.geolocation.GoogleMapsAPI;
import ua.artcode.taxi.utils.geolocation.GoogleMapsAPIImpl;
import ua.artcode.taxi.utils.geolocation.Location;

public class Distance implements Comparable {

    private Location fromLocation;
    private Location toLocation;
    private GoogleMapsAPI googleMapsAPI = new GoogleMapsAPIImpl();

    private int speedKmH = 60;
    private int timeInMin;

    public Distance() {
    }

    public Distance(Location fromLocation, Location toLocation) {
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
    }

    public Location getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(Location fromLocation) {
        this.fromLocation = fromLocation;
    }

    public Location getToLocation() {
        return toLocation;
    }

    public void setToLocation(Location toLocation) {
        this.toLocation = toLocation;
    }

    public int calculateDistance() {
        return (int) googleMapsAPI.getDistance(fromLocation, toLocation);
    }

    public void setSpeedKmH(int speedKmH) {
        this.speedKmH = speedKmH;
    }

    public int getTimeInMin() {
        return (this.calculateDistance() / 1000) / this.speedKmH;
    }

    @Override
    public int compareTo(Object o) {

        Distance tmp = (Distance)o;
        if(this.googleMapsAPI.getDistance(fromLocation, toLocation) <
                tmp.googleMapsAPI.getDistance(fromLocation, toLocation)) {
            return -1;

        } else if (this.googleMapsAPI.getDistance(fromLocation, toLocation) >
                tmp.googleMapsAPI.getDistance(fromLocation, toLocation)) {
            return 1;
        }
        return 0;
    }
}
