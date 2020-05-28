package com.sec.entity;

import java.time.LocalDate;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public abstract class BasicEntity implements MapManipulation {
  @Id
  @GeneratedValue
  private long id;
  
  private LocalDate lastMod;
}
