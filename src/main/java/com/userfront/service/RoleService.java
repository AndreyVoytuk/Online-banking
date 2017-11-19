package com.userfront.service;

import com.userfront.domain.security.Role;

public interface RoleService {

	Role findByName(String name);
}
