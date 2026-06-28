package com.tunduk.candidateservice.service.impl;

import com.tunduk.candidateservice.dto.CandidateResponse;
import com.tunduk.candidateservice.dto.StatusChangeRequest;
import com.tunduk.candidateservice.dto.StatusHistoryEntry;
import com.tunduk.candidateservice.messaging.StatusChangedProducer;
import com.tunduk.candidateservice.model.Candidate;
import com.tunduk.candidateservice.model.StatusHistory;
import com.tunduk.candidateservice.model.enums.CandidateStatus;
import com.tunduk.candidateservice.repository.StatusHistoryRepository;
import com.tunduk.candidateservice.service.CandidateService;
import com.tunduk.candidateservice.service.StatusHistoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatusHistoryImpl implements StatusHistoryService {
    private final CandidateService candidateService;
    private final StatusHistoryRepository historyRepository;
    private final StatusChangedProducer statusChangedProducer;

    @Override
    @Transactional
    public CandidateResponse changeStatus(String id, StatusChangeRequest request) {
        Candidate candidate = candidateService.findByIdOrThrow(id);
        CandidateStatus prevStatus = candidate.getStatus();
        prevStatus.validateTransition(request.getStatus());
        candidate.setStatus(request.getStatus());

        StatusHistory history = new StatusHistory();
        history.setCandidateId(id);
        history.setFromStatus(candidate.getStatus());
        history.setToStatus(request.getStatus());
        history.setComment(request.getComment());
        historyRepository.save(history);

        statusChangedProducer.sendStatusChanged(id, prevStatus, request.getStatus());

        log.info("Candidate {} status changed from {} to {}", id, candidate.getStatus(), request.getStatus());
        return CandidateResponse.builder()
                .id(candidate.getId())
                .name(candidate.getName())
                .email(candidate.getEmail())
                .phone(candidate.getPhone())
                .position(candidate.getPosition())
                .status(request.getStatus())
                .verdict(candidate.getVerdict())
                .createdAt(candidate.getCreatedAt())
                .updatedAt(candidate.getUpdatedAt())
                .build();
    }

    @Override
    public List<StatusHistoryEntry> getHistory(String id) {
        candidateService.findByIdOrThrow(id);
        return historyRepository.findByCandidateIdOrderByChangedAtDesc(id).stream()
                .map(history -> StatusHistoryEntry.builder()
                        .fromStatus(history.getFromStatus())
                        .toStatus(history.getToStatus())
                        .comment(history.getComment())
                        .changedAt(history.getChangedAt())
                        .build())
                .toList();
    }
}
