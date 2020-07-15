package com.czp.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.czp.entity.Subscription;
import com.czp.entity.User;
import com.czp.entity.viewEntity.SubscriptionToView;
import com.czp.repository.SubscriptionRepository;
import com.czp.utility.Utility;

@Service
public class SubscriptionServiceOnlyInfo extends ServiceWithMsg {
	
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
