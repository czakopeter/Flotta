package com.sec.billing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sec.billing.PayDevision;
import com.sec.billing.repository.PayDevisionRepository;

@Service
public class PayDevisionService {

  private PayDevisionRepository payDevisionRepository;

  
  public void setPayDevisionRepository(PayDevisionRepository payDevisionRepository) {
    this.payDevisionRepository = payDevisionRepository;
  }
  
  @Autowired
  public PayDevisionService(PayDevisionRepository payDevisionRepository) {
    setPayDevisionRepository(payDevisionRepository);
  }

  public PayDevision get(long id) {
    return payDevisionRepository.findOne(id);
  }

}
