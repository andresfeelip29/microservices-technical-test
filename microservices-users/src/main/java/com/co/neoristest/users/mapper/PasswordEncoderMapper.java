package com.co.neoristest.users.mapper;

import com.co.neoristest.users.mapper.anotations.EncodedMapping;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PasswordEncoderMapper {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @EncodedMapping
    public String encode(String value) {
        return bCryptPasswordEncoder.encode(value);
    }
}