package com.co.neoristest.transactions.service.impl;

import com.co.neoristest.common.domain.enums.ExceptionMessage;
import com.co.neoristest.transactions.domain.Account;
import com.co.neoristest.transactions.exception.BalanceUpdateException;
import com.co.neoristest.transactions.service.ExternalRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.Collections;

@Slf4j
@Service
public class ExternalRequestServiceImpl implements ExternalRequestService {


    private final WebClient.Builder webClient;


    @Value("${microservices.accounts.url}")
    private String urlMicroserviceAccounts;


    public ExternalRequestServiceImpl(WebClient.Builder webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<Account> findAccountByAccountNumberFromMicroserviceAccount(String accountNumber) {
        log.info("Se realiza solicitud de consulta a microservcios cuentas con numero de cuenta: {}", accountNumber);
        return this.webClient.build().get()
                .uri(urlMicroserviceAccounts.concat("/cuentas/{accountNumber}"),
                        Collections.singletonMap("accountNumber", accountNumber))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Account.class)
                .onErrorResume(WebClientResponseException.class, e -> e.getStatusCode().is4xxClientError() ? Mono.error(new AccountNotFoundException(
                        String.format(ExceptionMessage.ACCOUNT_NOT_FOUND_IN_MICROSERVICES_ACCOUNTS.getMessage(), accountNumber))) : Mono.empty())
                .switchIfEmpty(Mono.error(new AccountNotFoundException(
                        String.format(ExceptionMessage.ACCOUNT_NOT_FOUND_IN_MICROSERVICES_ACCOUNTS.getMessage(), accountNumber))));
    }

    @Override
    public Mono<Account> updateBalanceToAccountFromMicroserviciosAccount(String accountNumber, BigDecimal newBalance) {
        log.info("Se realiza solicitud de consulta a microservcios para actualizar balance de cuenta numero: {}", accountNumber);
        return this.webClient.build().put()
                .uri(urlMicroserviceAccounts.concat("/cuentas/external/"), uriBuilder -> uriBuilder
                        .queryParam("accountNumber", accountNumber)
                        .queryParam("newBalance", newBalance)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Account.class)
                .onErrorResume(WebClientResponseException.class, e -> e.getStatusCode().is4xxClientError() ? Mono.error(new BalanceUpdateException(
                        String.format(ExceptionMessage.BALANCE_UPDATE_ERROR.getMessage(), accountNumber))) : Mono.empty())
                .switchIfEmpty(Mono.error(new BalanceUpdateException(
                        String.format(ExceptionMessage.BALANCE_UPDATE_ERROR.getMessage(), accountNumber))));
    }
}
