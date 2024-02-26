package com.co.neoristest.transactions.repository;


import com.co.neoristest.transactions.domain.models.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Repository
public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {

    Flux<Transaction> findAllByCreateAtBetweenAndClientId(@Param("initDate") LocalDateTime initDate,
                                                          @Param("endDate") LocalDateTime endDate,
                                                          @Param("clientId") Long clientId);
}
