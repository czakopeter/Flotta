package com.sec.entity;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sec.billing.PayDevision;

import javax.persistence.JoinColumn;

@Entity
@Table( name="users" )
public class User {

	@Id @GeneratedValue
	private Long id;
	
	@Column( unique=true, nullable=false )
	private String email;
	
	@Column( nullable=false )
	private String password;
	
	private String fullName;
	
	private boolean enabled;
	
	@OneToMany(mappedBy = "user")
	private List<Subscription> subscriptions = new LinkedList<>();
	
	@OneToMany( mappedBy = "user" )
	private Set<Device> devices = new HashSet<>();
	
	@ManyToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
	@JoinTable( 
		name = "users_roles",
		joinColumns = {@JoinColumn(name="user_id")}, 
		inverseJoinColumns = {@JoinColumn(name="role_id")}  
	)
	private Set<Role> roles = new HashSet<Role>();

	@ManyToMany
	private List<PayDevision> payDevs = new LinkedList<>();

  public User() {}
	
	public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public List<Subscription> getSubscriptions() {
    return subscriptions;
  }

  public void setSubscriptions(List<Subscription> subscriptions) {
    this.subscriptions = subscriptions;
  }

  public Set<Device> getDevices() {
    return devices;
  }

  public void setDevices(Set<Device> devices) {
    this.devices = devices;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  public List<PayDevision> getPayDevs() {
    return payDevs;
  }

  public void setPayDevs(List<PayDevision> payDevs) {
    this.payDevs = payDevs;
  }

  public void addRoles(String roleName) {
		if (this.roles == null || this.roles.isEmpty()) 
			this.roles = new HashSet<>();
		this.roles.add(new Role(roleName));
	}
	
	public void removeRole(String roleName) {
		if(this.roles != null) {
			this.roles.remove(new Role(roleName));
		}
	}

	public boolean equals(User u) {
		if(u == null) {
			return false;
		}
		return email.equals(u.getEmail());
	}
}