package com.czp.utility;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.czp.entity.BasicEntity;
import com.czp.entity.switchTable.BasicSwitchTable;

public class Utility {
  
  public static LocalDate getLatestDate(Map<LocalDate, ? extends BasicSwitchTable> map) {
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
  
  public static <E extends BasicEntity> boolean isSameByIdOrBothNull(E e1, E e2) {
    if(e1 == null && e2 == null) {
      return true;
    }
    if(e1 == null || e2 == null) {
      return false;
    }
    return e1.equals(e2);
  }
  
//  https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
  public static double round(double value, int places) {
    if (places < 0) throw new IllegalArgumentException();

    BigDecimal bd = BigDecimal.valueOf(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
}
}