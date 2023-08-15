package ru.practicum.exeptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponse {
    private final String message;
    private String status;
    private String reason;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp = LocalDateTime.now();

    private String stackTrace;

    public ErrorResponse(String message, String status, String reason, String stackTrace) {
        this.message = message;
        this.status = status;
        this.reason = reason;
        this.stackTrace = stackTrace;
    }
}