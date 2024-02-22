package com.co.neoristest.accounts.controller;

import com.co.neoristest.accounts.domain.dto.AccountDto;
import com.co.neoristest.accounts.domain.dto.AccountExternalDto;
import com.co.neoristest.accounts.domain.dto.AccountResponseDto;
import com.co.neoristest.accounts.service.AccountService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api/v1/cuentas")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/")
    public ResponseEntity<List<AccountResponseDto>> getAllAccounts() {
        log.info("Se recibe peticion para consulta de todos los clientes");
        return ResponseEntity.ok(this.accountService.getAllAccounts());
    }

    @GetMapping("/external/")
    public ResponseEntity<List<AccountExternalDto>> getAllAccountsFromMicroserviceClient(@RequestParam List<Long> accountsIds) {
        log.info("Se recibe peticion para consulta de todos los clientes");
        return ResponseEntity.ok(this.accountService.getAllAccountsFromMicroserviceUsers(accountsIds));
    }

    @GetMapping("/detail/{accountId}")
    public ResponseEntity<AccountResponseDto> findAccountUserDetail(@PathVariable Long accountId) {
        log.info("Se recibe peticion de consulta de cuenta con detalle con id: {}", accountId);
        return this.accountService.findAccountUserDetail(accountId)
                .map(account -> ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(account))
                .orElse(ResponseEntity.badRequest().build());
    }
    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountResponseDto> findAccountByAccountNumber(@PathVariable String accountNumber) {
        log.info("Se recibe peticion de consulta de cuenta por numero de cuenta: {}", accountNumber);
        return this.accountService.findAccountByAccountNumber(accountNumber)
                .map(account -> ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(account))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping("/")
    public ResponseEntity<AccountResponseDto> createAccount(@Valid @RequestBody AccountDto accountDTO) {
        log.info("Se recibe peticion para crear cuenta {}!", accountDTO);
        AccountResponseDto result = this.accountService.saveAccount(accountDTO);
        if (!Objects.isNull(result)) {
            log.info("Se crea cuenta con id: {}, y usuario id: {}", result.id(), result.user().getId());
            return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(result);
        }
        log.error("Error en solicitud de creacion de cuenta!");
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<AccountResponseDto> updateAccount(
            @Valid @RequestBody AccountDto accountDTO, @PathVariable Long accountId) {
        log.info("Se recibe peticion para actualizar cuenta {}!", accountDTO);
        AccountResponseDto result = this.accountService.updateAccount(accountDTO, accountId);
        if (!Objects.isNull(result)) {
            log.info("Se actualiza cuenta con id: {}, y usuario id: {}", result.id(), result.user().getId());
            return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(result);
        }
        log.error("Error en solicitud de actualizacion de cuenta!");
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long accountId) {
        log.info("Se recibe peticion para eliminacion de cuenta con id: {}!", accountId);
        Boolean result = this.accountService.deleteAccount(accountId);
        if (!Objects.isNull(result)) {
            log.info("Cuenta con id: {} eliminada con exito!", accountId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).contentType(MediaType.APPLICATION_JSON).build();
        }
        log.error("Error en solicitud de eliminacion de cuenta!");
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/external/")
    public ResponseEntity<AccountResponseDto> updateBalanceAccount(@RequestParam Long accountId, @RequestParam BigDecimal newBalance) {
        log.info("Se recibe peticion externa desde microservicio de transaccion para actualizar balance de la cuenta con id: {}", accountId);
        return this.accountService.updateBalanceAccount(accountId, newBalance)
                .map(account -> ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(account))
                .orElse(ResponseEntity.badRequest().build());
    }

}
