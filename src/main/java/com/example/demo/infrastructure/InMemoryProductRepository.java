package com.example.demo.infrastructure;

import com.example.demo.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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
    public Product update(String id, Product updatedProduct) {
        Product productToUpdate = db.get(id);
        db.put(productToUpdate.getId(), updatedProduct);
        return db.get(productToUpdate.getId());
    }

    @Override
    public void delete(String id) {
        db.remove(id);
    }

    @Override
    public List<Product> getAll() {
        List<Product> allProducts = new ArrayList<>();
        db.keySet().forEach(id -> allProducts.add(db.get(id)));
        return allProducts;
    }
}
