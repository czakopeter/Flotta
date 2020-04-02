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

import com.sec.entity.Sim;
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

  @RequestMapping("/")
  public String home(Model model, Authentication a) {
    System.out.println("Principal\t" + ((UserDetailsImpl)a.getPrincipal()).getFullName());
    System.out.println("Authorities\t" + a.getAuthorities());
    model.addAttribute("title", "Homepage");
    model.addAttribute("name", service.findUser(a.getName()).getFullName());
    LocalDate date = service.findSimById(1).getSimSub().getConnect();
    model.addAttribute("date", date);
    return "index";
  }

  @RequestMapping("/users")
  public String users(Model model, Authentication a) {
    model.addAttribute("title", "Users");
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

  @RequestMapping("/sims")
  public String simsList(Model model) {
    model.addAttribute("title", "Sims");
//    model.addAttribute("sims", service.findAllFreeSim());
    model.addAttribute("sims", service.findAllSim());
    model.addAttribute("sim", new Sim());
    return "sim_templates/sims";
  }
  
  @PostMapping("/sims")
  public String sims(Model model, @ModelAttribute Sim sim, @RequestParam(name = "command") String command, @RequestParam(name = "date") String date) {
    System.out.println(date);
    switch (command) {
    case "add":
      service.saveSim(sim, LocalDate.parse(date));
      break;
    default: 
      break;
    }
    model.addAttribute("title", "Sims");
//    model.addAttribute("sims", service.findAllFreeSim());
    model.addAttribute("sims", service.findAllSim());
    model.addAttribute("sim", new Sim());
    return "sim_templates/sims";
  }
  
  
}