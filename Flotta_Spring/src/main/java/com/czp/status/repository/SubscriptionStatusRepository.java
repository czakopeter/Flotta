package com.czp.status.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.czp.entity.Subscription;
import com.czp.status.SubscriptionStatus;

public interface SubscriptionStatusRepository extends CrudRepository<SubscriptionStatus, Long> {

  List<SubscriptionStatus> findAllBySub(Subscription sub);

  long countBySub(Subscription sub);

  SubscriptionStatus findFirstBySubOrderByDateDesc(Subscription sub);

  SubscriptionStatus findFirstBySubAndDateBeforeOrderByDateDesc(Subscription sub, LocalDate date);


}
