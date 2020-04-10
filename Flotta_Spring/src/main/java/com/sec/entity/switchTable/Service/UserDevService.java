package com.sec.entity.switchTable.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sec.entity.Device;
import com.sec.entity.User;
import com.sec.entity.switchTable.UserDev;
import com.sec.entity.switchTable.Repo.UserDevRepository;
import com.sec.service.DeviceService;

@Service
public class UserDevService {

  private UserDevRepository userDevRepository;
  
  private DeviceService deviceService;
  
  @Autowired
  public void setUserDevRepository(UserDevRepository userDevRepository) {
    this.userDevRepository = userDevRepository;
  }

  @Autowired
  public void setDeviceService(DeviceService deviceService) {
    this.deviceService = deviceService;
  }

  public void save(Device dev, User user, LocalDate date) {
    UserDev entity = new UserDev(user, dev, date);
    userDevRepository.save(entity);
    updateDeviceStatus(dev, user, date);
  }

  public void update(Device dev, User user, LocalDate date) {
    UserDev last = userDevRepository.findFirstByDevOrderByConnectDesc(dev);
    
    if(date.isAfter(last.getConnect())) {
      if((last.getUser() != null && user != null && last.getUser().getId() != user.getId()) || 
          (last.getUser() == null && user != null) ||
          (last.getUser() != null && user == null)) {
        userDevRepository.save(new UserDev(user, dev, date));
        updateDeviceStatus(dev, user, date);
      }
    } else if(date.isEqual(last.getConnect())) {
      //modifying
      UserDev lastBefore = userDevRepository.findFirstByDevAndConnectBeforeOrderByConnectDesc(dev, date);
      if(lastBefore != null && (
          (user == null && lastBefore.getUser() == null) ||
          (user != null && lastBefore.getUser() != null && user.getId() == lastBefore.getUser().getId())
          )) {
        userDevRepository.delete(last.getId());
        deviceService.deleteLastSatus(dev);
      } else {
        last.setUser(user);
        userDevRepository.save(last);
        updateDeviceStatus(dev, user, date);
      }
    } else {
      //error
    }
  }
  
  private void updateDeviceStatus(Device dev, User user, LocalDate date) {
    if(user != null) {
      deviceService.userHasConnected(dev, date);
    } else {
      deviceService.userHasntConnected(dev, date);
    }
  }

  public List<Device> findAllFreeDeviceByUser(User user) {
    List<UserDev> udList = userDevRepository.findAllByUser(user);
    Set<Device> dSet = new HashSet<Device>();
    udList.forEach(ud -> {
      if(user.equals(userDevRepository.findFirstByDevOrderByConnectDesc(ud.getDev()).getUser())) {
        dSet.add(ud.getDev());
      }
    });
    return new LinkedList<>(dSet);
  }

  public User findLastUser(Device device) {
    return userDevRepository.findFirstByDevOrderByConnectDesc(device).getUser();
  }
  
}