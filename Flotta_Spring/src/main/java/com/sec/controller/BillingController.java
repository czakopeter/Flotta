package com.sec.controller;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.sec.service.MainService;

@Controller
public class BillingController {

  private MainService service;
  
  private List<String> categories = new LinkedList<String>();
  
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
    model.addAttribute("categories", categories);
    model.addAttribute("add", new String());
    return "billing_templates/category";
  }
  
  @PostMapping("/billing/category")
  public String addCategory(Model model, @ModelAttribute("add") String outCat) {
    System.out.println(outCat);
    if(!categories.contains(outCat.toUpperCase())) {
      categories.add(outCat.toUpperCase());
    }
    model.addAttribute("categories", categories);
    model.addAttribute("add", new String());
    return "billing_templates/category";
  }
  
  @RequestMapping("/billing")
  public String basecPage(Model model) {
    return "billing_templates/index";
  }
  
  @PostMapping("/billing")
  public String uploadBill(Model model, @RequestParam("file") MultipartFile file) {
    service.fileUpload(file);
    return "billing_templates/index";
  }
}
