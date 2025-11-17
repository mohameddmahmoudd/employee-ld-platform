package com.talentprogram.LdPlatformNotificationService.dto;
import java.time.Instant;

import com.talentprogram.LdPlatformNotificationService.entity.Notification.Type;

public record NotificationViewDTO(
  Long id,
  Long recipientUserId,    
  Type type,
  String message,
  boolean isRead,
  Instant createdAt,
  Instant readAt
)
{}
