package com.tunduk.candidateservice.dto;

import com.tunduk.candidateservice.model.enums.CandidateStatus;
import com.tunduk.candidateservice.model.enums.Verdict;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class CandidateResponse {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String position;
    private String posLabel;
    private String city;
    private String telegram;
    private String totalExp;
    private String stack;
    private String education;
    private Verdict verdict;
    private String summary;
    private CandidateStatus status;
    private List<Criteria> criteria;
    private List<Experience> experience;
    private List<String> questions;
    private Instant createdAt;
    private Instant updatedAt;
}
