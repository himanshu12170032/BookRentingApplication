package com.example.bookrent.Service;

import com.example.bookrent.Dto.NotificationDto;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationService {
    public void scheduleNotification(Long userId, String message, LocalDateTime scheduledTime);
    public void sendNotifications();
    List<NotificationDto> getNotificationsForUser(Long userId);
}
