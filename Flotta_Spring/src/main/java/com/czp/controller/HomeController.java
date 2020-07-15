package com.czp.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.czp.service.MainService;

@Controller
public class HomeController {

  private MainService service;

  @Autowired
  public void setMainService(MainService service) {
    this.service = service;
  }

  @Autowired
  private SessionRegistry sr;
  
  
  @ModelAttribute
  public void title(Model model) {
    model.addAttribute("title", "Homepage");
  }
  
  @RequestMapping("/")
  public String home(Model model, Authentication a) {
//    model.addAttribute("user", service.findUserByEmail(a.getName()));
//    model.addAttribute("subscriptions", service.findAllActualSubscriptionByUser());
//    model.addAttribute("devices", service.findAllDeviceByUser(a.getName()));
    return "index";
  }
  
  @RequestMapping("/accessDenied")
  public String accessDenied(Model model) {
    model.addAttribute("title", "no title");
    model.addAttribute("error", "Access denied");
    return "accessDenied";
  }
  
  @RequestMapping("/loginError")
  public String loginError(Model model) {
    model.addAttribute("error", "Incorrect email or password!");
    return "auth/login";
  }
  
}