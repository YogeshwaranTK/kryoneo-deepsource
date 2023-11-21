package com.kjms.repository;

import com.kjms.domain.EntityJournalCheckList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntityJournalCheckListRepository extends JpaRepository<EntityJournalCheckList, Long> {
    @Query("FROM EntityJournalCheckList journalCheckList WHERE journalCheckList.id = ?1 AND journalCheckList.isDeleted = FALSE")
    Optional<EntityJournalCheckList> findByIdAndDeletedFalse(Long id);

    @Query("FROM EntityJournalCheckList journalCheckList WHERE journalCheckList.journal.id=?1 AND journalCheckList.isDeleted=false ORDER BY journalCheckList.displayPosition ASC")
    List<EntityJournalCheckList> findAllByJournal(Long journalId);
}
