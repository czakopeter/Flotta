package com.sec.service;

import java.time.LocalDate;
import java.util.Collections;
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
			list.add(s.toViewForViewing());
		}
		return list;
	}
	
	public List<LocalDate> findSubscriptionDatesById(long id) {
	  List<LocalDate> dates = subscriptionService.findSubscriptionDatesById(id);
	  Collections.sort(dates, Collections.reverseOrder());
	  return dates;
	}
	
	public boolean addSubscription(SubscriptionToView stv) {
	  Sim sim = simService.findByImei(stv.getImei());
	  if(subscriptionService.findByNumber(stv.getNumber()) == null) {
	    Subscription entity = new Subscription(stv.getNumber());
	    entity.setSim(sim);
	    entity.setCreateDate(stv.getCreateDate());
	    entity = subscriptionService.add(entity);
	    System.out.println(entity);
	    sim.setSubscription(entity);
	    simService.save(sim);
	    return true;
	  }
	  return false;
//	  Sim sim = simService.findByImei(stv.getImei());
//	  User user = userService.findById(stv.getUserId());
//	  Device dev = deviceService.findById(stv.getDeviceId());
//	  Subscription sub = new Subscription();
//	  sub.setNumber(stv.getNumber());
//	  sub.setDevice(dev);
//	  sub.setCreateDate(stv.getCreateDate());
//	  sub.setLast(stv.getCreateDate());
//	  subscriptionService.save(sub);
//	  sub = subscriptionService.findByNumber(stv.getNumber());
//	  simService.save(sub, stv.getImei(), stv.getDate());
//	  userSubService.save(sub, user, stv.getDate());
//	  subDevService.save(sub, dev, stv.getDate());
//	  subNoteService.save(sub, stv.getNote(), stv.getDate());
//	  return true;
  }
	
	private Subscription convert(SubscriptionToView stv) {
	  Subscription result = new Subscription(stv.getNumber());
	  result.setId(stv.getId());
	  result.setSim(simService.findByImei(stv.getImei()));
	  result.setDevice(deviceService.findById(stv.getDeviceId()));
	  result.setUser(userService.findById(stv.getUserId()));
	  result.setLast(stv.getDate() != null ? stv.getDate() : stv.getCreateDate());
	  result.setCreateDate(stv.getCreateDate());
	  return result;
	}
	
	public boolean updateSubscription(long id, SubscriptionToView stv) {
	  Subscription sub = subscriptionService.findById(id);
	  
	  Sim sim = simService.findByImei(stv.getImei());
    User user = userService.findById(stv.getUserId());
    Device dev = deviceService.findById(stv.getDeviceId());
    sub.setDevice(dev);
    sub.setLast(stv.getDate());
    
    subscriptionService.save(sub);
    
//    subSimService.update(sub.getId(), sim.getId(), stv.getDate(), stv.getImeiChangeReason());
//    userSubService.update(sub, user, stv.getDate());
//    subDevService.updateFromSubscription(sub, dev, stv.getDate());
//    subDevService.save(sub, dev, stv.getDate());
//    subNoteService.update(sub, stv.getNote(), stv.getDate());
    return true;
  }
	
	
	public String getSubscriptionServiceError() {
    return subscriptionService.removeMsg();
  }
	
	public SubscriptionToView findSubscriptionById(long id) {
    return subscriptionService.findById(id).toViewForEditing();
  }
	
	public SubscriptionToView findSubscriptionByIdAndDate(long id, String date) {
    return subscriptionService.findByIdAndDate(id, LocalDate.parse(date)).toViewForViewing();
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

  public boolean addSim(Sim sim) {
    return simService.add(sim);
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
  
  public String getSimError() {
    return simService.removeMsg();
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
    deviceService.findAll().forEach(
        d -> list.add(d.toViewForViewing()));
    return list;
  }
  
  public List<DeviceToView> findAllDevicesByUser(long userId) {
    User user = userService.findById(userId);
    List<DeviceToView> result = new LinkedList<>();
    userDevService.findAllFreeDeviceByUser(user).forEach(d -> {
      result.add(d.toViewForViewing());
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
    return deviceService.findById(id).toViewForEditing();
  }
  
  public DeviceToView findDeviceByIdAndDate(long id, String date) {
    return deviceService.findByIdAndDate(id, LocalDate.parse(date)).toViewForViewing();
  }
  
  public List<LocalDate> findDeviceDatesById(long id) {
    List<LocalDate> dates = deviceService.findDevicesDatesById(id);
    Collections.sort(dates, Collections.reverseOrder());
    return dates;
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
  
  public List<Subscription> test() {
    return subscriptionService.findAll();
  }

  public boolean canCreateSubscription() {
    System.out.println(!simService.findAllFree().isEmpty());
    return !simService.findAllFree().isEmpty();
  }

  

}