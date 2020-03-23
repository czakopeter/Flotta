package com.sec.service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sec.entity.Device;
import com.sec.entity.DeviceType;
import com.sec.entity.Sim;
import com.sec.entity.Subscription;
import com.sec.entity.User;
import com.sec.entity.switchTable.Service.SubDevService;
import com.sec.entity.switchTable.Service.SubSimService;
import com.sec.entity.switchTable.Service.UserDevService;
import com.sec.entity.switchTable.Service.UserSubService;
import com.sec.entity.viewEntity.DeviceToView;
import com.sec.entity.viewEntity.SubscriptionToView;

@Service
public class MainService {
	
	private SubscriptionService subscriptionService;
	
	private UserService userService;
	
	private SimService simService;
	
	private DeviceTypeService deviceTypeService;
	
	private DeviceService deviceService;
	
	private SubSimService subSimService;
	
	private UserSubService userSubService;
	
	private UserDevService userDevService;
	
	private SubDevService subDevService;

	@Autowired
	public MainService(SubscriptionService subscriptionService, UserService userService, SimService simService, DeviceTypeService deviceTypeService, DeviceService deviceService) {
		this.subscriptionService = subscriptionService;
		this.userService = userService;
		this.simService = simService;
		this.deviceTypeService = deviceTypeService;
		this.deviceService = deviceService;
	}
	
	@Autowired
	public void setSubSimService(SubSimService subSimService) {
    this.subSimService = subSimService;
  }
	
	@Autowired
	public void setUserSubService(UserSubService userSubService) {
    this.userSubService = userSubService;
  }
	
	@Autowired
	public void setUserService(UserService userService) {
    this.userService = userService;
  }

	@Autowired
  public void setUserDevService(UserDevService userDevService) {
    this.userDevService = userDevService;
  }
	
	@Autowired
	public void setSubDevService(SubDevService subDevService) {
    this.subDevService = subDevService;
  }
	
	
	//------- SUBSCRIPTION SERVICE --------


  public List<SubscriptionToView> findAllSubscription() {
		List<SubscriptionToView> list = new LinkedList<>();
		for(Subscription s : subscriptionService.findAll()) {
			list.add(s.toView());
		}
		return list;
	}
	
	public SubscriptionToView findSubscriptionByNumber(String number) {
		return subscriptionService.findByNumber(number).toView();
	}
	
	public SubscriptionToView findSubscriptionByNumberAndDate(String number, LocalDate date) {
		return subscriptionService.findByNumber(number).toView(date);
	}
	
	public List<LocalDate> findSubscriptionDatesById(long id) {
	  return subscriptionService.findById(id).getAllModificationDateDesc();
	}
	
	public boolean saveSubscription(SubscriptionToView subscription) {
	  subscriptionService.save(subscription, subscription.getDate());
	  Sim sim = simService.findByImei(subscription.getImei());
	  User user = userService.findById(subscription.getUserId());
	  Device dev = deviceService.findById(subscription.getDeviceId());
	  Subscription sub = subscriptionService.findByNumber(subscription.getNumber());
	  subSimService.save(sub, sim, subscription.getDate());
	  userSubService.save(sub, user, subscription.getDate());
	  subDevService.save(sub, dev, subscription.getDate());
	  return true;
  }
	
	public boolean updateSubscription(long id, SubscriptionToView stv) {
	  Subscription sub = subscriptionService.findById(id);
	  Sim sim = simService.findByImei(stv.getImei());
    User user = userService.findById(stv.getUserId());
    Device dev = deviceService.findById(stv.getDeviceId());
    System.out.println(dev);
    System.out.println(userDevService);
    subSimService.update(sub.getId(), sim.getId(), stv.getDate(), stv.getImeiChangeReason());
    userSubService.update(sub, user, stv.getDate());
    subDevService.update(sub, dev, stv.getDate());
    return true;
  }
	
	public String getSubscriptionServiceError() {
    return subscriptionService.getError();
  }
	
	public SubscriptionToView findSubscriptionById(long id) {
    return subscriptionService.findById(id).toView();
  }
	
	public SubscriptionToView findSubscriptionByIdAndDate(long id, String date) {
    return subscriptionService.findById(id).toView(LocalDate.parse(date));
  }
	
	//TODO put UserServiceImp function here
	//-------- USER SERVICE --------
	public String registerUser(User user) {
		user.addRoles("ADMIN");
		if(userService.registerUser(user)) {
			return "ok";
		} else {
			return "error";
		}
	}
		
	public List<User> findAllUser() {
		return userService.findAll();
	}
	
	public User findUser(String email) {
		return userService.findByEmail(email);
	}

//-------- SIM SERVICE --------
	
  public List<Sim> findAllFreeSim() {
    return simService.findAllFree();
  }

  public void addSim(Sim sim, LocalDate date) {
    simService.addSim(sim, date);
  }

  public Sim findSimById(int i) {
    return simService.findById(i);
  }
  
  public List<String> getSimChangeReasons() {
    List<String> r = new LinkedList<>();
    r.add("CHANGED");
    r.add("STOLE");
    r.add("LOST");
    return r;
  }
  
  public List<Sim> findAllSim() {
    return simService.findAll();
  }
  
//-------- DEVICE TYPE SERVICE --------

  public List<DeviceType> findAllDeviceTypes() {
    return deviceTypeService.findAll();
  }

  public List<String> findAllBrandOfDevicesType() {
    return deviceTypeService.findAllBrandOfDevicesType();
  }

  public void saveDeviceType(DeviceType deviceType) {
    deviceTypeService.save(deviceType);
  }

//-------- DEVICE SERVICE --------
  
  public List<DeviceToView> findAllDevices() {
    List<DeviceToView> list = new LinkedList<>();
    deviceService.findAll().forEach(d -> list.add(d.toView()));
    return list;
  }
  
  public List<User> findAllDevicesByUser(long userId) {
    List<User> list = new LinkedList<>();
    if(userId != 0) {
//      User u = userService.findById(userId);
    }
    return list;
  }

  public boolean saveDevice(DeviceToView device) {
    DeviceType deviceType = deviceTypeService.findByName(device.getTypeName());
    User user = userService.findById(device.getUserId());
    return deviceService.save(device, deviceType, user, device.getDate());
  }

  public DeviceToView findDeviceById(long id) {
    return deviceService.findById(id).toView();
  }
  
  public DeviceToView findDeviceByIdAndDate(long id, String date) {
    return deviceService.findById(id).toView(LocalDate.parse(date));
  }
  
  public List<LocalDate> findDeviceDatesById(long id) {
    return deviceService.findById(id).getAllModificationDateDesc();
  }

  public void updateDevice(long id, DeviceToView dtv) {
    User user = userService.findById(dtv.getUserId());
    deviceService.update(id, user, dtv.getDate());
  }

  public String getDeviceServiceError() {
    return deviceService.getError();
  }

}