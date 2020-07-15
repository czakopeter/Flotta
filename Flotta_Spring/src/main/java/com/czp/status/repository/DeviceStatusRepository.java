package com.czp.status.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.czp.entity.Device;
import com.czp.status.DeviceStatus;

public interface DeviceStatusRepository extends CrudRepository<DeviceStatus, Long> {

  List<DeviceStatus> findAllByDev(Device dev);

  DeviceStatus findFirstByDevOrderByDateDesc(Device dev);

  long countByDev(Device dev);

  DeviceStatus findFirstByDevAndDateBeforeOrderByDateDesc(Device dev, LocalDate date);

}
