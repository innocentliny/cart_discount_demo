# 藉由 TDD 展示計算購物車折扣的小 demo
_**本專案僅做為示範用途**_  

使用 TDD 方式，展示計算購物車中，不同商品的各自折扣，最後得到折扣後的總價。  

## 設計理念
- 每個商品有指定的促銷方案。
- 每種折扣規則有指定的促銷方案。
- 每個折扣規則從購物車取出符合促銷方案的商品計算折扣。
- 為折扣前的總售價 - 總折扣金額 = 最終付款金額。

## 折扣規則
目前有以下折扣規則：
- [MinPriceDiscountRule](src/main/java/org/example/demo/domain/MinPriceDiscountRule.java)：指定商品滿最低金額就折抵指定金額，折抵不累計。
- [QtyLevelPercentageDiscountRule](src/main/java/org/example/demo/domain/QtyLevelPercentageDiscountRule.java)：同商品依照數量而有不同的打折，例如滿 2 件以上打 9 折，滿 4 件以上打 8 折。
- [QuantityDiscountRule](src/main/java/org/example/demo/domain/QuantityDiscountRule.java)：指定商品滿 N 件折抵指定金額，折抵可累計。
- [QuantityPercentageDiscountRule](src/main/java/org/example/demo/domain/QuantityPercentageDiscountRule.java)：同商品滿 N 件打折，例如滿 2 件打 9 折，買 3 件時，只有 2 件打折，1 件原價。
- [SecondProductPercentageDiscountRule](src/main/java/org/example/demo/domain/SecondProductPercentageDiscountRule.java)：指定商品第 2 件打折。
- [SecondProductPriceDiscountRule](src/main/java/org/example/demo/domain/SecondProductPriceDiscountRule.java)：指定商品第 2 件 N 元。
- [ValueDealDiscountRule](src/main/java/org/example/demo/domain/ValueDealDiscountRule.java)：超值配折扣，例如指定飲料跟指定麵包合購，只要 N 元。

請參考 unit test：[CartDiscountTest.java](src/test/java/org/example/demo/CartDiscountTest.java)

## 設計原則
- Single Responsibility Principle
- Open-Close Principle
- Dependency Inversion Principle
- Interface Segregation Principle