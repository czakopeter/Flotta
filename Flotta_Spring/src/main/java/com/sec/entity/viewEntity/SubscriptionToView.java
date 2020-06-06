package com.sec.entity.viewEntity;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.sec.entity.Device;
import com.sec.entity.Sim;
import com.sec.entity.User;

public class SubscriptionToView {
  private long id;
  
	private String number;
	
	private String oldImei;
	
	private String imei;
	
	private String simChangeReason;
	
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

	public String getOldImei() {
    return oldImei;
  }

  public void setOldImei(String oldImei) {
    this.oldImei = oldImei;
  }

  public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getSimChangeReason() {
    return simChangeReason;
  }

  public void setSimChangeReason(String simChangeReason) {
    this.simChangeReason = simChangeReason;
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
    return "SubscriptionToView [number=" + number + ", imei=" + imei + ", simChangeReason=" + simChangeReason + ", userId=" + userId + ", userName=" + userName + ", deviceId=" + deviceId + ", deviceName=" + deviceName + ", date=" + date + ", min=" + min + "]";
  }

  public void setUser(User user) {
    this.userId = user != null ? user.getId() : 0;
    this.userName = user != null ? user.getFullName() : "";
  }

  public void setSim(Sim sim) {
    this.oldImei = sim != null ? sim.getImei() : "";
    this.imei = sim != null ? sim.getImei() : "";
  }

  public void setDevice(Device device) {
    this.deviceId = device != null ? device.getId() : 0;
    this.deviceName = device != null ? device.getDeviceType().getName() : "";
  }
	
}
