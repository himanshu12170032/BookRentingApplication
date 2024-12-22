package com.example.bookrent.Service;

import com.example.bookrent.Converter.Converter;
import com.example.bookrent.Dto.NotificationDto;
import com.example.bookrent.Entity.Notification;
import com.example.bookrent.Exception.ResourceNotFoundException;
import com.example.bookrent.Repository.NotificationRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService{
    private final NotificationRepo notificationRepo;

    public NotificationServiceImpl(NotificationRepo notificationRepo) {
        this.notificationRepo = notificationRepo;
    }

    public void scheduleNotification(Long userId, String message, LocalDateTime scheduledTime) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setScheduledTime(scheduledTime);
        notification.setIsSent(false);
        notificationRepo.save(notification);
    }

    private NotificationDto convertToDto(Notification notification) {
        return new NotificationDto(
                notification.getId(),
                notification.getUserId(),
                notification.getMessage(),
                notification.getScheduledTime(),
                notification.getIsSent()
        );
    }


    @Override
    public List<NotificationDto> getNotificationsForUser(Long userId) {
        // Fetch all notifications for the user (both global and user-specific)
        List<Notification> notifications = notificationRepo.findByUserIdOrIsGlobalTrue(userId);

        if (notifications.isEmpty()) {
            throw new ResourceNotFoundException("No notifications found for this user");
        }

        // Convert to DTO list
        return notifications.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void sendNotifications() {
        List<Notification> notifications = notificationRepo.findByIsSentFalse();
        for (Notification notification : notifications) {
            if (notification.getScheduledTime().isBefore(LocalDateTime.now())) {
                // Send notification to users
                if (notification.getIsGlobal()) {
                    System.out.println("Global Notification sent: " + notification.getMessage());
                } else {
                    System.out.println("Notification sent to user " + notification.getUserId() + ": " + notification.getMessage());
                }
                notification.setIsSent(true);
                notificationRepo.save(notification);
            }
        }
    }
}
