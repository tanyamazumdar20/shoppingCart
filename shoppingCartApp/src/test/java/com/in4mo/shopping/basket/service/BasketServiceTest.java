package com.in4mo.shopping.basket.service;

import com.in4mo.shopping.basket.model.PurchasedItems;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
class BasketServiceTest {

    @Autowired
    private BasketService basketService;

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

    @Value("${in4mo.shop}")
    private String shop;

    @Value("${in4mo.street}")
    private String street;

    @Value("${in4mo.pincode}")
    private String pincode;

    @Value("${in4mo.nip}")
    private String nip;

    private static final BigDecimal number100 = new BigDecimal(100);

    private static final String RECEIPT_OF_ITEMS = "---%s---" +
            "%n     %s" +
            "%n     %s" +
            "%n     %s" +
            "%n     Date: %s" +
            "%n------------------------------------" +
            "%n         Receipt         " +
            "%n------------------------------------" +
            "%nApples    %s x %s€     : %s€ " +
            "%nBananas    %s x %s€     : %s€ " +
            "%nPapayas   %s x %s€       : %s€ " +
            "%nOranges    %s x %s€     : %s€ " +
            "%n====================================" +
            "%nTotal Price     : %s€";

    @Test
    void testGetReceiptAfterCalculationWhenValuesAreNonNull() {
        assertThat(basketService.getReceiptAfterCalculation(getPurchasedItems())).isEqualTo(String.format(
                RECEIPT_OF_ITEMS,
                shop,
                street,
                pincode,
                nip,
                LocalDate.now(),
                getPurchasedItems().getNumberOfApples(), priceOfOneApple.divide(number100), new BigDecimal(0.50).setScale(2, RoundingMode.HALF_EVEN),
                getPurchasedItems().getNumberOfBananas(), priceOfOneBanana.divide(number100), new BigDecimal(0.15).setScale(2, RoundingMode.HALF_EVEN),
                getPurchasedItems().getNumberOfPapayas(), priceOfOnePapaya.divide(number100), new BigDecimal(2.0).setScale(1, RoundingMode.HALF_EVEN),
                getPurchasedItems().getNumberOfOranges(), priceOfOneOrange.divide(number100), new BigDecimal(0.9).setScale(1, RoundingMode.HALF_EVEN),
                new BigDecimal(3.55).setScale(2, RoundingMode.HALF_EVEN)));
    }

    @Test
    void testGetReceiptAfterCalculationWhenValuesAreNull() {

        Exception exception = assertThrows(NullPointerException.class, () -> {
            basketService.getReceiptAfterCalculation(getPurchasedItemsNull());
        });

        String expectedMessage = "Please try again and enter proper values";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void promotionCalculationWhenNoOfPapayaIsLessThanOrEqualsThree() {
        assertThat(basketService.promotionCalculation(BigDecimal.ZERO, priceOfOnePapaya, priceOfThreePapayasInCostOfTwo)).isEqualTo(BigDecimal.ZERO);
        assertThat(basketService.promotionCalculation(new BigDecimal(1), priceOfOnePapaya, priceOfThreePapayasInCostOfTwo)).isEqualTo(new BigDecimal(50));
        assertThat(basketService.promotionCalculation(new BigDecimal(2), priceOfOnePapaya, priceOfThreePapayasInCostOfTwo)).isEqualTo(new BigDecimal(100));
        assertThat(basketService.promotionCalculation(new BigDecimal(3), priceOfOnePapaya, priceOfThreePapayasInCostOfTwo)).isEqualTo(new BigDecimal(100));
    }

    @Test
    void promotionCalculationWhenNoOfPapayaIsGreaterThanThree() {
        assertThat(basketService.promotionCalculation(new BigDecimal(25), priceOfOnePapaya, priceOfThreePapayasInCostOfTwo)).isEqualTo(new BigDecimal(850));
        assertThat(basketService.promotionCalculation(new BigDecimal(20), priceOfOnePapaya, priceOfThreePapayasInCostOfTwo)).isEqualTo(new BigDecimal(700));
        assertThat(basketService.promotionCalculation(new BigDecimal(37), priceOfOnePapaya, priceOfThreePapayasInCostOfTwo)).isEqualTo(new BigDecimal(1250));
        assertThat(basketService.promotionCalculation(new BigDecimal(5), priceOfOnePapaya, priceOfThreePapayasInCostOfTwo)).isEqualTo(new BigDecimal(200));
    }

    @Test
    void promotionCalculationWhenNoOfPapayaIsNull() {
        assertThat(basketService.promotionCalculation(null, priceOfOnePapaya, priceOfThreePapayasInCostOfTwo)).isEqualTo(BigDecimal.ZERO);
    }

    private PurchasedItems getPurchasedItems() {
        PurchasedItems items = new PurchasedItems();
        items.setNumberOfApples(new BigDecimal(2));
        items.setNumberOfOranges(new BigDecimal(3));
        items.setNumberOfBananas(BigDecimal.ONE);
        items.setNumberOfPapayas(new BigDecimal(5));
        return items;
    }

    private PurchasedItems getPurchasedItemsNull() {
        PurchasedItems items = new PurchasedItems();
        items.setNumberOfApples(null);
        items.setNumberOfOranges(null);
        items.setNumberOfBananas(null);
        items.setNumberOfPapayas(null);
        return items;
    }
}