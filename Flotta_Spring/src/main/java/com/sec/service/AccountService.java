package com.sec.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sec.entity.Subscription;
import com.sec.entity.User;

@Service
public class AccountService {

  public void addBill(List<String> billRows) {
  }
  
  public List<String> getSubBillHistory(Subscription sub) {
    return new LinkedList<>();
  }
  
  public List<String> getUserBillHistory(User user){
    return new LinkedList<>();
  }
}
