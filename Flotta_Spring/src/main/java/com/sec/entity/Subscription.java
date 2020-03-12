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

import com.sec.entity.switchTable.SubDev;
import com.sec.entity.switchTable.SubSim;
import com.sec.entity.switchTable.UserSub;
import com.sec.entity.viewEntity.SubscriptionToView;
import com.sec.enums.SimStatusEnum;
import com.sec.status.SimStatus;

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

    LocalDate last = getLastDeviceModificationDate();
    System.out.println(last);
    SubDev sd = subDev.get(last);
    System.out.println(sd);
    
//    SubDev sd = subDev.get(getLastDeviceModificationDate());
    stv.setDeviceId(sd.getDev() != null ? sd.getDev().getId() : 0);
    stv.setDeviceName(sd.getDev() != null ? sd.getDev().getDeviceType().getName() : "");

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
    return stv;
  }

  public void simModification(Sim sim, String changeReason, LocalDate date) {
    if(sim.isFree()) {
      if (subSim == null) {
        subSim = new HashMap<>();
      }
      
      LocalDate last = getLastSimModificationDate();
      
      if(last != null) {
        if(sim.getId() == subSim.get(last).getId()) {
          return;
        }
        SubSim ss = subSim.get(last);
//        if(date.isAfter(last)) {
//          ss.getSim().addStatus(SimStatusEnum.valueOf(changeReason), date);
//          sim.addStatus(SimStatusEnum.ACTIVE, date);
//          subSim.put(date, new SubSim(this, sim, date));
//        } else if(date.isEqual(last)) {
//          ss.getSim().removeLastStatus();
//          sim.addStatus(SimStatusEnum.ACTIVE, date);
//          ss.setSim(sim);
//          ss.setSub(this);
//          ss.setConnect(date);
//          subSim.put(date, ss);
//        } else {
//          return;
//        }
        
        subSim.remove(last);
        subSim.put(last, new SubSim(this, sim, date));
        
      }
      
//      sim.addStatus(SimStatusEnum.ACTIVE, date);
//      subSim.put(date, new SubSim(this, sim, date));
    }
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

  public void userModification(User user, LocalDate date) {
    if (subUsers == null || subUsers.isEmpty()) {
      subUsers = new HashMap<>();
      subUsers.put(date, new UserSub(user, this, date));
    } else {
      UserSub us = subUsers.get(date);
      if (us == null) {
        LocalDate last = getLastUserModificationDate();
        if (!usersEquals(subUsers.get(last).getUser(), user)) {
          us = subUsers.get(last);
          us.setDisconnect(date);
          subUsers.put(last, us);
          subUsers.put(date, new UserSub(user, this, date));
        }

      } else {
        if (subUsers.size() == 1 || !usersEquals(subUsers.get(getUserModficationDateListDest().get(1)).getUser(), user)) {
          us.setUser(user);
          subUsers.put(date, us);
        }
      }
    }
  }

  public void deviceModification(Device dev, LocalDate date) {
    if (subDev == null || subDev.isEmpty()) {
      System.out.println("empty subdev");
      subDev = new HashMap<>();
      System.out.println(subDev);
      subDev.put(date, new SubDev(this, dev, date));
      System.out.println(subDev);
    } else {
      SubDev sd = subDev.get(date);
      if (sd == null) {
        LocalDate last = getLastDeviceModificationDate();
        if (!devicesEquals(subDev.get(last).getDev(), dev)) {
          sd = subDev.get(last);
          sd.setDisconnect(date);
          subDev.put(last, sd);
          subDev.put(date, new SubDev(this, dev, date));
        }
      } else {
        if (subUsers.size() == 1 || !devicesEquals(subDev.get(getDeviceModficationDateListDest().get(1)).getDev(), dev)) {
          sd.setDev(dev);
          subDev.put(date, sd);
        }
      }
    }
  }

  private boolean devicesEquals(Device d1, Device d2) {
    return d1 != null ? d1.equals(d2) : (d2 == null ? true : false);
  }

  private boolean usersEquals(User u1, User u2) {
    return u1 != null ? u1.equals(u2) : (u2 == null ? true : false);
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

  public List<LocalDate> getAllModificationDateDesc() {
    Set<LocalDate> dates = new HashSet<>();
    dates.addAll(getUserModficationDateListDest());
    dates.addAll(getSimModficationDateListDest());
    dates.addAll(getDeviceModficationDateListDest());

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
