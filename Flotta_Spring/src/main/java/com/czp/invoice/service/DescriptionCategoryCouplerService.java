package com.czp.invoice.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.czp.invoice.Category;
import com.czp.invoice.DescriptionCategoryCoupler;
import com.czp.invoice.FeeItem;
import com.czp.invoice.Invoice;
import com.czp.invoice.exception.UnknonwFeeItemDescriptionException;
import com.czp.invoice.repository.DescriptionCategoryCouplerRepository;

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

  public Map<Category, List<FeeItem> > partition(Invoice invoice, long templateId) {
    DescriptionCategoryCoupler bpt = descriptionCategoryCouplerRepository.findOne(templateId);
    try {
      return bpt.partition(invoice);
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

  public void upgradeDescriptionCategoryCoupler(long id, List<String> descriptions, List<Category> categories) {
    DescriptionCategoryCoupler bpt = descriptionCategoryCouplerRepository.findOne(id);
    Map<String, Category> descriptionCategoryMap = bpt.getDescriptionCategoryMap();
    
    if(descriptions.size() == categories.size()) {
      for(int i = 0; i < descriptions.size(); i++) {
        descriptionCategoryMap.put(descriptions.get(i), categories.get(i));
      }
      
      descriptionCategoryCouplerRepository.save(bpt);
    }
  }

  public List<DescriptionCategoryCoupler> findAll() {
    return descriptionCategoryCouplerRepository.findAll();
  }

  public List<String> findAllInvoiceDescription() {
    Set<String> descriptions = new HashSet<>();
    for(DescriptionCategoryCoupler coupler : descriptionCategoryCouplerRepository.findAll()) {
      descriptions.addAll(coupler.getDescriptionCategoryMap().keySet());
    }
    List<String> result = new LinkedList<>(descriptions);
    Collections.sort(result);
    return result;
  }
  
}
