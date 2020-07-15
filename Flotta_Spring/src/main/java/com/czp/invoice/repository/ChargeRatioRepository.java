package com.czp.invoice.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.czp.invoice.ChargeRatioByCategory;

public interface ChargeRatioRepository extends CrudRepository<ChargeRatioByCategory, Long> {

  ChargeRatioByCategory findByName(String name);
  
  List<ChargeRatioByCategory> findAll();

}
