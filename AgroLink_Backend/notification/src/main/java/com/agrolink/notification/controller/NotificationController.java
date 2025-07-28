package com.agrolink.notification.controller;

import com.agrolink.notification.dto.NotificationRequest;
import com.agrolink.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/send")
    public String send(@RequestBody NotificationRequest request) {
        return notificationService.sendNotification(request);
    }
}
