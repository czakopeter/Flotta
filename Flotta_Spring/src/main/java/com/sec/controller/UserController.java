package com.sec.controller;

import java.util.Map;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sec.entity.User;
import com.sec.service.MainService;

@Controller
public class UserController {

  private MainService service;

  @Autowired
  public void setMainService(MainService service) {
    this.service = service;
  }

  @ModelAttribute
  public void title(Model model) {
    System.out.println(LocaleContextHolder.getLocale());
    model.addAttribute("title", "User");
  }
  
  @RequestMapping("/user/all")
  public String listUsers(Model model, Authentication a) {
    model.addAttribute("users", service.findAllUser());
    return "user_templates/userAll";
  }

  @RequestMapping("/user/new")
  public String addUser(Model model) {
    model.addAttribute("user", new User());
    return "user_templates/userNew";
  }
  
  @PostMapping("/user/new")
  public String addUser(Model model, @ModelAttribute("user") User user) {
    if(service.registerUser(user)) {
      return "redirect:/user/all";
    }
    model.addAttribute("user", user);
    model.addAttribute("error", service.getUserError());
    return "user_templates/userNew";
  }
  
  @RequestMapping("/user/{id}")
  public String user(Model model, @PathVariable("id") long id) {
    model.addAttribute("user", service.findUserById(id));
    return "user_templates/userEdit";
  }
  
  @PostMapping("/user/{id}")
  public String user(Model model, @PathVariable("id") long id, @RequestParam() Map<String, Boolean> roles) {
      
    if(!service.updateUser(id, roles)) {
      model.addAttribute("error", service.getUserError());
    }
    model.addAttribute("user", service.findUserById(id));
    return "user_templates/userEdit";
  }
  
  @GetMapping("/profile")
  public String passwordChange(Model model) {
    return "profile_templates/profile";
  }
  
  @PostMapping("/profile/changePassword")
  public String passwordChange(Model model, @RequestParam Map<String, String> params) {
    if(service.changePassword(params.get("old-password"), params.get("new-password"), params.get("confirm-new-password"))) {
      model.addAttribute("success", "Change password was success");
    } else {
      model.addAttribute("error", service.getUserError());
    }
    return "profile_templates/profile";
  }
  
  @GetMapping("/registration")
  public String firstUserRegistration(Model model, RedirectAttributes redirectAttributes) {
    if(service.registrationAvailable()) {
      model.addAttribute("user", new User());
      return "registration";
    } else {
      redirectAttributes.addFlashAttribute("warning", ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("warning.alreadyHaveAdministratior"));
      return "redirect:/login";
    }
  }
  
  @PostMapping("/registration")
  public String firstUserRegistration(Model model, @ModelAttribute User user, RedirectAttributes redirectAttributes) {
    if(service.firstUserRegistration(user)) {
      redirectAttributes.addFlashAttribute("success", "Successful registration! Activation email has been sent to "
          + user.getEmail() + 
          " address! You must activate it!");
      return "redirect:/login";
    } else {
      model.addAttribute("user", user);
      model.addAttribute("error", "Invalid full name or email!");
      return "registration";
    }
  }
  
  @GetMapping("/activation/{key}")
  public String activation(@PathVariable("key") String key, RedirectAttributes redirectAttributes) {
    if(service.activation(key)) {
      redirectAttributes.addFlashAttribute("success", "Successful activation!");
    } else {
      redirectAttributes.addFlashAttribute("error", "Invalid key '" + key + "'!");
    }
    return "redirect:/login";
  }
  
  @GetMapping("/passwordReset")
  public String passwordReset() {
    return "/passwordReset";
  }
  
  @PostMapping("/passwordReset")
  public String passwordReset(RedirectAttributes redirectAttributes, @RequestParam("email") String email) {
    if(service.passwordReset(email)) {
      redirectAttributes.addFlashAttribute("waring", "The email has been sent to " + email + " address!");
    }
    return "redirect:/login";
  }
  
  @GetMapping("/login")
  public String login(Model model) {
//    model.addAttribute("warning", ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("button.save"));
    return "auth/login";
  }
}