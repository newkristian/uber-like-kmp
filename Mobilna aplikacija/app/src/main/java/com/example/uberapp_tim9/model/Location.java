package com.example.uberapp_tim9.model;

public class Location {
    private String mAddress;
    private double mLatitude;
    private double mLongtitude;

    public Location(double mLatitude, double mLongtitude, String mAddress) {
        this.mLatitude = mLatitude;
        this.mLongtitude = mLongtitude;
        this.mAddress = mAddress;
    }

    public Location(String mAddress) {
        this.mAddress = mAddress;
    }

    public double getmLatitude() {
        return mLatitude;
    }

    public double getmLongtitude() {
        return mLongtitude;
    }

    public String getmAddress() {
        return mAddress;
    }
}


