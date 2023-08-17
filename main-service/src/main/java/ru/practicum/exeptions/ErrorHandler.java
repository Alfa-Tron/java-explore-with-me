package ru.practicum.exeptions;


import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;


@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(final IllegalArgumentException e) {
        log.debug("Получен статус 400 IllegalArgumentEx {}", Arrays.toString(e.getStackTrace()));
        return new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.name(), "Bad reequest", Arrays.toString(e.getStackTrace()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(final ConflictException e) {
        log.debug("Получен статус 409 conflict {}", Arrays.toString(e.getStackTrace()));
        return new ErrorResponse(e.getMessage(), HttpStatus.CONFLICT.name(), "dublication", Arrays.toString(e.getStackTrace()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleObjectNotFoundException(final ObjectNotFoundException e) {
        log.debug("Получен статус 404 not found {}", Arrays.toString(e.getStackTrace()));
        return new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.name(), "onject not found", Arrays.toString(e.getStackTrace()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(BadRequestException e) {
        log.debug("Получен статус 400 bad request {}", Arrays.toString(e.getStackTrace()));

        return new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.name(), "bad parametrs", Arrays.toString(e.getStackTrace()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.debug("Получен статус 409 DataIntegrityViolationEx {} ", Arrays.toString(e.getStackTrace()));
        return new ErrorResponse(e.getMessage(), HttpStatus.CONFLICT.name(), "Data problem", Arrays.toString(e.getStackTrace()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidEx(MethodArgumentNotValidException e) {
        log.debug("Получен статус 400 {}", Arrays.toString(e.getStackTrace()));
        return new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.name(), "", Arrays.toString(e.getStackTrace()));
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.debug("Получен статус 400 {}", Arrays.toString(e.getStackTrace()));
        return new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.name(), "", Arrays.toString(e.getStackTrace()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException e) {
        log.debug("Получен статус 400 {}", Arrays.toString(e.getStackTrace()));
        return new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.name(), "", Arrays.toString(e.getStackTrace()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(Throwable e) {
        log.debug("Получен статус 500 {}", Arrays.toString(e.getStackTrace()));
        return new ErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.name(), "", Arrays.toString(e.getStackTrace()));
    }


}