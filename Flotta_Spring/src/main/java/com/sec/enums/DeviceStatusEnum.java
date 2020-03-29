package com.sec.enums;

public enum DeviceStatusEnum {
  FREE(0),
  ACTIVE(1),
  REFUSED(2),
  SOLD(3),
  STOLE(4),
  LOST(5);
  
  private int code;
  
  private DeviceStatusEnum(int code) {
    this.code = code;
  }
  
  public int getCode( ) {
    return code;
  }
  
  public boolean isFree() {
    return 0 == this.code;
  }
}