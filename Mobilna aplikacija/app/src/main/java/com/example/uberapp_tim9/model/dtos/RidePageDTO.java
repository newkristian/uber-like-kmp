package com.example.uberapp_tim9.model.dtos;

import java.util.Set;

public class RidePageDTO {

    public Long totalCount;
    public Set<RideCreatedDTO> results;

    public RidePageDTO(Long totalCount, Set<RideCreatedDTO> results) {
        this.totalCount = totalCount;
        this.results = results;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public Set<RideCreatedDTO> getResults() {
        return results;
    }

    public void setResults(Set<RideCreatedDTO> results) {
        this.results = results;
    }
}
