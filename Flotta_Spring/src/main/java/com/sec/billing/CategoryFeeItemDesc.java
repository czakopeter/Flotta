package com.sec.billing;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class CategoryFeeItemDesc {
  
  @Id
  @GeneratedValue
  private long id;
  
  private Category category;
  
  private String feeDesc;
  
  private int userPercentage;
  
  public CategoryFeeItemDesc(Category category, String feeDesc) {
    this.category = category;
    this.feeDesc = feeDesc;
    this.userPercentage = 100;
  }
  
  public CategoryFeeItemDesc(Category category, String feeDesc, int userPercentage) {
    this.category = category;
    this.feeDesc = feeDesc;
    this.userPercentage = userPercentage;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }
  
  public String getFeeDesc() {
    return feeDesc;
  }

  public void setFeeDesc(String feeDesc) {
    this.feeDesc = feeDesc;
  }

  public int getUserPercentage() {
    return userPercentage;
  }

  public void setUserPercentage(int userPercentage) {
    if(userPercentage > 100) {
      this.userPercentage = 100;
    } else if(userPercentage < 0) {
      this.userPercentage = 0;
    } else {
      this.userPercentage = userPercentage - (userPercentage % 5);
    }
  }  
}
