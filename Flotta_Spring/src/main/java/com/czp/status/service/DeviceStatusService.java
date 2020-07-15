package com.czp.status.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.czp.entity.Device;
import com.czp.enums.DeviceStatusEnum;
import com.czp.status.DeviceStatus;
import com.czp.status.repository.DeviceStatusRepository;

@Service
public class DeviceStatusService {

  private DeviceStatusRepository deviceStatusRepository;

  @Autowired
  public void setSimStatusRepository(DeviceStatusRepository deviceStatusRepository) {
    this.deviceStatusRepository = deviceStatusRepository;
  }

  public DeviceStatus findById(long id) {
    return deviceStatusRepository.findOne(id);
  }

  public List<DeviceStatus> findAllByDev(Device dev) {
    return deviceStatusRepository.findAllByDev(dev);
  }

  public boolean save(Device dev, DeviceStatusEnum status, LocalDate date) {
    deviceStatusRepository.save(new DeviceStatus(status, dev, date));
    return true;
  }
  
  public void deleteLastStatus(Device dev) {
    long pcs = deviceStatusRepository.countByDev(dev);
    DeviceStatus status = deviceStatusRepository.findFirstByDevOrderByDateDesc(dev);
    if(pcs == 1) {
      status.setStatus(DeviceStatusEnum.FREE);
      deviceStatusRepository.save(status);
    } else if(pcs > 1) {
      deviceStatusRepository.delete(status.getId());
    }
  }

  public void setStatus(Device dev, DeviceStatusEnum status, LocalDate date) {
    System.out.println(dev + ",\t" + status + ", date: " + date);
    long pcs = deviceStatusRepository.countByDev(dev);
    
    if(pcs == 0) {
      deviceStatusRepository.save(new DeviceStatus(status, dev, date));
    } else {
      DeviceStatus last = deviceStatusRepository.findFirstByDevOrderByDateDesc(dev);
      if(date.isAfter(last.getDate())) {
        //add new status
        if(!status.equals(last.getStatus())) {
          deviceStatusRepository.save(new DeviceStatus(status, dev, date));
        }
      } else if (date.isEqual(last.getDate())) {
        //modify last
        DeviceStatus lastBefore = deviceStatusRepository.findFirstByDevAndDateBeforeOrderByDateDesc(dev, date);
        if(lastBefore != null && lastBefore.getStatus().equals(status)) {
          deviceStatusRepository.delete(last.getId());
        } else {
          last.setStatus(status);
          deviceStatusRepository.save(last);
        }
      } else {
        //error
      }
    }
  }
}
