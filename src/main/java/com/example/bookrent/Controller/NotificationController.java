package com.example.bookrent.Controller;

import com.example.bookrent.Dto.NotificationDto;
import com.example.bookrent.Entity.Role;
import com.example.bookrent.Service.NotificationService;
import com.example.bookrent.Service.UserService;
import com.example.bookrent.Entity.User;
import com.example.bookrent.Exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    // Schedule notification: requires authentication and user ID check
    @PostMapping("/schedule")
    public ResponseEntity<String> scheduleNotification(@RequestHeader("Authorization") String jwt,
                                                       @RequestParam Long userId,
                                                       @RequestParam String message,
                                                       @RequestParam LocalDateTime scheduledTime) throws UserNotFoundException {

        User currentUser = userService.findUserByProfile(jwt);

        if (!currentUser.getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only schedule notifications for your own account.");
        }

        notificationService.scheduleNotification(userId, message, scheduledTime);
        return new ResponseEntity<>("Notification scheduled successfully", HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<NotificationDto>> getAllNotifications(@RequestHeader("Authorization") String jwt) throws UserNotFoundException {
        User currentUser = userService.findUserByProfile(jwt);
        List<NotificationDto> notifications = notificationService.getNotificationsForUser(currentUser.getId());
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendNotifications(@RequestHeader("Authorization") String jwt) throws UserNotFoundException {

        User currentUser = userService.findUserByProfile(jwt);

        if (!currentUser.getRole().equals(Role.ADMIN)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admins can send notifications.");
        }

        notificationService.sendNotifications();
        return new ResponseEntity<>("Notifications sent successfully", HttpStatus.OK);
    }
}
