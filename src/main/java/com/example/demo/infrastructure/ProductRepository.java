package com.example.demo.infrastructure;

import java.util.List;

import com.example.demo.domain.Product;

public interface ProductRepository {
  void save(Product product);

  Product find(String id);

  Product update(String id, Product product);

  void delete(String id);

  List<Product> getAll();

  List<Product> getByTags(List<String> tags);
}
