package com.sec.entity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.springframework.format.annotation.DateTimeFormat;

import com.sec.entity.switchTable.BasicSwitchTable;

@MappedSuperclass
public class BasicEntity {
  @Id
  @GeneratedValue
  protected long id;
  
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }
  
  protected LocalDate getLatestDate(Map<LocalDate, ? extends BasicSwitchTable> map) {
    if(map == null || map.isEmpty()) {
      throw new IllegalArgumentException("Map should contains at least one element");
    }
    List<LocalDate> dates = new LinkedList<>(map.keySet());
    Collections.sort(dates, Collections.reverseOrder());
    return dates.get(0);
  }
  
  public static LocalDate floorDate(Map<LocalDate, ? extends BasicSwitchTable> map, LocalDate date) {
    if (map == null || map.isEmpty()) {
      return null;
    }
    List<LocalDate> dates = new LinkedList<>(map.keySet());
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
  
  public static boolean areTwoLastAreSame(Map<LocalDate, ? extends BasicSwitchTable> map) {
    if(map == null) {
      throw new NullPointerException();
    }
    if(map.size() > 1) {
      List<LocalDate> dates = new LinkedList<>(map.keySet());
      Collections.sort(dates, Collections.reverseOrder());
      return equals(map.get(dates.get(0)), map.get(dates.get(1)));
    }
    return false;
  }
  
  public static boolean equals(BasicSwitchTable o1, BasicSwitchTable o2) {
    if(o1 == null || o2 == null) {
      throw new NullPointerException();
    }
    if(o1.getClass() != o2.getClass()) {
      throw new IllegalArgumentException();
    }
    
    return o1.isSameSwitchedPairs(o2);
  }
}
