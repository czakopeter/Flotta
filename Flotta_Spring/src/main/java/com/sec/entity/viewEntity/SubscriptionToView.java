package com.sec.entity.viewEntity;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class SubscriptionToView {
  private long id;
  
	private String number;
	
	private String imei;
	
	private String imeiChangeReason;
	
	private long userId;
	
	private String userName;
	
	private long deviceId;
	
	private String deviceName;
	
	private String note;
	
  @DateTimeFormat (pattern="yyyy-MM-dd")
	private LocalDate date;
	
	private String min;
	
	private boolean editable;
	
	
	
	public SubscriptionToView() {
	}

	public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getImeiChangeReason() {
    return imeiChangeReason;
  }

  public void setImeiChangeReason(String imeiChangeReason) {
    this.imeiChangeReason = imeiChangeReason;
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

  public long getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(long deviceId) {
    this.deviceId = deviceId;
  }

  public String getDeviceName() {
    return deviceName;
  }

  public void setDeviceName(String deviceName) {
    this.deviceName = deviceName;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
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
    return "SubscriptionToView [number=" + number + ", imei=" + imei + ", imeiChangeReason=" + imeiChangeReason + ", userId=" + userId + ", userName=" + userName + ", deviceId=" + deviceId + ", deviceName=" + deviceName + ", date=" + date + ", min=" + min + "]";
  }

	
}
