package com.tunduk.candidateservice.exception;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private int status;
    private String error;
    private String message;
    private List<FieldError> details;
    private Instant timestamp;
    private String path;

    @Data
    @Builder
    public static class FieldError {
        private String field;
        private String message;
    }
}
