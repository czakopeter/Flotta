package com.sec.billing.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sec.billing.Category;
import com.sec.billing.PayDivision;
import com.sec.billing.repository.PayDivisionRepository;

@Service
public class PayDivisionService {

  private PayDivisionRepository payDevisionRepository;

  
  public void setPayDevisionRepository(PayDivisionRepository payDevisionRepository) {
    this.payDevisionRepository = payDevisionRepository;
  }
  
  @Autowired
  public PayDivisionService(PayDivisionRepository payDevisionRepository) {
    setPayDevisionRepository(payDevisionRepository);
  }

  public PayDivision get(long id) {
    return payDevisionRepository.findOne(id);
  }

  public boolean addPayDivision(PayDivision payDevision, List<Category> categories, List<Integer> scales) {
    PayDivision check = payDevisionRepository.findByName(payDevision.getName());
    
    if(check == null && categories.size() == scales.size()) {
      payDevision.setAvailable(true);
      for(int i = 0; i < scales.size(); i++ ) {
        payDevision.add(categories.get(i), scales.get(i));
      }
      payDevisionRepository.save(payDevision);
      return true;
    }
      //hiba
    return false;
  }

  public List<PayDivision> findAll() {
    return payDevisionRepository.findAll();
  }

  public PayDivision findPayDivisionById(long id) {
    return payDevisionRepository.findOne(id);
  }

  public boolean editPayDivision(long id, List<Category> categories, List<Integer> scales) {
    PayDivision entity = payDevisionRepository.findOne(id);
    System.out.println(entity == null);
    System.out.println(categories.size());
    System.out.println(scales.size());
    if(entity != null && categories.size() == scales.size()) {
      for(int i = 0; i < scales.size(); i++) {
        entity.add(categories.get(i), scales.get(i));
        payDevisionRepository.save(entity);
      }
      return true;
    }
    return false;
  }

}
