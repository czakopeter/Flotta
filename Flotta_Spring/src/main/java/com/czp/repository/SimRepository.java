package com.czp.repository;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.czp.entity.Sim;

public interface SimRepository extends CrudRepository<Sim, Long> {

	List<Sim> findAll();

  Sim findByImei(String imei);

  List<Sim> findAllBySimSubIsNullAndReasonIsNull();
	
}