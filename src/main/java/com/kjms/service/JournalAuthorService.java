package com.kjms.service;

import com.kjms.domain.EntityJournal;
import com.kjms.domain.EntityJournalAuthor;
import com.kjms.repository.EntityJournalAuthorRepository;
import com.kjms.repository.EntityJournalRepository;
import com.kjms.repository.EntityUserRepository;
import com.kjms.service.dto.JournalAuthor;
import com.kjms.service.dto.RoleUser;
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
public class JournalAuthorService {
    private final EntityJournalAuthorRepository entityJournalAuthorRepository;
    private final EntityJournalRepository entityJournalRepository;
    private final UserService userService;
    private final EntityUserRepository entityUserRepository;

    private final JournalRolesMapper journalRolesMapper;

    public JournalAuthorService(EntityJournalAuthorRepository entityJournalAuthorRepository, EntityJournalRepository entityJournalRepository, UserService userService, EntityUserRepository entityUserRepository, JournalRolesMapper journalRolesMapper) {
        this.entityJournalAuthorRepository = entityJournalAuthorRepository;
        this.entityJournalRepository = entityJournalRepository;
        this.userService = userService;
        this.entityUserRepository = entityUserRepository;
        this.journalRolesMapper = journalRolesMapper;
    }

    public List<RoleUser> makeAsAuthor(Long journalId, List<String> userIds) {

        List<RoleUser> roleUsers = new ArrayList<>();

        List<EntityJournalAuthor> entityJournalAuthors = new ArrayList<>();

        Optional<EntityJournal> entityJournal = entityJournalRepository.findById(journalId);

        entityJournal.ifPresent(journal -> userIds.forEach(userId -> {

            EntityJournalAuthor entityJournalAuthor = new EntityJournalAuthor();

            entityUserRepository.findById(userId).ifPresent(entityJournalAuthor::setUser);

            entityJournalAuthor.setJournal(journal);

            entityJournalAuthor.setCreatedAt(Instant.now());

            entityJournalAuthor.setCreatedBy(userService.getCurrentUser());

            entityJournalAuthor.setDeleted(false);

            entityJournalAuthors.add(entityJournalAuthor);

            roleUsers.add(journalRolesMapper.mapEntityJournalAuthorToRoleUser(entityJournalAuthor));

        }));

        entityJournalAuthorRepository.saveAll(entityJournalAuthors);

        return roleUsers;
    }

    public Page<RoleUser> getAuthors(Long journalId, Pageable pageable) {

        return journalRolesMapper.mapPageEntityJournalAuthorsToRoleUsers(entityJournalAuthorRepository.getAuthorsById(journalId, pageable));
    }

    public void removeAuthor(Long journalId, String userId, String deletedRemarks) {

        entityJournalAuthorRepository.deleteAuthorRoleFromUser(journalId, userId, deletedRemarks);
    }
}
