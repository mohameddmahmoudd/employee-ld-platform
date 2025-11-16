package com.talentprogram.LdPlatformUserService.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.talentprogram.LdPlatformUserService.config.JwtConfig;
import com.talentprogram.LdPlatformUserService.entity.Role;
import com.talentprogram.LdPlatformUserService.entity.User;

class JwtServiceTest {

    private JwtService jwtService;
    private User user;

    @BeforeEach
    void setUp() {
        JwtConfig cfg = new JwtConfig();
        cfg.setSecret("HrIiHp1aql+FNsQ+4KGTs4Co4ayc8tcEgXZpzRKvR51sDLRsGXbysLriPUlcEtIHc21kflfOlIHF2I+yw0OgdQ==");
        cfg.setExpiration(1_000_000L);
        cfg.setIssuer("user-service");
        jwtService = new JwtService(cfg);

        user = new User();
        ReflectionTestUtils.setField(user, "id", 7L);
        user.setUsername("mohamed");
        user.setRoles(Set.of(role("EMPLOYEE")));
    }

    @Test
    void generatedTokenRoundTripsClaims() throws Exception {
        String token = jwtService.generateToken(Map.of("roles", Set.of("EMPLOYEE")), user);

        assertThat(jwtService.extractUsername(token)).isEqualTo("mohamed");
        assertThat(jwtService.extractUserId(token)).isEqualTo(7L);
        assertThat(jwtService.isTokenValid(token, user)).isTrue();
    }

    @Test
    void tokenIsInvalidForDifferentUser() throws Exception {
        String token = jwtService.generateToken(Map.of(), user);

        User other = new User();
        ReflectionTestUtils.setField(other, "id", 9L);
        other.setUsername("other");

        assertThat(jwtService.isTokenValid(token, other)).isFalse();
    }

    private Role role(String name) {
        Role role = new Role();
        role.setName(name);
        return role;
    }
}
