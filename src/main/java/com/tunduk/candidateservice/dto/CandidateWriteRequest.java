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
    @Size(min = 2, max = 100)
    private String name;

    @NotBlank
    @Email
    private String email;

    @Pattern(regexp = "^\\+\\d[\\d ]{6,20}$", message = "Некорректный формат телефона")
    private String phone;

    @NotBlank
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
    private List<Criteria> criteria = new ArrayList<>();

    @Valid
    private List<Experience> experience = new ArrayList<>();

    private List<String> questions = new ArrayList<>();
}

