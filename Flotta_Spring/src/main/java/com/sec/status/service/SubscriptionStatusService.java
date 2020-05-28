package com.sec.status.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sec.status.SubscriptionStatus;
import com.sec.status.repo.SubscriptionStatusRepository;
import com.sec.entity.Subscription;
import com.sec.enums.SubscriptionStatusEnum;

@Service
public class SubscriptionStatusService {

  private SubscriptionStatusRepository subscriptionStatusRepository;

  @Autowired
  public void setSubscriptionStatusRepository(SubscriptionStatusRepository subscriptionStatusRepository) {
    this.subscriptionStatusRepository = subscriptionStatusRepository;
  }

  public SubscriptionStatus findById(long id) {
    return subscriptionStatusRepository.findOne(id);
  }
  
  public List<SubscriptionStatus> findAllBySub(Subscription sub) {
    return subscriptionStatusRepository.findAllBySubscription(sub);
  }

  public boolean save(Subscription sub, SubscriptionStatusEnum status, LocalDate date) {
    subscriptionStatusRepository.save(new SubscriptionStatus(status, sub, date));
    return true;
  }
  
  public void deleteLastStatus(Subscription sub) {
    long pcs = subscriptionStatusRepository.countBySubscription(sub);
    SubscriptionStatus status = subscriptionStatusRepository.findFirstBySubscriptionOrderByDateDesc(sub);
    if(pcs == 1) {
      status.setStatus(SubscriptionStatusEnum.FREE);
      subscriptionStatusRepository.save(status);
    } else if(pcs > 1) {
      subscriptionStatusRepository.delete(status.getId());
    }
  }

  public void setStatus(Subscription sub, SubscriptionStatusEnum status, LocalDate date) {
    long pcs = subscriptionStatusRepository.countBySubscription(sub);
    
    if(pcs == 0) {
      subscriptionStatusRepository.save(new SubscriptionStatus(status, sub, date));
    } else {
      SubscriptionStatus last = subscriptionStatusRepository.findFirstBySubscriptionOrderByDateDesc(sub);
      if(date.isAfter(last.getDate())) {
        //add new status
        if(!status.equals(last.getStatus())) {
          subscriptionStatusRepository.save(new SubscriptionStatus(status, sub, date));
        }
      } else if (date.isEqual(last.getDate())) {
        //modify last
        SubscriptionStatus lastBefore = subscriptionStatusRepository.findFirstBySubscriptionAndDateBeforeOrderByDateDesc(sub, date);
        if(lastBefore != null && lastBefore.getStatus().equals(status)) {
          subscriptionStatusRepository.delete(last.getId());
        } else {
          last.setStatus(status);
          subscriptionStatusRepository.save(last);
        }
      } else {
        //error
      }
    }
  }
}
