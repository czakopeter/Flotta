package com.sec.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sec.entity.Sim;
import com.sec.service.MainService;

@Controller
public class SimController {

  private MainService service;
  
  @Autowired
  public void setService(MainService service) {
    this.service = service;
  }

  @ModelAttribute
  public void title(Model model) {
    model.addAttribute("title", "Sim");
  }
  
  @RequestMapping("/sim/all")
  public String simsList(Model model) {
    model.addAttribute("sims", service.findAllSim());
    return "sim_templates/simAll";
  }
  
  @RequestMapping("sim/new")
  public String createSim(Model model) {
    model.addAttribute("sim", new Sim());
    return "sim_templates/simNew";
  }
  
  @PostMapping("sim/new")
  public String addSim(Model model, @ModelAttribute Sim sim) {
    if(service.addSim(sim)) {
      return "redirect:/sim/all";
    } else {
      model.addAttribute("sim", sim);
      model.addAttribute("error", service.getSimError());
      return "sim_templates/simNew";
    }
  }
  
  @PostMapping("/sim/all")
  public String sims(Model model, @ModelAttribute Sim sim, @RequestParam(name = "command") String command, @RequestParam(name = "date") String date) {
    System.out.println(date);
    switch (command) {
    case "add":
//      service.saveSim(sim, LocalDate.parse(date));
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
