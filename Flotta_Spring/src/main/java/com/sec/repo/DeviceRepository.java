package com.sec.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.sec.entity.Device;
import com.sec.entity.DeviceType;

public interface DeviceRepository extends CrudRepository<Device, Long> {

  List<Device> findAll();
  
  Device findBySerialNumber(String selialNumber);

  List<Device> findAllByDeviceType(DeviceType deviceType);

}
