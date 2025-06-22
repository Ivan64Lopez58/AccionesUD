package com.AccionesUD.AccionesUD.domain.service.auth;

import com.AccionesUD.AccionesUD.domain.model.Role;
import com.AccionesUD.AccionesUD.domain.model.User;
import com.AccionesUD.AccionesUD.dto.auth.RegisterRequest;
import com.AccionesUD.AccionesUD.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserRegistrationServiceTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserRegistrationService registrationService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        registrationService = new UserRegistrationService(userRepository, passwordEncoder);
    }

    @Test
    void registerUser_Success() {
        RegisterRequest request = createValidRequest();

        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(userRepository.existsById(request.getId())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        User savedUser = User.builder()
                .id(request.getId())
                .username(request.getUsername())
                .password("encodedPassword")
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .phone(request.getPhone())
                .address(request.getAddress())
                .role(Role.USER)
                .dailyOrderLimit(request.getDailyOrderLimit())
                .otpEnabled(request.isOtpEnabled())
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = registrationService.registerUser(request);

        assertNotNull(result);
        assertEquals("encodedPassword", result.getPassword());
        assertEquals(request.getUsername(), result.getUsername());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_UsernameAlreadyExists_ThrowsException() {
        RegisterRequest request = createValidRequest();

        when(userRepository.existsByUsername(request.getUsername())).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            registrationService.registerUser(request);
        });

        assertEquals("El correo electrónico ya está registrado.", ex.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void registerUser_IdAlreadyExists_ThrowsException() {
        RegisterRequest request = createValidRequest();

        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(userRepository.existsById(request.getId())).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            registrationService.registerUser(request);
        });

        assertEquals("Ya existe un usuario con esta identificación.", ex.getMessage());
        verify(userRepository, never()).save(any());
    }

    private RegisterRequest createValidRequest() {
        RegisterRequest request = new RegisterRequest();
        request.setId(123456D);
        request.setUsername("test@example.com");
        request.setPassword("securePassword");
        request.setFirstname("Test");
        request.setLastname("User");
        request.setPhone(1234567890D);
        request.setAddress("Some Address");
        request.setDailyOrderLimit(5);
        request.setOtpEnabled(true);
        return request;
    }
}
