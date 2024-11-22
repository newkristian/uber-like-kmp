package com.example.uberapp_tim9.model.dtos;


public class RejectionReasonDTO {

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    private String reason;

    public RejectionReasonDTO(String reason) {
        this.reason = reason;
    }
}
