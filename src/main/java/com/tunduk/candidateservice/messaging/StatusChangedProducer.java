package com.tunduk.candidateservice.messaging;

import com.tunduk.candidateservice.dto.event.StatusChangedEvent;
import com.tunduk.candidateservice.model.enums.CandidateStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatusChangedProducer {

    private final KafkaTemplate<String, StatusChangedEvent> kafkaTemplate;

    @Value("${app.kafka.topics.status-changed}")
    private String topic;

    public void sendStatusChanged(String candidateId, CandidateStatus fromStatus, CandidateStatus toStatus) {
        StatusChangedEvent event = StatusChangedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .candidateId(candidateId)
                .fromStatus(fromStatus)
                .toStatus(toStatus)
                .changedAt(Instant.now()).build();

        kafkaTemplate.send(topic, candidateId, event).whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to send StatusChangedEvent for candidate {}: {}", candidateId, ex.getMessage());
            } else {
                log.info("StatusChangedEvent sent: candidateId={}, {} -> {}", candidateId, fromStatus, toStatus);
            }
        });
    }
}