package com.sec.entity.switchTable.Service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sec.entity.Subscription;
import com.sec.entity.User;
import com.sec.entity.switchTable.UserSub;
import com.sec.entity.switchTable.Repo.UserSubRepository;
import com.sec.service.SubscriptionService;

@Service
public class UserSubService {

  private UserSubRepository userSubRepository;
  
  private SubscriptionService subscriptionService;
  
  @Autowired
  public void setUserSubRepository(UserSubRepository userSubRepository) {
    this.userSubRepository = userSubRepository;
  }

  @Autowired
  public void setSubscriptionService(SubscriptionService subscriptionService) {
    this.subscriptionService = subscriptionService;
  }

  public void save(Subscription sub, User user, LocalDate date) {
    UserSub entity = new UserSub(user, sub, date);
    userSubRepository.save(entity);
    updateSubscriptionStatus(sub, user, date);
  }

  public void update(Subscription sub, User user, LocalDate date) {
    UserSub last = userSubRepository.findFirstBySubOrderByConnectDesc(sub);
    
    if(date.isAfter(last.getConnect())) {
      //add new user
      if((last.getUser() != null && user != null && last.getUser().getId() != user.getId()) ||
          (last.getUser() == null && user != null) ||
          (last.getUser() != null && user == null)) {
        userSubRepository.save(new UserSub(user, sub, date));
        updateSubscriptionStatus(sub, user, date);
      }
    } else if(date.isEqual(last.getConnect())) {
      //modify last user
      UserSub lastBefore = userSubRepository.findFirstBySubAndConnectBeforeOrderByConnectDesc(sub, date);
      if(lastBefore != null && (
          (user == null && lastBefore.getUser() == null) ||
          (user != null && lastBefore.getUser() != null && user.getId() == lastBefore.getUser().getId())
          )) {
        userSubRepository.delete(last.getId());
        subscriptionService.deleteLastSatus(sub);
      } else {
        last.setUser(user);
        userSubRepository.save(last);
        updateSubscriptionStatus(sub, user, date);
      }
    } else {
      //error
    }
  }
  
  private void updateSubscriptionStatus(Subscription sub, User user, LocalDate date) {
    if(user != null) {
      subscriptionService.userHasConnected(sub, date);
    } else {
      subscriptionService.userHasntConnected(sub, date);
    }
  }

  public List<LocalDate> findAllBeginDateBySubBetween(String number, LocalDate begin, LocalDate end) {
    Subscription sub = subscriptionService.findByNumber(number);
    List<UserSub> list = userSubRepository.findAllBySubAndConnectBetween(sub, begin, end);
    List<LocalDate> dates = new LinkedList<>();
    for(UserSub us : list) {
      dates.add(us.getConnect());
    }
    return dates;
  }

  public User getUser(String number, LocalDate begin, LocalDate end) {
    Subscription sub = subscriptionService.findByNumber(number);
    UserSub us = userSubRepository.findFirstBySubAndConnectBeforeOrderByConnectDesc(sub, end);
    return us.getUser();
  }
  
}
