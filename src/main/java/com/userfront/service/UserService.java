package com.userfront.service;

import java.util.Set;

import com.userfront.domain.User;
import com.userfront.domain.security.UserRole;

public interface UserService {

	void save(User user);

	User findByUsername(String username);

	User findByEmail(String email);

	boolean checkUserExists(String username, String email);

	boolean checkUsernameExists(String username);

	boolean checkEmailExists(String email);

	public User createUser(User user, Set<UserRole> userRoles);

	User saveUser(User user);
}
