package com.co.neoristest.transactions.mapper;

import com.co.neoristest.transactions.domain.dto.TransactionResponseDto;
import com.co.neoristest.transactions.domain.models.Transaction;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TransactionMapper {

    TransactionResponseDto transactionToTransactionResponseDto(Transaction transaction);
}
