package com.sec.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.sec.repo.SubscriptionRepository;
import com.sec.entity.Sim;
import com.sec.entity.Subscription;
import com.sec.entity.User;
import com.sec.entity.viewEntity.SubscriptionToView;
import com.sec.enums.SimStatusEnum;

@Service
public class SubscriptionService {
	
  private Map<String, String> msg = new HashMap<String, String>();
  
	private SubscriptionRepository subscriptionRepository;

	@Autowired
	public void setSubscriptionRepository(SubscriptionRepository subscriptionRepository) {
		this.subscriptionRepository = subscriptionRepository;
	}
	
	public Subscription findByNumber(String number) {
		if(number != null) {
			return subscriptionRepository.findByNumber(number);
		}
		return null;
	}
	
	public List<Subscription> findAll() {
		return subscriptionRepository.findAll();
	}

  public Subscription findById(long id) {
    return subscriptionRepository.findOne(id);
  }

  public void save(SubscriptionToView subscription, LocalDate date) {
    Subscription check = subscriptionRepository.findByNumber(subscription.getNumber());
    if(check == null) {
      Subscription s = new Subscription(subscription.getNumber());
      subscriptionRepository.save(s);
    } else {
      addError("Number already exists");
    }
  }
  
  public String getError() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return msg.remove(auth.getName());
  }
  
  private void addError(String err) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    msg.put(auth.getName(), err);
  }

}
