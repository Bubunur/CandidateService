package com.tunduk.candidateservice.messaging;

import com.tunduk.candidateservice.dto.event.CvParsedEvent;
import com.tunduk.candidateservice.model.Candidate;
import com.tunduk.candidateservice.model.enums.CandidateStatus;
import com.tunduk.candidateservice.repository.CandidateRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CvParsedConsumer {

    private final CandidateRepository candidateRepository;

    @Transactional
    @KafkaListener(
            topics = "${app.kafka.topics.cv-parsed}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(@Payload CvParsedEvent event) {
        log.info("Received CvParsedEvent: candidateId={}, parsedAt={}", event.getCandidateId(), event.getParsedAt());

        if (candidateRepository.existsByIdAndCreatedAt(event.getCandidateId(), event.getParsedAt())) {
            log.warn("Duplicate event, skipping: candidateId={}, parsedAt={}", event.getCandidateId(), event.getParsedAt());
            return;
        }

        Candidate candidate = toCandidate(event);
        candidateRepository.save(candidate);

        log.info("Candidate saved: id={}", candidate.getId());
    }

    private Candidate toCandidate(CvParsedEvent e) {
        Candidate c = new Candidate();
        c.setId(e.getCandidateId());
        c.setStatus(CandidateStatus.NEW);
        c.setCreatedAt(e.getParsedAt());
        c.setName(e.getName());
        c.setEmail(e.getEmail());
        c.setPhone(e.getPhone());
        c.setTelegram(e.getTelegram());

        c.setPosition(e.getPosition());
        c.setPosLabel(e.getPosLabel());
        c.setCity(e.getCity());

        c.setExperience(e.getExperience());
        c.setTotalExp(e.getTotalExp());
        c.setStack(e.getStack());
        c.setEducation(e.getEducation());

        c.setCriteria(e.getCriteria());
        c.setQuestions(e.getQuestions());
        c.setVerdict(e.getVerdict());
        c.setSummary(e.getSummary());
        return c;
    }
}