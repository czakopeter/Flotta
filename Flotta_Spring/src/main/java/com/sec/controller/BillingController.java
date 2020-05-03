package com.sec.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.sec.billing.Category;
import com.sec.billing.exception.FileUploadException;
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
  
  @RequestMapping("billing/all")
  public String bills(Model model) {
    model.addAttribute("bills", service.findAllBill());
    model.addAttribute("templates", service.findAllBillPartitionTemplate());
    return "billing_templates/billAll";
  }
  
  @PostMapping("/billing/all")
  public String uploadBill(Model model, @RequestParam("file") MultipartFile file) {
    try {
      service.fileUpload(file);
    } catch (FileUploadException e) {
      model.addAttribute("error", e.getMessage());
    }
    model.addAttribute("bills", service.findAllBill());
    model.addAttribute("templates", service.findAllBillPartitionTemplate());
    return "billing_templates/billAll";
  }
  
  @RequestMapping("billing/billPartition")
  public String billPartitions(Model model) {
    return "redirect:/billing/all";
  }
  
  @PostMapping("billing/billPartition")
  public String billPartitionTemplate(Model model, @RequestParam(name = "bill_id") long billId, @RequestParam(name = "template_id") long templateId) {
    System.out.println("billId " + billId);
    System.out.println("templateId " + templateId);
    if(service.billPartitionByTemplateId(billId, templateId)) {
//      return "redirect:/billing/all";
      model.addAttribute("userFeeMap", service.splitting);
      return "billing_templates/splittedBill";
    } else {
      model.addAttribute("templateId", templateId);
      model.addAttribute("feeDescriptions", service.getUnknownFeeDescToTemplate(templateId));
      model.addAttribute("categories", service.findAllCategory());
      return "billing_templates/billPartitionTemplateUpgrade";
    }
  }
  
  @PostMapping("billing/billPartitionUpdate")
  public String billPartitionTemplate(Model model, @RequestParam long templateId, @RequestParam(name = "description") List<String> descriptions, @RequestParam(name = "category") List<Long> categories) {
    System.out.println("billing/billPartitionUpdate");
    service.upgradeBillPartitionTemplate(templateId, descriptions, categories);
    return "redirect:/billing/all";
  }
  
  @RequestMapping("billing/{invoiceNumber}")
  public String bill(Model model, @PathVariable("invoiceNumber") String invoiceNumber) {
   System.out.println(invoiceNumber);
    model.addAttribute("bill", service.findBillByInvoiceNumber(invoiceNumber));
    
    return "billing_templates/billDetails";
  }
  
  @RequestMapping("finance")
  public String finance(Model model, Authentication auth) {
    
    model.addAttribute("userFinance", service.getUserFinance(auth.getName()));
    return "billing_templates/finance";
  }
}
