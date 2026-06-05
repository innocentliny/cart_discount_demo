package org.example.demo.domain;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 同商品滿 N 件打折，例如滿 2 件打 9 折，買 3 件時，只有 2 件打折，1 件原價。
 */
@Slf4j
public class QuantityPercentageDiscountRule implements DiscountRule {
    private final String targetTag;
    private final int quantity;
    private final double percentage;

    public QuantityPercentageDiscountRule(String targetTag, int quantity, double percentage) {
        this.targetTag = targetTag;
        this.quantity = quantity;
        this.percentage = percentage;
    }

    @Override
    public double getDiscount(Cart cart) {
        List<Product> matchedProducts = new ArrayList<>();
        double totalDiscount = 0d;

        for (Product product : cart.getProducts(targetTag)) {
            matchedProducts.add(product);
            if (matchedProducts.size() == quantity) {
                double discount = matchedProducts.stream().mapToDouble(Product::getPrice).sum() * (100 - percentage) / 100;
                log.info("Discount {} for {}", discount, matchedProducts);
                totalDiscount += discount;
                matchedProducts.clear();
            }
        }

        return totalDiscount;
    }
}
