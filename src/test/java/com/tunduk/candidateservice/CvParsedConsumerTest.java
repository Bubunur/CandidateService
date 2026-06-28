package com.tunduk.candidateservice;


import com.tunduk.candidateservice.dto.event.CvParsedEvent;
import com.tunduk.candidateservice.model.Candidate;
import com.tunduk.candidateservice.model.enums.CandidateStatus;
import com.tunduk.candidateservice.model.enums.Verdict;
import com.tunduk.candidateservice.repository.CandidateRepository;
import com.tunduk.candidateservice.service.impl.CandidateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CandidateService.createFromKafka — idempotency unit tests")
class CvParsedConsumerTest {

    @Mock
    CandidateRepository candidateRepository;

    @InjectMocks
    CandidateServiceImpl candidateService;

    private CvParsedEvent event;

    @BeforeEach
    void setUp() {
        event = new CvParsedEvent();
        event.setEventId("event-uuid-001");
        event.setCandidateId("ivanov");
        event.setParsedAt(Instant.parse("2026-06-04T10:00:00Z"));
        event.setName("Иванов Иван Иванович");
        event.setEmail("ivanov@email.com");
        event.setPhone("+996 999 123456");
        event.setPosition("java-middle");
        event.setPosLabel("Java — ведущий программист");
        event.setCity("Бишкек");
        event.setTelegram("@ivanov_dev");
        event.setTotalExp("~3.5 г.");
        event.setStack("Java, Spring Boot, PostgreSQL, Kafka");
        event.setEducation("КНУ, Информатика, 2020");
        event.setVerdict(Verdict.PARTIAL);
        event.setSummary("Backend-разработчик с опытом Spring Boot 3 года.");
        event.setCriteria(List.of());
        event.setExperience(List.of());
        event.setQuestions(List.of());
    }


    @Test
    @DisplayName("New candidateId → candidate is created with status NEW")
    void shouldCreateNewCandidate() {
        when(candidateRepository.existsByIdAndCreatedAt("ivanov", Instant.parse("2026-06-04T10:00:00Z"))).thenReturn(false);
        when(candidateRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        candidateService.createFromKafka(event);

        var captor = ArgumentCaptor.forClass(Candidate.class);
        verify(candidateRepository).save(captor.capture());

        Candidate saved = captor.getValue();
        assertThat(saved.getId()).isEqualTo("ivanov");
        assertThat(saved.getName()).isEqualTo("Иванов Иван Иванович");
        assertThat(saved.getEmail()).isEqualTo("ivanov@email.com");
        assertThat(saved.getPosition()).isEqualTo("java-middle");
        assertThat(saved.getVerdict()).isEqualTo(Verdict.PARTIAL);
        assertThat(saved.getStatus()).isEqualTo(CandidateStatus.NEW);
        assertThat(saved.getCreatedAt()).isEqualTo(Instant.parse("2026-06-04T10:00:00Z"));
    }

    @Test
    @DisplayName("Duplicate event with same candidateId → skipped, no save")
    void shouldSkipDuplicateEvent() {
        when(candidateRepository.existsByIdAndCreatedAt("ivanov", Instant.parse("2026-06-04T10:00:00Z"))).thenReturn(true);

        candidateService.createFromKafka(event);

        verify(candidateRepository, never()).save(any());
    }

    @Test
    @DisplayName("Same candidateId sent twice — only first call triggers save")
    void sameEventTwiceOnlyOneSave() {
        when(candidateRepository.existsByIdAndCreatedAt("ivanov", Instant.parse("2026-06-04T10:00:00Z")))
                .thenReturn(false)
                .thenReturn(true);
        when(candidateRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        candidateService.createFromKafka(event);
        candidateService.createFromKafka(event);

        verify(candidateRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("All event fields are mapped to entity correctly")
    void fieldMappingIsCorrect() {
        when(candidateRepository.existsByIdAndCreatedAt("ivanov", Instant.parse("2026-06-04T10:00:00Z"))).thenReturn(false);
        when(candidateRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        candidateService.createFromKafka(event);

        var captor = ArgumentCaptor.forClass(Candidate.class);
        verify(candidateRepository).save(captor.capture());

        Candidate saved = captor.getValue();
        assertThat(saved.getPhone()).isEqualTo("+996 999 123456");
        assertThat(saved.getPosLabel()).isEqualTo("Java — ведущий программист");
        assertThat(saved.getCity()).isEqualTo("Бишкек");
        assertThat(saved.getTelegram()).isEqualTo("@ivanov_dev");
        assertThat(saved.getTotalExp()).isEqualTo("~3.5 г.");
        assertThat(saved.getStack()).isEqualTo("Java, Spring Boot, PostgreSQL, Kafka");
        assertThat(saved.getEducation()).isEqualTo("КНУ, Информатика, 2020");
        assertThat(saved.getSummary()).isEqualTo("Backend-разработчик с опытом Spring Boot 3 года.");
        assertThat(saved.getCriteria()).isEmpty();
        assertThat(saved.getExperience()).isEmpty();
        assertThat(saved.getQuestions()).isEmpty();
    }

    @Test
    @DisplayName("Null lists in event are replaced with empty lists")
    void nullListsDefaultToEmpty() {
        event.setCriteria(null);
        event.setExperience(null);
        event.setQuestions(null);

        when(candidateRepository.existsByIdAndCreatedAt("ivanov", Instant.parse("2026-06-04T10:00:00Z"))).thenReturn(false);
        when(candidateRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        candidateService.createFromKafka(event);

        var captor = ArgumentCaptor.forClass(Candidate.class);
        verify(candidateRepository).save(captor.capture());
        Candidate saved = captor.getValue();
        assertThat(saved.getCriteria()).isNotNull().isEmpty();
        assertThat(saved.getExperience()).isNotNull().isEmpty();
        assertThat(saved.getQuestions()).isNotNull().isEmpty();
    }
}