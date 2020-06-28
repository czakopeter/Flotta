package com.sec.entity.note;

import java.security.InvalidParameterException;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sec.entity.Device;
import com.sec.entity.Subscription;
import com.sec.entity.switchTable.BasicSwitchTable;
import com.sec.entity.switchTable.SubDev;

@Entity
@Table( name = "sub_note" )
public class SubNote extends BasicSwitchTable {

  private String note;

  @ManyToOne
  private Subscription sub;

  public SubNote() {
  }

  public SubNote(Subscription sub, String note, LocalDate date) {
    this.sub = sub;
    this.note = note;
    this.beginDate = date;
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
  
  @Override
  public <Other extends BasicSwitchTable> boolean isSameSwitchedPairs(Other other) {
    if(other == null) {
      throw new NullPointerException();
    }
    if(!(other instanceof SubNote)) {
      throw new InvalidParameterException();
    }
    SubNote act = (SubNote)other;
    
    return (act.note == null && this.note == null || this.note.equals(act.note)) && Subscription.isSameByIdOrBothNull(this.sub, act.sub);
  }
  
}
