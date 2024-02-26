package com.co.neoristest.transactions.service.impl;

import com.co.neoristest.common.domain.enums.ExceptionMessage;
import com.co.neoristest.common.exception.AccountHasNoBalanceException;
import com.co.neoristest.common.utils.CalculatedBalance;
import com.co.neoristest.transactions.domain.Account;
import com.co.neoristest.transactions.domain.User;
import com.co.neoristest.transactions.domain.dto.TransactionDto;
import com.co.neoristest.transactions.domain.dto.TransactionResponseDto;
import com.co.neoristest.transactions.domain.models.Transaction;
import com.co.neoristest.transactions.exception.TransactionNotFoundException;
import com.co.neoristest.transactions.mapper.TransactionMapper;
import com.co.neoristest.transactions.repository.TransactionRepository;
import com.co.neoristest.transactions.service.ExternalRequestService;
import com.co.neoristest.transactions.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    private final ExternalRequestService externalRequestService;

    private final TransactionMapper transactionMapper;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  ExternalRequestService externalRequestService,
                                  TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.externalRequestService = externalRequestService;
        this.transactionMapper = transactionMapper;
    }

    @Override
    public Flux<TransactionResponseDto> findAll() {
        log.info("Se realiza consulta de todas las transacciones!");
        return this.transactionRepository.findAll()
                .map(this.transactionMapper::transactionToTransactionResponseDto);
    }

    @Override
    public Mono<TransactionResponseDto> findById(String id) {
        log.info("Se realiza consulta de movimiento con id: {}", id);
        return this.transactionRepository.findById(id)
                .map(this.transactionMapper::transactionToTransactionResponseDto)
                .switchIfEmpty(Mono.error(
                        new TransactionNotFoundException(String.format(ExceptionMessage.TRANSACTION_NOT_FOUND.getMessage(), id))));
    }

    @Override
    public Mono<TransactionResponseDto> saveTransaction(TransactionDto transactionDto) {

        Mono<TransactionResponseDto> result;

        log.info("Se realiza proceso de transaccion en data: {}", transactionDto);

        if (Objects.isNull(transactionDto)) return Mono.empty();

        log.info("Se realiza consulta a microservicio de cuentas para verificar existencia y estado con cuenta origen : {}",
                transactionDto.accountOriginNumber());

        Mono<Account> accountOrigin = this.externalRequestService
                .findAccountByAccountNumberFromMicroserviceAccount(transactionDto.accountOriginNumber());

        log.info("Se realiza consulta a microservicio de cuentas para verificar existencia y estado con cuenta destino : {}",
                transactionDto.accountOriginNumber());

        Mono<Account> accountDestiny = this.externalRequestService
                .findAccountByAccountNumberFromMicroserviceAccount(transactionDto.accountOriginNumber());


        result = accountOrigin.zipWith(accountDestiny, (o, d) -> {

            if (CalculatedBalance.isLessThanZero(o.getBalance()) || CalculatedBalance.isZero(o.getBalance()) ||
                    CalculatedBalance.hasBalanceForTransaction(o.getBalance(), transactionDto.transactionValue()))
                throw new AccountHasNoBalanceException(
                        ExceptionMessage.ACCOUNT_HAS_NOT_BALANCE_FOR_TRANSACTION.getMessage());


            BigDecimal newBalanceAccountOrigin = CalculatedBalance.subtractAmount(transactionDto.transactionValue(), o.getBalance());
            BigDecimal newBalanceAccountDestiny = CalculatedBalance.addAmount(transactionDto.transactionValue(), d.getBalance());

            return Transaction.builder().
                    createAt(LocalDateTime.now())
                    .transactionValue(transactionDto.transactionValue())
                    .initialBalanceAccountOrigin(o.getBalance())
                    .finalBalanceAccountOrigin(newBalanceAccountOrigin)
                    .initialBalanceAccountDestiny(d.getBalance())
                    .finalBalanceAccountDestiny(newBalanceAccountDestiny)
                    .clientId(o.getUser().getId())
                    .accountOriginNumber(o.getAccountNumber())
                    .accountDestinyNumber(d.getAccountNumber())
                    .state(true)
                    .build();
        }).flatMap(transaction ->
                this.externalRequestService.updateBalanceToAccountFromMicroserviciosAccount(transaction.getAccountOriginNumber(), transaction.getFinalBalanceAccountOrigin())
                        .flatMap(account -> this.externalRequestService.updateBalanceToAccountFromMicroserviciosAccount(transaction.getAccountDestinyNumber(), transaction.getFinalBalanceAccountDestiny()))
                        .flatMap(account -> this.transactionRepository.save(transaction))
        ).map(this.transactionMapper::transactionToTransactionResponseDto);

        return result;
    }

    @Override
    public Mono<Void> deleteTransaction(String id) {
        log.info("Se se realiza proceso de eliminacion de transaccion con id: {}", id);
        return this.transactionRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new TransactionNotFoundException(String.format(ExceptionMessage.TRANSACTION_NOT_FOUND.getMessage(), id))))
                .flatMap(this.transactionRepository::delete)
                .then();
    }

    @Override
    public Flux<TransactionResponseDto> filterTransactionForRageDateAndClientId(LocalDateTime initDate, LocalDateTime endDate, Long clientId) {
        return this.transactionRepository.findAllByCreateAtBetweenAndClientId(initDate, endDate, clientId)
                .map(this.transactionMapper::transactionToTransactionResponseDto);
    }
}
