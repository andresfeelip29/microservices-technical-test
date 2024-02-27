package com.co.neoristest.accounts.controller;

import com.co.neoristest.accounts.domain.User;
import com.co.neoristest.accounts.domain.dto.AccountDto;
import com.co.neoristest.accounts.domain.dto.AccountResponseDto;
import com.co.neoristest.accounts.domain.models.Account;
import com.co.neoristest.accounts.exception.handler.AccountGlobalExceptionHandler;
import com.co.neoristest.accounts.mapper.AccountMapper;
import com.co.neoristest.accounts.service.impl.AccountServiceImpl;
import com.co.neoristest.common.domain.enums.BankAccountType;
import com.co.neoristest.common.utils.GenerateRamdomAccountNumber;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest
@ContextConfiguration(classes = AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountServiceImpl accountService;

    @Spy
    private AccountMapper accountMapper = Mappers.getMapper(AccountMapper.class);

    @Autowired
    private ObjectMapper objectMapper;

    private AccountDto accountDtoForSave;

    private AccountResponseDto accountResponseDto;

    private static final Long ACCOUNT_ID = 3L;
    private static final Long USER_ID = 1L;

    private static final String BASE_URL = "/api/v1/cuentas";

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);

        var accountController = new AccountController(accountService);

        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        var converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);

        mockMvc = MockMvcBuilders.standaloneSetup(accountController)
                .setControllerAdvice(new AccountGlobalExceptionHandler())
                .setMessageConverters(converter)
                .build();

        this.accountDtoForSave = new AccountDto(BankAccountType.SAVINGS_ACCOUNT,
                new BigDecimal("100200"), true, USER_ID);

        User user = new User(USER_ID, "Admin", true);

        Account account = this.accountMapper.accountDtoToAccount(this.accountDtoForSave);
        account.setId(ACCOUNT_ID);
        account.setAccountNumber(GenerateRamdomAccountNumber.generateBankAccountNumber());
        account.setUser(user);

        this.accountResponseDto = this.accountMapper.accountToAccountResponseDto(account);
    }

    @Test
    void testRequestAccountControllerForSaveAccount() throws Exception {

        when(this.accountService.saveAccount(any(AccountDto.class))).thenReturn(this.accountResponseDto);

        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders
                        .post(BASE_URL.concat("/"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(this.accountDtoForSave)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

    }

    @Test
    void testRequestAccountControllerForFindAccountDetailById() throws Exception {

        when(this.accountService.findAccountUserDetail(ACCOUNT_ID)).thenReturn(Optional.of(this.accountResponseDto));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .get(BASE_URL.concat("/detail/".concat(ACCOUNT_ID.toString())))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();
    }

    @Test
    void testRequestControllerForDeleteById() throws Exception {

        when(this.accountService.deleteAccount(ACCOUNT_ID)).thenReturn(true);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .delete(BASE_URL.concat("/".concat(ACCOUNT_ID.toString())))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isNoContent())
                .andReturn();
    }

}
