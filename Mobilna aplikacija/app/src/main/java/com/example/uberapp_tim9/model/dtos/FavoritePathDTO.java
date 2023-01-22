package com.example.uberapp_tim9.model.dtos;

import com.example.uberapp_tim9.model.Location;

import java.util.Map;

public class FavoritePathDTO {
    private int id;
    private String favoriteName;
    private Map<String, Location> locations;
    private String vehicleType;
    private boolean babyTransport;
    private boolean petTransport;

    public FavoritePathDTO() {

    }

    public FavoritePathDTO(int id, String favoriteName, Map<String, Location> locations, String vehicleType, boolean babyTransport, boolean petTransport) {
        this.id = id;
        this.favoriteName = favoriteName;
        this.locations = locations;
        this.vehicleType = vehicleType;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
    }

    public int getId() {
        return id;
    }

    public String getFavoriteName() {
        return favoriteName;
    }

    public Map<String, Location> getLocations() {
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
