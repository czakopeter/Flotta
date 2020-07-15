package com.czp.invoice.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.czp.entity.User;
import com.czp.entity.viewEntity.InvoiceOfUserByNumber;
import com.czp.invoice.FeeItem;

public interface FeeItemRepository extends CrudRepository<FeeItem, Long> {

//  List<FeeItem> findAllByInvoice(long id);

  List<FeeItem> findAllByUserId(long userId);

//  List<FeeItem> findAllByInvoiceId(long id);

  List<FeeItem> findAllByUserIdAndSubscription(long userId, String subscription);

  List<FeeItem> findAllByUserIdAndAcceptedByUserFalseAndAcceptedByCompanyTrue(long id);
}
