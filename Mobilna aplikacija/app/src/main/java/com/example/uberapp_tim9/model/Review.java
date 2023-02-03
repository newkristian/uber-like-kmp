package com.example.uberapp_tim9.model;

public class Review {
    private int mID;
    private int mRating;
    private String mComment;
    private Passenger passenger;
    private Ride ride;
    private boolean driverReview;

    public Review(int mID, int mRating, String mComment, Passenger passenger, Ride ride) {
        this.mID = mID;
        this.mRating = mRating;
        this.mComment = mComment;
        this.passenger = passenger;
        this.ride = ride;
    }

    public Review(int mRating, String mComment) {
        this.mRating = mRating;
        this.mComment = mComment;
    }

    public Review(int mRating, String mComment, boolean driverReview) {
        this.mRating = mRating;
        this.mComment = mComment;
        this.driverReview = driverReview;
    }

    public Review(com.example.uberapp_tim9.model.dtos.Review review, boolean driverReview) {
        this.mID = review.getId();
        this.mRating = review.getRating();
        this.mComment = review.getComment();
        this.passenger = new Passenger(review.getReviewer());
        this.driverReview = driverReview;
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

    public boolean isDriverReview() {
        return driverReview;
    }
}
