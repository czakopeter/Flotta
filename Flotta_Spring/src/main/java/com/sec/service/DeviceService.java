package com.sec.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.sec.repo.DeviceRepository;
import com.sec.status.service.DeviceStatusService;
import com.sec.entity.Device;
import com.sec.entity.DeviceType;
import com.sec.entity.Subscription;
import com.sec.entity.User;
import com.sec.entity.viewEntity.DeviceToView;
import com.sec.entity.viewEntity.SubscriptionToView;
import com.sec.enums.DeviceStatusEnum;

@Service
public class DeviceService extends ServiceWithMsg{
  
  private DeviceRepository deviceRepository;
  
  private DeviceStatusService deviceStatusService;

  @Autowired
  public void setDeviceRepository(DeviceRepository deviceRepository) {
    this.deviceRepository = deviceRepository;
  }
  
  @Autowired
  public void setDeviceStatusService(DeviceStatusService deviceStatusService) {
    this.deviceStatusService = deviceStatusService;
  }

  public Device findBySerialNumber(String selialNumber) {
    if (selialNumber != null) {
      return deviceRepository.findBySerialNumber(selialNumber);
    }
    return null;
  }
  
  public Device findById(long id) {
    return deviceRepository.findOne(id);
  }

  public List<Device> findAll() {
    return deviceRepository.findAll();
  }

//  public boolean save(DeviceToView device, DeviceType deviceType, User user, LocalDate date) {
//    Device check = deviceRepository.findBySerialNumber(device.getSerialNumber());
//    if (check == null) {
//      Device dev = new Device(device.getSerialNumber(), deviceType);
//      dev.userModification(user, date);
//      dev.subModification(null, date);
//      deviceRepository.save(dev);
//      deviceStatusService.save(dev, DeviceStatusEnum.FREE, date);
//      return true;
//    }
//    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//    msg.put(
//        auth.getName(), "Serial number (" + device.getSerialNumber() + ") already exists!");
//    return false;
//  }

//  public void update(long id, User user, LocalDate date) {
//    Device d = deviceRepository.findOne(id);
//    if(d != null) {
//      d.userModification(user, date);
//      deviceRepository.save(d);
//    }
//  }

  public boolean add(DeviceToView dtv, DeviceType deviceType) {
    if(deviceRepository.findBySerialNumber(dtv.getNumber()) == null) {
      Device entity = new Device(dtv.getNumber(), deviceType, dtv.getDate());
      deviceRepository.save(entity);
      return true;
    } else {
      appendMsg("Number already exists");
      return false;
    }
  }
  
  public Device save(String serialNumber, DeviceType deviceType, LocalDate date) {
    Device check = deviceRepository.findBySerialNumber(serialNumber);
    if(check == null) {
      Device d = new Device(serialNumber);
      d.setDeviceType(deviceType);
      return deviceRepository.save(d);
    } else {
      appendMsg("Serial number already exists");
      return null;
    }
  }
  
  public String getError() {
    return removeMsg();
  }
  
  

  public void userHasConnected(Device dev, LocalDate date) {
    deviceStatusService.setStatus(dev, DeviceStatusEnum.ACTIVE, date);
  }

  public void userHasntConnected(Device dev, LocalDate date) {
    deviceStatusService.setStatus(dev, DeviceStatusEnum.FREE, date);
  }

  public void deleteLastSatus(Device dev) {
    deviceStatusService.deleteLastStatus(dev);
  }

  public List<DeviceToView> findAllActualByUser(User user) {
    List<DeviceToView> result = new LinkedList<DeviceToView>();
    List<Device> all = deviceRepository.findAll();
    for(Device s : all) {
      if(User.isSameByIdOrBothNull(user, s.getActualUser())) {
        result.add(s.toView());
      }
    }
    return result;
  }
  
  
}
