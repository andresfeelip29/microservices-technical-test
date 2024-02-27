package com.co.neoristest.accounts.service;

import com.co.neoristest.accounts.client.UserFeignRequest;
import com.co.neoristest.accounts.domain.User;
import com.co.neoristest.accounts.domain.dto.AccountDto;
import com.co.neoristest.accounts.domain.dto.AccountResponseDto;
import com.co.neoristest.accounts.domain.models.Account;
import com.co.neoristest.accounts.domain.models.AccountUser;
import com.co.neoristest.accounts.exception.AccountNotFoundException;
import com.co.neoristest.accounts.mapper.AccountMapper;
import com.co.neoristest.accounts.repository.AccountClientRepository;
import com.co.neoristest.accounts.repository.AccountRepository;
import com.co.neoristest.accounts.service.impl.AccountServiceImpl;
import com.co.neoristest.common.domain.enums.BankAccountType;
import com.co.neoristest.common.domain.enums.ExceptionMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountClientRepository accountClientRepository;

    @Spy
    private AccountMapper accountMapper = Mappers.getMapper(AccountMapper.class);

    @Mock
    private UserFeignRequest userFeignRequest;

    @InjectMocks
    private AccountServiceImpl accountService;

    private AccountDto accountDtoForSave;

    private AccountDto accountDtoForUpdate;

    private Account accountForSave;

    private Account accountForUpdate;

    private User user;
    private User userUpdate;

    private static final Long ACCOUNT_ID = 3L;
    private static final Long ACCOUNT_INCORRET_ID = 4L;
    private static final Long FIRST_USER_ID = 1L;
    private static final Long SECOND_USER_ID = 2L;

    @BeforeEach
    public void setUp() {
        this.accountDtoForSave = new AccountDto(BankAccountType.SAVINGS_ACCOUNT,
                new BigDecimal("100200"), true, FIRST_USER_ID);

        this.accountDtoForUpdate = new AccountDto(BankAccountType.CHECKING_ACCOUNT,
                new BigDecimal("200"), true, SECOND_USER_ID);

        this.user = new User(FIRST_USER_ID, "Admin", true);

        this.userUpdate = new User(SECOND_USER_ID, "Admin2", true);

        AccountUser accountUser = new AccountUser(FIRST_USER_ID);
        AccountUser accountUserUpdate = new AccountUser(SECOND_USER_ID);

        this.accountForSave = this.accountMapper.accountDtoToAccount(accountDtoForSave);
        this.accountForSave.setId(ACCOUNT_ID);
        this.accountForSave.setAccountUser(accountUser);

        this.accountForUpdate = this.accountMapper.updateAccountToAccountDto(this.accountForSave, this.accountDtoForUpdate);
        this.accountForUpdate.setId(ACCOUNT_ID);
        this.accountForUpdate.setAccountUser(accountUserUpdate);

    }

    @Test
    void testServiceForFindAccountDetailById() {

        when(this.accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(this.accountForSave));

        Optional<AccountResponseDto> accountOptional = this.accountService.findAccountUserDetail(ACCOUNT_ID);
        assertTrue(accountOptional.isPresent());

        AccountResponseDto result = accountOptional.get();
        assertNotNull(result);
        assertEquals(ACCOUNT_ID, result.id());

        AccountNotFoundException accountNotFoundException = assertThrows(AccountNotFoundException.class,
                () -> this.accountService.findAccountUserDetail(ACCOUNT_INCORRET_ID));

        assertEquals(String.format("No existe cuenta registrada en sistema con id: %d!", ACCOUNT_INCORRET_ID), accountNotFoundException.getMessage());
    }

    @Test
    void testServiceSaveAccount() {
        when(this.userFeignRequest.findUserFromMicroserviceUser(FIRST_USER_ID)).thenReturn(this.user);

        when(this.accountRepository.save(any(Account.class))).thenReturn(this.accountForSave);

        assertNotNull(this.accountService.saveAccount(this.accountDtoForSave));
    }

    @Test
    void testServiceUpdateAccount() {
        when(this.userFeignRequest.findUserFromMicroserviceUser(SECOND_USER_ID)).thenReturn(this.userUpdate);

        when(this.accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(this.accountForSave));

        when(this.accountRepository.save(any(Account.class))).thenReturn(this.accountForUpdate);

        AccountResponseDto result = this.accountService.updateAccount(this.accountDtoForUpdate, this.accountForSave.getId());

        assertNotNull(result);

        AccountNotFoundException accountNotFoundException = assertThrows(AccountNotFoundException.class,
                () -> this.accountService.updateAccount(this.accountDtoForUpdate, ACCOUNT_INCORRET_ID));

        assertEquals(String.format(ExceptionMessage.ACCOUNT_NOT_FOUND.getMessage(), ACCOUNT_INCORRET_ID), accountNotFoundException.getMessage());
    }

    @Test
    void testServiceDeleteAccount() {
        when(this.accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(this.accountForSave));

        doNothing().when(this.accountRepository).delete(any(Account.class));

        assertTrue(this.accountService.deleteAccount(ACCOUNT_ID));

        AccountNotFoundException accountNotFoundException = assertThrows(AccountNotFoundException.class,
                () -> this.accountService.deleteAccount(ACCOUNT_INCORRET_ID));

        assertEquals(String.format(ExceptionMessage.ACCOUNT_NOT_FOUND.getMessage(), ACCOUNT_INCORRET_ID), accountNotFoundException.getMessage());

    }
}
