package com.sec.entity.switchTable.Repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sec.entity.Subscription;
import com.sec.entity.switchTable.SubSim;

@Repository
public interface SubSimRepository extends CrudRepository<SubSim, Long> {

  List<SubSim> findAllBySub(Subscription s);

  SubSim findBySubAndConnect(Subscription s, LocalDate date);

  void deleteBySubAndDate(Subscription s, LocalDate date);

}
