package com.sec.controller;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.sec.billing.BillPartitionTemplate;
import com.sec.service.MainService;

@Controller
public class BillingController {

  private MainService service;
  
  @Autowired
  public void setMainService(MainService service) {
    this.service = service;
  }
  
  @ModelAttribute
  private void title(Model model) {
    model.addAttribute("title", "Billing");
  }
  
  @RequestMapping("/billing/category")
  public String categories(Model model) {
    model.addAttribute("categories", service.findAllCategory());
    model.addAttribute("add", new String());
    return "billing_templates/category";
  }
  
  @PostMapping("/billing/category")
  public String addCategory(Model model, @ModelAttribute("add") String outCat) {
    service.addCategory(outCat);
    model.addAttribute("categories", service.findAllCategory());
    model.addAttribute("add", new String());
    return "billing_templates/category";
  }
  
  @RequestMapping("/billing")
  public String basecPage(Model model) {
    model.addAttribute("templates", service.findAllBillPartitionTemplate());
    return "billing_templates/index";
  }
  
  @PostMapping("/billing")
  public String uploadBill(Model model, @RequestParam("file") MultipartFile file) {
    if(!service.fileUpload(file)) {
      model.addAttribute("msg", "Error in upload");
    }
    return "billing_templates/index";
  }
  
  @RequestMapping("billing/all")
  public String bills(Model model) {
    model.addAttribute("bills", service.findAllBill());
    model.addAttribute("templates", service.findAllBillPartitionTemplate());
    return "billing_templates/billAll";
  }
  
  @RequestMapping("billing/billPartition")
  public String billPartition(Model model) {
    return "redirect:/billing_templates/billAll";
  }
  
  @PostMapping("billing/billPartition")
  public String billPartition(Model model, @RequestParam(name = "bill_id") long billId, @RequestParam(name = "template_id") long templateId) {
    return "redirect:/billing_templates/billAll";
  }
  
  @RequestMapping("billing/{invoiceNumber}")
  public String bill(Model model, @PathVariable("invoiceNumber") String invoiceNumber) {
   System.out.println(invoiceNumber);
    model.addAttribute("bill", service.findBillByInvoiceNumber(invoiceNumber));
    
    return "billing_templates/billDetails";
  }
}
