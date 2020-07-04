package com.sec.billing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "fee_items")
public class FeeItem {

  @Id
  @GeneratedValue
  long id;

  @ManyToOne
  private Invoice invoice;

  private String subscription;

  private String description;

  private LocalDate begin;

  private LocalDate end;

  private double netAmount;

  private double taxAmount;

  private double taxPercentage;
  
  private double userGross;
  
  private double compGross;
  
  private String category;
  
  private long userId;
  
  private boolean acceptedByUser;
  
  private boolean acceptedByCompany;

  public FeeItem() {
  }

  public FeeItem(Invoice invoice, String subscription, String description, LocalDate begin, LocalDate end, double netAmount, double taxAmount, double taxPercentage, double userGross) {
    this.invoice = invoice;
    this.subscription = subscription;
    this.description = description;
    this.begin = begin;
    this.end = end;
    this.netAmount = netAmount;
    this.taxAmount = taxAmount;
    this.taxPercentage = taxPercentage;
    this.userGross = userGross;
  }

  public FeeItem(FeeItem feeItem) {
    this.invoice = feeItem.invoice;
    this.subscription = feeItem.subscription;
    this.description = feeItem.description;
    this.begin = feeItem.begin;
    this.end = feeItem.end;
    this.netAmount = feeItem.netAmount;
    this.taxAmount = feeItem.taxAmount;
    this.taxPercentage = feeItem.taxPercentage;
    this.userGross = feeItem.userGross;
    this.compGross = feeItem.compGross;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Invoice getInvoice() {
    return invoice;
  }

  public void setInvoice(Invoice invoice) {
    this.invoice = invoice;
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
  
  public double getUserGross() {
    return userGross;
  }

  public void setUserGross(double userGross) {
    this.userGross = userGross;
  }
  
  public double getCompGross() {
    return compGross;
  }

  public void setCompGross(double compGross) {
    this.compGross = compGross;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public boolean isValidByUser() {
    return acceptedByUser;
  }

  public void setValidByUser(boolean validByUser) {
    this.acceptedByUser = validByUser;
  }

  public boolean isValidByCompany() {
    return acceptedByCompany;
  }

  public void setValidByCompany(boolean validByCompany) {
    this.acceptedByCompany = validByCompany;
  }

  @Override
  public String toString() {
    return "FeeItem [subscription=" + subscription + ", description=" + description + ", begin=" + begin + ", end=" + end + ", netAmount=" + netAmount + ", taxAmount=" + taxAmount + ", taxPercentage=" + taxPercentage + "]";
  }

  public List<FeeItem> splitBeforeDate(List<LocalDate> dates) {
    List<FeeItem> result = new LinkedList<>();
    Collections.sort(dates);
    LocalDate b = begin;
    for (LocalDate date : dates) {
      if (date.isEqual(begin) || date.isBefore(begin) || date.isAfter(end)) {
        // nothing
      } else {
        FeeItem fee = getPartOfFeeItem(b, date);
        
        result.add(fee);
        b = date;
      }
    }
    FeeItem last = getPartOfFeeItem(b, end.plusDays(1));
    last.setId(this.id);
    result.add(last);
    return result;
  }

  // LocalDate b is inclusive
  // LocalDate e is exclusive
  private FeeItem getPartOfFeeItem(LocalDate b, LocalDate e) {
    long all = begin.until(end, ChronoUnit.DAYS) + 1;
    long part = b.until(e, ChronoUnit.DAYS);
    FeeItem result = new FeeItem(this);
    result.setId(0);
    result.setBegin(b);
    result.setEnd(e.minusDays(1));
    result.setNetAmount(round(netAmount * part / all, 2));
    result.setTaxAmount(round(taxAmount * part / all, 2));
    result.setUserGross(round(userGross * part / all, 2));
    result.setCompGross(round(compGross * part / all, 2));
    result.setValidByUser(false);
    result.setValidByCompany(false);
    return result;
  }
  
  
  //https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
  private static double round(double value, int places) {
    if (places < 0) throw new IllegalArgumentException();

    BigDecimal bd = BigDecimal.valueOf(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }

}
