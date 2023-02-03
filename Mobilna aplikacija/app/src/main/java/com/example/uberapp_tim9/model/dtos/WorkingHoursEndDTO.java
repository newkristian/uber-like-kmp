package com.example.uberapp_tim9.model.dtos;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WorkingHoursEndDTO {
    private String end;

    public WorkingHoursEndDTO() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        this.end = LocalDateTime.now().format(dtf);
    }
}
