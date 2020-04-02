package com.sec.entity.note;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sec.entity.Device;

@Entity
@Table( name = "dev_note" )
public class DevNote {

  @Id
  @GeneratedValue
  private Long id;
  
  private String note;

  @ManyToOne
  private Device dev;

  private LocalDate date;


  public DevNote() {
  }

  public DevNote(Device dev, String note, LocalDate date) {
    this.dev = dev;
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

  public Device getDev() {
    return dev;
  }

  public void setDev(Device dev) {
    this.dev = dev;
  }
  
  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }
  
}
