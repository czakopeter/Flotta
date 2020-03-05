package com.sec.entity.switchTable;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.sec.entity.Subscription;
import com.sec.entity.User;

@Entity
@Table( name="user_sub_st")
public class UserSub {
	
	@Id
	@GeneratedValue
	Long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn( name = "sub_id")
	private Subscription sub;
	
	@DateTimeFormat (pattern="yyyy-MM-dd")
	private LocalDate connect;
	
	@DateTimeFormat (pattern="yyyy-MM-dd")
	private LocalDate disconnect;

	public UserSub() {
	}

	public UserSub(User user, Subscription subscription, LocalDate date) {
		this.user = user;
		this.sub = subscription;
		this.connect = date;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
				", sub=" + (sub != null ? sub.getNumber().toString() : "no sub") + 
				", connect=" + Objects.toString(connect, "no connect") +
				", disconnect=" + Objects.toString(disconnect, "no disconnect") + "]";
	}
	
	static class UserSubDateAscComperator implements Comparator<UserSub> {
		@Override
		public int compare(UserSub o1, UserSub o2) {
			return o1.connect.compareTo(o2.connect);
		}
	}
	
	static class UserSubDateDescComperator implements Comparator<UserSub> {
		@Override
		public int compare(UserSub o1, UserSub o2) {
			return o2.connect.compareTo(o1.connect);
		}
	}
}
