package com.example.uberapp_tim9.model;

import com.example.uberapp_tim9.model.dtos.RouteDTO;

import java.time.LocalTime;

public class Path {
    private int mID;
    private Location mStartPoint;
    private Location mEndPoint;
    private double mKilometers;
    private LocalTime mEstimatedTime;
    private double mPrice;

    public Path(int mID,
                Location mStartPoint,
                Location mEndPoint,
                double mKilometers,
                LocalTime mEstimatedTime,
                double mPrice) {
        this.mID = mID;
        this.mStartPoint = mStartPoint;
        this.mEndPoint = mEndPoint;
        this.mKilometers = mKilometers;
        this.mEstimatedTime = mEstimatedTime;
        this.mPrice = mPrice;
    }

    public Path(Location mStartPoint, Location mEndPoint) {
        this.mStartPoint = mStartPoint;
        this.mEndPoint = mEndPoint;
    }

    public Path(RouteDTO route) {
        this.mStartPoint = new Location(route.getDeparture());
        this.mEndPoint = new Location(route.getDestination());
    }

    public int getmID() {
        return mID;
    }

    public Path(double mKilometers) {
        this.mKilometers = mKilometers;
    }

    public Location getmStartPoint() {
        return mStartPoint;
    }

    public Location getmEndPoint() {
        return mEndPoint;
    }

    public double getmKilometers() {
        return mKilometers;
    }

    public LocalTime getmEstimatedTime() {
        return mEstimatedTime;
    }

    public double getmPrice() {
        return mPrice;
    }
}
