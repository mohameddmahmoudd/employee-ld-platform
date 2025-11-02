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
import java.time.Instant;
import java.util.Collection;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@NoArgsConstructor
public class User implements UserDetails
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    @Setter
    private String username;

    @Column(name = "full_name", nullable = false)
    @Getter @Setter
    private String fullName;

    @Column(name="title")
    @Getter @Setter
    private String title;

    @Column(name = "password", nullable = false)
    @Setter
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="manager_id")
    @Getter @Setter
    private User manager;       

    @Column(name = "learning_points")
    @Getter @Setter
    private Integer learningPoints;


    @Column(name="created_at", nullable=false)
    private Instant createdAt = Instant.now();

    @Column(name="isEnabled", nullable=false)
    @Getter @Setter
    private Boolean isEnabled = true;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
    name = "user_roles",
    joinColumns = @JoinColumn(name="user_id"),
    inverseJoinColumns = @JoinColumn(name="role_id")
    )
    @Getter @Setter
    private Set<Role> roles;

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
        return this.password;
    }

}
