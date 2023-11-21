//package com.kjms.security;
//
//import com.kjms.domain.*;
//import com.kjms.repository.*;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//public class UserPermissionService {
//
//    private final EntityUserRoleRepository userRoleRepository;
//    private final EntityRolePermissionRepository rolePermissionRepository;
//    private final EntityJournalRepository journalRepository;
////    private final EntityJournalUserGroupRepository journalUserGroupRepository;
////    private final EntityJournalGroupPermissionRepository journalGroupPermissionRepository;
//
//    public UserPermissionService(EntityUserRoleRepository userRoleRepository, EntityRolePermissionRepository rolePermissionRepository, EntityJournalRepository journalRepository
////                                 EntityJournalUserGroupRepository journalUserGroupRepository,
////                                 EntityJournalGroupPermissionRepository journalGroupPermissionRepository
//    ) {
//        this.userRoleRepository = userRoleRepository;
//        this.rolePermissionRepository = rolePermissionRepository;
//        this.journalRepository = journalRepository;
////        this.journalUserGroupRepository = journalUserGroupRepository;
////        this.journalGroupPermissionRepository = journalGroupPermissionRepository;
//    }
//
//    public List<String> getUserAdministrationPermissions(EntityUser user) {
//        return rolePermissionRepository
//            .findAllDistinctPermissionByIsDeletedIsFalseAndRoleIn(userRoleRepository.findDistinctRoleByIsDeletedIsFalseAndUser(user));
//    }
//
//    public Map<Long, List<String>> getUserJournalPermissions(EntityUser user) {
//
//        Map<Long, List<String>> journalPermissions = new HashMap<>();
//
////        journalGroupPermissionRepository
////            .findPermissionByLoginUser(user)
////            .collect(Collectors.groupingBy(EntityJournalGroupPermission::getJournalId))
////            .forEach((key, value) -> journalPermissions.put(key.getId(),value.stream().map(EntityJournalGroupPermission::getJournalPermissionId).collect(Collectors.toList()) ));
//
//        return journalPermissions;
//    }
//
//}
