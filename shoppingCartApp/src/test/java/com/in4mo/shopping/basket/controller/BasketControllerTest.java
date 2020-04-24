package com.in4mo.shopping.basket.controller;

import com.in4mo.shopping.basket.model.Basket;
import com.in4mo.shopping.basket.model.PurchasedItems;
import com.in4mo.shopping.basket.service.BasketService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(BasketController.class)
class BasketControllerTest {

    @MockBean
    private BasketService basketService;

    @MockBean
    private PurchasedItems purchasedItems;

    @MockBean
    private Basket basket;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAllItems() throws Exception {
        //given
        when(basketService.getItemsWithPrice()).thenReturn(FIXTURES.responseFetchAllItems);

        // when
        RequestBuilder requestBuilderGet = MockMvcRequestBuilders.get("/items");
        final MvcResult result = mockMvc.perform(requestBuilderGet).andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getResponse().getContentAsString()).isEqualToNormalizingWhitespace(FIXTURES.responseFetchAllItems);
    }

    @Test
    void testPriceCalculationReceipt() throws Exception {
        //given
        when(basketService.getReceiptAfterCalculation(purchasedItems)).thenReturn(anyString());

        //when
        RequestBuilder requestBuilderPut = MockMvcRequestBuilders.put("/receipt")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(FIXTURES.requestBodyInJson);
        final MvcResult result = mockMvc.perform(requestBuilderPut).andReturn();

        //then
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    private static class FIXTURES {
        private static final BigDecimal priceOfOneApple = new BigDecimal("0.25");
        private static final BigDecimal priceOfOneOrange = new BigDecimal("0.30");
        private static final BigDecimal priceOfOneBanana = new BigDecimal("0.15");
        private static final BigDecimal priceOfOnePapaya = new BigDecimal("0.50");
        private static final BigDecimal priceOfThreePapayasInCostOfTwo = BigDecimal.ONE;
        private static final String shop = "In4mo Shopping Center";
        private static final String street = "Leborska 3B";
        private static final String pincode = "80-386 Gdansk";
        private static final String nip = "NIP 123-456-56-88";
        private static final String RECEIPT_OF_ITEMS = "---%s---" +
                "%n     %s      " +
                "%n     %s    " +
                "%n     %s     " +
                "%n     Date: %s" +
                "%n-------------------------" +
                "%n         Receipt         " +
                "%n-------------------------" +
                "%nApple  %s*%s€ : %s€ " +
                "%nOrange %s*%s€ : %s€ " +
                "%nBanana %s*%s€ : %s€ " +
                "%nPapaya %s*%s€ : %s€ " +
                "%n==========================" +
                "%nTotal Price: %s€";
        private static final String responseTotalPrice = String.format(
                RECEIPT_OF_ITEMS,
                shop,
                street,
                pincode,
                nip,
                LocalDate.now(),
                new BigDecimal(2), priceOfOneApple, new BigDecimal(0.50).setScale(2, RoundingMode.HALF_EVEN),
                new BigDecimal(3), priceOfOneOrange, new BigDecimal(0.9).setScale(1, RoundingMode.HALF_EVEN),
                BigDecimal.ONE, priceOfOneBanana, new BigDecimal(0.15).setScale(2, RoundingMode.HALF_EVEN),
                new BigDecimal(5), priceOfOnePapaya, new BigDecimal(2.0).setScale(1, RoundingMode.HALF_EVEN),
                new BigDecimal(3.55).setScale(2, RoundingMode.HALF_EVEN));

        private static final String LIST_OF_ITEMS_IN_SHOP = "---%s---" +
                "%n     Date: %s" +
                "%n     Price of each item" +
                "%n----------------------------" +
                "%n Apple  x 1 : %s¢" +
                "%n Orange x 1 : %s¢" +
                "%n Banana x 1 : %s¢" +
                "%n Papaya x 1 : %s¢" +
                "%n Promotion on papaya: Buy 3 in price of 2 : %s€";

        private static final String responseFetchAllItems = String.format(
                LIST_OF_ITEMS_IN_SHOP,
                shop,
                LocalDate.now(),
                priceOfOneApple,
                priceOfOneOrange,
                priceOfOneBanana,
                priceOfOnePapaya,
                priceOfThreePapayasInCostOfTwo
        );

        private static final String requestBodyInJson = String.format("{\n" +
                "\"numberOfApples\": %s,\n" +
                "\"numberOfOranges\": %s,\n" +
                "\"numberOfBananas\": %s,\n" +
                "\"numberOfPapayas\": %s\n" +
                "}", 2, 3, 1, 5);
    }
}