package com.example.uberapp_tim9.model.dtos;

import com.example.uberapp_tim9.model.Location;

public class LocationDTO {

    private String address;
    private Double latitude;
    private Double longitude;

    public LocationDTO(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocationDTO(Location location) {
        this.address = location.getmAddress();
        this.latitude = location.getmLatitude();
        this.longitude = location.getmLongitude();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}