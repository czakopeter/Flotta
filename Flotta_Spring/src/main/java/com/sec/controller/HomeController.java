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
    
    model.addAttribute("user", service.findUser(a.getName()));
    return "index";
  }
  
  @RequestMapping("/users")
  public String users(Model model, Authentication a) {
//    model.addAttribute("title", "Users");
    model.addAttribute("users", service.findAllUser());
    model.addAttribute("user", new User());
    return "users";
  }

  @PostMapping("/users")
  public String userCheck(Model model, Authentication a, @RequestParam(name = "command") String command,
      @ModelAttribute User u) {
    switch (command) {
    case "add":
      service.registerUser(u);
      break;
    }
    return "redirect:/users";
  }

  @RequestMapping("/registration")
  public String registration(Model model) {
    model.addAttribute("user", new User());
    return "registration";
  }

  @PostMapping("/registration")
  public String reg(Model model, @ModelAttribute User user) {
    String respond = service.registerUser(user);
    switch (respond) {
    case "ok":
      return "/auth/login";
    case "fail":
    default:
      model.addAttribute("user", new User());
      model.addAttribute("msg", respond);
      return "registration";
    }
  }

  
  
  
}