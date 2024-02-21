package com.co.neoristest.users.domain.models;

import com.co.neoristest.common.models.entity.BaseEntity;
import com.co.neoristest.users.domain.AccountDetail;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@Table(name = "usuarios")
@Entity
public class User extends BaseEntity {

    public User() {
        this.userAccounts = new ArrayList<>();
    }

    @NotEmpty
    @Column(name = "usuario")
    private String username;

    @NotEmpty
    @Column(name = "contraseña")
    private String password;

    @NotNull
    @Column(name = "estado")
    private String status;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "usuario_id")
    private List<UserAccount> userAccounts;

    @Transient
    private List<AccountDetail> accountDetails;

    public void addAccountToUser(UserAccount userAccount) {
        this.userAccounts.add(userAccount);
    }

    public void removeAccountToUser(UserAccount userAccount) {
        this.userAccounts.remove(userAccount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return Objects.equals(getUsername(), user.getUsername()) && Objects.equals(getPassword(), user.getPassword()) && Objects.equals(getStatus(), user.getStatus()) && Objects.equals(getUserAccounts(), user.getUserAccounts()) && Objects.equals(getAccountDetails(), user.getAccountDetails());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getUsername(), getPassword(), getStatus(), getUserAccounts(), getAccountDetails());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("username='").append(username).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", status='").append(status).append('\'');
        sb.append(", userAccounts=").append(userAccounts);
        sb.append(", accountDetails=").append(accountDetails);
        sb.append('}');
        return sb.toString();
    }
}
