package com.sec.billing;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

//@Entity
//@Table(name = "fee_items")
public class SplittedFeeItem {

  @Id
  @GeneratedValue
  long id;

  @ManyToOne
  private Bill bill;

  private String subscription;

  private String description;

  private LocalDate begin;

  private LocalDate end;

  private double gross;
  
  private String category;
  
  private long user;
  
  private boolean validByUser;
  
  private boolean validByCompany;

  public SplittedFeeItem() {
  }

  public SplittedFeeItem(Bill bill, String subscription, String description, LocalDate begin, LocalDate end, double gross) {
    this.bill = bill;
    this.subscription = subscription;
    this.description = description;
    this.begin = begin;
    this.end = end;
    this.gross = gross;
  }

  public SplittedFeeItem(SplittedFeeItem feeItem) {
    this.bill = feeItem.bill;
    this.subscription = feeItem.subscription;
    this.description = feeItem.description;
    this.begin = feeItem.begin;
    this.end = feeItem.end;
    this.gross = feeItem.gross;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Bill getBill() {
    return bill;
  }

  public void setBill(Bill bill) {
    this.bill = bill;
  }

  public String getSubscription() {
    return subscription;
  }

  public void setSubscription(String subscription) {
    this.subscription = subscription;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LocalDate getBegin() {
    return begin;
  }

  public void setBegin(LocalDate begin) {
    this.begin = begin;
  }

  public LocalDate getEnd() {
    return end;
  }

  public void setEnd(LocalDate end) {
    this.end = end;
  }
  
  public double getGross() {
    return gross;
  }

  public void setGross(double gross) {
    this.gross = gross;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public long getUser() {
    return user;
  }

  public void setUser(long user) {
    this.user = user;
  }

  public boolean isValidByUser() {
    return validByUser;
  }

  public void setValidByUser(boolean validByUser) {
    this.validByUser = validByUser;
  }

  public boolean isValidByCompany() {
    return validByCompany;
  }

  public void setValidByCompany(boolean validByCompany) {
    this.validByCompany = validByCompany;
  }

}
