package com.example.uberapp_tim9.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class Ride {
    private int mID;
    private LocalDateTime mStartTime;
    private LocalDateTime mEndTime;
    private double mTotalPrice;
    private LocalTime mEstimatedTime;
    private RideStatus mStatus;
    private boolean mIsPanicPressed;
    private boolean mHasBaby;
    private boolean mHasPets;
    private boolean mIsSplitFare;
    private List<Payment> mPayments;
    private List<Path> mPaths;
    private List<Passenger> mPassengers;
    private List<Review> mReviews;
    private VehicleType mVehicleType;
    private Driver mDriver;
    private Rejection mRejection;

    public Ride(int mID, LocalDateTime mStartTime, LocalDateTime mEndTime, List<Passenger> mPassengers) {
        this.mID = mID;
        this.mStartTime = mStartTime;
        this.mEndTime = mEndTime;
        this.mPassengers = mPassengers;
    }

    public Ride(double mTotalPrice, List<Path> mPaths) {
        this.mTotalPrice = mTotalPrice;
        this.mPaths = mPaths;
    }

    public int getmID() {
        return mID;
    }

    public double getmTotalPrice() {
        return mTotalPrice;
    }

    public LocalDateTime getmStartTime() {
        return mStartTime;
    }

    public LocalDateTime getmEndTime() {
        return mEndTime;
    }

    public List<Passenger> getmPassengers() {
        return mPassengers;
    }

    public double getTotalKilometers() {
        double total = 0;
        for (Path path : mPaths) {
            total += path.getmKilometers();
        }
        return total;
    }
}
