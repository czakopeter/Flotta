package com.sec.entity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.sec.entity.note.SubNote;
import com.sec.entity.switchTable.BasicSwitchTable;
import com.sec.entity.switchTable.GenSW;
import com.sec.entity.switchTable.SubDev;
import com.sec.entity.switchTable.SubSim;
import com.sec.entity.switchTable.UserSub;
import com.sec.entity.viewEntity.SubscriptionToView;
import com.sec.enums.SimStatusEnum;
import com.sec.status.SubscriptionStatus;

@Entity
@Table(name = "subscriptions")
public class Subscription extends BasicEntity {

  private String number;

  @OneToMany(mappedBy = "sub", cascade = CascadeType.ALL)
  @MapKey(name = "beginDate")
  private Map<LocalDate, SubSim> subSim = new HashMap<>();

  @OneToMany(mappedBy = "sub", cascade = CascadeType.ALL)
  @MapKey(name = "beginDate")
  private Map<LocalDate, UserSub> subUsers = new HashMap<>();

  @OneToMany(mappedBy = "sub", cascade = CascadeType.ALL)
  @MapKey(name = "beginDate")
  private Map<LocalDate, SubDev> subDev = new HashMap<>();

  @OneToMany(mappedBy = "sub", cascade = CascadeType.ALL)
  @MapKey(name = "beginDate")
  private Map<LocalDate, SubNote> notes = new HashMap<>();

  @OneToMany(mappedBy = "sub")
  @MapKey(name = "date")
  private Map<LocalDate, SubscriptionStatus> statuses = new HashMap<>();

  private LocalDate createDate;
  
  private LocalDate firstAvailableDate;
  
  public Subscription() {
  }

  public Subscription(String number) {
    this.number = number;
  }

  public Subscription(String number, LocalDate date) {
    this.number = number;
    this.createDate = date;
    this.firstAvailableDate = date;
    this.subSim.put(date, new SubSim(this, null, date));
    this.notes.put(date, new SubNote(this, "", date));
    this.subDev.put(date, new SubDev(this, null, date));
    this.subUsers.put(date, new UserSub(null, this, date));
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public Map<LocalDate, SubSim> getSubSim() {
    return subSim;
  }

  public void setSubSim(Map<LocalDate, SubSim> subSim) {
    this.subSim = subSim;
  }

  public Map<LocalDate, UserSub> getSubUsers() {
    return subUsers;
  }

  public void setSubUsers(Map<LocalDate, UserSub> subUsers) {
    this.subUsers = subUsers;
  }

  public Map<LocalDate, SubDev> getSubDev() {
    return subDev;
  }

  public void setSubDev(Map<LocalDate, SubDev> subDev) {
    this.subDev = subDev;
  }

  public Map<LocalDate, SubNote> getNotes() {
    return notes;
  }

  public void setNotes(Map<LocalDate, SubNote> notes) {
    this.notes = notes;
  }

  public Map<LocalDate, SubscriptionStatus> getStatuses() {
    return statuses;
  }

  public void setStatuses(Map<LocalDate, SubscriptionStatus> statuses) {
    this.statuses = statuses;
  }

  public LocalDate getCreateDate() {
    return createDate;
  }

  public void setCreateDate(LocalDate createDate) {
    this.createDate = createDate;
  }
  
  public LocalDate getFirstAvailableDate() {
    return firstAvailableDate;
  }

  public void setFirstAvailableDate(LocalDate firstAvailableDate) {
    this.firstAvailableDate = firstAvailableDate;
  }

  @Override
  public String toString() {
    return "Subscription [id=" + id + ", number=" + number + ", subSim=" + subSim + ", subUsers=" + subUsers + "]";
  }

  public SubscriptionToView toView() {
    SubscriptionToView stv = new SubscriptionToView();
    stv.setId(id);
    stv.setNumber(number);
    stv.setDate(firstAvailableDate);
    stv.setMin(firstAvailableDate.toString());
    stv.setEditable(true);

    stv.setSim(subSim.get(getLatestDate(subSim)).getSim());

    stv.setUser(subUsers.get(getLatestDate(subUsers)).getUser());

    stv.setDevice(subDev.get(getLatestDate(subDev)).getDev());

    stv.setNote(notes.get(getLatestDate(notes)).getNote());

    return stv;
  }

  public SubscriptionToView toView(LocalDate date) {
    if (date.isBefore(createDate)) {
    }
      
    SubscriptionToView stv = new SubscriptionToView();
    stv.setId(id);
    stv.setNumber(number);
    stv.setDate(date);
    stv.setMin("");
    stv.setEditable(!date.isBefore(firstAvailableDate));

    stv.setSim(subSim.get(floorDate(subSim, date)).getSim());

    stv.setUser(subUsers.get(floorDate(subUsers, date)).getUser());

    stv.setDevice(subDev.get(floorDate(subDev, date)).getDev());

    stv.setNote(notes.get(floorDate(notes, date)).getNote());
    return stv;
  }

  public List<LocalDate> getAllModificationDateDesc() {
    Set<LocalDate> dates = new HashSet<>();
    dates.addAll(subUsers.keySet());
    dates.addAll(subSim.keySet());
    dates.addAll(subDev.keySet());
    dates.addAll(notes.keySet());

    List<LocalDate> result = new LinkedList<>(dates);
    Collections.sort(result, Collections.reverseOrder());
    return result;
  }

  public void addSim(Sim sim, String reason, LocalDate date) {
    if (subSim == null) {
      System.out.println("Subscription: subSim switch table can't be null");
    } else if (subSim.isEmpty()) {
      System.out.println("Subscription: subSim switch table can't be empty");
    } else {
      LocalDate lastSimModDate = getLatestDate(subSim);
      SubSim last = subSim.get(lastSimModDate);
      if(last.getSim() == null) {
        sim.setStatus(SimStatusEnum.ACTIVE);
        last.setSim(sim);
      } else if (date.isAfter(lastSimModDate)) {
        if (!Sim.isSameByIdOrBothNull(sim, last.getSim())) {
          subSim.put(date, new SubSim(this, sim, date));
          last.getSim().setStatus(SimStatusEnum.CHANGED);
          last.getSim().setReason(reason);
          firstAvailableDate = date;
        }
      }
    }
  }

  public void addUser(User user, LocalDate date) {
    System.out.println(areTwoLastAreSame(subUsers));
    
    if (subUsers == null) {
      System.out.println("Subscription: subUser switch table can't be null");
    } else if (subUsers.isEmpty()) {
      System.out.println("Subscription: subUser switch table can't be empty");
    } else {
      LocalDate lastUserModDate = getLatestDate(subUsers);
      if (lastUserModDate == null) {
        System.out.println("Subscription: last user modification date can't be null");
      } else if (date.isAfter(lastUserModDate)) {
        UserSub last = subUsers.get(lastUserModDate);
        if (!User.isSameByIdOrBothNull(user, last.getUser())) {
          subUsers.put(date, new UserSub(user, this, date));
          firstAvailableDate = date;
        }
      } else if (date.isEqual(lastUserModDate)) {
      }
    }
  }
  
  private boolean notValidForAdd(Map<LocalDate, ? extends BasicSwitchTable> map, BasicEntity entity, LocalDate date) {
    if(map == null || map.isEmpty() || date == null) {
      return false;
    }
    return false;
  }

  public void addDevice(Device device, LocalDate date) {
    if (subDev == null || date == null) {
      System.out.println("Subscription: subDev switch table can't be null");
    } else if (subDev.isEmpty()) {
      System.out.println("Subscription: subDev switch table can't be empty");
    } else {
      LocalDate lastDeviceModDate = getLatestDate(subDev);
      if (lastDeviceModDate == null) {
        System.out.println("Subscription: last device modification date can't be null");
      } else if (date.isAfter(lastDeviceModDate)) {
        SubDev last = subDev.get(lastDeviceModDate);
        if (!Device.isSameByIdOrBothNull(device, last.getDev())) {
          subDev.put(date, new SubDev(this, device, date));
          firstAvailableDate = date;
        }
      } else if(date.isEqual(lastDeviceModDate)) {
      }
    }
  }
  
  public User getActualUser() {
    return subUsers.get(getLatestDate(subUsers)).getUser();
  }

  public void addNote(String note, LocalDate date) {
    LocalDate lastNoteModDate = getLatestDate(notes);
    if(lastNoteModDate == null) {
      
    } else if(date.isAfter(lastNoteModDate)) {
      SubNote last = notes.get(lastNoteModDate);
      if(!note.equals(last.getNote())) {
        notes.put(date, new SubNote(this, note, date));
        firstAvailableDate = date;
      }
    } else if(date.isEqual(lastNoteModDate)) {
    }
  }
  
}
