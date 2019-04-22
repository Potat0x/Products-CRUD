package com.example.demo.domain;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ProductFacade {
    ProductResponseDto create(ProductRequestDto requestDto);

    ProductResponseDto find(String id);

    ProductResponseDto update(String id, ProductRequestDto requestDto);

    ProductListResponseDto getProducts(List<String> tags);

    void delete(String id);
}