package com.sec.controller;


import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sec.service.MainService;

@Controller
public class ProfileController {

  private MainService service;

  @Autowired
  public void setMainService(MainService service) {
    this.service = service;
  }

  @ModelAttribute
  public void title(Model model) {
//    model.addAttribute("title", "Profile");
  }
  
  @GetMapping("/profile")
  public String passwordChange(Model model) {
    return "profile_templates/profile";
  }
  
  @RequestMapping("/profile/items")
  public String listItems(Model model) {
    model.addAttribute("devices", service.findAllActualDeviceOfUser());
    model.addAttribute("subscriptions", service.findAllActualSubscriptionOfUser());
    return "profile/items";
  }
  
  @GetMapping("/profile/changePassword")
  public String preparePasswordChanging(Model model) {
    return "profile/passwordChange";
  }
  
  @PostMapping("/profile/changePassword")
  public String passwordChange(Model model, @RequestParam Map<String, String> params, RedirectAttributes ra) {
    if(service.changePassword(params.get("old-password"), params.get("new-password"), params.get("confirm-new-password"))) {
      ra.addFlashAttribute("success", "Change password was success");
      return "redirect:/logout";
    } else {
      model.addAttribute("error", service.getUserError());
    }
    return "profile/passwordChange";
  }
  
  @GetMapping("/profile/finance")
  public String showActualInvoiceStatus(Model model) {
    model.addAttribute("invoicePerNumbers", service.getActualFinanceSummary());
    return "profile/financeSummary";
  }
  
  @PostMapping("/profile/finance/{number}/accept")
  public String acceptUserAmountOfOneNumber(Model model, @PathVariable("number") String number) {
    return "redirect:/profile/finance";
  }
  
  @PostMapping("/profile/finance/accept")
  public String acceptUserAmountOfMoreNumber(Model model, @RequestParam Map<String, String> accept) {
    if(accept != null) {
      for(String k : accept.keySet()) {
        System.out.println(k);
        System.out.println(accept.get(k));
      }
    }
    return "redirect:/profile/finance";
  }

  //TODO ellenőrizni h number és user kapcsolatban van e
  @PostMapping("/profile/finance/{number}/details")
  public String details(Model model, @PathVariable ("number") String number) {
    model.addAttribute("invoicePart", service.findUnacceptedInvoiceOfOneNumberOfUser(number));
    return "profile/financeDetails";
  }
  
}