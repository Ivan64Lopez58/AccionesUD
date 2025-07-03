package com.AccionesUD.AccionesUD.domain.service.auth;

import com.AccionesUD.AccionesUD.domain.model.auth.PasswordResetToken;
import com.AccionesUD.AccionesUD.repository.PasswordResetTokenRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final PasswordResetTokenRepository tokenRepository;

public void deleteTokensByEmail(String email) {
    tokenRepository.deleteByEmail(email);
}

public String generateToken(String email) {
    String token = UUID.randomUUID().toString();
    PasswordResetToken resetToken = PasswordResetToken.builder()
            .token(token)
            .email(email)
            .expiration(LocalDateTime.now().plusMinutes(15))
            .used(false)
            .build();
    tokenRepository.save(resetToken);
    return token;
}

public void validateToken(String token) {
    PasswordResetToken resetToken = tokenRepository.findByToken(token)
            .orElseThrow(() -> new RuntimeException("Token inválido"));

    if (resetToken.isUsed() || resetToken.getExpiration().isBefore(LocalDateTime.now())) {
        throw new RuntimeException("Token expirado o ya usado");
    }
}

public String getEmailFromToken(String token) {
    return tokenRepository.findByToken(token)
            .orElseThrow(() -> new RuntimeException("Token inválido"))
            .getEmail();
}

public void markTokenAsUsed(String token) {
    PasswordResetToken resetToken = tokenRepository.findByToken(token)
            .orElseThrow(() -> new RuntimeException("Token inválido"));
    resetToken.setUsed(true);
    tokenRepository.save(resetToken);
}

}