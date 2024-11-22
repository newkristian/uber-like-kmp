package com.example.uberapp_tim9.model.dtos;

public class DriverReviewDTO extends Review {

    public DriverReviewDTO(Integer id, Integer rating, String comment, PassengerIdEmailDTO reviewer) {
        super(id,rating,comment,reviewer);
    }



}
