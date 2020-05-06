package com.sec.billing.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.sec.billing.FeeItem;
import com.sec.entity.User;

public interface FeeItemRepository extends CrudRepository<FeeItem, Long> {

  List<FeeItem> findAllByBill(long id);

  List<FeeItem> findAllByUserId(long userId);
}
