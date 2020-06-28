package com.sec.entity.switchTable;

import java.security.InvalidParameterException;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.sec.entity.Device;
import com.sec.entity.Subscription;

@Entity
@Table( name="sub_dev_st")
public class SubDev extends BasicSwitchTable {
	
	@ManyToOne()
	@JoinColumn( name = "sub_id")
	private Subscription sub;
	
	@ManyToOne()
	@JoinColumn( name = "dev_id")
	private Device dev;
	
	@DateTimeFormat (pattern="yyyy-MM-dd")
	private LocalDate disconnect;

	public SubDev() {
	}
	
  public SubDev(Subscription sub, Device dev, LocalDate date) {
    this.sub = sub;
    this.dev = dev;
    this.beginDate = date;
  }

  public Subscription getSub() {
    return sub;
  }

  public void setSub(Subscription sub) {
    this.sub = sub;
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
    return "SubDev [id=" + id + ", sub=" + (sub == null ? "0" : sub.getId()) + ", dev=" + (dev == null ? "0" : dev.getId()) + ", beginDate=" + beginDate + "]";
  }

  @Override
  public <Other extends BasicSwitchTable> boolean isSameSwitchedPairs(Other other) {
    if(other == null) {
      throw new NullPointerException();
    }
    if(!(other instanceof SubDev)) {
      throw new InvalidParameterException();
    }
    SubDev act = (SubDev)other;
    
    return Device.isSameByIdOrBothNull(this.dev, act.dev) && Subscription.isSameByIdOrBothNull(this.sub, act.sub);
  }

}
