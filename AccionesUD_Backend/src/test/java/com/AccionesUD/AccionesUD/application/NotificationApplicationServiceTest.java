package com.AccionesUD.AccionesUD.application;

import com.AccionesUD.AccionesUD.domain.model.notification.Notification;
import com.AccionesUD.AccionesUD.domain.service.notification.NotificationService;
import com.AccionesUD.AccionesUD.dto.notification.NotificationRequest;
import com.AccionesUD.AccionesUD.utilities.notification.NotificationMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class NotificationApplicationServiceTest {

    private NotificationService domainService;
    private NotificationMapper mapper;
    private NotificationApplicationService applicationService;

    @BeforeEach
    void setUp() {
        domainService = mock(NotificationService.class);
        mapper = mock(NotificationMapper.class);
        applicationService = new NotificationApplicationService(domainService, mapper);
    }

    @Test
    void testProcessNotification() {
        // Arrange
        NotificationRequest request = new NotificationRequest();
        request.setType("INFO");
        request.setMessage("Test message");

        Notification mapped = new Notification();
        mapped.setType("INFO");
        mapped.setMessage("Test message");
        mapped.setRecipient("user1");

        Notification saved = new Notification();
        saved.setId(1L);
        saved.setType("INFO");
        saved.setMessage("Test message");
        saved.setRecipient("user1");

        when(mapper.toNotification(request, "user1")).thenReturn(mapped);
        when(domainService.save(mapped)).thenReturn(saved);

        // Act
        Notification result = applicationService.processNotification(request, "user1");

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("INFO", result.getType());
        verify(mapper).toNotification(request, "user1");
        verify(domainService).save(mapped);
    }

    @Test
    void testGetUserNotifications_ByType() {
        // Arrange
        List<Notification> expected = Arrays.asList(new Notification(), new Notification());
        when(domainService.getByRecipientAndType("user1", "ORDER")).thenReturn(expected);

        // Act
        List<Notification> result = applicationService.getUserNotifications("user1", "ORDER", null, null);

        // Assert
        assertEquals(2, result.size());
        verify(domainService).getByRecipientAndType("user1", "ORDER");
    }

    @Test
    void testGetUserNotifications_ByDate() {
        String afterDate = "2025-06-01T00:00:00";
        LocalDateTime parsedDate = LocalDateTime.parse(afterDate);
        List<Notification> expected = List.of(new Notification());

        when(domainService.getByRecipientAndDateAfter("user1", parsedDate)).thenReturn(expected);

        List<Notification> result = applicationService.getUserNotifications("user1", null, afterDate, null);

        assertEquals(1, result.size());
        verify(domainService).getByRecipientAndDateAfter("user1", parsedDate);
    }

    @Test
    void testGetUserNotifications_ByKeyword() {
        List<Notification> expected = List.of(new Notification());

        when(domainService.getByRecipientAndKeyword("user1", "orden")).thenReturn(expected);

        List<Notification> result = applicationService.getUserNotifications("user1", null, null, "orden");

        assertEquals(1, result.size());
        verify(domainService).getByRecipientAndKeyword("user1", "orden");
    }

    @Test
    void testGetUserNotifications_All() {
        List<Notification> expected = List.of(new Notification(), new Notification(), new Notification());

        when(domainService.getByRecipient("user1")).thenReturn(expected);

        List<Notification> result = applicationService.getUserNotifications("user1", null, null, null);

        assertEquals(3, result.size());
        verify(domainService).getByRecipient("user1");
    }

    @Test
    void testMarkAsRead() {
        applicationService.markAsRead(42L);
        verify(domainService).markAsRead(42L);
    }
}
