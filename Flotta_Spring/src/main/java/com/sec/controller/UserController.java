package com.sec.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
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
import com.sec.entity.viewEntity.DeviceToView;
import com.sec.entity.viewEntity.SubscriptionToView;
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
  
  @RequestMapping("/user/all")
  public String users(Model model, Authentication a) {
    model.addAttribute("enabledUsers", service.findAllUserByStatus(User.ENABLED));
    model.addAttribute("disabledUsers", service.findAllUserByStatus(User.DISABLED));
    model.addAttribute("requiredPasswordChangUsers", service.findAllUserByStatus(User.REQUIRED_PASSWORD_CHANGE));
    model.addAttribute("waitingForValidationUsers", service.findAllUserByStatus(User.WAITING_FOR_VALIDATION));
    model.addAttribute("users", service.findAllUser());
    model.addAttribute("user", new User());
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
  public String subscription(Model model, @PathVariable("id") long id) {
    model.addAttribute("user", service.findUserById(id));
    return "user_templates/userEdit";
  }
  
  @PostMapping("/user/{id}")
  public String subscription(Model model, @PathVariable("id") long id, @ModelAttribute("user") User user
      , @RequestParam(name = "adminRole", required = false) boolean adminRole
      , @RequestParam(name = "userRole", required = false) boolean userRole
      , @RequestParam(name = "mobileRole", required = false) boolean mobileRole
      , @RequestParam(name = "financeRole", defaultValue = "false") boolean financeRole
      , @RequestParam(name = "roleName") List<String> rolesName) {
    
      boolean[] roles = {adminRole, userRole, mobileRole, financeRole};
    if(!service.updateUser(id, roles, rolesName)) {
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
  public String passwordChange(Model model, @RequestParam(name = "new-password") String newPsw, @RequestParam(name = "confirm-new-password") String confirmNewPsw) {
    if(service.changePassword(newPsw, confirmNewPsw)) {
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
//      model.addAttribute("success", "Registration is success! We send a verification email to "
//          + user.getEmail() + 
//          " address! You must verify your registration!");
//      return "user_templates/registration";
      redirectAttributes.addFlashAttribute("success", "Successful registration! Verification email has been sent to "
          + user.getEmail() + 
          " address! You must verify it!");
      return "redirect:/login";
    } else {
      model.addAttribute("user", user);
      model.addAttribute("error", "Invalid full name or email!");
      return "registration";
    }
  }
  
  @GetMapping("/verification/{key}")
  public String verifyAndChangePassword(@PathVariable("key") String key, RedirectAttributes redirectAttributes) {
    if(service.varification(key)) {
      redirectAttributes.addFlashAttribute("success", "Successful validation!");
    } else {
      redirectAttributes.addFlashAttribute("error", "Invalid key '" + key + "'!");
    }
    return "redirect:/login";
  }
  
  @GetMapping("/login")
  public String login(Model model) {
    return "auth/login";
  }
}