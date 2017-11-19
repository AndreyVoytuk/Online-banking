package com.userfront.service.UserServiceImpl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.userfront.dao.RoleDao;
import com.userfront.domain.security.Role;
import com.userfront.service.RoleService;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

	@Autowired
	RoleDao roleDao;

	@Override
	public Role findByName(String name) {
		return roleDao.findByName(name);
	}

}
