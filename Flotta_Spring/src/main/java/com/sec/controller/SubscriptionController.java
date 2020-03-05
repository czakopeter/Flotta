package com.sec.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
  public void test(Model model) {
    System.out.println("\nSUBSCRIPTION");
  }

  @RequestMapping("/subscription/all")
  public String addSubscription(Model model) {
    model.addAttribute("title", "Subscriptions");
    model.addAttribute("canCreateNew", "Subscriptions");
    model.addAttribute("subscriptions", service.findAllSubscription());
    return "subscription_templates/subscriptionAll";
  }

  @RequestMapping("/subscription/new")
  public String subscriptions(Model model) {
    model.addAttribute("title", "Subscriptions");
    model.addAttribute("subscription", new SubscriptionToView());
    model.addAttribute("users", service.findAllUser());
    model.addAttribute("sims", service.findAllFreeSim());
    return "subscription_templates/subscriptionNew";
  }

  @PostMapping("/subscription/new")
  public String subscriptions(Model model, @ModelAttribute("subscription") SubscriptionToView stv, @RequestParam(name = "order", defaultValue = "save") String order) {
    String[] orderPart = order.split(" ");
    switch (orderPart[0]) {
    case "save":
      if (service.saveSubscription(stv)) {
        return "redirect:/subscription/all";
      } else {
        model.addAttribute("subscription", stv);
        model.addAttribute("users", service.findAllUser());
        model.addAttribute("sims", service.findAllFreeSim());
        model.addAttribute("devices", service.findAllDevicesByUser(stv.getUserId()));
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
    model.addAttribute("title", "Subscriptions");
    model.addAttribute("subscription", service.findSubscriptionById(id));
    model.addAttribute("sims", service.findAllFreeSim());
    model.addAttribute("users", service.findAllUser());
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
    case "dateSliceCh":
      stv = service.findSubscriptionByIdAndDate(id, orderPart[1]);
      break;
    default:
      break;
    }
    model.addAttribute("sims", service.findAllFreeSim());
    model.addAttribute("users", service.findAllUser());
    model.addAttribute("dates", service.findSubscriptionDatesById(id));
    model.addAttribute("subscription", stv);
    model.addAttribute("simChangeReasons", service.getSimChangeReasons());
    return stv.isEditable() ? "subscription_templates/subscriptionEdit" : "subscription_templates/subscriptionView";
  }

}
