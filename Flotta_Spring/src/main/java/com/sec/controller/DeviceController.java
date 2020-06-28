package com.sec.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sec.entity.DeviceType;
import com.sec.entity.viewEntity.DeviceToView;
import com.sec.service.MainService;

@Controller
public class DeviceController {

  private MainService service;

  @Autowired
  public void setMainService(MainService service) {
    this.service = service;
  }

  @ModelAttribute
  public void title(Model model) {
    model.addAttribute("title", "Device");
  }

  @RequestMapping("/device/all")
  public String devices(Model model) {
    model.addAttribute("canCreateNew", !service.findAllBrandOfDevicesType().isEmpty());
    model.addAttribute("devices", service.findAllDevices());
    return "device_templates/deviceAll";
  }
  
  @GetMapping("device/new")
  public String addDevice(Model model) {
    model.addAttribute("device", new DeviceToView());
    model.addAttribute("deviceTypes", service.findAllDeviceTypes());
    return "device_templates/deviceNew";
  }
  
  @PostMapping("device/new")
  public String addDevice(Model model, @ModelAttribute("device") DeviceToView dtv) {
    if(service.saveDevice(dtv)) {
      return "redirect:/device/all";
    } else {
      model.addAttribute("device", dtv);
      model.addAttribute("deviceTypes", service.findAllDeviceTypes());
      model.addAttribute("error", service.getDeviceServiceError());
      return "device_templates/deviceNew";
    }
  }
  
  @RequestMapping("device/{id}")
  public String device(Model model, @PathVariable("id") long id) {
    model.addAttribute("device", service.findDeviceById(id));
    model.addAttribute("dates", service.findDeviceDatesById(id));
    model.addAttribute("users", service.findAllUser());
    return "device_templates/deviceEdit";
  }
  
  @PostMapping("device/{id}")
  public String device(Model model, @PathVariable("id") long id, @RequestParam(name = "order", defaultValue = "save") String order, @ModelAttribute() DeviceToView dtv) {
    String[] orderPart = order.split(" ");
    switch (orderPart[0]) {
    case "save":
      service.updateDevice(id, dtv);
      dtv = service.findDeviceById(id);
      break;
    case "userCh":
      dtv.setEditable(true);
      break;
    case "dateSliceCh":
      dtv = service.findDeviceByIdAndDate(id, orderPart[1]);
      break;
    default:
      break;
    }
    model.addAttribute("dates", service.findDeviceDatesById(id));
    model.addAttribute("users", service.findAllUser());
    model.addAttribute("device", dtv);
    return dtv.isEditable() ? "device_templates/deviceEdit" : "device_templates/deviceView";
  }
  
}
