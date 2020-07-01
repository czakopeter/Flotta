package com.sec.entity.viewEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import com.sec.billing.FeeItem;

public class SummaryInvoiceOfNumberForUser {
  private String number;
  
  private LocalDate begin;
  
  private LocalDate end;
  
  private double grossAmount;
  
  public SummaryInvoiceOfNumberForUser() {}
  
  public SummaryInvoiceOfNumberForUser(String number) {
    this.number = number;
  }
  
  public void addFeeItem(FeeItem item) {
    if(item.getSubscription().equals(this.number)) {
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
}
