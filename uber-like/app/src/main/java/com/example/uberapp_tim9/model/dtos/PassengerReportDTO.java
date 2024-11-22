package com.example.uberapp_tim9.model.dtos;

public class PassengerReportDTO {
    private Integer numberOfRides;
    private Double totalDistance;
    private Double totalCost;

    public PassengerReportDTO() {
    }

    public PassengerReportDTO(Integer numberOfRides, Double totalDistance, Double totalCost) {
        this.numberOfRides = numberOfRides;
        this.totalDistance = totalDistance;
        this.totalCost = totalCost;
    }

    public Integer getNumberOfRides() {
        return numberOfRides;
    }

    public Double getTotalDistance() {
        return totalDistance;
    }

    public Double getTotalCost() {
        return totalCost;
    }
}
