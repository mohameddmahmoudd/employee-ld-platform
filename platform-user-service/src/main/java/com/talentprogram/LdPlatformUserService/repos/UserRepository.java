package com.talentprogram.LdPlatformUserService.repos;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.talentprogram.LdPlatformUserService.model.User;

public interface UserRepository extends JpaRepository<User, Long>
{
    Optional<User> findByName(String name);
    boolean existsByName(String name);
}
