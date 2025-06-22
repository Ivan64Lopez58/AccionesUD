package com.AccionesUD.AccionesUD.controller.auth;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.AccionesUD.AccionesUD.application.auth.PasswordResetApplicationService;
import com.AccionesUD.AccionesUD.dto.auth.PasswordResetRequestDTO;
import com.AccionesUD.AccionesUD.dto.auth.PasswordResetTokenDTO;
import com.AccionesUD.AccionesUD.dto.auth.PasswordUpdateDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@WebMvcTest(PasswordResetController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class PasswordResetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PasswordResetApplicationService appService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void requestReset_shouldReturnMessage() throws Exception {
        PasswordResetRequestDTO dto = new PasswordResetRequestDTO();
        dto.setEmail("test@example.com");

        mockMvc.perform(post("/auth/password/request")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Correo de recuperación enviado."));

        Mockito.verify(appService).requestReset(dto);
    }

    @Test
    void validateToken_shouldReturnMessage() throws Exception {
        PasswordResetTokenDTO dto = new PasswordResetTokenDTO();
        dto.setToken("abc123");

        mockMvc.perform(post("/auth/password/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Token válido."));

        Mockito.verify(appService).validateToken("abc123");
    }

    @Test
    void updatePassword_shouldReturnMessage() throws Exception {
        PasswordUpdateDTO dto = new PasswordUpdateDTO();
        dto.setToken("abc123");
        dto.setNewPassword("nuevaClave");

        mockMvc.perform(post("/auth/password/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Contraseña actualizada correctamente."));

        Mockito.verify(appService).updatePassword(dto);
    }
}
