package com.AccionesUD.AccionesUD.domain.service.auth;

import com.AccionesUD.AccionesUD.domain.model.User;
import com.AccionesUD.AccionesUD.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    private final String email = "test@example.com";
    private final String rawPassword = "newPassword123";
    private final String encodedPassword = "encodedPassword";

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    void ensureUserExists_UserExists_NoExceptionThrown() {
        User user = new User();
        when(userRepository.findByUsername(email)).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> userService.ensureUserExists(email));
        verify(userRepository, times(1)).findByUsername(email);
    }

    @Test
    void ensureUserExists_UserNotFound_ThrowsException() {
        when(userRepository.findByUsername(email)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userService.ensureUserExists(email);
        });

        assertEquals("Usuario no encontrado", ex.getMessage());
    }

    @Test
    void updatePassword_UserExists_UpdatesPasswordAndSaves() {
        User user = new User();
        user.setUsername(email);
        when(userRepository.findByUsername(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        userService.updatePassword(email, rawPassword);

        assertEquals(encodedPassword, user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void updatePassword_UserNotFound_ThrowsException() {
        when(userRepository.findByUsername(email)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userService.updatePassword(email, rawPassword);
        });

        assertEquals("Usuario no encontrado", ex.getMessage());
        verify(userRepository, never()).save(any());
    }
}
