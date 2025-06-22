package com.AccionesUD.AccionesUD.controller.notification;

import com.AccionesUD.AccionesUD.application.NotificationApplicationService;
import com.AccionesUD.AccionesUD.domain.model.notification.Notification;
import com.AccionesUD.AccionesUD.dto.notification.NotificationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@WebMvcTest(NotificationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationApplicationService service;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setupSecurityContext() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("usuario@correo.com");

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);

        SecurityContextHolder.setContext(context);
    }

    @Test
    void sendNotification_shouldReturnNotification() throws Exception {
        NotificationRequest request = new NotificationRequest();
        request.setTitle("Alerta");
        request.setMessage("Mensaje de prueba");

        Notification mockNotification = new Notification();
        mockNotification.setId(1L);
        mockNotification.setTitle("Alerta");
        mockNotification.setMessage("Mensaje de prueba");
        mockNotification.setRecipient("usuario@correo.com");
        mockNotification.setCreatedAt(LocalDateTime.now());

        Mockito.when(service.processNotification(Mockito.any(), Mockito.anyString()))
               .thenReturn(mockNotification);

        mockMvc.perform(post("/api/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Alerta"))
                .andExpect(jsonPath("$.message").value("Mensaje de prueba"));
    }

    @Test
    void getUserNotifications_shouldReturnList() throws Exception {
        Notification notif = new Notification();
        notif.setId(1L);
        notif.setTitle("Notificación");
        notif.setMessage("Contenido");
        notif.setRecipient("usuario@correo.com");
        notif.setCreatedAt(LocalDateTime.now());

        List<Notification> mockList = Collections.singletonList(notif);

        Mockito.when(service.getUserNotifications(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.any()))
               .thenReturn(mockList);

        mockMvc.perform(get("/api/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void markAsRead_shouldReturnOk() throws Exception {
        mockMvc.perform(patch("/api/notifications/1/read"))
               .andExpect(status().isOk());

        Mockito.verify(service).markAsRead(1L);
    }
}
