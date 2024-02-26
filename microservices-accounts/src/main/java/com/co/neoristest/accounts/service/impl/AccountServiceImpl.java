package com.co.neoristest.accounts.service.impl;

import com.co.neoristest.accounts.client.UserFeignRequest;
import com.co.neoristest.accounts.domain.User;
import com.co.neoristest.accounts.domain.dto.AccountDto;
import com.co.neoristest.accounts.domain.dto.AccountExternalDto;
import com.co.neoristest.accounts.domain.dto.AccountResponseDto;
import com.co.neoristest.accounts.domain.models.Account;
import com.co.neoristest.accounts.domain.models.AccountUser;
import com.co.neoristest.accounts.exception.AccountNotFoundException;
import com.co.neoristest.accounts.exception.BalanceNegativeException;
import com.co.neoristest.accounts.exception.ClientAccountNotFoundException;
import com.co.neoristest.accounts.exception.ClientNotFoundException;
import com.co.neoristest.accounts.mapper.AccountMapper;
import com.co.neoristest.accounts.repository.AccountClientRepository;
import com.co.neoristest.accounts.repository.AccountRepository;
import com.co.neoristest.accounts.service.AccountService;
import com.co.neoristest.common.domain.enums.ExceptionMessage;
import com.co.neoristest.common.utils.CalculatedBalance;
import com.co.neoristest.common.utils.GenerateRamdomAccountNumber;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final AccountClientRepository accountClientRepository;

    private final AccountMapper accountMapper;

    private final UserFeignRequest userFeignRequest;

    public AccountServiceImpl(AccountRepository accountRepository, AccountClientRepository
            accountClientRepository, AccountMapper accountMapper,
                              UserFeignRequest userFeignRequest) {
        this.accountRepository = accountRepository;
        this.accountClientRepository = accountClientRepository;
        this.accountMapper = accountMapper;
        this.userFeignRequest = userFeignRequest;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountResponseDto> getAllAccounts() {
        log.info("Se realiza consulta para listar todas las cuentas");
        return this.accountRepository.findAll()
                .stream()
                .map(this.accountMapper::accountToAccountResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountExternalDto> getAllAccountsFromMicroserviceUsers(List<Long> accountsIds) {
        log.info("Se realiza consulta de cuentas {}, desde microservicio de usuarios", accountsIds);
        return this.accountRepository.findAllById(accountsIds)
                .stream()
                .map(this.accountMapper::accountToAccountExternalDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccountResponseDto> findAccountUserDetail(Long accountId) {
        log.info("Se realiza consulta con detalle de usuario, con cuenta id: {}", accountId);
        Account account = this.accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(String.format(ExceptionMessage.ACCOUNT_NOT_FOUND.getMessage(), accountId)));
        try {
            log.info("Se realiza consulta a microservicio de usuarios, a cliente con id : {}", account.getAccountUser().getClientId());
            User userFromMicroserviceClient = this.userFeignRequest.findUserFromMicroserviceUser(account.getAccountUser().getClientId());
            account.setUser(userFromMicroserviceClient);
            return Optional.ofNullable(this.accountMapper.accountToAccountResponseDto(account));
        } catch (FeignException e) {
            log.info("No se encontro cuenta con id: {}, asociada algun usuario en consulta a microservicio clientes", accountId);
            throw new ClientAccountNotFoundException(String.format(ExceptionMessage.ACCOUNT_ASSOCIATED_TO_CLIENT_NO_FOUND.getMessage(), accountId));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccountResponseDto> findAccountByAccountNumber(String accountNumber) {
        log.info("Se realiza consulta con numero de cuenta: {}", accountNumber);
        Account account = this.accountRepository.findAccountByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(String.format(ExceptionMessage.ACCOUNT_NUMBER_NOT_FOUND.getMessage(), accountNumber)));
        try {
            log.info("Se realiza consulta a microservicio de usuarios, a usuario con id : {}", account.getAccountUser().getClientId());
            User userFromMicroserviceClient = this.userFeignRequest.findUserFromMicroserviceUser(account.getAccountUser().getClientId());
            account.setUser(userFromMicroserviceClient);
            return Optional.ofNullable(this.accountMapper.accountToAccountResponseDto(account));
        } catch (FeignException e) {
            log.info("No se encontro cuenta con numero: {}, asociada algun usuario en consulta a microservicio clientes", accountNumber);
            throw new ClientAccountNotFoundException(String.format(ExceptionMessage.ACCOUNT_NUMBER_ASSOCIATED_TO_CLIENT_NO_FOUND.getMessage(), accountNumber));
        }
    }

    @Override
    @Transactional
    public AccountResponseDto saveAccount(AccountDto accountDTO) {

        AccountResponseDto response = null;
        log.info("Se realiza proceso de almacenado de informacion de cuenta: {}", accountDTO);
        if (Objects.isNull(accountDTO)) return null;
        log.info("Se realiza consulta a microservicio de usuarios para asignacion de cuenta, a usuario con id : {}", accountDTO.userId());

        if (Boolean.TRUE.equals(CalculatedBalance.isLessThanZero(accountDTO.balance()))) {
            throw new BalanceNegativeException(ExceptionMessage.BALANCE_NEGATIVE.getMessage());
        }

        try {

            User userFromMicroserviceUsers = this.userFeignRequest.findUserFromMicroserviceUser(accountDTO.userId());
            Account account = this.accountMapper.accountDtoToAccount(accountDTO);
            AccountUser accountUser = this.accountClientRepository.save(new AccountUser(userFromMicroserviceUsers.getId()));
            account.setAccountUser(accountUser);
            account.setAccountNumber(GenerateRamdomAccountNumber.generateBankAccountNumber());
            account = this.accountRepository.save(account);
            account.setUser(userFromMicroserviceUsers);
            response = this.accountMapper.accountToAccountResponseDto(account);

            log.info("Se realiza consulta a microservicio de usuarios para creacion de la relacion, a usuario con id : {}", accountDTO.userId());
            this.userFeignRequest.saveUserAccountFromMicroserviceUser(account.getId(), userFromMicroserviceUsers.getId());

        } catch (FeignException e) {
            log.info("No se encontro usuario con id: {} en consulta a microservicio usuarios", accountDTO.userId());
            throw new ClientNotFoundException(String.format(ExceptionMessage.USER_NOT_FOUND.getMessage(), accountDTO.userId()));

        }
        return response;
    }

    @Override
    @Transactional
    public AccountResponseDto updateAccount(AccountDto accountDTO, Long accountId) {
        AccountResponseDto response = null;

        log.info("Se realiza proceso de actualizacion de informacion de cuenta: {}", accountDTO);

        if (Objects.isNull(accountDTO)) return null;

        if (Boolean.TRUE.equals(CalculatedBalance.isLessThanZero(accountDTO.balance()))) {
            throw new BalanceNegativeException(ExceptionMessage.BALANCE_NEGATIVE.getMessage());
        }

        try {
            User userFromMicroserviceUsers = this.userFeignRequest.findUserFromMicroserviceUser(accountDTO.userId());
            Account account = this.accountRepository.findById(accountId)
                    .orElseThrow(() -> new AccountNotFoundException(String.format(ExceptionMessage.ACCOUNT_NOT_FOUND.getMessage(), accountId)));

            this.userFeignRequest.deleteAccountUserFromMicroserviceUser(account.getAccountUser().getClientId(), accountId);

            account.getAccountUser().setClientId(accountDTO.userId());
            account = this.accountMapper.updateAccountToAccountDto(account, accountDTO);
            response = this.accountMapper.accountToAccountResponseDto(this.accountRepository.save(account));

            log.info("Se realiza consulta a microservicio de usuarios para creacion de la relacion, a usuario con id : {}", accountDTO.userId());
            this.userFeignRequest.saveUserAccountFromMicroserviceUser(account.getId(), userFromMicroserviceUsers.getId());

        } catch (FeignException e) {
            log.info("No se encontro usuario con id: {} en consulta a microservicio usuarios", accountDTO.userId());
            throw new ClientNotFoundException(String.format(ExceptionMessage.USER_NOT_FOUND.getMessage(), accountDTO.userId()));

        }
        return response;
    }

    @Override
    @Transactional
    public Boolean deleteAccount(Long accountId) {
        log.info("Se inicia proceso de eliminacion de cuenta: {}", accountId);

        Account account = this.accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(String.format(ExceptionMessage.ACCOUNT_NOT_FOUND.getMessage(), accountId)));

        try {
            this.accountRepository.delete(account);
            this.userFeignRequest.deleteAccountUserFromMicroserviceUser(account.getAccountUser().getClientId(), accountId);
            log.info("Se elimina cuenta con id: {}", accountId);
            return true;
        } catch (FeignException e) {
            log.info("No se encontro cuenta con id: {}, asociada algun usuario en consulta a microservicio clientes", accountId);
            throw new ClientAccountNotFoundException(String.format(ExceptionMessage.ACCOUNT_ASSOCIATED_TO_CLIENT_NO_FOUND.getMessage(), accountId));
        }
    }

    @Override
    @Transactional
    public Optional<AccountResponseDto> updateBalanceAccount(String accountNumber, BigDecimal newBalance) {
        Optional<AccountResponseDto> response;
        log.info("Se inicia proceso de actualizacion de balance de la cuenta con id: {} " +
                "desde microservicio de movimientos", accountNumber);
        Account account = this.accountRepository.findAccountByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(String.format(ExceptionMessage.ACCOUNT_NUMBER_NOT_FOUND.getMessage(), accountNumber)));
        account.setBalance(newBalance);
        response = Optional.ofNullable(this.accountMapper.accountToAccountResponseDto(this.accountRepository.save(account)));
        if (response.isPresent()) {
            log.info("Nuevo balance actualizado con exito!");
        } else {
            log.info("Error a actualizar balance!");
        }
        return response;
    }
}
