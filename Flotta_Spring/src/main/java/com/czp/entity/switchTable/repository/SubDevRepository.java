package com.czp.entity.switchTable.repository;

import java.time.LocalDate;

import org.springframework.data.repository.CrudRepository;

import com.czp.entity.Device;
import com.czp.entity.Subscription;
import com.czp.entity.switchTable.SubDev;

public interface SubDevRepository extends CrudRepository<SubDev, Long> {

  SubDev findFirstBySubOrderByBeginDateDesc(Subscription sub);

  SubDev findFirstBySubAndBeginDateBeforeOrderByBeginDateDesc(Subscription sub, LocalDate date);

  SubDev findFirstByDevOrderByBeginDateDesc(Device device);

  SubDev findFirstByDevAndBeginDateBeforeOrderByBeginDateDesc(Device dev, LocalDate date);
  
}
