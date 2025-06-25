package com.AccionesUD.AccionesUD.application.auth;

import java.util.Map;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.AccionesUD.AccionesUD.domain.model.notification.NotificationEvent;
import com.AccionesUD.AccionesUD.domain.model.notification.NotificationType;
import com.AccionesUD.AccionesUD.domain.model.User;
import com.AccionesUD.AccionesUD.domain.model.auth.LoginResult;
import com.AccionesUD.AccionesUD.domain.service.auth.LoginService;
import com.AccionesUD.AccionesUD.domain.service.auth.OtpService;
import com.AccionesUD.AccionesUD.domain.service.auth.UserRegistrationService;
import com.AccionesUD.AccionesUD.dto.auth.AuthResponse;
import com.AccionesUD.AccionesUD.dto.auth.LoginRequest;
import com.AccionesUD.AccionesUD.dto.auth.OtpRequest;
import com.AccionesUD.AccionesUD.dto.auth.RegisterRequest;
import com.AccionesUD.AccionesUD.utilities.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthApplicationService {
    
    private final LoginService loginService;
    private final OtpService otpService;
    private final UserRegistrationService registrationService;
    private final JwtService jwtService;
    private final ApplicationEventPublisher eventPublisher;

    public Object login(LoginRequest request) {
        LoginResult result = loginService.login(request.getUsername(), request.getPassword());

        if (!result.isOtpRequired()) {
            // ✅ Publicar evento de inicio de sesión
            eventPublisher.publishEvent(
                new NotificationEvent(
                    this,
                    request.getUsername(),
                    NotificationType.LOGIN,
                    "Inicio de sesión",
                    "Inicio de sesión exitoso"
                )
            );
            return AuthResponse.builder().token(result.getJwt()).build();
        }

        // ✅ Publicar evento de solicitud de OTP
        eventPublisher.publishEvent(
            new NotificationEvent(
                this,
                request.getUsername(),
                NotificationType.OTP,
                "Verificación OTP",
                "Se solicitó un código OTP para autenticación"
            )
        );

        return Map.of("otpRequired", true, "message", result.getOtpMessage());
    }

    public AuthResponse verifyOtp(OtpRequest request) {
        var result = otpService.verifyOtp(request.getUsername(), request.getOtp());

        // ✅ Publicar evento de verificación de OTP exitosa
        eventPublisher.publishEvent(
            new NotificationEvent(
                this,
                request.getUsername(),
                NotificationType.OTP,
                "OTP verificado",
                "Verificación OTP exitosa"
            )
        );

        return AuthResponse.builder().token(result.getJwt()).build();
    }

    public AuthResponse register(RegisterRequest request) {
        User user = registrationService.registerUser(request);
        String token = jwtService.getToken(user);

        // ✅ Publicar evento de registro exitoso
        eventPublisher.publishEvent(
            new NotificationEvent(
                this,
                user.getUsername(),
                NotificationType.REGISTER,
                "Registro exitoso",
                "Te has registrado correctamente en Acciones UD"
            )
        );

        return AuthResponse.builder().token(token).build();
    }
}
