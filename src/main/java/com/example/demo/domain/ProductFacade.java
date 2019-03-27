package com.example.demo.domain;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PatchMapping;

@Component
public interface ProductFacade {
    //crud
    ProductResponseDto create(ProductRequestDto productRequest);
    ProductResponseDto find(String id);
    ProductResponseDto update(String id, String name);
}