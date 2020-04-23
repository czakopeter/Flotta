package com.sec.billing.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sec.billing.repository.NodeRepository;

@Service
public class NodeService {
  
  private NodeRepository nodeRepository;

  @Autowired
  public void setNodeRepository(NodeRepository nodeRepository) {
    this.nodeRepository = nodeRepository;
  }
  
  public List<Node> findAllRoot() {
    return nodeRepository.findAllByParentIsNull();
  }
}
