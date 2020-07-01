package com.sec.billing.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.sec.billing.ChargeRatioByCategory;

public interface ChargeRatioRepository extends CrudRepository<ChargeRatioByCategory, Long> {

  ChargeRatioByCategory findByName(String name);
  
  List<ChargeRatioByCategory> findAll();

}
