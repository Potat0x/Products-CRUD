package com.example.demo.domain;

import java.math.BigDecimal;
import java.util.Objects;

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
    return "Price{" + "amount=" + amount + ", currencyCode=" + currencyCode + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Price price = (Price) o;
    return Objects.equals(amount, price.amount) && currencyCode == price.currencyCode;
  }

  @Override
  public int hashCode() {
    return Objects.hash(amount, currencyCode);
  }
}
