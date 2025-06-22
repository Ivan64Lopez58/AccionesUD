package com.AccionesUD.AccionesUD.utilities.notification;

import org.springframework.stereotype.Component;

import com.AccionesUD.AccionesUD.domain.model.notification.Notification;
import com.AccionesUD.AccionesUD.dto.notification.NotificationRequest;

@Component
public class NotificationMapper {

    public Notification toNotification(NotificationRequest request, String recipient) {
        Notification notification = new Notification();
        notification.setType(request.type);
        notification.setTitle(request.title);
        notification.setMessage(request.message);
        notification.setRecipient(recipient);
        return notification;
    }
}
