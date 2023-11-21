package com.kjms.service;

import com.kjms.domain.EntityJournalEditorialRole;
import com.kjms.repository.EntityJournalAuthorRepository;
import com.kjms.repository.EntityJournalEditorialRoleRepository;
import com.kjms.repository.EntityJournalEditorialUserRepository;
import com.kjms.repository.EntityJournalReviewerRepository;
import com.kjms.security.SecurityUtils;
import com.kjms.service.dto.EditorialUserRole;
import com.kjms.service.dto.JournalAccess;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JournalAccessService {
    private final EntityJournalEditorialUserRepository entityJournalEditorialUserRepository;
    private final UserService userService;
    private final EntityJournalAuthorRepository entityJournalAuthorRepository;
    private final EntityJournalReviewerRepository entityJournalReviewerRepository;

    public JournalAccessService(EntityJournalEditorialUserRepository entityJournalEditorialUserRepository, UserService userService, EntityJournalAuthorRepository entityJournalAuthorRepository, EntityJournalReviewerRepository entityJournalReviewerRepository, EntityJournalEditorialRoleRepository entityJournalEditorialRoleRepository) {
        this.entityJournalEditorialUserRepository = entityJournalEditorialUserRepository;
        this.userService = userService;
        this.entityJournalAuthorRepository = entityJournalAuthorRepository;
        this.entityJournalReviewerRepository = entityJournalReviewerRepository;
    }

    public JournalAccess getAccess(Long journalId) {

        Optional<EntityJournalEditorialRole> entityJournalEditorialRoleOptional = entityJournalEditorialUserRepository.getUserEditorialRole(journalId, userService.getCurrentUser().getId());

        boolean isAuthorRoleExist = entityJournalAuthorRepository.isAuthorRoleExist(journalId, userService.getCurrentUser().getId());

        boolean isReviewerRoleExist = entityJournalReviewerRepository.isReviewerRoleExist(journalId, userService.getCurrentUser().getId());

        EditorialUserRole editorialUserRole = null;

        if (entityJournalEditorialRoleOptional.isPresent()) {

            EntityJournalEditorialRole entityJournalEditorialRole = entityJournalEditorialRoleOptional.get();

            editorialUserRole = new EditorialUserRole(SecurityUtils.getCurrentUserId(), entityJournalEditorialRole.getId(), entityJournalEditorialRole.getRoleName(), entityJournalEditorialRole.getSubmissionEnabled(), entityJournalEditorialRole.getReviewEnabled(), entityJournalEditorialRole.getCopyEditingEnabled(), entityJournalEditorialRole.getProductionEnabled());
        }

        return new JournalAccess(entityJournalEditorialRoleOptional.isPresent(), isAuthorRoleExist, isReviewerRoleExist, editorialUserRole);
    }
}
