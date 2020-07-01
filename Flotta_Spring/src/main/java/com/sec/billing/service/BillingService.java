package com.sec.billing.service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sec.billing.Bill;
import com.sec.billing.DescriptionCategoryCoupler;
import com.sec.billing.Category;
import com.sec.billing.FeeItem;
import com.sec.billing.ChargeRatioByCategory;
import com.sec.billing.exception.FileUploadException;
import com.sec.entity.User;
import com.sec.entity.viewEntity.OneCategoryOfUserFinance;
import com.sec.entity.viewEntity.SummaryInvoiceOfNumberForUser;

@Service
public class BillingService {

  private BillService billService;
  
  private CategoryService categoryService;
  
  private DescriptionCategoryCouplerService descriptionCategoryCouplerService;
  
  private ChargeRatioService chargeRatioService;

  @Autowired
  public void setBillService(BillService billService) {
    this.billService = billService;
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

  public boolean addCategory(String category) {
    return categoryService.save(category);
  }

  public List<DescriptionCategoryCoupler> findAllBillPartitionTemplate() {
    return  descriptionCategoryCouplerService.findAll();
  }
  
  public List<FeeItem> findAllFeeItemByBillId(long id) {
    return billService.findAllFeeItemByBillId(id);
  }
  
  public Bill findBillById(long id) {
    return billService.findById(id);
  }

  public boolean billPartitionByTemplateId(long billId, long templateId) {
    //TODO missing bill or template problem throw exception
    Bill bill = billService.findById(billId);
    if(bill != null) {
      Map<Category, List<FeeItem> >  result =  descriptionCategoryCouplerService.partition(bill, templateId);
      billService.save(bill);
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

  public void save(Bill bill) {
    billService.save(bill);
  }

  public List<OneCategoryOfUserFinance> getFinanceByUserId(long id) {
    return billService.getFinanceByUserId(id);
  }

  public void save(List<FeeItem> fees) {
    billService.save(fees);
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

  public List<SummaryInvoiceOfNumberForUser> getActualFinanceSummary() {
    return billService.getActualFinanceSummary();
  }
  
}
