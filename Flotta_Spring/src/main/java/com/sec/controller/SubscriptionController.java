package com.sec.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sec.entity.Sim;
import com.sec.entity.viewEntity.DeviceToView;
import com.sec.entity.viewEntity.SubscriptionToView;
import com.sec.service.MainService;

@Controller
public class SubscriptionController {

  private MainService service;

  @Autowired
  public void setMainService(MainService service) {
    this.service = service;
  }

  @ModelAttribute
  public void title(Model model) {
    model.addAttribute("title", "Subscription");
  }

  @RequestMapping("/subscription/all")
  public String subscriptions(Model model) {
    model.addAttribute("canCreateNew", service.canCreateSubscription());
    model.addAttribute("subscriptions", service.findAllSubscription());
    return "subscription_templates/subscriptionAll";
  }

  @RequestMapping("/subscription/new")
  public String addSubscription(Model model) {
    
    if(service.canCreateSubscription()) {
      model.addAttribute("subscription", new SubscriptionToView());
      model.addAttribute("freeSims", service.findAllFreeSim());
      return "subscription_templates/subscriptionNew";
    } else {
      return "redirect:/subscription/all";
    }
  }

  @PostMapping("/subscription/new")
  public String addSubscription(Model model, @ModelAttribute("subscription") SubscriptionToView stv, @RequestParam(name = "order", defaultValue = "save") String order) {
    String[] orderPart = order.split(" ");
    switch (orderPart[0]) {
    case "save":
      if (service.addSubscription(stv)) {
        return "redirect:/subscription/all";
      } else {
        model.addAttribute("subscription", stv);
        model.addAttribute("error", service.getSubscriptionServiceError());
        return "subscription_templates/subscriptionNew";
      }
    case "update":
      break;
    default:
      break;
    }
    model.addAttribute("subscription", stv);
    model.addAttribute("users", service.findAllUser());
    model.addAttribute("sims", service.findAllFreeSim());
    model.addAttribute("devices", service.findAllDevicesByUser(stv.getUserId()));
    model.addAttribute("error", service.getSubscriptionServiceError());
    return "subscription_templates/subscriptionNew";
  }

  @RequestMapping("/subscription/{id}")
  public String subscription(Model model, @PathVariable("id") long id) {
    SubscriptionToView stv = service.findSubscriptionById(id);
    model.addAttribute("subscription", stv);
    model.addAttribute("freeSims", service.findAllFreeSim());
    model.addAttribute("users", service.findAllUser());
    model.addAttribute("devices", service.findAllDevicesByUser(stv.getUserId()));
    model.addAttribute("dates", service.findSubscriptionDatesById(id));
    model.addAttribute("simChangeReasons", service.getSimChangeReasons());
    return "subscription_templates/subscriptionEdit";
  }

  @PostMapping("/subscription/{id}")
  public String subscription(Model model, @PathVariable("id") long id, @RequestParam(name = "order", defaultValue = "save") String order, @ModelAttribute() SubscriptionToView stv) {
    System.out.println(order);
    System.out.println(stv);
    String[] orderPart = order.split(" ");
    switch (orderPart[0]) {
    case "save":
      service.updateSubscription(id, stv);
      stv = service.findSubscriptionById(id);
      break;
    case "userCh":
      stv.setEditable(true);
      break;
    case "deviceCh":
      stv.setEditable(true);
      if(stv.getDeviceId() != 0) {
        DeviceToView selectedDevice = service.findDeviceById(stv.getDeviceId());
        if(selectedDevice.getDate().isAfter(LocalDate.parse(stv.getMin()))) {
          stv.setMin(selectedDevice.getDate().toString());
        }
      }
      break;
    case "dateSliceCh":
      stv = service.findSubscriptionByIdAndDate(id, orderPart[1]);
      break;
    default:
      break;
    }
    model.addAttribute("freeSims", service.findAllFreeSim());
    model.addAttribute("users", service.findAllUser());
    model.addAttribute("devices", service.findAllDevicesByUser(stv.getUserId()));
    model.addAttribute("dates", service.findSubscriptionDatesById(id));
    model.addAttribute("subscription", stv);
    model.addAttribute("simChangeReasons", service.getSimChangeReasons());
    return stv.isEditable() ? "subscription_templates/subscriptionEdit" : "subscription_templates/subscriptionView";
  }

}
