package com.talentprogram.LdPlatformNotificationService.repos;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.talentprogram.LdPlatformNotificationService.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.transaction.Transactional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long>
{
    Optional<Notification> findById(Long id);
    List<Notification> findByRecipientUserId(Long userId);
    Page<Notification> findByRecipientUserIdOrderByCreatedAtDesc(Long recipientUserId, Pageable pageable);
    boolean existsById(Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(
        """
        update Notification n
        set n.isRead = true,
        n.readAt = :readAt
        where n.id = :id
        and n.recipientUserId = :userId
        and n.isRead = false
        """
    )
    @Transactional
    int markRead(@Param("userId") Long userId,
        @Param("id") Long id,
        @Param("readAt") Instant readAt
    );

    @Transactional
    long deleteByIdAndRecipientUserId(Long id, Long recipientUserId);
            
}
