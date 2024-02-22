package com.co.neoristest.accounts.service;

import com.co.neoristest.accounts.domain.dto.AccountDto;
import com.co.neoristest.accounts.domain.dto.AccountExternalDto;
import com.co.neoristest.accounts.domain.dto.AccountResponseDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountService {

    List<AccountResponseDto> getAllAccounts();

    List<AccountExternalDto> getAllAccountsFromMicroserviceUsers(List<Long> accountsIds);

    Optional<AccountResponseDto> findAccountUserDetail(Long accountId);

    Optional<AccountResponseDto> findAccountByAccountNumber(String accountNumber);

    AccountResponseDto saveAccount(AccountDto accountDTO);

    AccountResponseDto updateAccount(AccountDto accountDTO, Long accountId);

    Boolean deleteAccount(Long accountId);

    Optional<AccountResponseDto> updateBalanceAccount(Long accountId, BigDecimal newBalance);
}
