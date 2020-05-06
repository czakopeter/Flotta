package com.sec.billing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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
  
  private double gross;
  
  private String catergory;
  
  private long userId;
  
  private boolean validByUser;
  
  private boolean validByCompany;

  public FeeItem() {
  }

  public FeeItem(Bill bill, String subscription, String description, LocalDate begin, LocalDate end, double netAmount, double taxAmount, double taxPercentage, double gross) {
    this.bill = bill;
    this.subscription = subscription;
    this.description = description;
    this.begin = begin;
    this.end = end;
    this.netAmount = netAmount;
    this.taxAmount = taxAmount;
    this.taxPercentage = taxPercentage;
    this.gross = gross;
  }

  public FeeItem(FeeItem feeItem) {
    this.bill = feeItem.bill;
    this.subscription = feeItem.subscription;
    this.description = feeItem.description;
    this.begin = feeItem.begin;
    this.end = feeItem.end;
    this.netAmount = feeItem.netAmount;
    this.taxAmount = feeItem.taxAmount;
    this.taxPercentage = feeItem.taxPercentage;
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
  
  public double getGross() {
    return gross;
  }

  public void setGross(double gross) {
    this.gross = gross;
  }
  
  public String getCatergory() {
    return catergory;
  }

  public void setCatergory(String catergory) {
    this.catergory = catergory;
  }

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
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
    result.setGross(round(gross * part / all, 2));
    result.setValidByUser(false);
    result.setValidByCompany(false);
    return result;
  }
  
  
  //https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
  public static double round(double value, int places) {
    if (places < 0) throw new IllegalArgumentException();

    BigDecimal bd = BigDecimal.valueOf(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
}

}
