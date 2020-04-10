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

  public void updateFromSubscription(Subscription sub, Device dev, LocalDate date) {
    if (sub == null) {
      throw new NullPointerException();
    }
    
    SubDev lastFromSub = subDevRepository.findFirstBySubOrderByConnectDesc(sub);
    
    
    
    if(dev == null) {
      if(date.isAfter(lastFromSub.getConnect())) {
        if(lastFromSub.getDev() == null) {
          //nothing
        } else {
          subDevRepository.save(new SubDev(sub, null, date));
          subDevRepository.save(new SubDev(null, lastFromSub.getDev(), date));
        }
      } else if (date.isEqual(lastFromSub.getConnect())) {
        if(lastFromSub.getDev() == null) {
          //nothing
        } else {
          SubDev lastBeforeFromSub = subDevRepository.findFirstBySubAndConnectBeforeOrderByConnectDesc(sub, date);
          SubDev lastBeforeFromDev = subDevRepository.findFirstByDevAndConnectBeforeOrderByConnectDesc(dev, date);
          
          if(lastBeforeFromSub == null && lastBeforeFromDev == null) {
            subDevRepository.save(new SubDev(sub, null, date));
            lastFromSub.setSub(null);
            subDevRepository.save(lastFromSub);
          }
          if(lastBeforeFromSub == null && lastBeforeFromDev != null) {
            if(lastBeforeFromDev.getSub() == null) {
              lastFromSub.setDev(null);
              subDevRepository.save(lastFromSub);
            } else {
              subDevRepository.save(new SubDev(null, lastFromSub.getDev(), date));
              lastFromSub.setDev(null);
              subDevRepository.save(lastFromSub);
            }
          }
          if(lastBeforeFromSub != null && lastBeforeFromDev == null) {
            if(lastBeforeFromSub.getDev() == null) {
              lastFromSub.setSub(null);
              subDevRepository.save(lastFromSub);
            } else {
              subDevRepository.save(new SubDev(sub, null, date));
              lastFromSub.setSub(null);
              subDevRepository.save(lastFromSub);
            }
          }
          if(lastBeforeFromSub != null && lastBeforeFromDev != null) {
            subDevRepository.delete(lastFromSub.getId());
            if(lastBeforeFromSub.getDev() != null) {
              subDevRepository.save(new SubDev(sub, null, date));
            }
            if(lastBeforeFromDev.getSub() != null) {
              subDevRepository.save(new SubDev(null, lastFromSub.getDev(), date));
            }
          }
        }
      }
    }
  }

  public Subscription findLastSub(Device device) {
    return subDevRepository.findFirstByDevOrderByConnectDesc(device).getSub();
  }

}
