package com.example.demo.repositories.search_criteria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderCriteria {
    private String key;
    private Boolean desc;

    public OrderCriteria(String key, String direction) {
        desc = !direction.equals("asc");
        this.key = key;
    }
}
