package com.talentprogram.LdPlatformUserService.utils;
import com.talentprogram.LdPlatformUserService.model.Role;
import com.talentprogram.LdPlatformUserService.model.User;
import com.talentprogram.LdPlatformUserService.dto.UserDTO;
import java.util.Set;

public class ViewMapper {
    public static UserDTO toUserDTO(User u) {
    Long mgrId = (u.getManager() == null) ? null : u.getManager().getId();
    Set<Role> roleNames = u.getRoles();
    return new UserDTO(u.getId(), u.getUsername(), u.getFullName(), u.getTitle(), mgrId, roleNames);
  }
}
