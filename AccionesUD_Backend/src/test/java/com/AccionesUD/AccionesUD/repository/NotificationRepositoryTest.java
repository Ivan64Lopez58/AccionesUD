
// === NotificationRepositoryTest ===
package com.AccionesUD.AccionesUD.repository;

import com.AccionesUD.AccionesUD.domain.model.notification.Notification;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    private Notification buildNotification(String recipient, String type, String message) {
        Notification notif = new Notification();
        notif.setRecipient(recipient);
        notif.setType(type);
        notif.setTitle("Test Title");
        notif.setMessage(message);
        notif.setCreatedAt(LocalDateTime.now());
        notif.setRead(false);
        return notif;
    }

    @Test
    void testFindByRecipient() {
        notificationRepository.save(buildNotification("user1", "INFO", "mensaje"));
        List<Notification> result = notificationRepository.findByRecipient("user1");
        assertFalse(result.isEmpty());
    }

    @Test
    void testFindByRecipientAndType() {
        notificationRepository.save(buildNotification("user1", "ALERTA", "mensaje urgente"));
        List<Notification> result = notificationRepository.findByRecipientAndType("user1", "ALERTA");
        assertEquals(1, result.size());
    }

    @Test
    void testFindByRecipientAndCreatedAtAfter() {
        LocalDateTime now = LocalDateTime.now();
        notificationRepository.save(buildNotification("user1", "INFO", "reciente"));
        List<Notification> result = notificationRepository.findByRecipientAndCreatedAtAfter("user1", now.minusMinutes(1));
        assertFalse(result.isEmpty());
    }

    @Test
    void testFindByRecipientAndMessageContainingIgnoreCase() {
        notificationRepository.save(buildNotification("user1", "INFO", "Fallo Crítico en el sistema"));
        List<Notification> result = notificationRepository.findByRecipientAndMessageContainingIgnoreCase("user1", "crítico");
        assertFalse(result.isEmpty());
    }

@PersistenceContext
private EntityManager entityManager;

@Test
void testMarkAsRead() {
    Notification notif = buildNotification("user1", "INFO", "No leída");
    notif = notificationRepository.save(notif);

    notificationRepository.markAsRead(notif.getId());

    // Forzar recarga desde BD
    entityManager.flush();
    entityManager.clear();

    Notification updated = notificationRepository.findById(notif.getId()).orElseThrow();
    assertTrue(updated.isRead());
}

}