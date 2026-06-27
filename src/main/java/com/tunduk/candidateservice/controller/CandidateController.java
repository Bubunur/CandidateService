package com.tunduk.candidateservice.controller;

import com.tunduk.candidateservice.dto.CandidatePage;
import com.tunduk.candidateservice.dto.CandidateResponse;
import com.tunduk.candidateservice.dto.CandidateWriteRequest;
import com.tunduk.candidateservice.model.enums.CandidateStatus;
import com.tunduk.candidateservice.model.enums.Verdict;
import com.tunduk.candidateservice.service.CandidateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/candidates")
@RequiredArgsConstructor
public class CandidateController {
    private final CandidateService candidateService;

    @GetMapping
    public CandidatePage list(
            @RequestParam(required = false) Verdict verdict,
            @RequestParam(required = false) CandidateStatus status,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {

        return candidateService.getCandidatesList(verdict, status, position, search, page, size, sort);
    }

    @GetMapping("/{id}")
    public CandidateResponse get(@PathVariable String id) {
        return candidateService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CandidateResponse create(@Valid @RequestBody CandidateWriteRequest request) {
        return candidateService.create(request);
    }
}
