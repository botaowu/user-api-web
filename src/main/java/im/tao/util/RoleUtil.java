package im.tao.util;

import im.tao.entity.Role;

import java.util.List;

public class RoleUtil {

    public static boolean contains(List<Role> roles, Role role) {
        if (roles == null || role == null) {
            return false;
        }

        for (Role item : roles) {
            if (item.getName() != null && item.getName().equals(role.getName())) {
                return true;
            }
        }

        return false;
    }

}
