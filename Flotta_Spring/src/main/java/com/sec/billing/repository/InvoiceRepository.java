package com.sec.billing.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.sec.billing.Invoice;

public interface InvoiceRepository extends CrudRepository<Invoice, Long> {
  
  List<Invoice> findAll();

  Invoice findByInvoiceNumber(String invoiceNumber);

}
