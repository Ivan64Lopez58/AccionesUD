package com.AccionesUD.AccionesUD.domain.service.auth;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import com.AccionesUD.AccionesUD.domain.model.auth.PasswordResetToken;

import com.AccionesUD.AccionesUD.repository.PasswordResetTokenRepository;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;



import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private PasswordResetTokenRepository tokenRepository;

    @InjectMocks
    private TokenService tokenService;

    

    @Test
    void generateToken_shouldSaveTokenAndReturnIt() {
        // Arrange
        String email = "test@example.com";

        // Act
        String token = tokenService.generateToken(email);

        // Assert
        assertNotNull(token);
        verify(tokenRepository).save(argThat(savedToken ->
            savedToken.getEmail().equals(email)
            && savedToken.getToken().equals(token)
            && !savedToken.isUsed()
        ));
    }


    @Test
    void markTokenAsUsed_shouldSetUsedAndSave() {
        String token = "some-token";
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .email("email@example.com")
                .expiration(LocalDateTime.now().plusMinutes(10))
                .used(false)
                .build();

        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(resetToken));

        tokenService.markTokenAsUsed(token);

        assertTrue(resetToken.isUsed());
        verify(tokenRepository).save(resetToken);
    }

    @Test
    void getEmailFromToken_shouldReturnEmail() {
        String token = "email-token";
        String email = "user@example.com";

        PasswordResetToken tokenEntity = PasswordResetToken.builder()
                .token(token)
                .email(email)
                .expiration(LocalDateTime.now().plusMinutes(10))
                .used(false)
                .build();

        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(tokenEntity));

        String resultEmail = tokenService.getEmailFromToken(token);

        assertEquals(email, resultEmail);
    }

    @Test
    void deleteTokensByEmail_shouldCallRepository() {
        // Arrange
        String email = "test@example.com";

        // Act
        tokenService.deleteTokensByEmail(email);

        // Assert
        verify(tokenRepository).deleteByEmail(email);
    }

    @Test
    void validateToken_shouldThrowException_whenTokenNotFound() {
        // Arrange
        String invalidToken = "invalid-token";
        when(tokenRepository.findByToken(invalidToken)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> tokenService.validateToken(invalidToken));
    }

    @Test
void getEmailFromToken_shouldThrowException_whenTokenNotFound() {
    String invalidToken = "not-exist";
    when(tokenRepository.findByToken(invalidToken)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> tokenService.getEmailFromToken(invalidToken));
}


@Test
void markTokenAsUsed_shouldThrowException_whenTokenNotFound() {
    String invalidToken = "no-token";
    when(tokenRepository.findByToken(invalidToken)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> tokenService.markTokenAsUsed(invalidToken));
}



@Test
void validateToken_shouldThrowException_whenTokenExpired() {
    String token = "expired-token";
    PasswordResetToken expiredToken = PasswordResetToken.builder()
            .token(token)
            .email("test@example.com")
            .expiration(LocalDateTime.now().minusMinutes(1)) // <- Expirado
            .used(false)
            .build();

    when(tokenRepository.findByToken(token)).thenReturn(Optional.of(expiredToken));

    assertThrows(RuntimeException.class, () -> tokenService.validateToken(token));
}

@Test
void validateToken_shouldThrowException_whenTokenUsedAndExpired() {
    String token = "used-and-expired-token";
    PasswordResetToken tokenObj = PasswordResetToken.builder()
            .token(token)
            .email("test@example.com")
            .expiration(LocalDateTime.now().minusMinutes(5)) // Expirado
            .used(true) // También usado
            .build();

    when(tokenRepository.findByToken(token)).thenReturn(Optional.of(tokenObj));

    assertThrows(RuntimeException.class, () -> tokenService.validateToken(token));
}


@Test
void validateToken_shouldThrowException_whenTokenIsUsedOrExpired() {
    String token = "used-or-expired-token";

    // Caso 1: token usado pero no expirado
    PasswordResetToken usedToken = PasswordResetToken.builder()
            .token(token)
            .email("test@example.com")
            .expiration(LocalDateTime.now().plusMinutes(10))
            .used(true)
            .build();
    when(tokenRepository.findByToken(token)).thenReturn(Optional.of(usedToken));
    assertThrows(RuntimeException.class, () -> tokenService.validateToken(token));

    // Caso 2: token no usado pero expirado
    PasswordResetToken expiredToken = PasswordResetToken.builder()
            .token(token)
            .email("test@example.com")
            .expiration(LocalDateTime.now().minusMinutes(10))
            .used(false)
            .build();
    when(tokenRepository.findByToken(token)).thenReturn(Optional.of(expiredToken));
    assertThrows(RuntimeException.class, () -> tokenService.validateToken(token));
}


}
