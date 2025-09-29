package com.example.booking_app.dto.security;

import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;

    private String email;

    private String firstName;

    private String lastName;
}
