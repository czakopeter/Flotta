package com.sec.status.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.sec.entity.Sim;
import com.sec.status.SimStatus;

public interface SimStatusRepository extends CrudRepository<SimStatus, Long> {

  List<SimStatus> findAllBySim(Sim sim);

  SimStatus findFirstBySimOrderByConnectDesc(Sim sim);

}
