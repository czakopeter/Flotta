package com.sec.status.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sec.status.SimStatus;
import com.sec.status.repo.SimStatusRepository;
import com.sec.entity.Sim;
import com.sec.enums.SimStatusEnum;

@Service
public class SimStatusService {

  private SimStatusRepository simStatusRepository;

  @Autowired
  public void setSimStatusRepository(SimStatusRepository simStatusRepository) {
    this.simStatusRepository = simStatusRepository;
  }

  public SimStatus findById(long id) {
    return simStatusRepository.findOne(id);
  }

  public List<SimStatus> findAllBySim(Sim sim) {
    return simStatusRepository.findAllBySim(sim);
  }

  public boolean save(Sim sim, String status, LocalDate date) {
    try {
      simStatusRepository.save(new SimStatus(SimStatusEnum.valueOf(status), sim, date));
    } catch (IllegalArgumentException e) {
      return false;
    }
    return true;
  }
  
  public void deleteLastStatus(Sim sim) {
    SimStatus status = simStatusRepository.findFirstBySimOrderByBeginDesc(sim);
    if(status != null && !status.isFree()) {
      simStatusRepository.delete(status);
    }
  }
  
//  public void deleteAllStatus(Sim sim) {
//    simStatusRepository.deleteAllBySim(sim);
//  }

  public void modifyLastStatus(Sim sim, String imeiChangeReason) {
    SimStatus ss = simStatusRepository.findFirstBySimOrderByBeginDesc(sim);
    ss.setStatus(SimStatusEnum.valueOf(imeiChangeReason));
    simStatusRepository.save(ss);
  }
  
}
