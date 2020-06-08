package com.sec.controller;

import java.time.LocalDate;
import java.util.Locale;
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
    model.addAttribute("title", "User");
  }
  
  @RequestMapping("/users")
  public String users(Model model, Authentication a) {
    model.addAttribute("users", service.findAllUser());
    model.addAttribute("user", new User());
    return "user_templates/users";
  }

  @PostMapping("/users")
  public String userCheck(Model model, Authentication a, @RequestParam(name = "command") String command,
      @ModelAttribute User u) {
    System.out.println(command);
    switch (command) {
    case "add":
      service.registerUser(u);
      break;
    }
    return "redirect:/users";
  }
  
  @GetMapping("/passwordChange/{passwordRenewerKey}")
  public String forcedPasswordChange(Model model) {
    return "user_templates/passwordChange";
  }
  
  @GetMapping("/profile")
  public String passwordChange(Model model) {
    return "user_templates/profile";
  }
  
  @PostMapping("/profile/changePassword")
  public String passwordChange(Model model, @RequestParam(name = "new-password") String newPsw, @RequestParam(name = "confirm-new-password") String confirmNewPsw) {
    System.out.println(newPsw);
    System.out.println(confirmNewPsw);
    if(service.changePassword(newPsw, confirmNewPsw)) {
      model.addAttribute("success", "Change password was success");
    } else {
      model.addAttribute("error", service.getUserError());
    }
    return "user_templates/profile";
  }
  
  @GetMapping("/verifyAndChangePassword/{key}")
  public String verifyAndChangePassword(@PathVariable("key") String key, RedirectAttributes redirectAttributes) {
    if(service.isValidKeyOfUser(key)) {
      return "user_templates/verifyAndChangePassword";
    } else {
      redirectAttributes.addFlashAttribute("error", "Invalid key '" + key + "'!");
      return "redirect:/login";
    }
  }
  
  @PostMapping("/verifyAndChangePassword/{key}")
  public String verifyAndChangePassword(Model model, @PathVariable("key") String key, @RequestParam(name = "new-password") String password, @RequestParam(name = "confirm-new-password") String confirmPassword, RedirectAttributes redirectAttributes) {
    if(service.verifyAndChangePassword(key, password, confirmPassword)) {
      redirectAttributes.addFlashAttribute("success", "Verification and password change was success");
    } else {
      redirectAttributes.addFlashAttribute("error", service.getUserError());
    }
    return "redirect:/login";
  }
  
  @GetMapping("/registration")
  public String firstUserRegistration(Model model, RedirectAttributes redirectAttributes) {
    if(service.registrationAvailable()) {
      model.addAttribute("user", new User());
      return "user_templates/registration";
    } else {
      ;
      redirectAttributes.addFlashAttribute("warning", ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("warning"));
//      redirectAttributes.addFlashAttribute("warning", "Already have administrator!");
//      return "auth/login";
      return "redirect:/login";
    }
  }
  
  @PostMapping("/registration")
  public String firstUserRegistration(Model model, @ModelAttribute User user, RedirectAttributes redirectAttributes) {
    if(service.firstUserRegistration(user)) {
//      model.addAttribute("success", "Registration is success! We send a verification email to "
//          + user.getEmail() + 
//          " address! You must verify your registration!");
//      return "auth/login";
      redirectAttributes.addFlashAttribute("success", "Registration is success! We send a verification email to "
          + user.getEmail() + 
          " address! You must verify your registration!");
      return "redirect:/login";
    } else {
      model.addAttribute("error", service.getUserError());
      return "user_templates/registration";
    }
  }
  
  @GetMapping("/login")
  public String login(Model model) {
    if(service.registrationAvailable()) {
      return "redirect:/registration";
    }
    return "auth/login";
  }
}