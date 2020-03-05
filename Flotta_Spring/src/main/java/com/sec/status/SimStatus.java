package com.sec.status;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sec.entity.Sim;
import com.sec.enums.SimStatusEnum;


@Entity
@Table(name = "sim_status")
public class SimStatus {

  @Id
  @GeneratedValue
  private long id;
  
  @ManyToOne(fetch = FetchType.LAZY)
  private Sim sim;
  
  private SimStatusEnum status;
  
  private LocalDate connect;
  
  public SimStatus() {
  }

  public SimStatus(SimStatusEnum status, Sim sim, LocalDate date) {
    this.status = status;
    this.sim = sim;
    this.connect = date;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Sim getSim() {
    return sim;
  }

  public void setSim(Sim sim) {
    this.sim = sim;
  }

  public SimStatusEnum getStatus() {
    return status;
  }

  public void setStatus(SimStatusEnum status) {
    this.status = status;
  }

  public LocalDate getConnect() {
    return connect;
  }

  public void setConnect(LocalDate connect) {
    this.connect = connect;
  }

  public boolean isFree() {
    return status.isFree();
  }

}
