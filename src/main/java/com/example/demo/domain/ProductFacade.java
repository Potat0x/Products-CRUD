package com.example.demo.domain;

import org.springframework.stereotype.Component;

@Component
public interface ProductFacade {
    ProductResponseDto create(ProductRequestDto requestDto);
    ProductResponseDto find(String id);
    ProductResponseDto update(String id, ProductRequestDto requestDto);
    void delete(String id);
}