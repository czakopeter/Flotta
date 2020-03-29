package com.sec.entity.note.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sec.entity.Subscription;
import com.sec.entity.note.SubNote;
import com.sec.entity.note.repository.SubNoteRepository;

@Service
public class SubNoteService {

  private SubNoteRepository subNoteRepository;
  
  @Autowired
  public void setSubNoteRepository(SubNoteRepository subNoteRepository) {
    this.subNoteRepository = subNoteRepository;
  }

  public void save(Subscription sub, String note, LocalDate date) {
    List<SubNote> list = subNoteRepository.findAllBySub(sub);
    if(list.isEmpty()) {
      subNoteRepository.save(new SubNote(sub, note, date));
    }
  }

  public void update(Subscription sub, String note, LocalDate date) {
    SubNote last = subNoteRepository.findFirstBySubOrderByDateDesc(sub);
    
    if(date.isAfter(last.getDate())) {
      if((last.getNote().isEmpty()) ||
          (note.isEmpty()) ||
          !note.equalsIgnoreCase(last.getNote())) {
        subNoteRepository.save(new SubNote(sub, note, date));
      }
    } else if(date.isEqual(last.getDate())) {
      //modifying
      SubNote lastBefore = subNoteRepository.findFirstBySubAndDateBeforeOrderByDateDesc(sub, date);
      if(lastBefore != null && (
          note.equalsIgnoreCase(lastBefore.getNote())
          )) {
        subNoteRepository.delete(last.getId());
      } else {
        last.setNote(note);
        subNoteRepository.save(last);
      }
    } else {
      //error
    }
  }

}
