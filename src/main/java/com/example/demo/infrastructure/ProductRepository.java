package com.example.demo.infrastructure;

import com.example.demo.domain.Product;

public interface ProductRepository {
    void save(Product product);//normalnie: ProductEntity między pakietami

    Product find(String id);
}
