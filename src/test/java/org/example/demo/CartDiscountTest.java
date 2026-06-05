package org.example.demo;

import org.example.demo.domain.*;
import org.example.demo.service.CartCheckoutService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CartDiscountTest {
    private Cart cart;
    private CartCheckoutService checkoutService = new CartCheckoutService();
    private double checkoutPrice;
    private List<DiscountRule> rules;

    @BeforeEach
    void setUp() {
        cart = new Cart();
        checkoutPrice = 0;
        rules = new ArrayList<>();
    }

    // 買兩箱以上打 88 折
    @Test
    void buy_4_boxes_and_88_percent_discount() {
        given_product_in_cart(4, new Product("一箱水", 10d, Set.of("滿量折扣")));
        given_discount_rule(new QuantityPercentageDiscountRule("滿量折扣", 2, 88d));

        when_get_checkout_price();

        then_checkout_price_is((10 + 10) * 0.88d * 2);
    }

    private void given_discount_rule(DiscountRule rule) {
        rules.add(rule);
    }

    private void then_checkout_price_is(double expected) {
        Assertions.assertEquals(expected, checkoutPrice);
    }

    private void given_product_in_cart(int count, Product product) {
        for (int i = 0; i < count; i++) {
            cart.addProduct(product);
        }
    }

    // 滿額折扣
    @Test
    void equal_greater_than_min_price_and_get_discount() {
        given_product_in_cart(2, new Product("柚子酒", 500d, Set.of("滿額折扣")));
        given_discount_rule(new MinPriceDiscountRule("滿額折扣", 1000d, 100d));

        when_get_checkout_price();

        then_checkout_price_is(900d);
    }

    private void when_get_checkout_price() {
        checkoutPrice = checkoutService.getCheckoutPrice(cart, rules);
    }

    // 多種折扣合併計算
    @Test
    void test_combined_discounts() {
        given_product_in_cart(4, new Product("product_1", 10d, Set.of("滿量折扣")));
        given_product_in_cart(2, new Product("product_2", 500d, Set.of("滿額折扣")));
        given_product_in_cart(6, new Product("product_3", 100d, Set.of("滿件折抵")));
        given_product_in_cart(3, new Product("product_4", 100d, Set.of("雞湯塊")));
        given_product_in_cart(2, new Product("product_5", 20d, Set.of("第二件N元")));

        given_discount_rule(new QuantityPercentageDiscountRule("滿量折扣", 2, 88d));
        given_discount_rule(new MinPriceDiscountRule("滿額折扣", 1000d, 100d));
        given_discount_rule(new QuantityDiscountRule("滿件折抵", 6, 100d));
        given_discount_rule(new SecondProductPercentageDiscountRule("雞湯塊", 50d));
        given_discount_rule(new SecondProductPriceDiscountRule("第二件N元", 10d));

        when_get_checkout_price();

        double product1FinalPrice = (10 + 10) * 0.88d * 2;
        double product2FinalPrice = 500 * 2 - 100;
        double product3FinalPrice = 6 * 100d - 100;
        double product4FinalPrice = 100 + 100 * 0.5 + 100;
        double product5FinalPrice = 20 + 10;
        then_checkout_price_is(product1FinalPrice +
                product2FinalPrice +
                product3FinalPrice +
                product4FinalPrice +
                product5FinalPrice);
    }

    // 超值配折扣
    @Test
    void test_value_deal() {
        given_product_in_cart(1, new Product("drink", 39d, Set.of("超值配飲料39")));

        given_product_in_cart(1, new Product("drink", 39d, Set.of("超值配飲料39")));
        given_product_in_cart(1, new Product("food", 39d, Set.of("超值配鮮食39")));

        given_product_in_cart(1, new Product("drink", 49d, Set.of("超值配飲料49")));
        given_product_in_cart(1, new Product("food", 49d, Set.of("超值配鮮食49")));

        given_product_in_cart(1, new Product("drink", 59d, Set.of("超值配飲料59")));
        given_product_in_cart(1, new Product("food", 59d, Set.of("超值配鮮食59")));

        given_product_in_cart(1, new Product("drink", 49d, Set.of("超值配飲料49")));
        given_product_in_cart(1, new Product("food", 59d, Set.of("超值配鮮食59")));

        given_product_in_cart(1, new Product("drink", 59d, Set.of("超值配飲料59")));
        given_product_in_cart(1, new Product("food", 49d, Set.of("超值配鮮食49")));

        given_discount_rule(new ValueDealDiscountRule("超值配飲料39", "超值配鮮食39", 39d)
                .addValueDeal("超值配飲料49", "超值配鮮食49", 49d)
                .addValueDeal("超值配飲料59", "超值配鮮食59", 59d)
                .addValueDeal("超值配飲料49", "超值配鮮食59", 49d) // 跨區超值配
                .addValueDeal("超值配飲料59", "超值配鮮食49", 59d)  // 跨區超值配
        );

        when_get_checkout_price();

        then_checkout_price_is(39 + 39 + 49 + 59 + 49 + 59);
    }

    // 數量促銷：滿 4 件打 8 折。
    @Test
    void test_buy_4_qty_and_get_80_percent_discount() {
        given_product_in_cart(4, new Product("food", 100d, Set.of("數量促銷")));

        // 滿 2 件打 9 折，4 件以上打 8 折。
        given_discount_rule(new QtyLevelPercentageDiscountRule("數量促銷", 2, 90d)
                .addLevel(4, 80d));

        when_get_checkout_price();

        then_checkout_price_is(100 * 4 * 0.8);
    }

    // 數量促銷：滿 2 件，但不足 4 件，故打 9 折。
    @Test
    void test_buy_3_qty_and_get_90_percent_discount() {
        given_product_in_cart(3, new Product("food", 100d, Set.of("數量促銷")));

        // 滿 2 件打 9 折，4 件以上打 8 折。
        given_discount_rule(new QtyLevelPercentageDiscountRule("數量促銷", 2, 90d)
                .addLevel(4, 80d));

        when_get_checkout_price();

        then_checkout_price_is(100 * 3 * 0.9);
    }
}
