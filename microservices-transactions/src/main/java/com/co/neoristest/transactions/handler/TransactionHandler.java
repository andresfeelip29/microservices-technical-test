package com.co.neoristest.transactions.handler;

import com.co.neoristest.common.utils.ParseDate;
import com.co.neoristest.transactions.domain.dto.TransactionDto;
import com.co.neoristest.transactions.domain.dto.TransactionResponseDto;
import com.co.neoristest.transactions.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class TransactionHandler {

    private final TransactionService transactionService;

    private final Validator validator;

    public TransactionHandler(TransactionService transactionService, Validator validator) {
        this.transactionService = transactionService;
        this.validator = validator;
    }

    public Mono<ServerResponse> findAll(ServerRequest request) {
        log.info("Se recibe peticion de consulta de cuentas!");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.transactionService.findAll(), TransactionResponseDto.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        String id = request.pathVariable("id");
        return this.transactionService.findById(id)
                .flatMap(result -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(result))
                ).switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON).build());
    }

    public Mono<ServerResponse> saveTransaction(ServerRequest request) {
        Mono<TransactionDto> transactionDtoMono = request.bodyToMono(TransactionDto.class);
        return transactionDtoMono.flatMap(transactionDto -> {
            Errors errors = new BeanPropertyBindingResult(transactionDto, TransactionDto.class.getName());
            this.validator.validate(transactionDto, errors);
            if (errors.hasErrors()) {
                return this.fieldValidate(errors);
            }
            return this.transactionService.saveTransaction(transactionDto).flatMap(result -> ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(result))
            ).switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON).build());
        });
    }

    public Mono<ServerResponse> deleteTransaction(ServerRequest request) {
        String id = request.pathVariable("id");
        return this.transactionService.deleteTransaction(id)
                .then(ServerResponse.noContent().build())
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> filterTransactionForRageDateAndClientId(ServerRequest request) {
        Optional<String> paramInitDate = request.queryParam("initDate");
        Optional<String> paramEndDate = request.queryParam("endDate");
        Optional<String> paramClientId = request.queryParam("clientId");
        if (paramInitDate.isPresent() && paramEndDate.isPresent() && paramClientId.isPresent()) {
            LocalDateTime initDate = ParseDate.formattDateTimeToParam(paramInitDate.get());
            LocalDateTime endDate = ParseDate.formattDateTimeToParam(paramEndDate.get());
            Long clientId = Long.parseLong(paramClientId.get());
            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(this.transactionService.filterTransactionForRageDateAndClientId(initDate, endDate, clientId), TransactionResponseDto.class);
        }
        return ServerResponse.badRequest().build();
    }

    private Mono<ServerResponse> fieldValidate(Errors errors) {
        Map<String, Object> result = new HashMap<>();
        return Flux.fromIterable(errors.getFieldErrors())
                .map(fieldError -> "Campo ".concat(fieldError.getField()).concat(" ").concat(Objects.requireNonNull(fieldError.getDefaultMessage())))
                .collectList()
                .flatMap(list -> {
                    result.put("status", HttpStatus.PRECONDITION_FAILED.value());
                    result.put("messages", list);
                    return ServerResponse.status(HttpStatus.PRECONDITION_FAILED)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromValue(result));
                });
    }

}
