package com.sec.billing;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class PayDivision {
  
  @Id
  @GeneratedValue
  private long id;
  
  private String name;
  
  private boolean available;
  
  @ElementCollection
  private Map<Category, Integer> categoryScale = new HashMap<>();
  
  public PayDivision() {
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

  public void add(Category category, int scale) {
    if(validScale(scale)) {
      categoryScale.put(category, scale);
    }
  }

  public Map<Category, Integer> getCategoryScale() {
    return categoryScale;
  }

  public void setCategoryScale(Map<Category, Integer> categoryScale) {
    this.categoryScale = categoryScale;
  }

  private boolean validScale(int ratio) {
    return ratio >= 0 && ratio <= 100 && ratio % 5 == 0;
  }
  
  public List<Category> getOrderedCategories() {
    List<Category> result = new LinkedList<Category>(categoryScale.keySet());
    Collections.sort(result);
    return result;
  }
  
}
