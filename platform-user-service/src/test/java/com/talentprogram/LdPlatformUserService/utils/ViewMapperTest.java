package com.talentprogram.LdPlatformUserService.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.talentprogram.LdPlatformUserService.dto.UserDTO;
import com.talentprogram.LdPlatformUserService.entity.Role;
import com.talentprogram.LdPlatformUserService.entity.User;

class ViewMapperTest {

    @Test
    void toUserDTOExtractsFields() {
        User user = new User();
        ReflectionTestUtils.setField(user, "id", 5L);
        user.setUsername("user");
        user.setFullName("User Name");
        user.setTitle("Engineer");
        user.setRoles(Set.of(role("EMPLOYEE")));

        UserDTO dto = ViewMapper.toUserDTO(user);

        assertThat(dto.id()).isEqualTo(5L);
        assertThat(dto.roles()).containsExactly("EMPLOYEE");
    }

    private Role role(String name) {
        Role role = new Role();
        role.setName(name);
        return role;
    }
}
