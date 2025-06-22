package com.AccionesUD.AccionesUD.domain.service.auth;

import com.AccionesUD.AccionesUD.domain.model.User;
import com.AccionesUD.AccionesUD.domain.model.auth.LoginResult;
import com.AccionesUD.AccionesUD.repository.UserRepository;
import com.AccionesUD.AccionesUD.utilities.email.EmailService;
import com.AccionesUD.AccionesUD.utilities.security.JwtService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginServiceTest {

    private AuthenticationManager authManager;
    private UserRepository userRepository;
    private EmailService emailService;
    private OtpService otpService;
    private JwtService jwtService;
    private LoginService loginService;

    @BeforeEach
    void setup() {
        authManager = mock(AuthenticationManager.class);
        userRepository = mock(UserRepository.class);
        emailService = mock(EmailService.class);
        otpService = mock(OtpService.class);
        jwtService = mock(JwtService.class);

        loginService = new LoginService(authManager, userRepository, emailService, otpService, jwtService);
    }

    @Test
    void login_withOtpEnabled_shouldSendOtpAndReturnResult() {
        // Arrange
        String username = "user@test.com";
        String password = "password";
        User user = new User();
        user.setUsername(username);
        user.setOtpEnabled(true);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        LoginResult result = loginService.login(username, password);

        // Assert
        verify(authManager).authenticate(new UsernamePasswordAuthenticationToken(username, password));
        verify(otpService).storeOtp(eq(username), anyString());
        verify(emailService).sendOtpEmail(eq(username), anyString());

        assertTrue(result.isOtpRequired());
        assertEquals("OTP enviado al correo", result.getOtpMessage());
        assertNull(result.getJwt());
    }

    @Test
    void login_withOtpDisabled_shouldReturnJwt() {
        // Arrange
        String username = "user@test.com";
        String password = "password";
        String jwt = "mockJwt";

        User user = new User();
        user.setUsername(username);
        user.setOtpEnabled(false);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(jwtService.getToken(user)).thenReturn(jwt);

        // Act
        LoginResult result = loginService.login(username, password);

        // Assert
        verify(authManager).authenticate(new UsernamePasswordAuthenticationToken(username, password));
        verify(jwtService).getToken(user);
        assertFalse(result.isOtpRequired());
        assertEquals(jwt, result.getJwt());
    }

    @Test
    void login_userNotFound_shouldThrowException() {
        // Arrange
        String username = "notfound@test.com";
        String password = "password";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Assert
        assertThrows(RuntimeException.class, () -> loginService.login(username, password));
    }
}
