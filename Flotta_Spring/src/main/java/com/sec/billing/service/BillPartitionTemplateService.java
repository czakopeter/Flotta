package com.sec.billing.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    billPartitionTemplateRepository.save(bpt);
  }

  public BillPartitionTemplate findTemplate(long templateId) {
    return billPartitionTemplateRepository.findOne(templateId);
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
    return templateMissingFeeItemDescription.remove(templateId);
  }

  public void upgradeBillPartitionTemplate(long templateId, List<String> descriptions, List<Long> categories) {
    // TODO Auto-generated method stub
    
  }
  
}
