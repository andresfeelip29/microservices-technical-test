package com.co.neoristest.users.service;

import com.co.neoristest.common.domain.enums.BankAccountType;
import com.co.neoristest.common.domain.enums.ExceptionMessage;
import com.co.neoristest.users.client.AccountFeignRequest;
import com.co.neoristest.users.domain.AccountDetail;
import com.co.neoristest.users.domain.dto.UserDto;
import com.co.neoristest.users.domain.dto.UserResponseDto;
import com.co.neoristest.users.domain.models.User;
import com.co.neoristest.users.domain.models.UserAccount;
import com.co.neoristest.users.exception.UserNotFoundException;
import com.co.neoristest.users.mapper.UserMapper;
import com.co.neoristest.users.repository.UserAccountRepository;
import com.co.neoristest.users.repository.UserRepository;
import com.co.neoristest.users.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserAccountRepository userAccountRepository;

    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Mock
    private AccountFeignRequest accountFeignRequest;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDto userDtoForSave;
    private UserDto userDtoForUpdate;
    private User userForSave;
    private User userForUpdate;
    private AccountDetail accountDetail;
    private static final Long FIRST_USER_ID = 1L;
    private static final Long ACCOUNT_ID = 3L;
    private static final Long USER_INCORRET_ID = 4L;

    @BeforeEach
    public void setUp() {
        this.userDtoForSave = new UserDto("admin",
                "$2a$10$/pEfDmFssLZuyvgNl65UQOLkxXB1KUKB0CDK28oYs0SFuzL1qeE7C", true);

        this.userDtoForUpdate = new UserDto("usuario2",
                "$2a$10$4KwWG0WsaQlX73Y2i6bf/u5FD/sGipdxmt3HcUkVTDVZ1BNuVA9qi", true);

        this.accountDetail = new AccountDetail(ACCOUNT_ID, "40985777321",
                BankAccountType.CHECKING_ACCOUNT, new BigDecimal("200"), true);


        this.userForSave = this.userMapper.userDtoToUser(this.userDtoForSave);
        UserAccount userAccount = new UserAccount(ACCOUNT_ID, this.userForSave);
        this.userForSave.setId(FIRST_USER_ID);
        this.userForSave.addAccountToUser(userAccount);

        this.userForUpdate = this.userMapper.updateUserToUserDto(this.userForSave, this.userDtoForUpdate);
    }


    @Test
    void testServiceForFindUserDetailById() {

        when(this.userRepository.findById(FIRST_USER_ID)).thenReturn(Optional.of(this.userForSave));

        when(this.accountFeignRequest.getAllAccoutDetail(List.of(ACCOUNT_ID))).thenReturn(List.of(this.accountDetail));

        Optional<UserResponseDto> userResponseOptional = this.userService.findUserAccountDetail(FIRST_USER_ID);
        assertTrue(userResponseOptional.isPresent());

        UserResponseDto result = userResponseOptional.get();
        assertNotNull(result.accountDetails());
        assertNotNull(result);
        assertEquals(FIRST_USER_ID, result.id());

        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class,
                () -> this.userService.findUserAccountDetail(USER_INCORRET_ID));

        assertEquals(String.format(ExceptionMessage.USER_NOT_FOUND.getMessage(), USER_INCORRET_ID), userNotFoundException.getMessage());
    }

    @Test
    void testServiceSaveUser() {

        when(this.userRepository.save(any(User.class))).thenReturn(this.userForSave);

        assertNotNull(this.userService.save(this.userDtoForSave));
    }

    @Test
    void testServiceUpdateUser() {

        when(this.userRepository.findById(FIRST_USER_ID)).thenReturn(Optional.of(this.userForSave));

        when(this.userRepository.save(any(User.class))).thenReturn(this.userForUpdate);

        UserResponseDto result = this.userService.update(this.userDtoForUpdate, this.userForSave.getId());

        assertNotNull(result);

        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class,
                () -> this.userService.update(this.userDtoForUpdate, USER_INCORRET_ID));

        assertEquals(String.format(ExceptionMessage.USER_NOT_FOUND.getMessage(), USER_INCORRET_ID), userNotFoundException.getMessage());
    }

}
