package com.co.neoristest.users.mapper;

import com.co.neoristest.users.domain.AccountDetail;
import com.co.neoristest.users.domain.dto.UserDto;
import com.co.neoristest.users.domain.dto.UserResponseDto;
import com.co.neoristest.users.domain.models.User;
import com.co.neoristest.users.domain.models.UserAccount;
import com.co.neoristest.users.mapper.anotations.EncodedMapping;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = PasswordEncoderMapper.class)
public interface UserMapper {

    @Mapping(source = "password", target = "password", qualifiedBy = EncodedMapping.class)
    User userDtoToUser(UserDto userDto);

    @Mapping(source = "userAccounts" , target = "accounts", qualifiedByName = "accountListToListIds")
    @Mapping(source = "accountDetails" , target = "accountDetails", qualifiedByName = "accountList")
    UserResponseDto userToUserResponseDto(User user);

    @Mapping(source = "password", target = "password", qualifiedBy = EncodedMapping.class)
    User updateUserToUserDto(@MappingTarget User user, UserDto userDto);

    @Named("accountListToListIds")
    public static List<Long> convertListIds(List<UserAccount> userAccounts) {
        if (userAccounts != null) {
            return userAccounts
                    .stream()
                    .map(UserAccount::getAccountId)
                    .toList();
        }
        return Collections.emptyList();
    }

    @Named("accountList")
    public static List<AccountDetail> seListAccount(List<AccountDetail> accounts) {
        if (accounts == null) {
            return Collections.emptyList();
        }
        return accounts;
    }

}
