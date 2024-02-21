package com.co.neoristest.users.domain;


import com.co.neoristest.common.domain.enums.BankAccountType;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDetail {

    private Long id;
    private String accountNumber;
    private BankAccountType accountType;
    private BigDecimal balance;
    private Boolean status;


}