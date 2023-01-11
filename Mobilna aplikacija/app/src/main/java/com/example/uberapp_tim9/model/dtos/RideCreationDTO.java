package com.example.uberapp_tim9.model.dtos;

import java.util.Set;

public class RideCreationDTO {
    private Set<RouteDTO> locations;
    private Set<PassengerIdEmailDTO> passengers;
    private String vehicleType;
    private Boolean babyTransport;
    private Boolean petTransport;

    public Set<RouteDTO> getLocations() {
        return locations;
    }

    public void setLocations(Set<RouteDTO> locations) {
        this.locations = locations;
    }

    public Set<PassengerIdEmailDTO> getPassengers() {
        return passengers;
    }

    public void setPassengers(Set<PassengerIdEmailDTO> passengers) {
        this.passengers = passengers;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Boolean getBabyTransport() {
        return babyTransport;
    }

    public void setBabyTransport(Boolean babyTransport) {
        this.babyTransport = babyTransport;
    }

    public Boolean getPetTransport() {
        return petTransport;
    }

    public void setPetTransport(Boolean petTransport) {
        this.petTransport = petTransport;
    }
}
