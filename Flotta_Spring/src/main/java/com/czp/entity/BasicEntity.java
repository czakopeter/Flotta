package com.czp.entity;


import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BasicEntity {
  @Id
  @GeneratedValue
  protected long id;
  
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    BasicEntity other = (BasicEntity) obj;
    if (id != other.id)
      return false;
    return true;
  }
  
}
