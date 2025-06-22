package com.AccionesUD.AccionesUD.application.auth;

import com.AccionesUD.AccionesUD.domain.service.auth.TokenService;
import com.AccionesUD.AccionesUD.domain.service.auth.UserService;
import com.AccionesUD.AccionesUD.dto.auth.PasswordResetRequestDTO;
import com.AccionesUD.AccionesUD.dto.auth.PasswordUpdateDTO;
import com.AccionesUD.AccionesUD.utilities.email.EmailService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordResetApplicationService {

private final TokenService tokenService;
private final UserService userService;
private final EmailService emailService;
@Transactional
public void requestReset(PasswordResetRequestDTO dto) {
    String email = dto.getEmail();
    userService.ensureUserExists(email);
    tokenService.deleteTokensByEmail(email);
    String token = tokenService.generateToken(email);
    String link = "http://localhost:4200/reset-password?token=" + token;
    emailService.sendEmail(email, "Recuperación de contraseña", "Da clic en el enlace: " + link);
}

public void validateToken(String token) {
    tokenService.validateToken(token);
}

public void updatePassword(PasswordUpdateDTO dto) {
    tokenService.validateToken(dto.getToken());
    String email = tokenService.getEmailFromToken(dto.getToken());
    userService.updatePassword(email, dto.getNewPassword());
    tokenService.markTokenAsUsed(dto.getToken());
}

}