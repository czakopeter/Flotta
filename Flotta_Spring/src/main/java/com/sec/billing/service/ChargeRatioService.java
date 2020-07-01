package com.sec.billing.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sec.billing.Category;
import com.sec.billing.ChargeRatioByCategory;
import com.sec.billing.repository.ChargeRatioRepository;

@Service
public class ChargeRatioService {

  private ChargeRatioRepository chargeRatioRepository;

  @Autowired
  public void setChargeRatioRepository(ChargeRatioRepository chargeRatioRepository) {
    this.chargeRatioRepository = chargeRatioRepository;
  }
  
  public ChargeRatioService() {}

  public boolean addChargeRatio(ChargeRatioByCategory chargeRatio, List<Category> categories, List<Integer> ratios) {
    ChargeRatioByCategory check = chargeRatioRepository.findByName(chargeRatio.getName());
    
    if(check == null && categories.size() == ratios.size()) {
      chargeRatio.setAvailable(true);
      for(int i = 0; i < ratios.size(); i++ ) {
        chargeRatio.add(categories.get(i), ratios.get(i));
      }
      chargeRatioRepository.save(chargeRatio);
      return true;
    }
      //hiba
    return false;
  }

  public List<ChargeRatioByCategory> findAll() {
    return chargeRatioRepository.findAll();
  }

  public ChargeRatioByCategory findChargeRatioById(long id) {
    return chargeRatioRepository.findOne(id);
  }

  public boolean editChargeRatio(long id, List<Category> categories, List<Integer> ratios) {
    ChargeRatioByCategory entity = chargeRatioRepository.findOne(id);
    if(entity != null && categories.size() == ratios.size()) {
      for(int i = 0; i < ratios.size(); i++) {
        entity.add(categories.get(i), ratios.get(i));
      }
      chargeRatioRepository.save(entity);
      return true;
    }
    return false;
  }

}
