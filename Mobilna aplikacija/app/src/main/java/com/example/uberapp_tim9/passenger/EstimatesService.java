package com.example.uberapp_tim9.passenger;

import com.example.uberapp_tim9.model.Location;
import com.example.uberapp_tim9.model.dtos.LocationDTO;

public class EstimatesService {

    private final static double AVERAGE_RADIUS_OF_EARTH = 6371;

    public static double calculateDistance(LocationDTO departure, LocationDTO destination) {

        double latDistance = Math.toRadians(departure.getLatitude() - destination.getLatitude());
        double lngDistance = Math.toRadians(departure.getLongitude() - destination.getLongitude());

        double a = (Math.sin(latDistance / 2) * Math.sin(latDistance / 2)) +
                (Math.cos(Math.toRadians(departure.getLatitude()))) *
                        (Math.cos(Math.toRadians(destination.getLatitude()))) *
                        (Math.sin(lngDistance / 2)) *
                        (Math.sin(lngDistance / 2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return AVERAGE_RADIUS_OF_EARTH * c;
    }
}
