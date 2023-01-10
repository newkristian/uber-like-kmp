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
}
