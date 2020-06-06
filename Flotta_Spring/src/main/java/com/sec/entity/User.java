package com.sec.entity;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sec.billing.PayDevision;
import com.sec.entity.switchTable.GenSW;
import com.sec.entity.switchTable.UserDev;
import com.sec.entity.switchTable.UserSub;

import javax.persistence.JoinColumn;

@Entity
@Table( name="users" )
public class User extends BasicEntity {

	@Column( unique=true, nullable=false )
	private String email;
	
	@Column( nullable=false )
	private String password;
	
	private String fullName;
	
	private boolean enabled;
	
	private String passwordRenewerKey;
	
  @OneToMany( mappedBy = "user" )
	private Set<UserSub> userSubs;
	
	@OneToMany( mappedBy = "user" )
  private Set<UserDev> userDevs;
	
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
	
	public String getPasswordRenewerKey() {
    return passwordRenewerKey;
  }

  public void setPasswordRenewerKey(String passwordRenewerKey) {
    this.passwordRenewerKey = passwordRenewerKey;
  }


	public Set<UserSub> getUserSubs() {
		return userSubs;
	}

	public void setUserSubs(Set<UserSub> userSubs) {
		this.userSubs = userSubs;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", password=" + password + ", fullName=" + fullName
				+ ", enabled=" + enabled + ", subscriptions=" + Objects.toString(userSubs, "NULL") + ", roles=" + Objects.toString(roles, "NULL") + "]";
	}
	
	public boolean equalsByEmail(User u) {
		if(u == null) {
			return false;
		}
		return email.equals(u.getEmail());
	}
	
	public List<PayDevision> getPayDevs() {
    return payDevs;
  }

  public void setPayDevs(List<PayDevision> payDevs) {
    this.payDevs = payDevs;
  }
  
  public void addPayDevision(PayDevision payDevision) {
    this.payDevs.add(payDevision);
  }
}