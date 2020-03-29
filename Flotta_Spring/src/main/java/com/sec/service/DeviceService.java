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
import com.sec.entity.Device;
import com.sec.entity.DeviceType;
import com.sec.entity.User;
import com.sec.entity.viewEntity.DeviceToView;

@Service
public class DeviceService {
  
  private Map<String, String> msg = new HashMap<String, String>();

  private DeviceRepository deviceRepository;

  @Autowired
  public void setDeviceRepository(DeviceRepository deviceRepository) {
    this.deviceRepository = deviceRepository;
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

  public boolean save(DeviceToView device, DeviceType deviceType, User user, LocalDate date) {
    Device check = deviceRepository.findBySerialNumber(device.getSerialNumber());
    if (check == null) {
      Device d = new Device(device.getSerialNumber(), deviceType);
      d.userModification(user, date);
      d.subModification(null, date);
      deviceRepository.save(d);
      return true;
    }
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    msg.put(
        auth.getName(), "Serial number (" + device.getSerialNumber() + ") already exists!");
    return false;
  }

  public void update(long id, User user, LocalDate date) {
    Device d = deviceRepository.findOne(id);
    if(d != null) {
      d.userModification(user, date);
      deviceRepository.save(d);
    }
  }

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
}
