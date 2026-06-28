package com.tunduk.candidateservice.model.enums;

import com.tunduk.candidateservice.exception.InvalidStatusTransitionException;

import java.util.Set;
public enum CandidateStatus {
    NEW, IN_REVIEW, INVITED, APPROVED, REJECTED;

    private Set<CandidateStatus> allowedTransitions;

    static {
        NEW.allowedTransitions      = Set.of(IN_REVIEW);
        IN_REVIEW.allowedTransitions = Set.of(INVITED, REJECTED);
        INVITED.allowedTransitions   = Set.of(APPROVED, REJECTED);
        APPROVED.allowedTransitions  = Set.of();
        REJECTED.allowedTransitions  = Set.of();
    }

    public void validateTransition(CandidateStatus next) {
        if (!allowedTransitions.contains(next)) {
            throw new InvalidStatusTransitionException(
                    "Переход из " + this + " в " + next + " запрещён"
            );
        }
    }
}
