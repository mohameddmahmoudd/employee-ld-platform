package com.talentprogram.LdPlatformUserService.repos;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.talentprogram.LdPlatformUserService.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Short> 
{
    Optional<Role> findByName(String name);
}
