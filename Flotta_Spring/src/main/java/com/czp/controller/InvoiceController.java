package com.czp.controller;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.czp.invoice.Invoice;
import com.czp.invoice.exception.FileUploadException;
import com.czp.service.MainService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class InvoiceController {

  private MainService service;
  
  @Autowired
  public void setMainService(MainService service) {
    this.service = service;
  }
  
  @ModelAttribute
  private void title(Model model) {
    model.addAttribute("title", "Invoice");
  }
  
  @RequestMapping("invoice/all")
  public String listInvoces(Model model) {
    model.addAttribute("invoices", service.findAllInvoice());
    model.addAttribute("couplers", service.findAllDescriptionCategoryCoupler());
    return "invoice_templates/invoiceAll";
  }
  
  @PostMapping("/invoice/all")
  public String addInvoice(Model model, @RequestParam("file") MultipartFile file, RedirectAttributes ra) {
    try {
      service.fileUpload(file);
    } catch (FileUploadException e) {
      ra.addFlashAttribute("error", e.getMessage());
    }
    return "redirect:/invoice/all";
  }
  
//TODO show full header of bill
  @RequestMapping("invoice/{invoiceNumber}")
  public String invoiceDetails(Model model, @PathVariable("invoiceNumber") String invoiceNumber) {
    model.addAttribute("invoice", service.findInvoiceByInvoiceNumber(invoiceNumber));
    return "invoice_templates/invoiceDetails";
  }
  
  
  @PostMapping(path = "/invoice/continueProcessing/{invoiceNumber}")
  public String continueProcessingOfInvoice(@PathVariable(value = "invoiceNumber") String invoiceNumber) {
    service.resetInvoiceByInvoiceNumber(invoiceNumber);
    return "redirect:/invoice/all";
  }
  
  @PostMapping(path = "/invoice/delete/{invoiceNumber}")
  public String deleteInvoice(@PathVariable(value = "invoiceNumber") String invoiceNumber) {
    service.deleteInvoiceByInvoiceNumber(invoiceNumber);
    return "redirect:/invoice/all";
  }
  
//  @RequestMapping(path = "/invoice/continueProcessing, produces = "application/json; charset=UTF-8")
//  @ResponseBody
//  public String continueProcessingOfInvoice(@RequestParam(value = "invoiceNumber") String invoiceNumber) {
//    service.resetInvoiceByInvoiceNumber(invoiceNumber);
    
//    StringBuilder sb = new StringBuilder();
//    sb.append("{");
//    sb.append("\"hasProblem\":" + invoice.hasProblem());
//    if(invoice.hasProblem()) {
//      sb.append(", \"problem\":\"" + invoice.getProblem() + "\"");
//    }
//    sb.append("}");
//    return sb.toString();
//  }
  
//  @PostMapping("/finance/invoicePartition")
//  public String billPartitionTemplate(Model model, @RequestParam(name = "bill_id") long billId, @RequestParam(name = "template_id") long templateId) {
//    if(service.billDivisionByTemplateId(billId, templateId)) {
//      return "redirect:/billing/all";
//      model.addAttribute("userFeeMap", service.splitting);
//      return "billing_templates/splittedBill";
//    } else {
//      model.addAttribute("templateId", templateId);
//      model.addAttribute("feeDescriptions", service.getUnknownFeeDescToTemplate(templateId));
//      model.addAttribute("categories", service.findAllCategory());
//      return "billing_templates/invoiceDescriptionCategoryCouplerUpgrade";
//    }
//  }
//  
//  @PostMapping("billing/billPartitionUpdate")
//  public String billPartitionTemplate(Model model, @RequestParam long templateId, @RequestParam(name = "description") List<String> descriptions, @RequestParam(name = "category") List<Long> categories) {
//    System.out.println("billing/billPartitionUpdate");
//    service.upgradeBillPartitionTemplate(templateId, descriptions, categories);
//    return "redirect:/billing/all";
//  }
//  
  public class Car implements Serializable {
    private static final long serialVersionUID = 1L;
    String plate;
    Car(String plate) {
      this.plate = plate;
    }

    public void setPlate(String plate) {
      this.plate = plate;
    }
    
    public String getPlate() {
      return plate;
    }
  }

}
