package com.example.demo.domain;


import java.math.BigDecimal;

final class Price {
    private final BigDecimal amount;
    private final CurrencyCode currencyCode;

    public Price(String amount, CurrencyCode currencyCode) {
        this.amount = new BigDecimal(amount);
        this.currencyCode = currencyCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public CurrencyCode getCurrencyCode() {
        return currencyCode;
    }

    @Override
    public String toString() {
        return "Price{" +
                "amount=" + amount +
                ", currencyCode=" + currencyCode +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Price)) return false;

        Price price = (Price) o;

        if (amount != null ? !amount.equals(price.amount) : price.amount != null) return false;
        return currencyCode == price.currencyCode;
    }

    @Override
    public int hashCode() {
        int result = amount != null ? amount.hashCode() : 0;
        result = 31 * result + (currencyCode != null ? currencyCode.hashCode() : 0);
        return result;
    }
}
