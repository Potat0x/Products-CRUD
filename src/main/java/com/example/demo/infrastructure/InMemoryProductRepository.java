package com.example.demo.infrastructure;

import com.example.demo.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class InMemoryProductRepository implements ProductRepository {
    private final Map<String, Product> db = new HashMap<>();

    @Override
    public void save(Product product) {
        db.put(product.getId(), product);
    }

    @Override
    public Product find(String id) {
        return db.get(id);
    }
}
