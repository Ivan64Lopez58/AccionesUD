package com.AccionesUD.AccionesUD.application.auth;

import com.AccionesUD.AccionesUD.domain.model.User;
import com.AccionesUD.AccionesUD.domain.model.auth.LoginResult;
import com.AccionesUD.AccionesUD.domain.model.auth.OtpVerificationResult;
import com.AccionesUD.AccionesUD.domain.service.auth.LoginService;
import com.AccionesUD.AccionesUD.domain.service.auth.OtpService;
import com.AccionesUD.AccionesUD.domain.service.auth.UserRegistrationService;
import com.AccionesUD.AccionesUD.dto.auth.*;
import com.AccionesUD.AccionesUD.utilities.security.JwtService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthApplicationServiceTest {

    @Mock
    private LoginService loginService;

    @Mock
    private OtpService otpService;

    @Mock
    private UserRegistrationService registrationService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthApplicationService authApplicationService;

    private final String USERNAME = "user@example.com";
    private final String PASSWORD = "password";
    private final String OTP = "123456";
    private final String TOKEN = "jwt-token";

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUsername(USERNAME);
    }

    @Test
    void login_shouldReturnTokenIfOtpNotRequired() {
        LoginRequest request = new LoginRequest();
        request.setUsername(USERNAME);
        request.setPassword(PASSWORD);

        LoginResult result = new LoginResult(false, null, TOKEN, user);

        when(loginService.login(USERNAME, PASSWORD)).thenReturn(result);

        Object response = authApplicationService.login(request);

        assertTrue(response instanceof AuthResponse);
        assertEquals(TOKEN, ((AuthResponse) response).getToken());

        verify(loginService).login(USERNAME, PASSWORD);
    }

    @Test
    void login_shouldReturnOtpRequiredMessage() {
        LoginRequest request = new LoginRequest();
        request.setUsername(USERNAME);
        request.setPassword(PASSWORD);

        LoginResult result = new LoginResult(true, "OTP requerido", null, user);

        when(loginService.login(USERNAME, PASSWORD)).thenReturn(result);

        Object response = authApplicationService.login(request);

        assertTrue(response instanceof Map);
        Map<String, Object> map = (Map<String, Object>) response;
        assertTrue((Boolean) map.get("otpRequired"));
        assertEquals("OTP requerido", map.get("message"));

        verify(loginService).login(USERNAME, PASSWORD);
    }

    @Test
void verifyOtp_shouldReturnAuthResponse() {
    OtpRequest request = new OtpRequest();
    request.setUsername(USERNAME);
    request.setOtp(OTP);

    OtpVerificationResult otpResult = mock(OtpVerificationResult.class);
    when(otpResult.getJwt()).thenReturn(TOKEN);
    when(otpService.verifyOtp(USERNAME, OTP)).thenReturn(otpResult);

    AuthResponse response = authApplicationService.verifyOtp(request);

    assertNotNull(response);
    assertEquals(TOKEN, response.getToken());

    verify(otpService).verifyOtp(USERNAME, OTP);
}


    @Test
    void register_shouldReturnToken() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername(USERNAME);
        request.setPassword(PASSWORD);

        when(registrationService.registerUser(request)).thenReturn(user);
        when(jwtService.getToken(user)).thenReturn(TOKEN);

        AuthResponse response = authApplicationService.register(request);

        assertNotNull(response);
        assertEquals(TOKEN, response.getToken());

        verify(registrationService).registerUser(request);
        verify(jwtService).getToken(user);
    }
}
