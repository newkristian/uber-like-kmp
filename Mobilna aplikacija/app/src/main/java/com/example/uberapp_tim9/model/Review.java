package com.example.uberapp_tim9.model;

public class Review {
    private int mID;
    private int mRating;
    private String mComment;
    private Passenger passenger;
    private Ride ride;

    public Review(int mID, int mRating, String mComment, Passenger passenger, Ride ride) {
        this.mID = mID;
        this.mRating = mRating;
        this.mComment = mComment;
        this.passenger = passenger;
        this.ride = ride;
    }

    public int getmID() {
        return mID;
    }

    public int getmRating() {
        return mRating;
    }

    public String getmComment() {
        return mComment;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public Ride getRide() {
        return ride;
    }
}
