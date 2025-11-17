package com.talentprogram.LdPlatformNotificationService.dto;
import com.talentprogram.LdPlatformNotificationService.entity.Notification.Type;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateNotificationDTO(
    @NotNull Long recipientUserId,
    @NotNull Type type,
    @NotBlank @Size(max = 500) String message
)
{}

