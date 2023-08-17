package ru.practicum;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorResponse {
    private String error;
    private String stackTrace;


    public ErrorResponse(String error, String stackTrace) {
        this.error = error;
        this.stackTrace = stackTrace;
    }
}