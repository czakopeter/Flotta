package com.sec.entity.switchTable.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sec.entity.Subscription;
import com.sec.entity.switchTable.SubSim;
import com.sec.entity.switchTable.Repo.SubSimRepository;

@Service
public class SubSimService {

  private SubSimRepository subSimRepository;

  @Autowired
  public void setSubSimRepository(SubSimRepository subSimRepository) {
    this.subSimRepository = subSimRepository;
  }
  
  public List<SubSim> findAllBySub(Subscription s) {
    return subSimRepository.findAllBySub(s);
  }
  
  public SubSim findBySubAndConnect(Subscription s, LocalDate date) {
    return subSimRepository.findBySubAndConnect(s, date);
  }
  
  public void delete(long id) {
    subSimRepository.delete(id);
  }
  
  public void delete(Subscription s, LocalDate date) {
    subSimRepository.deleteBySubAndDate(s, date);
  }

}
