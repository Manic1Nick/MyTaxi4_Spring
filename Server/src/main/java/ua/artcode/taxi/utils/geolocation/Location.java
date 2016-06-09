package ua.artcode.taxi.utils.geolocation;

public class Location {

    private String formattedAddress;
    private double lat;
    private double lng;
    private String placeId;

    public Location(String formattedAddress, double lat, double lng) {
        this.formattedAddress = formattedAddress;
        this.lat = lat;
        this.lng = lng;
    }

    public Location(String formattedAddress, double lat, double lng, String placeId) {
        this.formattedAddress = formattedAddress;
        this.lat = lat;
        this.lng = lng;
        this.placeId = placeId;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    @Override
    public String toString() {
        return "Location{" +
                "formattedAddress='" + formattedAddress + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", placeId='" + placeId + '\'' +
                '}';
    }

}