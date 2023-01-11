package com.example.uberapp_tim9.model.dtos;


public class RouteDTO {

    private LocationDTO departure;

    private LocationDTO destination;

    public LocationDTO getDeparture() {
        return departure;
    }

    public void setDeparture(LocationDTO departure) {
        this.departure = departure;
    }

    public LocationDTO getDestination() {
        return destination;
    }

    public void setDestination(LocationDTO destination) {
        this.destination = destination;
    }
}
