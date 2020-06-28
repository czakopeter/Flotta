package com.sec.billing.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.sec.billing.BillPartitionTemplate;


public interface BillPartitionTemplateRepository extends CrudRepository<BillPartitionTemplate, Long>{

  List<BillPartitionTemplate> findAll();
  
  List<BillPartitionTemplate> findAllByAvailableTrue();
  
  List<BillPartitionTemplate> findAllByAvailableFalse();
  
  BillPartitionTemplate findByName(String name);

}
