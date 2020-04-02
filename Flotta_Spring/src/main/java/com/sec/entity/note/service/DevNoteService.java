package com.sec.entity.note.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sec.entity.Device;
import com.sec.entity.note.DevNote;
import com.sec.entity.note.repository.DevNoteRepository;

@Service
public class DevNoteService {

  private DevNoteRepository devNoteRepository;
  
  @Autowired
  public void setDevNoteRepository(DevNoteRepository devNoteRepository) {
    this.devNoteRepository = devNoteRepository;
  }

  public void save(Device dev, String note, LocalDate date) {
    List<DevNote> list = devNoteRepository.findAllByDev(dev);
    if(list.isEmpty()) {
      devNoteRepository.save(new DevNote(dev, note, date));
    }
  }

  public void update(Device dev, String note, LocalDate date) {
    DevNote last = devNoteRepository.findFirstByDevOrderByDateDesc(dev);
    if(last == null) {
      save(dev, note, date);
      return;}
    
    if(date.isAfter(last.getDate())) {
      if((last.getNote().isEmpty()) ||
          (note.isEmpty()) ||
          !note.equalsIgnoreCase(last.getNote())) {
        devNoteRepository.save(new DevNote(dev, note, date));
      }
    } else if(date.isEqual(last.getDate())) {
      //modifying
      DevNote lastBefore = devNoteRepository.findFirstByDevAndDateBeforeOrderByDateDesc(dev, date);
      if(lastBefore != null && (
          note.equalsIgnoreCase(lastBefore.getNote())
          )) {
        devNoteRepository.delete(last.getId());
      } else {
        last.setNote(note);
        devNoteRepository.save(last);
      }
    } else {
      //error
    }
  }

}
