package org.example.demo.domain;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * 同商品依照數量而有不同的打折，例如滿 2 件以上打 9 折，滿四件以上打 8 折。
 */
@Slf4j
public class QtyLevelPercentageDiscountRule implements DiscountRule {
    private final String targetTag;
    private NavigableMap<Integer, Double> qtyDiscounts = new TreeMap<>();

    public QtyLevelPercentageDiscountRule(String targetTag, int minQty, double percentage) {
        this.targetTag = targetTag;
        this.qtyDiscounts.put(Integer.MIN_VALUE, 100d); // 初始化
        this.qtyDiscounts.put(minQty, percentage);
    }

    public QtyLevelPercentageDiscountRule addLevel(int minQty, double percentage) {
        this.qtyDiscounts.put(minQty, percentage);
        return this;
    }

    @Override
    public double getDiscount(Cart cart) {
        List<Product> products = cart.getProducts(targetTag);
        int size = products.size();

        return size * products.getFirst().getPrice() * (100 - this.qtyDiscounts.floorEntry(size).getValue()) / 100;
    }
}
