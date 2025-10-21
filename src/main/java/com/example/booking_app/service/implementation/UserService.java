package com.example.booking_app.service.implementation;

import com.example.booking_app.dto.security.UserRegistrationRequestDto;
import com.example.booking_app.dto.security.UserResponseDto;
import com.example.booking_app.exception.RegistrationException;
import com.example.booking_app.mapper.UserMapper;
import com.example.booking_app.model.User;
import com.example.booking_app.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import static com.example.booking_app.model.Role.ROLE_CUSTOMER;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    public UserResponseDto registerUser(UserRegistrationRequestDto request)
        throws RegistrationException {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RegistrationException("User with this email exist");
        }

        User user = userMapper.toModel(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(ROLE_CUSTOMER);
        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }
}
