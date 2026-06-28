package com.tunduk.candidateservice.controller;

import com.tunduk.candidateservice.dto.*;
import com.tunduk.candidateservice.model.enums.CandidateStatus;
import com.tunduk.candidateservice.model.enums.Verdict;
import com.tunduk.candidateservice.service.CandidateService;
import com.tunduk.candidateservice.service.StatusHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/candidates")
@RequiredArgsConstructor
@Tag(name = "Candidates", description = "Управление кандидатами CV-Scan")
public class CandidateController {
    private final CandidateService candidateService;
    private final StatusHistoryService statusService;

    @GetMapping
    @Operation(summary = "Список кандидатов с фильтрацией и пагинацией")
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

    @Operation(summary = "Получить кандидата по ID")
    @GetMapping("/{id}")
    public CandidateResponse get(@PathVariable String id) {
        return candidateService.getById(id);
    }

    @PostMapping
    @Operation(summary = "Создание кандидата")
    @ResponseStatus(HttpStatus.CREATED)
    public CandidateResponse create(@Valid @RequestBody CandidateWriteRequest request) {
        return candidateService.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить кандидата (статус не меняется)")
    public CandidateResponse update(
            @PathVariable String id,
            @Valid @RequestBody CandidateWriteRequest request) {

        return candidateService.update(id, request);
    }

    @Operation(summary = "Удалить кандидата")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        candidateService.delete(id);
    }

    @Operation(summary = "Сменить статус кандидата")
    @PatchMapping("/{id}/status")
    public CandidateResponse changeStatus(
            @PathVariable String id,
            @Valid @RequestBody StatusChangeRequest request) {

        return statusService.changeStatus(id, request);
    }

    @Operation(summary = "История изменений статуса (убывание по дате)")
    @GetMapping("/{id}/status-history")
    public List<StatusHistoryEntry> statusHistory(@PathVariable String id) {
        return statusService.getHistory(id);
    }
}
