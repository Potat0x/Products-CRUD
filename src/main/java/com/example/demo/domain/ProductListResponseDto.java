package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ProductListResponseDto {
    private final List<ProductResponseDto> products;

    @JsonCreator
    ProductListResponseDto(@JsonProperty("products") List<ProductResponseDto> products) {
        this.products = nullToEmpty(products);
    }

    public List<ProductResponseDto> getProducts() {
        return nullToEmpty(products);
    }

    private List<ProductResponseDto> nullToEmpty(List<ProductResponseDto> list) {
        return list != null ? list : Collections.emptyList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductListResponseDto that = (ProductListResponseDto) o;
        return Objects.equals(products, that.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(products);
    }
}
