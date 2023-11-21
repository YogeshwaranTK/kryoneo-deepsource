package com.kjms.service;

import com.kjms.domain.EntityJournal;
import com.kjms.domain.EntityJournalReviewer;
import com.kjms.repository.EntityJournalRepository;
import com.kjms.repository.EntityJournalReviewerRepository;
import com.kjms.repository.EntityUserRepository;
import com.kjms.service.dto.RoleUser;
import com.kjms.service.dto.RootRoleType;
import com.kjms.service.mapper.JournalRolesMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class JournalReviewerService {

    private final EntityJournalRepository entityJournalRepository;
    private final UserService userService;
    private final EntityUserRepository entityUserRepository;

    private final JournalRolesMapper journalRolesMapper;
    private final EntityJournalReviewerRepository entityJournalReviewerRepository;

    public JournalReviewerService(EntityJournalRepository entityJournalRepository, UserService userService, EntityUserRepository entityUserRepository, JournalRolesMapper journalRolesMapper,
                                  EntityJournalReviewerRepository entityJournalReviewerRepository) {
        this.entityJournalRepository = entityJournalRepository;
        this.userService = userService;
        this.entityUserRepository = entityUserRepository;
        this.journalRolesMapper = journalRolesMapper;
        this.entityJournalReviewerRepository = entityJournalReviewerRepository;
    }

    public List<RoleUser> makeAsReviewer(Long journalId, List<String> userIds) {

        List<RoleUser> roleUsers = new ArrayList<>();

        List<EntityJournalReviewer> entityJournalReviewers = new ArrayList<>();

        Optional<EntityJournal> entityJournal = entityJournalRepository.findById(journalId);

        entityJournal.ifPresent(journal -> userIds.forEach(userId -> {

            EntityJournalReviewer entityJournalReviewer = new EntityJournalReviewer();

            entityUserRepository.findById(userId).ifPresent(entityJournalReviewer::setUser);

            entityJournalReviewer.setJournal(journal);

            entityJournalReviewer.setCreatedAt(Instant.now());

            entityJournalReviewer.setCreatedBy(userService.getCurrentUser());

            entityJournalReviewer.setDeleted(false);

            entityJournalReviewers.add(entityJournalReviewer);

            roleUsers.add(journalRolesMapper.mapEntityJournalReviewerToRoleUser(entityJournalReviewer, RootRoleType.reviewer));

        }));

        entityJournalReviewerRepository.saveAll(entityJournalReviewers);

        return roleUsers;
    }

    public Page<RoleUser> getReviewers(Long journalId, Pageable pageable) {

        return journalRolesMapper.mapPageEntityJournalReviewersToRoleUsers(entityJournalReviewerRepository.getReviewersById(journalId, pageable));
    }

    public void removeReviewer(Long journalId, String userId, String deletedRemarks) {

        entityJournalReviewerRepository.deleteReviewerRoleFromUser(journalId, userId, deletedRemarks);
    }
}
