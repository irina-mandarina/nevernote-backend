package com.example.demo.repositories.specification.specification_builders;

import com.example.demo.Entities.Log;
import com.example.demo.repositories.specification.LogSpecification;
import com.example.demo.types.SearchOperation;
import com.example.demo.repositories.search_criteria.SpecSearchCriteria;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;

public class LogSpecificationsBuilder {
    private final List<SpecSearchCriteria> params;

    public LogSpecificationsBuilder() {
        params = new ArrayList<>();
    }

    // API

    public final LogSpecificationsBuilder with(final String key, final String operation, final Object value, final String prefix, final String suffix) {
        return with(null, key, operation, value, prefix, suffix);
    }

    public final LogSpecificationsBuilder with(final String orPredicate, final String key,
                                               final String operation, final Object value,
                                               final String prefix, final String suffix) {
        SearchOperation op = SearchOperation.getSimpleOperation(operation.charAt(0));
        if (op != null) {
            if (op == SearchOperation.EQUALITY) { // the operation may be complex operation
                final boolean startWithAsterisk = prefix != null && prefix.contains(SearchOperation.ZERO_OR_MORE_REGEX);
                final boolean endWithAsterisk = suffix != null && suffix.contains(SearchOperation.ZERO_OR_MORE_REGEX);

                if (startWithAsterisk && endWithAsterisk) {
                    op = SearchOperation.CONTAINS;
                } else if (startWithAsterisk) {
                    op = SearchOperation.ENDS_WITH;
                } else if (endWithAsterisk) {
                    op = SearchOperation.STARTS_WITH;
                }
            }
            params.add(new SpecSearchCriteria(orPredicate, key, op, value));
        }
        return this;
    }

    public Specification<Log> build() {
        if (params.size() == 0)
            return null;

        Specification<Log> result = new LogSpecification(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            result = params.get(i).isOrPredicate()
                    ? Specification.where(result).or(new LogSpecification(params.get(i)))
                    : Specification.where(result).and(new LogSpecification(params.get(i)));
        }

        return result;
    }

    public final LogSpecificationsBuilder with(LogSpecification spec) {
        params.add(spec.getCriteria());
        return this;
    }

    public final LogSpecificationsBuilder with(SpecSearchCriteria criteria) {
        params.add(criteria);
        return this;
    }
}
