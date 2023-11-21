package com.kjms.config;

import com.kjms.repository.published.EntityPublishedJournalRepository;
import com.kjms.service.graphql.JournalGraphQLQuery;
import com.kjms.service.graphql.exception.GraphQLErrorAdapter;
import graphql.ExceptionWhileDataFetching;
import graphql.GraphQL;
import graphql.GraphQLError;
import graphql.kickstart.execution.error.GraphQLErrorHandler;
import graphql.schema.GraphQLSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class GraphQLConfiguration {

    @Bean
    public GraphQL graphQL(GraphQLSchema graphQLSchema) {
        return GraphQL.newGraphQL(graphQLSchema).build();
    }

    @Bean
    public GraphQLErrorHandler errorHandler() {
        return new GraphQLErrorHandler() {
            @Override
            public List<GraphQLError> processErrors(List<GraphQLError> errors) {
                // Separate errors into client errors and server errors
                List<GraphQLError> clientErrors = errors.stream()
                    .filter(this::isClientError)
                    .collect(Collectors.toList());

                List<GraphQLError> serverErrors = errors.stream()
                    .filter(e -> !isClientError(e))
                    .map(GraphQLErrorAdapter::new)
                    .collect(Collectors.toList());

                List<GraphQLError> e = new ArrayList<>();
                e.addAll(clientErrors);
                e.addAll(serverErrors);
                return e;
            }

            // Check if an error is a client error
            protected boolean isClientError(GraphQLError error) {
                return !(error instanceof ExceptionWhileDataFetching || error instanceof Throwable);
            }
        };
    }

    //     Define a bean for the GraphQL query resolver
    @Bean
    public JournalGraphQLQuery query(EntityPublishedJournalRepository entityPublishedJournalRepository) {
        return new JournalGraphQLQuery(entityPublishedJournalRepository);
    }
}
