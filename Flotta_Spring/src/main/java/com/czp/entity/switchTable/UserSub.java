package com.czp.entity.switchTable;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.czp.entity.Device;
import com.czp.entity.Subscription;
import com.czp.entity.User;
import com.czp.utility.Utility;

@Entity
@Table( name="user_sub_st")
public class UserSub extends BasicSwitchTable {
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn( name = "sub_id")
	private Subscription sub;
	
	@DateTimeFormat (pattern="yyyy-MM-dd")
	private LocalDate disconnect;

	public UserSub() {
	}

	public UserSub(User user, Subscription subscription, LocalDate date) {
		this.user = user;
		this.sub = subscription;
		this.beginDate = date;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Subscription getSub() {
		return sub;
	}

	public void setSub(Subscription sub) {
		this.sub = sub;
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
				", sub=" + (sub != null ? sub.getNumber().toString() : "no sub") + 
				", connect=" + Objects.toString(beginDate, "no connect") +
				", disconnect=" + Objects.toString(disconnect, "no disconnect") + "]";
	}
	
//	static class UserSubDateAscComperator implements Comparator<UserSub> {
//		@Override
//		public int compare(UserSub o1, UserSub o2) {
//			return o1.beginDate.compareTo(o2.beginDate);
//		}
//	}
//	
//	static class UserSubDateDescComperator implements Comparator<UserSub> {
//		@Override
//		public int compare(UserSub o1, UserSub o2) {
//			return o2.beginDate.compareTo(o1.beginDate);
//		}
//	}

	
	@Override
  public <Other extends BasicSwitchTable> boolean isSameSwitchedPairs(Other other) {
    if(other == null) {
      throw new NullPointerException();
    }
    if(!(other instanceof UserSub)) {
      throw new InvalidParameterException();
    }
    UserSub act = (UserSub)other;
    
    return Utility.isSameByIdOrBothNull(this.user, act.user) && Utility.isSameByIdOrBothNull(this.sub, act.sub);
  }
}