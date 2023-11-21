package com.kjms.service;

import com.kjms.domain.EntityJournalEditorialRole;
import com.kjms.repository.EntityJournalAuthorRepository;
import com.kjms.repository.EntityJournalEditorialRoleRepository;
import com.kjms.repository.EntityJournalEditorialUserRepository;
import com.kjms.repository.EntityJournalReviewerRepository;
import com.kjms.security.SecurityUtils;
import com.kjms.service.dto.JournalRole;
import com.kjms.service.mapper.JournalRolesMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JournalRoleService {
    private final EntityJournalAuthorRepository entityJournalAuthorRepository;
    private final EntityJournalReviewerRepository entityJournalReviewerRepository;
    private final EntityJournalEditorialUserRepository entityJournalEditorialUserRepository;
    private final EntityJournalEditorialRoleRepository entityJournalEditorialRoleRepository;

    private final JournalRolesMapper journalRolesMapper;

    public JournalRoleService(EntityJournalAuthorRepository entityJournalAuthorRepository,
                              EntityJournalReviewerRepository entityJournalReviewerRepository,
                              EntityJournalEditorialUserRepository entityJournalEditorialUserRepository,
                              EntityJournalEditorialRoleRepository entityJournalEditorialRoleRepository, JournalRolesMapper journalRolesMapper) {
        this.entityJournalAuthorRepository = entityJournalAuthorRepository;
        this.entityJournalReviewerRepository = entityJournalReviewerRepository;
        this.entityJournalEditorialUserRepository = entityJournalEditorialUserRepository;
        this.entityJournalEditorialRoleRepository = entityJournalEditorialRoleRepository;
        this.journalRolesMapper = journalRolesMapper;
    }

    public JournalRole getJournalAccessLevel(Long journalId) {

        boolean isAuthorRoleExist = entityJournalAuthorRepository.isAuthorRoleExist(journalId, SecurityUtils.getCurrentUserId());

        boolean isReviewerRoleExist = entityJournalReviewerRepository.isReviewerRoleExist(journalId, SecurityUtils.getCurrentUserId());

        Optional<EntityJournalEditorialRole> entityJournalEditorialUser = entityJournalEditorialUserRepository.getUserEditorialRole(journalId, SecurityUtils.getCurrentUserId());

        boolean isSubmissionEnabled = false;

        boolean isReviewEnabled = false;

        boolean isCopyEditingEnabled = false;

        boolean isProductionEnabled = false;

        if (entityJournalEditorialUser.isPresent()) {

            isSubmissionEnabled = entityJournalEditorialUser.get().getSubmissionEnabled();
            isReviewEnabled = entityJournalEditorialUser.get().getReviewEnabled();
            isCopyEditingEnabled = entityJournalEditorialUser.get().getCopyEditingEnabled();
            isProductionEnabled = entityJournalEditorialUser.get().getProductionEnabled();

        }

        return new JournalRole(isAuthorRoleExist, isReviewerRoleExist, entityJournalEditorialUser.isPresent(), isSubmissionEnabled,
            isReviewEnabled, isCopyEditingEnabled, isProductionEnabled,
            new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    public List<JournalEditorialRole> getJournalRoles(Long journalId) {

        return journalRolesMapper.mapEntityJournalEditorialRolesToJournalEditorialRoles(entityJournalEditorialRoleRepository.getJournalEditorialRoles(journalId));
    }
}
