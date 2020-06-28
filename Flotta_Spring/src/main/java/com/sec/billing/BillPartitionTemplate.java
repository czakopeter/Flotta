package com.sec.billing;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.sec.billing.exception.UnknonwFeeItemDescriptionException;

@Entity
public class BillPartitionTemplate {

  @Id
  @GeneratedValue
  private long id;

  private String name;

  private boolean available;

  @ManyToMany(cascade = CascadeType.ALL)
  private Map<String, Category> connection = new HashMap<String, Category>();

  public BillPartitionTemplate() {
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isAvailable() {
    return available;
  }

  public void setAvailable(boolean available) {
    this.available = available;
  }

  public Map<String, Category> getConnection() {
    return connection;
  }

  public void setConnection(Map<String, Category> connection) {
    this.connection = connection;
  }

  public void addToConnection(String desc, Category category) {
    Category cat = connection.get(desc);
    if (cat == null) {
      connection.put(desc, category);
    }
  }

  public Map<Category, List<FeeItem> > partition(Bill bill) throws UnknonwFeeItemDescriptionException {
    Map<Category, List<FeeItem>> result = new HashMap<>();
    Set<String> unknownFreeItemDesc = new HashSet<>();

    for (FeeItem fi : bill.getFeeItems()) {
      Category category = connection.get(fi.getDescription());
      if (category == null) {
        unknownFreeItemDesc.add(fi.getDescription());
      } else {
        fi.setCatergory(category.getName());
      }
      if (unknownFreeItemDesc.size() != 0) {
        List<FeeItem> value = result.get(category);
        if (value == null) {
          value = new LinkedList<FeeItem>();
        }
        value.add(fi);
        result.put(category, value);
      }
    }
    if(!unknownFreeItemDesc.isEmpty()) {
      throw new UnknonwFeeItemDescriptionException(unknownFreeItemDesc);
    }
    return result;
  }
  
  public List<String> getSortedDescriptions() {
    List<String> descriptions = new LinkedList<>(connection.keySet());
    Collections.sort(descriptions);
    return descriptions;
  }
  
}
