//package com.kjms.service.dto.requests;
//
//import io.swagger.v3.oas.annotations.media.Schema;
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.NotNull;
//
///**
// * Represents a request object for update Group.
// */
//@Schema(description = "Request Body for Update Group", title = "Group Update")
//public class GroupUpdateRequest {
//
//    @Schema(required = true)
//    @NotNull
//    private Long id;
//
//    @Schema(required = true, defaultValue = "New Group")
//    @NotNull
//    @NotBlank
//    private String name;
//
//    @Schema(required = true, defaultValue = "This is test description")
//    @NotNull
//    @NotBlank
//    private String description;
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//}
