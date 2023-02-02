package com.example.uberapp_tim9.model.dtos;

public class VehicleReviewDTO extends Review {

    public VehicleReviewDTO(Integer id, PassengerIdEmailDTO reviewer, String comment, Integer rating) {
        super(id,rating,comment,reviewer);
    }


}
