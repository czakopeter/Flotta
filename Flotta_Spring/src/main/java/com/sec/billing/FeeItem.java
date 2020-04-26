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
  
//  private Subscription subscription;
  
  private String description;
  
  private LocalDate begin;
  
  private LocalDate end;
  
  private double netAmount;
  
  private double taxAmount;
  
  private double taxPercentage;

  public FeeItem(Element feeRoot) {
    description = feeRoot.getElementsByTagName("Desc").item(0).getFirstChild().getNodeValue().trim();
    begin = LocalDate.parse(feeRoot.getElementsByTagName("Begin").item(0).getFirstChild().getNodeValue(), DateTimeFormatter.ofPattern("uuuu.MM.dd."));
    end = LocalDate.parse(feeRoot.getElementsByTagName("End").item(0).getFirstChild().getNodeValue(), DateTimeFormatter.ofPattern("uuuu.MM.dd."));
    netAmount = Double.valueOf(feeRoot.getElementsByTagName("NetA").item(0).getFirstChild().getNodeValue().replace(",", "."));
    taxAmount = Double.valueOf(feeRoot.getElementsByTagName("TaxA").item(0).getFirstChild().getNodeValue().replace(",", "."));
    taxPercentage = Double.valueOf(feeRoot.getElementsByTagName("TaxP").item(0).getFirstChild().getNodeValue().replace(",", ".").replace("%", ""));
    
//    Node p = feeRoot.getFirstChild();
//    description = p.getFirstChild().getNodeValue();
//    p = p.getNextSibling();
//    System.out.println(p.getNodeName());
//    begin = LocalDate.parse(p.getChildNodes().item(0).getFirstChild().getNodeValue(), DateTimeFormatter.ofPattern("uuuu.MM.dd."));
//    end = LocalDate.parse(p.getChildNodes().item(1).getFirstChild().getNodeValue(), DateTimeFormatter.ofPattern("uuuu.MM.dd."));
//    p = p.getNextSibling();
//    netAmount = Double.valueOf(p.getFirstChild().getNodeValue().replace(',', '.'));
//    p = p.getNextSibling();
//    taxPercentage = Double.valueOf(p.getFirstChild().getNodeValue().replace("%", "").replace(',', '.'));
//    p = p.getNextSibling();
//    taxAmount = Double.valueOf(p.getFirstChild().getNodeValue().replace(',', '.'));
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

//  public Subscription getSubscription() {
//    return subscription;
//  }
//
//  public void setSubscription(Subscription subscription) {
//    this.subscription = subscription;
//  }

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
    return "FeeItem [description=" + description + ", begin=" + begin + ", end=" + end + ", netAmount=" + netAmount + ", taxAmount=" + taxAmount + ", taxPercentage=" + taxPercentage + "]";
  }
  
}
