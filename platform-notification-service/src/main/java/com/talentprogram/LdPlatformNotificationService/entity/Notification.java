package com.talentprogram.LdPlatformNotificationService.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Index;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import java.time.Instant;

@Entity
@Table(
  name = "notifications",
  indexes = {
    @Index(name = "ix_notif_rec_created", columnList = "recipient_user_id, created_at DESC")
  }
)
@NoArgsConstructor
public class Notification {

  public enum Type {
    INFO, SUCCESS, ERROR, SYSTEM
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(columnDefinition = "bigserial")
  @Getter
  private Long id;

 
  @Column(name = "recipient_user_id", nullable = false)
  @Getter @Setter
  private Long recipientUserId;

  @Column(name = "recipient_username")
  @Getter @Setter
  private String recipientUsername;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", length = 64, nullable = false)
  @Getter @Setter
  private Type type;

  @Column(name = "message", nullable = false, columnDefinition = "text")
  @Getter @Setter
  private String message;

  @Column(name = "is_read", nullable = false)
  @Getter @Setter
  private boolean read;

  @Column(name = "read_at", columnDefinition = "timestamptz")
  @Getter @Setter
  private Instant readAt;

  @CreationTimestamp
  @Column(name = "created_at", columnDefinition = "timestamptz")
  @Getter @Setter
  private Instant createdAt;

  public void markUnread() {
    this.read = false;
    this.readAt = null;
  }

}
