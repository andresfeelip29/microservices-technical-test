package com.co.neoristest.transactions.domain.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Document(collection = "transacciones")
public class Transaction {

    @Id
    private String id;

    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    @JoinColumn(name = "fecha_creacion")
    private LocalDateTime createAt;

    @NotNull
    @JoinColumn(name = "valor_transaccion")
    private BigDecimal transactionValue;

    @NotNull
    @JoinColumn(name = "balance_incial_cuenta_origen")
    private BigDecimal initialBalanceAccountOrigin;

    @NotNull
    @JoinColumn(name = "balance_final_cuenta_origen")
    private BigDecimal finalBalanceAccountOrigin;

    @NotNull
    @JoinColumn(name = "balance_incial_cuenta_destino")
    private BigDecimal initialBalanceAccountDestiny;

    @NotNull
    @JoinColumn(name = "balance_final_cuenta_destino")
    private BigDecimal finalBalanceAccountDestiny;

    @NotNull
    @JoinColumn(name = "cliente_id")
    private Long clientId;

    @NotNull
    @JoinColumn(name = "numero_cuenta_origen")
    private String accountOriginNumber;

    @NotNull
    @JoinColumn(name = "numero_cuenta_destino")
    private String accountDestinyNumber;

    @NotNull
    @JoinColumn(name = "estado")
    private Boolean state;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return getId().equals(that.getId()) && getCreateAt().equals(that.getCreateAt()) && getTransactionValue().equals(that.getTransactionValue()) &&
                getInitialBalanceAccountOrigin().equals(that.getInitialBalanceAccountOrigin()) &&
                getFinalBalanceAccountOrigin().equals(that.getFinalBalanceAccountOrigin()) &&
                getInitialBalanceAccountDestiny().equals(that.getInitialBalanceAccountDestiny()) &&
                getFinalBalanceAccountDestiny().equals(that.getFinalBalanceAccountDestiny()) && getClientId().equals(that.getClientId()) &&
                getAccountOriginNumber().equals(that.getAccountOriginNumber()) && getAccountDestinyNumber().equals(that.getAccountDestinyNumber()) &&
                getState().equals(that.getState());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCreateAt(), getTransactionValue(), getInitialBalanceAccountOrigin(),
                getFinalBalanceAccountOrigin(),
                getInitialBalanceAccountDestiny(), getFinalBalanceAccountDestiny(), getClientId(), getAccountOriginNumber(),
                getAccountDestinyNumber(), getState());
    }
}
