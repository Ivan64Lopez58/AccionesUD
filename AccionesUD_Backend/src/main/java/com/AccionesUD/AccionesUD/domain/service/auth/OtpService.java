package com.AccionesUD.AccionesUD.domain.service.auth;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.AccionesUD.AccionesUD.domain.model.User;
import com.AccionesUD.AccionesUD.domain.model.auth.OtpVerificationResult;
import com.AccionesUD.AccionesUD.dto.auth.AuthResponse;
import com.AccionesUD.AccionesUD.dto.auth.OtpRequest;
import com.AccionesUD.AccionesUD.repository.UserRepository;
import com.AccionesUD.AccionesUD.utilities.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OtpService {

    private static final long OTP_VALIDITY_MILLIS = 1 * 60 * 1000;
    private final Map<String, OtpEntry> otpStorage = new ConcurrentHashMap<>();

    private final JwtService jwtService;
    private final UserRepository userRepository;

    private record OtpEntry(String code, long timestamp) {}

    public void storeOtp(String username, String code) {
        otpStorage.put(username, new OtpEntry(code, System.currentTimeMillis()));
    }

    public AuthResponse verifyOtp(OtpRequest request) {
        OtpEntry entry = otpStorage.get(request.getUsername());

        if (entry == null || !entry.code().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        if (System.currentTimeMillis() - entry.timestamp() > OTP_VALIDITY_MILLIS) {
            otpStorage.remove(request.getUsername());
            throw new RuntimeException("OTP expired");
        }

        otpStorage.remove(request.getUsername());
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtService.getToken(user);

        return AuthResponse.builder().token(token).build();
    }


    public OtpVerificationResult verifyOtp(String username, String otp) {
        
        OtpEntry entry = otpStorage.get(username);

        if (entry == null || !entry.code().equals(otp)) {
            throw new RuntimeException("OTP inválido");
        }

        if (System.currentTimeMillis() - entry.timestamp() > OTP_VALIDITY_MILLIS) {
            otpStorage.remove(username);
            throw new RuntimeException("OTP expirado");
        }

        otpStorage.remove(username);
        User user = userRepository.findByUsername(username).orElseThrow();
        String token = jwtService.getToken(user);
        return new OtpVerificationResult(token, user);
    }

}
