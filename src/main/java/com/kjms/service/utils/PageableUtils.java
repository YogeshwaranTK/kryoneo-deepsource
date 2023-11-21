package com.kjms.service.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;

import java.util.Collections;
import java.util.Set;

public interface PageableUtils {

    /**
     * <p>
     * This method is used for sorting by alias columns
     * Example : If the sort key is name then it will change (name)
     * </p>
     *
     * @param pageable the pageable to change property.
     * @param sortName column to encapsulate with parenthesis.
     * @return {@link Pageable} with parenthesis encapsulation of sort property.
     */
    static Pageable parenthesisEncapsulation(final Pageable pageable, String sortName) {

        Sort sort = Sort.by(Collections.emptyList());

        for (final Sort.Order order : pageable.getSort()) {
            if (order.getProperty().equals(sortName)) {
                sort = sort.and(JpaSort.unsafe(order.getDirection(), "(" + order.getProperty() + ")"));
            } else {
                sort = sort.and(Sort.by(order.getDirection(), order.getProperty()));
            }
        }

        return PageRequest.of(
            pageable.getPageNumber(),
            pageable.getPageSize(),
            sort
        );
    }

    // This method is to check sort properties contains allowed sort properties
    static boolean onlyContainsAllowedProperties(Pageable pageable, Set<String> properties) {
        return pageable.getSort().stream().map(Sort.Order::getProperty)
            .allMatch(properties::contains);
    }
}
