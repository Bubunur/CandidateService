package com.tunduk.candidateservice.dto;

import com.tunduk.candidateservice.model.enums.CandidateStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StatusChangeRequest {

    @NotNull
    private CandidateStatus status;

    private String comment;
}