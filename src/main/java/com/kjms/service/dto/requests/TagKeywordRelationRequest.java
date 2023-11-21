//package com.kjms.service.requests;
//
//import com.kjms.domain.RelationTypes;
//import io.swagger.v3.oas.annotations.media.Schema;
//import java.util.List;
//import javax.validation.constraints.NotNull;
//
///**
// * Represents a request object for update Tag relation.
// */
//@Schema(description = "Request Body for Update Tag Relation", title = "Tag Relation")
//public class TagKeywordRelationRequest {
//
//    @Schema(required = true)
//    @NotNull
//    private RelationTypes relationType;
//
//    @Schema(required = true)
//    @NotNull
//    private List<String> tagKeywords;
//
//    public RelationTypes getRelationType() {
//        return relationType;
//    }
//
//    public void setRelationType(RelationTypes relationType) {
//        this.relationType = relationType;
//    }
//
//    public List<String> getTagKeywords() {
//        return tagKeywords;
//    }
//
//    public void setTagKeywords(List<String> tags) {
//        this.tagKeywords = tags;
//    }
//}
