package com.AccionesUD.AccionesUD.controller.notification;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.AccionesUD.AccionesUD.application.NotificationApplicationService;
import com.AccionesUD.AccionesUD.domain.model.notification.Notification;
import com.AccionesUD.AccionesUD.dto.notification.NotificationRequest;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationApplicationService service;

    public NotificationController(NotificationApplicationService service) {
        this.service = service;
    }

    // controller/notification/NotificationController.java
    @PostMapping
    public ResponseEntity<Notification> sendNotification(@RequestBody NotificationRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Notification saved = service.processNotification(request, username);
        return ResponseEntity.ok(saved);
    }

   
        // controller/notification/NotificationController.java
    @GetMapping
    public ResponseEntity<List<Notification>> getUserNotifications(
        @RequestParam(required = false) String type,
        @RequestParam(required = false) String afterDate,
        @RequestParam(required = false) String keyword
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Notification> results = service.getUserNotifications(username, type, afterDate, keyword);
        return ResponseEntity.ok(results);
    }

    @PatchMapping("/{id}/read")
        public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        service.markAsRead(id);
        return ResponseEntity.ok().build();
}


}
