package com.sec.billing.service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sec.billing.Bill;
import com.sec.billing.BillPartitionTemplate;
import com.sec.billing.Category;
import com.sec.billing.FeeItem;
import com.sec.billing.exception.FileUploadException;
import com.sec.billing.exception.UnknonwFeeItemDescriptionException;
import com.sec.entity.User;

@Service
public class BillingService {

  private BillService billService;
  
  private CategoryService categoryService;
  
  private BillPartitionTemplateService billPartitionTemplateService;

  @Autowired
  public void setBillService(BillService billService) {
    this.billService = billService;
  }
  
  @Autowired
  public void setCategoryService(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @Autowired
  public void setBillPartitionTemplateService(BillPartitionTemplateService billPartitionTemplateService) {
    this.billPartitionTemplateService = billPartitionTemplateService;
  }
  
  // beolvassa a számlát
  // valid akkor konvertál validFeeItem-re
  // newm valid új template készítése
  public boolean uploadBill(MultipartFile file) throws FileUploadException {
    billService.uploadBill(file);
    return true;
  }

  List<FeeItem> getFeeItemsOfUser(User user) {
    return null;
  }

  List<FeeItem> getFeeItemsOfUser(User user, LocalDate begin, LocalDate end) {
    return null;
  }

  void connectValidFeeItemToCategory(FeeItem item, String category) {

  }

  public List<Bill> findAllBill() {
    return billService.findAll();
  }

  public Bill findBilldByInvoiceNumber(String invoiceNumber) {
    return billService.findByInvoiceNumber(invoiceNumber);
  }

  public List<Category> findAllCategory() {
    return categoryService.findAll();
  }

  public void addCategory(String category) {
    categoryService.save(category);
  }

  public List<BillPartitionTemplate> findAllBillPartitionTemplate() {
    List<BillPartitionTemplate> result = new LinkedList<>();
    BillPartitionTemplate bpt = new BillPartitionTemplate();
    bpt.setId(1);
    bpt.setName("basic");
    List<Category> cList = categoryService.findAll();
    bpt.addToConnection("Mobil telefon szolgaltatas", cList.get(0));
    bpt.addToConnection("Telekom mobilhalozaton belul", cList.get(1));
    bpt.addToConnection("Belfoldi mas mobilhalozat / Telenor", cList.get(1));
    bpt.addToConnection("Belfoldi mas mobilhalozat / Vodafone", cList.get(1));
    bpt.addToConnection("Mobil net 1GB", cList.get(2));
    result.add(bpt);
    
    billPartitionTemplateService.save(bpt);
    
    return result;
  }

  public boolean billPartitionByTemplateId(long billId, long templateId) {
    //TODO missing bill or template problem throw exception
    Bill bill = billService.findById(billId);
    if(bill != null) {
      Map<Category, List<FeeItem> >  result =  billPartitionTemplateService.partition(bill, templateId);
      return result == null ?  false : true;
    }
    return false;
  }

  public List<String> getUnknownFeeDescToTemplate(long templateId) {
    return billPartitionTemplateService.getTemplateMissingFeeItemDescription(templateId);
  }
}
