package com.AccionesUD.AccionesUD.domain.service.auth;

import com.AccionesUD.AccionesUD.domain.model.User;
import com.AccionesUD.AccionesUD.domain.model.auth.OtpVerificationResult;
import com.AccionesUD.AccionesUD.dto.auth.AuthResponse;
import com.AccionesUD.AccionesUD.dto.auth.OtpRequest;
import com.AccionesUD.AccionesUD.repository.UserRepository;
import com.AccionesUD.AccionesUD.utilities.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OtpServiceTest {

    private JwtService jwtService;
    private UserRepository userRepository;
    private OtpService otpService;

    private final String username = "testuser";
    private final String validOtp = "123456";
    private final String invalidOtp = "000000";

    @BeforeEach
    void setUp() {
        jwtService = mock(JwtService.class);
        userRepository = mock(UserRepository.class);
        otpService = new OtpService(jwtService, userRepository);
    }

    @Test
    void testStoreOtpAndVerifyOtp_Success() {
        User mockUser = new User();
        mockUser.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        when(jwtService.getToken(mockUser)).thenReturn("mocked-jwt");

        otpService.storeOtp(username, validOtp);

        OtpRequest request = new OtpRequest();
        request.setUsername(username);
        request.setOtp(validOtp);

        AuthResponse response = otpService.verifyOtp(request);

        assertNotNull(response);
        assertEquals("mocked-jwt", response.getToken());
    }

    @Test
    void testVerifyOtp_InvalidOtp_ShouldThrowException() {
        otpService.storeOtp(username, validOtp);

        OtpRequest request = new OtpRequest();
        request.setUsername(username);
        request.setOtp(invalidOtp);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            otpService.verifyOtp(request);
        });

        assertEquals("Invalid OTP", ex.getMessage());
    }

    @Test
    void testVerifyOtp_ExpiredOtp_ShouldThrowException() throws InterruptedException {
        otpService.storeOtp(username, validOtp);

        // Simula expiración del OTP
        Thread.sleep(61 * 1000);

        OtpRequest request = new OtpRequest();
        request.setUsername(username);
        request.setOtp(validOtp);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            otpService.verifyOtp(request);
        });

        assertEquals("OTP expired", ex.getMessage());
    }

    @Test
    void testVerifyOtp_ReturnsTokenAndUser() {
        User mockUser = new User();
        mockUser.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        when(jwtService.getToken(mockUser)).thenReturn("mocked-token");

        otpService.storeOtp(username, validOtp);

        OtpVerificationResult result = otpService.verifyOtp(username, validOtp);

        assertNotNull(result);
        assertEquals("mocked-token", result.getJwt());
        assertEquals(mockUser, result.getUser());
    }

    @Test
    void testVerifyOtp_InvalidOtp_StringOverload() {
        otpService.storeOtp(username, validOtp);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            otpService.verifyOtp(username, invalidOtp);
        });

        assertEquals("OTP inválido", ex.getMessage());
    }

    @Test
    void testVerifyOtp_ExpiredOtp_StringOverload() throws InterruptedException {
        otpService.storeOtp(username, validOtp);

        Thread.sleep(61 * 1000);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            otpService.verifyOtp(username, validOtp);
        });

        assertEquals("OTP expirado", ex.getMessage());
    }
}
