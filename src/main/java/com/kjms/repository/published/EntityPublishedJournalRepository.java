package com.kjms.repository.published;

import com.kjms.domain.published.journal.EntityPublishedJournal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntityPublishedJournalRepository extends JpaRepository<EntityPublishedJournal,String> {
}
