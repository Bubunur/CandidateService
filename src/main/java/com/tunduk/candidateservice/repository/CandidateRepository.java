package com.tunduk.candidateservice.repository;

import com.tunduk.candidateservice.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface CandidateRepository
        extends JpaRepository<Candidate, String>, JpaSpecificationExecutor<Candidate> {

    Optional<Candidate> findByEmail(String email);

    boolean existsByIdAndCreatedAt(String id, Instant createdAt);
}