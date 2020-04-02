package com.sec.status.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.sec.entity.Subscription;
import com.sec.status.SubscriptionStatus;

public interface SubscriptionStatusRepository extends CrudRepository<SubscriptionStatus, Long> {

  List<SubscriptionStatus> findAllBySub(Subscription sub);

  long countBySub(Subscription sub);

  SubscriptionStatus findFirstBySubOrderByDateDesc(Subscription sub);

  SubscriptionStatus findFirstBySubAndDateBeforeOrderByDateDesc(Subscription sub, LocalDate date);


}
