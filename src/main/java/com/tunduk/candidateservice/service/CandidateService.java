package com.tunduk.candidateservice.service;

import com.tunduk.candidateservice.dto.CandidatePage;
import com.tunduk.candidateservice.dto.CandidateResponse;
import com.tunduk.candidateservice.model.enums.CandidateStatus;
import com.tunduk.candidateservice.model.enums.Verdict;

public interface CandidateService {
    CandidatePage getCandidatesList(Verdict verdict, CandidateStatus status, String position, String search, int page, int size, String sort);

    CandidateResponse getById(String id);
}
