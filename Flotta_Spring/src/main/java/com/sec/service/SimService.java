package com.sec.service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sec.entity.Sim;
import com.sec.entity.Subscription;
import com.sec.enums.SimStatusEnum;
import com.sec.repo.SimRepository;
import com.sec.status.service.SimStatusService;

@Service
public class SimService extends ServiceWithMsg {
	
	private SimRepository simRepository;
	
//	private SimStatusService simStatusService;
	
	@Autowired
	public void setSimRepository(SimRepository simRepository) {
    this.simRepository = simRepository;
  }
	
//	@Autowired
//  public void setSimStatusService(SimStatusService simStatusService) {
//    this.simStatusService = simStatusService;
//  }

  public List<Sim> findAll() {
    return simRepository.findAll(); 
  }
	
//	public List<Sim> findAllFree() {
//	  List<Sim> free = new LinkedList<>();
//	  for(Sim s : simRepository.findAll()) {
//	    if(s.isFree()) {
//	      free.add(s);
//	    }
//	  }
//		return free;
//	}

	public Sim findById(long id) {
		return simRepository.findOne(id);
	}
	
	public Sim findByImei(String imei) {
	  return simRepository.findByImei(imei);
	}

//  public void save(Sim sim, LocalDate date) {
//    Sim check = simRepository.findByImei(sim.getImei());
//    if(check == null) {
//      sim.addStatus(SimStatusEnum.FREE, date);
//      simRepository.save(sim);
//    }
//  }
	
	public void save(Subscription subscription, String imei, LocalDate date) {
	  if(simRepository.findByImei(imei) == null) {
	    Sim sim = new Sim();
	    sim.setImei(imei);
	    sim.setSubscription(subscription);
	    simRepository.save(sim);
	  }
	}
	
	public void save(Sim sim) {
	  simRepository.save(sim);
	}

  public List<Sim> findAllFree() {
    List<Sim> result = simRepository.findAllBySubscriptionIsNullAndReasonIsNull();
    return result;
  }

  public boolean isValid(String imei, LocalDate createDate) {
    if(simRepository.findByImei(imei) != null) {
      appendMsg(imei + " already exists");
      return false;
    }
    return true;
  }

  public Sim addSim(String imei, Subscription subscription, LocalDate createDate) {
    return simRepository.save(new Sim(imei, subscription));
  }

  public boolean add(Sim entity) {
    Sim sim = simRepository.findByImei(entity.getImei());
    if(sim == null) {
      simRepository.save(entity);
      return true;
    }
    appendMsg("Imie " + entity.getImei() + " already exists!");
    return false;
  }

//  public void removeLastStatusModification(long id) {
//    Sim sim = simRepository.findOne(id);
//    if(sim != null) {
//     simStatusService.deleteLastStatus(sim);
//    }
//  }

//  public void modifySimLastStatus(long simId, String imeiChangeReason) {
//    Sim sim = simRepository.findOne(simId);
//    if(sim != null && !sim.isFree()) {
//      simStatusService.modifyLastStatus(sim, imeiChangeReason);
//    }
//  }
}
