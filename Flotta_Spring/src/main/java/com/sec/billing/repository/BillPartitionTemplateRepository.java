package com.sec.billing.repository;

import org.springframework.data.repository.CrudRepository;

import com.sec.billing.BillPartitionTemplate;

public interface BillPartitionTemplateRepository extends CrudRepository<BillPartitionTemplate, Long>{

  BillPartitionTemplate findByName(String name);

}
