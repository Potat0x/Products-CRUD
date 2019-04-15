package com.example.demo.infrastructure;

import com.example.demo.domain.Product;

import java.util.List;

public interface ProductRepository {
    void save(Product product);//normalnie: ProductEntity między pakietami
    Product find(String id);
    Product update(String id, Product product);
    void delete(String id);
    List<Product> getAll();
    List<Product> getByTags(List<String> tags);
}
