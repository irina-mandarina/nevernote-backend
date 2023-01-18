package com.example.demo.repositories.search_criteria.consumers;
import java.util.function.Consumer;

import com.example.demo.Entities.Log;
import com.example.demo.repositories.search_criteria.SearchCriteria;
import com.example.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@RequiredArgsConstructor
public class LogSearchQueryCriteriaConsumer implements Consumer<SearchCriteria>{
    private Predicate predicate;
    private CriteriaBuilder builder;
    private Root<Log> r;

    public LogSearchQueryCriteriaConsumer(Predicate predicate, CriteriaBuilder builder, Root<Log> r) {
        super();
        this.predicate = predicate;
        this.builder = builder;
        this.r= r;
    }

    @Override
    public void accept(SearchCriteria param) {

        if (param.getOperation().equalsIgnoreCase(">")) {
            predicate = builder.and(predicate, builder.greaterThanOrEqualTo(r.get(param.getKey()), param.getValue().toString()));
        } else if (param.getOperation().equalsIgnoreCase("<")) {
            predicate = builder.and(predicate, builder.lessThanOrEqualTo(r.get(param.getKey()), param.getValue().toString()));
        } else if (param.getOperation().equalsIgnoreCase(":")) {
            if (r.get(param.getKey()).getJavaType() == String.class) {
                predicate = builder.and(predicate, builder.like(r.get(param.getKey()), "%" + param.getValue() + "%"));
            } else {
                predicate = builder.and(predicate, builder.equal(r.get(param.getKey()), param.getValue()));
            }
        }
    }

    public Predicate getPredicate() {
        return predicate;
    }
}