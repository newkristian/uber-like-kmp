package com.example.uberapp_tim9.model.dtos;

public class Review {

    private Integer id;
    private Integer rating;
    private String comment;
    private PassengerIdEmailDTO reviewer;

    public Review(Integer id, Integer rating, String comment, PassengerIdEmailDTO reviewer) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.reviewer = reviewer;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public PassengerIdEmailDTO getReviewer() {
        return reviewer;
    }

    public void setReviewer(PassengerIdEmailDTO reviewer) {
        this.reviewer = reviewer;
    }
}
