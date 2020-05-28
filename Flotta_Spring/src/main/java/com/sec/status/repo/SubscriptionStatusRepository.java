package com.sec.status.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.sec.entity.Subscription;
import com.sec.status.SubscriptionStatus;

public interface SubscriptionStatusRepository extends CrudRepository<SubscriptionStatus, Long> {

  List<SubscriptionStatus> findAllBySubscription(Subscription sub);

  long countBySubscription(Subscription sub);

  SubscriptionStatus findFirstBySubscriptionOrderByDateDesc(Subscription sub);

  SubscriptionStatus findFirstBySubscriptionAndDateBeforeOrderByDateDesc(Subscription sub, LocalDate date);


}
