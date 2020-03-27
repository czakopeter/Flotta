package com.sec.entity.note;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sec.entity.Subscription;

@Entity
@Table( name = "sub_note" )
public class SubNote {

  @Id
  @GeneratedValue
  private Long id;
  
  private String note;

  @ManyToOne
  private Subscription sub;

  private LocalDate date;


  public SubNote() {
  }

  public SubNote(Subscription sub, String note, LocalDate date) {
    this.sub = sub;
    this.note = note;
    this.date = date;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public Subscription getSub() {
    return sub;
  }

  public void setSub(Subscription sub) {
    this.sub = sub;
  }
  
  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }
  
}
