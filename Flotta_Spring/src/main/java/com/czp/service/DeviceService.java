package com.czp.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.czp.entity.Device;
import com.czp.entity.DeviceType;
import com.czp.entity.Subscription;
import com.czp.entity.User;
import com.czp.entity.viewEntity.DeviceToView;
import com.czp.entity.viewEntity.SubscriptionToView;
import com.czp.enums.DeviceStatusEnum;
import com.czp.repository.DeviceRepository;
import com.czp.status.service.DeviceStatusService;
import com.czp.utility.Utility;

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

  public boolean add(DeviceToView dtv) {
    if(deviceRepository.findBySerialNumber(dtv.getSerialNumber()) == null) {
      Device entity = new Device(dtv.getSerialNumber(), dtv.getDate());
      deviceRepository.save(entity);
      return true;
    } else {
      appendMsg("Serial number already exists");
      return false;
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

  
  //TODO nem kell az  összes eszköz, csak ami valaha a felhasználónál volt és aktív
  public List<DeviceToView> findAllCurrentByUser(User user) {
    List<DeviceToView> result = new LinkedList<DeviceToView>();
    List<Device> all = deviceRepository.findAll();
    for(Device s : all) {
      if(Utility.isSameByIdOrBothNull(user, s.getActualUser())) {
        result.add(s.toView());
      }
    }
    return result;
  }

  //TODO DeviceService save
  public void save(Device device) {
    deviceRepository.save(device);
  }
}