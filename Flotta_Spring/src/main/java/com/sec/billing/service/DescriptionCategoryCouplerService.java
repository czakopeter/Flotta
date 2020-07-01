package com.sec.billing.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sec.billing.Bill;
import com.sec.billing.DescriptionCategoryCoupler;
import com.sec.billing.Category;
import com.sec.billing.FeeItem;
import com.sec.billing.exception.UnknonwFeeItemDescriptionException;
import com.sec.billing.repository.DescriptionCategoryCouplerRepository;

@Service
public class DescriptionCategoryCouplerService {
  
  private DescriptionCategoryCouplerRepository descriptionCategoryCouplerRepository;
  
  private Map<Long, List<String> > missingFeeItemDescriptions = new HashMap<Long, List<String> >();
  
  @Autowired
  public void setDescriptionCategoryCouplerRepository(DescriptionCategoryCouplerRepository descriptionCategoryCouplerRepository) {
    this.descriptionCategoryCouplerRepository = descriptionCategoryCouplerRepository;
  }

  public void save(DescriptionCategoryCoupler bpt) {
    DescriptionCategoryCoupler saved = descriptionCategoryCouplerRepository.findByName(bpt.getName());
    if(saved == null) {
      descriptionCategoryCouplerRepository.save(bpt);
    }
  }

  public DescriptionCategoryCoupler findById(long id) {
    return descriptionCategoryCouplerRepository.findOne(id);
  }

  public Map<Category, List<FeeItem> > partition(Bill bill, long templateId) {
    DescriptionCategoryCoupler bpt = descriptionCategoryCouplerRepository.findOne(templateId);
    try {
      return bpt.partition(bill);
    } catch (UnknonwFeeItemDescriptionException e) {
      missingFeeItemDescriptions.put(templateId, e.getUnknownDescriptions());
    }
    return null;
  }
  
  public List<String> getMissingFeeItemDescription(long id) {
    List<String> descriptions = missingFeeItemDescriptions.remove(id);
    Collections.sort(descriptions);
    return descriptions;
  }

  public void upgradeBillPartitionTemplate(long templateId, List<String> descriptions, List<Category> categories) {
    DescriptionCategoryCoupler bpt = descriptionCategoryCouplerRepository.findOne(templateId);
    Map<String, Category> connection = bpt.getDescriptionCategoryMap();
    
    int i = 0;
    for(String description : descriptions) {
      connection.put(description, categories.get(i));
      i++;
    }
    
    descriptionCategoryCouplerRepository.save(bpt);
  }

  public List<DescriptionCategoryCoupler> findAll() {
    return descriptionCategoryCouplerRepository.findAll();
  }

  public List<String> findAllBillDescription() {
    Set<String> descriptions = new HashSet<>();
    for(DescriptionCategoryCoupler template: descriptionCategoryCouplerRepository.findAll()) {
      descriptions.addAll(template.getDescriptionCategoryMap().keySet());
    }
    List<String> result = new LinkedList<>(descriptions);
    Collections.sort(result);
    return result;
  }
  
}
