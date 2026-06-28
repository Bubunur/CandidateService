package com.tunduk.candidateservice;

import com.tunduk.candidateservice.dto.StatusChangeRequest;
import com.tunduk.candidateservice.exception.CandidateNotFoundException;
import com.tunduk.candidateservice.exception.InvalidStatusTransitionException;
import com.tunduk.candidateservice.messaging.StatusChangedProducer;
import com.tunduk.candidateservice.model.Candidate;
import com.tunduk.candidateservice.model.StatusHistory;
import com.tunduk.candidateservice.model.enums.CandidateStatus;
import com.tunduk.candidateservice.model.enums.Verdict;
import com.tunduk.candidateservice.repository.StatusHistoryRepository;
import com.tunduk.candidateservice.service.CandidateService;
import com.tunduk.candidateservice.service.impl.StatusHistoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatusServiceTests {
    @Mock
    StatusHistoryRepository historyRepository;
    @Mock
    StatusChangedProducer producer;
    @Mock
    CandidateService candidateService;

    @InjectMocks
    StatusHistoryImpl statusService;
    private Candidate candidate;

    @BeforeEach
    void setup() {
        candidate = new Candidate();
        candidate.setId("test-candidate");
        candidate.setName("Test User");
        candidate.setEmail("test@email.com");
        candidate.setPosition("java-middle");
        candidate.setVerdict(Verdict.FIT);
        candidate.setStatus(CandidateStatus.NEW);
    }

    @Test
    @DisplayName("changeStatus on unknown id should throw 404")
    void notFoundThrows() {
        when(candidateService.findByIdOrThrow("missing"))
                .thenThrow(new CandidateNotFoundException("missing"));

        var req = new StatusChangeRequest();
        req.setStatus(CandidateStatus.IN_REVIEW);

        assertThatThrownBy(() -> statusService.changeStatus("missing", req))
                .isInstanceOf(CandidateNotFoundException.class);
    }

    @Test
    @DisplayName("getHistory delegates correctly and requires candidate existence")
    void historyDelegates() {
        when(candidateService.findByIdOrThrow("test-candidate")).thenReturn(candidate);
        when(historyRepository.findByCandidateIdOrderByChangedAtDesc("test-candidate"))
                .thenReturn(java.util.List.of());

        var result = statusService.getHistory("test-candidate");

        assertThat(result).isEmpty();
        verify(historyRepository).findByCandidateIdOrderByChangedAtDesc("test-candidate");
    }

    @Nested
    @DisplayName("Allowed transitions")
    class AllowedTransitions {

        @Test
        @DisplayName("NEW → IN_REVIEW should succeed and record history")
        void newToInReview() {
            candidate.setStatus(CandidateStatus.NEW);
            when(candidateService.findByIdOrThrow("test-candidate")).thenReturn(candidate);
            when(historyRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

            var req = new StatusChangeRequest();
            req.setStatus(CandidateStatus.IN_REVIEW);
            req.setComment("Смотрим кандидата");

            statusService.changeStatus("test-candidate", req);

            assertThat(candidate.getStatus()).isEqualTo(CandidateStatus.IN_REVIEW);

            var historyCaptor = ArgumentCaptor.forClass(StatusHistory.class);
            verify(historyRepository).save(historyCaptor.capture());
            StatusHistory saved = historyCaptor.getValue();
            assertThat(saved.getFromStatus()).isEqualTo(CandidateStatus.NEW);
            assertThat(saved.getToStatus()).isEqualTo(CandidateStatus.IN_REVIEW);
            assertThat(saved.getComment()).isEqualTo("Смотрим кандидата");

            verify(producer).sendStatusChanged("test-candidate", CandidateStatus.NEW, CandidateStatus.IN_REVIEW);
        }

        @ParameterizedTest(name = "{0} → {1}")
        @CsvSource({
                "NEW,       IN_REVIEW",
                "IN_REVIEW, INVITED",
                "IN_REVIEW, REJECTED",
                "INVITED,   APPROVED",
                "INVITED,   REJECTED"
        })
        @DisplayName("Valid transition: {0} → {1}")
        void validTransitions(CandidateStatus from, CandidateStatus to) {
            candidate.setStatus(from);
            when(candidateService.findByIdOrThrow("test-candidate")).thenReturn(candidate);
            when(historyRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

            var req = new StatusChangeRequest();
            req.setStatus(to);

            assertThatNoException().isThrownBy(
                    () -> statusService.changeStatus("test-candidate", req));

            assertThat(candidate.getStatus()).isEqualTo(to);
            verify(historyRepository).save(any());
            verify(producer).sendStatusChanged("test-candidate", from, to);
        }
    }

    @Nested
    @DisplayName("Invalid transitions — must throw 422")
    class InvalidTransitions {

        @ParameterizedTest(name = "{0} → {1}")
        @CsvSource({
                "NEW,      INVITED",
                "NEW,      APPROVED",
                "NEW,      REJECTED",
                "INVITED,  IN_REVIEW",
                "APPROVED, IN_REVIEW",
                "APPROVED, REJECTED",
                "REJECTED, IN_REVIEW",
                "REJECTED, APPROVED"
        })
        @DisplayName("Invalid transition: {0} → {1}")
        void invalidTransitions(CandidateStatus from, CandidateStatus to) {
            candidate.setStatus(from);
            when(candidateService.findByIdOrThrow("test-candidate")).thenReturn(candidate);

            var req = new StatusChangeRequest();
            req.setStatus(to);

            assertThatThrownBy(() -> statusService.changeStatus("test-candidate", req))
                    .isInstanceOf(InvalidStatusTransitionException.class)
                    .hasMessageContaining(from.name())
                    .hasMessageContaining(to.name());

            assertThat(candidate.getStatus()).isEqualTo(from);
            verify(historyRepository, never()).save(any());
            verify(producer, never()).sendStatusChanged(any(), any(), any());
        }
    }
}
