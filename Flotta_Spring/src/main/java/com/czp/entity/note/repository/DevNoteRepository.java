package com.czp.entity.note.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.czp.entity.Device;
import com.czp.entity.note.DevNote;

public interface DevNoteRepository extends CrudRepository<DevNote, Long>{

  List<DevNote> findAllByDev(Device dev);

  DevNote findFirstByDevOrderByBeginDateDesc(Device dev);

  DevNote findFirstByDevAndBeginDateBeforeOrderByBeginDateDesc(Device dev, LocalDate date);

}
