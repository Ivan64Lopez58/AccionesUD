package com.AccionesUD.AccionesUD.controller.auth;

import com.AccionesUD.AccionesUD.application.auth.AuthApplicationService;
import com.AccionesUD.AccionesUD.dto.auth.AuthResponse;
import com.AccionesUD.AccionesUD.dto.auth.LoginRequest;
import com.AccionesUD.AccionesUD.dto.auth.OtpRequest;
import com.AccionesUD.AccionesUD.dto.auth.RegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test") // evita cargar filtros/productivos si usas @Profile en tus beans
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthApplicationService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void loginTest() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("test@correo.com");
        request.setPassword("clave123");

        Mockito.when(authService.login(Mockito.any(LoginRequest.class)))
               .thenReturn("Código OTP enviado");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Código OTP enviado"));
    }

    @Test
    void verifyOtpTest() throws Exception {
        OtpRequest otpRequest = new OtpRequest();
        otpRequest.setUsername("test@correo.com");
        otpRequest.setOtp("123456");

        AuthResponse mockResponse = new AuthResponse();
        mockResponse.setToken("mockToken");

        Mockito.when(authService.verifyOtp(Mockito.any(OtpRequest.class)))
               .thenReturn(mockResponse);

        mockMvc.perform(post("/auth/verify-otp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(otpRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mockToken"));
    }

    @Test
    void registerTest() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setUsername("usuario@correo.com");
        req.setPassword("clave123");
        req.setFirstname("Dario");
        req.setLastname("Pérez");
        req.setId(12345678D);
        req.setPhone(3011234567D);
        req.setAddress("Calle 123");

        AuthResponse mockResponse = new AuthResponse();
        mockResponse.setToken("mockTokenDeRegistro");

        Mockito.when(authService.register(Mockito.any(RegisterRequest.class)))
               .thenReturn(mockResponse);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mockTokenDeRegistro"));
    }

    @Test
void register_shouldReturnBadRequestWhenExceptionThrown() throws Exception {
    RegisterRequest req = new RegisterRequest();
    req.setUsername("usuario@correo.com");
    req.setPassword("clave123");
    req.setFirstname("Dario");
    req.setLastname("Pérez");
    req.setId(12345678D);
    req.setPhone(3011234567D);
    req.setAddress("Calle 123");

    // Simula que el servicio lanza una excepción
    Mockito.when(authService.register(Mockito.any(RegisterRequest.class)))
           .thenThrow(new RuntimeException("Error al registrar"));

    mockMvc.perform(post("/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Error al registrar"));
}

}
