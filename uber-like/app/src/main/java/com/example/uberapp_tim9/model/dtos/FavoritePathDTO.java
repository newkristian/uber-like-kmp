package com.example.uberapp_tim9.model.dtos;

import com.example.uberapp_tim9.model.Ride;
import com.example.uberapp_tim9.shared.LoggedUserInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FavoritePathDTO {
    private int id;
    private String favoriteName;
    private List<RouteDTO> locations;
    private String vehicleType;
    private boolean babyTransport;
    private boolean petTransport;
    private Set<PassengerIdEmailDTO> passengers;

    public Integer getRideId() {
        return rideId;
    }

    public void setRideId(Integer rideId) {
        this.rideId = rideId;
    }

    private Integer rideId;

    public FavoritePathDTO() {

    }

    public FavoritePathDTO(int id, String favoriteName, List<RouteDTO> locations, String vehicleType, boolean babyTransport, boolean petTransport) {
        this.id = id;
        this.favoriteName = favoriteName;
        this.locations = locations;
        this.vehicleType = vehicleType;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
    }

    public FavoritePathDTO(Ride ride) {
        this.id = -1;
        this.favoriteName = ride.getmPaths().get(0).getmStartPoint().getmAddress() + " - " + ride.getmPaths().get(0).getmEndPoint().getmAddress();
        this.locations = new ArrayList<>();
        for (int i = 0; i < ride.getmPaths().size(); i++) {
            this.locations.add(new RouteDTO(ride.getmPaths().get(i)));
        }
        this.vehicleType = ride.getmVehicleType();
        this.babyTransport = ride.ismHasBaby();
        this.petTransport = ride.ismHasPets();
        this.passengers = new HashSet<>();
        this.passengers.add(new PassengerIdEmailDTO(LoggedUserInfo.id, "mail@mail.com"));
        this.rideId = ride.getmID();
    }

    public int getId() {
        return id;
    }

    public String getFavoriteName() {
        return favoriteName;
    }

    public List<RouteDTO> getLocations() {
        return locations;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public boolean isBabyTransport() {
        return babyTransport;
    }

    public boolean isPetTransport() {
        return petTransport;
    }
}
