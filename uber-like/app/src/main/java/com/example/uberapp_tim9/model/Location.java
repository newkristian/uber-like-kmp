package com.example.uberapp_tim9.model;

import com.example.uberapp_tim9.model.dtos.LocationDTO;

public class Location {
    private String mAddress;
    private double mLatitude;
    private double mLongitude;

    public Location(double mLatitude, double mLongtitude, String mAddress) {
        this.mLatitude = mLatitude;
        this.mLongitude = mLongtitude;
        this.mAddress = mAddress;
    }

    public Location(String mAddress) {
        this.mAddress = mAddress;
    }

    public Location(LocationDTO departure) {
        this.mAddress = departure.getAddress();
        this.mLatitude = departure.getLatitude();
        this.mLongitude = departure.getLongitude();
    }

    public double getmLatitude() {
        return mLatitude;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public String getmAddress() {
        return mAddress;
    }
}


