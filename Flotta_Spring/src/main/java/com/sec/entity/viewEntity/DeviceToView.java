package com.sec.entity.viewEntity;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class DeviceToView {
  private long id;
  
	private String serialNumber;
	
	private String typeName;
	
	private long userId;
	
	private String userName;
	
	private String number;
	
	@DateTimeFormat (pattern="yyyy-MM-dd")
	private LocalDate date;
	
	private String min;
	
	private boolean editable;
	
	public DeviceToView() {
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

  public String getTypeName() {
    return typeName;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public String getMin() {
    return min;
  }

  public void setMin(String min) {
    this.min = min;
  }

  public boolean isEditable() {
    return editable;
  }

  public void setEditable(boolean editable) {
    this.editable = editable;
  }

  @Override
  public String toString() {
    return "DeviceToView [serialNumber=" + serialNumber + ", typeName=" + typeName + ", date=" + date + ", min=" + min + ", editable=" + editable + "]";
  }
  
  
}
