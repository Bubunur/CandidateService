package com.tunduk.candidateservice.dto;


import com.tunduk.candidateservice.model.enums.Verdict;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CandidateWriteRequest {
    @NotBlank
    @NotNull
    @Size(min = 2, max = 100)
    private String name;

    @NotBlank
    @Email
    @NotNull
    private String email;

    @NotNull
    @Pattern(regexp = "^\\+\\d[\\d ]{6,20}$", message = "Некорректный формат телефона")
    private String phone;

    @NotBlank
    @NotNull
    @Pattern(regexp = "^[a-z0-9-]+$", message = "position должен содержать только строчные латинские буквы, цифры и дефис")
    private String position;
    private String posLabel;
    private String city;
    private String telegram;
    private String totalExp;
    private String stack;
    private String education;

    @NotNull
    private Verdict verdict;

    private String summary;

    @Valid
    private List<CriteriaItem> criteria = new ArrayList<>();

    @Valid
    private List<ExperienceItem> experience = new ArrayList<>();

    private List<String> questions = new ArrayList<>();
}

