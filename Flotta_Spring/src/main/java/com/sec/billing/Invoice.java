package com.sec.billing;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.w3c.dom.Element;

@Entity
@Table(name = "invoices")
public class Invoice {

  @Id
  @GeneratedValue
  private long id;
  
  @Lob
  private String xmlString;
  
  private LocalDate fromDate;
  
  private LocalDate endDate;
  
  private String invoiceNumber;
  
  private double invoiceNetAmount;
  
  private double invoiceTaxAmount;
  
  private double invoiceGrossAmount;
  
  private boolean closed;
  
  @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
  List<FeeItem> feeItems = new LinkedList<>();
  
  public Invoice() {}
  
  public Invoice(String xmlString, LocalDate fromDate, LocalDate endDate, String invoiceNumber, Double invoiceNetAmount, Double invoiceTaxAmount, double invoiceGrossAmount) {
    this.xmlString = xmlString;
    this.fromDate = fromDate;
    this.endDate = endDate;
    this.invoiceNumber = invoiceNumber;
    this.invoiceNetAmount = invoiceNetAmount;
    this.invoiceTaxAmount = invoiceTaxAmount;
    this.invoiceGrossAmount = invoiceGrossAmount;
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
  
  public void addFee(FeeItem feeItem) {
    this.feeItems.add(feeItem);
  }

  public double getInvoiceGrossAmount() {
    return invoiceGrossAmount;
  }

  public void setInvoiceGrossAmount(double invoiceGrossAmount) {
    this.invoiceGrossAmount = invoiceGrossAmount;
  }

  public boolean isClosed() {
    return closed;
  }

  public void setClosed(boolean closed) {
    this.closed = closed;
  }
  
}
