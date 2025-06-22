package com.AccionesUD.AccionesUD.utilities.notification;

import com.AccionesUD.AccionesUD.domain.model.notification.Notification;
import com.AccionesUD.AccionesUD.dto.notification.NotificationRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotificationMapperTest {

    @Test
    void toNotification_MapsFieldsCorrectly() {
        NotificationRequest request = new NotificationRequest();
        request.type = "INFO";
        request.title = "Actualización";
        request.message = "Tu orden ha sido procesada.";

        String recipient = "user@example.com";

        NotificationMapper mapper = new NotificationMapper();
        Notification notification = mapper.toNotification(request, recipient);

        assertNotNull(notification);
        assertEquals("INFO", notification.getType());
        assertEquals("Actualización", notification.getTitle());
        assertEquals("Tu orden ha sido procesada.", notification.getMessage());
        assertEquals("user@example.com", notification.getRecipient());
    }
}
