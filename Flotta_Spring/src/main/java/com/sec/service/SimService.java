package com.sec.service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sec.entity.Sim;
import com.sec.enums.SimStatusEnum;
import com.sec.repo.SimRepository;
import com.sec.status.SimStatus;

@Service
public class SimService {
	
	private SimRepository simRepository;
	
	@Autowired
	public void setSimRepository(SimRepository simRepository) {
    this.simRepository = simRepository;
  }
	
	public List<Sim> findAll() {
    return simRepository.findAll(); 
  }
	
	public List<Sim> findAllFree() {
	  List<Sim> free = new LinkedList<>();
	  for(Sim s : simRepository.findAll()) {
	    if(s.isFree()) {
	      free.add(s);
	    }
	  }
		return free;
	}

	public Sim findById(long id) {
		return simRepository.findOne(id);
	}
	
	public Sim findByImei(String imei) {
    return simRepository.findByImei(imei);
	}

  public void addSim(Sim sim, LocalDate date) {
    Sim check = simRepository.findByImei(sim.getImei());
    if(check == null) {
      sim.addStatus(SimStatusEnum.FREE, date);
      simRepository.save(sim);
    }
  }
}
