//package com.kjms.service.mapper;
//
//import com.kjms.config.Constants;
//import com.kjms.domain.EntityRole;
//import com.kjms.domain.EntityUser;
////import com.kjms.domain.EntityUserRole;
//import com.kjms.service.dto.CustomUser;
//import com.kjms.service.dto.Role;
//import org.springframework.data.domain.Page;
//import org.springframework.stereotype.Service;
//
//import java.util.Set;
//import java.util.stream.Collectors;
//
///**
// * Mapper class responsible for converting {@link EntityRole} to {@link Role}
// */
//@Service
//public class RoleMapper {
//
//    public Role entityRoleToAdministrationRole(EntityRole entityRole) {
//        Role role = new Role();
//
//        role.setId(entityRole.getId());
//        role.setName(entityRole.getName());
//        role.setCreatedDate(entityRole.getCreatedAt());
//        role.setLastModifiedDate(entityRole.getLastModifiedAt());
//        role.setCreatedBy(entityRole.getCreatedBy() != null ? entityRole.getCreatedBy().getFullName() : Constants.SYSTEM);
//        role.setLastModifiedBy(entityRole.getLastModifiedBy() != null ? entityRole.getLastModifiedBy().getFullName() : null);
//        role.setDefault(entityRole.getRoleType() != null);
//
//        return role;
//    }
//
//    public Page<Role> entityRolesToAdministrationRoles(Page<Object[]> entityRoles) {
//        return entityRoles
//            .map(objects -> {
//                Role role = entityRoleToAdministrationRole((EntityRole) objects[0]);
//
//                role.setMemberCount((long) objects[1]);
//
//                return role;
//            });
//    }
//
//    public Page<CustomUser> entityUserToRoleUser(Page<EntityUser> entityUsers){
//        return entityUsers.map(
//            user -> {
//                CustomUser customUser = new CustomUser();
//
//                customUser.setUserId(user.getId());
//                customUser.setEmail(user.getEmail());
//                customUser.setUserFullName(user.getFullName());
//                customUser.setCreatedDate(user.getCreatedAt());
//
//                return customUser;
//            }
//        );
//    }
//}
