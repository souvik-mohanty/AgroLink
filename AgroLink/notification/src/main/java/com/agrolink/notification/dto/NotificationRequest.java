package com.agrolink.notification.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
    private String recipient;
    private String type; // EMAIL, SMS, PUSH
    private String subject;
    private String message;
}
