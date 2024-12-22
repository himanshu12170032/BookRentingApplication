package com.example.bookrent.Service;

import java.time.LocalDateTime;

public interface NotificationService {
    public void scheduleNotification(Long userId, String message, LocalDateTime scheduledTime);
    public void sendNotifications();
}
