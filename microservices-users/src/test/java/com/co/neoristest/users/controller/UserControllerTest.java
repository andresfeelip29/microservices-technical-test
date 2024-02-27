package com.co.neoristest.users.controller;

import com.co.neoristest.users.domain.dto.UserDto;
import com.co.neoristest.users.domain.dto.UserResponseDto;
import com.co.neoristest.users.domain.models.User;
import com.co.neoristest.users.domain.models.UserAccount;
import com.co.neoristest.users.exception.handler.UserGlobalExceptionHandler;
import com.co.neoristest.users.mapper.UserMapper;
import com.co.neoristest.users.service.impl.UserServiceImpl;
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


import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest
@ContextConfiguration(classes = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Autowired
    private ObjectMapper objectMapper;

    private UserDto userDtoSave;

    private UserResponseDto userResponseDto;

    private static final Long ACCOUNT_ID = 3L;

    private static final Long USER_ID = 1L;

    private static final String BASE_URL = "/api/v1/usuarios";


    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);

        var userController = new UserController(userService);

        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        var converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);

        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new UserGlobalExceptionHandler())
                .setMessageConverters(converter)
                .build();

        this.userDtoSave = new UserDto("admin",
                "$2a$10$/pEfDmFssLZuyvgNl65UQOLkxXB1KUKB0CDK28oYs0SFuzL1qeE7C", true);


        User user = this.userMapper.userDtoToUser(this.userDtoSave);
        UserAccount userAccount = new UserAccount(ACCOUNT_ID, user);
        user.setId(USER_ID);
        user.addAccountToUser(userAccount);

        this.userResponseDto = this.userMapper.userToUserResponseDto(user);

    }


    @Test
    void testRequestUserControllerForSaveUser() throws Exception {

        when(this.userService.save(any(UserDto.class))).thenReturn(this.userResponseDto);

        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders
                        .post(BASE_URL.concat("/"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(this.userDtoSave)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();
    }

    @Test
    void testRequestUserControllerForFindUserDetailById() throws Exception {

        when(this.userService.findUserAccountDetail(USER_ID)).thenReturn(Optional.of(this.userResponseDto));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .get(BASE_URL.concat("/detail/".concat(USER_ID.toString())))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();
    }

    @Test
    void testRequestControllerForDeleteById() throws Exception {

        doNothing().when(this.userService).delete(any(Long.class));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .delete(BASE_URL.concat("/".concat(USER_ID.toString())))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isNoContent())
                .andReturn();
    }
}
