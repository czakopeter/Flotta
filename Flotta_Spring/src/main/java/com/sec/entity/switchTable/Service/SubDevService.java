package com.sec.entity.switchTable.Service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sec.entity.Device;
import com.sec.entity.Subscription;
import com.sec.entity.switchTable.SubDev;
import com.sec.entity.switchTable.Repo.SubDevRepository;

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
      System.out.println("SubDevService: new mod date is after last mod date");
      if((last.getDev() != null && dev != null && last.getDev().getId() != dev.getId()) ||
          (last.getDev() == null && dev != null) ||
          (last.getDev() != null && dev == null)) {
        System.out.println("SubDevService: add new row");
        subDevRepository.save(new SubDev(null, last.getDev(),date));
        subDevRepository.save(new SubDev(sub, dev, date));
      }
    } else if(date.isEqual(last.getConnect())) {
      //modifying
      System.out.println("SubDevService: modifying");
      SubDev lastBefore = subDevRepository.findFirstBySubAndConnectBeforeOrderByConnectDesc(sub, date);
      if(lastBefore != null && (
          (dev == null && lastBefore.getDev() == null) ||
          (dev != null && lastBefore != null && dev.getId() == lastBefore.getDev().getId())
          )) {
        subDevRepository.delete(last.getId());
      } else {
        last.setDev(dev);
        subDevRepository.save(last);
      }
    } else {
      //error
    }
  }

  public Subscription findLastSub(Device device) {
    return subDevRepository.findFirstByDevOrderByConnectDesc(device).getSub();
  }
  
}
