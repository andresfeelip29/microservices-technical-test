package com.co.neoristest.accounts.exception.handler;

import com.co.neoristest.accounts.exception.AccountNotFoundException;
import com.co.neoristest.accounts.exception.BalanceNegativeException;
import com.co.neoristest.accounts.exception.ClientAccountNotFoundException;
import com.co.neoristest.accounts.exception.ClientNotFoundException;
import com.co.neoristest.common.domain.dto.ErrorResponse;
import com.co.neoristest.common.exception.BankAccountException;
import com.co.neoristest.common.exception.handler.GlobalExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AccountGlobalExceptionHandler extends GlobalExceptionHandler {


    @ExceptionHandler(value = {ClientAccountNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleException(ClientAccountNotFoundException e) {
        return this.buildErrorResponse(e, e.getMessage(), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(value = {BalanceNegativeException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleException(BalanceNegativeException e) {
        return this.buildErrorResponse(e, e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {AccountNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleException(AccountNotFoundException e) {
        return this.buildErrorResponse(e, e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {ClientNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleException(ClientNotFoundException e) {
        return this.buildErrorResponse(e, e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleException(HttpMessageNotReadableException e) {
        return this.buildErrorResponse(e, problemCut(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {BankAccountException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleException(BankAccountException e) {
        return this.buildErrorResponse(e, e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private String problemCut(String problem){
        return problem.split("problem:")[1];
    }

}
