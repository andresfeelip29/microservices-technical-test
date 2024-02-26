package com.co.neoristest.transactions.service;

import com.co.neoristest.transactions.domain.dto.TransactionDto;
import com.co.neoristest.transactions.domain.dto.TransactionResponseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface TransactionService {

    Flux<TransactionResponseDto> findAll();

    Mono<TransactionResponseDto> findById(String id);

    Mono<TransactionResponseDto> saveTransaction(TransactionDto transactionDto);

    Mono<Void> deleteTransaction(String id);

    Flux<TransactionResponseDto> filterTransactionForRageDateAndClientId(LocalDateTime initDate,
                                                                      LocalDateTime endDate,
                                                                      Long clientId);
}
