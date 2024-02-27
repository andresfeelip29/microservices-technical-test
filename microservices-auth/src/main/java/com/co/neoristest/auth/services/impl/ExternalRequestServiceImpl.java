package com.co.neoristest.auth.services.impl;

import com.co.neoristest.auth.domain.dto.UserRequestDto;
import com.co.neoristest.auth.domain.dto.UserResponseDto;
import com.co.neoristest.auth.exception.ErrorSaveUserMicroservicesUsersException;
import com.co.neoristest.auth.exception.UserNotFoundInMicroservicesUserException;
import com.co.neoristest.auth.services.ExternalRequestService;
import com.co.neoristest.common.domain.enums.ExceptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import javax.security.auth.login.AccountNotFoundException;

@Slf4j
@Service
public class ExternalRequestServiceImpl implements ExternalRequestService {

    private final WebClient.Builder webClient;

    @Value("${microservices.users.url}")
    private String urlMicroserviceClient;

    public ExternalRequestServiceImpl(WebClient.Builder webClient) {
        this.webClient = webClient;

    }

    @Override
    public UserRequestDto findUserInMicroservicesUserByUsername(String username) {
        log.info("Se realiza solicitud de consulta a microservcios usuario con username: {}", username);
        return this.webClient.build()
                .get()
                .uri(urlMicroserviceClient.concat("/auth/login"), uriBuilder ->
                        uriBuilder.queryParam("username", username)
                                .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(UserRequestDto.class)
                .switchIfEmpty(Mono.error(new UserNotFoundInMicroservicesUserException(
                        String.format(ExceptionMessage.USERNAME_NOT_FOUND.getMessage(), username))))
                .block();
    }

    @Override
    public UserResponseDto saveUserToMicroservicesUsers(UserRequestDto userRequestDto) {
        log.info("Se realiza solicitud de guardado a microservcios usuario con data: {}", userRequestDto);
        return this.webClient.build()
                .post()
                .uri(urlMicroserviceClient.concat("/usuarios/"))
                .body(Mono.just(userRequestDto), UserRequestDto.class)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(UserResponseDto.class)
                .onErrorResume(WebClientResponseException.class, e -> e.getStatusCode().is4xxClientError() ? Mono.error(new ErrorSaveUserMicroservicesUsersException(
                        ExceptionMessage.ERROR_SAVE_USER_IN_MICROSERVICES_USERS.getMessage())) : Mono.empty())
                .switchIfEmpty(Mono.error(new UserNotFoundInMicroservicesUserException(ExceptionMessage.ERROR_SAVE_USER_IN_MICROSERVICES_USERS.getMessage())))
                .block();
    }
}
