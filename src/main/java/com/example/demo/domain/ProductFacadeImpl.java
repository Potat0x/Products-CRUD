package com.example.demo.domain;

import com.example.demo.infrastructure.ProductRepository;
import com.example.demo.infrastructure.exceptions.ProductNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
class ProductFacadeImpl implements ProductFacade {

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
        LocalDateTime now = LocalDateTime.now();
        Product product = new Product(id, productRequest.getName(), now);

        repository.save(product);
        ProductResponseDto productResponseDto = new ProductResponseDto(product.getId(), product.getName());
        return productResponseDto;
    }

    @Override
    public ProductResponseDto find(String id) {
        checkIfProductExists(id);
        Product product = repository.find(id);
        return new ProductResponseDto(product.getId(), product.getName());
    }

    @Override
    public ProductResponseDto update(String id, String name) {
        checkIfProductExists(id);
        Product product = repository.update(id, name);
        return new ProductResponseDto(product.getId(), product.getName());
    }

    @Override
    public void delete(String id) {
        checkIfProductExists(id);
        repository.delete(id);
    }

    private void checkIfProductExists(String id) {
        if (repository.find(id) == null) {
            throw new ProductNotFoundException();
        }
    }
}
