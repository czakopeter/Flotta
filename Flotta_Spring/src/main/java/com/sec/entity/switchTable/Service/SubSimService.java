package com.sec.entity.switchTable.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sec.entity.Sim;
import com.sec.entity.Subscription;
import com.sec.entity.switchTable.SubSim;
import com.sec.entity.switchTable.Repo.SubSimRepository;
import com.sec.enums.SimStatusEnum;
import com.sec.service.SimService;
import com.sec.service.SubscriptionService;

@Service
public class SubSimService {

  private SubSimRepository subSimRepository;
  
  private SubscriptionService subscriptionService;
  
  private SimService simService;

  @Autowired
  SubSimService(SubSimRepository subSimRepository, SubscriptionService subscriptionService, SimService simService) {
    this.subSimRepository = subSimRepository;
    this.subscriptionService = subscriptionService;
    this.simService = simService;
  }
  
  public List<SubSim> findAllBySub(Subscription s) {
    return subSimRepository.findAllBySub(s);
  }
  
  public SubSim findBySubAndConnect(Subscription s, LocalDate date) {
    return subSimRepository.findBySubAndConnect(s, date);
  }
  
  public void delete(long id) {
    subSimRepository.delete(id);
  }
  
  public void delete(Subscription s, LocalDate date) {
    subSimRepository.deleteBySubAndConnect(s, date);
  }

  public void save(Subscription s, Sim sim, LocalDate date) {
    subSimRepository.save(new SubSim(s, sim, date));
  }

  public void update(long subId, Long simId, LocalDate date, String imeiChangeReason) {
    SubSim last = subSimRepository.findFirstBySubOrderByConnectDesc(subscriptionService.findById(subId));
    switch (imeiChangeReason) {
    case "CHANGED":
    case "STOLE":
    case "LOST":
      if(date.isEqual(last.getConnect())) {
        //utolsó visszaállítása FREE állapotba + hozzá tartozó SubSim törlése
        simService.removeLastStatusModification(last.getSim().getId());
        last.setSim(null);
        last.setSub(null);
        subSimRepository.delete(last);
        //az új utolsó cseréje okának módosítása
        last = subSimRepository.findFirstBySubOrderByConnectDesc(subscriptionService.findById(subId));
        simService.modifySimLastStatus(last.getSim().getId(), imeiChangeReason);
        //új SubSim létrehozása a paraméterek szerint + sim aktiválása
        subSimRepository.save(new SubSim(subscriptionService.findById(subId), simService.findById(simId), date));
        simService.findById(simId).addStatus(SimStatusEnum.ACTIVE, date);
      } else {
        last.getSim().addStatus(SimStatusEnum.valueOf(imeiChangeReason), date);
        subSimRepository.save(new SubSim(subscriptionService.findById(subId), simService.findById(simId), date));
        simService.findById(simId).addStatus(SimStatusEnum.ACTIVE, date);
      }
      break;
    default:
      break;
    }
  }
}
