package com.AccionesUD.AccionesUD.domain.service.auth;

import java.util.Random;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.AccionesUD.AccionesUD.domain.model.User;
import com.AccionesUD.AccionesUD.domain.model.auth.LoginResult;
import com.AccionesUD.AccionesUD.repository.UserRepository;
import com.AccionesUD.AccionesUD.utilities.email.EmailService;
import com.AccionesUD.AccionesUD.utilities.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final OtpService otpService;
    private final JwtService jwtService;

    public LoginResult login(String username, String password) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password)
        );

    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    if (user.isOtpEnabled()) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpService.storeOtp(username, otp);
        emailService.sendOtpEmail(username, otp);
        return new LoginResult(true, "OTP enviado al correo", null, user);
    }

    String token = jwtService.getToken(user);
    return new LoginResult(false, null, token, user);
}
}