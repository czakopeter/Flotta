package com.sec.entity;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.sec.entity.note.Note;
import com.sec.entity.viewEntity.DeviceToView;
import com.sec.status.DeviceStatus;

@Entity
@Table(name = "devices")
public class Device {

  @Id
  @GeneratedValue
  private long id;

  private String serialNumber;
  
  @OneToOne(mappedBy = "device")
  private Subscription subscription;

  @ManyToOne
  @JoinColumn(name = "type_id")
  private DeviceType deviceType;

  @ManyToOne(cascade = CascadeType.ALL)
  private User user;

  @OneToOne
  private Note note;

  @ManyToOne(cascade = CascadeType.ALL)
  private DeviceStatus status;
  
  private LocalDate last;
  
  public Device() {
  }

  public Device(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public Device(String serialNumber, DeviceType deviceType) {
    this.serialNumber = serialNumber;
    this.deviceType = deviceType;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getSerialNumber() {
    return serialNumber;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public Subscription getSubscription() {
    return subscription;
  }

  public void setSubscription(Subscription subscription) {
    this.subscription = subscription;
  }

  public DeviceType getDeviceType() {
    return deviceType;
  }

  public void setDeviceType(DeviceType deviceType) {
    this.deviceType = deviceType;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Note getNote() {
    return note;
  }

  public void setNote(Note note) {
    this.note = note;
  }

  public DeviceStatus getStatus() {
    return status;
  }

  public void setStatus(DeviceStatus status) {
    this.status = status;
  }
  
  public LocalDate getLast() {
    return last;
  }

  public void setLast(LocalDate last) {
    this.last = last;
  }

  public DeviceToView toViewForEditing() {
    return toView(true);
  }
  
  public DeviceToView toViewForViewing() {
    return toView(false);
  }
  
  private DeviceToView toView(boolean editable) {
    DeviceToView dtv = new DeviceToView();
    dtv.setId(id);
    dtv.setSerialNumber(serialNumber);
    dtv.setTypeName(deviceType.getName());
    dtv.setNumber(subscription == null ? "" : subscription.getNumber());
    dtv.setUserId(user == null ? 0 : user.getId());
    dtv.setUserName(user == null ? "" : user.getFullName() + " (" + user.getEmail() + ")");
    dtv.setNote(note == null ? "" : note.getNote());
    dtv.setMin(last.toString());
    dtv.setDate(last);
    dtv.setEditable(editable);

    return dtv;
  }

}
