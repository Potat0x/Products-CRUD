package com.example.demo.domain;

import java.util.List;

public interface ProductFacade {
    ProductResponseDto create(ProductRequestDto requestDto);

    ProductResponseDto find(String id);

    ProductResponseDto update(String id, ProductRequestDto requestDto);

    ProductListResponseDto getProductsByTags(List<String> tags);

    void delete(String id);
}