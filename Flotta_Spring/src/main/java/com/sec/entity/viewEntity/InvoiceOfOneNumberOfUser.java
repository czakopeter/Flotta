package com.sec.entity.viewEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.LinkedList;
import java.util.List;

import com.sec.billing.FeeItem;

public class InvoiceOfOneNumberOfUser {
  private String number;
  
  private String invoiceNumber;
  
  private LocalDate begin;
  
  private LocalDate end;
  
  private double grossAmount;
  
  private List<FeeItem> fees = new LinkedList<>();
  
  public InvoiceOfOneNumberOfUser() {}
  
  public InvoiceOfOneNumberOfUser(String number, String invoiceNumber) {
    this.number = number;
    this.invoiceNumber = invoiceNumber;
  }
  
  public void addFeeItem(FeeItem item) {
    if(item.getSubscription().equals(this.number)
//        && item.getInvoice().getInvoiceNumber().equals(this.invoiceNumber)
        ) {
      if(begin == null) {
        begin = item.getBegin();
        end = item.getEnd();
      } else {
        if(item.getBegin().isBefore(begin)) {
          begin = item.getBegin();
        }
        if(item.getEnd().isAfter(end)) {
          end = item.getEnd();
        }
      }
      grossAmount += item.getUserGross();
      fees.add(item);
    }
  }
  
  public String getPeriod() {
    if(begin == null) {
      return "";
    } else {
      return begin.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")) + " - " + end.format(DateTimeFormatter.ofPattern("MM.dd"));
    }
  }
  
  public String getNumber() {
    return this.number;
  }
  
  public double getGrossAmount() {
    return this.grossAmount;
  }
  
  public List<FeeItem> getFees() {
    return fees;
  }
  
  public LocalDate getBegin() {
    return begin;
  }
  
  public LocalDate getEnd() {
    return end;
  }
}
