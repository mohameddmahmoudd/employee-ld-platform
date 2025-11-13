package com.talentprogram.LdPlatformUserService.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.talentprogram.LdPlatformUserService.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    @EntityGraph(attributePaths = {"roles", "manager"})
    
    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
    
    boolean existsById(Long id);
}
