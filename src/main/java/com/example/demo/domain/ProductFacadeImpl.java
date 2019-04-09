package com.example.demo.domain;

import com.example.demo.infrastructure.ProductRepository;
import com.example.demo.infrastructure.exceptions.EmptyProductNameException;
import com.example.demo.infrastructure.exceptions.ProductNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
class ProductFacadeImpl implements ProductFacade {

    private final ProductRepository repository;

    public ProductFacadeImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProductResponseDto create(ProductRequestDto requestDto) {
        assertRequestValid(requestDto);

        String id = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        Product product = new Product(id, requestDto.getName(), now);

        repository.save(product);
        ProductResponseDto productResponseDto = new ProductResponseDto(product.getId(), product.getName());
        return productResponseDto;
    }

    @Override
    public ProductResponseDto find(String id) {
        assertProductExists(id);
        Product product = repository.find(id);
        return new ProductResponseDto(product.getId(), product.getName());
    }

    @Override
    public ProductResponseDto update(String id, ProductRequestDto requestDto) {
        assertProductExists(id);
        assertRequestValid(requestDto);

        Product product = repository.find(id);
        Product updatedProduct = repository.update(id, product.withNewName(requestDto.getName()));
        return new ProductResponseDto(updatedProduct.getId(), updatedProduct.getName());
    }

    @Override
    public ProductListResponseDto getProducts() {
        List<ProductResponseDto> respnseDtos = new ArrayList<>();
        for (Product p : repository.getAll()) {
            respnseDtos.add(new ProductResponseDto(p.getId(), p.getName()));
        }
        return new ProductListResponseDto(respnseDtos);
    }

    @Override
    public void delete(String id) {
        assertProductExists(id);
        repository.delete(id);
    }

    private void assertRequestValid(ProductRequestDto productRequestDto) {
        if (!productRequestDto.isValid()) {
            throw new EmptyProductNameException();
        }
    }

    private void assertProductExists(String id) {
        if (repository.find(id) == null) {
            throw new ProductNotFoundException();
        }
    }
}
