package com.sec.billing.service;

import java.time.LocalDate;
import java.util.HashMap;
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
import com.sec.billing.SplittedFeeItem;
import com.sec.billing.exception.FileUploadException;
import com.sec.billing.exception.UnknonwFeeItemDescriptionException;
import com.sec.billing.repository.FeeItemRepository;
import com.sec.entity.User;
import com.sec.entity.viewEntity.OneCategoryOfUserFinance;

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
      OneCategoryOfUserFinance o = tmp.get(fee.getCatergory());
      if(o == null) {
        o = new OneCategoryOfUserFinance(fee.getUserId(), fee.getCatergory());
      }
      o.addFeeItem(fee);
      tmp.put(fee.getCatergory(), o);
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
    return feeItemRepository.findAllByBill(id);
  }

  public List<FeeItem> findAllByUserId(long userId) {
    return feeItemRepository.findAllByUserId(userId);
  }
}
