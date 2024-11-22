package com.example.uberapp_tim9.model.dtos;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WorkingHoursStartDTO {
    private String start;

    public WorkingHoursStartDTO() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        this.start = LocalDateTime.now().format(dtf);
    }
}
