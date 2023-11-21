package com.kjms.service.graphql;

import com.kjms.repository.published.EntityPublishedJournalRepository;
import com.kjms.service.graphql.dto.JournalFilter;
import com.kjms.service.graphql.dto.JournalPageableResponse;
import com.kjms.service.graphql.dto.PageInput;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.transaction.Transactional;

@Transactional
public class JournalGraphQLQuery implements GraphQLQueryResolver {

    private final EntityPublishedJournalRepository entityPublishedJournalRepository;

    public JournalGraphQLQuery(EntityPublishedJournalRepository entityPublishedJournalRepository) {

        this.entityPublishedJournalRepository = entityPublishedJournalRepository;
    }

    public JournalPageableResponse journals(JournalFilter filter, PageInput pageInput, JournalSortBy sortBy) {
        // Create a Pageable object for pagination
        Pageable pageable = PageRequest.of(pageInput.getPage() - 1, pageInput.getSize(), getSort(sortBy));


        return new JournalPageableResponse();
    }

    public Sort getSort(JournalSortBy sortBy) {

        switch (sortBy) {
            case NAME_ASC:
                return Sort.by("journal.name").ascending();
            case NAME_DESC:
                return Sort.by("journal.name").descending();
            default:
                return Sort.by("journal.name").ascending();
        }
    }

}
