package com.example.uberapp_tim9.model.dtos;

import java.util.List;

public class TimeUntilOnDepartureDTO {

    List<Integer> passengersIds;
    String timeFormatted;

    public TimeUntilOnDepartureDTO(List<Integer> passengersIds, String timeFormatted) {
        this.passengersIds = passengersIds;
        this.timeFormatted = timeFormatted;
    }

    public List<Integer> getPassengersIds() {
        return passengersIds;
    }

    public void setPassengersIds(List<Integer> passengersIds) {
        this.passengersIds = passengersIds;
    }

    public String getTimeFormatted() {
        return timeFormatted;
    }

    public void setTimeFormatted(String timeFormatted) {
        this.timeFormatted = timeFormatted;
    }
}
