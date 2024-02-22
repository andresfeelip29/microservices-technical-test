package com.co.neoristest.auth.services.impl;

import com.co.neoristest.auth.domain.dto.UserRequestDto;
import com.co.neoristest.auth.services.ExternalRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
public class UserServiceImpl implements UserDetailsService {

    private final ExternalRequestService externalRequestService;

    public UserServiceImpl(ExternalRequestService externalRequestService) {
        this.externalRequestService = externalRequestService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserRequestDto user = this.externalRequestService.findUserInMicroservicesUserByUsername(username);

        log.info("Usuario consultado con credenciales: {}", user);

        return new User(username, user.password(), true, true,
                true, true,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
