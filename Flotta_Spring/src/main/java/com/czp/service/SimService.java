package com.czp.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.czp.entity.Sim;
import com.czp.enums.SimStatusEnum;
import com.czp.repository.SimRepository;
import com.czp.status.service.SimStatusService;
import com.czp.utility.Validator;

@Service
public class SimService extends ServiceWithMsg {
  
  private static String[] changeReasons = {"CHANGED", "STOLE", "LOST"};
	
	private SimRepository simRepository;
	
	private SimStatusService simStatusService;
	
	@Autowired
	public void setSimRepository(SimRepository simRepository) {
    this.simRepository = simRepository;
  }
	
	@Autowired
  public void setSimStatusService(SimStatusService simStatusService) {
    this.simStatusService = simStatusService;
  }

  public List<Sim> findAll() {
    return simRepository.findAll(); 
  }
	
	public List<Sim> findAllFree() {
	  List<Sim> result = simRepository.findAllBySimSubIsNullAndReasonIsNull();
    return result;
	}

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
//
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

  //TODO check imei, pin, puk format
  public boolean add(Sim sim) {
//    if(Validator.isValidImieWithLuhnAlg(sim.getImei())) {
//      appendMsg("Imei " + sim.getImei() + " is not valid!");
//      return false;
//    }
    Sim check = simRepository.findByImei(sim.getImei());
    if(check == null) {
      sim.setStatus(SimStatusEnum.FREE);
      simRepository.save(sim);
      return true;
    } else {
      appendMsg("Imei " + sim.getImei() + " already exists!");
      return false;
    }
  }

  public List<String> getAllChagneReason() {
    return Arrays.asList(changeReasons);
  }
}
