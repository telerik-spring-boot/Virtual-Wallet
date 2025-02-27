package com.telerik.virtualwallet.repositories.role;

import com.telerik.virtualwallet.models.Role;

public interface RoleRepository {

    Role getRoleByName(String name);

}
