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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
  public String passwordChange(Model model, @RequestParam Map<String, String> params) {
    if(service.changePassword(params.get("old-password"), params.get("new-password"), params.get("confirm-new-password"))) {
      model.addAttribute("success", "Change password was success");
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
  
  @GetMapping("/profile/finance/accept")
  public String acceptOrAskForRevision(Model model, @RequestParam Map<String, String> accept) {
    if(accept != null) {
      for(String k : accept.keySet()) {
        System.out.println(k);
        System.out.println(accept.get(k));
      }
    }
    return "redirect:/profile/finance";
  }
//  
//  @RequestMapping("/loginError")
//  public String loginError(Model model) {
//    model.addAttribute("error", "Incorrect email or password!");
//    return "auth/login";
//  }
  
}