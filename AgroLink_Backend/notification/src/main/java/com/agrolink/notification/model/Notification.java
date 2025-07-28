package com.agrolink.notification.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    @Id
    private String id;
    private String recipient;
    private String type; // EMAIL, SMS, PUSH
    private String subject;
    private String message;
    private LocalDateTime timestamp;
}
