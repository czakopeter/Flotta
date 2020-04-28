package com.sec.billing.repository;

import org.springframework.data.repository.CrudRepository;

import com.sec.billing.BillPartitionTemplate;
import com.sec.billing.Category;

public interface BillPartitionTemplateRepository extends CrudRepository<BillPartitionTemplate, Long>{

}
