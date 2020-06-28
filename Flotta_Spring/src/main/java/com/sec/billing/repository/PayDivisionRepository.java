package com.sec.billing.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.sec.billing.PayDivision;

public interface PayDivisionRepository extends CrudRepository<PayDivision, Long> {

  PayDivision findByName(String name);
  
  List<PayDivision> findAll();

}
