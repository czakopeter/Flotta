package com.sec.entity.switchTable.Repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.sec.entity.Subscription;
import com.sec.entity.switchTable.UserSub;

public interface UserSubRepository extends CrudRepository<UserSub, Long> {

  UserSub findBySubAndConnect(Subscription sub, LocalDate date);

  UserSub findFirstBySubOrderByConnectDesc(Subscription sub);

  UserSub findFirstBySubAndConnectLessThanOrderByConnectDesc(Subscription sub, LocalDate date);

  UserSub findFirstBySubAndConnectBeforeOrderByConnectDesc(Subscription sub, LocalDate date);

  void deleteAllBySub(Subscription sub);

  List<UserSub> findAllBySubAndConnectBetween(Subscription sub, LocalDate begin, LocalDate end);

}
