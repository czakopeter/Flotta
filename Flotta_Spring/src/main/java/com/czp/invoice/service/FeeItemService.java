package com.czp.invoice.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.czp.entity.User;
import com.czp.entity.switchTable.UserDev;
import com.czp.entity.viewEntity.InvoiceOfUserByNumber;
import com.czp.entity.viewEntity.OneCategoryOfUserFinance;
import com.czp.invoice.FeeItem;
import com.czp.invoice.Invoice;
import com.czp.invoice.repository.FeeItemRepository;
import com.czp.service.UserDetailsImpl;

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

//  public List<FeeItem> findAllByInvoiceId(long id) {
//    return feeItemRepository.findAllByInvoiceId(id);
//  }

  public List<FeeItem> findAllByUserId(long userId) {
    return feeItemRepository.findAllByUserId(userId);
  }

  public void save(List<FeeItem> fees) {
    feeItemRepository.save(fees);
    
  }

  //TODO szétválasztani ha egy számnak több számlája is van, jelenleg feltétel h csak egy legyen
  public List<InvoiceOfUserByNumber> getPendingInvoicesOfCurrentUser(User user) {
    Map<String, InvoiceOfUserByNumber> result = new HashMap<>();
    for(FeeItem fee : feeItemRepository.findAllByUserIdAndAcceptedByUserFalseAndAcceptedByCompanyTrue(user.getId())) {
      InvoiceOfUserByNumber i = result.get(fee.getSubscription());
      if(i == null) {
        i = new InvoiceOfUserByNumber(fee.getSubscription(), "22222222222222222");
        result.put(fee.getSubscription(), i);
      }
      i.addFeeItem(fee);
    }
    return new LinkedList<>(result.values());
  }

  public InvoiceOfUserByNumber getPendingInvoiceOfCurrentUserByNumber(User user, String number) {
    List<FeeItem> fees = feeItemRepository.findAllByUserIdAndSubscription(user.getId(), number);
    InvoiceOfUserByNumber result = new InvoiceOfUserByNumber(number, "22222222222222222");
    for(FeeItem fee : fees) {
      result.addFeeItem(fee);
    }
    return result;
  }

  public boolean acceptInvoiceOfCurrentUserByNumber(User user, String number) {
    List<FeeItem> fees = feeItemRepository.findAllByUserIdAndSubscription(user.getId(), number);
    if(fees.isEmpty()) {
      return false;
    } else {
      for(FeeItem fee : fees) {
        fee.setAcceptedByUser(true);
      }
      feeItemRepository.save(fees);
      return true;
    }
  }

  public void askForRevision(User user, String number, Map<String, String> map) {
    List<FeeItem> fees = feeItemRepository.findAllByUserIdAndSubscription(user.getId(), number);
    for(FeeItem fee : fees) {
      fee.setAcceptedByCompany(false);
    }
    feeItemRepository.save(fees);
    
    
  }
}
