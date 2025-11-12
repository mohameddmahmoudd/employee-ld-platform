package com.talentprogram.LdPlatformUserService.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.talentprogram.LdPlatformUserService.model.User;
import com.talentprogram.LdPlatformUserService.repos.RoleRepository;
import com.talentprogram.LdPlatformUserService.repos.UserRepository;

import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;
import com.talentprogram.LdPlatformUserService.model.Role;
import com.talentprogram.LdPlatformUserService.dto.UserUpdateInfoDTO;

import java.util.List;

@Service
public class UserService 
{
    private final UserRepository users;
    private final RoleRepository roles;

    public UserService(UserRepository users, RoleRepository roles) {
        this.users = users;
        this.roles = roles;
    }

    public Optional<User> getUserById(Long id) {
        return users.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return users.findByUsername(username);
    }

    @Transactional
    public Optional<User> updateUser(Long id, UserUpdateInfoDTO entity) {
        
        User user = users.findById(id).orElseThrow(() -> new EntityNotFoundException("user"));

        if (user != null) {
            user.setUsername(entity.username());
            user.setFullName(entity.fullName());
            user.setTitle(entity.title());
            users.save(user);
        }
        return Optional.of(user);
    }

    public void deleteUser(Long id) {
        users.deleteById(id);
    }

    @Transactional
    public List<String> updateUserRole(Long id, List<String> role) {
        User user = users.findById(id).orElse(null);
        if (user == null) return List.of();

        user.getRoles().clear();

        for (String roleName : role) {
            String normalized = roleName == null ? "" : roleName.trim().toUpperCase();
            Role roleEntity = roles.findByName(normalized)
                    .orElseThrow(() -> new IllegalArgumentException("Unknown role: " + roleName));
            user.getRoles().add(roleEntity);
        }

        users.save(user);
        return user.getRoles().stream().map(Role::getName).toList();
    }

    @Transactional
    public Optional<User> updateUserManager(Long id, Long managerId) {

        User user = users.findById(id).orElse(null);

        if (user != null) {
            user.setManager(users.findById(managerId).orElse(null));
            users.save(user);
        }
        else
        {
            throw new EntityNotFoundException("User not found");
        }

        return Optional.of(user);
    }

    public void updateUserPassword(Long id, String newPassword) {
        User user = users.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        user.setPassword(newPassword);
        users.save(user);
    }

    public List<String> getAllRoles() {
        return roles.findAll().stream().map(Role::getName).toList();
    }

}