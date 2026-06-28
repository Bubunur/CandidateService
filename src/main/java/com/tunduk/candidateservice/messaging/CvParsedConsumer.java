package com.tunduk.candidateservice.messaging;

import com.tunduk.candidateservice.dto.event.CvParsedEvent;
import com.tunduk.candidateservice.model.Candidate;
import com.tunduk.candidateservice.model.enums.CandidateStatus;
import com.tunduk.candidateservice.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CvParsedConsumer {
    private final CandidateRepository candidateRepository;

    @KafkaListener(
            topics = "${kafka.topics.cv-parsed}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void cvParsedConsume(@Payload CvParsedEvent event) {
        log.info("Received CvParsedEvent: {}", event);
        if (candidateRepository.existsByIdAndCreatedAt(event.getCandidateId(), event.getParsedAt())) {
            log.info("Candidate with id {} and createdAt {} already exists. Skipping.", event.getCandidateId(), event.getParsedAt());
            return;
        }
        Candidate candidate = new Candidate();
        candidate.setId(event.getCandidateId());
        candidate.setName(event.getName());
        candidate.setEmail(event.getEmail());
        candidate.setPhone(event.getPhone());
        candidate.setPosition(event.getPosition());
        candidate.setCreatedAt(event.getParsedAt());
        candidate.setStatus(CandidateStatus.NEW);
        candidate.setExperience(event.getExperience());
        candidate.setCity(event.getCity());
        candidate.setCriteria(event.getCriteria());
        candidate.setEducation(event.getEducation());
        candidate.setPosLabel(event.getPosLabel());
        candidate.setQuestions(event.getQuestions());
        candidate.setTelegram(event.getTelegram());
        candidate.setTotalExp(event.getTotalExp());
        candidate.setStack(event.getStack());
        candidate.setVerdict(event.getVerdict());
        candidate.setSummary(event.getSummary());

        candidateRepository.save(candidate);
        log.info("Candidate with id {} created successfully.", event.getCandidateId());
    }
}
