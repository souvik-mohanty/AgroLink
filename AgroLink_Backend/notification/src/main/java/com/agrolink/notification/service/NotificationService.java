package com.agrolink.notification.service;

import com.agrolink.notification.dto.NotificationRequest;
import com.agrolink.notification.model.Notification;
import com.agrolink.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    private JavaMailSender mailSender;

    public String sendNotification(NotificationRequest request) {
        // Save to MongoDB (logging)
        Notification log = Notification.builder()
                .recipient(request.getRecipient())
                .type(request.getType())
                .subject(request.getSubject())
                .message(request.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        notificationRepository.save(log);

        // Handle type-specific sending
        return switch (request.getType().toUpperCase()) {
            case "EMAIL" -> sendEmail(request);
            case "SMS" -> sendSMS(request);
            case "PUSH" -> sendPush(request);
            default -> "Unknown notification type!";
        };
    }

    private String sendEmail(NotificationRequest request) {
        System.out.println("sending email");
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(request.getRecipient());
            message.setSubject(request.getSubject());
            message.setText(request.getMessage());
            mailSender.send(message);
            return "Email sent successfully.";

        } catch (Exception e) {
            return "Failed to send email: " + e.getMessage();
        }
    }

    private String sendSMS(NotificationRequest request) {
        // Integrate with SMS API like Twilio, MSG91, etc.
        // Placeholder
        System.out.println("SMS sent to " + request.getRecipient() + ": " + request.getMessage());
        return "SMS sent (mock).";
    }

    private String sendPush(NotificationRequest request) {
        // Push notification logic (Firebase, OneSignal, etc.)
        // Placeholder
        System.out.println("Push notification: " + request.getMessage());
        return "Push notification sent (mock).";
    }
}
