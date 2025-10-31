package com.talentprogram.LdPlatformUserService.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.util.Set;

import java.time.Instant;

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
public class User 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    @Getter @Setter
    private String name;

    @Column(name="title")
    @Getter @Setter
    private String title;

    @Column(name = "password", nullable = false)
    @Getter @Setter
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
    name = "user_roles",
    joinColumns = @JoinColumn(name="user_id"),
    inverseJoinColumns = @JoinColumn(name="role_id")
    )
    @Getter
    private Set<Role> roles;
}
