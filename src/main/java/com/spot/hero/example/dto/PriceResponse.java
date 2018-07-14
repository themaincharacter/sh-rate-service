package com.spot.hero.example.dto;

import java.math.BigDecimal;

public class PriceResponse {
    private BigDecimal price;

    public PriceResponse(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
