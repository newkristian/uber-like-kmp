package com.example.uberapp_tim9.model.dtos;

import java.time.LocalDateTime;

public class RejectionDTO {

    private String reason;
    private LocalDateTime timeOfRejection;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getTimeOfRejection() {
        return timeOfRejection;
    }

    public void setTimeOfRejection(LocalDateTime timeOfRejection) {
        this.timeOfRejection = timeOfRejection;
    }
}
