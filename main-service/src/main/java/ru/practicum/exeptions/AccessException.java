package ru.practicum.exeptions;

public class AccessException extends RuntimeException {
    public AccessException(final String message) {
        super(message);
    }

}
