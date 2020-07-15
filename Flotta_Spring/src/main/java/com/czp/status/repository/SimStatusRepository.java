package com.czp.status.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.czp.entity.Sim;
import com.czp.status.SimStatus;

public interface SimStatusRepository extends CrudRepository<SimStatus, Long> {

  List<SimStatus> findAllBySim(Sim sim);

  SimStatus findFirstBySimOrderByDateDesc(Sim sim);

}
