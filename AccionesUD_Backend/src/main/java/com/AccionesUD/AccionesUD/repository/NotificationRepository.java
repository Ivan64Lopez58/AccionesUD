package com.AccionesUD.AccionesUD.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.AccionesUD.AccionesUD.domain.model.notification.Notification;

import jakarta.transaction.Transactional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipient(String recipient);
    List<Notification> findByRecipientAndType(String recipient, String type);
    List<Notification> findByRecipientAndCreatedAtAfter(String recipient, LocalDateTime date);
    List<Notification> findByRecipientAndMessageContainingIgnoreCase(String recipient, String message);

    // NotificationRepository.java
    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.read = true WHERE n.id = :id")
    void markAsRead(@Param("id") Long id);

}
