package com.sec.billing;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sec.entity.User;

@Service
public class BillingService {

  private BillService billService;

  @Autowired
  public void setBillService(BillService billService) {
    this.billService = billService;
  }

  // beolvassa a számlát
  // valid akkor konvertál validFeeItem-re
  // newm valid új template készítése
  public boolean uploadBill(MultipartFile file) {
    System.out.println("update bill");
    try {
      billService.uploadBill(file);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return false;
    }
    System.out.println("update bill correct");
    return true;
  }

  List<FeeItem> getFeeItemsOfUser(User user) {
    return null;
  }

  List<FeeItem> getFeeItemsOfUser(User user, LocalDate begin, LocalDate end) {
    return null;
  }

  boolean addCategory(String category) {
    return true;
  }

  void connectValidFeeItemToCategory(FeeItem item, String category) {

  }

  public List<Bill> findAll() {
    return new LinkedList<Bill>();
  }

}
