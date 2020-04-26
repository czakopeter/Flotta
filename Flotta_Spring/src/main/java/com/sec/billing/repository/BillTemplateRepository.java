package com.sec.billing.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.sec.billing.MyNode;

public interface BillTemplateRepository extends CrudRepository<MyNode, Long> {

  List<MyNode> findAllByParentIsNull();
  
  List<MyNode> findAllByParentIsNullAndName(String name);
}
