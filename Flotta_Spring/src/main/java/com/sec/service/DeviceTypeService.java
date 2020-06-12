package com.sec.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sec.entity.DeviceType;
import com.sec.repo.DeviceTypeRepository;

@Service
public class DeviceTypeService {

  private DeviceTypeRepository deviceTypeRepository;

  @Autowired
  public void setDeviceTypeRepository(DeviceTypeRepository deviceTypeRepository) {
    this.deviceTypeRepository = deviceTypeRepository;
  }

  public List<DeviceType> findAll() {
    return deviceTypeRepository.findAll();
  }

  public DeviceType findById(long id) {
    return deviceTypeRepository.findOne(id);
  }

  public List<String> findAllBrandOfDevicesType() {
    return deviceTypeRepository.findAllBrandOfDevicesType();
  }

  public void save(DeviceType deviceType) {
    System.out.println(deviceType);
    if (deviceIsSaveable(deviceType)) {
        deviceTypeRepository.save(deviceType);
    }
  }
  
  private boolean deviceIsSaveable(DeviceType deviceType) {
    DeviceType name = deviceTypeRepository.findByName(deviceType.getName());
    DeviceType brandAndModel = deviceTypeRepository.findByBrandAndModel(deviceType.getBrand(), deviceType.getModel());
    if(deviceType.getId() == null) {
      return name == null || brandAndModel == null;
    } else {
      DeviceType id = deviceTypeRepository.findOne(deviceType.getId());
      if(id.getDevices() != null && id.getDevices().size() > 0 && (id.getSimNumber() > deviceType.getSimNumber() || (id.isMicrosd() && !deviceType.isMicrosd()))) {
        return false;
      } else {
        if((name != null && name.getId() != id.getId()) && (brandAndModel != null && brandAndModel.getId() != id.getId())) {
          return false;
        }
        return true;
      }
    }
  }
  
  public boolean deviceTypesEquals(DeviceType dt1, DeviceType dt2) {

    return true;
  }

  public DeviceType findByName(String name) {
    return deviceTypeRepository.findByName(name);
  }
}
