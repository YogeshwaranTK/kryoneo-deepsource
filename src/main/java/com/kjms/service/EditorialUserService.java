package com.kjms.service;

import com.kjms.domain.EntityJournalEditorialRole;
import com.kjms.domain.EntityJournalEditorialUser;
import com.kjms.repository.EntityJournalEditorialRoleRepository;
import com.kjms.repository.EntityJournalEditorialUserRepository;
import com.kjms.repository.EntityJournalRepository;
import com.kjms.repository.EntityUserRepository;
import com.kjms.service.dto.requests.EditorialUserCreateRequest;
import com.kjms.service.dto.RoleUser;
import com.kjms.service.mapper.JournalRolesMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class EditorialUserService {
    private final EntityJournalEditorialUserRepository entityJournalEditorialUserRepository;
    private final EntityUserRepository entityUserRepository;
    private final JournalRolesMapper journalRolesMapper;
    private final UserService userService;
    private final EntityJournalEditorialRoleRepository entityJournalEditorialRoleRepository;
    private final EntityJournalRepository entityJournalRepository;

    public EditorialUserService(EntityJournalEditorialUserRepository entityJournalEditorialUserRepository,
                                EntityUserRepository entityUserRepository,
                                JournalRolesMapper journalRolesMapper,
                                UserService userService,
                                EntityJournalEditorialRoleRepository entityJournalEditorialRoleRepository,
                                EntityJournalRepository entityJournalRepository) {
        this.entityJournalEditorialUserRepository = entityJournalEditorialUserRepository;
        this.entityUserRepository = entityUserRepository;
        this.journalRolesMapper = journalRolesMapper;
        this.userService = userService;
        this.entityJournalEditorialRoleRepository = entityJournalEditorialRoleRepository;
        this.entityJournalRepository = entityJournalRepository;
    }


    public List<RoleUser> createEditorialUsers(EditorialUserCreateRequest editorialUserCreateRequest, Long journalId) {

        if (editorialUserCreateRequest.getExistingRole()) {

            return assignEditorialUserRole(editorialUserCreateRequest.getUserIds(), editorialUserCreateRequest.getRoleId(), journalId);

        } else {

            return assignEditorialUserRole(editorialUserCreateRequest.getUserIds(), createEditorialRole(editorialUserCreateRequest, journalId).getId(), journalId);
        }
    }

    private EntityJournalEditorialRole createEditorialRole(EditorialUserCreateRequest editorialUserCreateRequest, Long journalId) {

        EntityJournalEditorialRole entityJournalEditorialRole = new EntityJournalEditorialRole();

        entityJournalRepository.findById(journalId).ifPresent(entityJournalEditorialRole::setJournal);

        entityJournalEditorialRole.setRoleName(editorialUserCreateRequest.getRoleName());

        entityJournalEditorialRole.setSubmissionEnabled(editorialUserCreateRequest.getSubmissionEnabled());

        entityJournalEditorialRole.setReviewEnabled(editorialUserCreateRequest.getReviewEnabled());

        entityJournalEditorialRole.setCopyEditingEnabled(editorialUserCreateRequest.getCopyEditingEnabled());

        entityJournalEditorialRole.setProductionEnabled(editorialUserCreateRequest.getProductionEnabled());

        entityJournalEditorialRole.setDeleted(false);

        entityJournalEditorialRole.setCreatedBy(userService.getCurrentUser());

        entityJournalEditorialRole.setCreatedAt(Instant.now());

        entityJournalEditorialRoleRepository.save(entityJournalEditorialRole);

        return entityJournalEditorialRole;
    }

    private List<RoleUser> assignEditorialUserRole(List<String> userIds, Long roleId, Long journalId) {

        List<EntityJournalEditorialUser> entityJournalEditorialUsers = new ArrayList<>();

        userIds.forEach(userId -> {

            EntityJournalEditorialUser entityJournalEditorialUser = new EntityJournalEditorialUser();

            entityUserRepository.findById(userId).ifPresent(entityJournalEditorialUser::setUser);

            entityJournalEditorialRoleRepository.findJournalEditorialRoleByJournalIdAndId(roleId, journalId).ifPresent(entityJournalEditorialUser::setEntityJournalEditorialRole);

            entityJournalEditorialUser.setCreatedBy(userService.getCurrentUser());

            entityJournalEditorialUser.setCreatedAt(Instant.now());

            entityJournalEditorialUser.setDeleted(false);

            entityJournalEditorialUsers.add(entityJournalEditorialUser);

        });

        entityJournalEditorialUserRepository.saveAll(entityJournalEditorialUsers);

        return journalRolesMapper.mapEntityJournalEditorialUsersToRoleUsers(entityJournalEditorialUsers);
    }

    public Page<RoleUser> getEditorialUsers(Long journalId, String searchText, Pageable pageable) {
        Page<EntityJournalEditorialUser> entityJournalEditorialUsers;

        if (!StringUtils.hasText(searchText)) {

            entityJournalEditorialUsers = entityJournalEditorialUserRepository.getEditorialUsers(journalId, pageable);
        } else {

            entityJournalEditorialUsers = entityJournalEditorialUserRepository.getEditorialUsersWithSearch(journalId, searchText, pageable);

        }

        return journalRolesMapper.mapPageEntityJournalEditorialUsersToRoleUsers(entityJournalEditorialUsers);
    }

    public void removeEditorialUsers(String userId, Long journalId, String deletedRemarks) {

        entityJournalEditorialUserRepository.deleteEditorialRoleFromUser(userId, journalId, deletedRemarks);

    }
}
