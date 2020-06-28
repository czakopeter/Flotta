package com.sec.entity.switchTable.Repo;

import java.time.LocalDate;

import org.springframework.data.repository.CrudRepository;

import com.sec.entity.Device;
import com.sec.entity.Subscription;
import com.sec.entity.switchTable.SubDev;

public interface SubDevRepository extends CrudRepository<SubDev, Long> {

  SubDev findFirstBySubOrderByBeginDateDesc(Subscription sub);

  SubDev findFirstBySubAndBeginDateBeforeOrderByBeginDateDesc(Subscription sub, LocalDate date);

  SubDev findFirstByDevOrderByBeginDateDesc(Device device);

  SubDev findFirstByDevAndBeginDateBeforeOrderByBeginDateDesc(Device dev, LocalDate date);
  
}
