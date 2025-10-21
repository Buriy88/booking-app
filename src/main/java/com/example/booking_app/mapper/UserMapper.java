package com.example.booking_app.mapper;

import com.example.booking_app.dto.security.UserRegistrationRequestDto;
import com.example.booking_app.dto.security.UserResponseDto;
import com.example.booking_app.model.Role;
import com.example.booking_app.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    default User toModel(UserRegistrationRequestDto dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setRole(Role.ROLE_CUSTOMER);
        return user;
    }

    UserResponseDto toDto(User user);
}
