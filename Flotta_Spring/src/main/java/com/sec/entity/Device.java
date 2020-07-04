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

import org.hibernate.validator.constraints.NotEmpty;

import com.sec.entity.note.DevNote;
import com.sec.entity.switchTable.SubDev;
import com.sec.entity.switchTable.UserDev;
import com.sec.entity.viewEntity.DeviceToView;
import com.sec.status.DeviceStatus;

@Entity
@Table(name = "devices")
public class Device extends BasicEntity {

  @Id
  @GeneratedValue
  private Long id;

  private String serialNumber;

  @ManyToOne
  @JoinColumn(name = "type_id")
  private DeviceType deviceType;

  @OneToMany(mappedBy = "dev", cascade = CascadeType.ALL)
  @MapKey(name = "beginDate")
  private Map<LocalDate, UserDev> devUsers;

  @OneToMany(mappedBy = "dev", cascade = CascadeType.ALL)
  @MapKey(name = "beginDate")
  private Map<LocalDate, SubDev> devSubs;

  @OneToMany(mappedBy = "dev", cascade = CascadeType.ALL)
  @MapKey(name = "beginDate")
  private Map<LocalDate, DevNote> notes;

  @OneToMany(mappedBy = "dev", cascade = CascadeType.ALL)
  @MapKey(name = "date")
  private Map<LocalDate, DeviceStatus> statuses = new HashMap<LocalDate, DeviceStatus>();
  
  private LocalDate createDate;

  public Device() {
  }

  public Device(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public Device(String serialNumber, LocalDate date) {
    this.serialNumber = serialNumber;
    this.createDate = date;
    this.deviceType = null;
    devUsers.put(date, new UserDev(null, this, date));
    devSubs.put(date, new SubDev(null, this, date));
    notes.put(date, new DevNote(this, "", date));
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
    DeviceToView dtv = new DeviceToView();
    dtv.setId(id);
    dtv.setSerialNumber(serialNumber);
    dtv.setTypeName(deviceType.getName());
    dtv.setEditable(true);
    
    dtv.setUser(devUsers.get(getLatestDate(devUsers)).getUser());
    
    dtv.setSubscription(devSubs.get(getLatestDate(devSubs)).getSub());
    
    dtv.setNote(notes.get(getLatestDate(notes)).getNote());

    return dtv;
  }

  public DeviceToView toView(LocalDate date) {
    DeviceToView dtv = new DeviceToView();
    dtv.setId(id);
    dtv.setSerialNumber(serialNumber);
    dtv.setTypeName(deviceType.getName());
    dtv.setDate(date);
    dtv.setEditable(!date.isBefore(getAllModificationDateDesc().get(0)));

    dtv.setUser(devUsers.get(floorDate(devUsers, date)).getUser());
    
    dtv.setSubscription(devSubs.get(floorDate(devSubs, date)).getSub());
    
    dtv.setNote(notes.get(floorDate(notes, date)).getNote());
    
    return dtv;
  }

  public void addUser(User user, LocalDate date) {
    LocalDate lastUserModDate = getLatestDate(devUsers);
    if(lastUserModDate == null) {
      
    } else if(date.isAfter(lastUserModDate)) {
      UserDev last = devUsers.get(lastUserModDate);
      if(!User.isSameByIdOrBothNull(user, last.getUser())) {
        devUsers.put(date, new UserDev(user, this, date));
      }
    } else if(date.isEqual(lastUserModDate)) {
      
    }
  }
  
  public void addSubscription(Subscription sub, LocalDate date) {
    LocalDate lastUserModDate = getLatestDate(devUsers);
    if(lastUserModDate == null) {
      
    } else if(date.isAfter(lastUserModDate)) {
      SubDev last = devSubs.get(lastUserModDate);
      if(!Subscription.isSameByIdOrBothNull(sub, last.getSub())) {
        devSubs.put(date, new SubDev(sub, this, date));
      }
    } else if(date.isEqual(lastUserModDate)) {
      
    }
  }
  
  public void addNote(String note, LocalDate date) {
    LocalDate lastUserModDate = getLatestDate(notes);
    if(lastUserModDate == null) {
      
    } else if(date.isAfter(lastUserModDate)) {
      DevNote last = notes.get(lastUserModDate);
      if(!note.equals(last.getNote())) {
        notes.put(date, new DevNote(this, note, date));
      }
    } else if(date.isEqual(lastUserModDate)) {
      
    }
  }

  @Override
  public String toString() {
    return "Device [id=" + id + ", serialNumber=" + serialNumber + ", deviceType=" + deviceType + "]";
  }

  public static boolean isSameByIdOrBothNull(Device d1, Device d2) {
    if(d1 == null && d2 == null) {
      return true;
    }
    if(d1 == null || d2 == null) {
      return false;
    }
    return d1.equals(d2);
  }
  
  public User getActualUser() {
    return devUsers.get(getLatestDate(devUsers)).getUser();
  }
  
  public List<LocalDate> getAllModificationDateDesc() {
    Set<LocalDate> dates = new HashSet<>();
    dates.addAll(devUsers.keySet());
    dates.addAll(devSubs.keySet());
    dates.addAll(notes.keySet());

    List<LocalDate> result = new LinkedList<>(dates);
    Collections.sort(result, Collections.reverseOrder());
    return result;
  }
}
