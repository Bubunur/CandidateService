package com.tunduk.candidateservice.messaging;

import com.tunduk.candidateservice.dto.event.CvParsedEvent;
import com.tunduk.candidateservice.service.CandidateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CvParsedConsumer {

    private final CandidateService candidateService;

    @KafkaListener(
            topics = "${app.kafka.topics.cv-parsed}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(@Payload CvParsedEvent event) {
        log.info("Received CvParsedEvent: candidateId={}, parsedAt={}", event.getCandidateId(), event.getParsedAt());
        candidateService.createFromKafka(event);
    }

}