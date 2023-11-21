package com.kjms.repository;


import com.kjms.domain.EntityCommonMailAction;
import com.kjms.service.mail.CommonMailActionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EntityMailActionTemplateRepository extends JpaRepository<EntityCommonMailAction, CommonMailActionId> {

//    @Query("FROM EntityMailActionTemplate mt WHERE mt.journal.id =:journalId AND mt.id =:actionId")
//    EntityMailActionTemplate getJournalTemplateHTML(@Param("actionId") CommonMailActionId actionId, @Param("journalId") Long journalId);

    @Query("FROM EntityCommonMailAction mt WHERE mt.id =:actionId")
   Optional<EntityCommonMailAction> getCommonTemplateHTML(@Param("actionId") CommonMailActionId actionId);

}
