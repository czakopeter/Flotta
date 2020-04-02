package com.sec.entity.switchTable.Repo;

import java.time.LocalDate;

import org.springframework.data.repository.CrudRepository;

import com.sec.entity.Subscription;
import com.sec.entity.switchTable.SubDev;

public interface SubDevRepository extends CrudRepository<SubDev, Long> {

  SubDev findFirstBySubOrderByConnectDesc(Subscription sub);

  SubDev findFirstBySubAndConnectBeforeOrderByConnectDesc(Subscription sub, LocalDate date);

}
