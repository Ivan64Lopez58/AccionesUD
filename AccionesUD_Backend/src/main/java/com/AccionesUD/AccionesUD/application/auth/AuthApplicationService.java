package com.AccionesUD.AccionesUD.application.auth;


import java.util.Map;

import org.springframework.stereotype.Service;

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

    public Object login(LoginRequest request) {
        LoginResult result = loginService.login(request.getUsername(), request.getPassword());

        if (result.isOtpRequired()) {
            return Map.of("otpRequired", true, "message", result.getOtpMessage());
        }


        return AuthResponse.builder().token(result.getJwt()).build();
    }


    public AuthResponse verifyOtp(OtpRequest request) {
        var result = otpService.verifyOtp(request.getUsername(), request.getOtp());
        return AuthResponse.builder().token(result.getJwt()).build();
    }


    public AuthResponse register(RegisterRequest request) {
        User user = registrationService.registerUser(request);
        String token = jwtService.getToken(user);
        return AuthResponse.builder().token(token).build();
    }

}
