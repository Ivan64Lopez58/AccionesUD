package com.AccionesUD.AccionesUD.controller.profile;

import com.AccionesUD.AccionesUD.application.profile.UserProfileApplicationService;
import com.AccionesUD.AccionesUD.dto.profile.UpdateUserProfileRequest;
import com.AccionesUD.AccionesUD.dto.profile.UserProfileResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@WebMvcTest(UserProfileController.class)
@ActiveProfiles("test")
class UserProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserProfileApplicationService appService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "usuario@correo.com", roles = {"USER"})
    void updateUserProfile_shouldReturnMessage() throws Exception {
        // Arrange
        UpdateUserProfileRequest dto = new UpdateUserProfileRequest();
        dto.setFirstname("NuevoNombre");
        dto.setLastname("Apellido");
        dto.setAddress("Calle 123");
        dto.setPhone(3100000000D);
        dto.setUsername("usuario@correo.com");

        // Act & Assert
        mockMvc.perform(put("/api/user/profile/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Perfil actualizado exitosamente"));

        // Verify
        verify(appService).updateUserProfile(dto);
    }

    @Test
    @WithMockUser(username = "usuario@correo.com", roles = {"USER"})
    void getMyProfile_shouldReturnProfile() throws Exception {
        // Arrange
        UserProfileResponse response = new UserProfileResponse();
        response.setFirstname("Juan");
        response.setLastname("Pérez");
        response.setUsername("usuario@correo.com");

        when(appService.getProfile("usuario@correo.com")).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/user/profile/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").value("Juan"))
                .andExpect(jsonPath("$.lastname").value("Pérez"))
                .andExpect(jsonPath("$.username").value("usuario@correo.com"));

        // Verify
        verify(appService).getProfile("usuario@correo.com");
    }
}