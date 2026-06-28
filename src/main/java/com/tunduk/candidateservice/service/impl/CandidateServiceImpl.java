package com.tunduk.candidateservice.service.impl;

import com.tunduk.candidateservice.dto.CandidatePage;
import com.tunduk.candidateservice.dto.CandidateResponse;
import com.tunduk.candidateservice.dto.CandidateWriteRequest;
import com.tunduk.candidateservice.dto.event.CvParsedEvent;
import com.tunduk.candidateservice.exception.CandidateNotFoundException;
import com.tunduk.candidateservice.exception.EmailDuplicateException;
import com.tunduk.candidateservice.model.Candidate;
import com.tunduk.candidateservice.model.enums.CandidateStatus;
import com.tunduk.candidateservice.model.enums.Verdict;
import com.tunduk.candidateservice.repository.CandidateRepository;
import com.tunduk.candidateservice.service.CandidateService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;

    @Override
    public CandidatePage getCandidatesList(Verdict verdict, CandidateStatus status, String position, String search,
                                           int page, int size, String sort) {
        List<Specification<Candidate>> specs = new ArrayList<>();

        if (verdict != null)
            specs.add((root, query, cb) -> cb.equal(root.get("verdict"), verdict));
        if (status != null)
            specs.add((root, query, cb) -> cb.equal(root.get("status"), status));
        if (position != null && !position.isBlank())
            specs.add((root, query, cb) -> cb.equal(root.get("position"), position));
        if (search != null && !search.isBlank())
            specs.add((root, query, cb) -> cb.like(root.get("name"), "%" + search + "%"));

        Specification<Candidate> combined = specs.stream()
                .reduce(Specification.where(null), Specification::and);

        String[] parts = sort.split(",", 2);
        Sort.Direction direction = parts.length > 1 && parts[1].trim().equalsIgnoreCase("desc")
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sortObj = Sort.by(direction, parts[0].trim());

        Pageable pageable = PageRequest.of(page, size, sortObj);
        Page<Candidate> result = candidateRepository.findAll(combined, pageable);

        return CandidatePage.builder()
                .content(result.getContent().stream()
                        .map(c -> CandidateResponse.builder()
                                .id(c.getId())
                                .name(c.getName())
                                .email(c.getEmail())
                                .phone(c.getPhone())
                                .position(c.getPosition())
                                .posLabel(c.getPosLabel())
                                .city(c.getCity())
                                .telegram(c.getTelegram())
                                .totalExp(c.getTotalExp())
                                .stack(c.getStack())
                                .education(c.getEducation())
                                .verdict(c.getVerdict())
                                .summary(c.getSummary())
                                .status(c.getStatus())
                                .criteria(c.getCriteria())
                                .experience(c.getExperience())
                                .questions(c.getQuestions())
                                .createdAt(c.getCreatedAt())
                                .updatedAt(c.getUpdatedAt())
                                .build())
                        .toList())
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
    }

    @Override
    public CandidateResponse getById(String id) {
        Candidate candidate = findByIdOrThrow(id);

        return CandidateResponse.builder()
                .id(candidate.getId())
                .name(candidate.getName())
                .email(candidate.getEmail())
                .phone(candidate.getPhone())
                .position(candidate.getPosition())
                .posLabel(candidate.getPosLabel())
                .city(candidate.getCity())
                .telegram(candidate.getTelegram())
                .totalExp(candidate.getTotalExp())
                .stack(candidate.getStack())
                .education(candidate.getEducation())
                .verdict(candidate.getVerdict())
                .summary(candidate.getSummary())
                .status(candidate.getStatus())
                .criteria(candidate.getCriteria())
                .experience(candidate.getExperience())
                .questions(candidate.getQuestions())
                .createdAt(candidate.getCreatedAt())
                .updatedAt(candidate.getUpdatedAt())
                .build();
    }

    @Override
    @Transactional
    public CandidateResponse create(CandidateWriteRequest request) {
        if (candidateRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailDuplicateException("Candidate with email " + request.getEmail() + " already exists");
        }

        String id = generateSlug(request.getEmail());
        Candidate candidate = new Candidate();
        candidate.setId(id);
        candidate.setName(request.getName());
        candidate.setEmail(request.getEmail());
        candidate.setPhone(request.getPhone());
        candidate.setPosition(request.getPosition());
        candidate.setPosLabel(request.getPosLabel());
        candidate.setCity(request.getCity());
        candidate.setTelegram(request.getTelegram());
        candidate.setTotalExp(request.getTotalExp());
        candidate.setStack(request.getStack());
        candidate.setEducation(request.getEducation());
        candidate.setVerdict(request.getVerdict());
        candidate.setSummary(request.getSummary());
        candidate.setCriteria(request.getCriteria());
        candidate.setExperience(request.getExperience());
        candidate.setQuestions(request.getQuestions());
        candidate.setStatus(CandidateStatus.NEW);

        Candidate saved = candidateRepository.save(candidate);

        return CandidateResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .email(saved.getEmail())
                .phone(saved.getPhone())
                .position(saved.getPosition())
                .posLabel(saved.getPosLabel())
                .city(saved.getCity())
                .telegram(saved.getTelegram())
                .totalExp(saved.getTotalExp())
                .stack(saved.getStack())
                .education(saved.getEducation())
                .verdict(saved.getVerdict())
                .summary(saved.getSummary())
                .status(saved.getStatus())
                .criteria(saved.getCriteria())
                .experience(saved.getExperience())
                .questions(saved.getQuestions())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    @Override
    @Transactional
    public CandidateResponse update(String id, CandidateWriteRequest request) {
        Candidate candidate = findByIdOrThrow(id);
        if (!candidate.getEmail().equals(request.getEmail()) &&
                candidateRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailDuplicateException("Candidate with email " + request.getEmail() + " already exists");
        }
        candidate.setName(request.getName());
        candidate.setEmail(request.getEmail());
        candidate.setPhone(request.getPhone());
        candidate.setPosition(request.getPosition());
        candidate.setPosLabel(request.getPosLabel());
        candidate.setCity(request.getCity());
        candidate.setTelegram(request.getTelegram());
        candidate.setTotalExp(request.getTotalExp());
        candidate.setStack(request.getStack());
        candidate.setEducation(request.getEducation());
        candidate.setVerdict(request.getVerdict());
        candidate.setSummary(request.getSummary());
        candidate.setCriteria(request.getCriteria());
        candidate.setExperience(request.getExperience());
        candidate.setQuestions(request.getQuestions());

        Candidate saved = candidateRepository.save(candidate);

        return CandidateResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .email(saved.getEmail())
                .phone(saved.getPhone())
                .position(saved.getPosition())
                .posLabel(saved.getPosLabel())
                .city(saved.getCity())
                .telegram(saved.getTelegram())
                .totalExp(saved.getTotalExp())
                .stack(saved.getStack())
                .education(saved.getEducation())
                .verdict(saved.getVerdict())
                .summary(saved.getSummary())
                .status(saved.getStatus())
                .criteria(saved.getCriteria())
                .experience(saved.getExperience())
                .questions(saved.getQuestions())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    @Override
    @Transactional
    public void delete(String id) {
        Candidate candidate = candidateRepository.findById(id).orElseThrow(
                () -> new CandidateNotFoundException("Candidate with id " + id + " not found")
        );
        candidateRepository.delete(candidate);
    }

    @Override
    public Candidate findByIdOrThrow(String id) {
        return candidateRepository.findById(id).orElseThrow(
                () -> new CandidateNotFoundException("Candidate with id " + id + " not found")
        );
    }

    @Override
    @Transactional
    public void createFromKafka(CvParsedEvent event) {
        if (candidateRepository.existsByIdAndCreatedAt(event.getCandidateId(), event.getParsedAt())) {
            log.info("Duplicate CvParsedEvent received for candidateId={}, parsedAt={}. Skipping.", event.getCandidateId(), event.getParsedAt());
            return;
        }

        Candidate candidate = new Candidate();
        candidate.setId(event.getCandidateId());
        candidate.setName(event.getName());
        candidate.setEmail(event.getEmail());
        candidate.setPhone(event.getPhone());
        candidate.setPosition(event.getPosition());
        candidate.setPosLabel(event.getPosLabel());
        candidate.setCity(event.getCity());
        candidate.setTelegram(event.getTelegram());
        candidate.setTotalExp(event.getTotalExp());
        candidate.setStack(event.getStack());
        candidate.setEducation(event.getEducation());
        candidate.setVerdict(event.getVerdict());
        candidate.setSummary(event.getSummary());
        candidate.setCriteria(event.getCriteria() != null ? event.getCriteria() : new ArrayList<>());
        candidate.setExperience(event.getExperience() != null ? event.getExperience() : new ArrayList<>());
        candidate.setQuestions(event.getQuestions() != null ? event.getQuestions() : new ArrayList<>());
        candidate.setStatus(CandidateStatus.NEW);
        candidate.setCreatedAt(event.getParsedAt());

        candidateRepository.save(candidate);
        log.info("Candidate saved from kafka");
    }

    private String generateSlug(String email) {
        String base = email.split("@")[0]
                .toLowerCase()
                .replaceAll("[^a-z0-9]", "-")
                .replaceAll("-+", "-");
        if (!candidateRepository.existsById(base)) return base;
        int i = 2;
        while (candidateRepository.existsById(base + "-" + i)) i++;
        return base + "-" + i;
    }
}
