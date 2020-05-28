package com.sec.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.sec.repo.SubscriptionRepository;
import com.sec.status.service.SubscriptionStatusService;
import com.sec.entity.Device;
import com.sec.entity.Sim;
import com.sec.entity.Subscription;
import com.sec.entity.switchTable.SubDev;
import com.sec.entity.switchTable.Service.SubDevService;
import com.sec.entity.viewEntity.SubscriptionToView;
import com.sec.enums.SubscriptionStatusEnum;

@Service
public class SubscriptionService extends ServiceWithMsg {
	
	private SubscriptionRepository subscriptionRepository;
	
	private SubscriptionStatusService subscriptionStatusService;
	
	private SubDevService subDevService;
	
	private SimService simService;

	@Autowired
	public void setSubscriptionRepository(SubscriptionRepository subscriptionRepository) {
		this.subscriptionRepository = subscriptionRepository;
	}
	
	@Autowired
	public void setSubscriptionStatusService(SubscriptionStatusService subscriptionStatusService) {
    this.subscriptionStatusService = subscriptionStatusService;
  }

	@Autowired
	public void setSubDevService(SubDevService subDevService) {
    this.subDevService = subDevService;
  }
	
	@Autowired
  public void setSimService(SimService simService) {
    this.simService = simService;
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
  
  public Subscription findByIdAndDate(long id, LocalDate date) {
    Subscription result = subscriptionRepository.findOne(id);
    result.setDevice(subDevService.findBySubIdAndDate(id, date).getDev());
    return result;
  }
  
  public void save(SubscriptionToView subscription, LocalDate date) {
    Subscription check = subscriptionRepository.findByNumber(subscription.getNumber());
    if(check == null) {
      Subscription s = new Subscription(subscription.getNumber());
      subscriptionRepository.save(s);
    } else {
      appendMsg("Number already exists");
    }
  }
  
  public void save(Subscription subscription) {
    System.out.println(subscription);
    if(subscription.getDevice() != null && subscription.getDevice().getSubscription() != null) {
      
    }
    if(subscription.getId() == 0) {
      //new subscription
      if(subscriptionRepository.findByNumber(subscription.getNumber()) == null) {
        subscriptionRepository.save(subscription);
        subDevService.save(subscription, subscription.getDevice(), subscription.getLast());
      } else {
        appendMsg("Number already exists");
      }
    } else {
      //update subscription
      subscriptionRepository.save(subscription);
      subDevService.save(subscription, subscription.getDevice(), subscription.getLast());
    }
    
  }
  
  private boolean equals(Subscription s1, Subscription s2) {
    //TODO write full check
    return s1.getNumber().equalsIgnoreCase(s2.getNumber());
  }
  
  public String removeMsg() {
    appendMsg(simService.removeMsg());
    return super.removeMsg();
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

  public List<LocalDate> findSubscriptionDatesById(long id) {
    Set<LocalDate> datesSet = new HashSet<>();
    datesSet.add(subscriptionRepository.findOne(id).getCreateDate());
    datesSet.addAll(subDevService.findAllDatesBySubId(id));
    return new LinkedList<>(datesSet);
  }

  public Subscription findByNumberAndDate(String number, LocalDate date) {
    // TODO Auto-generated method stub
    return null;
  }

  public Subscription add(Subscription entity) {
    if(isValidSubscriptionForAdd(entity)) {
      entity = subscriptionRepository.save(entity);
      return subscriptionRepository.findOne(entity.getId());
    }
    return null;
  }
  
  private boolean isValidSubscriptionForAdd(Subscription entity) {
    return true;
  }

  public boolean isValidSubscriptionForAdd(SubscriptionToView subscription) {
    boolean valid =
        !isNumberAlreadyExists(subscription.getNumber()) &&
        simService.isValid(subscription.getImei(), subscription.getCreateDate());
    return valid;
  }
  
  private boolean isNumberAlreadyExists(String number) {
    
    boolean exist = subscriptionRepository.findByNumber(number) != null;
    if(exist) {
      appendMsg(number + " already exists");
    }
    return exist;
  }
  
}
