package com.barclays.bankingchatbot.service.notification;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class NotificationService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Data
    public static class Notification {
        private String id;
        private String userId;
        private String title;
        private String message;
        private NotificationType type;
        private LocalDateTime timestamp;
        private boolean read;
        private String channel; // email, sms, push, in-app

        public Notification(String userId, String title, String message, NotificationType type, String channel) {
            this.id = java.util.UUID.randomUUID().toString();
            this.userId = userId;
            this.title = title;
            this.message = message;
            this.type = type;
            this.channel = channel;
            this.timestamp = LocalDateTime.now();
            this.read = false;
        }
    }

    public enum NotificationType {
        TRANSACTION_ALERT,
        APPOINTMENT_REMINDER,
        SECURITY_ALERT,
        PROMOTIONAL,
        SYSTEM_MAINTENANCE,
        ACCOUNT_UPDATE,
        LOAN_STATUS,
        PAYMENT_DUE
    }

    private final List<Notification> notifications = new ArrayList<>();

    @Async
    public CompletableFuture<Boolean> sendNotification(String userId, String title, 
                                                     String message, NotificationType type, 
                                                     String... channels) {
        try {
            for (String channel : channels) {
                Notification notification = new Notification(userId, title, message, type, channel);
                notifications.add(notification);

                switch (channel.toLowerCase()) {
                    case "email":
                        sendEmailNotification(userId, title, message);
                        break;
                    case "sms":
                        sendSMSNotification(userId, message);
                        break;
                    case "push":
                        sendPushNotification(userId, title, message);
                        break;
                    case "in-app":
                        // Already stored in notifications list
                        break;
                    default:
                        log.warn("Unknown notification channel: {}", channel);
                }

                log.info("Sent {} notification to user {}: {}", channel, userId, title);
            }
            return CompletableFuture.completedFuture(true);
        } catch (Exception e) {
            log.error("Failed to send notification to user {}: {}", userId, e.getMessage());
            return CompletableFuture.completedFuture(false);
        }
    }

    private void sendEmailNotification(String userId, String title, String message) {
        if (mailSender != null) {
            try {
                SimpleMailMessage email = new SimpleMailMessage();
                email.setTo(getUserEmail(userId));
                email.setSubject(title);
                email.setText(message);
                email.setFrom("noreply@bankingchatbot.com");
                
                mailSender.send(email);
                log.info("Email sent successfully to user: {}", userId);
            } catch (Exception e) {
                log.error("Failed to send email to user {}: {}", userId, e.getMessage());
            }
        }
    }

    private void sendSMSNotification(String userId, String message) {
        // Integration with SMS service (Twilio, AWS SNS, etc.)
        // For demo purposes, just log
        log.info("SMS notification would be sent to user {}: {}", userId, message);
    }

    private void sendPushNotification(String userId, String title, String message) {
        // Integration with push notification service (Firebase, etc.)
        // For demo purposes, just log
        log.info("Push notification would be sent to user {}: {} - {}", userId, title, message);
    }

    public List<Notification> getUserNotifications(String userId) {
        return notifications.stream()
                .filter(n -> n.getUserId().equals(userId))
                .sorted((n1, n2) -> n2.getTimestamp().compareTo(n1.getTimestamp()))
                .toList();
    }

    public List<Notification> getUnreadNotifications(String userId) {
        return notifications.stream()
                .filter(n -> n.getUserId().equals(userId) && !n.isRead())
                .sorted((n1, n2) -> n2.getTimestamp().compareTo(n1.getTimestamp()))
                .toList();
    }

    public void markAsRead(String notificationId) {
        notifications.stream()
                .filter(n -> n.getId().equals(notificationId))
                .findFirst()
                .ifPresent(n -> n.setRead(true));
    }

    public void markAllAsRead(String userId) {
        notifications.stream()
                .filter(n -> n.getUserId().equals(userId))
                .forEach(n -> n.setRead(true));
    }

    // Predefined notification templates
    public CompletableFuture<Boolean> sendTransactionAlert(String userId, String transactionDetails) {
        return sendNotification(userId, 
                "Transaction Alert", 
                "A transaction has been processed: " + transactionDetails,
                NotificationType.TRANSACTION_ALERT,
                "email", "in-app");
    }

    public CompletableFuture<Boolean> sendAppointmentReminder(String userId, String appointmentDetails) {
        return sendNotification(userId,
                "Appointment Reminder",
                "Don't forget your upcoming appointment: " + appointmentDetails,
                NotificationType.APPOINTMENT_REMINDER,
                "email", "sms", "in-app");
    }

    public CompletableFuture<Boolean> sendSecurityAlert(String userId, String alertDetails) {
        return sendNotification(userId,
                "Security Alert",
                "Important security notice: " + alertDetails,
                NotificationType.SECURITY_ALERT,
                "email", "sms", "push", "in-app");
    }

    public CompletableFuture<Boolean> sendLoanStatusUpdate(String userId, String status) {
        return sendNotification(userId,
                "Loan Application Update",
                "Your loan application status has been updated: " + status,
                NotificationType.LOAN_STATUS,
                "email", "in-app");
    }

    private String getUserEmail(String userId) {
        // In real implementation, fetch from user database
        return userId + "@example.com";
    }

    public long getUnreadCount(String userId) {
        return notifications.stream()
                .filter(n -> n.getUserId().equals(userId) && !n.isRead())
                .count();
    }

    public void cleanupOldNotifications() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(30);
        notifications.removeIf(n -> n.getTimestamp().isBefore(cutoff));
        log.info("Cleaned up notifications older than 30 days");
    }
}
