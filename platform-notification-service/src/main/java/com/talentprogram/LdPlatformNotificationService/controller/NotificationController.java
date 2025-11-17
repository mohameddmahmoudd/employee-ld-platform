package com.talentprogram.LdPlatformNotificationService.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import com.talentprogram.LdPlatformNotificationService.dto.PagedResponse;
import com.talentprogram.LdPlatformNotificationService.service.NotificationService;
import com.talentprogram.LdPlatformNotificationService.util.Paging;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.talentprogram.LdPlatformNotificationService.dto.NotificationViewDTO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.talentprogram.LdPlatformNotificationService.security.JwtPrincipal;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/notifications")
public class NotificationController
{
    private final NotificationService notificationService;  

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public PagedResponse<NotificationViewDTO> getNotificationsList(
        @AuthenticationPrincipal JwtPrincipal me,
        @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        Page<NotificationViewDTO> page = notificationService.list(me.userId(), pageable);
        return Paging.from(page);
    }
    
    @PatchMapping("/{id}/read")
    @ResponseStatus(NO_CONTENT)
    public void markRead(
      @AuthenticationPrincipal JwtPrincipal me,
      @PathVariable Long id
    ){
        notificationService.markRead(me.userId(), id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(
      @AuthenticationPrincipal JwtPrincipal me,
      @PathVariable Long id
    ){
        notificationService.delete(me.userId(), id);
    }

  /**
   * Unread badge count.
  @GetMapping("/unread-count")
  public UnreadCount unreadCount(@AuthenticationPrincipal JwtPrincipal me) {
    return new UnreadCount(notificationService.unreadCount(me.userId()));
  }*/ 
    
}
