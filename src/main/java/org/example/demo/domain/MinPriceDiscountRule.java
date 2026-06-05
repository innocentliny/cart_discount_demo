package org.example.demo.domain;

/**
 * 指定商品滿最低金額就折抵指定金額，折抵不累計。
 */
public class MinPriceDiscountRule implements DiscountRule {
    private final String targetTag;
    private final double minPrice;
    private final double discount;

    public MinPriceDiscountRule(String targetTag, double minPrice, double discount) {
        this.targetTag = targetTag;
        this.minPrice = minPrice;
        this.discount = discount;
    }

    @Override
    public double getDiscount(Cart cart) {
        if (cart.getTotalPrice(targetTag) >= minPrice) {
            return discount;
        }
        return 0;
    }
}
