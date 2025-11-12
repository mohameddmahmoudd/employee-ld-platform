package com.talentprogram.LdPlatformUserService.controller;

import org.springframework.web.bind.annotation.RestController;
import com.talentprogram.LdPlatformUserService.service.UserService;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.talentprogram.LdPlatformUserService.dto.UserDTO;
import com.talentprogram.LdPlatformUserService.dto.UserUpdateInfoDTO;
import com.talentprogram.LdPlatformUserService.model.User;

import java.util.Objects;
import java.util.Optional;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.talentprogram.LdPlatformUserService.utils.ViewMapper;
import java.util.List;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/users")
public class UserController
{
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/id/{id}")
    public UserDTO getUser(@PathVariable Long id) {
       return ViewMapper.toUserDTO(userService.getUserById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id)));

    }

    @GetMapping("/username/{username}")
    public UserDTO getUserByUsername(@PathVariable String username) {
        return ViewMapper.toUserDTO(Objects.requireNonNull(userService.getUserByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with username: " + username))));
    }

    @PutMapping("/{id}")
    public UserDTO updateUserInfo(@PathVariable Long id, @RequestBody UserUpdateInfoDTO entity) {
        Optional<User> updatedUser = userService.updateUser(id, entity);
        if (updatedUser.isPresent()) {
            User user = updatedUser.get();
            return ViewMapper.toUserDTO(user);
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PatchMapping("/{id}/role")
    public List<String> updateUserRole(@PathVariable Long id, @RequestParam String role) {
        return userService.updateUserRole(id, role);
    }

    @PatchMapping("/{id}/managerId")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public UserDTO updateUserManager(@PathVariable Long id, @RequestParam Long managerId)
    {
        return ViewMapper.toUserDTO(userService.updateUserManager(id, managerId).orElse(null));
    }

}