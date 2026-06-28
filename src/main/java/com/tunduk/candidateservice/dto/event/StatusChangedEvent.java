package com.tunduk.candidateservice.dto.event;

import com.tunduk.candidateservice.model.enums.CandidateStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class StatusChangedEvent {
    private String eventId;
    private String candidateId;
    private CandidateStatus fromStatus;
    private CandidateStatus toStatus;
    private Instant changedAt;
}
