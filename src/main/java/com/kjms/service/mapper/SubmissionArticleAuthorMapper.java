package com.kjms.service.mapper;

import com.kjms.domain.EntitySubmissionAuthor;
import com.kjms.domain.EntitySubmissionAuthorAffiliation;
import com.kjms.service.dto.SubmissionArticleAuthor;
import com.kjms.service.dto.SubmissionArticleAuthorAffiliation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Mapper class responsible for converting {@link EntitySubmissionAuthor} to {@link SubmissionArticleAuthor}
 */
@Service
public class SubmissionArticleAuthorMapper {

    private SubmissionArticleAuthor entitySubmissionArticleAuthorToSubmissionArticleAuthor(
        EntitySubmissionAuthor entitySubmissionAuthor
    ) {
        SubmissionArticleAuthor submissionArticleAuthor = new SubmissionArticleAuthor();

        submissionArticleAuthor.setId(entitySubmissionAuthor.getId());
        submissionArticleAuthor.setPrefix(entitySubmissionAuthor.getPrefix());
        submissionArticleAuthor.setFirstName(entitySubmissionAuthor.getFirstName());
        submissionArticleAuthor.setSurName(entitySubmissionAuthor.getSurName());
        submissionArticleAuthor.setMiddleName(entitySubmissionAuthor.getMiddleName());
        submissionArticleAuthor.setEmail(entitySubmissionAuthor.getEmail());
        submissionArticleAuthor.setOrcidId(entitySubmissionAuthor.getOrcidId());

        submissionArticleAuthor.setPrimary(entitySubmissionAuthor.getPrimary());

        return submissionArticleAuthor;
    }

    public List<SubmissionArticleAuthor> entitySubmissionArticlesAuthorToSubmissionArticleAuthors(
        List<EntitySubmissionAuthor> entitySubmissionAuthors
    ) {
        return entitySubmissionAuthors.stream().map(this::entitySubmissionArticleAuthorToSubmissionArticleAuthor)
            .collect(Collectors.toList());
    }

    public Map<Long,List<SubmissionArticleAuthorAffiliation>> entitySubmissionArticleAuthorAffiliationsToSubmissionArticleAuthorAffiliation(
        List<EntitySubmissionAuthorAffiliation> entitySubmissionAuthorAffiliations
    ) {
        return entitySubmissionAuthorAffiliations.stream()
            .map(entitySubmissionAuthorAffiliation -> {

                SubmissionArticleAuthorAffiliation submissionArticleAuthorAffiliation = new SubmissionArticleAuthorAffiliation();

                submissionArticleAuthorAffiliation.setAffiliation(entitySubmissionAuthorAffiliation.getAffiliation());
                submissionArticleAuthorAffiliation.setId(entitySubmissionAuthorAffiliation.getId());
                submissionArticleAuthorAffiliation.setSubmissionAuthorId(entitySubmissionAuthorAffiliation.getSubmissionArticleAuthor().getId());

                return submissionArticleAuthorAffiliation;
            }).collect(Collectors.groupingBy(SubmissionArticleAuthorAffiliation::getSubmissionAuthorId));
    }
}
