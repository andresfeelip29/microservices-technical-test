package com.co.neoristest.transactions.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponseDto(String id, @JsonFormat(pattern = "dd/MM/yyyy HH:mm") LocalDateTime createAt,
                                     BigDecimal transactionValue,
                                     String accountOriginNumber,
                                     BigDecimal finalBalanceAccountOrigin,
                                     String accountDestinyNumber,
                                     BigDecimal finalBalanceAccountDestiny,
                                     Boolean state
) {


}
