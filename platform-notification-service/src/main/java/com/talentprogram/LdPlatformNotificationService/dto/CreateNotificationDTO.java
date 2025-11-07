package com.talentprogram.LdPlatformNotificationService.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.talentprogram.LdPlatformNotificationService.model.Notification.Type;

public record CreateNotificationDTO(
    @NotNull Long recipientUserId,
    @NotNull Type type,
    @NotBlank @Size(max = 500) String message
)
{}

