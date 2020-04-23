package com.sec.billing.service;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import groovy.transform.ToString;

@Entity
@Table(name = "nodes")
public class Node {

  @Id
  @GeneratedValue
  private long id;
  
  private long templateId;
  
  private String name;
  
  @ManyToOne
  private Node parent;
  
  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
  private List<Node> child = new LinkedList<Node>();

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getTemplateId() {
    return templateId;
  }

  public void setTemplateId(long templateId) {
    this.templateId = templateId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  public Node getParent() {
    return parent;
  }

  public void setParent(Node parent) {
    this.parent = parent;
  }

  public List<Node> getChild() {
    return child;
  }

  public void setChild(List<Node> child) {
    this.child = child;
  }
  
  public void show() {
    System.out.println("name = " + name + "(OPEN)");
    for(Node n : child) {
      n.show();
    }
    System.out.println("name = " + name + "(CLOSE)");
  }
}
