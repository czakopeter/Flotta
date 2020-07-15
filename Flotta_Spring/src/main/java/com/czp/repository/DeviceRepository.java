package com.czp.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.czp.entity.Device;
import com.czp.entity.DeviceType;

public interface DeviceRepository extends CrudRepository<Device, Long> {

  List<Device> findAll();
  
  Device findBySerialNumber(String serialNumber);

  List<Device> findAllByDeviceType(DeviceType deviceType);

}
