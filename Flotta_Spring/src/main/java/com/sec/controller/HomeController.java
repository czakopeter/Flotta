package com.sec.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sec.entity.Device;
import com.sec.entity.Sim;
import com.sec.entity.Subscription;
import com.sec.entity.User;
import com.sec.service.MainService;
import com.sec.service.UserDetailsImpl;

@Controller
public class HomeController {

  private MainService service;

  @Autowired
  public void setMainService(MainService service) {
    this.service = service;
  }

  @ModelAttribute
  public void title(Model model) {
    model.addAttribute("title", "Homepage");
  }
  
  @RequestMapping("/")
  public String home(Model model, Authentication a) {
//    model.addAttribute("user", service.findUserByEmail(a.getName()));
    model.addAttribute("subscriptions", service.findAllSubscriptionByUser(a.getName()));
    model.addAttribute("devices", service.findAllDeviceByUser(a.getName()));
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