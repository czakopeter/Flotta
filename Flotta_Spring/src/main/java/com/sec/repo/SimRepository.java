package com.sec.repo;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.sec.entity.Sim;

public interface SimRepository extends CrudRepository<Sim, Long> {

	List<Sim> findAll();

  Sim findByImei(String imei);
	
}