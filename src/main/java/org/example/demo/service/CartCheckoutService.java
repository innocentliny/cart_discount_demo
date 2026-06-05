package org.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.example.demo.domain.Cart;
import org.example.demo.domain.DiscountRule;

import java.util.List;

/**
 * 購物車結帳
 */
@Slf4j
public class CartCheckoutService {
    /**
     * @return 結帳價格 = 總價 - 總折扣
     */
    public double getCheckoutPrice(Cart cart, List<DiscountRule> rules) {
        return cart.getTotalPrice() - rules.stream().mapToDouble(discountRule -> discountRule.getDiscount(cart)).sum();
    }

}
