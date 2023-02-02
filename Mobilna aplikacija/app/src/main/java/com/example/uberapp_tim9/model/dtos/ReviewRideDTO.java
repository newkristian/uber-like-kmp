package com.example.uberapp_tim9.model.dtos;


import java.util.Set;

public class ReviewRideDTO {
    private Set<DriverReviewDTO> driverReview;
    private Set<VehicleReviewDTO> vehicleReview;

    public ReviewRideDTO(Set<DriverReviewDTO> driverReview, Set<VehicleReviewDTO> vehicleReview) {
        this.driverReview = driverReview;
        this.vehicleReview = vehicleReview;
    }

    public Set<DriverReviewDTO> getDriverReview() {
        return driverReview;
    }

    public void setDriverReview(Set<DriverReviewDTO> driverReview) {
        this.driverReview = driverReview;
    }

    public Set<VehicleReviewDTO> getVehicleReview() {
        return vehicleReview;
    }

    public void setVehicleReview(Set<VehicleReviewDTO> vehicleReview) {
        this.vehicleReview = vehicleReview;
    }
}
