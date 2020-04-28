package com.sec.billing;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.sec.entity.Subscription;

@Entity
@Table(name = "fee_items")
public class FeeItem {

  @Id
  @GeneratedValue
  long id;

  @ManyToOne
  private Bill bill;

  private String subscription;

  private String description;

  private LocalDate begin;

  private LocalDate end;

  private double netAmount;

  private double taxAmount;

  private double taxPercentage;

  public FeeItem() {
  }

  public FeeItem(Bill bill, String subscription, String description, LocalDate begin, LocalDate end, double netAmount, double taxAmount, double taxPercentage) {
    this.bill = bill;
    this.subscription = subscription;
    this.description = description;
    this.begin = begin;
    this.end = end;
    this.netAmount = netAmount;
    this.taxAmount = taxAmount;
    this.taxPercentage = taxPercentage;
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

  public double getNetAmount() {
    return netAmount;
  }

  public void setNetAmount(double netAmount) {
    this.netAmount = netAmount;
  }

  public double getTaxAmount() {
    return taxAmount;
  }

  public void setTaxAmount(double taxAmount) {
    this.taxAmount = taxAmount;
  }

  public double getTaxPercentage() {
    return taxPercentage;
  }

  public void setTaxPercentage(double taxPercentage) {
    this.taxPercentage = taxPercentage;
  }

  @Override
  public String toString() {
    return "FeeItem [subscription=" + subscription + ", description=" + description + ", begin=" + begin + ", end=" + end + ", netAmount=" + netAmount + ", taxAmount=" + taxAmount + ", taxPercentage=" + taxPercentage + "]";
  }

}
