package com.tunduk.candidateservice.repository;

import com.tunduk.candidateservice.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateRepository
        extends JpaRepository<Candidate, String>, JpaSpecificationExecutor<Candidate> {
}