package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductRequestDto {
    private final String name;
    private final PriceDto price;

    @JsonCreator
    public ProductRequestDto(@JsonProperty("name") String name, @JsonProperty("price") PriceDto price) {
        this.name = name;
        this.price = price;
    }

    @JsonGetter("name")
    String getName() {
        return name;
    }

    @JsonGetter("price")
    public PriceDto getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "ProductRequestDto{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }

    boolean isValid() {
        return name != null && !name.isBlank();
    }
}
