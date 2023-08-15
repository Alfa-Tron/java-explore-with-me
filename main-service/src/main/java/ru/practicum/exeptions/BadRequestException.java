package ru.practicum.exeptions;

public class BadRequestException extends IllegalArgumentException {
    public BadRequestException(final String message) {
        super(message);
    }
}