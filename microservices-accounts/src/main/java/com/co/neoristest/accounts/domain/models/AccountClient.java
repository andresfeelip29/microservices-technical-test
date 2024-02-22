package com.co.neoristest.accounts.domain.models;

import com.co.neoristest.common.models.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cuentas_usuarios")
@Entity
public class AccountClient extends BaseEntity {

    @Column(name = "cliente_id")
    private Long clientId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AccountClient that = (AccountClient) o;
        return Objects.equals(getClientId(), that.getClientId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getClientId());
    }
}
