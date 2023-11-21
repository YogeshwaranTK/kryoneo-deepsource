//package com.kjms.service.dto.requests;
//
//import io.swagger.v3.oas.annotations.media.Schema;
//import java.util.List;
//import javax.validation.constraints.NotEmpty;
//import javax.validation.constraints.NotNull;
//
///**
// * Represents a request object for create group user.
// */
//@Schema(description = "Request Body for Create Group User", title = "Group User Create")
//public class GroupUserCreateRequest {
//
//    @Schema(required = true)
//    @NotNull
//    private Long groupId;
//
//    @Schema(required = true)
//    @NotNull
//    @NotEmpty
//    private List<String> userIds;
//
//    public Long getGroupId() {
//        return groupId;
//    }
//
//    public void setGroupId(Long groupId) {
//        this.groupId = groupId;
//    }
//
//    public List<String> getUserIds() {
//        return userIds;
//    }
//
//    public void setUserIds(List<String> userIds) {
//        this.userIds = userIds;
//    }
//}
