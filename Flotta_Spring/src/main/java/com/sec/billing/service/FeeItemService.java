package com.sec.billing.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sec.billing.FeeItem;
import com.sec.billing.Invoice;
import com.sec.billing.repository.FeeItemRepository;
import com.sec.entity.viewEntity.OneCategoryOfUserFinance;
import com.sec.entity.viewEntity.InvoiceOfOneNumberOfUser;

@Service
public class FeeItemService {

  private FeeItemRepository feeItemRepository;

  @Autowired
  public void setFeeItemRepository(FeeItemRepository feeItemRepository) {
    this.feeItemRepository = feeItemRepository;
  }
  
  public List<OneCategoryOfUserFinance> getFinanceByUserId(long id) {
    Map<String, OneCategoryOfUserFinance> tmp = new HashMap<String, OneCategoryOfUserFinance>();
    for(FeeItem fee : feeItemRepository.findAllByUserId(id)) {
      OneCategoryOfUserFinance o = tmp.get(fee.getCategory());
      if(o == null) {
        o = new OneCategoryOfUserFinance(fee.getUserId(), fee.getCategory());
      }
      o.addFeeItem(fee);
      tmp.put(fee.getCategory(), o);
    }
    return new LinkedList<>(tmp.values());
  }
  
  //TODO upgrade to get fees between begin and end
//  public List<OneCategoryOfUserFinance> getFinance(long userId, LocalDate begin, LocalDate end) {
//    Map<String, OneCategoryOfUserFinance> tmp = new HashMap<String, OneCategoryOfUserFinance>();
//    for(FeeItem fee : feeItemRepository.findAllByUserId(userId)) {
//      OneCategoryOfUserFinance o = tmp.get(fee.getCatergory());
//      if(o == null) {
//        o = new OneCategoryOfUserFinance(fee.getUserId(), fee.getCatergory(), fee.getGross());
//      }
//      o.addFeeItem(fee);
//      tmp.put(fee.getCatergory(), o);
//    }
//    return new LinkedList<>(tmp.values());
//  }

  public List<FeeItem> findAllByBillId(long id) {
    return feeItemRepository.findAllByInvoiceId(id);
  }

  public List<FeeItem> findAllByUserId(long userId) {
    return feeItemRepository.findAllByUserId(userId);
  }

  public void save(List<FeeItem> fees) {
    feeItemRepository.save(fees);
    
  }

  public List<InvoiceOfOneNumberOfUser> getActualFinanceSummary() {
    List<InvoiceOfOneNumberOfUser> result = new LinkedList<>();
    InvoiceOfOneNumberOfUser s = new InvoiceOfOneNumberOfUser("201234567", "1111");
    FeeItem row = new FeeItem();
    row.setBegin(LocalDate.of(2020, 1, 10));
    row.setEnd(LocalDate.of(2020, 1, 20));
    row.setSubscription("201234567");
    row.setUserGross(2000);
    s.addFeeItem(row);
    result.add(s);
    
    s = new InvoiceOfOneNumberOfUser("207654321", "1111");
    
    row = new FeeItem();
    row.setBegin(LocalDate.of(2020, 1, 13));
    row.setEnd(LocalDate.of(2020, 1, 31));
    row.setSubscription("207654321");
    row.setUserGross(1500);
    s.addFeeItem(row);
    result.add(s);
    
    return result;
  }

  public InvoiceOfOneNumberOfUser getUnacceptedInvoiceOfOneNumberOfUser(String number) {
    InvoiceOfOneNumberOfUser result = new InvoiceOfOneNumberOfUser("201234567", "1111");
    FeeItem row = new FeeItem();
    row.setBegin(LocalDate.of(2020, 1, 1));
    row.setEnd(LocalDate.of(2020, 1, 31));
    row.setSubscription("201234567");
    row.setUserGross(2000);
    row.setCategory("Monthly");
    result.addFeeItem(row);
    
    row = new FeeItem();
    row.setBegin(LocalDate.of(2020, 1, 17));
    row.setEnd(LocalDate.of(2020, 1, 31));
    row.setSubscription("201234567");
    row.setUserGross(1500);
    row.setCategory("Internet");
    result.addFeeItem(row);
    return result;
  }
}
