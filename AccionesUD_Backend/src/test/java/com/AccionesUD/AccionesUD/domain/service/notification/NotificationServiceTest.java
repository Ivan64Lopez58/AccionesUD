package com.AccionesUD.AccionesUD.domain.service.notification;

import com.AccionesUD.AccionesUD.domain.model.notification.Notification;
import com.AccionesUD.AccionesUD.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    private NotificationRepository repository;
    private NotificationService service;

    @BeforeEach
    void setUp() {
        repository = mock(NotificationRepository.class);
        service = new NotificationService(repository);
    }

    @Test
    void save_SetsCreatedAtAndReturnsSavedNotification() {
        Notification notification = new Notification();
        Notification saved = new Notification();
        when(repository.save(any(Notification.class))).thenReturn(saved);

        Notification result = service.save(notification);

        assertNotNull(notification.getCreatedAt());
        assertEquals(saved, result);
        verify(repository, times(1)).save(notification);
    }

    @Test
    void getById_NotificationExists_ReturnsNotification() {
        Notification notification = new Notification();
        when(repository.findById(1L)).thenReturn(Optional.of(notification));

        Notification result = service.getById(1L);

        assertEquals(notification, result);
    }

    @Test
    void getById_NotificationNotFound_ThrowsException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.getById(1L));
        assertEquals("Notificación no encontrada", ex.getMessage());
    }

    @Test
    void markAsRead_CallsRepository() {
        service.markAsRead(5L);
        verify(repository, times(1)).markAsRead(5L);
    }

    @Test
    void getByRecipient_ReturnsList() {
        List<Notification> notifications = List.of(new Notification(), new Notification());
        when(repository.findByRecipient("juan")).thenReturn(notifications);

        List<Notification> result = service.getByRecipient("juan");

        assertEquals(2, result.size());
    }

    @Test
    void getByRecipientAndType_ReturnsList() {
        List<Notification> notifications = List.of(new Notification());
        when(repository.findByRecipientAndType("maria", "ALERTA")).thenReturn(notifications);

        List<Notification> result = service.getByRecipientAndType("maria", "ALERTA");

        assertEquals(1, result.size());
    }

    @Test
    void getByRecipientAndDateAfter_ReturnsList() {
        LocalDateTime date = LocalDateTime.now().minusDays(1);
        List<Notification> notifications = List.of(new Notification());
        when(repository.findByRecipientAndCreatedAtAfter("luis", date)).thenReturn(notifications);

        List<Notification> result = service.getByRecipientAndDateAfter("luis", date);

        assertEquals(1, result.size());
    }

    @Test
    void getByRecipientAndKeyword_ReturnsList() {
        List<Notification> notifications = List.of(new Notification(), new Notification());
        when(repository.findByRecipientAndMessageContainingIgnoreCase("ana", "urgente")).thenReturn(notifications);

        List<Notification> result = service.getByRecipientAndKeyword("ana", "urgente");

        assertEquals(2, result.size());
    }
}
