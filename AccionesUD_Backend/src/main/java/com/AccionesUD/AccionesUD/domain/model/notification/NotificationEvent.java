package com.AccionesUD.AccionesUD.domain.model.notification;

import org.springframework.context.ApplicationEvent;

import lombok.Data;

@Data

public class NotificationEvent extends ApplicationEvent {
    private final String username;
    private final NotificationType type;
    private final String title;
    private final String message;

    public NotificationEvent(Object source, String username, NotificationType type, String title, String message) {
        super(source);
        this.username = username;
        this.type = type;
        this.title = title;
        this.message = message;
}
}
