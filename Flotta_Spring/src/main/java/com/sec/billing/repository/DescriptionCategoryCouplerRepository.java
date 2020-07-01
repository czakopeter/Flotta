package com.sec.billing.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.sec.billing.DescriptionCategoryCoupler;


public interface DescriptionCategoryCouplerRepository extends CrudRepository<DescriptionCategoryCoupler, Long>{

  List<DescriptionCategoryCoupler> findAll();
  
  List<DescriptionCategoryCoupler> findAllByAvailableTrue();
  
  List<DescriptionCategoryCoupler> findAllByAvailableFalse();
  
  DescriptionCategoryCoupler findByName(String name);

}
