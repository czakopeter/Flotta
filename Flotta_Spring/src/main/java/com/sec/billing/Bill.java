package com.sec.billing;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.w3c.dom.Element;

@Entity
@Table(name = "bills")
public class Bill {

//  private Concerned costumerData;
  
//  private  Concerned companyData;
  
  @Id
  @GeneratedValue
  private long id;
  
  private String xmlString;
  
  private LocalDate fromDate;
  
  private LocalDate endDate;
  
  private String invoiceNumber;
  
  private double invoiceNetAmount;
  
  private double invoiceTaxAmount;
  
  @OneToMany(mappedBy = "bill")
  List<FeeItem> feeItems = new LinkedList<FeeItem>();
  
  public Bill() {}
  
  public Bill(LocalDate fromDate, LocalDate endDate, String invoiceNumber, double invoiceNetAmount, double invoiceTaxAmount, List<FeeItem> feeItems) {
    this.fromDate = fromDate;
    this.endDate = endDate;
    this.invoiceNumber = invoiceNumber;
    this.invoiceNetAmount = invoiceNetAmount;
    this.invoiceTaxAmount = invoiceTaxAmount;
    this.feeItems = feeItems;
  }

  public Bill(String xmlString, LocalDate fromDate, LocalDate endDate, String invoiceNumber, Double invoiceNetAmount, Double invoiceTaxAmount) {
    this.xmlString = xmlString;
    this.fromDate = fromDate;
    this.endDate = endDate;
    this.invoiceNumber = invoiceNumber;
    this.invoiceNetAmount = invoiceNetAmount;
    this.invoiceTaxAmount = invoiceTaxAmount;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getXmlString() {
    return xmlString;
  }

  public void setXmlString(String xmlString) {
    this.xmlString = xmlString;
  }

  public LocalDate getFromDate() {
    return fromDate;
  }

  public void setFromDate(LocalDate fromDate) {
    this.fromDate = fromDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public String getInvoiceNumber() {
    return invoiceNumber;
  }

  public void setInvoiceNumber(String invoiceNumber) {
    this.invoiceNumber = invoiceNumber;
  }

  public double getInvoiceNetAmount() {
    return invoiceNetAmount;
  }

  public void setInvoiceNetAmount(double invoiceNetAmount) {
    this.invoiceNetAmount = invoiceNetAmount;
  }

  public double getInvoiceTaxAmount() {
    return invoiceTaxAmount;
  }

  public void setInvoiceTaxAmount(double invoiceTaxAmount) {
    this.invoiceTaxAmount = invoiceTaxAmount;
  }

  public List<FeeItem> getFeeItems() {
    return feeItems;
  }

  public void setFeeItems(List<FeeItem> feeItems) {
    this.feeItems = feeItems;
  }
  
  @Override
  public String toString() {
    return "Bill [fromDate=" + fromDate + ", endDate=" + endDate + ", invoiceNumber=" + invoiceNumber + ", invoiceNetAmount=" + invoiceNetAmount + ", invoiceTaxAmount=" + invoiceTaxAmount + "]";
  }

  public void addFee(FeeItem feeItem) {
    this.feeItems.add(feeItem);
    System.out.println(feeItem);
  }
  
}
