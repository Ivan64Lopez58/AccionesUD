package com.AccionesUD.AccionesUD.dto.notification;

import com.AccionesUD.AccionesUD.domain.model.notification.NotificationType;
import lombok.Data;

@Data
public class NotificationResponse {
    private NotificationType type;
    private String title;
    private String message;
    private String createdAt;

    public NotificationResponse(NotificationType type, String title, String message, String createdAt) {
        this.type = type;
        this.title = title;
        this.message = message;
        this.createdAt = createdAt;
    }
}
