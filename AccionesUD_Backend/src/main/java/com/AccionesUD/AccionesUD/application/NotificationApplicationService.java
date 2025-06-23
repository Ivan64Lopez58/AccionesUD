package com.AccionesUD.AccionesUD.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.AccionesUD.AccionesUD.domain.model.notification.Notification;
import com.AccionesUD.AccionesUD.domain.service.notification.NotificationService;
import com.AccionesUD.AccionesUD.dto.notification.NotificationRequest;
import com.AccionesUD.AccionesUD.dto.notification.NotificationResponse;
import com.AccionesUD.AccionesUD.utilities.notification.NotificationMapper;
import com.AccionesUD.AccionesUD.utilities.TranslatorClient;

@Service
public class NotificationApplicationService {

    private final NotificationService domainService;
    private final NotificationMapper mapper;
    private final TranslatorClient translatorClient;
    private final Executor executor = Executors.newFixedThreadPool(8); // Ajusta según el CPU

    public NotificationApplicationService(NotificationService domainService, NotificationMapper mapper, TranslatorClient translatorClient) {
        this.domainService = domainService;
        this.mapper = mapper;
        this.translatorClient = translatorClient;
    }

    public Notification processNotification(NotificationRequest request, String username) {
        Notification notification = mapper.toNotification(request, username);
        return domainService.save(notification);
    }

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

    //  Método corregido con traducción concurrente
    public List<NotificationResponse> getNotificationsForUser(String username, String idioma) {
    List<Notification> notifications = domainService.getByRecipient(username);

    List<CompletableFuture<NotificationResponse>> futures = notifications.stream()
        .map(n -> CompletableFuture.supplyAsync(() -> new NotificationResponse(
            n.getType(),
            translatorClient.traducir(n.getTitle(), idioma),
            translatorClient.traducir(n.getMessage(), idioma),
            n.getCreatedAt().toString()
        ), executor))
        .collect(Collectors.toList());

    return futures.stream()
        .map(CompletableFuture::join)
        .collect(Collectors.toList());
    }

    public void markAsRead(Long id) {
        domainService.markAsRead(id);
    }
}
