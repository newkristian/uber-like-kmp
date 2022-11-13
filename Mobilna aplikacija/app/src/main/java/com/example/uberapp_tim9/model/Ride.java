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
}
