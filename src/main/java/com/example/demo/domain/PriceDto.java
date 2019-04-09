package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PriceDto {
    private final String amount;
    private final String currency;

    @JsonCreator
    public PriceDto(@JsonProperty("amount") String amount, @JsonProperty("currency") String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public String getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PriceDto)) return false;

        PriceDto priceDto = (PriceDto) o;

        if (amount != null ? !amount.equals(priceDto.amount) : priceDto.amount != null) return false;
        return currency != null ? currency.equals(priceDto.currency) : priceDto.currency == null;
    }

    @Override
    public int hashCode() {
        int result = amount != null ? amount.hashCode() : 0;
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        return result;
    }
}
