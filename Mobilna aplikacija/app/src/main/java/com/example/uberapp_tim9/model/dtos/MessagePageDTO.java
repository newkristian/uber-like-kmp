package com.example.uberapp_tim9.model.dtos;

import java.util.Set;

public class MessagePageDTO {
    public Long totalCount;
    public Set<MessageDTO> results;

    public MessagePageDTO(Long totalCount, Set<MessageDTO> results) {
        this.totalCount = totalCount;
        this.results = results;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public Set<MessageDTO> getResults() {
        return results;
    }

    public void setResults(Set<MessageDTO> results) {
        this.results = results;
    }
}
