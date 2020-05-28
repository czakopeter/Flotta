package com.sec.entity.note;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sec.entity.Device;

@Entity
@Table( name = "notes" )
public class Note {

  @Id
  @GeneratedValue
  private long id;
  
  private String note;

  private LocalDate date;

  public Note() {
  }

  public Note(String note, LocalDate date) {
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

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }
  
}
