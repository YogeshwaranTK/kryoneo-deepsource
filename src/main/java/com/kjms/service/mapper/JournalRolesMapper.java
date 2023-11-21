package com.kjms.service.mapper;

import com.kjms.domain.EntityJournalAuthor;
import com.kjms.domain.EntityJournalEditorialRole;
import com.kjms.domain.EntityJournalEditorialUser;
import com.kjms.domain.EntityJournalReviewer;
import com.kjms.service.JournalEditorialRole;
import com.kjms.service.dto.RoleUser;
import com.kjms.service.dto.RootRoleType;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JournalRolesMapper {
    public RoleUser mapEntityJournalAuthorToRoleUser(EntityJournalAuthor entityJournalAuthor) {

        RoleUser roleUser = new RoleUser();

        roleUser.setUserId(entityJournalAuthor.getUser().getId());

        roleUser.setFullName(entityJournalAuthor.getUser().getFullName());

        roleUser.setRootRoleType(RootRoleType.author);

        return roleUser;
    }

    public RoleUser mapEntityJournalReviewerToRoleUser(EntityJournalReviewer entityJournalReviewer, RootRoleType rootRoleType) {

        RoleUser roleUser = new RoleUser();

        roleUser.setUserId(entityJournalReviewer.getUser().getId());

        roleUser.setFullName(entityJournalReviewer.getUser().getFullName());

        roleUser.setRootRoleType(rootRoleType);

        return roleUser;
    }

    public RoleUser mapEntityJournalEditorialRoleUserToRoleUser(EntityJournalEditorialUser entityJournalReviewer) {

        RoleUser roleUser = new RoleUser();

        roleUser.setUserId(entityJournalReviewer.getUser().getId());

        roleUser.setFullName(entityJournalReviewer.getUser().getFullName());

        roleUser.setRootRoleType(RootRoleType.editorial_user);

        return roleUser;
    }

    public List<RoleUser> mapEntityJournalEditorialUsersToRoleUsers(List<EntityJournalEditorialUser> entityJournalEditorialUsers) {

        return entityJournalEditorialUsers.stream().map(this::mapEntityJournalEditorialRoleUserToRoleUser).collect(Collectors.toList());
    }

    public Page<RoleUser> mapPageEntityJournalEditorialUsersToRoleUsers(Page<EntityJournalEditorialUser> entityJournalEditorialUsers) {

        return entityJournalEditorialUsers.map(this::mapEntityJournalEditorialRoleUserToRoleUser);
    }

    public Page<RoleUser> mapPageEntityJournalAuthorsToRoleUsers(Page<EntityJournalAuthor> entityJournalAuthors) {

        return entityJournalAuthors.map(this::mapEntityJournalAuthorToRoleUser);
    }

    public Page<RoleUser> mapPageEntityJournalReviewersToRoleUsers(Page<EntityJournalReviewer> entityJournalReviewers) {

        return entityJournalReviewers.map(entityJournalReviewer -> mapEntityJournalReviewerToRoleUser(entityJournalReviewer, RootRoleType.reviewer));
    }

    public List<JournalEditorialRole> mapEntityJournalEditorialRolesToJournalEditorialRoles(List<EntityJournalEditorialRole> entityJournalEditorialRoles) {

        List<JournalEditorialRole> journalEditorialRoles = new ArrayList<>();

        entityJournalEditorialRoles.forEach(entityJournalEditorialRole -> {

            journalEditorialRoles.add(mapEntityJournalEditorialRoleToJournalEditorialRole(entityJournalEditorialRole));
        });

        return journalEditorialRoles;
    }

    private JournalEditorialRole mapEntityJournalEditorialRoleToJournalEditorialRole(EntityJournalEditorialRole entityJournalEditorialRole) {

        JournalEditorialRole journalEditorialRole = new JournalEditorialRole();

        journalEditorialRole.setId(entityJournalEditorialRole.getId());

        journalEditorialRole.setRoleName(entityJournalEditorialRole.getRoleName());

        return journalEditorialRole;
    }
}
