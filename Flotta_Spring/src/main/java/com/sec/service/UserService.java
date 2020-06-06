package com.sec.service;

import java.util.List;
import java.util.Random;

//import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sec.entity.User;
import com.sec.repo.UserRepository;

@Service
public class UserService implements UserDetailsService {
	
	private UserRepository userRepository;
	
	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}

		return new UserDetailsImpl(user);
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public List<User> getUsers(String email) {
		return userRepository.findAll();
	}
	
	public List<User> findAll() {
		return userRepository.findAll();
	}

	public boolean registerUser(User userToRegister) {
		User userCheck = userRepository.findByEmail(userToRegister.getEmail());

		if (userCheck != null) {
			return false;
		}
		
		userToRegister.setPassword("defaultPassword");
		userToRegister.setEnabled(true);
		userToRegister.setPasswordRenewerKey(generatePasswordRenewerKey());
		userRepository.save(userToRegister);

		return true;
	}

	public String generatePasswordRenewerKey() {
		Random random = new Random();
		char[] key = new char[16]; 
		for (int i = 0; i < key.length; i++) {
			key[i] = (char) ('a' + random.nextInt(26));
		}
		return new String(key);
  }

	public User findById(long userId) {
	  return userId == 0 ? null : userRepository.findOne(userId);
	}

  public void modify(User user) {
    userRepository.save(user);
    
  }
}
