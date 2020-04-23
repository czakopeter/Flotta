package com.sec.billing.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.sec.billing.service.Node;

public interface NodeRepository extends CrudRepository<Node, Long> {

  List<Node> findAllByParentIsNull();
}
