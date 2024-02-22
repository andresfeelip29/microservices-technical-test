package com.co.neoristest.accounts.repository;

import com.co.neoristest.accounts.domain.models.AccountClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountClientRepository extends JpaRepository<AccountClient, Long> {
}
