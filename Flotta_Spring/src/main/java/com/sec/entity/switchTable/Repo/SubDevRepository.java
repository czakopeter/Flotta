package com.sec.entity.switchTable.Repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.sec.entity.Device;
import com.sec.entity.Subscription;
import com.sec.entity.switchTable.SubDev;

public interface SubDevRepository extends CrudRepository<SubDev, Long> {

  SubDev findFirstBySubOrderByConnectDesc(Subscription sub);

  SubDev findFirstBySubAndConnectBeforeOrderByConnectDesc(Subscription sub, LocalDate date);

  SubDev findFirstByDevOrderByConnectDesc(Device device);

  SubDev findFirstByDevAndConnectBeforeOrderByConnectDesc(Device dev, LocalDate date);

  SubDev findFirstBySubIdAndConnectBeforeOrderByConnectDesc(long id, LocalDate date);

  List<SubDev> findAllBySub(Subscription subscription);

  @Query("select sd.connect from SubDev sd where sd.sub.id = ?1")
  List<LocalDate> findAllDatesBySubId(long id);
  
}
