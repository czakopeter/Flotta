package com.czp.entity.note.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.czp.entity.Subscription;
import com.czp.entity.note.SubNote;

public interface SubNoteRepository extends CrudRepository<SubNote, Long>{

  List<SubNote> findAllBySub(Subscription sub);

  SubNote findFirstBySubOrderByBeginDateDesc(Subscription sub);

  SubNote findFirstBySubAndBeginDateBeforeOrderByBeginDateDesc(Subscription sub, LocalDate date);

}
