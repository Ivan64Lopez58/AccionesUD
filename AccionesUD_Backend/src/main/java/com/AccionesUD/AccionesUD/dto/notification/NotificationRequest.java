package com.AccionesUD.AccionesUD.dto.notification;

import com.AccionesUD.AccionesUD.domain.model.notification.NotificationType;

import lombok.Data;

@Data
public class NotificationRequest {
    public NotificationType type;
    public String message;
    public String title;

    public NotificationRequest(NotificationType type, String title, String message) {
        this.type = type;
        this.title = title;
        this.message = message;
        
    }
}


