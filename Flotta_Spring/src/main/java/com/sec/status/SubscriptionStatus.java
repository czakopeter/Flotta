package com.sec.status;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sec.entity.Subscription;
import com.sec.enums.SubscriptionStatusEnum;


@Entity
@Table(name = "sub_status")
public class SubscriptionStatus {

  @Id
  @GeneratedValue
  private long id;
  
  @ManyToOne(fetch = FetchType.LAZY)
  private Subscription subscription;
  
  private SubscriptionStatusEnum status;
  
  private LocalDate date;
  
  public SubscriptionStatus() {
  }

  public SubscriptionStatus(SubscriptionStatusEnum status, Subscription subscription, LocalDate date) {
    this.status = status;
    this.subscription = subscription;
    this.date = date;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Subscription getSubscription() {
    return subscription;
  }

  public void setSubscription(Subscription subscription) {
    this.subscription = subscription;
  }

  public SubscriptionStatusEnum getStatus() {
    return status;
  }

  public void setStatus(SubscriptionStatusEnum status) {
    this.status = status;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public boolean isFree() {
    return status.isFree();
  }

}
