package com.AccionesUD.AccionesUD.dto.notification;

import lombok.Data;

@Data
public class NotificationRequest {
    public String type;
    public String message;
    public String title;
}
