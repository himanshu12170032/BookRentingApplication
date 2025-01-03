package com.example.bookrent.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {
    private Long id;

    private Long userId;
    private String message;
    private LocalDateTime scheduledTime;
    private Boolean isSent;
}
