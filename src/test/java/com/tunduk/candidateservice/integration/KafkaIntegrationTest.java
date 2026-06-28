package com.tunduk.candidateservice.integration;


import com.tunduk.candidateservice.dto.event.CvParsedEvent;
import com.tunduk.candidateservice.model.enums.Verdict;
import com.tunduk.candidateservice.repository.CandidateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

public class KafkaIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private KafkaTemplate<String, CvParsedEvent> kafkaTemplate;

    @Autowired
    private CandidateRepository candidateRepository;

    @Value("${app.kafka.topics.cv-parsed}")
    private String topic;

    @Test
    void shouldCreateCandidateFromKafkaEvent() {
        String candidateId = UUID.randomUUID().toString();

        CvParsedEvent event = new CvParsedEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setCandidateId(candidateId);
        event.setParsedAt(Instant.now());
        event.setName("Kafka Кандидат");
        event.setEmail("kafka@example.com");
        event.setPhone("+7 999 000 00 00");
        event.setPosition("java-developer");
        event.setVerdict(Verdict.FIT);

        kafkaTemplate.send(topic, event);
        await()
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    boolean exists = candidateRepository
                            .findByEmail("kafka@example.com")
                            .isPresent();
                    assertThat(exists).isTrue();
                });
    }

    @Test
    void shouldNotDuplicateCandidateOnSameEvent() {
        String email = "duplicate@example.com";

        CvParsedEvent event = new CvParsedEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setCandidateId(UUID.randomUUID().toString());
        event.setParsedAt(Instant.now());
        event.setName("Дубль");
        event.setEmail(email);
        event.setPhone("+7 999 111 11 11");
        event.setPosition("qa-engineer");
        event.setVerdict(Verdict.FIT);

        kafkaTemplate.send(topic, event);
        kafkaTemplate.send(topic, event);

        await()
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    long count = candidateRepository.countByEmail(email);
                    assertThat(count).isEqualTo(1);
                });
    }
}
