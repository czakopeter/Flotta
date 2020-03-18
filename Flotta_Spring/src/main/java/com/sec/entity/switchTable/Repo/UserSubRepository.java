package com.sec.entity.switchTable.Repo;

import java.time.LocalDate;

import org.springframework.data.repository.CrudRepository;

import com.sec.entity.Subscription;
import com.sec.entity.switchTable.UserSub;

public interface UserSubRepository extends CrudRepository<UserSub, Long> {

  UserSub findBySubAndConnect(Subscription sub, LocalDate date);

  UserSub findFirstBySubOrderByConnectDesc(Subscription sub);

}
