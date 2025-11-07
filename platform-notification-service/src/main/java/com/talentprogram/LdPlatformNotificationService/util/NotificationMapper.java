package com.talentprogram.LdPlatformNotificationService.util;
import com.talentprogram.LdPlatformNotificationService.model.Notification;
import com.talentprogram.LdPlatformNotificationService.dto.NotificationViewDTO;

public final class NotificationMapper {
  private NotificationMapper() {}

  public static NotificationViewDTO toView(Notification n) {
    return new NotificationViewDTO(
        n.getId(),
        n.getRecipientUserId(),
        n.getType(),
        n.getMessage(),
        n.isRead(),
        n.getCreatedAt(),
        n.getReadAt()
    );
  }
}
