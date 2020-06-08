package com.sec.service;

import java.util.List;
import java.util.Random;

//import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sec.entity.Role;
import com.sec.entity.User;
import com.sec.repo.UserRepository;
import com.sec.validator.Validator;

@Service
public class UserService extends ServiceWithMsg implements UserDetailsService {
	
	private UserRepository userRepository;
	
	private EmailService emailService;
	
	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Autowired
	public void setEmailService(EmailService emailService) {
    this.emailService = emailService;
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
		
		userToRegister.setPassword("psw");
		userToRegister.setEnabled(true);
		userToRegister.setPasswordRenewerKey(generatePasswordRenewerKey());
		userRepository.save(userToRegister);
		
		emailService.sendMessage(userToRegister);

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
  
  public boolean changePassword(String password, String confirmPsw) {
    if(password != null && password.contentEquals(confirmPsw)) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      User user = userRepository.findByEmail(auth.getName());
      user.setPassword(password);
      userRepository.save(user);
      return true;
    } else {
      appendMsg("Confirm password is different");
      return false;
    }
  }

  public boolean registrationAvailable() {
    return userRepository.findAll().isEmpty();
  }
  
  public boolean firstUserRegistration(User user) {
    if(userRepository.findAll().isEmpty()) {
      user.addRoles("ADMIN");
      user.setEnabled(false);
      user.setPasswordRenewerKey(generatePasswordRenewerKey());
      user.setPassword("password");
      userRepository.save(user);
      emailService.sendMessage(user);
      return true;
    }
    return false;
  }

  public boolean isValidKeyOfUser(String key) {
    return userRepository.findByPasswordRenewerKey(key) != null;
  }

  public boolean verifyAndChangePassword(String key, String password, String confirmPassword) {
    User user = userRepository.findByPasswordRenewerKey(key);
    if(user == null) {
      appendMsg("Invalid key '" + key + "'!");
    } else if(Validator.equalsAndValidPassword(password, confirmPassword)) {
      user.setPasswordRenewerKey(null);
      user.setPassword(password);
      user.setEnabled(true);
      userRepository.save(user);
      return true;
    } else {
      appendMsg("Invalid password");
    }
    return false;
  }
}
