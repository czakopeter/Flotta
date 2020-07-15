package com.czp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.czp.entity.Sim;
import com.czp.service.MainService;

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
  
  @GetMapping("/sim/all")
  public String listSims(Model model) {
    model.addAttribute("sims", service.findAllSim());
    return "sim_templates/simAll";
  }
  
  @RequestMapping("sim/new")
  public String addSim(Model model) {
    model.addAttribute("sim", new Sim());
    return "sim_templates/simNew";
  }
  
  @PostMapping("sim/new")
  public String addSim(Model model, @ModelAttribute Sim sim, RedirectAttributes ra) {
    if(service.addSim(sim)) {
      ra.addFlashAttribute("success", "Creation was success");
      return "redirect:/sim/all";
    } else {
      model.addAttribute("sim", sim);
      model.addAttribute("error", service.getSimError());
      return "sim_templates/simNew";
    }
  }
}