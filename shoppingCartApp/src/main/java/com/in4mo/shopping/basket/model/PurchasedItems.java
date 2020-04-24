package com.in4mo.shopping.basket.model;

import java.math.BigDecimal;

public class PurchasedItems {

    private BigDecimal numberOfApples;
    private BigDecimal numberOfOranges;
    private BigDecimal numberOfBananas;
    private BigDecimal numberOfPapayas;

    public BigDecimal getNumberOfApples() {
        return numberOfApples;
    }

    public void setNumberOfApples(BigDecimal numberOfApples) {
        this.numberOfApples = numberOfApples;
    }

    public BigDecimal getNumberOfOranges() {
        return numberOfOranges;
    }

    public void setNumberOfOranges(BigDecimal numberOfOranges) {
        this.numberOfOranges = numberOfOranges;
    }

    public BigDecimal getNumberOfBananas() {
        return numberOfBananas;
    }

    public void setNumberOfBananas(BigDecimal numberOfBananas) {
        this.numberOfBananas = numberOfBananas;
    }

    public BigDecimal getNumberOfPapayas() {
        return numberOfPapayas;
    }

    public void setNumberOfPapayas(BigDecimal numberOfPapayas) {
        this.numberOfPapayas = numberOfPapayas;
    }
}
