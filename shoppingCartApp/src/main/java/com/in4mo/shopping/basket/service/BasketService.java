package com.in4mo.shopping.basket.service;

import com.in4mo.shopping.basket.model.Basket;
import com.in4mo.shopping.basket.model.PurchasedItems;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class BasketService {

    @Value("${in4mo.shop}")
    private String shop;

    @Value("${in4mo.street}")
    private String street;

    @Value("${in4mo.pincode}")
    private String pincode;

    @Value("${in4mo.nip}")
    private String nip;

    @Value("${apple.price.for.one}")
    private BigDecimal priceOfOneApple;

    @Value("${orange.price.for.one}")
    private BigDecimal priceOfOneOrange;

    @Value("${banana.price.for.one}")
    private BigDecimal priceOfOneBanana;

    @Value("${papaya.price.for.one}")
    private BigDecimal priceOfOnePapaya;

    @Value("${papayas.three.price.of.two}")
    private BigDecimal priceOfThreePapayasInCostOfTwo;

    private static final String KEY_APPLE = "Apples";
    private static final String KEY_ORANGE = "Oranges";
    private static final String KEY_BANANA = "Bananas";
    private static final String KEY_PAPAYA = "Papaya";
    private static final String KEY_PAPAYA_PROMO = "Papayas";

    private static final BigDecimal numberHundred = new BigDecimal(100);

    private Basket getItemPriceFromProperties() {
        Map<String, BigDecimal> fruitPriceMap = new HashMap<>();
        fruitPriceMap.put(KEY_APPLE, priceOfOneApple.divide(numberHundred));
        fruitPriceMap.put(KEY_ORANGE, priceOfOneOrange.divide(numberHundred));
        fruitPriceMap.put(KEY_BANANA, priceOfOneBanana.divide(numberHundred));
        fruitPriceMap.put(KEY_PAPAYA, priceOfOnePapaya.divide(numberHundred));
        fruitPriceMap.put(KEY_PAPAYA_PROMO, priceOfThreePapayasInCostOfTwo.divide(numberHundred));
        return new Basket(fruitPriceMap);
    }

    public String getItemsWithPrice() {
        StringBuilder list = new StringBuilder(String.format("---%s---", shop))
                .append(String.format("%n     Date: %s", LocalDate.now()))
                .append(String.format("%n     Price of each item"))
                .append(String.format("%n----------------------------"))
                .append(String.format("%n Apple  x 1 : %s¢", priceOfOneApple))
                .append(String.format("%n Orange x 1 : %s¢", priceOfOneOrange))
                .append(String.format("%n Banana x 1 : %s¢", priceOfOneBanana))
                .append(String.format("%n Papaya x 1 : %s¢", priceOfOnePapaya))
                .append(String.format("%n Promotion on papaya: Buy 3 in price of 2 : %s€", priceOfThreePapayasInCostOfTwo.divide(numberHundred)));
        return list.toString();
    }

    public String getReceiptAfterCalculation(PurchasedItems noOfItems) {
        validationOfNumberOfItemsInput(noOfItems);
        Map<String, BigDecimal> itemsPurchasedMap = new HashMap<>();
        itemsPurchasedMap.put(KEY_APPLE, noOfItems.getNumberOfApples());
        itemsPurchasedMap.put(KEY_ORANGE, noOfItems.getNumberOfOranges());
        itemsPurchasedMap.put(KEY_BANANA, noOfItems.getNumberOfBananas());
        itemsPurchasedMap.put(KEY_PAPAYA_PROMO, noOfItems.getNumberOfPapayas());

        StringBuilder receipt = new StringBuilder(String.format("---%s---", shop))
                .append(String.format("%n     %s", street))
                .append(String.format("%n     %s", pincode))
                .append(String.format("%n     %s", nip))
                .append(String.format("%n     Date: %s", LocalDate.now()))
                .append(String.format("%n------------------------------------"))
                .append(String.format("%n         Receipt         "))
                .append(String.format("%n------------------------------------"));

        BigDecimal sum = BigDecimal.ZERO;
        for (Map.Entry<String, BigDecimal> entry : getItemPriceFromProperties().getFruitPriceMap().entrySet()) {
            String k = entry.getKey();
            BigDecimal v = entry.getValue();
            if (k.equals(KEY_PAPAYA_PROMO)) {
                sum = sum.add(promotionCalculation(itemsPurchasedMap.get(k), priceOfOnePapaya.divide(numberHundred), v));
                receipt.append(String.format("%n%s   %s x %s€       : %s€ ", k, itemsPurchasedMap.get(k),
                        priceOfOnePapaya.divide(numberHundred), promotionCalculation(itemsPurchasedMap.get(k), priceOfOnePapaya.divide(numberHundred), v)));

            } else {
                if (!k.equals(KEY_PAPAYA)) {
                    sum = sum.add(itemsPurchasedMap.get(k).multiply(v));
                    receipt.append(String.format("%n%s    %s x %s€     : %s€ ", k, itemsPurchasedMap.get(k), v, itemsPurchasedMap.get(k).multiply(v)));
                }
            }
        }
        receipt.append(String.format("%n===================================="))
                .append(String.format("%nTotal Price     : %s€", sum));
        return receipt.toString();
    }

    protected BigDecimal promotionCalculation(BigDecimal noOfPapayas, BigDecimal pricePerPapaya, BigDecimal priceOfTwoPapayas) {
        if (noOfPapayas == null) {
            noOfPapayas = BigDecimal.ZERO;
        }
        BigDecimal numberThree = new BigDecimal(3);
        if (noOfPapayas.compareTo(numberThree) == 0) {
            return priceOfTwoPapayas;
        }
        if (noOfPapayas.compareTo(numberThree) < 0) {
            return noOfPapayas.multiply(priceOfOnePapaya);
        }

        BigDecimal remainder = noOfPapayas.remainder(numberThree);
        BigDecimal quotient = noOfPapayas.divideToIntegralValue(numberThree);
        return (priceOfTwoPapayas.multiply(quotient)).add(pricePerPapaya.multiply(remainder));
    }

    private void validationOfNumberOfItemsInput(PurchasedItems noOfItems) {
        if (noOfItems.getNumberOfApples() == null
                && noOfItems.getNumberOfOranges() == null
                && noOfItems.getNumberOfBananas() == null
                && noOfItems.getNumberOfPapayas() == null) {
            throw new NullPointerException("Please try again and enter proper values");
        }
        if (noOfItems.getNumberOfApples() == null) {
            noOfItems.setNumberOfApples(BigDecimal.ZERO);
        }
        if (noOfItems.getNumberOfOranges() == null) {
            noOfItems.setNumberOfOranges(BigDecimal.ZERO);
        }
        if (noOfItems.getNumberOfBananas() == null) {
            noOfItems.setNumberOfBananas(BigDecimal.ZERO);
        }
        if (noOfItems.getNumberOfPapayas() == null) {
            noOfItems.setNumberOfPapayas(BigDecimal.ZERO);
        }
    }
}


