package com.czp.repository;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.czp.entity.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

	Role findByRole(String role);
	
	List<Role> findAll();
	
}