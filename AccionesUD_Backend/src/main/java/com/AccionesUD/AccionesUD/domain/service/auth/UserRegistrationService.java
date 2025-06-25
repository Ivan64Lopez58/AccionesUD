package com.AccionesUD.AccionesUD.domain.service.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.AccionesUD.AccionesUD.domain.model.Role;
import com.AccionesUD.AccionesUD.domain.model.User;
import com.AccionesUD.AccionesUD.dto.auth.RegisterRequest;
import com.AccionesUD.AccionesUD.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("El correo electrónico ya está registrado.");
        }

        if (userRepository.existsById(request.getId())) {
            throw new RuntimeException("Ya existe un usuario con esta identificación.");
        }

        User user = User.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .firstname(request.getFirstname())
            .lastname(request.getLastname())
            .id(request.getId())
            .phone(request.getPhone())
            .address(request.getAddress())
            .role(Role.USER)
            .dailyOrderLimit(request.getDailyOrderLimit())
            .otpEnabled(request.isOtpEnabled())
            .build();

        return userRepository.save(user);
    }
}
