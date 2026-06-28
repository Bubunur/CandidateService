package com.tunduk.candidateservice.model;

import com.tunduk.candidateservice.model.enums.CandidateStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "candidate_status_history")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatusHistory {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    UUID id;
    @Column(name = "candidate_id", nullable = false)
    String candidateId;

    @Enumerated(EnumType.STRING)
    @Column(name = "from_status")
    private CandidateStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "to_status", nullable = false)
    private CandidateStatus toStatus;

    private String comment;
    @Column(name = "changed_at", nullable = false, updatable = false)
    private Instant changedAt;

    @PrePersist
    void onPersist() {
        if (changedAt == null) changedAt = Instant.now();
    }
}
