package com.example.uberapp_tim9.model.dtos;

import java.util.Set;

public class DriverPageDTO {
    public Long totalCount;
    public Set<DriverDTO> results;

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public Set<DriverDTO> getResults() {
        return results;
    }

    public void setResults(Set<DriverDTO> results) {
        this.results = results;
    }
}