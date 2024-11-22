package com.example.uberapp_tim9.model.dtos;

import java.time.LocalDateTime;

public class WorkingHoursDTO {
    private Integer id;
    private String start;
    private String end;

    public WorkingHoursDTO() {
    }

    public WorkingHoursDTO(Integer id, String start, String end) {
        this.id = id;
        this.start = start;
        this.end = end;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
