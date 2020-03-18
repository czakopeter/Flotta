package com.sec.entity.switchTable.Service;

import java.time.LocalDate;

import com.sec.entity.Subscription;
import com.sec.entity.User;
import com.sec.entity.switchTable.UserSub;
import com.sec.entity.switchTable.Repo.UserSubRepository;

public class UserSubService {

  private UserSubRepository userSubRepository;
  
  public void save(Subscription sub, User user, LocalDate date) {
    UserSub entity = new UserSub(user, sub, date);
    userSubRepository.save(entity);
  }

  public void update(Subscription sub, User user, LocalDate date) {
    UserSub last = userSubRepository.findFirstBySubOrderByConnectDesc(sub);
    
    if(date.isAfter(last.getConnect())) {
      //new row
    } else if(date.isEqual(last.getConnect())) {
      //modifying
    } else {
      //error
    }
  }

}
