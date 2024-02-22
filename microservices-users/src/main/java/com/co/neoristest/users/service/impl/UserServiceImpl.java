package com.co.neoristest.users.service.impl;

import com.co.neoristest.common.domain.enums.ExceptionMessage;
import com.co.neoristest.users.client.AccountFeignRequest;
import com.co.neoristest.users.domain.dto.UserDto;
import com.co.neoristest.users.domain.dto.UserExternalDto;
import com.co.neoristest.users.domain.dto.UserLoginResponseDto;
import com.co.neoristest.users.domain.dto.UserResponseDto;
import com.co.neoristest.users.domain.models.User;
import com.co.neoristest.users.domain.models.UserAccount;
import com.co.neoristest.users.exception.UserNotFoundException;
import com.co.neoristest.users.mapper.UserMapper;
import com.co.neoristest.users.repository.UserRepository;
import com.co.neoristest.users.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final AccountFeignRequest accountFeignRequest;


    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper,
                           AccountFeignRequest accountFeignRequest) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.accountFeignRequest = accountFeignRequest;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        log.info("Se realiza consulta de todos los usuarios!");
        return this.userRepository.findAll()
                .stream()
                .map(this.userMapper::userToUserResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserResponseDto> findUserById(Long id) {
        log.info("Se realiza consulta de usuario por id: {}", id);
        return Optional.ofNullable(this.userRepository.findById(id)
                .map(this.userMapper::userToUserResponseDto)
                .orElseThrow(() -> new UserNotFoundException(String.format(ExceptionMessage.USER_NOT_FOUND.getMessage(), id))));
    }

    @Override
    public Optional<UserExternalDto> findUserByIdFromMicroservicesAccount(Long id) {
        log.info("Se realiza consulta de usuario por id: {}", id);
        return Optional.ofNullable(this.userRepository.findById(id)
                .map(this.userMapper::userToUserExternalDto)
                .orElseThrow(() -> new UserNotFoundException(String.format(ExceptionMessage.USER_NOT_FOUND.getMessage(), id))));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserResponseDto> findUserAccountDetail(Long id) {
        log.info("Se realiza consulta del detaller del usuario con id: {}", id);
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format(ExceptionMessage.USER_NOT_FOUND.getMessage(), id)));
        if (!user.getUserAccounts().isEmpty()) {
            List<Long> ids = user.getUserAccounts()
                    .stream()
                    .map(UserAccount::getAccountId)
                    .toList();
            log.info("Se realiza consulta a microservicios cuentas, con los siguientes ids: {}", ids);
            user.setAccountDetails(this.accountFeignRequest.getAllAccoutDetail(ids));
        }

        return Optional.ofNullable(this.userMapper.userToUserResponseDto(user));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserLoginResponseDto> findUserByUsername(String username) {
        log.info("Se realiza consulta de usuario por username: {}", username);
        return Optional.ofNullable(this.userRepository.findByUsername(username)
                .map(this.userMapper::userToUserLoginResponseDto)
                .orElseThrow(() -> new UserNotFoundException(String.format(ExceptionMessage.USERNAME_NOT_FOUND.getMessage(), username))));
    }

    @Override
    @Transactional
    public UserResponseDto save(UserDto userDto) {
        User user = this.userMapper.userDtoToUser(userDto);
        if (Objects.isNull(user)) return null;
        user = this.userRepository.save(user);
        log.info("Se almacena usuario con id: {}", user.getId());
        return this.userMapper.userToUserResponseDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto update(UserDto userDto, Long id) {
        User user = this.userRepository.findById(id).
                orElseThrow(() -> new UserNotFoundException(String.format(ExceptionMessage.USER_NOT_FOUND.getMessage(),
                        id)));
        user = this.userRepository.save(this.userMapper.updateUserToUserDto(user, userDto));
        log.info("Se actualiza usuario con id: {}", user.getId());
        return this.userMapper.userToUserResponseDto(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = this.userRepository.findById(id).
                orElseThrow(() -> new UserNotFoundException(String.format(ExceptionMessage.USER_NOT_FOUND.getMessage(),
                        id)));
        this.userRepository.delete(user);
        log.info("Se elimina usuario con id: {}", id);
    }

    @Override
    @Transactional
    public void associateAccountWithUser(Long userId, Long accountId) {
        log.info("Se realiza consulta desde microservicio de cuentas, para asociar cuenta a usuario con id : {}", userId);
        UserAccount userAccount = null;
        User user = this.userRepository.findById(userId).
                orElseThrow(() -> new UserNotFoundException(String.format(ExceptionMessage.USER_NOT_FOUND.getMessage(),
                        userId)));
        userAccount = new UserAccount();
        userAccount.setAccountId(accountId);
        user.addAccountToUser(userAccount);
        this.userRepository.save(user);
        log.info("Se realiza asignacion de cuenta con id: {} a usuario con id : {}", accountId, userId);
    }

    @Override
    @Transactional
    public void deleteAccountUserFromMicroserviceAccount(Long userId, Long accountId) {
        log.info("Se inicia proceso de eliminar de cuenta con id: {}, desde microservicio de cuentas", accountId);
        User user = this.userRepository.findById(userId).
                orElseThrow(() -> new UserNotFoundException(String.format(ExceptionMessage.USER_NOT_FOUND.getMessage(),
                        userId)));
        user.getUserAccounts().forEach(userAccount -> {
            if (userAccount.getAccountId().equals(accountId)) {
                user.removeAccountToUser(userAccount);
            }
        });
        this.userRepository.save(user);
        log.info("Se elimina cuenta con id: {}, asociada al usuario con id: {}", accountId, userId);
    }
}
