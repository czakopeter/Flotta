package com.czp.repository;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.czp.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {

	User findByEmail(String email);
	
	List<User> findAll();

  User findByPasswordRenewerKey(String key);

  List<User> findAllByStatus(int status);

  List<User> findAllByEnabled(boolean enabled);
	
}