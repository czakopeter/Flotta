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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sec.entity.note.DevNote;
import com.sec.entity.switchTable.SubDev;
import com.sec.entity.switchTable.UserDev;
import com.sec.entity.viewEntity.DeviceToView;
import com.sec.status.DeviceStatus;

@Entity
@Table(name = "devices")
public class Device {

  @Id
  @GeneratedValue
  private Long id;

  private String serialNumber;

  @ManyToOne
  @JoinColumn(name = "type_id")
  private DeviceType deviceType;

  @OneToMany(mappedBy = "dev", cascade = CascadeType.ALL)
  @MapKey( name = "connect")
  private Map<LocalDate, UserDev> devUsers;

  @OneToMany(mappedBy = "sub", cascade = CascadeType.ALL)
  @MapKey( name = "connect")
  private Map<LocalDate, SubDev> devSubs;
  
  @OneToMany(mappedBy = "dev", cascade = CascadeType.ALL)
  @MapKey( name = "date")
  private Map<LocalDate, DevNote> notes;
  
  @OneToMany(mappedBy = "dev", cascade = CascadeType.ALL)
  @MapKey(name = "date")
  private Map<LocalDate, DeviceStatus> statuses = new HashMap<LocalDate, DeviceStatus>();

  public Device() {
  }

  public Device(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public Device(String serialNumber, DeviceType deviceType) {
    this.serialNumber = serialNumber;
    this.deviceType = deviceType;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getSerialNumber() {
    return serialNumber;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public DeviceType getDeviceType() {
    return deviceType;
  }

  public void setDeviceType(DeviceType deviceType) {
    this.deviceType = deviceType;
  }

  public Map<LocalDate, UserDev> getDevUsers() {
    return devUsers;
  }

  public void setDevUsers(Map<LocalDate, UserDev> devUsers) {
    this.devUsers = devUsers;
  }

  public Map<LocalDate, SubDev> getDevSubs() {
    return devSubs;
  }

  public void setDevSubs(Map<LocalDate, SubDev> devSubs) {
    this.devSubs = devSubs;
  }

  public Map<LocalDate, DevNote> getNotes() {
    return notes;
  }

  public void setNotes(Map<LocalDate, DevNote> notes) {
    this.notes = notes;
  }

  public Map<LocalDate, DeviceStatus> getStatuses() {
    return statuses;
  }

  public void setStatuses(Map<LocalDate, DeviceStatus> statuses) {
    this.statuses = statuses;
  }

  public DeviceToView toView() {
    DeviceToView d = new DeviceToView();
    d.setId(id);
    d.setSerialNumber(serialNumber);
    d.setTypeName(deviceType.getName());
    d.setEditable(true);

    LocalDate lastUserMod = getLastUserModificationDate();

    UserDev ud = devUsers.get(lastUserMod);
    d.setUserId(ud.getUser() != null ? ud.getUser().getId() : 0);
    d.setUserName(ud.getUser() != null ? ud.getUser().getFullName() : "");
    
    SubDev sd = devSubs.get(getLastDeviceModificationDate());
    d.setNumber(sd != null ? sd.getSub() != null ? sd.getSub().getNumber() : "" : "" );

    LocalDate last = lastUserMod;

    d.setDate(last);
    d.setMin(last.toString());
    
    LocalDate dn = getLastNoteModificationDate();
    d.setNote(dn == null ? "" : notes.get(dn).getNote());
    return d;
  }
  
  public DeviceToView toView(LocalDate date) {
    DeviceToView dtv = new DeviceToView();
    dtv.setId(id);
    dtv.setSerialNumber(serialNumber);
    dtv.setTypeName(deviceType.getName());
    dtv.setDate(date);
    dtv.setEditable(!date.isBefore(getAllModificationDateDesc().get(0)));
    dtv.setMin(date.toString());
    
    LocalDate userModDate = floorDate(new LinkedList<>(devUsers.keySet()), date);
    UserDev ud = devUsers.get(userModDate);
    dtv.setUserId(ud.getUser() != null ? ud.getUser().getId() : 0);
    dtv.setUserName(ud.getUser() != null ? ud.getUser().getFullName() : "");
    
    LocalDate noteModDate = floorDate(new LinkedList<>(notes.keySet()), date);
    DevNote dn = notes.get(noteModDate);
    dtv.setNote(dn != null ? dn.getNote() : "");
    
    return dtv;
  }

//  public void userModification(User user, LocalDate date) {
//    if (devUsers == null) {
//      devUsers = new HashMap<>();
//      devUsers.put(date, new UserDev(user, this, date));
//    } else {
//      UserDev us = devUsers.get(date);
//      if (us == null) {
//        LocalDate last = getLastUserModificationDate();
//        if (!usersEquals(devUsers.get(last).getUser(), user)) {
//          us = devUsers.get(last);
//          us.setDisconnect(date);
//          devUsers.put(last, us);
//          devUsers.put(date, new UserDev(user, this, date));
//        }
//
//      } else {
//        if (devUsers.size() == 1 || !usersEquals(devUsers.get(getUserModficationDateListDest().get(1)).getUser(), user)) {
//          us.setUser(user);
//          devUsers.put(date, us);
//        }
//      }
//    }
//  }

//  private boolean usersEquals(User u1, User u2) {
//    return u1 != null ? u1.equals(u2) : (u2 == null ? true : false);
//  }

  private LocalDate getLastUserModificationDate() {
    return getUserModficationDateListDest().get(0);
  }
  
  private List<LocalDate> getUserModficationDateListDest() {
    List<LocalDate> dList = new LinkedList<>(devUsers.keySet());
    Collections.sort(dList, Collections.reverseOrder());
    return dList;
  }
  
  private LocalDate getLastDeviceModificationDate() {
    try {
      return getDeviceModficationDateListDest().get(0);
    } catch (IndexOutOfBoundsException e) {
      return null;
    }
    
  }
  
  private List<LocalDate> getDeviceModficationDateListDest() {
    List<LocalDate> dList = new LinkedList<>(devSubs.keySet());
    Collections.sort(dList, Collections.reverseOrder());
    return dList;
  }

  @Override
  public String toString() {
    return "Device [id=" + id + ", serialNumber=" + serialNumber + ", deviceType=" + deviceType + "]";
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
    dates.addAll(devUsers.keySet());
    dates.addAll(notes.keySet());
    
    List<LocalDate> result = new LinkedList<>(dates);
    Collections.sort(result, Collections.reverseOrder());
    return result;
  }
  
  private LocalDate floorDate(List<LocalDate> dates, LocalDate date) {
    if(dates == null || dates.isEmpty()) {
      return null;
    }
    if(dates.contains(date)) {
      return date;
    }
    Collections.sort(dates);
    LocalDate r = null;
    for(LocalDate a : dates) {
      if(date.isBefore(a)) {
        break;
      }
      r = a;
    }
    return r;
  }

  public void subModification(Subscription sub, LocalDate date) {
    devSubs = new HashMap<>();
    devSubs.put(date, new SubDev(sub, this, date));
  }
  
//  public UserDev getLastUserDev() {
//    return devUsers.get(getLastUserModificationDate());
//  }

}
