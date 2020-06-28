package com.sec.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.sec.billing.BillPartitionTemplate;
import com.sec.billing.Category;
import com.sec.billing.PayDivision;
import com.sec.billing.exception.FileUploadException;
import com.sec.service.MainService;

@Controller
public class CategoryController {

  private MainService service;
  
  @Autowired
  public void setMainService(MainService service) {
    this.service = service;
  }
  
  @ModelAttribute
  private void title(Model model) {
    model.addAttribute("title", "Category");
  }
  
  @GetMapping("/finance/category/all")
  public String listCategories(Model model) {
    model.addAttribute("categories", service.findAllCategory());
    model.addAttribute("add", new String());
    return "finance_templates/category";
  }
  
  @PostMapping("/finance/category/all")
  public String addCategory(Model model, @ModelAttribute("add") String category) {
    if(service.addCategory(category)) {
    }
    return "redirect:/finance/category/all";
  }
  
  @GetMapping("finance/billPartitionTemplate/all")
  public String listBillPartitionTemplate(Model model) {
    model.addAttribute("billPartitionTemplates", service.findAllBillPartitionTemplate());
    return "finance_templates/billPartitionTemplateAll";
  }
  
  @GetMapping("/finance/billPartitionTemplate/{id}")
  public String prepareEditingBillPartitionTemplate(Model model, @PathVariable("id") long id) {
    model.addAttribute("template", service.findBillPartitionTemplateById(id));
    model.addAttribute("categories", service.findAllCategory());
    return "finance_templates/billPartitionTemplateEdit";
  }
  
  @PostMapping("/finance/billPartitionTemplate/{id}")
  public String editingBillPartitionTemplate(Model model, @PathVariable("id") long id, @RequestParam("description") List<String> descriptions, @RequestParam("category") List<Long> categories) {
    model.addAttribute("template", service.findBillPartitionTemplateById(id));
    model.addAttribute("categories", service.findAllCategory());
    service.upgradeBillPartitionTemplate(id, descriptions, categories);
    return "finance_templates/billPartitionTemplateEdit";
  }
  
  @GetMapping("/finance/billPartitionTemplate/{id}/view")
  public String viewBillPartitionTemplate(Model model, @PathVariable("id") long id) {
    model.addAttribute("template", service.findBillPartitionTemplateById(id));
    return "finance_templates/billPartitionTemplateView";
  }
  
  @GetMapping("finance/billPartitionTemplate/{id}/update")
  public String prepareUpdatingBillPartitionTemplate(Model model, @PathVariable("id") long id) {
    model.addAttribute("id", id);
    model.addAttribute("descriptions", service.getUnknownFeeDescToTemplate(id));
    model.addAttribute("categories", service.findAllCategory());
    return "finance_templates/billPartitionTemplateUpgrade";
  }
  
  @PostMapping("finance/billPartitionTemplate/{id}/update")
  public String updateBillPartitionTemplate(Model model, @PathVariable("id") long id, @RequestParam("description") List<String> descriptions, @RequestParam("category") List<Long> categories) {
    service.upgradeBillPartitionTemplate(id, descriptions, categories);
    return "redirect:/billing/all";
  }
  
  @GetMapping("finance/chargeRatio/all")
  public String listChargeRatio(Model model) {
    model.addAttribute("chargeRatios", service.findAllPayDivision());
    return "/finance_templates/chargeRatioByCategoryAll";
  }
  
  @GetMapping("/finance/chargeRatio/new")
  public String prepareAddingChargeRatio(Model model) {
    model.addAttribute("unusedCategories", service.findAllCategory());
    model.addAttribute("chargeRatio", new PayDivision());
    return "finance_templates/chargeRatioByCategoryNew";
  }
  
  @PostMapping("/finance/chargeRatio/new")
  public String addPayDivision(Model model, @ModelAttribute("chargeRatio") PayDivision chargeRatio, @RequestParam("category") List<Long> categories, @RequestParam("ratio") List<Integer> ratio) {
    if(service.addPayDivision(chargeRatio, categories, ratio)) {
      return "redirect:/finance/chargeRation/all";
    }
    model.addAttribute("unusedCategories", service.findAllCategory());
    return "finance_templates/chargeRatioByCategoryNew";
  }
  
  @GetMapping("/finance/chargeRation/{id}")
  public String prepareEditingChargeRatio(Model model, @PathVariable("id") long id) {
    model.addAttribute("categories", service.findAllCategory());
    model.addAttribute("chargeRatio", service.findPayDivisionById(id));
    model.addAttribute("unusedCategories", service.getUnusedCategoryOfPayDivision(id));
    return "finance_templates/chargeRatioByCategoryEdit";
  }
  
  @PostMapping("/finance/chargeRatio/{id}")
  public String editChargeRatio(Model model, @PathVariable("id") long id, @RequestParam("category") List<Long> categories, @RequestParam("ratio") List<Integer> ratios) {
    if(service.editPayDivision(id, categories, ratios)) {
    }
    model.addAttribute("chargeRatio", service.findPayDivisionById(id));
    model.addAttribute("categories", service.findAllCategory());
    return "finance_templates/chargeRatioByCategoryEdit";
  }
  
}
