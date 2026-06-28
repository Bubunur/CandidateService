package com.tunduk.candidateservice.service;

import com.tunduk.candidateservice.dto.CandidateResponse;
import com.tunduk.candidateservice.dto.StatusChangeRequest;
import com.tunduk.candidateservice.dto.StatusHistoryEntry;
import jakarta.validation.Valid;

import java.util.List;

public interface StatusHistoryService {
    CandidateResponse changeStatus(String id, @Valid StatusChangeRequest request);
    List<StatusHistoryEntry> getHistory(String id);
}
