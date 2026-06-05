package org.example.demo.domain;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 超值配折扣
 */
@Slf4j
public class ValueDealDiscountRule implements DiscountRule {
    private List<ValueDeal> valueDeals = new ArrayList<>();

    public ValueDealDiscountRule(String firstTag, String secondTag, double price) {
        valueDeals.add(new ValueDeal(firstTag, secondTag, price));
    }

    public ValueDealDiscountRule addValueDeal(String firstTag, String secondTag, double price) {
        valueDeals.add(new ValueDeal(firstTag, secondTag, price));
        return this;
    }

    @Override
    public double getDiscount(Cart cart) {
        double totalDiscount = 0;
        List<Product> products = new ArrayList<>(cart.getProducts());

        for (ValueDeal valueDeal : valueDeals) {
            List<Product> firstProducts = products.stream().filter(product -> product.containsTag(valueDeal.firstTag)).toList();
            List<Product> secondProducts = products.stream().filter(product -> product.containsTag(valueDeal.secondTag)).toList();

            if (firstProducts.isEmpty() || secondProducts.isEmpty()) {
                continue;
            }

            for (int i = 0; i < Math.min(firstProducts.size(), secondProducts.size()); i++) {
                products.remove(firstProducts.get(i));
                products.remove(secondProducts.get(i));
                totalDiscount += firstProducts.get(i).getPrice() + secondProducts.get(i).getPrice() - valueDeal.price;
            }
        }
        return totalDiscount;
    }

    private class ValueDeal {
        private final String firstTag;
        private final String secondTag;
        private final double price;

        private ValueDeal(String firstTag, String secondTag, double price) {
            this.firstTag = firstTag;
            this.secondTag = secondTag;
            this.price = price;
        }
    }
}
