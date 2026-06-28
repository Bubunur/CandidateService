package com.tunduk.candidateservice.service;

import com.tunduk.candidateservice.dto.CandidatePage;
import com.tunduk.candidateservice.dto.CandidateResponse;
import com.tunduk.candidateservice.dto.CandidateWriteRequest;
import com.tunduk.candidateservice.dto.event.CvParsedEvent;
import com.tunduk.candidateservice.model.Candidate;
import com.tunduk.candidateservice.model.enums.CandidateStatus;
import com.tunduk.candidateservice.model.enums.Verdict;
import jakarta.validation.Valid;

public interface CandidateService {
    CandidatePage getCandidatesList(Verdict verdict, CandidateStatus status, String position, String search, int page, int size, String sort);
    CandidateResponse getById(String id);
    CandidateResponse create(@Valid CandidateWriteRequest request);
    CandidateResponse update(String id, @Valid CandidateWriteRequest request);
    void delete(String id);
    Candidate findByIdOrThrow(String id);
    void createFromKafka(CvParsedEvent event);
}
