package com.co.neoristest.gateway.exception.handler;

import com.co.neoristest.common.domain.dto.ErrorResponse;
import com.co.neoristest.common.exception.handler.GlobalExceptionHandler;
import com.co.neoristest.gateway.exception.MissingAuthorizationHeaderException;
import com.co.neoristest.gateway.exception.UnAuthorizedAccessToApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GatewayGlobalExceptionHandler extends GlobalExceptionHandler {

    @ExceptionHandler(value = {MissingAuthorizationHeaderException.class , UnAuthorizedAccessToApplicationException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleException(Throwable e) {
        return this.buildErrorResponse(e, e.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
