package com.sec.entity.switchTable.Repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sec.entity.Subscription;
import com.sec.entity.switchTable.SubSim;

public interface SubSimRepository extends CrudRepository<SubSim, Long> {

  List<SubSim> findAllBySub(Subscription s);

  SubSim findBySubAndConnect(Subscription s, LocalDate date);

  void deleteBySubAndConnect(Subscription s, LocalDate date);

  SubSim findBySubAndConnect(long subId, LocalDate date);

  List<SubSim> findAllBySub(long subId);

  SubSim findFirstBySubOrderByConnectDesc(Subscription sub);
}
