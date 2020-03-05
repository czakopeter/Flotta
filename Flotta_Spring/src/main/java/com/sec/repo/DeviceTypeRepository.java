package com.sec.repo;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.sec.entity.DeviceType;

public interface DeviceTypeRepository extends CrudRepository<DeviceType, Long> {

	List<DeviceType> findAll();

	@Query("select distinct d.brand from DeviceType d")
  List<String> findAllBrandOfDevicesType();

  DeviceType findByName(String name);

  DeviceType findByBrandAndModel(String brand, String model);

}