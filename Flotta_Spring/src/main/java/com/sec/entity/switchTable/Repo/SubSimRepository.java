package com.sec.entity.switchTable.Repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.sec.entity.Subscription;
import com.sec.entity.switchTable.SubSim;

public interface SubSimRepository extends CrudRepository<SubSim, Long> {

  List<SubSim> findAllBySub(Subscription s);

  SubSim findBySubAndBeginDate(Subscription s, LocalDate date);

  void deleteBySubAndBeginDate(Subscription s, LocalDate date);

  SubSim findBySubAndBeginDate(long subId, LocalDate date);

  List<SubSim> findAllBySub(long subId);

  SubSim findFirstBySubOrderByBeginDateDesc(Subscription sub);
}
