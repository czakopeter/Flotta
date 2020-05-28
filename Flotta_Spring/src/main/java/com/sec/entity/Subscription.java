package com.sec.entity;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.sec.entity.note.Note;
import com.sec.entity.viewEntity.SubscriptionToView;
import com.sec.status.SubscriptionStatus;

@Entity
@Table(name = "subscriptions")
public class Subscription {

  @Id
  @GeneratedValue
  private long id;

  private String number;
  
  @OneToOne(mappedBy = "subscription", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
  private Sim sim;
  
  @ManyToOne
  private User user;
  
  @OneToOne
  private Device device;
  
  @OneToOne
  @JoinColumn(name = "note_id")
  private Note note;
  
  private LocalDate last;
  
  private LocalDate createDate;

  @OneToOne  
  private SubscriptionStatus status;
  
  public Subscription() {
  }

  public Subscription(String number) {
    this.number = number;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getId() {
    return id;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }
  
  public Device getDevice() {
    return device;
  }
  
  public void setDevice(Device device) {
    this.device = device;
  }

  public Sim getSim() {
    return sim;
  }

  public void setSim(Sim sim) {
    this.sim = sim;
    if(sim != null && sim.getSubscription() != this) {
      sim.setSubscription(this);
    }
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

  public SubscriptionStatus getStatus() {
    return status;
  }

  public void setStatus(SubscriptionStatus status) {
    this.status = status;
  }

  public LocalDate getLast() {
    return last;
  }

  public void setLast(LocalDate last) {
    this.last = last;
  }

  public LocalDate getCreateDate() {
    return createDate;
  }

  public void setCreateDate(LocalDate createDate) {
    this.createDate = createDate;
  }

  public SubscriptionToView toViewForEditing() {
    return toView(true);
  }
  
  public SubscriptionToView toViewForViewing() {
    return toView(false);
  }
  
  public SubscriptionToView toView(boolean editable) {
    SubscriptionToView stv = new SubscriptionToView();
    stv.setId(id);
    stv.setNumber(number);
    stv.setOldImei(sim.getImei());
    stv.setImei(sim.getImei());
    stv.setUserId(user == null ? 0 : user.getId());
    stv.setUserName(user == null ? "" : user.getFullName());
    stv.setDeviceId(device == null ? 0 : device.getId());
    stv.setDeviceName(device == null ? "" : device.getDeviceType().getName() + " (" + device.getSerialNumber() + ")");
    stv.setNote(note == null ? "" : note.getNote());
    stv.setMin(last != null ? last.toString() : createDate.toString());
    stv.setDate(last != null ? last : createDate);
    stv.setCreateDate(createDate);
    stv.setEditable(editable);
    return stv;
  }

  @Override
  public String toString() {
    return "Subscription [id=" + id + ", number=" + number + "]";
  }

}
