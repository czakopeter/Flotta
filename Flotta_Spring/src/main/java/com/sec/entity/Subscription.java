package com.sec.entity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sec.entity.note.SubNote;
import com.sec.entity.switchTable.SubDev;
import com.sec.entity.switchTable.SubSim;
import com.sec.entity.switchTable.UserSub;
import com.sec.entity.viewEntity.SubscriptionToView;
import com.sec.status.SubscriptionStatus;

@Entity
@Table(name = "subscriptions")
public class Subscription {

  @Id
  @GeneratedValue
  private Long id;

  private String number;

  @OneToMany(mappedBy = "sub", cascade = CascadeType.ALL)
  @MapKey(name = "connect")
  private Map<LocalDate, SubSim> subSim = new HashMap<>();

  @OneToMany(mappedBy = "sub", cascade = CascadeType.ALL)
  @MapKey(name = "connect")
  private Map<LocalDate, UserSub> subUsers = new HashMap<>();

  @OneToMany(mappedBy = "sub", cascade = CascadeType.ALL)
  @MapKey(name = "connect")
  private Map<LocalDate, SubDev> subDev = new HashMap<>();

  @OneToMany(mappedBy = "sub")
  @MapKey(name = "date")
  private Map<LocalDate, SubNote> notes = new HashMap<>();
  
  @OneToMany(mappedBy = "sub")
  @MapKey(name = "date")
  private Map<LocalDate, SubscriptionStatus> statuses = new HashMap<>();
  
  public Subscription() {
  }

  public Subscription(String number) {
    this.number = number;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  @Override
  public String toString() {
    return "Subscription [id=" + id + ", number=" + number + ", subSim=" + subSim + ", subUsers=" + subUsers + "]";
  }

  public SubscriptionToView toView() {
    SubscriptionToView stv = new SubscriptionToView();
    stv.setId(id);
    stv.setNumber(number);
    stv.setDate(getAllModificationDateDesc().get(0));
    stv.setEditable(true);
    stv.setMin(getAllModificationDateDesc().get(0).toString());

    stv.setImei(subSim.get(getLastSimModificationDate()).getSim().getImei());

    UserSub us = subUsers.get(getLastUserModificationDate());
    stv.setUserId(us.getUser() != null ? us.getUser().getId() : 0);
    stv.setUserName(us.getUser() != null ? us.getUser().getFullName() : "");

    SubDev sd = subDev.get(getLastDeviceModificationDate());
    
    stv.setDeviceId(sd.getDev() != null ? sd.getDev().getId() : 0);
    stv.setDeviceName(sd.getDev() != null ? sd.getDev().getDeviceType().getName() : "");
    
    LocalDate sn = getLastNoteModificationDate();
    stv.setNote(sn == null ? "" : notes.get(sn).getNote());

    return stv;
  }

  public SubscriptionToView toView(LocalDate date) {
    SubscriptionToView stv = new SubscriptionToView();
    stv.setId(id);
    stv.setNumber(number);
    stv.setDate(date);
    stv.setEditable(!date.isBefore(getAllModificationDateDesc().get(0)));
    stv.setMin(date.toString());

    LocalDate simModDate = floorDate(new LinkedList<>(subSim.keySet()), date);
    stv.setImei(subSim.get(simModDate).getSim().getImei());

    LocalDate userModDate = floorDate(new LinkedList<>(subUsers.keySet()), date);
    UserSub us = subUsers.get(userModDate);

    stv.setUserId(us.getUser() != null ? us.getUser().getId() : 0);
    stv.setUserName(us.getUser() != null ? us.getUser().getFullName() : "");

    LocalDate deviceModDate = floorDate(new LinkedList<>(subDev.keySet()), date);
    SubDev sd = subDev.get(deviceModDate);

    stv.setDeviceId(sd == null || sd.getDev() == null ? 0 : sd.getDev().getId());
    stv.setDeviceName(sd == null || sd.getDev() == null ? "" : sd.getDev().getDeviceType().getName());
    
    LocalDate noteModDate = floorDate(new LinkedList<>(notes.keySet()), date);
    SubNote sn = notes.get(noteModDate);
    stv.setNote(sn != null ? sn.getNote() : "");
    return stv;
  }

  private LocalDate getLastSimModificationDate() {
    try {
      return getSimModficationDateListDest().get(0);
    } catch (IndexOutOfBoundsException e) {
      return null;
    }
  }

  private List<LocalDate> getSimModficationDateListDest() {
    List<LocalDate> dates = new LinkedList<>(subSim.keySet());
    Collections.sort(dates, Collections.reverseOrder());
    return dates;
  }

  private LocalDate getLastUserModificationDate() {
    try {
      return getUserModficationDateListDest().get(0);
    } catch (IndexOutOfBoundsException e) {
      return null;
    }
  }

  private List<LocalDate> getUserModficationDateListDest() {
    List<LocalDate> dates = new LinkedList<>(subUsers.keySet());
    Collections.sort(dates, Collections.reverseOrder());
    return dates;
  }

  private LocalDate getLastDeviceModificationDate() {
    try {
      return getDeviceModficationDateListDest().get(0);
    } catch (IndexOutOfBoundsException e) {
      return null;
    }
  }

  private List<LocalDate> getDeviceModficationDateListDest() {
    List<LocalDate> dates = new LinkedList<>(subDev.keySet());
    Collections.sort(dates, Collections.reverseOrder());
    return dates;
  }
  
  private LocalDate getLastNoteModificationDate() {
    if(notes.isEmpty()) {
      return null;
    } else {
      List<LocalDate> dates = new LinkedList<>(notes.keySet());
      Collections.sort(dates, Collections.reverseOrder());
      return dates.get(0);
    }
    
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

  private LocalDate floorDate(List<LocalDate> dates, LocalDate date) {
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
}
