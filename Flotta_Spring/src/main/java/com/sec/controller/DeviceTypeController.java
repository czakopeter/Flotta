package com.sec.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sec.entity.DeviceType;
import com.sec.service.MainService;

@Controller
public class DeviceTypeController {

  private MainService service;

  @Autowired
  public void setMainService(MainService service) {
    this.service = service;
  }
  
  @ModelAttribute
  public void title(Model model) {
    model.addAttribute("title", "DeviceType");
  }

  @RequestMapping("/deviceTypes")
  public String deviceTypes(Model model) {
    model.addAttribute("deviceTypes", service.findAllDeviceTypes());
    model.addAttribute("brandList", service.findAllBrandOfDevicesType());
    model.addAttribute("deviceType", new DeviceType());
    return "device_templates/deviceTypes";
  }
  
  @PostMapping("/deviceTypes")
  public String postDeviceTypes(Model model, @ModelAttribute DeviceType outDT) {
    service.saveDeviceType(outDT);
    return deviceTypes(model);
  }
}