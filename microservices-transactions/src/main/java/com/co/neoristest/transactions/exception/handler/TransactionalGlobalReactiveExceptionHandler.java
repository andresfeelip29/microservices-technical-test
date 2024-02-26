package com.co.neoristest.transactions.exception.handler;

import com.co.neoristest.common.domain.dto.ErrorResponse;
import com.co.neoristest.common.exception.AccountHasNoBalanceException;
import com.co.neoristest.transactions.exception.BalanceUpdateException;
import com.co.neoristest.transactions.exception.TransactionNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Map;
import java.util.Optional;


@Slf4j
@Component
public class TransactionalGlobalReactiveExceptionHandler extends AbstractErrorWebExceptionHandler {

    public TransactionalGlobalReactiveExceptionHandler(ErrorAttributes errorAttributes,
                                                       WebProperties.Resources resources,
                                                       ApplicationContext applicationContext,
                                                       ServerCodecConfigurer configurer) {
        super(errorAttributes, resources, applicationContext);
        this.setMessageReaders(configurer.getReaders());
        this.setMessageWriters(configurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::handleException);
    }

    private @NotNull Mono<ServerResponse> handleException(ServerRequest request) {

        Throwable error = getError(request);

        if (error instanceof BalanceUpdateException) {
            return this.buildErrorResponse(error.getMessage(), HttpStatus.NOT_FOUND);
        }

        if (error instanceof TransactionNotFoundException) {
            return this.buildErrorResponse(error.getMessage(), HttpStatus.NOT_FOUND);
        }

        if (error instanceof AccountNotFoundException) {
            return this.buildErrorResponse(error.getMessage(), HttpStatus.NOT_FOUND);
        }

        if (error instanceof WebClientResponseException) {
            return this.buildErrorResponse(error.getMessage(), HttpStatus.NOT_FOUND);
        }

        if (error instanceof ConstraintViolationException) {
            return this.buildErrorResponse(error.getMessage(), HttpStatus.BAD_REQUEST);
        }

        if (error instanceof AccountHasNoBalanceException) {
            return this.buildErrorResponse(error.getMessage(), HttpStatus.BAD_REQUEST);
        }


        return this.buildErrorResponse(error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    protected Mono<ServerResponse> buildErrorResponse(String message, HttpStatus httpStatus) {
        return ServerResponse.status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ErrorResponse.builder()
                        .code(httpStatus.value())
                        .message(message)
                        .build()));
    }
}
