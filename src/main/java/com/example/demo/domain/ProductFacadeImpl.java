package com.example.demo.domain;

import com.example.demo.infrastructure.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class ProductFacadeImpl implements ProductFacade {

    private final ProductRepository repository;

    public ProductFacadeImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProductResponseDto create(ProductRequestDto productRequest) {
        if (!productRequest.isValid()) {
            throw new RuntimeException("product name cannot be empty");
        }

        String id = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();//ctrl alt v
        Product product = new Product(id, productRequest.getName(), now);

        repository.save(product);
        ProductResponseDto productResponseDto = new ProductResponseDto(product.getId(), product.getName());
        return productResponseDto;
    }
}
