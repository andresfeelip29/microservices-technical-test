package com.co.neoristest.accounts.domain.dto;

import com.co.neoristest.accounts.domain.User;
import com.co.neoristest.common.domain.enums.BankAccountType;

import java.math.BigDecimal;

public record AccountResponseDto(Long id, String accountNumber, BankAccountType accountType,
                                 BigDecimal balance, Boolean status, User user) {
}
