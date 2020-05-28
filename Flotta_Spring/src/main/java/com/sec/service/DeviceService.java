package com.sec.service;

import java.time.LocalDate;
import java.util.HashMap;
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
import com.sec.enums.DeviceStatusEnum;

@Service
public class DeviceService {
  
  private Map<String, String> msg = new HashMap<String, String>();

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
    if(0 == id) {return null;}
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

  public Device save(String serialNumber, DeviceType deviceType, LocalDate date) {
    Device check = deviceRepository.findBySerialNumber(serialNumber);
    if(check == null) {
      Device d = new Device(serialNumber);
      d.setDeviceType(deviceType);
      return deviceRepository.save(d);
    } else {
      addError("Serial number already exists");
      return null;
    }
  }
  
  public String getError() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return msg.remove(auth.getName());
  }
  
  private void addError(String err) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    msg.put(auth.getName(), err);
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

  public Device findByIdAndDate(long id, LocalDate date) {
    return null;
  }

  public List<LocalDate> findDevicesDatesById(long id) {
    return null;
  }
}
