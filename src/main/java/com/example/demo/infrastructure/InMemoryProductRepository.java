package com.example.demo.infrastructure;

import com.example.demo.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
class InMemoryProductRepository implements ProductRepository {
    private final Map<String, Product> db = new HashMap<>();

    @Override
    public void save(Product product) {
        db.put(product.getId(), product);
    }

    @Override
    public Product find(String id) {
        return db.get(id);
    }

    @Override
    public Product update(String id, Product product) {
        Product updatedProdduct = db.get(id).withNewName(product.getName());
        db.put(product.getId(), product);
        return updatedProdduct;
    }

    @Override
    public void delete(String id) {
        db.remove(id);
    }
}
