package com.example.uberapp_tim9.model.dtos;

import java.util.List;

public class FavoritePathDTO {
    private int id;
    private String favoriteName;
    private List<RouteDTO> locations;
    private String vehicleType;
    private boolean babyTransport;
    private boolean petTransport;

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
