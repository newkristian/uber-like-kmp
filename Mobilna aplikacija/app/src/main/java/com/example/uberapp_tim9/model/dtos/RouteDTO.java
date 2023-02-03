package com.example.uberapp_tim9.model.dtos;


import com.example.uberapp_tim9.model.Path;

public class RouteDTO {
    private LocationDTO departure;

    private LocationDTO destination;

    public RouteDTO(Path path) {
        this.departure = new LocationDTO(path.getmStartPoint());
        this.destination = new LocationDTO(path.getmEndPoint());
    }

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

    public RouteDTO() {

    }

    public RouteDTO(LocationDTO departure, LocationDTO destination) {
        this.departure = departure;
        this.destination = destination;
    }


}
