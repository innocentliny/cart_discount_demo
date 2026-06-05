package org.example.demo.domain;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 指定商品第二件 N 元
 */
@Slf4j
public class SecondProductPriceDiscountRule implements DiscountRule {
    private final String targetTag;
    private final double targetPrice;

    public SecondProductPriceDiscountRule(String targetTag, double targetPrice) {
        this.targetTag = targetTag;
        this.targetPrice = targetPrice;
    }

    @Override
    public double getDiscount(Cart cart) {
        List<Product> matchedProducts = new ArrayList<>(2);
        double totalDiscount = 0;

        for (Product product : cart.getProducts(targetTag)) {
            matchedProducts.add(product);
            if (matchedProducts.size() == 2) {
                log.info("指定商品第二件 {} 元 for {}", targetPrice, product);
                totalDiscount += product.getPrice() - targetPrice;
                matchedProducts.clear();
            }
        }

        return totalDiscount;
    }
}
