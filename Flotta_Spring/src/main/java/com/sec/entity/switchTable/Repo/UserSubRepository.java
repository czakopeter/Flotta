package com.sec.entity.switchTable.Repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.sec.entity.Subscription;
import com.sec.entity.switchTable.UserSub;

public interface UserSubRepository extends CrudRepository<UserSub, Long> {

  UserSub findBySubAndBeginDate(Subscription sub, LocalDate date);

  UserSub findFirstBySubOrderByBeginDateDesc(Subscription sub);

  UserSub findFirstBySubAndBeginDateLessThanOrderByBeginDateDesc(Subscription sub, LocalDate date);

  UserSub findFirstBySubAndBeginDateBeforeOrderByBeginDateDesc(Subscription sub, LocalDate date);

  void deleteAllBySub(Subscription sub);

  List<UserSub> findAllBySubAndBeginDateBetween(Subscription sub, LocalDate begin, LocalDate end);

}
