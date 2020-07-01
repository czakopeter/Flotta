package com.sec.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.sec.repo.SubscriptionRepository;
import com.sec.status.service.SubscriptionStatusService;
import com.sec.entity.Subscription;
import com.sec.entity.User;
import com.sec.entity.viewEntity.SubscriptionToView;
import com.sec.enums.SubscriptionStatusEnum;

@Service
public class SubscriptionService extends ServiceWithMsg {
	
	private SubscriptionRepository subscriptionRepository;
	
	private SubscriptionStatusService subscriptionStatusService;

	@Autowired
	public void setSubscriptionRepository(SubscriptionRepository subscriptionRepository) {
		this.subscriptionRepository = subscriptionRepository;
	}
	
	@Autowired
	public void setSubscriptionStatusService(SubscriptionStatusService subscriptionStatusService) {
    this.subscriptionStatusService = subscriptionStatusService;
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
  
  public void userHasConnected(Subscription sub, LocalDate date) {
    subscriptionStatusService.setStatus(sub, SubscriptionStatusEnum.ACTIVE, date);
  }

  public void userHasntConnected(Subscription sub, LocalDate date) {
    subscriptionStatusService.setStatus(sub, SubscriptionStatusEnum.FREE, date);
  }

  public void deleteLastSatus(Subscription sub) {
    subscriptionStatusService.deleteLastStatus(sub);
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

  public List<SubscriptionToView> findAllActualByUser(User user) {
    List<SubscriptionToView> result = new LinkedList<SubscriptionToView>();
    List<Subscription> all = subscriptionRepository.findAll();
    for(Subscription s : all) {
      if(User.isSameByIdOrBothNull(user, s.getActualUser())) {
        result.add(s.toView());
      }
    }
    
    return result;
  }

}
