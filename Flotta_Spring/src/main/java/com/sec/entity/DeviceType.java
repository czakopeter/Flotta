package com.sec.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "deviceTypes")
public class DeviceType {

  @Id
  @GeneratedValue
  private Long id;

  private boolean visible;

  private String brand;

  private String model;
  
  private String name;

  private int simNumber;

  private boolean microsd;

  @OneToMany(mappedBy = "deviceType")
  private List<Device> devices;

  //TODO description or attributes; sim type, battery, size ...
  
  public DeviceType() {
    visible = true;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public boolean isVisible() {
    return visible;
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand.trim().replaceAll("\\s+", " ");
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model.trim().replaceAll("\\s+", " ");
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name.trim().replaceAll("\\s+", " ");
  }

  public int getSimNumber() {
    return simNumber;
  }

  public void setSimNumber(int simNumber) {
    this.simNumber = simNumber;
  }

  public boolean isMicrosd() {
    return microsd;
  }

  public void setMicrosd(boolean microsd) {
    this.microsd = microsd;
  }

  public List<Device> getDevices() {
    return devices;
  }

  public void setDevices(List<Device> devices) {
    this.devices = devices;
  }

  @Override
  public String toString() {
    return "DeviceType [id=" + id + ", visible=" + visible + ", brand=" + brand + ", model=" + model + ", name=" + name + ", simNumber=" + simNumber + ", microsd=" + microsd + "]";
  }
  
  
  
}