package com.talentprogram.LdPlatformUserService.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Index;
import jakarta.persistence.ForeignKey;
import org.hibernate.annotations.CreationTimestamp;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;


@Entity
@Table(
  name = "users",
  uniqueConstraints = @UniqueConstraint(name = "uk_users_name", columnNames = "name"),
  indexes = {
    @Index(name = "ix_users_manager", columnList = "manager_id"),
    @Index(name = "ix_users_learning_pts", columnList = "learning_points DESC")
  }
)
@NoArgsConstructor
public class User implements UserDetails
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(columnDefinition = "bigserial")
  @Getter
  private Long id;
 
  @Column(name = "name", nullable = false, unique = true)
  @Setter
  private String username;
 
  @Column(name = "full_name", nullable = false)
  @Getter @Setter
  private String fullName;
 
  @Column(length = 100)
  @Getter @Setter
  private String title;
 
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "manager_id",
    foreignKey = @ForeignKey(name = "fk_users_manager"))
  @Getter @Setter
  private User manager;
 
  @OneToMany(mappedBy = "manager")
  private Set<User> directReports = new HashSet<>();
 
  @Column(length = 100, nullable = false)
  @Setter
  private String password;
 
  @Column(name = "learning_points")
  @Getter @Setter
  private Integer learningPoints = 0;
 
  @CreationTimestamp
  @Column(name = "created_at", nullable = false, columnDefinition = "timestamptz")
  @Getter
  private OffsetDateTime createdAt;
 
 
  @Column(name="isEnabled", nullable=false)
  @Getter @Setter
  private Boolean isEnabled = true;
 
  // user_roles join table (composite PK handled by the join)
  @ManyToMany
  @JoinTable(
    name = "user_roles",
    joinColumns = @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user_roles_user")),
    inverseJoinColumns = @JoinColumn(name = "role_id", foreignKey = @ForeignKey(name = "fk_user_roles_role")),
    uniqueConstraints = @UniqueConstraint(name = "pk_user_roles", columnNames = {"user_id","role_id"})
  )
  @Setter @Getter
  private Set<Role> roles;
 
 
  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof User)) return false;
    User user = (User) o;
    return Objects.equals(username, user.username);
  }
  @Override public int hashCode() { return Objects.hash(username); }
      // UserDetails interface methods
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> (GrantedAuthority) role::getName).toList();
    }
   
    @Override
    public String getUsername() {
        return this.username;  
    }
 
    @Override
    public boolean isAccountNonExpired() {
        return isEnabled;    
    }
 
    @Override
    public boolean isAccountNonLocked() {
        return isEnabled;
    }
 
    @Override
    public String getPassword() {
        return this.id.toString();
    }
}


