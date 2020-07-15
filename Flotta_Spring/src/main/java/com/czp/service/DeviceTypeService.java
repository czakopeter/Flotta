package com.czp.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.czp.entity.DeviceType;
import com.czp.repository.DeviceTypeRepository;

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
    DeviceType name = deviceTypeRepository.findByNameIgnoreCase(deviceType.getName());
    DeviceType brandAndModel = deviceTypeRepository.findByBrandAndModelIgnoreCase(deviceType.getBrand(), deviceType.getModel());
    if(deviceType.getId() == null) {
      return name == null && brandAndModel == null;
    } else {
      DeviceType saved = deviceTypeRepository.findOne(deviceType.getId());
      if((saved.getDevices() != null && saved.getDevices().size() > 0) ||
         (name != null && name.getId() != saved.getId()) || 
         (brandAndModel != null && brandAndModel.getId() != saved.getId())) {
        return false;
      }
      return true;
    }
  }
  
  public DeviceType findByName(String name) {
    return deviceTypeRepository.findByNameIgnoreCase(name);
  }
}
