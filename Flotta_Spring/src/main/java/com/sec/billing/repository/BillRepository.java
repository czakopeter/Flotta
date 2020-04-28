package com.sec.billing.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.sec.billing.Bill;

public interface BillRepository extends CrudRepository<Bill, Long> {
  
  List<Bill> findAll();

  Bill findByInvoiceNumber(String invoiceNumber);

}
