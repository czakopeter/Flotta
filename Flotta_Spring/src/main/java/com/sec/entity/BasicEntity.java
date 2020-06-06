package com.sec.entity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.springframework.format.annotation.DateTimeFormat;

@MappedSuperclass
public class BasicEntity {
  @Id
  @GeneratedValue
  protected long id;
  
  @DateTimeFormat (pattern="yyyy-MM-dd")
  protected LocalDate firstAvailableDate;
  
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }
  
  public LocalDate getFirstAvailableDate() {
    return firstAvailableDate;
  }

  public void setFirstAvailableDate(LocalDate date) {
    this.firstAvailableDate = date;
  }

  protected LocalDate getLatestDate(Map<LocalDate, ? extends Object> map) {
    if(map == null || map.isEmpty()) {
      return null;
    }
    List<LocalDate> dates = new LinkedList<>(map.keySet());
    Collections.sort(dates, Collections.reverseOrder());
    return dates.get(0);
  }
  
  protected LocalDate floorDate(List<LocalDate> dates, LocalDate date) {
    if (dates == null || dates.isEmpty()) {
      return null;
    }
    if (dates.contains(date)) {
      return date;
    }
    Collections.sort(dates);
    LocalDate r = null;
    for (LocalDate a : dates) {
      if (date.isBefore(a)) {
        break;
      }
      r = a;
    }
    return r;
  }

  public static <E extends BasicEntity> boolean isSameByIdOrBothNull(E e1, E e2) {
    if(e1 == null && e2 == null) {
      return true;
    }
    if(e1 == null || e2 == null) {
      return false;
    }
    return e1.equals(e2);
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    BasicEntity other = (BasicEntity) obj;
    if (id != other.id)
      return false;
    return true;
  }
  
}
