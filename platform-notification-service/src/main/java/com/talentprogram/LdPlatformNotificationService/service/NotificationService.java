package com.talentprogram.LdPlatformNotificationService.service;

import java.time.Clock;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.talentprogram.LdPlatformNotificationService.model.Notification;
import com.talentprogram.LdPlatformNotificationService.repos.NotificationRepository;
import com.talentprogram.LdPlatformNotificationService.util.NotificationMapper;

import org.springframework.stereotype.Service;
import com.talentprogram.LdPlatformNotificationService.dto.CreateNotificationDTO;
import com.talentprogram.LdPlatformNotificationService.dto.NotificationViewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class NotificationService {

    private final NotificationRepository notificationsRepository;
    private final Clock clock;

    public NotificationService(NotificationRepository notificationsRepository, Clock clock) {
        this.notificationsRepository = notificationsRepository;
        this.clock = clock;
    }

    public NotificationViewDTO create(CreateNotificationDTO req)
    {
        Long userId = req.recipientUserId();

        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Recipient user ID is required");
        }

        var now = Instant.now(clock);
        Notification notification = new Notification();
        notification.setRecipientUserId(userId);
        notification.setType(req.type());
        notification.setMessage(req.message());
        notification.setRead(false);
        notification.setCreatedAt(now);

        notification = notificationsRepository.save(notification);

        return NotificationMapper.toView(notification);

    }              
  
    public Page<NotificationViewDTO> list(Long userId, Pageable pageable)
    {
        return notificationsRepository.findByRecipientUserIdOrderByCreatedAtDesc(userId, pageable)
               .map(NotificationMapper::toView);
    }  

    public void markRead(Long userId, Long id) 
    {
        int updated = notificationsRepository.markRead(userId, id, Instant.now(clock));
        if (updated == 0) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not found");        
    }

    public void delete(Long userId, Long id)
    {
        long deleted = notificationsRepository.deleteByIdAndRecipientUserId(id, userId);
        if (deleted == 0) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not found");
    }

    /*long unreadCount(Long userId) */  
}
