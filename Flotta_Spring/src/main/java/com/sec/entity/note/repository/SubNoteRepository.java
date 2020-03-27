package com.sec.entity.note.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.sec.entity.Subscription;
import com.sec.entity.note.SubNote;

public interface SubNoteRepository extends CrudRepository<SubNote, Long>{

  SubNote save(SubNote subNote);

  List<SubNote> findAllBySub(Subscription sub);

  SubNote findFirstBySubOrderByDateDesc(Subscription sub);

  SubNote findFirstBySubAndDateBeforeOrderByDateDesc(Subscription sub, LocalDate date);

}
