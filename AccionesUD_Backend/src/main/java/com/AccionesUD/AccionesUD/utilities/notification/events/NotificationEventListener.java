package com.AccionesUD.AccionesUD.utilities.notification.events;

import com.AccionesUD.AccionesUD.application.NotificationApplicationService;
import com.AccionesUD.AccionesUD.domain.model.notification.NotificationEvent;
import com.AccionesUD.AccionesUD.dto.notification.NotificationRequest;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventListener {

    private final NotificationApplicationService notificationService;

    public NotificationEventListener(NotificationApplicationService notificationService) {
        this.notificationService = notificationService;
    }

    @EventListener
    public void handleNotificationEvent(NotificationEvent event) {
        NotificationRequest request = new NotificationRequest(
            event.getType(),
            event.getTitle(),
            event.getMessage()
        );
        notificationService.processNotification(request, event.getUsername());
    }
}