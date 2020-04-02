package com.sec.entity.switchTable;

import java.time.LocalDate;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.sec.entity.Sim;
import com.sec.entity.Subscription;

@Entity
@Table( name="sub_sim_st")
public class SubSim {
  
  @Id
  @GeneratedValue
  Long id;
  
  @ManyToOne(cascade = CascadeType.ALL)
//  @ManyToOne
  @JoinColumn( name = "sub_id")
  private Subscription sub;
  
  @OneToOne(cascade = CascadeType.ALL)
//  @OneToOne
  @JoinColumn( name = "sim_id")
  private Sim sim;
  
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate connect;

  public SubSim() {
  }

  public SubSim(Subscription sub, Sim sim, LocalDate connect) {
    this.sub = sub;
    this.sim = sim;
    this.connect = connect;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Subscription getSub() {
    return sub;
  }

  public void setSub(Subscription sub) {
    this.sub = sub;
  }

  public Sim getSim() {
    return sim;
  }

  public void setSim(Sim sim) {
    this.sim = sim;
  }

  public LocalDate getConnect() {
    return connect;
  }

  public void setConnect(LocalDate connect) {
    this.connect = connect;
  }

//  public LocalDate getDisconnect() {
//    return disconnect;
//  }
//
//  public void setDisconnect(LocalDate disconnect) {
//    this.disconnect = disconnect;
//  }
}
