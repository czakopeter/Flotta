package com.sec.entity.switchTable.Service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sec.entity.Device;
import com.sec.entity.User;
import com.sec.entity.switchTable.UserDev;
import com.sec.entity.switchTable.Repo.UserDevRepository;

@Service
public class UserDevService {

  private UserDevRepository userDevRepository;
  
  @Autowired
  public void setUserDevRepository(UserDevRepository userDevRepository) {
    this.userDevRepository = userDevRepository;
  }

  public void save(Device dev, User user, LocalDate date) {
    UserDev entity = new UserDev(user, dev, date);
    userDevRepository.save(entity);
  }

  public void update(Device dev, User user, LocalDate date) {
    UserDev last = userDevRepository.findFirstByDevOrderByConnectDesc(dev);
    
    if(date.isAfter(last.getConnect())) {
      if(last.getUser().getId() != user.getId()) {
        userDevRepository.save(new UserDev(user, dev, date));
      }
    } else if(date.isEqual(last.getConnect())) {
      //modifying
      if(user.getId() != userDevRepository.findFirstByDevAndConnectBeforeOrderByConnectDesc(dev, date).getId()) {
        last.setUser(user);
        userDevRepository.save(last);
      } else {
        userDevRepository.delete(last.getId());
      }
    } else {
      //error
    }
  }
  
}