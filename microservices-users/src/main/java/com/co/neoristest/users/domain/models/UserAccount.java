package com.co.neoristest.users.domain.models;

import com.co.neoristest.common.models.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usuarios_cuentas")
@Entity
public class UserAccount extends BaseEntity {

    @Column(name = "cuenta_id")
    private Long accountId;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserAccount that = (UserAccount) o;
        return Objects.equals(getAccountId(), that.getAccountId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getAccountId());
    }
}
