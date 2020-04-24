package com.in4mo.shopping.basket.model;

import java.math.BigDecimal;
import java.util.Map;

public class Basket {

    private Map<String, BigDecimal> fruitPriceMap;

    public Basket(Map<String, BigDecimal> fruitPriceMap) {
        this.fruitPriceMap = fruitPriceMap;
    }

    public Map<String, BigDecimal> getFruitPriceMap() {
        return fruitPriceMap;
    }
}
