package com.czp.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.czp.entity.Device;
import com.czp.entity.DeviceType;
import com.czp.entity.Sim;
import com.czp.entity.Subscription;
import com.czp.entity.User;
import com.czp.entity.note.DevNote;
import com.czp.entity.note.service.DevNoteService;
import com.czp.entity.note.service.SubNoteService;
import com.czp.entity.switchTable.Service.SubDevService;
import com.czp.entity.switchTable.Service.SubSimService;
import com.czp.entity.switchTable.Service.UserDevService;
import com.czp.entity.switchTable.Service.UserSubService;
import com.czp.entity.viewEntity.DeviceToView;
import com.czp.entity.viewEntity.InvoiceOfUserByNumber;
import com.czp.entity.viewEntity.OneCategoryOfUserFinance;
import com.czp.entity.viewEntity.SubscriptionToView;
import com.czp.invoice.Category;
import com.czp.invoice.ChargeRatioByCategory;
import com.czp.invoice.DescriptionCategoryCoupler;
import com.czp.invoice.FeeItem;
import com.czp.invoice.Invoice;
import com.czp.invoice.exception.FileUploadException;
import com.czp.invoice.service.BillingService;
import com.czp.invoice.service.ChargeRatioService;
import com.czp.invoice.service.InvoiceService;


@Service
public class MainService {
	
	private SubscriptionService subscriptionService;
	
	private UserService userService;
	
	private SimService simService;
	
	private DeviceTypeService deviceTypeService;
	
	private DeviceService deviceService;
	
//	private SubSimService subSimService;
	
	private UserSubService userSubService;
	
	private UserDevService userDevService;
	
//	private SubDevService subDevService;
	
//	private SubNoteService subNoteService;
	
//	private DevNoteService devNoteService;
	
	private BillingService billingService;
	
	private ChargeRatioService chargeRatioService;

	@Autowired
	public MainService(SubscriptionService subscriptionService, UserService userService, SimService simService, DeviceTypeService deviceTypeService, DeviceService deviceService) {
		this.subscriptionService = subscriptionService;
		this.userService = userService;
		this.simService = simService;
		this.deviceTypeService = deviceTypeService;
		this.deviceService = deviceService;
	}
	
//	@Autowired
//	public void setSubSimService(SubSimService subSimService) {
//    this.subSimService = subSimService;
//  }
	
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
	
//	@Autowired
//	public void setSubDevService(SubDevService subDevService) {
//    this.subDevService = subDevService;
//  }
	
//	@Autowired
//	public void setSubNoteService(SubNoteService subNoteService) {
//    this.subNoteService = subNoteService;
//  }
	
//	@Autowired
//  public void setDevNoteService(DevNoteService devNoteService) {
//    this.devNoteService = devNoteService;
//  }
	
	@Autowired
  public void setBillingService(BillingService billingService) {
    this.billingService = billingService;
  }
	
	@Autowired
	public void setChargeRatioService(ChargeRatioService chargeRatioService) {
    this.chargeRatioService = chargeRatioService;
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
  
  public SubscriptionToView findSubscriptionById(long id) {
    return subscriptionService.findById(id).toView();
  }
  
  public SubscriptionToView findSubscriptionByIdAndDate(long id, String date) {
    return subscriptionService.findById(id).toView(LocalDate.parse(date));
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
	
	public boolean addSubscription(SubscriptionToView stv) {
    Sim sim = simService.findByImei(stv.getImei());
    if(sim != null && subscriptionService.add(stv)) {
      Subscription saved = subscriptionService.findByNumber(stv.getNumber());
      saved.addSim(sim, null, stv.getDate());
      subscriptionService.save(saved);
      return true;
    }
    return false;
  }
	
	public boolean updateSubscription(long id, SubscriptionToView stv) {
	  Subscription sub = subscriptionService.findById(id);
	  Sim sim = simService.findByImei(stv.getImei());
    User user = userService.findById(stv.getUserId());
    Device dev = deviceService.findById(stv.getDeviceId());
    
    sub.addSim(sim, stv.getSimChangeReason(), stv.getDate());
    sub.addUser(user, stv.getDate());
    sub.addDevice(dev, stv.getDate());
    sub.addNote(stv.getNote(), stv.getDate());
    subscriptionService.save(sub);
    return true;
  }
	
	public String getSubscriptionServiceError() {
    return subscriptionService.removeMsg();
  }
	
	//TODO put UserServiceImp function here
	//-------- USER SERVICE --------
	public boolean registerUser(User user) {
		if(userService.registerUser(user)) {
			return true;
		}
		return false;
	}
		
	public List<User> findAllUser() {
		return userService.findAll();
	}
	
	public User findUserByEmail(String email) {
		return userService.findByEmail(email);
	}
	
	public boolean changePassword(String oldPsw, String newPsw, String confirmPsw) {
    return userService.changePassword(oldPsw, newPsw, confirmPsw);
 }

 public String getUserError() {
   return userService.removeMsg();
 }
 
 public boolean firstUserRegistration(User user) {
   return userService.firstUserRegistration(user);
 }
 
 public boolean registrationAvailable() {
   return userService.registrationAvailable();
 }
 
 public boolean activation(String key) {
   return userService.activation(key);
 }
 
//-------- SIM SERVICE --------
	
  public List<Sim> findAllFreeSim() {
    return simService.findAllFree();
  }

  public List<Sim> findAllSim() {
    return simService.findAll();
  }
  
  public Sim findSimById(int i) {
    return simService.findById(i);
  }
  
  public List<String> getSimChangeReasons() {
    return simService.getAllChagneReason();
  }
  
  public boolean addSim(Sim sim) {
    return simService.add(sim);
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
  
//  private DeviceToView toView(Device device) {
//    DeviceToView dtv = new DeviceToView();
//    dtv.setId(device.getId());
//    dtv.setSerialNumber(device.getSerialNumber());
//    dtv.setTypeName(device.getDeviceType().getName());
//    dtv.setEditable(true);
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
        d -> list.add(d.toView()));
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

  public boolean saveDevice(DeviceToView dtv) {
    DeviceType deviceType = deviceTypeService.findByName(dtv.getTypeName());
    if(deviceType != null && deviceService.add(dtv)) {
      Device saved = deviceService.findBySerialNumber(dtv.getSerialNumber());
      saved.setDeviceType(deviceType);
      deviceService.save(saved);
      return true;
    }
    return false;
  }
  
  public void updateDevice(long id, DeviceToView dtv) {
    Device device = deviceService.findById(id);
    User user = userService.findById(dtv.getUserId());
    
    device.addUser(user, dtv.getDate());
    device.addNote(dtv.getNote(), dtv.getDate());
    
    deviceService.save(device);
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

  public List<Invoice> findAllInvoice() {
    return billingService.findAllInvoice();
  }

  public Invoice findInvoiceByInvoiceNumber(String invoiceNumber) {
    return billingService.findInvoicedByInvoiceNumber(invoiceNumber);
  }

  public List<DescriptionCategoryCoupler> findAllDescriptionCategoryCoupler() {
    return billingService.findAllDescriptionCategoryCoupler();
  }
  
  public boolean invoiceDivisionByTemplateId(long billId, long templateId) {
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
      LocalDate begin = fee.getBeginDate();
      LocalDate end = fee.getEndDate();
      List<LocalDate> allNewUserBegin = userSubService.findAllBeginDateBySubBetween(number, begin, end);
      result.addAll(fee.splitBeforeDate(allNewUserBegin));
    }
    return result;
  }
  
  private List<FeeItem> setFeeItemsUser(List<FeeItem> fees) {
    for(FeeItem fee : fees) {
      User user = userSubService.getUser(fee.getSubscription(), fee.getBeginDate(), fee.getEndDate());
      if(user != null) {
        fee.setUserId(user.getId());
      }
    }
    return fees;
  }
  
  public List<Category> findAllCategory() {
    return billingService.findAllCategory();
  }

  public boolean addCategory(String category) {
    return billingService.addCategory(category);
  }

  public List<String> getUnknownFeeDescToTemplate(long templateId) {
    return billingService.getUnknownFeeDescToTemplate(templateId);
  }

  public void upgradeDescriptionCategoryCoupler(long id, List<String> descriptions, List<Long> categories) {
    billingService.upgradeDescriptionCategoryCoupler(id, descriptions, categories);
  }

  public List<OneCategoryOfUserFinance> getUserFinance(String email) {
    User user = userService.findByEmail(email);
    return billingService.getFinanceByUserId(user.getId());
  }

  public List<SubscriptionToView> findAllCurrentSubscriptionOfUser() {
    return subscriptionService.findAllCurrentByUser(getCurrentUser());
  }
  
  public List<DeviceToView> findAllCurrentDeviceOfUser() {
    return deviceService.findAllCurrentByUser(getCurrentUser());
  }

  public String getSimError() {
    return simService.removeMsg();
  }

  public boolean canCreateSubscription() {
    return !simService.findAllFree().isEmpty();
  }

  public List<User> findAllUserByStatus(int status) {
    return userService.findAllByStatus(status);
  }

  public User findUserById(long id) {
    return userService.findById(id);
  }

  public boolean updateUser(long id, Map<String, Boolean> roles) {
    return userService.modifyRoles(id, roles);
  }

  public boolean passwordReset(String email) {
    return userService.passwordReset(email);
  }

  public DescriptionCategoryCoupler findBillPartitionTemplateById(long id) {
    return billingService.findBillPartitionTemplateById(id);
  }

  public List<String> findAllFeeDescription() {
    return billingService.findAllBillDescription();
  }

  public boolean addChargeRatio(ChargeRatioByCategory payDevision, List<Long> categories, List<Integer> ratios) {
    return billingService.addPayDivision(payDevision, categories, ratios);
  }

  public List<ChargeRatioByCategory> findAllChargeRatio() {
    return billingService.findAllChargeRatio();
  }

  public ChargeRatioByCategory findChargeRatioById(long id) {
    return billingService.findChargeRatioById(id);
  }

  public boolean editChargeRatio(long id, List<Long> categories, List<Integer> ratios) {
    return billingService.editChargeRatio(id, categories, ratios);
  }

  public List<Category> getUnusedCategoryOfChargeRatio(long id) {
    return billingService.getUnusedCategoryOfChargeRatio(id);
  }

  public List<InvoiceOfUserByNumber> getPendingInvoicesOfCurrentUser() {
    return billingService.getPendingInvoicesOfCurrentUser(getCurrentUser());
  }

  public InvoiceOfUserByNumber getPendingInvoiceOfCurrentUserByNumber(String number) {
    return billingService.getPendingInvoiceOfCurrentUserByNumber(getCurrentUser(), number);
  }
  
  private User getCurrentUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return userService.findByEmail(((UserDetailsImpl)auth.getPrincipal()).getFullName());
  }

  public boolean acceptInvoiceOfCurrentUserByNumber(String number) {
    return billingService.acceptInvoiceOfCurrentUserByNumber(getCurrentUser(), number);
  }

  public boolean acceptInvoicesOfCurrentUserByNumbers(List<String> numbers) {
    return billingService.acceptInvoicesOfCurrentUserByNumbers(getCurrentUser(), numbers);
  }

  public void askForRevision(String number, Map<String, String> map) {
    billingService.askForRevision(getCurrentUser(), number, map);
    createRevisionMessage(getCurrentUser(), number, map);
  }
  
  private void createRevisionMessage(User user, String number, Map<String,String> notes) {
    StringBuilder sb = new StringBuilder();
    sb.append("Sender email: " + user.getEmail());
    sb.append("\nSubscription: " + number);
    for(String key : notes.keySet()) {
      if(!key.equals("textarea") && !notes.get(key).isEmpty()) {
        sb.append("\nFeeItem id: " + key + "\n\tnote: " + notes.get(key));
      }
    }
    if(!notes.get("textarea").isEmpty()) {
      sb.append("\nNote for entiry invoce:\n" + notes.get("textarea"));
    }
    
    System.out.println(sb);
  }

  public void resetInvoiceByInvoiceNumber(String invoiceNumber) {
    billingService.resetInvoiceByInvoiceNumber(invoiceNumber);
  }

  public void deleteInvoiceByInvoiceNumber(String invoiceNumber) {
    billingService.deleteInvoiceByInvoiceNumber(invoiceNumber);
  }

}