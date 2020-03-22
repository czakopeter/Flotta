package com.sec.entity.switchTable.Service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sec.entity.Device;
import com.sec.entity.Subscription;
import com.sec.entity.User;
import com.sec.entity.switchTable.SubDev;
import com.sec.entity.switchTable.UserSub;
import com.sec.entity.switchTable.Repo.SubDevRepository;
import com.sec.repo.UserRepository;

@Service
public class SubDevService {

  private SubDevRepository subDevRepository;
  
  @Autowired
  public void setUserSubRepository(SubDevRepository userSubRepository) {
    this.subDevRepository = userSubRepository;
  }

  public void save(Subscription sub, Device dev, LocalDate date) {
    SubDev entity = new SubDev(sub, dev, date);
    subDevRepository.save(entity);
  }

  public void update(Subscription sub, Device dev, LocalDate date) {
    SubDev last = subDevRepository.findFirstBySubOrderByConnectDesc(sub);
    
    if(date.isAfter(last.getConnect())) {
      if(last.getDev().getId() != dev.getId()) {
        subDevRepository.save(new SubDev(sub, dev, date));
      }
    } else if(date.isEqual(last.getConnect())) {
      //modifying
      if(dev.getId() != subDevRepository.findFirstBySubAndConnectBeforeOrderByConnectDesc(sub, date).getId()) {
        last.setDev(dev);
        subDevRepository.save(last);
      } else {
        subDevRepository.delete(last.getId());
      }
    } else {
      //error
    }
  }
  
}
