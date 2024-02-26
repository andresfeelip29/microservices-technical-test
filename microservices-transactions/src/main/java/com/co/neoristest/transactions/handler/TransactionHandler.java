package com.co.neoristest.transactions.handler;

import com.co.neoristest.common.utils.ParseDate;
import com.co.neoristest.transactions.domain.dto.TransactionDto;
import com.co.neoristest.transactions.domain.dto.TransactionResponseDto;
import com.co.neoristest.transactions.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Component
public class TransactionHandler {


    private final TransactionService transactionService;

    public TransactionHandler(TransactionService transactionService) {
        this.transactionService = transactionService;
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
        Mono<TransactionDto> movementDTOMono = request.bodyToMono(TransactionDto.class);
        return movementDTOMono.flatMap(this.transactionService::saveTransaction)
                .flatMap(result -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(result))
                ).switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON).build());
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



}
