package ua.artcode.taxi.utils.geolocation;

import ua.artcode.taxi.exception.InputDataWrongException;

public interface GoogleMapsAPI {

    Location findLocation(String unformatted) throws InputDataWrongException;

    Location findLocation(String country, String city, String street, String houseNum) throws InputDataWrongException;

    double getDistance(Location pointA, Location pointB) throws InputDataWrongException;

}