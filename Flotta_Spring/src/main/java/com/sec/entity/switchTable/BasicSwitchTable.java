package com.sec.entity.switchTable;

import java.time.LocalDate;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BasicSwitchTable {

  @Id
  @GeneratedValue
  protected long id;
  
  protected LocalDate beginDate;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public LocalDate getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(LocalDate beginDate) {
    this.beginDate = beginDate;
  }
  
  public abstract <Other extends BasicSwitchTable> boolean isSameSwitchedPairs(Other other);
}
