package com.sec.service;

import java.util.HashSet;
import java.util.List;
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

import com.sec.entity.Role;
import com.sec.entity.User;
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
		userToRegister.setEnabled(false);
		userToRegister.setStatus(User.WAITING_FOR_VALIDATION);
		userToRegister.addRoles("USER");
		userToRegister.setPassword(generateKey(16));
		userToRegister.setPasswordRenewerKey(generateKey(16));
		if(emailService.sendMessage(userToRegister)) {
		  userRepository.save(userToRegister);
		} else {
		  return false;
		}
		return true;
	}

	public String generateKey(int length) {
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
  public boolean changePassword(String password, String confirmPsw) {
    if(password != null && password.contentEquals(confirmPsw)) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      User user = userRepository.findByEmail(auth.getName());
      user.setPassword(password);
      user.setPasswordRenewerKey(null);
      userRepository.save(user);
      return true;
    } else {
      appendMsg("Confirm password is different");
      return false;
    }
  }

  public boolean registrationAvailable() {
    for(User user : findAll()) {
      if(user.hasRole("ADMIN")) {
        return false;
      }
    }
    return true;
  }
  
  public boolean firstUserRegistration(User user) {
    if(userRepository.findAll().isEmpty()) {
      
      user.setEnabled(false);
      user.addRoles("ADMIN");
      user.setStatus(User.WAITING_FOR_VALIDATION);
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

  public boolean varification(String key) {
    User user = userRepository.findByPasswordRenewerKey(key);
    if(user != null) {
      user.setEnabled(true);
      user.setStatus(User.ENABLED);
      userRepository.save(user);
      return true;
    }
    return false;
  }

  public List<User> findAllByStatus(int status) {
    return userRepository.findAllByStatus(status);
  }

  public boolean update(long id, boolean[] roles, List<String> rolesName) {
    
    User user = userRepository.findOne(id);
    
    if(user != null) {
      System.out.println("nem null user");
      try {
        user.setRoles(toRoleSet(roles, rolesName));
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
  
  private Set<Role> toRoleSet(boolean[] roles, List<String> rolesName) throws IllegalArgumentException {
    Set<Role> result = new HashSet<>();
    if(roles.length != 4 || rolesName.size() != 4) {
      throw new IllegalArgumentException("Size of argument boolean array not exactly 4!");
    }
    for(int i = 0; i < roles.length; i++) {
      if(roles[i]) {
        Role r = roleRepository.findByRole(rolesName.get(i).toUpperCase());
        if(r == null) {
          throw new IllegalArgumentException("Role '" + rolesName.get(i) + "' doesn't exists!");
        }
        result.add(r);
      }
    }
    result.add(roleRepository.findByRole("BASIC"));
    
    return result;
  }

}
