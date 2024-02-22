package com.co.neoristest.accounts.mapper;

import com.co.neoristest.accounts.domain.dto.AccountDto;
import com.co.neoristest.accounts.domain.dto.AccountExternalDto;
import com.co.neoristest.accounts.domain.dto.AccountResponseDto;
import com.co.neoristest.accounts.domain.models.Account;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AccountMapper {

    Account accountDtoToAccount(AccountDto accountDto);

    AccountResponseDto accountToAccountResponseDto(Account account);

    Account updateAccountToAccountDto(@MappingTarget Account account, AccountDto accountDto);

    AccountExternalDto accountToAccountExternalDto(Account account);


}
