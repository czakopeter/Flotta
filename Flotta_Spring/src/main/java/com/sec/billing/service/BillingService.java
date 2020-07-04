package com.sec.billing.service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sec.billing.Invoice;
import com.sec.billing.DescriptionCategoryCoupler;
import com.sec.billing.Category;
import com.sec.billing.FeeItem;
import com.sec.billing.ChargeRatioByCategory;
import com.sec.billing.exception.FileUploadException;
import com.sec.entity.User;
import com.sec.entity.viewEntity.OneCategoryOfUserFinance;
import com.sec.entity.viewEntity.InvoiceOfOneNumberOfUser;

@Service
public class BillingService {

  private InvoiceService invoiceService;
  
  private CategoryService categoryService;
  
  private DescriptionCategoryCouplerService descriptionCategoryCouplerService;
  
  private ChargeRatioService chargeRatioService;

  @Autowired
  public void setInvoiceService(InvoiceService invoiceService) {
    this.invoiceService = invoiceService;
  }
  
  @Autowired
  public void setCategoryService(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @Autowired
  public void setBillPartitionTemplateService(DescriptionCategoryCouplerService descriptionCategoryCouplerService) {
    this.descriptionCategoryCouplerService = descriptionCategoryCouplerService;
  }
  
  @Autowired
  public void setChargeRatioService(ChargeRatioService chargeRatioService ) {
    this.chargeRatioService = chargeRatioService;
  }
  
  // beolvassa a számlát
  // valid akkor konvertál validFeeItem-re
  // newm valid új template készítése
  public boolean uploadBill(MultipartFile file) throws FileUploadException {
    invoiceService.uploadBill(file);
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

  public List<Invoice> findAllBill() {
    return invoiceService.findAll();
  }

  public Invoice findBilldByInvoiceNumber(String invoiceNumber) {
    return invoiceService.findByInvoiceNumber(invoiceNumber);
  }

  public List<Category> findAllCategory() {
    return categoryService.findAll();
  }

  public boolean addCategory(String category) {
    return categoryService.save(category);
  }

  public List<DescriptionCategoryCoupler> findAllBillPartitionTemplate() {
    return  descriptionCategoryCouplerService.findAll();
  }
  
  public List<FeeItem> findAllFeeItemByBillId(long id) {
    return invoiceService.findAllFeeItemByBillId(id);
  }
  
  public Invoice findBillById(long id) {
    return invoiceService.findById(id);
  }

  public boolean billPartitionByTemplateId(long billId, long templateId) {
    //TODO missing bill or template problem throw exception
    Invoice invoice = invoiceService.findById(billId);
    if(invoice != null) {
      Map<Category, List<FeeItem> >  result =  descriptionCategoryCouplerService.partition(invoice, templateId);
      invoiceService.save(invoice);
      return result == null ?  false : true;
    }
    return false;
  }
  
  public List<String> getUnknownFeeDescToTemplate(long templateId) {
    return descriptionCategoryCouplerService.getMissingFeeItemDescription(templateId);
  }

  public void upgradeBillPartitionTemplate(long templateId, List<String> descriptions, List<Long> categories) {
    descriptionCategoryCouplerService.upgradeBillPartitionTemplate(templateId, descriptions, idListToCategoryList(categories));
  }
  
  private List<Category> idListToCategoryList(List<Long> catIds) {
    List<Category> result = new LinkedList<>();
    for(long id : catIds) {
      result.add(categoryService.findById(id));
    }
    return result;
  }

  public void save(Invoice invoice) {
    invoiceService.save(invoice);
  }

  public List<OneCategoryOfUserFinance> getFinanceByUserId(long id) {
    return invoiceService.getFinanceByUserId(id);
  }

  public void save(List<FeeItem> fees) {
    invoiceService.save(fees);
  }

  public DescriptionCategoryCoupler findBillPartitionTemplateById(long id) {
    return descriptionCategoryCouplerService.findById(id);
  }

  public List<String> findAllBillDescription() {
    return descriptionCategoryCouplerService.findAllBillDescription();
  }

  public boolean addPayDivision(ChargeRatioByCategory payDevision, List<Long> categories, List<Integer> ratios) {
    return chargeRatioService.addChargeRatio(payDevision, idListToCategoryList(categories), ratios);
  }

  public List<ChargeRatioByCategory> findAllChargeRatio() {
    return chargeRatioService.findAll();
  }

  public ChargeRatioByCategory findChargeRatioById(long id) {
    return chargeRatioService.findChargeRatioById(id);
  }

  public boolean editChargeRatio(long id, List<Long> categories, List<Integer> ratios) {
    return chargeRatioService.editChargeRatio(id, idListToCategoryList(categories), ratios);
  }

  public List<Category> getUnusedCategoryOfChargeRatio(long id) {
    List<Category> result = new LinkedList<>(categoryService.findAll());
    ChargeRatioByCategory crbc = chargeRatioService.findChargeRatioById(id);
    result.removeAll(crbc.getCategoryRatioMap().keySet());
    return result;
  }

  public List<InvoiceOfOneNumberOfUser> getActualFinanceSummary() {
    return invoiceService.getActualFinanceSummary();
  }

  public InvoiceOfOneNumberOfUser getUnacceptedInvoiceOfOneNumberOfUser(String number) {
    return invoiceService.getUnacceptedInvoiceOfOneNumberOfUser(number);
  }
  
}
