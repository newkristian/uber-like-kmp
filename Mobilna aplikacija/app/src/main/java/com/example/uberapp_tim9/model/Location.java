package com.example.uberapp_tim9.model;

public class Location {
    private double mLatitude;
    private double mLongtitude;

    public Location(double mLatitude, double mLongtitude) {
        this.mLatitude = mLatitude;
        this.mLongtitude = mLongtitude;
    }

    public double getmLatitude() {
        return mLatitude;
    }

    public double getmLongtitude() {
        return mLongtitude;
    }
}


