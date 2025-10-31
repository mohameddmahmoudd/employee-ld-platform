package com.talentprogram.LdPlatformUserService.repos;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.talentprogram.LdPlatformUserService.model.Role;

public interface RoleRepository extends JpaRepository<Role, Short> 
{
    Optional<Role> findByName(String name);
}
