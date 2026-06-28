package com.tunduk.candidateservice.dto;

import com.tunduk.candidateservice.model.enums.CandidateStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class StatusHistoryEntry {
    private UUID id;
    private String candidateId;
    private CandidateStatus fromStatus;
    private CandidateStatus toStatus;
    private String comment;
    private Instant changedAt;
}
