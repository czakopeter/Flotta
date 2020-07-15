package com.czp.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.czp.entity.Subscription;
import com.czp.entity.User;
import com.czp.entity.viewEntity.SubscriptionToView;
import com.czp.enums.SubscriptionStatusEnum;
import com.czp.repository.SubscriptionRepository;
import com.czp.status.service.SubscriptionStatusService;
import com.czp.utility.Utility;

@Service
public class SubscriptionService extends ServiceWithMsg {
	
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
  
  public boolean add(SubscriptionToView stv) {
    if(subscriptionRepository.findByNumber(stv.getNumber()) == null) {
      Subscription entity = new Subscription(stv.getNumber(), stv.getDate());
      subscriptionRepository.save(entity);
      return true;
    } else {
      appendMsg("Number already exists");
      return false;
    }
  }

  public void save(Subscription sub) {
    subscriptionRepository.save(sub);
  }

  public List<SubscriptionToView> findAllCurrentByUser(User user) {
    List<SubscriptionToView> result = new LinkedList<SubscriptionToView>();
    List<Subscription> all = subscriptionRepository.findAll();
    for(Subscription s : all) {
      if(Utility.isSameByIdOrBothNull(user, s.getActualUser())) {
        result.add(s.toView());
      }
    }
    return result;
  }

}
