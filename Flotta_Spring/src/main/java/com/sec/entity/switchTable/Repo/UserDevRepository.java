package com.sec.entity.switchTable.Repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.sec.entity.Device;
import com.sec.entity.User;
import com.sec.entity.switchTable.UserDev;

public interface UserDevRepository extends CrudRepository<UserDev, Long>{

  UserDev findFirstByDevOrderByConnectDesc(Device dev);

  UserDev findFirstByDevAndConnectBeforeOrderByConnectDesc(Device dev, LocalDate date);

  List<UserDev> findAllByUser(User user);
  
  List<UserDev> findAllByDev(Device dev);

}
