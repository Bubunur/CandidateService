package com.tunduk.candidateservice.dto.event;

import com.tunduk.candidateservice.dto.CriteriaItem;
import com.tunduk.candidateservice.dto.ExperienceItem;
import com.tunduk.candidateservice.model.enums.Verdict;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CvParsedEvent {
    private String eventId;
    private String candidateId;
    private Instant parsedAt;
    private String name;
    private String position;
    private String posLabel;
    private String email;
    private String phone;
    private String city;
    private String telegram;
    private String totalExp;
    private String stack;
    private String education;
    private Verdict verdict;
    private String summary;
    private List<CriteriaItem> criteria = new ArrayList<>();
    private List<ExperienceItem> experience = new ArrayList<>();
    private List<String> questions = new ArrayList<>();
}
