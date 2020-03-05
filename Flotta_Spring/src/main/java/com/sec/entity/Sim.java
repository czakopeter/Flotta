package com.sec.entity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.sec.entity.switchTable.SubSim;
import com.sec.enums.SimStatusEnum;
import com.sec.status.SimStatus;

@Entity
@Table(name = "sims")
public class Sim {

  @Id
  @GeneratedValue
  private Long id;

  @Column(unique = true)
  private String imei;

  private String serviceProvider;

  @OneToOne(mappedBy = "sim")
  @JoinColumn(name = "sub_id")
  private SubSim simSub;

  @OneToMany(mappedBy = "sim", cascade = CascadeType.ALL)
  @MapKey(name = "connect")
  private Map<LocalDate, SimStatus> stats = new HashMap<LocalDate, SimStatus>();

  public Sim() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getImei() {
    return imei;
  }

  public void setImei(String imei) {
    this.imei = imei;
  }

  public String getServiceProvider() {
    return serviceProvider;
  }

  public void setServiceProvider(String serviceProvider) {
    this.serviceProvider = serviceProvider;
  }

  public SubSim getSimSub() {
    return simSub;
  }

  public void setSimSub(SubSim simSub) {
    this.simSub = simSub;
  }

  public Map<LocalDate, SimStatus> getStats() {
    return stats;
  }

  public void setStats(Map<LocalDate, SimStatus> stats) {
    this.stats = stats;
  }

  public boolean isFree() {
    List<LocalDate> dates = new LinkedList<>(stats.keySet());
    Collections.sort(dates, Collections.reverseOrder());
    return stats.get(dates.get(0)).isFree();
  }

  public void addStatus(SimStatusEnum status, LocalDate date) {
    stats.put(date, new SimStatus(status, this, date));
  }

  public void removeLastStatus() {
    if(!stats.isEmpty()) {
      List<LocalDate> dates = new LinkedList<>(stats.keySet());
      Collections.sort(dates, Collections.reverseOrder());
      stats.remove(dates.get(0));
      
    }
  }
  
  public String getStatus() {
    List<LocalDate> dates = new LinkedList<>(stats.keySet());
    Collections.sort(dates, Collections.reverseOrder());
    return stats.get(dates.get(0)).getStatus().toString();
  }

}
