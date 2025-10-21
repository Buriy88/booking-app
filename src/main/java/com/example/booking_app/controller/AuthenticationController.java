package com.example.booking_app.controller;

import com.example.booking_app.dto.security.UserLoginRequestDto;
import com.example.booking_app.dto.security.UserLoginResponseDto;
import com.example.booking_app.dto.security.UserRegistrationRequestDto;
import com.example.booking_app.dto.security.UserResponseDto;
import com.example.booking_app.exception.RegistrationException;
import com.example.booking_app.service.implementation.AuthenticationService;
import com.example.booking_app.service.implementation.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Controller for user registration")
public class AuthenticationController {
    private final UserService userService;

    private final AuthenticationService authenticationService;

    @Operation(summary = "Register a new user",
        description = "Registers a new user if the email is not already in use")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User successfully registered"),
        @ApiResponse(responseCode = "400",
            description = "Email is already in use", content = @Content)
    })
    @PostMapping("/registration")
    public UserResponseDto register(
        @RequestBody @Valid UserRegistrationRequestDto request) throws RegistrationException {
        return userService.registerUser(request);
    }

    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful login"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto request) {
        return authenticationService.login(request);
    }

}
