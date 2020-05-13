package com.sec.billing;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class PayDevision {
  
  @Id
  @GeneratedValue
  private long id;
  
  private String name;
  
  private boolean available;
  
  @ElementCollection
  private Map<String, Integer> categoryRatio = new HashMap<>();
  
  public PayDevision() {
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

  public void add(String category, int ratio) {
    if(!categoryRatio.containsKey(category) && validRatio(ratio)) {
      categoryRatio.put(category, ratio);
    }
  }

  public Map<String, Integer> getCategoryRatio() {
    return categoryRatio;
  }

  public void setCategoryRatio(Map<String, Integer> categoryRatio) {
    this.categoryRatio = categoryRatio;
  }

  public void setCatRatio(String category, int ratio) {
    if(categoryRatio.containsKey(category) && validRatio(ratio)) {
      categoryRatio.put(category, ratio);
    }
  }
  
  private boolean validRatio(int ratio) {
    return ratio >= 0 && ratio <= 100 && ratio % 5 == 0;
  }
  
}
