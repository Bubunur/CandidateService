package com.tunduk.candidateservice.controller;

import com.tunduk.candidateservice.dto.*;
import com.tunduk.candidateservice.model.enums.CandidateStatus;
import com.tunduk.candidateservice.model.enums.Verdict;
import com.tunduk.candidateservice.service.CandidateService;
import com.tunduk.candidateservice.service.StatusHistoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/candidates")
@RequiredArgsConstructor
public class CandidateController {
    private final CandidateService candidateService;
    private final StatusHistoryService statusService;

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

    @PutMapping("/{id}")
    public CandidateResponse update(
            @PathVariable String id,
            @Valid @RequestBody CandidateWriteRequest request) {

        return candidateService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        candidateService.delete(id);
    }

    @PatchMapping("/{id}/status")
    public CandidateResponse changeStatus(
            @PathVariable String id,
            @Valid @RequestBody StatusChangeRequest request) {

        return statusService.changeStatus(id, request);
    }

    @GetMapping("/{id}/status-history")
    public List<StatusHistoryEntry> statusHistory(@PathVariable String id) {
        return statusService.getHistory(id);
    }
}
