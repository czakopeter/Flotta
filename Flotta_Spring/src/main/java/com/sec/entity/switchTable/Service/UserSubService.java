package com.sec.entity.switchTable.Service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sec.entity.Subscription;
import com.sec.entity.User;
import com.sec.entity.switchTable.UserSub;
import com.sec.entity.switchTable.Repo.UserSubRepository;
import com.sec.repo.UserRepository;

@Service
public class UserSubService {

  private UserSubRepository userSubRepository;
  
  @Autowired
  public void setUserSubRepository(UserSubRepository userSubRepository) {
    this.userSubRepository = userSubRepository;
  }

  public void save(Subscription sub, User user, LocalDate date) {
    UserSub entity = new UserSub(user, sub, date);
    userSubRepository.save(entity);
  }

  public void update(Subscription sub, User user, LocalDate date) {
    UserSub last = userSubRepository.findFirstBySubOrderByConnectDesc(sub);
    
    if(date.isAfter(last.getConnect())) {
      //add new user
      if((last.getUser() != null && user != null && last.getUser().getId() != user.getId()) ||
          (last.getUser() == null && user != null) ||
          (last.getUser() != null && user == null)) {
        userSubRepository.save(new UserSub(user, sub, date));
      }
    } else if(date.isEqual(last.getConnect())) {
      //modify last user
      UserSub lastBefore = userSubRepository.findFirstBySubAndConnectBeforeOrderByConnectDesc(sub, date);
      if(lastBefore != null && (
          (user == null && lastBefore.getUser() == null) ||
          (user != null && lastBefore.getUser() != null && user.getId() == lastBefore.getUser().getId())
          )) {
        userSubRepository.delete(last.getId());
      } else {
        last.setUser(user);
        userSubRepository.save(last);
      }
    } else {
      //error
    }
  }
  
}
