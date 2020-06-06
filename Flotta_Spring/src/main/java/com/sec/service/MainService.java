package com.sec.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sec.billing.Bill;
import com.sec.billing.BillPartitionTemplate;
import com.sec.billing.Category;
import com.sec.billing.FeeItem;
import com.sec.billing.exception.FileUploadException;
import com.sec.billing.service.BillingService;
import com.sec.billing.service.PayDevisionService;
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
import com.sec.entity.viewEntity.OneCategoryOfUserFinance;
import com.sec.entity.viewEntity.SubscriptionToView;
import com.sec.entity.note.DevNote;
import com.sec.entity.note.service.DevNoteService;
import com.sec.entity.note.service.SubNoteService;


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
	
	private SubNoteService subNoteService;
	
	private DevNoteService devNoteService;
	
	private BillingService billingService;
	
	private PayDevisionService payDevisionService;

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
	
	@Autowired
	public void setSubNoteService(SubNoteService subNoteService) {
    this.subNoteService = subNoteService;
  }
	
	@Autowired
  public void setDevNoteService(DevNoteService devNoteService) {
    this.devNoteService = devNoteService;
  }
	
	@Autowired
  public void setBillingService(BillingService billingService) {
    this.billingService = billingService;
  }
	
	@Autowired
	public void setPayDevisionService(PayDevisionService payDevisionService) {
    this.payDevisionService = payDevisionService;
  }
	
	public Map<User, List<FeeItem>> splitting = new HashMap<>();
	
	
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
	
	public boolean updateSubscription(long id, SubscriptionToView stv) {
	  Subscription sub = subscriptionService.findById(id);
	  Sim sim = simService.findByImei(stv.getImei());
    User user = userService.findById(stv.getUserId());
    Device dev = deviceService.findById(stv.getDeviceId());
    
    sub.addSim(sim, stv.getSimChangeReason(), stv.getDate());
    sub.addUser(user, stv.getDate());
    sub.addDevice(dev, stv.getDate());
    subscriptionService.save(sub);
    
//    Modification through switch table
//    subSimService.update(sub, sim, stv.getDate(), stv.getSimChangeReason());
//    userSubService.update(sub, user, stv.getDate());
//    subDevService.updateFromSubscription(sub, dev, stv.getDate());
//    subNoteService.update(sub, stv.getNote(), stv.getDate());
//    sub.setLastMod(sub.getAllModificationDateDesc().get(0));
    
    return true;
  }
	
	public String getSubscriptionServiceError() {
    return subscriptionService.removeMsg();
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
//		user.addRoles("ADMIN");
		user.addRoles("FIRST");
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

  public void saveSim(Sim sim, LocalDate date) {
    simService.save(sim, date);
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
  
  private DeviceToView toView(Device device) {
    DeviceToView dtv = new DeviceToView();
    dtv.setId(device.getId());
    dtv.setSerialNumber(device.getSerialNumber());
    dtv.setTypeName(device.getDeviceType().getName());
    dtv.setEditable(true);
    
    User user = userDevService.findLastUser(device);
    if(user != null) {
      dtv.setUserId(user.getId());
      dtv.setUserName(user.getFullName());
    } else {
      dtv.setUserId(0);
      dtv.setUserName("");
    }
    
    Subscription sub = subDevService.findLastSub(device);
    if(sub != null) {
      dtv.setNumber(sub.getNumber());
    } else {
      dtv.setNumber("");
    }
    
    DevNote note = devNoteService.findLastNote(device);
    
    if(note != null) {
      dtv.setNote(note.getNote());
    } else {
      dtv.setNote("");
    }
    
    return dtv;
  }
  
//  private DeviceToView toView(Device device, LocalDate date) {
//    DeviceToView dtv = new DeviceToView();
//    dtv.setId(device.getId());
//    dtv.setSerialNumber(device.getSerialNumber());
//    dtv.setTypeName(device.getDeviceType().getName());
//    
////    dtv.setEditable(true);
//    
//    User user = userDevService.findLastUser(device);
//    if(user != null) {
//      dtv.setUserId(user.getId());
//      dtv.setUserName(user.getFullName());
//    } else {
//      dtv.setUserId(0);
//      dtv.setUserName("");
//    }
//    
//    Subscription sub = subDevService.findLastSub(device);
//    if(sub != null) {
//      dtv.setNumber(sub.getNumber());
//    } else {
//      dtv.setNumber("");
//    }
//    
//    DevNote note = devNoteService.findLastNote(device);
//    
//    if(note != null) {
//      dtv.setNote(note.getNote());
//    } else {
//      dtv.setNote("");
//    }
//    
//    return dtv;
//  }
  
  public List<DeviceToView> findAllDevices() {
    List<DeviceToView> list = new LinkedList<>();
    deviceService.findAll().forEach(
        d -> list.add(toView(d)));
    return list;
  }
  
  public List<DeviceToView> findAllDevicesByUser(long userId) {
    User user = userService.findById(userId);
    List<DeviceToView> result = new LinkedList<>();
    userDevService.findAllFreeDeviceByUser(user).forEach(d -> {
      result.add(d.toView());
    });
    return result ;
  }

  public boolean saveDevice(DeviceToView device) {
    DeviceType deviceType = deviceTypeService.findByName(device.getTypeName());
    Device saved = deviceService.save(device.getSerialNumber(), deviceType, device.getDate());
    if(saved != null) {
      User user = userService.findById(device.getUserId());
      
      userDevService.save(saved, user, device.getDate());
      subDevService.save(null, saved, device.getDate());
      devNoteService.save(saved, device.getNote(), device.getDate());
      
      return true;
    } else {
      return false;
    }
  }
  
  public void updateDevice(long id, DeviceToView dtv) {
    Device device = deviceService.findById(id);
    User user = userService.findById(dtv.getUserId());
    userDevService.update(device, user, dtv.getDate());
    devNoteService.update(device, dtv.getNote(), dtv.getDate());
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

  public String getDeviceServiceError() {
    return deviceService.getError();
  }
  
  //-------- BILLING SERVICE --------
  
  public boolean fileUpload(MultipartFile file) throws FileUploadException {
    return billingService.uploadBill(file);
  }

  public List<Bill> findAllBill() {
    return billingService.findAllBill();
  }

  public Bill findBillByInvoiceNumber(String invoiceNumber) {
    return billingService.findBilldByInvoiceNumber(invoiceNumber);
  }

  public List<BillPartitionTemplate> findAllBillPartitionTemplate() {
    return billingService.findAllBillPartitionTemplate();
  }
  
  public boolean billDivisionByTemplateId(long billId, long templateId) {
    List<FeeItem> fees = billingService.findAllFeeItemByBillId(billId);

    fees = divideFeeItemsByUserChanges(fees);
    fees = setFeeItemsUser(fees);
    billingService.save(fees);
    
    return billingService.billPartitionByTemplateId(billId, templateId);
  }
  
  private List<FeeItem> divideFeeItemsByUserChanges(List<FeeItem> fees) {
    List<FeeItem> result = new LinkedList<>();
    for(FeeItem fee : fees) {
      String number = fee.getSubscription();
      LocalDate begin = fee.getBegin();
      LocalDate end = fee.getEnd();
      List<LocalDate> allNewUserBegin = userSubService.findAllBeginDateBySubBetween(number, begin, end);
      result.addAll(fee.splitBeforeDate(allNewUserBegin));
    }
    return result;
  }
  
  private List<FeeItem> setFeeItemsUser(List<FeeItem> fees) {
    for(FeeItem fee : fees) {
      User user = userSubService.getUser(fee.getSubscription(), fee.getBegin(), fee.getEnd());
      if(user != null) {
        fee.setUserId(user.getId());
      }
    }
    return fees;
  }
  
  public List<Category> findAllCategory() {
    return billingService.findAllCategory();
  }

  public void addCategory(String outCat) {
    billingService.addCategory(outCat);
  }

  public List<String> getUnknownFeeDescToTemplate(long templateId) {
    return billingService.getUnknownFeeDescToTemplate(templateId);
  }

  public void upgradeBillPartitionTemplate(long tempalteId, List<String> descriptions, List<Long> categories) {
    billingService.upgradeBillPartitionTemplate(tempalteId, descriptions, categories);
  }

  public List<OneCategoryOfUserFinance> getUserFinance(String email) {
    User user = userService.findByEmail(email);
    return billingService.getFinanceByUserId(user.getId());
  }
  
  public void test() {
    User user1 = userService.findById(1);
    user1.addPayDevision(payDevisionService.get(1));
    userService.modify(user1);
    User user2 = userService.findById(2);
    user2.addPayDevision(payDevisionService.get(1));
    userService.modify(user2);
    user1.setPayDevs(new LinkedList<>());
    userService.modify(user1);
  }

  //TODO write get all actual user's subscription
  public List<SubscriptionToView> findAllSubscriptionByUser(String email) {
    User user = userService.findByEmail(email);
    return new LinkedList<>();
  }
  
  //TODO write get all actual user's device
  public List<DeviceToView> findAllDeviceByUser(String email) {
    User user = userService.findByEmail(email);
    return new LinkedList<>();
  }

  public boolean addSim(Sim sim) {
    return simService.add(sim);
  }

  public String getSimError() {
    return simService.removeMsg();
  }

  public boolean canCreateSubscription() {
    return !simService.findAllFree().isEmpty();
  }

  public boolean addSubscription(SubscriptionToView stv) {
    return subscriptionService.add(stv);
  }
}