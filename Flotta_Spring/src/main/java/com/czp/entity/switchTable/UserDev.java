package com.czp.entity.switchTable;

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

import com.czp.entity.Device;
import com.czp.entity.Sim;
import com.czp.entity.Subscription;
import com.czp.entity.User;
import com.czp.utility.Utility;

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
	private LocalDate disconnect;

	public UserDev() {
	}

	public UserDev(User user, Device device, LocalDate date) {
		this.user = user;
		this.dev = device;
		this.beginDate = date;
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
				", connect=" + Objects.toString(beginDate, "no beginDate") +
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
    
    return Utility.isSameByIdOrBothNull(this.user, act.user) && Utility.isSameByIdOrBothNull(this.dev, act.dev);
  }
}