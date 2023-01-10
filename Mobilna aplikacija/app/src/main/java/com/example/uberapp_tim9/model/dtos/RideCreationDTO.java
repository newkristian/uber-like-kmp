package com.example.uberapp_tim9.model.dtos;

import java.util.Set;

public class RideCreationDTO {
    private Set<RouteDTO> locations;
    private Set<PassengerIdEmailDTO> passengers;
    private String vehicleType;
    private Boolean babyTransport;
    private Boolean petTransport;
}
