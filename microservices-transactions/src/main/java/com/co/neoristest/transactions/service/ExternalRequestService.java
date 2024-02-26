package com.co.neoristest.transactions.service;

import com.co.neoristest.transactions.domain.Account;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface ExternalRequestService {

    Mono<Account> findAccountByAccountNumberFromMicroserviceAccount(String accountNumber);

    Mono<Account> updateBalanceToAccountFromMicroserviciosAccount(String accountNumber, BigDecimal newBalance);
}
