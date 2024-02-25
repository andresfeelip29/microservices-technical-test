package com.co.neoristest.common.exception.handler;


import com.co.neoristest.common.domain.dto.ErrorResponse;
import com.co.neoristest.common.exception.DomainException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return this.buildErrorResponse(exception, exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        List<String> errs = this.fieldValidate(e);
        return this.buildErrorResponse(e, String.join(", ", errs), HttpStatus.PRECONDITION_FAILED);
    }


    @ExceptionHandler(value = {ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleException(ValidationException validationException) {
        ResponseEntity<ErrorResponse> errorResponse;
        if (validationException instanceof ConstraintViolationException) {
            String violations = extractViolationsFromException((ConstraintViolationException) validationException);
            log.error(violations, validationException);
            errorResponse = this.buildErrorResponse(validationException,
                    validationException.getMessage(),
                    HttpStatus.BAD_REQUEST);
        } else {
            String exceptionMessage = validationException.getMessage();
            log.error(exceptionMessage, validationException);
            errorResponse = this.buildErrorResponse(validationException,
                    validationException.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
        return errorResponse;
    }

    private String extractViolationsFromException(ConstraintViolationException validationException) {
        return validationException.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("--"));
    }


    private List<String> fieldValidate(MethodArgumentNotValidException result) {
        return result.getBindingResult()
                .getAllErrors()
                .stream()
                .map(err -> ((FieldError) err).getField().concat(" ").concat(Objects.requireNonNull(err.getDefaultMessage())))
                .toList();
    }

    protected ResponseEntity<ErrorResponse> buildErrorResponse(Throwable e, String message, HttpStatus httpStatus) {
        log.error(e.getMessage());
        return ResponseEntity.status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.builder()
                        .code(httpStatus.value())
                        .message(message)
                        .build());
    }

}
