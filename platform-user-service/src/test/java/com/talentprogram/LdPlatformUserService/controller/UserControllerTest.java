package com.talentprogram.LdPlatformUserService.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.talentprogram.LdPlatformUserService.dto.PasswordUpdateRequestDTO;
import com.talentprogram.LdPlatformUserService.dto.UserUpdateInfoDTO;
import com.talentprogram.LdPlatformUserService.entity.Role;
import com.talentprogram.LdPlatformUserService.entity.User;
import com.talentprogram.LdPlatformUserService.service.UserService;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);
        user.setUsername("user");
        user.setFullName("Test User");
        user.setTitle("Engineer");
        user.setRoles(Set.of(role("EMPLOYEE")));
    }

    @Test
    void getUserByIdReturnsDto() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/id/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("user"));
    }

    @Test
    void getByUsernameReturnsDto() throws Exception {
        when(userService.getUserByUsername("user")).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/username/user"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.fullName").value("Test User"));
    }

    @Test
    void updateUserInfoReturnsUpdatedDto() throws Exception {
        UserUpdateInfoDTO info = new UserUpdateInfoDTO(1L, "user", "New", "Lead");
        User updated = new User();
        ReflectionTestUtils.setField(updated, "id", 1L);
        updated.setUsername("user");
        updated.setFullName("New");
        updated.setTitle("Lead");
        updated.setRoles(Set.of(role("EMPLOYEE")));
        when(userService.updateUser(eq(1L), any(UserUpdateInfoDTO.class))).thenReturn(Optional.of(updated));

        mockMvc.perform(MockMvcRequestBuilders.put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(info)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Lead"));
    }

    @Test
    void deleteUserReturns204() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    void updateUserRoleReturnsList() throws Exception {
        when(userService.updateUserRole(eq(1L), any())).thenReturn(List.of("ADMIN", "EMPLOYEE"));

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/1/role")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(List.of("ADMIN", "EMPLOYEE"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", containsInAnyOrder("ADMIN", "EMPLOYEE")));
    }

    @Test
    void updateUserManagerUsesRequestParam() throws Exception {
        when(userService.updateUserManager(1L, 2L)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/1/managerId").param("managerId", "2"))
            .andExpect(status().isNoContent());
    }

    @Test
    void updatePasswordWithParam() throws Exception {
        doNothing().when(userService).updateUserPassword(1L, "new");

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/1/updatePassword").param("newPassword", "new"))
            .andExpect(status().isNoContent());
    }

    @Test
    void updatePasswordWithBody() throws Exception {
        doNothing().when(userService).updateUserPassword(1L, "changed");
        PasswordUpdateRequestDTO body = new PasswordUpdateRequestDTO("changed");

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/1/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().isNoContent());
    }

    @Test
    void fetchDefaultRolesDelegatesToService() throws Exception {
        when(userService.getAllRoles()).thenReturn(List.of("EMPLOYEE", "MANAGER"));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/roles"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", containsInAnyOrder("EMPLOYEE", "MANAGER")));
    }

    private Role role(String name) {
        Role role = new Role();
        role.setName(name);
        return role;
    }
}
