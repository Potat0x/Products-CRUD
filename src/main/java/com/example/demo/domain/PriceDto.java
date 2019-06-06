package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;
import java.util.Objects;

import com.example.demo.domain.exceptions.InvalidDtoException;

public class PriceDto {
  private final String amount;
  private final String currency;

  @JsonCreator
  public PriceDto(
      @JsonProperty("amount") String amount, @JsonProperty("currency") String currency) {
    this.amount = amount;
    this.currency = currency;
  }

  public String getAmount() {
    return amount;
  }

  public String getCurrency() {
    return currency;
  }

  static void assertValid(PriceDto price) {
    if (price == null) {
      throw new InvalidDtoException("product price cannot be empty");
    }

    if (Strings.isNullOrEmpty(price.getCurrency())) {
      throw new InvalidDtoException("price.currency cannot be empty");
    } else {
      try {
        CurrencyCode.valueOf(price.getCurrency());
      } catch (IllegalArgumentException e) {
        throw new InvalidDtoException("invalid price.currency");
      }
    }

    if (Strings.isNullOrEmpty(price.getAmount())) {
      throw new InvalidDtoException("price.amount cannot be empty");
    }

    try {
      if (Double.parseDouble(price.getAmount()) < 0) {
        throw new InvalidDtoException("price.amount cannot be negative");
      }
    } catch (NumberFormatException e) {
      throw new InvalidDtoException("invalid price.amount");
    }
  }

  @Override
  public String toString() {
    return "PriceDto{" + "amount='" + amount + '\'' + ", currency='" + currency + '\'' + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PriceDto priceDto = (PriceDto) o;
    return Objects.equals(amount, priceDto.amount) && Objects.equals(currency, priceDto.currency);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amount, currency);
  }
}
