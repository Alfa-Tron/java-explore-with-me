package ru.practicum;


public class ErrorResponse {
    private final String error;
    private final String stackTrace;


    public ErrorResponse(String error, String stackTrace) {
        this.error = error;
        this.stackTrace = stackTrace;
    }
}