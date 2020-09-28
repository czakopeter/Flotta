package com.czp.service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

//import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.czp.entity.Role;
import com.czp.entity.User;
import com.czp.enums.UserStatusEnum;
import com.czp.repository.RoleRepository;
import com.czp.repository.UserRepository;
import com.czp.utility.Validator;

@Service
public class UserService extends ServiceWithMsg implements UserDetailsService {
	
	private UserRepository userRepository;
	
	private EmailService emailService;
	
	private RoleRepository roleRepository;
	
	
	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Autowired
	public void setEmailService(EmailService emailService) {
    this.emailService = emailService;
  }
	
	@Autowired
  public void setRoleRepository(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  @Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		return new UserDetailsImpl(user);
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public List<User> findAll() {
		return userRepository.findAll();
	}

	public boolean registerUser(User user) {
		User userCheck = userRepository.findByEmail(user.getEmail());

		if (userCheck != null) {
		  appendMsg("Already exists!");
			return false;
		}
		user.setEnabled(false);
		user.setStatus(UserStatusEnum.WAITING_FOR_ACTIVATION);
		user.addRoles(roleRepository.findByRole("BASIC"));
		user.setPassword(generateKey(16));
		user.setPasswordRenewerKey(generateKey(16));
		if(emailService.sendMessage(user.getEmail(),
		    "Activation email",
		    emailService.createMessageText(EmailService.FAILED_PASSWORD_CHANGE, new String[] {user.getFullName(), user.getPasswordRenewerKey(), user.getPassword()}))) {
		  userRepository.save(user);
		} else {
		  appendMsg("Email send failed!");
		  return false;
		}
		return true;
	}

	private String generateKey(int length) {
		Random random = new Random();
		char[] key = new char[length]; 
		for (int i = 0; i < key.length; i++) {
			key[i] = (char) ('a' + random.nextInt(26));
		}
		return new String(key);
  }

	public User findById(long userId) {
	  return userId <= 0 ? null : userRepository.findOne(userId);
	}

  public void save(User user) {
    userRepository.save(user);
  }
  
  //TODO email küldést engedélyezni
  public boolean changePassword(String oldPsw, String newPsw, String confirmPsw) {
    User user = getActualUser();
    if(user.getPassword().equals(oldPsw) && Validator.validPassword(newPsw) && newPsw.contentEquals(confirmPsw)) {
      user.setPassword(newPsw);
      user.setPasswordRenewerKey(null);
      user.setStatus(UserStatusEnum.ENABLED);
      userRepository.save(user);
      emailService.sendMessage(user.getEmail(),
          "Password change",
          emailService.createMessageText(EmailService.SUCCESS_PASSWORD_CHANGE, new String[] {user.getFullName()}));
      return true;
    } else {
      appendMsg("Probleb with the added data!");
      emailService.sendMessage(user.getEmail(),
          "Password change",
          emailService.createMessageText(EmailService.FAILED_PASSWORD_CHANGE, new String[] {user.getFullName()}));
      return false;
    }
  }

  public boolean registrationAvailable() {
     return userRepository.findAllByEnabled(true).isEmpty();
  }
  

  public boolean firstUserRegistration(User user) {
    if(userRepository.findAll().isEmpty()) {
      user.setEnabled(false);
      user.addRoles(roleRepository.findByRole("ADMIN"));
      user.setStatus(UserStatusEnum.WAITING_FOR_ACTIVATION);
      user.setPassword(generateKey(16));
      user.setPasswordRenewerKey(generateKey(16));
      if(emailService.sendMessage(user.getEmail(), "Activation email", EmailService.ACTIVATION_AND_INITIAL_PASSWORD)) {
        userRepository.save(user);
      } else {
        appendMsg("Email send failed!");
        return false;
      }
    }
    return true;
  }

  public boolean activation(String key) {
    User user = userRepository.findByPasswordRenewerKey(key);
    if(user != null) {
      user.setEnabled(true);
      user.setStatus(UserStatusEnum.REQUIRED_PASSWORD_CHANGE);
      userRepository.save(user);
      return true;
    }
    return false;
  }

  public List<User> findAllByStatus(int status) {
    return userRepository.findAllByStatus(status);
  }

  public boolean updateUser(long id,  Map<String, Boolean> roles) {
    User user = userRepository.findOne(id);
    if(user != null) {
      user.setRoles(toRoleSet(roles));
      userRepository.save(user);
      return true;
    } else {
      appendMsg("User doesn't exists!");
    }
    return false;
  }
  
  private Set<Role> toRoleSet(Map<String, Boolean> roles) {
    Set<Role> result = new HashSet<>();
    for(String key : new LinkedList<>(roles.keySet())) {
      Role r = roleRepository.findByRole(key.toUpperCase());
      if(r != null) {
        result.add(r);
      }
    }
    result.add(roleRepository.findByRole("BASIC"));
    
    return result;
  }

  public boolean passwordReset(String email) {
    User user = userRepository.findByEmail(email);
    if(user != null) {
      user.setPassword(generateKey(16));
      user.setPasswordRenewerKey(generateKey(16));
      user.setStatus(UserStatusEnum.WAITING_FOR_ACTIVATION);
      if(emailService.sendMessage(user.getEmail(),
          "Activation email",
          emailService.createMessageText(EmailService.ACTIVATION_AND_INITIAL_PASSWORD, new String[] {user.getFullName(), user.getPasswordRenewerKey() , user.getPassword()}))) {
        userRepository.save(user);
      }
      return true;
    }
    return false;
  }
  
  private User getActualUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return userRepository.findByEmail(auth.getName());
  }
}
