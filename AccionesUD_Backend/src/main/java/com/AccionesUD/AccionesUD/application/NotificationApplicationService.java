package com.AccionesUD.AccionesUD.application;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.AccionesUD.AccionesUD.domain.model.notification.Notification;
import com.AccionesUD.AccionesUD.domain.service.notification.NotificationService;
import com.AccionesUD.AccionesUD.dto.notification.NotificationRequest;
import com.AccionesUD.AccionesUD.utilities.notification.NotificationMapper;

@Service
public class NotificationApplicationService {

    private final NotificationService domainService;
    private final NotificationMapper mapper;

    public NotificationApplicationService(NotificationService domainService, NotificationMapper mapper) {
        this.domainService = domainService;
        this.mapper = mapper;
    }

    // application/NotificationApplicationService.java
    public Notification processNotification(NotificationRequest request, String username) {
        Notification notification = mapper.toNotification(request, username);
        return domainService.save(notification);
    }

    // application/NotificationApplicationService.java

    public List<Notification> getUserNotifications(String username, String type, String afterDate, String keyword) {
        if (type != null) {
            return domainService.getByRecipientAndType(username, type);
        } else if (afterDate != null) {
            LocalDateTime date = LocalDateTime.parse(afterDate); // yyyy-MM-ddTHH:mm:ss
            return domainService.getByRecipientAndDateAfter(username, date);
        } else if (keyword != null) {
            return domainService.getByRecipientAndKeyword(username, keyword);
        } else {
            return domainService.getByRecipient(username);
        }
    }

    public void markAsRead(Long id) {
        domainService.markAsRead(id);
    }
}
