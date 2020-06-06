package com.sec.entity.switchTable;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.sec.entity.Device;
import com.sec.entity.Sim;
import com.sec.entity.Subscription;
import com.sec.entity.User;

@Entity
@Table( name="user_dev_st")
public class UserDev extends BasicSwitchTable {
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn( name = "dev_id")
	private Device dev;
	
	@DateTimeFormat (pattern="yyyy-MM-dd")
	private LocalDate connect;
	
	@DateTimeFormat (pattern="yyyy-MM-dd")
	private LocalDate disconnect;

	public UserDev() {
	}

	public UserDev(User user, Device device, LocalDate date) {
		this.user = user;
		this.dev = device;
		this.connect = date;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Device getDev() {
    return dev;
  }

  public void setDev(Device dev) {
    this.dev = dev;
  }

  public LocalDate getConnect() {
		return connect;
	}

	public void setConnect(LocalDate connect) {
		this.connect = connect;
	}
	
	public LocalDate getDisconnect() {
		return disconnect;
	}

	public void setDisconnect(LocalDate disconnect) {
		this.disconnect = disconnect;
	}

	@Override
	public String toString() {
		return "UserSub [user=" + (user != null ? user.getFullName().toString() : "no user") + 
				", sub=" + (dev != null ? dev.getSerialNumber().toString() : "no dev") + 
				", connect=" + Objects.toString(connect, "no connect") +
				", disconnect=" + Objects.toString(disconnect, "no disconnect") + "]";
	}

  @Override
  public <Other extends BasicSwitchTable> boolean isSameSwitchedPairs(Other other) {
    if(other == null) {
      throw new NullPointerException();
    }
    if(!(other instanceof UserDev)) {
      throw new InvalidParameterException();
    }
    UserDev act = (UserDev)other;
    
    return User.isSameByIdOrBothNull(this.user, act.user) && Device.isSameByIdOrBothNull(this.dev, act.dev);
  }
}
