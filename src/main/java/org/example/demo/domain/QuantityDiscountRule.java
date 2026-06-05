package org.example.demo.domain;

import lombok.extern.slf4j.Slf4j;

/**
 * 指定商品滿 N 件折抵指定金額，折抵可累計。
 */
@Slf4j
public class QuantityDiscountRule implements DiscountRule {
    private final String targetTag;
    private final int quantity;
    private final double discountAmount;

    public QuantityDiscountRule(String targetTag, int quantity, double discountAmount) {
        this.targetTag = targetTag;
        this.quantity = quantity;
        this.discountAmount = discountAmount;
    }

    @Override
    public double getDiscount(Cart cart) {
        double totalDiscount = 0d;
        long remain = cart.getCount(this.targetTag);

        while ((remain -= quantity) >= 0) {
            totalDiscount += discountAmount;
        }

        return totalDiscount;
    }
}
