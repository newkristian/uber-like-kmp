package com.example.uberapp_tim9.model.dtos;


import com.example.uberapp_tim9.model.enumerations.RideStatus;

import java.time.LocalDateTime;

public class RideCreatedDTO extends RideCreationDTO {

    private Integer id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double totalCost;
    private DriverIdEmailDTO driver;
    private RejectionDTO rejection;
    private Integer estimatedTimeInMinutes;
    private RideStatus status;

    public String getRideSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ruta: ");
        for(RouteDTO route : this.getLocations()){
            sb.append(route.getDeparture().getAddress());
            sb.append(" - ");
            sb.append(route.getDestination().getAddress());
            break;
        }
        sb.append("\n");
        sb.append("Vreme puta (min): ");
        sb.append(this.estimatedTimeInMinutes);
        sb.append("\n");
        sb.append("Broj putnika: ");
        sb.append(this.getPassengers().size());
        sb.append("\n");
        sb.append("Ukupna cena (din): ");
        sb.append(Math.round(this.totalCost * 100) / 100);
        return sb.toString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public DriverIdEmailDTO getDriver() {
        return driver;
    }

    public void setDriver(DriverIdEmailDTO driver) {
        this.driver = driver;
    }

    public RejectionDTO getRejection() {
        return rejection;
    }

    public void setRejection(RejectionDTO rejection) {
        this.rejection = rejection;
    }

    public Integer getEstimatedTimeInMinutes() {
        return estimatedTimeInMinutes;
    }

    public void setEstimatedTimeInMinutes(Integer estimatedTimeInMinutes) {
        this.estimatedTimeInMinutes = estimatedTimeInMinutes;
    }

    public RideStatus getStatus() {
        return status;
    }

    public void setStatus(RideStatus status) {
        this.status = status;
    }
}
