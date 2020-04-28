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

    if (equals(dev, lastFromSub.getDev())) {
      return;
    }

    closeDevice(dev, date);
    
    if (date.isAfter(lastFromSub.getConnect())) {
      if (lastFromSub.getDev() != null) {
        subDevRepository.save(new SubDev(null, lastFromSub.getDev(), date));
      }
      subDevRepository.save(new SubDev(sub, dev, date));
    } else if (date.isEqual(lastFromSub.getConnect())) {
      
    }

    if (dev == null) {
      if (date.isEqual(lastFromSub.getConnect())) {
        if (lastFromSub.getDev() == null) {
          // nothing
        } else {
          SubDev lastBeforeFromSub = subDevRepository.findFirstBySubAndConnectBeforeOrderByConnectDesc(sub, date);
          SubDev lastBeforeFromDev = subDevRepository.findFirstByDevAndConnectBeforeOrderByConnectDesc(lastFromSub.getDev(), date);

          if (lastBeforeFromSub == null && lastBeforeFromDev == null) {
            subDevRepository.save(new SubDev(sub, null, date));
            lastFromSub.setSub(null);
            subDevRepository.save(lastFromSub);
          }
          if (lastBeforeFromSub == null && lastBeforeFromDev != null) {
            if (lastBeforeFromDev.getSub() == null) {
              lastFromSub.setDev(null);
              subDevRepository.save(lastFromSub);
            } else {
              subDevRepository.save(new SubDev(null, lastFromSub.getDev(), date));
              lastFromSub.setDev(null);
              subDevRepository.save(lastFromSub);
            }
          }
          if (lastBeforeFromSub != null && lastBeforeFromDev == null) {
            if (lastBeforeFromSub.getDev() == null) {
              lastFromSub.setSub(null);
              subDevRepository.save(lastFromSub);
            } else {
              subDevRepository.save(new SubDev(sub, null, date));
              lastFromSub.setSub(null);
              subDevRepository.save(lastFromSub);
            }
          }
          if (lastBeforeFromSub != null && lastBeforeFromDev != null) {
            subDevRepository.delete(lastFromSub.getId());
            if (lastBeforeFromSub.getDev() != null) {
              subDevRepository.save(new SubDev(sub, null, date));
            }
            if (lastBeforeFromDev.getSub() != null) {
              subDevRepository.save(new SubDev(null, lastFromSub.getDev(), date));
            }
          }
        }
      }
    }
    if (dev != null) {
      if (date.isEqual(lastFromSub.getConnect())) {
        if (equals(lastFromSub.getDev(), dev)) {
          // nothing
        } else if (lastFromSub.getDev() == null) {
          closeDevice(dev, date);
          SubDev lastBeforeFromSub = subDevRepository.findFirstBySubAndConnectBeforeOrderByConnectDesc(sub, date);
          if (lastBeforeFromSub == null) {
            lastFromSub.setDev(dev);
            subDevRepository.save(lastFromSub);
          } else {
            if (equals(dev, lastBeforeFromSub.getDev())) {
              System.out.println("IDE " + lastFromSub.getId());
              subDevRepository.delete(lastFromSub.getId());
            } else {
              lastFromSub.setDev(dev);
              subDevRepository.save(lastFromSub);
            }
          }
        } else {
          closeDevice(dev, date);
          SubDev lastBeforeFromSub = subDevRepository.findFirstBySubAndConnectBeforeOrderByConnectDesc(sub, date);
          SubDev lastBeforeFromDev = subDevRepository.findFirstByDevAndConnectBeforeOrderByConnectDesc(lastFromSub.getDev(), date);

          if (lastBeforeFromSub == null && lastBeforeFromDev == null) {
            subDevRepository.save(new SubDev(sub, dev, date));
            lastFromSub.setSub(null);
            subDevRepository.save(lastFromSub);
          }
          if (lastBeforeFromSub == null && lastBeforeFromDev != null) {
            if (lastBeforeFromDev.getSub() != null) {
              subDevRepository.save(new SubDev(sub, dev, date));
            }
            lastFromSub.setDev(dev);
            subDevRepository.save(lastFromSub);
          }
          if (lastBeforeFromSub != null && lastBeforeFromDev == null) {
            if (!equals(dev, lastBeforeFromSub.getDev())) {
              subDevRepository.save(new SubDev(sub, dev, date));
            }
            lastFromSub.setSub(null);
            subDevRepository.save(lastFromSub);
          }
          if (lastBeforeFromSub != null && lastBeforeFromDev != null) {
            if (lastBeforeFromDev.getSub() != null) {
              subDevRepository.save(new SubDev(null, lastFromSub.getDev(), date));
            }
            if (equals(lastBeforeFromSub.getDev(), dev)) {
              subDevRepository.delete(lastFromSub.getId());
            } else {
              lastFromSub.setDev(dev);
              subDevRepository.save(lastFromSub);
            }
          }
        }
      }
    }
  }

  private void closeDevice(Device dev, LocalDate date) {
    if (dev == null) {
      return;
    }

    SubDev last = subDevRepository.findFirstByDevOrderByConnectDesc(dev);

    if (date.isAfter(last.getConnect())) {
      if (last.getSub() != null) {
        subDevRepository.save(new SubDev(last.getSub(), null, date));
      }
    } else if (date.isEqual(last.getConnect())) {
      if (last.getSub() == null) {
        subDevRepository.delete(last);
      } else {
        last.setDev(null);
        subDevRepository.save(last);
      }
    }
  }

  private boolean equals(Device d1, Device d2) {
    if (d1 == null && d2 == null) {
      return true;
    } else if (d1 == null || d2 == null) {
      return false;
    }
    return d1.getId().equals(d2.getId());
  }

  public Subscription findLastSub(Device device) {
    return subDevRepository.findFirstByDevOrderByConnectDesc(device).getSub();
  }

}
