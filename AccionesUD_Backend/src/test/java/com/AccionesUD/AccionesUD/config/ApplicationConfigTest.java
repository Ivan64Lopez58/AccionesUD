package com.AccionesUD.AccionesUD.config;

import com.AccionesUD.AccionesUD.domain.model.User;
import com.AccionesUD.AccionesUD.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApplicationConfigTest {

    private UserRepository userRepository;
    private ApplicationConfig applicationConfig;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        applicationConfig = new ApplicationConfig(userRepository);
    }

    @Test
    void userDetailService_shouldReturnUserWhenFound() {
        // Arrange
        String username = "testuser";
        User expectedUser = new User();
        expectedUser.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(expectedUser));

        // Act
        UserDetailsService userDetailsService = applicationConfig.userDetailService();
        var result = userDetailsService.loadUserByUsername(username);

        // Assert
        assertEquals(expectedUser, result);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void userDetailService_shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        String username = "nonexistent";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        UserDetailsService userDetailsService = applicationConfig.userDetailService();
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username));
        verify(userRepository, times(1)).findByUsername(username);
    }
}
