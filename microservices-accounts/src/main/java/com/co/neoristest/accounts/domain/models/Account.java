package com.co.neoristest.accounts.domain.models;

import com.co.neoristest.accounts.domain.User;
import com.co.neoristest.common.domain.enums.BankAccountType;
import com.co.neoristest.common.models.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cuentas")
@Entity
public class Account extends BaseEntity {

    @NotEmpty
    @Column(name = "numero_cuenta")
    private String accountNumber;

    @NotNull
    @JoinColumn(name = "tipo_cuenta")
    @Enumerated(EnumType.STRING)
    private BankAccountType accountType;

    @NotNull
    @Column(name = "saldo")
    private BigDecimal balance;

    @NotNull
    @Column(name = "estado")
    private Boolean status;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private AccountClient accountClient;

    @Transient
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Account account = (Account) o;
        return getAccountNumber().equals(account.getAccountNumber()) && getAccountType() == account.getAccountType()
                && getBalance().equals(account.getBalance()) && getStatus().equals(account.getStatus()) &&
                getAccountClient().equals(account.getAccountClient()) && getUser().equals(account.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getAccountNumber(),
                getAccountType(), getBalance(), getStatus(),
                getAccountClient(), getUser());
    }
}
