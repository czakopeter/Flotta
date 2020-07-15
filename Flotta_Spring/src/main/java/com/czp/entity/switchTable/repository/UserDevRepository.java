package com.czp.entity.switchTable.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.czp.entity.Device;
import com.czp.entity.User;
import com.czp.entity.switchTable.UserDev;

public interface UserDevRepository extends CrudRepository<UserDev, Long>{

  UserDev findFirstByDevOrderByBeginDateDesc(Device dev);

  UserDev findFirstByDevAndBeginDateBeforeOrderByBeginDateDesc(Device dev, LocalDate date);

  List<UserDev> findAllByUser(User user);
  
  List<UserDev> findAllByDev(Device dev);

}
