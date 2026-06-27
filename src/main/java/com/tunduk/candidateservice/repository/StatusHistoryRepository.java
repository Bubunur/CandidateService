package com.tunduk.candidateservice.repository;

import com.tunduk.candidateservice.model.StatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface StatusHistoryRepository extends JpaRepository<StatusHistory, UUID> {
}
