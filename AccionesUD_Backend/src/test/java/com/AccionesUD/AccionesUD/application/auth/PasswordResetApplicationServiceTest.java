package com.AccionesUD.AccionesUD.application.auth;

import com.AccionesUD.AccionesUD.domain.service.auth.TokenService;
import com.AccionesUD.AccionesUD.domain.service.auth.UserService;
import com.AccionesUD.AccionesUD.dto.auth.PasswordResetRequestDTO;
import com.AccionesUD.AccionesUD.dto.auth.PasswordUpdateDTO;
import com.AccionesUD.AccionesUD.utilities.email.EmailService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class PasswordResetApplicationServiceTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private UserService userService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private PasswordResetApplicationService service;

    private final String EMAIL = "usuario@correo.com";
    private final String TOKEN = "abc123";
    private final String LINK = "http://localhost:4200/reset-password?token=" + TOKEN;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void requestReset_shouldSendEmailWithToken() {
        PasswordResetRequestDTO dto = new PasswordResetRequestDTO();
        dto.setEmail(EMAIL);

        when(tokenService.generateToken(EMAIL)).thenReturn(TOKEN);

        service.requestReset(dto);

        verify(userService).ensureUserExists(EMAIL);
        verify(tokenService).deleteTokensByEmail(EMAIL);
        verify(tokenService).generateToken(EMAIL);
        verify(emailService).sendEmail(EMAIL, "Recuperación de contraseña", "Da clic en el enlace: " + LINK);
    }

    @Test
    void validateToken_shouldCallTokenService() {
        service.validateToken(TOKEN);

        verify(tokenService).validateToken(TOKEN);
    }

    @Test
    void updatePassword_shouldUpdateAndMarkTokenUsed() {
        String newPassword = "NewSecurePassword!";
        PasswordUpdateDTO dto = new PasswordUpdateDTO();
        dto.setToken(TOKEN);
        dto.setNewPassword(newPassword);

        when(tokenService.getEmailFromToken(TOKEN)).thenReturn(EMAIL);

        service.updatePassword(dto);

        verify(tokenService).validateToken(TOKEN);
        verify(tokenService).getEmailFromToken(TOKEN);
        verify(userService).updatePassword(EMAIL, newPassword);
        verify(tokenService).markTokenAsUsed(TOKEN);
    }
}
