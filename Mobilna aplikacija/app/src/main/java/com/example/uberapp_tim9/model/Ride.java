package com.example.uberapp_tim9.model;

import com.example.uberapp_tim9.model.dtos.LocationDTO;
import com.example.uberapp_tim9.model.dtos.PassengerIdEmailDTO;
import com.example.uberapp_tim9.model.dtos.RideCreatedDTO;
import com.example.uberapp_tim9.model.dtos.RouteDTO;
import com.example.uberapp_tim9.passenger.EstimatesService;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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

    public Ride(){
    }

    public Ride(int mID, LocalDateTime mStartTime, LocalDateTime mEndTime, List<Passenger> mPassengers) {
        this.mID = mID;
        this.mStartTime = mStartTime;
        this.mEndTime = mEndTime;
        this.mPassengers = mPassengers;
    }

    public Ride(int mID,
                LocalDateTime mStartTime,
                LocalDateTime mEndTime,
                double mTotalPrice,
                LocalTime mEstimatedTime,
                List<Path> mPaths) {
        this.mID = mID;
        this.mStartTime = mStartTime;
        this.mEndTime = mEndTime;
        this.mTotalPrice = mTotalPrice;
        this.mEstimatedTime = mEstimatedTime;
        this.mPaths = mPaths;
    }

    public Ride(int mID, LocalDateTime mStartTime, LocalDateTime mEndTime, double mTotalPrice, List<Path> mPaths, List<Passenger> mPassengers) {
        this.mID = mID;
        this.mStartTime = mStartTime;
        this.mEndTime = mEndTime;
        this.mTotalPrice = mTotalPrice;
        this.mPaths = mPaths;
        this.mPassengers = mPassengers;
    }

    public Ride(double mTotalPrice, List<Path> mPaths) {
        this.mTotalPrice = mTotalPrice;
        this.mPaths = mPaths;
    }

    public Ride(int mID) {
        this.mID = mID;
    }

    public Ride(RideCreatedDTO dto) {
        this.mID = dto.getId();
        this.mStartTime = dto.getStartTime();
        this.mEndTime = dto.getEndTime();
        this.mTotalPrice = dto.getTotalCost();
        this.mPaths = new ArrayList<>();
        for (RouteDTO route : dto.getLocations()) {
            this.mPaths.add(new Path(route));
        }
        this.mPassengers = new ArrayList<>();
        for (PassengerIdEmailDTO passenger : dto.getPassengers()) {
            this.mPassengers.add(new Passenger(passenger));
        }
        this.mDriver = new Driver(dto.getDriver());
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
            if (path.getmStartPoint() != null && path.getmEndPoint() != null) {
                total += EstimatesService.calculateDistance(
                        new LocationDTO(path.getmStartPoint()),
                        new LocationDTO(path.getmEndPoint()));
            }
        }
        return total;
    }

    public LocalTime getmEstimatedTime() {
        return mEstimatedTime;
    }

    public int getTotalPassengers()
    {
        return mPassengers.size();
    }

    public int getmID() {
        return mID;
    }

    public void setmID(int mID) {
        this.mID = mID;
    }

    public void setmStartTime(LocalDateTime mStartTime) {
        this.mStartTime = mStartTime;
    }

    public void setmEndTime(LocalDateTime mEndTime) {
        this.mEndTime = mEndTime;
    }

    public void setmTotalPrice(double mTotalPrice) {
        this.mTotalPrice = mTotalPrice;
    }

    public void setmEstimatedTime(LocalTime mEstimatedTime) {
        this.mEstimatedTime = mEstimatedTime;
    }

    public RideStatus getmStatus() {
        return mStatus;
    }

    public void setmStatus(RideStatus mStatus) {
        this.mStatus = mStatus;
    }

    public boolean ismIsPanicPressed() {
        return mIsPanicPressed;
    }

    public void setmIsPanicPressed(boolean mIsPanicPressed) {
        this.mIsPanicPressed = mIsPanicPressed;
    }

    public boolean ismHasBaby() {
        return mHasBaby;
    }

    public void setmHasBaby(boolean mHasBaby) {
        this.mHasBaby = mHasBaby;
    }

    public boolean ismHasPets() {
        return mHasPets;
    }

    public void setmHasPets(boolean mHasPets) {
        this.mHasPets = mHasPets;
    }

    public boolean ismIsSplitFare() {
        return mIsSplitFare;
    }

    public void setmIsSplitFare(boolean mIsSplitFare) {
        this.mIsSplitFare = mIsSplitFare;
    }

    public List<Payment> getmPayments() {
        return mPayments;
    }

    public void setmPayments(List<Payment> mPayments) {
        this.mPayments = mPayments;
    }

    public List<Path> getmPaths() {
        return mPaths;
    }

    public void setmPaths(List<Path> mPaths) {
        this.mPaths = mPaths;
    }

    public void setmPassengers(List<Passenger> mPassengers) {
        this.mPassengers = mPassengers;
    }

    public List<Review> getmReviews() {
        return mReviews;
    }

    public void setmReviews(List<Review> mReviews) {
        this.mReviews = mReviews;
    }

    public VehicleType getmVehicleType() {
        return mVehicleType;
    }

    public void setmVehicleType(VehicleType mVehicleType) {
        this.mVehicleType = mVehicleType;
    }

    public Driver getmDriver() {
        return mDriver;
    }

    public void setmDriver(Driver mDriver) {
        this.mDriver = mDriver;
    }

    public Rejection getmRejection() {
        return mRejection;
    }

    public void setmRejection(Rejection mRejection) {
        this.mRejection = mRejection;
    }
}
