package com.czp.invoice.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.czp.invoice.DescriptionCategoryCoupler;


public interface DescriptionCategoryCouplerRepository extends CrudRepository<DescriptionCategoryCoupler, Long>{

  List<DescriptionCategoryCoupler> findAll();
  
  List<DescriptionCategoryCoupler> findAllByAvailableTrue();
  
  List<DescriptionCategoryCoupler> findAllByAvailableFalse();
  
  DescriptionCategoryCoupler findByName(String name);

}
