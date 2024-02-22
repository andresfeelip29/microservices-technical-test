package com.co.neoristest.auth.services;

import com.co.neoristest.auth.domain.dto.UserRequestDto;

public interface ExternalRequestService {

     UserRequestDto findUserInMicroservicesUserByUsername(String username);
}
