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
import com.sec.billing.BillPartitionTemplate;
import com.sec.billing.Category;
import com.sec.billing.FeeItem;
import com.sec.billing.exception.UnknonwFeeItemDescriptionException;
import com.sec.billing.repository.BillPartitionTemplateRepository;

@Service
public class BillPartitionTemplateService {
  
  private BillPartitionTemplateRepository billPartitionTemplateRepository;
  
  private Map<Long, List<String> > templateMissingFeeItemDescription = new HashMap<Long, List<String> >();
  
  @Autowired
  public void setBillPartitionTemplateRepository(BillPartitionTemplateRepository billPartitionTemplateRepository) {
    this.billPartitionTemplateRepository = billPartitionTemplateRepository;
  }

  public void save(BillPartitionTemplate bpt) {
    BillPartitionTemplate saved = billPartitionTemplateRepository.findByName(bpt.getName());
    if(saved == null) {
      billPartitionTemplateRepository.save(bpt);
    }
  }

  public BillPartitionTemplate findById(long id) {
    return billPartitionTemplateRepository.findOne(id);
  }

  public Map<Category, List<FeeItem> > partition(Bill bill, long templateId) {
    BillPartitionTemplate bpt = billPartitionTemplateRepository.findOne(templateId);
    try {
      return bpt.partition(bill);
    } catch (UnknonwFeeItemDescriptionException e) {
      templateMissingFeeItemDescription.put(templateId, e.getUnknownDescriptions());
    }
    return null;
  }
  
  public List<String> getTemplateMissingFeeItemDescription(long templateId) {
    List<String> descriptions = templateMissingFeeItemDescription.remove(templateId);
    Collections.sort(descriptions);
    return descriptions;
  }

  public void upgradeBillPartitionTemplate(long templateId, List<String> descriptions, List<Category> categories) {
    BillPartitionTemplate bpt = billPartitionTemplateRepository.findOne(templateId);
    Map<String, Category> connection = bpt.getConnection();
    
    int i = 0;
    for(String description : descriptions) {
      connection.put(description, categories.get(i));
      i++;
    }
    
    billPartitionTemplateRepository.save(bpt);
  }

  public List<BillPartitionTemplate> findAll() {
    return billPartitionTemplateRepository.findAll();
  }

  public List<String> findAllBillDescription() {
    Set<String> descriptions = new HashSet<>();
    for(BillPartitionTemplate template: billPartitionTemplateRepository.findAll()) {
      descriptions.addAll(template.getConnection().keySet());
    }
    List<String> result = new LinkedList<>(descriptions);
    Collections.sort(result);
    return result;
  }
  
}
