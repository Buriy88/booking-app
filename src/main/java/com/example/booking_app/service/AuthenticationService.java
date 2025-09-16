package com.example.booking_app.service;

import com.example.booking_app.dto.UserLoginRequestDto;
import com.example.booking_app.dto.UserLoginResponseDto;
import com.example.booking_app.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager manager;

    private final JwtUtil jwtUtil;

    public UserLoginResponseDto login(UserLoginRequestDto request) {
        Authentication authentication = manager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String token = jwtUtil.generateToken(authentication.getName());
        return new UserLoginResponseDto(token);
    }
}
