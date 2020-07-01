package com.sec.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

//import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.sec.entity.Role;
import com.sec.entity.User;
import com.sec.enums.UserStatusEnum;
import com.sec.repo.RoleRepository;
import com.sec.repo.UserRepository;
import com.sec.validator.Validator;

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

	public boolean registerUser(User userToRegister) {
		User userCheck = userRepository.findByEmail(userToRegister.getEmail());

		if (userCheck != null) {
			return false;
		}
		userToRegister.setEnabled(false);
		userToRegister.setStatus(UserStatusEnum.WAITING_FOR_ACTIVATION);
		userToRegister.addRoles(roleRepository.findByRole("BASIC"));
		userToRegister.setPassword(generateKey(16));
		userToRegister.setPasswordRenewerKey(generateKey(16));
		if(emailService.sendMessage(userToRegister)) {
		  userRepository.save(userToRegister);
		} else {
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
	  return userId == 0 ? null : userRepository.findOne(userId);
	}

  public void modify(User user) {
    userRepository.save(user);
  }
  
  //az kötelező jelszó csere után a menű nem jelenik a szerepkörök szerint
  public boolean changePassword(String oldPsw, String newPsw, String confirmPsw) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    User user = userRepository.findByEmail(auth.getName());
    if(user.getPassword().equals(oldPsw) && newPsw != null && newPsw.contentEquals(confirmPsw)) {
      user.setPassword(newPsw);
      user.setPasswordRenewerKey(null);
      userRepository.save(user);
      emailService.sendEmailAboutPasswordChange(true);
      refreshAuthorization(new UserDetailsImpl(user));
      return true;
    } else {
      appendMsg("Probleb with the added data!");
      emailService.sendEmailAboutPasswordChange(false);
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
      if(emailService.sendMessage(user)) {
        userRepository.save(user);
      } else {
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

  public boolean modifyRoles(long id, Map<String, Boolean> roles) {
    User user = userRepository.findOne(id);
    if(user != null) {
      try {
        user.setRoles(toRoleSet(roles));
        userRepository.save(user);
        return true;
      } catch (IllegalArgumentException e) {
        appendMsg(e.getMessage());
      }
    } else {
      appendMsg("User doesn't exists!");
    }
    return false;
  }
  
  private Set<Role> toRoleSet(Map<String, Boolean> roles) {
    Set<Role> result = new HashSet<>();
    for(String key : roles.keySet()) {
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
      userRepository.save(user);
      emailService.sendMessage(user);
      return true;
    }
    return false;
  }
  
  private void refreshAuthorization(UserDetails user) {
    UsernamePasswordAuthenticationToken a = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(a);
  }

}
