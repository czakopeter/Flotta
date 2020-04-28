package com.sec.billing.exception;

import java.util.List;
import java.util.Set;

public class UnknonwFeeItemDescriptionException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = 6078416202624522162L;

  private List<String> unknownDescriptions;
  
  public UnknonwFeeItemDescriptionException(Set<String> unknownFreeItemDesc) {
    for(String s : unknownFreeItemDesc) {
      this.unknownDescriptions.add(s);
    }
  }

  public List<String> getUnknownDescriptions() {
    return unknownDescriptions;
  }
  
} 
