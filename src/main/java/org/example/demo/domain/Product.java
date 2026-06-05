package org.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Set;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Product {
    private final String name;
    private final Double price; // TODO should use BigDecimal
    /**
     * Discount tag
     */
    private final Set<String> tags;

    public boolean containsTag(String tag) {
        return this.tags.contains(tag);
    }
}
