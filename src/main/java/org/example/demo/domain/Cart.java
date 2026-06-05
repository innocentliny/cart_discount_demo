package org.example.demo.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Cart {
    private List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        // TODO no null product
        products.add(product);
    }

    public List<Product> getProducts(String tag) {
        return products.stream().filter(product -> product.containsTag(tag)).toList();
    }

    public double getTotalPrice() {
        return products.stream().mapToDouble(Product::getPrice).sum();
    }

    public double getTotalPrice(String tag) {
        return getProducts(tag).stream().mapToDouble(Product::getPrice).sum();
    }

    public long getCount(String tag) {
        return products.stream().filter(product -> product.containsTag(tag)).count();
    }
}
