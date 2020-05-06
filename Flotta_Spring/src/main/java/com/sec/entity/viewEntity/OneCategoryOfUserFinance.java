package com.sec.entity.viewEntity;

import java.util.LinkedList;
import java.util.List;

import com.sec.billing.FeeItem;

public class OneCategoryOfUserFinance {
  
  private long userId;
  
  private String categoryName;
  
  private double totalGross;
  
  List<FeeItem> feeItems = new LinkedList<FeeItem>();
  
  public OneCategoryOfUserFinance() {}
  
  public OneCategoryOfUserFinance(long userId, String categoryName) {
    this.userId = userId;
    this.categoryName = categoryName;
    this.totalGross = totalGross;
  }

  public OneCategoryOfUserFinance(String categoryName, long totalGross, List<FeeItem> feeItems) {
    this.categoryName = categoryName;
    this.totalGross = totalGross;
    this.feeItems = feeItems;
  }

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public double getTotalGross() {
    return totalGross;
  }

  public void setTotalGross(double totalGross) {
    this.totalGross = totalGross;
  }

  public List<FeeItem> getFeeItems() {
    return feeItems;
  }

  public void setFeeItems(List<FeeItem> feeItems) {
    this.feeItems = feeItems;
  }
  
  public void addFeeItem(FeeItem fee) {
    feeItems.add(fee);
    totalGross += fee.getGross();
  }
}
