package org.example.demo.domain;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 指定商品第二件打折
 */
@Slf4j
public class SecondProductPercentageDiscountRule implements DiscountRule {
    private final String targetTag;
    private final double percentage;

    public SecondProductPercentageDiscountRule(String targetTag, double percentage) {
        this.targetTag = targetTag;
        this.percentage = percentage;
    }

    @Override
    public double getDiscount(Cart cart) {
        List<Product> matchedProducts = new ArrayList<>(2);
        double totalDiscount = 0d;
        for (Product product : cart.getProducts(targetTag)) {
            matchedProducts.add(product);
            if (matchedProducts.size() == 2) {
                log.info("指定商品第二件打折 {}% for {}", percentage, product);
                totalDiscount += product.getPrice() * (100 - percentage) / 100;
                matchedProducts.clear();
            }
        }
        return totalDiscount;
    }
}
