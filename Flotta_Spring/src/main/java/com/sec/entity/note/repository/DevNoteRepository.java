package com.sec.entity.note.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.sec.entity.Device;
import com.sec.entity.Subscription;
import com.sec.entity.note.DevNote;
import com.sec.entity.note.SubNote;

public interface DevNoteRepository extends CrudRepository<DevNote, Long>{

  DevNote save(DevNote devNote);

  List<DevNote> findAllByDev(Device dev);

  DevNote findFirstByDevOrderByDateDesc(Device dev);

  DevNote findFirstByDevAndDateBeforeOrderByDateDesc(Device dev, LocalDate date);

}
